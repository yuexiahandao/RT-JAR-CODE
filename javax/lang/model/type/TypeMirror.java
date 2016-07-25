package javax.lang.model.type;

public abstract interface TypeMirror
{
  public abstract TypeKind getKind();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract String toString();

  public abstract <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.TypeMirror
 * JD-Core Version:    0.6.2
 */