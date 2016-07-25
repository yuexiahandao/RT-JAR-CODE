package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;

public abstract interface AnnotationSource
{
  public abstract <A extends Annotation> A readAnnotation(Class<A> paramClass);

  public abstract boolean hasAnnotation(Class<? extends Annotation> paramClass);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
 * JD-Core Version:    0.6.2
 */