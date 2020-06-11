/**
 * 文 件 名: EapiClient.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2014-3-20
 */
package com.syan.netonej.http.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.HttpPostProcesser;
import com.syan.netonej.http.HttpPostProcesserFactory;
import com.syan.netonej.http.HttpStatus;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.xml.XmlparserFacotry;

public class EapiClient extends BaseClient {
	
	
	private static final Log log = LogFactory.getLog(EapiClient.class);
	/**
	 * EAPI 服务IP
	 */
	private  String _host;
	/**
	 * EAPI 服务PORT
	 */
	private  String _port;
	
	private  String EAPI_ACTION="eapi.php";
	/**
	 * Parameters - ID类型键值  "idmagic"
	 */
	private final static String PARAME_IDMAGIC = "idmagic";	
	/**
	 * 根据action号选择返回对应的服务URL
	 * @param action 业务类型号
	 * @return 完整服务URL
	 */
	@Override
	protected String getServiceUrl(String action){
		StringBuffer uri = new StringBuffer("https://"+getHostIp()+":"+getPort()+"/"+action);		
		return uri.toString();
	}
	
	public EapiClient(String host,String port) {
		super(HttpPostProcesserFactory.createPostProcesser());
		this._host = host;
		this._port = port;
	}
	

	public NetoneResponse uploadCert(String base64_cert) throws NetonejExcepption{
		
		NetoneResponse response=null;
		try {	
		
			Map<String,String> params = new HashMap<String,String>();		
			
			params.put("module", "pki");
			params.put("action", "addtrusted");
			params.put("data", base64_cert);				
			response=processer.doPost(getServiceUrl(EAPI_ACTION), params);			
		} catch (Exception e) {		
			log.error("-EAPI（uploadCert）上传证书失败", e);
			e.printStackTrace();
			throw new NetonejExcepption("-EAPI（uploadCert）上传证书失败"+ e,e);
		}
		return response;			
	}
	
	/**
	 * 
	 * @param data
	 * @param idmagic scn,id,snhex,sndec,tnsha1
	 * @return
	 * @throws NetonejExcepption
	 */
public NetoneResponse deleteCert(String data,String idmagic) throws NetonejExcepption{
		
		NetoneResponse response=null;
		try {	
		
			Map<String,String> params = new HashMap<String,String>();		
			params.put("module", "pki");
			params.put("action", "deltrusted");
			params.put(PARAME_IDMAGIC, idmagic);		
			params.put("data", data);				
			response=processer.doPost(getServiceUrl(EAPI_ACTION), params);			
		} catch (Exception e) {		
			log.error("-EAPI（uploadCert）上传证书失败", e);
			e.printStackTrace();
			throw new NetonejExcepption("-EAPI（deleteCert）删除证书失败"+ e,e);
		}
		return response;			
	}

	/** 枚举服务端证书列表
	 * @return NetoneCertList  服务端证书列表
	 * @throws NetonejExcepption
	 */
	public NetoneCertList listCertificates() throws NetonejExcepption{	
		
		log.debug("-EAPI（listCertificates）");
		
		NetoneCertList list = null;
		Map<String, String> params = new HashMap<String, String>();		
		params.put(RESPONSE_FORMAT_KEY,responseformat);		
		params.put("module", "pki");
		params.put("action", "listtrusted");
		
		try{
			 NetoneResponse response=processer.doPost(getServiceUrl(EAPI_ACTION), params);	
			 list= new NetoneCertList(response.getStatusCode());
			 if(response.getStatusCode()==HttpStatus.SC_OK){				
			 	list.setCertList((List<NetoneCertificate>)XmlparserFacotry.parseXmlString(response.getRetString()));
			 }
		}catch(Exception e){
			
			log.error("-EAPI（listtrusted）枚举服务端的证书", e);
			
			throw new NetonejExcepption("-EAPI（listtrusted）枚举服务端的证书"+ e,e);
		}
		return list;
	}
	
	@Override
	public String getHostIp() {
		return this._host;
	}

	@Override
	public String getPort() {
		return this._port;
	}

}
