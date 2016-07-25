package javax.xml.stream;

public abstract interface Location
{
  public abstract int getLineNumber();

  public abstract int getColumnNumber();

  public abstract int getCharacterOffset();

  public abstract String getPublicId();

  public abstract String getSystemId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.Location
 * JD-Core Version:    0.6.2
 */