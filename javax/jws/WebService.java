package javax.jws;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface WebService
{
  public abstract String name();

  public abstract String targetNamespace();

  public abstract String serviceName();

  public abstract String portName();

  public abstract String wsdlLocation();

  public abstract String endpointInterface();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.jws.WebService
 * JD-Core Version:    0.6.2
 */