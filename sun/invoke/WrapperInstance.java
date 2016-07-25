package sun.invoke;

import java.lang.invoke.MethodHandle;

public abstract interface WrapperInstance
{
  public abstract MethodHandle getWrapperInstanceTarget();

  public abstract Class<?> getWrapperInstanceType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.WrapperInstance
 * JD-Core Version:    0.6.2
 */