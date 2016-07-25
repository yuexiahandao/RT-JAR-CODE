package com.sun.jmx.mbeanserver;

import javax.management.ObjectName;
import javax.management.loading.ClassLoaderRepository;

public abstract interface ModifiableClassLoaderRepository extends ClassLoaderRepository
{
  public abstract void addClassLoader(ClassLoader paramClassLoader);

  public abstract void removeClassLoader(ClassLoader paramClassLoader);

  public abstract void addClassLoader(ObjectName paramObjectName, ClassLoader paramClassLoader);

  public abstract void removeClassLoader(ObjectName paramObjectName);

  public abstract ClassLoader getClassLoader(ObjectName paramObjectName);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository
 * JD-Core Version:    0.6.2
 */