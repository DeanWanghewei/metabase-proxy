package org.wei.metabaseproxy.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.wei.metabaseproxy.client.gaia.GaiaClient;
import org.wei.metabaseproxy.client.metabase.RequestQuery;
import org.wei.metabaseproxy.client.metabase.RequestQueryNative;
import org.wei.metabaseproxy.client.metabase.ResponseQuery;
import org.wei.metabaseproxy.model.GaiaClientInfoModel;
import org.wei.metabaseproxy.msg.AlertNotifier;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年08月05日 14:56
 */
@Service(Service.Level.PROJECT)
public final class GaiaClientService {
    private final Project project;
    private GaiaClientInfoModel clientInfoModel;

    public GaiaClientService(Project project) {
        this.project = project;
    }

    public GaiaClientInfoModel getClientInfoModel() {
        if (clientInfoModel == null) {
            initClient();
        }
        if (clientInfoModel == null) {
            // idea 弹窗提醒登陆
            AlertNotifier.notifyError("Gaia Login Error", "请先登录 Gaia 插件");
        }
        return clientInfoModel;
    }

    public void submitQuery(String querySql, int databaseId) {
        RequestQuery requestQuery = new RequestQuery(databaseId);
        requestQuery.setQueryNative(new RequestQueryNative(querySql));
        ResponseQuery responseQuery = getClient().executeQuery(requestQuery);
    }


    private GaiaClient getClient() {
        GaiaClientInfoModel info = getClientInfoModel();
        return new GaiaClient(info);
    }


    private void initClient() {
        try {
            // 通过类名字符串方式获取服务，避免编译时依赖问题和类加载器隔离问题
            Class<?> tokenProviderClass = Class.forName("org.wei.idea.plugin.gaia.code.merge.spi.TokenProvider");
            Object providerObj = project.getService(tokenProviderClass);
            if (providerObj != null) {
                // 使用反射调用方法避免直接类型转换
                Class<?> providerClass = providerObj.getClass();
                Object accessToken = providerClass.getMethod("getAccessToken").invoke(providerObj);
                Object gaiaApiUrl = providerClass.getMethod("getGaiaAPiUrl").invoke(providerObj);
                if (accessToken != null && gaiaApiUrl != null) {
                    clientInfoModel = new GaiaClientInfoModel(gaiaApiUrl.toString(), accessToken.toString());
                }
            }
        } catch (Exception e) {
            clientInfoModel = null;
        }
    }
}
