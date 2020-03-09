import java.io.UnsupportedEncodingException;
import java.util.Base64;

import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneEnvelope;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneSignPKCS7;

public class PCSTest {

	private static String host = "221.214.5.66";
	private static String port = "9178";
	private static String datastr = "12344321";
	//private static String id = "8bc3b982177cb92d8e119df48b42b2fa";
	private static String id = "1d548cf6bbe12ce06414c0c449ea3ce4";
	private static String pwd = "111111";
	
	private static String xml="<pcs><version>1.5.4</version><action>envseal</action><kid>8bc3b982177cb92d8e119df48b42b2fa</kid><id>9650555257405359368</id><idmagic>sndec</idmagic><cipher>AES-256-CBC</cipher><data>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</data></pcs>";
	private static PCSClient pcs=new PCSClient(host, port);
	
	//列出可用的Key_ID
	public void testGetKeyId() {
		try {
			NetoneKeyList result = pcs.getPcsIds();
			if(result.getStatusCode()==200){
				System.out.println("No.1 - PCS 获取PCS所有密钥id结果："+result.getKeyList());
			}else{
				System.out.println("获取Keys ID失败！");
			}
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//获取与Key ID相关的x509证书
	public void test1GetBase64CertificateById() {
		try {
			NetoneCertificate result = pcs.getBase64CertificateById(id, "");
			System.out.println("No.2 - PCS  使用密钥id获取公钥证书结果："+result.getStatusCode());
			System.setProperty("PCS.Base64Certificate",result.getCertBase64String());
			System.out.println("cert:"+result.getCertBase64String());
			System.out.println("cert-serial:"+result.getDecSerial()+" "+result.getHexSerial());
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//生成P1签名
	public void testSignaturePKCS1() {
		try {
			byte[] databyte=datastr.getBytes();
			String data=new String(Base64.getEncoder().encode(databyte));
			//NetonePCS result = pcs.createPKCS1Signature(id, pwd, "kid", data, "0","md5");
			NetonePCS result=pcs.createPKCS1Signature("12370000495571840L","71840L", "scn", data, "0", "ecdsa-sm2-with-sm3");
			if(result.getStatusCode()==200){
				System.out.println("No.3 - PCS  使用密钥id生成PKCS#1数字签名结果："+result.getStatusCode());
				System.out.println("sign:"+result.getRetBase64String());
			}
			
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//生成P7签名
	public void testSignaturePKCS7() throws UnsupportedEncodingException {
		String testData="测试";
		String testDataB64=Base64.getEncoder().encodeToString(testData.getBytes("utf-8"));
		System.out.println(testDataB64);
		String testDatDec=new String(Base64.getDecoder().decode(testDataB64),"utf-8");
		System.out.println(testDatDec);
		
		
		try {
			NetoneSignPKCS7 result = pcs.createPKCS7Signature("40727d75fcf32d689690b0fe3976240f", "", "kid", "123",true,"ecdsa-sm2-with-sm3");//ecdsa-sm2
			System.out.println("No.4 - PCS  使用密钥id生成PKCS#7数字签名结果："+result.getStatusCode());
			//assertNotNull(result.getRetBase64String());
			System.out.println("sign7:"+result.getRetBase64String());
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//数字信封封包
	public void test2EnvelopePacket() {
		String Base64Certificate = System.getProperty("PCS.Base64Certificate");
		try {
			NetoneEnvelope result = pcs.envelopePacket(id, pwd, "", datastr, Base64Certificate);
			if(result.getStatusCode()==200){
			System.out.println("No.5 - PCS  使用密钥id进行数字信封封包结果："+result.getStatusCode()+"  数字信封封包成功！");
			System.setProperty("PCS.envelope",result.getRetBase64String());
			System.out.println(result.getRetBase64String());
			}
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//数字信封解包
	public void test3EnvelopeUnpack() {
		String envelope = System.getProperty("PCS.envelope");
		//String envelope="MIIB1wYJKoZIhvcNAQcDoIIByDCCAcQCAQAxggF/MIIBewIBADBjMFIxCzAJBgNVBAYTAkNOMQ0wCwYDVQQIEwRXQU5HMQ0wCwYDVQQHEwRXQU5HMRIwEAYDVQQKEwl3YW5nIGx0ZC4xETAPBgNVBAMTCGhvc3R3YW5nAg0Ah2F7QiuzTs2bHtkxMA0GCSqGSIb3DQEBAQUABIIBAGp0F0UsqBlPR9Vv7lwBp7giVnORyb9Yn0SLTDrZiT+9dhL6BMCv2AsHFdsw28ghIMG8AphdeTMVa+XXmEVbw7zOU1q+hJQ7w60H2Ww9MOyi1h5OoOvGdIbafdP0kl9KWQecbho97RJhtX5XazmD885vuyiWIqb7yjp0douEqx/gJGHV1wgXaYp5/xoKkOPUgaTqo5Jwb5B98L+uGRogpjMy1nRt7J5HBREw0YX/8Imo0muxvBBf34d3NSJS5W1s5B52C3RmXa5aHogkKDdaDMwNrEZE9gFUn+knp7SdAF+1Cp8hSyAXNVAcfBKynpF0TEpV3Et8OAD0gsr//MEtqRUwPAYJKoZIhvcNAQcBMB0GCWCGSAFlAwQBFgQQ+lA5iAdqCLteoTLf2J2IMoAQF9yBy2tcgIpX3MBNY/lWCA==";
		try {
			NetonePCS result =pcs.envelopeUnpack(id, pwd, "kid", envelope);
			if(result.getStatusCode()==200){
			System.out.println("No.6 - PCS  使用密钥id进行数字信封解包结果："+result.getStatusCode()+"  "+result.getStatusCodeMessage());
			System.out.println(result.getRetBase64String());
			}
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//私钥加密
	public void testP3riKeyEncrypt() {
		try {
			NetonePCS result =pcs.priKeyEncrypt(id, pwd, "", datastr);
			System.out.println("No.7 - PCS  使用密钥id进行私钥加密结果："+result.getStatusCode()+"  "+result.getStatusCodeMessage());
			System.setProperty("PCS.encryptData",result.getRetBase64String());
			System.out.println(result.getRetBase64String());
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//公钥加密
	public void testP1ubKeyEncrypt() {
		try {
			String b64data=Base64.getEncoder().encodeToString(datastr.getBytes());
			NetonePCS result =pcs.pubKeyEncrypt("40727d75fcf32d689690b0fe3976240f", "kid", b64data);
			System.out.println("No.8 - PCS  使用密钥id进行公钥加密结果："+result.getStatusCode()+"  "+result.getStatusCodeMessage());
			System.setProperty("PCS.encryptData2",result.getRetBase64String());
			System.out.println(result.getRetBase64String());
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//公钥解密
	public void testP4ubKeyDecrypt() {
		String encryptData2 = System.getProperty("PCS.encryptData");
		try {
			NetonePCS result =pcs.pubKeyDecrypt(id, "", encryptData2);
			System.out.println("No.9 - PCS  使用密钥id进行公钥解密结果："+result.getStatusCode()+"  "+result.getStatusCodeMessage());
			System.out.println(result.getRetBase64String());
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//私钥解密
	public void testP2riKeyDecrypt() {
		//String encryptData = System.getProperty("PCS.encryptData2");
		try {
			String smid="40727d75fcf32d689690b0fe3976240f";
			String encData="MHECIQC7MLU5ccjLLHcExFKOALXKlVjrjhvPs2IS0j3OzMDo4gIgdqGBZFoCJ/UHNE+nMs+4q0lzpjmNRHouPOxVW30Lq/UEIHIWJGGQ+i0ShdMyM++ji+UAJgCFh6prs0NlI/OHjRnoBAiNywFlFYG1Hg==";
			NetonePCS result =pcs.priKeyDecrypt(smid, "", "kid", encData);
			System.out.println("No.10 - PCS  使用密钥id进行私钥解密结果："+result.getStatusCode()+"  "+result.getStatusCodeMessage());
			System.out.println(result.getRetBase64String());
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
	//创建XML数字签名
	public byte[] testSignatureXML(){
		String database64=new String(Base64.getEncoder().encode(xml.getBytes()));
		/*byte[] databyte=datastr.getBytes();
		String database64=new String(Base64.getEncoder().encode(databyte));*/
		byte[] xml=null;
		try {
			NetonePCS result =pcs.createXMLSignature(id, pwd, "kid", database64);
			System.out.println("No.11 - PCS  使用密钥id生成XML签名结果："+result.getStatusCode()+"  "+result.getStatusCodeMessage());
			System.out.println(result.getRetBase64String());
			xml=result.getEncoded();
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
		return xml;	
	}


	/**
	 * 修改密码口令
	 * 响应状态码：200代表执行成功. 400:代表参数错误，403代表无权访问或者密钥, 404表示密钥没有找到, 5xx:服务端异常错误.
	 * 响应结果说明：
	 * 0: 成功
	 * 1: 旧口令验证不通过
	 * 2: 密钥库操作失败
	 * 3: 更新新口令失败
	 */
	public static void testchpwd(){
		try {
			NetonePCS result = pcs.changePassword(id, null, oldpwd, newpwd);
			if(result.getStatusCode()==200){
				String msg = result.getRetBase64String();
				if(msg.equals("0")){
					System.out.println("成功");
				}else if(msg.equals("1")){
					System.out.println("旧口令验证不通过");
				}else if(msg.equals("2")){
					System.out.println("密钥库操作失败");
				}else if(msg.equals("3")){
					System.out.println("更新新口令失败");
				}
			}
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
	}
	
}
