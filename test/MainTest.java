
public class MainTest {
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		//SVS功能调试
		//SVSTest svstest=new SVSTest();
		//svstest.testlistcerts();//枚举服务端证书列表
		//svstest.testVerifyCertificate(); //验证证书
		//svstest.testVerifyPKCS1String();//验证P1签名
		//svstest.testVerifyPKCS7detachString();//验证P7数字签名（detached模式）
		//svstest.testVerifyPKCS7attachString();//验证P7数字签名(attached模式)
		//svstest.testVerifyXML();//验证XML数字签名
		
		//TSA功能调试
		TSATest tsaTest=new TSATest();
		tsaTest.tempTest();
		//tsaTest.testCreateTimestamp();
		//tsaTest.testVerifyTimestamp();
		
		//PCS功能调试
		//PCSTest pcsTest=new PCSTest();
		//pcsTest.testGetKeyId();
		//pcsTest.test1GetBase64CertificateById();
		//pcsTest.testSignaturePKCS1();
		//pcsTest.testSignaturePKCS7();
		//pcsTest.test2EnvelopePacket();
		//pcsTest.test3EnvelopeUnpack();
		//pcsTest.testP3riKeyEncrypt();
		//pcsTest.testP4ubKeyDecrypt();
		//pcsTest.testP1ubKeyEncrypt();
		//pcsTest.testP2riKeyDecrypt();
		//String testString=pcsTest.testSignatureXML();
		
		/*PCSTest pcsTest=new PCSTest();
		String path="D:/123.xml";
		byte[] testxml=pcsTest.testSignatureXML();
		SaveXML saveXML=new SaveXML();
		saveXML.getFile(testxml, path);*/
		//String test=pcsTest.testSignatureXML();
		//SVSTest svsTest=new SVSTest();
		//svsTest.testVerifyXML(test);
		
	}

}
