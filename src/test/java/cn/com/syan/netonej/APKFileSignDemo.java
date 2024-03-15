package cn.com.syan.netonej;

import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.entity.NetoneResponse;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class APKFileSignDemo {

    //设置PCS（私钥密码服务）的服务器IP与端口号
    private PCSClient pcsClient = new PCSClient("192.168.20.223","9178");
    //签名私钥KID
    String kid = "86d879253d9c96bae3a688dbbdeb1186";
    //私钥保护口令
    String pin = "111111";

    /**
     * 第一种方式：直接读取文件内容签名
     * 文件保护结构
     * @throws Exception
     */
    @Test
    public void testFileSign_fileData() throws Exception{
        //读取待签名的APK文件
        byte[] data = FileUtil.read("/Users/momocat/Documents/temp/wfxxcj-linux-mips64-v2.4.3.zip");

        NetoneResponse response = pcsClient.fileSignBuilder()
                .setId("sm2").setIdmagic(IdMagic.SCN)//指定签名密钥
                .setPasswd(pin)//指定签名密钥的保护口令
                .setAlgo("SM3")//指定摘要算法
                .build(data);

        //获取状态码 200为成功 其他为失败
        System.out.println(response.getStatusCode());
        //获取签名结果
        System.out.println(Base64.toBase64String(response.getBytesResult()));
    }

    /**
     * 第二种方式：设置文件路径方式，完成签名，输出到文件
     * @throws Exception
     */
    @Test
    public void testFileSign_filePath() throws Exception{
        //APK文件路径
        String in = "/Users/momocat/Documents/temp/6.apk";
        //输出文件路径
        String out = "/Users/momocat/Documents/temp/7.apk";
        //签名
        pcsClient.fileSignBuilder()
                .setId("sm2").setIdmagic(IdMagic.SCN)
                .setPasswd(pin)
                .setAlgo("SM3")
                .build(in,out);//设置输入和输出路径

        NetoneDigest digest = new NetoneDigest("md5");
        digest.update(new FileInputStream(in));
        System.out.println(Base64.toBase64String(digest.digest()));
    }

    @Test
    public void testFileSignVerify_filePath() throws Exception{
        //APK文件路径
            String in = "/Users/momocat/Documents/temp/test_270m.apk";
            File file = new File(in);
            FileInputStream is = new FileInputStream(file);
            //读取前两个字节（确定编码与内容长度）
            byte[] bytes = new byte[2];
            int bytesRead = is.read(bytes);
            if (bytesRead != 2) {
                throw new Exception("文件读取失败");
            }
            if(bytes[0] != 48){
                throw new Exception("文件解析失败,非ASN1编码");
            }
            int lenflag = bytes[1] & 0xFF;
            int dlen = lenflag + 2;
            if(lenflag > 127){
                //计算长度占多少字节
                int byteCount = 0x7F & lenflag;
                dlen = byteCount + 2;
                System.out.println(dlen);
            }
            //开始读取版本号
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
            randomAccessFile.seek(dlen);
            byte[] buffer = new byte[2]; // 读取缓冲区大小
            bytesRead = randomAccessFile.read(buffer);
            System.out.println(Integer.toHexString(buffer[1]));
    }
}
