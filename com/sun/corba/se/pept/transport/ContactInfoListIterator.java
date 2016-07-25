package com.sun.corba.se.pept.transport;

import java.util.Iterator;

public abstract interface ContactInfoListIterator extends Iterator
{
  public abstract ContactInfoList getContactInfoList();

  public abstract void reportSuccess(ContactInfo paramContactInfo);

  public abstract boolean reportException(ContactInfo paramContactInfo, RuntimeException paramRuntimeException);

  public abstract RuntimeException getFailureException();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.ContactInfoListIterator
 * JD-Core Version:    0.6.2
 */