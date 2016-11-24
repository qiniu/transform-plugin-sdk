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
  private SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);

  public AppendDateTimeJavaParser(Integer order, List<String> pluginFields, StructType schema, Map<String, Serializable> configuration) {
    super(order, pluginFields, schema, configuration);
  }

  /**
   * @param originData 用户每行打点/输入数据
   * @return 每行输出数据, 必须由pluginFields中每个字段所对应的字段值共同组成, 其中pluginFields为用户创建transform的Plugin中所写字段
   */
  @Override
  public List<Row> parse(Row originData) {
    String date = sdf.format(new Date());

    // 将pluginFields中所有字段的对应字段值放入List中, 且本例子中pluginFields只有一个字段, 因此List只需存放该字段的对应值
    List<Object> list = new ArrayList<Object>();
    list.add(date);

    // 返回一个List, 其中每行数据必须由pluginFields中所有字段对应的字段值共同组成, 且由于本例子是单行到单行的转换, 因此List的size为1.
    List<Row> result = new ArrayList<Row>();
    result.add(new GenericRow(list.toArray()));
    return result;
  }

}
