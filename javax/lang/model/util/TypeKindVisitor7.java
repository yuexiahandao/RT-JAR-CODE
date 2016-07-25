/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.type.UnionType;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_7)
/*     */ public class TypeKindVisitor7<R, P> extends TypeKindVisitor6<R, P>
/*     */ {
/*     */   protected TypeKindVisitor7()
/*     */   {
/*  83 */     super(null);
/*     */   }
/*     */ 
/*     */   protected TypeKindVisitor7(R paramR)
/*     */   {
/*  93 */     super(paramR);
/*     */   }
/*     */ 
/*     */   public R visitUnion(UnionType paramUnionType, P paramP)
/*     */   {
/* 106 */     return defaultAction(paramUnionType, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.TypeKindVisitor7
 * JD-Core Version:    0.6.2
 */