/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_6)
/*     */ public class TypeKindVisitor6<R, P> extends SimpleTypeVisitor6<R, P>
/*     */ {
/*     */   protected TypeKindVisitor6()
/*     */   {
/*  88 */     super(null);
/*     */   }
/*     */ 
/*     */   protected TypeKindVisitor6(R paramR)
/*     */   {
/*  99 */     super(paramR);
/*     */   }
/*     */ 
/*     */   public R visitPrimitive(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 113 */     TypeKind localTypeKind = paramPrimitiveType.getKind();
/* 114 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[localTypeKind.ordinal()]) {
/*     */     case 1:
/* 116 */       return visitPrimitiveAsBoolean(paramPrimitiveType, paramP);
/*     */     case 2:
/* 119 */       return visitPrimitiveAsByte(paramPrimitiveType, paramP);
/*     */     case 3:
/* 122 */       return visitPrimitiveAsShort(paramPrimitiveType, paramP);
/*     */     case 4:
/* 125 */       return visitPrimitiveAsInt(paramPrimitiveType, paramP);
/*     */     case 5:
/* 128 */       return visitPrimitiveAsLong(paramPrimitiveType, paramP);
/*     */     case 6:
/* 131 */       return visitPrimitiveAsChar(paramPrimitiveType, paramP);
/*     */     case 7:
/* 134 */       return visitPrimitiveAsFloat(paramPrimitiveType, paramP);
/*     */     case 8:
/* 137 */       return visitPrimitiveAsDouble(paramPrimitiveType, paramP);
/*     */     }
/*     */ 
/* 140 */     throw new AssertionError("Bad kind " + localTypeKind + " for PrimitiveType" + paramPrimitiveType);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsBoolean(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 153 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsByte(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 165 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsShort(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 177 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsInt(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 189 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsLong(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 201 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsChar(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 213 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsFloat(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 225 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitPrimitiveAsDouble(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 237 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitNoType(NoType paramNoType, P paramP)
/*     */   {
/* 251 */     TypeKind localTypeKind = paramNoType.getKind();
/* 252 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[localTypeKind.ordinal()]) {
/*     */     case 9:
/* 254 */       return visitNoTypeAsVoid(paramNoType, paramP);
/*     */     case 10:
/* 257 */       return visitNoTypeAsPackage(paramNoType, paramP);
/*     */     case 11:
/* 260 */       return visitNoTypeAsNone(paramNoType, paramP);
/*     */     }
/*     */ 
/* 263 */     throw new AssertionError("Bad kind " + localTypeKind + " for NoType" + paramNoType);
/*     */   }
/*     */ 
/*     */   public R visitNoTypeAsVoid(NoType paramNoType, P paramP)
/*     */   {
/* 276 */     return defaultAction(paramNoType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitNoTypeAsPackage(NoType paramNoType, P paramP)
/*     */   {
/* 288 */     return defaultAction(paramNoType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitNoTypeAsNone(NoType paramNoType, P paramP)
/*     */   {
/* 300 */     return defaultAction(paramNoType, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.TypeKindVisitor6
 * JD-Core Version:    0.6.2
 */