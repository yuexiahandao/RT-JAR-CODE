/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class ModelStandardTransform
/*     */   implements ModelTransform
/*     */ {
/*     */   public static final boolean DIRECTION_MIN2MAX = false;
/*     */   public static final boolean DIRECTION_MAX2MIN = true;
/*     */   public static final boolean POLARITY_UNIPOLAR = false;
/*     */   public static final boolean POLARITY_BIPOLAR = true;
/*     */   public static final int TRANSFORM_LINEAR = 0;
/*     */   public static final int TRANSFORM_CONCAVE = 1;
/*     */   public static final int TRANSFORM_CONVEX = 2;
/*     */   public static final int TRANSFORM_SWITCH = 3;
/*     */   public static final int TRANSFORM_ABSOLUTE = 4;
/*  53 */   private boolean direction = false;
/*  54 */   private boolean polarity = false;
/*  55 */   private int transform = 0;
/*     */ 
/*     */   public ModelStandardTransform() {
/*     */   }
/*     */ 
/*     */   public ModelStandardTransform(boolean paramBoolean) {
/*  61 */     this.direction = paramBoolean;
/*     */   }
/*     */ 
/*     */   public ModelStandardTransform(boolean paramBoolean1, boolean paramBoolean2) {
/*  65 */     this.direction = paramBoolean1;
/*  66 */     this.polarity = paramBoolean2;
/*     */   }
/*     */ 
/*     */   public ModelStandardTransform(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
/*     */   {
/*  71 */     this.direction = paramBoolean1;
/*  72 */     this.polarity = paramBoolean2;
/*  73 */     this.transform = paramInt;
/*     */   }
/*     */ 
/*     */   public double transform(double paramDouble)
/*     */   {
/*  79 */     if (this.direction == true)
/*  80 */       paramDouble = 1.0D - paramDouble;
/*  81 */     if (this.polarity == true)
/*  82 */       paramDouble = paramDouble * 2.0D - 1.0D;
/*     */     double d1;
/*     */     double d2;
/*  83 */     switch (this.transform) {
/*     */     case 1:
/*  85 */       d1 = Math.signum(paramDouble);
/*  86 */       d2 = Math.abs(paramDouble);
/*  87 */       d2 = -(0.4166666666666667D / Math.log(10.0D)) * Math.log(1.0D - d2);
/*  88 */       if (d2 < 0.0D)
/*  89 */         d2 = 0.0D;
/*  90 */       else if (d2 > 1.0D)
/*  91 */         d2 = 1.0D;
/*  92 */       return d1 * d2;
/*     */     case 2:
/*  94 */       d1 = Math.signum(paramDouble);
/*  95 */       d2 = Math.abs(paramDouble);
/*  96 */       d2 = 1.0D + 0.4166666666666667D / Math.log(10.0D) * Math.log(d2);
/*  97 */       if (d2 < 0.0D)
/*  98 */         d2 = 0.0D;
/*  99 */       else if (d2 > 1.0D)
/* 100 */         d2 = 1.0D;
/* 101 */       return d1 * d2;
/*     */     case 3:
/* 103 */       if (this.polarity == true) {
/* 104 */         return paramDouble > 0.0D ? 1.0D : -1.0D;
/*     */       }
/* 106 */       return paramDouble > 0.5D ? 1.0D : 0.0D;
/*     */     case 4:
/* 108 */       return Math.abs(paramDouble);
/*     */     }
/*     */ 
/* 113 */     return paramDouble;
/*     */   }
/*     */ 
/*     */   public boolean getDirection() {
/* 117 */     return this.direction;
/*     */   }
/*     */ 
/*     */   public void setDirection(boolean paramBoolean) {
/* 121 */     this.direction = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getPolarity() {
/* 125 */     return this.polarity;
/*     */   }
/*     */ 
/*     */   public void setPolarity(boolean paramBoolean) {
/* 129 */     this.polarity = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getTransform() {
/* 133 */     return this.transform;
/*     */   }
/*     */ 
/*     */   public void setTransform(int paramInt) {
/* 137 */     this.transform = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelStandardTransform
 * JD-Core Version:    0.6.2
 */