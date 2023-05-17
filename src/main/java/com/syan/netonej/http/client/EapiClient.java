/**
 * 文 件 名: EapiClient.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2014-3-20
 */
package com.syan.netonej.http.client;

import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.eapi.CertificateDeleteBuilder;
import com.syan.netonej.http.client.eapi.CertificateListBuilder;
import com.syan.netonej.http.client.eapi.CertificateUploadBuilder;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneResponse;

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

	@Deprecated
	public NetoneResponse uploadCert(String cert,boolean revoked) throws NetonejException{
		return certificateUploadBuilder().setCert(cert).setRevoked(revoked).build();
	}

	@Deprecated
	public NetoneResponse uploadCert(String cert) throws NetonejException {
		return uploadCert(cert,false);
	}

	@Deprecated
	public NetoneResponse deleteCert(String data, String idmagic) throws NetonejException{
		return deleteCert(data,IdMagic.valueOf(idmagic.toUpperCase()));
	}

	public NetoneResponse deleteCert(String data, IdMagic idMagic) throws NetonejException{
		return deleteCert(data,idMagic,false);
	}

	public NetoneResponse deleteCert(String data, IdMagic idMagic,boolean revoked) throws NetonejException{
		return certificateDeleteBuilder().setId(data).setIdMagic(idMagic).setRevoked(revoked).build();
	}

	public NetoneCertList listCertificates() throws NetonejException{
		return listCertificates(false);
	}

	public NetoneCertList listCertificates(boolean revoked) throws NetonejException{
		return certificateListBuilder().setRevoked(revoked).build();
	}

}
