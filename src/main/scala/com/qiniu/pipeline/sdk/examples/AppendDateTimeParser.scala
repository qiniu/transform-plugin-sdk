package com.qiniu.pipeline.sdk.examples

import java.io.{Serializable => JSerializable}
import java.text.SimpleDateFormat
import java.util.Date
import com.qiniu.pipeline.sdk.plugin.ScalaParser
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType


class AppendDateTimeParser(order: Integer,
                           pluginFields: Seq[String],
                           schema: StructType,
                           configuration: Map[String, JSerializable])
    extends ScalaParser(order, pluginFields, schema, configuration) {

  override def parse(originData: Row): Seq[Row] = {

    // orignalRow表示用户的原始输入数据
    val orignalRow = originData.toSeq

    // newData指outputFields中每个字段对应的字段值, 其中pluginFields为用户Plugin中所写字段.
    val newData = {
      val dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
      //  如果需要使用UTC时间,则添加 dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"))
      dateFormatter.format(new Date())
    }

    // 返回一个Seq, 其中每行数据必须由原始输入数据与pluginFields中每个字段对应的字段值共同组成
    Seq(Row.fromSeq(orignalRow ++ Seq(newData)))
  }
}