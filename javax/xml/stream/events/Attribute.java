package javax.xml.stream.events;

import javax.xml.namespace.QName;

public abstract interface Attribute extends XMLEvent
{
  public abstract QName getName();

  public abstract String getValue();

  public abstract String getDTDType();

  public abstract boolean isSpecified();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.Attribute
 * JD-Core Version:    0.6.2
 */