package javax.xml.stream.events;

public abstract interface NotationDeclaration extends XMLEvent
{
  public abstract String getName();

  public abstract String getPublicId();

  public abstract String getSystemId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.NotationDeclaration
 * JD-Core Version:    0.6.2
 */