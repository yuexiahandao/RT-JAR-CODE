/*     */ package org.jcp.xml.dsig.internal;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.utils.UnsyncByteArrayOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class DigesterOutputStream extends OutputStream
/*     */ {
/*  48 */   private boolean buffer = false;
/*     */   private UnsyncByteArrayOutputStream bos;
/*     */   private final MessageDigest md;
/*  51 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal");
/*     */ 
/*     */   public DigesterOutputStream(MessageDigest paramMessageDigest)
/*     */   {
/*  59 */     this(paramMessageDigest, false);
/*     */   }
/*     */ 
/*     */   public DigesterOutputStream(MessageDigest paramMessageDigest, boolean paramBoolean)
/*     */   {
/*  69 */     this.md = paramMessageDigest;
/*  70 */     this.buffer = paramBoolean;
/*  71 */     if (paramBoolean)
/*  72 */       this.bos = new UnsyncByteArrayOutputStream();
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */   {
/*  78 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */   {
/*  83 */     if (this.buffer) {
/*  84 */       this.bos.write(paramInt);
/*     */     }
/*  86 */     this.md.update((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  91 */     if (this.buffer) {
/*  92 */       this.bos.write(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*  94 */     if (log.isLoggable(Level.FINER)) {
/*  95 */       log.log(Level.FINER, "Pre-digested input:");
/*  96 */       StringBuffer localStringBuffer = new StringBuffer(paramInt2);
/*  97 */       for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
/*  98 */         localStringBuffer.append((char)paramArrayOfByte[i]);
/*     */       }
/* 100 */       log.log(Level.FINER, localStringBuffer.toString());
/*     */     }
/* 102 */     this.md.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public byte[] getDigestValue()
/*     */   {
/* 109 */     return this.md.digest();
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */   {
/* 117 */     if (this.buffer) {
/* 118 */       return new ByteArrayInputStream(this.bos.toByteArray());
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.DigesterOutputStream
 * JD-Core Version:    0.6.2
 */