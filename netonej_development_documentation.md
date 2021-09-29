## NetONEJ 开发说明文档

NetoneJ是一个连接先安NetONE安全网关 SVS PCS TSA EAPI等模块的工具类库，通过对各模块提供的API进行封装，屏蔽了底层调用细节，降低了使用相关API的学习成本，可以提高集成效率。

### 1.PCS模块

PKI密码运算服务(PKI Crypto Service)主要功能:

- 私钥的管理.
- 私钥的加解密操作.
- 公钥的加解密操作
- 生成PKCS#1数字签名.
- 生成PKCS#7数字签名.
- XML签名
- 数字信封封包
- 数字信封解包



#### 1.1 构造PCSClient对象

支持三种构建方式

```
PCSClient client = new PCSClient(String host, String port, String application);

PCSClient client = new PCSClient(String host, String port);

PCSClient client = new PCSClient(String host);
```

* host为服务器IP，支持https协议

* port为端口号，默认为9178

* application为应用模式，取值`abck2`用于农行K宝二代项目， 默认为标准模式

#### 1.2 获取可用的密钥(Kid)

**1.2.1获取所有可用的密钥ID**

```
NetoneKeyList getKids();
```

**1.2.2获取前n个符合条件的密钥**

```
NetoneKeyList getKids(int limit);
```

**1.2.3获取特定算法的密钥**

```
NetoneKeyList getKids(KeyAlgorithm keyAlgorithm);
```

参数keyAlgorithm的取值是一个枚举类型对象(如下)，定义了支持的密钥算法类型

```
public enum KeyAlgorithm {
	SM2,
	RSA,
	ECC,
	SK;
}
```

**1.2.4获取特定用法的密钥**

```
NetoneKeyList getKids(KeyUseage keyUseage);
```

参数keyUseage的取值是一个枚举类型对象(如下)，定义了支持的密钥用法类型

```
public enum  KeyUseage {
		SIGN, //签名用法
    ENCRYPT //加密用法
}
```

**1.2.5获取前n个特定算法的密钥**

```
NetoneKeyList getKids(int limit,KeyAlgorithm keyAlgorithm);
```

**1.2.6获取前n个特定用法的密钥**

```
NetoneKeyList getKids(int limit,KeyUseage keyUseage);
```

**1.2.7获取特定算法和特定用法的密钥**

```
NetoneKeyList getKids(KeyAlgorithm keyAlgorithm,KeyUseage keyUseage);
```

**1.2.8获取前n个特定算法和特定用法的密钥**

```
NetoneKeyList getKids(int limit, KeyAlgorithm keyAlgorithm,KeyUseage keyUseage);
```

#### 1.3 获取Kid对应的数字证书

通过该函数获取的数字证书均为base64编码格式

**1.3.1根据密钥ID获取证书**

```
NetoneCertificate getBase64CertificateById(String kid);
```

参数kid为密钥对应的ID

**1.3.2根据证书CN/证书序列号/证书指纹获取证书**

```
NetoneCertificate getBase64CertificateById(String id, IdMagic idMagic);
```

参数idMagic的取值是一个枚举类型对象(如下)，定义了支持的kid类型

```
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
```

> 注意：在NetoneJ中所有使用kid地方, 都可以使用idmagic参数, idmagic可以用来指明kid参数的数据来源。后续不在赘述。



#### 1.4 创建PKCS#1签名

NetoneJ提供了4种调用方式来实现签名功能：

