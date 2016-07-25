package javax.swing.text;

public abstract interface Element
{
  public abstract Document getDocument();

  public abstract Element getParentElement();

  public abstract String getName();

  public abstract AttributeSet getAttributes();

  public abstract int getStartOffset();

  public abstract int getEndOffset();

  public abstract int getElementIndex(int paramInt);

  public abstract int getElementCount();

  public abstract Element getElement(int paramInt);

  public abstract boolean isLeaf();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.Element
 * JD-Core Version:    0.6.2
 */