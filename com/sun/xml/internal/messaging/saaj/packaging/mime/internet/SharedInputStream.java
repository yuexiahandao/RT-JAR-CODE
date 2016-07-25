package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import java.io.InputStream;
import java.io.OutputStream;

public abstract interface SharedInputStream
{
  public abstract long getPosition();

  public abstract InputStream newStream(long paramLong1, long paramLong2);

  public abstract void writeTo(long paramLong1, long paramLong2, OutputStream paramOutputStream);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.SharedInputStream
 * JD-Core Version:    0.6.2
 */