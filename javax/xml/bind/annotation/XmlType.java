package javax.xml.bind.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface XmlType
{
  public abstract String name();

  public abstract String[] propOrder();

  public abstract String namespace();

  public abstract Class factoryClass();

  public abstract String factoryMethod();

  public static final class DEFAULT
  {
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.annotation.XmlType
 * JD-Core Version:    0.6.2
 */