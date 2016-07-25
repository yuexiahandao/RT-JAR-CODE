package javax.management;

public abstract interface DynamicMBean
{
  public abstract Object getAttribute(String paramString)
    throws AttributeNotFoundException, MBeanException, ReflectionException;

  public abstract void setAttribute(Attribute paramAttribute)
    throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException;

  public abstract AttributeList getAttributes(String[] paramArrayOfString);

  public abstract AttributeList setAttributes(AttributeList paramAttributeList);

  public abstract Object invoke(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws MBeanException, ReflectionException;

  public abstract MBeanInfo getMBeanInfo();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.DynamicMBean
 * JD-Core Version:    0.6.2
 */