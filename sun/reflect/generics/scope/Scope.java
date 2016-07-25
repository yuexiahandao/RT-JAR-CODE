package sun.reflect.generics.scope;

import java.lang.reflect.TypeVariable;

public abstract interface Scope
{
  public abstract TypeVariable<?> lookup(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.scope.Scope
 * JD-Core Version:    0.6.2
 */