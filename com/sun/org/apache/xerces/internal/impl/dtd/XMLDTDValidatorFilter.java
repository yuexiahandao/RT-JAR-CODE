package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;

public abstract interface XMLDTDValidatorFilter extends XMLDocumentFilter
{
  public abstract boolean hasGrammar();

  public abstract boolean validate();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidatorFilter
 * JD-Core Version:    0.6.2
 */