/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.RenderingHints.Key;
/*     */ import java.awt.geom.AffineTransform;
/*     */ 
/*     */ public class FontRenderContext
/*     */ {
/*     */   private transient AffineTransform tx;
/*     */   private transient Object aaHintValue;
/*     */   private transient Object fmHintValue;
/*     */   private transient boolean defaulting;
/*     */ 
/*     */   protected FontRenderContext()
/*     */   {
/*  79 */     this.aaHintValue = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
/*  80 */     this.fmHintValue = RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
/*  81 */     this.defaulting = true;
/*     */   }
/*     */ 
/*     */   public FontRenderContext(AffineTransform paramAffineTransform, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 107 */     if ((paramAffineTransform != null) && (!paramAffineTransform.isIdentity())) {
/* 108 */       this.tx = new AffineTransform(paramAffineTransform);
/*     */     }
/* 110 */     if (paramBoolean1)
/* 111 */       this.aaHintValue = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
/*     */     else {
/* 113 */       this.aaHintValue = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
/*     */     }
/* 115 */     if (paramBoolean2)
/* 116 */       this.fmHintValue = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
/*     */     else
/* 118 */       this.fmHintValue = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
/*     */   }
/*     */ 
/*     */   public FontRenderContext(AffineTransform paramAffineTransform, Object paramObject1, Object paramObject2)
/*     */   {
/* 147 */     if ((paramAffineTransform != null) && (!paramAffineTransform.isIdentity()))
/* 148 */       this.tx = new AffineTransform(paramAffineTransform);
/*     */     try
/*     */     {
/* 151 */       if (RenderingHints.KEY_TEXT_ANTIALIASING.isCompatibleValue(paramObject1))
/* 152 */         this.aaHintValue = paramObject1;
/*     */       else
/* 154 */         throw new IllegalArgumentException("AA hint:" + paramObject1);
/*     */     }
/*     */     catch (Exception localException1) {
/* 157 */       throw new IllegalArgumentException("AA hint:" + paramObject1);
/*     */     }
/*     */     try {
/* 160 */       if (RenderingHints.KEY_FRACTIONALMETRICS.isCompatibleValue(paramObject2))
/* 161 */         this.fmHintValue = paramObject2;
/*     */       else
/* 163 */         throw new IllegalArgumentException("FM hint:" + paramObject2);
/*     */     }
/*     */     catch (Exception localException2) {
/* 166 */       throw new IllegalArgumentException("FM hint:" + paramObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isTransformed()
/*     */   {
/* 180 */     if (!this.defaulting) {
/* 181 */       return this.tx != null;
/*     */     }
/* 183 */     return !getTransform().isIdentity();
/*     */   }
/*     */ 
/*     */   public int getTransformType()
/*     */   {
/* 196 */     if (!this.defaulting) {
/* 197 */       if (this.tx == null) {
/* 198 */         return 0;
/*     */       }
/* 200 */       return this.tx.getType();
/*     */     }
/*     */ 
/* 203 */     return getTransform().getType();
/*     */   }
/*     */ 
/*     */   public AffineTransform getTransform()
/*     */   {
/* 215 */     return this.tx == null ? new AffineTransform() : new AffineTransform(this.tx);
/*     */   }
/*     */ 
/*     */   public boolean isAntiAliased()
/*     */   {
/* 230 */     return (this.aaHintValue != RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) && (this.aaHintValue != RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
/*     */   }
/*     */ 
/*     */   public boolean usesFractionalMetrics()
/*     */   {
/* 247 */     return (this.fmHintValue != RenderingHints.VALUE_FRACTIONALMETRICS_OFF) && (this.fmHintValue != RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
/*     */   }
/*     */ 
/*     */   public Object getAntiAliasingHint()
/*     */   {
/* 261 */     if (this.defaulting) {
/* 262 */       if (isAntiAliased()) {
/* 263 */         return RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
/*     */       }
/* 265 */       return RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
/*     */     }
/*     */ 
/* 268 */     return this.aaHintValue;
/*     */   }
/*     */ 
/*     */   public Object getFractionalMetricsHint()
/*     */   {
/* 281 */     if (this.defaulting) {
/* 282 */       if (usesFractionalMetrics()) {
/* 283 */         return RenderingHints.VALUE_FRACTIONALMETRICS_ON;
/*     */       }
/* 285 */       return RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
/*     */     }
/*     */ 
/* 288 */     return this.fmHintValue;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 301 */       return equals((FontRenderContext)paramObject);
/*     */     } catch (ClassCastException localClassCastException) {
/*     */     }
/* 304 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(FontRenderContext paramFontRenderContext)
/*     */   {
/* 318 */     if (this == paramFontRenderContext) {
/* 319 */       return true;
/*     */     }
/* 321 */     if (paramFontRenderContext == null) {
/* 322 */       return false;
/*     */     }
/*     */ 
/* 326 */     if ((!paramFontRenderContext.defaulting) && (!this.defaulting)) {
/* 327 */       if ((paramFontRenderContext.aaHintValue == this.aaHintValue) && (paramFontRenderContext.fmHintValue == this.fmHintValue))
/*     */       {
/* 330 */         return this.tx == null ? false : paramFontRenderContext.tx == null ? true : this.tx.equals(paramFontRenderContext.tx);
/*     */       }
/* 332 */       return false;
/*     */     }
/* 334 */     return (paramFontRenderContext.getAntiAliasingHint() == getAntiAliasingHint()) && (paramFontRenderContext.getFractionalMetricsHint() == getFractionalMetricsHint()) && (paramFontRenderContext.getTransform().equals(getTransform()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 345 */     int i = this.tx == null ? 0 : this.tx.hashCode();
/*     */ 
/* 349 */     if (this.defaulting) {
/* 350 */       i += getAntiAliasingHint().hashCode();
/* 351 */       i += getFractionalMetricsHint().hashCode();
/*     */     } else {
/* 353 */       i += this.aaHintValue.hashCode();
/* 354 */       i += this.fmHintValue.hashCode();
/*     */     }
/* 356 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.FontRenderContext
 * JD-Core Version:    0.6.2
 */