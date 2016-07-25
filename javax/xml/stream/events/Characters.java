package javax.xml.stream.events;

public abstract interface Characters extends XMLEvent
{
  public abstract String getData();

  public abstract boolean isWhiteSpace();

  public abstract boolean isCData();

  public abstract boolean isIgnorableWhiteSpace();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.Characters
 * JD-Core Version:    0.6.2
 */