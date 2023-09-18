> kxmall 针对中小商户、企业和个人学习者开发。使用Java编码，采用SpringBoot、Mybatis-Plus等易用框架，适合个人学习研究。同时支持单机部署、集群部署，用户与店铺范围动态定位，中小商户企业可根据业务动态扩容。kxmall使用uniapp前端框架，可同时编译到 微信小程序、H5、Android App、iOS App等几个平台，可为中小商户企业节约大量维护成本。也可支撑中小商户企业前期平台横扩需求。

---
QQ讨论群：865607763 (已满)

QQ讨论群：432259672 (进群前，请在网页右上角点star)

#### 数据库初始化sql文件，请进入讨论交流群，群文件自行下载，欢迎讨论与交流
---
#### 优先更新地址

kxmall项目结构:

- Java 后端服务
    - kxmall-admin: 启动器（打包打这个就行）
    - kxmall-admin-api: 提供管理员管理系统的WebApi
    - kxmall-app-api: 提供APP、小程序、H5用户请求的WebApi
    - kxmall-rider-api: 提供骑手APP、小程序、H5用户请求的WebApi
    - kxmall-biz: 提供通用业务代码
    - kxmall-data: 提供数据模型以及数据访问层封装
    - kxmall-core: 提供注解、工具类等
    
- Vue 前端页面
    - kxmall-admin-ui: 基于element-ui的后台管理页面
    - kxmall-app-ui: 基于uniapp的小程序、H5、APP前端代码
    - kxmall-rider-ui: 基于uniapp的小程序、H5、APP骑手代码
  
- kxmall代码生成器-自动生成代码（独立项目）
    - reverse-kxmall-launch: 自动生成代码启动器
    - reverse-kxmall-ui：自动生成代码前端
    - 下载地址 https://download.csdn.net/download/qq_38377190/87213044
- 常见问题汇总（持续更新中）
  - 常见问题总体解决方案
  - [点我进入](https://gitee.com/zhengkaixing/kxmall/wikis/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98/%E5%89%8D%E7%AB%AFbuild%E6%89%93%E5%8C%85%E4%B8%80%E7%9B%B4%E5%8D%A1%E4%BD%8F)&nbsp;&nbsp;
  
- sql: 数据库初始化SQL脚本

* 阿里云折扣场：[点我进入](https://www.aliyun.com/minisite/goods?userCode=gclm7a7u)&nbsp;&nbsp;
* 腾讯云秒杀场：[点我进入](https://url.cn/G0fq6Mm5)&nbsp;&nbsp;

#### 数据库初始化sql文件，请进入讨论交流群，群文件自行下载，欢迎讨论与交流
---
#### 优先更新地址

[01-kxmall源码地址](https://gitee.com/zhengkaixing/kxmall.git) https://gitee.com/zhengkaixing/kxmall.git

---

#### 用户端系统演示

下面是微信小程序真机模式调试的界面，可Android安装Apk,也可同时支持苹果。
在这基础上，还增加了H5。可内置到微信公众号上，变成公众号商城！尽情体验！


---
- H5客户端（可打包成小程序、APP）
  - 演示地址: [https://h5.kxmall.vip](https://h5.kxmall.vip)
  - 登录名:13333333333 验证码:666666 （访问请打开浏览器F12开发模式,使用手机模式进行操作）
  - 客户端由于调用地图需要https，所以程序目前固定id为11仓库
  - 已跳过支付模块，可正常体验操作流程
- 微信小程序-体验（可打包成小程序、APP）
  - 已跳过支付模块，可正常体验操作流程（注意：需要自己手动获取一下定位，方可正常使用。）
  - ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/weixin-mini2.jpg)
- Pages

| 河禾生鲜 | 河禾生鲜 | 河禾生鲜 |
| :----: | :----: | :----: |
| ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-1.jpeg)  | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-2.jpeg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-3.jpeg) |
| ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-4.jpeg)  | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-5.jpeg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-6.jpeg) |

| 商家订单推送 | 骑手订单推送 |
| :----: | :----: |
| ![订单推送](https://kxmalls.oss-cn-hangzhou.aliyuncs.com/kxmalls10.jpg) |  ![订单推送](https://kxmalls.oss-cn-hangzhou.aliyuncs.com/kxmalls9.jpg)

#### 后台端系统演示

使用免费开源框架vue-element-admin，基于element-ui的后台管理页面！尽情体验！


---
- Admin后台
  - 演示地址: [http://www.kxmall.vip](http://www.kxmall.vip)
  - 登录名:guest 密码:123456 (guest仅有只读权限，无读配置权限)
  - 登录名(超级管理员):admin (需要体验的，密码可以关注公众号，回复:2)
  - ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/PublicQr.jpg)
- Pages
 
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-1.png)  
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-2.png)  
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-3.png)   
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-4.jpg)   


#### 骑手端系统演示

---
- h5骑手后台（可打包成小程序、APP）
  - 演示地址: [http://rider.kxmall.vip](http://rider.kxmall.vip)
  - 登录名:16666666666 验证码:123456 （访问请打开浏览器F12开发模式,使用手机模式进行操作）
- 微信小程序-体验（可打包成小程序、APP）
  - 微信一键登录（注意：需要进入管理后台进行审核，方可正常使用。）
  - ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/weixin-mini3.jpg)
- Pages

| 河禾生鲜 | 河禾生鲜 | 河禾生鲜 |
| :----: | :----: | :----: |
| ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-rider-1.jpg)  | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-rider-2.jpg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-rider-3.jpg) |


#### 项目部署方式

>项目部署
##### ⓪ 服务器推荐
服务器可根据自身业务来选购，单机环境推荐2C4G
* 阿里云折扣场：[点我进入](https://www.aliyun.com/minisite/goods?userCode=gclm7a7u)&nbsp;&nbsp;
* 腾讯云秒杀场：[点我进入](https://curl.qcloud.com/XslPhodG)&nbsp;&nbsp;

##### ① 基础运行环境

| 运行环境 | 版本号 |
|:--------|:--------|
|  MySQL   |  5.7（推荐）   |
|  JDK   |  1.8（推荐）   |
|  Redis   |  4.0.1（其他也可以）   |
|  Nginx  |  只要Web容器就可以了  |

Redis安装可直接使用yum安装 
	
	yum install redis

安装完成后使用 redis-cli 命令，若能进入，则表示redis安装完成

1.服务器安装必备软件[JDK | mysql | Redis | Nginx]

本地部署文档

[kxmall本地项目启动文档](doc/kxmall本地项目启动文档.doc)
****
========================分割线===========================
****
### 生鲜多商户商城上线拉！！！！！
---
| kxmalls客户端 | kxmalls骑手端 |
| :----: | :----: |
| ![生鲜多商户](https://kxmalls.oss-cn-hangzhou.aliyuncs.com/kxmalls1.png)  |  ![生鲜多商户](https://kxmalls.oss-cn-hangzhou.aliyuncs.com/kxmalls2.png) |

[kxmalls源码地址](https://gitee.com/zhengkaixing/kxmalls.git) https://gitee.com/zhengkaixing/kxmalls.git

体验账号（了解加QQ群：587493946）（体验流程，可使用余额支付）

客户端 http://www.kxmalls.vip

商家账号 账号：小黑水果生鲜  密码： 123456

超级管理员  admin 账号密码 （私聊群主）

客户端   http://h5.kxmalls.vip   账号 16666666666  密码 123456

骑手端   http://rider.kxmalls.vip   账号 13333333333   密码 123456

#### 版权声明

个人学习可免费使用，进QQ讨论群（432259672）联系群主。


### SaaS服务

正在研发中...
