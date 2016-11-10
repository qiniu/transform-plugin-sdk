# transform-plugin-sdk

## 添加依赖
maven方式:

```
<dependency>
  <groupId>com.qiniu</groupId>
  <artifactId>transform-plugin-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```
或者sbt方式:

```
"com.qiniu" %% "transform-plugin-sdk" % 1.0.0
```

或者gradle方式：

```
compile 'com.qiniu:transform-plugin-sdk:1.0.0'
```

## plugin编写

### Java语言编写plugin

* 自定义 `plugin` 需要继承 `JavaParser` 抽象类,  [JavaParser example](https://github.com/qiniu/transform-plugin-sdk/blob/develop/src/main/scala/com/qiniu/pipeline/sdk/examples/AppendDateTimeJavaParser.java)。
* 重写 `JavaParser` 中 `parse` 方法， `parse` 方法支持单行到单行或者多行间的转换，函数返回值为 `List<Row>`。
* **注意:** parse的返回值中每行必须由原始行数据以及`TransformSpec`中`plugin`的`output`所有字段对应的值共同组成，例如：

`TransformSpec`中`plugin`如下， 其中`com.qiniu.AppendTwoFieldsJavaParser`功能是为每行数据添加2个字段：

```
"plugin":   {
     "name": "com.qiniu.AppendTwoFieldsJavaParser",
     "output": [
           {
              "name":"Field1",
              "type":"string"
           },{
              "name":"Field2",
              "type":"string"
           }
   	]
  }
```
那么`parse` 返回值 `List<Row>`为：

``` java
// 原始每行数据
List<Object> newRow = new ArrayList<Object>(Arrays.asList(((GenericRow)originData).values()));
// 根据plugin中output的字段顺序Field1和Field2，依次在原始行数据后追加Field1和Field2相应值Value1和Value2
newRow.add(Value1);
newRow.add(Value2);
// parse方法的返回值result需为
List<Row> result = new ArrayList<Row>();
result.add(new GenericRow(newRow.toArray()));
```
，上面`parse`是单行到单行间的转换，每行返回值是在输入行数据后面按顺序继续追加`output`中所有字段。对于单行到多行转换的parse，每行也需按顺序继续追加`output`中所有字段。


### Scala语言编写plugin

* 自定义 `plugin` 需要继承 `ScalaParser` 抽象类，[ScalaParser example](https://github.com/qiniu/transform-plugin-sdk/blob/develop/src/main/scala/com/qiniu/pipeline/sdk/examples/AppendDateTimeParser.scala)。
* 重写 `ScalaParser` 中 `parse` 方法， `parse` 方法支持单行到单行或者多行间的转换，函数返回值为 `Seq[Row]`。
* **注意:** parse的返回值中必须由原始行数据以及在`TransformSpec`中`plugin`的`output`所有字段对应的值共同组成，例如：

`TransformSpec`中`plugin`如下， 其中`com.qiniu.AppendTwoFieldsScalaParser`功能是为每行数据添加2个字段：

```
"plugin":   {
     "name": "com.qiniu.AppendTwoFieldsScalaParser",
     "output": [
           {
              "name":"Field1",
              "type":"string"
           },{
              "name":"Field2",
              "type":"string"
           }
   	]
  }
```
那么`parse` 返回值 `Seq[Row]`为：

``` scala
// 原始每行数据
val orignalRow = originData.toSeq
// 根据plugin中output的字段顺序Field1和Field2，依次在原始行数据后追加Field1和Field2相应值Value1和Value2
val newRow = orignalRow ++ Seq(Value1, Value2)
// parse方法的返回值result需为
val result = Seq(Row.fromSeq(newRow))
```
，上面`parse`是单行到单行间的转换，每行返回值是在输入行数据后面按顺序继续追加`output`中所有字段。对于是单行到多行转换的parse，每行也需按顺序继续追加`output`中所有字段。


### Tips相关函数说明
* 获取原始行数据中某个key对应的value:

**Java**

``` java
// 1.首先获取key在原始行数据中的index
int index = getFieldIndex(key);
// 2.原始每行数据
List<Object> newRow = new ArrayList<Object>(Arrays.asList(((GenericRow)originData).values()));
// 3.获取key对应的Value值, 其类型Type为源repo中该key的类型，注意当key为date类型时候，类型Type为Long类型。
Type value = (Type)newRow.get(index);
```

**Scala**

``` scala
// 1.首先获取key在原始行数据中的index
val index:Int = getFieldIndex(key)
// 2.原始每行数据
val orignalRow: Seq[Any] = originData.toSeq
// 3.获取key对应的Value值, 其类型Type为源repo中该key的类型，注意当key为date类型时候，类型Type为Long类型。
val value: Type = newRow.get(index).asInstanceOf[Type];
```

* 获取`TransformSpec`中`plugin`的`output`所有字段：

**Java**

``` java
List<String> pluginOutputs = getPluginFields();
```

**Scala**

``` scala
val pluginOutputs: Seq[String] = getPluginFields
```