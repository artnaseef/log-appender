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

package com.artnaseef.logging.logback.marshaller.model;

import com.artnaseef.logging.model.GenericLoggingEvent;

import org.slf4j.Marker;

import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;

/**
 * Created by art on 2/28/18.
 */
public class LogbackWrappedGenericLoggingEvent implements ILoggingEvent {

  private final GenericLoggingEvent nested;

  public LogbackWrappedGenericLoggingEvent(GenericLoggingEvent nested) {
    this.nested = nested;
  }

//========================================
// ILoggingEvent Interface
//----------------------------------------

  @Override
  public String getThreadName() {
    return this.nested.getThreadName();
  }

  @Override
  public Level getLevel() {
    if (this.nested.getLevel() == null) {
      return null;
    }
    return Level.valueOf(this.nested.getLevel());
  }

  @Override
  public String getMessage() {
    return this.nested.getMessage();
  }

  @Override
  public String getLoggerName() {
    return this.nested.getLoggerName();
  }

  @Override
  public Map<String, String> getMdc() {
    return this.nested.getMdc();
  }

  @Override
  public Object[] getArgumentArray() {
    return new Object[0];
  }

  @Override
  public String getFormattedMessage() {
    return this.nested.getMessage();
  }

  @Override
  public LoggerContextVO getLoggerContextVO() {
    return null;
  }

  @Override
  public IThrowableProxy getThrowableProxy() {
    return null;
  }

  @Override
  public StackTraceElement[] getCallerData() {
    return this.nested.getCallStack();
  }

  @Override
  public boolean hasCallerData() {
    if (this.nested.getCallStack() != null) {
      return true;
    }

    return false;
  }

  @Override
  public Marker getMarker() {
    return null;
  }

  @Override
  public Map<String, String> getMDCPropertyMap() {
    return this.nested.getMdc();
  }

  @Override
  public long getTimeStamp() {
    return this.nested.getTimestamp();
  }

  @Override
  public void prepareForDeferredProcessing() {
  }
}
