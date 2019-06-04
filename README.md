# FlightReservation
简单的航班预订系统，实现了登录界面、航班的检索、信息的录入、用户订单信息报表、用户取票通知和凭证报表、管理员航班报表、管理员录入飞机信息、管理员录入航班信息、管理员录入座位信息等功能。


## 预览

### 主界面
![avatar](https://github.com/Troubledesigner/FlightReservation/blob/master/Preview/main.png)

### 选择航班
![avatar](https://github.com/Troubledesigner/FlightReservation/blob/master/Preview/flightTable.png)

### 订单详情
![avatar](https://github.com/Troubledesigner/FlightReservation/blob/master/Preview/orderTable.png)

## 安装方法

本系统使用Java语言开发，在IDEA集成开发环境下进行。

Java JDK 1.8.0.211

如果想要尝试使用本系统，需要的库包括：

1. charm-glisten-4.4.1
2. controlsfx-8.40.14
3. jfoenix-8.0.8
4. mysql-connector-java-5.1.47

需要mySQL数据库，库中必须包含表：

序号|表名
-|-
1|AirCraft
2|Flight
3|Passenger
4|Seats
5|T_Bill
6|T_Order
7|T_Order_Ticket
8|T_User
9|T_User_Order
10|Ticket

*建表语句见文件mysql.sql*

*创建触发器的语句见trigger.sql*

*创建视图的语句见view.sql*

## Copyright
仅供参考，请勿直接照搬照抄。