```
NetonePCS createPKCS1Signature(String id, String pwd,IdMagic idMagic, String data, DataType datatype, DigestAlgorithm algo);

NetonePCS createPKCS1Signature(String id, String pwd,IdMagic idMagic, String data, DataType datatype);

NetonePCS createPKCS1Signature(String id, String pwd, String data, DataType datatype, DigestAlgorithm algo);

NetonePCS createPKCS1Signature(String id, String pwd, String data, DataType datatype);
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* pwd：密钥访问口令，如果没有设置密钥口令，请设置为null或者空字符`“”`

* data：待签名数据，BASE64编码格式的字符串，数据长度不限

* datatype：待签名数据的类型. 取值是一个DataType枚举类型对象，定义了支持的数据格式。如下：

  ```
  public enum DataType {
      PLAIN, //原文
      DIGEST, //原文的摘要数据（BASE64编码格式的字符串）
      DIGESTHEX //原文的摘要数据（16进制的字符串格式）
  }
  ```

* algo：摘要的算法类型，取值是一个DigestAlgorithm枚举类型对象，定义了签名时支持的摘要算法，如下：

  ```
  public enum DigestAlgorithm {
  	MD5("md5"), //md5摘要算法
  	SHA1("sha1"),//sha1摘要算法
  	SHA256("sha256"), //sha256摘要算法
  	SHA384("sha384"),//sha384摘要算法
  	SHA512("sha512"),//sha512摘要算法
  	SM3("sm3"),//sm3摘要算法
  	ECDSASM2WITHSM3("ecdsa-sm2-with-sm3")//sm2签名摘要算法
  }
  ```

  如果没有设置algo，PCS服务将使用缺省设置, 对于RSA密钥, 缺省摘要算法是sha1, 对于ECC密钥, 缺省摘要算法是ecdsa-sm2-with-sm3

#### 1.5 创建PKCS#7签名

NetoneJ提供了10种调用方式来实现PKCS#7签名功能：

```
NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, boolean attach, DigestAlgorithm algo)；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, DigestAlgorithm algo)；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data,boolean attach)；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data)；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data)；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, DigestAlgorithm algo)；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach）；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, boolean fullchain,DigestAlgorithm algo)；

NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, boolean fullchain)；

NetoneSignPKCS7  createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, boolean attach, boolean fullchain,DigestAlgorithm algo)；
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* pwd：密钥访问口令，如果没有设置密钥口令，请设置为null或者空字符`“”`

* data：待签名数据，BASE64编码格式的字符串，数据长度不限
* attach：表示是否在签名结果里面包含原文数据 ，缺省=true
*  fullchain表示是否嵌入整个证书链，缺省=false

* algo：摘要的算法类型，取值是一个枚举类型对象，定义了签名时支持的摘要算法

#### 1.6  数字信封封包

NetoneJ提供了以下调用方式来实现封包功能：

```
NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic, String data, String base64Certificate, CipherAlgorithm cipher)；

NetoneEnvelope envelopePacket(String id, String pwd, String data, String base64Certificate, CipherAlgorithm cipher)；

NetoneEnvelope envelopePacket(String id, String pwd, String data, CipherAlgorithm cipher)；
    
NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data, CipherAlgorithm cipher)；
    
NetoneEnvelope envelopePacket(String id, String pwd, String data, String base64Certificate)；
    
NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data, String base64Certificate)；
    
NetoneEnvelope envelopePacket(String id, String pwd, String data)；

NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data) ；
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* pwd：密钥访问口令，如果没有设置密钥口令，请设置为null或者空字符`“”`

* data: BASE64编码的待封包数据

* base64Certificate: BASE64编码的接收方证书. 如果peer未设置, 那么pcs将使用id所指定的证书作为接收方证书封包(只加密,不签名), 否则将使用id所指定的证书作为签名证书, peer指定的证书作为接收方证书封包(既签名, 又加密)

* cipher：封包对称加密算法，如果没有设置，则使用PCS服务端缺省对称加密算法，取值是一个CipherAlgorithm枚举类型对象，定义了支持的数据格式。如下：

  ```
  public enum CipherAlgorithm {
  	SM4CBC("SM4-CBC"),
  	AES192CBC("AES-192-CBC"),
  	AES256CBC("AES-256-CBC");
  }
  ```

#### 1.7 数字信封解包

```
NetonePCS envelopeUnpack(String id, String pwd, IdMagic idMagic, String envelope) ;

NetonePCS envelopeUnpack(String id, String pwd, String envelope);
```

* id：kID
* idMagic：密钥ID的数据来源类型

* pwd：密钥访问口令，如果没有设置密钥口令，请设置为null或者空字符`“”`
* envelope: BASE64编码的待解包数据

#### 1.8 私钥加密

ECC算法不支持私钥加密

```
NetonePCS priKeyEncrypt(String id, String pwd, String data);

