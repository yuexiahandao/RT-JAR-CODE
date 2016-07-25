package com.sun.org.glassfish.gmbal;

import java.util.Map;

@ManagedObject
@Description("Base interface for any MBean that works in the AMX framework")
public abstract interface AMXMBeanInterface
{
  public abstract Map<String, ?> getMeta();

  @ManagedAttribute(id="Name")
  @Description("Return the name of this MBean.")
  public abstract String getName();

  @ManagedAttribute(id="Parent")
  @Description("The container that contains this MBean")
  public abstract AMXMBeanInterface getParent();

  @ManagedAttribute(id="Children")
  @Description("All children of this AMX MBean")
  public abstract AMXMBeanInterface[] getChildren();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.gmbal.AMXMBeanInterface
 * JD-Core Version:    0.6.2
 */