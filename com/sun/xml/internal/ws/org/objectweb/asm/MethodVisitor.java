package com.sun.xml.internal.ws.org.objectweb.asm;

public abstract interface MethodVisitor
{
  public abstract AnnotationVisitor visitAnnotationDefault();

  public abstract AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean);

  public abstract AnnotationVisitor visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean);

  public abstract void visitAttribute(Attribute paramAttribute);

  public abstract void visitCode();

  public abstract void visitFrame(int paramInt1, int paramInt2, Object[] paramArrayOfObject1, int paramInt3, Object[] paramArrayOfObject2);

  public abstract void visitInsn(int paramInt);

  public abstract void visitIntInsn(int paramInt1, int paramInt2);

  public abstract void visitVarInsn(int paramInt1, int paramInt2);

  public abstract void visitTypeInsn(int paramInt, String paramString);

  public abstract void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3);

  public abstract void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3);

  public abstract void visitJumpInsn(int paramInt, Label paramLabel);

  public abstract void visitLabel(Label paramLabel);

  public abstract void visitLdcInsn(Object paramObject);

  public abstract void visitIincInsn(int paramInt1, int paramInt2);

  public abstract void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label[] paramArrayOfLabel);

  public abstract void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfInt, Label[] paramArrayOfLabel);

  public abstract void visitMultiANewArrayInsn(String paramString, int paramInt);

  public abstract void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString);

  public abstract void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt);

  public abstract void visitLineNumber(int paramInt, Label paramLabel);

  public abstract void visitMaxs(int paramInt1, int paramInt2);

  public abstract void visitEnd();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
 * JD-Core Version:    0.6.2
 */