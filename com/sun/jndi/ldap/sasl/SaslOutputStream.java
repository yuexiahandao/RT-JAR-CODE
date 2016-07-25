/*     */ package com.sun.jndi.ldap.sasl;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ class SaslOutputStream extends FilterOutputStream
/*     */ {
/*     */   private static final boolean debug = false;
/*  38 */   private byte[] lenBuf = new byte[4];
/*  39 */   private int rawSendSize = 65536;
/*     */   private SaslClient sc;
/*     */ 
/*     */   SaslOutputStream(SaslClient paramSaslClient, OutputStream paramOutputStream)
/*     */     throws SaslException
/*     */   {
/*  43 */     super(paramOutputStream);
/*  44 */     this.sc = paramSaslClient;
/*     */ 
/*  50 */     String str = (String)paramSaslClient.getNegotiatedProperty("javax.security.sasl.rawsendsize");
/*  51 */     if (str != null)
/*     */       try {
/*  53 */         this.rawSendSize = Integer.parseInt(str);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/*  55 */         throw new SaslException("javax.security.sasl.rawsendsize property must be numeric string: " + str);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/*  65 */     byte[] arrayOfByte = new byte[1];
/*  66 */     arrayOfByte[0] = ((byte)paramInt);
/*  67 */     write(arrayOfByte, 0, 1);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  83 */     for (int j = 0; j < paramInt2; j += this.rawSendSize)
/*     */     {
/*  86 */       int i = paramInt2 - j < this.rawSendSize ? paramInt2 - j : this.rawSendSize;
/*     */ 
/*  89 */       byte[] arrayOfByte = this.sc.wrap(paramArrayOfByte, paramInt1 + j, i);
/*     */ 
/*  92 */       intToNetworkByteOrder(arrayOfByte.length, this.lenBuf, 0, 4);
/*     */ 
/*  97 */       this.out.write(this.lenBuf, 0, 4);
/*     */ 
/* 100 */       this.out.write(arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 105 */     Object localObject = null;
/*     */     try {
/* 107 */       this.sc.dispose();
/*     */     }
/*     */     catch (SaslException localSaslException) {
/* 110 */       localObject = localSaslException;
/*     */     }
/* 112 */     super.close();
/*     */ 
/* 114 */     if (localObject != null)
/* 115 */       throw localObject;
/*     */   }
/*     */ 
/*     */   private static void intToNetworkByteOrder(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 126 */     if (paramInt3 > 4) {
/* 127 */       throw new IllegalArgumentException("Cannot handle more than 4 bytes");
/*     */     }
/*     */ 
/* 130 */     for (int i = paramInt3 - 1; i >= 0; i--) {
/* 131 */       paramArrayOfByte[(paramInt2 + i)] = ((byte)(paramInt1 & 0xFF));
/* 132 */       paramInt1 >>>= 8;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.sasl.SaslOutputStream
 * JD-Core Version:    0.6.2
 */