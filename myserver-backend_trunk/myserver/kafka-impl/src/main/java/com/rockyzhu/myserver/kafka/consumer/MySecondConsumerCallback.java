package com.rockyzhu.myserver.kafka.consumer;

import com.rocky.messages.sample.StudentMessage;
import com.rocky.messages.sample.TeacherMessage;
import com.rockyzhu.avro.converter.StudentMessageConverter;
import com.rockyzhu.avro.converter.TeacherMessageConverter;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Properties;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;


public class MySecondConsumerCallback extends ConsumerCallback<GenericRecord, GenericRecord> {

  private KafkaProducer<GenericRecord, GenericRecord> _producer;
  private final String _nextTopic = "topicB_2";

  public MySecondConsumerCallback() {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("client.id", "myserver-backend");
    properties.put("key.serializer", KafkaAvroSerializer.class);
    properties.put("value.serializer", KafkaAvroSerializer.class);
    properties.put("schema.registry.url", "http://localhost:8081");

    _producer = new KafkaProducer<>(properties);
  }

  @Override
  protected void process(ConsumerRecord<GenericRecord, GenericRecord> record) {
    /**
     * but the dog cument says:
     * Once we release a new version of the avro deserializer that can return SpecificData, the deep copy will be unnecessary
     */
    TeacherMessage teacherMessage = new TeacherMessageConverter().convert(record.key());
    StudentMessage studentMessage = new StudentMessageConverter().convert(record.value());
    System.out.println("received: (Teacher: " + teacherMessage.toString() + ", Student: " + studentMessage.toString() + ")");
  }
}

/**
 * org.apache.avro.generic.GenericData$Record cannot be cast to com.rocky.messages.sample.TeacherMessage
 *
public class MySecondConsumerCallback extends ConsumerCallback<TeacherMessage, StudentMessage> {

  @Override
  protected void process(ConsumerRecord<TeacherMessage, StudentMessage> record) {
    TeacherMessage teacherMessage = new TeacherMessageConverter().convert(record.key());
    StudentMessage studentMessage = new StudentMessageConverter().convert(record.value());
  }
*/