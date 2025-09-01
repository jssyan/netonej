package com.syan.netonej.http.entity.engine;

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
public class AsyEncryptResponse extends NetoneResponse{

    public AsyEncryptResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    //{"transId":"6bb779bc26564ff6ad48b0c0d097a7b4","kid":"1",
                    // "ciphertext":"BDZ9i3iwwpOuWC1o54s2L3QPJhyRBcxhdxi5sIqo6beHG2/dC5FlVJdcuBRdfKWndF55MPqpuBaXnsyuA/RhtWIBuy/KMnaoypUVEJsFgk7EvkCalU/IY6cVl11qt6TzoeF2ndDNSnu74xpQJrGsu2U=",
                    // "plaintext":null}
                    setResult(new JSONObject(result).getString("ciphertext"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
