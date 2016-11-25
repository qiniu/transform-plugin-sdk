# transform-plugin-sdk

## 添加依赖
maven方式:

```
<dependency>
  <groupId>com.qiniu</groupId>
  <artifactId>transform-plugin-sdk</artifactId>
  <version>1.0.1</version>
  <scope>provided</scope>
</dependency>
```
或者sbt方式:

```
"com.qiniu" %% "transform-plugin-sdk" % 1.0.1 % "provided"
```

或者gradle方式：

```
configurations {
    provided
}
  sourceSets {
    main { compileClasspath += configurations.provided }
}
  dependencies {
    provided 'com.qiniu:transform-plugin-sdk:1.0.1'
}

```

## plugin编写

### Java语言编写plugin

* 自定义 `plugin` 需要继承 `JavaParser` 抽象类,  [JavaParser example](https://github.com/qiniu/transform-plugin-sdk/blob/develop/src/main/scala/com/qiniu/pipeline/sdk/examples/AppendDateTimeJavaParser.java)。
* 重写 `JavaParser` 中 `parse` 方法， `parse` 方法支持单行到单行或者多行间的转换，函数返回值类型为 `List<Row>`。
* **注意:** parse的返回值`List<Row>`中每行数据由`TransformSpec`中`plugin`的`output`所有字段所对应的值组成，且这些字段的组织顺序必须与`TransformSpec`中`plugin`的`output`中字段顺序保持一致，例如：

`TransformSpec`中`plugin`如下， 其中`com.qiniu.AppendTwoFieldsJavaParser`功能为返回2个字段：

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
那么`parse` 返回值`parseResult`(类型为`List<Row>`)为：

``` java
// 1.根据plugin中output的字段顺序Field1和Field2，因此每行输出数据为Field1和Field2相应值Value1和Value2组成的List
	List<Object> resultRow = new ArrayList<Object>();
	resultRow.add(Value1);
	resultRow.add(Value2);
// 2.由于本parse方法是单行到单行间的转换，最终返回的 `parseResult`的size为1，即只包含resultRow
	List<Row> parseResult = new ArrayList<Row>();
	parseResult.add(new GenericRow(resultRow.toArray()));
```
，上面`parse`是单行到单行间的转换，因此输出的parseResult中仅包含一行数据，该行数据需要按一定顺序组织`output`中所有字段。如果对于是单行到N行转换的parse，则输出的parseResult中包含N行数据，其中每行数据需要按一定顺序组织`output`中所有字段。


### Scala语言编写plugin

* 自定义 `plugin` 需要继承 `ScalaParser` 抽象类，[ScalaParser example](https://github.com/qiniu/transform-plugin-sdk/blob/develop/src/main/scala/com/qiniu/pipeline/sdk/examples/AppendDateTimeParser.scala)。
* 重写 `ScalaParser` 中 `parse` 方法， `parse` 方法支持单行到单行或者多行间的转换，函数返回值类型为 `Seq[Row]`。
* **注意:** parse的返回值`Seq[Row>`中每行数据由`TransformSpec`中`plugin`的`output`所有字段所对应的值组成，且这些字段的组织顺序必须与`TransformSpec`中`plugin`的`output`中字段顺序保持一致，例如：

`TransformSpec`中`plugin`如下， 其中`com.qiniu.AppendTwoFieldsScalaParser`功能为返回2个字段：

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
那么`parse` 返回值`parseResult`(类型为`Seq[Row]`)为：

``` scala
// 1.根据plugin中output的字段顺序Field1和Field2，因此每行输出数据为Field1和Field2相应值Value1和Value2组成的Seq	
	val resultRow = Seq(Value1, Value2)
// 2.由于本parse方法是单行到单行间的转换，最终返回的 `parseResult`的size为1，即只包含resultRow
	val parseResult = Seq(Row.fromSeq(resultRow))
```
，上面`parse`是单行到单行间的转换，因此输出的parseResult中仅包含一行数据，该行数据需要按一定顺序组织`output`中所有字段。如果对于是单行到N行转换的parse，则输出的parseResult中包含N行数据，其中每行数据需要按一定顺序组织`output`中所有字段。


### 注意事项以及相关辅助函数说明

* 编写Parser类注意事项，假设现需要用Java和Scala分别编写`UserDefinedJavaParser`和`UserDefinedScalaParser`两种`plugin`:

**Java**

``` java

class UserDefinedJavaParser extends JavaParser{
	// 1. UserDefinedJavaParser需要提供如下构造函数
	public UserDefinedJavaParser(Integer order, List<String> pluginFields, StructType schema, Map<String, Serializable> configuration) {
   		 super(order, pluginFields, schema, configuration);
 	 }
  // 2. 根据Java语言编写plugin的说明实现parse方法
   public List<Row> parse(Row originData) {
   
      }
}

```

**Scala**

``` scala
// 1. 与java类似，需要提供如下构造函数
class UserDefinedScalaParser (order: Integer,
                 pluginFields: Seq[String],
                 schema: StructType,
                 configuration: Map[String, JSerializable]) extends ScalaParser(order, pluginFields, schema, configuration) {
 // 2. 根据Scala语言编写plugin的说明实现parse方法
 override def parse(originData: Row): Seq[Row] = {
   
  }
 
 }

```

* 获取原始行数据中某个key对应的value:

**Java**

``` java
// 1.首先获取key在原始行数据中的index
	int index = getFieldIndex(key);
// 2.原始每行输入数据
	List<Object> newRow = new ArrayList<Object>(Arrays.asList(((GenericRow)originData).values()));
// 3.获取key对应的Value值, 其类型Type为源repo中该key的类型，注意当key为date类型时候，类型Type为Long类型。
	Type value = (Type)newRow.get(index);
```

**Scala**

``` scala
// 1.首先获取key在原始行数据中的index
	val index:Int = getFieldIndex(key)
// 2.原始每行输入数据
	val orignalRow: Seq[Any] = originData.toSeq
// 3.获取key对应的Value, 其类型Type为源repo中该key的类型，注意当key为date类型时候，类型Type为Long类型。
	val value: Type = newRow.get(index).asInstanceOf[Type];
```

* 获取`TransformSpec`中`plugin`的`output`所有字段：

**Java**

``` java
List<String> pluginOutputs = getPluginFields();
```

**Scala**

``` scala
// 注意JList为java中List接口，非Scala中List
	val pluginOutputs: JList[String] = getPluginFields
```
