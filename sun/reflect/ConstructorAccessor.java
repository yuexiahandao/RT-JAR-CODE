package sun.reflect;

import java.lang.reflect.InvocationTargetException;

public abstract interface ConstructorAccessor
{
  public abstract Object newInstance(Object[] paramArrayOfObject)
    throws InstantiationException, IllegalArgumentException, InvocationTargetException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.ConstructorAccessor
 * JD-Core Version:    0.6.2
 */