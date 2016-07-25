package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import java.nio.ByteBuffer;
import org.omg.CORBA.Request;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA_2_3.portable.InputStream;

public abstract interface CorbaMessageMediator extends MessageMediator, ResponseHandler
{
  public abstract void setReplyHeader(LocateReplyOrReplyMessage paramLocateReplyOrReplyMessage);

  public abstract LocateReplyMessage getLocateReplyHeader();

  public abstract ReplyMessage getReplyHeader();

  public abstract void setReplyExceptionDetailMessage(String paramString);

  public abstract RequestMessage getRequestHeader();

  public abstract GIOPVersion getGIOPVersion();

  public abstract byte getEncodingVersion();

  public abstract int getRequestId();

  public abstract Integer getRequestIdInteger();

  public abstract boolean isOneWay();

  public abstract short getAddrDisposition();

  public abstract String getOperationName();

  public abstract ServiceContexts getRequestServiceContexts();

  public abstract ServiceContexts getReplyServiceContexts();

  public abstract Message getDispatchHeader();

  public abstract void setDispatchHeader(Message paramMessage);

  public abstract ByteBuffer getDispatchBuffer();

  public abstract void setDispatchBuffer(ByteBuffer paramByteBuffer);

  public abstract int getThreadPoolToUse();

  public abstract byte getStreamFormatVersion();

  public abstract byte getStreamFormatVersionForReply();

  public abstract void sendCancelRequestIfFinalFragmentNotSent();

  public abstract void setDIIInfo(Request paramRequest);

  public abstract boolean isDIIRequest();

  public abstract Exception unmarshalDIIUserException(String paramString, InputStream paramInputStream);

  public abstract void setDIIException(Exception paramException);

  public abstract void handleDIIReply(InputStream paramInputStream);

  public abstract boolean isSystemExceptionReply();

  public abstract boolean isUserExceptionReply();

  public abstract boolean isLocationForwardReply();

  public abstract boolean isDifferentAddrDispositionRequestedReply();

  public abstract short getAddrDispositionReply();

  public abstract IOR getForwardedIOR();

  public abstract SystemException getSystemExceptionReply();

  public abstract ObjectKey getObjectKey();

  public abstract void setProtocolHandler(CorbaProtocolHandler paramCorbaProtocolHandler);

  public abstract CorbaProtocolHandler getProtocolHandler();

  public abstract OutputStream createReply();

  public abstract OutputStream createExceptionReply();

  public abstract boolean executeReturnServantInResponseConstructor();

  public abstract void setExecuteReturnServantInResponseConstructor(boolean paramBoolean);

  public abstract boolean executeRemoveThreadInfoInResponseConstructor();

  public abstract void setExecuteRemoveThreadInfoInResponseConstructor(boolean paramBoolean);

  public abstract boolean executePIInResponseConstructor();

  public abstract void setExecutePIInResponseConstructor(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.protocol.CorbaMessageMediator
 * JD-Core Version:    0.6.2
 */