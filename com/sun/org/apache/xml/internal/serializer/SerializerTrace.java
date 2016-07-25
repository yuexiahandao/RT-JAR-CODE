package com.sun.org.apache.xml.internal.serializer;

import org.xml.sax.Attributes;

public abstract interface SerializerTrace
{
  public static final int EVENTTYPE_STARTDOCUMENT = 1;
  public static final int EVENTTYPE_ENDDOCUMENT = 2;
  public static final int EVENTTYPE_STARTELEMENT = 3;
  public static final int EVENTTYPE_ENDELEMENT = 4;
  public static final int EVENTTYPE_CHARACTERS = 5;
  public static final int EVENTTYPE_IGNORABLEWHITESPACE = 6;
  public static final int EVENTTYPE_PI = 7;
  public static final int EVENTTYPE_COMMENT = 8;
  public static final int EVENTTYPE_ENTITYREF = 9;
  public static final int EVENTTYPE_CDATA = 10;
  public static final int EVENTTYPE_OUTPUT_PSEUDO_CHARACTERS = 11;
  public static final int EVENTTYPE_OUTPUT_CHARACTERS = 12;

  public abstract boolean hasTraceListeners();

  public abstract void fireGenerateEvent(int paramInt);

  public abstract void fireGenerateEvent(int paramInt, String paramString, Attributes paramAttributes);

  public abstract void fireGenerateEvent(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3);

  public abstract void fireGenerateEvent(int paramInt, String paramString1, String paramString2);

  public abstract void fireGenerateEvent(int paramInt, String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.SerializerTrace
 * JD-Core Version:    0.6.2
 */