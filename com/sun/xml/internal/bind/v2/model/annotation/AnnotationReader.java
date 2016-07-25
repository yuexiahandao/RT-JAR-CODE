package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;
import java.lang.annotation.Annotation;

public abstract interface AnnotationReader<T, C, F, M>
{
  public abstract void setErrorHandler(ErrorHandler paramErrorHandler);

  public abstract <A extends Annotation> A getFieldAnnotation(Class<A> paramClass, F paramF, Locatable paramLocatable);

  public abstract boolean hasFieldAnnotation(Class<? extends Annotation> paramClass, F paramF);

  public abstract boolean hasClassAnnotation(C paramC, Class<? extends Annotation> paramClass);

  public abstract Annotation[] getAllFieldAnnotations(F paramF, Locatable paramLocatable);

  public abstract <A extends Annotation> A getMethodAnnotation(Class<A> paramClass, M paramM1, M paramM2, Locatable paramLocatable);

  public abstract boolean hasMethodAnnotation(Class<? extends Annotation> paramClass, String paramString, M paramM1, M paramM2, Locatable paramLocatable);

  public abstract Annotation[] getAllMethodAnnotations(M paramM, Locatable paramLocatable);

  public abstract <A extends Annotation> A getMethodAnnotation(Class<A> paramClass, M paramM, Locatable paramLocatable);

  public abstract boolean hasMethodAnnotation(Class<? extends Annotation> paramClass, M paramM);

  @Nullable
  public abstract <A extends Annotation> A getMethodParameterAnnotation(Class<A> paramClass, M paramM, int paramInt, Locatable paramLocatable);

  @Nullable
  public abstract <A extends Annotation> A getClassAnnotation(Class<A> paramClass, C paramC, Locatable paramLocatable);

  @Nullable
  public abstract <A extends Annotation> A getPackageAnnotation(Class<A> paramClass, C paramC, Locatable paramLocatable);

  public abstract T getClassValue(Annotation paramAnnotation, String paramString);

  public abstract T[] getClassArrayValue(Annotation paramAnnotation, String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
 * JD-Core Version:    0.6.2
 */