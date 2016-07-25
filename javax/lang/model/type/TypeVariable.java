package javax.lang.model.type;

import javax.lang.model.element.Element;

public abstract interface TypeVariable extends ReferenceType
{
  public abstract Element asElement();

  public abstract TypeMirror getUpperBound();

  public abstract TypeMirror getLowerBound();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.TypeVariable
 * JD-Core Version:    0.6.2
 */