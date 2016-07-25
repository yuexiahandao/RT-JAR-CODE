/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.AnnotationValueVisitor;
/*     */ import javax.lang.model.element.UnknownAnnotationValueException;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_6)
/*     */ public abstract class AbstractAnnotationValueVisitor6<R, P>
/*     */   implements AnnotationValueVisitor<R, P>
/*     */ {
/*     */   public final R visit(AnnotationValue paramAnnotationValue, P paramP)
/*     */   {
/*  86 */     return paramAnnotationValue.accept(this, paramP);
/*     */   }
/*     */ 
/*     */   public final R visit(AnnotationValue paramAnnotationValue)
/*     */   {
/*  98 */     return paramAnnotationValue.accept(this, null);
/*     */   }
/*     */ 
/*     */   public R visitUnknown(AnnotationValue paramAnnotationValue, P paramP)
/*     */   {
/* 113 */     throw new UnknownAnnotationValueException(paramAnnotationValue, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.AbstractAnnotationValueVisitor6
 * JD-Core Version:    0.6.2
 */