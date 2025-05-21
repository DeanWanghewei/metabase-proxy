package org.wei.metabaseproxy.metabase;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.wei.metabaseproxy.model.LoginUserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 13:58
 */
public class MetabaseClient {
    private final OkHttpClient client;
    private LoginUserModel loginUser;
    private String serverUrl;

    public MetabaseClient() {
        // 配置 OkHttpClient 并设置超时时间
        this.client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES) // 连接超时时间
                .readTimeout(3, TimeUnit.MINUTES)    // 读取超时时间
                .writeTimeout(3, TimeUnit.MINUTES)   // 写入超时时间
                .build();
    }

    public LoginUserModel getLoginUser() {
        return loginUser;
    }

    public ResponseQuery executeQuery(RequestQuery query) {
        Request request = new Request.Builder().url(serverUrl + MetabaseConstant.METABASE_URI_DATABASE_QUERY)
                .post(RequestBody.create(new Gson().toJson(query), MetabaseConstant.REQUEST_HEADER_JSON))
                .header(MetabaseConstant.METABASE_HEADER_COOKIE, loginUser.getHeaderCookie())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String resBody = response.body().string();
                ResponseQuery responseQuery = new Gson().fromJson(resBody, ResponseQuery.class);
                return responseQuery;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public ResponseListDatabase listDatabase() {
        Request request = new Request.Builder().url(serverUrl + MetabaseConstant.METABASE_URI_DATABASE)
                .get()
                .header(MetabaseConstant.METABASE_HEADER_COOKIE, loginUser.getHeaderCookie())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return new Gson().fromJson(response.body().string(), ResponseListDatabase.class);
            } else {
                return new ResponseListDatabase();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseCurrentUser getCurrentUser() {
        Request request = new Request.Builder().url(serverUrl + MetabaseConstant.METABASE_URI_CURRENT_USER).get().header(MetabaseConstant.METABASE_HEADER_COOKIE, loginUser.getHeaderCookie()).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return new Gson().fromJson(response.body().string(), ResponseCurrentUser.class);
            } else {
                return new ResponseCurrentUser();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LoginUserModel login(String username, String password, String serverUrl) {
        this.serverUrl = serverUrl;
        RequestBodyLogin jsonBody = new RequestBodyLogin(username, password);
        // 使用 okhttp 发器post请求
        RequestBody body = RequestBody.create(new Gson().toJson(jsonBody), MetabaseConstant.REQUEST_HEADER_JSON);

        // 构建请求
        Request request = new Request.Builder().url(serverUrl + MetabaseConstant.METABASE_URI_LOGIN) // 替换为你的目标 URL
                .post(body).build();

        // 发起请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                ResponseLogin responseLogin = new Gson().fromJson(response.body().string(), ResponseLogin.class);
                List<String> headers = response.headers("Set-Cookie");

                ArrayList<String> requestHeaders = new ArrayList<>();
                headers.forEach(item -> {
                    for (String s : item.split(";")) {
                        String[] split = s.split("=");
                        if (split.length == 2 && (split[0].startsWith("metabase") || split[0].startsWith("SERVERID"))) {
                            requestHeaders.add(split[0] + "=" + split[1]);
                        }
                    }
                });

                String header = Joiner.on(";").join(requestHeaders);
                LoginUserModel loginUserModel = new LoginUserModel();
                loginUserModel.setHeaderCookie(header);
                loginUserModel.setId(responseLogin.getId());
                loginUserModel.setSuccess(true);
                this.loginUser = loginUserModel;
                return loginUserModel;
            } else {
                LoginUserModel loginUserModel = new LoginUserModel();
                loginUserModel.setErrorMsg("httpStatusCode: %s, msg: %s".formatted(response.code(), response.body().string()));
                return loginUserModel;
            }
        } catch (Exception e) {
            LoginUserModel loginUserModel = new LoginUserModel();
            loginUserModel.setErrorMsg("requestError: %s".formatted(e.getMessage()));
            return loginUserModel;
        }
    }
}
