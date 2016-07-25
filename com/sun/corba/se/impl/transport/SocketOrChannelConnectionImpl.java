/*      */ package com.sun.corba.se.impl.transport;
/*      */ 
/*      */ import com.sun.corba.se.impl.encoding.CDROutputObject;
/*      */ import com.sun.corba.se.impl.encoding.CachedCodeBase;
/*      */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo.CodeSetContext;
/*      */ import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.CancelRequestMessage;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*      */ import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
/*      */ import com.sun.corba.se.pept.encoding.InputObject;
/*      */ import com.sun.corba.se.pept.encoding.OutputObject;
/*      */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*      */ import com.sun.corba.se.pept.transport.Acceptor;
/*      */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*      */ import com.sun.corba.se.pept.transport.Connection;
/*      */ import com.sun.corba.se.pept.transport.ConnectionCache;
/*      */ import com.sun.corba.se.pept.transport.ContactInfo;
/*      */ import com.sun.corba.se.pept.transport.EventHandler;
/*      */ import com.sun.corba.se.pept.transport.InboundConnectionCache;
/*      */ import com.sun.corba.se.pept.transport.OutboundConnectionCache;
/*      */ import com.sun.corba.se.pept.transport.ResponseWaitingRoom;
/*      */ import com.sun.corba.se.pept.transport.Selector;
/*      */ import com.sun.corba.se.pept.transport.TransportManager;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import com.sun.corba.se.spi.orb.ORBData;
/*      */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
/*      */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
/*      */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
/*      */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
/*      */ import com.sun.corba.se.spi.orbutil.threadpool.Work;
/*      */ import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
/*      */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*      */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*      */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*      */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*      */ import com.sun.corba.se.spi.transport.CorbaResponseWaitingRoom;
/*      */ import com.sun.corba.se.spi.transport.ORBSocketFactory;
/*      */ import com.sun.corba.se.spi.transport.ReadTimeouts;
/*      */ import com.sun.org.omg.SendingContext.CodeBase;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.Socket;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.SelectableChannel;
/*      */ import java.nio.channels.SelectionKey;
/*      */ import java.nio.channels.SocketChannel;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.omg.CORBA.COMM_FAILURE;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.INTERNAL;
/*      */ import org.omg.CORBA.SystemException;
/*      */ import sun.corba.OutputStreamFactory;
/*      */ 
/*      */ public class SocketOrChannelConnectionImpl extends EventHandlerBase
/*      */   implements CorbaConnection, Work
/*      */ {
/*  102 */   public static boolean dprintWriteLocks = false;
/*      */   protected long enqueueTime;
/*      */   protected SocketChannel socketChannel;
/*      */   protected CorbaContactInfo contactInfo;
/*      */   protected Acceptor acceptor;
/*      */   protected ConnectionCache connectionCache;
/*      */   protected Socket socket;
/*  127 */   protected long timeStamp = 0L;
/*  128 */   protected boolean isServer = false;
/*      */ 
/*  132 */   protected int requestId = 5;
/*      */   protected CorbaResponseWaitingRoom responseWaitingRoom;
/*      */   protected int state;
/*  135 */   protected Object stateEvent = new Object();
/*  136 */   protected Object writeEvent = new Object();
/*      */   protected boolean writeLocked;
/*  138 */   protected int serverRequestCount = 0;
/*      */ 
/*  142 */   Map serverRequestMap = null;
/*      */ 
/*  147 */   protected boolean postInitialContexts = false;
/*      */   protected IOR codeBaseServerIOR;
/*  156 */   protected CachedCodeBase cachedCodeBase = new CachedCodeBase(this);
/*      */   protected ORBUtilSystemException wrapper;
/*      */   protected ReadTimeouts readTimeouts;
/*      */   protected boolean shouldReadGiopHeaderOnly;
/*  168 */   protected CorbaMessageMediator partialMessageMediator = null;
/*      */ 
/* 1331 */   protected CodeSetComponentInfo.CodeSetContext codeSetContext = null;
/*      */   protected MessageMediator clientReply_1_1;
/*      */   protected MessageMediator serverRequest_1_1;
/*      */ 
/*      */   public SocketChannel getSocketChannel()
/*      */   {
/*  113 */     return this.socketChannel;
/*      */   }
/*      */ 
/*      */   protected SocketOrChannelConnectionImpl(ORB paramORB)
/*      */   {
/*  173 */     this.orb = paramORB;
/*  174 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.transport");
/*      */ 
/*  177 */     setWork(this);
/*  178 */     this.responseWaitingRoom = new CorbaResponseWaitingRoomImpl(paramORB, this);
/*  179 */     setReadTimeouts(paramORB.getORBData().getTransportTCPReadTimeouts());
/*      */   }
/*      */ 
/*      */   protected SocketOrChannelConnectionImpl(ORB paramORB, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  187 */     this(paramORB);
/*  188 */     setUseSelectThreadToWait(paramBoolean1);
/*  189 */     setUseWorkerThreadForEvent(paramBoolean2);
/*      */   }
/*      */ 
/*      */   public SocketOrChannelConnectionImpl(ORB paramORB, CorbaContactInfo paramCorbaContactInfo, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String paramString2, int paramInt)
/*      */   {
/*  201 */     this(paramORB, paramBoolean1, paramBoolean2);
/*      */ 
/*  203 */     this.contactInfo = paramCorbaContactInfo;
/*      */     try
/*      */     {
/*  206 */       this.socket = paramORB.getORBData().getSocketFactory().createSocket(paramString1, new InetSocketAddress(paramString2, paramInt));
/*      */ 
/*  209 */       this.socketChannel = this.socket.getChannel();
/*      */ 
/*  211 */       if (this.socketChannel != null) {
/*  212 */         boolean bool = !paramBoolean1;
/*  213 */         this.socketChannel.configureBlocking(bool);
/*      */       }
/*      */       else
/*      */       {
/*  217 */         setUseSelectThreadToWait(false);
/*      */       }
/*  219 */       if (paramORB.transportDebugFlag)
/*  220 */         dprint(".initialize: connection created: " + this.socket);
/*      */     }
/*      */     catch (Throwable localThrowable) {
/*  223 */       throw this.wrapper.connectFailure(localThrowable, paramString1, paramString2, Integer.toString(paramInt));
/*      */     }
/*      */ 
/*  226 */     this.state = 1;
/*      */   }
/*      */ 
/*      */   public SocketOrChannelConnectionImpl(ORB paramORB, CorbaContactInfo paramCorbaContactInfo, String paramString1, String paramString2, int paramInt)
/*      */   {
/*  236 */     this(paramORB, paramCorbaContactInfo, paramORB.getORBData().connectionSocketUseSelectThreadToWait(), paramORB.getORBData().connectionSocketUseWorkerThreadForEvent(), paramString1, paramString2, paramInt);
/*      */   }
/*      */ 
/*      */   public SocketOrChannelConnectionImpl(ORB paramORB, Acceptor paramAcceptor, Socket paramSocket, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  249 */     this(paramORB, paramBoolean1, paramBoolean2);
/*      */ 
/*  251 */     this.socket = paramSocket;
/*  252 */     this.socketChannel = paramSocket.getChannel();
/*  253 */     if (this.socketChannel != null) {
/*      */       try
/*      */       {
/*  256 */         boolean bool = !paramBoolean1;
/*  257 */         this.socketChannel.configureBlocking(bool);
/*      */       } catch (IOException localIOException) {
/*  259 */         RuntimeException localRuntimeException = new RuntimeException();
/*  260 */         localRuntimeException.initCause(localIOException);
/*  261 */         throw localRuntimeException;
/*      */       }
/*      */     }
/*  264 */     this.acceptor = paramAcceptor;
/*      */ 
/*  266 */     this.serverRequestMap = Collections.synchronizedMap(new HashMap());
/*  267 */     this.isServer = true;
/*      */ 
/*  269 */     this.state = 2;
/*      */   }
/*      */ 
/*      */   public SocketOrChannelConnectionImpl(ORB paramORB, Acceptor paramAcceptor, Socket paramSocket)
/*      */   {
/*  277 */     this(paramORB, paramAcceptor, paramSocket, paramSocket.getChannel() == null ? false : paramORB.getORBData().connectionSocketUseSelectThreadToWait(), paramSocket.getChannel() == null ? false : paramORB.getORBData().connectionSocketUseWorkerThreadForEvent());
/*      */   }
/*      */ 
/*      */   public boolean shouldRegisterReadEvent()
/*      */   {
/*  293 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean shouldRegisterServerReadEvent()
/*      */   {
/*  298 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean read()
/*      */   {
/*      */     try {
/*  304 */       if (this.orb.transportDebugFlag) {
/*  305 */         dprint(".read->: " + this);
/*      */       }
/*  307 */       CorbaMessageMediator localCorbaMessageMediator = readBits();
/*      */       boolean bool;
/*  308 */       if (localCorbaMessageMediator != null)
/*      */       {
/*  311 */         return dispatch(localCorbaMessageMediator);
/*      */       }
/*  313 */       return true;
/*      */     } finally {
/*  315 */       if (this.orb.transportDebugFlag)
/*  316 */         dprint(".read<-: " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected CorbaMessageMediator readBits()
/*      */   {
/*      */     try
/*      */     {
/*  325 */       if (this.orb.transportDebugFlag)
/*  326 */         dprint(".readBits->: " + this);
/*      */       MessageMediator localMessageMediator;
/*  331 */       if (this.contactInfo != null) {
/*  332 */         localMessageMediator = this.contactInfo.createMessageMediator(this.orb, this);
/*      */       }
/*  334 */       else if (this.acceptor != null)
/*  335 */         localMessageMediator = this.acceptor.createMessageMediator(this.orb, this);
/*      */       else {
/*  337 */         throw new RuntimeException("SocketOrChannelConnectionImpl.readBits");
/*      */       }
/*      */ 
/*  340 */       return (CorbaMessageMediator)localMessageMediator;
/*      */     }
/*      */     catch (ThreadDeath localThreadDeath) {
/*  343 */       if (this.orb.transportDebugFlag)
/*  344 */         dprint(".readBits: " + this + ": ThreadDeath: " + localThreadDeath, localThreadDeath);
/*      */       try
/*      */       {
/*  347 */         purgeCalls(this.wrapper.connectionAbort(localThreadDeath), false, false);
/*      */       } catch (Throwable localThrowable2) {
/*  349 */         if (this.orb.transportDebugFlag) {
/*  350 */           dprint(".readBits: " + this + ": purgeCalls: Throwable: " + localThrowable2, localThrowable2);
/*      */         }
/*      */       }
/*  353 */       throw localThreadDeath;
/*      */     } catch (Throwable localThrowable1) {
/*  355 */       if (this.orb.transportDebugFlag) {
/*  356 */         dprint(".readBits: " + this + ": Throwable: " + localThrowable1, localThrowable1);
/*      */       }
/*      */       try
/*      */       {
/*  360 */         if ((localThrowable1 instanceof INTERNAL))
/*  361 */           sendMessageError(GIOPVersion.DEFAULT_VERSION);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  364 */         if (this.orb.transportDebugFlag) {
/*  365 */           dprint(".readBits: " + this + ": sendMessageError: IOException: " + localIOException, localIOException);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  370 */       this.orb.getTransportManager().getSelector(0).unregisterForEvent(this);
/*      */ 
/*  372 */       purgeCalls(this.wrapper.connectionAbort(localThrowable1), true, false);
/*      */     }
/*      */     finally
/*      */     {
/*  381 */       if (this.orb.transportDebugFlag) {
/*  382 */         dprint(".readBits<-: " + this);
/*      */       }
/*      */     }
/*  385 */     return null;
/*      */   }
/*      */ 
/*      */   protected CorbaMessageMediator finishReadingBits(MessageMediator paramMessageMediator)
/*      */   {
/*      */     try
/*      */     {
/*  392 */       if (this.orb.transportDebugFlag) {
/*  393 */         dprint(".finishReadingBits->: " + this);
/*      */       }
/*      */ 
/*  397 */       if (this.contactInfo != null) {
/*  398 */         paramMessageMediator = this.contactInfo.finishCreatingMessageMediator(this.orb, this, paramMessageMediator);
/*      */       }
/*  400 */       else if (this.acceptor != null) {
/*  401 */         paramMessageMediator = this.acceptor.finishCreatingMessageMediator(this.orb, this, paramMessageMediator);
/*      */       }
/*      */       else {
/*  404 */         throw new RuntimeException("SocketOrChannelConnectionImpl.finishReadingBits");
/*      */       }
/*      */ 
/*  407 */       return (CorbaMessageMediator)paramMessageMediator;
/*      */     }
/*      */     catch (ThreadDeath localThreadDeath) {
/*  410 */       if (this.orb.transportDebugFlag)
/*  411 */         dprint(".finishReadingBits: " + this + ": ThreadDeath: " + localThreadDeath, localThreadDeath);
/*      */       try
/*      */       {
/*  414 */         purgeCalls(this.wrapper.connectionAbort(localThreadDeath), false, false);
/*      */       } catch (Throwable localThrowable2) {
/*  416 */         if (this.orb.transportDebugFlag) {
/*  417 */           dprint(".finishReadingBits: " + this + ": purgeCalls: Throwable: " + localThrowable2, localThrowable2);
/*      */         }
/*      */       }
/*  420 */       throw localThreadDeath;
/*      */     } catch (Throwable localThrowable1) {
/*  422 */       if (this.orb.transportDebugFlag) {
/*  423 */         dprint(".finishReadingBits: " + this + ": Throwable: " + localThrowable1, localThrowable1);
/*      */       }
/*      */       try
/*      */       {
/*  427 */         if ((localThrowable1 instanceof INTERNAL))
/*  428 */           sendMessageError(GIOPVersion.DEFAULT_VERSION);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  431 */         if (this.orb.transportDebugFlag) {
/*  432 */           dprint(".finishReadingBits: " + this + ": sendMessageError: IOException: " + localIOException, localIOException);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  437 */       this.orb.getTransportManager().getSelector(0).unregisterForEvent(this);
/*      */ 
/*  439 */       purgeCalls(this.wrapper.connectionAbort(localThrowable1), true, false);
/*      */     }
/*      */     finally
/*      */     {
/*  448 */       if (this.orb.transportDebugFlag) {
/*  449 */         dprint(".finishReadingBits<-: " + this);
/*      */       }
/*      */     }
/*  452 */     return null;
/*      */   }
/*      */ 
/*      */   protected boolean dispatch(CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/*      */     try {
/*  458 */       if (this.orb.transportDebugFlag) {
/*  459 */         dprint(".dispatch->: " + this);
/*      */       }
/*      */ 
/*  469 */       boolean bool1 = paramCorbaMessageMediator.getProtocolHandler().handleRequest(paramCorbaMessageMediator);
/*      */ 
/*  473 */       return bool1;
/*      */     }
/*      */     catch (ThreadDeath localThreadDeath) {
/*  476 */       if (this.orb.transportDebugFlag)
/*  477 */         dprint(".dispatch: ThreadDeath", localThreadDeath);
/*      */       try
/*      */       {
/*  480 */         purgeCalls(this.wrapper.connectionAbort(localThreadDeath), false, false);
/*      */       } catch (Throwable localThrowable2) {
/*  482 */         if (this.orb.transportDebugFlag) {
/*  483 */           dprint(".dispatch: purgeCalls: Throwable", localThrowable2);
/*      */         }
/*      */       }
/*  486 */       throw localThreadDeath;
/*      */     } catch (Throwable localThrowable1) {
/*  488 */       if (this.orb.transportDebugFlag) {
/*  489 */         dprint(".dispatch: Throwable", localThrowable1);
/*      */       }
/*      */       try
/*      */       {
/*  493 */         if ((localThrowable1 instanceof INTERNAL))
/*  494 */           sendMessageError(GIOPVersion.DEFAULT_VERSION);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  497 */         if (this.orb.transportDebugFlag) {
/*  498 */           dprint(".dispatch: sendMessageError: IOException", localIOException);
/*      */         }
/*      */       }
/*  501 */       purgeCalls(this.wrapper.connectionAbort(localThrowable1), false, false);
/*      */     }
/*      */     finally
/*      */     {
/*  505 */       if (this.orb.transportDebugFlag) {
/*  506 */         dprint(".dispatch<-: " + this);
/*      */       }
/*      */     }
/*      */ 
/*  510 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean shouldUseDirectByteBuffers()
/*      */   {
/*  515 */     return getSocketChannel() != null;
/*      */   }
/*      */ 
/*      */   public ByteBuffer read(int paramInt1, int paramInt2, int paramInt3, long paramLong)
/*      */     throws IOException
/*      */   {
/*  521 */     if (shouldUseDirectByteBuffers())
/*      */     {
/*  523 */       localObject = this.orb.getByteBufferPool().getByteBuffer(paramInt1);
/*      */ 
/*  526 */       if (this.orb.transportDebugFlag)
/*      */       {
/*  528 */         int i = System.identityHashCode(localObject);
/*  529 */         StringBuffer localStringBuffer = new StringBuffer(80);
/*  530 */         localStringBuffer.append(".read: got ByteBuffer id (");
/*  531 */         localStringBuffer.append(i).append(") from ByteBufferPool.");
/*  532 */         String str = localStringBuffer.toString();
/*  533 */         dprint(str);
/*      */       }
/*      */ 
/*  536 */       ((ByteBuffer)localObject).position(paramInt2);
/*  537 */       ((ByteBuffer)localObject).limit(paramInt1);
/*      */ 
/*  539 */       readFully((ByteBuffer)localObject, paramInt3, paramLong);
/*      */ 
/*  541 */       return localObject;
/*      */     }
/*      */ 
/*  544 */     Object localObject = new byte[paramInt1];
/*  545 */     readFully(getSocket().getInputStream(), (byte[])localObject, paramInt2, paramInt3, paramLong);
/*      */ 
/*  547 */     ByteBuffer localByteBuffer = ByteBuffer.wrap((byte[])localObject);
/*  548 */     localByteBuffer.limit(paramInt1);
/*  549 */     return localByteBuffer;
/*      */   }
/*      */ 
/*      */   public ByteBuffer read(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, long paramLong)
/*      */     throws IOException
/*      */   {
/*  556 */     int i = paramInt1 + paramInt2;
/*  557 */     if (shouldUseDirectByteBuffers())
/*      */     {
/*  559 */       if (!paramByteBuffer.isDirect()) {
/*  560 */         throw this.wrapper.unexpectedNonDirectByteBufferWithChannelSocket();
/*      */       }
/*  562 */       if (i > paramByteBuffer.capacity()) {
/*  563 */         if (this.orb.transportDebugFlag)
/*      */         {
/*  565 */           int j = System.identityHashCode(paramByteBuffer);
/*  566 */           StringBuffer localStringBuffer = new StringBuffer(80);
/*  567 */           localStringBuffer.append(".read: releasing ByteBuffer id (").append(j).append(") to ByteBufferPool.");
/*      */ 
/*  569 */           String str = localStringBuffer.toString();
/*  570 */           dprint(str);
/*      */         }
/*  572 */         this.orb.getByteBufferPool().releaseByteBuffer(paramByteBuffer);
/*  573 */         paramByteBuffer = this.orb.getByteBufferPool().getByteBuffer(i);
/*      */       }
/*  575 */       paramByteBuffer.position(paramInt1);
/*  576 */       paramByteBuffer.limit(i);
/*  577 */       readFully(paramByteBuffer, paramInt2, paramLong);
/*  578 */       paramByteBuffer.position(0);
/*  579 */       paramByteBuffer.limit(i);
/*  580 */       return paramByteBuffer;
/*      */     }
/*  582 */     if (paramByteBuffer.isDirect()) {
/*  583 */       throw this.wrapper.unexpectedDirectByteBufferWithNonChannelSocket();
/*      */     }
/*  585 */     byte[] arrayOfByte = new byte[i];
/*  586 */     readFully(getSocket().getInputStream(), arrayOfByte, paramInt1, paramInt2, paramLong);
/*      */ 
/*  588 */     return ByteBuffer.wrap(arrayOfByte);
/*      */   }
/*      */ 
/*      */   public void readFully(ByteBuffer paramByteBuffer, int paramInt, long paramLong)
/*      */     throws IOException
/*      */   {
/*  594 */     int i = 0;
/*  595 */     int j = 0;
/*  596 */     long l1 = this.readTimeouts.get_initial_time_to_wait();
/*  597 */     long l2 = 0L;
/*      */     do
/*      */     {
/*  617 */       j = getSocketChannel().read(paramByteBuffer);
/*      */ 
/*  619 */       if (j < 0) {
/*  620 */         throw new IOException("End-of-stream");
/*      */       }
/*  622 */       if (j == 0) {
/*      */         try {
/*  624 */           Thread.sleep(l1);
/*  625 */           l2 += l1;
/*  626 */           l1 = ()(l1 * this.readTimeouts.get_backoff_factor());
/*      */         }
/*      */         catch (InterruptedException localInterruptedException)
/*      */         {
/*  631 */           if (this.orb.transportDebugFlag) {
/*  632 */             dprint("readFully(): unexpected exception " + localInterruptedException.toString());
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  638 */         i += j;
/*      */       }
/*      */     }
/*  641 */     while ((i < paramInt) && (l2 < paramLong));
/*      */ 
/*  643 */     if ((i < paramInt) && (l2 >= paramLong))
/*      */     {
/*  646 */       throw this.wrapper.transportReadTimeoutExceeded(new Integer(paramInt), new Integer(i), new Long(paramLong), new Long(l2));
/*      */     }
/*      */ 
/*  651 */     getConnectionCache().stampTime(this);
/*      */   }
/*      */ 
/*      */   public void readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
/*      */     throws IOException
/*      */   {
/*  659 */     int i = 0;
/*  660 */     int j = 0;
/*  661 */     long l1 = this.readTimeouts.get_initial_time_to_wait();
/*  662 */     long l2 = 0L;
/*      */     do
/*      */     {
/*  682 */       j = paramInputStream.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
/*  683 */       if (j < 0) {
/*  684 */         throw new IOException("End-of-stream");
/*      */       }
/*  686 */       if (j == 0) {
/*      */         try {
/*  688 */           Thread.sleep(l1);
/*  689 */           l2 += l1;
/*  690 */           l1 = ()(l1 * this.readTimeouts.get_backoff_factor());
/*      */         }
/*      */         catch (InterruptedException localInterruptedException)
/*      */         {
/*  695 */           if (this.orb.transportDebugFlag) {
/*  696 */             dprint("readFully(): unexpected exception " + localInterruptedException.toString());
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  702 */         i += j;
/*      */       }
/*      */     }
/*  705 */     while ((i < paramInt2) && (l2 < paramLong));
/*      */ 
/*  707 */     if ((i < paramInt2) && (l2 >= paramLong))
/*      */     {
/*  710 */       throw this.wrapper.transportReadTimeoutExceeded(new Integer(paramInt2), new Integer(i), new Long(paramLong), new Long(l2));
/*      */     }
/*      */ 
/*  715 */     getConnectionCache().stampTime(this);
/*      */   }
/*      */ 
/*      */   public void write(ByteBuffer paramByteBuffer)
/*      */     throws IOException
/*      */   {
/*  721 */     if (shouldUseDirectByteBuffers())
/*      */     {
/*      */       do
/*      */       {
/*  733 */         getSocketChannel().write(paramByteBuffer);
/*      */       }
/*  735 */       while (paramByteBuffer.hasRemaining());
/*      */     }
/*      */     else {
/*  738 */       if (!paramByteBuffer.hasArray()) {
/*  739 */         throw this.wrapper.unexpectedDirectByteBufferWithNonChannelSocket();
/*      */       }
/*  741 */       byte[] arrayOfByte = paramByteBuffer.array();
/*  742 */       getSocket().getOutputStream().write(arrayOfByte, 0, paramByteBuffer.limit());
/*  743 */       getSocket().getOutputStream().flush();
/*      */     }
/*      */ 
/*  749 */     getConnectionCache().stampTime(this);
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */   {
/*      */     try
/*      */     {
/*  758 */       if (this.orb.transportDebugFlag) {
/*  759 */         dprint(".close->: " + this);
/*      */       }
/*  761 */       writeLock();
/*      */ 
/*  769 */       if (isBusy()) {
/*  770 */         writeUnlock();
/*  771 */         if (this.orb.transportDebugFlag)
/*  772 */           dprint(".close: isBusy so no close: " + this);
/*      */       }
/*      */       else
/*      */       {
/*      */         try
/*      */         {
/*      */           try {
/*  779 */             sendCloseConnection(GIOPVersion.V1_0);
/*      */           } catch (Throwable localThrowable) {
/*  781 */             this.wrapper.exceptionWhenSendingCloseConnection(localThrowable);
/*      */           }
/*      */ 
/*  784 */           synchronized (this.stateEvent) {
/*  785 */             this.state = 3;
/*  786 */             this.stateEvent.notifyAll();
/*      */           }
/*      */ 
/*  795 */           purgeCalls(this.wrapper.connectionRebind(), false, true);
/*      */         }
/*      */         catch (Exception localException) {
/*  798 */           if (this.orb.transportDebugFlag)
/*  799 */             dprint(".close: exception: " + this, localException);
/*      */         }
/*      */         try
/*      */         {
/*  803 */           Selector localSelector = this.orb.getTransportManager().getSelector(0);
/*  804 */           localSelector.unregisterForEvent(this);
/*  805 */           if (this.socketChannel != null) {
/*  806 */             this.socketChannel.close();
/*      */           }
/*  808 */           this.socket.close();
/*      */         } catch (IOException localIOException) {
/*  810 */           if (this.orb.transportDebugFlag) {
/*  811 */             dprint(".close: " + this, localIOException);
/*      */           }
/*      */         }
/*  814 */         closeConnectionResources();
/*      */       }
/*      */     } finally { if (this.orb.transportDebugFlag)
/*  817 */         dprint(".close<-: " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void closeConnectionResources()
/*      */   {
/*  823 */     if (this.orb.transportDebugFlag) {
/*  824 */       dprint(".closeConnectionResources->: " + this);
/*      */     }
/*  826 */     Selector localSelector = this.orb.getTransportManager().getSelector(0);
/*  827 */     localSelector.unregisterForEvent(this);
/*      */     try {
/*  829 */       if (this.socketChannel != null)
/*  830 */         this.socketChannel.close();
/*  831 */       if ((this.socket != null) && (!this.socket.isClosed()))
/*  832 */         this.socket.close();
/*      */     } catch (IOException localIOException) {
/*  834 */       if (this.orb.transportDebugFlag) {
/*  835 */         dprint(".closeConnectionResources: " + this, localIOException);
/*      */       }
/*      */     }
/*  838 */     if (this.orb.transportDebugFlag)
/*  839 */       dprint(".closeConnectionResources<-: " + this);
/*      */   }
/*      */ 
/*      */   public Acceptor getAcceptor()
/*      */   {
/*  846 */     return this.acceptor;
/*      */   }
/*      */ 
/*      */   public ContactInfo getContactInfo()
/*      */   {
/*  851 */     return this.contactInfo;
/*      */   }
/*      */ 
/*      */   public EventHandler getEventHandler()
/*      */   {
/*  856 */     return this;
/*      */   }
/*      */ 
/*      */   public OutputObject createOutputObject(MessageMediator paramMessageMediator)
/*      */   {
/*  862 */     throw new RuntimeException("*****SocketOrChannelConnectionImpl.createOutputObject - should not be called.");
/*      */   }
/*      */ 
/*      */   public boolean isServer()
/*      */   {
/*  871 */     return this.isServer;
/*      */   }
/*      */ 
/*      */   public boolean isBusy()
/*      */   {
/*  876 */     if ((this.serverRequestCount > 0) || (getResponseWaitingRoom().numberRegistered() > 0))
/*      */     {
/*  879 */       return true;
/*      */     }
/*  881 */     return false;
/*      */   }
/*      */ 
/*      */   public long getTimeStamp()
/*      */   {
/*  887 */     return this.timeStamp;
/*      */   }
/*      */ 
/*      */   public void setTimeStamp(long paramLong)
/*      */   {
/*  892 */     this.timeStamp = paramLong;
/*      */   }
/*      */ 
/*      */   public void setState(String paramString)
/*      */   {
/*  897 */     synchronized (this.stateEvent) {
/*  898 */       if (paramString.equals("ESTABLISHED")) {
/*  899 */         this.state = 2;
/*  900 */         this.stateEvent.notifyAll();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeLock()
/*      */   {
/*      */     try
/*      */     {
/*  917 */       if ((dprintWriteLocks) && (this.orb.transportDebugFlag)) {
/*  918 */         dprint(".writeLock->: " + this);
/*      */       }
/*      */       while (true)
/*      */       {
/*  922 */         int i = this.state;
/*  923 */         switch (i)
/*      */         {
/*      */         case 1:
/*  926 */           synchronized (this.stateEvent) {
/*  927 */             if (this.state == 1)
/*      */             {
/*      */               try
/*      */               {
/*  932 */                 this.stateEvent.wait();
/*      */               } catch (InterruptedException localInterruptedException1) {
/*  934 */                 if (this.orb.transportDebugFlag) {
/*  935 */                   dprint(".writeLock: OPENING InterruptedException: " + this);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*  940 */           break;
/*      */         case 2:
/*  943 */           synchronized (this.writeEvent) {
/*  944 */             if (!this.writeLocked) {
/*  945 */               this.writeLocked = true;
/*      */ 
/*  996 */               if ((dprintWriteLocks) && (this.orb.transportDebugFlag))
/*  997 */                 dprint(".writeLock<-: " + this);
/*      */               return;
/*      */             }
/*      */             try
/*      */             {
/*  952 */               while ((this.state == 2) && (this.writeLocked))
/*  953 */                 this.writeEvent.wait(100L);
/*      */             }
/*      */             catch (InterruptedException localInterruptedException2) {
/*  956 */               if (this.orb.transportDebugFlag) {
/*  957 */                 dprint(".writeLock: ESTABLISHED InterruptedException: " + this);
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*  962 */           break;
/*      */         case 5:
/*  970 */           synchronized (this.stateEvent) {
/*  971 */             if (this.state == 5)
/*      */             {
/*  974 */               throw this.wrapper.writeErrorSend();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         case 4:
/*  980 */           synchronized (this.stateEvent) {
/*  981 */             if (this.state == 4)
/*      */             {
/*  984 */               throw this.wrapper.connectionCloseRebind();
/*      */             }
/*      */           }
/*      */         case 3:
/*      */         default:
/*  988 */           if (this.orb.transportDebugFlag) {
/*  989 */             dprint(".writeLock: default: " + this);
/*      */           }
/*      */ 
/*  992 */           throw new RuntimeException(".writeLock: bad state");
/*      */         }
/*      */       }
/*      */     } finally {
/*  996 */       if ((dprintWriteLocks) && (this.orb.transportDebugFlag))
/*  997 */         dprint(".writeLock<-: " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeUnlock()
/*      */   {
/*      */     try
/*      */     {
/* 1005 */       if ((dprintWriteLocks) && (this.orb.transportDebugFlag)) {
/* 1006 */         dprint(".writeUnlock->: " + this);
/*      */       }
/* 1008 */       synchronized (this.writeEvent) {
/* 1009 */         this.writeLocked = false;
/* 1010 */         this.writeEvent.notify();
/*      */       }
/*      */     } finally {
/* 1013 */       if ((dprintWriteLocks) && (this.orb.transportDebugFlag))
/* 1014 */         dprint(".writeUnlock<-: " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void sendWithoutLock(OutputObject paramOutputObject)
/*      */   {
/*      */     try
/*      */     {
/* 1032 */       CDROutputObject localCDROutputObject = (CDROutputObject)paramOutputObject;
/* 1033 */       localCDROutputObject.writeTo(this);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1083 */       COMM_FAILURE localCOMM_FAILURE = this.wrapper.writeErrorSend(localIOException);
/* 1084 */       purgeCalls(localCOMM_FAILURE, false, true);
/* 1085 */       throw localCOMM_FAILURE;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void registerWaiter(MessageMediator paramMessageMediator)
/*      */   {
/* 1091 */     this.responseWaitingRoom.registerWaiter(paramMessageMediator);
/*      */   }
/*      */ 
/*      */   public void unregisterWaiter(MessageMediator paramMessageMediator)
/*      */   {
/* 1096 */     this.responseWaitingRoom.unregisterWaiter(paramMessageMediator);
/*      */   }
/*      */ 
/*      */   public InputObject waitForResponse(MessageMediator paramMessageMediator)
/*      */   {
/* 1101 */     return this.responseWaitingRoom.waitForResponse(paramMessageMediator);
/*      */   }
/*      */ 
/*      */   public void setConnectionCache(ConnectionCache paramConnectionCache)
/*      */   {
/* 1106 */     this.connectionCache = paramConnectionCache;
/*      */   }
/*      */ 
/*      */   public ConnectionCache getConnectionCache()
/*      */   {
/* 1111 */     return this.connectionCache;
/*      */   }
/*      */ 
/*      */   public void setUseSelectThreadToWait(boolean paramBoolean)
/*      */   {
/* 1121 */     this.useSelectThreadToWait = paramBoolean;
/*      */ 
/* 1125 */     setReadGiopHeaderOnly(shouldUseSelectThreadToWait());
/*      */   }
/*      */ 
/*      */   public void handleEvent()
/*      */   {
/* 1130 */     if (this.orb.transportDebugFlag) {
/* 1131 */       dprint(".handleEvent->: " + this);
/*      */     }
/* 1133 */     getSelectionKey().interestOps(getSelectionKey().interestOps() & (getInterestOps() ^ 0xFFFFFFFF));
/*      */ 
/* 1136 */     if (shouldUseWorkerThreadForEvent()) {
/* 1137 */       Object localObject = null;
/*      */       try {
/* 1139 */         int i = 0;
/* 1140 */         if (shouldReadGiopHeaderOnly()) {
/* 1141 */           this.partialMessageMediator = readBits();
/* 1142 */           i = this.partialMessageMediator.getThreadPoolToUse();
/*      */         }
/*      */ 
/* 1146 */         if (this.orb.transportDebugFlag) {
/* 1147 */           dprint(".handleEvent: addWork to pool: " + i);
/*      */         }
/* 1149 */         this.orb.getThreadPoolManager().getThreadPool(i).getWorkQueue(0).addWork(getWork());
/*      */       }
/*      */       catch (NoSuchThreadPoolException localNoSuchThreadPoolException) {
/* 1152 */         localObject = localNoSuchThreadPoolException;
/*      */       } catch (NoSuchWorkQueueException localNoSuchWorkQueueException) {
/* 1154 */         localObject = localNoSuchWorkQueueException;
/*      */       }
/*      */ 
/* 1157 */       if (localObject != null) {
/* 1158 */         if (this.orb.transportDebugFlag) {
/* 1159 */           dprint(".handleEvent: " + localObject);
/*      */         }
/* 1161 */         INTERNAL localINTERNAL = new INTERNAL("NoSuchThreadPoolException");
/* 1162 */         localINTERNAL.initCause(localObject);
/* 1163 */         throw localINTERNAL;
/*      */       }
/*      */     } else {
/* 1166 */       if (this.orb.transportDebugFlag) {
/* 1167 */         dprint(".handleEvent: doWork");
/*      */       }
/* 1169 */       getWork().doWork();
/*      */     }
/* 1171 */     if (this.orb.transportDebugFlag)
/* 1172 */       dprint(".handleEvent<-: " + this);
/*      */   }
/*      */ 
/*      */   public SelectableChannel getChannel()
/*      */   {
/* 1178 */     return this.socketChannel;
/*      */   }
/*      */ 
/*      */   public int getInterestOps()
/*      */   {
/* 1183 */     return 1;
/*      */   }
/*      */ 
/*      */   public Connection getConnection()
/*      */   {
/* 1190 */     return this;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/* 1200 */     return toString();
/*      */   }
/*      */ 
/*      */   public void doWork()
/*      */   {
/*      */     try {
/* 1206 */       if (this.orb.transportDebugFlag) {
/* 1207 */         dprint(".doWork->: " + this);
/*      */       }
/*      */ 
/* 1215 */       if (!shouldReadGiopHeaderOnly()) {
/* 1216 */         read();
/*      */       }
/*      */       else
/*      */       {
/* 1221 */         CorbaMessageMediator localCorbaMessageMediator = getPartialMessageMediator();
/*      */ 
/* 1225 */         localCorbaMessageMediator = finishReadingBits(localCorbaMessageMediator);
/*      */ 
/* 1227 */         if (localCorbaMessageMediator != null)
/*      */         {
/* 1230 */           dispatch(localCorbaMessageMediator);
/*      */         }
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1234 */       if (this.orb.transportDebugFlag) {
/* 1235 */         dprint(".doWork: ignoring Throwable: " + localThrowable + " " + this);
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 1240 */       if (this.orb.transportDebugFlag)
/* 1241 */         dprint(".doWork<-: " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEnqueueTime(long paramLong)
/*      */   {
/* 1248 */     this.enqueueTime = paramLong;
/*      */   }
/*      */ 
/*      */   public long getEnqueueTime()
/*      */   {
/* 1253 */     return this.enqueueTime;
/*      */   }
/*      */ 
/*      */   public boolean shouldReadGiopHeaderOnly()
/*      */   {
/* 1263 */     return this.shouldReadGiopHeaderOnly;
/*      */   }
/*      */ 
/*      */   protected void setReadGiopHeaderOnly(boolean paramBoolean) {
/* 1267 */     this.shouldReadGiopHeaderOnly = paramBoolean;
/*      */   }
/*      */ 
/*      */   public ResponseWaitingRoom getResponseWaitingRoom()
/*      */   {
/* 1272 */     return this.responseWaitingRoom;
/*      */   }
/*      */ 
/*      */   public void serverRequestMapPut(int paramInt, CorbaMessageMediator paramCorbaMessageMediator)
/*      */   {
/* 1281 */     this.serverRequestMap.put(new Integer(paramInt), paramCorbaMessageMediator);
/*      */   }
/*      */ 
/*      */   public CorbaMessageMediator serverRequestMapGet(int paramInt)
/*      */   {
/* 1286 */     return (CorbaMessageMediator)this.serverRequestMap.get(new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public void serverRequestMapRemove(int paramInt)
/*      */   {
/* 1292 */     this.serverRequestMap.remove(new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public Socket getSocket()
/*      */   {
/* 1300 */     return this.socket;
/*      */   }
/*      */ 
/*      */   public synchronized void serverRequestProcessingBegins()
/*      */   {
/* 1313 */     this.serverRequestCount += 1;
/*      */   }
/*      */ 
/*      */   public synchronized void serverRequestProcessingEnds()
/*      */   {
/* 1318 */     this.serverRequestCount -= 1;
/*      */   }
/*      */ 
/*      */   public synchronized int getNextRequestId()
/*      */   {
/* 1327 */     return this.requestId++;
/*      */   }
/*      */ 
/*      */   public ORB getBroker()
/*      */   {
/* 1335 */     return this.orb;
/*      */   }
/*      */ 
/*      */   public CodeSetComponentInfo.CodeSetContext getCodeSetContext()
/*      */   {
/* 1347 */     if (this.codeSetContext == null) {
/* 1348 */       synchronized (this) {
/* 1349 */         return this.codeSetContext;
/*      */       }
/*      */     }
/*      */ 
/* 1353 */     return this.codeSetContext;
/*      */   }
/*      */ 
/*      */   public synchronized void setCodeSetContext(CodeSetComponentInfo.CodeSetContext paramCodeSetContext)
/*      */   {
/* 1358 */     if (this.codeSetContext == null)
/*      */     {
/* 1360 */       if ((OSFCodeSetRegistry.lookupEntry(paramCodeSetContext.getCharCodeSet()) == null) || (OSFCodeSetRegistry.lookupEntry(paramCodeSetContext.getWCharCodeSet()) == null))
/*      */       {
/* 1365 */         throw this.wrapper.badCodesetsFromClient();
/*      */       }
/*      */ 
/* 1368 */       this.codeSetContext = paramCodeSetContext;
/*      */     }
/*      */   }
/*      */ 
/*      */   public MessageMediator clientRequestMapGet(int paramInt)
/*      */   {
/* 1385 */     return this.responseWaitingRoom.getMessageMediator(paramInt);
/*      */   }
/*      */ 
/*      */   public void clientReply_1_1_Put(MessageMediator paramMessageMediator)
/*      */   {
/* 1392 */     this.clientReply_1_1 = paramMessageMediator;
/*      */   }
/*      */ 
/*      */   public MessageMediator clientReply_1_1_Get()
/*      */   {
/* 1397 */     return this.clientReply_1_1;
/*      */   }
/*      */ 
/*      */   public void clientReply_1_1_Remove()
/*      */   {
/* 1402 */     this.clientReply_1_1 = null;
/*      */   }
/*      */ 
/*      */   public void serverRequest_1_1_Put(MessageMediator paramMessageMediator)
/*      */   {
/* 1409 */     this.serverRequest_1_1 = paramMessageMediator;
/*      */   }
/*      */ 
/*      */   public MessageMediator serverRequest_1_1_Get()
/*      */   {
/* 1414 */     return this.serverRequest_1_1;
/*      */   }
/*      */ 
/*      */   public void serverRequest_1_1_Remove()
/*      */   {
/* 1419 */     this.serverRequest_1_1 = null;
/*      */   }
/*      */ 
/*      */   protected String getStateString(int paramInt)
/*      */   {
/* 1424 */     synchronized (this.stateEvent) {
/* 1425 */       switch (paramInt) { case 1:
/* 1426 */         return "OPENING";
/*      */       case 2:
/* 1427 */         return "ESTABLISHED";
/*      */       case 3:
/* 1428 */         return "CLOSE_SENT";
/*      */       case 4:
/* 1429 */         return "CLOSE_RECVD";
/*      */       case 5:
/* 1430 */         return "ABORT"; }
/* 1431 */       return "???";
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized boolean isPostInitialContexts()
/*      */   {
/* 1437 */     return this.postInitialContexts;
/*      */   }
/*      */ 
/*      */   public synchronized void setPostInitialContexts()
/*      */   {
/* 1442 */     this.postInitialContexts = true;
/*      */   }
/*      */ 
/*      */   public void purgeCalls(SystemException paramSystemException, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1461 */     int i = paramSystemException.minor;
/*      */     try
/*      */     {
/* 1464 */       if (this.orb.transportDebugFlag) {
/* 1465 */         dprint(".purgeCalls->: " + i + "/" + paramBoolean1 + "/" + paramBoolean2 + " " + this);
/*      */       }
/*      */ 
/* 1473 */       synchronized (this.stateEvent) {
/* 1474 */         if ((this.state == 5) || (this.state == 4)) {
/* 1475 */           if (this.orb.transportDebugFlag) {
/* 1476 */             dprint(".purgeCalls: exiting since state is: " + getStateString(this.state) + " " + this);
/*      */           }
/*      */ 
/*      */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1486 */         if (!paramBoolean2)
/* 1487 */           writeLock();
/*      */       }
/*      */       catch (SystemException localSystemException) {
/* 1490 */         if (this.orb.transportDebugFlag) {
/* 1491 */           dprint(".purgeCalls: SystemException" + localSystemException + "; continuing " + this);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1498 */       synchronized (this.stateEvent) {
/* 1499 */         if (i == 1398079697) {
/* 1500 */           this.state = 4;
/* 1501 */           paramSystemException.completed = CompletionStatus.COMPLETED_NO;
/*      */         } else {
/* 1503 */           this.state = 5;
/* 1504 */           paramSystemException.completed = CompletionStatus.COMPLETED_MAYBE;
/*      */         }
/* 1506 */         this.stateEvent.notifyAll();
/*      */       }
/*      */       try
/*      */       {
/* 1510 */         this.socket.getInputStream().close();
/* 1511 */         this.socket.getOutputStream().close();
/* 1512 */         this.socket.close();
/*      */       } catch (Exception localException) {
/* 1514 */         if (this.orb.transportDebugFlag) {
/* 1515 */           dprint(".purgeCalls: Exception closing socket: " + localException + " " + this);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1523 */       this.responseWaitingRoom.signalExceptionToAllWaiters(paramSystemException);
/*      */     } finally {
/* 1525 */       if (this.contactInfo != null)
/* 1526 */         ((OutboundConnectionCache)getConnectionCache()).remove(this.contactInfo);
/* 1527 */       else if (this.acceptor != null) {
/* 1528 */         ((InboundConnectionCache)getConnectionCache()).remove(this);
/*      */       }
/*      */ 
/* 1543 */       writeUnlock();
/*      */ 
/* 1545 */       if (this.orb.transportDebugFlag)
/* 1546 */         dprint(".purgeCalls<-: " + i + "/" + paramBoolean1 + "/" + paramBoolean2 + " " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void sendCloseConnection(GIOPVersion paramGIOPVersion)
/*      */     throws IOException
/*      */   {
/* 1561 */     Message localMessage = MessageBase.createCloseConnection(paramGIOPVersion);
/* 1562 */     sendHelper(paramGIOPVersion, localMessage);
/*      */   }
/*      */ 
/*      */   public void sendMessageError(GIOPVersion paramGIOPVersion)
/*      */     throws IOException
/*      */   {
/* 1568 */     Message localMessage = MessageBase.createMessageError(paramGIOPVersion);
/* 1569 */     sendHelper(paramGIOPVersion, localMessage);
/*      */   }
/*      */ 
/*      */   public void sendCancelRequest(GIOPVersion paramGIOPVersion, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1581 */     CancelRequestMessage localCancelRequestMessage = MessageBase.createCancelRequest(paramGIOPVersion, paramInt);
/* 1582 */     sendHelper(paramGIOPVersion, localCancelRequestMessage);
/*      */   }
/*      */ 
/*      */   protected void sendHelper(GIOPVersion paramGIOPVersion, Message paramMessage)
/*      */     throws IOException
/*      */   {
/* 1589 */     CDROutputObject localCDROutputObject = OutputStreamFactory.newCDROutputObject(this.orb, null, paramGIOPVersion, this, paramMessage, (byte)1);
/*      */ 
/* 1592 */     paramMessage.write(localCDROutputObject);
/*      */ 
/* 1594 */     localCDROutputObject.writeTo(this);
/*      */   }
/*      */ 
/*      */   public void sendCancelRequestWithLock(GIOPVersion paramGIOPVersion, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1601 */     writeLock();
/*      */     try {
/* 1603 */       sendCancelRequest(paramGIOPVersion, paramInt);
/*      */     } finally {
/* 1605 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void setCodeBaseIOR(IOR paramIOR)
/*      */   {
/* 1624 */     this.codeBaseServerIOR = paramIOR;
/*      */   }
/*      */ 
/*      */   public final IOR getCodeBaseIOR() {
/* 1628 */     return this.codeBaseServerIOR;
/*      */   }
/*      */ 
/*      */   public final CodeBase getCodeBase()
/*      */   {
/* 1634 */     return this.cachedCodeBase;
/*      */   }
/*      */ 
/*      */   protected void setReadTimeouts(ReadTimeouts paramReadTimeouts)
/*      */   {
/* 1641 */     this.readTimeouts = paramReadTimeouts;
/*      */   }
/*      */ 
/*      */   protected void setPartialMessageMediator(CorbaMessageMediator paramCorbaMessageMediator) {
/* 1645 */     this.partialMessageMediator = paramCorbaMessageMediator;
/*      */   }
/*      */ 
/*      */   protected CorbaMessageMediator getPartialMessageMediator() {
/* 1649 */     return this.partialMessageMediator;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1654 */     synchronized (this.stateEvent) {
/* 1655 */       return "SocketOrChannelConnectionImpl[ " + (this.socketChannel == null ? this.socket.toString() : this.socketChannel.toString()) + " " + getStateString(this.state) + " " + shouldUseSelectThreadToWait() + " " + shouldUseWorkerThreadForEvent() + " " + shouldReadGiopHeaderOnly() + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   public void dprint(String paramString)
/*      */   {
/* 1670 */     ORBUtility.dprint("SocketOrChannelConnectionImpl", paramString);
/*      */   }
/*      */ 
/*      */   protected void dprint(String paramString, Throwable paramThrowable)
/*      */   {
/* 1675 */     dprint(paramString);
/* 1676 */     paramThrowable.printStackTrace(System.out);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.SocketOrChannelConnectionImpl
 * JD-Core Version:    0.6.2
 */