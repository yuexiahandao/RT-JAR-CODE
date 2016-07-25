package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;

public abstract interface InitialContextFactory
{
  public abstract Context getInitialContext(Hashtable<?, ?> paramHashtable)
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.spi.InitialContextFactory
 * JD-Core Version:    0.6.2
 */