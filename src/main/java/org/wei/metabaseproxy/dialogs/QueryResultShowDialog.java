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

import static org.wei.metabaseproxy.constants.ResultShowConstant.TOOL_WINDOW_ID;

import javax.swing.*;
import java.awt.BorderLayout;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月20日 14:35
 */
public class QueryResultShowDialog {
    private static final Logger LOG = Logger.getInstance(QueryResultShowDialog.class);

    private JTabbedPane tabbedPane;
    private JPanel dbPanel;
    private JPanel sqlPanel;
    private JPanel resultPanel;
    private JPanel mainPanel;
    private JTextArea sqlArea;
    private JLabel dbLabel;
    private JTextArea resultArea;

    public QueryConsoleState showInitConsole(Project project, DatabaseModel databaseModel, String sql) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow == null) {
            toolWindow = ToolWindowManager.getInstance(project).registerToolWindow(TOOL_WINDOW_ID, true,
                    ToolWindowAnchor.BOTTOM);
        }

        // 1. Query Database 区域
        dbLabel = new JLabel( databaseModel.getName());
        dbLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        dbPanel = new JPanel(new BorderLayout());
        dbPanel.add(dbLabel, BorderLayout.NORTH);

        // 2. Query SQL 区域
        sqlArea = new JTextArea(sql);
        sqlArea.setEditable(false);
        sqlArea.setLineWrap(true);
        sqlArea.setWrapStyleWord(true);
        sqlArea.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        JScrollPane sqlScrollPane = new JScrollPane(sqlArea);
        sqlScrollPane.setPreferredSize(new java.awt.Dimension(400, 80));
        sqlPanel = new JPanel(new BorderLayout());
        sqlPanel.add(sqlScrollPane, BorderLayout.CENTER);

        // 3. Query Result 区域
        resultPanel = new JPanel(new BorderLayout());
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        // JTabbedPane 左侧tab
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.addTab("Query Database", dbPanel);
        tabbedPane.addTab("Query SQL", sqlPanel);
        tabbedPane.addTab("Query Result", resultPanel);
        tabbedPane.setSelectedIndex(1); // 默认选中 Query SQL

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        Content content = toolWindow.getContentManager().getFactory().createContent(scrollPane,
                TOOL_WINDOW_ID, true);
        toolWindow.getContentManager().addContent(content, 0);
        toolWindow.show(null);
        toolWindow.getContentManager().setSelectedContent(content);
        return new QueryConsoleState(toolWindow, null); // 这里的ConsoleView已不再使用
    }

    public void showResult(Project project, QueryConsoleState consoleState, QueryResultModel queryResult) {
        // 自动切换到 Query Result tab
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(2);
        }
        resultArea.setText("");
        if (queryResult.isSuccess()) {
            resultArea.append(buildTable(queryResult.getHead(), queryResult.getData()));
        } else {
            showError(project, consoleState, queryResult.getErrorMsg());
        }
    }

    public void showResult(Project project, QueryConsoleState consoleState, List<String> head,
            List<List<String>> data) {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(2);
        }
        resultArea.setText("");
        resultArea.append(buildTable(head, data));
    }

    public void showError(Project project, QueryConsoleState consoleState, String errorMsg) {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(2);
        }
        resultArea.setText("");
        resultArea.setForeground(java.awt.Color.RED);
        resultArea.append(errorMsg);
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
     * | 单元格内容 | 单元格内容 |
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
        if (input == null)
            return "NULL";
        return input.replace("\n", " ").replace("\r", " ").replace("\t", " ");
    }

}
