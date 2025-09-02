package com.syan.netonej.http.client.ca;


/**
 * @Author: xuyaoyao
 * @Date: 2025/2/21 16:03
 * @Description:
 */
public class Extension {

    private String name;

    private String oid;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
