/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementVisitor;
/*     */ import javax.lang.model.element.UnknownElementException;
/*     */ 
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_6)
/*     */ public abstract class AbstractElementVisitor6<R, P>
/*     */   implements ElementVisitor<R, P>
/*     */ {
/*     */   public final R visit(Element paramElement, P paramP)
/*     */   {
/*  90 */     return paramElement.accept(this, paramP);
/*     */   }
/*     */ 
/*     */   public final R visit(Element paramElement)
/*     */   {
/* 104 */     return paramElement.accept(this, null);
/*     */   }
/*     */ 
/*     */   public R visitUnknown(Element paramElement, P paramP)
/*     */   {
/* 122 */     throw new UnknownElementException(paramElement, paramP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.AbstractElementVisitor6
 * JD-Core Version:    0.6.2
 */