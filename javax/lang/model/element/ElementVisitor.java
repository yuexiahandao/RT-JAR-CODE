package javax.lang.model.element;

public abstract interface ElementVisitor<R, P>
{
  public abstract R visit(Element paramElement, P paramP);

  public abstract R visit(Element paramElement);

  public abstract R visitPackage(PackageElement paramPackageElement, P paramP);

  public abstract R visitType(TypeElement paramTypeElement, P paramP);

  public abstract R visitVariable(VariableElement paramVariableElement, P paramP);

  public abstract R visitExecutable(ExecutableElement paramExecutableElement, P paramP);

  public abstract R visitTypeParameter(TypeParameterElement paramTypeParameterElement, P paramP);

  public abstract R visitUnknown(Element paramElement, P paramP);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.ElementVisitor
 * JD-Core Version:    0.6.2
 */