package org.wei.metabaseproxy.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wei.metabaseproxy.model.LoginUserModel;
import org.wei.metabaseproxy.service.MetabaseProxyService;

import javax.swing.*;
import java.awt.*;

/**
 * @author deanwanghewei@gmail.com
 * description
 * 创建一个实现 com.intellij.openapi.options.Configurable 的类，用于提供设置界面。
 * @date 2025年05月19日 11:08
 */
public class MetabaseSettingsConfigurable implements SearchableConfigurable {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField serverUrlField;
    private JButton loginButton;
    private JLabel statusLabel;


    private final Project project;
    private final MetabaseProxyService userService;


    // IDEA 注入
    public MetabaseSettingsConfigurable(Project project) {
        this.project = project;
        this.userService = project.getService(MetabaseProxyService.class);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Metabase Proxy Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 5, 5); // 设置组件之间的间距


        // 标题部分
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Metabase proxy  Settings");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14));
        panel.add(titleLabel, gbc);

        // 用户名输入
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("User name:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(40); // 初始列数
        panel.add(usernameField, gbc);

        // 用户名说明
        gbc.gridy++;
        gbc.gridx = 1;
        JLabel userNote = new JLabel("<html><small>New user name will be applied for all new sessions after restart</small></html>");
        panel.add(userNote, gbc);

        // 密码输入
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(40);
        panel.add(passwordField, gbc);


        // 服务器URL
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("MetabaseServer URL:"), gbc);
        serverUrlField = new JTextField(40); // 初始列数
        gbc.gridx = 1;
        panel.add(serverUrlField, gbc);


        // 登录按钮
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // 横跨两列
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> performLogin());
        panel.add(loginButton, gbc);

        // 状态标签
        gbc.gridy++;
        statusLabel = new JLabel(getStatusText());
        panel.add(statusLabel, gbc);

        // 分隔线
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);

        // 说明文字
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel description = new JLabel("<html>本地代理metabase 的数据库查询能力.</html>");
        description.setPreferredSize(new Dimension(300, 60));
        panel.add(description, gbc);

        return panel;
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String serverUrl = this.serverUrlField.getText();
        if (!serverUrl.startsWith("http://") && !serverUrl.startsWith("https://")) {
            statusLabel.setText("❌ Server URL must start with http:// or https://");
            return;
        }
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("❌ Username or password cannot be empty.");
            return;
        }


        LoginUserModel loginUserModel = userService.login(username, password, serverUrl);
        if (loginUserModel.isSuccess()) {
            userService.refreshCurrentUserName();
            statusLabel.setText("✅ Logged in as: " + userService.getCurrentUserName());
        } else {
            statusLabel.setText("❌ Login failed." + loginUserModel.getErrorMsg());
        }
    }

    private String getStatusText() {
        if (userService.isLoggedIn()) {
            return "✅ Logged in as: " + userService.getCurrentUserName();
        } else {
            return "❌ Not logged in.";
        }
    }

    @Override
    public boolean isModified() {
        // 判断是否内容有变化，用于“Apply”按钮的启用状态
        return !usernameField.getText().equals(SettingsState.getInstance(project).getUsername())
                || !new String(passwordField.getPassword()).equals(SettingsState.getInstance(project).getPassword())
                || !serverUrlField.getText().equals(SettingsState.getInstance(project).getServerUrl())
                ;
    }

    @Override
    public void apply() throws ConfigurationException {
        SettingsState.getInstance(project).setUsername(usernameField.getText());
        SettingsState.getInstance(project).setPassword(new String(passwordField.getPassword()));
        SettingsState.getInstance(project).setServerUrl(serverUrlField.getText());
    }

    @Override
    public void reset() {
        usernameField.setText(SettingsState.getInstance(project).getUsername());
        passwordField.setText(SettingsState.getInstance(project).getPassword());
        serverUrlField.setText(SettingsState.getInstance(project).getServerUrl());
    }

    @Override
    public void disposeUIResources() {
    }

    @Override
    public @NotNull @NonNls String getId() {
        return "metabaseProxy.settings";
    }
}
