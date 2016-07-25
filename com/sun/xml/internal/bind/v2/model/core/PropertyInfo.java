package com.sun.xml.internal.bind.v2.model.core;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource;
import java.util.Collection;
import javax.activation.MimeType;
import javax.xml.namespace.QName;

public abstract interface PropertyInfo<T, C> extends AnnotationSource
{
  public abstract TypeInfo<T, C> parent();

  public abstract String getName();

  public abstract String displayName();

  public abstract boolean isCollection();

  public abstract Collection<? extends TypeInfo<T, C>> ref();

  public abstract PropertyKind kind();

  public abstract Adapter<T, C> getAdapter();

  public abstract ID id();

  public abstract MimeType getExpectedMimeType();

  public abstract boolean inlineBinaryData();

  @Nullable
  public abstract QName getSchemaType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.PropertyInfo
 * JD-Core Version:    0.6.2
 */