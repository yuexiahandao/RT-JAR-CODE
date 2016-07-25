/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ public final class BerEncoder extends Ber
/*     */ {
/*     */   private int curSeqIndex;
/*     */   private int[] seqOffset;
/*     */   private static final int INITIAL_SEQUENCES = 16;
/*     */   private static final int DEFAULT_BUFSIZE = 1024;
/*     */   private static final int BUF_GROWTH_FACTOR = 8;
/*     */ 
/*     */   public BerEncoder()
/*     */   {
/*  51 */     this(1024);
/*     */   }
/*     */ 
/*     */   public BerEncoder(int paramInt)
/*     */   {
/*  60 */     this.buf = new byte[paramInt];
/*  61 */     this.bufsize = paramInt;
/*  62 */     this.offset = 0;
/*     */ 
/*  64 */     this.seqOffset = new int[16];
/*  65 */     this.curSeqIndex = 0;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  73 */     while (this.offset > 0) {
/*  74 */       this.buf[(--this.offset)] = 0;
/*     */     }
/*  76 */     while (this.curSeqIndex > 0)
/*  77 */       this.seqOffset[(--this.curSeqIndex)] = 0;
/*     */   }
/*     */ 
/*     */   public int getDataLen()
/*     */   {
/*  87 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public byte[] getBuf()
/*     */   {
/*  99 */     if (this.curSeqIndex != 0) {
/* 100 */       throw new IllegalStateException("BER encode error: Unbalanced SEQUENCEs.");
/*     */     }
/* 102 */     return this.buf;
/*     */   }
/*     */ 
/*     */   public byte[] getTrimmedBuf()
/*     */   {
/* 111 */     int i = getDataLen();
/* 112 */     byte[] arrayOfByte = new byte[i];
/*     */ 
/* 114 */     System.arraycopy(getBuf(), 0, arrayOfByte, 0, i);
/* 115 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public void beginSeq(int paramInt)
/*     */   {
/* 126 */     if (this.curSeqIndex >= this.seqOffset.length) {
/* 127 */       int[] arrayOfInt = new int[this.seqOffset.length * 2];
/*     */ 
/* 129 */       for (int i = 0; i < this.seqOffset.length; i++) {
/* 130 */         arrayOfInt[i] = this.seqOffset[i];
/*     */       }
/* 132 */       this.seqOffset = arrayOfInt;
/*     */     }
/*     */ 
/* 135 */     encodeByte(paramInt);
/* 136 */     this.seqOffset[this.curSeqIndex] = this.offset;
/*     */ 
/* 143 */     ensureFreeBytes(3);
/* 144 */     this.offset += 3;
/*     */ 
/* 146 */     this.curSeqIndex += 1;
/*     */   }
/*     */ 
/*     */   public void endSeq()
/*     */     throws Ber.EncodeException
/*     */   {
/* 153 */     this.curSeqIndex -= 1;
/* 154 */     if (this.curSeqIndex < 0) {
/* 155 */       throw new IllegalStateException("BER encode error: Unbalanced SEQUENCEs.");
/*     */     }
/*     */ 
/* 158 */     int i = this.seqOffset[this.curSeqIndex] + 3;
/* 159 */     int j = this.offset - i;
/*     */ 
/* 161 */     if (j <= 127) {
/* 162 */       shiftSeqData(i, j, -2);
/* 163 */       this.buf[this.seqOffset[this.curSeqIndex]] = ((byte)j);
/* 164 */     } else if (j <= 255) {
/* 165 */       shiftSeqData(i, j, -1);
/* 166 */       this.buf[this.seqOffset[this.curSeqIndex]] = -127;
/* 167 */       this.buf[(this.seqOffset[this.curSeqIndex] + 1)] = ((byte)j);
/* 168 */     } else if (j <= 65535) {
/* 169 */       this.buf[this.seqOffset[this.curSeqIndex]] = -126;
/* 170 */       this.buf[(this.seqOffset[this.curSeqIndex] + 1)] = ((byte)(j >> 8));
/* 171 */       this.buf[(this.seqOffset[this.curSeqIndex] + 2)] = ((byte)j);
/* 172 */     } else if (j <= 16777215) {
/* 173 */       shiftSeqData(i, j, 1);
/* 174 */       this.buf[this.seqOffset[this.curSeqIndex]] = -125;
/* 175 */       this.buf[(this.seqOffset[this.curSeqIndex] + 1)] = ((byte)(j >> 16));
/* 176 */       this.buf[(this.seqOffset[this.curSeqIndex] + 2)] = ((byte)(j >> 8));
/* 177 */       this.buf[(this.seqOffset[this.curSeqIndex] + 3)] = ((byte)j);
/*     */     } else {
/* 179 */       throw new Ber.EncodeException("SEQUENCE too long");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void shiftSeqData(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 188 */     if (paramInt3 > 0) {
/* 189 */       ensureFreeBytes(paramInt3);
/*     */     }
/* 191 */     System.arraycopy(this.buf, paramInt1, this.buf, paramInt1 + paramInt3, paramInt2);
/* 192 */     this.offset += paramInt3;
/*     */   }
/*     */ 
/*     */   public void encodeByte(int paramInt)
/*     */   {
/* 199 */     ensureFreeBytes(1);
/* 200 */     this.buf[(this.offset++)] = ((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public void encodeInt(int paramInt)
/*     */   {
/* 217 */     encodeInt(paramInt, 2);
/*     */   }
/*     */ 
/*     */   public void encodeInt(int paramInt1, int paramInt2)
/*     */   {
/* 227 */     int i = -8388608;
/* 228 */     int j = 4;
/*     */ 
/* 230 */     while ((((paramInt1 & i) == 0) || ((paramInt1 & i) == i)) && (j > 1)) {
/* 231 */       j--;
/* 232 */       paramInt1 <<= 8;
/*     */     }
/*     */ 
/* 235 */     encodeInt(paramInt1, paramInt2, j);
/*     */   }
/*     */ 
/*     */   private void encodeInt(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 247 */     if (paramInt3 > 4) {
/* 248 */       throw new IllegalArgumentException("BER encode error: INTEGER too long.");
/*     */     }
/*     */ 
/* 251 */     ensureFreeBytes(2 + paramInt3);
/*     */ 
/* 253 */     this.buf[(this.offset++)] = ((byte)paramInt2);
/* 254 */     this.buf[(this.offset++)] = ((byte)paramInt3);
/*     */ 
/* 256 */     int i = -16777216;
/*     */ 
/* 258 */     while (paramInt3-- > 0) {
/* 259 */       this.buf[(this.offset++)] = ((byte)((paramInt1 & i) >> 24));
/* 260 */       paramInt1 <<= 8;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeBoolean(boolean paramBoolean)
/*     */   {
/* 271 */     encodeBoolean(paramBoolean, 1);
/*     */   }
/*     */ 
/*     */   public void encodeBoolean(boolean paramBoolean, int paramInt)
/*     */   {
/* 282 */     ensureFreeBytes(3);
/*     */ 
/* 284 */     this.buf[(this.offset++)] = ((byte)paramInt);
/* 285 */     this.buf[(this.offset++)] = 1;
/* 286 */     this.buf[(this.offset++)] = (paramBoolean ? -1 : 0);
/*     */   }
/*     */ 
/*     */   public void encodeString(String paramString, boolean paramBoolean)
/*     */     throws Ber.EncodeException
/*     */   {
/* 298 */     encodeString(paramString, 4, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void encodeString(String paramString, int paramInt, boolean paramBoolean)
/*     */     throws Ber.EncodeException
/*     */   {
/* 310 */     encodeByte(paramInt);
/*     */ 
/* 312 */     int i = 0;
/*     */ 
/* 314 */     byte[] arrayOfByte = null;
/*     */     int j;
/* 316 */     if (paramString == null)
/* 317 */       j = 0;
/* 318 */     else if (paramBoolean)
/*     */       try {
/* 320 */         arrayOfByte = paramString.getBytes("UTF8");
/* 321 */         j = arrayOfByte.length;
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 323 */         throw new Ber.EncodeException("UTF8 not available on platform");
/*     */       }
/*     */     else {
/*     */       try {
/* 327 */         arrayOfByte = paramString.getBytes("8859_1");
/* 328 */         j = arrayOfByte.length;
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/* 330 */         throw new Ber.EncodeException("8859_1 not available on platform");
/*     */       }
/*     */     }
/*     */ 
/* 334 */     encodeLength(j);
/*     */ 
/* 336 */     ensureFreeBytes(j);
/* 337 */     while (i < j)
/* 338 */       this.buf[(this.offset++)] = arrayOfByte[(i++)];
/*     */   }
/*     */ 
/*     */   public void encodeOctetString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws Ber.EncodeException
/*     */   {
/* 348 */     encodeByte(paramInt1);
/* 349 */     encodeLength(paramInt3);
/*     */ 
/* 351 */     if (paramInt3 > 0) {
/* 352 */       ensureFreeBytes(paramInt3);
/* 353 */       System.arraycopy(paramArrayOfByte, paramInt2, this.buf, this.offset, paramInt3);
/* 354 */       this.offset += paramInt3;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeOctetString(byte[] paramArrayOfByte, int paramInt)
/*     */     throws Ber.EncodeException
/*     */   {
/* 362 */     encodeOctetString(paramArrayOfByte, paramInt, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   private void encodeLength(int paramInt) throws Ber.EncodeException {
/* 366 */     ensureFreeBytes(4);
/*     */ 
/* 368 */     if (paramInt < 128) {
/* 369 */       this.buf[(this.offset++)] = ((byte)paramInt);
/* 370 */     } else if (paramInt <= 255) {
/* 371 */       this.buf[(this.offset++)] = -127;
/* 372 */       this.buf[(this.offset++)] = ((byte)paramInt);
/* 373 */     } else if (paramInt <= 65535) {
/* 374 */       this.buf[(this.offset++)] = -126;
/* 375 */       this.buf[(this.offset++)] = ((byte)(paramInt >> 8));
/* 376 */       this.buf[(this.offset++)] = ((byte)(paramInt & 0xFF));
/* 377 */     } else if (paramInt <= 16777215) {
/* 378 */       this.buf[(this.offset++)] = -125;
/* 379 */       this.buf[(this.offset++)] = ((byte)(paramInt >> 16));
/* 380 */       this.buf[(this.offset++)] = ((byte)(paramInt >> 8));
/* 381 */       this.buf[(this.offset++)] = ((byte)(paramInt & 0xFF));
/*     */     } else {
/* 383 */       throw new Ber.EncodeException("string too long");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeStringArray(String[] paramArrayOfString, boolean paramBoolean)
/*     */     throws Ber.EncodeException
/*     */   {
/* 392 */     if (paramArrayOfString == null)
/* 393 */       return;
/* 394 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 395 */       encodeString(paramArrayOfString[i], paramBoolean);
/*     */   }
/*     */ 
/*     */   private void ensureFreeBytes(int paramInt)
/*     */   {
/* 416 */     if (this.bufsize - this.offset < paramInt) {
/* 417 */       int i = this.bufsize * 8;
/* 418 */       if (i - this.offset < paramInt) {
/* 419 */         i += paramInt;
/*     */       }
/* 421 */       byte[] arrayOfByte = new byte[i];
/*     */ 
/* 423 */       System.arraycopy(this.buf, 0, arrayOfByte, 0, this.offset);
/*     */ 
/* 425 */       this.buf = arrayOfByte;
/* 426 */       this.bufsize = i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.BerEncoder
 * JD-Core Version:    0.6.2
 */