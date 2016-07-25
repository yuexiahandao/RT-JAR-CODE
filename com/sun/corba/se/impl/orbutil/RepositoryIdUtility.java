package com.sun.corba.se.impl.orbutil;

public abstract interface RepositoryIdUtility
{
  public static final int NO_TYPE_INFO = 0;
  public static final int SINGLE_REP_TYPE_INFO = 2;
  public static final int PARTIAL_LIST_TYPE_INFO = 6;

  public abstract boolean isChunkedEncoding(int paramInt);

  public abstract boolean isCodeBasePresent(int paramInt);

  public abstract int getTypeInfo(int paramInt);

  public abstract int getStandardRMIChunkedNoRepStrId();

  public abstract int getCodeBaseRMIChunkedNoRepStrId();

  public abstract int getStandardRMIChunkedId();

  public abstract int getCodeBaseRMIChunkedId();

  public abstract int getStandardRMIUnchunkedId();

  public abstract int getCodeBaseRMIUnchunkedId();

  public abstract int getStandardRMIUnchunkedNoRepStrId();

  public abstract int getCodeBaseRMIUnchunkedNoRepStrId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.RepositoryIdUtility
 * JD-Core Version:    0.6.2
 */