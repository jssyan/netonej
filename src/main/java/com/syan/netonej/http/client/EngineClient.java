package com.syan.netonej.http.client;

import com.syan.netonej.http.client.engine.*;


public class EngineClient {

    protected String host;

    protected String port = "9158";

    protected CcgwClient ccgwClient;

    public EngineClient(CcgwClient ccgwClient) {
        this.ccgwClient = ccgwClient;
        this.host = ccgwClient.getHost();
        this.port = ccgwClient.getPort();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public CcgwClient getCcgwClient() {
        return ccgwClient;
    }

    public void setCcgwClient(CcgwClient ccgwClient) {
        this.ccgwClient = ccgwClient;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public RandBuilder randBuilder(){
        return new RandBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public EncryptBuilder encryptBuilder(){
        return new EncryptBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public DecryptBuilder decryptBuilder(){
        return new DecryptBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public HmacBuilder hmacBuilder(){
        return new HmacBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public CmacBuilder cmacBuilder(){
        return new CmacBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public SignBuilder signBuilder(){
        return new SignBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public VerifyBuilder verifyBuilder(){
        return new VerifyBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public AsyEncryptBuilder asyEncryptBuilder(){
        return new AsyEncryptBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }

    public AsyDecryptBuilder asyDecryptBuilder(){
        return new AsyDecryptBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public PublicKeyBuilder publicKeyBuilder(){
        return new PublicKeyBuilder().setHost(host).setPort(port).setCcgwClient( ccgwClient );
    }
}
