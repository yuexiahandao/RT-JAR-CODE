package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;

abstract interface PropertySeed<T, C, F, M> extends Locatable, AnnotationSource
{
  public abstract String getName();

  public abstract T getRawType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.PropertySeed
 * JD-Core Version:    0.6.2
 */