package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.nio.ByteBuffer;

public abstract interface BufferManagerRead
{
  public abstract void processFragment(ByteBuffer paramByteBuffer, FragmentMessage paramFragmentMessage);

  public abstract ByteBufferWithInfo underflow(ByteBufferWithInfo paramByteBufferWithInfo);

  public abstract void init(Message paramMessage);

  public abstract MarkAndResetHandler getMarkAndResetHandler();

  public abstract void cancelProcessing(int paramInt);

  public abstract void close(ByteBufferWithInfo paramByteBufferWithInfo);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferManagerRead
 * JD-Core Version:    0.6.2
 */