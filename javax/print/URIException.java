package javax.print;

import java.net.URI;

public abstract interface URIException
{
  public static final int URIInaccessible = 1;
  public static final int URISchemeNotSupported = 2;
  public static final int URIOtherProblem = -1;

  public abstract URI getUnsupportedURI();

  public abstract int getReason();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.URIException
 * JD-Core Version:    0.6.2
 */