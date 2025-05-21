package org.wei.metabaseproxy.model;

import org.jetbrains.annotations.NotNull;
import org.wei.metabaseproxy.metabase.ResponseDatabase;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 17:47
 */
public class DatabaseModel implements java.io.Serializable,  Comparable<DatabaseModel> {
    private int id;
    private String name;

    public DatabaseModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DatabaseModel(ResponseDatabase item) {
        this.id = item.getId();
        this.name = item.getName();
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + id + ") ";
    }


    @Override
    public int compareTo(@NotNull DatabaseModel o) {
        return this.id - o.id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DatabaseModel){
            DatabaseModel other = (DatabaseModel) obj;
            if(other.id == this.id){
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        return this.id;
    }
}
