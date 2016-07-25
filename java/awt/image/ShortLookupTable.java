/*     */ package java.awt.image;
/*     */ 
/*     */ public class ShortLookupTable extends LookupTable
/*     */ {
/*     */   short[][] data;
/*     */ 
/*     */   public ShortLookupTable(int paramInt, short[][] paramArrayOfShort)
/*     */   {
/*  64 */     super(paramInt, paramArrayOfShort.length);
/*  65 */     this.numComponents = paramArrayOfShort.length;
/*  66 */     this.numEntries = paramArrayOfShort[0].length;
/*  67 */     this.data = new short[this.numComponents][];
/*     */ 
/*  69 */     for (int i = 0; i < this.numComponents; i++)
/*  70 */       this.data[i] = paramArrayOfShort[i];
/*     */   }
/*     */ 
/*     */   public ShortLookupTable(int paramInt, short[] paramArrayOfShort)
/*     */   {
/*  85 */     super(paramInt, paramArrayOfShort.length);
/*  86 */     this.numComponents = 1;
/*  87 */     this.numEntries = paramArrayOfShort.length;
/*  88 */     this.data = new short[1][];
/*  89 */     this.data[0] = paramArrayOfShort;
/*     */   }
/*     */ 
/*     */   public final short[][] getTable()
/*     */   {
/*  99 */     return this.data;
/*     */   }
/*     */ 
/*     */   public int[] lookupPixel(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 122 */     if (paramArrayOfInt2 == null)
/*     */     {
/* 124 */       paramArrayOfInt2 = new int[paramArrayOfInt1.length];
/*     */     }
/*     */     int i;
/*     */     int j;
/* 127 */     if (this.numComponents == 1)
/*     */     {
/* 129 */       for (i = 0; i < paramArrayOfInt1.length; i++) {
/* 130 */         j = (paramArrayOfInt1[i] & 0xFFFF) - this.offset;
/* 131 */         if (j < 0) {
/* 132 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 136 */         paramArrayOfInt2[i] = this.data[0][j];
/*     */       }
/*     */     }
/*     */     else {
/* 140 */       for (i = 0; i < paramArrayOfInt1.length; i++) {
/* 141 */         j = (paramArrayOfInt1[i] & 0xFFFF) - this.offset;
/* 142 */         if (j < 0) {
/* 143 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 147 */         paramArrayOfInt2[i] = this.data[i][j];
/*     */       }
/*     */     }
/* 150 */     return paramArrayOfInt2;
/*     */   }
/*     */ 
/*     */   public short[] lookupPixel(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*     */   {
/* 173 */     if (paramArrayOfShort2 == null)
/*     */     {
/* 175 */       paramArrayOfShort2 = new short[paramArrayOfShort1.length];
/*     */     }
/*     */     int i;
/*     */     int j;
/* 178 */     if (this.numComponents == 1)
/*     */     {
/* 180 */       for (i = 0; i < paramArrayOfShort1.length; i++) {
/* 181 */         j = (paramArrayOfShort1[i] & 0xFFFF) - this.offset;
/* 182 */         if (j < 0) {
/* 183 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 187 */         paramArrayOfShort2[i] = this.data[0][j];
/*     */       }
/*     */     }
/*     */     else {
/* 191 */       for (i = 0; i < paramArrayOfShort1.length; i++) {
/* 192 */         j = (paramArrayOfShort1[i] & 0xFFFF) - this.offset;
/* 193 */         if (j < 0) {
/* 194 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 198 */         paramArrayOfShort2[i] = this.data[i][j];
/*     */       }
/*     */     }
/* 201 */     return paramArrayOfShort2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ShortLookupTable
 * JD-Core Version:    0.6.2
 */