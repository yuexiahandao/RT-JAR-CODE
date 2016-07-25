/*    */ package java.lang.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class AnnotationTypeMismatchException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8125925355765570191L;
/*    */   private final Method element;
/*    */   private final String foundType;
/*    */ 
/*    */   public AnnotationTypeMismatchException(Method paramMethod, String paramString)
/*    */   {
/* 66 */     super("Incorrectly typed data found for annotation element " + paramMethod + " (Found data of type " + paramString + ")");
/*    */ 
/* 68 */     this.element = paramMethod;
/* 69 */     this.foundType = paramString;
/*    */   }
/*    */ 
/*    */   public Method element()
/*    */   {
/* 78 */     return this.element;
/*    */   }
/*    */ 
/*    */   public String foundType()
/*    */   {
/* 89 */     return this.foundType;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.annotation.AnnotationTypeMismatchException
 * JD-Core Version:    0.6.2
 */