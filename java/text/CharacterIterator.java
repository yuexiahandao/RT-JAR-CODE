package java.text;

public abstract interface CharacterIterator extends Cloneable
{
  public static final char DONE = 'ğ¿¿';

  public abstract char first();

  public abstract char last();

  public abstract char current();

  public abstract char next();

  public abstract char previous();

  public abstract char setIndex(int paramInt);

  public abstract int getBeginIndex();

  public abstract int getEndIndex();

  public abstract int getIndex();

  public abstract Object clone();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.CharacterIterator
 * JD-Core Version:    0.6.2
 */