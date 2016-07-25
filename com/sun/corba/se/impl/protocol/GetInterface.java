/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.oa.NullServant;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*     */ 
/*     */ class GetInterface extends SpecialMethod
/*     */ {
/*     */   public boolean isNonExistentMethod()
/*     */   {
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 147 */     return "_interface";
/*     */   }
/*     */ 
/*     */   public CorbaMessageMediator invoke(Object paramObject, CorbaMessageMediator paramCorbaMessageMediator, byte[] paramArrayOfByte, ObjectAdapter paramObjectAdapter)
/*     */   {
/* 154 */     ORB localORB = (ORB)paramCorbaMessageMediator.getBroker();
/* 155 */     ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get(localORB, "oa.invocation");
/*     */ 
/* 158 */     if ((paramObject == null) || ((paramObject instanceof NullServant))) {
/* 159 */       return paramCorbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(paramCorbaMessageMediator, localORBUtilSystemException.badSkeleton(), null);
/*     */     }
/*     */ 
/* 162 */     return paramCorbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(paramCorbaMessageMediator, localORBUtilSystemException.getinterfaceNotImplemented(), null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.GetInterface
 * JD-Core Version:    0.6.2
 */