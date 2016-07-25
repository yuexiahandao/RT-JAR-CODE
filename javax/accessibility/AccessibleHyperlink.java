package javax.accessibility;

public abstract class AccessibleHyperlink
  implements AccessibleAction
{
  public abstract boolean isValid();

  public abstract int getAccessibleActionCount();

  public abstract boolean doAccessibleAction(int paramInt);

  public abstract String getAccessibleActionDescription(int paramInt);

  public abstract Object getAccessibleActionObject(int paramInt);

  public abstract Object getAccessibleActionAnchor(int paramInt);

  public abstract int getStartIndex();

  public abstract int getEndIndex();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleHyperlink
 * JD-Core Version:    0.6.2
 */