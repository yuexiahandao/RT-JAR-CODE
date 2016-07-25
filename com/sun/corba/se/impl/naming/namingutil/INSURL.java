package com.sun.corba.se.impl.naming.namingutil;

import java.util.List;

public abstract interface INSURL
{
  public abstract boolean getRIRFlag();

  public abstract List getEndpointInfo();

  public abstract String getKeyString();

  public abstract String getStringifiedName();

  public abstract boolean isCorbanameURL();

  public abstract void dPrint();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.namingutil.INSURL
 * JD-Core Version:    0.6.2
 */