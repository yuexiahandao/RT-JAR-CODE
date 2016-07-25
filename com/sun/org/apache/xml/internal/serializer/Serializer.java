package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.xml.sax.ContentHandler;

public abstract interface Serializer
{
  public abstract void setOutputStream(OutputStream paramOutputStream);

  public abstract OutputStream getOutputStream();

  public abstract void setWriter(Writer paramWriter);

  public abstract Writer getWriter();

  public abstract void setOutputFormat(Properties paramProperties);

  public abstract Properties getOutputFormat();

  public abstract ContentHandler asContentHandler()
    throws IOException;

  public abstract DOMSerializer asDOMSerializer()
    throws IOException;

  public abstract boolean reset();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.Serializer
 * JD-Core Version:    0.6.2
 */