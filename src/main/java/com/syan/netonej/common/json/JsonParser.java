package com.syan.netonej.common.json;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejException;
import org.bouncycastle.util.encoders.Base64;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author mmdet
 * @Date 2022-05-06 09:29
 * @Description
 */
public class JsonParser {

    public static List<NetoneCertificate> parserCertList(String js) throws NetonejException {
        try {
            List<NetoneCertificate> list=new ArrayList<NetoneCertificate>();
            JSONObject json=new JSONObject(js);
            JSONObject items=json.getJSONObject("item");
            if(items != null){
                String[] keys =json.getNames(items);
                if(keys != null && keys.length > 0){
                    for (int i = 0; i < keys.length; i++) {
                        JSONObject item=items.getJSONObject(keys[i]);
                        String cert = item.getString("crt");
                        String privk = "No";
                        if(item.has("privk")){
                            privk = item.getString("privk");
                        }
                        String base64String = NetonejUtil.pemStringToBase64(new String(Base64.decode(cert)));
                        list.add(new NetoneCertificate(base64String,privk));
                    }
                }
            }
            return list;
        } catch (JSONException e) {
            throw new NetonejException("json解析失败",e);
        }
    }
}
