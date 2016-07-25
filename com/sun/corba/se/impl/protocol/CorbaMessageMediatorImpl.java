/*      */ package com.sun.corba.se.impl.protocol;
/*      */ 
/*      */ import com.sun.corba.se.impl.corba.RequestImpl;
/*      */ import com.sun.corba.se.impl.encoding.BufferManagerRead;
/*      */ import com.sun.corba.se.impl.encoding.BufferManagerReadStream;
/*      */ import com.sun.corba.se.impl.encoding.BufferManagerWrite;
/*      */ import com.sun.corba.se.impl.encoding.CDRInputObject;
/*      */ import com.sun.corba.se.impl.encoding.CDROutputObject;
/*      */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*      */ import com.sun.corba.se.impl.logging.InterceptorsSystemException;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.AddressingDispositionHelper;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.CancelRequestMessage;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage_1_1;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage_1_2;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_0;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_1;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_2;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_0;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_1;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_2;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_0;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_1;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_2;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_0;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_1;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_2;
/*      */ import com.sun.corba.se.pept.broker.Broker;
/*      */ import com.sun.corba.se.pept.encoding.InputObject;
/*      */ import com.sun.corba.se.pept.encoding.OutputObject;
/*      */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*      */ import com.sun.corba.se.pept.protocol.ProtocolHandler;
/*      */ import com.sun.corba.se.pept.transport.Acceptor;
/*      */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*      */ import com.sun.corba.se.pept.transport.Connection;
/*      */ import com.sun.corba.se.pept.transport.ContactInfo;
/*      */ import com.sun.corba.se.pept.transport.EventHandler;
/*      */ import com.sun.corba.se.pept.transport.ResponseWaitingRoom;
/*      */ import com.sun.corba.se.pept.transport.Selector;
/*      */ import com.sun.corba.se.pept.transport.TransportManager;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.ObjectKey;
/*      */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*      */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*      */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*      */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*      */ import com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent;
/*      */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*      */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*      */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*      */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*      */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*      */ import com.sun.corba.se.spi.protocol.ForwardException;
/*      */ import com.sun.corba.se.spi.protocol.PIHandler;
/*      */ import com.sun.corba.se.spi.servicecontext.MaxStreamFormatVersionServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.ServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*      */ import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
/*      */ import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
/*      */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*      */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.EmptyStackException;
/*      */ import java.util.Iterator;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.Environment;
/*      */ import org.omg.CORBA.ExceptionList;
/*      */ import org.omg.CORBA.Request;
/*      */ import org.omg.CORBA.SystemException;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.UNKNOWN;
/*      */ import org.omg.CORBA.UnknownUserException;
/*      */ import org.omg.CORBA.portable.UnknownException;
/*      */ import org.omg.CORBA_2_3.portable.InputStream;
/*      */ import sun.corba.OutputStreamFactory;
/*      */ 
/*      */ public class CorbaMessageMediatorImpl
/*      */   implements CorbaMessageMediator, CorbaProtocolHandler, MessageHandler
/*      */ {
/*      */   protected ORB orb;
/*      */   protected ORBUtilSystemException wrapper;
/*      */   protected InterceptorsSystemException interceptorWrapper;
/*      */   protected CorbaContactInfo contactInfo;
/*      */   protected CorbaConnection connection;
/*      */   protected short addrDisposition;
/*      */   protected CDROutputObject outputObject;
/*      */   protected CDRInputObject inputObject;
/*      */   protected Message messageHeader;
/*      */   protected RequestMessage requestHeader;
/*      */   protected LocateReplyOrReplyMessage replyHeader;
/*      */   protected String replyExceptionDetailMessage;
/*      */   protected IOR replyIOR;
/*      */   protected Integer requestIdInteger;
/*      */   protected Message dispatchHeader;
/*      */   protected ByteBuffer dispatchByteBuffer;
/*      */   protected byte streamFormatVersion;
/*  156 */   protected boolean streamFormatVersionSet = false;
/*      */   protected Request diiRequest;
/*  160 */   protected boolean cancelRequestAlreadySent = false;
/*      */   protected ProtocolHandler protocolHandler;
/*  163 */   protected boolean _executeReturnServantInResponseConstructor = false;
/*  164 */   protected boolean _executeRemoveThreadInfoInResponseConstructor = false;
/*  165 */   protected boolean _executePIInResponseConstructor = false;
/*      */ 
/*  702 */   protected boolean isThreadDone = false;
/*      */ 
/*      */   public CorbaMessageMediatorImpl(ORB paramORB, ContactInfo paramContactInfo, Connection paramConnection, GIOPVersion paramGIOPVersion, IOR paramIOR, int paramInt, short paramShort, String paramString, boolean paramBoolean)
/*      */   {
/*  181 */     this(paramORB, paramConnection);
/*      */ 
/*  183 */     this.contactInfo = ((CorbaContactInfo)paramContactInfo);
/*  184 */     this.addrDisposition = paramShort;
/*      */ 
/*  186 */     this.streamFormatVersion = getStreamFormatVersionForThisRequest(this.contactInfo.getEffectiveTargetIOR(), paramGIOPVersion);
/*      */ 
/*  190 */     this.streamFormatVersionSet = true;
/*      */ 
/*  192 */     this.requestHeader = MessageBase.createRequest(this.orb, paramGIOPVersion, ORBUtility.getEncodingVersion(paramORB, paramIOR), paramInt, !paramBoolean, this.contactInfo.getEffectiveTargetIOR(), this.addrDisposition, paramString, new ServiceContexts(paramORB), null);
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediatorImpl(ORB paramORB, Connection paramConnection)
/*      */   {
/*  212 */     this.orb = paramORB;
/*  213 */     this.connection = ((CorbaConnection)paramConnection);
/*  214 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*      */ 
/*  216 */     this.interceptorWrapper = InterceptorsSystemException.get(paramORB, "rpc.protocol");
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediatorImpl(ORB paramORB, CorbaConnection paramCorbaConnection, Message paramMessage, ByteBuffer paramByteBuffer)
/*      */   {
/*  232 */     this(paramORB, paramCorbaConnection);
/*  233 */     this.dispatchHeader = paramMessage;
/*  234 */     this.dispatchByteBuffer = paramByteBuffer;
/*      */   }
/*      */ 
/*      */   public Broker getBroker()
/*      */   {
/*  244 */     return this.orb;
/*      */   }
/*      */ 
/*      */   public ContactInfo getContactInfo()
/*      */   {
/*  249 */     return this.contactInfo;
/*      */   }
/*      */ 
/*      */   public Connection getConnection()
/*      */   {
/*  254 */     return this.connection;
/*      */   }
/*      */ 
/*      */   public void initializeMessage()
/*      */   {
/*  259 */     getRequestHeader().write(this.outputObject);
/*      */   }
/*      */ 
/*      */   public void finishSendingRequest()
/*      */   {
/*  265 */     this.outputObject.finishSendingMessage();
/*      */   }
/*      */ 
/*      */   public InputObject waitForResponse()
/*      */   {
/*  270 */     if (getRequestHeader().isResponseExpected()) {
/*  271 */       return this.connection.waitForResponse(this);
/*      */     }
/*  273 */     return null;
/*      */   }
/*      */ 
/*      */   public void setOutputObject(OutputObject paramOutputObject)
/*      */   {
/*  278 */     this.outputObject = ((CDROutputObject)paramOutputObject);
/*      */   }
/*      */ 
/*      */   public OutputObject getOutputObject()
/*      */   {
/*  283 */     return this.outputObject;
/*      */   }
/*      */ 
/*      */   public void setInputObject(InputObject paramInputObject)
/*      */   {
/*  288 */     this.inputObject = ((CDRInputObject)paramInputObject);
/*      */   }
/*      */ 
/*      */   public InputObject getInputObject()
/*      */   {
/*  293 */     return this.inputObject;
/*      */   }
/*      */ 
/*      */   public void setReplyHeader(LocateReplyOrReplyMessage paramLocateReplyOrReplyMessage)
/*      */   {
/*  303 */     this.replyHeader = paramLocateReplyOrReplyMessage;
/*  304 */     this.replyIOR = paramLocateReplyOrReplyMessage.getIOR();
/*      */   }
/*      */ 
/*      */   public LocateReplyMessage getLocateReplyHeader()
/*      */   {
/*  309 */     return (LocateReplyMessage)this.replyHeader;
/*      */   }
/*      */ 
/*      */   public ReplyMessage getReplyHeader()
/*      */   {
/*  314 */     return (ReplyMessage)this.replyHeader;
/*      */   }
/*      */ 
/*      */   public void setReplyExceptionDetailMessage(String paramString)
/*      */   {
/*  319 */     this.replyExceptionDetailMessage = paramString;
/*      */   }
/*      */ 
/*      */   public RequestMessage getRequestHeader()
/*      */   {
/*  324 */     return this.requestHeader;
/*      */   }
/*      */ 
/*      */   public GIOPVersion getGIOPVersion()
/*      */   {
/*  329 */     if (this.messageHeader != null) {
/*  330 */       return this.messageHeader.getGIOPVersion();
/*      */     }
/*  332 */     return getRequestHeader().getGIOPVersion();
/*      */   }
/*      */ 
/*      */   public byte getEncodingVersion() {
/*  336 */     if (this.messageHeader != null) {
/*  337 */       return this.messageHeader.getEncodingVersion();
/*      */     }
/*  339 */     return getRequestHeader().getEncodingVersion();
/*      */   }
/*      */ 
/*      */   public int getRequestId()
/*      */   {
/*  344 */     return getRequestHeader().getRequestId();
/*      */   }
/*      */ 
/*      */   public Integer getRequestIdInteger()
/*      */   {
/*  349 */     if (this.requestIdInteger == null) {
/*  350 */       this.requestIdInteger = new Integer(getRequestHeader().getRequestId());
/*      */     }
/*  352 */     return this.requestIdInteger;
/*      */   }
/*      */ 
/*      */   public boolean isOneWay()
/*      */   {
/*  357 */     return !getRequestHeader().isResponseExpected();
/*      */   }
/*      */ 
/*      */   public short getAddrDisposition()
/*      */   {
/*  362 */     return this.addrDisposition;
/*      */   }
/*      */ 
/*      */   public String getOperationName()
/*      */   {
/*  367 */     return getRequestHeader().getOperation();
/*      */   }
/*      */ 
/*      */   public ServiceContexts getRequestServiceContexts()
/*      */   {
/*  372 */     return getRequestHeader().getServiceContexts();
/*      */   }
/*      */ 
/*      */   public ServiceContexts getReplyServiceContexts()
/*      */   {
/*  377 */     return getReplyHeader().getServiceContexts();
/*      */   }
/*      */ 
/*      */   public void sendCancelRequestIfFinalFragmentNotSent()
/*      */   {
/*  382 */     if ((!sentFullMessage()) && (sentFragment()) && (!this.cancelRequestAlreadySent))
/*      */     {
/*      */       try
/*      */       {
/*  386 */         if (this.orb.subcontractDebugFlag) {
/*  387 */           dprint(".sendCancelRequestIfFinalFragmentNotSent->: " + opAndId(this));
/*      */         }
/*      */ 
/*  390 */         this.connection.sendCancelRequestWithLock(getGIOPVersion(), getRequestId());
/*      */ 
/*  395 */         this.cancelRequestAlreadySent = true;
/*      */       } catch (IOException localIOException) {
/*  397 */         if (this.orb.subcontractDebugFlag) {
/*  398 */           dprint(".sendCancelRequestIfFinalFragmentNotSent: !ERROR : " + opAndId(this), localIOException);
/*      */         }
/*      */ 
/*  404 */         throw this.interceptorWrapper.ioexceptionDuringCancelRequest(CompletionStatus.COMPLETED_MAYBE, localIOException);
/*      */       }
/*      */       finally {
/*  407 */         if (this.orb.subcontractDebugFlag)
/*  408 */           dprint(".sendCancelRequestIfFinalFragmentNotSent<-: " + opAndId(this));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean sentFullMessage()
/*      */   {
/*  417 */     return this.outputObject.getBufferManager().sentFullMessage();
/*      */   }
/*      */ 
/*      */   public boolean sentFragment()
/*      */   {
/*  422 */     return this.outputObject.getBufferManager().sentFragment();
/*      */   }
/*      */ 
/*      */   public void setDIIInfo(Request paramRequest)
/*      */   {
/*  427 */     this.diiRequest = paramRequest;
/*      */   }
/*      */ 
/*      */   public boolean isDIIRequest()
/*      */   {
/*  432 */     return this.diiRequest != null;
/*      */   }
/*      */ 
/*      */   public Exception unmarshalDIIUserException(String paramString, InputStream paramInputStream)
/*      */   {
/*  437 */     if (!isDIIRequest()) {
/*  438 */       return null;
/*      */     }
/*      */ 
/*  441 */     ExceptionList localExceptionList = this.diiRequest.exceptions();
/*      */     try
/*      */     {
/*  445 */       for (int i = 0; i < localExceptionList.count(); i++) {
/*  446 */         TypeCode localTypeCode = localExceptionList.item(i);
/*  447 */         if (localTypeCode.id().equals(paramString))
/*      */         {
/*  452 */           Any localAny = this.orb.create_any();
/*  453 */           localAny.read_value(paramInputStream, localTypeCode);
/*      */ 
/*  455 */           return new UnknownUserException(localAny);
/*      */         }
/*      */       }
/*      */     } catch (Exception localException) {
/*  459 */       throw this.wrapper.unexpectedDiiException(localException);
/*      */     }
/*      */ 
/*  463 */     return this.wrapper.unknownCorbaExc(CompletionStatus.COMPLETED_MAYBE);
/*      */   }
/*      */ 
/*      */   public void setDIIException(Exception paramException)
/*      */   {
/*  468 */     this.diiRequest.env().exception(paramException);
/*      */   }
/*      */ 
/*      */   public void handleDIIReply(InputStream paramInputStream)
/*      */   {
/*  473 */     if (!isDIIRequest()) {
/*  474 */       return;
/*      */     }
/*  476 */     ((RequestImpl)this.diiRequest).unmarshalReply(paramInputStream);
/*      */   }
/*      */ 
/*      */   public Message getDispatchHeader()
/*      */   {
/*  481 */     return this.dispatchHeader;
/*      */   }
/*      */ 
/*      */   public void setDispatchHeader(Message paramMessage)
/*      */   {
/*  486 */     this.dispatchHeader = paramMessage;
/*      */   }
/*      */ 
/*      */   public ByteBuffer getDispatchBuffer()
/*      */   {
/*  491 */     return this.dispatchByteBuffer;
/*      */   }
/*      */ 
/*      */   public void setDispatchBuffer(ByteBuffer paramByteBuffer)
/*      */   {
/*  496 */     this.dispatchByteBuffer = paramByteBuffer;
/*      */   }
/*      */ 
/*      */   public int getThreadPoolToUse() {
/*  500 */     int i = 0;
/*  501 */     Message localMessage = getDispatchHeader();
/*      */ 
/*  504 */     if (localMessage != null) {
/*  505 */       i = localMessage.getThreadPoolToUse();
/*      */     }
/*  507 */     return i;
/*      */   }
/*      */ 
/*      */   public byte getStreamFormatVersion()
/*      */   {
/*  517 */     if (this.streamFormatVersionSet) {
/*  518 */       return this.streamFormatVersion;
/*      */     }
/*  520 */     return getStreamFormatVersionForReply();
/*      */   }
/*      */ 
/*      */   public byte getStreamFormatVersionForReply()
/*      */   {
/*  535 */     ServiceContexts localServiceContexts = getRequestServiceContexts();
/*      */ 
/*  537 */     MaxStreamFormatVersionServiceContext localMaxStreamFormatVersionServiceContext = (MaxStreamFormatVersionServiceContext)localServiceContexts.get(17);
/*      */ 
/*  541 */     if (localMaxStreamFormatVersionServiceContext != null) {
/*  542 */       int i = ORBUtility.getMaxStreamFormatVersion();
/*  543 */       int j = localMaxStreamFormatVersionServiceContext.getMaximumStreamFormatVersion();
/*      */ 
/*  545 */       return (byte)Math.min(i, j);
/*      */     }
/*      */ 
/*  549 */     if (getGIOPVersion().lessThan(GIOPVersion.V1_3)) {
/*  550 */       return 1;
/*      */     }
/*  552 */     return 2;
/*      */   }
/*      */ 
/*      */   public boolean isSystemExceptionReply()
/*      */   {
/*  558 */     return this.replyHeader.getReplyStatus() == 2;
/*      */   }
/*      */ 
/*      */   public boolean isUserExceptionReply()
/*      */   {
/*  563 */     return this.replyHeader.getReplyStatus() == 1;
/*      */   }
/*      */ 
/*      */   public boolean isLocationForwardReply()
/*      */   {
/*  568 */     return (this.replyHeader.getReplyStatus() == 3) || (this.replyHeader.getReplyStatus() == 4);
/*      */   }
/*      */ 
/*      */   public boolean isDifferentAddrDispositionRequestedReply()
/*      */   {
/*  575 */     return this.replyHeader.getReplyStatus() == 5;
/*      */   }
/*      */ 
/*      */   public short getAddrDispositionReply()
/*      */   {
/*  580 */     return this.replyHeader.getAddrDisposition();
/*      */   }
/*      */ 
/*      */   public IOR getForwardedIOR()
/*      */   {
/*  585 */     return this.replyHeader.getIOR();
/*      */   }
/*      */ 
/*      */   public SystemException getSystemExceptionReply()
/*      */   {
/*  590 */     return this.replyHeader.getSystemException(this.replyExceptionDetailMessage);
/*      */   }
/*      */ 
/*      */   public ObjectKey getObjectKey()
/*      */   {
/*  600 */     return getRequestHeader().getObjectKey();
/*      */   }
/*      */ 
/*      */   public void setProtocolHandler(CorbaProtocolHandler paramCorbaProtocolHandler)
/*      */   {
/*  605 */     throw this.wrapper.methodShouldNotBeCalled();
/*      */   }
/*      */ 
/*      */   public CorbaProtocolHandler getProtocolHandler()
/*      */   {
/*  611 */     return this;
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.portable.OutputStream createReply()
/*      */   {
/*  623 */     getProtocolHandler().createResponse(this, (ServiceContexts)null);
/*  624 */     return (org.omg.CORBA_2_3.portable.OutputStream)getOutputObject();
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.portable.OutputStream createExceptionReply()
/*      */   {
/*  631 */     getProtocolHandler().createUserExceptionResponse(this, (ServiceContexts)null);
/*  632 */     return (org.omg.CORBA_2_3.portable.OutputStream)getOutputObject();
/*      */   }
/*      */ 
/*      */   public boolean executeReturnServantInResponseConstructor()
/*      */   {
/*  637 */     return this._executeReturnServantInResponseConstructor;
/*      */   }
/*      */ 
/*      */   public void setExecuteReturnServantInResponseConstructor(boolean paramBoolean)
/*      */   {
/*  643 */     this._executeReturnServantInResponseConstructor = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean executeRemoveThreadInfoInResponseConstructor()
/*      */   {
/*  648 */     return this._executeRemoveThreadInfoInResponseConstructor;
/*      */   }
/*      */ 
/*      */   public void setExecuteRemoveThreadInfoInResponseConstructor(boolean paramBoolean)
/*      */   {
/*  653 */     this._executeRemoveThreadInfoInResponseConstructor = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean executePIInResponseConstructor()
/*      */   {
/*  658 */     return this._executePIInResponseConstructor;
/*      */   }
/*      */ 
/*      */   public void setExecutePIInResponseConstructor(boolean paramBoolean)
/*      */   {
/*  663 */     this._executePIInResponseConstructor = paramBoolean;
/*      */   }
/*      */ 
/*      */   private byte getStreamFormatVersionForThisRequest(IOR paramIOR, GIOPVersion paramGIOPVersion)
/*      */   {
/*  670 */     int i = ORBUtility.getMaxStreamFormatVersion();
/*      */ 
/*  673 */     IOR localIOR = this.contactInfo.getEffectiveTargetIOR();
/*      */ 
/*  675 */     IIOPProfileTemplate localIIOPProfileTemplate = (IIOPProfileTemplate)localIOR.getProfile().getTaggedProfileTemplate();
/*      */ 
/*  677 */     Iterator localIterator = localIIOPProfileTemplate.iteratorById(38);
/*  678 */     if (!localIterator.hasNext())
/*      */     {
/*  681 */       if (paramGIOPVersion.lessThan(GIOPVersion.V1_3)) {
/*  682 */         return 1;
/*      */       }
/*  684 */       return 2;
/*      */     }
/*      */ 
/*  687 */     int j = ((MaxStreamFormatVersionComponent)localIterator.next()).getMaxStreamFormatVersion();
/*      */ 
/*  690 */     return (byte)Math.min(i, j);
/*      */   }
/*      */ 
/*      */   public boolean handleRequest(MessageMediator paramMessageMediator)
/*      */   {
/*      */     try
/*      */     {
/*  712 */       this.dispatchHeader.callback(this);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*  717 */     return this.isThreadDone;
/*      */   }
/*      */ 
/*      */   private void setWorkThenPoolOrResumeSelect(Message paramMessage)
/*      */   {
/*  727 */     if (getConnection().getEventHandler().shouldUseSelectThreadToWait()) {
/*  728 */       resumeSelect(paramMessage);
/*      */     }
/*      */     else
/*      */     {
/*  733 */       this.isThreadDone = true;
/*      */ 
/*  736 */       this.orb.getTransportManager().getSelector(0).unregisterForEvent(getConnection().getEventHandler());
/*      */ 
/*  739 */       this.orb.getTransportManager().getSelector(0).registerForEvent(getConnection().getEventHandler());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setWorkThenReadOrResumeSelect(Message paramMessage)
/*      */   {
/*  746 */     if (getConnection().getEventHandler().shouldUseSelectThreadToWait()) {
/*  747 */       resumeSelect(paramMessage);
/*      */     }
/*      */     else
/*      */     {
/*  751 */       this.isThreadDone = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resumeSelect(Message paramMessage)
/*      */   {
/*  761 */     if (transportDebug()) {
/*  762 */       dprint(".resumeSelect:->");
/*      */ 
/*  764 */       localObject = "?";
/*  765 */       if ((paramMessage instanceof RequestMessage)) {
/*  766 */         localObject = new Integer(((RequestMessage)paramMessage).getRequestId()).toString();
/*      */       }
/*  769 */       else if ((paramMessage instanceof ReplyMessage)) {
/*  770 */         localObject = new Integer(((ReplyMessage)paramMessage).getRequestId()).toString();
/*      */       }
/*  773 */       else if ((paramMessage instanceof FragmentMessage_1_2)) {
/*  774 */         localObject = new Integer(((FragmentMessage_1_2)paramMessage).getRequestId()).toString();
/*      */       }
/*      */ 
/*  778 */       dprint(".resumeSelect: id/" + (String)localObject + " " + getConnection());
/*      */     }
/*      */ 
/*  789 */     Object localObject = getConnection().getEventHandler();
/*  790 */     this.orb.getTransportManager().getSelector(0).registerInterestOps((EventHandler)localObject);
/*      */ 
/*  792 */     if (transportDebug())
/*  793 */       dprint(".resumeSelect:<-");
/*      */   }
/*      */ 
/*      */   private void setInputObject()
/*      */   {
/*  802 */     if (getConnection().getContactInfo() != null) {
/*  803 */       this.inputObject = ((CDRInputObject)getConnection().getContactInfo().createInputObject(this.orb, this));
/*      */     }
/*  806 */     else if (getConnection().getAcceptor() != null) {
/*  807 */       this.inputObject = ((CDRInputObject)getConnection().getAcceptor().createInputObject(this.orb, this));
/*      */     }
/*      */     else
/*      */     {
/*  811 */       throw new RuntimeException("CorbaMessageMediatorImpl.setInputObject");
/*      */     }
/*  813 */     this.inputObject.setMessageMediator(this);
/*  814 */     setInputObject(this.inputObject);
/*      */   }
/*      */ 
/*      */   private void signalResponseReceived()
/*      */   {
/*  822 */     this.connection.getResponseWaitingRoom().responseReceived(this.inputObject);
/*      */   }
/*      */ 
/*      */   public void handleInput(Message paramMessage)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  830 */       this.messageHeader = paramMessage;
/*      */ 
/*  832 */       if (transportDebug()) {
/*  833 */         dprint(".handleInput->: " + MessageBase.typeToString(paramMessage.getType()));
/*      */       }
/*      */ 
/*  836 */       setWorkThenReadOrResumeSelect(paramMessage);
/*      */ 
/*  838 */       switch (paramMessage.getType())
/*      */       {
/*      */       case 5:
/*  841 */         if (transportDebug()) {
/*  842 */           dprint(".handleInput: CloseConnection: purging");
/*      */         }
/*  844 */         this.connection.purgeCalls(this.wrapper.connectionRebind(), true, false);
/*  845 */         break;
/*      */       case 6:
/*  847 */         if (transportDebug()) {
/*  848 */           dprint(".handleInput: MessageError: purging");
/*      */         }
/*  850 */         this.connection.purgeCalls(this.wrapper.recvMsgError(), true, false);
/*  851 */         break;
/*      */       default:
/*  857 */         throw this.wrapper.badGiopRequestType();
/*      */       }
/*  859 */       releaseByteBufferToPool();
/*      */     } finally {
/*  861 */       if (transportDebug())
/*  862 */         dprint(".handleInput<-: " + MessageBase.typeToString(paramMessage.getType()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(RequestMessage_1_0 paramRequestMessage_1_0)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  871 */       if (transportDebug()) dprint(".REQUEST 1.0->: " + paramRequestMessage_1_0); try
/*      */       {
/*  873 */         this.messageHeader = (this.requestHeader = paramRequestMessage_1_0);
/*  874 */         setInputObject();
/*      */       } finally {
/*  876 */         setWorkThenPoolOrResumeSelect(paramRequestMessage_1_0);
/*      */       }
/*  878 */       getProtocolHandler().handleRequest(paramRequestMessage_1_0, this);
/*      */     } catch (Throwable localThrowable) {
/*  880 */       if (transportDebug())
/*  881 */         dprint(".REQUEST 1.0: !!ERROR!!: " + paramRequestMessage_1_0, localThrowable);
/*      */     }
/*      */     finally {
/*  884 */       if (transportDebug()) dprint(".REQUEST 1.0<-: " + paramRequestMessage_1_0); 
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(RequestMessage_1_1 paramRequestMessage_1_1) throws IOException
/*      */   {
/*      */     try {
/*  891 */       if (transportDebug()) dprint(".REQUEST 1.1->: " + paramRequestMessage_1_1); try
/*      */       {
/*  893 */         this.messageHeader = (this.requestHeader = paramRequestMessage_1_1);
/*  894 */         setInputObject();
/*  895 */         this.connection.serverRequest_1_1_Put(this);
/*      */       } finally {
/*  897 */         setWorkThenPoolOrResumeSelect(paramRequestMessage_1_1);
/*      */       }
/*  899 */       getProtocolHandler().handleRequest(paramRequestMessage_1_1, this);
/*      */     } catch (Throwable localThrowable) {
/*  901 */       if (transportDebug())
/*  902 */         dprint(".REQUEST 1.1: !!ERROR!!: " + paramRequestMessage_1_1, localThrowable);
/*      */     }
/*      */     finally {
/*  905 */       if (transportDebug()) dprint(".REQUEST 1.1<-: " + paramRequestMessage_1_1); 
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(RequestMessage_1_2 paramRequestMessage_1_2)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*      */       try
/*      */       {
/*  915 */         this.messageHeader = (this.requestHeader = paramRequestMessage_1_2);
/*      */ 
/*  917 */         paramRequestMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
/*  918 */         setInputObject();
/*      */ 
/*  920 */         if (transportDebug()) dprint(".REQUEST 1.2->: id/" + paramRequestMessage_1_2.getRequestId() + ": " + paramRequestMessage_1_2);
/*      */ 
/*  931 */         this.connection.serverRequestMapPut(paramRequestMessage_1_2.getRequestId(), this);
/*      */       }
/*      */       finally
/*      */       {
/*  937 */         setWorkThenPoolOrResumeSelect(paramRequestMessage_1_2);
/*      */       }
/*      */ 
/*  940 */       getProtocolHandler().handleRequest(paramRequestMessage_1_2, this);
/*      */     } catch (Throwable localThrowable) {
/*  942 */       if (transportDebug()) dprint(".REQUEST 1.2: id/" + paramRequestMessage_1_2.getRequestId() + ": !!ERROR!!: " + paramRequestMessage_1_2, localThrowable);
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*  949 */       this.connection.serverRequestMapRemove(paramRequestMessage_1_2.getRequestId());
/*      */ 
/*  951 */       if (transportDebug()) dprint(".REQUEST 1.2<-: id/" + paramRequestMessage_1_2.getRequestId() + ": " + paramRequestMessage_1_2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(ReplyMessage_1_0 paramReplyMessage_1_0)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*      */       try
/*      */       {
/*  962 */         if (transportDebug()) dprint(".REPLY 1.0->: " + paramReplyMessage_1_0);
/*  963 */         this.messageHeader = (this.replyHeader = paramReplyMessage_1_0);
/*  964 */         setInputObject();
/*      */ 
/*  967 */         this.inputObject.unmarshalHeader();
/*      */ 
/*  969 */         signalResponseReceived();
/*      */       } finally {
/*  971 */         setWorkThenReadOrResumeSelect(paramReplyMessage_1_0);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/*  974 */       if (transportDebug()) dprint(".REPLY 1.0: !!ERROR!!: " + paramReplyMessage_1_0, localThrowable); 
/*      */     }
/*      */     finally
/*      */     {
/*  977 */       if (transportDebug()) dprint(".REPLY 1.0<-: " + paramReplyMessage_1_0); 
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(ReplyMessage_1_1 paramReplyMessage_1_1) throws IOException
/*      */   {
/*      */     try {
/*  984 */       if (transportDebug()) dprint(".REPLY 1.1->: " + paramReplyMessage_1_1);
/*  985 */       this.messageHeader = (this.replyHeader = paramReplyMessage_1_1);
/*  986 */       setInputObject();
/*      */ 
/*  988 */       if (paramReplyMessage_1_1.moreFragmentsToFollow())
/*      */       {
/*  992 */         this.connection.clientReply_1_1_Put(this);
/*      */ 
/*  998 */         setWorkThenPoolOrResumeSelect(paramReplyMessage_1_1);
/*      */ 
/* 1002 */         this.inputObject.unmarshalHeader();
/*      */ 
/* 1004 */         signalResponseReceived();
/*      */       }
/*      */       else
/*      */       {
/* 1015 */         this.inputObject.unmarshalHeader();
/*      */ 
/* 1017 */         signalResponseReceived();
/*      */ 
/* 1019 */         setWorkThenReadOrResumeSelect(paramReplyMessage_1_1);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1022 */       if (transportDebug()) dprint(".REPLY 1.1: !!ERROR!!: " + paramReplyMessage_1_1); 
/*      */     }
/*      */     finally
/*      */     {
/* 1025 */       if (transportDebug()) dprint(".REPLY 1.1<-: " + paramReplyMessage_1_1); 
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(ReplyMessage_1_2 paramReplyMessage_1_2) throws IOException
/*      */   {
/*      */     try
/*      */     {
/*      */       try { this.messageHeader = (this.replyHeader = paramReplyMessage_1_2);
/*      */ 
/* 1036 */         paramReplyMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
/*      */ 
/* 1038 */         if (transportDebug()) {
/* 1039 */           dprint(".REPLY 1.2->: id/" + paramReplyMessage_1_2.getRequestId() + ": more?: " + paramReplyMessage_1_2.moreFragmentsToFollow() + ": " + paramReplyMessage_1_2);
/*      */         }
/*      */ 
/* 1045 */         setInputObject();
/*      */ 
/* 1047 */         signalResponseReceived();
/*      */       } finally {
/* 1049 */         setWorkThenReadOrResumeSelect(paramReplyMessage_1_2);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1052 */       if (transportDebug()) dprint(".REPLY 1.2: id/" + paramReplyMessage_1_2.getRequestId() + ": !!ERROR!!: " + paramReplyMessage_1_2, localThrowable);
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1058 */       if (transportDebug()) dprint(".REPLY 1.2<-: id/" + paramReplyMessage_1_2.getRequestId() + ": " + paramReplyMessage_1_2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(LocateRequestMessage_1_0 paramLocateRequestMessage_1_0)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1068 */       if (transportDebug())
/* 1069 */         dprint(".LOCATE_REQUEST 1.0->: " + paramLocateRequestMessage_1_0);
/*      */       try {
/* 1071 */         this.messageHeader = paramLocateRequestMessage_1_0;
/* 1072 */         setInputObject();
/*      */       } finally {
/* 1074 */         setWorkThenPoolOrResumeSelect(paramLocateRequestMessage_1_0);
/*      */       }
/* 1076 */       getProtocolHandler().handleRequest(paramLocateRequestMessage_1_0, this);
/*      */     } catch (Throwable localThrowable) {
/* 1078 */       if (transportDebug())
/* 1079 */         dprint(".LOCATE_REQUEST 1.0: !!ERROR!!: " + paramLocateRequestMessage_1_0, localThrowable);
/*      */     }
/*      */     finally {
/* 1082 */       if (transportDebug())
/* 1083 */         dprint(".LOCATE_REQUEST 1.0<-: " + paramLocateRequestMessage_1_0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(LocateRequestMessage_1_1 paramLocateRequestMessage_1_1) throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1091 */       if (transportDebug())
/* 1092 */         dprint(".LOCATE_REQUEST 1.1->: " + paramLocateRequestMessage_1_1);
/*      */       try {
/* 1094 */         this.messageHeader = paramLocateRequestMessage_1_1;
/* 1095 */         setInputObject();
/*      */       } finally {
/* 1097 */         setWorkThenPoolOrResumeSelect(paramLocateRequestMessage_1_1);
/*      */       }
/* 1099 */       getProtocolHandler().handleRequest(paramLocateRequestMessage_1_1, this);
/*      */     } catch (Throwable localThrowable) {
/* 1101 */       if (transportDebug())
/* 1102 */         dprint(".LOCATE_REQUEST 1.1: !!ERROR!!: " + paramLocateRequestMessage_1_1, localThrowable);
/*      */     }
/*      */     finally {
/* 1105 */       if (transportDebug())
/* 1106 */         dprint(".LOCATE_REQUEST 1.1<-:" + paramLocateRequestMessage_1_1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(LocateRequestMessage_1_2 paramLocateRequestMessage_1_2) throws IOException
/*      */   {
/*      */     try {
/*      */       try {
/* 1114 */         this.messageHeader = paramLocateRequestMessage_1_2;
/*      */ 
/* 1116 */         paramLocateRequestMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
/* 1117 */         setInputObject();
/*      */ 
/* 1119 */         if (transportDebug()) {
/* 1120 */           dprint(".LOCATE_REQUEST 1.2->: id/" + paramLocateRequestMessage_1_2.getRequestId() + ": " + paramLocateRequestMessage_1_2);
/*      */         }
/*      */ 
/* 1125 */         if (paramLocateRequestMessage_1_2.moreFragmentsToFollow())
/* 1126 */           this.connection.serverRequestMapPut(paramLocateRequestMessage_1_2.getRequestId(), this);
/*      */       }
/*      */       finally {
/* 1129 */         setWorkThenPoolOrResumeSelect(paramLocateRequestMessage_1_2);
/*      */       }
/* 1131 */       getProtocolHandler().handleRequest(paramLocateRequestMessage_1_2, this);
/*      */     } catch (Throwable localThrowable) {
/* 1133 */       if (transportDebug()) {
/* 1134 */         dprint(".LOCATE_REQUEST 1.2: id/" + paramLocateRequestMessage_1_2.getRequestId() + ": !!ERROR!!: " + paramLocateRequestMessage_1_2, localThrowable);
/*      */       }
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1140 */       if (transportDebug())
/* 1141 */         dprint(".LOCATE_REQUEST 1.2<-: id/" + paramLocateRequestMessage_1_2.getRequestId() + ": " + paramLocateRequestMessage_1_2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(LocateReplyMessage_1_0 paramLocateReplyMessage_1_0)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1151 */       if (transportDebug())
/* 1152 */         dprint(".LOCATE_REPLY 1.0->:" + paramLocateReplyMessage_1_0);
/*      */       try {
/* 1154 */         this.messageHeader = paramLocateReplyMessage_1_0;
/* 1155 */         setInputObject();
/* 1156 */         this.inputObject.unmarshalHeader();
/* 1157 */         signalResponseReceived();
/*      */       } finally {
/* 1159 */         setWorkThenReadOrResumeSelect(paramLocateReplyMessage_1_0);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1162 */       if (transportDebug())
/* 1163 */         dprint(".LOCATE_REPLY 1.0: !!ERROR!!: " + paramLocateReplyMessage_1_0, localThrowable);
/*      */     }
/*      */     finally {
/* 1166 */       if (transportDebug())
/* 1167 */         dprint(".LOCATE_REPLY 1.0<-: " + paramLocateReplyMessage_1_0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(LocateReplyMessage_1_1 paramLocateReplyMessage_1_1) throws IOException
/*      */   {
/*      */     try {
/* 1174 */       if (transportDebug()) dprint(".LOCATE_REPLY 1.1->: " + paramLocateReplyMessage_1_1); try
/*      */       {
/* 1176 */         this.messageHeader = paramLocateReplyMessage_1_1;
/* 1177 */         setInputObject();
/*      */ 
/* 1179 */         this.inputObject.unmarshalHeader();
/* 1180 */         signalResponseReceived();
/*      */       } finally {
/* 1182 */         setWorkThenReadOrResumeSelect(paramLocateReplyMessage_1_1);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1185 */       if (transportDebug())
/* 1186 */         dprint(".LOCATE_REPLY 1.1: !!ERROR!!: " + paramLocateReplyMessage_1_1, localThrowable);
/*      */     }
/*      */     finally {
/* 1189 */       if (transportDebug()) dprint(".LOCATE_REPLY 1.1<-: " + paramLocateReplyMessage_1_1); 
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(LocateReplyMessage_1_2 paramLocateReplyMessage_1_2) throws IOException
/*      */   {
/*      */     try
/*      */     {
/*      */       try { this.messageHeader = paramLocateReplyMessage_1_2;
/*      */ 
/* 1200 */         paramLocateReplyMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
/*      */ 
/* 1202 */         setInputObject();
/*      */ 
/* 1204 */         if (transportDebug()) dprint(".LOCATE_REPLY 1.2->: id/" + paramLocateReplyMessage_1_2.getRequestId() + ": " + paramLocateReplyMessage_1_2);
/*      */ 
/* 1209 */         signalResponseReceived();
/*      */       } finally {
/* 1211 */         setWorkThenPoolOrResumeSelect(paramLocateReplyMessage_1_2);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1214 */       if (transportDebug()) {
/* 1215 */         dprint(".LOCATE_REPLY 1.2: id/" + paramLocateReplyMessage_1_2.getRequestId() + ": !!ERROR!!: " + paramLocateReplyMessage_1_2, localThrowable);
/*      */       }
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1221 */       if (transportDebug()) dprint(".LOCATE_REPLY 1.2<-: id/" + paramLocateReplyMessage_1_2.getRequestId() + ": " + paramLocateReplyMessage_1_2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(FragmentMessage_1_1 paramFragmentMessage_1_1)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1231 */       if (transportDebug()) {
/* 1232 */         dprint(".FRAGMENT 1.1->: more?: " + paramFragmentMessage_1_1.moreFragmentsToFollow() + ": " + paramFragmentMessage_1_1);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1237 */         this.messageHeader = paramFragmentMessage_1_1;
/* 1238 */         MessageMediator localMessageMediator = null;
/* 1239 */         CDRInputObject localCDRInputObject = null;
/*      */ 
/* 1241 */         if (this.connection.isServer())
/* 1242 */           localMessageMediator = this.connection.serverRequest_1_1_Get();
/*      */         else {
/* 1244 */           localMessageMediator = this.connection.clientReply_1_1_Get();
/*      */         }
/* 1246 */         if (localMessageMediator != null) {
/* 1247 */           localCDRInputObject = (CDRInputObject)localMessageMediator.getInputObject();
/*      */         }
/*      */ 
/* 1259 */         if (localCDRInputObject == null) {
/* 1260 */           if (transportDebug()) {
/* 1261 */             dprint(".FRAGMENT 1.1: ++++DISCARDING++++: " + paramFragmentMessage_1_1);
/*      */           }
/*      */ 
/* 1264 */           releaseByteBufferToPool();
/*      */ 
/* 1281 */           setWorkThenReadOrResumeSelect(paramFragmentMessage_1_1);
/*      */         }
/*      */         else
/*      */         {
/* 1268 */           localCDRInputObject.getBufferManager().processFragment(this.dispatchByteBuffer, paramFragmentMessage_1_1);
/*      */ 
/* 1271 */           if (!paramFragmentMessage_1_1.moreFragmentsToFollow()) {
/* 1272 */             if (this.connection.isServer())
/* 1273 */               this.connection.serverRequest_1_1_Remove();
/*      */             else
/* 1275 */               this.connection.clientReply_1_1_Remove();
/*      */           }
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 1281 */         setWorkThenReadOrResumeSelect(paramFragmentMessage_1_1);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1284 */       if (transportDebug())
/* 1285 */         dprint(".FRAGMENT 1.1: !!ERROR!!: " + paramFragmentMessage_1_1, localThrowable);
/*      */     }
/*      */     finally {
/* 1288 */       if (transportDebug()) dprint(".FRAGMENT 1.1<-: " + paramFragmentMessage_1_1); 
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(FragmentMessage_1_2 paramFragmentMessage_1_2) throws IOException
/*      */   {
/*      */     try
/*      */     {
/*      */       try { this.messageHeader = paramFragmentMessage_1_2;
/*      */ 
/* 1303 */         paramFragmentMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
/*      */ 
/* 1305 */         if (transportDebug()) {
/* 1306 */           dprint(".FRAGMENT 1.2->: id/" + paramFragmentMessage_1_2.getRequestId() + ": more?: " + paramFragmentMessage_1_2.moreFragmentsToFollow() + ": " + paramFragmentMessage_1_2);
/*      */         }
/*      */ 
/* 1312 */         Object localObject1 = null;
/* 1313 */         InputObject localInputObject = null;
/*      */ 
/* 1315 */         if (this.connection.isServer()) {
/* 1316 */           localObject1 = this.connection.serverRequestMapGet(paramFragmentMessage_1_2.getRequestId());
/*      */         }
/*      */         else {
/* 1319 */           localObject1 = this.connection.clientRequestMapGet(paramFragmentMessage_1_2.getRequestId());
/*      */         }
/*      */ 
/* 1322 */         if (localObject1 != null) {
/* 1323 */           localInputObject = ((MessageMediator)localObject1).getInputObject();
/*      */         }
/*      */ 
/* 1326 */         if (localInputObject == null) {
/* 1327 */           if (transportDebug()) {
/* 1328 */             dprint(".FRAGMENT 1.2: id/" + paramFragmentMessage_1_2.getRequestId() + ": ++++DISCARDING++++: " + paramFragmentMessage_1_2);
/*      */           }
/*      */ 
/* 1335 */           releaseByteBufferToPool();
/*      */ 
/* 1356 */           setWorkThenReadOrResumeSelect(paramFragmentMessage_1_2); } else { ((CDRInputObject)localInputObject).getBufferManager().processFragment(this.dispatchByteBuffer, paramFragmentMessage_1_2);
/*      */ 
/* 1344 */           if (this.connection.isServer()); }  } finally { setWorkThenReadOrResumeSelect(paramFragmentMessage_1_2); }
/*      */     }
/*      */     catch (Throwable localThrowable) {
/* 1359 */       if (transportDebug()) {
/* 1360 */         dprint(".FRAGMENT 1.2: id/" + paramFragmentMessage_1_2.getRequestId() + ": !!ERROR!!: " + paramFragmentMessage_1_2, localThrowable);
/*      */       }
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1366 */       if (transportDebug()) dprint(".FRAGMENT 1.2<-: id/" + paramFragmentMessage_1_2.getRequestId() + ": " + paramFragmentMessage_1_2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleInput(CancelRequestMessage paramCancelRequestMessage)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*      */       try
/*      */       {
/* 1377 */         this.messageHeader = paramCancelRequestMessage;
/* 1378 */         setInputObject();
/*      */ 
/* 1381 */         this.inputObject.unmarshalHeader();
/*      */ 
/* 1383 */         if (transportDebug()) dprint(".CANCEL->: id/" + paramCancelRequestMessage.getRequestId() + ": " + paramCancelRequestMessage.getGIOPVersion() + ": " + paramCancelRequestMessage);
/*      */ 
/* 1388 */         processCancelRequest(paramCancelRequestMessage.getRequestId());
/* 1389 */         releaseByteBufferToPool();
/*      */       } finally {
/* 1391 */         setWorkThenReadOrResumeSelect(paramCancelRequestMessage);
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1394 */       if (transportDebug()) dprint(".CANCEL: id/" + paramCancelRequestMessage.getRequestId() + ": !!ERROR!!: " + paramCancelRequestMessage, localThrowable);
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1400 */       if (transportDebug()) dprint(".CANCEL<-: id/" + paramCancelRequestMessage.getRequestId() + ": " + paramCancelRequestMessage.getGIOPVersion() + ": " + paramCancelRequestMessage);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void throwNotImplemented()
/*      */   {
/* 1409 */     this.isThreadDone = false;
/* 1410 */     throwNotImplemented("");
/*      */   }
/*      */ 
/*      */   private void throwNotImplemented(String paramString)
/*      */   {
/* 1415 */     throw new RuntimeException("CorbaMessageMediatorImpl: not implemented " + paramString);
/*      */   }
/*      */ 
/*      */   private void dprint(String paramString, Throwable paramThrowable)
/*      */   {
/* 1420 */     dprint(paramString);
/* 1421 */     paramThrowable.printStackTrace(System.out);
/*      */   }
/*      */ 
/*      */   private void dprint(String paramString)
/*      */   {
/* 1426 */     ORBUtility.dprint("CorbaMessageMediatorImpl", paramString);
/*      */   }
/*      */ 
/*      */   protected String opAndId(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1431 */     return ORBUtility.operationNameAndRequestId(paramCorbaMessageMediator);
/*      */   }
/*      */ 
/*      */   private boolean transportDebug()
/*      */   {
/* 1436 */     return this.orb.transportDebugFlag;
/*      */   }
/*      */ 
/*      */   private final void processCancelRequest(int paramInt)
/*      */   {
/* 1474 */     if (!this.connection.isServer()) {
/* 1475 */       return;
/*      */     }
/*      */ 
/* 1483 */     Object localObject = this.connection.serverRequestMapGet(paramInt);
/*      */     int i;
/* 1485 */     if (localObject == null)
/*      */     {
/* 1487 */       localObject = this.connection.serverRequest_1_1_Get();
/* 1488 */       if (localObject == null)
/*      */       {
/* 1494 */         return;
/*      */       }
/*      */ 
/* 1497 */       i = ((MessageHandler)localObject).getRequestId();
/*      */ 
/* 1499 */       if (i != paramInt)
/*      */       {
/* 1502 */         return;
/*      */       }
/*      */ 
/* 1505 */       if (i != 0);
/*      */     }
/*      */     else
/*      */     {
/* 1519 */       i = ((MessageHandler)localObject).getRequestId();
/*      */     }
/*      */ 
/* 1522 */     RequestMessage localRequestMessage = ((MessageHandler)localObject).getRequestHeader();
/* 1523 */     if (localRequestMessage.getType() != 0)
/*      */     {
/* 1526 */       this.wrapper.badMessageTypeForCancel();
/*      */     }
/*      */ 
/* 1542 */     BufferManagerReadStream localBufferManagerReadStream = (BufferManagerReadStream)((CDRInputObject)((MessageMediator)localObject).getInputObject()).getBufferManager();
/*      */ 
/* 1544 */     localBufferManagerReadStream.cancelProcessing(paramInt);
/*      */   }
/*      */ 
/*      */   public void handleRequest(RequestMessage paramRequestMessage, CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/*      */     try
/*      */     {
/* 1556 */       beginRequest(paramCorbaMessageMediator);
/*      */       try {
/* 1558 */         handleRequestRequest(paramCorbaMessageMediator);
/* 1559 */         if (paramCorbaMessageMediator.isOneWay())
/*      */           return;
/*      */       }
/*      */       catch (Throwable localThrowable1) {
/* 1563 */         if (paramCorbaMessageMediator.isOneWay()) {
/*      */           return;
/*      */         }
/* 1566 */         handleThrowableDuringServerDispatch(paramCorbaMessageMediator, localThrowable1, CompletionStatus.COMPLETED_MAYBE);
/*      */       }
/*      */ 
/* 1569 */       sendResponse(paramCorbaMessageMediator);
/*      */     } catch (Throwable localThrowable2) {
/* 1571 */       dispatchError(paramCorbaMessageMediator, "RequestMessage", localThrowable2);
/*      */     } finally {
/* 1573 */       endRequest(paramCorbaMessageMediator);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleRequest(LocateRequestMessage paramLocateRequestMessage, CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/*      */     try
/*      */     {
/* 1581 */       beginRequest(paramCorbaMessageMediator);
/*      */       try {
/* 1583 */         handleLocateRequest(paramCorbaMessageMediator);
/*      */       } catch (Throwable localThrowable1) {
/* 1585 */         handleThrowableDuringServerDispatch(paramCorbaMessageMediator, localThrowable1, CompletionStatus.COMPLETED_MAYBE);
/*      */       }
/*      */ 
/* 1588 */       sendResponse(paramCorbaMessageMediator);
/*      */     } catch (Throwable localThrowable2) {
/* 1590 */       dispatchError(paramCorbaMessageMediator, "LocateRequestMessage", localThrowable2);
/*      */     } finally {
/* 1592 */       endRequest(paramCorbaMessageMediator);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void beginRequest(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1598 */     ORB localORB = (ORB)paramCorbaMessageMediator.getBroker();
/* 1599 */     if (localORB.subcontractDebugFlag) {
/* 1600 */       dprint(".handleRequest->:");
/*      */     }
/* 1602 */     this.connection.serverRequestProcessingBegins();
/*      */   }
/*      */ 
/*      */   private void dispatchError(CorbaMessageMediator paramCorbaMessageMediator, String paramString, Throwable paramThrowable)
/*      */   {
/* 1608 */     if (this.orb.subcontractDebugFlag)
/* 1609 */       dprint(".handleRequest: " + opAndId(paramCorbaMessageMediator) + ": !!ERROR!!: " + paramString, paramThrowable);
/*      */   }
/*      */ 
/*      */   private void sendResponse(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1620 */     if (this.orb.subcontractDebugFlag) {
/* 1621 */       dprint(".handleRequest: " + opAndId(paramCorbaMessageMediator) + ": sending response");
/*      */     }
/*      */ 
/* 1625 */     CDROutputObject localCDROutputObject = (CDROutputObject)paramCorbaMessageMediator.getOutputObject();
/*      */ 
/* 1627 */     if (localCDROutputObject != null)
/*      */     {
/* 1629 */       localCDROutputObject.finishSendingMessage();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void endRequest(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1635 */     ORB localORB = (ORB)paramCorbaMessageMediator.getBroker();
/* 1636 */     if (localORB.subcontractDebugFlag) {
/* 1637 */       dprint(".handleRequest<-: " + opAndId(paramCorbaMessageMediator));
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1643 */       OutputObject localOutputObject = paramCorbaMessageMediator.getOutputObject();
/* 1644 */       if (localOutputObject != null) {
/* 1645 */         localOutputObject.close();
/*      */       }
/* 1647 */       InputObject localInputObject = paramCorbaMessageMediator.getInputObject();
/* 1648 */       if (localInputObject != null) {
/* 1649 */         localInputObject.close();
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1655 */       if (localORB.subcontractDebugFlag)
/* 1656 */         dprint(".endRequest: IOException:" + localIOException.getMessage(), localIOException);
/*      */     }
/*      */     finally {
/* 1659 */       ((CorbaConnection)paramCorbaMessageMediator.getConnection()).serverRequestProcessingEnds();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void handleRequestRequest(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1666 */     ((CDRInputObject)paramCorbaMessageMediator.getInputObject()).unmarshalHeader();
/*      */ 
/* 1668 */     ORB localORB = (ORB)paramCorbaMessageMediator.getBroker();
/* 1669 */     synchronized (localORB) {
/* 1670 */       localORB.checkShutdownState();
/*      */     }
/*      */ 
/* 1673 */     ??? = paramCorbaMessageMediator.getObjectKey();
/* 1674 */     if (localORB.subcontractDebugFlag) {
/* 1675 */       localObject2 = ((ObjectKey)???).getTemplate();
/* 1676 */       dprint(".handleRequest: " + opAndId(paramCorbaMessageMediator) + ": dispatching to scid: " + ((ObjectKeyTemplate)localObject2).getSubcontractId());
/*      */     }
/*      */ 
/* 1680 */     Object localObject2 = ((ObjectKey)???).getServerRequestDispatcher(localORB);
/*      */ 
/* 1682 */     if (localORB.subcontractDebugFlag) {
/* 1683 */       dprint(".handleRequest: " + opAndId(paramCorbaMessageMediator) + ": dispatching to sc: " + localObject2);
/*      */     }
/*      */ 
/* 1687 */     if (localObject2 == null) {
/* 1688 */       throw this.wrapper.noServerScInDispatch();
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1699 */       localORB.startingDispatch();
/* 1700 */       ((CorbaServerRequestDispatcher)localObject2).dispatch(paramCorbaMessageMediator);
/*      */     } finally {
/* 1702 */       localORB.finishedDispatch();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void handleLocateRequest(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1708 */     ORB localORB = (ORB)paramCorbaMessageMediator.getBroker();
/* 1709 */     LocateRequestMessage localLocateRequestMessage = (LocateRequestMessage)paramCorbaMessageMediator.getDispatchHeader();
/*      */ 
/* 1711 */     IOR localIOR = null;
/* 1712 */     LocateReplyMessage localLocateReplyMessage = null;
/* 1713 */     short s = -1;
/*      */     try
/*      */     {
/* 1716 */       ((CDRInputObject)paramCorbaMessageMediator.getInputObject()).unmarshalHeader();
/* 1717 */       CorbaServerRequestDispatcher localCorbaServerRequestDispatcher = localLocateRequestMessage.getObjectKey().getServerRequestDispatcher(localORB);
/*      */ 
/* 1719 */       if (localCorbaServerRequestDispatcher == null) {
/* 1720 */         return;
/*      */       }
/*      */ 
/* 1723 */       localIOR = localCorbaServerRequestDispatcher.locate(localLocateRequestMessage.getObjectKey());
/*      */ 
/* 1725 */       if (localIOR == null) {
/* 1726 */         localLocateReplyMessage = MessageBase.createLocateReply(localORB, localLocateRequestMessage.getGIOPVersion(), localLocateRequestMessage.getEncodingVersion(), localLocateRequestMessage.getRequestId(), 1, null);
/*      */       }
/*      */       else
/*      */       {
/* 1733 */         localLocateReplyMessage = MessageBase.createLocateReply(localORB, localLocateRequestMessage.getGIOPVersion(), localLocateRequestMessage.getEncodingVersion(), localLocateRequestMessage.getRequestId(), 2, localIOR);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (AddressingDispositionException localAddressingDispositionException)
/*      */     {
/* 1746 */       localLocateReplyMessage = MessageBase.createLocateReply(localORB, localLocateRequestMessage.getGIOPVersion(), localLocateRequestMessage.getEncodingVersion(), localLocateRequestMessage.getRequestId(), 5, null);
/*      */ 
/* 1752 */       s = localAddressingDispositionException.expectedAddrDisp();
/*      */     }
/*      */     catch (RequestCanceledException localRequestCanceledException)
/*      */     {
/* 1756 */       return;
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1766 */       localLocateReplyMessage = MessageBase.createLocateReply(localORB, localLocateRequestMessage.getGIOPVersion(), localLocateRequestMessage.getEncodingVersion(), localLocateRequestMessage.getRequestId(), 0, null);
/*      */     }
/*      */ 
/* 1773 */     CDROutputObject localCDROutputObject = createAppropriateOutputObject(paramCorbaMessageMediator, localLocateRequestMessage, localLocateReplyMessage);
/*      */ 
/* 1776 */     paramCorbaMessageMediator.setOutputObject(localCDROutputObject);
/* 1777 */     localCDROutputObject.setMessageMediator(paramCorbaMessageMediator);
/*      */ 
/* 1779 */     localLocateReplyMessage.write(localCDROutputObject);
/*      */ 
/* 1781 */     if (localIOR != null) {
/* 1782 */       localIOR.write(localCDROutputObject);
/*      */     }
/* 1784 */     if (s != -1)
/* 1785 */       AddressingDispositionHelper.write(localCDROutputObject, s);
/*      */   }
/*      */ 
/*      */   private CDROutputObject createAppropriateOutputObject(CorbaMessageMediator paramCorbaMessageMediator, Message paramMessage, LocateReplyMessage paramLocateReplyMessage)
/*      */   {
/*      */     CDROutputObject localCDROutputObject;
/* 1795 */     if (paramMessage.getGIOPVersion().lessThan(GIOPVersion.V1_2))
/*      */     {
/* 1797 */       localCDROutputObject = OutputStreamFactory.newCDROutputObject((ORB)paramCorbaMessageMediator.getBroker(), this, GIOPVersion.V1_0, (CorbaConnection)paramCorbaMessageMediator.getConnection(), paramLocateReplyMessage, (byte)1);
/*      */     }
/*      */     else
/*      */     {
/* 1806 */       localCDROutputObject = OutputStreamFactory.newCDROutputObject((ORB)paramCorbaMessageMediator.getBroker(), paramCorbaMessageMediator, paramLocateReplyMessage, (byte)1);
/*      */     }
/*      */ 
/* 1812 */     return localCDROutputObject;
/*      */   }
/*      */ 
/*      */   public void handleThrowableDuringServerDispatch(CorbaMessageMediator paramCorbaMessageMediator, Throwable paramThrowable, CompletionStatus paramCompletionStatus)
/*      */   {
/* 1820 */     if (((ORB)paramCorbaMessageMediator.getBroker()).subcontractDebugFlag) {
/* 1821 */       dprint(".handleThrowableDuringServerDispatch: " + opAndId(paramCorbaMessageMediator) + ": " + paramThrowable);
/*      */     }
/*      */ 
/* 1839 */     handleThrowableDuringServerDispatch(paramCorbaMessageMediator, paramThrowable, paramCompletionStatus, 1);
/*      */   }
/*      */ 
/*      */   protected void handleThrowableDuringServerDispatch(CorbaMessageMediator paramCorbaMessageMediator, Throwable paramThrowable, CompletionStatus paramCompletionStatus, int paramInt)
/*      */   {
/*      */     Object localObject;
/* 1854 */     if (paramInt > 10) {
/* 1855 */       if (((ORB)paramCorbaMessageMediator.getBroker()).subcontractDebugFlag) {
/* 1856 */         dprint(".handleThrowableDuringServerDispatch: " + opAndId(paramCorbaMessageMediator) + ": cannot handle: " + paramThrowable);
/*      */       }
/*      */ 
/* 1863 */       localObject = new RuntimeException("handleThrowableDuringServerDispatch: cannot create response.");
/*      */ 
/* 1866 */       ((RuntimeException)localObject).initCause(paramThrowable);
/* 1867 */       throw ((Throwable)localObject);
/*      */     }
/*      */     try
/*      */     {
/* 1871 */       if ((paramThrowable instanceof ForwardException)) {
/* 1872 */         localObject = (ForwardException)paramThrowable;
/* 1873 */         createLocationForward(paramCorbaMessageMediator, ((ForwardException)localObject).getIOR(), null);
/* 1874 */         return;
/*      */       }
/*      */ 
/* 1877 */       if ((paramThrowable instanceof AddressingDispositionException)) {
/* 1878 */         handleAddressingDisposition(paramCorbaMessageMediator, (AddressingDispositionException)paramThrowable);
/*      */ 
/* 1881 */         return;
/*      */       }
/*      */ 
/* 1886 */       localObject = convertThrowableToSystemException(paramThrowable, paramCompletionStatus);
/*      */ 
/* 1889 */       createSystemExceptionResponse(paramCorbaMessageMediator, (SystemException)localObject, null);
/* 1890 */       return;
/*      */     }
/*      */     catch (Throwable localThrowable)
/*      */     {
/* 1898 */       handleThrowableDuringServerDispatch(paramCorbaMessageMediator, localThrowable, paramCompletionStatus, paramInt + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected SystemException convertThrowableToSystemException(Throwable paramThrowable, CompletionStatus paramCompletionStatus)
/*      */   {
/* 1910 */     if ((paramThrowable instanceof SystemException)) {
/* 1911 */       return (SystemException)paramThrowable;
/*      */     }
/*      */ 
/* 1914 */     if ((paramThrowable instanceof RequestCanceledException))
/*      */     {
/* 1920 */       return this.wrapper.requestCanceled(paramThrowable);
/*      */     }
/*      */ 
/* 1936 */     return this.wrapper.runtimeexception(CompletionStatus.COMPLETED_MAYBE, paramThrowable);
/*      */   }
/*      */ 
/*      */   protected void handleAddressingDisposition(CorbaMessageMediator paramCorbaMessageMediator, AddressingDispositionException paramAddressingDispositionException)
/*      */   {
/* 1944 */     short s = -1;
/*      */     CDROutputObject localCDROutputObject;
/* 1950 */     switch (paramCorbaMessageMediator.getRequestHeader().getType()) {
/*      */     case 0:
/* 1952 */       ReplyMessage localReplyMessage = MessageBase.createReply((ORB)paramCorbaMessageMediator.getBroker(), paramCorbaMessageMediator.getGIOPVersion(), paramCorbaMessageMediator.getEncodingVersion(), paramCorbaMessageMediator.getRequestId(), 5, null, null);
/*      */ 
/* 1960 */       localCDROutputObject = OutputStreamFactory.newCDROutputObject((ORB)paramCorbaMessageMediator.getBroker(), this, paramCorbaMessageMediator.getGIOPVersion(), (CorbaConnection)paramCorbaMessageMediator.getConnection(), localReplyMessage, (byte)1);
/*      */ 
/* 1968 */       paramCorbaMessageMediator.setOutputObject(localCDROutputObject);
/* 1969 */       localCDROutputObject.setMessageMediator(paramCorbaMessageMediator);
/* 1970 */       localReplyMessage.write(localCDROutputObject);
/* 1971 */       AddressingDispositionHelper.write(localCDROutputObject, paramAddressingDispositionException.expectedAddrDisp());
/*      */ 
/* 1973 */       return;
/*      */     case 3:
/* 1976 */       LocateReplyMessage localLocateReplyMessage = MessageBase.createLocateReply((ORB)paramCorbaMessageMediator.getBroker(), paramCorbaMessageMediator.getGIOPVersion(), paramCorbaMessageMediator.getEncodingVersion(), paramCorbaMessageMediator.getRequestId(), 5, null);
/*      */ 
/* 1984 */       s = paramAddressingDispositionException.expectedAddrDisp();
/*      */ 
/* 1987 */       localCDROutputObject = createAppropriateOutputObject(paramCorbaMessageMediator, paramCorbaMessageMediator.getRequestHeader(), localLocateReplyMessage);
/*      */ 
/* 1991 */       paramCorbaMessageMediator.setOutputObject(localCDROutputObject);
/* 1992 */       localCDROutputObject.setMessageMediator(paramCorbaMessageMediator);
/* 1993 */       localLocateReplyMessage.write(localCDROutputObject);
/* 1994 */       Object localObject = null;
/* 1995 */       if (localObject != null) {
/* 1996 */         localObject.write(localCDROutputObject);
/*      */       }
/* 1998 */       if (s != -1) {
/* 1999 */         AddressingDispositionHelper.write(localCDROutputObject, s);
/*      */       }
/* 2001 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediator createResponse(CorbaMessageMediator paramCorbaMessageMediator, ServiceContexts paramServiceContexts)
/*      */   {
/* 2013 */     return createResponseHelper(paramCorbaMessageMediator, getServiceContextsForReply(paramCorbaMessageMediator, null));
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediator createUserExceptionResponse(CorbaMessageMediator paramCorbaMessageMediator, ServiceContexts paramServiceContexts)
/*      */   {
/* 2022 */     return createResponseHelper(paramCorbaMessageMediator, getServiceContextsForReply(paramCorbaMessageMediator, null), true);
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediator createUnknownExceptionResponse(CorbaMessageMediator paramCorbaMessageMediator, UnknownException paramUnknownException)
/*      */   {
/* 2033 */     ServiceContexts localServiceContexts = null;
/* 2034 */     UNKNOWN localUNKNOWN = new UNKNOWN(0, CompletionStatus.COMPLETED_MAYBE);
/*      */ 
/* 2036 */     localServiceContexts = new ServiceContexts((ORB)paramCorbaMessageMediator.getBroker());
/* 2037 */     UEInfoServiceContext localUEInfoServiceContext = new UEInfoServiceContext(localUNKNOWN);
/* 2038 */     localServiceContexts.put(localUEInfoServiceContext);
/* 2039 */     return createSystemExceptionResponse(paramCorbaMessageMediator, localUNKNOWN, localServiceContexts);
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediator createSystemExceptionResponse(CorbaMessageMediator paramCorbaMessageMediator, SystemException paramSystemException, ServiceContexts paramServiceContexts)
/*      */   {
/* 2047 */     if (paramCorbaMessageMediator.getConnection() != null)
/*      */     {
/* 2057 */       localObject1 = (CorbaMessageMediatorImpl)((CorbaConnection)paramCorbaMessageMediator.getConnection()).serverRequestMapGet(paramCorbaMessageMediator.getRequestId());
/*      */ 
/* 2061 */       localObject2 = null;
/* 2062 */       if (localObject1 != null) {
/* 2063 */         localObject2 = ((CorbaMessageMediatorImpl)localObject1).getOutputObject();
/*      */       }
/*      */ 
/* 2068 */       if ((localObject2 != null) && (((CorbaMessageMediatorImpl)localObject1).sentFragment()) && (!((CorbaMessageMediatorImpl)localObject1).sentFullMessage()))
/*      */       {
/* 2072 */         return localObject1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2079 */     if (paramCorbaMessageMediator.executePIInResponseConstructor())
/*      */     {
/* 2086 */       ((ORB)paramCorbaMessageMediator.getBroker()).getPIHandler().setServerPIInfo(paramSystemException);
/*      */     }
/*      */ 
/* 2089 */     if ((((ORB)paramCorbaMessageMediator.getBroker()).subcontractDebugFlag) && (paramSystemException != null))
/*      */     {
/* 2092 */       dprint(".createSystemExceptionResponse: " + opAndId(paramCorbaMessageMediator), paramSystemException);
/*      */     }
/*      */ 
/* 2097 */     Object localObject1 = getServiceContextsForReply(paramCorbaMessageMediator, paramServiceContexts);
/*      */ 
/* 2104 */     addExceptionDetailMessage(paramCorbaMessageMediator, paramSystemException, (ServiceContexts)localObject1);
/*      */ 
/* 2106 */     Object localObject2 = createResponseHelper(paramCorbaMessageMediator, (ServiceContexts)localObject1, false);
/*      */ 
/* 2113 */     ORBUtility.writeSystemException(paramSystemException, (org.omg.CORBA_2_3.portable.OutputStream)((MessageHandler)localObject2).getOutputObject());
/*      */ 
/* 2116 */     return localObject2;
/*      */   }
/*      */ 
/*      */   private void addExceptionDetailMessage(CorbaMessageMediator paramCorbaMessageMediator, SystemException paramSystemException, ServiceContexts paramServiceContexts)
/*      */   {
/* 2123 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 2124 */     PrintWriter localPrintWriter = new PrintWriter(localByteArrayOutputStream);
/* 2125 */     paramSystemException.printStackTrace(localPrintWriter);
/* 2126 */     localPrintWriter.flush();
/* 2127 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB)paramCorbaMessageMediator.getBroker());
/*      */ 
/* 2129 */     localEncapsOutputStream.putEndian();
/* 2130 */     localEncapsOutputStream.write_wstring(localByteArrayOutputStream.toString());
/* 2131 */     UnknownServiceContext localUnknownServiceContext = new UnknownServiceContext(14, localEncapsOutputStream.toByteArray());
/*      */ 
/* 2134 */     paramServiceContexts.put(localUnknownServiceContext);
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediator createLocationForward(CorbaMessageMediator paramCorbaMessageMediator, IOR paramIOR, ServiceContexts paramServiceContexts)
/*      */   {
/* 2140 */     ReplyMessage localReplyMessage = MessageBase.createReply((ORB)paramCorbaMessageMediator.getBroker(), paramCorbaMessageMediator.getGIOPVersion(), paramCorbaMessageMediator.getEncodingVersion(), paramCorbaMessageMediator.getRequestId(), 3, getServiceContextsForReply(paramCorbaMessageMediator, paramServiceContexts), paramIOR);
/*      */ 
/* 2150 */     return createResponseHelper(paramCorbaMessageMediator, localReplyMessage, paramIOR);
/*      */   }
/*      */ 
/*      */   protected CorbaMessageMediator createResponseHelper(CorbaMessageMediator paramCorbaMessageMediator, ServiceContexts paramServiceContexts)
/*      */   {
/* 2156 */     ReplyMessage localReplyMessage = MessageBase.createReply((ORB)paramCorbaMessageMediator.getBroker(), paramCorbaMessageMediator.getGIOPVersion(), paramCorbaMessageMediator.getEncodingVersion(), paramCorbaMessageMediator.getRequestId(), 0, paramServiceContexts, null);
/*      */ 
/* 2165 */     return createResponseHelper(paramCorbaMessageMediator, localReplyMessage, null);
/*      */   }
/*      */ 
/*      */   protected CorbaMessageMediator createResponseHelper(CorbaMessageMediator paramCorbaMessageMediator, ServiceContexts paramServiceContexts, boolean paramBoolean)
/*      */   {
/* 2171 */     ReplyMessage localReplyMessage = MessageBase.createReply((ORB)paramCorbaMessageMediator.getBroker(), paramCorbaMessageMediator.getGIOPVersion(), paramCorbaMessageMediator.getEncodingVersion(), paramCorbaMessageMediator.getRequestId(), paramBoolean ? 1 : 2, paramServiceContexts, null);
/*      */ 
/* 2181 */     return createResponseHelper(paramCorbaMessageMediator, localReplyMessage, null);
/*      */   }
/*      */ 
/*      */   protected CorbaMessageMediator createResponseHelper(CorbaMessageMediator paramCorbaMessageMediator, ReplyMessage paramReplyMessage, IOR paramIOR)
/*      */   {
/* 2189 */     runServantPostInvoke(paramCorbaMessageMediator);
/* 2190 */     runInterceptors(paramCorbaMessageMediator, paramReplyMessage);
/* 2191 */     runRemoveThreadInfo(paramCorbaMessageMediator);
/*      */ 
/* 2193 */     if (((ORB)paramCorbaMessageMediator.getBroker()).subcontractDebugFlag) {
/* 2194 */       dprint(".createResponseHelper: " + opAndId(paramCorbaMessageMediator) + ": " + paramReplyMessage);
/*      */     }
/*      */ 
/* 2199 */     paramCorbaMessageMediator.setReplyHeader(paramReplyMessage);
/*      */     Object localObject;
/* 2204 */     if (paramCorbaMessageMediator.getConnection() == null) {
/* 2205 */       localObject = OutputStreamFactory.newCDROutputObject(this.orb, paramCorbaMessageMediator, paramCorbaMessageMediator.getReplyHeader(), paramCorbaMessageMediator.getStreamFormatVersion(), 0);
/*      */     }
/*      */     else
/*      */     {
/* 2211 */       localObject = paramCorbaMessageMediator.getConnection().getAcceptor().createOutputObject(paramCorbaMessageMediator.getBroker(), paramCorbaMessageMediator);
/*      */     }
/*      */ 
/* 2214 */     paramCorbaMessageMediator.setOutputObject((OutputObject)localObject);
/* 2215 */     paramCorbaMessageMediator.getOutputObject().setMessageMediator(paramCorbaMessageMediator);
/*      */ 
/* 2217 */     paramReplyMessage.write((org.omg.CORBA_2_3.portable.OutputStream)paramCorbaMessageMediator.getOutputObject());
/* 2218 */     if (paramReplyMessage.getIOR() != null) {
/* 2219 */       paramReplyMessage.getIOR().write((org.omg.CORBA_2_3.portable.OutputStream)paramCorbaMessageMediator.getOutputObject());
/*      */     }
/*      */ 
/* 2226 */     return paramCorbaMessageMediator;
/*      */   }
/*      */ 
/*      */   protected void runServantPostInvoke(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 2236 */     ORB localORB = null;
/*      */ 
/* 2239 */     if (paramCorbaMessageMediator.executeReturnServantInResponseConstructor())
/*      */     {
/* 2245 */       paramCorbaMessageMediator.setExecuteReturnServantInResponseConstructor(false);
/* 2246 */       paramCorbaMessageMediator.setExecuteRemoveThreadInfoInResponseConstructor(true);
/*      */       try
/*      */       {
/* 2249 */         localORB = (ORB)paramCorbaMessageMediator.getBroker();
/* 2250 */         OAInvocationInfo localOAInvocationInfo = localORB.peekInvocationInfo();
/* 2251 */         ObjectAdapter localObjectAdapter = localOAInvocationInfo.oa();
/*      */         try {
/* 2253 */           localObjectAdapter.returnServant();
/*      */         } catch (Throwable localThrowable) {
/* 2255 */           this.wrapper.unexpectedException(localThrowable);
/*      */ 
/* 2257 */           if ((localThrowable instanceof Error))
/* 2258 */             throw ((Error)localThrowable);
/* 2259 */           if ((localThrowable instanceof RuntimeException))
/* 2260 */             throw ((RuntimeException)localThrowable);
/*      */         } finally {
/* 2262 */           localObjectAdapter.exit();
/*      */         }
/*      */       } catch (EmptyStackException localEmptyStackException) {
/* 2265 */         throw this.wrapper.emptyStackRunServantPostInvoke(localEmptyStackException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void runInterceptors(CorbaMessageMediator paramCorbaMessageMediator, ReplyMessage paramReplyMessage)
/*      */   {
/* 2273 */     if (paramCorbaMessageMediator.executePIInResponseConstructor())
/*      */     {
/* 2277 */       ((ORB)paramCorbaMessageMediator.getBroker()).getPIHandler().invokeServerPIEndingPoint(paramReplyMessage);
/*      */ 
/* 2283 */       ((ORB)paramCorbaMessageMediator.getBroker()).getPIHandler().cleanupServerPIRequest();
/*      */ 
/* 2287 */       paramCorbaMessageMediator.setExecutePIInResponseConstructor(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void runRemoveThreadInfo(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 2295 */     if (paramCorbaMessageMediator.executeRemoveThreadInfoInResponseConstructor()) {
/* 2296 */       paramCorbaMessageMediator.setExecuteRemoveThreadInfoInResponseConstructor(false);
/* 2297 */       ((ORB)paramCorbaMessageMediator.getBroker()).popInvocationInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ServiceContexts getServiceContextsForReply(CorbaMessageMediator paramCorbaMessageMediator, ServiceContexts paramServiceContexts)
/*      */   {
/* 2304 */     CorbaConnection localCorbaConnection = (CorbaConnection)paramCorbaMessageMediator.getConnection();
/*      */ 
/* 2306 */     if (((ORB)paramCorbaMessageMediator.getBroker()).subcontractDebugFlag) {
/* 2307 */       dprint(".getServiceContextsForReply: " + opAndId(paramCorbaMessageMediator) + ": " + localCorbaConnection);
/*      */     }
/*      */ 
/* 2312 */     if (paramServiceContexts == null) {
/* 2313 */       paramServiceContexts = new ServiceContexts((ORB)paramCorbaMessageMediator.getBroker());
/*      */     }
/*      */ 
/* 2318 */     if ((localCorbaConnection != null) && (!localCorbaConnection.isPostInitialContexts())) {
/* 2319 */       localCorbaConnection.setPostInitialContexts();
/* 2320 */       localObject = new SendingContextServiceContext(((ORB)paramCorbaMessageMediator.getBroker()).getFVDCodeBaseIOR());
/*      */ 
/* 2324 */       if (paramServiceContexts.get(((SendingContextServiceContext)localObject).getId()) != null) {
/* 2325 */         throw this.wrapper.duplicateSendingContextServiceContext();
/*      */       }
/* 2327 */       paramServiceContexts.put((ServiceContext)localObject);
/*      */ 
/* 2329 */       if (((ORB)paramCorbaMessageMediator.getBroker()).subcontractDebugFlag) {
/* 2330 */         dprint(".getServiceContextsForReply: " + opAndId(paramCorbaMessageMediator) + ": added SendingContextServiceContext");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2337 */     Object localObject = new ORBVersionServiceContext(ORBVersionFactory.getORBVersion());
/*      */ 
/* 2340 */     if (paramServiceContexts.get(((ORBVersionServiceContext)localObject).getId()) != null) {
/* 2341 */       throw this.wrapper.duplicateOrbVersionServiceContext();
/*      */     }
/* 2343 */     paramServiceContexts.put((ServiceContext)localObject);
/*      */ 
/* 2345 */     if (((ORB)paramCorbaMessageMediator.getBroker()).subcontractDebugFlag) {
/* 2346 */       dprint(".getServiceContextsForReply: " + opAndId(paramCorbaMessageMediator) + ": added ORB version service context");
/*      */     }
/*      */ 
/* 2350 */     return paramServiceContexts;
/*      */   }
/*      */ 
/*      */   private void releaseByteBufferToPool()
/*      */   {
/* 2357 */     if (this.dispatchByteBuffer != null) {
/* 2358 */       this.orb.getByteBufferPool().releaseByteBuffer(this.dispatchByteBuffer);
/* 2359 */       if (transportDebug()) {
/* 2360 */         int i = System.identityHashCode(this.dispatchByteBuffer);
/* 2361 */         StringBuffer localStringBuffer = new StringBuffer();
/* 2362 */         localStringBuffer.append(".handleInput: releasing ByteBuffer (" + i + ") to ByteBufferPool");
/*      */ 
/* 2364 */         dprint(localStringBuffer.toString());
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.CorbaMessageMediatorImpl
 * JD-Core Version:    0.6.2
 */