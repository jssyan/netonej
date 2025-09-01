package com.syan.netonej.http.entity.ccgw;

import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.json.JSONException;
import com.syan.netonej.common.json.JSONObject;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.NetoneResponse;

/**
 * @Author: xuyaoyao
 * @Date: 2025/9/1 14:28
 * @Description:
 */
public class CmacResponse extends NetoneResponse{

    private String mac;

    private String iv;

    public CmacResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    //{"transId":"7670f3eafcf54105b076efc74c8df121","mac":"GlT7t/Dk7kphbydBBYW96g==","iv":"a5ab73f5028dc95a"}
                    JSONObject json = new JSONObject(result);
                    this.setIv(json.optString("iv", null)); // 使用optString避免JSONException;
                    this.setMac(json.getString("mac"));
                    setResult(new JSONObject(result).getString("mac"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
