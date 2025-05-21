package org.wei.metabaseproxy.model;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月20日 10:10
 */
public class RunSqlParamsModel implements java.io.Serializable {
    /**
     * 数据库
     */
    private DatabaseModel selectedDatabase;
    /**
     * 用户变量,或者之前缓存的变量
     */
    private HashMap<String, String> userVarMap;

    /**
     * 需要用户输入的参数
     */
    private Set<String> userInputVarList;


    public DatabaseModel getSelectedDatabase() {
        return selectedDatabase;
    }

    public void setSelectedDatabase(DatabaseModel selectedDatabase) {
        this.selectedDatabase = selectedDatabase;
    }

    public HashMap<String, String> getUserVarMap() {
        return userVarMap;
    }

    public void setUserVarMap(HashMap<String, String> userVarMap) {
        this.userVarMap = userVarMap;
    }

    public Set<String> getUserInputVarList() {
        return userInputVarList;
    }

    public void setUserInputVarList(Set<String> userInputVarList) {
        this.userInputVarList = userInputVarList;
    }
}
