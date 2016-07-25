/*     */ package com.sun.security.sasl;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ final class PlainClient
/*     */   implements SaslClient
/*     */ {
/*  38 */   private boolean completed = false;
/*     */   private byte[] pw;
/*     */   private String authorizationID;
/*     */   private String authenticationID;
/*  42 */   private static byte SEP = 0;
/*     */ 
/*     */   PlainClient(String paramString1, String paramString2, byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/*  57 */     if ((paramString2 == null) || (paramArrayOfByte == null)) {
/*  58 */       throw new SaslException("PLAIN: authorization ID and password must be specified");
/*     */     }
/*     */ 
/*  62 */     this.authorizationID = paramString1;
/*  63 */     this.authenticationID = paramString2;
/*  64 */     this.pw = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getMechanismName()
/*     */   {
/*  74 */     return "PLAIN";
/*     */   }
/*     */ 
/*     */   public boolean hasInitialResponse() {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   public void dispose() throws SaslException {
/*  82 */     clearPassword();
/*     */   }
/*     */ 
/*     */   public byte[] evaluateChallenge(byte[] paramArrayOfByte)
/*     */     throws SaslException
/*     */   {
/*  96 */     if (this.completed) {
/*  97 */       throw new IllegalStateException("PLAIN authentication already completed");
/*     */     }
/*     */ 
/* 100 */     this.completed = true;
/*     */     try
/*     */     {
/* 103 */       Object localObject = this.authorizationID != null ? this.authorizationID.getBytes("UTF8") : null;
/*     */ 
/* 106 */       byte[] arrayOfByte1 = this.authenticationID.getBytes("UTF8");
/*     */ 
/* 108 */       byte[] arrayOfByte2 = new byte[this.pw.length + arrayOfByte1.length + 2 + (localObject == null ? 0 : localObject.length)];
/*     */ 
/* 111 */       int i = 0;
/* 112 */       if (localObject != null) {
/* 113 */         System.arraycopy(localObject, 0, arrayOfByte2, 0, localObject.length);
/* 114 */         i = localObject.length;
/*     */       }
/* 116 */       arrayOfByte2[(i++)] = SEP;
/* 117 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte2, i, arrayOfByte1.length);
/*     */ 
/* 119 */       i += arrayOfByte1.length;
/* 120 */       arrayOfByte2[(i++)] = SEP;
/*     */ 
/* 122 */       System.arraycopy(this.pw, 0, arrayOfByte2, i, this.pw.length);
/*     */ 
/* 124 */       clearPassword();
/* 125 */       return arrayOfByte2;
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 127 */       throw new SaslException("Cannot get UTF-8 encoding of ids", localUnsupportedEncodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/* 138 */     return this.completed;
/*     */   }
/*     */ 
/*     */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 148 */     if (this.completed) {
/* 149 */       throw new SaslException("PLAIN supports neither integrity nor privacy");
/*     */     }
/*     */ 
/* 152 */     throw new IllegalStateException("PLAIN authentication not completed");
/*     */   }
/*     */ 
/*     */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/* 162 */     if (this.completed) {
/* 163 */       throw new SaslException("PLAIN supports neither integrity nor privacy");
/*     */     }
/*     */ 
/* 166 */     throw new IllegalStateException("PLAIN authentication not completed");
/*     */   }
/*     */ 
/*     */   public Object getNegotiatedProperty(String paramString)
/*     */   {
/* 181 */     if (this.completed) {
/* 182 */       if (paramString.equals("javax.security.sasl.qop")) {
/* 183 */         return "auth";
/*     */       }
/* 185 */       return null;
/*     */     }
/*     */ 
/* 188 */     throw new IllegalStateException("PLAIN authentication not completed");
/*     */   }
/*     */ 
/*     */   private void clearPassword()
/*     */   {
/* 193 */     if (this.pw != null)
/*     */     {
/* 195 */       for (int i = 0; i < this.pw.length; i++) {
/* 196 */         this.pw[i] = 0;
/*     */       }
/* 198 */       this.pw = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/* 203 */     clearPassword();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.PlainClient
 * JD-Core Version:    0.6.2
 */