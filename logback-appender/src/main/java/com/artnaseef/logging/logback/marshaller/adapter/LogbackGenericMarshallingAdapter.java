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

package com.artnaseef.logging.logback.marshaller.adapter;

import com.artnaseef.logging.LogEventMarshaller;
import com.artnaseef.logging.logback.marshaller.model.LogbackWrappedGenericLoggingEvent;
import com.artnaseef.logging.model.GenericLoggingEvent;

import java.util.Optional;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Adapter, which plugs in as a marshaller, converts log messages between Logback event messages and generic event
 * messages, and passes them to a nested marshaller.
 *
 * Created by art on 2/28/18.
 */
public class LogbackGenericMarshallingAdapter implements LogEventMarshaller {

  private LogEventMarshaller nestedMarshaller;

  public LogEventMarshaller getNestedMarshaller() {
    return nestedMarshaller;
  }

  public void setNestedMarshaller(LogEventMarshaller nestedMarshaller) {
    this.nestedMarshaller = nestedMarshaller;
  }

//========================================
//
//----------------------------------------

  @Override
  public String marshal(Object event) {
    ILoggingEvent iLoggingEvent = (ILoggingEvent) event;

    GenericLoggingEvent genericLoggingEvent = new GenericLoggingEvent();
    genericLoggingEvent.setCallStack(iLoggingEvent.getCallerData());
    genericLoggingEvent.setLevel(this.extractLevelName(iLoggingEvent));
    genericLoggingEvent.setLoggerName(iLoggingEvent.getLoggerName());
    genericLoggingEvent.setMdc(iLoggingEvent.getMDCPropertyMap());
    genericLoggingEvent.setMessage(iLoggingEvent.getFormattedMessage());
    genericLoggingEvent.setThreadName(iLoggingEvent.getThreadName());
    genericLoggingEvent.setTimestamp(iLoggingEvent.getTimeStamp());

    return this.nestedMarshaller.marshal(genericLoggingEvent);
  }

  @Override
  public Object unmarshal(String marshalled) {
    Object nestedResult = this.nestedMarshaller.unmarshal(marshalled);

    if (nestedResult instanceof GenericLoggingEvent) {
      return new LogbackWrappedGenericLoggingEvent((GenericLoggingEvent) nestedResult);
    }

    return null;
  }

//========================================
// Internals
//----------------------------------------

  private String extractLevelName(ILoggingEvent iLoggingEvent) {
    return Optional.of(iLoggingEvent).map(ILoggingEvent::getLevel).map(Object::toString).orElse("null");
  }
}
