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
