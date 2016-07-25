/*     */ package java.math;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ class BitSieve
/*     */ {
/*     */   private long[] bits;
/*     */   private int length;
/*  62 */   private static BitSieve smallSieve = new BitSieve();
/*     */ 
/*     */   private BitSieve()
/*     */   {
/*  76 */     this.length = 9600;
/*  77 */     this.bits = new long[unitIndex(this.length - 1) + 1];
/*     */ 
/*  80 */     set(0);
/*  81 */     int i = 1;
/*  82 */     int j = 3;
/*     */     do
/*     */     {
/*  86 */       sieveSingle(this.length, i + j, j);
/*  87 */       i = sieveSearch(this.length, i + 1);
/*  88 */       j = 2 * i + 1;
/*  89 */     }while ((i > 0) && (j < this.length));
/*     */   }
/*     */ 
/*     */   BitSieve(BigInteger paramBigInteger, int paramInt)
/*     */   {
/* 105 */     this.bits = new long[unitIndex(paramInt - 1) + 1];
/* 106 */     this.length = paramInt;
/* 107 */     int i = 0;
/*     */ 
/* 109 */     int j = smallSieve.sieveSearch(smallSieve.length, i);
/* 110 */     int k = j * 2 + 1;
/*     */ 
/* 113 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger(paramBigInteger);
/* 114 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger();
/*     */     do
/*     */     {
/* 117 */       i = localMutableBigInteger1.divideOneWord(k, localMutableBigInteger2);
/*     */ 
/* 120 */       i = k - i;
/* 121 */       if (i % 2 == 0)
/* 122 */         i += k;
/* 123 */       sieveSingle(paramInt, (i - 1) / 2, k);
/*     */ 
/* 126 */       j = smallSieve.sieveSearch(smallSieve.length, j + 1);
/* 127 */       k = j * 2 + 1;
/* 128 */     }while (j > 0);
/*     */   }
/*     */ 
/*     */   private static int unitIndex(int paramInt)
/*     */   {
/* 135 */     return paramInt >>> 6;
/*     */   }
/*     */ 
/*     */   private static long bit(int paramInt)
/*     */   {
/* 142 */     return 1L << (paramInt & 0x3F);
/*     */   }
/*     */ 
/*     */   private boolean get(int paramInt)
/*     */   {
/* 149 */     int i = unitIndex(paramInt);
/* 150 */     return (this.bits[i] & bit(paramInt)) != 0L;
/*     */   }
/*     */ 
/*     */   private void set(int paramInt)
/*     */   {
/* 157 */     int i = unitIndex(paramInt);
/* 158 */     this.bits[i] |= bit(paramInt);
/*     */   }
/*     */ 
/*     */   private int sieveSearch(int paramInt1, int paramInt2)
/*     */   {
/* 167 */     if (paramInt2 >= paramInt1) {
/* 168 */       return -1;
/*     */     }
/* 170 */     int i = paramInt2;
/*     */     do {
/* 172 */       if (!get(i))
/* 173 */         return i;
/* 174 */       i++;
/* 175 */     }while (i < paramInt1 - 1);
/* 176 */     return -1;
/*     */   }
/*     */ 
/*     */   private void sieveSingle(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 185 */     while (paramInt2 < paramInt1) {
/* 186 */       set(paramInt2);
/* 187 */       paramInt2 += paramInt3;
/*     */     }
/*     */   }
/*     */ 
/*     */   BigInteger retrieve(BigInteger paramBigInteger, int paramInt, Random paramRandom)
/*     */   {
/* 196 */     int i = 1;
/* 197 */     for (int j = 0; j < this.bits.length; j++) {
/* 198 */       long l = this.bits[j] ^ 0xFFFFFFFF;
/* 199 */       for (int k = 0; k < 64; k++) {
/* 200 */         if ((l & 1L) == 1L) {
/* 201 */           BigInteger localBigInteger = paramBigInteger.add(BigInteger.valueOf(i));
/*     */ 
/* 203 */           if (localBigInteger.primeToCertainty(paramInt, paramRandom))
/* 204 */             return localBigInteger;
/*     */         }
/* 206 */         l >>>= 1;
/* 207 */         i += 2;
/*     */       }
/*     */     }
/* 210 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.math.BitSieve
 * JD-Core Version:    0.6.2
 */