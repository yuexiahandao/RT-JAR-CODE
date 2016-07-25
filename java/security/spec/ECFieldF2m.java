/*     */ package java.security.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ECFieldF2m
/*     */   implements ECField
/*     */ {
/*     */   private int m;
/*     */   private int[] ks;
/*     */   private BigInteger rp;
/*     */ 
/*     */   public ECFieldF2m(int paramInt)
/*     */   {
/*  54 */     if (paramInt <= 0) {
/*  55 */       throw new IllegalArgumentException("m is not positive");
/*     */     }
/*  57 */     this.m = paramInt;
/*  58 */     this.ks = null;
/*  59 */     this.rp = null;
/*     */   }
/*     */ 
/*     */   public ECFieldF2m(int paramInt, BigInteger paramBigInteger)
/*     */   {
/*  86 */     this.m = paramInt;
/*  87 */     this.rp = paramBigInteger;
/*  88 */     if (paramInt <= 0) {
/*  89 */       throw new IllegalArgumentException("m is not positive");
/*     */     }
/*  91 */     int i = this.rp.bitCount();
/*  92 */     if ((!this.rp.testBit(0)) || (!this.rp.testBit(paramInt)) || ((i != 3) && (i != 5)))
/*     */     {
/*  94 */       throw new IllegalArgumentException("rp does not represent a valid reduction polynomial");
/*     */     }
/*     */ 
/*  98 */     BigInteger localBigInteger = this.rp.clearBit(0).clearBit(paramInt);
/*  99 */     this.ks = new int[i - 2];
/* 100 */     for (int j = this.ks.length - 1; j >= 0; j--) {
/* 101 */       int k = localBigInteger.getLowestSetBit();
/* 102 */       this.ks[j] = k;
/* 103 */       localBigInteger = localBigInteger.clearBit(k);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ECFieldF2m(int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 135 */     this.m = paramInt;
/* 136 */     this.ks = ((int[])paramArrayOfInt.clone());
/* 137 */     if (paramInt <= 0) {
/* 138 */       throw new IllegalArgumentException("m is not positive");
/*     */     }
/* 140 */     if ((this.ks.length != 1) && (this.ks.length != 3)) {
/* 141 */       throw new IllegalArgumentException("length of ks is neither 1 nor 3");
/*     */     }
/*     */ 
/* 144 */     for (int i = 0; i < this.ks.length; i++) {
/* 145 */       if ((this.ks[i] < 1) || (this.ks[i] > paramInt - 1)) {
/* 146 */         throw new IllegalArgumentException("ks[" + i + "] is out of range");
/*     */       }
/*     */ 
/* 149 */       if ((i != 0) && (this.ks[i] >= this.ks[(i - 1)])) {
/* 150 */         throw new IllegalArgumentException("values in ks are not in descending order");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 155 */     this.rp = BigInteger.ONE;
/* 156 */     this.rp = this.rp.setBit(paramInt);
/* 157 */     for (i = 0; i < this.ks.length; i++)
/* 158 */       this.rp = this.rp.setBit(this.ks[i]);
/*     */   }
/*     */ 
/*     */   public int getFieldSize()
/*     */   {
/* 168 */     return this.m;
/*     */   }
/*     */ 
/*     */   public int getM()
/*     */   {
/* 178 */     return this.m;
/*     */   }
/*     */ 
/*     */   public BigInteger getReductionPolynomial()
/*     */   {
/* 190 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public int[] getMidTermsOfReductionPolynomial()
/*     */   {
/* 203 */     if (this.ks == null) {
/* 204 */       return null;
/*     */     }
/* 206 */     return (int[])this.ks.clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 219 */     if (this == paramObject) return true;
/* 220 */     if ((paramObject instanceof ECFieldF2m))
/*     */     {
/* 223 */       return (this.m == ((ECFieldF2m)paramObject).m) && (Arrays.equals(this.ks, ((ECFieldF2m)paramObject).ks));
/*     */     }
/*     */ 
/* 226 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 235 */     int i = this.m << 5;
/* 236 */     i += (this.rp == null ? 0 : this.rp.hashCode());
/*     */ 
/* 239 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.ECFieldF2m
 * JD-Core Version:    0.6.2
 */