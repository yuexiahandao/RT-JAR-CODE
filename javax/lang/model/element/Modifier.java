/*    */ package javax.lang.model.element;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public enum Modifier
/*    */ {
/* 52 */   PUBLIC, 
/* 53 */   PROTECTED, 
/* 54 */   PRIVATE, 
/* 55 */   ABSTRACT, 
/* 56 */   STATIC, 
/* 57 */   FINAL, 
/* 58 */   TRANSIENT, 
/* 59 */   VOLATILE, 
/* 60 */   SYNCHRONIZED, 
/* 61 */   NATIVE, 
/* 62 */   STRICTFP;
/*    */ 
/* 65 */   private String lowercase = null;
/*    */ 
/*    */   public String toString()
/*    */   {
/* 71 */     if (this.lowercase == null) {
/* 72 */       this.lowercase = name().toLowerCase(Locale.US);
/*    */     }
/* 74 */     return this.lowercase;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.Modifier
 * JD-Core Version:    0.6.2
 */