package javax.lang.model.type;

public abstract interface TypeVisitor<R, P>
{
  public abstract R visit(TypeMirror paramTypeMirror, P paramP);

  public abstract R visit(TypeMirror paramTypeMirror);

  public abstract R visitPrimitive(PrimitiveType paramPrimitiveType, P paramP);

  public abstract R visitNull(NullType paramNullType, P paramP);

  public abstract R visitArray(ArrayType paramArrayType, P paramP);

  public abstract R visitDeclared(DeclaredType paramDeclaredType, P paramP);

  public abstract R visitError(ErrorType paramErrorType, P paramP);

  public abstract R visitTypeVariable(TypeVariable paramTypeVariable, P paramP);

  public abstract R visitWildcard(WildcardType paramWildcardType, P paramP);

  public abstract R visitExecutable(ExecutableType paramExecutableType, P paramP);

  public abstract R visitNoType(NoType paramNoType, P paramP);

  public abstract R visitUnknown(TypeMirror paramTypeMirror, P paramP);

  public abstract R visitUnion(UnionType paramUnionType, P paramP);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.TypeVisitor
 * JD-Core Version:    0.6.2
 */