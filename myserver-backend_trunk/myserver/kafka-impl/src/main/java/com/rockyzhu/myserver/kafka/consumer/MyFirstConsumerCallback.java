package com.rockyzhu.myserver.kafka.consumer;

import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;


public class MyFirstConsumerCallback extends ConsumerCallback<Integer, String> {

  private KafkaProducer<String, Double> _producer;
  private final String _nextTopic = "topicB_1";

  public MyFirstConsumerCallback() {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("client.id", "myserver-backend");
    properties.put("key.serializer", StringSerializer.class); //"org.apache.kafka.common.serialization.IntegerSerializer"
    properties.put("value.serializer", DoubleSerializer.class); //"org.apache.kafka.common.serialization.StringSerializer"
    _producer = new KafkaProducer<>(properties);
  }

  public void process(ConsumerRecord<Integer, String> record) {
    System.out.println("received: (key: " + record.key() + ", val: " + record.value() + ")");
    String nextKey = record.value();
    Double nextVal = Double.valueOf(record.key() + 0.0);
    _producer.send(new ProducerRecord<>(_nextTopic, nextKey, nextVal), new Callback() {
      @Override
      public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (metadata != null) {
          System.out.println("sent: (topic: " + _nextTopic + ", key: " + nextKey + ", value: " + nextVal + ") to partition " + metadata.partition() + ", offset: " + metadata.offset());
        } else {
          System.out.println("Failed to send " +  ", key: " + nextKey + ", value: " + nextVal + "");
        }
      }
    });
  }
}
