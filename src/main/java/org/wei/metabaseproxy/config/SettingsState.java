package org.wei.metabaseproxy.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import static org.wei.metabaseproxy.constants.ResultShowConstant.RESULT_VIEW_TYPE_TABLE;
import static org.wei.metabaseproxy.constants.ResultShowConstant.RESULT_VIEW_TYPE_TEXT;

/**
 * @author deanwanghewei@gmail.com
 *         description
 *         使用 PersistentStateComponent 和 @Service 来保存用户设置
 *         来保存用户输入的用户名和密码
 * @date 2025年05月19日 11:14
 */
@Service(Service.Level.PROJECT)
@State(name = "orgWeiMetabaseProxy", storages = @Storage("orgWeiMetabaseProxySetting.xml"))
public final class SettingsState implements PersistentStateComponent<SettingsState> {

    private String username = "";
    private String password = "";
    private String serverUrl = "";
    private boolean isLoggedIn = false;
    private String resultViewType = RESULT_VIEW_TYPE_TEXT; // 可选值: "component", "console"

    // 注入方式获取实例（推荐）
    public static SettingsState getInstance(@NotNull com.intellij.openapi.project.Project project) {
        return project.getService(SettingsState.class);
    }

    @Override
    public @NotNull SettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }

    public String getResultViewType() {
        return resultViewType;
    }

    public void setResultViewType(String resultViewType) {
        this.resultViewType = resultViewType;
    }
}
