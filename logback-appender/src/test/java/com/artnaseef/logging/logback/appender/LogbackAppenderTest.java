package com.artnaseef.logging.logback.appender;

import com.artnaseef.logging.appender.LogForwarder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.qos.logback.classic.spi.ILoggingEvent;

import static org.junit.Assert.*;

/**
 * Verify operation of the logback appender.
 *
 * Created by art on 3/1/18.
 */
public class LogbackAppenderTest {

  private LogbackAppender target;

  private LogForwarder mockLogForwarder;
  private ILoggingEvent mockLoggingEvent;

  /**
   * Setup common test data and interactions.
   */
  @Before
  public void setupTest() throws Exception {
    this.target = new LogbackAppender();

    this.mockLogForwarder = Mockito.mock(LogForwarder.class);
    this.mockLoggingEvent = Mockito.mock(ILoggingEvent.class);

  }

  /**
   * Verify operation of the getter and setter for log forwarder;
   */
  @Test
  public void testGetSetLogForwarder() throws Exception {
    assertNull(this.target.getLogForwarder());

    this.target.setLogForwarder(this.mockLogForwarder);
    assertSame(this.mockLogForwarder, this.target.getLogForwarder());
  }

  /**
   * Verify operation of the append method.
   */
  @Test
  public void testAppend() throws Exception {
    //
    // Setup test data and interactions
    //
    this.target.setLogForwarder(this.mockLogForwarder);

    //
    // Execute
    //
    this.target.append(this.mockLoggingEvent);

    //
    // Verify
    //
    Mockito.verify(this.mockLogForwarder).onLogMessage(this.mockLoggingEvent);
  }
}