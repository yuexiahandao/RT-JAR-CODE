package com.sun.xml.internal.ws.api.handler;

import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;

public abstract interface MessageHandler<C extends MessageHandlerContext> extends Handler<C>
{
  public abstract Set<QName> getHeaders();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.handler.MessageHandler
 * JD-Core Version:    0.6.2
 */