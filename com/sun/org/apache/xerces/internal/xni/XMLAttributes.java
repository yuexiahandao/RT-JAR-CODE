package com.sun.org.apache.xerces.internal.xni;

public abstract interface XMLAttributes
{
  public abstract int addAttribute(QName paramQName, String paramString1, String paramString2);

  public abstract void removeAllAttributes();

  public abstract void removeAttributeAt(int paramInt);

  public abstract int getLength();

  public abstract int getIndex(String paramString);

  public abstract int getIndex(String paramString1, String paramString2);

  public abstract void setName(int paramInt, QName paramQName);

  public abstract void getName(int paramInt, QName paramQName);

  public abstract String getPrefix(int paramInt);

  public abstract String getURI(int paramInt);

  public abstract String getLocalName(int paramInt);

  public abstract String getQName(int paramInt);

  public abstract QName getQualifiedName(int paramInt);

  public abstract void setType(int paramInt, String paramString);

  public abstract String getType(int paramInt);

  public abstract String getType(String paramString);

  public abstract String getType(String paramString1, String paramString2);

  public abstract void setValue(int paramInt, String paramString);

  public abstract void setValue(int paramInt, String paramString, XMLString paramXMLString);

  public abstract String getValue(int paramInt);

  public abstract String getValue(String paramString);

  public abstract String getValue(String paramString1, String paramString2);

  public abstract void setNonNormalizedValue(int paramInt, String paramString);

  public abstract String getNonNormalizedValue(int paramInt);

  public abstract void setSpecified(int paramInt, boolean paramBoolean);

  public abstract boolean isSpecified(int paramInt);

  public abstract Augmentations getAugmentations(int paramInt);

  public abstract Augmentations getAugmentations(String paramString1, String paramString2);

  public abstract Augmentations getAugmentations(String paramString);

  public abstract void setAugmentations(int paramInt, Augmentations paramAugmentations);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.XMLAttributes
 * JD-Core Version:    0.6.2
 */