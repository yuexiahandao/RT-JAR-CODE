/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.TypeParameterElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_6)
/*     */ public class SimpleElementVisitor6<R, P> extends AbstractElementVisitor6<R, P>
/*     */ {
/*     */   protected final R DEFAULT_VALUE;
/*     */ 
/*     */   protected SimpleElementVisitor6()
/*     */   {
/*  95 */     this.DEFAULT_VALUE = null;
/*     */   }
/*     */ 
/*     */   protected SimpleElementVisitor6(R paramR)
/*     */   {
/* 105 */     this.DEFAULT_VALUE = paramR;
/*     */   }
/*     */ 
/*     */   protected R defaultAction(Element paramElement, P paramP)
/*     */   {
/* 117 */     return this.DEFAULT_VALUE;
/*     */   }
/*     */ 
/*     */   public R visitPackage(PackageElement paramPackageElement, P paramP)
/*     */   {
/* 128 */     return defaultAction(paramPackageElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitType(TypeElement paramTypeElement, P paramP)
/*     */   {
/* 139 */     return defaultAction(paramTypeElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 154 */     if (paramVariableElement.getKind() != ElementKind.RESOURCE_VARIABLE) {
/* 155 */       return defaultAction(paramVariableElement, paramP);
/*     */     }
/* 157 */     return visitUnknown(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitExecutable(ExecutableElement paramExecutableElement, P paramP)
/*     */   {
/* 168 */     return defaultAction(paramExecutableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitTypeParameter(TypeParameterElement paramTypeParameterElement, P paramP)
/*     */   {
/* 179 */     return defaultAction(paramTypeParameterElement, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.SimpleElementVisitor6
 * JD-Core Version:    0.6.2
 */