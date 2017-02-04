package com.rockyzhu.client;

import com.linkedin.r2.RemoteInvocationException;
import com.linkedin.restli.client.GetRequest;
import com.linkedin.restli.client.RestClient;
import com.linkedin.restli.common.ComplexResourceKey;
import com.linkedin.restli.common.EmptyRecord;
import com.rockyzhu.myserver.api.AddEntity;
import com.rockyzhu.myserver.api.AddKey;
import com.rockyzhu.myserver.api.Buddy;
import com.rockyzhu.rest.resources.AddRequestBuilders;
import com.rockyzhu.rest.resources.ReverseRequestBuilders;
import java.io.FileInputStream;
import java.util.Properties;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by hozhu on 12/7/16.
 */
public class TestRestClientWithD2Client {

  private final static AddRequestBuilders ADD_REQUEST_BUILDERS = new AddRequestBuilders();
  private final static ReverseRequestBuilders REVERSE_REQUEST_BUILDERS = new ReverseRequestBuilders();

  private RestClient _restClient;

  private Object[][] _testData = new Object[][] {
      {"d2://add", 1, 1},
      {"d2://add", 2, 2},
      {"d2://add", 3, 3},
      {"d2://add", 4, 4},
      {"d2://add", 5, 5},
      {"d2://add", 6, 6},
      {"d2://reverse", 1l},
      {"d2://reverse", 12l},
      {"d2://reverse", 123l},
      {"d2://reverse", 1234l},
      {"d2://reverse", 12345l},
      {"d2://reverse", 123456l},
  };

  @BeforeMethod
  public void before() throws IOException, InterruptedException {
    String path = new File(new File(".").getAbsolutePath()).getCanonicalPath() + "/src/main/config/D2Client.properties";
    Properties properties = new Properties();
    properties.load(new FileInputStream(path));
    _restClient = new RestClient(D2ClientFactory.createInstance(properties), "d2://");
  }

  @AfterMethod
  public void after() {
    D2ClientFactory.shutDown();
  }

  @Test
  public void test() throws URISyntaxException, RemoteInvocationException, InterruptedException {

    for (Object[] testData : _testData) {
      if (testData.length == 3) {
        GetRequest<AddEntity> getRequestAdd = ADD_REQUEST_BUILDERS.get()
            .id(new ComplexResourceKey<>(new AddKey().setA((Integer) testData[1]).setB((Integer) testData[2]), new EmptyRecord()))
            .build();
        AddEntity result = _restClient.sendRequest(getRequestAdd).getResponseEntity();
        System.out.println(testData[1] + " + " + testData[2] + " = " + result.getResult());
      } else {
        GetRequest<Buddy> getRequestReverse = REVERSE_REQUEST_BUILDERS.get()
            .id((Long) testData[1])
            .build();
        Buddy result = _restClient.sendRequest(getRequestReverse).getResponseEntity();
        System.out.println(testData[1] + " -> " + result.getMessage());
      }
    }
  }
}
