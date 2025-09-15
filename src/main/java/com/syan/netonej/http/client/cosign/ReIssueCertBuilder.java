package com.syan.netonej.http.client.cosign;


import com.syan.netonej.common.dict.CosignAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ca.CertificateResponse;

import java.util.HashMap;
import java.util.Map;


public class ReIssueCertBuilder extends BaseClient<ReIssueCertBuilder> {
    private String appid;

    private String transid;

    private String sign;

    private String kid;

    //原签名证书序列号（十六进制）
    private String serial;

    //名称（用户姓名，企业名称等）
    private String cn;

    //证书 O 项,证书所属组织
    private String o;

    //证书 OU 项,证书所属组织单元
    private String ou;

    //证书 C 项,国家,默认 CN（缺省值，默认为CN）
    private String c;

    //证书 S 项,证书所属省
    private String s;

    //证书 L 项,证书所属市
    private String l;

    //证书 E 项,电子邮箱
    private String e;

    //证书签发请求（base64编码）
    private String csr;

    //签发双证(1) , 签发单证（0), 默认1
    private String certType;

    public ReIssueCertBuilder setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public ReIssueCertBuilder setTransid(String transid) {
        this.transid = transid;
        return this;
    }

    public ReIssueCertBuilder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public ReIssueCertBuilder setKid(String kid) {
        this.kid = kid;
        return this;
    }

    public ReIssueCertBuilder setSerial(String serial) {
        this.serial = serial;
        return this;
    }

    public ReIssueCertBuilder setCn(String cn) {
        this.cn = cn;
        return this;
    }

    public ReIssueCertBuilder setO(String o) {
        this.o = o;
        return this;
    }

    public ReIssueCertBuilder setOu(String ou) {
        this.ou = ou;
        return this;
    }

    public ReIssueCertBuilder setC(String c) {
        this.c = c;
        return this;
    }

    public ReIssueCertBuilder setS(String s) {
        this.s = s;
        return this;
    }

    public ReIssueCertBuilder setL(String l) {
        this.l = l;
        return this;
    }

    public ReIssueCertBuilder setE(String e) {
        this.e = e;
        return this;
    }

    public ReIssueCertBuilder setCsr(String csr) {
        this.csr = csr;
        return this;
    }

    public ReIssueCertBuilder setCertType(String certType) {
        this.certType = certType;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();
        if(appid!=null){
            params.put("appid",appid);
        }else{
            throw new NetonejException("appid not be empty");
        }

        if(transid!=null){
            params.put("transid",transid);
        }else{
            throw new NetonejException("transid not be empty");
        }

        if(sign!=null){
            params.put("sign",sign);
        }else{
            throw new NetonejException("sign not be empty");
        }

        if(kid!=null){
            params.put("kid",kid);
        }else{
            throw new NetonejException("kid not be empty");
        }

        if(serial != null){
            params.put("serial", serial);
        }else{
            throw new NetonejException("serial not be empty");
        }

        if(cn != null){
            params.put("cn", cn);
        }else{
            throw new NetonejException("cn not be empty");
        }

        if(o != null){
            params.put("o", o);
        }

        if(ou != null){
            params.put("ou", ou);
        }

        if(c != null){
            params.put("c", c);
        }else{
            params.put("c", "CN");
        }

        if(s != null){
            params.put("s", s);
        }

        if(l != null){
            params.put("l", l);
        }

        if(e != null){
            params.put("e", e);
        }
        if(csr != null){
            params.put("csr", csr);
        }else{
            throw new NetonejException("csr not be empty");
        }

        if(certType != null){
            params.put("certType", certType);
        }else{
            params.put("certType", "1");
        }

        return params;
    }


    @Override
    public CertificateResponse build() throws NetonejException {
        return new CertificateResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CosignAction.COSIGN_ACTION_REISSUE;
    }
}
