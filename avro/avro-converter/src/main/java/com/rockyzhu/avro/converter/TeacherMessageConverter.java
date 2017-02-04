package com.rockyzhu.avro.converter;

import com.rocky.messages.sample.TeacherMessage;
import java.io.File;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;


public class TeacherMessageConverter extends Converter<TeacherMessage> {

  public TeacherMessageConverter() {
    try {
      _schema = new Schema.Parser().parse(new File("/Users/hozhu/work/components/kafka/avro-schema/src/main/avro/TeacherMessage.avsc"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public GenericRecord convert(TeacherMessage message) {
    GenericRecord record = new GenericData.Record(_schema);
    record.put("name", message.getName());
    record.put("yanZhi", message.getYanZhi());
    return record;
  }

  @Override
  public TeacherMessage convert(GenericRecord record) {
    TeacherMessage message = new TeacherMessage();
    /*
    Object name = record.get("name");
    if (name instanceof Utf8) {
      message.setName(name.toString());
    }
    */
    message.setName(record.get("name").toString());
    message.setYanZhi((Integer)record.get("yanZhi"));
    return message;
  }
}