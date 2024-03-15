package com.syan.netonej.http.client.tsa;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-07 11:43
 * @Description
 */
public class TSAVerifyBuilder extends BaseClient<TSAVerifyBuilder> {

    private String timestamp;
    private byte[] data;
    private DataType dataType = DataType.PLAIN;

    public TSAVerifyBuilder setTimestamp(byte[] timestamp) {
        this.timestamp = Base64.toBase64String(timestamp);
        return this;
    }

    public  TSAVerifyBuilder setBase64Timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TSAVerifyBuilder setData(byte[] data) {
        this.data = data;
        return this;
    }

    public TSAVerifyBuilder setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tsr", timestamp);
        if (data != null) {
            if (DataType.PLAIN == dataType) {
                params.put("data", Base64.toBase64String(data));
            } else {
                params.put("digest", Hex.toHexString(data));
            }
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.TSA_ACTION_TSAV;
    }

    @Override
    public NetoneTSA build() throws NetonejException {
       return new NetoneTSA(Base64.decode(timestamp),super.build());
    }
}
