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
/*     */ public class ElementScanner6<R, P> extends AbstractElementVisitor6<R, P>
/*     */ {
/*     */   protected final R DEFAULT_VALUE;
/*     */ 
/*     */   protected ElementScanner6()
/*     */   {
/* 107 */     this.DEFAULT_VALUE = null;
/*     */   }
/*     */ 
/*     */   protected ElementScanner6(R paramR)
/*     */   {
/* 115 */     this.DEFAULT_VALUE = paramR;
/*     */   }
/*     */ 
/*     */   public final R scan(Iterable<? extends Element> paramIterable, P paramP)
/*     */   {
/* 129 */     Object localObject = this.DEFAULT_VALUE;
/* 130 */     for (Element localElement : paramIterable)
/* 131 */       localObject = scan(localElement, paramP);
/* 132 */     return localObject;
/*     */   }
/*     */ 
/*     */   public R scan(Element paramElement, P paramP)
/*     */   {
/* 141 */     return paramElement.accept(this, paramP);
/*     */   }
/*     */ 
/*     */   public final R scan(Element paramElement)
/*     */   {
/* 149 */     return scan(paramElement, null);
/*     */   }
/*     */ 
/*     */   public R visitPackage(PackageElement paramPackageElement, P paramP)
/*     */   {
/* 160 */     return scan(paramPackageElement.getEnclosedElements(), paramP);
/*     */   }
/*     */ 
/*     */   public R visitType(TypeElement paramTypeElement, P paramP)
/*     */   {
/* 171 */     return scan(paramTypeElement.getEnclosedElements(), paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 186 */     if (paramVariableElement.getKind() != ElementKind.RESOURCE_VARIABLE) {
/* 187 */       return scan(paramVariableElement.getEnclosedElements(), paramP);
/*     */     }
/* 189 */     return visitUnknown(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitExecutable(ExecutableElement paramExecutableElement, P paramP)
/*     */   {
/* 200 */     return scan(paramExecutableElement.getParameters(), paramP);
/*     */   }
/*     */ 
/*     */   public R visitTypeParameter(TypeParameterElement paramTypeParameterElement, P paramP)
/*     */   {
/* 211 */     return scan(paramTypeParameterElement.getEnclosedElements(), paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.ElementScanner6
 * JD-Core Version:    0.6.2
 */