package com.syan.netonej.http.client.tsa;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.util.encoders.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 17:08
 * @Description
 */
public class TSACreateBuilder extends BaseClient<TSACreateBuilder> {

    private byte[] data;
    private DataType dataType = DataType.PLAIN;
    private String algo;

    public TSACreateBuilder setData(byte[] data) {
        this.data = data;
        return this;
    }

    public TSACreateBuilder setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public TSACreateBuilder setAlgo(String algo) {
        this.algo = algo;
        return this;
    }

    public TSACreateBuilder setAlgo(DigestAlgorithm digestAlgorithm) {
        this.algo = digestAlgorithm.getName();
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if (DataType.PLAIN == dataType) {
            params.put("data", Base64.toBase64String(data));
        } else {
            params.put("digest", NetonejUtil.byte2HexString(data));
        }
        if (!NetonejUtil.isEmpty(algo)) {
            params.put("algo", algo.trim().toLowerCase());
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return dataType == DataType.TIMESTAMP_REQUEST?Action.TSA_ACTION_TSRS:Action.TSA_ACTION_TSAC;
    }

    @Override
    public NetoneTSA build() throws NetonejException {
        if (dataType == DataType.TIMESTAMP_REQUEST) {
            return new NetoneTSA(super.build(data));
        } else {
            return new NetoneTSA(super.build());
        }
    }
}
