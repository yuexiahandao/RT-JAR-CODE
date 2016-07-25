package com.sun.corba.se.impl.encoding;

abstract interface MarkAndResetHandler
{
  public abstract void mark(RestorableInputStream paramRestorableInputStream);

  public abstract void fragmentationOccured(ByteBufferWithInfo paramByteBufferWithInfo);

  public abstract void reset();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.MarkAndResetHandler
 * JD-Core Version:    0.6.2
 */