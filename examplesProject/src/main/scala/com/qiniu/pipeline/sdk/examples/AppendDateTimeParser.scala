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

  val TIMEFORMAT = "MM/dd/yyyy HH:mm:ss"
  val dateFormatter = new SimpleDateFormat(TIMEFORMAT)

  override def parse(originData: Row): Seq[Row] = {

    // 将pluginFields中所有字段的对应字段值放入Seq中, 本例子中pluginFields只有一个字段, 因此只需存放该字段的对应值
    val newDataSeq = Seq{
      // 如果需要使用UTC时间,则添加 dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"))
      dateFormatter.format(new Date())
    }

    // 返回一个Seq, 其中每行数据必须由pluginFields中所有字段对应的字段值共同组成, 且由于本例子是单行到单行的转换, 因此List的size为1.
    Seq(Row.fromSeq(newDataSeq))
  }
}