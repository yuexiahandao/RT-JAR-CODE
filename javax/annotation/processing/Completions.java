/*    */ package javax.annotation.processing;
/*    */ 
/*    */ public class Completions
/*    */ {
/*    */   public static Completion of(String paramString1, String paramString2)
/*    */   {
/* 77 */     return new SimpleCompletion(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   public static Completion of(String paramString)
/*    */   {
/* 87 */     return new SimpleCompletion(paramString, "");
/*    */   }
/*    */ 
/*    */   private static class SimpleCompletion
/*    */     implements Completion
/*    */   {
/*    */     private String value;
/*    */     private String message;
/*    */ 
/*    */     SimpleCompletion(String paramString1, String paramString2)
/*    */     {
/* 47 */       if ((paramString1 == null) || (paramString2 == null))
/* 48 */         throw new NullPointerException("Null completion strings not accepted.");
/* 49 */       this.value = paramString1;
/* 50 */       this.message = paramString2;
/*    */     }
/*    */ 
/*    */     public String getValue() {
/* 54 */       return this.value;
/*    */     }
/*    */ 
/*    */     public String getMessage()
/*    */     {
/* 59 */       return this.message;
/*    */     }
/*    */ 
/*    */     public String toString()
/*    */     {
/* 64 */       return "[\"" + this.value + "\", \"" + this.message + "\"]";
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.processing.Completions
 * JD-Core Version:    0.6.2
 */