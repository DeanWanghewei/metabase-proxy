package org.wei.metabaseproxy.client.gaia;

import okhttp3.OkHttpClient;
import org.wei.metabaseproxy.client.metabase.RequestQuery;
import org.wei.metabaseproxy.client.metabase.ResponseQuery;
import org.wei.metabaseproxy.model.GaiaClientInfoModel;

import java.util.concurrent.TimeUnit;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年08月05日 15:06
 */
public class GaiaClient {
    private final OkHttpClient client;
    private GaiaClientInfoModel gaiaClientInfoModel;

    public GaiaClient(GaiaClientInfoModel gaiaClientInfoModel) {
        this.gaiaClientInfoModel = gaiaClientInfoModel;
        // 配置 OkHttpClient 并设置超时时间
        this.client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES) // 连接超时时间
                .readTimeout(3, TimeUnit.MINUTES)    // 读取超时时间
                .writeTimeout(3, TimeUnit.MINUTES)   // 写入超时时间
                .build();
    }

    public ResponseQuery executeQuery(RequestQuery requestQuery) {
        return null;
    }
}
