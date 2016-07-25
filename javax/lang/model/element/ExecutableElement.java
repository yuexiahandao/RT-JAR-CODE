package javax.lang.model.element;

import java.util.List;
import javax.lang.model.type.TypeMirror;

public abstract interface ExecutableElement extends Element, Parameterizable
{
  public abstract List<? extends TypeParameterElement> getTypeParameters();

  public abstract TypeMirror getReturnType();

  public abstract List<? extends VariableElement> getParameters();

  public abstract boolean isVarArgs();

  public abstract List<? extends TypeMirror> getThrownTypes();

  public abstract AnnotationValue getDefaultValue();

  public abstract Name getSimpleName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.ExecutableElement
 * JD-Core Version:    0.6.2
 */