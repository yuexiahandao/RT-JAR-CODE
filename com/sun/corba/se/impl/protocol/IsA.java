/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.oa.NullServant;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ class IsA extends SpecialMethod
/*     */ {
/*     */   public boolean isNonExistentMethod()
/*     */   {
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 107 */     return "_is_a";
/*     */   }
/*     */ 
/*     */   public CorbaMessageMediator invoke(Object paramObject, CorbaMessageMediator paramCorbaMessageMediator, byte[] paramArrayOfByte, ObjectAdapter paramObjectAdapter)
/*     */   {
/* 114 */     if ((paramObject == null) || ((paramObject instanceof NullServant))) {
/* 115 */       localObject1 = (ORB)paramCorbaMessageMediator.getBroker();
/* 116 */       localObject2 = ORBUtilSystemException.get((ORB)localObject1, "oa.invocation");
/*     */ 
/* 119 */       return paramCorbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(paramCorbaMessageMediator, ((ORBUtilSystemException)localObject2).badSkeleton(), null);
/*     */     }
/*     */ 
/* 123 */     Object localObject1 = paramObjectAdapter.getInterfaces(paramObject, paramArrayOfByte);
/* 124 */     Object localObject2 = ((InputStream)paramCorbaMessageMediator.getInputObject()).read_string();
/*     */ 
/* 126 */     boolean bool = false;
/* 127 */     for (int i = 0; i < localObject1.length; i++) {
/* 128 */       if (localObject1[i].equals(localObject2)) {
/* 129 */         bool = true;
/* 130 */         break;
/*     */       }
/*     */     }
/* 133 */     CorbaMessageMediator localCorbaMessageMediator = paramCorbaMessageMediator.getProtocolHandler().createResponse(paramCorbaMessageMediator, null);
/*     */ 
/* 135 */     ((OutputStream)localCorbaMessageMediator.getOutputObject()).write_boolean(bool);
/* 136 */     return localCorbaMessageMediator;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.IsA
 * JD-Core Version:    0.6.2
 */