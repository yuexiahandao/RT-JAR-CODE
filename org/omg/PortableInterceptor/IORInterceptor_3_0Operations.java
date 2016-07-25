package org.omg.PortableInterceptor;

public abstract interface IORInterceptor_3_0Operations extends IORInterceptorOperations
{
  public abstract void components_established(IORInfo paramIORInfo);

  public abstract void adapter_manager_state_changed(int paramInt, short paramShort);

  public abstract void adapter_state_changed(ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate, short paramShort);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.IORInterceptor_3_0Operations
 * JD-Core Version:    0.6.2
 */