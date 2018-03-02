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

package com.artnaseef.logging.appender.activemq;

import com.artnaseef.logging.LogEventMarshaller;
import com.artnaseef.logging.appender.LogForwarder;

import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.LinkedBlockingDeque;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Asynchronous forwarder of logging events to ActiveMQ.
 *
 * Maintains a queue of log event messages to forward and a separate thread for dispatching the events to AcitveMQ.
 *
 * Created by art on 2/28/18.
 */
public class AsyncActiveMQLogForwarder implements LogForwarder {

  private boolean showQueueFailureStack = false;

  private final Object sync = new Object();
  private final Object jmsSync = new Object();

  // General Configuration
  private int maxQueueSize = 1000;

  // Configurable Marshaller
  private LogEventMarshaller marshaller;

  // JMS Configuration
  private String jmsUrl;
  private String jmsUsername;
  private String jmsPassword;
  private boolean useQueue;
  private String destinationName;
  private Long messageTtl;

  // Runtime State
  private boolean running = true;
  private LinkedBlockingDeque<String> queue;
  private Thread execThread;
  private ConnectionFactory jmsConnectionFactory;
  private Connection jmsConnection;
  private Session jmsSession;
  private MessageProducer jmsProducer;

//========================================
// Getters and Setters
//----------------------------------------

  public int getMaxQueueSize() {
    return maxQueueSize;
  }

  public void setMaxQueueSize(int maxQueueSize) {
    this.maxQueueSize = maxQueueSize;
  }

  public boolean isShowQueueFailureStack() {
    return showQueueFailureStack;
  }

  public void setShowQueueFailureStack(boolean showQueueFailureStack) {
    this.showQueueFailureStack = showQueueFailureStack;
  }

  public LogEventMarshaller getMarshaller() {
    return marshaller;
  }

  public void setMarshaller(LogEventMarshaller marshaller) {
    this.marshaller = marshaller;
  }

  public String getJmsUrl() {
    return jmsUrl;
  }

  public void setJmsUrl(String jmsUrl) {
    this.jmsUrl = jmsUrl;
  }

  public String getJmsUsername() {
    return jmsUsername;
  }

  public void setJmsUsername(String jmsUsername) {
    this.jmsUsername = jmsUsername;
  }

  public String getJmsPassword() {
    return jmsPassword;
  }

  public void setJmsPassword(String jmsPassword) {
    this.jmsPassword = jmsPassword;
  }

  public boolean isUseQueue() {
    return useQueue;
  }

  public void setUseQueue(boolean useQueue) {
    this.useQueue = useQueue;
  }

  public String getDestinationName() {
    return destinationName;
  }

  public void setDestinationName(String destinationName) {
    this.destinationName = destinationName;
  }

  public Long getMessageTtl() {
    return messageTtl;
  }

  public void setMessageTtl(Long messageTtl) {
    this.messageTtl = messageTtl;
  }

  public ConnectionFactory getJmsConnectionFactory() {
    return jmsConnectionFactory;
  }

  public void setJmsConnectionFactory(ConnectionFactory jmsConnectionFactory) {
    this.jmsConnectionFactory = jmsConnectionFactory;
  }

//========================================
// Processing
//----------------------------------------

  @Override
  public void onLogMessage(Object event) {
    this.checkInit();

    String body = this.marshaller.marshal(event);

    if (!queue.offer(body)) {
      System.out.println(
          AsyncActiveMQLogForwarder.class.getName() + "Failed to add event to full queue: size=" + queue.size()
          + "; event=" + body);
    }
  }

//========================================
// Internals
//----------------------------------------

  private void checkInit() {
    synchronized (this.sync) {
      if (this.queue == null) {
        this.queue = new LinkedBlockingDeque<String>(this.maxQueueSize);
      }

      if (this.execThread == null) {
        this.execThread = new Thread(this::dispatch);
        this.execThread.start();
      }
    }
  }

  private void dispatch() {
    while (running) {
      try {
        this.checkJms();

        String event = this.queue.take();

        this.sendEventToJms(event);
      } catch (InterruptedException exc) {
        // Just eat it - this should only happen at shutdown, which means the loop will terminate due to running being
        //  false
      }
    }

    cleanupJmsConnection();
  }

  private void sendEventToJms(String event) {
    try {
      synchronized (this.jmsSync) {
        if (this.jmsSession != null) {
          TextMessage textMessage = this.jmsSession.createTextMessage(event);
          this.jmsProducer.send(textMessage);
        }
      }
    } catch (JMSException jmsExc) {
      System.out.println(AsyncActiveMQLogForwarder.class.getName() + " - jms error sending event; aborting");
      jmsExc.printStackTrace();

        // Use the failover transport to block instead of giving up
        this.running = false;
    }
  }

  private void cleanupJmsConnection() {
    Connection closeConnection;
    synchronized (this.jmsSync) {
      closeConnection = this.jmsConnection;
      this.jmsConnection = null;
      this.jmsProducer = null;
    }

    if (closeConnection != null) {
      try {
        closeConnection.close();
      } catch (JMSException jmsExc) {
        System.out.println(AsyncActiveMQLogForwarder.class.getName() + " - jms error on shutdown");
        jmsExc.printStackTrace();
      }
    }
  }

  private void checkJms() {
    synchronized (this.jmsSync) {
      try {
        this.setupJms();
      } catch (JMSException jmsExc) {
        System.out.println(AsyncActiveMQLogForwarder.class.getName() + " - failed to create JMS connection; aborting");
        jmsExc.printStackTrace();

        // Use the failover transport to block instead of giving up
        this.running = false;
      }
    }
  }

  private void setupJms() throws JMSException {
    if (this.jmsConnection == null) {
      if (this.jmsConnectionFactory == null) {
        prepareConnectionFactory();
      }

      this.jmsConnection = this.jmsConnectionFactory.createConnection();
      this.jmsSession = this.jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      prepareMessageProducer();
    }
  }

  private void prepareMessageProducer() throws JMSException {
    Destination destination;
    if (this.useQueue) {
      destination = this.jmsSession.createQueue(this.destinationName);
    } else {
      destination = this.jmsSession.createTopic(this.destinationName);
    }

    this.jmsProducer = this.jmsSession.createProducer(destination);

    if (this.messageTtl != null) {
      this.jmsProducer.setTimeToLive(this.messageTtl);
    }
  }

  private void prepareConnectionFactory() {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(this.jmsUrl);

    if (this.jmsUsername != null) {
      activeMQConnectionFactory.setUserName(this.jmsUsername);
    }

    if (this.jmsPassword != null) {
      activeMQConnectionFactory.setPassword(this.jmsPassword);
    }

    this.jmsConnectionFactory = activeMQConnectionFactory;
  }
}
