package org.wei.metabaseproxy.model;

import java.io.Serializable;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 14:49
 */
public class LoginUserModel implements Serializable {
    private String id;

    private String headerCookie;

    private boolean success;

    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeaderCookie() {
        return headerCookie;
    }

    public void setHeaderCookie(String headerCookie) {
        this.headerCookie = headerCookie;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
