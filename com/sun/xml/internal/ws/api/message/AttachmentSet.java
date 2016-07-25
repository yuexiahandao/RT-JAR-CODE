package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.Nullable;

public abstract interface AttachmentSet extends Iterable<Attachment>
{
  @Nullable
  public abstract Attachment get(String paramString);

  public abstract boolean isEmpty();

  public abstract void add(Attachment paramAttachment);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.AttachmentSet
 * JD-Core Version:    0.6.2
 */