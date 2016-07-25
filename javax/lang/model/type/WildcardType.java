package javax.lang.model.type;

public abstract interface WildcardType extends TypeMirror
{
  public abstract TypeMirror getExtendsBound();

  public abstract TypeMirror getSuperBound();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.WildcardType
 * JD-Core Version:    0.6.2
 */