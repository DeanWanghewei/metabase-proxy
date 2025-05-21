package org.wei.metabaseproxy.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.serialization.Property;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author deanwanghewei@gmail.com
 *  description
 * 使用 PersistentStateComponent 来保存用户输入的用户名和密码
 * 使用 PersistentStateComponent 和 @Service 来保存用户设置
 * @date 2025年05月19日 11:14
 */
@Service(Service.Level.PROJECT)
@State(
        name = "orgWeiMetabaseProxy",
        storages = @Storage("orgWeiMetabaseProxySetting.xml")
)
public final class SettingsState implements PersistentStateComponent<SettingsState> {
    private static final SettingsState INSTANCE = new SettingsState();

    private String username = "";
    private String password = "";
    private String serverUrl = "";

    public static SettingsState getInstance() {
        return INSTANCE;
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
}
