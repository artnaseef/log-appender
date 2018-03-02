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
