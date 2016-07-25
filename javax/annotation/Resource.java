/*    */ package javax.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ @Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ public @interface Resource
/*    */ {
/*    */   public abstract String name();
/*    */ 
/*    */   public abstract String lookup();
/*    */ 
/*    */   public abstract Class type();
/*    */ 
/*    */   public abstract AuthenticationType authenticationType();
/*    */ 
/*    */   public abstract boolean shareable();
/*    */ 
/*    */   public abstract String mappedName();
/*    */ 
/*    */   public abstract String description();
/*    */ 
/*    */   public static enum AuthenticationType
/*    */   {
/* 87 */     CONTAINER, 
/* 88 */     APPLICATION;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.Resource
 * JD-Core Version:    0.6.2
 */