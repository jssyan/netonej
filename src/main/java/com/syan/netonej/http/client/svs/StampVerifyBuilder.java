package com.syan.netonej.http.client.svs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneSVS;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 15:54
 * @Description 验证电子签章（svs v3.8.0开始支持）
 */
public class StampVerifyBuilder extends BaseClient<StampVerifyBuilder> {
    private String digest;//待验证原文杂凑值得BASE64编码
    private String ses;//签章得BASE64编码，支持国密标准GMT/0031 和国标GB/T 38540两种格式得签章数据

    public StampVerifyBuilder setDigest(String digest) {
        this.digest = digest;
        return this;
    }

    public StampVerifyBuilder setSes(String ses) {
        this.ses = ses;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if(NetonejUtil.isEmpty(digest)){
            throw new NetonejException("原文杂凑值不能为空");
        }
        params.put("digest", digest);

        if(NetonejUtil.isEmpty(ses)){
            throw new NetonejException("电子签章不能为空");
        }
        params.put("ses", ses);
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.SVS_ACTION_STAMP;
    }

    @Override
    public NetoneSVS build() throws NetonejException {
        return new NetoneSVS(super.build());
    }
}
