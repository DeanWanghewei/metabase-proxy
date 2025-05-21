package org.wei.metabaseproxy.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.wei.metabaseproxy.model.DatabaseModel;

import java.util.HashMap;

/**
 * @author deanwanghewei@gmail.com
 * description
 * 用户临时参数保存
 * @date 2025年05月19日 11:14
 */
public class CurrentUserVarState implements PersistentStateComponent<CurrentUserVarState> {
    private static final CurrentUserVarState INSTANCE = new CurrentUserVarState();

    private DatabaseModel cacheDatabaseSelected;

    private HashMap<String, String> userVarMap = new HashMap<>();

    public static CurrentUserVarState getInstance() {
        return INSTANCE;
    }

    @Override
    public @NotNull CurrentUserVarState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CurrentUserVarState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public DatabaseModel getCacheDatabaseSelected() {
        return cacheDatabaseSelected;
    }

    public void setCacheDatabaseSelected(DatabaseModel cacheDatabaseSelected) {
        this.cacheDatabaseSelected = cacheDatabaseSelected;
    }

    public HashMap<String, String> getUserVarMap() {
        return userVarMap;
    }
}
