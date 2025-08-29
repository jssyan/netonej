package cn.com.syan.netonej;


import com.syan.netonej.common.dict.KeyAlgorithm;
import com.syan.netonej.common.dict.KeyUseage;
import com.syan.netonej.http.client.CcgwClient;
import com.syan.netonej.http.entity.KeyListItem;
import com.syan.netonej.http.entity.NetoneKeyList;

import org.junit.Test;

import java.util.List;

/**
 * @Author: xuyaoyao
 * @Date: 2025/8/29 13:24
 * @Description:
 */
public class CcgwClientTest {

    private CcgwClient client = new CcgwClient("http://192.168.10.89","8028","bbb622eea8faf4a9"," 936b55c4e8c94cdbb00e1aadc4887b8d");

    /**
     * pcs 获取可用的密钥Id
     * @throws Exception
     */
    @Test
    public void test() throws Exception {


        NetoneKeyList response =
                client.keyBuilder()
                        .setLimit(10) //可选，返回前n个符合条件的密钥
                        .setKeyUseage(KeyUseage.SIGN)//可选，用于返回特定用法的密钥列表（根据证书对应的密钥用法）
                        .setKeyAlgorithm(KeyAlgorithm.SM2) //可选,用于返回特定算法的密钥
                        .build();
        //结果
        System.out.println(response.getStatusCode());
        //错误描述
        System.out.println(response.getStatusCodeMessage());
        List<KeyListItem> list =  response.getKeyList();
        System.out.println(list.size());
    }
}
