package com.rockyzhu.myserver.kafka.streams;

import java.util.Locale;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;


public class KafkaStreamProcessorSupplier implements ProcessorSupplier<String, String> {

  private final String _stateStore;

  public KafkaStreamProcessorSupplier(String stateStore) {
    _stateStore = stateStore;
  }

  @Override
  public Processor<String, String> get() {

    return new Processor<String, String>() {
      private ProcessorContext _context;
      private KeyValueStore<String, Integer> _kvStore;

      @Override
      public void init(ProcessorContext context) {
        _context = context;
        context.schedule(1000);
        _kvStore = (KeyValueStore<String, Integer>)context.getStateStore(_stateStore);
      }

      @Override
      public void process(String key, String value) {
        String[] words = value.toLowerCase(Locale.getDefault()).split(" ");
        for (String word : words) {
          Integer oldValue = _kvStore.get(word);
          _kvStore.put(word, oldValue == null ? 1 : oldValue + 1);
          System.out.println(key + " + " + word + " + " + _kvStore.get(word));
          _context.forward(word, _kvStore.get(word).toString());
        }
        _context.commit();
      }

      @Override
      public void punctuate(long timestamp) {

/*
        try (KeyValueIterator<String, Integer> iter = _kvStore.all()) {
          System.out.println("----------- " + timestamp + " ----------- ");

          while (iter.hasNext()) {
            KeyValue<String, Integer> entry = iter.next();
            iter.remove();
            System.out.println("[" + entry.key + ", " + entry.value + "]");

            _context.forward(entry.key, entry.value.toString());
          }
        }
//        _kvStore.flush();
*/
      }

      @Override
      public void close() {
        _kvStore.close();
      }
    };
  }

}
