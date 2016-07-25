/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public final class UniqueTag
/*     */ {
/*     */   private static final int ID_NOT_FOUND = 1;
/*     */   private static final int ID_NULL_VALUE = 2;
/*     */   private static final int ID_DOUBLE_MARK = 3;
/*  60 */   public static final UniqueTag NOT_FOUND = new UniqueTag(1);
/*     */ 
/*  66 */   public static final UniqueTag NULL_VALUE = new UniqueTag(2);
/*     */ 
/*  73 */   public static final UniqueTag DOUBLE_MARK = new UniqueTag(3);
/*     */   private final int tagId;
/*     */ 
/*     */   private UniqueTag(int paramInt)
/*     */   {
/*  79 */     this.tagId = paramInt;
/*     */   }
/*     */ 
/*     */   public Object readResolve()
/*     */   {
/*  84 */     switch (this.tagId) {
/*     */     case 1:
/*  86 */       return NOT_FOUND;
/*     */     case 2:
/*  88 */       return NULL_VALUE;
/*     */     case 3:
/*  90 */       return DOUBLE_MARK;
/*     */     }
/*  92 */     throw new IllegalStateException(String.valueOf(this.tagId));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     String str;
/* 100 */     switch (this.tagId) {
/*     */     case 1:
/* 102 */       str = "NOT_FOUND";
/* 103 */       break;
/*     */     case 2:
/* 105 */       str = "NULL_VALUE";
/* 106 */       break;
/*     */     case 3:
/* 108 */       str = "DOUBLE_MARK";
/* 109 */       break;
/*     */     default:
/* 111 */       throw Kit.codeBug();
/*     */     }
/* 113 */     return super.toString() + ": " + str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.UniqueTag
 * JD-Core Version:    0.6.2
 */