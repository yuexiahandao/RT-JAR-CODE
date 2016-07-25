package javax.lang.model.type;

import java.util.List;

public abstract interface UnionType extends TypeMirror
{
  public abstract List<? extends TypeMirror> getAlternatives();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.UnionType
 * JD-Core Version:    0.6.2
 */