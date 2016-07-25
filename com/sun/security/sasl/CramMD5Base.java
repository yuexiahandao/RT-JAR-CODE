/*     */ package com.sun.security.sasl;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ abstract class CramMD5Base
/*     */ {
/*  45 */   protected boolean completed = false;
/*  46 */   protected boolean aborted = false;
/*     */   protected byte[] pw;
/*     */   private static final int MD5_BLOCKSIZE = 64;
/*     */   private static final String SASL_LOGGER_NAME = "javax.security.sasl";
/*     */   protected static Logger logger;
/*     */ 
/*     */   protected CramMD5Base()
/*     */   {
/*  50 */     initLogger();
/*     */   }
/*     */ 
/*     */   public String getMechanismName()
/*     */   {
/*  59 */     return "CRAM-MD5";
/*     */   }
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/*  69 */     return this.completed;
/*     */   }
/*     */ 
/*     */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/*  79 */     if (this.completed) {
/*  80 */       throw new IllegalStateException("CRAM-MD5 supports neither integrity nor privacy");
/*     */     }
/*     */ 
/*  83 */     throw new IllegalStateException("CRAM-MD5 authentication not completed");
/*     */   }
/*     */ 
/*     */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SaslException
/*     */   {
/*  94 */     if (this.completed) {
/*  95 */       throw new IllegalStateException("CRAM-MD5 supports neither integrity nor privacy");
/*     */     }
/*     */ 
/*  98 */     throw new IllegalStateException("CRAM-MD5 authentication not completed");
/*     */   }
/*     */ 
/*     */   public Object getNegotiatedProperty(String paramString)
/*     */   {
/* 113 */     if (this.completed) {
/* 114 */       if (paramString.equals("javax.security.sasl.qop")) {
/* 115 */         return "auth";
/*     */       }
/* 117 */       return null;
/*     */     }
/*     */ 
/* 120 */     throw new IllegalStateException("CRAM-MD5 authentication not completed");
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws SaslException
/*     */   {
/* 126 */     clearPassword();
/*     */   }
/*     */ 
/*     */   protected void clearPassword() {
/* 130 */     if (this.pw != null)
/*     */     {
/* 132 */       for (int i = 0; i < this.pw.length; i++) {
/* 133 */         this.pw[i] = 0;
/*     */       }
/* 135 */       this.pw = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/* 140 */     clearPassword();
/*     */   }
/*     */ 
/*     */   static final String HMAC_MD5(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 160 */     MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
/*     */ 
/* 163 */     if (paramArrayOfByte1.length > 64) {
/* 164 */       paramArrayOfByte1 = localMessageDigest.digest(paramArrayOfByte1);
/*     */     }
/*     */ 
/* 167 */     byte[] arrayOfByte1 = new byte[64];
/* 168 */     byte[] arrayOfByte2 = new byte[64];
/*     */ 
/* 173 */     for (int i = 0; i < paramArrayOfByte1.length; i++) {
/* 174 */       arrayOfByte1[i] = paramArrayOfByte1[i];
/* 175 */       arrayOfByte2[i] = paramArrayOfByte1[i];
/*     */     }
/*     */ 
/* 179 */     for (i = 0; i < 64; i++)
/*     */     {
/*     */       int tmp76_74 = i;
/*     */       byte[] tmp76_73 = arrayOfByte1; tmp76_73[tmp76_74] = ((byte)(tmp76_73[tmp76_74] ^ 0x36));
/*     */       int tmp87_85 = i;
/*     */       byte[] tmp87_83 = arrayOfByte2; tmp87_83[tmp87_85] = ((byte)(tmp87_83[tmp87_85] ^ 0x5C));
/*     */     }
/*     */ 
/* 185 */     localMessageDigest.update(arrayOfByte1);
/* 186 */     localMessageDigest.update(paramArrayOfByte2);
/* 187 */     byte[] arrayOfByte3 = localMessageDigest.digest();
/*     */ 
/* 190 */     localMessageDigest.update(arrayOfByte2);
/* 191 */     localMessageDigest.update(arrayOfByte3);
/* 192 */     arrayOfByte3 = localMessageDigest.digest();
/*     */ 
/* 195 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 197 */     for (i = 0; i < arrayOfByte3.length; i++) {
/* 198 */       if ((arrayOfByte3[i] & 0xFF) < 16) {
/* 199 */         localStringBuffer.append("0" + Integer.toHexString(arrayOfByte3[i] & 0xFF));
/*     */       }
/*     */       else {
/* 202 */         localStringBuffer.append(Integer.toHexString(arrayOfByte3[i] & 0xFF));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 207 */     Arrays.fill(arrayOfByte1, (byte)0);
/* 208 */     Arrays.fill(arrayOfByte2, (byte)0);
/* 209 */     arrayOfByte1 = null;
/* 210 */     arrayOfByte2 = null;
/*     */ 
/* 212 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static synchronized void initLogger()
/*     */   {
/* 219 */     if (logger == null)
/* 220 */       logger = Logger.getLogger("javax.security.sasl");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.CramMD5Base
 * JD-Core Version:    0.6.2
 */