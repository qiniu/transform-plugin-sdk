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
   * @param order         内部使用的保留字段
   * @param pluginFields  用户Plugin中所写字段
   * @param schema        用户打点/输入数据的schema和outputFields共同组成的Schema
   * @param configuration 内部使用的保留字段
   */
  public JavaParser(Integer order, List<String> pluginFields, StructType schema, Map<String, Serializable> configuration) {
    super(order, pluginFields, schema, configuration);
  }

  /**
   * 获取用户在plugin中输入的所有字段
   *
   * @return 用户在plugin中输入的所有字段
   */
  @Override
  public List<String> getPluginFields() {
    return super.getPluginFields();
  }

  /**
   * 获取经过plugin处理后每行数据的schema
   *
   * @return 经过plugin处理后每行数据的schema
   */
  @Override
  public StructType getSchema() {
    return super.getSchema();
  }

  /**
   * @param originData 用户每行打点/输入数据
   * @return 返回一个List, 其中每行数据必须由原始输入数据与pluginFields中每个字段对应的字段值共同组成
   */
  abstract public List<Row> parse(Row originData);
}
