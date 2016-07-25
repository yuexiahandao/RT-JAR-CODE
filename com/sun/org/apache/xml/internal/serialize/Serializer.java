package com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

public abstract interface Serializer
{
  public abstract void setOutputByteStream(OutputStream paramOutputStream);

  public abstract void setOutputCharStream(Writer paramWriter);

  public abstract void setOutputFormat(OutputFormat paramOutputFormat);

  public abstract DocumentHandler asDocumentHandler()
    throws IOException;

  public abstract ContentHandler asContentHandler()
    throws IOException;

  public abstract DOMSerializer asDOMSerializer()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.Serializer
 * JD-Core Version:    0.6.2
 */