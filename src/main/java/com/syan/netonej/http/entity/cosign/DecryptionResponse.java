package com.syan.netonej.http.entity.cosign;

import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.json.JSONException;
import com.syan.netonej.common.json.JSONObject;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.NetoneResponse;

/**
 * @Author: xuyaoyao
 * @Date: 2025/9/12 10:34
 * @Description:
 */
public class DecryptionResponse extends NetoneResponse {
    private String appid;

    private String transid;

    private String sign;

    private String kid;

    private String T2;


    public DecryptionResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    JSONObject json = new JSONObject(result);
                    this.setAppid(json.getString("appid"));
                    this.setTransid(json.getString("transid"));
                    this.setSign(json.getString("sign"));
                    this.setKid(json.getString("kid"));
                    this.setT2(json.getString("T2"));
                    setResult(json.getString("T2"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getT2() {
        return T2;
    }

    public void setT2(String t2) {
        T2 = t2;
    }
}
