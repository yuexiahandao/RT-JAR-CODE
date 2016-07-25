/*    */ package javax.security.auth.callback;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class LanguageCallback
/*    */   implements Callback, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 2019050433478903213L;
/*    */   private Locale locale;
/*    */ 
/*    */   public void setLocale(Locale paramLocale)
/*    */   {
/* 63 */     this.locale = paramLocale;
/*    */   }
/*    */ 
/*    */   public Locale getLocale()
/*    */   {
/* 77 */     return this.locale;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.callback.LanguageCallback
 * JD-Core Version:    0.6.2
 */