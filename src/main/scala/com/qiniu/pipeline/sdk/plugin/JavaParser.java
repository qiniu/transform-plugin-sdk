package com.qiniu.pipeline.sdk.plugin;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Java编写Plugin时, 需要继承JavaParser类
 */

public abstract class JavaParser extends Parser {
  /**
   * @param order         系统内部使用字段
   * @param pluginFields  用户Plugin中填写的所有字段
   * @param schema        用户打点/输入数据的schema
   * @param configuration 系统内部使用字段
   */
  public JavaParser(Integer order, List<String> pluginFields, StructType schema, Map<String, Serializable> configuration) {
    super(order, pluginFields, schema, configuration);
  }

  /**
   * 获取用户在plugin中输入的所有字段
   *
   * @return 返回类型为String的List, 即用户在plugin中填写的所有字段
   */
  @Override
  public List<String> getPluginFields() {
    return super.getPluginFields();
  }

  /**
   * 获取用户打点/输入数据的schema
   *
   * @return 返回StructType, 即用户打点/输入数据的schema
   */
  @Override
  public StructType getSchema() {
    return super.getSchema();
  }

  /**
   * @param originData 用户每行打点/输入数据
   * @return 返回类型为Row的List, 即plugin支持单行到多行的转换, 且List中每行数据row必须由pluginFields中所有字段对应的字段值共同组成
   */
  abstract public List<Row> parse(Row originData);
}

