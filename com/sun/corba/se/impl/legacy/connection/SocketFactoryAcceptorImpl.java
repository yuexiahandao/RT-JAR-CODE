/*    */ package com.sun.corba.se.impl.legacy.connection;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*    */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*    */ import com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl;
/*    */ import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.orb.ORBData;
/*    */ 
/*    */ public class SocketFactoryAcceptorImpl extends SocketOrChannelAcceptorImpl
/*    */ {
/*    */   public SocketFactoryAcceptorImpl(ORB paramORB, int paramInt, String paramString1, String paramString2)
/*    */   {
/* 51 */     super(paramORB, paramInt, paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   public boolean initialize()
/*    */   {
/* 61 */     if (this.initialized) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (this.orb.transportDebugFlag)
/* 65 */       dprint("initialize: " + this);
/*    */     try
/*    */     {
/* 68 */       this.serverSocket = this.orb.getORBData().getLegacySocketFactory().createServerSocket(this.type, this.port);
/*    */ 
/* 70 */       internalInitialize();
/*    */     } catch (Throwable localThrowable) {
/* 72 */       throw this.wrapper.createListenerFailed(localThrowable, Integer.toString(this.port));
/*    */     }
/* 74 */     this.initialized = true;
/* 75 */     return true;
/*    */   }
/*    */ 
/*    */   protected String toStringName()
/*    */   {
/* 85 */     return "SocketFactoryAcceptorImpl";
/*    */   }
/*    */ 
/*    */   protected void dprint(String paramString)
/*    */   {
/* 90 */     ORBUtility.dprint(toStringName(), paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.SocketFactoryAcceptorImpl
 * JD-Core Version:    0.6.2
 */