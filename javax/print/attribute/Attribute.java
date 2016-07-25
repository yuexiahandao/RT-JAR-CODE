package javax.print.attribute;

import java.io.Serializable;

public abstract interface Attribute extends Serializable
{
  public abstract Class<? extends Attribute> getCategory();

  public abstract String getName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.Attribute
 * JD-Core Version:    0.6.2
 */