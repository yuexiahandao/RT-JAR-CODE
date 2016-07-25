package javax.naming;

public abstract interface NameParser
{
  public abstract Name parse(String paramString)
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.NameParser
 * JD-Core Version:    0.6.2
 */