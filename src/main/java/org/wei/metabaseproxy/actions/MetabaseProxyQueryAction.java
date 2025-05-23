package org.wei.metabaseproxy.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.wei.metabaseproxy.config.CurrentUserVarState;
import org.wei.metabaseproxy.dialogs.QueryConsoleState;
import org.wei.metabaseproxy.dialogs.QueryResultShowDialog;
import org.wei.metabaseproxy.dialogs.RunSqlParamsDialog;
import org.wei.metabaseproxy.model.DatabaseModel;
import org.wei.metabaseproxy.model.QueryResultModel;
import org.wei.metabaseproxy.model.RunSqlParamsModel;
import org.wei.metabaseproxy.service.MetabaseProxyService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 17:18
 */
public class MetabaseProxyQueryAction extends AnAction {

    protected static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{([^}])*}");

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        // 显示查询结果或者错误结果
        doAction(anActionEvent, false);
    }

    /**
     * 执行动作
     *
     * @param anActionEvent     事件
     * @param defaultShowDialog 默认显示对话框
     * @return
     */
    protected void doAction(@NotNull AnActionEvent anActionEvent, boolean defaultShowDialog) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            Messages.showErrorDialog("无法获取项目信息", "错误");
            return;
        }

        MetabaseProxyService proxyService = project.getService(MetabaseProxyService.class);

        // 获取用户选中的内容
        String selectedText = getQuerySql(anActionEvent);
        if (selectedText == null || selectedText.isEmpty()) {
            Messages.showErrorDialog("请选择要查询的内容", "错误");
            return;
        }

        if (!proxyService.isLoggedIn()) {
            Messages.showErrorDialog("请先在设置中登录MetaBaseProxy", "错误");
            return;
        }

        DatabaseModel cacheDatabase = CurrentUserVarState.getInstance().getCacheDatabaseSelected();

        Matcher matcher = VAR_PATTERN.matcher(selectedText);
        HashMap<String, String> varCache = new HashMap<>();
        HashSet<String> notCacheVarList = new HashSet<>();
        while (matcher.find()) {
            String item = matcher.group();
            String varName = item.substring(2, item.length() - 1);
            if (CurrentUserVarState.getInstance().getUserVarMap().containsKey(varName)) {
                varCache.put(varName, CurrentUserVarState.getInstance().getUserVarMap().get(varName));
            } else {
                notCacheVarList.add(varName);
            }
        }
        if (!notCacheVarList.isEmpty() || cacheDatabase == null || defaultShowDialog) {
            // 弹窗,让用户输入变量值
            // 查询数据库
            List<DatabaseModel> databaseModelList = proxyService.listUserDatabases();
            RunSqlParamsModel runSqlParamsModel = new RunSqlParamsModel();
            runSqlParamsModel.setSelectedDatabase(cacheDatabase);
            runSqlParamsModel.setUserInputVarList(notCacheVarList);
            runSqlParamsModel.setUserVarMap(varCache);

            RunSqlParamsDialog dialog = new RunSqlParamsDialog(anActionEvent.getProject(), runSqlParamsModel,
                    databaseModelList);
            dialog.show();

            if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
                return;
            }
            RunSqlParamsModel userResetParams = dialog.getUserResetParams();
            cacheDatabase = userResetParams.getSelectedDatabase();
            varCache = userResetParams.getUserVarMap();
            varCache.forEach((k, v) -> CurrentUserVarState.getInstance().getUserVarMap().put(k, v));
            CurrentUserVarState.getInstance().setCacheDatabaseSelected(cacheDatabase);
        }

        for (String s : varCache.keySet()) {
            selectedText = selectedText.replace("${" + s + "}", varCache.get(s));
        }

        QueryResultShowDialog queryResultShowDialog = new QueryResultShowDialog();
        String finalSelectedText = formatSql(selectedText);
        DatabaseModel finalCacheDatabase = cacheDatabase;
        final MetabaseProxyService finalProxyService = proxyService;
        // 先显示控制台
        QueryConsoleState consoleState = queryResultShowDialog.showInitConsole(anActionEvent.getProject(),
                finalCacheDatabase, finalSelectedText);

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Metabase Proxy Query") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    // 执行耗时网络请求
                    QueryResultModel resultModel = finalProxyService.query(finalSelectedText,
                            finalCacheDatabase.getId());

                    // 回到 UI 线程更新内容
                    ApplicationManager.getApplication().invokeLater(() -> queryResultShowDialog
                            .showResult(anActionEvent.getProject(), consoleState, resultModel));
                } catch (Exception e) {
                    ApplicationManager.getApplication().invokeLater(() -> queryResultShowDialog
                            .showError(anActionEvent.getProject(), consoleState, "查询失败错误: " + e.getMessage()));
                }
            }
        });
    }

    protected String getQuerySql(AnActionEvent event) {
        String userSelect = getUserSelect(event);
        if (StringUtil.isEmpty(userSelect)) {
            userSelect = findCurrentSqlByCurrentFile(event);
        }
        return userSelect;
    }

    protected String getUserSelect(AnActionEvent event) {
        // 获取用户选中的内容
        Editor editor = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        return editor.getSelectionModel().getSelectedText();
    }

    protected String findCurrentSqlByCurrentFile(AnActionEvent event) {
        PsiFile currentFile = getCurrentFile(event);
        // 获取鼠标当前位置
        int offset = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.CARET).getOffset();
        // 判断当前行是否有 ;
        int delimiterLineIndex = currentLineFirstDelimiterOffset(offset, currentFile);
        return getRangeText(delimiterLineIndex, currentFile);
    }

    private String getRangeText(int delimiterLineIndex, PsiFile currentFile) {
        String text = currentFile.getText();
        // 以delimiterLineIndex 为中心, 向左向右查找 ;
        int left = delimiterLineIndex;
        int right = delimiterLineIndex;
        while (left >= 0 && right < text.length()) {
            if (text.charAt(left) == ';') {
                break;
            }
            left--;
        }
        while (right < text.length() && left >= 0) {
            if (text.charAt(right) == ';') {
                break;
            }
            right++;
        }
        return text.substring(left + 1, right);
    }

    private String formatSql(String sql) {
        if (StringUtil.isEmpty(sql)) {
            return sql;
        }
        return addDefaultLimit(sql);
    }

    private String addDefaultLimit(String sql) {
        String querySql = sql;
        int limitStartOffset = sql.lastIndexOf(")");
        if (limitStartOffset == -1) {
            limitStartOffset = 0;
        }
        String subString = sql.substring(limitStartOffset, sql.length() - 1);
        if (!subString.toUpperCase().contains(" LIMIT ")) {
            querySql = querySql + " LIMIT 100";
        }
        return querySql;

    }

    private int currentLineFirstDelimiterOffset(int offset, PsiFile currentFile) {
        String text = currentFile.getText();
        int lineNumber = currentFile.getViewProvider().getDocument().getLineNumber(offset);
        int lineStartOffset = currentFile.getViewProvider().getDocument().getLineStartOffset(lineNumber);
        int lineEndOffset = currentFile.getViewProvider().getDocument().getLineEndOffset(lineNumber);
        String lineText = text.substring(lineStartOffset, lineEndOffset);
        // 获取当前行第一个 ; 的位置
        if (lineText.indexOf(';') == -1) {
            return offset;
        } else {
            return lineStartOffset + lineText.indexOf(';') - 1;
        }
    }

    protected PsiFile getCurrentFile(AnActionEvent event) {
        return event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE);
    }

}
