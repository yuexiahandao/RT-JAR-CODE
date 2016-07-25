package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.xml.internal.bind.v2.runtime.Location;

public abstract interface Locatable
{
  public abstract Locatable getUpstream();

  public abstract Location getLocation();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.Locatable
 * JD-Core Version:    0.6.2
 */