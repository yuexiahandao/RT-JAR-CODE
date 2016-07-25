/*     */ package java.util.spi;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class CurrencyNameProvider extends LocaleServiceProvider
/*     */ {
/*     */   public abstract String getSymbol(String paramString, Locale paramLocale);
/*     */ 
/*     */   public String getDisplayName(String paramString, Locale paramLocale)
/*     */   {
/*  96 */     if ((paramString == null) || (paramLocale == null)) {
/*  97 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 101 */     char[] arrayOfChar = paramString.toCharArray();
/* 102 */     if (arrayOfChar.length != 3) {
/* 103 */       throw new IllegalArgumentException("The currencyCode is not in the form of three upper-case letters.");
/*     */     }
/* 105 */     for (int k : arrayOfChar) {
/* 106 */       if ((k < 65) || (k > 90)) {
/* 107 */         throw new IllegalArgumentException("The currencyCode is not in the form of three upper-case letters.");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 112 */     ??? = Arrays.asList(getAvailableLocales());
/* 113 */     if (!((List)???).contains(paramLocale)) {
/* 114 */       throw new IllegalArgumentException("The locale is not available");
/*     */     }
/*     */ 
/* 117 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.spi.CurrencyNameProvider
 * JD-Core Version:    0.6.2
 */