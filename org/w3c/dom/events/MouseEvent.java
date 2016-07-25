package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;

public abstract interface MouseEvent extends UIEvent
{
  public abstract int getScreenX();

  public abstract int getScreenY();

  public abstract int getClientX();

  public abstract int getClientY();

  public abstract boolean getCtrlKey();

  public abstract boolean getShiftKey();

  public abstract boolean getAltKey();

  public abstract boolean getMetaKey();

  public abstract short getButton();

  public abstract EventTarget getRelatedTarget();

  public abstract void initMouseEvent(String paramString, boolean paramBoolean1, boolean paramBoolean2, AbstractView paramAbstractView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, short paramShort, EventTarget paramEventTarget);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.events.MouseEvent
 * JD-Core Version:    0.6.2
 */