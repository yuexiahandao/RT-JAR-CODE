package com.sun.corba.se.impl.protocol.giopmsgheaders;

import java.io.IOException;

public abstract interface MessageHandler
{
  public abstract void handleInput(Message paramMessage)
    throws IOException;

  public abstract void handleInput(RequestMessage_1_0 paramRequestMessage_1_0)
    throws IOException;

  public abstract void handleInput(RequestMessage_1_1 paramRequestMessage_1_1)
    throws IOException;

  public abstract void handleInput(RequestMessage_1_2 paramRequestMessage_1_2)
    throws IOException;

  public abstract void handleInput(ReplyMessage_1_0 paramReplyMessage_1_0)
    throws IOException;

  public abstract void handleInput(ReplyMessage_1_1 paramReplyMessage_1_1)
    throws IOException;

  public abstract void handleInput(ReplyMessage_1_2 paramReplyMessage_1_2)
    throws IOException;

  public abstract void handleInput(LocateRequestMessage_1_0 paramLocateRequestMessage_1_0)
    throws IOException;

  public abstract void handleInput(LocateRequestMessage_1_1 paramLocateRequestMessage_1_1)
    throws IOException;

  public abstract void handleInput(LocateRequestMessage_1_2 paramLocateRequestMessage_1_2)
    throws IOException;

  public abstract void handleInput(LocateReplyMessage_1_0 paramLocateReplyMessage_1_0)
    throws IOException;

  public abstract void handleInput(LocateReplyMessage_1_1 paramLocateReplyMessage_1_1)
    throws IOException;

  public abstract void handleInput(LocateReplyMessage_1_2 paramLocateReplyMessage_1_2)
    throws IOException;

  public abstract void handleInput(FragmentMessage_1_1 paramFragmentMessage_1_1)
    throws IOException;

  public abstract void handleInput(FragmentMessage_1_2 paramFragmentMessage_1_2)
    throws IOException;

  public abstract void handleInput(CancelRequestMessage paramCancelRequestMessage)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
 * JD-Core Version:    0.6.2
 */