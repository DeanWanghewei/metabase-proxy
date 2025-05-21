package org.wei.metabaseproxy.metabase;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 14:15
 */
public class ResponseCurrentUser {
    private String id;
    private String email;
    private String first_name;
    private String last_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
