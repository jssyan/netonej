/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import java.util.HashMap;
import java.util.Map;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.tsp.TimeStampRequest;

public class TSAClient extends BaseClient{

	public TSAClient(String host,String port){
		super(host,port);
	}

	public TSAClient(String host){
		super(host,"9198");
	}

	@Deprecated
	public NetoneTSA createTimestamp(byte[] data,String algo,int datatype) throws NetonejExcepption{
		try {
			Map<String, String> params = new HashMap<>();
			if(datatype==0){
				String database64str=NetonejUtil.base64Encode(data);
				params.put("data", database64str);
			}else{
				String digest=NetonejUtil.byte2HexString(data);
				params.put("digest", digest);
			}

			if(!NetonejUtil.isEmpty(algo)){
				params.put("algo", algo);
			}

				NetoneResponse response=doHttpPost(Action.TSA_ACTION_TSAC, params);
				return new NetoneTSA(response);
		} catch (Exception e) {
			throw new NetonejExcepption("-TSA(tsac)时间戳签发失败"+ e,e);
		}
	}

	@Deprecated
	public NetoneTSA createTimestamp(String data,String algo) throws NetonejExcepption{
		return createTimestamp(data,algo,DataType.PLAIN);
	}


	public NetoneTSA createTimestamp(String data, DataType dataType,DigestAlgorithm algo) throws NetonejExcepption{
		return createTimestamp(data,algo.getName(),dataType);
	}

	public NetoneTSA createTimestamp(String data, DataType dataType) throws NetonejExcepption{
		return createTimestamp(data,null,dataType);
	}

	/**
	 * 创建时间戳签名
	 * @param data 待时间戳签名数据
	 * @param algo 摘要算法（'md5' or 'sha1' or 'sm3'）
	 * @param dataType 待签名数据的类型. 0表示是原文, !0表示摘要
	 * @return 时间戳签名结果
	 * @throws NetonejExcepption API全局异常类
	 */
	private NetoneTSA createTimestamp(String data, String algo, DataType dataType) throws NetonejExcepption{
		if(dataType == DataType.DIGEST){
			throw new NetonejExcepption("不支持的数据类型 DataType.DIGEST");
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			if(DataType.PLAIN == dataType){
				params.put("data", data);
			}else{
				params.put("digest", data);
			}
			if(!NetonejUtil.isEmpty(algo)){
				params.put("algo", algo.trim().toLowerCase());
			}
			NetoneResponse response=doHttpPost(Action.TSA_ACTION_TSAC, params);
			return new NetoneTSA(response);
		} catch (Exception e) {
			throw new NetonejExcepption("-TSA(tsac)时间戳签发失败"+ e,e);
		}
	}


	@Deprecated
	public NetoneTSA createTimestampTsrs(byte[] data) throws NetonejExcepption{
		return createTimestamp(data);
	}

	/**
	 * 创建时间戳签名
	 * @param data 时间戳请求
	 * @return 时间戳签名结果
	 * @throws NetonejExcepption API全局异常类
	 */
	public NetoneTSA createTimestamp(byte[] data) throws NetonejExcepption{
		try {
			TimeStampRequest timeStampRequest = new TimeStampRequest(data);
			NetoneResponse response=doHttpPostBytes(Action.TSA_ACTION_TSRS, data);
			return new NetoneTSA(response);
		} catch (Exception e) {
			throw new NetonejExcepption("-TSA(tsrs)时间戳签发失败"+ e,e);
		}
	}
	/**
	 * 该方法为了提供给检测平台检测而写
	 * @param data asn1格式的时间戳请求
	 * @return
	 * @throws NetonejExcepption
	 */
	public byte[] createTimestampTsrsByAsn1Req(byte[] data) throws NetonejExcepption{
		try {
			NetoneResponse response=doHttpPostBytes(Action.TSA_ACTION_TSRS, data);
			NetoneTSA netoneTSA = new NetoneTSA(response);
			if(netoneTSA.getStatusCode() == 200){
				return netoneTSA.getEncoded();
			}
			return null;
		} catch (Exception e) {
			throw new NetonejExcepption("-TSA(tsrs)时间戳签发失败"+ e,e);
		}
	}

	/**
	 * 验证时间戳签名
	 * @param timestamp 时间戳签名数据
	 * @return TSR实体对象类 {@link com.syan.netonej.http.entity.NetoneTSA}
	 * @throws NetonejExcepption API全局异常类
	 */
	public NetoneTSA verifyTimestamp(String timestamp) throws NetonejExcepption{
		return verifyTimestampAction(timestamp,null,null);
	}

	public NetoneTSA verifyTimestamp(String timestamp,String data) throws NetonejExcepption{
		return verifyTimestampAction(timestamp,null,DataType.PLAIN);
	}

	public NetoneTSA verifyTimestamp(String timestamp,String data,DataType dataType) throws NetonejExcepption{
		return verifyTimestampAction(timestamp,null,dataType);
	}

	private NetoneTSA verifyTimestampAction(String timestamp,String data,DataType dataType) throws NetonejExcepption{
		if(dataType == DataType.DIGEST){
			throw new NetonejExcepption("不支持的数据类型 DataType.DIGEST");
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("tsr", timestamp);
		try {
			if(!NetonejUtil.isEmpty(data)){
				if(DataType.PLAIN == dataType){
					params.put("data", data);
				}else{
					params.put("digest", data);
				}
			}
			NetoneResponse response=doHttpPost(Action.TSA_ACTION_TSAV, params);
			if(response.getStatusCode()==200){
				//如验签通过直接解析时间戳签名数据
				response.setRetString(timestamp);
			}
			return new NetoneTSA(response);
		} catch (Exception e) {
			throw new NetonejExcepption("-TSA(tsav)时间戳验证失败"+ e,e);
		}
	}

}
