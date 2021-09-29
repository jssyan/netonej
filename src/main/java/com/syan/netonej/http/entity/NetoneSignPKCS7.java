/**
 * 文 件 名:  NetoneSignPKCS7.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述: PKCS#7签名对象
 * 修 改 人:  liyb
 * 修改时间:  2013-04-28 
 */
package com.syan.netonej.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import com.syan.netonej.common.CMSSignedDataUtil;

/**Netone PKCS#7签名对象
 * 
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneSignPKCS7  extends NetonePCS{
	/**
	 * PKCS#7签名对象
	 */
	private CMSSignedData signedData;
	
	/**
	 * 签名证书
	 */
	private List<NetoneCertificate> signedCertificates=new ArrayList<NetoneCertificate>();
	
	
	/**
	 * 证书链
	 */
	private List<NetoneCertificate>  certList=new ArrayList<NetoneCertificate>() ;
	
	/**
	 * 原文
	 */
	private String textdata;
	
	/**
	 *  获取签名原文
	 * @return 原文
	 */
	public String getTextdata() {
		return textdata;
	}

	
	/** 构造PKCS#7返回对象
	 * @param response  调用post服务返回对象
	 * @throws CMSException 
	 * @throws IOException 
	 * @throws CertificateException 
	 */
	public NetoneSignPKCS7(NetoneResponse response) throws CMSException, CertificateException, IOException{
		super(response);
		if(response.getStatusCode()==200){
			  try{
				   CMSSignedDataUtil cms = new CMSSignedDataUtil(Base64.decode(response.getRetString()));
				    List<X509CertificateHolder> certs = cms.getCertificates();
			        for (X509CertificateHolder holder : certs) {
			        	certList.add(new NetoneCertificate(holder.getEncoded()));		         
			        }		        
			        List<IssuerAndSerialNumber> issuerAndSerialNumbers = cms.getSignerIssuerAndSerialNumber();
			        for (IssuerAndSerialNumber is : issuerAndSerialNumbers) {
			            X509CertificateHolder signer = cms.getSignerCert(is);
			        	signedCertificates.add(new NetoneCertificate(signer.getEncoded())) ;		       
			        }
			       signedData=new CMSSignedData(new ByteArrayInputStream(Base64.decode(response.getRetString()))); 
		        //签名原文
		        if(signedData.getSignedContent()!=null){
		        	textdata =new String((byte[])signedData.getSignedContent().getContent());
		        }

		  }catch(Exception e){
			  e.printStackTrace();
		  }
	        
		}
	}

	/**
	 * 获取签名数据的证书链列表
	 * @return 证书链列表
	 */
	public List<NetoneCertificate> getCertList() {
		return certList;
	}

	/**
	 * 获取签名者证书
	 * @return  {@link com.syan.netonej.http.entity.NetoneCertificate} 签名者证书
	 */
	public List<NetoneCertificate> getSignedCertificate() {
		return signedCertificates;
	}



}
