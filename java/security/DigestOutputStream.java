/*     */ package java.security;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class DigestOutputStream extends FilterOutputStream
/*     */ {
/*  57 */   private boolean on = true;
/*     */   protected MessageDigest digest;
/*     */ 
/*     */   public DigestOutputStream(OutputStream paramOutputStream, MessageDigest paramMessageDigest)
/*     */   {
/*  73 */     super(paramOutputStream);
/*  74 */     setMessageDigest(paramMessageDigest);
/*     */   }
/*     */ 
/*     */   public MessageDigest getMessageDigest()
/*     */   {
/*  84 */     return this.digest;
/*     */   }
/*     */ 
/*     */   public void setMessageDigest(MessageDigest paramMessageDigest)
/*     */   {
/*  94 */     this.digest = paramMessageDigest;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 115 */     this.out.write(paramInt);
/* 116 */     if (this.on)
/* 117 */       this.digest.update((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 145 */     this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 146 */     if (this.on)
/* 147 */       this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void on(boolean paramBoolean)
/*     */   {
/* 161 */     this.on = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 169 */     return "[Digest Output Stream] " + this.digest.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.DigestOutputStream
 * JD-Core Version:    0.6.2
 */