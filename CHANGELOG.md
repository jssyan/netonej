## v3.0.15

- 内部网络请求优化
- 接口函数优化、封装了大量的枚举类,降低参数输入难度
- Client构造方式增加了默认端口方式，host参数支持输入协议类型，如“https://192.168.0.1"
- 优化了错误提示信息

#### 依赖库调整
| 依赖库   |  版本            |说明            |
| --------|------------- |------------- |
| commons-codec      |1.6 |移除|
| commons-logging      |1.1.1 |移除|
| httpclient-client      |4.3.5 |移除|
| httpclient-core      |4.3.5 |移除|
| spongycastle-core     |1.51.00 |移除|
| spongycastle-pkix     |1.51.00 |移除|
| spongycastle-prov     |1.51.00 |移除|
| okhttp3     |3.10.0 |增加|
| okio     |1.14.0 |增加|
| bcpkix-jdk15on-1.68     |1.68 |增加|
| bcpkix-jdk15on-1.68     |1.68 |增加|

#### 以下API标记为Deprecated，不再建议使用，未来将会逐渐删除下列API

| 类   |  移除函数           |替代函数            |
| --------|------------- |------------- |
| PCSClient      |getPcsIds() |getKids()|
|PCSClient      |getBase64CertificateById(String id, String idMagic) |getBase64CertificateById(String id, IdMagic idMagic)或其他重载函数 |
| PCSClient      |createPKCS1Signature(byte[] plainText, String certDN, String pwd, String algo) |使用同名的其他其他重载函数|
|PCSClient      |createPKCS1Signature(String id, String pwd, String idMagic, byte[] data, String datatype, String algo) |使用同名的其他其他重载函数|
| PCSClient     |createPKCS1Signature(String id, String pwd, String idMagic, String data, String datatype, String algo) |使用同名的其他其他重载函数|
| PCSClient     |createPKCS7Signature(String id, String pwd, String idMagic, String data, boolean attach, String algo) |使用同名的其他其他重载函数|
| SVSClient     |verifyPKCS1(byte[] plainText, String signature, String certDN) |使用同名的其他其他重载函数|
| SVSClient     |verifyPKCS1(String data, String signature, String algo, String datt, String certificate, boolean dataB64) |使用同名的其他其他重载函数|
| SVSClient     |verifyPKCS1(String data, String signature, String datt, String Base64Certificate, boolean dataB64) |使用同名的其他其他重载函数|



## v3.0.14

1.时间戳接口支持输入时间戳请求、返回byte[]类型的结果

## v3.0.13
1.优化升级

## v3.0.12
1.pcs接口增加密钥密码口令修改


## v3.0.11
1.增加数字信封中对称加密算法的设置
2.增加异常重试功能
3.增加闲置连接定时清理功能

## v3.0.10
1.修复vp7返回解析失败bug
2.修复vp7返回null

## v3.0～v3.0.9
1.增加p1 byte[]
2.优化sm3算法性能
3.增加设置切换https访问
4.签发时间戳增加主题返回，处理中文乱码
5.与netone上发布版本一致从3.0.1开始

## v2.0
1.时间戳摘要返回值，时间戳签发序列号
2.修复p7解析失败
3.增加sha256摘要

