/*    */ package java.util.spi;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public abstract class LocaleNameProvider extends LocaleServiceProvider
/*    */ {
/*    */   public abstract String getDisplayLanguage(String paramString, Locale paramLocale);
/*    */ 
/*    */   public String getDisplayScript(String paramString, Locale paramLocale)
/*    */   {
/* 98 */     return null;
/*    */   }
/*    */ 
/*    */   public abstract String getDisplayCountry(String paramString, Locale paramLocale);
/*    */ 
/*    */   public abstract String getDisplayVariant(String paramString, Locale paramLocale);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.spi.LocaleNameProvider
 * JD-Core Version:    0.6.2
 */