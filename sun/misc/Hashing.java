/*     */ package sun.misc;
/*     */ 
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ 
/*     */ public class Hashing
/*     */ {
/*     */   private Hashing()
/*     */   {
/*  40 */     throw new Error("No instances");
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(byte[] paramArrayOfByte) {
/*  44 */     return murmur3_32(0, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(int paramInt, byte[] paramArrayOfByte) {
/*  48 */     return murmur3_32(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/*  53 */     int i = paramInt1;
/*  54 */     int j = paramInt3;
/*     */     int k;
/*  57 */     while (j >= 4) {
/*  58 */       k = paramArrayOfByte[paramInt2] & 0xFF | (paramArrayOfByte[(paramInt2 + 1)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt2 + 2)] & 0xFF) << 16 | paramArrayOfByte[(paramInt2 + 3)] << 24;
/*     */ 
/*  63 */       j -= 4;
/*  64 */       paramInt2 += 4;
/*     */ 
/*  66 */       k *= -862048943;
/*  67 */       k = Integer.rotateLeft(k, 15);
/*  68 */       k *= 461845907;
/*     */ 
/*  70 */       i ^= k;
/*  71 */       i = Integer.rotateLeft(i, 13);
/*  72 */       i = i * 5 + -430675100;
/*     */     }
/*     */ 
/*  77 */     if (j > 0) {
/*  78 */       k = 0;
/*     */ 
/*  80 */       switch (j) {
/*     */       case 3:
/*  82 */         k ^= (paramArrayOfByte[(paramInt2 + 2)] & 0xFF) << 16;
/*     */       case 2:
/*  85 */         k ^= (paramArrayOfByte[(paramInt2 + 1)] & 0xFF) << 8;
/*     */       case 1:
/*  88 */         k ^= paramArrayOfByte[paramInt2] & 0xFF;
/*     */       }
/*     */ 
/*  91 */       k *= -862048943;
/*  92 */       k = Integer.rotateLeft(k, 15);
/*  93 */       k *= 461845907;
/*  94 */       i ^= k;
/*     */     }
/*     */ 
/* 100 */     i ^= paramInt3;
/*     */ 
/* 103 */     i ^= i >>> 16;
/* 104 */     i *= -2048144789;
/* 105 */     i ^= i >>> 13;
/* 106 */     i *= -1028477387;
/* 107 */     i ^= i >>> 16;
/*     */ 
/* 109 */     return i;
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(char[] paramArrayOfChar) {
/* 113 */     return murmur3_32(0, paramArrayOfChar, 0, paramArrayOfChar.length);
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(int paramInt, char[] paramArrayOfChar) {
/* 117 */     return murmur3_32(paramInt, paramArrayOfChar, 0, paramArrayOfChar.length);
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3) {
/* 121 */     int i = paramInt1;
/*     */ 
/* 123 */     int j = paramInt2;
/* 124 */     int k = paramInt3;
/*     */     int m;
/* 127 */     while (k >= 2) {
/* 128 */       m = paramArrayOfChar[(j++)] & 0xFFFF | paramArrayOfChar[(j++)] << '\020';
/*     */ 
/* 130 */       k -= 2;
/*     */ 
/* 132 */       m *= -862048943;
/* 133 */       m = Integer.rotateLeft(m, 15);
/* 134 */       m *= 461845907;
/*     */ 
/* 136 */       i ^= m;
/* 137 */       i = Integer.rotateLeft(i, 13);
/* 138 */       i = i * 5 + -430675100;
/*     */     }
/*     */ 
/* 143 */     if (k > 0) {
/* 144 */       m = paramArrayOfChar[j];
/*     */ 
/* 146 */       m *= -862048943;
/* 147 */       m = Integer.rotateLeft(m, 15);
/* 148 */       m *= 461845907;
/* 149 */       i ^= m;
/*     */     }
/*     */ 
/* 154 */     i ^= paramInt3 * 2;
/*     */ 
/* 157 */     i ^= i >>> 16;
/* 158 */     i *= -2048144789;
/* 159 */     i ^= i >>> 13;
/* 160 */     i *= -1028477387;
/* 161 */     i ^= i >>> 16;
/*     */ 
/* 163 */     return i;
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(int[] paramArrayOfInt) {
/* 167 */     return murmur3_32(0, paramArrayOfInt, 0, paramArrayOfInt.length);
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(int paramInt, int[] paramArrayOfInt) {
/* 171 */     return murmur3_32(paramInt, paramArrayOfInt, 0, paramArrayOfInt.length);
/*     */   }
/*     */ 
/*     */   public static int murmur3_32(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
/* 175 */     int i = paramInt1;
/*     */ 
/* 177 */     int j = paramInt2;
/* 178 */     int k = paramInt2 + paramInt3;
/*     */ 
/* 181 */     while (j < k) {
/* 182 */       int m = paramArrayOfInt[(j++)];
/*     */ 
/* 184 */       m *= -862048943;
/* 185 */       m = Integer.rotateLeft(m, 15);
/* 186 */       m *= 461845907;
/*     */ 
/* 188 */       i ^= m;
/* 189 */       i = Integer.rotateLeft(i, 13);
/* 190 */       i = i * 5 + -430675100;
/*     */     }
/*     */ 
/* 197 */     i ^= paramInt3 * 4;
/*     */ 
/* 200 */     i ^= i >>> 16;
/* 201 */     i *= -2048144789;
/* 202 */     i ^= i >>> 13;
/* 203 */     i *= -1028477387;
/* 204 */     i ^= i >>> 16;
/*     */ 
/* 206 */     return i;
/*     */   }
/*     */ 
/*     */   public static int stringHash32(String paramString)
/*     */   {
/* 236 */     return Holder.LANG_ACCESS.getStringHash32(paramString);
/*     */   }
/*     */ 
/*     */   public static int randomHashSeed(Object paramObject)
/*     */   {
/*     */     int i;
/* 248 */     if (VM.isBooted()) {
/* 249 */       i = ThreadLocalRandom.current().nextInt();
/*     */     }
/*     */     else
/*     */     {
/* 253 */       int[] arrayOfInt = { System.identityHashCode(Hashing.class), System.identityHashCode(paramObject), System.identityHashCode(Thread.currentThread()), (int)Thread.currentThread().getId(), (int)(System.currentTimeMillis() >>> 2), (int)(System.nanoTime() >>> 5), (int)(Runtime.getRuntime().freeMemory() >>> 4) };
/*     */ 
/* 263 */       i = murmur3_32(arrayOfInt);
/*     */     }
/*     */ 
/* 267 */     return 0 != i ? i : 1;
/*     */   }
/*     */ 
/*     */   private static class Holder
/*     */   {
/* 221 */     static final JavaLangAccess LANG_ACCESS = SharedSecrets.getJavaLangAccess();
/*     */ 
/* 222 */     static { if (null == LANG_ACCESS)
/* 223 */         throw new Error("Shared secrets not initialized");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Hashing
 * JD-Core Version:    0.6.2
 */