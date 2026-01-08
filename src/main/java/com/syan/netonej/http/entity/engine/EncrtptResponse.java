package com.syan.netonej.http.entity.engine;

import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.json.JSONException;
import com.syan.netonej.common.json.JSONObject;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.NetoneResponse;


public class EncrtptResponse extends NetoneResponse {
    private Integer kid;

    private String ciphertext;

    //对称加密算法类型：SM1,SM4,AES
    private String algorithm;

    //加密模式：CBC,ECB,OFB,CFB
    private String algorithmMode;

    //填充模式：PKCS5Padding,NoPadding
    private String paddingMode;

    // 初始化向量（Initialization Vector），用于某些加密模式（如CBC）中增强安全性，确保相同明文加密后产生不同的密文
    private String iv;

    //GCM 模式加密生成的校验码 tag，和密文一起返回（Base64 编码）
    private String authData;

    public EncrtptResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    //{"transId":"5f4c921e41ff4e09a50be3b1a220f796","kid":"1","algorithm":"SM4","algorithmMode":"CBC","paddingMode":"PKCS5Padding","iv":"86644c70a7a44052","ciphertext":"Nemn1n1P0T/rTSZGAAC32NUquTgfgYWFSIXombHBdKM="}
                    JSONObject json = new JSONObject(result);
                    this.setKid(json.getInt("kid"));
                    this.setAlgorithm(json.getString("algorithm"));
                    this.setAlgorithmMode(json.getString("algorithmMode"));
                    this.setPaddingMode(json.getString("paddingMode"));
                    this.setIv(json.optString("iv", null)); // 使用optString避免JSONException;
                    this.setCiphertext(json.getString("ciphertext"));
                    this.setAuthData(json.optString("authData", null));
                    setResult(json.getString("ciphertext"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Integer getKid() {
        return kid;
    }

    public void setKid(Integer kid) {
        this.kid = kid;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public String getAuthData() {
        return authData;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithmMode() {
        return algorithmMode;
    }

    public void setAlgorithmMode(String algorithmMode) {
        this.algorithmMode = algorithmMode;
    }

    public String getPaddingMode() {
        return paddingMode;
    }

    public void setPaddingMode(String paddingMode) {
        this.paddingMode = paddingMode;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
