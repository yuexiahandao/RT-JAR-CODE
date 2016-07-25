/*     */ package java.util.zip;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import sun.misc.JavaUtilZipAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ public class Adler32
/*     */   implements Checksum
/*     */ {
/*  41 */   private int adler = 1;
/*     */ 
/*     */   public void update(int paramInt)
/*     */   {
/*  56 */     this.adler = update(this.adler, paramInt);
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  63 */     if (paramArrayOfByte == null) {
/*  64 */       throw new NullPointerException();
/*     */     }
/*  66 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2)) {
/*  67 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/*  69 */     this.adler = updateBytes(this.adler, paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte)
/*     */   {
/*  78 */     this.adler = updateBytes(this.adler, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   private void update(ByteBuffer paramByteBuffer)
/*     */   {
/*  94 */     int i = paramByteBuffer.position();
/*  95 */     int j = paramByteBuffer.limit();
/*  96 */     assert (i <= j);
/*  97 */     int k = j - i;
/*  98 */     if (k <= 0)
/*  99 */       return;
/* 100 */     if ((paramByteBuffer instanceof DirectBuffer)) {
/* 101 */       this.adler = updateByteBuffer(this.adler, ((DirectBuffer)paramByteBuffer).address(), i, k);
/* 102 */     } else if (paramByteBuffer.hasArray()) {
/* 103 */       this.adler = updateBytes(this.adler, paramByteBuffer.array(), i + paramByteBuffer.arrayOffset(), k);
/*     */     } else {
/* 105 */       byte[] arrayOfByte = new byte[k];
/* 106 */       paramByteBuffer.get(arrayOfByte);
/* 107 */       this.adler = updateBytes(this.adler, arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/* 109 */     paramByteBuffer.position(j);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 116 */     this.adler = 1;
/*     */   }
/*     */ 
/*     */   public long getValue()
/*     */   {
/* 123 */     return this.adler & 0xFFFFFFFF; } 
/*     */   private static native int update(int paramInt1, int paramInt2);
/*     */ 
/*     */   private static native int updateBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*     */ 
/*     */   private static native int updateByteBuffer(int paramInt1, long paramLong, int paramInt2, int paramInt3);
/*     */ 
/* 128 */   static { SharedSecrets.setJavaUtilZipAccess(new JavaUtilZipAccess() {
/*     */       public void update(Adler32 paramAnonymousAdler32, ByteBuffer paramAnonymousByteBuffer) {
/* 130 */         paramAnonymousAdler32.update(paramAnonymousByteBuffer);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.Adler32
 * JD-Core Version:    0.6.2
 */