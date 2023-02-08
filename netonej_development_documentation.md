## NetONEJ 开发说明文档

NetoneJ是一个连接先安NetONE安全网关 SVS PCS TSA EAPI等模块的工具类库。

### 1.PCS模块

PKI密码运算服务(PKI Crypto Service)主要功能:

- 公私钥的加解密操作.
- 生成PKCS#1数字签名.
- 生成PKCS#7数字签名.
- XML签名
- 数字信封封包
- 数字信封解包



#### 1.1 构造PCSClient对象


```
PCSClient client = new PCSClient(String host, String port);
```

其中，参数host为服务器IP地址,port为端口号，默认为9178。


#### 1.2 获取可用的密钥(Kid)
```
NetoneKeyList list = pcsClient.keyBuilder()
          .setLimit(10) //可选，返回前n个符合条件的密钥
          .setKeyUseage(KeyUseage.SIGN)//可选，用于返回特定用法的密钥列表（根据证书对应的密钥用法）
          .setKeyAlgorithm(KeyAlgorithm.SM2) //可选,用于返回特定算法的密钥
          .build();
```

#### 1.3 获取Kid对应的数字证书

通过该函数获取的数字证书均为base64编码格式
```
 NetoneCertificate certificate = pcsClient.certificateBuilder()
                .setId(kid)
                .setIdmagic(IdMagic.KID)
                .build();
        System.out.println(certificate.getPEMString());
```

参数idMagic的取值是一个枚举类型对象(如下)，定义了支持的id类型

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

> 注意：在NetoneJ中所有使用id地方, 都可以使用idmagic可以用来指明id参数的数据类型/来源。后续不在赘述。



#### 1.4 创建PKCS#1签名


```
NetonePCS pcs = pcsClient.pkcs1Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(cn) //设置id参数，这里设置的证书cn项
                .setIdmagic(IdMagic.SCN)//指定id的数据类型
                .setData(data)//签名原文
                .setDataType(DataType.PLAIN)//可选，默认为原文签名
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)//可选,指定签名摘要算法
                .setUserId("userid".getBytes())//可选，SM2签发者ID
                .build();
```

* datatype：待签名数据的类型. 取值是一个DataType枚举类型对象。如下：

```
  public enum DataType {
    PLAIN,//表示是原文数据
    DIGEST,//摘要数据
    TIMESTAMP_REQUEST//der编码格式的时间戳请求,用于签发时间戳
}
  ```

* algo：摘要的算法类型，取值是一个DigestAlgorithm枚举类型对象，如下：

  ```
  public enum DigestAlgorithm {
  	MD5("md5"), //md5摘要算法
  	SHA1("sha1"),//sha1摘要算法
  	SHA256("sha256"), //sha256摘要算法
  	SHA384("sha384"),//sha384摘要算法
  	SHA512("sha512"),//sha512摘要算法
  	SM3("sm3"),//sm3摘要算法
  	ECDSASM2WITHSM3("ecdsa-sm2-with-sm3"),//sm2签名摘要算法
          ECDSASM2("ecdsa-sm2");//sm2签名摘要算法
  }
  ```

  如果没有设置algo，PCS服务将使用缺省设置, 对于RSA密钥, 缺省摘要算法是sha1, 对于ECC密钥, 缺省摘要算法是ecdsa-sm2-with-sm3

#### 1.5 创建PKCS#7签名


```
NetonePCS pcs = pcsClient.pkcs7Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(kid)
                .setIdmagic(IdMagic.CID)
                .setData(data.getBytes())
                .setAlgo(DigestAlgorithm.ECDSASM2)
                .setAttach(false)//可选，签名结果中是否包含原始数据
                .setFullchain(false)//可选，签名结果是否嵌入整个证书链
                .setNoattr(false)//可选，签名结果中是否包含签名时间等属性
                .build();
```

#### 1.6  数字信封封包


```
 NetonePCS pcs = pcsClient.envelopePacketBuilder()
                .setId(kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo("sm1-cbc")//可选，设置对称密钥算法
                .setPeer(rsacert)//可选，设置加密证书
                .setPeerMagic(IdMagic.KID)//可选，指定加密证书的类型
                .setDigestAlgo("ecdsa-sm2-with-sm3")//可选
                .build();
```

* peer: 如果peer未设置, 那么pcs将使用id所指定的证书作为接收方证书封包(只加密,不签名), 否则将使用id所指定的证书作为签名证书, peer指定的证书作为接收方证书封包(既签名, 又加密)

* cipher：封包对称加密算法，如果没有设置，则使用PCS服务端缺省对称加密算法，取值是一个CipherAlgorithm枚举类型对象。如下：

  ```
  public enum CipherAlgorithm {
  	DES("DES"),
	DESEDE3CBC("DES-EDE3-CBC"),
	SM4CBC("SM4-CBC"),
	SM4OFB("SM4-OFB"),
	SMS4CBC("SMS4-CBC"),
	SMS4OFB("SMS4-OFB"),
	SM1CBC("SM1-CBC"),
	AES192CBC("AES-192-CBC"),
	AES256CBC("AES-256-CBC");
  }
  ```

#### 1.7 数字信封解包

```
 NetonePCS pcs = pcsClient.envelopeUnpackBuilder()
                .setId(rsakid)
                .setPasswd(pin)//可选
                .setBase64Data(data)//设置待解包的数据
                .build();
```

#### 1.8 私钥加密

ECC算法不支持私钥加密

