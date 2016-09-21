package com.qiniu.pipeline.sdk.examples;

import com.qiniu.pipeline.sdk.plugin.JavaParser;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRow;
import org.apache.spark.sql.types.StructType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


public class AppendDateTimeJavaParser extends JavaParser {
  private String TIMEFORMAT = "MM/dd/yyyy HH:mm:ss";

  public AppendDateTimeJavaParser(Integer order, List<String> pluginFields, StructType schema, Map<String, Serializable> configuration) {
    super(order, pluginFields, schema, configuration);
  }

  /**
   * @param originData 用户每行打点/输入数据
   * @return 每行输出数据, 必须由originData与pluginFields中每个字段对应的字段值共同组成, 其中pluginFields为用户Plugin中所写字段
   */
  @Override
  public List<Row> parse(Row originData) {
    SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
    String date = sdf.format(new Date());
    GenericRow genericRow = (GenericRow) originData;

    // 最初list由用户原始输入数据组成
    List<Object> list = new ArrayList<Object>(Arrays.asList(genericRow.values()));

    // list添加pluginFields中字段对应的字段值
    list.add(date);
    // 返回一个List, 其中每行必须由原始输入数据与pluginFields中每个字段对应的字段值共同组成
    List<Row> result = new ArrayList<Row>();
    result.add(new GenericRow(list.toArray()));
    return result;
  }

}
