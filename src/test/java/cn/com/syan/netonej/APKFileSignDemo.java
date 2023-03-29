package cn.com.syan.netonej;

import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.entity.NetoneResponse;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class APKFileSignDemo {

    //设置PCS（私钥密码服务）的服务器IP与端口号
    private PCSClient pcsClient = new PCSClient("192.168.10.215","9178");
    //签名私钥KID
    String kid = "afaf4cdb49964c24e172112f2a4b98c9";
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
        byte[] data = FileUtil.read("/Users/kisscat/Documents/app.apk");

        NetoneResponse response = pcsClient.fileSignBuilder()
                .setId(kid).setIdmagic(IdMagic.KID)//指定签名密钥
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
        String in = "/Users/kisscat/Documents/app.apk";
        //输出文件路径
        String out = "/Users/kisscat/Documents/22.svaaa";
        //签名
        pcsClient.fileSignBuilder()
                .setId(kid).setIdmagic(IdMagic.KID)
                .setPasswd(pin)
                .setAlgo("SM3")
                .build(in,out);//设置输入和输出路径
    }
}
