/*
 * Project: UniraSDK
 * 
 * @(#) SM2Certificate.java   14-6-25 上午10:45
 *
 * Copyright 2013 Jiangsu Syan Technology Co.,Ltd. All rights reserved.
 * Jiangsu Syan PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.syan.netonej.common.x509;


import com.syan.netonej.common.key.SM2BCPublicKey;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.misc.MiscObjectIdentifiers;
import org.spongycastle.asn1.misc.NetscapeCertType;
import org.spongycastle.asn1.misc.NetscapeRevocationURL;
import org.spongycastle.asn1.misc.VerisignCzagExtension;
import org.spongycastle.asn1.util.ASN1Dump;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.asn1.x509.Extension;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.jce.X509Principal;
import org.spongycastle.jce.interfaces.ECPublicKey;
import org.spongycastle.util.encoders.Hex;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.*;

/**
 * <p>
 * This class provides...
 * </p>
 *
 * @author Iceberg
 * @version $Revision $Date:14-6-25 上午10:45
 * @since 1.0
 */
public class SM2X509Certificate extends X509Certificate {
    private Certificate c;
    private BasicConstraints basicConstraints;
    private boolean[] keyUsage;
    private boolean hashValueSet;
    private int hashValue;

    public SM2X509Certificate(Certificate c) throws CertificateParsingException {
        this.c = c;

        try {
            byte[] bytes = this.getExtensionBytes("2.5.29.19");

            if (bytes != null) {
                basicConstraints = BasicConstraints.getInstance(ASN1Sequence.fromByteArray(bytes));
            }
        } catch (Exception e) {
            throw new CertificateParsingException(
                    "cannot construct BasicConstraints: " + e);
        }

        try {
            byte[] bytes = this.getExtensionBytes("2.5.29.15");
            if (bytes != null) {
                DERBitString bits = DERBitString.getInstance(ASN1Sequence.fromByteArray(bytes));
                bytes = bits.getBytes();
                int length = (bytes.length * 8) - bits.getPadBits();

                keyUsage = new boolean[(length < 9) ? 9 : length];

                for (int i = 0; i != length; i++) {
                    keyUsage[i] = (bytes[i / 8] & (0x80 >>> (i % 8))) != 0;
                }
            } else {
                keyUsage = null;
            }
        } catch (Exception e) {
            throw new CertificateParsingException("cannot construct KeyUsage: " + e);
        }
    }

    /**
     * 将证书的base64值进行解码，构造成ecc证书
     *
     * @param bytes
     * @return
     * @throws java.io.IOException
     * @throws java.security.cert.CertificateParsingException
     *
     */
    public static SM2X509Certificate decode(byte[] bytes) throws IOException, CertificateParsingException {
        X509CertificateHolder holder = null;
        SM2X509Certificate cert = null;

        try {
            holder = new X509CertificateHolder(bytes);
            cert = new SM2X509Certificate(holder.toASN1Structure());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException(e.getMessage());
        }
        return cert;
    }

    /**
     * 将证书的base64值进行解码，构造成ecc证书
     *
     * @param cert
     * @return
     * @throws java.security.cert.CertificateParsingException
     *
     * @throws java.io.IOException
     */
    public static SM2X509Certificate decode(String cert) throws CertificateParsingException, IOException {
        return decode(cert.getBytes());
    }

    public void checkValidity() throws CertificateExpiredException,
            CertificateNotYetValidException {
        this.checkValidity(new Date());
    }

    public void checkValidity(Date date) throws CertificateExpiredException,
            CertificateNotYetValidException {
        if (date.getTime() > this.getNotAfter().getTime()) // for other VM
        // compatibility
        {
            throw new CertificateExpiredException("certificate expired on "
                    + c.getEndDate().getTime());
        }

        if (date.getTime() < this.getNotBefore().getTime()) {
            throw new CertificateNotYetValidException(
                    "certificate not valid till " + c.getStartDate().getTime());
        }
    }

    public int getVersion() {
        return c.getVersion().getValue().intValue();
    }

    public BigInteger getSerialNumber() {
        return c.getSerialNumber().getValue();
    }

    public Principal getIssuerDN() {

        return new X509Principal(c.getIssuer());
    }

