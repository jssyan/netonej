#NetONEJ示例代码
NetONEJ是用于连接NetONE安全网关 SVS PCS TSA模块的工具包，集成netonej-$version.jar，可以快速方便的调用NetONE安全网关API执行数字签名、验证、加密、解密、数字信封等PKI操作。

1. bin路径下，提供了将依赖包打包在一起的形式：**netonej-$version-jar-with-dependencies.jar**,不需要额外集成其他依赖即可使用。
2. bin路径下，也提供了netonej jar与依赖包分离的形式：netonej-$version.jar以及所依赖的其他jar，依赖jar放在 bin/dependencies 下，需要同时集成netonej-$version.jar及dependencies下的jar才能使用
3. test路径下提供了测试代码，可根据示例进行代码编写。
