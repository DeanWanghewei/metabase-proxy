package org.wei.metabaseproxy.dialogs;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.wei.metabaseproxy.model.DatabaseModel;
import org.wei.metabaseproxy.model.QueryResultModel;

import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月20日 14:35
 */
public class QueryResultShowDialog {
    private static final Logger LOG = Logger.getInstance(QueryResultShowDialog.class);


    public QueryConsoleState showInitConsole(Project project, DatabaseModel databaseModel, String sql) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Metabase Proxy");
        if (toolWindow == null) {
            toolWindow = ToolWindowManager.getInstance(project).registerToolWindow("Metabase Proxy", true, ToolWindowAnchor.BOTTOM);
        }

        ConsoleView consoleView = new ConsoleViewImpl(project, false);
        Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Query Result", false);

        toolWindow.getContentManager().addContent(content, 0);
        toolWindow.show(null);
        toolWindow.getContentManager().setSelectedContent(content);
        // 激活当前 content

        consoleView.clear();
        consoleView.print("Query Database: \n" + databaseModel.getName() + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
        consoleView.print("Query SQL: \n" + sql + "\n", ConsoleViewContentType.NORMAL_OUTPUT);

        return new QueryConsoleState(toolWindow, consoleView);
    }


    public void showResult(Project project, QueryConsoleState consoleState, QueryResultModel queryResult) {
        consoleState.getConsoleView().print("Query Result: \n", ConsoleViewContentType.NORMAL_OUTPUT);
        if (queryResult.isSuccess()) {
            showResult(project, consoleState, queryResult.getHead(), queryResult.getData());
        } else {
            showError(project, consoleState, queryResult.getErrorMsg());
        }
    }


    public void showResult(Project project, QueryConsoleState consoleState, List<String> head, List<List<String>> data) {
        ConsoleView consoleView = consoleState.getConsoleView();
        // 构建并打印表格内容
        String tableOutput = buildTable(head, data);
        consoleView.print(tableOutput, ConsoleViewContentType.NORMAL_OUTPUT);

    }

    public void showError(Project project, QueryConsoleState consoleState, String errorMsg) {
        consoleState.getConsoleView().print(errorMsg, ConsoleViewContentType.ERROR_OUTPUT);
    }


    private String buildTable(List<String> head, List<List<String>> data) {
        StringBuilder sb = new StringBuilder();
        // 计算显示宽度时考虑中文字符
        int[] columnWidths = calculateColumnWidths(head, data);

        // 生成顶部边框
        appendSeparator(sb, columnWidths);

        // 打印表头
        appendRow(sb, head, columnWidths);

        // 生成表头分隔线
        appendSeparator(sb, columnWidths);

        // 打印数据行
        for (List<String> row : data) {
            appendRow(sb, row, columnWidths);
        }

        // 生成底部边框
        appendSeparator(sb, columnWidths);
        return sb.toString();
    }

    /**
     * 计算各列最大显示宽度（汉字按2个宽度计算）
     */
    private int[] calculateColumnWidths(List<String> head, List<List<String>> data) {
        int[] columnWidths = new int[head.size()];

        // 初始化列宽
        for (int i = 0; i < head.size(); i++) {
            columnWidths[i] = getDisplayWidth(head.get(i));
        }

        // 遍历数据行找最大值
        for (List<String> row : data) {
            for (int i = 0; i < row.size(); i++) {
                int currentWidth = getDisplayWidth(row.get(i));
                if (currentWidth > columnWidths[i]) {
                    columnWidths[i] = currentWidth;
                }
            }
        }
        return columnWidths;
    }

    /**
     * 字符显示宽度计算（ASCII字符1宽度，非ASCII字符2宽度）
     */
    private int getDisplayWidth(String s) {
        if (s == null) {
            return 0;
        }
        int width = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            width += (c <= '\u00FF') ? 1 : 2;
        }
        return width;
    }

    /**
     * 生成表格分隔线
     * +----------------+---------------+
     */
    private void appendSeparator(StringBuilder sb, int[] columnWidths) {
        sb.append("+");
        for (int width : columnWidths) {
            for (int j = 0; j < width + 2; j++) {
                sb.append("-");
            }
            sb.append("+");
        }
        sb.append("\n");
    }

    /**
     * 生成表格数据行
     * |  单元格内容  |  单元格内容  |
     */
    private void appendRow(StringBuilder sb, List<String> row, int[] columnWidths) {
        sb.append("|");
        for (int i = 0; i < row.size(); i++) {
            // 清理特殊字符并格式化
            String cell = safeReplace(row.get(i));
            int cellWidth = getDisplayWidth(cell);
            int padding = columnWidths[i] - cellWidth;

            sb.append(" ").append(cell);
            for (int j = 0; j < padding; j++) {
                sb.append(" ");
            }
            sb.append(" |");
        }
        sb.append("\n");
    }

    private static String safeReplace(String input) {
        if (input == null) return "NULL";
        return input.replace("\n", " ").replace("\r", " ").replace("\t", " ");
    }

}
