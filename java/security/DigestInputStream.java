/*     */ package java.security;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class DigestInputStream extends FilterInputStream
/*     */ {
/*  69 */   private boolean on = true;
/*     */   protected MessageDigest digest;
/*     */ 
/*     */   public DigestInputStream(InputStream paramInputStream, MessageDigest paramMessageDigest)
/*     */   {
/*  85 */     super(paramInputStream);
/*  86 */     setMessageDigest(paramMessageDigest);
/*     */   }
/*     */ 
/*     */   public MessageDigest getMessageDigest()
/*     */   {
/*  96 */     return this.digest;
/*     */   }
/*     */ 
/*     */   public void setMessageDigest(MessageDigest paramMessageDigest)
/*     */   {
/* 106 */     this.digest = paramMessageDigest;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 124 */     int i = this.in.read();
/* 125 */     if ((this.on) && (i != -1)) {
/* 126 */       this.digest.update((byte)i);
/*     */     }
/* 128 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 161 */     int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 162 */     if ((this.on) && (i != -1)) {
/* 163 */       this.digest.update(paramArrayOfByte, paramInt1, i);
/*     */     }
/* 165 */     return i;
/*     */   }
/*     */ 
/*     */   public void on(boolean paramBoolean)
/*     */   {
/* 178 */     this.on = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 186 */     return "[Digest Input Stream] " + this.digest.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.DigestInputStream
 * JD-Core Version:    0.6.2
 */