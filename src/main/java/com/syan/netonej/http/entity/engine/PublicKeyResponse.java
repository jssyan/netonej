package com.syan.netonej.http.entity.engine;

import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.json.JSONException;
import com.syan.netonej.common.json.JSONObject;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.NetoneResponse;

/**
 * @Author: xuyaoyao
 * @Date: 2025/9/1 14:40
 * @Description:
 */
public class PublicKeyResponse extends NetoneResponse {
    private String signPublicKey;

    private String encPublicKey;

    public PublicKeyResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    //{"transId":"0a29f39f63cf46baa466cf99309d09f4",
                    // "signPublicKey":"MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE9mDFzVpBdAGvfGeJsFpj2XcxZI34rmLz5UBxXaw9/1DbikCztxHIXikC1keV7liVJKJ+Wt5PLQqiAwYjC2S8DQ==",
                    // "encPublicKey":"MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE9mDFzVpBdAGvfGeJsFpj2XcxZI34rmLz5UBxXaw9/1DbikCztxHIXikC1keV7liVJKJ+Wt5PLQqiAwYjC2S8DQ=="}
                    JSONObject json = new JSONObject(result);
                    this.setSignPublicKey(json.getString("signPublicKey")); // 使用optString避免JSONException;
                    this.setEncPublicKey(json.getString("encPublicKey"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String getSignPublicKey() {
        return signPublicKey;
    }

    public void setSignPublicKey(String signPublicKey) {
        this.signPublicKey = signPublicKey;
    }

    public String getEncPublicKey() {
        return encPublicKey;
    }

    public void setEncPublicKey(String encPublicKey) {
        this.encPublicKey = encPublicKey;
    }
}
