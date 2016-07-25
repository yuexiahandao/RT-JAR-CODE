package javax.management.loading;

public abstract interface ClassLoaderRepository
{
  public abstract Class<?> loadClass(String paramString)
    throws ClassNotFoundException;

  public abstract Class<?> loadClassWithout(ClassLoader paramClassLoader, String paramString)
    throws ClassNotFoundException;

  public abstract Class<?> loadClassBefore(ClassLoader paramClassLoader, String paramString)
    throws ClassNotFoundException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.loading.ClassLoaderRepository
 * JD-Core Version:    0.6.2
 */