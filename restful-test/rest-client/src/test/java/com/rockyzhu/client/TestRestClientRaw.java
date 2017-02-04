package com.rockyzhu.client;

import com.linkedin.r2.RemoteInvocationException;
import com.linkedin.r2.transport.common.Client;
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter;
import com.linkedin.r2.transport.http.client.HttpClientFactory;
import com.linkedin.restli.client.CreateIdRequest;
import com.linkedin.restli.client.DeleteRequest;
import com.linkedin.restli.client.GetRequest;
import com.linkedin.restli.client.RestClient;
import com.linkedin.restli.client.UpdateRequest;
import com.linkedin.restli.common.ComplexResourceKey;
import com.linkedin.restli.common.EmptyRecord;
import com.rockyzhu.myserver.api.AddEntity;
import com.rockyzhu.myserver.api.AddKey;
import com.rockyzhu.myserver.api.Buddy;
import com.rockyzhu.rest.resources.AddRequestBuilders;
import com.rockyzhu.rest.resources.ReverseRequestBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

/**
 * @author Rocky Zhu
 */
public class TestRestClientRaw {

  private final static AddRequestBuilders ADD_REQUEST_BUILDERS = new AddRequestBuilders();
  private final static ReverseRequestBuilders REVERSE_REQUEST_BUILDERS = new ReverseRequestBuilders();

  final HttpClientFactory http = new HttpClientFactory();
  final Client r2Client = new TransportClientAdapter(
      http.getClient(Collections.<String, String>emptyMap()));

  private final RestClient _restClient = new RestClient(r2Client, "http://localhost:7070/myserver-backend/");
  //private final RestClient _restClient = new RestClient(r2Client, "73.231.82.5:7070/myserver-backend/");

  @Test
  public void testAdd() throws RemoteInvocationException {
    // 1. Get
    GetRequest<AddEntity> getRequestOne = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(5).setB(16), new EmptyRecord()))
        .build();
    AddEntity entityOne = _restClient.sendRequest(getRequestOne).getResponseEntity();
    Assert.assertEquals((int)entityOne.getResult(), 21);

    GetRequest<AddEntity> getRequestTwo = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(16).setB(5), new EmptyRecord()))
        .build();
    AddEntity entityTwo = _restClient.sendRequest(getRequestTwo).getResponseEntity();
    Assert.assertEquals((int)entityTwo.getResult(), 21);

    // 2. Create
    CreateIdRequest<ComplexResourceKey<AddKey, EmptyRecord>, AddEntity> createIdRequest =
        ADD_REQUEST_BUILDERS.create()
            .input(new AddEntity().setA(5).setB(16).setResult(8))
            .build();
    _restClient.sendRequest(createIdRequest).getResponse();

    GetRequest<AddEntity> getRequestThree = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(5).setB(16), new EmptyRecord()))
        .build();
    AddEntity entityThree = _restClient.sendRequest(getRequestThree).getResponseEntity();
    Assert.assertEquals((int)entityThree.getResult(), 8);

    GetRequest<AddEntity> getRequestFour = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(16).setB(5), new EmptyRecord()))
        .build();
    AddEntity entityFour = _restClient.sendRequest(getRequestFour).getResponseEntity();
    Assert.assertEquals((int)entityFour.getResult(), 8);

    // 3. Update
    UpdateRequest<AddEntity> updateRequest = ADD_REQUEST_BUILDERS.update()
        .id(new ComplexResourceKey<>(new AddKey().setA(16).setB(5), new EmptyRecord()))
        .input(new AddEntity().setResult(27))
        .build();
    _restClient.sendRequest(updateRequest).getResponse();

    GetRequest<AddEntity> getRequestFive = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(5).setB(16), new EmptyRecord()))
        .build();
    AddEntity entityFive = _restClient.sendRequest(getRequestFive).getResponseEntity();
    Assert.assertEquals((int)entityFive.getResult(), 27);

    GetRequest<AddEntity> getRequestSix = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(16).setB(5), new EmptyRecord()))
        .build();
    AddEntity entitySix = _restClient.sendRequest(getRequestSix).getResponseEntity();
    Assert.assertEquals((int)entitySix.getResult(), 27);

    // 4. Delete
    DeleteRequest<AddEntity> deleteRequest = ADD_REQUEST_BUILDERS.delete()
        .id(new ComplexResourceKey<>(new AddKey().setA(16).setB(5), new EmptyRecord()))
        .build();
    _restClient.sendRequest(deleteRequest);

    GetRequest<AddEntity> getRequestSeven = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(5).setB(16), new EmptyRecord()))
        .build();
    AddEntity entitySeven = _restClient.sendRequest(getRequestSeven).getResponseEntity();
    Assert.assertEquals((int)entitySeven.getResult(), 21);

    GetRequest<AddEntity> getRequestEight = ADD_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(new AddKey().setA(16).setB(5), new EmptyRecord()))
        .build();
    AddEntity entityEight = _restClient.sendRequest(getRequestEight).getResponseEntity();
    Assert.assertEquals((int)entityEight.getResult(), 21);
  }

  @Test
  public void testReverse() {
    GetRequest<Buddy> getRequestReverse = REVERSE_REQUEST_BUILDERS.get()
        .id(123l)
        .build();
    try {
      Buddy buddyReverse = _restClient.sendRequest(getRequestReverse).getResponseEntity();
      Assert.assertEquals(buddyReverse.getMessage(), "321");
    } catch (RemoteInvocationException e) {
      System.out.println("exception found in reverse");
      Assert.fail();
    }
  }
}
