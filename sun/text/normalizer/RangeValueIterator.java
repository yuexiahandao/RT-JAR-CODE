package sun.text.normalizer;

public abstract interface RangeValueIterator
{
  public abstract boolean next(Element paramElement);

  public abstract void reset();

  public static class Element
  {
    public int start;
    public int limit;
    public int value;
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.RangeValueIterator
 * JD-Core Version:    0.6.2
 */