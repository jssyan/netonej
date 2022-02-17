<p align="center">
	<a href=""><img src="resource/logo/logo.png" width="45%"></a>
</p>
<p align="center">
	<strong>一个连接NetONE安全网关 SVS PCS TSA模块的工具类库</strong>
</p>

<p align="center">
  <a target="_blank" href="">
		<img src="https://img.shields.io/badge/release-v3.0.17-blue.svg" />
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

NetoneJ是一个连接先安`NetONE`安全网关 SVS PCS TSA EAPI等模块的工具类库，通过对各模块提供的API进行封装，屏蔽了底层调用细节，降低了使用相关API的学习成本，可以提高集成效率。



## 包含的依赖库

以下内容为NetoneJ所使用的依赖库文件，如下列表：

| 依赖库   |     介绍      |版本            |下载            |
| --------|------------- |----------|--------------- |
| okhttp3      |     一个处理网络请求的框架           |3.10.0|[jar](https://repo1.maven.org/maven2/com/squareup/okhttp3/okhttp/3.10.0/okhttp-3.10.0.jar) [maven](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/3.10.0)
| okio    |     okhttp所依赖的一个io库  |1.14.0|[jar](https://repo1.maven.org/maven2/com/squareup/okio/okio/1.14.0/okio-1.14.0.jar) [maven](https://mvnrepository.com/artifact/com.squareup.okio/okio/1.14.0)
| bouncycastle-bcprov |     密码组件              |1.68|[jar](https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.68/bcprov-jdk15on-1.68.jar) [maven](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on/1.68)
| bouncycastle-bcpkix      |     密码组件                |1.68|[jar](https://repo1.maven.org/maven2/org/bouncycastle/bcpkix-jdk15on/1.68/bcpkix-jdk15on-1.68.jar) [maven](https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on/1.68)



## 下载

请下载最新版本的jar包使用

| 依赖库   |  说明            |
| --------|------------- |
| netonej-X.X.X.jar      |打包时不包含依赖库|
| netonej-X.X.X-jar-with-dependencies.jar      |打包时包含了所有依赖库|

<p align="left">
	<a href="https://github.com/jssyan/netonej/releases">点击下载NetoneJ最新包</a>
</p>

## 帮助文档

<p align="left">
	<a href="netonej_development_documentation.md">📘 接口说明文档</a>
</p>

<p align="left">
	<a href="resource/doc/index.html">📝 参考API</a>
</p>

<p align="left">
	<a href="CHANGELOG.md">📙 版本更新历史</a>
</p>


## 测试、调试

NetonJ工程中提供了大量的单元测试代码，供开发者参考与调试

方式一、.可通过 Eclipse 或者 Idea 等开发工具运行

- 在电脑安装好 Java、Maven 等开发环境
- 将源码下载/git clone到本地、并导入 eclipse 或者 idea
- 在项目的 /src/test目录下找到想要测试或调试的类，修改服务host、port后即可执行相关测试函数。



方式二、.下载NetoneJ以及相关依赖,依赖到您的项目中,拷贝项目的 /src/test目录下测试代码进行测试



## 提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本、Netone版本和NetoneJ版本以及相关依赖库版本。

- [Github issue](https://github.com/jssyan/netonej/issues)

