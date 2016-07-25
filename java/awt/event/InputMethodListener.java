package java.awt.event;

import java.util.EventListener;

public abstract interface InputMethodListener extends EventListener
{
  public abstract void inputMethodTextChanged(InputMethodEvent paramInputMethodEvent);

  public abstract void caretPositionChanged(InputMethodEvent paramInputMethodEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.InputMethodListener
 * JD-Core Version:    0.6.2
 */