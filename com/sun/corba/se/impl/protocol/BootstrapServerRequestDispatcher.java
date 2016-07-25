/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.MarshalInputStream;
/*     */ import com.sun.corba.se.impl.encoding.MarshalOutputStream;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
/*     */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*     */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.omg.CORBA.SystemException;
/*     */ 
/*     */ public class BootstrapServerRequestDispatcher
/*     */   implements CorbaServerRequestDispatcher
/*     */ {
/*     */   private ORB orb;
/*     */   ORBUtilSystemException wrapper;
/*     */   private static final boolean debug = false;
/*     */ 
/*     */   public BootstrapServerRequestDispatcher(ORB paramORB)
/*     */   {
/*  71 */     this.orb = paramORB;
/*  72 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */   }
/*     */ 
/*     */   public void dispatch(MessageMediator paramMessageMediator)
/*     */   {
/*  82 */     CorbaMessageMediator localCorbaMessageMediator1 = (CorbaMessageMediator)paramMessageMediator;
/*  83 */     CorbaMessageMediator localCorbaMessageMediator2 = null;
/*     */     try
/*     */     {
/*  86 */       MarshalInputStream localMarshalInputStream = (MarshalInputStream)localCorbaMessageMediator1.getInputObject();
/*     */ 
/*  88 */       localObject1 = localCorbaMessageMediator1.getOperationName();
/*  89 */       localCorbaMessageMediator2 = localCorbaMessageMediator1.getProtocolHandler().createResponse(localCorbaMessageMediator1, null);
/*  90 */       MarshalOutputStream localMarshalOutputStream = (MarshalOutputStream)localCorbaMessageMediator2.getOutputObject();
/*     */       java.lang.Object localObject2;
/*     */       java.lang.Object localObject3;
/*  93 */       if (((String)localObject1).equals("get"))
/*     */       {
/*  95 */         localObject2 = localMarshalInputStream.read_string();
/*     */ 
/*  98 */         localObject3 = this.orb.getLocalResolver().resolve((String)localObject2);
/*     */ 
/* 102 */         localMarshalOutputStream.write_Object((org.omg.CORBA.Object)localObject3);
/* 103 */       } else if (((String)localObject1).equals("list")) {
/* 104 */         localObject2 = this.orb.getLocalResolver().list();
/* 105 */         localMarshalOutputStream.write_long(((Set)localObject2).size());
/* 106 */         localObject3 = ((Set)localObject2).iterator();
/* 107 */         while (((Iterator)localObject3).hasNext()) {
/* 108 */           String str = (String)((Iterator)localObject3).next();
/* 109 */           localMarshalOutputStream.write_string(str);
/*     */         }
/*     */       } else {
/* 112 */         throw this.wrapper.illegalBootstrapOperation(localObject1);
/*     */       }
/*     */     }
/*     */     catch (SystemException localSystemException)
/*     */     {
/* 117 */       localCorbaMessageMediator2 = localCorbaMessageMediator1.getProtocolHandler().createSystemExceptionResponse(localCorbaMessageMediator1, localSystemException, null);
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 121 */       localObject1 = this.wrapper.bootstrapRuntimeException(localRuntimeException);
/* 122 */       localCorbaMessageMediator2 = localCorbaMessageMediator1.getProtocolHandler().createSystemExceptionResponse(localCorbaMessageMediator1, (SystemException)localObject1, null);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 126 */       java.lang.Object localObject1 = this.wrapper.bootstrapException(localException);
/* 127 */       localCorbaMessageMediator2 = localCorbaMessageMediator1.getProtocolHandler().createSystemExceptionResponse(localCorbaMessageMediator1, (SystemException)localObject1, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public IOR locate(ObjectKey paramObjectKey)
/*     */   {
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   public int getId()
/*     */   {
/* 147 */     throw this.wrapper.genericNoImpl();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.BootstrapServerRequestDispatcher
 * JD-Core Version:    0.6.2
 */