
package com.syan.netonej.http.client;

import com.syan.netonej.http.client.pcs.*;

public class PCSClient{

    protected String host;

    protected String port = "9178";

    public PCSClient() {
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
        return new KeyBuilder().setHost(host).setPort(port);
    }

    public CertificateBuilder certificateBuilder(){
        return new CertificateBuilder().setHost(host).setPort(port);
    }

//    public CMACBuilder cmacBuilder(){
//        return new CMACBuilder().setHost(host).setPort(port);
//    }

    public EnvelopePacketBuilder envelopePacketBuilder(){
        return new EnvelopePacketBuilder().setHost(host).setPort(port);
    }

    public EnvelopeUnpackBuilder envelopeUnpackBuilder(){
        return new EnvelopeUnpackBuilder().setHost(host).setPort(port);
    }

    public PinBuilder pinBuilder(){
        return new PinBuilder().setHost(host).setPort(port);
    }

    public PKCS1Builder pkcs1Builder(){
        return new PKCS1Builder().setHost(host).setPort(port);
    }

    public PKCS7Builder pkcs7Builder(){
        return new PKCS7Builder().setHost(host).setPort(port);
    }

    public PrivateKeyBuilder privateKeyBuilder(){
        return new PrivateKeyBuilder().setHost(host).setPort(port);
    }

    public PublicKeyBuilder publicKeyBuilder(){
        return new PublicKeyBuilder().setHost(host).setPort(port);
    }

    public XmlSignBuilder xmlSignBuilder(){
        return new XmlSignBuilder().setHost(host).setPort(port);
    }

    public FileSignBuilder fileSignBuilder(){
        return new FileSignBuilder().setHost(host).setPort(port);
    }

}
