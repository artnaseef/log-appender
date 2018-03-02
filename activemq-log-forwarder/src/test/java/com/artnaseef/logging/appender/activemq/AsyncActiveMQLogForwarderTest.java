package com.artnaseef.logging.appender.activemq;

import com.artnaseef.logging.LogEventMarshaller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import static org.junit.Assert.*;

/**
 * Created by art on 3/1/18.
 */
public class AsyncActiveMQLogForwarderTest {

  private AsyncActiveMQLogForwarder target;

  private LogEventMarshaller mockMarshaller;
  private ConnectionFactory mockJmsConnectionFactory;
  private Connection mockJmsConnection;
  private Session mockJmsSession;
  private Queue mockJmsDestination;
  private MessageProducer mockJmsProducer;
  private TextMessage mockJmsTextMessage;

  /**
   * Setup common test data and interactions.
   */
  @Before
  public void setupTest() throws Exception {
    this.target = new AsyncActiveMQLogForwarder();

    this.mockMarshaller = Mockito.mock(LogEventMarshaller.class);
    this.mockJmsConnectionFactory = Mockito.mock(ConnectionFactory.class);
    this.mockJmsConnection = Mockito.mock(Connection.class);
    this.mockJmsSession = Mockito.mock(Session.class);
    this.mockJmsProducer = Mockito.mock(MessageProducer.class);
    this.mockJmsDestination = Mockito.mock(Queue.class);
    this.mockJmsTextMessage = Mockito.mock(TextMessage.class);

    Mockito.when(this.mockJmsConnectionFactory.createConnection()).thenReturn(this.mockJmsConnection, null);
    Mockito.when(this.mockJmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE))
        .thenReturn(this.mockJmsSession, null);
    Mockito.when(this.mockJmsSession.createQueue("x-test-queue-x")).thenReturn(this.mockJmsDestination, null);
    Mockito.when(this.mockJmsSession.createProducer(this.mockJmsDestination)).thenReturn(this.mockJmsProducer, null);
    Mockito.when(this.mockJmsSession.createTextMessage("x-marshalled-event-x")).thenReturn(this.mockJmsTextMessage, null);
  }

  /**
   * Verify operation of the getter and setter for max queue size.
   */
  @Test
  public void testGetMaxQueueSize() throws Exception {
    assertEquals(1000, this.target.getMaxQueueSize());

    this.target.setMaxQueueSize(50);
    assertEquals(50, this.target.getMaxQueueSize());
  }

  /**
   * Verify operation of the getter and setter for show queue failure stack.
   */
  @Test
  public void testGetSetShowQueueFailureStack() throws Exception {
    assertFalse(this.target.isShowQueueFailureStack());

    this.target.setShowQueueFailureStack(true);
    assertTrue(this.target.isShowQueueFailureStack());
  }

  /**
   * Verify operation of the getter and setter for the mashaller
   */
  @Test
  public void testGetSetMarshaller() throws Exception {
    assertNull(this.target.getMarshaller());

    this.target.setMarshaller(this.mockMarshaller);
    assertSame(this.mockMarshaller, this.target.getMarshaller());
  }

  /**
   * Verify operation of the getter and setter for the JMS Url.
   */
  @Test
  public void testGetSetJmsUrl() throws Exception {
    assertNull(this.target.getJmsUrl());

    this.target.setJmsUrl("x-jms-url-x");
    assertEquals("x-jms-url-x", this.target.getJmsUrl());
  }

  /**
   * Verify operation of the getter and setter for JMS Username.
   */
  @Test
  public void testGetSetJmsUsername() throws Exception {
    assertNull(this.target.getJmsUsername());

    this.target.setJmsUsername("x-jms-user-name-x");
    assertEquals("x-jms-user-name-x", this.target.getJmsUsername());
  }

  /**
   * Verify operation of the getter and setter for JMS Password.
   */
  @Test
  public void testGetJmsPassword() throws Exception {
    assertNull(this.target.getJmsPassword());

    this.target.setJmsPassword("x-jms-password-x");
    assertEquals("x-jms-password-x", this.target.getJmsPassword());
  }

  /**
   * Verify operation of the getter and setter for use queue.
   */
  @Test
  public void testGetSetUseQueue() throws Exception {
    assertFalse(this.target.isUseQueue());

    this.target.setUseQueue(true);
    assertTrue(this.target.isUseQueue());
  }

  /**
   * Verify operation of the getter and setter for destination name.
   */
  @Test
  public void testGetDestinationName() throws Exception {
    assertNull(this.target.getDestinationName());

    this.target.setDestinationName("x-destination-name-x");
    assertEquals("x-destination-name-x", this.target.getDestinationName());
  }

  /**
   * Verify operation of the getter and setter for message ttl
   */
  @Test
  public void testGetSetMessageTtl() throws Exception {
    assertNull(this.target.getMessageTtl());

    this.target.setMessageTtl(105L);
    assertEquals(Long.valueOf(105), this.target.getMessageTtl());
  }

  /**
   * Verify operation of the getter and setter for JMS Connection Factory
   */
  @Test
  public void testGetSetJmsConnectionFactory() throws Exception {
    assertNull(this.target.getJmsConnectionFactory());

    this.target.setJmsConnectionFactory(mockJmsConnectionFactory);
    assertSame(this.mockJmsConnectionFactory, this.target.getJmsConnectionFactory());
  }


  /**
   * Verify operation of the onLogMessage processing.
   */
  @Test
  public void testOnLogMessage() throws Exception {
    //
    // Setup test data and interactions
    //
    Object testEvent = new Object();

    Mockito.when(this.mockMarshaller.marshal(testEvent)).thenReturn("x-marshalled-event-x", null);

    this.target.setJmsConnectionFactory(this.mockJmsConnectionFactory);
    this.target.setMarshaller(this.mockMarshaller);
    this.target.setUseQueue(true);
    this.target.setDestinationName("x-test-queue-x");

    //
    // Execute
    //
    this.target.onLogMessage(testEvent);

    //
    // Verify
    //
    Mockito.verify(this.mockJmsProducer).send(this.mockJmsTextMessage);
  }
}