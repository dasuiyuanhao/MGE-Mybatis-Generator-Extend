# mybatis代码生成工具(Mybatis Generator Extend)

[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/handosme/mybatis-generator-plus.svg?branch=master)](https://travis-ci.org/handosme/mybatis-generator-plus)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)](http://search.maven.org/#artifactdetails%7Corg.ihansen.mbp%7Cmybatis-generator-plus%7C1.1%7Cjar)

[mybatis-generator extend](https://github.com/dasuiyuanhao/MGE-Mybatis-Generator-Extend)

### 1.介绍:  
MGE(Mybatis-generator Extend)基于mybatis-generator-core v.1.3.2源码扩展，增加如下主要特性:
1. 扩展Mybatis-Generator，增加Service、Agent、Dto代码生成，增加分页查询方法[使用pagehelper](https://github.com/pagehelper/Mybatis-PageHelper);
```java
//分页查询demo
@Test
public void selectPageTest() throws Exception {
	OperateLogExample relationshipsExample = new OperateLogExample();
    relationshipsExample.setPagination(0L,10L);
    List<OperateLog> operateLogList = operateLogMapper.selectByExample(relationshipsExample);
    //...
```


### 2.使用方式
#### 2.1. 方式一：Eclipse 或者 idea使用插件[推荐] 
Idea的Maven配置：
```xml
<plugins>
    <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        <version>1.3.2</version>
        <configuration>
            <configurationFile>src/main/resources/mysqlGeneratorConfig.xml</configurationFile>
            <verbose>true</verbose>
            <overwrite>true</overwrite>
        </configuration>
</plugins>
```


你也可以选择直接下载jar文件，然后安装到本地maven仓库:   
v.1.1 jar file 下载地址:[mbp.jar](http://static-ali.ihansen.org/jar/mbp/1.3.2-plus/mbp.jar)       
v.1.1 sources file下载地址:[mbp-sources.jar](http://static-ali.ihansen.org/jar/mbp/1.3.2-plus/mbp-sources.jar)  
将jar安装到本地仓库的方式:
```
mvn install:install-file  -Dfile=/Users/user/download/mbp.jar  -DgroupId=org.ihansen.mbp  -DartifactId=mybatis-generator-plus -Dversion=1.1 -Dpackaging=jar
```

#### 2.2. 方式一：运行可执行jar文件[推荐] 
包含运行依赖包的可独立执行jar文件：[mbp-jar-with-dependencies.jar](http://static-ali.ihansen.org/jar/mbp/1.3.2-plus/mbp-jar-with-dependencies.jar)   
供参考的MBP配置文件: 
[MybatisGeneratorCfg.xml](https://github.com/handosme/mybatis-generator-plus/blob/master/src/test/resources/MybatisGeneratorCfg.xml)  
使用如下命令执行即可生成自动文件：
```bash
java -jar mbp-jar-with-dependencies.jar -configfile MybatisGeneratorCfg.xml -overwrite
```


### 联系方式:  
QQ 704648761
Email 704648761@qq.com


