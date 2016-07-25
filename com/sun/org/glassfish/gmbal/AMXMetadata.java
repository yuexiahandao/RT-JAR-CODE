package com.sun.org.glassfish.gmbal;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AMXMetadata
{
  @DescriptorKey("amx.isSingleton")
  public abstract boolean isSingleton();

  @DescriptorKey("amx.group")
  public abstract String group();

  @DescriptorKey("amx.subTypes")
  public abstract String[] subTypes();

  @DescriptorKey("amx.genericInterfaceName")
  public abstract String genericInterfaceName();

  @DescriptorKey("immutableInfo")
  public abstract boolean immutableInfo();

  @DescriptorKey("interfaceName")
  public abstract String interfaceClassName();

  @DescriptorKey("type")
  public abstract String type();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.gmbal.AMXMetadata
 * JD-Core Version:    0.6.2
 */