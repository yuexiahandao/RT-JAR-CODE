/*     */ package com.sun.corba.se.impl.legacy.connection;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.transport.SocketOrChannelConnectionImpl;
/*     */ import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
/*     */ import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*     */ import com.sun.corba.se.spi.transport.SocketInfo;
/*     */ import java.net.Socket;
/*     */ import java.nio.channels.SocketChannel;
/*     */ 
/*     */ public class SocketFactoryConnectionImpl extends SocketOrChannelConnectionImpl
/*     */ {
/*     */   public SocketFactoryConnectionImpl(ORB paramORB, CorbaContactInfo paramCorbaContactInfo, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  62 */     super(paramORB, paramBoolean1, paramBoolean2);
/*     */ 
/*  66 */     this.contactInfo = paramCorbaContactInfo;
/*     */ 
/*  68 */     boolean bool = !paramBoolean1;
/*  69 */     SocketInfo localSocketInfo = ((SocketFactoryContactInfoImpl)paramCorbaContactInfo).socketInfo;
/*     */     try
/*     */     {
/*  73 */       this.socket = paramORB.getORBData().getLegacySocketFactory().createSocket(localSocketInfo);
/*     */ 
/*  75 */       this.socketChannel = this.socket.getChannel();
/*  76 */       if (this.socketChannel != null) {
/*  77 */         this.socketChannel.configureBlocking(bool);
/*     */       }
/*     */       else
/*     */       {
/*  81 */         setUseSelectThreadToWait(false);
/*     */       }
/*  83 */       if (paramORB.transportDebugFlag)
/*  84 */         dprint(".initialize: connection created: " + this.socket);
/*     */     }
/*     */     catch (GetEndPointInfoAgainException localGetEndPointInfoAgainException) {
/*  87 */       throw this.wrapper.connectFailure(localGetEndPointInfoAgainException, localSocketInfo.getType(), localSocketInfo.getHost(), Integer.toString(localSocketInfo.getPort()));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*  91 */       throw this.wrapper.connectFailure(localException, localSocketInfo.getType(), localSocketInfo.getHost(), Integer.toString(localSocketInfo.getPort()));
/*     */     }
/*     */ 
/*  95 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 100 */     synchronized (this.stateEvent) {
/* 101 */       return "SocketFactoryConnectionImpl[ " + (this.socketChannel == null ? this.socket.toString() : this.socketChannel.toString()) + " " + getStateString(this.state) + " " + shouldUseSelectThreadToWait() + " " + shouldUseWorkerThreadForEvent() + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dprint(String paramString)
/*     */   {
/* 115 */     ORBUtility.dprint("SocketFactoryConnectionImpl", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.SocketFactoryConnectionImpl
 * JD-Core Version:    0.6.2
 */