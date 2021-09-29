package com.syan.netonej.common.dict;

/**
 * @Author mmdet
 * @Date 2021-08-13 14:24
 * @Description 所有使用密钥ID地方, 都可以使用idmagic参数, idmagic可以用来指明id参数的数据来源
 */
public enum IdMagic {
    KID,        //密钥ID(大小写无关). 这是缺省参数, 如果idmagic没有设置, 或者设置成空, 将使用该参数
    SNHEX,      //ID为证书序列号的16进制表示, (大小写无关)
    SNDEC,      //ID为证书序列号的10进制表示, (大小写无关)
    TNSHA1,     //ID为证书指纹(SHA1),大小写无关
    SCN,        //ID为证书主题项中的CN, 大小写敏感,
    EXTPRI,     //ID里面包含是base64编码的私钥(DER或者PEM格式),目的是支持外部公私钥的密码运算
    EXTPUB,     //ID里面包含是base64编码的公钥(DER或者PEM格式),目的是支持外部公私钥的密码运算
    CID         //用户自定义的密钥id(字符串类型), 如果服务端密钥库未设置cid, 那么cid等于kid
}