```
 NetonePCS pcs = pcsClient.privateKeyBuilder()
                .setId(rsakid)
                .setPasswd(pin)
                .setData(data)//待加密数据
                .setEncrypt()//加密
                .build();
```

#### 1.9 私钥解密

```
 NetonePCS pcs = pcsClient.privateKeyBuilder()
                .setId(kid)
                .setPasswd(pin)
                .setBase64Data(pcs.getResult())//待解密数据
                .setDecrypt()//解密
                .build();
```

#### 1.10 公钥加密

```
 NetonePCS pcs = pcsClient.publicKeyBuilder()
                .setId(kid)
                .setData(data)
                .setEncrypt()
                .build();
```

#### 1.11 公钥解密

ECC算法不支持公钥解密

```
NetonePCS pcs = pcsClient.publicKeyBuilder()
                .setId(kid)
                .setBase64Data(pcs.getResult())
                .setDecrypt()
                .build();
```

#### 1.12 创建XML签名

XML签名不支持ECC算法, 无这方面的公开标准

```
String data = "<data>123456</data>";
        NetonePCS pcs = pcsClient.xmlSignBuilder()
                .setPasswd(pin)
                .setId(rsakid)
                .setData(data)
                .setSigmode(SignMode.enveloping)
                .setAlgo(DigestAlgorithm.SHA1)
                .build();
```

* signMode:签名模式,取值为SignMode枚举对象，定义了签名模式，缺省时为enveloped，如下

```
  public enum  SignMode {
      enveloped,
      enveloping
  }
```

#### 1.13 修改密钥访问口令

```
NetonePCS response = pcsClient.pinBuilder()
                .setId(kid)
                .setOldpwd(pin)//设置旧密码
                .setNewpwd("123456")//设置新密码
                .build();
```


### 2.SVS模块

签名验证服务(Signature Verifcation Server)提供了对证书及证书签名信息进行可信的验证服务.主要功能:

- 证书验证,包括验证证书及颁发者的有效性,证书状态的验证.(CRL/OCSP)
- 业务数据签名验证.

#### 2.1 构造SVSClient对象


```
SVSClient client = new SVSClient(String host, String port);
```
#### 2.2 验证证书

```
 NetoneResponse svs = svsClient.certificateVerifyBuilder()
                .setCert(cert)//设置证书(Base64编码)
                .setId("kid").setIdMagic(IdMagic.KID)//可选，使用id形式
                .build();
```

#### 2.3 验证PKCS#1数字签名

```
 NetoneSVS svs = svsClient.pkcs1VerifyBuilder()
                .setCert(cert) //使用证书验证
                .setId(cn).setIdmagic(IdMagic.SCN)//可选，使用id形式
                .setBase64Signature(pkcs1)//base64格式的签名
                .setData(data)//原文
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)//可选，签名摘要算法
                .build();
```

#### 2.4 验证PKCS#7数字签名

```
NetoneSVS svs = svsClient.pkcs7VerifyBuilder()
                .setBase64Pkcs7(p7Dettach)//P7签名
                .setBase64Data(data)//可选，原文，Attach模式下不需要设置
                .build();
```

#### 2.5 验证XML签名

```
 svs = svsClient.xmlSignVerifyBuilder()
                .setBase64Data(p1)//签名值
                .build();
```

#### 2.6 枚举服务端的证书

```
NetoneCertList list = svsClient
                .certificateListBuilder()
                .build();
```

### 3.TSA模块

时间戳服务(TSA Server)提供了对数据时间的可信服务.主要功能:

- tsac: 签发时间戳(RFC3161), 输入参数是摘要.

- tsav: 验证时间戳.

- tsrs: 签发时间戳(RFC3161), 输入参数是TimestampRequest



#### 3.1 构造TSAClient对象

```
TSAClient client = new TSAClient(String host, String port);
```

#### 3.2 创建时间戳

```
 NetoneTSA netoneTSA = tsaClient.tsaCreateBuilder()
                .setAlgo(DigestAlgorithm.SHA1)//可选。设置签名摘要算法
                .setData(data.getBytes())//设置签署原文
                .build();
```

#### 3.3 验证时间戳

```
 netoneTSA = tsaClient.tsaVerifyBuilder()
                .setData(data.getBytes())//设置签名原文
                .setBase64Timestamp(netoneTSA.getResult())//设置base64格式的时间戳
                .setDataType(DataType.TIMESTAMP_REQUEST) //可选，表示data为时间戳请求
                .build();
```

### 4.EAPI模块

#### 4.1 构造EapiClient对象

```
EapiClient client = new EapiClient(String host, String port);
```

#### 4.2枚举服务端证书列表

```
 NetoneCertList list = eapiClient
                .certificateListBuilder()
                .setRevoked(false)//可选，是否获取不可信证书
                .build();
```
#### 4.3上传证书

```
 NetoneResponse response = eapiClient
                .certificateUploadBuilder()
                .setRevoked(true)//可选，是否上传到不可信列表
                .setCert(cert)//要上传的证书
                .build();
```
#### 4.4删除证书

```
  NetoneResponse response = eapiClient.certificateDeleteBuilder()
                .setId("test_tsa").setIdMagic(IdMagic.SCN)//设置要删除证书的CN
                .setRevoked(true)//可选，删除不可信证书
                .build();
```
### 5.本地密码运算
#### 5.1 摘要运算
```
NetoneDigest digest = new NetoneDigest("SHA256");
digest.update(data.getBytes());
byte[] hsah = digest.digest();
```