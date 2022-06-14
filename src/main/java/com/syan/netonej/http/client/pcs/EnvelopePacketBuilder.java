package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.CipherAlgorithm;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetonePCS;
import org.bouncycastle.util.encoders.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:06
 * @Description
 */
public class EnvelopePacketBuilder extends BaseClient<EnvelopePacketBuilder> {
    private String id;
    private IdMagic idmagic;
    private IdMagic peerMagic;
    private String passwd;
    private String data;
    private String cipher;
    private String peer;
    private String digestAlgo;
    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if (NetonejUtil.isEmpty(id)) {
            throw new NetonejException("id not be empty");
        }
        params.put("id", id);
        if (data == null) {
            throw new NetonejException("data not be empty");
        }
        params.put("data", data);

        if (!NetonejUtil.isEmpty(passwd)) {
            params.put("passwd", passwd);
        }
        if (idmagic != null) {
            params.put("idmagic", idmagic.name().toLowerCase());
        }
        if (!NetonejUtil.isEmpty(cipher)) {
            params.put("cipher", cipher);
        }
        if(!NetonejUtil.isEmpty(peer)){
            if(peerMagic != null){
                String magic = peerMagic.name().toLowerCase();
                params.put("peer", magic+"://"+peer);
            }else{
                params.put("peer", peer);
            }
        }
        if (!NetonejUtil.isEmpty(digestAlgo)) {
            params.put("algo", digestAlgo);
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_ENVSEAL;
    }

    public EnvelopePacketBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public EnvelopePacketBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public EnvelopePacketBuilder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public EnvelopePacketBuilder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }

    public EnvelopePacketBuilder setData(String data) {
        return setData(data.getBytes());
    }

    public EnvelopePacketBuilder setBase64Data(String data) {
        this.data = data;
        return this;
    }
    public EnvelopePacketBuilder setCipherAlgo(String algo) {
        this.cipher = algo;
        return this;
    }

    public EnvelopePacketBuilder setCipherAlgo(CipherAlgorithm cipher) {
        this.cipher = cipher.getName();
        return this;
    }

    public EnvelopePacketBuilder setDigestAlgo(String digestAlgo) {
        this.digestAlgo = digestAlgo;
        return this;
    }

    public EnvelopePacketBuilder setDigestAlgo(DigestAlgorithm digestAlgo) {
        this.digestAlgo = digestAlgo.getName();
        return this;
    }

    public EnvelopePacketBuilder setPeer(String cert) {
        this.peer = cert;
        return this;
    }


    public EnvelopePacketBuilder setPeerMagic(IdMagic magic) {
        this.peerMagic = magic;
        return this;
    }

    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }
}
