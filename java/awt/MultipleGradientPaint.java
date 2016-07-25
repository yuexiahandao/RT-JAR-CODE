/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public abstract class MultipleGradientPaint
/*     */   implements Paint
/*     */ {
/*     */   final int transparency;
/*     */   final float[] fractions;
/*     */   final Color[] colors;
/*     */   final AffineTransform gradientTransform;
/*     */   final CycleMethod cycleMethod;
/*     */   final ColorSpaceType colorSpace;
/*     */   ColorModel model;
/*     */   float[] normalizedIntervals;
/*     */   boolean isSimpleLookup;
/*     */   SoftReference<int[][]> gradients;
/*     */   SoftReference<int[]> gradient;
/*     */   int fastGradientArraySize;
/*     */ 
/*     */   MultipleGradientPaint(float[] paramArrayOfFloat, Color[] paramArrayOfColor, CycleMethod paramCycleMethod, ColorSpaceType paramColorSpaceType, AffineTransform paramAffineTransform)
/*     */   {
/* 142 */     if (paramArrayOfFloat == null) {
/* 143 */       throw new NullPointerException("Fractions array cannot be null");
/*     */     }
/*     */ 
/* 146 */     if (paramArrayOfColor == null) {
/* 147 */       throw new NullPointerException("Colors array cannot be null");
/*     */     }
/*     */ 
/* 150 */     if (paramCycleMethod == null) {
/* 151 */       throw new NullPointerException("Cycle method cannot be null");
/*     */     }
/*     */ 
/* 154 */     if (paramColorSpaceType == null) {
/* 155 */       throw new NullPointerException("Color space cannot be null");
/*     */     }
/*     */ 
/* 158 */     if (paramAffineTransform == null) {
/* 159 */       throw new NullPointerException("Gradient transform cannot be null");
/*     */     }
/*     */ 
/* 163 */     if (paramArrayOfFloat.length != paramArrayOfColor.length) {
/* 164 */       throw new IllegalArgumentException("Colors and fractions must have equal size");
/*     */     }
/*     */ 
/* 168 */     if (paramArrayOfColor.length < 2) {
/* 169 */       throw new IllegalArgumentException("User must specify at least 2 colors");
/*     */     }
/*     */ 
/* 175 */     float f1 = -1.0F;
/* 176 */     for (float f2 : paramArrayOfFloat) {
/* 177 */       if ((f2 < 0.0F) || (f2 > 1.0F)) {
/* 178 */         throw new IllegalArgumentException("Fraction values must be in the range 0 to 1: " + f2);
/*     */       }
/*     */ 
/* 183 */       if (f2 <= f1) {
/* 184 */         throw new IllegalArgumentException("Keyframe fractions must be increasing: " + f2);
/*     */       }
/*     */ 
/* 189 */       f1 = f2;
/*     */     }
/*     */ 
/* 196 */     int i = 0;
/* 197 */     ??? = 0;
/* 198 */     ??? = paramArrayOfFloat.length;
/* 199 */     int m = 0;
/*     */ 
/* 201 */     if (paramArrayOfFloat[0] != 0.0F)
/*     */     {
/* 203 */       i = 1;
/* 204 */       ???++;
/* 205 */       m++;
/*     */     }
/* 207 */     if (paramArrayOfFloat[(paramArrayOfFloat.length - 1)] != 1.0F)
/*     */     {
/* 209 */       ??? = 1;
/* 210 */       ???++;
/*     */     }
/*     */ 
/* 213 */     this.fractions = new float[???];
/* 214 */     System.arraycopy(paramArrayOfFloat, 0, this.fractions, m, paramArrayOfFloat.length);
/* 215 */     this.colors = new Color[???];
/* 216 */     System.arraycopy(paramArrayOfColor, 0, this.colors, m, paramArrayOfColor.length);
/*     */ 
/* 218 */     if (i != 0) {
/* 219 */       this.fractions[0] = 0.0F;
/* 220 */       this.colors[0] = paramArrayOfColor[0];
/*     */     }
/* 222 */     if (??? != 0) {
/* 223 */       this.fractions[(??? - 1)] = 1.0F;
/* 224 */       this.colors[(??? - 1)] = paramArrayOfColor[(paramArrayOfColor.length - 1)];
/*     */     }
/*     */ 
/* 228 */     this.colorSpace = paramColorSpaceType;
/* 229 */     this.cycleMethod = paramCycleMethod;
/*     */ 
/* 232 */     this.gradientTransform = new AffineTransform(paramAffineTransform);
/*     */ 
/* 235 */     int n = 1;
/* 236 */     for (int i1 = 0; i1 < paramArrayOfColor.length; i1++) {
/* 237 */       n = (n != 0) && (paramArrayOfColor[i1].getAlpha() == 255) ? 1 : 0;
/*     */     }
/* 239 */     this.transparency = (n != 0 ? 1 : 3);
/*     */   }
/*     */ 
/*     */   public final float[] getFractions()
/*     */   {
/* 252 */     return Arrays.copyOf(this.fractions, this.fractions.length);
/*     */   }
/*     */ 
/*     */   public final Color[] getColors()
/*     */   {
/* 263 */     return (Color[])Arrays.copyOf(this.colors, this.colors.length);
/*     */   }
/*     */ 
/*     */   public final CycleMethod getCycleMethod()
/*     */   {
/* 272 */     return this.cycleMethod;
/*     */   }
/*     */ 
/*     */   public final ColorSpaceType getColorSpace()
/*     */   {
/* 283 */     return this.colorSpace;
/*     */   }
/*     */ 
/*     */   public final AffineTransform getTransform()
/*     */   {
/* 296 */     return new AffineTransform(this.gradientTransform);
/*     */   }
/*     */ 
/*     */   public final int getTransparency()
/*     */   {
/* 309 */     return this.transparency;
/*     */   }
/*     */ 
/*     */   public static enum ColorSpaceType
/*     */   {
/*  73 */     SRGB, 
/*     */ 
/*  79 */     LINEAR_RGB;
/*     */   }
/*     */ 
/*     */   public static enum CycleMethod
/*     */   {
/*  51 */     NO_CYCLE, 
/*     */ 
/*  57 */     REFLECT, 
/*     */ 
/*  63 */     REPEAT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MultipleGradientPaint
 * JD-Core Version:    0.6.2
 */