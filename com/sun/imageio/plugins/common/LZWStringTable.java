/*     */ package com.sun.imageio.plugins.common;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class LZWStringTable
/*     */ {
/*     */   private static final int RES_CODES = 2;
/*     */   private static final short HASH_FREE = -1;
/*     */   private static final short NEXT_FIRST = -1;
/*     */   private static final int MAXBITS = 12;
/*     */   private static final int MAXSTR = 4096;
/*     */   private static final short HASHSIZE = 9973;
/*     */   private static final short HASHSTEP = 2039;
/*     */   byte[] strChr;
/*     */   short[] strNxt;
/*     */   short[] strHsh;
/*     */   short numStrings;
/*     */   int[] strLen;
/*     */ 
/*     */   public LZWStringTable()
/*     */   {
/*  66 */     this.strChr = new byte[4096];
/*  67 */     this.strNxt = new short[4096];
/*  68 */     this.strLen = new int[4096];
/*  69 */     this.strHsh = new short[9973];
/*     */   }
/*     */ 
/*     */   public int addCharString(short paramShort, byte paramByte)
/*     */   {
/*  82 */     if (this.numStrings >= 4096) {
/*  83 */       return 65535;
/*     */     }
/*     */ 
/*  86 */     int i = hash(paramShort, paramByte);
/*  87 */     while (this.strHsh[i] != -1) {
/*  88 */       i = (i + 2039) % 9973;
/*     */     }
/*     */ 
/*  91 */     this.strHsh[i] = this.numStrings;
/*  92 */     this.strChr[this.numStrings] = paramByte;
/*  93 */     if (paramShort == -1) {
/*  94 */       this.strNxt[this.numStrings] = -1;
/*  95 */       this.strLen[this.numStrings] = 1;
/*     */     } else {
/*  97 */       this.strNxt[this.numStrings] = paramShort;
/*  98 */       this.strLen[this.numStrings] = (this.strLen[paramShort] + 1);
/*     */     }
/*     */ 
/* 101 */     return this.numStrings++;
/*     */   }
/*     */ 
/*     */   public short findCharString(short paramShort, byte paramByte)
/*     */   {
/* 113 */     if (paramShort == -1) {
/* 114 */       return (short)(paramByte & 0xFF);
/*     */     }
/*     */ 
/* 117 */     int i = hash(paramShort, paramByte);
/*     */     int j;
/* 118 */     while ((j = this.strHsh[i]) != -1) {
/* 119 */       if ((this.strNxt[j] == paramShort) && (this.strChr[j] == paramByte)) {
/* 120 */         return (short)j;
/*     */       }
/* 122 */       i = (i + 2039) % 9973;
/*     */     }
/*     */ 
/* 125 */     return -1;
/*     */   }
/*     */ 
/*     */   public void clearTable(int paramInt)
/*     */   {
/* 133 */     this.numStrings = 0;
/*     */ 
/* 135 */     for (int i = 0; i < 9973; i++) {
/* 136 */       this.strHsh[i] = -1;
/*     */     }
/*     */ 
/* 139 */     i = (1 << paramInt) + 2;
/* 140 */     for (int j = 0; j < i; j++)
/* 141 */       addCharString((short)-1, (byte)j);
/*     */   }
/*     */ 
/*     */   public static int hash(short paramShort, byte paramByte)
/*     */   {
/* 146 */     return (((short)(paramByte << 8) ^ paramShort) & 0xFFFF) % 9973;
/*     */   }
/*     */ 
/*     */   public int expandCode(byte[] paramArrayOfByte, int paramInt1, short paramShort, int paramInt2)
/*     */   {
/* 170 */     if ((paramInt1 == -2) && 
/* 171 */       (paramInt2 == 1)) {
/* 172 */       paramInt2 = 0;
/*     */     }
/*     */ 
/* 175 */     if ((paramShort == -1) || (paramInt2 == this.strLen[paramShort]))
/*     */     {
/* 178 */       return 0;
/*     */     }
/*     */ 
/* 182 */     int j = this.strLen[paramShort] - paramInt2;
/* 183 */     int k = paramArrayOfByte.length - paramInt1;
/*     */     int i;
/* 184 */     if (k > j)
/* 185 */       i = j;
/*     */     else {
/* 187 */       i = k;
/*     */     }
/*     */ 
/* 190 */     int m = j - i;
/*     */ 
/* 192 */     int n = paramInt1 + i;
/*     */ 
/* 196 */     while ((n > paramInt1) && (paramShort != -1)) {
/* 197 */       m--; if (m < 0) {
/* 198 */         paramArrayOfByte[(--n)] = this.strChr[paramShort];
/*     */       }
/* 200 */       paramShort = this.strNxt[paramShort];
/*     */     }
/*     */ 
/* 203 */     if (j > i) {
/* 204 */       return -i;
/*     */     }
/* 206 */     return i;
/*     */   }
/*     */ 
/*     */   public void dump(PrintStream paramPrintStream)
/*     */   {
/* 212 */     for (int i = 258; i < this.numStrings; i++)
/* 213 */       paramPrintStream.println(" strNxt[" + i + "] = " + this.strNxt[i] + " strChr " + Integer.toHexString(this.strChr[i] & 0xFF) + " strLen " + Integer.toHexString(this.strLen[i]));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.LZWStringTable
 * JD-Core Version:    0.6.2
 */