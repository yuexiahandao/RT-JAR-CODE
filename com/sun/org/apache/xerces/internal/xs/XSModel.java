package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSModel
{
  public abstract StringList getNamespaces();

  public abstract XSNamespaceItemList getNamespaceItems();

  public abstract XSNamedMap getComponents(short paramShort);

  public abstract XSNamedMap getComponentsByNamespace(short paramShort, String paramString);

  public abstract XSObjectList getAnnotations();

  public abstract XSElementDeclaration getElementDeclaration(String paramString1, String paramString2);

  public abstract XSAttributeDeclaration getAttributeDeclaration(String paramString1, String paramString2);

  public abstract XSTypeDefinition getTypeDefinition(String paramString1, String paramString2);

  public abstract XSAttributeGroupDefinition getAttributeGroup(String paramString1, String paramString2);

  public abstract XSModelGroupDefinition getModelGroupDefinition(String paramString1, String paramString2);

  public abstract XSNotationDeclaration getNotationDeclaration(String paramString1, String paramString2);

  public abstract XSObjectList getSubstitutionGroup(XSElementDeclaration paramXSElementDeclaration);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSModel
 * JD-Core Version:    0.6.2
 */