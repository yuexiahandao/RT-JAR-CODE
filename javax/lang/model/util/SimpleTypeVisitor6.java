/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.ErrorType;
/*     */ import javax.lang.model.type.ExecutableType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.NullType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
/*     */ import javax.lang.model.type.WildcardType;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_6)
/*     */ public class SimpleTypeVisitor6<R, P> extends AbstractTypeVisitor6<R, P>
/*     */ {
/*     */   protected final R DEFAULT_VALUE;
/*     */ 
/*     */   protected SimpleTypeVisitor6()
/*     */   {
/*  94 */     this.DEFAULT_VALUE = null;
/*     */   }
/*     */ 
/*     */   protected SimpleTypeVisitor6(R paramR)
/*     */   {
/* 104 */     this.DEFAULT_VALUE = paramR;
/*     */   }
/*     */ 
/*     */   protected R defaultAction(TypeMirror paramTypeMirror, P paramP)
/*     */   {
/* 113 */     return this.DEFAULT_VALUE;
/*     */   }
/*     */ 
/*     */   public R visitPrimitive(PrimitiveType paramPrimitiveType, P paramP)
/*     */   {
/* 124 */     return defaultAction(paramPrimitiveType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitNull(NullType paramNullType, P paramP)
/*     */   {
/* 135 */     return defaultAction(paramNullType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitArray(ArrayType paramArrayType, P paramP)
/*     */   {
/* 146 */     return defaultAction(paramArrayType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitDeclared(DeclaredType paramDeclaredType, P paramP)
/*     */   {
/* 157 */     return defaultAction(paramDeclaredType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitError(ErrorType paramErrorType, P paramP)
/*     */   {
/* 168 */     return defaultAction(paramErrorType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitTypeVariable(TypeVariable paramTypeVariable, P paramP)
/*     */   {
/* 179 */     return defaultAction(paramTypeVariable, paramP);
/*     */   }
/*     */ 
/*     */   public R visitWildcard(WildcardType paramWildcardType, P paramP)
/*     */   {
/* 190 */     return defaultAction(paramWildcardType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitExecutable(ExecutableType paramExecutableType, P paramP)
/*     */   {
/* 201 */     return defaultAction(paramExecutableType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitNoType(NoType paramNoType, P paramP)
/*     */   {
/* 212 */     return defaultAction(paramNoType, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.SimpleTypeVisitor6
 * JD-Core Version:    0.6.2
 */