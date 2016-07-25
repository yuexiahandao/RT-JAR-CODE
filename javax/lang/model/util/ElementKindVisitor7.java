/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_7)
/*     */ public class ElementKindVisitor7<R, P> extends ElementKindVisitor6<R, P>
/*     */ {
/*     */   protected ElementKindVisitor7()
/*     */   {
/*  85 */     super(null);
/*     */   }
/*     */ 
/*     */   protected ElementKindVisitor7(R paramR)
/*     */   {
/*  95 */     super(paramR);
/*     */   }
/*     */ 
/*     */   public R visitVariableAsResourceVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 108 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.ElementKindVisitor7
 * JD-Core Version:    0.6.2
 */