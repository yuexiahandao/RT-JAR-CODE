package java.awt.peer;

import java.awt.im.InputMethodRequests;

public abstract interface TextComponentPeer extends ComponentPeer
{
  public abstract void setEditable(boolean paramBoolean);

  public abstract String getText();

  public abstract void setText(String paramString);

  public abstract int getSelectionStart();

  public abstract int getSelectionEnd();

  public abstract void select(int paramInt1, int paramInt2);

  public abstract void setCaretPosition(int paramInt);

  public abstract int getCaretPosition();

  public abstract InputMethodRequests getInputMethodRequests();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.TextComponentPeer
 * JD-Core Version:    0.6.2
 */