package javax.lang.model.type;

import java.util.List;
import javax.lang.model.element.Element;

public abstract interface DeclaredType extends ReferenceType
{
  public abstract Element asElement();

  public abstract TypeMirror getEnclosingType();

  public abstract List<? extends TypeMirror> getTypeArguments();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.DeclaredType
 * JD-Core Version:    0.6.2
 */