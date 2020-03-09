import java.io.UnsupportedEncodingException;
import java.util.Base64;

import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.client.SVSClient;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneSVS;

public class TestSM2Sign {
	public static String dataStr="";
	
	//获取SM2签名
	public static String createSign() throws UnsupportedEncodingException, NetonejExcepption{
		String smsign="";
		dataStr=Base64.getEncoder().encodeToString("测试原文内容".getBytes("UTF-8"));
		System.out.println("base64原文："+dataStr);
		PCSClient pcsClient=new PCSClient("192.168.10.166", "9176");
		NetonePCS netonePCS=pcsClient.createPKCS1Signature("1a59f4ec393dd150f632de5e35b521d9", "", "kid", dataStr, "0", "ecdsa-sm2-with-sm3");
		smsign=netonePCS.getRetBase64String();
		System.out.println("SM2签名："+smsign);
		return smsign;
	}
	
	//SM2签名验签
	public static void verifySign(String smsign) throws NetonejExcepption, UnsupportedEncodingException{
		String cert="MIIBljCCATygAwIBAgINAO3wiCydIzHSr1jvSzAKBggqgRzPVQGDdTAQMQ4wDAYDVQQDEwVTTUNBMjAiGA8yMDE4MDIxNDE2MDAwMFoYDzIwMjAwMjE0MTYwMDAwWjAVMRMwEQYDVQQDEwp0aW1lc3RhbXAxMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEEVzzQqXrjZRWyRlCxheGvpVUvn7GK4JW3ky/Aeofe/heSVZuRNHWzICVE/Ec6PnbqQzoGjnzMi2i5j9PSbC9JaNyMHAwCQYDVR0TBAIwADAdBgNVHQ4EFgQUNJMME4CuloBbXkSOt56cq7aMENcwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwgwHwYDVR0jBBgwFoAUb1w5KqEXTsLUthIRex7eGoRQYTUwCwYDVR0PBAQDAgbAMAoGCCqBHM9VAYN1A0gAMEUCIQDPsCqgTLSxF1Guj/IQF6v2JbRfQtdqkn1UdUx5+9GTWQIgRVuPo7ktn+OznEvkNAYhOj+6PCO5FSox5KStyU64c9Y=";		
		String data=new String("测试原文内容".getBytes("UTF-8"),"UTF-8");
		SVSClient svsClient=new SVSClient("192.168.10.166", "9188");
		NetoneSVS result=svsClient.verifyPKCS1(data, smsign, "ecdsa-sm2-with-sm3","0", cert, false);
		int rescode=result.getStatusCode();
		System.out.println(rescode);
	}
		
	
	public static void main(String[] args) throws UnsupportedEncodingException, NetonejExcepption {
		// TODO Auto-generated method stub
		verifySign(createSign());
		
		
	}

}
