package javax.lang.model.element;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import javax.lang.model.type.TypeMirror;

public abstract interface Element
{
  public abstract TypeMirror asType();

  public abstract ElementKind getKind();

  public abstract List<? extends AnnotationMirror> getAnnotationMirrors();

  public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);

  public abstract Set<Modifier> getModifiers();

  public abstract Name getSimpleName();

  public abstract Element getEnclosingElement();

  public abstract List<? extends Element> getEnclosedElements();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract <R, P> R accept(ElementVisitor<R, P> paramElementVisitor, P paramP);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.Element
 * JD-Core Version:    0.6.2
 */