package javax.management.openmbean;

import java.util.Set;

public abstract interface OpenMBeanParameterInfo
{
  public abstract String getDescription();

  public abstract String getName();

  public abstract OpenType<?> getOpenType();

  public abstract Object getDefaultValue();

  public abstract Set<?> getLegalValues();

  public abstract Comparable<?> getMinValue();

  public abstract Comparable<?> getMaxValue();

  public abstract boolean hasDefaultValue();

  public abstract boolean hasLegalValues();

  public abstract boolean hasMinValue();

  public abstract boolean hasMaxValue();

  public abstract boolean isValue(Object paramObject);

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract String toString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.OpenMBeanParameterInfo
 * JD-Core Version:    0.6.2
 */