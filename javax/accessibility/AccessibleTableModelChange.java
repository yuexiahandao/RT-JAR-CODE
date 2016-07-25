package javax.accessibility;

public abstract interface AccessibleTableModelChange
{
  public static final int INSERT = 1;
  public static final int UPDATE = 0;
  public static final int DELETE = -1;

  public abstract int getType();

  public abstract int getFirstRow();

  public abstract int getLastRow();

  public abstract int getFirstColumn();

  public abstract int getLastColumn();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleTableModelChange
 * JD-Core Version:    0.6.2
 */