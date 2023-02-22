package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.common.entity.FileSignSequence;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneResponse;
import org.bouncycastle.util.encoders.Base64;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:32
 * @Description 文件保护结构
 */
public class FileSignBuilder extends BaseClient<FileSignBuilder> {

    //签名需要的参数
    private String id;
    private IdMagic idmagic = IdMagic.KID;
    private String passwd;
    //对文件摘要的算法，默认SM3
    private String algo="SM3";

    public FileSignBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public FileSignBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public FileSignBuilder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public FileSignBuilder setAlgo(String algo) {
        this.algo = algo;
        return this;
    }

    public FileSignBuilder setAlgo(DigestAlgorithm digestAlgorithm) {
        this.algo = digestAlgorithm.getName();
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
       return null;
    }

    @Override
    protected String buildUrlPath() {
        return null;
    }

    @Override
    public NetoneResponse build(byte[] fileData) throws NetonejException {
        //1.对原文摘要
        String[] digestResult = digestFileData(fileData,null);
        String digestOID = digestResult[0];//摘要算法的OID
        String digestData = digestResult[1];//base64摘要数据
        //2.将原文摘要进行p7签名
        NetonePCS pcs = new PKCS7Builder().setHost(host).setPort(port)
                .setId(id).setIdmagic(idmagic).setPasswd(passwd)
                .setBase64Data(digestData).setAlgo(algo)
                .setAttach(false)
                .build();
        if(pcs.getStatusCode() != 200){
            String errMsg = pcs.getStatusCodeMessage();
            throw new NetonejException("P7签名失败 "+errMsg);
        }
        String p7 = pcs.getResult();
        //3.组装ASN1结构数据返回
        byte[] asn1 = FileSignSequence.getDERSeqObject(1,fileData,digestOID,Base64.decode(p7));
        NetoneResponse response = new NetoneResponse();
        response.setStatusCode(200);
        response.setResult(asn1);
        return response;
    }

    public void build(String inPath,String outPath) throws NetonejException {
        //1.对原文摘要
        String[] digestResult = digestFileData(null,inPath);
        String digestOID = digestResult[0];//摘要算法的OID
        String digestData = digestResult[1];//base64摘要数据
        //2.将原文摘要进行p7签名
        NetonePCS pcs = new PKCS7Builder().setHost(host).setPort(port)
                .setId(id).setIdmagic(idmagic).setPasswd(passwd)
                .setBase64Data(digestData).setAlgo(algo)
                .setAttach(false)
                .build();
        if(pcs.getStatusCode() != 200){
            String errMsg = pcs.getStatusCodeMessage();
            throw new NetonejException("P7签名失败 "+errMsg);
        }
        String p7 = pcs.getResult();
        //3.组装ASN1结构数据返回
        FileSignSequence.getDERSeqObject(1,digestOID,Base64.decode(p7),inPath,outPath);
    }

    private String[] digestFileData(byte[] fileData,String fileInPath) throws NetonejException{
        NetoneDigest digest = new NetoneDigest(algo);
        String[] result = new String[2];
        //拿到OID
        result[0] = digest.getDigestOID();
        byte[] digestResult = null;
        if(fileData != null){
            digestResult=digest.digest(fileData);
        }else if(fileInPath != null){
            FileInputStream in = null;
            try {
                in = new FileInputStream(fileInPath);
                digest.update(in);
                digestResult = digest.digest();
            } catch (IOException e){
                throw new NetonejException("文件流操作失败 ",e);
            }finally {
                if(in != null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            throw new NetonejException("文件内容不能为空");
        }
        //拿到摘要结果
        result[1]=Base64.toBase64String(digestResult);
        //返回
        return result;
    }
}
