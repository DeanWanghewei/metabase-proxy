package org.wei.metabaseproxy.client.metabase;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 14:07
 */
public class RequestBodyLogin implements java.io.Serializable {
    private String username;
    private String password;
    private boolean remember = true;

    public RequestBodyLogin(String username, String password, boolean remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
    }

    public RequestBodyLogin(String username, String password) {
        this.username = username;
        this.password = password;
        this.remember = true;
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

    public boolean isRemember() {
        return remember;
    }
}
