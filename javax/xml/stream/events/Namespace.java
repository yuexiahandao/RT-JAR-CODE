package javax.xml.stream.events;

public abstract interface Namespace extends Attribute
{
  public abstract String getPrefix();

  public abstract String getNamespaceURI();

  public abstract boolean isDefaultNamespaceDeclaration();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.Namespace
 * JD-Core Version:    0.6.2
 */