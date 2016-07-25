package com.sun.corba.se.spi.transport;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo.CodeSetContext;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.ResponseWaitingRoom;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.omg.CORBA.SystemException;

public abstract interface CorbaConnection extends com.sun.corba.se.pept.transport.Connection, com.sun.corba.se.spi.legacy.connection.Connection
{
  public static final int OPENING = 1;
  public static final int ESTABLISHED = 2;
  public static final int CLOSE_SENT = 3;
  public static final int CLOSE_RECVD = 4;
  public static final int ABORT = 5;

  public abstract boolean shouldUseDirectByteBuffers();

  public abstract boolean shouldReadGiopHeaderOnly();

  public abstract ByteBuffer read(int paramInt1, int paramInt2, int paramInt3, long paramLong)
    throws IOException;

  public abstract ByteBuffer read(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, long paramLong)
    throws IOException;

  public abstract void write(ByteBuffer paramByteBuffer)
    throws IOException;

  public abstract void dprint(String paramString);

  public abstract int getNextRequestId();

  public abstract ORB getBroker();

  public abstract CodeSetComponentInfo.CodeSetContext getCodeSetContext();

  public abstract void setCodeSetContext(CodeSetComponentInfo.CodeSetContext paramCodeSetContext);

  public abstract MessageMediator clientRequestMapGet(int paramInt);

  public abstract void clientReply_1_1_Put(MessageMediator paramMessageMediator);

  public abstract MessageMediator clientReply_1_1_Get();

  public abstract void clientReply_1_1_Remove();

  public abstract void serverRequest_1_1_Put(MessageMediator paramMessageMediator);

  public abstract MessageMediator serverRequest_1_1_Get();

  public abstract void serverRequest_1_1_Remove();

  public abstract boolean isPostInitialContexts();

  public abstract void setPostInitialContexts();

  public abstract void purgeCalls(SystemException paramSystemException, boolean paramBoolean1, boolean paramBoolean2);

  public abstract void setCodeBaseIOR(IOR paramIOR);

  public abstract IOR getCodeBaseIOR();

  public abstract CodeBase getCodeBase();

  public abstract void sendCloseConnection(GIOPVersion paramGIOPVersion)
    throws IOException;

  public abstract void sendMessageError(GIOPVersion paramGIOPVersion)
    throws IOException;

  public abstract void sendCancelRequest(GIOPVersion paramGIOPVersion, int paramInt)
    throws IOException;

  public abstract void sendCancelRequestWithLock(GIOPVersion paramGIOPVersion, int paramInt)
    throws IOException;

  public abstract ResponseWaitingRoom getResponseWaitingRoom();

  public abstract void serverRequestMapPut(int paramInt, CorbaMessageMediator paramCorbaMessageMediator);

  public abstract CorbaMessageMediator serverRequestMapGet(int paramInt);

  public abstract void serverRequestMapRemove(int paramInt);

  public abstract SocketChannel getSocketChannel();

  public abstract void serverRequestProcessingBegins();

  public abstract void serverRequestProcessingEnds();

  public abstract void closeConnectionResources();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.CorbaConnection
 * JD-Core Version:    0.6.2
 */