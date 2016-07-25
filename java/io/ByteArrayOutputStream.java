/*     */ package java.io;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ByteArrayOutputStream extends OutputStream
/*     */ {
/*     */   protected byte[] buf;
/*     */   protected int count;
/*     */   private static final int MAX_ARRAY_SIZE = 2147483639;
/*     */ 
/*     */   public ByteArrayOutputStream()
/*     */   {
/*  62 */     this(32);
/*     */   }
/*     */ 
/*     */   public ByteArrayOutputStream(int paramInt)
/*     */   {
/*  73 */     if (paramInt < 0) {
/*  74 */       throw new IllegalArgumentException("Negative initial size: " + paramInt);
/*     */     }
/*     */ 
/*  77 */     this.buf = new byte[paramInt];
/*     */   }
/*     */ 
/*     */   private void ensureCapacity(int paramInt)
/*     */   {
/*  92 */     if (paramInt - this.buf.length > 0)
/*  93 */       grow(paramInt);
/*     */   }
/*     */ 
/*     */   private void grow(int paramInt)
/*     */   {
/* 112 */     int i = this.buf.length;
/* 113 */     int j = i << 1;
/* 114 */     if (j - paramInt < 0)
/* 115 */       j = paramInt;
/* 116 */     if (j - 2147483639 > 0)
/* 117 */       j = hugeCapacity(paramInt);
/* 118 */     this.buf = Arrays.copyOf(this.buf, j);
/*     */   }
/*     */ 
/*     */   private static int hugeCapacity(int paramInt) {
/* 122 */     if (paramInt < 0)
/* 123 */       throw new OutOfMemoryError();
/* 124 */     return paramInt > 2147483639 ? 2147483647 : 2147483639;
/*     */   }
/*     */ 
/*     */   public synchronized void write(int paramInt)
/*     */   {
/* 135 */     ensureCapacity(this.count + 1);
/* 136 */     this.buf[this.count] = ((byte)paramInt);
/* 137 */     this.count += 1;
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 149 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 - paramArrayOfByte.length > 0))
/*     */     {
/* 151 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 153 */     ensureCapacity(this.count + paramInt2);
/* 154 */     System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.count, paramInt2);
/* 155 */     this.count += paramInt2;
/*     */   }
/*     */ 
/*     */   public synchronized void writeTo(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 167 */     paramOutputStream.write(this.buf, 0, this.count);
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/* 179 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public synchronized byte[] toByteArray()
/*     */   {
/* 191 */     return Arrays.copyOf(this.buf, this.count);
/*     */   }
/*     */ 
/*     */   public synchronized int size()
/*     */   {
/* 202 */     return this.count;
/*     */   }
/*     */ 
/*     */   public synchronized String toString()
/*     */   {
/* 221 */     return new String(this.buf, 0, this.count);
/*     */   }
/*     */ 
/*     */   public synchronized String toString(String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 245 */     return new String(this.buf, 0, this.count, paramString);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized String toString(int paramInt)
/*     */   {
/* 272 */     return new String(this.buf, paramInt, 0, this.count);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ByteArrayOutputStream
 * JD-Core Version:    0.6.2
 */