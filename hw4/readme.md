# 第4次作业设计报告

通过调用[WebService接口](http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl)，实现了查找ip地址来源的功能

执行命令

```
wsimport -keep -p wsproxy http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl

javac .\wsclient\*.java
java wsclient.CodeInfoClient [需要查询的ip地址]
```

执行结果

![](pic.png)

