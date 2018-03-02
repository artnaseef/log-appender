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
