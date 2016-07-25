package com.sun.org.apache.bcel.internal.generic;

public abstract interface ConstantPushInstruction extends PushInstruction, TypedInstruction
{
  public abstract Number getValue();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction
 * JD-Core Version:    0.6.2
 */