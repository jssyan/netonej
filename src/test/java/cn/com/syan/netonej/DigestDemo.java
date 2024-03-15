package cn.com.syan.netonej;

import com.syan.netonej.common.NetoneDigest;
import org.bouncycastle.util.encoders.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author mmdet
 * @Date 2023/3/30 17:21
 * @Description
 */
public class DigestDemo {

    public static void fileDigest(){
        String path = "/Users/kisscat/Downloads/tkks_exam_client_setup_220816.exe";
        InputStream in = null;
        try {
            in = new FileInputStream(path);
            //一次读取1M的大小个字节进行计算，可按需调大
            byte[] buffer = new byte[1024 * 1024];
            //摘要对象
            NetoneDigest digest = new NetoneDigest("SM3");
            //读取文件,多轮计算摘要
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            //摘要结果
            byte[] result = digest.digest();
            //输出base64编码的摘要结果
            System.out.println(Base64.toBase64String(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
