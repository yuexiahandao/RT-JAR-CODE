package com.sun.xml.internal.org.jvnet.fastinfoset;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract interface EncodingAlgorithm
{
  public abstract Object decodeFromBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws EncodingAlgorithmException;

  public abstract Object decodeFromInputStream(InputStream paramInputStream)
    throws EncodingAlgorithmException, IOException;

  public abstract void encodeToOutputStream(Object paramObject, OutputStream paramOutputStream)
    throws EncodingAlgorithmException, IOException;

  public abstract Object convertFromCharacters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws EncodingAlgorithmException;

  public abstract void convertToCharacters(Object paramObject, StringBuffer paramStringBuffer)
    throws EncodingAlgorithmException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm
 * JD-Core Version:    0.6.2
 */