package javax.xml.stream.events;

public abstract interface EntityReference extends XMLEvent
{
  public abstract EntityDeclaration getDeclaration();

  public abstract String getName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.EntityReference
 * JD-Core Version:    0.6.2
 */