# sip-proxy

[![Maven Central](https://img.shields.io/maven-central/v/io.github.lunasaw/sip-proxy)](https://mvnrepository.com/artifact/io.github.lunasaw/sip-common)
[![GitHub license](https://img.shields.io/badge/MIT_License-blue.svg)](https://raw.githubusercontent.com/lunasaw/gb28181-proxy/master/LICENSE)

[www.isluna.ml](http://lunasaw.github.io)

基于sip实现gb28181的通信框架，区分client和server。以便于快速构建发起SIP请求和处理响应。支持级联，告警，订阅等标准协议信令服务。项目不仅限于gb28181协议。也可以利用封装的SIP方法处理其他协议。

## 实现功能

- [x] SIP 通用请求构建
- [x] spring-boot starter自动配置
    - [x] 端口监听
        - [x] UDP 监听
        - [x] TCP 监听
    - [x] 基于javax的xml转化，写bean的方式写xml
- [x] GB28181
    - [x] Server
        - [x] 设备注册
        - [ ] 目录订阅
        - [x] 设备认证
        - [x] 设备控制
        - [x] 云台控制(PTZ)
        - [x] 安放告警
        - [x] 设备查询
        - [x] 实时点播
        - [x] 视频回放点播
        - [x] 视频回放控制
        - [ ] 设备移动订阅
    - [x] Client
        - [x] 设备注册
        - [ ] 目录更新上报
        - [x] 设备控制响应
        - [x] 告警上报
        - [x] 事件推送
        - [x] 设备状态回复
        - [x] 设备录像上报
        - [x] 心跳检测
        - [x] 实时点播响应
        - [x] 实时回放控制响应
        - [x] 视频回放点播
- [ ] 基于gb28181-proxy 实现平台级操作。搭建信令服务平台
- [ ] 基于流媒体搭建完整的视频监控级联平台  [voglander](https://github.com/lunasaw/voglander) 进行中
- [ ] 基于ZLM的start框架 [zlm-spring-boot-starter](https://github.com/lunasaw/zlm-spring-boot-starter) 进行中
- [ ] 基于客户端搭建本地NVR平台管理
- [ ] wike教程
- [ ] 其他。。。

# 如何使用

<a href="https://lunasaw.github.io/gb28181-proxy/" target="_blank">文档链接</a>

> 全量包

```xml

<dependency>
    <groupId>io.github.lunasaw</groupId>
    <artifactId>gb28181-proxy</artifactId>
    <version>${last.version}</version>
</dependency>
```

> 按需引入 基于sip的请求封装包。注意：因为涉及到github action打包识别问题。故sip-common永远比client和sever小一个版本

```xml

<dependency>
    <groupId>io.github.lunasaw</groupId>
    <artifactId>sip-common</artifactId>
    <version>${last.version}</version>
</dependency>
```

> gb28181设备client

```xml
<dependency>
    <groupId>io.github.lunasaw</groupId>
    <artifactId>gb28181-client</artifactId>
    <version>${last.version}</version>
</dependency>
```

> sip服务器server

```xml
<dependency>
    <groupId>io.github.lunasaw</groupId>
    <artifactId>gb28181-server</artifactId>
    <version>${last.version}</version>
</dependency>
```

# 代码规范

- 后端使用同一份代码格式化膜模板ali-code-style.xml，ecplise直接导入使用，idea使用Eclipse Code Formatter插件配置xml后使用。
- 前端代码使用vs插件的Beautify格式化，缩进使用TAB
- 后端代码非特殊情况准守P3C插件规范
- 注释要尽可能完整明晰，提交的代码必须要先格式化
- xml文件和前端一样，使用TAB缩进
