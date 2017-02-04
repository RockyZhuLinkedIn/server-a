package com.rockyzhu.myserver.kafka.consumer;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;


public class MyFirstConsumerProcessorFactory {
  public static ConsumerProcessor getMyFirstConsumerProcessor() {
    MyFirstConsumerCallback callback = new MyFirstConsumerCallback();
    return new ConsumerProcessor("topicA", callback, IntegerDeserializer.class, StringDeserializer.class, "localhost:9092", "groupIdOne");
  }
}
