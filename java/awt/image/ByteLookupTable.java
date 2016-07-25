/*     */ package java.awt.image;
/*     */ 
/*     */ public class ByteLookupTable extends LookupTable
/*     */ {
/*     */   byte[][] data;
/*     */ 
/*     */   public ByteLookupTable(int paramInt, byte[][] paramArrayOfByte)
/*     */   {
/*  67 */     super(paramInt, paramArrayOfByte.length);
/*  68 */     this.numComponents = paramArrayOfByte.length;
/*  69 */     this.numEntries = paramArrayOfByte[0].length;
/*  70 */     this.data = new byte[this.numComponents][];
/*     */ 
/*  72 */     for (int i = 0; i < this.numComponents; i++)
/*  73 */       this.data[i] = paramArrayOfByte[i];
/*     */   }
/*     */ 
/*     */   public ByteLookupTable(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/*  91 */     super(paramInt, paramArrayOfByte.length);
/*  92 */     this.numComponents = 1;
/*  93 */     this.numEntries = paramArrayOfByte.length;
/*  94 */     this.data = new byte[1][];
/*  95 */     this.data[0] = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public final byte[][] getTable()
/*     */   {
/* 105 */     return this.data;
/*     */   }
/*     */ 
/*     */   public int[] lookupPixel(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 128 */     if (paramArrayOfInt2 == null)
/*     */     {
/* 130 */       paramArrayOfInt2 = new int[paramArrayOfInt1.length];
/*     */     }
/*     */     int i;
/*     */     int j;
/* 133 */     if (this.numComponents == 1)
/*     */     {
/* 135 */       for (i = 0; i < paramArrayOfInt1.length; i++) {
/* 136 */         j = paramArrayOfInt1[i] - this.offset;
/* 137 */         if (j < 0) {
/* 138 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 142 */         paramArrayOfInt2[i] = this.data[0][j];
/*     */       }
/*     */     }
/*     */     else {
/* 146 */       for (i = 0; i < paramArrayOfInt1.length; i++) {
/* 147 */         j = paramArrayOfInt1[i] - this.offset;
/* 148 */         if (j < 0) {
/* 149 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 153 */         paramArrayOfInt2[i] = this.data[i][j];
/*     */       }
/*     */     }
/* 156 */     return paramArrayOfInt2;
/*     */   }
/*     */ 
/*     */   public byte[] lookupPixel(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 179 */     if (paramArrayOfByte2 == null)
/*     */     {
/* 181 */       paramArrayOfByte2 = new byte[paramArrayOfByte1.length];
/*     */     }
/*     */     int i;
/*     */     int j;
/* 184 */     if (this.numComponents == 1)
/*     */     {
/* 186 */       for (i = 0; i < paramArrayOfByte1.length; i++) {
/* 187 */         j = (paramArrayOfByte1[i] & 0xFF) - this.offset;
/* 188 */         if (j < 0) {
/* 189 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 193 */         paramArrayOfByte2[i] = this.data[0][j];
/*     */       }
/*     */     }
/*     */     else {
/* 197 */       for (i = 0; i < paramArrayOfByte1.length; i++) {
/* 198 */         j = (paramArrayOfByte1[i] & 0xFF) - this.offset;
/* 199 */         if (j < 0) {
/* 200 */           throw new ArrayIndexOutOfBoundsException("src[" + i + "]-offset is " + "less than zero");
/*     */         }
/*     */ 
/* 204 */         paramArrayOfByte2[i] = this.data[i][j];
/*     */       }
/*     */     }
/* 207 */     return paramArrayOfByte2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ByteLookupTable
 * JD-Core Version:    0.6.2
 */