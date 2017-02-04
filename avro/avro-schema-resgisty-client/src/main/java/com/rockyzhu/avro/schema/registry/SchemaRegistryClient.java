package com.rockyzhu.avro.schema.registry;

import avro.shaded.com.google.common.collect.Lists;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.avro.Schema;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;


public class SchemaRegistryClient {
  private HttpClient _httpClient;

  public synchronized Schema getSchemaByID(String id) {
    return null;
  }

  public synchronized Schema getLatestSchemaByName(String name) {
    return null;
  }

  public synchronized String getIDBySchema(String topic, Schema schema) {
    return null;
  }


  public synchronized String registerSchema(String topic, Schema schema, String uri) throws IOException {

    HttpPost post = new HttpPost(uri);
    post.setHeader("Content-Type", "application/vnd.schemaregistry.v1+json");
    List<NameValuePair> postParams = Lists.newArrayList();
    postParams.add(new BasicNameValuePair("schema", schema.toString()));
    postParams.add(new BasicNameValuePair("name", topic));
    post.setEntity(new UrlEncodedFormEntity(postParams));

    try {
      HttpResponse response = _httpClient.execute(post); // IOException is added to function signature
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
        System.out.println("------------      Schema Below is Not Registered    ------------");
        System.out.println(schema);
        System.out.println("------------      Schema Above is Not Registered    ------------");
        throw new RuntimeException();
      }

      HttpEntity entity = response.getEntity();
    } finally {
      post.releaseConnection();                           // Will this still be executed
    }
    return null;
  }

  public synchronized String registerSchemaWithName(String schemaName) {
    return null;
  }

}
