package org.w3c.dom.events;

public abstract interface Event
{
  public static final short CAPTURING_PHASE = 1;
  public static final short AT_TARGET = 2;
  public static final short BUBBLING_PHASE = 3;

  public abstract String getType();

  public abstract EventTarget getTarget();

  public abstract EventTarget getCurrentTarget();

  public abstract short getEventPhase();

  public abstract boolean getBubbles();

  public abstract boolean getCancelable();

  public abstract long getTimeStamp();

  public abstract void stopPropagation();

  public abstract void preventDefault();

  public abstract void initEvent(String paramString, boolean paramBoolean1, boolean paramBoolean2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.events.Event
 * JD-Core Version:    0.6.2
 */