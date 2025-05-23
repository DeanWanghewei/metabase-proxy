package org.wei.metabaseproxy.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.wei.metabaseproxy.config.SettingsState;
import org.wei.metabaseproxy.metabase.*;
import org.wei.metabaseproxy.model.DatabaseModel;
import org.wei.metabaseproxy.model.LoginUserModel;
import org.wei.metabaseproxy.model.QueryResultModel;

import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 11:03
 */
@Service(Service.Level.PROJECT)
public final class MetabaseProxyService {
    private final Project project;
    private MetabaseClient metabaseClient = null;
    private ResponseCurrentUser currentUserName = null;

    public MetabaseProxyService(Project project) {
        this.project = project;
    }

    public QueryResultModel query(String querySql, int databaseId) {
        RequestQuery requestQuery = new RequestQuery(databaseId);
        requestQuery.setQueryNative(new RequestQueryNative(querySql));
        ResponseQuery responseQuery = getMetabaseClient().executeQuery(requestQuery);
        return new QueryResultModel(responseQuery, querySql);
    }

    public LoginUserModel login(String username, String password, String serverUrl) {
        LoginUserModel loginUserModel = callLoginApi(username, password, serverUrl);
        if (loginUserModel.isSuccess()) {
            SettingsState.getInstance(project).setLoggedIn(true);
            return loginUserModel;
        }
        return loginUserModel;
    }

    public void refreshCurrentUserName() {
        if (isLoggedIn()) {
            currentUserName = getMetabaseClient().getCurrentUser();
        }
    }

    public String getCurrentUserName() {
        if (currentUserName == null) {
            return "Not Login";
        } else {
            return currentUserName.getFirst_name();
        }
    }

    private LoginUserModel callLoginApi(String username, String password, String serverUrl) {
        // 这里实现实际的网络请求逻辑，例如使用 HttpClient 或 OkHttp 请求 Metabase 登录接口
        MetabaseClient client = getMetabaseClient();
        return client.login(username, password, serverUrl);
    }

    private MetabaseClient getMetabaseClient() {
        if (metabaseClient == null) {
            metabaseClient = new MetabaseClient();
            // 如果登录状态为true但客户端为空，尝试使用保存的凭据重新登录
            SettingsState settings = SettingsState.getInstance(project);
            if (settings.isLoggedIn() && !settings.getUsername().isEmpty() && !settings.getPassword().isEmpty()
                    && !settings.getServerUrl().isEmpty()) {
                try {
                    LoginUserModel loginResult = metabaseClient.login(settings.getUsername(), settings.getPassword(),
                            settings.getServerUrl());
                    if (!loginResult.isSuccess()) {
                        // 重新登录失败，清除登录状态
                        settings.setLoggedIn(false);
                    }
                } catch (Exception e) {
                    // 重新登录失败，清除登录状态
                    settings.setLoggedIn(false);
                }
            }
        }
        return metabaseClient;
    }

    public boolean isLoggedIn() {
        return SettingsState.getInstance(project).isLoggedIn();
    }

    public void logout() {
        SettingsState.getInstance(project).setLoggedIn(false);
        this.metabaseClient = null;
        this.currentUserName = null;
    }

    /**
     * 查询数据库
     *
     * @return
     */
    public List<DatabaseModel> listUserDatabases() {
        MetabaseClient client = getMetabaseClient();
        ResponseListDatabase responseListDatabase = client.listDatabase();
        return responseListDatabase.getData().stream().map(DatabaseModel::new).toList();
    }
}
