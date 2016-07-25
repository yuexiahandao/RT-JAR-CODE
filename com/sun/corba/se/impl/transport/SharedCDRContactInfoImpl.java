/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDROutputObject;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.protocol.CorbaMessageMediatorImpl;
/*     */ import com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl;
/*     */ import com.sun.corba.se.pept.broker.Broker;
/*     */ import com.sun.corba.se.pept.encoding.OutputObject;
/*     */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public class SharedCDRContactInfoImpl extends CorbaContactInfoBase
/*     */ {
/*  56 */   private static int requestId = 0;
/*     */   protected ORBUtilSystemException wrapper;
/*     */ 
/*     */   public SharedCDRContactInfoImpl(ORB paramORB, CorbaContactInfoList paramCorbaContactInfoList, IOR paramIOR, short paramShort)
/*     */   {
/*  66 */     this.orb = paramORB;
/*  67 */     this.contactInfoList = paramCorbaContactInfoList;
/*  68 */     this.effectiveTargetIOR = paramIOR;
/*  69 */     this.addressingDisposition = paramShort;
/*     */   }
/*     */ 
/*     */   public ClientRequestDispatcher getClientRequestDispatcher()
/*     */   {
/*  80 */     return new SharedCDRClientRequestDispatcherImpl();
/*     */   }
/*     */ 
/*     */   public boolean isConnectionBased()
/*     */   {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean shouldCacheConnection()
/*     */   {
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   public String getConnectionCacheType()
/*     */   {
/*  95 */     throw getWrapper().methodShouldNotBeCalled();
/*     */   }
/*     */ 
/*     */   public Connection createConnection()
/*     */   {
/* 100 */     throw getWrapper().methodShouldNotBeCalled();
/*     */   }
/*     */ 
/*     */   public MessageMediator createMessageMediator(Broker paramBroker, ContactInfo paramContactInfo, Connection paramConnection, String paramString, boolean paramBoolean)
/*     */   {
/* 110 */     if (paramConnection != null)
/*     */     {
/* 112 */       throw new RuntimeException("connection is not null");
/*     */     }
/*     */ 
/* 115 */     CorbaMessageMediatorImpl localCorbaMessageMediatorImpl = new CorbaMessageMediatorImpl((ORB)paramBroker, paramContactInfo, null, GIOPVersion.chooseRequestVersion((ORB)paramBroker, this.effectiveTargetIOR), this.effectiveTargetIOR, requestId++, getAddressingDisposition(), paramString, paramBoolean);
/*     */ 
/* 128 */     return localCorbaMessageMediatorImpl;
/*     */   }
/*     */ 
/*     */   public OutputObject createOutputObject(MessageMediator paramMessageMediator)
/*     */   {
/* 133 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */ 
/* 136 */     CDROutputObject localCDROutputObject = OutputStreamFactory.newCDROutputObject(this.orb, paramMessageMediator, localCorbaMessageMediator.getRequestHeader(), localCorbaMessageMediator.getStreamFormatVersion(), 0);
/*     */ 
/* 141 */     paramMessageMediator.setOutputObject(localCDROutputObject);
/* 142 */     return localCDROutputObject;
/*     */   }
/*     */ 
/*     */   public String getMonitoringName()
/*     */   {
/* 152 */     throw getWrapper().methodShouldNotBeCalled();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 167 */     return "SharedCDRContactInfoImpl[]";
/*     */   }
/*     */ 
/*     */   protected ORBUtilSystemException getWrapper()
/*     */   {
/* 179 */     if (this.wrapper == null) {
/* 180 */       this.wrapper = ORBUtilSystemException.get(this.orb, "rpc.transport");
/*     */     }
/*     */ 
/* 183 */     return this.wrapper;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.SharedCDRContactInfoImpl
 * JD-Core Version:    0.6.2
 */