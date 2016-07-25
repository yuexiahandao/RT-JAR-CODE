/*     */ package java.math;
/*     */ 
/*     */ public enum RoundingMode
/*     */ {
/* 118 */   UP(0), 
/*     */ 
/* 141 */   DOWN(1), 
/*     */ 
/* 165 */   CEILING(2), 
/*     */ 
/* 189 */   FLOOR(3), 
/*     */ 
/* 215 */   HALF_UP(4), 
/*     */ 
/* 240 */   HALF_DOWN(5), 
/*     */ 
/* 272 */   HALF_EVEN(6), 
/*     */ 
/* 295 */   UNNECESSARY(7);
/*     */ 
/*     */   final int oldMode;
/*     */ 
/*     */   private RoundingMode(int paramInt)
/*     */   {
/* 307 */     this.oldMode = paramInt;
/*     */   }
/*     */ 
/*     */   public static RoundingMode valueOf(int paramInt)
/*     */   {
/* 319 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/* 322 */       return UP;
/*     */     case 1:
/* 325 */       return DOWN;
/*     */     case 2:
/* 328 */       return CEILING;
/*     */     case 3:
/* 331 */       return FLOOR;
/*     */     case 4:
/* 334 */       return HALF_UP;
/*     */     case 5:
/* 337 */       return HALF_DOWN;
/*     */     case 6:
/* 340 */       return HALF_EVEN;
/*     */     case 7:
/* 343 */       return UNNECESSARY;
/*     */     }
/*     */ 
/* 346 */     throw new IllegalArgumentException("argument out of range");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.math.RoundingMode
 * JD-Core Version:    0.6.2
 */