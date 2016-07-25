package org.omg.PortableInterceptor;

public abstract interface ObjectReferenceTemplate extends ObjectReferenceFactory
{
  public abstract String server_id();

  public abstract String orb_id();

  public abstract String[] adapter_name();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectReferenceTemplate
 * JD-Core Version:    0.6.2
 */