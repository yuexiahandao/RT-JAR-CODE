package com.sun.xml.internal.txw2.output;

public abstract interface XmlSerializer
{
  public abstract void startDocument();

  public abstract void beginStartTag(String paramString1, String paramString2, String paramString3);

  public abstract void writeAttribute(String paramString1, String paramString2, String paramString3, StringBuilder paramStringBuilder);

  public abstract void writeXmlns(String paramString1, String paramString2);

  public abstract void endStartTag(String paramString1, String paramString2, String paramString3);

  public abstract void endTag();

  public abstract void text(StringBuilder paramStringBuilder);

  public abstract void cdata(StringBuilder paramStringBuilder);

  public abstract void comment(StringBuilder paramStringBuilder);

  public abstract void endDocument();

  public abstract void flush();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.XmlSerializer
 * JD-Core Version:    0.6.2
 */