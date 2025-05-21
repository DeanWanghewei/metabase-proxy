package org.wei.metabaseproxy.metabase;

import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 16:57
 */
public class ResponseQueryData {
    private List<List<String>> rows;
    private List<ResponseQueryDataResultDataCols> cols;
    private ResponseQueryDataResultMetaData results_metadata;

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    public List<ResponseQueryDataResultDataCols> getCols() {
        return cols;
    }

    public void setCols(List<ResponseQueryDataResultDataCols> cols) {
        this.cols = cols;
    }

    public ResponseQueryDataResultMetaData getResults_metadata() {
        return results_metadata;
    }

    public void setResults_metadata(ResponseQueryDataResultMetaData results_metadata) {
        this.results_metadata = results_metadata;
    }
}
