# Readhub-Android

## 项目简介

- 应用介绍

	**该应用非无码科技官方产品，仅是基于个人兴趣开发的一款浏览Readhub内容的Android应用。app内所有资讯内容均来自无码科技产品 Readhub。**

- Readhub 介绍：
 
	Readhub 是无码科技团队的一个副产品。这个产品有什么用？每天花几分钟了解一下互联网行业里发生的事情。行业里，每天值得关注的事情，可能并不超过 5 件，其他的信息都是可读可不读的。无码科技团队想通过技术和产品的手段，把每天获取资讯这个事情做得优雅一点。

	以上内容均来自微信公众号「小道消息」，关于 Readhub 的更多介绍，可以点击链接[无码科技发布第一款产品](https://mp.weixin.qq.com/s?__biz=MjM5ODIyMTE0MA==&mid=2650969398&idx=1&sn=70c44b9bb994d9a8d98453b97555890b&chksm=bd38310d8a4fb81b878d2a252e813b304873412d2131d7e4787efb52f68ca8676eaad89bd245&scene=0&key=afcd625aa1116852d5c1c05e8cc727fbb36dd1a1b29b2d479b7102b73bafb061942b0a8684a5d01354a97047e79d47a8f18b6757d69cfc201f1088dbe061eef3a801718c08ecf740af13f55f3f3e7e65&ascene=0&uin=OTk0NDIyNDgw&devicetype=iMac14%2C2+OSX+OSX+10.12.4+build(16E195)&version=12020610&nettype=WIFI&fontScale=100&pass_ticket=z4VWnrxOnq2HBP%2BrcsexXO%2F5kXUdPvn9hiTeEgb9DUGwzmC8y%2BNyqBW3b9SjanRq) 查看！

## 下载体验

### 下载地址

- 扫码下载

	![扫码下载摇吧](./demo/readhub_download.png )

- 直接下载

	[https://github.com/bihe0832/readhub-android/blob/master/demo/readhub-release-newer.apk](https://github.com/bihe0832/readhub-android/blob/master/demo/readhub-release-newer.apk)

### 应用效果

![应用效果](./demo/readhub.gif )

## 运行代码

### 代码说明

- 该项目是结合个人之前开源的一个apk项目(shakeba:[https://github.com/bihe0832/Shakeba](https://github.com/bihe0832/Shakeba))的代码修改而来，因此里面大量文件的命名包含shakeba字段，后续逐渐整理。

### 运行方法

如何修改配置及运行工程，请参考本人博客：[终端基于gradle的开源项目运行环境配置指引](
http://blog.bihe0832.com/android-as-gradle-config.html)

### 待完善项
	
#### 待增加内容

- 增加新内容主动推送
- 增加数据本地缓存
- 增加已查看内容标记
- 增加上次查看历史记录

## 更新历史

### v1.0

- 2017-05-11：完成app框架，使用webview内置官网实现第一版

- 2017-05-12：实现Native的基本内容，目前仅能展示摘要信息

- 2017-05-13：优化webview的跳转，增加资讯信息点击的交互，点击展示或跳转，长按分享

- 2017-05-15：增加专题内容隐藏、展示、跳转

### v1.1

- 2017-06-07：增加开发者资讯栏目，优化无网络或者网络异常时的用户体验