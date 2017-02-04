package com.rockyzhu.myserver.kafka.consumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;

public class MySecondConsumerProcessorFactory {
  public static ConsumerProcessor getMySecondConsumerProcessor() {
    MySecondConsumerCallback callback = new MySecondConsumerCallback();
    return new ConsumerProcessor("topicA3", callback, KafkaAvroDeserializer.class, KafkaAvroDeserializer.class, "localhost:9092", "groupIdOne");
  }
}
