/*     */ package sun.util.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.ResourceBundle.Control;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.util.LocaleDataMetaInfo;
/*     */ 
/*     */ public class LocaleData
/*     */ {
/*     */   private static final String localeDataJarName = "localedata.jar";
/*     */ 
/*     */   public static Locale[] getAvailableLocales()
/*     */   {
/*  80 */     return (Locale[])AvailableLocales.localeList.clone();
/*     */   }
/*     */ 
/*     */   public static ResourceBundle getCalendarData(Locale paramLocale)
/*     */   {
/*  88 */     return getBundle("sun.util.resources.CalendarData", paramLocale);
/*     */   }
/*     */ 
/*     */   public static OpenListResourceBundle getCurrencyNames(Locale paramLocale)
/*     */   {
/*  96 */     return (OpenListResourceBundle)getBundle("sun.util.resources.CurrencyNames", paramLocale);
/*     */   }
/*     */ 
/*     */   public static OpenListResourceBundle getLocaleNames(Locale paramLocale)
/*     */   {
/* 104 */     return (OpenListResourceBundle)getBundle("sun.util.resources.LocaleNames", paramLocale);
/*     */   }
/*     */ 
/*     */   public static OpenListResourceBundle getTimeZoneNames(Locale paramLocale)
/*     */   {
/* 112 */     return (OpenListResourceBundle)getBundle("sun.util.resources.TimeZoneNames", paramLocale);
/*     */   }
/*     */ 
/*     */   public static ResourceBundle getCollationData(Locale paramLocale)
/*     */   {
/* 120 */     return getBundle("sun.text.resources.CollationData", paramLocale);
/*     */   }
/*     */ 
/*     */   public static ResourceBundle getDateFormatData(Locale paramLocale)
/*     */   {
/* 128 */     return getBundle("sun.text.resources.FormatData", paramLocale);
/*     */   }
/*     */ 
/*     */   public static ResourceBundle getNumberFormatData(Locale paramLocale)
/*     */   {
/* 136 */     return getBundle("sun.text.resources.FormatData", paramLocale);
/*     */   }
/*     */ 
/*     */   private static ResourceBundle getBundle(String paramString, final Locale paramLocale) {
/* 140 */     return (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public ResourceBundle run() {
/* 142 */         return ResourceBundle.getBundle(this.val$baseName, paramLocale, LocaleData.LocaleDataResourceBundleControl.getRBControlInstance());
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static boolean isNonEuroLangSupported()
/*     */   {
/* 225 */     String str1 = File.separator;
/* 226 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("java.home")) + str1 + "lib" + str1 + "ext" + str1 + "localedata.jar";
/*     */ 
/* 234 */     File localFile = new File(str2);
/* 235 */     boolean bool = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Boolean run() {
/* 238 */         return Boolean.valueOf(this.val$f.exists());
/*     */       }
/*     */     })).booleanValue();
/*     */ 
/* 242 */     return bool;
/*     */   }
/*     */ 
/*     */   private static Locale[] createLocaleList()
/*     */   {
/* 252 */     String str1 = LocaleDataMetaInfo.getSupportedLocaleString("sun.text.resources.FormatData");
/*     */ 
/* 255 */     if (str1.length() == 0) {
/* 256 */       return null;
/*     */     }
/*     */ 
/* 260 */     int i = str1.indexOf("|");
/* 261 */     StringTokenizer localStringTokenizer = null;
/* 262 */     if (isNonEuroLangSupported()) {
/* 263 */       localStringTokenizer = new StringTokenizer(str1.substring(0, i) + str1.substring(i + 1));
/*     */     }
/*     */     else
/*     */     {
/* 267 */       localStringTokenizer = new StringTokenizer(str1.substring(0, i));
/*     */     }
/*     */ 
/* 271 */     Locale[] arrayOfLocale = new Locale[localStringTokenizer.countTokens()];
/* 272 */     for (int j = 0; j < arrayOfLocale.length; j++) {
/* 273 */       String str2 = localStringTokenizer.nextToken().replace('_', '-');
/* 274 */       if (str2.equals("ja-JP-JP"))
/* 275 */         str2 = "ja-JP-u-ca-japanese-x-lvariant-JP";
/* 276 */       else if (str2.equals("th-TH-TH"))
/* 277 */         str2 = "th-TH-u-nu-thai-x-lvariant-TH";
/* 278 */       else if (str2.equals("no-NO-NY")) {
/* 279 */         str2 = "no-NO-x-lvariant-NY";
/*     */       }
/* 281 */       arrayOfLocale[j] = Locale.forLanguageTag(str2);
/*     */     }
/* 283 */     return arrayOfLocale;
/*     */   }
/*     */ 
/*     */   private static class AvailableLocales
/*     */   {
/*  70 */     static final Locale[] localeList = LocaleData.access$000();
/*     */   }
/*     */ 
/*     */   static class LocaleDataResourceBundleControl extends ResourceBundle.Control
/*     */   {
/* 151 */     private static LocaleDataResourceBundleControl rbControlInstance = new LocaleDataResourceBundleControl();
/*     */ 
/*     */     public static LocaleDataResourceBundleControl getRBControlInstance()
/*     */     {
/* 155 */       return rbControlInstance;
/*     */     }
/*     */ 
/*     */     public List<Locale> getCandidateLocales(String paramString, Locale paramLocale)
/*     */     {
/* 170 */       List localList = super.getCandidateLocales(paramString, paramLocale);
/*     */ 
/* 172 */       String str1 = LocaleDataMetaInfo.getSupportedLocaleString(paramString);
/*     */ 
/* 174 */       if (str1.length() == 0) {
/* 175 */         return localList;
/*     */       }
/*     */ 
/* 178 */       for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 179 */         Locale localLocale = (Locale)localIterator.next();
/* 180 */         String str2 = null;
/* 181 */         if (localLocale.getScript().length() > 0) {
/* 182 */           str2 = localLocale.toLanguageTag().replace('-', '_');
/*     */         } else {
/* 184 */           str2 = localLocale.toString();
/* 185 */           int i = str2.indexOf("_#");
/* 186 */           if (i >= 0) {
/* 187 */             str2 = str2.substring(0, i);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 195 */         if ((str2.length() != 0) && (str1.indexOf(" " + str2 + " ") == -1)) {
/* 196 */           localIterator.remove();
/*     */         }
/*     */       }
/* 199 */       return localList;
/*     */     }
/*     */ 
/*     */     public Locale getFallbackLocale(String paramString, Locale paramLocale)
/*     */     {
/* 212 */       if ((paramString == null) || (paramLocale == null)) {
/* 213 */         throw new NullPointerException();
/*     */       }
/* 215 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.resources.LocaleData
 * JD-Core Version:    0.6.2
 */