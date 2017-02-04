package com.rockyzhu.myserver.kafka.consumer;

import java.util.Properties;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.connect.util.ShutdownableThread;


public class ConsumerProcessor<K, V> extends ShutdownableThread {
  private final KafkaConsumer<K, V> _consumer;
  private final String _topic;
  private final ConsumerCallback<K, V> _callback;

  public ConsumerProcessor(String topic, ConsumerCallback callback, Class keyDeserializerType, Class valueDeserializerType,
      String bootstrapServers, String groupId) {
    super(ConsumerProcessor.class.getName(), false);
    _topic = topic;
    _callback = callback;
    Properties properties = new Properties();
    properties.put("bootstrap.servers", bootstrapServers);
    properties.put("group.id", groupId);
    properties.put("key.deserializer", keyDeserializerType);
    properties.put("value.deserializer", valueDeserializerType);
    properties.put("schema.registry.url", "http://localhost:8081");
    _consumer = new KafkaConsumer<>(properties);
  }

  @Override
  public void execute() {
    _consumer.subscribe(ImmutableList.of(_topic));
    while (true) {
      ConsumerRecords<K, V> records = _consumer.poll(0);
      for (ConsumerRecord<K, V> record : records) {
        _callback.process(record);
      }
    }
  }

  public void close() {
    _consumer.close();
  }
}
