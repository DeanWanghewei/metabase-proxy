package org.wei.metabaseproxy.client.metabase;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 16:54
 */
public class ResponseQuery {
    private ResponseQueryData data;
    private Long row_count;
    private String status;
    private String error;

    public ResponseQueryData getData() {
        return data;
    }

    public void setData(ResponseQueryData data) {
        this.data = data;
    }

    public Long getRow_count() {
        return row_count;
    }

    public void setRow_count(Long row_count) {
        this.row_count = row_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
