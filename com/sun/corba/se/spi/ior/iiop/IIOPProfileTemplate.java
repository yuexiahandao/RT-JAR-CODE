package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedProfileTemplate;

public abstract interface IIOPProfileTemplate extends TaggedProfileTemplate
{
  public abstract GIOPVersion getGIOPVersion();

  public abstract IIOPAddress getPrimaryAddress();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate
 * JD-Core Version:    0.6.2
 */