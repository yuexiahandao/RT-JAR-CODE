package javax.management.openmbean;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

public abstract interface OpenMBeanInfo
{
  public abstract String getClassName();

  public abstract String getDescription();

  public abstract MBeanAttributeInfo[] getAttributes();

  public abstract MBeanOperationInfo[] getOperations();

  public abstract MBeanConstructorInfo[] getConstructors();

  public abstract MBeanNotificationInfo[] getNotifications();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract String toString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.OpenMBeanInfo
 * JD-Core Version:    0.6.2
 */