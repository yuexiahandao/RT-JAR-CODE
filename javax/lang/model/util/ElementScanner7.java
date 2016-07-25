/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_7)
/*     */ public class ElementScanner7<R, P> extends ElementScanner6<R, P>
/*     */ {
/*     */   protected ElementScanner7()
/*     */   {
/*  98 */     super(null);
/*     */   }
/*     */ 
/*     */   protected ElementScanner7(R paramR)
/*     */   {
/* 106 */     super(paramR);
/*     */   }
/*     */ 
/*     */   public R visitVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 118 */     return scan(paramVariableElement.getEnclosedElements(), paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.ElementScanner7
 * JD-Core Version:    0.6.2
 */