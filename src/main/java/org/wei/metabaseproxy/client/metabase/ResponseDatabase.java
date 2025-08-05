package org.wei.metabaseproxy.client.metabase;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 16:32
 */
public class ResponseDatabase {
    private int id;
    private String name;
    private String description;
    private String engine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
}
