package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

abstract interface WriterChain
{
  public abstract void write(int paramInt)
    throws IOException;

  public abstract void write(char[] paramArrayOfChar)
    throws IOException;

  public abstract void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void write(String paramString)
    throws IOException;

  public abstract void write(String paramString, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void flush()
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract Writer getWriter();

  public abstract OutputStream getOutputStream();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.WriterChain
 * JD-Core Version:    0.6.2
 */