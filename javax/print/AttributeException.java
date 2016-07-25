package javax.print;

import javax.print.attribute.Attribute;

public abstract interface AttributeException
{
  public abstract Class[] getUnsupportedAttributes();

  public abstract Attribute[] getUnsupportedValues();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.AttributeException
 * JD-Core Version:    0.6.2
 */