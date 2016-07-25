package javax.jws.soap;

import java.lang.annotation.Annotation;

@Deprecated
public @interface SOAPMessageHandler
{
  public abstract String name();

  public abstract String className();

  public abstract InitParam[] initParams();

  public abstract String[] roles();

  public abstract String[] headers();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.jws.soap.SOAPMessageHandler
 * JD-Core Version:    0.6.2
 */