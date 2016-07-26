package java.lang.reflect;

public abstract interface TypeVariable<D extends GenericDeclaration> extends Type {
    public abstract Type[] getBounds();

    public abstract D getGenericDeclaration();

    public abstract String getName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.TypeVariable
 * JD-Core Version:    0.6.2
 */