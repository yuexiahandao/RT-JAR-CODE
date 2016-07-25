/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_7)
/*     */ public class SimpleElementVisitor7<R, P> extends SimpleElementVisitor6<R, P>
/*     */ {
/*     */   protected SimpleElementVisitor7()
/*     */   {
/*  81 */     super(null);
/*     */   }
/*     */ 
/*     */   protected SimpleElementVisitor7(R paramR)
/*     */   {
/*  91 */     super(paramR);
/*     */   }
/*     */ 
/*     */   public R visitVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 103 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.SimpleElementVisitor7
 * JD-Core Version:    0.6.2
 */