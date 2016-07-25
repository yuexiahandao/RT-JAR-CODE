package sun.awt;

import java.awt.Component;

public abstract interface RequestFocusController
{
  public abstract boolean acceptRequestFocus(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, CausedFocusEvent.Cause paramCause);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.RequestFocusController
 * JD-Core Version:    0.6.2
 */