NetonePCS priKeyEncrypt(String id, String pwd,IdMagic idMagic, String data);
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* pwd：密钥访问口令，如果没有设置密钥口令，请设置为null或者空字符`“”`
* data: BASE64编码的待封包数据

#### 1.9 私钥解密

```
NetonePCS priKeyDecrypt(String id, String pwd, String encryptData)

NetonePCS priKeyDecrypt(String id, String pwd, IdMagic idMagic, String encryptData);  
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* pwd：密钥访问口令，如果没有设置密钥口令，请设置为null或者空字符`“”`
* encryptData: 待解密数据（Base64编码），长度不限

#### 1.10 公钥加密

```
NetonePCS pubKeyEncrypt(String id, IdMagic idMagic, String data);
NetonePCS pubKeyEncrypt(String id, String data);
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* data: 待加密数据（Base64编码），长度不限

#### 1.11 公钥解密

ECC算法不支持公钥解密

```
NetonePCS pubKeyDecrypt(String id, IdMagic idMagic, String encryptData)；

NetonePCS pubKeyDecrypt(String id, String encryptData)；
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* encryptData: 待解密数据（Base64编码），长度不限

#### 1.12 创建XML签名

XML签名不支持ECC算法, 无这方面的公开标准

```
NetonePCS createXMLSignature(String id, String pwd, String data)；

NetonePCS createXMLSignature(String id, String pwd, String data,SignMode signMode)；

NetonePCS createXMLSignature(String id, String pwd, IdMagic idMagic,String data)；

NetonePCS createXMLSignature(String id, String pwd, IdMagic idMagic,String data,SignMode signMode)；
```

* id：密钥ID
* idMagic：密钥ID的数据来源类型

* pwd：密钥访问口令，如果没有设置密钥口令，请设置为null或者空字符`“”`

* data: BASE64编码的待签名数据，数据长度不限

* signMode:签名模式,取值为SignMode枚举对象，定义了签名模式，缺省时为enveloped，如下

  ```
  
  public enum  SignMode {
      enveloped,
      enveloping
  }
  ```

#### 1.13 修改密钥访问口令

```
NetonePCS changePassword(String id, String oldpwd,String newpwd)

NetonePCS changePassword(String id, IdMagic idMagic, String oldpwd,String newpwd)
```

* id: 密钥ID

* oldpwd: 旧口令

* newpwd: 新口令

* idMagic: 密钥ID的数据来源类型

### 2.SVS模块

签名验证服务(Signature Verifcation Server)提供了对证书及证书签名信息进行可信的验证服务.主要功能:

- 证书验证,包括验证证书及颁发者的有效性,证书状态的验证.(CRL/OCSP)
- 业务数据签名验证.

#### 2.1 构造SVSClient对象

支持三种构建方式

```
SVSClient client = new SVSClient(String host, String port, String application);

SVSClient client = new SVSClient(String host, String port);

SVSClient client = new SVSClient(String host);
```

* host为服务器IP，支持https协议

* port为端口号，默认为9188

* application为应用模式，取值abck2用于农行K宝二代项目， 默认为标准模式

#### 2.2 验证证书

```
NetoneSVS verifyCertificate(String Base64Certificate);
NetoneSVS verifyCertificate(String Base64Certificate,String signts);
NetoneSVS verifyCertificate(String id,IdMagic idMagic);
NetoneSVS verifyCertificate(String id,IdMagic idMagic,String signts)
```

* Base64Certificate:所需要验证的证书(B64编码)
* id: 密钥ID

* idMagic: 密钥ID的数据来源类型

* signts: 格式为gYYYMMDDHHmmSS[.NNN]Z, Z是字符。例如 g2020050101120101.232Z 本参数表示签名发生时间(GMT时间)，如果这个参数存在，那么将按照以下规则验证证书：

1. 如果signts在证书有效期在之内，则忽略证书有效期检查；
2. 如果证书被废除日期在signts之后，则忽略证书废除检查结果； 



#### 2.3 验证PKCS#1数字签名

```
NetoneSVS verifyPKCS1(String data, String signature, DigestAlgorithm algo, DataType datt, String certificate);

NetoneSVS verifyPKCS1(String data, String signature, DataType datt, String certificate);

NetoneSVS verifyPKCS1(String data, String signature, DigestAlgorithm algo, String certificate);

