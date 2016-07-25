package sun.security.krb5.internal;

public abstract interface SeqNumber
{
  public abstract void randInit();

  public abstract void init(int paramInt);

  public abstract int current();

  public abstract int next();

  public abstract int step();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.SeqNumber
 * JD-Core Version:    0.6.2
 */