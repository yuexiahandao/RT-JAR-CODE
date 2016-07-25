/*     */ package javax.lang.model.type;
/*     */ 
/*     */ public enum TypeKind
/*     */ {
/*  46 */   BOOLEAN, 
/*     */ 
/*  51 */   BYTE, 
/*     */ 
/*  56 */   SHORT, 
/*     */ 
/*  61 */   INT, 
/*     */ 
/*  66 */   LONG, 
/*     */ 
/*  71 */   CHAR, 
/*     */ 
/*  76 */   FLOAT, 
/*     */ 
/*  81 */   DOUBLE, 
/*     */ 
/*  87 */   VOID, 
/*     */ 
/*  93 */   NONE, 
/*     */ 
/*  98 */   NULL, 
/*     */ 
/* 103 */   ARRAY, 
/*     */ 
/* 108 */   DECLARED, 
/*     */ 
/* 113 */   ERROR, 
/*     */ 
/* 118 */   TYPEVAR, 
/*     */ 
/* 123 */   WILDCARD, 
/*     */ 
/* 129 */   PACKAGE, 
/*     */ 
/* 134 */   EXECUTABLE, 
/*     */ 
/* 140 */   OTHER, 
/*     */ 
/* 147 */   UNION;
/*     */ 
/*     */   public boolean isPrimitive()
/*     */   {
/* 155 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/* 164 */       return true;
/*     */     }
/*     */ 
/* 167 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.TypeKind
 * JD-Core Version:    0.6.2
 */