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
public class AsyDecryptResponse extends NetoneResponse{

    public AsyDecryptResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    //{"transId":"2e25983c0cc94cbda24dd5def8958d48","kid":"1","ciphertext":null,
                    // "plaintext":"YWUyNDljYzU0MzlmMTUwNA=="}
                    setResult(new JSONObject(result).getString("plaintext"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
