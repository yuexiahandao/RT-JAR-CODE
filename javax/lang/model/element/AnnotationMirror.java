package javax.lang.model.element;

import java.util.Map;
import javax.lang.model.type.DeclaredType;

public abstract interface AnnotationMirror
{
  public abstract DeclaredType getAnnotationType();

  public abstract Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValues();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.AnnotationMirror
 * JD-Core Version:    0.6.2
 */