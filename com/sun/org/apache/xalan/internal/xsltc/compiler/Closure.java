package com.sun.org.apache.xalan.internal.xsltc.compiler;

public abstract interface Closure
{
  public abstract boolean inInnerClass();

  public abstract Closure getParentClosure();

  public abstract String getInnerClassName();

  public abstract void addVariable(VariableRefBase paramVariableRefBase);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
 * JD-Core Version:    0.6.2
 */