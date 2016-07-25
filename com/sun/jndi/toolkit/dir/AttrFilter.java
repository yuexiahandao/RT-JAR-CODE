package com.sun.jndi.toolkit.dir;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public abstract interface AttrFilter
{
  public abstract boolean check(Attributes paramAttributes)
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.AttrFilter
 * JD-Core Version:    0.6.2
 */