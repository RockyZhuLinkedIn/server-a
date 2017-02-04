package com.rockyzhu.myserver.servlet;

import com.rockyzhu.myserver.kafka.consumer.ConsumerProcessor;
import com.rockyzhu.myserver.kafka.consumer.MyFirstConsumerProcessorFactory;
import com.rockyzhu.myserver.kafka.consumer.MySecondConsumerProcessorFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class MyServletContextListener implements ServletContextListener {

  //private KafkaStreams _kafkaStreams;
  private ConsumerProcessor _myFirstConsumerProcessor;
  private ConsumerProcessor _mySecondConsumerProcessor;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
    //_kafkaStreams = (KafkaStreams)ctx.getBean("kafkaStreams");
    _myFirstConsumerProcessor = MyFirstConsumerProcessorFactory.getMyFirstConsumerProcessor();
    _myFirstConsumerProcessor.start();
    _mySecondConsumerProcessor = MySecondConsumerProcessorFactory.getMySecondConsumerProcessor();
    _mySecondConsumerProcessor.start();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    //_kafkaStreams.close();
    _myFirstConsumerProcessor.close();
    _mySecondConsumerProcessor.close();
  }
}
