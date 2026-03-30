<p align="center">
	<a href=""><img src="resource/logo/logo.png" width="45%"></a>
</p>
<p align="center">
	<strong>一个连接NetONE安全网关 SVS PCS TSA模块的工具类库</strong>
</p>

<p align="center">
  <a target="_blank" href="">
		<img src="https://img.shields.io/badge/release-v3.1.0-blue.svg" />
	</a>
	<a target="_blank" href="">
		<img src="https://img.shields.io/badge/maven-3.6.0-yellowgreen.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="">
		<img src="https://travis-ci.com/dromara/hutool.svg?branch=v4-master" />
	</a>
	<a href="https://www.apache.org/licenses/LICENSE-2.0">
		<img src="https://img.shields.io/badge/License-Apache--2.0-red.svg"/>
	</a>
</p>



-------------------------------------------------------------------------------



## 简介

NetoneJ是一个连接`NetONE`安全模块的Java工具类库,可以在Java应用开发中方便快捷的使用如下服务或功能：

1.PCS 私钥密码服务
* 生成PKCS#1格式签名.
* 生成PKCS#7格式签名.
* 生成XML签名.
* 公私钥加密/解密.
* 数字信封解包、封包.

2.SVS 签名验证服务
* 数字签名验证(PKCS#1, PKCS#7等).
* 数字证书有效性验证.
* XML签名验证.

3.TSA 时间戳服务
* RFC3161时间戳签发和验证

4.EAPI 开放API服务
* 证书管理(获取、上传、删除)
* 证书黑名单/白名单

5.本地PKI密码运算
* 摘要运算

## 包含的依赖库

以下内容为NetoneJ所使用的依赖库，如列表：

| 依赖库   |     介绍      |版本            |下载            |
| --------|------------- |----------|--------------- |
| bouncycastle-bcprov |     密码组件              |1.68|[jar](https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.68/bcprov-jdk15on-1.68.jar) [maven](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on/1.68)
| bouncycastle-bcpkix      |     密码组件                |1.68|[jar](https://repo1.maven.org/maven2/org/bouncycastle/bcpkix-jdk15on/1.68/bcpkix-jdk15on-1.68.jar) [maven](https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on/1.68)



## 下载NetoneJ

<a href="https://github.com/jssyan/netonej/releases">点击前往NetoneJ发布记录选择对应的版本进行下载</a>

| NetoneJ   | 说明                       |
| -----|--------------------------|
| netonej-X.X.X.jar   | 该JAR不包含依赖库，开发者需添加相关依赖到项目 |
| netonej-X.X.X-jar-with-dependencies.jar   | 该JAR包含了依赖库，直接引用到项目使用     |


## 帮助文档

<p align="left">
	<a href="netonej_development_documentation.md">📘 接口说明文档</a>
</p>


## 快速开始
##### 1.使用PCS进行签名

首先需要创建一个`PCSClient`对象，设置PCS签名服务器的地址,端口号等
```
PCSClient client = new PCSClient("192.168.10.149","9178");
```
然后便可以进行PKCS1签名、PKCS7签名、数字封包解包等，如PKCS1签名示例如下：
```
public void createPKCS1Signature() throws NetonejException {
        String data = "123";
        NetonePCS pcs = pcsClient.pkcs1Builder()
                .setResponseformat("1")
                .setPasswd(pin)
                .setId(cn)
                .setIdmagic(IdMagic.SCN)
                .setData(data.getBytes())
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)
                .build();
        System.out.println(pcs.getResult());
        System.out.println(pcs.getSingerCert());
    }
```
PKCS7签名示例如下：
```aidl
public void createPKCS7Signature() throws NetonejException {
        String data = "hello";
        NetonePCS pcs = pcsClient.pkcs7Builder().setPasswd(pin)
                .setResponseformat("1")
                .setId(kid)
                .setIdmagic(IdMagic.CID)
                .setData(data.getBytes())
                .setAlgo(DigestAlgorithm.ECDSASM2)
                .setAttach(false)
                .build();
        System.out.println(pcs.getResult());
    }
```
##### 2.使用SVS进行验证签名

首先需要创建一个`SVSClient`对象，设置SVS验签服务器的地址,端口号等
```
SVSClient client = new SVSClient("192.168.10.149","9188");
```
然后可以进行PKCS1签名验证、PKCS7签名验证、证书验证等
```
public void verifyPKCS1() throws NetonejException {
        String data = "123";
        String pkcs1 = "MEQCICLf3sCOu3d3nwedhoAKKUK5N9cIUOAkTrFlJmygzq/VAiAlSikG9z7bOBCeRrjkGSuN8it+pFWTQxOu9hAExQZRWg==";
        NetoneSVS svs;
        svs = svsClient.pkcs1VerifyBuilder()
                //.setCert(cert) //使用证书验证
                .setId(cn)
                .setIdmagic(IdMagic.SCN)
                .setBase64Signature(pkcs1)
                .setData(data)
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)
                .build();
        System.out.println(svs.getStatusCode());
        System.out.println(svs.getCertificate().getSubject());
    }
```
##### 3.使用TSA进行时间戳签署
首先需要创建一个`TSAClient`对象，设置TSA时间戳服务器的地址,端口号等
```
TSAClient client = new TSAClient("192.168.10.149","9198");
```
然后可以进行时间戳签署、验证等
```
public void testGetTimestamp() throws NetonejException {
        String data = "123456";
        //签署
        NetoneTSA netoneTSA = tsaClient.tsaCreateBuilder()
                .setAlgo(DigestAlgorithm.SHA1)
                .setData(data.getBytes())
                .build();
        System.out.println(netoneTSA.getResult());
        //验证
        netoneTSA = tsaClient.tsaVerifyBuilder()
                .setData(data.getBytes())
                .setBase64Timestamp(netoneTSA.getResult())
                .build();
        System.out.println(netoneTSA.getStatusCode());
}
```
详细使用请参阅开发文档或单元测试代码。

## 测试、调试

NetonJ工程中提供了单元测试代码，供开发者参考与调试

方式一、.可通过 Eclipse 或者 Idea 等开发工具运行

- 在电脑安装好 Java、Maven 等开发环境
- 将源码下载/git clone到本地、并导入 IntelliJ IDEA 中
- 在源码的 /src/test目录下找到想要测试或调试的类，修改服务host、port后即可执行相关测试函数。



方式二、.下载NetoneJ以及相关依赖,依赖到您的项目中,拷贝源码中 /src/test目录下测试代码进行测试



## 提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本、Netone版本和NetoneJ版本以及相关依赖库版本。

- [Github issue](https://github.com/jssyan/netonej/issues)

