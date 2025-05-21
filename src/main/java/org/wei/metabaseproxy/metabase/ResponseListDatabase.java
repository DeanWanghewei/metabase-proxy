package org.wei.metabaseproxy.metabase;

import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 16:31
 */
public class ResponseListDatabase implements java.io.Serializable {
    private List<ResponseDatabase> data;


    public List<ResponseDatabase> getData() {
        return data;
    }

    public void setData(List<ResponseDatabase> data) {
        this.data = data;
    }
}
