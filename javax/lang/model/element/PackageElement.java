package javax.lang.model.element;

public abstract interface PackageElement extends Element, QualifiedNameable
{
  public abstract Name getQualifiedName();

  public abstract Name getSimpleName();

  public abstract boolean isUnnamed();

  public abstract Element getEnclosingElement();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.PackageElement
 * JD-Core Version:    0.6.2
 */