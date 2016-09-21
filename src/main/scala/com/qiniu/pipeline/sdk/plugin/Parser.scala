package com.qiniu.pipeline.sdk.plugin

import java.io.{Serializable => JSerializable}
import java.util.{List => JList, Map => JMap}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

abstract class Parser(order: Integer,
                      pluginFields: JList[String],
                      schema: StructType,
                      configuration: JMap[String, JSerializable])
    extends Comparable[Parser] with JSerializable {

  def parse(originData: Row): AnyRef

  private def getOrder = order

  def getPluginFields = pluginFields

  def getSchema = schema

  def getFieldIndex(field: String) = schema.fieldIndex(field)

  override def compareTo(that: Parser): Int = this.getOrder.compareTo(that.getOrder)
}