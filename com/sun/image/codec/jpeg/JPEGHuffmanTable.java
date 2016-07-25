/*     */ package com.sun.image.codec.jpeg;
/*     */ 
/*     */ public class JPEGHuffmanTable
/*     */ {
/*     */   private static final int HUFF_MAX_LEN = 17;
/*     */   private static final int HUFF_MAX_SYM = 256;
/*     */   private short[] lengths;
/*     */   private short[] symbols;
/*  48 */   public static final JPEGHuffmanTable StdDCLuminance = new JPEGHuffmanTable();
/*     */   public static final JPEGHuffmanTable StdDCChrominance;
/*     */   public static final JPEGHuffmanTable StdACLuminance;
/*     */   public static final JPEGHuffmanTable StdACChrominance;
/*     */ 
/*     */   private JPEGHuffmanTable()
/*     */   {
/* 149 */     this.lengths = null;
/* 150 */     this.symbols = null;
/*     */   }
/*     */ 
/*     */   public JPEGHuffmanTable(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*     */   {
/* 166 */     if (paramArrayOfShort1.length > 17)
/* 167 */       throw new IllegalArgumentException("lengths array is too long");
/* 168 */     for (int i = 1; i < paramArrayOfShort1.length; i++) {
/* 169 */       if (paramArrayOfShort1[i] < 0) {
/* 170 */         throw new IllegalArgumentException("Values in lengths array must be non-negative.");
/*     */       }
/*     */     }
/*     */ 
/* 174 */     if (paramArrayOfShort2.length > 256)
/* 175 */       throw new IllegalArgumentException("symbols array is too long");
/* 176 */     for (i = 0; i < paramArrayOfShort2.length; i++) {
/* 177 */       if (paramArrayOfShort2[i] < 0) {
/* 178 */         throw new IllegalArgumentException("Values in symbols array must be non-negative.");
/*     */       }
/*     */     }
/* 181 */     this.lengths = new short[paramArrayOfShort1.length];
/* 182 */     this.symbols = new short[paramArrayOfShort2.length];
/*     */ 
/* 184 */     System.arraycopy(paramArrayOfShort1, 0, this.lengths, 0, paramArrayOfShort1.length);
/* 185 */     System.arraycopy(paramArrayOfShort2, 0, this.symbols, 0, paramArrayOfShort2.length);
/*     */ 
/* 187 */     checkTable();
/*     */   }
/*     */ 
/*     */   private void checkTable()
/*     */   {
/* 198 */     int i = 2;
/* 199 */     int j = 0;
/* 200 */     for (int k = 1; k < this.lengths.length; k++) {
/* 201 */       j += this.lengths[k];
/* 202 */       i -= this.lengths[k];
/* 203 */       i *= 2;
/*     */     }
/*     */ 
/* 209 */     if (i < 0) {
/* 210 */       throw new IllegalArgumentException("Invalid Huffman Table provided, lengths are incorrect.");
/*     */     }
/*     */ 
/* 214 */     if (j > this.symbols.length)
/* 215 */       throw new IllegalArgumentException("Invalid Huffman Table provided, not enough symbols.");
/*     */   }
/*     */ 
/*     */   public short[] getLengths()
/*     */   {
/* 226 */     short[] arrayOfShort = new short[this.lengths.length];
/* 227 */     System.arraycopy(this.lengths, 0, arrayOfShort, 0, this.lengths.length);
/* 228 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   public short[] getSymbols()
/*     */   {
/* 238 */     short[] arrayOfShort = new short[this.symbols.length];
/* 239 */     System.arraycopy(this.symbols, 0, arrayOfShort, 0, this.symbols.length);
/* 240 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  52 */     short[] arrayOfShort1 = { 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 };
/*     */ 
/*  54 */     short[] arrayOfShort2 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
/*     */ 
/*  57 */     StdDCLuminance.lengths = arrayOfShort1;
/*  58 */     StdDCLuminance.symbols = arrayOfShort2;
/*  59 */     StdDCLuminance.checkTable();
/*     */ 
/*  64 */     StdDCChrominance = new JPEGHuffmanTable();
/*     */ 
/*  67 */     arrayOfShort1 = new short[] { 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 };
/*     */ 
/*  69 */     arrayOfShort2 = new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
/*     */ 
/*  72 */     StdDCChrominance.lengths = arrayOfShort1;
/*  73 */     StdDCChrominance.symbols = arrayOfShort2;
/*  74 */     StdDCChrominance.checkTable();
/*     */ 
/*  78 */     StdACLuminance = new JPEGHuffmanTable();
/*     */ 
/*  81 */     arrayOfShort1 = new short[] { 0, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125 };
/*     */ 
/*  83 */     arrayOfShort2 = new short[] { 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, 129, 145, 161, 8, 35, 66, 177, 193, 21, 82, 209, 240, 36, 51, 98, 114, 130, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 131, 132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 217, 218, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250 };
/*     */ 
/* 106 */     StdACLuminance.lengths = arrayOfShort1;
/* 107 */     StdACLuminance.symbols = arrayOfShort2;
/* 108 */     StdACLuminance.checkTable();
/*     */ 
/* 112 */     StdACChrominance = new JPEGHuffmanTable();
/*     */ 
/* 115 */     arrayOfShort1 = new short[] { 0, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119 };
/*     */ 
/* 117 */     arrayOfShort2 = new short[] { 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, 129, 8, 20, 66, 145, 161, 177, 193, 9, 35, 51, 82, 240, 21, 98, 114, 209, 10, 22, 36, 52, 225, 37, 241, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 130, 131, 132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 217, 218, 226, 227, 228, 229, 230, 231, 232, 233, 234, 242, 243, 244, 245, 246, 247, 248, 249, 250 };
/*     */ 
/* 140 */     StdACChrominance.lengths = arrayOfShort1;
/* 141 */     StdACChrominance.symbols = arrayOfShort2;
/* 142 */     StdACChrominance.checkTable();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.image.codec.jpeg.JPEGHuffmanTable
 * JD-Core Version:    0.6.2
 */