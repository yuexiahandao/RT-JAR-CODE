package javax.xml.bind.annotation.adapters;

public abstract class XmlAdapter<ValueType, BoundType>
{
  public abstract BoundType unmarshal(ValueType paramValueType)
    throws Exception;

  public abstract ValueType marshal(BoundType paramBoundType)
    throws Exception;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.annotation.adapters.XmlAdapter
 * JD-Core Version:    0.6.2
 */