package org.wei.metabaseproxy.metabase;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 17:12
 */
public class ResponseQueryDataResultMetaDataColumns {
    private String name;
    private String display_name;
    private String base_type;
    private String semantic_type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getBase_type() {
        return base_type;
    }

    public void setBase_type(String base_type) {
        this.base_type = base_type;
    }

    public String getSemantic_type() {
        return semantic_type;
    }

    public void setSemantic_type(String semantic_type) {
        this.semantic_type = semantic_type;
    }
}
