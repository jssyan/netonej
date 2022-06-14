/**
 * 文 件 名: EapiClient.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2014-3-20
 */
package com.syan.netonej.http.client;

import com.syan.netonej.http.client.eapi.CertificateDeleteBuilder;
import com.syan.netonej.http.client.eapi.CertificateListBuilder;
import com.syan.netonej.http.client.eapi.CertificateUploadBuilder;

public final class EapiClient{
	protected String host;

	protected String port = "9108";

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

	public EapiClient() {
	}

	public EapiClient(String host) {
		this.host = host;
	}
	public EapiClient(String host, String port) {
		this.host = host;
		this.port = port;
	}

	public CertificateListBuilder certificateListBuilder(){
		return new CertificateListBuilder().setHost(host).setPort(port);
	}

	public CertificateUploadBuilder certificateUploadBuilder(){
		return new CertificateUploadBuilder().setHost(host).setPort(port);
	}

	public CertificateDeleteBuilder certificateDeleteBuilder(){
		return new CertificateDeleteBuilder().setHost(host).setPort(port);
	}

}
