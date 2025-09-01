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
public class HmacResponse extends NetoneResponse{

    public HmacResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    //{"transId":"b113d48f4cce4c24ab3935d223a864c0","mac":"jQGO/ZzrnGj892j8M1thIfnlAkxdTQJPSjOP7UfKjvc=","iv":null}
                    setResult(new JSONObject(result).getString("mac"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
