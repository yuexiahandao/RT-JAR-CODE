/*    */ package javax.jws.soap;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
/*    */ public @interface SOAPBinding
/*    */ {
/*    */   public abstract Style style();
/*    */ 
/*    */   public abstract Use use();
/*    */ 
/*    */   public abstract ParameterStyle parameterStyle();
/*    */ 
/*    */   public static enum ParameterStyle
/*    */   {
/* 60 */     BARE, 
/* 61 */     WRAPPED;
/*    */   }
/*    */ 
/*    */   public static enum Style
/*    */   {
/* 44 */     DOCUMENT, 
/* 45 */     RPC;
/*    */   }
/*    */ 
/*    */   public static enum Use
/*    */   {
/* 52 */     LITERAL, 
/* 53 */     ENCODED;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.jws.soap.SOAPBinding
 * JD-Core Version:    0.6.2
 */