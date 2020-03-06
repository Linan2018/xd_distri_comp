# 第2次作业设计报告

## 1. 设计思想

### 1.1 应用层协议

**请求数据包格式**

**|**  操作数1  **|**  操作符  **|**  操作数2  **|**

示例：`byte("123+456")`

**注**：只支持两操作数、只支持加减乘除运算

**请求数据包格式**

**|**  计算结果  **|**

示例：`byte("579")`

### 1.2 服务器端设计逻辑

![](pic.png)

服务器端程序共包含4个主要的类分别是`Receiver`类、`Worker`类、`Process`类和`Sender`类，以及一个用于计数的`TransactionCounter`类

**`Receiver`类**用于监听6788号端口，一旦收到请求数据包，就将其放入`inQueue`队列

**`Worker`类**用于将`inQueue`队列中的请求数据包取出并负责创建新的线程（`Process`类）处理请求数据包

**`Process`类**在`Worker`对象中实例化，负责处理请求数据包并将结果打包成响应数据包，最后放入`outQueue`队列

**`Sender`类**用于将`outQueue`队列中的响应数据包取出，经6789号端口发出

**`TransactionCounter`类**在`Worker`对象中实例化，负责记录已完成的任务数，`Process`对象会调用它的public方法`increase`，为保证线程安全`increase`放大被声明为`synchronized`

## 2. 问题

若要在服务器端程序动态地显示`inQueue`队列和`outQueue`队列中元素的个数，需要调用`inQueue.size()`和`outQueue.size()`，但是，由于多个线程并发执行，获取到的元素个数可能是已经变化的结果，产生类似“写后读”的问题。

以`inQueue.size()`会产生的问题为例：

当`Receiver`线程收到新的请求数据包时，会将其放入`inQueue`队列，`inQueue.size()`增加1

当`inQueue`队列非空时，`Worker`线程会从`inQueue`队列中取出请求数据包，`inQueue.size()`减少1

分析`Receiver`类和`Worker`类核心代码

![](c.png)

若程序执行顺序如情形1所示，则语句2执行结果正确；若程序执行顺序如情形2所示，则语句2执行结果错误。

所以，在多线程并发执行的情况下，能够准确显示队列元素个数是可遇而不可求的。

**解决方案**

类似于`TransactionCounter`类，将队列作为互斥资源，同时只能有一个线程访问队列，但是这样做会产生另一个问题：多个用于处理请求数据包的`Process`线程需要将处理后形成响应数据包放入`outQueue`队列，如果`outQueue`队列是互斥资源，那么会导致大量`Process`线程阻塞，降低处理效率。

然而，现实生活中，用户不在乎服务器端显示了什么，服务器端也并不需要时刻检查队列长度；一种可行的方案是，隔（和线程切换时间相比）相当长一段时间检查一次队列长度来评估网络拥塞的情况，尽管可能出现个别的误差，但相较于时刻检查队列长度或将队列设为互斥资源造成的处理效率下降仍可接受。

## 3. 实验运行
运行`run_this.bat`
将打开4个终端，其中1个运行服务器程序，3个运行客户端程序，将累计测试10种请求：

```
11.11+11
0.1!10
1.1*10
111/11
2.2@222
22-2.2
22--
3+3+3
3.3+
33/3
```

结果：

```
22.11
Wrong input format! (only supports +, -, *, /)
11.0
10.090909
Wrong input format! (only supports +, -, *, /)
19.8
Wrong input format! (only supports two operands)
Wrong input format! (only supports two operands)
Wrong input format! (only supports two operands)
11.0
```

