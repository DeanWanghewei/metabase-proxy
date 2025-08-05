package org.wei.metabaseproxy.client.metabase;

import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 16:59
 */
public class ResponseQueryDataResultMetaData {
    private List<ResponseQueryDataResultMetaDataColumns> columns;

    public List<ResponseQueryDataResultMetaDataColumns> getColumns() {
        return columns;
    }

    public void setColumns(List<ResponseQueryDataResultMetaDataColumns> columns) {
        this.columns = columns;
    }
}
