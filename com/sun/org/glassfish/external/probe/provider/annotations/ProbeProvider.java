package com.sun.org.glassfish.external.probe.provider.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface ProbeProvider
{
  public abstract String providerName();

  public abstract String moduleProviderName();

  public abstract String moduleName();

  public abstract String probeProviderName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.probe.provider.annotations.ProbeProvider
 * JD-Core Version:    0.6.2
 */