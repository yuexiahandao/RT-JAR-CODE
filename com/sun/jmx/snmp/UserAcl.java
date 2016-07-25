package com.sun.jmx.snmp;

public abstract interface UserAcl
{
  public abstract String getName();

  public abstract boolean checkReadPermission(String paramString);

  public abstract boolean checkReadPermission(String paramString1, String paramString2, int paramInt);

  public abstract boolean checkContextName(String paramString);

  public abstract boolean checkWritePermission(String paramString);

  public abstract boolean checkWritePermission(String paramString1, String paramString2, int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.UserAcl
 * JD-Core Version:    0.6.2
 */