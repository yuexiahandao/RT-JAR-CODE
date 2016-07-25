package java.awt.peer;

import java.awt.CheckboxGroup;

public abstract interface CheckboxPeer extends ComponentPeer
{
  public abstract void setState(boolean paramBoolean);

  public abstract void setCheckboxGroup(CheckboxGroup paramCheckboxGroup);

  public abstract void setLabel(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.CheckboxPeer
 * JD-Core Version:    0.6.2
 */