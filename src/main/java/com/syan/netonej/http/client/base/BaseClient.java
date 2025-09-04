package com.syan.netonej.http.client.base;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.HttpURLConnectionClient;
import com.syan.netonej.http.client.CcgwClient;
import com.syan.netonej.http.entity.NetoneResponse;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import java.io.Serializable;
import java.util.*;

public abstract class BaseClient<R extends BaseClient> implements Serializable {
    private static final long serialVersionUID = -7174118653689916251L;

    protected String host;

    protected String port = "80";

    //绿色通道，0代表禁止，如果允许绿色通道，不要设置该参数即可
    protected String greenpass = "0";

    /**
     * 应用模式取值abck2用于农行K宝二代项目 默认为标准模式
     */
    protected String application;

    protected String responseformat = "0";

    protected CcgwClient ccgwClient;


    public R setCcgwClient(CcgwClient ccgwClient) {
        this.ccgwClient = ccgwClient;
        return (R)this;
    }

    public BaseClient() { }

    public R setHost(String host) {
        this.host = host;
        return (R)this;
    }

    public R setPort(String port) {
        this.port = port;
        return (R)this;
    }

    //绿色通道，需要服务端开启相关参数配合，设置为0表示禁止,设置为null或者空则表示允许绿色通道
    public R setGreenpass(String greenpass) {
        this.greenpass = greenpass;
        return (R)this;
    }

    //设置模式
    public R setApplication(String application) {
        this.application = application;
        return (R)this;
    }

    @Deprecated
    //希望签名服务器返回的数据格式，如xml text json等
    public R setResponseformat(String responseformat) {
        this.responseformat = responseformat;
        return (R)this;
    }

    @Deprecated
    public R setResponseformat(ResponseFormat responseformat) {
        this.responseformat = String.valueOf(responseformat.ordinal());
        return (R)this;
    }

    //表单参数设置
    protected abstract Map<String, String> buildParams() throws NetonejException;

    //资源路径 如/sl.svr
    protected abstract String buildUrlPath();

    //post 表单形式
    public NetoneResponse build() throws NetonejException{
        String url = buildFullUrl();
        Map<String, String> params = new HashMap<String, String>();
        params.put("responseformat", responseformat);
        if (!NetonejUtil.isEmpty(greenpass) && greenpass.equals("0")) {
            params.put("greenpass", greenpass);
        }
        if (!NetonejUtil.isEmpty(application)) {
            params.put("application", application);
        }
        Map<String, String> childParams = buildParams();
        if(childParams != null && childParams.size() > 0){
            params.putAll(childParams);
        }
        if(ccgwClient != null ){
            params.put("appId",ccgwClient.getAppId());
            String data = getSignatureString(params);
            String sign = hmacSM3(ccgwClient.getAppSecret().getBytes(), data.toString().getBytes());
            params.put("sign",sign);
        }

        NetoneResponse response = HttpURLConnectionClient.builder().url(url).sslContext(ccgwClient != null ? ccgwClient.getSslContext() : null).param(params).post();
        if(responseformat.equals("1")){
            response.setFormat(ResponseFormat.XML);
        }else{
            response.setFormat(ResponseFormat.TEXT);
        }
        return response;
    }

    //post bytes形式
    protected NetoneResponse build(byte[] bytes) throws NetonejException{
        String url = buildFullUrl();
        NetoneResponse response = HttpURLConnectionClient.builder().url(url).postBytes(bytes);
        return response;
    }

    //组织url
    private String buildFullUrl() throws NetonejException {
        if(NetonejUtil.isEmpty(host)){
            throw new NetonejException("host must be not empty");
        }
        String temp = host+":"+port;
        if(!temp.startsWith("http://") && !temp.startsWith("https://")){
            temp = "http://"+host+":"+port;
        }
        if(ccgwClient!= null){
            return temp+"/ccgw/api/v1/"+ccgwClient.getModuleName()+"/"+buildUrlPath();
        }else {
            return temp + "/" + buildUrlPath();
        }
    }


    /**
     * 将参数按key值排序
     */
    private String getSignatureString(Map<String, String> paramMap) {
        Map<String, Object> sortedParams = new TreeMap<String,Object>();
        sortedParams = sortMapByKey(paramMap);
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = sortedParams.get(key);
            if (!NetonejUtil.isEmpty(key)&& value!=null && !key.equals("sign")) {
                content.append(key).append(value);
            }
        }
        return content.toString();
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    private Map<String, Object> sortMapByKey(Map<String, String> map) {
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


    private String hmacSM3(byte[] key,byte[] data){
        HMac hMac = new HMac(new SM3Digest());
        hMac.init(new KeyParameter(key));
        hMac.update(data,0,data.length);
        byte[] out = new byte[hMac.getMacSize()];
        hMac.doFinal(out,0);
        return Hex.toHexString(out);
    }
}
