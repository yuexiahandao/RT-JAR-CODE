/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*    */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*    */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.ior.ObjectKey;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*    */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*    */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*    */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*    */ 
/*    */ public class INSServerRequestDispatcher
/*    */   implements CorbaServerRequestDispatcher
/*    */ {
/* 58 */   private ORB orb = null;
/*    */   private ORBUtilSystemException wrapper;
/*    */ 
/*    */   public INSServerRequestDispatcher(ORB paramORB)
/*    */   {
/* 62 */     this.orb = paramORB;
/* 63 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*    */   }
/*    */ 
/*    */   public IOR locate(ObjectKey paramObjectKey)
/*    */   {
/* 71 */     String str = new String(paramObjectKey.getBytes(this.orb));
/* 72 */     return getINSReference(str);
/*    */   }
/*    */ 
/*    */   public void dispatch(MessageMediator paramMessageMediator)
/*    */   {
/* 77 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*    */ 
/* 80 */     String str = new String(localCorbaMessageMediator.getObjectKey().getBytes(this.orb));
/* 81 */     localCorbaMessageMediator.getProtocolHandler().createLocationForward(localCorbaMessageMediator, getINSReference(str), null);
/*    */   }
/*    */ 
/*    */   private IOR getINSReference(String paramString)
/*    */   {
/* 90 */     IOR localIOR = ORBUtility.getIOR(this.orb.getLocalResolver().resolve(paramString));
/* 91 */     if (localIOR != null)
/*    */     {
/* 94 */       return localIOR;
/*    */     }
/*    */ 
/* 97 */     throw this.wrapper.servantNotFound();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.INSServerRequestDispatcher
 * JD-Core Version:    0.6.2
 */