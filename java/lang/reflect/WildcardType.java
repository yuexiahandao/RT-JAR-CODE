package java.lang.reflect;

public abstract interface WildcardType extends Type
{
  public abstract Type[] getUpperBounds();

  public abstract Type[] getLowerBounds();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.WildcardType
 * JD-Core Version:    0.6.2
 */