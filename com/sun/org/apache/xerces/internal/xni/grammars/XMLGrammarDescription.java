package com.sun.org.apache.xerces.internal.xni.grammars;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;

public abstract interface XMLGrammarDescription extends XMLResourceIdentifier
{
  public static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
  public static final String XML_DTD = "http://www.w3.org/TR/REC-xml";

  public abstract String getGrammarType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription
 * JD-Core Version:    0.6.2
 */