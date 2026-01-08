package com.syan.netonej.http.entity.engine;

import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.json.JSONException;
import com.syan.netonej.common.json.JSONObject;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.NetoneResponse;


public class SignResponse extends NetoneResponse {
    private Integer kid;

    private String signature;

    //对称加密算法类型：SM1,SM4,AES
    private String userId;


    public SignResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    // {"transId":"70129815f37b4b86b45a3f2df6dbc611","kid":"1",
                    // "signature":"MEYCIQC/DZnQkyP0Kz90ZMaqaIwlnTAoYF0/qbZb2tE5gA+oLgIhAMQNHG7PbZ2uBgJI+SVzLqRylYa7if8lQToUlN/sKNUI",
                    // "userId":null}
                    JSONObject json = new JSONObject(result);
                    this.setKid(json.getInt("kid"));
                    this.setUserId(json.optString("userId", null)); // 使用optString避免JSONException;
                    this.setSignature(json.getString("signature"));
                    setResult(json.getString("signature"));
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
