package org.w3c.dom.ls;

import org.w3c.dom.Document;
import org.w3c.dom.events.Event;

public abstract interface LSLoadEvent extends Event
{
  public abstract Document getNewDocument();

  public abstract LSInput getInput();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ls.LSLoadEvent
 * JD-Core Version:    0.6.2
 */