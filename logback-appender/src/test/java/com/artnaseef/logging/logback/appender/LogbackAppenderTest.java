/*
 * Copyright (c) 2018 Arthur Naseef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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