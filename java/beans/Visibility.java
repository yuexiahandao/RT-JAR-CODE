package java.beans;

public abstract interface Visibility
{
  public abstract boolean needsGui();

  public abstract void dontUseGui();

  public abstract void okToUseGui();

  public abstract boolean avoidingGui();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.Visibility
 * JD-Core Version:    0.6.2
 */