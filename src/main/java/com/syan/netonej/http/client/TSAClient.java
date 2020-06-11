/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.syan.netonej.common.Conventer;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.sm3.SM3Digest;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.HttpPostProcesserFactory;
import com.syan.netonej.http.HttpStatus;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.entity.NetoneTSA;



/**
* NetONE TSA API
* @author  gejq
* @version  2.0.0
* @since  1.0.0
 */
public class TSAClient extends BaseClient{
	private static final Log log = LogFactory.getLog(TSAClient.class);
	
	/**
	 * TSA - Action 创建时间戳
	 */
	private final static String TSA_ACTION_TSAC = "tsac.svr";
	/**
	 * TSA - Action 验证时间戳
	 */
	private final static String TSA_ACTION_TSAV = "tsav.svr";
	
	/**
	 * Parameters - 原文数据摘要键值(TSA专用)  "digest"
	 */
	private final static String PARAME_DIGEST= "digest";
	/**
	 * Parameters - 时间戳签名键值(TSA专用)  "tsr"
	 */
	private final static String PARAME_TSR= "tsr";
	
	/**
	 * Values - 摘要算法类型(TSA专用)  "sha1"
	 */
	protected final static String TSA_ALOG_SHA1= "sha1";
	protected final static String TSA_ALOG_SM3= "sm3";
	protected final static String TSA_ALOG_MD5= "md5";
	protected final static String TSA_ALOG_SHA256= "sha256";
	
	/**
	 * PCS 服务IP
	 */
	private  String _host;
	
	/**
	 * PCS 服务PORT
	 */
	private  String _port;
	
	
	/**
	 *  TSAClient 初始化
	 * @param host 服务器IP
	 * @param port 服务端口
	 */
	public TSAClient(String host,String port){
		super(HttpPostProcesserFactory.createPostProcesser());
		this._host = host;
		this._port = port;
	}
	
	/**
	 * 创建时间戳签名
	 * @param data 待时间戳签名数据
	 * @param algo 摘要算法（'md5' or 'sha1' or 'sm3'）
	 * @param datatype 待签名数据的类型. 0表示是原文, !0表示摘要(摘要数据是binary格式)
	 * @return 时间戳签名结果
	 * @throws NetonejExcepption API全局异常类
	 */
	public NetoneTSA createTimestamp(byte[] data,String algo,int datatype) throws NetonejExcepption{
		
		log.debug("-TSA（tsac）data："+data+" algo："+algo );
		NetoneTSA   tsa = null;
		try {
			String digest = "";
				if(datatype==0){
				
					if(TSA_ALOG_MD5.equalsIgnoreCase(algo)){
						digest=NetonejUtil.md5Encode(data);
					}else if(TSA_ALOG_SM3.equalsIgnoreCase(algo)){
						SM3Digest sm3=new SM3Digest();
						byte[] dataBytes=data;
						sm3.update(dataBytes, 0, dataBytes.length);
						byte[] out = new byte[32];
						sm3.doFinal(out, 0);
						digest=Conventer.bytesToHexString(out).toUpperCase();
					}else if(TSA_ALOG_SHA1.equalsIgnoreCase(algo)){
						digest=NetonejUtil.sha1Encode(data);
						
					}else if(TSA_ALOG_SHA256.equalsIgnoreCase(algo)){
						digest=NetonejUtil.digestByte(data, "SHA-256");					
					}
				
				}else{
					digest=NetonejUtil.byte2HexString(data);
				}
				Map<String, String> params = new HashMap<String, String>();		
				params.put(PARAME_DIGEST, digest);
				params.put("algo", algo);
				
					
				NetoneResponse response=processer.doPost(getServiceUrl(TSA_ACTION_TSAC), params);
				tsa = new NetoneTSA(response);
		} catch (Exception e) {
			
			log.error("-TSA(tsac)时间戳签发失败",e);
			
			throw new NetonejExcepption("-TSA(tsac)时间戳签发失败"+ e,e);
		}
	
		return tsa;
	}
	
	/**
	 * 创建时间戳签名
	 * @param data 待时间戳签名数据
	 * @param algo 摘要算法（'md5' or 'sha1' or 'sm3'）
	 * @return 时间戳签名结果
	 * @throws NetonejExcepption API全局异常类
	 */
	public NetoneTSA createTimestamp(String data,String algo) throws NetonejExcepption{
		
		log.debug("-TSA（tsac）data："+data+" algo："+algo );
		NetoneTSA   tsa = null;
		try {
				String digest = "";
				if(TSA_ALOG_MD5.equals(algo)){
					digest=NetonejUtil.md5Encode(data);
				}else if(TSA_ALOG_SM3.equals(algo)){
					SM3Digest sm3=new SM3Digest();
					byte[] dataBytes=data.getBytes("utf-8");
					sm3.update(dataBytes, 0, dataBytes.length);
					byte[] out = new byte[32];
					sm3.doFinal(out, 0);
					digest=Conventer.bytesToHexString(out).toUpperCase();
				}else if(TSA_ALOG_SHA1.equals(algo)){
					digest=NetonejUtil.sha1Encode(data);
					
				}else if(TSA_ALOG_SHA256.equals(algo)){
					digest=NetonejUtil.digestString(data, "SHA-256");
					
				}
				
				Map<String, String> params = new HashMap<String, String>();		
				params.put(PARAME_DIGEST, digest);
				params.put("algo", algo);
				
			
			
				NetoneResponse response=processer.doPost(getServiceUrl(TSA_ACTION_TSAC), params);
				tsa = new NetoneTSA(response);
		} catch (Exception e) {			
			log.error("-TSA(tsac)时间戳签发失败",e);			
			throw new NetonejExcepption("-TSA(tsac)时间戳签发失败"+ e,e);
		}
	
		return tsa;
	}
	
	/**
	 * 验证时间戳签名
	 * @param timestamp 时间戳签名数据
	 * @return TSR实体对象类 {@link com.syan.netonej.http.entity.NetoneTSA}
	 * @throws NetonejExcepption API全局异常类
	 */
	public NetoneTSA verifyTimestamp(String timestamp) throws NetonejExcepption{
		
		log.debug("-TSA（tsav）timestamp："+timestamp);
		
		NetoneTSA   tsa = null;
		Map<String, String> params = new HashMap<String, String>();		
		params.put(PARAME_TSR, timestamp);
		
		try {
			NetoneResponse response=processer.doPost(getServiceUrl(TSA_ACTION_TSAV), params);
			if(response.getStatusCode()==HttpStatus.SC_OK){
				//如验签通过直接解析时间戳签名数据
				response.setRetString(timestamp);
			}
			tsa = new NetoneTSA(response);
		} catch (Exception e) {
			
			log.error("-TSA(tsav)时间戳验证失败",e);
			
			throw new NetonejExcepption("-TSA(tsav)时间戳验证失败"+ e,e);
		}
	
		return tsa;
		
	}

	/* (non-Javadoc)
	 * @see com.syan.netonej.http.client.BaseClient#getHostIp()
	 */
	@Override
	public String getHostIp() {
		return this._host;
	}

	/* (non-Javadoc)
	 * @see com.syan.netonej.http.client.BaseClient#getPort()
	 */
	@Override
	public String getPort() {
		return this._port;
	}

}
