package javax.annotation.processing;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public abstract interface RoundEnvironment
{
  public abstract boolean processingOver();

  public abstract boolean errorRaised();

  public abstract Set<? extends Element> getRootElements();

  public abstract Set<? extends Element> getElementsAnnotatedWith(TypeElement paramTypeElement);

  public abstract Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> paramClass);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.processing.RoundEnvironment
 * JD-Core Version:    0.6.2
 */