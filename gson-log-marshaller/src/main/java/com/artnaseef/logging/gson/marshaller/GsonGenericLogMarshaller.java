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

package com.artnaseef.logging.gson.marshaller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.artnaseef.logging.LogEventMarshaller;
import com.artnaseef.logging.model.GenericLoggingEvent;

/**
 * Created by art on 2/28/18.
 */
public class GsonGenericLogMarshaller implements LogEventMarshaller {

  private Gson gson = new GsonBuilder().create();

//========================================
// Getters and Setters
//----------------------------------------

  public Gson getGson() {
    return gson;
  }

  public void setGson(Gson gson) {
    this.gson = gson;
  }

//========================================
// Processing
//----------------------------------------

  @Override
  public String marshal(Object event) {
    return this.gson.toJson(event);
  }

  @Override
  public Object unmarshal(String marshalled) {
    return this.gson.fromJson(marshalled, GenericLoggingEvent.class);
  }
}
