package com.syan.netonej.http.client.svs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneSVS;
import org.bouncycastle.util.encoders.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 16:26
 * @Description
 */
public class PKCS1VerifyBuilder extends BaseClient<PKCS1VerifyBuilder> {

    private String id;
    private IdMagic idmagic = IdMagic.KID;
    private String cert;//所需要验证的证书(B64编码). 或者使用(id/idmagic组合)
    private String signature;
    private String data;
    private byte[] signerid;
    private DataType dataType = DataType.PLAIN;
    private String algo;
    

    public PKCS1VerifyBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public PKCS1VerifyBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PKCS1VerifyBuilder setCert(String cert) {
        this.cert = cert;
        return this;
    }

    public PKCS1VerifyBuilder setSignature(byte[] signature) {
        this.signature = Base64.toBase64String(signature);
        return this;
    }

    public PKCS1VerifyBuilder setBase64Signature(String signature) {
        this.signature = signature;
        return this;
    }

    public PKCS1VerifyBuilder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }
    public PKCS1VerifyBuilder setData(String data) {
        return setData(data.getBytes());
    }
    public PKCS1VerifyBuilder setBase64Data(String data) {
        this.data = data;
        return this;
    }

    public PKCS1VerifyBuilder setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public PKCS1VerifyBuilder setAlgo(String algo) {
        this.algo = algo;
        return this;
    }

    public PKCS1VerifyBuilder setAlgo(DigestAlgorithm digestAlgorithm) {
        this.algo = digestAlgorithm.getName();
        return this;
    }

    public PKCS1VerifyBuilder setUserId(byte[] userId) {
        this.signerid = userId;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if (!NetonejUtil.isEmpty(cert)) {
            params.put("cert", cert);
        }else{
            if (!NetonejUtil.isEmpty(id)) {
                params.put("id", id);
                params.put("idmagic", idmagic.name().toLowerCase());
            }
        }
        params.put("signature", signature);
        if (!NetonejUtil.isEmpty(algo)) {
            params.put("algo", algo);
        }
        params.put("data", data);
        params.put("datt", dataType.ordinal()+"");
        if (signerid != null) {
            params.put("signerid", Base64.toBase64String(signerid));
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.SVS_ACTION_VP1;
    }

    @Override
    public NetoneSVS build() throws NetonejException {
        return new NetoneSVS(super.build());
    }
}
