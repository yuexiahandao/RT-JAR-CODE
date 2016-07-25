package javax.xml.ws.soap;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebServiceFeatureAnnotation(id="http://www.w3.org/2005/08/addressing/module", bean=AddressingFeature.class)
public @interface Addressing
{
  public abstract boolean enabled();

  public abstract boolean required();

  public abstract AddressingFeature.Responses responses();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.soap.Addressing
 * JD-Core Version:    0.6.2
 */