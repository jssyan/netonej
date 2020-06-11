/**
 * 文 件 名:  NetoneEnvelope.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述: 数字信封封包
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.http.entity;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1Set;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.cms.ContentInfo;
import org.spongycastle.asn1.cms.EncryptedContentInfo;
import org.spongycastle.asn1.cms.IssuerAndSerialNumber;
import org.spongycastle.asn1.cms.KeyTransRecipientInfo;
import org.spongycastle.asn1.cms.RecipientInfo;
import org.spongycastle.asn1.cms.SignerInfo;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;



/**
 * 数字信封封包
 * @author  liyb
 * @version  2.0.0
 * @since  2.0.0
 */
public class NetoneEnvelope extends NetonePCS {
	private static Map<String, String> oidMap =new HashMap<String, String>();
	static{
		oidMap.put("2.5.4.45", "x500uniqueidentifier");
		oidMap.put("2.5.4.45.17", "x500uniqueidentifier");
	}
	/**
	 * 签名并封包标识符
	 */
	private final String signedAndEnveloped="1.2.840.113549.1.7.4";
	/**
	 * 版本号
	 */
	private ASN1Integer version;   
    /**
     * 接收人信息
     */
    private KeyTransRecipientInfo recipientInfo;   
    /**
     * 摘要算法
     */
    private AlgorithmIdentifier digestAlgorithm;   
    /**
     * 加密数据
     */
    private EncryptedContentInfo encryptedContentInfo;   
    
    /**
     * 签名者信息
     */
    private SignerInfo signerInfo;
    
    /**
     * 接收人序列号
     */
    private String recipientSerialNumber ;
    /**
     * 接收人主题
     */
    private String recipientSubject ;
    /**
     * 签名者证书
     */
    private NetoneCertificate signerCertificate;    
    /**
     * 签名者序列号
     */
    private String signerSerialNumber ;
    
    /**
     * 签名者主题
     */
    private String signerSubject ;
    

	/**
	 * @return 摘要算法
	 */
	public AlgorithmIdentifier getDigestAlgorithm() {
		return digestAlgorithm;
	}

	/**
	 * @param digestAlgorithm 摘要算法
	 */
	public void setDigestAlgorithm(AlgorithmIdentifier digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
	}


	/**
	 * @param response 
	 * @throws CertificateException
	 * @throws IOException
	 */
	public NetoneEnvelope(NetoneResponse response) throws CertificateException, IOException   
    {   super(response);
    	
    	ASN1Sequence seq=ASN1Sequence.getInstance(Base64.decodeBase64(response.getRetString()));
		ContentInfo ci=ContentInfo.getInstance(seq);
		Enumeration  enumeration=null;		
		
		if(signedAndEnveloped.equals(ci.getContentType().toString())){
			seq=ASN1Sequence.getInstance(ci.getContent());	
		
	        Enumeration e = seq.getObjects();   
	     
	        version = (ASN1Integer)e.nextElement();   
	      	        
	     
	        //接收者信息
	        enumeration=  ((ASN1Set)e.nextElement()).getObjects(); 
	    	while(enumeration.hasMoreElements()){
				RecipientInfo rinfo=RecipientInfo.getInstance((ASN1Sequence)enumeration.nextElement());
					
				this.recipientInfo=(KeyTransRecipientInfo)rinfo.getInfo();
				IssuerAndSerialNumber sssuerSerial=IssuerAndSerialNumber.getInstance(recipientInfo.getRecipientIdentifier().getId());
        		X500Principal x500Principal=new X500Principal(sssuerSerial.getName().getEncoded());	
        		this.recipientSerialNumber=sssuerSerial.getSerialNumber().toString();
        		this.recipientSubject =x500Principal.getName(X500Principal.RFC2253,oidMap);
        		
	    	}
	    	
	    	//digestAlgorithms
	    	enumeration=  ((ASN1Set)e.nextElement()).getObjects(); 
	    	while(enumeration.hasMoreElements()){
	    			digestAlgorithm=AlgorithmIdentifier.getInstance((ASN1Sequence)enumeration.nextElement());
	      	}
	    	
	    	 encryptedContentInfo = EncryptedContentInfo.getInstance(e.nextElement());   
	    	 	           
        do   
        {   
            while(e.hasMoreElements())    
            {   
                Object o =e.nextElement();   
             
                if(o instanceof DERTaggedObject)   
                {   
                    DERTaggedObject tagged = (DERTaggedObject)o;   
                   
                    switch(tagged.getTagNo())   
                    {   
                    case 0: // '\0'    
                    	//接收人证书                    	 
                    	enumeration = ASN1Set.getInstance(tagged, false).getObjects();   
                    	while(enumeration.hasMoreElements()){                    		
                			ASN1Sequence s=	(ASN1Sequence)enumeration.nextElement();                			
                			signerCertificate=new NetoneCertificate(s.getEncoded());               			               			
                		}
                        break;   
   
                    case 1: // '\001'    
                    	
                    	//无证书链数据 暂不处理
//                        crls = ASN1Set.getInstance(tagged, false);   
                        break;   
   
                    default:   
                        throw new IllegalArgumentException("unknown tag value ".concat(String.valueOf(String.valueOf(tagged.getTagNo()))));   
                    }   
                } else   
                {   //签名者信息
                	ASN1Set  signerInfos = (ASN1Set)o;   
                	enumeration=  signerInfos.getObjects();
                	while(enumeration.hasMoreElements()){
                		signerInfo=SignerInfo.getInstance((ASN1Sequence)enumeration.nextElement());
                   		IssuerAndSerialNumber sssuerSerial=IssuerAndSerialNumber.getInstance(signerInfo.getSID().getId());
                		X500Principal x500Principal=new X500Principal(sssuerSerial.getName().getEncoded());	
                		this.signerSerialNumber=sssuerSerial.getSerialNumber().toString();
                		this.signerSubject =x500Principal.getName(X500Principal.RFC2253,oidMap);
        			}
                }   
            }   
            return;   
        } while(true);   
	}
	}   
     

	/** 签名者证书
	 * @return  {@link com.syan.netonej.http.entity.NetoneCertificate} 签名者证书
	 */
	public NetoneCertificate getSignerCertificate() {
		return signerCertificate;
	}

	/**
	 * 获取接收者序列号
	 * @return 接收者证书序列号
	 */
	public String getRecipientSerialNumber() {
		return recipientSerialNumber;
	}


	/**
	 * 获取接收者主题项
	 * @return 接收者主题项
	 */
	public String getRecipientSubject() {
		return recipientSubject;
	}


	/**
	 * 获取签名者证书序列号
	 * @return 签名者证书序列号
	 */
	public String getSignerSerialNumber() {
		return signerSerialNumber;
	}

	/**
	 * 获取签名者主题项
	 * @return 签名者主题项
	 */
	public String getSignerSubject() {
		return signerSubject;
	}
	
}
