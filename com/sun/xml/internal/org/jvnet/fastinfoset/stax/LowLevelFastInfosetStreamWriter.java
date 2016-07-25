package com.sun.xml.internal.org.jvnet.fastinfoset.stax;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;

public abstract interface LowLevelFastInfosetStreamWriter
{
  public abstract void initiateLowLevelWriting()
    throws XMLStreamException;

  public abstract int getNextElementIndex();

  public abstract int getNextAttributeIndex();

  public abstract int getLocalNameIndex();

  public abstract int getNextLocalNameIndex();

  public abstract void writeLowLevelTerminationAndMark()
    throws IOException;

  public abstract void writeLowLevelStartElementIndexed(int paramInt1, int paramInt2)
    throws IOException;

  public abstract boolean writeLowLevelStartElement(int paramInt, String paramString1, String paramString2, String paramString3)
    throws IOException;

  public abstract void writeLowLevelStartNamespaces()
    throws IOException;

  public abstract void writeLowLevelNamespace(String paramString1, String paramString2)
    throws IOException;

  public abstract void writeLowLevelEndNamespaces()
    throws IOException;

  public abstract void writeLowLevelStartAttributes()
    throws IOException;

  public abstract void writeLowLevelAttributeIndexed(int paramInt)
    throws IOException;

  public abstract boolean writeLowLevelAttribute(String paramString1, String paramString2, String paramString3)
    throws IOException;

  public abstract void writeLowLevelAttributeValue(String paramString)
    throws IOException;

  public abstract void writeLowLevelStartNameLiteral(int paramInt, String paramString1, byte[] paramArrayOfByte, String paramString2)
    throws IOException;

  public abstract void writeLowLevelStartNameLiteral(int paramInt1, String paramString1, int paramInt2, String paramString2)
    throws IOException;

  public abstract void writeLowLevelEndStartElement()
    throws IOException;

  public abstract void writeLowLevelEndElement()
    throws IOException;

  public abstract void writeLowLevelText(char[] paramArrayOfChar, int paramInt)
    throws IOException;

  public abstract void writeLowLevelText(String paramString)
    throws IOException;

  public abstract void writeLowLevelOctets(byte[] paramArrayOfByte, int paramInt)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
 * JD-Core Version:    0.6.2
 */