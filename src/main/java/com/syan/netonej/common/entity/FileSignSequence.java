package com.syan.netonej.common.entity;

import com.syan.netonej.exception.NetonejException;
import org.bouncycastle.asn1.*;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;

/**
 * @Author mmdet
 * @Date 2023/2/17 13:30
 * @Description
 */
public class FileSignSequence {

    /**
     * 通过文件内容，生成ASN1编码
     *
     * @param version
     * @param data
     * @param identifier
     * @param signature
     */
    public static byte[] getDERSeqObject(Integer version, byte[] data, String identifier, byte[] signature) throws NetonejException{
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(version));
        v.add(new ASN1ObjectIdentifier(identifier));
        v.add(new DEROctetString(signature));
        v.add(new DEROctetString(data));
        DERSequence seq = new DERSequence(v);
        try {
            return seq.getEncoded();
        } catch (IOException e) {
            throw new NetonejException("组织ASN1序列失败",e);
        }
    }


    /**
     * 通过文件路径生成ASN1编码（大文件下可用此方式）
     */
    public static void getDERSeqObject(Integer version, String identifier, byte[] signature, String fileInPath, String fileOutPath) throws NetonejException {

        FileInputStream fileInputStream = null;
        FileOutputStream byteOutputStream = null;
        try {
            //版本号
            ASN1Integer asn1Verison = new ASN1Integer(version);
            int asn1VerisonLen = asn1Verison.getEncoded().length;

            //OID
            ASN1ObjectIdentifier asn1DigestOID = new ASN1ObjectIdentifier(identifier);
            int asn1DigestOIDLen = asn1DigestOID.getEncoded().length;

            //p7签名
            DEROctetString asn1Signature = new DEROctetString(signature);
            int asn1SignatureLen = asn1Signature.getEncoded().length;

            //原文件
            File file = new File(fileInPath);
            long filelenth = file.length();
            //DER编码时，需要多少字节存储其长度信息（tag+len+filelength）
            long asn1FileLen = 1 + FileSignSequence.encodeLength(filelenth) + filelenth;

            //建立文件输出流
            File outFile = new File(fileOutPath);
            if (!outFile.exists()) {
            }
            byteOutputStream = new FileOutputStream(outFile,false);
            //写SEQUENCE的tag
            byteOutputStream.write(Hex.decode("30"));
            //写SEQUENCE内容的长度
            //版本号的长度+摘要的长度+签名的长度+原文的长度
            FileSignSequence.writeLength(asn1VerisonLen + asn1DigestOIDLen + asn1SignatureLen + asn1FileLen, byteOutputStream);

            byteOutputStream.write(asn1Verison.getEncoded());
            byteOutputStream.write(asn1DigestOID.getEncoded());
            byteOutputStream.write(asn1Signature.getEncoded());
            //原文tag
            byteOutputStream.write(Hex.decode("04"));
            //写原文长度
            FileSignSequence.writeLength(filelenth, byteOutputStream);
            //写原文内容
            fileInputStream = new FileInputStream(file);
            //一次读取1M的大小个字节进行计算，内存足够的话可以调大
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len=fileInputStream.read(buffer)) != -1){
                byteOutputStream.write(buffer,0,len);
            }
        } catch (IOException e) {
            throw new NetonejException("生成ASN1结构失败", e);
        } finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(byteOutputStream != null){
                try {
                    byteOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //输入数据的长度，计算编码时存储这个长度需要多少字节(不包含tag的长度（也是1字节）)
    public static int encodeLength(long length) {
        if (length > 127) {
            int size = 1;
            long val = length;

            while ((val >>>= 8) != 0) {
                size++;
            }
            //size：表示需要size个字节存储长度
            //编码后怎么知道有多个字节是表示长度的，需要额外一个字节表示其长度大小
            return size + 1;
        } else {
            return 1;
        }
    }

    public static void writeLength(
            long length, OutputStream out) throws IOException {
        if (length > 127) {
            int size = 1;
            long val = length;

            while ((val >>>= 8) != 0) {
                size++;
            }

            out.write((byte) (size | 0x80));

            for (int i = (size - 1) * 8; i >= 0; i -= 8) {
                out.write((byte) (length >> i));
            }

        } else {
            out.write((byte) length);
        }
    }
}
