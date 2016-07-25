package com.sun.corba.se.pept.transport;

import java.nio.ByteBuffer;

public abstract interface ByteBufferPool
{
  public abstract ByteBuffer getByteBuffer(int paramInt);

  public abstract void releaseByteBuffer(ByteBuffer paramByteBuffer);

  public abstract int activeCount();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.ByteBufferPool
 * JD-Core Version:    0.6.2
 */