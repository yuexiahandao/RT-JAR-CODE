/*     */ package java.awt.font;
/*     */ 
/*     */ public final class GlyphJustificationInfo
/*     */ {
/*     */   public static final int PRIORITY_KASHIDA = 0;
/*     */   public static final int PRIORITY_WHITESPACE = 1;
/*     */   public static final int PRIORITY_INTERCHAR = 2;
/*     */   public static final int PRIORITY_NONE = 3;
/*     */   public final float weight;
/*     */   public final int growPriority;
/*     */   public final boolean growAbsorb;
/*     */   public final float growLeftLimit;
/*     */   public final float growRightLimit;
/*     */   public final int shrinkPriority;
/*     */   public final boolean shrinkAbsorb;
/*     */   public final float shrinkLeftLimit;
/*     */   public final float shrinkRightLimit;
/*     */ 
/*     */   public GlyphJustificationInfo(float paramFloat1, boolean paramBoolean1, int paramInt1, float paramFloat2, float paramFloat3, boolean paramBoolean2, int paramInt2, float paramFloat4, float paramFloat5)
/*     */   {
/* 112 */     if (paramFloat1 < 0.0F) {
/* 113 */       throw new IllegalArgumentException("weight is negative");
/*     */     }
/*     */ 
/* 116 */     if (!priorityIsValid(paramInt1)) {
/* 117 */       throw new IllegalArgumentException("Invalid grow priority");
/*     */     }
/* 119 */     if (paramFloat2 < 0.0F) {
/* 120 */       throw new IllegalArgumentException("growLeftLimit is negative");
/*     */     }
/* 122 */     if (paramFloat3 < 0.0F) {
/* 123 */       throw new IllegalArgumentException("growRightLimit is negative");
/*     */     }
/*     */ 
/* 126 */     if (!priorityIsValid(paramInt2)) {
/* 127 */       throw new IllegalArgumentException("Invalid shrink priority");
/*     */     }
/* 129 */     if (paramFloat4 < 0.0F) {
/* 130 */       throw new IllegalArgumentException("shrinkLeftLimit is negative");
/*     */     }
/* 132 */     if (paramFloat5 < 0.0F) {
/* 133 */       throw new IllegalArgumentException("shrinkRightLimit is negative");
/*     */     }
/*     */ 
/* 136 */     this.weight = paramFloat1;
/* 137 */     this.growAbsorb = paramBoolean1;
/* 138 */     this.growPriority = paramInt1;
/* 139 */     this.growLeftLimit = paramFloat2;
/* 140 */     this.growRightLimit = paramFloat3;
/* 141 */     this.shrinkAbsorb = paramBoolean2;
/* 142 */     this.shrinkPriority = paramInt2;
/* 143 */     this.shrinkLeftLimit = paramFloat4;
/* 144 */     this.shrinkRightLimit = paramFloat5;
/*     */   }
/*     */ 
/*     */   private static boolean priorityIsValid(int paramInt)
/*     */   {
/* 149 */     return (paramInt >= 0) && (paramInt <= 3);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.GlyphJustificationInfo
 * JD-Core Version:    0.6.2
 */