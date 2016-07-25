package org.w3c.dom.events;

import org.w3c.dom.DOMException;

public abstract interface DocumentEvent
{
  public abstract Event createEvent(String paramString)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.events.DocumentEvent
 * JD-Core Version:    0.6.2
 */