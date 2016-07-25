package com.sun.xml.internal.ws.org.objectweb.asm;

public abstract interface AnnotationVisitor
{
  public abstract void visit(String paramString, Object paramObject);

  public abstract void visitEnum(String paramString1, String paramString2, String paramString3);

  public abstract AnnotationVisitor visitAnnotation(String paramString1, String paramString2);

  public abstract AnnotationVisitor visitArray(String paramString);

  public abstract void visitEnd();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor
 * JD-Core Version:    0.6.2
 */