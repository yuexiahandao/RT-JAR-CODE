package javax.lang.model.element;

public abstract interface Name extends CharSequence
{
  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract boolean contentEquals(CharSequence paramCharSequence);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.Name
 * JD-Core Version:    0.6.2
 */