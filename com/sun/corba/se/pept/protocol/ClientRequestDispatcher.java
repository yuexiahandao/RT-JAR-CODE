package com.sun.corba.se.pept.protocol;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.transport.ContactInfo;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;

public abstract interface ClientRequestDispatcher
{
  public abstract OutputObject beginRequest(Object paramObject, String paramString, boolean paramBoolean, ContactInfo paramContactInfo);

  public abstract InputObject marshalingComplete(Object paramObject, OutputObject paramOutputObject)
    throws ApplicationException, RemarshalException;

  public abstract void endRequest(Broker paramBroker, Object paramObject, InputObject paramInputObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.protocol.ClientRequestDispatcher
 * JD-Core Version:    0.6.2
 */