package org.w3c.dom.ls;

import org.w3c.dom.events.Event;

public abstract interface LSProgressEvent extends Event
{
  public abstract LSInput getInput();

  public abstract int getPosition();

  public abstract int getTotalSize();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ls.LSProgressEvent
 * JD-Core Version:    0.6.2
 */