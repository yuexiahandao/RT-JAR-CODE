package org.w3c.dom.events;

import org.w3c.dom.Node;

public abstract interface MutationEvent extends Event
{
  public static final short MODIFICATION = 1;
  public static final short ADDITION = 2;
  public static final short REMOVAL = 3;

  public abstract Node getRelatedNode();

  public abstract String getPrevValue();

  public abstract String getNewValue();

  public abstract String getAttrName();

  public abstract short getAttrChange();

  public abstract void initMutationEvent(String paramString1, boolean paramBoolean1, boolean paramBoolean2, Node paramNode, String paramString2, String paramString3, String paramString4, short paramShort);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.events.MutationEvent
 * JD-Core Version:    0.6.2
 */