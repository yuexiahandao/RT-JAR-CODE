/*    */ package javax.jws;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Target({java.lang.annotation.ElementType.PARAMETER})
/*    */ public @interface WebParam
/*    */ {
/*    */   public abstract String name();
/*    */ 
/*    */   public abstract String partName();
/*    */ 
/*    */   public abstract String targetNamespace();
/*    */ 
/*    */   public abstract Mode mode();
/*    */ 
/*    */   public abstract boolean header();
/*    */ 
/*    */   public static enum Mode
/*    */   {
/* 44 */     IN, 
/* 45 */     OUT, 
/* 46 */     INOUT;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.jws.WebParam
 * JD-Core Version:    0.6.2
 */