NetoneSVS verifyPKCS1(String data, String signature, String certificate);

NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DigestAlgorithm algo, DataType datt);

NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DataType datt);

NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DigestAlgorithm algo);

NetoneSVS verifyPKCS1(String id, IdMagic idMagic,String data,String signature);
```

* id: 密钥ID
* data: 签名时的原文数据(b64编码)

* idMagic: 密钥ID的数据来源类型

* signature: PKCS#1签名结果(b64编码)

* certificate: 签名时所用证书（Base64编码）

* algo：摘要的算法类型，取值是一个DigestAlgorithm枚举类型对象

* datt： 签名原文数据的类型. 取值是一个DataType枚举类型对象

#### 2.4 验证PKCS#7数字签名

```
NetoneSVS verifyPKCS7(String p7data, String p7odat);//原文分离时验证
NetoneSVS verifyPKCS7(String p7data); //原文不分离时验证
```

* p7data ： PKCS#7签名结果,Base64编码格式

* p7odat ： 签名时的原文数据,Base64编码格式

#### 2.5 验证XML签名

```
NetoneSVS verifyXML(String data)
```

* data:XML数据(Base64编码)

#### 2.6 枚举服务端的证书

```
NetoneCertList listCertificates();
```

### 3.TSA模块

时间戳服务(TSA Server)提供了对数据时间的可信服务.主要功能:

- tsac: 签发时间戳(RFC3161), 输入参数是摘要.

- tsav: 验证时间戳.

- tsrs: 签发时间戳(RFC3161), 输入参数是TimestampRequest

- tscs: 签发副署签名


#### 3.1 构造TSAClient对象

支持两种构建方式

```
TSAClient client = new TSAClient(String host, String port);

TSAClient client = new TSAClient(String host);
```

* host为服务器IP,支持https协议

* port为端口号，默认为9198

#### 3.2 创建时间戳

```
NetoneTSA createTimestamp(String data, DataType dataType);
NetoneTSA createTimestamp(String data, DataType dataType, DigestAlgorithm algo)；
NetoneTSA createTimestamp(byte[] tsaRequest)；
```

* data： 待时间戳签名数据原文
* algo： 摘要算法
* dataType： 待签名数据的类型，若类型为原文则需要base64编码格式，类型为摘要类型，则需要hex string格式（16进制的字符串）
* tsaRequest: 时间戳请求Timestamp Request

#### 3.3 验证时间戳

```
NetoneTSA verifyTimestamp(String timestamp)；
NetoneTSA verifyTimestamp(String timestamp,String data)
NetoneTSA verifyTimestamp(String timestamp,String data,DataType dataType)
```

* timestamp：时间戳签名数据

* data：时间戳签名数据的原文（不传递此项仅验证时间戳签名，而不验证时间戳内容）

* dataType： 原文数据的类型.若类型为原文则需要base64编码格式，类型为摘要类型，则需要hex string格式（16进制的字符串）

### 4.EAPI模块

EAPI可以被用来**远程**访问本机的资源. 如果开放了非安全的操作给未知远程用户, 就有可能会带来安全隐患, 请**小心**使用.

- 证书管理



#### 4.1 构造EapiClient对象

支持两种构建方式

```
TSAClient client = new TSAClient(String host, String port);

TSAClient client = new TSAClient(String host);
```

* host为服务器IP,https协议

* port为端口号，默认为9108

#### 4.2枚举服务端证书列表

```
NetoneCertList listCertificates();
NetoneCertList listCertificates(boolean revoked)
```

* revoked : true返回不可信列表，false返回可信列表，默认为false

#### 4.3上传证书

```
NetoneResponse uploadCert(String cert)
NetoneResponse uploadCert(String cert,boolean revoked);
```

* cert：base64编码的证书
* revoked : true上传到不可信列表，false上传到可信列表，默认为false

#### 4.4删除证书

```
NetoneResponse deleteCert(String data, IdMagic idMagic);
NetoneResponse deleteCert(String data, IdMagic idMagic,boolean revoked)
```

* data：删除证书的依据
* idMagic：data的数据类型，如CN、KID等
* revoked : true从不可信列表删除，false从可信列表删除，默认为false