    public X500Principal getIssuerX500Principal() {
        try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ASN1OutputStream aOut = new ASN1OutputStream(bOut);

            aOut.writeObject(c.getIssuer());

            return new X500Principal(bOut.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }

    public Principal getSubjectDN() {
        return new X509Principal(c.getSubject());
    }

    public X500Principal getSubjectX500Principal() {
        try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ASN1OutputStream aOut = new ASN1OutputStream(bOut);

            aOut.writeObject(c.getSubject());

            return new X500Principal(bOut.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("can't encode subject DN");
        }
    }

    public Date getNotBefore() {
        return c.getStartDate().getDate();
    }

    public Date getNotAfter() {
        return c.getEndDate().getDate();
    }

    public byte[] getTBSCertificate() throws CertificateEncodingException {
        try {
            return c.getTBSCertificate().getEncoded("DER");
        } catch (IOException e) {
            throw new CertificateEncodingException(e.toString());
        }
    }

    public byte[] getSignature() {
        return c.getSignature().getBytes();
    }

    /**
     * return a more "meaningful" representation for the signature algorithm
     * used in the certficate.
     */
    public String getSigAlgName() {
        return this.getSigAlgOID();
    }

    /**
     * return the object identifier for the signature.
     */
    public String getSigAlgOID() {
        return c.getSignatureAlgorithm().getAlgorithm().getId();
    }

    /**
     * return the signature parameters, or null if there aren't any.
     */
    public byte[] getSigAlgParams() {
        if (c.getSignatureAlgorithm().getParameters() != null) {
            try {
                return c.getSignatureAlgorithm().getParameters().toASN1Primitive().getEncoded();
            } catch (IOException e) {
                throw new IllegalStateException("can't get sig algo params");
            }
        } else {
            return null;
        }
    }

    public boolean[] getIssuerUniqueID() {
        DERBitString id = c.getTBSCertificate().getIssuerUniqueId();

        if (id != null) {
            byte[] bytes = id.getBytes();
            boolean[] boolId = new boolean[bytes.length * 8 - id.getPadBits()];

            for (int i = 0; i != boolId.length; i++) {
                boolId[i] = (bytes[i / 8] & (0x80 >>> (i % 8))) != 0;
            }

            return boolId;
        }

        return null;
    }

    public boolean[] getSubjectUniqueID() {
        DERBitString id = c.getTBSCertificate().getSubjectUniqueId();

        if (id != null) {
            byte[] bytes = id.getBytes();
            boolean[] boolId = new boolean[bytes.length * 8 - id.getPadBits()];

            for (int i = 0; i != boolId.length; i++) {
                boolId[i] = (bytes[i / 8] & (0x80 >>> (i % 8))) != 0;
            }

            return boolId;
        }

        return null;
    }

    public boolean[] getKeyUsage() {
        return keyUsage;
    }

    public List getExtendedKeyUsage() throws CertificateParsingException {
        byte[] bytes = this.getExtensionBytes("2.5.29.37");

        if (bytes != null) {
            try {
                ASN1InputStream dIn = new ASN1InputStream(bytes);
                ASN1Sequence seq = (ASN1Sequence) dIn.readObject();
                List list = new ArrayList();

                for (int i = 0; i != seq.size(); i++) {
                    list.add(((DERObjectIdentifier) seq.getObjectAt(i)).getId());
                }

                return Collections.unmodifiableList(list);
            } catch (Exception e) {
                throw new CertificateParsingException("error processing extended key usage extension");
            }
        }

        return null;
    }

    public int getBasicConstraints() {
        if (basicConstraints != null) {
            if (basicConstraints.isCA()) {
                if (basicConstraints.getPathLenConstraint() == null) {
                    return Integer.MAX_VALUE;
                } else {
                    return basicConstraints.getPathLenConstraint().intValue();
                }
            } else {
                return -1;
            }
        }

        return -1;
    }

    public Set getCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            Set set = new HashSet();
            Extensions extensions = c.getTBSCertificate().getExtensions();

            if (extensions != null) {
                Enumeration e = extensions.oids();

                while (e.hasMoreElements()) {
                    DERObjectIdentifier oid = (DERObjectIdentifier) e
                            .nextElement();
                    Extension ext = extensions.getExtension(new ASN1ObjectIdentifier(oid.getId()));

                    if (ext.isCritical()) {
                        set.add(oid.getId());
                    }
                }

                return set;
            }
        }

