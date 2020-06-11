/*
 * Project: bcpkix-jdk15on-149
 *
 * @(#) CMSSignedDataUtil.java   2015/4/3 11:24
 *
 * Copyright 2013 Jiangsu Syan Technology Co.,Ltd. All rights reserved.
 * Jiangsu Syan PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.syan.netonej.common;


import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1Set;
import org.spongycastle.asn1.cms.ContentInfo;
import org.spongycastle.asn1.cms.IssuerAndSerialNumber;
import org.spongycastle.asn1.cms.SignedData;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cms.CMSException;
import org.spongycastle.cms.CMSSignedData;
import org.spongycastle.util.encoders.Base64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 * This class provides...
 * </p>
 *
 * @author Iceberg
 * @version $Revision $Date:2015/4/3 11:24
 * @since 1.0
 */
public class CMSSignedDataUtil {

    private ContentInfo contentInfo;
    private SignedData signedData;

    public CMSSignedDataUtil(byte[] cmsSignedData) throws CMSException {
        contentInfo = ContentInfo.getInstance(ASN1Sequence.getInstance(cmsSignedData));
        signedData = SignedData.getInstance(contentInfo.getContent());
    }

    /**
     * 获得签名证书的颁发者和序列号
     *
     * @return 颁发者和序列号
     */
    public List<IssuerAndSerialNumber> getSignerIssuerAndSerialNumber() {
        ASN1Set signerInfos = signedData.getSignerInfos();
        List<IssuerAndSerialNumber> signers = new ArrayList<IssuerAndSerialNumber>();

        int size = signerInfos.size();
        ASN1Sequence signerInfo;
        IssuerAndSerialNumber issuerAndSerialNumber;
        for (int i = 0; i < size; i++) {
            signerInfo = (ASN1Sequence) signerInfos.getObjectAt(i);
            issuerAndSerialNumber = IssuerAndSerialNumber.getInstance(signerInfo.getObjectAt(1));
            signers.add(issuerAndSerialNumber);
        }

        return signers;
    }

    /**
     * 获取PKCS7签名中的证书库中的证书
     *
     * @return 证书列表
     * @throws IOException
     */
    public List<X509CertificateHolder> getCertificates() throws IOException {
        List localArrayList = new ArrayList<X509CertificateHolder>();
        ASN1Set localASN1Set = this.signedData.getCertificates();
        if (localASN1Set != null) {
            Enumeration localEnumeration = localASN1Set.getObjects();
            while (localEnumeration.hasMoreElements()) {
                ASN1Encodable localASN1Primitive = ((ASN1Encodable) localEnumeration.nextElement());
                if ((localASN1Primitive instanceof ASN1Sequence)) {
                    localArrayList.add(new X509CertificateHolder(((ASN1Sequence) localASN1Primitive).getEncoded()));
                }
            }
        }

        return localArrayList;
    }


    /**
     * 获得PKCS7中的签名证书
     *
     * @param issuerAndSerialNumber 颁发者和序列号
     * @return 签名证书
     * @throws IOException
     */
    public X509CertificateHolder getSignerCert(IssuerAndSerialNumber issuerAndSerialNumber) throws IOException {
        X509CertificateHolder signer = null;

        String issuer = issuerAndSerialNumber.getName().toString();
        String serial = issuerAndSerialNumber.getSerialNumber().toString();
        List<X509CertificateHolder> store = getCertificates();

        for (X509CertificateHolder holder : store) {
            if (holder.getSerialNumber().toString().equals(serial) && holder.getIssuer().toString().equals(issuer)) {
                signer = holder;
                break;
            }
        }

        return signer;
    }


}
