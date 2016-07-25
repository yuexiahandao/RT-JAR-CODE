/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.font.LineMetrics;
/*     */ 
/*     */ public final class CoreMetrics
/*     */ {
/*     */   public final float ascent;
/*     */   public final float descent;
/*     */   public final float leading;
/*     */   public final float height;
/*     */   public final int baselineIndex;
/*     */   public final float[] baselineOffsets;
/*     */   public final float strikethroughOffset;
/*     */   public final float strikethroughThickness;
/*     */   public final float underlineOffset;
/*     */   public final float underlineThickness;
/*     */   public final float ssOffset;
/*     */   public final float italicAngle;
/*     */ 
/*     */   public CoreMetrics(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, float[] paramArrayOfFloat, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10)
/*     */   {
/*  51 */     this.ascent = paramFloat1;
/*  52 */     this.descent = paramFloat2;
/*  53 */     this.leading = paramFloat3;
/*  54 */     this.height = paramFloat4;
/*  55 */     this.baselineIndex = paramInt;
/*  56 */     this.baselineOffsets = paramArrayOfFloat;
/*  57 */     this.strikethroughOffset = paramFloat5;
/*  58 */     this.strikethroughThickness = paramFloat6;
/*  59 */     this.underlineOffset = paramFloat7;
/*  60 */     this.underlineThickness = paramFloat8;
/*  61 */     this.ssOffset = paramFloat9;
/*  62 */     this.italicAngle = paramFloat10;
/*     */   }
/*     */ 
/*     */   public static CoreMetrics get(LineMetrics paramLineMetrics) {
/*  66 */     return ((FontLineMetrics)paramLineMetrics).cm;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/*  70 */     return Float.floatToIntBits(this.ascent + this.ssOffset);
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object paramObject) {
/*     */     try {
/*  75 */       return equals((CoreMetrics)paramObject);
/*     */     } catch (ClassCastException localClassCastException) {
/*     */     }
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean equals(CoreMetrics paramCoreMetrics)
/*     */   {
/*  83 */     if (paramCoreMetrics != null) {
/*  84 */       if (this == paramCoreMetrics) {
/*  85 */         return true;
/*     */       }
/*     */ 
/*  88 */       return (this.ascent == paramCoreMetrics.ascent) && (this.descent == paramCoreMetrics.descent) && (this.leading == paramCoreMetrics.leading) && (this.baselineIndex == paramCoreMetrics.baselineIndex) && (this.baselineOffsets[0] == paramCoreMetrics.baselineOffsets[0]) && (this.baselineOffsets[1] == paramCoreMetrics.baselineOffsets[1]) && (this.baselineOffsets[2] == paramCoreMetrics.baselineOffsets[2]) && (this.strikethroughOffset == paramCoreMetrics.strikethroughOffset) && (this.strikethroughThickness == paramCoreMetrics.strikethroughThickness) && (this.underlineOffset == paramCoreMetrics.underlineOffset) && (this.underlineThickness == paramCoreMetrics.underlineThickness) && (this.ssOffset == paramCoreMetrics.ssOffset) && (this.italicAngle == paramCoreMetrics.italicAngle);
/*     */     }
/*     */ 
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   public final float effectiveBaselineOffset(float[] paramArrayOfFloat)
/*     */   {
/* 109 */     switch (this.baselineIndex) {
/*     */     case -1:
/* 111 */       return paramArrayOfFloat[4] + this.ascent;
/*     */     case -2:
/* 113 */       return paramArrayOfFloat[3] - this.descent;
/*     */     }
/* 115 */     return paramArrayOfFloat[this.baselineIndex];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.CoreMetrics
 * JD-Core Version:    0.6.2
 */