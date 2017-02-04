package com.rockyzhu.myserver.kafka.streams;

import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;


public class KafkaStreamFactory {

  public static KafkaStreams getKafkaStreams(Properties allProperties) throws IOException {
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, allProperties.getProperty("kafka." + StreamsConfig.APPLICATION_ID_CONFIG)); //"streams-wordcount"
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, allProperties.getProperty("kafka." + StreamsConfig.BOOTSTRAP_SERVERS_CONFIG)); // "localhost:9092"

    //properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());    // must match _context.forward(k, )
    //properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName()); // must match _context.forward(, v)
    properties.put(StreamsConfig.PRODUCER_PREFIX + StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
    properties.put(StreamsConfig.PRODUCER_PREFIX + StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    properties.put(StreamsConfig.CONSUMER_PREFIX + StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    properties.put(StreamsConfig.CONSUMER_PREFIX + StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());


    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, allProperties.getProperty("kafka." + ConsumerConfig.AUTO_OFFSET_RESET_CONFIG)); // "earliest"

    /*
    KStreamBuilder builder = new KStreamBuilder();
    builder.stream("topicA").to("topicB");
    */
    final String stateStore = "Reverse";
    final String sourceName = "Source";
    final String processorName = "Processor";
    final String sinkName = "Sink";
    final String inputTopic = allProperties.getProperty("kafka.inputStreamTopic");
    final String outputTopic = allProperties.getProperty("kafka.outputStreamTopic");
    TopologyBuilder builder = new TopologyBuilder();
    builder.addSource(sourceName, inputTopic);
    builder.addProcessor(processorName, new KafkaStreamProcessorSupplier(stateStore), sourceName);
    builder.addStateStore(Stores.create(stateStore).withStringKeys().withIntegerValues().inMemory().build(), processorName);
    builder.addSink(sinkName, outputTopic, processorName);

    KafkaStreams kafkaStreams = new KafkaStreams(builder, properties);
    kafkaStreams.start();
    return kafkaStreams;
  }
}