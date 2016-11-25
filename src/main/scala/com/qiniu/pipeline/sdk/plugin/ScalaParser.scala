package com.qiniu.pipeline.sdk.plugin

import java.io.{Serializable => JSerializable}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import scala.collection.JavaConversions._

/**
  * Scala编写Plugin时, 需要继承ScalaParser类
  *
  * @param order         系统内部使用字段
  * @param pluginFields  用户Plugin中填写的所有字段
  * @param schema        用户打点/输入数据的schema
  * @param configuration 系统内部使用字段
  */
abstract class ScalaParser(order: Integer,
                           pluginFields: Seq[String],
                           schema: StructType,
                           configuration: Map[String, JSerializable]) extends Parser(order, pluginFields, schema, configuration) {
  /**
    * 获取用户打点/输入数据的schema
    *
    * @return 返回StructType, 即用户打点/输入数据的schema
    */
  override def getSchema(): StructType = schema

  /**
    *
    * @param originData 用户每行打点/输入数据
    * @return 返回Seq[Row],  即plugin支持单行到多行的转换, 且Seq中每行数据必须由pluginFields中所有字段对应的字段值共同组成
    */
  def parse(originData: Row): Seq[Row]
}
