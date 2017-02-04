package com.rockyzhu.myserver.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;


public abstract class ConsumerCallback<K, V> {
  abstract protected void process(ConsumerRecord<K, V> record);
}