        return null;
    }

    private byte[] getExtensionBytes(String oid) {
        Extensions exts = c.getTBSCertificate().getExtensions();

        if (exts != null) {
            Extension ext = exts.getExtension(new ASN1ObjectIdentifier(oid));
            if (ext != null) {
//                return ext.getValue().getOctets();
                return ext.getExtnValue().getOctets();
            }
        }

        return null;
    }

    public byte[] getExtensionValue(String oid) {
        Extensions exts = c.getTBSCertificate().getExtensions();

        if (exts != null) {
            Extension ext = exts.getExtension(new ASN1ObjectIdentifier(oid));

            if (ext != null) {
                try {
                    return ext.getEncoded();
                } catch (Exception e) {
                    throw new IllegalStateException("error parsing "
                            + e.toString());
                }
            }
        }

        return null;
    }

    public Set getNonCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            Set set = new HashSet();
            Extensions extensions = c.getTBSCertificate().getExtensions();

            if (extensions != null) {
                Enumeration e = extensions.oids();

                while (e.hasMoreElements()) {
                    DERObjectIdentifier oid = (DERObjectIdentifier) e
                            .nextElement();
                    Extension ext = extensions.getExtension(new ASN1ObjectIdentifier(oid.getId()));

                    if (!ext.isCritical()) {
                        set.add(oid.getId());
                    }
                }

                return set;
            }
        }

        return null;
    }

    public PublicKey getPublicKey() {
        PublicKey pub = null;

        SubjectPublicKeyInfo keyInfo = c.getSubjectPublicKeyInfo();
        DERBitString pubKeyData = keyInfo.getPublicKeyData();
        try {
            pub = new SM2BCPublicKey(getExactDERBitStringBytes(pubKeyData));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return pub;
    }

    /**
     * 获取真实的pubKey data，如果是4 必须作为第一位加入，如果是0，不加入byts,如果是1-7.暂时没遇到，待考证 by Wangjx
     * 2011-10-31 21:45
     * <p/>
     * 注意：存在加密机导出的公钥是65位，即04|x|y， 但证书签发后BC可能做过处理，将x多加了一位，目前发现加了-1，即变成 04|-1|x|y
     * 共66位的现象 此问题需要查BC的源码，临时解决方案为，如果是66位的，直接去掉第二位。by Wangjx 2011-11-04 17:10
     *
     * @param pubKeyData
     * @return
     * @note DERBitString的getBytes，为处理前的值，其第一位应该是04，即标记公钥是否压缩。从第二位取32位为x，32位之后为y，
     * 取完x后，剩余的补齐32位作为y
     */
    private byte[] getExactDERBitStringBytes(DERBitString pubKeyData) {
        byte[] rBytes = new byte[65];
        rBytes[0] = 4;
        byte[] bitBytes = pubKeyData.getBytes();
        int length = bitBytes.length;

        if (length == 65) {
            System.arraycopy(bitBytes, 1, rBytes, 1, 32);
            System.arraycopy(bitBytes, 33, rBytes, 33, length - 33);
        } else if (length == 66) {// 判断-1的位置
            byte x = bitBytes[33];
            byte x2 = bitBytes[34];
            if (bitBytes[33] == -1) {// Y 第一位补-1，
                System.arraycopy(bitBytes, 1, rBytes, 1, 32);
                System.arraycopy(bitBytes, 34, rBytes, 33, length - 34);
            } else {// X 第一位补-1
                System.arraycopy(bitBytes, 2, rBytes, 1, 32);
                System.arraycopy(bitBytes, 34, rBytes, 33, length - 34);
            }
        } else {// 67位
            System.arraycopy(bitBytes, 2, rBytes, 1, 32);
            System.arraycopy(bitBytes, 35, rBytes, 33, length - 34);
        }

        return rBytes;
    }

    public byte[] getEncoded() throws CertificateEncodingException {
        try {
            return c.getEncoded("DER");
        } catch (IOException e) {
            throw new CertificateEncodingException(e.toString());
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Certificate)) {
            return false;
        }

        Certificate other = (Certificate) o;

        try {
            byte[] b1 = this.getEncoded();
            byte[] b2 = other.getEncoded();

            return Arrays.equals(b1, b2);
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized int hashCode() {
        if (!hashValueSet) {
            hashValue = calculateHashCode();
            hashValueSet = true;
        }

        return hashValue;
    }

    private int calculateHashCode() {
        try {
            int hashCode = 0;
            byte[] certData = this.getEncoded();
            for (int i = 1; i < certData.length; i++) {
                hashCode += certData[i] * i;
            }
            return hashCode;
        } catch (CertificateEncodingException e) {
            return 0;
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String nl = System.getProperty("line.separator");

        buf.append("  [0]         Version: ").append(this.getVersion()).append(
                nl);
        buf.append("         SerialNumber: ").append(this.getSerialNumber())
                .append(nl);
        buf.append("             IssuerDN: ").append(this.getIssuerDN())
                .append(nl);
        buf.append("           Start Date: ").append(this.getNotBefore())
                .append(nl);
        buf.append("           Final Date: ").append(this.getNotAfter())
                .append(nl);
        buf.append("            SubjectDN: ").append(this.getSubjectDN())
                .append(nl);
        buf.append("           Public Key: ").append(this.getPublicKey())
                .append(nl);
        buf.append("  Signature Algorithm: ").append(this.getSigAlgName())
                .append(nl);

        byte[] sig = this.getSignature();

        buf.append("            Signature: ").append(
                new String(Hex.encode(sig, 0, 20))).append(nl);
        for (int i = 20; i < sig.length; i += 20) {
            if (i < sig.length - 20) {
                buf.append("                       ").append(
                        new String(Hex.encode(sig, i, 20))).append(nl);
            } else {
                buf.append("                       ").append(
                        new String(Hex.encode(sig, i, sig.length - i))).append(
                        nl);
            }
        }

        Extensions extensions = c.getTBSCertificate().getExtensions();

        if (extensions != null) {
            Enumeration e = extensions.oids();

            if (e.hasMoreElements()) {
                buf.append("       Extensions: \n");
            }

            while (e.hasMoreElements()) {
                DERObjectIdentifier oid = (DERObjectIdentifier) e.nextElement();
                Extension ext = extensions.getExtension(new ASN1ObjectIdentifier(oid.getId()));

                if (ext.getExtnValue() != null) {
                    byte[] octs = ext.getExtnValue().getOctets();
                    ASN1InputStream dIn = new ASN1InputStream(octs);
                    buf.append("                       critical(").append(
                            ext.isCritical()).append(") ");
                    try {
                        if (oid.equals(Extension.basicConstraints)) {
                            buf.append(BasicConstraints.getInstance((ASN1Sequence) dIn.readObject())).append(nl);
                        } else if (oid.equals(Extension.keyUsage)) {
                            buf.append(KeyUsage.getInstance((DERBitString) dIn.readObject())).append(nl);
                        } else if (oid.equals(MiscObjectIdentifiers.netscapeCertType)) {
                            buf.append(
                                    new NetscapeCertType((DERBitString) dIn.readObject())).append(nl);
                        } else if (oid
                                .equals(MiscObjectIdentifiers.netscapeRevocationURL)) {
                            buf.append(
                                    new NetscapeRevocationURL((DERIA5String) dIn.readObject())).append(nl);
                        } else if (oid
                                .equals(MiscObjectIdentifiers.verisignCzagExtension)) {
                            buf.append(
                                    new VerisignCzagExtension(
                                            (DERIA5String) dIn.readObject()))
                                    .append(nl);
                        } else {
                            buf.append(oid.getId());
                            buf.append(" value = ").append(
                                    ASN1Dump.dumpAsString(dIn.readObject()))
                                    .append(nl);
                            // buf.append(" value = ").append("*****").append(nl);
                        }
                    } catch (Exception ex) {
                        buf.append(oid.getId());
                        buf.append(" value = ").append("*****").append(nl);
                    }
                } else {
                    buf.append(nl);
                }
            }
        }

        return buf.toString();
    }

    /**
     * 验证签名
     */
    public final void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {

    }

    public final void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {

    }

    private void checkSignature(PublicKey key, Signature signature) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        /*
       if (!isAlgIdEqual(c.getSignatureAlgorithm(), c.getTBSCertificate()
                .getSignature())) {
            throw new CertificateException(
                    "signature algorithm in TBS cert not same as outer cert");
        }

        ASN1Encodable params = c.getSignatureAlgorithm().getParameters();

        // TODO This should go after the initVerify?
        X509SignatureUtil.setSignatureParameters(signature, params);

        signature.initVerify(key);

        signature.update(this.getTBSCertificate());

        if (!signature.verify(this.getSignature())) {
            throw new InvalidKeyException(
                    "Public key presented not for certificate signature");
        }

          */

    }

    private boolean isAlgIdEqual(AlgorithmIdentifier id1, AlgorithmIdentifier id2) {
        if (!id1.getObjectId().equals(id2.getObjectId())) {
            return false;
        }

        if (id1.getParameters() == null) {
            if (id2.getParameters() != null
                    && !id2.getParameters().equals(DERNull.INSTANCE)) {
                return false;
            }

            return true;
        }

        if (id2.getParameters() == null) {
            if (id1.getParameters() != null
                    && !id1.getParameters().equals(DERNull.INSTANCE)) {
                return false;
            }

            return true;
        }

        return id1.getParameters().equals(id2.getParameters());
    }

    public boolean hasUnsupportedCriticalExtension() {
        return false;
    }
}
