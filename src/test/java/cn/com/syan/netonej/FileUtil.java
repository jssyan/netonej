package cn.com.syan.netonej;

import java.io.*;

/**
 * @Author mmdet
 * @Date 2022/6/1 11:20
 * @Description
 */
public class FileUtil {

    //将Byte数组保存成文件
    public static void save(byte[] bytes, String filePath, String fileName) throws Exception{
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //将Byte数组保存成文件
    public static void save(byte[] bytes, File f) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void delete(String path){
        File file = new File(path);
        if(file.exists()){
            if(file.isFile()){
                file.delete();
            }
            if(file.isDirectory()){
                deleteDirectory(file);
            }
        }
    }

    private static void deleteDirectory(File file) {
        File[] files = file.listFiles();// 获得传入路径下的所有文件
        for(File f:files){
            if(f.isFile()){
               f.delete();
            }else{
                deleteDirectory(f);
            }
        }
    }

    //读取文件
    public static byte[] read(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            int iAvail = is.available();
            byte[] bytes = new byte[iAvail];
            is.read(bytes);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
