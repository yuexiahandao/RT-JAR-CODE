/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;
/*    */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*    */ import java.lang.annotation.Annotation;
/*    */ 
/*    */ public abstract class AbstractInlineAnnotationReaderImpl<T, C, F, M>
/*    */   implements AnnotationReader<T, C, F, M>
/*    */ {
/*    */   private ErrorHandler errorHandler;
/*    */ 
/*    */   public void setErrorHandler(ErrorHandler errorHandler)
/*    */   {
/* 47 */     if (errorHandler == null)
/* 48 */       throw new IllegalArgumentException();
/* 49 */     this.errorHandler = errorHandler;
/*    */   }
/*    */ 
/*    */   public final ErrorHandler getErrorHandler()
/*    */   {
/* 56 */     assert (this.errorHandler != null) : "error handler must be set before use";
/* 57 */     return this.errorHandler;
/*    */   }
/*    */ 
/*    */   public final <A extends Annotation> A getMethodAnnotation(Class<A> annotation, M getter, M setter, Locatable srcPos) {
/* 61 */     Annotation a1 = getter == null ? null : getMethodAnnotation(annotation, getter, srcPos);
/* 62 */     Annotation a2 = setter == null ? null : getMethodAnnotation(annotation, setter, srcPos);
/*    */ 
/* 64 */     if (a1 == null) {
/* 65 */       if (a2 == null) {
/* 66 */         return null;
/*    */       }
/* 68 */       return a2;
/*    */     }
/* 70 */     if (a2 == null) {
/* 71 */       return a1;
/*    */     }
/*    */ 
/* 74 */     getErrorHandler().error(new IllegalAnnotationException(Messages.DUPLICATE_ANNOTATIONS.format(new Object[] { annotation.getName(), fullName(getter), fullName(setter) }), a1, a2));
/*    */ 
/* 79 */     return a1;
/*    */   }
/*    */ 
/*    */   public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, String propertyName, M getter, M setter, Locatable srcPos)
/*    */   {
/* 85 */     boolean x = (getter != null) && (hasMethodAnnotation(annotation, getter));
/* 86 */     boolean y = (setter != null) && (hasMethodAnnotation(annotation, setter));
/*    */ 
/* 88 */     if ((x) && (y))
/*    */     {
/* 90 */       getMethodAnnotation(annotation, getter, setter, srcPos);
/*    */     }
/*    */ 
/* 93 */     return (x) || (y);
/*    */   }
/*    */ 
/*    */   protected abstract String fullName(M paramM);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl
 * JD-Core Version:    0.6.2
 */