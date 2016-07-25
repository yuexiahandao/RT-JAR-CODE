package javax.management;

import java.io.Serializable;

public abstract interface Descriptor extends Serializable, Cloneable
{
  public abstract Object getFieldValue(String paramString)
    throws RuntimeOperationsException;

  public abstract void setField(String paramString, Object paramObject)
    throws RuntimeOperationsException;

  public abstract String[] getFields();

  public abstract String[] getFieldNames();

  public abstract Object[] getFieldValues(String[] paramArrayOfString);

  public abstract void removeField(String paramString);

  public abstract void setFields(String[] paramArrayOfString, Object[] paramArrayOfObject)
    throws RuntimeOperationsException;

  public abstract Object clone()
    throws RuntimeOperationsException;

  public abstract boolean isValid()
    throws RuntimeOperationsException;

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.Descriptor
 * JD-Core Version:    0.6.2
 */