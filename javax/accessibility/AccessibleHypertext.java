package javax.accessibility;

public abstract interface AccessibleHypertext extends AccessibleText
{
  public abstract int getLinkCount();

  public abstract AccessibleHyperlink getLink(int paramInt);

  public abstract int getLinkIndex(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleHypertext
 * JD-Core Version:    0.6.2
 */