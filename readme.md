<p align="center">
	<a href=""><img src="resource/logo/logo.png" width="45%"></a>
</p>
<p align="center">
	<strong>一个连接NetONE安全网关 SVS PCS TSA模块的工具类库</strong>
</p>

<p align="center">
  <a target="_blank" href="">
		<img src="https://img.shields.io/badge/release-v3.0.18-blue.svg" />
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

NetoneJ是一个连接`NetONE`安全模块的Java工具类库,可以在Java应用开发中方便快捷的使用如下功能：
* PCS 签名服务
* SVS 验签服务
* TSA 时间戳
* EAPI 证书操作等

## 包含的依赖库

以下内容为NetoneJ所使用的依赖库文件，如下列表：

| 依赖库   |     介绍      |版本            |下载            |
| --------|------------- |----------|--------------- |
| okhttp3      |     一个处理网络请求的框架           |3.10.0|[jar](https://repo1.maven.org/maven2/com/squareup/okhttp3/okhttp/3.10.0/okhttp-3.10.0.jar) [maven](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/3.10.0)
| okio    |     okhttp所依赖的一个io库  |1.14.0|[jar](https://repo1.maven.org/maven2/com/squareup/okio/okio/1.14.0/okio-1.14.0.jar) [maven](https://mvnrepository.com/artifact/com.squareup.okio/okio/1.14.0)
| bouncycastle-bcprov |     密码组件              |1.68|[jar](https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.68/bcprov-jdk15on-1.68.jar) [maven](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on/1.68)
| bouncycastle-bcpkix      |     密码组件                |1.68|[jar](https://repo1.maven.org/maven2/org/bouncycastle/bcpkix-jdk15on/1.68/bcpkix-jdk15on-1.68.jar) [maven](https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on/1.68)



## 下载

请<a href="https://github.com/jssyan/netonej/releases">点击下载NetoneJ最新版本的jar包</a>使用

| 依赖库   |  说明            |
| --------|------------- |
| netonej-X.X.X.jar      |打包时不包含依赖库，开发者需添加相关依赖到项目|
| netonej-X.X.X-jar-with-dependencies.jar      |打包时包含了所有依赖库，直接使用即可|


## 帮助文档

<p align="left">
	<a href="netonej_development_documentation.md">📘 接口说明文档</a>
</p>

<p align="left">
	<a href="CHANGELOG.md">📙 版本更新历史</a>
</p>

## 快速开始
##### 1.使用PCS进行签名

首先需要创建一个`PCSClient`对象，设置PCS签名服务器的地址,端口号等
```
PCSClient client = new PCSClient("192.168.10.149","9178");
```
然后便可以进行PKCS1签名、PKCS7签名、数字封包解包等
```
public void createPKCS1Signature() throws NetonejExcepption {
    String data = Base64.getEncoder().encodeToString("123456".getBytes());
    NetonePCS pcs;
    pcs = client.createPKCS1Signature(cn,pin,IdMagic.SCN,data,DataType.PLAIN);
    System.out.println(pcs.getRetBase64String());
}
```
##### 2.使用SVS进行验证签名

首先需要创建一个`SVSClient`对象，设置SVS验签服务器的地址,端口号等
```
SVSClient client = new SVSClient("192.168.10.149","9188");
```
然后可以进行PKCS1签名验证、PKCS7签名验证、证书验证等
```
public void verifyPKCS1() throws NetonejExcepption {
    String data = Base64.getEncoder().encodeToString("123456".getBytes());
    String p1 = "MEUCIQDWh1CKmCnGRlkkdzjqigWakTjhOdp53RKVYKCnzB3OWgIgSH33VLFdhIO/etvDcqRz68Q23nUgbFxV7Y9/0+tJrrk=";
    NetoneSVS svs;
    //使用证书验证
    svs = client.verifyPKCS1(data,p1,DigestAlgorithm.ECDSASM2WITHSM3,DataType.PLAIN,cert);
    //使用证书的CN项验证或者KID
    svs = client.verifyPKCS1(cn,IdMagic.SCN,data,p1,DigestAlgorithm.ECDSASM2WITHSM3,DataType.PLAIN);
    System.out.println(svs.getStatusCode());
}
```
##### 3.使用TSA进行时间戳签署
首先需要创建一个`TSAClient`对象，设置TSA时间戳服务器的地址,端口号等
```
TSAClient client = new TSAClient("192.168.10.149","9198");
```
然后可以进行时间戳签署、验证等
```
public void testGetTimestamp() throws NetonejExcepption {
    String data = Base64.getEncoder().encodeToString("123456".getBytes());
    //签署
    NetoneTSA netoneTSA = client.createTimestamp(data,DataType.PLAIN,DigestAlgorithm.SHA1);
    System.out.println(netoneTSA.getTimestampbase64());
    //验证
    netoneTSA = client.verifyTimestamp(netoneTSA.getTimestampbase64(),data,DataType.PLAIN);
    System.out.println(netoneTSA.getStatusCode());
}
```
详细使用请参阅开发文档。

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

