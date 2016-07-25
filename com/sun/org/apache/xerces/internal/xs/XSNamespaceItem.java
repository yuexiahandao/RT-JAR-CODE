package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSNamespaceItem
{
  public abstract String getSchemaNamespace();

  public abstract XSNamedMap getComponents(short paramShort);

  public abstract XSObjectList getAnnotations();

  public abstract XSElementDeclaration getElementDeclaration(String paramString);

  public abstract XSAttributeDeclaration getAttributeDeclaration(String paramString);

  public abstract XSTypeDefinition getTypeDefinition(String paramString);

  public abstract XSAttributeGroupDefinition getAttributeGroup(String paramString);

  public abstract XSModelGroupDefinition getModelGroupDefinition(String paramString);

  public abstract XSNotationDeclaration getNotationDeclaration(String paramString);

  public abstract StringList getDocumentLocations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
 * JD-Core Version:    0.6.2
 */