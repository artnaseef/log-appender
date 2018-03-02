package com.artnaseef.logging.gson.marshaller;

import com.google.gson.Gson;

import com.artnaseef.logging.model.GenericLoggingEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Verify operation of the GsonGenericLogMarshaller.
 *
 * Created by art on 3/1/18.
 */
public class GsonGenericLogMarshallerTest {

  private GsonGenericLogMarshaller target;

  /**
   * Setup common test data and interactions.
   */
  @Before
  public void setupTest() throws Exception {
    this.target = new GsonGenericLogMarshaller();
  }

  /**
   * Verify operation of the getter and setter for GSON.
   */
  @Test
  public void testGetSetGson() throws Exception {
    Gson testGson = new Gson();
    assertNotNull(this.target.getGson());
    assertNotSame(testGson, this.target.getGson());

    this.target.setGson(testGson);
    assertSame(testGson, this.target.getGson());
  }

  /**
   * Verify operation of the marshal method.
   */
  @Test
  public void testMarshal() throws Exception {
    //
    // Setup test data and interactions
    //
    Map<String, String> testMap = new TreeMap<>();
    testMap.put("first", "1");

    //
    // Execute
    //
    String result = this.target.marshal(testMap);

    //
    // Verify
    //
    assertEquals("{\"first\":\"1\"}", result.replaceAll("\\s+", ""));
  }

  /**
   * Verify operation of the unmarshal method.
   */
  @Test
  public void testUnmarshal() throws Exception {
    //
    // Setup test data and interactions
    //
    GenericLoggingEvent testEvent = new GenericLoggingEvent();
    testEvent.setMessage("x-message-x");
    testEvent.setTimestamp(123123);

    //
    // Execute
    //
    Object result = this.target.unmarshal("{\"message\":\"x-message-x\",\"timestamp\":123123}");

    //
    // Verify
    //
    assertEquals(testEvent, result);
  }
}