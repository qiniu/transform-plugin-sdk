package com.qiniu.pipeline.sdk.plugin

import java.io.{Serializable => JSerializable}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import scala.collection.JavaConversions._

/**
  * Scala编写Plugin时, 需要继承ScalaParser类
  *
  * @param order         内部使用的保留字段
  * @param pluginFields  用户Plugin中所写字段
  * @param schema        用户打点数据的schema和pluginFields共同组成的Schema
  * @param configuration 内部使用的保留字段
  */
abstract class ScalaParser(order: Integer,
                           pluginFields: Seq[String],
                           schema: StructType,
                           configuration: Map[String, JSerializable]) extends Parser(order, pluginFields, schema, configuration) {
  /**
    *
    * @param originData 用户每行打点/输入数据
    * @return 返回一个Seq, 其中每行数据必须由原始输入数据与pluginFields中每个字段对应的字段值共同组成
    */
  def parse(originData: Row): Seq[Row]
}
