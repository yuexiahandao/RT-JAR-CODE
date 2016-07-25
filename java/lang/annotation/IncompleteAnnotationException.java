/*    */ package java.lang.annotation;
/*    */ 
/*    */ public class IncompleteAnnotationException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8445097402741811912L;
/*    */   private Class annotationType;
/*    */   private String elementName;
/*    */ 
/*    */   public IncompleteAnnotationException(Class<? extends Annotation> paramClass, String paramString)
/*    */   {
/* 58 */     super(paramClass.getName() + " missing element " + paramString);
/*    */ 
/* 60 */     this.annotationType = paramClass;
/* 61 */     this.elementName = paramString;
/*    */   }
/*    */ 
/*    */   public Class<? extends Annotation> annotationType()
/*    */   {
/* 72 */     return this.annotationType;
/*    */   }
/*    */ 
/*    */   public String elementName()
/*    */   {
/* 81 */     return this.elementName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.annotation.IncompleteAnnotationException
 * JD-Core Version:    0.6.2
 */