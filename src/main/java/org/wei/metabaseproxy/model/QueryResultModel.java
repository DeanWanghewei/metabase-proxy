package org.wei.metabaseproxy.model;

import org.wei.metabaseproxy.client.metabase.ResponseQuery;
import org.wei.metabaseproxy.client.metabase.ResponseQueryDataResultMetaDataColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 17:29
 */
public class QueryResultModel {
    /**
     * 结果表head
     */
    private List<String> head = new ArrayList<>();
    /**
     * 结果数据
     */
    private List<List<String>> data = new ArrayList<>();
    /**
     * 查询结果状态
     */
    private boolean success = true;
    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 查询sql
     */
    private String querySql;

    public QueryResultModel(ResponseQuery response, String querySql) {
        if (!response.getStatus().equals("completed")) {
            this.success = false;
            this.errorMsg = response.getError();
        } else {
            this.data = response.getData().getRows();
            for (ResponseQueryDataResultMetaDataColumns column : response.getData().getResults_metadata().getColumns()) {
                head.add(column.getName());
            }
        }
    }

    public List<String> getHead() {
        return head;
    }

    public List<List<String>> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }


}
