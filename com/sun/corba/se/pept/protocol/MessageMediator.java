package com.sun.corba.se.pept.protocol;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ContactInfo;

public abstract interface MessageMediator
{
  public abstract Broker getBroker();

  public abstract ContactInfo getContactInfo();

  public abstract Connection getConnection();

  public abstract void initializeMessage();

  public abstract void finishSendingRequest();

  @Deprecated
  public abstract InputObject waitForResponse();

  public abstract void setOutputObject(OutputObject paramOutputObject);

  public abstract OutputObject getOutputObject();

  public abstract void setInputObject(InputObject paramInputObject);

  public abstract InputObject getInputObject();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.protocol.MessageMediator
 * JD-Core Version:    0.6.2
 */