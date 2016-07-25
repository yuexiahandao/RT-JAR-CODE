package javax.xml.stream.events;

public abstract interface ProcessingInstruction extends XMLEvent
{
  public abstract String getTarget();

  public abstract String getData();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.ProcessingInstruction
 * JD-Core Version:    0.6.2
 */