package com.syan.netonej.http.entity;

/**
 * @Author mmdet
 * @Date 2022-05-06 10:49
 * @Description
 */
public class KeyListItem {
    private String id;
    private String certificate;
    private String privk;

    public KeyListItem() {
    }

    public KeyListItem(String id) {
        this.id = id;
    }

    public KeyListItem(String id, String certificate, String privk) {
        this.id = id;
        this.certificate = certificate;
        this.privk = privk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPrivk() {
        return privk;
    }

    public void setPrivk(String privk) {
        this.privk = privk;
    }
}
