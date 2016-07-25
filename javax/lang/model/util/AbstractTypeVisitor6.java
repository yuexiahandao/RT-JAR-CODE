/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVisitor;
/*     */ import javax.lang.model.type.UnionType;
/*     */ import javax.lang.model.type.UnknownTypeException;
/*     */ 
/*     */ public abstract class AbstractTypeVisitor6<R, P>
/*     */   implements TypeVisitor<R, P>
/*     */ {
/*     */   public final R visit(TypeMirror paramTypeMirror, P paramP)
/*     */   {
/*  82 */     return paramTypeMirror.accept(this, paramP);
/*     */   }
/*     */ 
/*     */   public final R visit(TypeMirror paramTypeMirror)
/*     */   {
/*  95 */     return paramTypeMirror.accept(this, null);
/*     */   }
/*     */ 
/*     */   public R visitUnion(UnionType paramUnionType, P paramP)
/*     */   {
/* 109 */     return visitUnknown(paramUnionType, paramP);
/*     */   }
/*     */ 
/*     */   public R visitUnknown(TypeMirror paramTypeMirror, P paramP)
/*     */   {
/* 126 */     throw new UnknownTypeException(paramTypeMirror, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.AbstractTypeVisitor6
 * JD-Core Version:    0.6.2
 */