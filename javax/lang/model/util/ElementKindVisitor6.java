/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.TypeParameterElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_6)
/*     */ public class ElementKindVisitor6<R, P> extends SimpleElementVisitor6<R, P>
/*     */ {
/*     */   protected ElementKindVisitor6()
/*     */   {
/*  91 */     super(null);
/*     */   }
/*     */ 
/*     */   protected ElementKindVisitor6(R paramR)
/*     */   {
/* 101 */     super(paramR);
/*     */   }
/*     */ 
/*     */   public R visitPackage(PackageElement paramPackageElement, P paramP)
/*     */   {
/* 115 */     assert (paramPackageElement.getKind() == ElementKind.PACKAGE) : "Bad kind on PackageElement";
/* 116 */     return defaultAction(paramPackageElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitType(TypeElement paramTypeElement, P paramP)
/*     */   {
/* 131 */     ElementKind localElementKind = paramTypeElement.getKind();
/* 132 */     switch (1.$SwitchMap$javax$lang$model$element$ElementKind[localElementKind.ordinal()]) {
/*     */     case 1:
/* 134 */       return visitTypeAsAnnotationType(paramTypeElement, paramP);
/*     */     case 2:
/* 137 */       return visitTypeAsClass(paramTypeElement, paramP);
/*     */     case 3:
/* 140 */       return visitTypeAsEnum(paramTypeElement, paramP);
/*     */     case 4:
/* 143 */       return visitTypeAsInterface(paramTypeElement, paramP);
/*     */     }
/*     */ 
/* 146 */     throw new AssertionError("Bad kind " + localElementKind + " for TypeElement" + paramTypeElement);
/*     */   }
/*     */ 
/*     */   public R visitTypeAsAnnotationType(TypeElement paramTypeElement, P paramP)
/*     */   {
/* 159 */     return defaultAction(paramTypeElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitTypeAsClass(TypeElement paramTypeElement, P paramP)
/*     */   {
/* 171 */     return defaultAction(paramTypeElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitTypeAsEnum(TypeElement paramTypeElement, P paramP)
/*     */   {
/* 183 */     return defaultAction(paramTypeElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitTypeAsInterface(TypeElement paramTypeElement, P paramP)
/*     */   {
/* 195 */     return defaultAction(paramTypeElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 210 */     ElementKind localElementKind = paramVariableElement.getKind();
/* 211 */     switch (1.$SwitchMap$javax$lang$model$element$ElementKind[localElementKind.ordinal()]) {
/*     */     case 5:
/* 213 */       return visitVariableAsEnumConstant(paramVariableElement, paramP);
/*     */     case 6:
/* 216 */       return visitVariableAsExceptionParameter(paramVariableElement, paramP);
/*     */     case 7:
/* 219 */       return visitVariableAsField(paramVariableElement, paramP);
/*     */     case 8:
/* 222 */       return visitVariableAsLocalVariable(paramVariableElement, paramP);
/*     */     case 9:
/* 225 */       return visitVariableAsParameter(paramVariableElement, paramP);
/*     */     case 10:
/* 228 */       return visitVariableAsResourceVariable(paramVariableElement, paramP);
/*     */     }
/*     */ 
/* 231 */     throw new AssertionError("Bad kind " + localElementKind + " for VariableElement" + paramVariableElement);
/*     */   }
/*     */ 
/*     */   public R visitVariableAsEnumConstant(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 244 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariableAsExceptionParameter(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 256 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariableAsField(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 268 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariableAsLocalVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 280 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariableAsParameter(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 292 */     return defaultAction(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitVariableAsResourceVariable(VariableElement paramVariableElement, P paramP)
/*     */   {
/* 306 */     return visitUnknown(paramVariableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitExecutable(ExecutableElement paramExecutableElement, P paramP)
/*     */   {
/* 321 */     ElementKind localElementKind = paramExecutableElement.getKind();
/* 322 */     switch (1.$SwitchMap$javax$lang$model$element$ElementKind[localElementKind.ordinal()]) {
/*     */     case 11:
/* 324 */       return visitExecutableAsConstructor(paramExecutableElement, paramP);
/*     */     case 12:
/* 327 */       return visitExecutableAsInstanceInit(paramExecutableElement, paramP);
/*     */     case 13:
/* 330 */       return visitExecutableAsMethod(paramExecutableElement, paramP);
/*     */     case 14:
/* 333 */       return visitExecutableAsStaticInit(paramExecutableElement, paramP);
/*     */     }
/*     */ 
/* 336 */     throw new AssertionError("Bad kind " + localElementKind + " for ExecutableElement" + paramExecutableElement);
/*     */   }
/*     */ 
/*     */   public R visitExecutableAsConstructor(ExecutableElement paramExecutableElement, P paramP)
/*     */   {
/* 349 */     return defaultAction(paramExecutableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitExecutableAsInstanceInit(ExecutableElement paramExecutableElement, P paramP)
/*     */   {
/* 361 */     return defaultAction(paramExecutableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitExecutableAsMethod(ExecutableElement paramExecutableElement, P paramP)
/*     */   {
/* 373 */     return defaultAction(paramExecutableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitExecutableAsStaticInit(ExecutableElement paramExecutableElement, P paramP)
/*     */   {
/* 385 */     return defaultAction(paramExecutableElement, paramP);
/*     */   }
/*     */ 
/*     */   public R visitTypeParameter(TypeParameterElement paramTypeParameterElement, P paramP)
/*     */   {
/* 400 */     assert (paramTypeParameterElement.getKind() == ElementKind.TYPE_PARAMETER) : "Bad kind on TypeParameterElement";
/* 401 */     return defaultAction(paramTypeParameterElement, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.ElementKindVisitor6
 * JD-Core Version:    0.6.2
 */