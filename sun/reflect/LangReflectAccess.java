package sun.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract interface LangReflectAccess
{
  public abstract Field newField(Class<?> paramClass1, String paramString1, Class<?> paramClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte);

  public abstract Method newMethod(Class<?> paramClass1, String paramString1, Class<?>[] paramArrayOfClass1, Class<?> paramClass2, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

  public abstract <T> Constructor<T> newConstructor(Class<T> paramClass, Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

  public abstract MethodAccessor getMethodAccessor(Method paramMethod);

  public abstract void setMethodAccessor(Method paramMethod, MethodAccessor paramMethodAccessor);

  public abstract ConstructorAccessor getConstructorAccessor(Constructor<?> paramConstructor);

  public abstract void setConstructorAccessor(Constructor<?> paramConstructor, ConstructorAccessor paramConstructorAccessor);

  public abstract int getConstructorSlot(Constructor<?> paramConstructor);

  public abstract String getConstructorSignature(Constructor<?> paramConstructor);

  public abstract byte[] getConstructorAnnotations(Constructor<?> paramConstructor);

  public abstract byte[] getConstructorParameterAnnotations(Constructor<?> paramConstructor);

  public abstract Method copyMethod(Method paramMethod);

  public abstract Field copyField(Field paramField);

  public abstract <T> Constructor<T> copyConstructor(Constructor<T> paramConstructor);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.LangReflectAccess
 * JD-Core Version:    0.6.2
 */