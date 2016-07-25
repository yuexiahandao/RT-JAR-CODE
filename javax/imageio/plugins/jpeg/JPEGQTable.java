/*     */ package javax.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class JPEGQTable
/*     */ {
/*  44 */   private static final int[] k1 = { 16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58, 60, 55, 14, 13, 16, 24, 40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 24, 35, 55, 64, 81, 104, 113, 92, 49, 64, 78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 112, 100, 103, 99 };
/*     */ 
/*  55 */   private static final int[] k1div2 = { 8, 6, 5, 8, 12, 20, 26, 31, 6, 6, 7, 10, 13, 29, 30, 28, 7, 7, 8, 12, 20, 29, 35, 28, 7, 9, 11, 15, 26, 44, 40, 31, 9, 11, 19, 28, 34, 55, 52, 39, 12, 18, 28, 32, 41, 52, 57, 46, 25, 32, 39, 44, 52, 61, 60, 51, 36, 46, 48, 49, 56, 50, 52, 50 };
/*     */ 
/*  66 */   private static final int[] k2 = { 17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 26, 66, 99, 99, 99, 99, 24, 26, 56, 99, 99, 99, 99, 99, 47, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
/*     */ 
/*  77 */   private static final int[] k2div2 = { 9, 9, 12, 24, 50, 50, 50, 50, 9, 11, 13, 33, 50, 50, 50, 50, 12, 13, 28, 50, 50, 50, 50, 50, 24, 33, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
/*     */ 
/*  95 */   public static final JPEGQTable K1Luminance = new JPEGQTable(k1, false);
/*     */ 
/* 107 */   public static final JPEGQTable K1Div2Luminance = new JPEGQTable(k1div2, false);
/*     */ 
/* 115 */   public static final JPEGQTable K2Chrominance = new JPEGQTable(k2, false);
/*     */ 
/* 127 */   public static final JPEGQTable K2Div2Chrominance = new JPEGQTable(k2div2, false);
/*     */   private int[] qTable;
/*     */ 
/*     */   private JPEGQTable(int[] paramArrayOfInt, boolean paramBoolean)
/*     */   {
/* 133 */     this.qTable = (paramBoolean ? Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length) : paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public JPEGQTable(int[] paramArrayOfInt)
/*     */   {
/* 145 */     if (paramArrayOfInt == null) {
/* 146 */       throw new IllegalArgumentException("table must not be null.");
/*     */     }
/* 148 */     if (paramArrayOfInt.length != 64) {
/* 149 */       throw new IllegalArgumentException("table.length != 64");
/*     */     }
/* 151 */     this.qTable = Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length);
/*     */   }
/*     */ 
/*     */   public int[] getTable()
/*     */   {
/* 160 */     return Arrays.copyOf(this.qTable, this.qTable.length);
/*     */   }
/*     */ 
/*     */   public JPEGQTable getScaledInstance(float paramFloat, boolean paramBoolean)
/*     */   {
/* 179 */     int i = paramBoolean ? 255 : 32767;
/* 180 */     int[] arrayOfInt = new int[this.qTable.length];
/* 181 */     for (int j = 0; j < this.qTable.length; j++) {
/* 182 */       int k = (int)(this.qTable[j] * paramFloat + 0.5F);
/* 183 */       if (k < 1) {
/* 184 */         k = 1;
/*     */       }
/* 186 */       if (k > i) {
/* 187 */         k = i;
/*     */       }
/* 189 */       arrayOfInt[j] = k;
/*     */     }
/* 191 */     return new JPEGQTable(arrayOfInt);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 199 */     String str = System.getProperty("line.separator", "\n");
/* 200 */     StringBuilder localStringBuilder = new StringBuilder("JPEGQTable:" + str);
/* 201 */     for (int i = 0; i < this.qTable.length; i++) {
/* 202 */       if (i % 8 == 0) {
/* 203 */         localStringBuilder.append('\t');
/*     */       }
/* 205 */       localStringBuilder.append(this.qTable[i]);
/* 206 */       localStringBuilder.append(i % 8 == 7 ? str : Character.valueOf(' '));
/*     */     }
/* 208 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.plugins.jpeg.JPEGQTable
 * JD-Core Version:    0.6.2
 */