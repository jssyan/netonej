import com.syan.netonej.http.client.TSAClient;
import com.syan.netonej.http.entity.NetoneTSA;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import com.syan.netonej.exception.NetonejExcepption;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.omg.CORBA.SystemException;

public class TSATest {

	private static String host = "192.168.10.166";
	private static String port = "9198";
	private String data = "12344321";
	private String timestrString="";
	private static TSAClient tsa=new TSAClient(host,port);
	
	//创建时间戳
	public void testCreateTimestamp() {
		try {
			NetoneTSA result = tsa.createTimestamp(data, "sha1");
			System.out.println("No.1 - TSA 签发的时间戳签名结果："+result.getStatusCode());
			timestrString=result.getTimestampbase64();
			if(result!=null){
			System.out.println("Timestampbase64:"+result.getTimestampbase64());
			System.out.println("Algo:"+result.getAlgo());
			System.out.println("Imprint:"+result.getImprint());
			System.out.println("Nonce:"+result.getNonce());
			System.out.println("Serial:"+result.getSerial());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			Date date=result.getTimestamp();
			String time=sdf.format(date);
			System.out.println(time);
			//System.out.println("Timestamp:"+result.getTimestamp());
			}
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
			//fail("NetoneExcepption");
		}
	}
	
	//验证时间戳
	public void testVerifyTimestamp() {
		try {
			//String timestamp = timestrString;
			String timestamp="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";
			System.out.println("获得的时间戳签名长度是："+timestamp.length());
			NetoneTSA result = tsa.verifyTimestamp(timestamp);
			System.out.println("No.2 - TSA 签发的时间戳签名验证结果："+result.getStatusCode());
			if(result.getTimestampbase64().length()>0){
			System.out.println("Timestampbase64:"+result.getTimestampbase64());
			System.out.println("Algo:"+result.getAlgo());
			System.out.println("Imprint:"+result.getImprint());
			System.out.println("Nonce:"+result.getNonce());
			System.out.println("Serial:"+result.getSerial());
			//System.out.println("Timestamp:"+result.getTimestamp());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS");
			Date date=result.getTimestamp();
			String time=sdf.format(date);
			System.out.println(time);
			}
		} catch (NetonejExcepption e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//temptest
	public void tempTest() throws NetonejExcepption, IOException{
		TSAClient ts=new TSAClient("192.168.10.166", "9198");
		InputStream ins=new FileInputStream("E:/ECMS_TestDocument/signed.pdf");
		byte[] datab=IOUtils.toByteArray(ins);
		String datasha1=DigestUtils.shaHex(datab);
		System.out.println(datasha1);		
		
		NetoneTSA nts=ts.createTimestamp(datab,"sha1",0);
		System.out.println(nts.getTimestampbase64());
		System.out.println(nts.getImprint());
	}

}
