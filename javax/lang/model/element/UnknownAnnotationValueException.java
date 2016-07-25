/*    */ package javax.lang.model.element;
/*    */ 
/*    */ import javax.lang.model.UnknownEntityException;
/*    */ 
/*    */ public class UnknownAnnotationValueException extends UnknownEntityException
/*    */ {
/*    */   private static final long serialVersionUID = 269L;
/*    */   private transient AnnotationValue av;
/*    */   private transient Object parameter;
/*    */ 
/*    */   public UnknownAnnotationValueException(AnnotationValue paramAnnotationValue, Object paramObject)
/*    */   {
/* 63 */     super("Unknown annotation value: " + paramAnnotationValue);
/* 64 */     this.av = paramAnnotationValue;
/* 65 */     this.parameter = paramObject;
/*    */   }
/*    */ 
/*    */   public AnnotationValue getUnknownAnnotationValue()
/*    */   {
/* 76 */     return this.av;
/*    */   }
/*    */ 
/*    */   public Object getArgument()
/*    */   {
/* 85 */     return this.parameter;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.UnknownAnnotationValueException
 * JD-Core Version:    0.6.2
 */