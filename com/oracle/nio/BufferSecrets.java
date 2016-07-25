/*     */ package com.oracle.nio;
/*     */ 
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import sun.misc.JavaNioAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ public final class BufferSecrets<A>
/*     */ {
/*  33 */   private static final JavaNioAccess javaNioAccess = SharedSecrets.getJavaNioAccess();
/*     */ 
/*  39 */   private static final BufferSecrets<?> theBufferSecrets = new BufferSecrets();
/*     */ 
/*     */   public static <A> BufferSecrets<A> instance()
/*     */   {
/*  55 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  56 */     if (localSecurityManager != null)
/*  57 */       localSecurityManager.checkPermission(new BufferSecretsPermission("access"));
/*  58 */     return theBufferSecrets;
/*     */   }
/*     */ 
/*     */   public ByteBuffer newDirectByteBuffer(long paramLong, int paramInt, A paramA)
/*     */   {
/*  90 */     if (paramInt < 0)
/*  91 */       throw new IllegalArgumentException("Negative capacity: " + paramInt);
/*  92 */     return javaNioAccess.newDirectByteBuffer(paramLong, paramInt, paramA);
/*     */   }
/*     */ 
/*     */   public long address(Buffer paramBuffer)
/*     */   {
/* 103 */     if ((paramBuffer instanceof DirectBuffer))
/* 104 */       return ((DirectBuffer)paramBuffer).address();
/* 105 */     if (paramBuffer == null) {
/* 106 */       throw new NullPointerException();
/*     */     }
/* 108 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public A attachment(Buffer paramBuffer)
/*     */   {
/* 120 */     if ((paramBuffer instanceof DirectBuffer))
/* 121 */       return ((DirectBuffer)paramBuffer).attachment();
/* 122 */     if (paramBuffer == null)
/* 123 */       throw new NullPointerException();
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   public void truncate(Buffer paramBuffer)
/*     */   {
/* 147 */     javaNioAccess.truncate(paramBuffer);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.oracle.nio.BufferSecrets
 * JD-Core Version:    0.6.2
 */