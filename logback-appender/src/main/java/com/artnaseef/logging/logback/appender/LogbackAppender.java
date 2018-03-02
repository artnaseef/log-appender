package com.artnaseef.logging.logback.appender;

import com.artnaseef.logging.appender.LogForwarder;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * Created by art on 2/28/18.
 */
public class LogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

  private LogForwarder logForwarder;

//========================================
// Getters and Setters
//----------------------------------------

  public LogForwarder getLogForwarder() {
    return logForwarder;
  }

  public void setLogForwarder(LogForwarder logForwarder) {
    this.logForwarder = logForwarder;
  }

//========================================
// Processing
//----------------------------------------

  @Override
  protected void append(ILoggingEvent iLoggingEvent) {
    this.logForwarder.onLogMessage(iLoggingEvent);
  }
}
