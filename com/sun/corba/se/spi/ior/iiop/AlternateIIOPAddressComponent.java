package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedComponent;

public abstract interface AlternateIIOPAddressComponent extends TaggedComponent
{
  public abstract IIOPAddress getAddress();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent
 * JD-Core Version:    0.6.2
 */