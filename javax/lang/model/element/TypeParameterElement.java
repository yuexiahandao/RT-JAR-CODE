package javax.lang.model.element;

import java.util.List;
import javax.lang.model.type.TypeMirror;

public abstract interface TypeParameterElement extends Element
{
  public abstract Element getGenericElement();

  public abstract List<? extends TypeMirror> getBounds();

  public abstract Element getEnclosingElement();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.TypeParameterElement
 * JD-Core Version:    0.6.2
 */