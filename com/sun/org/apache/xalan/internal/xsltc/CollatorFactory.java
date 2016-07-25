package com.sun.org.apache.xalan.internal.xsltc;

import java.text.Collator;
import java.util.Locale;

public abstract interface CollatorFactory
{
  public abstract Collator getCollator(String paramString1, String paramString2);

  public abstract Collator getCollator(Locale paramLocale);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.CollatorFactory
 * JD-Core Version:    0.6.2
 */