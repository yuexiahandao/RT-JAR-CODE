/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.security.DigestException;
/*     */ import java.security.MessageDigestSpi;
/*     */ import java.security.ProviderException;
/*     */ 
/*     */ abstract class DigestBase extends MessageDigestSpi
/*     */   implements Cloneable
/*     */ {
/*     */   private byte[] oneByte;
/*     */   private final String algorithm;
/*     */   private final int digestLength;
/*     */   private final int blockSize;
/*     */   final byte[] buffer;
/*     */   private int bufOfs;
/*     */   long bytesProcessed;
/* 223 */   static final byte[] padding = new byte[''];
/*     */ 
/*     */   DigestBase(String paramString, int paramInt1, int paramInt2)
/*     */   {
/*  80 */     this.algorithm = paramString;
/*  81 */     this.digestLength = paramInt1;
/*  82 */     this.blockSize = paramInt2;
/*  83 */     this.buffer = new byte[paramInt2];
/*     */   }
/*     */ 
/*     */   DigestBase(DigestBase paramDigestBase)
/*     */   {
/*  90 */     this.algorithm = paramDigestBase.algorithm;
/*  91 */     this.digestLength = paramDigestBase.digestLength;
/*  92 */     this.blockSize = paramDigestBase.blockSize;
/*  93 */     this.buffer = ((byte[])paramDigestBase.buffer.clone());
/*  94 */     this.bufOfs = paramDigestBase.bufOfs;
/*  95 */     this.bytesProcessed = paramDigestBase.bytesProcessed;
/*     */   }
/*     */ 
/*     */   protected final int engineGetDigestLength()
/*     */   {
/* 100 */     return this.digestLength;
/*     */   }
/*     */ 
/*     */   protected final void engineUpdate(byte paramByte)
/*     */   {
/* 105 */     if (this.oneByte == null) {
/* 106 */       this.oneByte = new byte[1];
/*     */     }
/* 108 */     this.oneByte[0] = paramByte;
/* 109 */     engineUpdate(this.oneByte, 0, 1);
/*     */   }
/*     */ 
/*     */   protected final void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 114 */     if (paramInt2 == 0) {
/* 115 */       return;
/*     */     }
/* 117 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2)) {
/* 118 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 120 */     if (this.bytesProcessed < 0L) {
/* 121 */       engineReset();
/*     */     }
/* 123 */     this.bytesProcessed += paramInt2;
/*     */ 
/* 125 */     if (this.bufOfs != 0) {
/* 126 */       int i = Math.min(paramInt2, this.blockSize - this.bufOfs);
/* 127 */       System.arraycopy(paramArrayOfByte, paramInt1, this.buffer, this.bufOfs, i);
/* 128 */       this.bufOfs += i;
/* 129 */       paramInt1 += i;
/* 130 */       paramInt2 -= i;
/* 131 */       if (this.bufOfs >= this.blockSize)
/*     */       {
/* 133 */         implCompress(this.buffer, 0);
/* 134 */         this.bufOfs = 0;
/*     */       }
/*     */     }
/*     */ 
/* 138 */     while (paramInt2 >= this.blockSize) {
/* 139 */       implCompress(paramArrayOfByte, paramInt1);
/* 140 */       paramInt2 -= this.blockSize;
/* 141 */       paramInt1 += this.blockSize;
/*     */     }
/*     */ 
/* 144 */     if (paramInt2 > 0) {
/* 145 */       System.arraycopy(paramArrayOfByte, paramInt1, this.buffer, 0, paramInt2);
/* 146 */       this.bufOfs = paramInt2;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void engineReset()
/*     */   {
/* 152 */     if (this.bytesProcessed == 0L)
/*     */     {
/* 154 */       return;
/*     */     }
/* 156 */     implReset();
/* 157 */     this.bufOfs = 0;
/* 158 */     this.bytesProcessed = 0L;
/*     */   }
/*     */ 
/*     */   protected final byte[] engineDigest()
/*     */   {
/* 163 */     byte[] arrayOfByte = new byte[this.digestLength];
/*     */     try {
/* 165 */       engineDigest(arrayOfByte, 0, arrayOfByte.length);
/*     */     } catch (DigestException localDigestException) {
/* 167 */       throw ((ProviderException)new ProviderException("Internal error").initCause(localDigestException));
/*     */     }
/*     */ 
/* 170 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected final int engineDigest(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws DigestException
/*     */   {
/* 176 */     if (paramInt2 < this.digestLength) {
/* 177 */       throw new DigestException("Length must be at least " + this.digestLength + " for " + this.algorithm + "digests");
/*     */     }
/*     */ 
/* 180 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2)) {
/* 181 */       throw new DigestException("Buffer too short to store digest");
/*     */     }
/* 183 */     if (this.bytesProcessed < 0L) {
/* 184 */       engineReset();
/*     */     }
/* 186 */     implDigest(paramArrayOfByte, paramInt1);
/* 187 */     this.bytesProcessed = -1L;
/* 188 */     return this.digestLength;
/*     */   }
/*     */ 
/*     */   abstract void implCompress(byte[] paramArrayOfByte, int paramInt);
/*     */ 
/*     */   abstract void implDigest(byte[] paramArrayOfByte, int paramInt);
/*     */ 
/*     */   abstract void implReset();
/*     */ 
/*     */   public abstract Object clone();
/*     */ 
/*     */   static
/*     */   {
/* 224 */     padding[0] = -128;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.DigestBase
 * JD-Core Version:    0.6.2
 */