/*     */ package java.security;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public abstract class MessageDigestSpi
/*     */ {
/*     */   private byte[] tempArray;
/*     */ 
/*     */   protected int engineGetDigestLength()
/*     */   {
/*  72 */     return 0;
/*     */   }
/*     */ 
/*     */   protected abstract void engineUpdate(byte paramByte);
/*     */ 
/*     */   protected abstract void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */   protected void engineUpdate(ByteBuffer paramByteBuffer)
/*     */   {
/* 106 */     if (!paramByteBuffer.hasRemaining())
/*     */       return;
/*     */     int j;
/*     */     int k;
/* 109 */     if (paramByteBuffer.hasArray()) {
/* 110 */       byte[] arrayOfByte = paramByteBuffer.array();
/* 111 */       j = paramByteBuffer.arrayOffset();
/* 112 */       k = paramByteBuffer.position();
/* 113 */       int m = paramByteBuffer.limit();
/* 114 */       engineUpdate(arrayOfByte, j + k, m - k);
/* 115 */       paramByteBuffer.position(m);
/*     */     } else {
/* 117 */       int i = paramByteBuffer.remaining();
/* 118 */       j = JCAUtil.getTempArraySize(i);
/* 119 */       if ((this.tempArray == null) || (j > this.tempArray.length)) {
/* 120 */         this.tempArray = new byte[j];
/*     */       }
/* 122 */       while (i > 0) {
/* 123 */         k = Math.min(i, this.tempArray.length);
/* 124 */         paramByteBuffer.get(this.tempArray, 0, k);
/* 125 */         engineUpdate(this.tempArray, 0, k);
/* 126 */         i -= k;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract byte[] engineDigest();
/*     */ 
/*     */   protected int engineDigest(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws DigestException
/*     */   {
/* 176 */     byte[] arrayOfByte = engineDigest();
/* 177 */     if (paramInt2 < arrayOfByte.length)
/* 178 */       throw new DigestException("partial digests not returned");
/* 179 */     if (paramArrayOfByte.length - paramInt1 < arrayOfByte.length) {
/* 180 */       throw new DigestException("insufficient space in the output buffer to store the digest");
/*     */     }
/* 182 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, arrayOfByte.length);
/* 183 */     return arrayOfByte.length;
/*     */   }
/*     */ 
/*     */   protected abstract void engineReset();
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 200 */     if ((this instanceof Cloneable)) {
/* 201 */       return super.clone();
/*     */     }
/* 203 */     throw new CloneNotSupportedException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.MessageDigestSpi
 * JD-Core Version:    0.6.2
 */