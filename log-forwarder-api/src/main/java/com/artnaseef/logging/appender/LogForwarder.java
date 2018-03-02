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

package com.artnaseef.logging.appender;

/**
 * Definition for a forwarder which takes a generic logging event and forwards it to a destination (such as a JMS
 * queue).
 *
 * Created by art on 2/28/18.
 */
public interface LogForwarder {

  /**
   * Given a logging event, which can be one of a number of formats, forward the event.
   *
   * @param event
   */
  void onLogMessage(Object event);
}
