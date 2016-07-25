package com.sun.corba.se.pept.transport;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.protocol.MessageMediator;

public abstract interface ContactInfo
{
  public abstract Broker getBroker();

  public abstract ContactInfoList getContactInfoList();

  public abstract ClientRequestDispatcher getClientRequestDispatcher();

  public abstract boolean isConnectionBased();

  public abstract boolean shouldCacheConnection();

  public abstract String getConnectionCacheType();

  public abstract void setConnectionCache(OutboundConnectionCache paramOutboundConnectionCache);

  public abstract OutboundConnectionCache getConnectionCache();

  public abstract Connection createConnection();

  public abstract MessageMediator createMessageMediator(Broker paramBroker, ContactInfo paramContactInfo, Connection paramConnection, String paramString, boolean paramBoolean);

  public abstract MessageMediator createMessageMediator(Broker paramBroker, Connection paramConnection);

  public abstract MessageMediator finishCreatingMessageMediator(Broker paramBroker, Connection paramConnection, MessageMediator paramMessageMediator);

  public abstract InputObject createInputObject(Broker paramBroker, MessageMediator paramMessageMediator);

  public abstract OutputObject createOutputObject(MessageMediator paramMessageMediator);

  public abstract int hashCode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.ContactInfo
 * JD-Core Version:    0.6.2
 */