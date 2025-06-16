package org.wei.metabaseproxy.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.wei.metabaseproxy.constants.ResultShowConstant;
import org.wei.metabaseproxy.model.DatabaseModel;
import org.wei.metabaseproxy.model.QueryResultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月20日 14:35
 */
public class QueryResultShowTableDialog {

    public static QueryTableConsoleState showInitConsole(Project project, DatabaseModel databaseModel, String sql) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ResultShowConstant.TOOL_WINDOW_ID);
        if (toolWindow == null) {
            toolWindow = ToolWindowManager.getInstance(project).registerToolWindow(ResultShowConstant.TOOL_WINDOW_ID,
                    true,
                    ToolWindowAnchor.BOTTOM);
        }
        // --- 新布局：左侧tab切换 ---
        // 1. Query Database 区域
        JLabel dbLabel = new JLabel(databaseModel.getName());
        dbLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        JPanel dbPanel = new JPanel(new BorderLayout());
        dbPanel.add(dbLabel, BorderLayout.NORTH);

        // 2. Query SQL 区域
        JTextArea sqlArea = new JTextArea(sql);
        sqlArea.setEditable(false);
        sqlArea.setLineWrap(true);
        sqlArea.setWrapStyleWord(true);
        sqlArea.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        JScrollPane sqlScrollPane = new JScrollPane(sqlArea);
        sqlScrollPane.setPreferredSize(new java.awt.Dimension(400, 80));
        JPanel sqlPanel = new JPanel(new BorderLayout());
        sqlPanel.add(sqlScrollPane, BorderLayout.CENTER);

        // 3. Query Result 区域
        JPanel resultPanel = new JPanel(new BorderLayout());
        // resultPanel 由 showTable/showError 填充

        // JTabbedPane 左侧tab
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.addTab("Query Database", dbPanel);
        tabbedPane.addTab("Query SQL", sqlPanel);
        tabbedPane.addTab("Query Result", resultPanel);
        tabbedPane.setSelectedIndex(1); // 默认选中 Query SQL

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        Content content = toolWindow.getContentManager().getFactory().createContent(scrollPane,
                ResultShowConstant.TOOL_WINDOW_ID, true);
        toolWindow.getContentManager().addContent(content, 0);
        toolWindow.show(null);
        toolWindow.getContentManager().setSelectedContent(content);
        // 传递tabbedPane
        return new QueryTableConsoleState(toolWindow, content, databaseModel.getName(), sql, mainPanel, resultPanel,
                tabbedPane);
    }

    public static void showResult(Project project, QueryTableConsoleState state, QueryResultModel queryResult) {
        // 自动切换到 Query Result tab
        if (state.getTabbedPane() != null) {
            state.getTabbedPane().setSelectedIndex(2);
        }
        if (queryResult.isSuccess()) {
            showTable(project, state, queryResult.getHead(), queryResult.getData());
        } else {
            showError(project, state, queryResult.getErrorMsg());
        }
    }

    public static void showTable(Project project, QueryTableConsoleState state, List<String> head,
            List<List<String>> data) {
        // 自动切换到 Query Result tab
        // 只更新结果区 resultPanel
        JPanel resultPanel = state.getResultPanel();
        resultPanel.removeAll();
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String col : head) {
            model.addColumn(col);
        }
        for (List<String> row : data) {
            model.addRow(row.toArray());
        }
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // 设置每一列的最小宽度为字段名的像素宽度+padding
        java.awt.FontMetrics fm = table.getFontMetrics(table.getTableHeader().getFont());
        int padding = 24;
        for (int col = 0; col < head.size(); col++) {
            String colName = head.get(col);
            int colWidth = fm.stringWidth(colName) + padding;
            table.getColumnModel().getColumn(col).setMinWidth(colWidth);
        }

        resultPanel.add(tableScrollPane, BorderLayout.CENTER);
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    public static void showError(Project project, QueryTableConsoleState state, String errorMsg) {
        // 只更新结果区 resultPanel
        JPanel resultPanel = state.getResultPanel();
        resultPanel.removeAll();
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(java.awt.Color.RED);
        textArea.append(errorMsg);
        JScrollPane errorScrollPane = new JScrollPane(textArea);
        resultPanel.add(errorScrollPane, BorderLayout.CENTER);
        resultPanel.revalidate();
        resultPanel.repaint();
    }
}
