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

package com.artnaseef.logging.model;

import java.util.Map;
import java.util.Objects;

/**
 * Generic logging model event for convenience.  Contains most of the event information common to all logging platforms.
 *
 * Created by art on 2/28/18.
 */
public class GenericLoggingEvent {
  private String message;
  private String level;
  private long timestamp;
  private String threadName;
  private String loggerName;
  private StackTraceElement[] callStack;
  private Map<String, String> mdc;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getThreadName() {
    return threadName;
  }

  public void setThreadName(String threadName) {
    this.threadName = threadName;
  }

  public String getLoggerName() {
    return loggerName;
  }

  public void setLoggerName(String loggerName) {
    this.loggerName = loggerName;
  }

  public StackTraceElement[] getCallStack() {
    return callStack;
  }

  public void setCallStack(StackTraceElement[] callStack) {
    this.callStack = callStack;
  }

  public Map<String, String> getMdc() {
    return mdc;
  }

  public void setMdc(Map<String, String> mdc) {
    this.mdc = mdc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericLoggingEvent that = (GenericLoggingEvent) o;
    return Objects.equals(timestamp, that.timestamp) &&
           Objects.equals(message, that.message) &&
           Objects.equals(level, that.level) &&
           Objects.equals(threadName, that.threadName) &&
           Objects.equals(loggerName, that.loggerName) &&
           Objects.equals(callStack, that.callStack) &&
           Objects.equals(mdc, that.mdc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, level, timestamp, threadName, loggerName, callStack, mdc);
  }
}
