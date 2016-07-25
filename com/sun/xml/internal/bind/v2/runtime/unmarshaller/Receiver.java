package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import org.xml.sax.SAXException;

public abstract interface Receiver
{
  public abstract void receive(UnmarshallingContext.State paramState, Object paramObject)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver
 * JD-Core Version:    0.6.2
 */