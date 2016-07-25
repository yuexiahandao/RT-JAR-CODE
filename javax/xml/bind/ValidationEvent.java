package javax.xml.bind;

public abstract interface ValidationEvent
{
  public static final int WARNING = 0;
  public static final int ERROR = 1;
  public static final int FATAL_ERROR = 2;

  public abstract int getSeverity();

  public abstract String getMessage();

  public abstract Throwable getLinkedException();

  public abstract ValidationEventLocator getLocator();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.ValidationEvent
 * JD-Core Version:    0.6.2
 */