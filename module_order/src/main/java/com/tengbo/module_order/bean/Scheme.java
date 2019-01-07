package com.tengbo.module_order.bean;

import java.util.List;

public class Scheme {
    private String schemeCode;
    private List<Node> schemeInstance;

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public List<Node> getSchemeInstance() {
        return schemeInstance;
    }

    public void setSchemeInstance(List<Node> schemeInstance) {
        this.schemeInstance = schemeInstance;
    }
}
