/*     */ package com.sun.corba.se.impl.legacy.connection;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.transport.SocketOrChannelContactInfoImpl;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import com.sun.corba.se.spi.transport.SocketInfo;
/*     */ 
/*     */ public class SocketFactoryContactInfoImpl extends SocketOrChannelContactInfoImpl
/*     */ {
/*     */   protected ORBUtilSystemException wrapper;
/*     */   protected SocketInfo socketInfo;
/*     */ 
/*     */   public SocketFactoryContactInfoImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SocketFactoryContactInfoImpl(ORB paramORB, CorbaContactInfoList paramCorbaContactInfoList, IOR paramIOR, short paramShort, SocketInfo paramSocketInfo)
/*     */   {
/*  65 */     super(paramORB, paramCorbaContactInfoList);
/*  66 */     this.effectiveTargetIOR = paramIOR;
/*  67 */     this.addressingDisposition = paramShort;
/*     */ 
/*  69 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.transport");
/*     */ 
/*  72 */     this.socketInfo = paramORB.getORBData().getLegacySocketFactory().getEndPointInfo(paramORB, paramIOR, paramSocketInfo);
/*     */ 
/*  76 */     this.socketType = this.socketInfo.getType();
/*  77 */     this.hostname = this.socketInfo.getHost();
/*  78 */     this.port = this.socketInfo.getPort();
/*     */   }
/*     */ 
/*     */   public Connection createConnection()
/*     */   {
/*  88 */     SocketFactoryConnectionImpl localSocketFactoryConnectionImpl = new SocketFactoryConnectionImpl(this.orb, this, this.orb.getORBData().connectionSocketUseSelectThreadToWait(), this.orb.getORBData().connectionSocketUseWorkerThreadForEvent());
/*     */ 
/*  93 */     return localSocketFactoryConnectionImpl;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return "SocketFactoryContactInfoImpl[" + this.socketType + " " + this.hostname + " " + this.port + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.SocketFactoryContactInfoImpl
 * JD-Core Version:    0.6.2
 */