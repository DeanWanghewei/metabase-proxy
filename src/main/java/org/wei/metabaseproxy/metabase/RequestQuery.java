package org.wei.metabaseproxy.metabase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 16:40
 */
public class RequestQuery implements java.io.Serializable {
    private int database;
    private String type = "native";
    private List<Object> parameters;
    @JsonProperty("native")
    @SerializedName("native")
    private RequestQueryNative queryNative;

    public RequestQuery(int database) {
        this.database = database;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    @JsonProperty("native")
    @SerializedName("native")
    public RequestQueryNative getQueryNative() {
        return queryNative;
    }
    @JsonProperty("native")
    @SerializedName("native")
    public void setQueryNative(RequestQueryNative queryNative) {
        this.queryNative = queryNative;
    }
}
