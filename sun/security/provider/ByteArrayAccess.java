/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ final class ByteArrayAccess
/*     */ {
/*  62 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */ 
/*  83 */   private static final boolean littleEndianUnaligned = (i != 0) && (unaligned()) && (localByteOrder == ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*  85 */   private static final boolean bigEndian = (i != 0) && (localByteOrder == ByteOrder.BIG_ENDIAN);
/*     */ 
/*  74 */   private static final int byteArrayOfs = unsafe.arrayBaseOffset([B.class);
/*     */ 
/*     */   private static boolean unaligned()
/*     */   {
/*  94 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("os.arch", ""));
/*     */ 
/*  96 */     return (str.equals("i386")) || (str.equals("x86")) || (str.equals("amd64")) || (str.equals("x86_64"));
/*     */   }
/*     */ 
/*     */   static void b2iLittle(byte[] paramArrayOfByte, int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3)
/*     */   {
/* 104 */     if ((paramInt1 < 0) || (paramArrayOfByte.length - paramInt1 < paramInt3) || (paramInt2 < 0) || (paramArrayOfInt.length - paramInt2 < paramInt3 / 4))
/*     */     {
/* 106 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 108 */     if (littleEndianUnaligned) {
/* 109 */       paramInt1 += byteArrayOfs;
/* 110 */       paramInt3 += paramInt1;
/* 111 */       while (paramInt1 < paramInt3) {
/* 112 */         paramArrayOfInt[(paramInt2++)] = unsafe.getInt(paramArrayOfByte, paramInt1);
/* 113 */         paramInt1 += 4;
/*     */       }
/*     */     }
/* 115 */     if ((bigEndian) && ((paramInt1 & 0x3) == 0)) {
/* 116 */       paramInt1 += byteArrayOfs;
/* 117 */       paramInt3 += paramInt1;
/* 118 */     }while (paramInt1 < paramInt3) {
/* 119 */       paramArrayOfInt[(paramInt2++)] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt1));
/* 120 */       paramInt1 += 4; continue;
/*     */ 
/* 123 */       paramInt3 += paramInt1;
/* 124 */       while (paramInt1 < paramInt3) {
/* 125 */         paramArrayOfInt[(paramInt2++)] = (paramArrayOfByte[paramInt1] & 0xFF | (paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 16 | paramArrayOfByte[(paramInt1 + 3)] << 24);
/*     */ 
/* 129 */         paramInt1 += 4;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void b2iLittle64(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 136 */     if ((paramInt < 0) || (paramArrayOfByte.length - paramInt < 64) || (paramArrayOfInt.length < 16))
/*     */     {
/* 138 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 140 */     if (littleEndianUnaligned) {
/* 141 */       paramInt += byteArrayOfs;
/* 142 */       paramArrayOfInt[0] = unsafe.getInt(paramArrayOfByte, paramInt);
/* 143 */       paramArrayOfInt[1] = unsafe.getInt(paramArrayOfByte, paramInt + 4);
/* 144 */       paramArrayOfInt[2] = unsafe.getInt(paramArrayOfByte, paramInt + 8);
/* 145 */       paramArrayOfInt[3] = unsafe.getInt(paramArrayOfByte, paramInt + 12);
/* 146 */       paramArrayOfInt[4] = unsafe.getInt(paramArrayOfByte, paramInt + 16);
/* 147 */       paramArrayOfInt[5] = unsafe.getInt(paramArrayOfByte, paramInt + 20);
/* 148 */       paramArrayOfInt[6] = unsafe.getInt(paramArrayOfByte, paramInt + 24);
/* 149 */       paramArrayOfInt[7] = unsafe.getInt(paramArrayOfByte, paramInt + 28);
/* 150 */       paramArrayOfInt[8] = unsafe.getInt(paramArrayOfByte, paramInt + 32);
/* 151 */       paramArrayOfInt[9] = unsafe.getInt(paramArrayOfByte, paramInt + 36);
/* 152 */       paramArrayOfInt[10] = unsafe.getInt(paramArrayOfByte, paramInt + 40);
/* 153 */       paramArrayOfInt[11] = unsafe.getInt(paramArrayOfByte, paramInt + 44);
/* 154 */       paramArrayOfInt[12] = unsafe.getInt(paramArrayOfByte, paramInt + 48);
/* 155 */       paramArrayOfInt[13] = unsafe.getInt(paramArrayOfByte, paramInt + 52);
/* 156 */       paramArrayOfInt[14] = unsafe.getInt(paramArrayOfByte, paramInt + 56);
/* 157 */       paramArrayOfInt[15] = unsafe.getInt(paramArrayOfByte, paramInt + 60);
/* 158 */     } else if ((bigEndian) && ((paramInt & 0x3) == 0)) {
/* 159 */       paramInt += byteArrayOfs;
/* 160 */       paramArrayOfInt[0] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt));
/* 161 */       paramArrayOfInt[1] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 4));
/* 162 */       paramArrayOfInt[2] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 8));
/* 163 */       paramArrayOfInt[3] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 12));
/* 164 */       paramArrayOfInt[4] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 16));
/* 165 */       paramArrayOfInt[5] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 20));
/* 166 */       paramArrayOfInt[6] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 24));
/* 167 */       paramArrayOfInt[7] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 28));
/* 168 */       paramArrayOfInt[8] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 32));
/* 169 */       paramArrayOfInt[9] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 36));
/* 170 */       paramArrayOfInt[10] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 40));
/* 171 */       paramArrayOfInt[11] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 44));
/* 172 */       paramArrayOfInt[12] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 48));
/* 173 */       paramArrayOfInt[13] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 52));
/* 174 */       paramArrayOfInt[14] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 56));
/* 175 */       paramArrayOfInt[15] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 60));
/*     */     } else {
/* 177 */       b2iLittle(paramArrayOfByte, paramInt, paramArrayOfInt, 0, 64);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void i2bLittle(int[] paramArrayOfInt, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 185 */     if ((paramInt1 < 0) || (paramArrayOfInt.length - paramInt1 < paramInt3 / 4) || (paramInt2 < 0) || (paramArrayOfByte.length - paramInt2 < paramInt3))
/*     */     {
/* 187 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 189 */     if (littleEndianUnaligned) {
/* 190 */       paramInt2 += byteArrayOfs;
/* 191 */       paramInt3 += paramInt2;
/* 192 */       while (paramInt2 < paramInt3) {
/* 193 */         unsafe.putInt(paramArrayOfByte, paramInt2, paramArrayOfInt[(paramInt1++)]);
/* 194 */         paramInt2 += 4;
/*     */       }
/*     */     }
/* 196 */     if ((bigEndian) && ((paramInt2 & 0x3) == 0)) {
/* 197 */       paramInt2 += byteArrayOfs;
/* 198 */       paramInt3 += paramInt2;
/* 199 */     }while (paramInt2 < paramInt3) {
/* 200 */       unsafe.putInt(paramArrayOfByte, paramInt2, Integer.reverseBytes(paramArrayOfInt[(paramInt1++)]));
/* 201 */       paramInt2 += 4; continue;
/*     */ 
/* 204 */       paramInt3 += paramInt2;
/* 205 */       while (paramInt2 < paramInt3) {
/* 206 */         int i = paramArrayOfInt[(paramInt1++)];
/* 207 */         paramArrayOfByte[(paramInt2++)] = ((byte)i);
/* 208 */         paramArrayOfByte[(paramInt2++)] = ((byte)(i >> 8));
/* 209 */         paramArrayOfByte[(paramInt2++)] = ((byte)(i >> 16));
/* 210 */         paramArrayOfByte[(paramInt2++)] = ((byte)(i >> 24));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void i2bLittle4(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/* 217 */     if ((paramInt2 < 0) || (paramArrayOfByte.length - paramInt2 < 4)) {
/* 218 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 220 */     if (littleEndianUnaligned) {
/* 221 */       unsafe.putInt(paramArrayOfByte, byteArrayOfs + paramInt2, paramInt1);
/* 222 */     } else if ((bigEndian) && ((paramInt2 & 0x3) == 0)) {
/* 223 */       unsafe.putInt(paramArrayOfByte, byteArrayOfs + paramInt2, Integer.reverseBytes(paramInt1));
/*     */     } else {
/* 225 */       paramArrayOfByte[paramInt2] = ((byte)paramInt1);
/* 226 */       paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 8));
/* 227 */       paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >> 16));
/* 228 */       paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >> 24));
/*     */     }
/*     */   }
/*     */ 
/*     */   static void b2iBig(byte[] paramArrayOfByte, int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3)
/*     */   {
/* 236 */     if ((paramInt1 < 0) || (paramArrayOfByte.length - paramInt1 < paramInt3) || (paramInt2 < 0) || (paramArrayOfInt.length - paramInt2 < paramInt3 / 4))
/*     */     {
/* 238 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 240 */     if (littleEndianUnaligned) {
/* 241 */       paramInt1 += byteArrayOfs;
/* 242 */       paramInt3 += paramInt1;
/* 243 */       while (paramInt1 < paramInt3) {
/* 244 */         paramArrayOfInt[(paramInt2++)] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt1));
/* 245 */         paramInt1 += 4;
/*     */       }
/*     */     }
/* 247 */     if ((bigEndian) && ((paramInt1 & 0x3) == 0)) {
/* 248 */       paramInt1 += byteArrayOfs;
/* 249 */       paramInt3 += paramInt1;
/* 250 */     }while (paramInt1 < paramInt3) {
/* 251 */       paramArrayOfInt[(paramInt2++)] = unsafe.getInt(paramArrayOfByte, paramInt1);
/* 252 */       paramInt1 += 4; continue;
/*     */ 
/* 255 */       paramInt3 += paramInt1;
/* 256 */       while (paramInt1 < paramInt3) {
/* 257 */         paramArrayOfInt[(paramInt2++)] = (paramArrayOfByte[(paramInt1 + 3)] & 0xFF | (paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 16 | paramArrayOfByte[paramInt1] << 24);
/*     */ 
/* 261 */         paramInt1 += 4;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void b2iBig64(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 268 */     if ((paramInt < 0) || (paramArrayOfByte.length - paramInt < 64) || (paramArrayOfInt.length < 16))
/*     */     {
/* 270 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 272 */     if (littleEndianUnaligned) {
/* 273 */       paramInt += byteArrayOfs;
/* 274 */       paramArrayOfInt[0] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt));
/* 275 */       paramArrayOfInt[1] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 4));
/* 276 */       paramArrayOfInt[2] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 8));
/* 277 */       paramArrayOfInt[3] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 12));
/* 278 */       paramArrayOfInt[4] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 16));
/* 279 */       paramArrayOfInt[5] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 20));
/* 280 */       paramArrayOfInt[6] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 24));
/* 281 */       paramArrayOfInt[7] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 28));
/* 282 */       paramArrayOfInt[8] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 32));
/* 283 */       paramArrayOfInt[9] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 36));
/* 284 */       paramArrayOfInt[10] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 40));
/* 285 */       paramArrayOfInt[11] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 44));
/* 286 */       paramArrayOfInt[12] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 48));
/* 287 */       paramArrayOfInt[13] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 52));
/* 288 */       paramArrayOfInt[14] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 56));
/* 289 */       paramArrayOfInt[15] = Integer.reverseBytes(unsafe.getInt(paramArrayOfByte, paramInt + 60));
/* 290 */     } else if ((bigEndian) && ((paramInt & 0x3) == 0)) {
/* 291 */       paramInt += byteArrayOfs;
/* 292 */       paramArrayOfInt[0] = unsafe.getInt(paramArrayOfByte, paramInt);
/* 293 */       paramArrayOfInt[1] = unsafe.getInt(paramArrayOfByte, paramInt + 4);
/* 294 */       paramArrayOfInt[2] = unsafe.getInt(paramArrayOfByte, paramInt + 8);
/* 295 */       paramArrayOfInt[3] = unsafe.getInt(paramArrayOfByte, paramInt + 12);
/* 296 */       paramArrayOfInt[4] = unsafe.getInt(paramArrayOfByte, paramInt + 16);
/* 297 */       paramArrayOfInt[5] = unsafe.getInt(paramArrayOfByte, paramInt + 20);
/* 298 */       paramArrayOfInt[6] = unsafe.getInt(paramArrayOfByte, paramInt + 24);
/* 299 */       paramArrayOfInt[7] = unsafe.getInt(paramArrayOfByte, paramInt + 28);
/* 300 */       paramArrayOfInt[8] = unsafe.getInt(paramArrayOfByte, paramInt + 32);
/* 301 */       paramArrayOfInt[9] = unsafe.getInt(paramArrayOfByte, paramInt + 36);
/* 302 */       paramArrayOfInt[10] = unsafe.getInt(paramArrayOfByte, paramInt + 40);
/* 303 */       paramArrayOfInt[11] = unsafe.getInt(paramArrayOfByte, paramInt + 44);
/* 304 */       paramArrayOfInt[12] = unsafe.getInt(paramArrayOfByte, paramInt + 48);
/* 305 */       paramArrayOfInt[13] = unsafe.getInt(paramArrayOfByte, paramInt + 52);
/* 306 */       paramArrayOfInt[14] = unsafe.getInt(paramArrayOfByte, paramInt + 56);
/* 307 */       paramArrayOfInt[15] = unsafe.getInt(paramArrayOfByte, paramInt + 60);
/*     */     } else {
/* 309 */       b2iBig(paramArrayOfByte, paramInt, paramArrayOfInt, 0, 64);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void i2bBig(int[] paramArrayOfInt, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 317 */     if ((paramInt1 < 0) || (paramArrayOfInt.length - paramInt1 < paramInt3 / 4) || (paramInt2 < 0) || (paramArrayOfByte.length - paramInt2 < paramInt3))
/*     */     {
/* 319 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 321 */     if (littleEndianUnaligned) {
/* 322 */       paramInt2 += byteArrayOfs;
/* 323 */       paramInt3 += paramInt2;
/* 324 */       while (paramInt2 < paramInt3) {
/* 325 */         unsafe.putInt(paramArrayOfByte, paramInt2, Integer.reverseBytes(paramArrayOfInt[(paramInt1++)]));
/* 326 */         paramInt2 += 4;
/*     */       }
/*     */     }
/* 328 */     if ((bigEndian) && ((paramInt2 & 0x3) == 0)) {
/* 329 */       paramInt2 += byteArrayOfs;
/* 330 */       paramInt3 += paramInt2;
/* 331 */     }while (paramInt2 < paramInt3) {
/* 332 */       unsafe.putInt(paramArrayOfByte, paramInt2, paramArrayOfInt[(paramInt1++)]);
/* 333 */       paramInt2 += 4; continue;
/*     */ 
/* 336 */       paramInt3 += paramInt2;
/* 337 */       while (paramInt2 < paramInt3) {
/* 338 */         int i = paramArrayOfInt[(paramInt1++)];
/* 339 */         paramArrayOfByte[(paramInt2++)] = ((byte)(i >> 24));
/* 340 */         paramArrayOfByte[(paramInt2++)] = ((byte)(i >> 16));
/* 341 */         paramArrayOfByte[(paramInt2++)] = ((byte)(i >> 8));
/* 342 */         paramArrayOfByte[(paramInt2++)] = ((byte)i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void i2bBig4(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/* 349 */     if ((paramInt2 < 0) || (paramArrayOfByte.length - paramInt2 < 4)) {
/* 350 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 352 */     if (littleEndianUnaligned) {
/* 353 */       unsafe.putInt(paramArrayOfByte, byteArrayOfs + paramInt2, Integer.reverseBytes(paramInt1));
/* 354 */     } else if ((bigEndian) && ((paramInt2 & 0x3) == 0)) {
/* 355 */       unsafe.putInt(paramArrayOfByte, byteArrayOfs + paramInt2, paramInt1);
/*     */     } else {
/* 357 */       paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >> 24));
/* 358 */       paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 16));
/* 359 */       paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >> 8));
/* 360 */       paramArrayOfByte[(paramInt2 + 3)] = ((byte)paramInt1);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void b2lBig(byte[] paramArrayOfByte, int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3)
/*     */   {
/* 368 */     if ((paramInt1 < 0) || (paramArrayOfByte.length - paramInt1 < paramInt3) || (paramInt2 < 0) || (paramArrayOfLong.length - paramInt2 < paramInt3 / 8))
/*     */     {
/* 370 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 372 */     if (littleEndianUnaligned) {
/* 373 */       paramInt1 += byteArrayOfs;
/* 374 */       paramInt3 += paramInt1;
/* 375 */       while (paramInt1 < paramInt3) {
/* 376 */         paramArrayOfLong[(paramInt2++)] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt1));
/* 377 */         paramInt1 += 8;
/*     */       }
/*     */     }
/* 379 */     if ((bigEndian) && ((paramInt1 & 0x3) == 0))
/*     */     {
/* 385 */       paramInt1 += byteArrayOfs;
/* 386 */       paramInt3 += paramInt1;
/* 387 */     }while (paramInt1 < paramInt3) {
/* 388 */       paramArrayOfLong[(paramInt2++)] = (unsafe.getInt(paramArrayOfByte, paramInt1) << 32 | unsafe.getInt(paramArrayOfByte, paramInt1 + 4) & 0xFFFFFFFF);
/*     */ 
/* 391 */       paramInt1 += 8; continue;
/*     */ 
/* 394 */       paramInt3 += paramInt1;
/* 395 */       while (paramInt1 < paramInt3) {
/* 396 */         int i = paramArrayOfByte[(paramInt1 + 3)] & 0xFF | (paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 16 | paramArrayOfByte[paramInt1] << 24;
/*     */ 
/* 400 */         paramInt1 += 4;
/* 401 */         int j = paramArrayOfByte[(paramInt1 + 3)] & 0xFF | (paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 16 | paramArrayOfByte[paramInt1] << 24;
/*     */ 
/* 405 */         paramArrayOfLong[(paramInt2++)] = (i << 32 | j & 0xFFFFFFFF);
/* 406 */         paramInt1 += 4;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void b2lBig128(byte[] paramArrayOfByte, int paramInt, long[] paramArrayOfLong)
/*     */   {
/* 413 */     if ((paramInt < 0) || (paramArrayOfByte.length - paramInt < 128) || (paramArrayOfLong.length < 16))
/*     */     {
/* 415 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 417 */     if (littleEndianUnaligned) {
/* 418 */       paramInt += byteArrayOfs;
/* 419 */       paramArrayOfLong[0] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt));
/* 420 */       paramArrayOfLong[1] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 8));
/* 421 */       paramArrayOfLong[2] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 16));
/* 422 */       paramArrayOfLong[3] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 24));
/* 423 */       paramArrayOfLong[4] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 32));
/* 424 */       paramArrayOfLong[5] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 40));
/* 425 */       paramArrayOfLong[6] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 48));
/* 426 */       paramArrayOfLong[7] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 56));
/* 427 */       paramArrayOfLong[8] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 64));
/* 428 */       paramArrayOfLong[9] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 72));
/* 429 */       paramArrayOfLong[10] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 80));
/* 430 */       paramArrayOfLong[11] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 88));
/* 431 */       paramArrayOfLong[12] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 96));
/* 432 */       paramArrayOfLong[13] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 104));
/* 433 */       paramArrayOfLong[14] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 112));
/* 434 */       paramArrayOfLong[15] = Long.reverseBytes(unsafe.getLong(paramArrayOfByte, paramInt + 120));
/*     */     }
/*     */     else {
/* 437 */       b2lBig(paramArrayOfByte, paramInt, paramArrayOfLong, 0, 128);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void l2bBig(long[] paramArrayOfLong, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 445 */     if ((paramInt1 < 0) || (paramArrayOfLong.length - paramInt1 < paramInt3 / 8) || (paramInt2 < 0) || (paramArrayOfByte.length - paramInt2 < paramInt3))
/*     */     {
/* 447 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 449 */     paramInt3 += paramInt2;
/* 450 */     while (paramInt2 < paramInt3) {
/* 451 */       long l = paramArrayOfLong[(paramInt1++)];
/* 452 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)(l >> 56));
/* 453 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)(l >> 48));
/* 454 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)(l >> 40));
/* 455 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)(l >> 32));
/* 456 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)(l >> 24));
/* 457 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)(l >> 16));
/* 458 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)(l >> 8));
/* 459 */       paramArrayOfByte[(paramInt2++)] = ((byte)(int)l);
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  77 */     int i = (unsafe.arrayIndexScale([B.class) == 1) && (unsafe.arrayIndexScale([I.class) == 4) && (unsafe.arrayIndexScale([J.class) == 8) && ((byteArrayOfs & 0x3) == 0) ? 1 : 0;
/*     */ 
/*  82 */     ByteOrder localByteOrder = ByteOrder.nativeOrder();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.ByteArrayAccess
 * JD-Core Version:    0.6.2
 */