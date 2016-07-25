package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.io.Serializable;

public abstract interface Repository extends Serializable
{
  public abstract void storeClass(JavaClass paramJavaClass);

  public abstract void removeClass(JavaClass paramJavaClass);

  public abstract JavaClass findClass(String paramString);

  public abstract JavaClass loadClass(String paramString)
    throws ClassNotFoundException;

  public abstract JavaClass loadClass(Class paramClass)
    throws ClassNotFoundException;

  public abstract void clear();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.Repository
 * JD-Core Version:    0.6.2
 */