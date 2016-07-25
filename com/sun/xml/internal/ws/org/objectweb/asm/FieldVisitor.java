package com.sun.xml.internal.ws.org.objectweb.asm;

public abstract interface FieldVisitor
{
  public abstract AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean);

  public abstract void visitAttribute(Attribute paramAttribute);

  public abstract void visitEnd();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.FieldVisitor
 * JD-Core Version:    0.6.2
 */