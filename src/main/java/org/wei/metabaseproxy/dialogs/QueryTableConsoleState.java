package org.wei.metabaseproxy.dialogs;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;

import javax.swing.*;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年06月11日 17:43
 */
public class QueryTableConsoleState {
    private final ToolWindow toolWindow;
    private final Content content;
    private final String dbName;
    private final String sql;
    private final JPanel mainPanel;
    private JPanel resultPanel;

    public QueryTableConsoleState(ToolWindow toolWindow, Content content, String dbName, String sql, JPanel mainPanel,
            JPanel resultPanel) {
        this.toolWindow = toolWindow;
        this.content = content;
        this.dbName = dbName;
        this.sql = sql;
        this.mainPanel = mainPanel;
        this.resultPanel = resultPanel;
    }

    public ToolWindow getToolWindow() {
        return toolWindow;
    }

    public Content getContent() {
        return content;
    }

    public String getDbName() {
        return dbName;
    }

    public String getSql() {
        return sql;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getResultPanel() {
        return resultPanel;
    }

    public void setResultPanel(JPanel resultPanel) {
        this.resultPanel = resultPanel;
    }
}
