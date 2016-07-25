/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.Arrays;
/*     */ import sun.awt.image.ImagingLib;
/*     */ 
/*     */ public class BandCombineOp
/*     */   implements RasterOp
/*     */ {
/*     */   float[][] matrix;
/*  62 */   int nrows = 0;
/*  63 */   int ncols = 0;
/*     */   RenderingHints hints;
/*     */ 
/*     */   public BandCombineOp(float[][] paramArrayOfFloat, RenderingHints paramRenderingHints)
/*     */   {
/*  85 */     this.nrows = paramArrayOfFloat.length;
/*  86 */     this.ncols = paramArrayOfFloat[0].length;
/*  87 */     this.matrix = new float[this.nrows][];
/*  88 */     for (int i = 0; i < this.nrows; i++)
/*     */     {
/*  94 */       if (this.ncols > paramArrayOfFloat[i].length) {
/*  95 */         throw new IndexOutOfBoundsException("row " + i + " too short");
/*     */       }
/*  97 */       this.matrix[i] = Arrays.copyOf(paramArrayOfFloat[i], this.ncols);
/*     */     }
/*  99 */     this.hints = paramRenderingHints;
/*     */   }
/*     */ 
/*     */   public final float[][] getMatrix()
/*     */   {
/* 108 */     float[][] arrayOfFloat = new float[this.nrows][];
/* 109 */     for (int i = 0; i < this.nrows; i++) {
/* 110 */       arrayOfFloat[i] = Arrays.copyOf(this.matrix[i], this.ncols);
/*     */     }
/* 112 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public WritableRaster filter(Raster paramRaster, WritableRaster paramWritableRaster)
/*     */   {
/* 135 */     int i = paramRaster.getNumBands();
/* 136 */     if ((this.ncols != i) && (this.ncols != i + 1)) {
/* 137 */       throw new IllegalArgumentException("Number of columns in the matrix (" + this.ncols + ") must be equal to the number" + " of bands ([+1]) in src (" + i + ").");
/*     */     }
/*     */ 
/* 143 */     if (paramWritableRaster == null) {
/* 144 */       paramWritableRaster = createCompatibleDestRaster(paramRaster);
/*     */     }
/* 146 */     else if (this.nrows != paramWritableRaster.getNumBands()) {
/* 147 */       throw new IllegalArgumentException("Number of rows in the matrix (" + this.nrows + ") must be equal to the number" + " of bands ([+1]) in dst (" + i + ").");
/*     */     }
/*     */ 
/* 154 */     if (ImagingLib.filter(this, paramRaster, paramWritableRaster) != null) {
/* 155 */       return paramWritableRaster;
/*     */     }
/*     */ 
/* 158 */     int[] arrayOfInt1 = null;
/* 159 */     int[] arrayOfInt2 = new int[paramWritableRaster.getNumBands()];
/*     */ 
/* 161 */     int j = paramRaster.getMinX();
/* 162 */     int k = paramRaster.getMinY();
/* 163 */     int m = paramWritableRaster.getMinX();
/* 164 */     int n = paramWritableRaster.getMinY();
/*     */     int i3;
/*     */     int i2;
/*     */     int i1;
/*     */     int i4;
/*     */     int i5;
/*     */     float f;
/*     */     int i6;
/* 167 */     if (this.ncols == i) {
/* 168 */       for (i3 = 0; i3 < paramRaster.getHeight(); n++) {
/* 169 */         i2 = m;
/* 170 */         i1 = j;
/* 171 */         for (i4 = 0; i4 < paramRaster.getWidth(); i2++) {
/* 172 */           arrayOfInt1 = paramRaster.getPixel(i1, k, arrayOfInt1);
/* 173 */           for (i5 = 0; i5 < this.nrows; i5++) {
/* 174 */             f = 0.0F;
/* 175 */             for (i6 = 0; i6 < this.ncols; i6++) {
/* 176 */               f += this.matrix[i5][i6] * arrayOfInt1[i6];
/*     */             }
/* 178 */             arrayOfInt2[i5] = ((int)f);
/*     */           }
/* 180 */           paramWritableRaster.setPixel(i2, n, arrayOfInt2);
/*     */ 
/* 171 */           i4++; i1++;
/*     */         }
/* 168 */         i3++; k++;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 186 */       for (i3 = 0; i3 < paramRaster.getHeight(); n++) {
/* 187 */         i2 = m;
/* 188 */         i1 = j;
/* 189 */         for (i4 = 0; i4 < paramRaster.getWidth(); i2++) {
/* 190 */           arrayOfInt1 = paramRaster.getPixel(i1, k, arrayOfInt1);
/* 191 */           for (i5 = 0; i5 < this.nrows; i5++) {
/* 192 */             f = 0.0F;
/* 193 */             for (i6 = 0; i6 < i; i6++) {
/* 194 */               f += this.matrix[i5][i6] * arrayOfInt1[i6];
/*     */             }
/* 196 */             arrayOfInt2[i5] = ((int)(f + this.matrix[i5][i]));
/*     */           }
/* 198 */           paramWritableRaster.setPixel(i2, n, arrayOfInt2);
/*     */ 
/* 189 */           i4++; i1++;
/*     */         }
/* 186 */         i3++; k++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 203 */     return paramWritableRaster;
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(Raster paramRaster)
/*     */   {
/* 223 */     return paramRaster.getBounds();
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleDestRaster(Raster paramRaster)
/*     */   {
/* 239 */     int i = paramRaster.getNumBands();
/* 240 */     if ((this.ncols != i) && (this.ncols != i + 1)) {
/* 241 */       throw new IllegalArgumentException("Number of columns in the matrix (" + this.ncols + ") must be equal to the number" + " of bands ([+1]) in src (" + i + ").");
/*     */     }
/*     */ 
/* 247 */     if (paramRaster.getNumBands() == this.nrows) {
/* 248 */       return paramRaster.createCompatibleWritableRaster();
/*     */     }
/*     */ 
/* 251 */     throw new IllegalArgumentException("Don't know how to create a  compatible Raster with " + this.nrows + " bands.");
/*     */   }
/*     */ 
/*     */   public final Point2D getPoint2D(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */   {
/* 272 */     if (paramPoint2D2 == null) {
/* 273 */       paramPoint2D2 = new Point2D.Float();
/*     */     }
/* 275 */     paramPoint2D2.setLocation(paramPoint2D1.getX(), paramPoint2D1.getY());
/*     */ 
/* 277 */     return paramPoint2D2;
/*     */   }
/*     */ 
/*     */   public final RenderingHints getRenderingHints()
/*     */   {
/* 287 */     return this.hints;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.BandCombineOp
 * JD-Core Version:    0.6.2
 */