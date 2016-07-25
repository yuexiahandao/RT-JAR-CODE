package com.sun.org.apache.bcel.internal.classfile;

public abstract interface Visitor
{
  public abstract void visitCode(Code paramCode);

  public abstract void visitCodeException(CodeException paramCodeException);

  public abstract void visitConstantClass(ConstantClass paramConstantClass);

  public abstract void visitConstantDouble(ConstantDouble paramConstantDouble);

  public abstract void visitConstantFieldref(ConstantFieldref paramConstantFieldref);

  public abstract void visitConstantFloat(ConstantFloat paramConstantFloat);

  public abstract void visitConstantInteger(ConstantInteger paramConstantInteger);

  public abstract void visitConstantInterfaceMethodref(ConstantInterfaceMethodref paramConstantInterfaceMethodref);

  public abstract void visitConstantLong(ConstantLong paramConstantLong);

  public abstract void visitConstantMethodref(ConstantMethodref paramConstantMethodref);

  public abstract void visitConstantNameAndType(ConstantNameAndType paramConstantNameAndType);

  public abstract void visitConstantPool(ConstantPool paramConstantPool);

  public abstract void visitConstantString(ConstantString paramConstantString);

  public abstract void visitConstantUtf8(ConstantUtf8 paramConstantUtf8);

  public abstract void visitConstantValue(ConstantValue paramConstantValue);

  public abstract void visitDeprecated(Deprecated paramDeprecated);

  public abstract void visitExceptionTable(ExceptionTable paramExceptionTable);

  public abstract void visitField(Field paramField);

  public abstract void visitInnerClass(InnerClass paramInnerClass);

  public abstract void visitInnerClasses(InnerClasses paramInnerClasses);

  public abstract void visitJavaClass(JavaClass paramJavaClass);

  public abstract void visitLineNumber(LineNumber paramLineNumber);

  public abstract void visitLineNumberTable(LineNumberTable paramLineNumberTable);

  public abstract void visitLocalVariable(LocalVariable paramLocalVariable);

  public abstract void visitLocalVariableTable(LocalVariableTable paramLocalVariableTable);

  public abstract void visitLocalVariableTypeTable(LocalVariableTypeTable paramLocalVariableTypeTable);

  public abstract void visitMethod(Method paramMethod);

  public abstract void visitSignature(Signature paramSignature);

  public abstract void visitSourceFile(SourceFile paramSourceFile);

  public abstract void visitSynthetic(Synthetic paramSynthetic);

  public abstract void visitUnknown(Unknown paramUnknown);

  public abstract void visitStackMap(StackMap paramStackMap);

  public abstract void visitStackMapEntry(StackMapEntry paramStackMapEntry);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Visitor
 * JD-Core Version:    0.6.2
 */