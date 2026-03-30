
package com.syan.netonej.http.client;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.pcs.*;
import com.syan.netonej.http.entity.*;
import org.bouncycastle.util.encoders.Base64;
import com.syan.netonej.http.client.CcgwClient;

public class PCSClient{

    protected String host;

    protected String port = "9178";

    protected CcgwClient ccgwClient;

    public PCSClient(CcgwClient ccgwClient) {
        this.ccgwClient = ccgwClient;
        this.host = ccgwClient.getHost();
        this.port = ccgwClient.getPort();
    }

    public PCSClient(String host) {
        this.host = host;
    }

    public PCSClient(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public KeyBuilder keyBuilder(){
        return new KeyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public CertificateBuilder certificateBuilder(){
        return new CertificateBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public EnvelopePacketBuilder envelopePacketBuilder(){
        return new EnvelopePacketBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public EnvelopeUnpackBuilder envelopeUnpackBuilder(){
        return new EnvelopeUnpackBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public PinBuilder pinBuilder(){
        return new PinBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public PKCS1Builder pkcs1Builder(){
        return new PKCS1Builder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public RandomBuilder randomBuilder(){
        return new RandomBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public PKCS7Builder pkcs7Builder(){
        return new PKCS7Builder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public PrivateKeyBuilder privateKeyBuilder(){
        return new PrivateKeyBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public PublicKeyBuilder publicKeyBuilder(){
        return new PublicKeyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public XmlSignBuilder xmlSignBuilder(){
        return new XmlSignBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public FileSignBuilder fileSignBuilder(){
        return new FileSignBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public CMACBuilder cmacBuilder(){
        return new CMACBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }
}
