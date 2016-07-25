package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;

public abstract interface AttributeReader
{
  public abstract Attribute createAttribute(int paramInt1, int paramInt2, DataInputStream paramDataInputStream, ConstantPool paramConstantPool);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.AttributeReader
 * JD-Core Version:    0.6.2
 */