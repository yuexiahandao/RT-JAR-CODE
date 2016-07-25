package java.rmi.server;

import java.net.MalformedURLException;

public abstract class RMIClassLoaderSpi
{
  public abstract Class<?> loadClass(String paramString1, String paramString2, ClassLoader paramClassLoader)
    throws MalformedURLException, ClassNotFoundException;

  public abstract Class<?> loadProxyClass(String paramString, String[] paramArrayOfString, ClassLoader paramClassLoader)
    throws MalformedURLException, ClassNotFoundException;

  public abstract ClassLoader getClassLoader(String paramString)
    throws MalformedURLException;

  public abstract String getClassAnnotation(Class<?> paramClass);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.RMIClassLoaderSpi
 * JD-Core Version:    0.6.2
 */