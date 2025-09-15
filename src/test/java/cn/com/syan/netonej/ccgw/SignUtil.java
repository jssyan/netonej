package cn.com.syan.netonej.ccgw;

import com.syan.netonej.common.NetonejUtil;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.util.*;

/**
 * @Author: xuyaoyao
 * @Date: 2025/9/15 13:33
 * @Description:
 */
public class SignUtil {

    public static void main(String[] args) {
        Map<String,Object> params = new HashMap<>();
        params.put("appid","7aed0001f5e6400ab61ec4e0261313a6");
        params.put("transid","58AA799E7641B81CD1DA989DEB7489DAA3D1510C73904B5384BCFF89F2B80543");
        params.put("kid","4bf9c8c2040249c586070462f8f5a7d8");
        params.put("cn","xyy");
        params.put("csr","MIIBIDCBxgIBADAZMQswCQYDVQQGEwJDTjEKMAgGA1UEAwwBMzBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABLH3AMWIy4Vw9twabBt4sucSo43YyIuoMfFWDOc8F6s2arB6CUZCm/UUeKtnmqParUvPePLRUj8Qh+aEhwBE1KugSzBJBgkqhkiG9w0BCQ4xPDA6MAkGA1UdEwQCMAAwHQYDVR0OBBYEFKyxeX/yzjDnf4h5tvXEONXnJZ5kMA4GA1UdDwEB/wQEAwID+DAKBggqgRzPVQGDdQNJADBGAiEAlynQ8SFe2EfAlzGyHcfDuumxBrRsfA2e8o0DzTWnBfsCIQCt05rjXqgN0UjAcbU5lv9jxAtdBPwgpoYuFaOzm+rIsQ==");
        genSignature(params,"");
    }

    public static String genSignature( Map<String,Object> params,String appSecret){
        String signContent = getSignatureContent(params);
        byte[] signData = SM3(ConvertUtils.hexString2Bytes(appSecret),signContent.getBytes());
        String sign = Base64.getEncoder().encodeToString(signData);
        System.out.println("签名值："+sign);
        return sign;
    }

    public static String getSignatureContent(Map<String, Object> paramMap) {
        Map<String, Object> sortedParams = new TreeMap<>();
        sortedParams = sortMapByKey(paramMap);
        //转成json串

        try {
            return mapToJsonString(sortedParams);
        } catch (Exception e) {
            return null;
        }
    }

    private static String mapToJsonString(Map<String, Object> sortedParams) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        boolean first = true;

        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            if (!first) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\"").append(entry.getKey()).append("\":");

            Object value = entry.getValue();
            if (value instanceof String) {
                jsonBuilder.append("\"").append(value.toString().replace("\"", "\\\"")).append("\"");
            } else {
                jsonBuilder.append(value);
            }

            first = false;
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
        sortMap.putAll(map);
        return sortMap;
    }


    public static byte[] SM3(byte[] key,byte[] data){
        HMac hMac = new HMac(new SM3Digest());
        hMac.init(new KeyParameter(key));
        hMac.update(data,0,data.length);
        byte[] out = new byte[hMac.getMacSize()];
        hMac.doFinal(out,0);
        return out;
    }


}
