/*     */ package java.io;
/*     */ 
/*     */ public class DataOutputStream extends FilterOutputStream
/*     */   implements DataOutput
/*     */ {
/*     */   protected int written;
/*  48 */   private byte[] bytearr = null;
/*     */ 
/* 204 */   private byte[] writeBuffer = new byte[8];
/*     */ 
/*     */   public DataOutputStream(OutputStream paramOutputStream)
/*     */   {
/*  60 */     super(paramOutputStream);
/*     */   }
/*     */ 
/*     */   private void incCount(int paramInt)
/*     */   {
/*  68 */     int i = this.written + paramInt;
/*  69 */     if (i < 0) {
/*  70 */       i = 2147483647;
/*     */     }
/*  72 */     this.written = i;
/*     */   }
/*     */ 
/*     */   public synchronized void write(int paramInt)
/*     */     throws IOException
/*     */   {
/*  88 */     this.out.write(paramInt);
/*  89 */     incCount(1);
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 107 */     this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 108 */     incCount(paramInt2);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 123 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public final void writeBoolean(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 139 */     this.out.write(paramBoolean ? 1 : 0);
/* 140 */     incCount(1);
/*     */   }
/*     */ 
/*     */   public final void writeByte(int paramInt)
/*     */     throws IOException
/*     */   {
/* 153 */     this.out.write(paramInt);
/* 154 */     incCount(1);
/*     */   }
/*     */ 
/*     */   public final void writeShort(int paramInt)
/*     */     throws IOException
/*     */   {
/* 167 */     this.out.write(paramInt >>> 8 & 0xFF);
/* 168 */     this.out.write(paramInt >>> 0 & 0xFF);
/* 169 */     incCount(2);
/*     */   }
/*     */ 
/*     */   public final void writeChar(int paramInt)
/*     */     throws IOException
/*     */   {
/* 182 */     this.out.write(paramInt >>> 8 & 0xFF);
/* 183 */     this.out.write(paramInt >>> 0 & 0xFF);
/* 184 */     incCount(2);
/*     */   }
/*     */ 
/*     */   public final void writeInt(int paramInt)
/*     */     throws IOException
/*     */   {
/* 197 */     this.out.write(paramInt >>> 24 & 0xFF);
/* 198 */     this.out.write(paramInt >>> 16 & 0xFF);
/* 199 */     this.out.write(paramInt >>> 8 & 0xFF);
/* 200 */     this.out.write(paramInt >>> 0 & 0xFF);
/* 201 */     incCount(4);
/*     */   }
/*     */ 
/*     */   public final void writeLong(long paramLong)
/*     */     throws IOException
/*     */   {
/* 216 */     this.writeBuffer[0] = ((byte)(int)(paramLong >>> 56));
/* 217 */     this.writeBuffer[1] = ((byte)(int)(paramLong >>> 48));
/* 218 */     this.writeBuffer[2] = ((byte)(int)(paramLong >>> 40));
/* 219 */     this.writeBuffer[3] = ((byte)(int)(paramLong >>> 32));
/* 220 */     this.writeBuffer[4] = ((byte)(int)(paramLong >>> 24));
/* 221 */     this.writeBuffer[5] = ((byte)(int)(paramLong >>> 16));
/* 222 */     this.writeBuffer[6] = ((byte)(int)(paramLong >>> 8));
/* 223 */     this.writeBuffer[7] = ((byte)(int)(paramLong >>> 0));
/* 224 */     this.out.write(this.writeBuffer, 0, 8);
/* 225 */     incCount(8);
/*     */   }
/*     */ 
/*     */   public final void writeFloat(float paramFloat)
/*     */     throws IOException
/*     */   {
/* 242 */     writeInt(Float.floatToIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   public final void writeDouble(double paramDouble)
/*     */     throws IOException
/*     */   {
/* 259 */     writeLong(Double.doubleToLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   public final void writeBytes(String paramString)
/*     */     throws IOException
/*     */   {
/* 274 */     int i = paramString.length();
/* 275 */     for (int j = 0; j < i; j++) {
/* 276 */       this.out.write((byte)paramString.charAt(j));
/*     */     }
/* 278 */     incCount(i);
/*     */   }
/*     */ 
/*     */   public final void writeChars(String paramString)
/*     */     throws IOException
/*     */   {
/* 294 */     int i = paramString.length();
/* 295 */     for (int j = 0; j < i; j++) {
/* 296 */       int k = paramString.charAt(j);
/* 297 */       this.out.write(k >>> 8 & 0xFF);
/* 298 */       this.out.write(k >>> 0 & 0xFF);
/*     */     }
/* 300 */     incCount(i * 2);
/*     */   }
/*     */ 
/*     */   public final void writeUTF(String paramString)
/*     */     throws IOException
/*     */   {
/* 323 */     writeUTF(paramString, this);
/*     */   }
/*     */ 
/*     */   static int writeUTF(String paramString, DataOutput paramDataOutput)
/*     */     throws IOException
/*     */   {
/* 347 */     int i = paramString.length();
/* 348 */     int j = 0;
/* 349 */     int m = 0;
/*     */     int k;
/* 352 */     for (int n = 0; n < i; n++) {
/* 353 */       k = paramString.charAt(n);
/* 354 */       if ((k >= 1) && (k <= 127))
/* 355 */         j++;
/* 356 */       else if (k > 2047)
/* 357 */         j += 3;
/*     */       else {
/* 359 */         j += 2;
/*     */       }
/*     */     }
/*     */ 
/* 363 */     if (j > 65535) {
/* 364 */       throw new UTFDataFormatException("encoded string too long: " + j + " bytes");
/*     */     }
/*     */ 
/* 367 */     byte[] arrayOfByte = null;
/* 368 */     if ((paramDataOutput instanceof DataOutputStream)) {
/* 369 */       DataOutputStream localDataOutputStream = (DataOutputStream)paramDataOutput;
/* 370 */       if ((localDataOutputStream.bytearr == null) || (localDataOutputStream.bytearr.length < j + 2))
/* 371 */         localDataOutputStream.bytearr = new byte[j * 2 + 2];
/* 372 */       arrayOfByte = localDataOutputStream.bytearr;
/*     */     } else {
/* 374 */       arrayOfByte = new byte[j + 2];
/*     */     }
/*     */ 
/* 377 */     arrayOfByte[(m++)] = ((byte)(j >>> 8 & 0xFF));
/* 378 */     arrayOfByte[(m++)] = ((byte)(j >>> 0 & 0xFF));
/*     */ 
/* 380 */     int i1 = 0;
/* 381 */     for (i1 = 0; i1 < i; i1++) {
/* 382 */       k = paramString.charAt(i1);
/* 383 */       if ((k < 1) || (k > 127)) break;
/* 384 */       arrayOfByte[(m++)] = ((byte)k);
/*     */     }
/*     */ 
/* 387 */     for (; i1 < i; i1++) {
/* 388 */       k = paramString.charAt(i1);
/* 389 */       if ((k >= 1) && (k <= 127)) {
/* 390 */         arrayOfByte[(m++)] = ((byte)k);
/*     */       }
/* 392 */       else if (k > 2047) {
/* 393 */         arrayOfByte[(m++)] = ((byte)(0xE0 | k >> 12 & 0xF));
/* 394 */         arrayOfByte[(m++)] = ((byte)(0x80 | k >> 6 & 0x3F));
/* 395 */         arrayOfByte[(m++)] = ((byte)(0x80 | k >> 0 & 0x3F));
/*     */       } else {
/* 397 */         arrayOfByte[(m++)] = ((byte)(0xC0 | k >> 6 & 0x1F));
/* 398 */         arrayOfByte[(m++)] = ((byte)(0x80 | k >> 0 & 0x3F));
/*     */       }
/*     */     }
/* 401 */     paramDataOutput.write(arrayOfByte, 0, j + 2);
/* 402 */     return j + 2;
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/* 414 */     return this.written;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.DataOutputStream
 * JD-Core Version:    0.6.2
 */