package com.syan.netonej.http.client.ca;


import com.syan.netonej.common.dict.CaAction;
import com.syan.netonej.common.json.JSONArray;
import com.syan.netonej.common.json.JSONException;
import com.syan.netonej.common.json.JSONObject;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ca.CertificateResponse;


import java.util.HashMap;
import java.util.Map;


public class IssueBuilder extends BaseClient<IssueBuilder> {
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

    //证书扩展项，是个对象数组
    private Extension[] extension;

    //证书签发请求（base64编码）
    private String csr;

    //签发双证(1) , 签发单证（0), 默认1
    private String certType;

    public IssueBuilder setCn(String cn) {
        this.cn = cn;
        return this;
    }

    public IssueBuilder setO(String o) {
        this.o = o;
        return this;
    }

    public IssueBuilder setOu(String ou) {
        this.ou = ou;
        return this;
    }

    public IssueBuilder setC(String c) {
        this.c = c;
        return this;
    }

    public IssueBuilder setS(String s) {
        this.s = s;
        return this;
    }

    public IssueBuilder setL(String l) {
        this.l = l;
        return this;
    }

    public IssueBuilder setE(String e) {
        this.e = e;
        return this;
    }

    public IssueBuilder setExtension(Extension[] extension) {
        this.extension = extension;
        return this;
    }

    public IssueBuilder setCsr(String csr) {
        this.csr = csr;
        return this;
    }

    public IssueBuilder setCertType(String certType) {
        this.certType = certType;
        return this;
    }


    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();

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

        if(extension != null && extension.length>0){
            JSONArray extensions = new JSONArray();
            for (Extension extension : extension) {
                try {
                    JSONObject ext = new JSONObject();
                    ext.put("name", extension.getName());
                    ext.put("oid", extension.getOid());
                    ext.put("value", extension.getValue());
                    extensions.put(ext);
                }catch (JSONException exception){

                }

            }
            String extensionStr = extensions.toString();
            params.put("extension", extensionStr);
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
        return CaAction.CA_ACTION_ISSUE;
    }
}
