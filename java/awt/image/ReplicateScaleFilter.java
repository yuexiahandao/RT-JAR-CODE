/*     */ package java.awt.image;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ReplicateScaleFilter extends ImageFilter
/*     */ {
/*     */   protected int srcWidth;
/*     */   protected int srcHeight;
/*     */   protected int destWidth;
/*     */   protected int destHeight;
/*     */   protected int[] srcrows;
/*     */   protected int[] srccols;
/*     */   protected Object outpixbuf;
/*     */ 
/*     */   public ReplicateScaleFilter(int paramInt1, int paramInt2)
/*     */   {
/* 101 */     if ((paramInt1 == 0) || (paramInt2 == 0)) {
/* 102 */       throw new IllegalArgumentException("Width (" + paramInt1 + ") and height (" + paramInt2 + ") must be non-zero");
/*     */     }
/*     */ 
/* 106 */     this.destWidth = paramInt1;
/* 107 */     this.destHeight = paramInt2;
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable<?, ?> paramHashtable)
/*     */   {
/* 124 */     Hashtable localHashtable = (Hashtable)paramHashtable.clone();
/* 125 */     String str1 = "rescale";
/* 126 */     String str2 = this.destWidth + "x" + this.destHeight;
/* 127 */     Object localObject = localHashtable.get(str1);
/* 128 */     if ((localObject != null) && ((localObject instanceof String))) {
/* 129 */       str2 = (String)localObject + ", " + str2;
/*     */     }
/* 131 */     localHashtable.put(str1, str2);
/* 132 */     super.setProperties(localHashtable);
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/* 148 */     this.srcWidth = paramInt1;
/* 149 */     this.srcHeight = paramInt2;
/* 150 */     if (this.destWidth < 0) {
/* 151 */       if (this.destHeight < 0) {
/* 152 */         this.destWidth = this.srcWidth;
/* 153 */         this.destHeight = this.srcHeight;
/*     */       } else {
/* 155 */         this.destWidth = (this.srcWidth * this.destHeight / this.srcHeight);
/*     */       }
/* 157 */     } else if (this.destHeight < 0) {
/* 158 */       this.destHeight = (this.srcHeight * this.destWidth / this.srcWidth);
/*     */     }
/* 160 */     this.consumer.setDimensions(this.destWidth, this.destHeight);
/*     */   }
/*     */ 
/*     */   private void calculateMaps() {
/* 164 */     this.srcrows = new int[this.destHeight + 1];
/* 165 */     for (int i = 0; i <= this.destHeight; i++) {
/* 166 */       this.srcrows[i] = ((2 * i * this.srcHeight + this.srcHeight) / (2 * this.destHeight));
/*     */     }
/* 168 */     this.srccols = new int[this.destWidth + 1];
/* 169 */     for (i = 0; i <= this.destWidth; i++)
/* 170 */       this.srccols[i] = ((2 * i * this.srcWidth + this.srcWidth) / (2 * this.destWidth));
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 189 */     if ((this.srcrows == null) || (this.srccols == null)) {
/* 190 */       calculateMaps();
/*     */     }
/*     */ 
/* 193 */     int k = (2 * paramInt1 * this.destWidth + this.srcWidth - 1) / (2 * this.srcWidth);
/* 194 */     int m = (2 * paramInt2 * this.destHeight + this.srcHeight - 1) / (2 * this.srcHeight);
/*     */     byte[] arrayOfByte;
/* 196 */     if ((this.outpixbuf != null) && ((this.outpixbuf instanceof byte[]))) {
/* 197 */       arrayOfByte = (byte[])this.outpixbuf;
/*     */     } else {
/* 199 */       arrayOfByte = new byte[this.destWidth];
/* 200 */       this.outpixbuf = arrayOfByte;
/*     */     }
/*     */     int j;
/* 202 */     for (int n = m; (j = this.srcrows[n]) < paramInt2 + paramInt4; n++) {
/* 203 */       int i1 = paramInt5 + paramInt6 * (j - paramInt2);
/*     */       int i;
/* 205 */       for (int i2 = k; (i = this.srccols[i2]) < paramInt1 + paramInt3; i2++) {
/* 206 */         arrayOfByte[i2] = paramArrayOfByte[(i1 + i - paramInt1)];
/*     */       }
/* 208 */       if (i2 > k)
/* 209 */         this.consumer.setPixels(k, n, i2 - k, 1, paramColorModel, arrayOfByte, k, this.destWidth);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 230 */     if ((this.srcrows == null) || (this.srccols == null)) {
/* 231 */       calculateMaps();
/*     */     }
/*     */ 
/* 234 */     int k = (2 * paramInt1 * this.destWidth + this.srcWidth - 1) / (2 * this.srcWidth);
/* 235 */     int m = (2 * paramInt2 * this.destHeight + this.srcHeight - 1) / (2 * this.srcHeight);
/*     */     int[] arrayOfInt;
/* 237 */     if ((this.outpixbuf != null) && ((this.outpixbuf instanceof int[]))) {
/* 238 */       arrayOfInt = (int[])this.outpixbuf;
/*     */     } else {
/* 240 */       arrayOfInt = new int[this.destWidth];
/* 241 */       this.outpixbuf = arrayOfInt;
/*     */     }
/*     */     int j;
/* 243 */     for (int n = m; (j = this.srcrows[n]) < paramInt2 + paramInt4; n++) {
/* 244 */       int i1 = paramInt5 + paramInt6 * (j - paramInt2);
/*     */       int i;
/* 246 */       for (int i2 = k; (i = this.srccols[i2]) < paramInt1 + paramInt3; i2++) {
/* 247 */         arrayOfInt[i2] = paramArrayOfInt[(i1 + i - paramInt1)];
/*     */       }
/* 249 */       if (i2 > k)
/* 250 */         this.consumer.setPixels(k, n, i2 - k, 1, paramColorModel, arrayOfInt, k, this.destWidth);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ReplicateScaleFilter
 * JD-Core Version:    0.6.2
 */