package com.sun.xml.internal.org.jvnet.mimepull;

import java.nio.ByteBuffer;

abstract interface Data
{
  public abstract int size();

  public abstract byte[] read();

  public abstract long writeTo(DataFile paramDataFile);

  public abstract Data createNext(DataHead paramDataHead, ByteBuffer paramByteBuffer);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.Data
 * JD-Core Version:    0.6.2
 */