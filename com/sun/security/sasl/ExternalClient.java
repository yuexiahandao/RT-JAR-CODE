/*     */ package com.sun.security.sasl;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ final class ExternalClient
/*     */   implements SaslClient
/*     */ {
/*     */   private byte[] username;
/*  40 */   private boolean completed = false;
/*     */ 
/*     */   ExternalClient(String paramString)
/*     */     throws SaslException
/*     */   {
/*  50 */     if (paramString != null) {
/*     */       try {
/*  52 */         this.username = paramString.getBytes("UTF8");
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  54 */         throw new SaslException("Cannot convert " + paramString + " into UTF-8", localUnsupportedEncodingException);
/*     */       }
/*     */     }
/*     */     else
/*  58 */       this.username = new byte[0];
/*     */   }
/*     */ 
/*     */   public String getMechanismName()
/*     */   {
/*  69 */     return "EXTERNAL";
/*     */   }
/*     */ 
/*     */   public boolean hasInitialResponse()
/*     */   {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws SaslException
/*     */   {
/*     */   }
/*     */ 
/*     */   public byte[] evaluateChallenge(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/*  95 */     if (this.completed) {
/*  96 */       throw new IllegalStateException("EXTERNAL authentication already completed");
/*     */     }
/*     */ 
/*  99 */     this.completed = true;
/* 100 */     return this.username;
/*     */   }
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/* 108 */     return this.completed;
/*     */   }
/*     */ 
/*     */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 118 */     if (this.completed) {
/* 119 */       throw new SaslException("EXTERNAL has no supported QOP");
/*     */     }
/* 121 */     throw new IllegalStateException("EXTERNAL authentication Not completed");
/*     */   }
/*     */ 
/*     */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 133 */     if (this.completed) {
/* 134 */       throw new SaslException("EXTERNAL has no supported QOP");
/*     */     }
/* 136 */     throw new IllegalStateException("EXTERNAL authentication not completed");
/*     */   }
/*     */ 
/*     */   public Object getNegotiatedProperty(String paramString)
/*     */   {
/* 152 */     if (this.completed) {
/* 153 */       return null;
/*     */     }
/* 155 */     throw new IllegalStateException("EXTERNAL authentication not completed");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.ExternalClient
 * JD-Core Version:    0.6.2
 */