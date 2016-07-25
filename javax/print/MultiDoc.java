package javax.print;

import java.io.IOException;

public abstract interface MultiDoc
{
  public abstract Doc getDoc()
    throws IOException;

  public abstract MultiDoc next()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.MultiDoc
 * JD-Core Version:    0.6.2
 */