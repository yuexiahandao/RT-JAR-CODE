/*     */ package java.io;
/*     */ 
/*     */ class Bits
/*     */ {
/*     */   static boolean getBoolean(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  40 */     return paramArrayOfByte[paramInt] != 0;
/*     */   }
/*     */ 
/*     */   static char getChar(byte[] paramArrayOfByte, int paramInt) {
/*  44 */     return (char)((paramArrayOfByte[(paramInt + 1)] & 0xFF) + (paramArrayOfByte[paramInt] << 8));
/*     */   }
/*     */ 
/*     */   static short getShort(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  49 */     return (short)((paramArrayOfByte[(paramInt + 1)] & 0xFF) + (paramArrayOfByte[paramInt] << 8));
/*     */   }
/*     */ 
/*     */   static int getInt(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  54 */     return (paramArrayOfByte[(paramInt + 3)] & 0xFF) + ((paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8) + ((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16) + (paramArrayOfByte[paramInt] << 24);
/*     */   }
/*     */ 
/*     */   static float getFloat(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  61 */     return Float.intBitsToFloat(getInt(paramArrayOfByte, paramInt));
/*     */   }
/*     */ 
/*     */   static long getLong(byte[] paramArrayOfByte, int paramInt) {
/*  65 */     return (paramArrayOfByte[(paramInt + 7)] & 0xFF) + ((paramArrayOfByte[(paramInt + 6)] & 0xFF) << 8) + ((paramArrayOfByte[(paramInt + 5)] & 0xFF) << 16) + ((paramArrayOfByte[(paramInt + 4)] & 0xFF) << 24) + ((paramArrayOfByte[(paramInt + 3)] & 0xFF) << 32) + ((paramArrayOfByte[(paramInt + 2)] & 0xFF) << 40) + ((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 48) + (paramArrayOfByte[paramInt] << 56);
/*     */   }
/*     */ 
/*     */   static double getDouble(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  76 */     return Double.longBitsToDouble(getLong(paramArrayOfByte, paramInt));
/*     */   }
/*     */ 
/*     */   static void putBoolean(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
/*     */   {
/*  85 */     paramArrayOfByte[paramInt] = ((byte)(paramBoolean ? 1 : 0));
/*     */   }
/*     */ 
/*     */   static void putChar(byte[] paramArrayOfByte, int paramInt, char paramChar) {
/*  89 */     paramArrayOfByte[(paramInt + 1)] = ((byte)paramChar);
/*  90 */     paramArrayOfByte[paramInt] = ((byte)(paramChar >>> '\b'));
/*     */   }
/*     */ 
/*     */   static void putShort(byte[] paramArrayOfByte, int paramInt, short paramShort) {
/*  94 */     paramArrayOfByte[(paramInt + 1)] = ((byte)paramShort);
/*  95 */     paramArrayOfByte[paramInt] = ((byte)(paramShort >>> 8));
/*     */   }
/*     */ 
/*     */   static void putInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*  99 */     paramArrayOfByte[(paramInt1 + 3)] = ((byte)paramInt2);
/* 100 */     paramArrayOfByte[(paramInt1 + 2)] = ((byte)(paramInt2 >>> 8));
/* 101 */     paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt2 >>> 16));
/* 102 */     paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >>> 24));
/*     */   }
/*     */ 
/*     */   static void putFloat(byte[] paramArrayOfByte, int paramInt, float paramFloat) {
/* 106 */     putInt(paramArrayOfByte, paramInt, Float.floatToIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   static void putLong(byte[] paramArrayOfByte, int paramInt, long paramLong) {
/* 110 */     paramArrayOfByte[(paramInt + 7)] = ((byte)(int)paramLong);
/* 111 */     paramArrayOfByte[(paramInt + 6)] = ((byte)(int)(paramLong >>> 8));
/* 112 */     paramArrayOfByte[(paramInt + 5)] = ((byte)(int)(paramLong >>> 16));
/* 113 */     paramArrayOfByte[(paramInt + 4)] = ((byte)(int)(paramLong >>> 24));
/* 114 */     paramArrayOfByte[(paramInt + 3)] = ((byte)(int)(paramLong >>> 32));
/* 115 */     paramArrayOfByte[(paramInt + 2)] = ((byte)(int)(paramLong >>> 40));
/* 116 */     paramArrayOfByte[(paramInt + 1)] = ((byte)(int)(paramLong >>> 48));
/* 117 */     paramArrayOfByte[paramInt] = ((byte)(int)(paramLong >>> 56));
/*     */   }
/*     */ 
/*     */   static void putDouble(byte[] paramArrayOfByte, int paramInt, double paramDouble) {
/* 121 */     putLong(paramArrayOfByte, paramInt, Double.doubleToLongBits(paramDouble));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.Bits
 * JD-Core Version:    0.6.2
 */