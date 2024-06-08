### 基于SpringBoot + Vue停车场车位出租系统.


#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok

>此项目的研究内容主要是建立基于Web的停车场管理系统，该系统面向停车场管理员、停车场用户，不同的用户登录系统获得的权限不同，使用的功能也不同，获取的信息也有所差异。

停车场管理员可以通过系统登录并管理停车场的日常运营，包括车辆进出管理、停车位管理、收费管理等。停车场工作人员主要负责车辆进出管理，监控停车场的车辆进出情况，使用系统进行车辆识别、放行、收费等操作，且工作人员可以实时查看停车场的停车位情况，并在需要时进行停车位的调整和管理。停车场用户功能有通过系统注册账号并登录，进行停车预订、支付等操；可以通过系统查询停车场的空闲停车位信息，并进行预订操作，选择停车场和停车位，预定停车时间；系统根据用户停车时间和停车场收费标准计算停车费用，用户可以通过系统选择支付方式进行支付。
具体如下：

> 系统公告、用户消息、订单管理、车位预约、车位管理、车位状态、用户管理、车辆管理、数据统计、月份统计、车位实时状态、我的信息、个人信息、我的车辆、车位预约、我的订单、我的消息、缴费成功


#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 后端启动方式

1.首先启动redis，进入redis目录终端。输入redis-server回车
2.导入sql文件，修改数据库与redis连接配置
3.idea中启动后端项目

#### 默认后台账户密码
[管理员]
admin
1234qwer


#### 项目截图

|  |  |
|---------------------|---------------------|
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713028991726.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029198442.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713028983586.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029187339.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713028963155.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029165546.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713028934008.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029141024.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713028919536.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029130546.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713028907072.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029122077.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713028896699.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029101970.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029224651.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029059420.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029215949.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1713029044370.png) |



#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解`

#### 其它资源

[2024年答辩顺利通过](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年答辩顺利通过](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年答辩通过率100%](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)


#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

More info: [悲伤的橘子树](https://berserker287.github.io/)

