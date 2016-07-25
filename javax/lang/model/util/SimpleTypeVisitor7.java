/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.type.UnionType;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_7)
/*     */ public class SimpleTypeVisitor7<R, P> extends SimpleTypeVisitor6<R, P>
/*     */ {
/*     */   protected SimpleTypeVisitor7()
/*     */   {
/*  80 */     super(null);
/*     */   }
/*     */ 
/*     */   protected SimpleTypeVisitor7(R paramR)
/*     */   {
/*  90 */     super(paramR);
/*     */   }
/*     */ 
/*     */   public R visitUnion(UnionType paramUnionType, P paramP)
/*     */   {
/* 103 */     return defaultAction(paramUnionType, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.SimpleTypeVisitor7
 * JD-Core Version:    0.6.2
 */