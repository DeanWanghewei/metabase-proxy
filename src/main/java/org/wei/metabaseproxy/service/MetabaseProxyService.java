package org.wei.metabaseproxy.service;

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
public class MetabaseProxyService {
    private boolean isLoggedIn = false;
    private MetabaseClient metabaseClient = null;
    private ResponseCurrentUser currentUserName = null;


    public QueryResultModel query(String querySql, int databaseId) {
        RequestQuery requestQuery = new RequestQuery(databaseId);
        requestQuery.setQueryNative(new RequestQueryNative(querySql));
        ResponseQuery responseQuery = getMetabaseClient().executeQuery(requestQuery);
        return new QueryResultModel(responseQuery, querySql);
    }

    public LoginUserModel login(String username, String password, String serverUrl) {
        LoginUserModel loginUserModel = callLoginApi(username, password, serverUrl);
        if (loginUserModel.isSuccess()) {
            this.isLoggedIn = true;
            SettingsState.getInstance().setUsername(username);
            SettingsState.getInstance().setPassword(password);
            SettingsState.getInstance().setServerUrl(serverUrl);
            return loginUserModel;
        }
        return loginUserModel;
    }

    public void refreshCurrentUserName() {
        if (isLoggedIn) {
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

    public void autoLoginIfPossible() {
        String username = SettingsState.getInstance().getUsername();
        String password = SettingsState.getInstance().getPassword();
        String serverUrl = SettingsState.getInstance().getServerUrl();
        if (!username.isEmpty() && !password.isEmpty() && !serverUrl.isEmpty()) {
            login(username, password, serverUrl);
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
        }
        return metabaseClient;
    }

    public void logout() {
        this.isLoggedIn = false;
        SettingsState.getInstance().setPassword("");
    }


    public boolean isLoggedIn() {
        return isLoggedIn;
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
