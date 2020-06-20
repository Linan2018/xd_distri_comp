# 第6次作业设计报告

## 题目一

### 1.  计算每个学生必修课的平均成绩

#### Map阶段

首先将一行字符串以`,`分割，例如`"170315班,史伦泰,计算机图形学,选修,82"`分为`{"170315班","史伦泰","计算机图形学","选修","82"}`

```java
String[] split = value.toString().split(",");
String name = split[1];
String type = split[3];
int grade = Integer.parseInt(split[4]);
```

如果是必修课，则向`context`写入`<课程名, 成绩>`

```java
if (type.equals("必修")) {
    context.write(new Text(name), new IntWritable(grade));
}
```

#### Combine阶段

使用默认的聚合方法，返回`<课程名, [成绩1, 成绩2, 成绩3...]>`

#### Reduce阶段

首先统计分数的个数总分数

```java
int sum = 0;
int count = 0;
for (IntWritable val : values) {
    sum += val.get();
    count++;
}
```

计算平均成绩并向`context`写入`<课程名, 平均成绩>`

```java
int a = sum / count;
IntWritable result = new IntWritable();
result.set(a);
context.write(key, result);
```

### 2. 按科目统计每个班的平均成绩

#### Map阶段

首先将一行字符串以`,`分割，例如`"170315班,史伦泰,计算机图形学,选修,82"`分为`{"170315班","史伦泰","计算机图形学","选修","82"}`

```java
String[] split = value.toString().split(",");
String cls = split[0];
String subject = split[2];
int grade = Integer.parseInt(split[4]);
```

如果是必修课，则向`context`写入`<班级-科目, 成绩>`

```java
context.write(new Text(cls + "-" + subject), new IntWritable(grade));
```

#### Combine阶段

使用默认的聚合方法，返回`<班级-科目, [成绩1, 成绩2, 成绩3...]>`

#### Reduce阶段

**\* **与"计算每个学生必修课的平均成绩"中的Reduce阶段相同

## 题目二

### 找出所有具有grandchild-grandparent关系的人名组

文件中的父子关系可以用树形图表示，如

```
a,b
b,c
b,d
d,e
```

可表示为

![image-20200603143808823](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20200603143808823.png)

可以发现祖孙关系有

```
d	a
c	a
e	b
```

#### Map阶段

首先将一行字符串以`,`分割，例如`"a,b"`分为`{"a","b"}`

```java
String[] split = value.toString().split(",");
```

向`context`写入`<父, -子>`和`<子, -父>`（用`+`和`-`区分父子关系）

```java
context.write(new Text(split[0]), new Text("-" + split[1]));
context.write(new Text(split[1]), new Text("+" + split[0]));
```



![image-20200603144747462](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20200603144747462.png)

#### Combine阶段

使用默认的聚合方法

![image-20200603145131053](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20200603145131053.png)

#### Reduce阶段

将每一项的`value`（列表）中含有`+`和含有`-`的名称组合起来，形成一条结果

```java
ArrayList<Text> grandparent = new ArrayList<Text>();
ArrayList<Text> grandchild = new ArrayList<Text>();

for (Text t : values) {
    String s = t.toString();
    if (s.startsWith("+")) {
        grandparent.add(new Text(s.substring(1)));
    }
    else {
        grandchild.add(new Text(s.substring(1)));
    }
}

for (int i = 0; i < grandchild.size(); i++) {
    for (int j = 0; j < grandparent.size(); j++) {
        context.write(grandchild.get(i), grandparent.get(j));
    }
}
```

![image-20200603145610086](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20200603145610086.png)

## 运行方式

以题目一为例

启动HDFS

```bash
start-all
```

编译运行

```bash
setcp
javac Problem1.java -encoding utf-8
jar cvf mrtest.jar *.class
hadoop fs -mkdir /input1
hadoop fs -put ./input_file_1.txt /input1
hadoop jar ./mrtest.jar Problem1 /input1 /output1
```

输出结果

```bash
chcp 65001
hadoop fs -cat /output1/part-r-00000 >output1.txt 2>&1
```

