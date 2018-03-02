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
