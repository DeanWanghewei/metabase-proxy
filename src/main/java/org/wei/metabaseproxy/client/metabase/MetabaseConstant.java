package org.wei.metabaseproxy.client.metabase;

import okhttp3.MediaType;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 14:03
 */
public class MetabaseConstant {
    public static final String METABASE_HEADER_COOKIE = "cookie";

    /**
     * 登陆接口
     */
    public static final String METABASE_URI_LOGIN = "/api/session";
    /**
     * 当前用户信息
     */
    public static final String METABASE_URI_CURRENT_USER = "/api/user/current";

    /**
     * 数据库列表
     */
    public static final String METABASE_URI_DATABASE = "/api/database";

    /**
     * 数据库查询接口
     */
    public static final String METABASE_URI_DATABASE_QUERY = "/api/dataset";


    /**
     * 请求的json格式
     */
    public static final MediaType REQUEST_HEADER_JSON = MediaType.get("application/json; charset=utf-8");
}
