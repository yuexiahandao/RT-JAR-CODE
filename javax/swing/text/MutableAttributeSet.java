package javax.swing.text;

import java.util.Enumeration;

public abstract interface MutableAttributeSet extends AttributeSet
{
  public abstract void addAttribute(Object paramObject1, Object paramObject2);

  public abstract void addAttributes(AttributeSet paramAttributeSet);

  public abstract void removeAttribute(Object paramObject);

  public abstract void removeAttributes(Enumeration<?> paramEnumeration);

  public abstract void removeAttributes(AttributeSet paramAttributeSet);

  public abstract void setResolveParent(AttributeSet paramAttributeSet);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.MutableAttributeSet
 * JD-Core Version:    0.6.2
 */