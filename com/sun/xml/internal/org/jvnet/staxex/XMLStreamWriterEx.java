package com.sun.xml.internal.org.jvnet.staxex;

import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract interface XMLStreamWriterEx extends XMLStreamWriter
{
  public abstract void writeBinary(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString)
    throws XMLStreamException;

  public abstract void writeBinary(DataHandler paramDataHandler)
    throws XMLStreamException;

  public abstract OutputStream writeBinary(String paramString)
    throws XMLStreamException;

  public abstract void writePCDATA(CharSequence paramCharSequence)
    throws XMLStreamException;

  public abstract NamespaceContextEx getNamespaceContext();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
 * JD-Core Version:    0.6.2
 */