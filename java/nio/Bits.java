/*     */ package java.nio;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import sun.misc.JavaNioAccess;
/*     */ import sun.misc.JavaNioAccess.BufferPool;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.misc.VM;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class Bits
/*     */ {
/*     */   private static final Unsafe unsafe;
/*     */   private static final ByteOrder byteOrder;
/*     */   private static int pageSize;
/*     */   private static boolean unaligned;
/*     */   private static boolean unalignedKnown;
/*     */   private static volatile long maxMemory;
/*     */   private static volatile long reservedMemory;
/*     */   private static volatile long totalCapacity;
/*     */   private static volatile long count;
/*     */   private static boolean memoryLimitSet;
/*     */   static final int JNI_COPY_TO_ARRAY_THRESHOLD = 6;
/*     */   static final int JNI_COPY_FROM_ARRAY_THRESHOLD = 6;
/*     */   static final long UNSAFE_COPY_THRESHOLD = 1048576L;
/*     */ 
/*     */   static short swap(short paramShort)
/*     */   {
/*  44 */     return Short.reverseBytes(paramShort);
/*     */   }
/*     */ 
/*     */   static char swap(char paramChar) {
/*  48 */     return Character.reverseBytes(paramChar);
/*     */   }
/*     */ 
/*     */   static int swap(int paramInt) {
/*  52 */     return Integer.reverseBytes(paramInt);
/*     */   }
/*     */ 
/*     */   static long swap(long paramLong) {
/*  56 */     return Long.reverseBytes(paramLong);
/*     */   }
/*     */ 
/*     */   private static char makeChar(byte paramByte1, byte paramByte2)
/*     */   {
/*  63 */     return (char)(paramByte1 << 8 | paramByte2 & 0xFF);
/*     */   }
/*     */ 
/*     */   static char getCharL(ByteBuffer paramByteBuffer, int paramInt) {
/*  67 */     return makeChar(paramByteBuffer._get(paramInt + 1), paramByteBuffer._get(paramInt));
/*     */   }
/*     */ 
/*     */   static char getCharL(long paramLong)
/*     */   {
/*  72 */     return makeChar(_get(paramLong + 1L), _get(paramLong));
/*     */   }
/*     */ 
/*     */   static char getCharB(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/*  77 */     return makeChar(paramByteBuffer._get(paramInt), paramByteBuffer._get(paramInt + 1));
/*     */   }
/*     */ 
/*     */   static char getCharB(long paramLong)
/*     */   {
/*  82 */     return makeChar(_get(paramLong), _get(paramLong + 1L));
/*     */   }
/*     */ 
/*     */   static char getChar(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean)
/*     */   {
/*  87 */     return paramBoolean ? getCharB(paramByteBuffer, paramInt) : getCharL(paramByteBuffer, paramInt);
/*     */   }
/*     */ 
/*     */   static char getChar(long paramLong, boolean paramBoolean) {
/*  91 */     return paramBoolean ? getCharB(paramLong) : getCharL(paramLong);
/*     */   }
/*     */   private static byte char1(char paramChar) {
/*  94 */     return (byte)(paramChar >> '\b'); } 
/*  95 */   private static byte char0(char paramChar) { return (byte)paramChar; }
/*     */ 
/*     */   static void putCharL(ByteBuffer paramByteBuffer, int paramInt, char paramChar) {
/*  98 */     paramByteBuffer._put(paramInt, char0(paramChar));
/*  99 */     paramByteBuffer._put(paramInt + 1, char1(paramChar));
/*     */   }
/*     */ 
/*     */   static void putCharL(long paramLong, char paramChar) {
/* 103 */     _put(paramLong, char0(paramChar));
/* 104 */     _put(paramLong + 1L, char1(paramChar));
/*     */   }
/*     */ 
/*     */   static void putCharB(ByteBuffer paramByteBuffer, int paramInt, char paramChar) {
/* 108 */     paramByteBuffer._put(paramInt, char1(paramChar));
/* 109 */     paramByteBuffer._put(paramInt + 1, char0(paramChar));
/*     */   }
/*     */ 
/*     */   static void putCharB(long paramLong, char paramChar) {
/* 113 */     _put(paramLong, char1(paramChar));
/* 114 */     _put(paramLong + 1L, char0(paramChar));
/*     */   }
/*     */ 
/*     */   static void putChar(ByteBuffer paramByteBuffer, int paramInt, char paramChar, boolean paramBoolean) {
/* 118 */     if (paramBoolean)
/* 119 */       putCharB(paramByteBuffer, paramInt, paramChar);
/*     */     else
/* 121 */       putCharL(paramByteBuffer, paramInt, paramChar);
/*     */   }
/*     */ 
/*     */   static void putChar(long paramLong, char paramChar, boolean paramBoolean) {
/* 125 */     if (paramBoolean)
/* 126 */       putCharB(paramLong, paramChar);
/*     */     else
/* 128 */       putCharL(paramLong, paramChar);
/*     */   }
/*     */ 
/*     */   private static short makeShort(byte paramByte1, byte paramByte2)
/*     */   {
/* 135 */     return (short)(paramByte1 << 8 | paramByte2 & 0xFF);
/*     */   }
/*     */ 
/*     */   static short getShortL(ByteBuffer paramByteBuffer, int paramInt) {
/* 139 */     return makeShort(paramByteBuffer._get(paramInt + 1), paramByteBuffer._get(paramInt));
/*     */   }
/*     */ 
/*     */   static short getShortL(long paramLong)
/*     */   {
/* 144 */     return makeShort(_get(paramLong + 1L), _get(paramLong));
/*     */   }
/*     */ 
/*     */   static short getShortB(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 149 */     return makeShort(paramByteBuffer._get(paramInt), paramByteBuffer._get(paramInt + 1));
/*     */   }
/*     */ 
/*     */   static short getShortB(long paramLong)
/*     */   {
/* 154 */     return makeShort(_get(paramLong), _get(paramLong + 1L));
/*     */   }
/*     */ 
/*     */   static short getShort(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean)
/*     */   {
/* 159 */     return paramBoolean ? getShortB(paramByteBuffer, paramInt) : getShortL(paramByteBuffer, paramInt);
/*     */   }
/*     */ 
/*     */   static short getShort(long paramLong, boolean paramBoolean) {
/* 163 */     return paramBoolean ? getShortB(paramLong) : getShortL(paramLong);
/*     */   }
/*     */   private static byte short1(short paramShort) {
/* 166 */     return (byte)(paramShort >> 8); } 
/* 167 */   private static byte short0(short paramShort) { return (byte)paramShort; }
/*     */ 
/*     */   static void putShortL(ByteBuffer paramByteBuffer, int paramInt, short paramShort) {
/* 170 */     paramByteBuffer._put(paramInt, short0(paramShort));
/* 171 */     paramByteBuffer._put(paramInt + 1, short1(paramShort));
/*     */   }
/*     */ 
/*     */   static void putShortL(long paramLong, short paramShort) {
/* 175 */     _put(paramLong, short0(paramShort));
/* 176 */     _put(paramLong + 1L, short1(paramShort));
/*     */   }
/*     */ 
/*     */   static void putShortB(ByteBuffer paramByteBuffer, int paramInt, short paramShort) {
/* 180 */     paramByteBuffer._put(paramInt, short1(paramShort));
/* 181 */     paramByteBuffer._put(paramInt + 1, short0(paramShort));
/*     */   }
/*     */ 
/*     */   static void putShortB(long paramLong, short paramShort) {
/* 185 */     _put(paramLong, short1(paramShort));
/* 186 */     _put(paramLong + 1L, short0(paramShort));
/*     */   }
/*     */ 
/*     */   static void putShort(ByteBuffer paramByteBuffer, int paramInt, short paramShort, boolean paramBoolean) {
/* 190 */     if (paramBoolean)
/* 191 */       putShortB(paramByteBuffer, paramInt, paramShort);
/*     */     else
/* 193 */       putShortL(paramByteBuffer, paramInt, paramShort);
/*     */   }
/*     */ 
/*     */   static void putShort(long paramLong, short paramShort, boolean paramBoolean) {
/* 197 */     if (paramBoolean)
/* 198 */       putShortB(paramLong, paramShort);
/*     */     else
/* 200 */       putShortL(paramLong, paramShort);
/*     */   }
/*     */ 
/*     */   private static int makeInt(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
/*     */   {
/* 207 */     return paramByte1 << 24 | (paramByte2 & 0xFF) << 16 | (paramByte3 & 0xFF) << 8 | paramByte4 & 0xFF;
/*     */   }
/*     */ 
/*     */   static int getIntL(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 214 */     return makeInt(paramByteBuffer._get(paramInt + 3), paramByteBuffer._get(paramInt + 2), paramByteBuffer._get(paramInt + 1), paramByteBuffer._get(paramInt));
/*     */   }
/*     */ 
/*     */   static int getIntL(long paramLong)
/*     */   {
/* 221 */     return makeInt(_get(paramLong + 3L), _get(paramLong + 2L), _get(paramLong + 1L), _get(paramLong));
/*     */   }
/*     */ 
/*     */   static int getIntB(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 228 */     return makeInt(paramByteBuffer._get(paramInt), paramByteBuffer._get(paramInt + 1), paramByteBuffer._get(paramInt + 2), paramByteBuffer._get(paramInt + 3));
/*     */   }
/*     */ 
/*     */   static int getIntB(long paramLong)
/*     */   {
/* 235 */     return makeInt(_get(paramLong), _get(paramLong + 1L), _get(paramLong + 2L), _get(paramLong + 3L));
/*     */   }
/*     */ 
/*     */   static int getInt(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean)
/*     */   {
/* 242 */     return paramBoolean ? getIntB(paramByteBuffer, paramInt) : getIntL(paramByteBuffer, paramInt);
/*     */   }
/*     */ 
/*     */   static int getInt(long paramLong, boolean paramBoolean) {
/* 246 */     return paramBoolean ? getIntB(paramLong) : getIntL(paramLong);
/*     */   }
/*     */   private static byte int3(int paramInt) {
/* 249 */     return (byte)(paramInt >> 24); } 
/* 250 */   private static byte int2(int paramInt) { return (byte)(paramInt >> 16); } 
/* 251 */   private static byte int1(int paramInt) { return (byte)(paramInt >> 8); } 
/* 252 */   private static byte int0(int paramInt) { return (byte)paramInt; }
/*     */ 
/*     */   static void putIntL(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
/* 255 */     paramByteBuffer._put(paramInt1 + 3, int3(paramInt2));
/* 256 */     paramByteBuffer._put(paramInt1 + 2, int2(paramInt2));
/* 257 */     paramByteBuffer._put(paramInt1 + 1, int1(paramInt2));
/* 258 */     paramByteBuffer._put(paramInt1, int0(paramInt2));
/*     */   }
/*     */ 
/*     */   static void putIntL(long paramLong, int paramInt) {
/* 262 */     _put(paramLong + 3L, int3(paramInt));
/* 263 */     _put(paramLong + 2L, int2(paramInt));
/* 264 */     _put(paramLong + 1L, int1(paramInt));
/* 265 */     _put(paramLong, int0(paramInt));
/*     */   }
/*     */ 
/*     */   static void putIntB(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
/* 269 */     paramByteBuffer._put(paramInt1, int3(paramInt2));
/* 270 */     paramByteBuffer._put(paramInt1 + 1, int2(paramInt2));
/* 271 */     paramByteBuffer._put(paramInt1 + 2, int1(paramInt2));
/* 272 */     paramByteBuffer._put(paramInt1 + 3, int0(paramInt2));
/*     */   }
/*     */ 
/*     */   static void putIntB(long paramLong, int paramInt) {
/* 276 */     _put(paramLong, int3(paramInt));
/* 277 */     _put(paramLong + 1L, int2(paramInt));
/* 278 */     _put(paramLong + 2L, int1(paramInt));
/* 279 */     _put(paramLong + 3L, int0(paramInt));
/*     */   }
/*     */ 
/*     */   static void putInt(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 283 */     if (paramBoolean)
/* 284 */       putIntB(paramByteBuffer, paramInt1, paramInt2);
/*     */     else
/* 286 */       putIntL(paramByteBuffer, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   static void putInt(long paramLong, int paramInt, boolean paramBoolean) {
/* 290 */     if (paramBoolean)
/* 291 */       putIntB(paramLong, paramInt);
/*     */     else
/* 293 */       putIntL(paramLong, paramInt);
/*     */   }
/*     */ 
/*     */   private static long makeLong(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte paramByte6, byte paramByte7, byte paramByte8)
/*     */   {
/* 302 */     return paramByte1 << 56 | (paramByte2 & 0xFF) << 48 | (paramByte3 & 0xFF) << 40 | (paramByte4 & 0xFF) << 32 | (paramByte5 & 0xFF) << 24 | (paramByte6 & 0xFF) << 16 | (paramByte7 & 0xFF) << 8 | paramByte8 & 0xFF;
/*     */   }
/*     */ 
/*     */   static long getLongL(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 313 */     return makeLong(paramByteBuffer._get(paramInt + 7), paramByteBuffer._get(paramInt + 6), paramByteBuffer._get(paramInt + 5), paramByteBuffer._get(paramInt + 4), paramByteBuffer._get(paramInt + 3), paramByteBuffer._get(paramInt + 2), paramByteBuffer._get(paramInt + 1), paramByteBuffer._get(paramInt));
/*     */   }
/*     */ 
/*     */   static long getLongL(long paramLong)
/*     */   {
/* 324 */     return makeLong(_get(paramLong + 7L), _get(paramLong + 6L), _get(paramLong + 5L), _get(paramLong + 4L), _get(paramLong + 3L), _get(paramLong + 2L), _get(paramLong + 1L), _get(paramLong));
/*     */   }
/*     */ 
/*     */   static long getLongB(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 335 */     return makeLong(paramByteBuffer._get(paramInt), paramByteBuffer._get(paramInt + 1), paramByteBuffer._get(paramInt + 2), paramByteBuffer._get(paramInt + 3), paramByteBuffer._get(paramInt + 4), paramByteBuffer._get(paramInt + 5), paramByteBuffer._get(paramInt + 6), paramByteBuffer._get(paramInt + 7));
/*     */   }
/*     */ 
/*     */   static long getLongB(long paramLong)
/*     */   {
/* 346 */     return makeLong(_get(paramLong), _get(paramLong + 1L), _get(paramLong + 2L), _get(paramLong + 3L), _get(paramLong + 4L), _get(paramLong + 5L), _get(paramLong + 6L), _get(paramLong + 7L));
/*     */   }
/*     */ 
/*     */   static long getLong(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean)
/*     */   {
/* 357 */     return paramBoolean ? getLongB(paramByteBuffer, paramInt) : getLongL(paramByteBuffer, paramInt);
/*     */   }
/*     */ 
/*     */   static long getLong(long paramLong, boolean paramBoolean) {
/* 361 */     return paramBoolean ? getLongB(paramLong) : getLongL(paramLong);
/*     */   }
/*     */   private static byte long7(long paramLong) {
/* 364 */     return (byte)(int)(paramLong >> 56); } 
/* 365 */   private static byte long6(long paramLong) { return (byte)(int)(paramLong >> 48); } 
/* 366 */   private static byte long5(long paramLong) { return (byte)(int)(paramLong >> 40); } 
/* 367 */   private static byte long4(long paramLong) { return (byte)(int)(paramLong >> 32); } 
/* 368 */   private static byte long3(long paramLong) { return (byte)(int)(paramLong >> 24); } 
/* 369 */   private static byte long2(long paramLong) { return (byte)(int)(paramLong >> 16); } 
/* 370 */   private static byte long1(long paramLong) { return (byte)(int)(paramLong >> 8); } 
/* 371 */   private static byte long0(long paramLong) { return (byte)(int)paramLong; }
/*     */ 
/*     */   static void putLongL(ByteBuffer paramByteBuffer, int paramInt, long paramLong) {
/* 374 */     paramByteBuffer._put(paramInt + 7, long7(paramLong));
/* 375 */     paramByteBuffer._put(paramInt + 6, long6(paramLong));
/* 376 */     paramByteBuffer._put(paramInt + 5, long5(paramLong));
/* 377 */     paramByteBuffer._put(paramInt + 4, long4(paramLong));
/* 378 */     paramByteBuffer._put(paramInt + 3, long3(paramLong));
/* 379 */     paramByteBuffer._put(paramInt + 2, long2(paramLong));
/* 380 */     paramByteBuffer._put(paramInt + 1, long1(paramLong));
/* 381 */     paramByteBuffer._put(paramInt, long0(paramLong));
/*     */   }
/*     */ 
/*     */   static void putLongL(long paramLong1, long paramLong2) {
/* 385 */     _put(paramLong1 + 7L, long7(paramLong2));
/* 386 */     _put(paramLong1 + 6L, long6(paramLong2));
/* 387 */     _put(paramLong1 + 5L, long5(paramLong2));
/* 388 */     _put(paramLong1 + 4L, long4(paramLong2));
/* 389 */     _put(paramLong1 + 3L, long3(paramLong2));
/* 390 */     _put(paramLong1 + 2L, long2(paramLong2));
/* 391 */     _put(paramLong1 + 1L, long1(paramLong2));
/* 392 */     _put(paramLong1, long0(paramLong2));
/*     */   }
/*     */ 
/*     */   static void putLongB(ByteBuffer paramByteBuffer, int paramInt, long paramLong) {
/* 396 */     paramByteBuffer._put(paramInt, long7(paramLong));
/* 397 */     paramByteBuffer._put(paramInt + 1, long6(paramLong));
/* 398 */     paramByteBuffer._put(paramInt + 2, long5(paramLong));
/* 399 */     paramByteBuffer._put(paramInt + 3, long4(paramLong));
/* 400 */     paramByteBuffer._put(paramInt + 4, long3(paramLong));
/* 401 */     paramByteBuffer._put(paramInt + 5, long2(paramLong));
/* 402 */     paramByteBuffer._put(paramInt + 6, long1(paramLong));
/* 403 */     paramByteBuffer._put(paramInt + 7, long0(paramLong));
/*     */   }
/*     */ 
/*     */   static void putLongB(long paramLong1, long paramLong2) {
/* 407 */     _put(paramLong1, long7(paramLong2));
/* 408 */     _put(paramLong1 + 1L, long6(paramLong2));
/* 409 */     _put(paramLong1 + 2L, long5(paramLong2));
/* 410 */     _put(paramLong1 + 3L, long4(paramLong2));
/* 411 */     _put(paramLong1 + 4L, long3(paramLong2));
/* 412 */     _put(paramLong1 + 5L, long2(paramLong2));
/* 413 */     _put(paramLong1 + 6L, long1(paramLong2));
/* 414 */     _put(paramLong1 + 7L, long0(paramLong2));
/*     */   }
/*     */ 
/*     */   static void putLong(ByteBuffer paramByteBuffer, int paramInt, long paramLong, boolean paramBoolean) {
/* 418 */     if (paramBoolean)
/* 419 */       putLongB(paramByteBuffer, paramInt, paramLong);
/*     */     else
/* 421 */       putLongL(paramByteBuffer, paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   static void putLong(long paramLong1, long paramLong2, boolean paramBoolean) {
/* 425 */     if (paramBoolean)
/* 426 */       putLongB(paramLong1, paramLong2);
/*     */     else
/* 428 */       putLongL(paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   static float getFloatL(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 435 */     return Float.intBitsToFloat(getIntL(paramByteBuffer, paramInt));
/*     */   }
/*     */ 
/*     */   static float getFloatL(long paramLong) {
/* 439 */     return Float.intBitsToFloat(getIntL(paramLong));
/*     */   }
/*     */ 
/*     */   static float getFloatB(ByteBuffer paramByteBuffer, int paramInt) {
/* 443 */     return Float.intBitsToFloat(getIntB(paramByteBuffer, paramInt));
/*     */   }
/*     */ 
/*     */   static float getFloatB(long paramLong) {
/* 447 */     return Float.intBitsToFloat(getIntB(paramLong));
/*     */   }
/*     */ 
/*     */   static float getFloat(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean) {
/* 451 */     return paramBoolean ? getFloatB(paramByteBuffer, paramInt) : getFloatL(paramByteBuffer, paramInt);
/*     */   }
/*     */ 
/*     */   static float getFloat(long paramLong, boolean paramBoolean) {
/* 455 */     return paramBoolean ? getFloatB(paramLong) : getFloatL(paramLong);
/*     */   }
/*     */ 
/*     */   static void putFloatL(ByteBuffer paramByteBuffer, int paramInt, float paramFloat) {
/* 459 */     putIntL(paramByteBuffer, paramInt, Float.floatToRawIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   static void putFloatL(long paramLong, float paramFloat) {
/* 463 */     putIntL(paramLong, Float.floatToRawIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   static void putFloatB(ByteBuffer paramByteBuffer, int paramInt, float paramFloat) {
/* 467 */     putIntB(paramByteBuffer, paramInt, Float.floatToRawIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   static void putFloatB(long paramLong, float paramFloat) {
/* 471 */     putIntB(paramLong, Float.floatToRawIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   static void putFloat(ByteBuffer paramByteBuffer, int paramInt, float paramFloat, boolean paramBoolean) {
/* 475 */     if (paramBoolean)
/* 476 */       putFloatB(paramByteBuffer, paramInt, paramFloat);
/*     */     else
/* 478 */       putFloatL(paramByteBuffer, paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   static void putFloat(long paramLong, float paramFloat, boolean paramBoolean) {
/* 482 */     if (paramBoolean)
/* 483 */       putFloatB(paramLong, paramFloat);
/*     */     else
/* 485 */       putFloatL(paramLong, paramFloat);
/*     */   }
/*     */ 
/*     */   static double getDoubleL(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 492 */     return Double.longBitsToDouble(getLongL(paramByteBuffer, paramInt));
/*     */   }
/*     */ 
/*     */   static double getDoubleL(long paramLong) {
/* 496 */     return Double.longBitsToDouble(getLongL(paramLong));
/*     */   }
/*     */ 
/*     */   static double getDoubleB(ByteBuffer paramByteBuffer, int paramInt) {
/* 500 */     return Double.longBitsToDouble(getLongB(paramByteBuffer, paramInt));
/*     */   }
/*     */ 
/*     */   static double getDoubleB(long paramLong) {
/* 504 */     return Double.longBitsToDouble(getLongB(paramLong));
/*     */   }
/*     */ 
/*     */   static double getDouble(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean) {
/* 508 */     return paramBoolean ? getDoubleB(paramByteBuffer, paramInt) : getDoubleL(paramByteBuffer, paramInt);
/*     */   }
/*     */ 
/*     */   static double getDouble(long paramLong, boolean paramBoolean) {
/* 512 */     return paramBoolean ? getDoubleB(paramLong) : getDoubleL(paramLong);
/*     */   }
/*     */ 
/*     */   static void putDoubleL(ByteBuffer paramByteBuffer, int paramInt, double paramDouble) {
/* 516 */     putLongL(paramByteBuffer, paramInt, Double.doubleToRawLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   static void putDoubleL(long paramLong, double paramDouble) {
/* 520 */     putLongL(paramLong, Double.doubleToRawLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   static void putDoubleB(ByteBuffer paramByteBuffer, int paramInt, double paramDouble) {
/* 524 */     putLongB(paramByteBuffer, paramInt, Double.doubleToRawLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   static void putDoubleB(long paramLong, double paramDouble) {
/* 528 */     putLongB(paramLong, Double.doubleToRawLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   static void putDouble(ByteBuffer paramByteBuffer, int paramInt, double paramDouble, boolean paramBoolean) {
/* 532 */     if (paramBoolean)
/* 533 */       putDoubleB(paramByteBuffer, paramInt, paramDouble);
/*     */     else
/* 535 */       putDoubleL(paramByteBuffer, paramInt, paramDouble);
/*     */   }
/*     */ 
/*     */   static void putDouble(long paramLong, double paramDouble, boolean paramBoolean) {
/* 539 */     if (paramBoolean)
/* 540 */       putDoubleB(paramLong, paramDouble);
/*     */     else
/* 542 */       putDoubleL(paramLong, paramDouble);
/*     */   }
/*     */ 
/*     */   private static byte _get(long paramLong)
/*     */   {
/* 551 */     return unsafe.getByte(paramLong);
/*     */   }
/*     */ 
/*     */   private static void _put(long paramLong, byte paramByte) {
/* 555 */     unsafe.putByte(paramLong, paramByte);
/*     */   }
/*     */ 
/*     */   static Unsafe unsafe() {
/* 559 */     return unsafe;
/*     */   }
/*     */ 
/*     */   static ByteOrder byteOrder()
/*     */   {
/* 568 */     if (byteOrder == null)
/* 569 */       throw new Error("Unknown byte order");
/* 570 */     return byteOrder;
/*     */   }
/*     */ 
/*     */   static int pageSize()
/*     */   {
/* 594 */     if (pageSize == -1)
/* 595 */       pageSize = unsafe().pageSize();
/* 596 */     return pageSize;
/*     */   }
/*     */ 
/*     */   static int pageCount(long paramLong) {
/* 600 */     return (int)(paramLong + pageSize() - 1L) / pageSize();
/*     */   }
/*     */ 
/*     */   static boolean unaligned()
/*     */   {
/* 607 */     if (unalignedKnown)
/* 608 */       return unaligned;
/* 609 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("os.arch"));
/*     */ 
/* 611 */     unaligned = (str.equals("i386")) || (str.equals("x86")) || (str.equals("amd64")) || (str.equals("x86_64"));
/*     */ 
/* 613 */     unalignedKnown = true;
/* 614 */     return unaligned;
/*     */   }
/*     */ 
/*     */   static void reserveMemory(long paramLong, int paramInt)
/*     */   {
/* 633 */     synchronized (Bits.class) {
/* 634 */       if ((!memoryLimitSet) && (VM.isBooted())) {
/* 635 */         maxMemory = VM.maxDirectMemory();
/* 636 */         memoryLimitSet = true;
/*     */       }
/*     */ 
/* 641 */       if (paramInt <= maxMemory - totalCapacity) {
/* 642 */         reservedMemory += paramLong;
/* 643 */         totalCapacity += paramInt;
/* 644 */         count += 1L;
/* 645 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 649 */     System.gc();
/*     */     try {
/* 651 */       Thread.sleep(100L);
/*     */     }
/*     */     catch (InterruptedException ) {
/* 654 */       Thread.currentThread().interrupt();
/*     */     }
/* 656 */     synchronized (Bits.class) {
/* 657 */       if (totalCapacity + paramInt > maxMemory)
/* 658 */         throw new OutOfMemoryError("Direct buffer memory");
/* 659 */       reservedMemory += paramLong;
/* 660 */       totalCapacity += paramInt;
/* 661 */       count += 1L;
/*     */     }
/*     */   }
/*     */ 
/*     */   static synchronized void unreserveMemory(long paramLong, int paramInt)
/*     */   {
/* 667 */     if (reservedMemory > 0L) {
/* 668 */       reservedMemory -= paramLong;
/* 669 */       totalCapacity -= paramInt;
/* 670 */       count -= 1L;
/* 671 */       assert (reservedMemory > -1L);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void copyFromArray(Object paramObject, long paramLong1, long paramLong2, long paramLong3, long paramLong4)
/*     */   {
/* 747 */     long l1 = paramLong1 + paramLong2;
/* 748 */     while (paramLong4 > 0L) {
/* 749 */       long l2 = paramLong4 > 1048576L ? 1048576L : paramLong4;
/* 750 */       unsafe.copyMemory(paramObject, l1, null, paramLong3, l2);
/* 751 */       paramLong4 -= l2;
/* 752 */       l1 += l2;
/* 753 */       paramLong3 += l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void copyToArray(long paramLong1, Object paramObject, long paramLong2, long paramLong3, long paramLong4)
/*     */   {
/* 774 */     long l1 = paramLong2 + paramLong3;
/* 775 */     while (paramLong4 > 0L) {
/* 776 */       long l2 = paramLong4 > 1048576L ? 1048576L : paramLong4;
/* 777 */       unsafe.copyMemory(null, paramLong1, paramObject, l1, l2);
/* 778 */       paramLong4 -= l2;
/* 779 */       paramLong1 += l2;
/* 780 */       l1 += l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void copyFromCharArray(Object paramObject, long paramLong1, long paramLong2, long paramLong3)
/*     */   {
/* 787 */     copyFromShortArray(paramObject, paramLong1, paramLong2, paramLong3);
/*     */   }
/*     */ 
/*     */   static void copyToCharArray(long paramLong1, Object paramObject, long paramLong2, long paramLong3)
/*     */   {
/* 793 */     copyToShortArray(paramLong1, paramObject, paramLong2, paramLong3);
/*     */   }
/*     */ 
/*     */   static native void copyFromShortArray(Object paramObject, long paramLong1, long paramLong2, long paramLong3);
/*     */ 
/*     */   static native void copyToShortArray(long paramLong1, Object paramObject, long paramLong2, long paramLong3);
/*     */ 
/*     */   static native void copyFromIntArray(Object paramObject, long paramLong1, long paramLong2, long paramLong3);
/*     */ 
/*     */   static native void copyToIntArray(long paramLong1, Object paramObject, long paramLong2, long paramLong3);
/*     */ 
/*     */   static native void copyFromLongArray(Object paramObject, long paramLong1, long paramLong2, long paramLong3);
/*     */ 
/*     */   static native void copyToLongArray(long paramLong1, Object paramObject, long paramLong2, long paramLong3);
/*     */ 
/*     */   static
/*     */   {
/* 548 */     unsafe = Unsafe.getUnsafe();
/*     */ 
/* 574 */     long l = unsafe.allocateMemory(8L);
/*     */     try {
/* 576 */       unsafe.putLong(l, 72623859790382856L);
/* 577 */       int i = unsafe.getByte(l);
/* 578 */       switch (i) { case 1:
/* 579 */         byteOrder = ByteOrder.BIG_ENDIAN; break;
/*     */       case 8:
/* 580 */         byteOrder = ByteOrder.LITTLE_ENDIAN; break;
/*     */       default:
/* 582 */         if (!$assertionsDisabled) throw new AssertionError();
/* 583 */         byteOrder = null; }
/*     */     }
/*     */     finally {
/* 586 */       unsafe.freeMemory(l);
/*     */     }
/*     */ 
/* 591 */     pageSize = -1;
/*     */ 
/* 604 */     unalignedKnown = false;
/*     */ 
/* 623 */     maxMemory = VM.maxDirectMemory();
/*     */ 
/* 627 */     memoryLimitSet = false;
/*     */ 
/* 679 */     SharedSecrets.setJavaNioAccess(new JavaNioAccess()
/*     */     {
/*     */       public JavaNioAccess.BufferPool getDirectBufferPool()
/*     */       {
/* 683 */         return new JavaNioAccess.BufferPool()
/*     */         {
/*     */           public String getName() {
/* 686 */             return "direct";
/*     */           }
/*     */ 
/*     */           public long getCount() {
/* 690 */             return Bits.count;
/*     */           }
/*     */ 
/*     */           public long getTotalCapacity() {
/* 694 */             return Bits.totalCapacity;
/*     */           }
/*     */ 
/*     */           public long getMemoryUsed() {
/* 698 */             return Bits.reservedMemory;
/*     */           }
/*     */         };
/*     */       }
/*     */ 
/*     */       public ByteBuffer newDirectByteBuffer(long paramAnonymousLong, int paramAnonymousInt, Object paramAnonymousObject) {
/* 704 */         return new DirectByteBuffer(paramAnonymousLong, paramAnonymousInt, paramAnonymousObject);
/*     */       }
/*     */ 
/*     */       public void truncate(Buffer paramAnonymousBuffer) {
/* 708 */         paramAnonymousBuffer.truncate();
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.Bits
 * JD-Core Version:    0.6.2
 */