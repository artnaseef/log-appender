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

package com.artnaseef.logging;

/**
 * Interface for implementation of a marshaller for putting content into a forwarder as a string and extracting the
 * object back out from a string.
 *
 * Created by art on 2/28/18.
 */
public interface LogEventMarshaller {

  /**
   * Given a logging event, marshal it into a string.
   *
   * @param event logging event to marshal.
   * @return string representing the content of the logging event.
   */
  String marshal(Object event);

  /**
   * Given a marshalled string, construct the logging event containing the marshalled data.
   *
   * @param marshalled
   * @return
   */
  Object unmarshal(String marshalled);
}
