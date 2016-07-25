package com.sun.java_cup.internal.runtime;

public abstract interface Scanner
{
  public abstract Symbol next_token()
    throws Exception;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java_cup.internal.runtime.Scanner
 * JD-Core Version:    0.6.2
 */