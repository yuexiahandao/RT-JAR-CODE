package javax.lang.model.type;

import java.util.List;

public abstract interface ExecutableType extends TypeMirror
{
  public abstract List<? extends TypeVariable> getTypeVariables();

  public abstract TypeMirror getReturnType();

  public abstract List<? extends TypeMirror> getParameterTypes();

  public abstract List<? extends TypeMirror> getThrownTypes();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.ExecutableType
 * JD-Core Version:    0.6.2
 */