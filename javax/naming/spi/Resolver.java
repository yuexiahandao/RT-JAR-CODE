package javax.naming.spi;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

public abstract interface Resolver
{
  public abstract ResolveResult resolveToClass(Name paramName, Class<? extends Context> paramClass)
    throws NamingException;

  public abstract ResolveResult resolveToClass(String paramString, Class<? extends Context> paramClass)
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.spi.Resolver
 * JD-Core Version:    0.6.2
 */