package javax.xml.bind.annotation.adapters;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.PACKAGE})
public @interface XmlJavaTypeAdapters
{
  public abstract XmlJavaTypeAdapter[] value();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters
 * JD-Core Version:    0.6.2
 */