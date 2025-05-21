package org.wei.metabaseproxy.metabase;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 16:51
 */
public class RequestQueryNative {
    private String query;
    @JsonProperty("template-tags")
    private Object templateTags = new Object();

    public RequestQueryNative(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Object getTemplateTags() {
        return templateTags;
    }

    public void setTemplateTags(Object templateTags) {
        this.templateTags = templateTags;
    }
}
