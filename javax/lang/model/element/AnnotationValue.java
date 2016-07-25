package javax.lang.model.element;

public abstract interface AnnotationValue
{
  public abstract Object getValue();

  public abstract String toString();

  public abstract <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.AnnotationValue
 * JD-Core Version:    0.6.2
 */