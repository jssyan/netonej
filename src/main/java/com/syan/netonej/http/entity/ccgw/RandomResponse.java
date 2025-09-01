package com.syan.netonej.http.entity.ccgw;

import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.json.JSONException;
import com.syan.netonej.common.json.JSONObject;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.common.xml.XmlData;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.NetoneResponse;

import java.io.ByteArrayInputStream;

/**
 * @Author: xuyaoyao
 * @Date: 2025/9/1 14:28
 * @Description:
 */
public class RandomResponse extends NetoneResponse{

    public RandomResponse(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                try {
                    //result示例值：
                    //{"transId":"9e9641b2b1a94f2080de6a47fc29b2bf","random":"rIeFNW2QQm7Hh8gJ3vc1EJGu/Iv1Ao/0C4RTZNnjvWk="}
                    setResult(new JSONObject(result).getString("random"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
