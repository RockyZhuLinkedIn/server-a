package com.rockyzhu.avro.converter;

import com.rocky.messages.sample.StudentMessage;
import java.io.File;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;


public class StudentMessageConverter extends Converter<StudentMessage> {

  public StudentMessageConverter(){
    try {
      _schema = new Schema.Parser().parse(new File("/Users/hozhu/work/components/kafka/avro-schema/src/main/avro/StudentMessage.avsc"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public GenericRecord convert(StudentMessage message) {
    GenericRecord value = new GenericData.Record(_schema);
    value.put("name", message.getName());
    value.put("fenShu", message.getFenShu());
    return value;
  }

  @Override
  public StudentMessage convert(GenericRecord record) {
    StudentMessage message = new StudentMessage();
    message.setName(record.get("name").toString());
    message.setFenShu((Integer)record.get("fenShu"));
    return message;
  }
}
