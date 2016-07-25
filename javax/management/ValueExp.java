package javax.management;

import java.io.Serializable;

public abstract interface ValueExp extends Serializable
{
  public abstract ValueExp apply(ObjectName paramObjectName)
    throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException;

  @Deprecated
  public abstract void setMBeanServer(MBeanServer paramMBeanServer);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.ValueExp
 * JD-Core Version:    0.6.2
 */