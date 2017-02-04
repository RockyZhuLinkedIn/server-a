package com.rockyzhu.avro.converter;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;


public abstract class Converter<T> {
  protected Schema _schema;

  public abstract GenericRecord convert(T message);
  public abstract T convert(GenericRecord record);
}
