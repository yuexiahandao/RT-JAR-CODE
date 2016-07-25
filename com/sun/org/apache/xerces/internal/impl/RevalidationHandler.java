package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;

public abstract interface RevalidationHandler extends XMLDocumentFilter
{
  public abstract boolean characterData(String paramString, Augmentations paramAugmentations);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.RevalidationHandler
 * JD-Core Version:    0.6.2
 */