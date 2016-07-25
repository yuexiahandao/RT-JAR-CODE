/*     */ package sun.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle.Control;
/*     */ 
/*     */ public class CoreResourceBundleControl extends ResourceBundle.Control
/*     */ {
/*     */   private final Collection<Locale> excludedJDKLocales;
/*  58 */   private static CoreResourceBundleControl resourceBundleControlInstance = new CoreResourceBundleControl();
/*     */ 
/*     */   protected CoreResourceBundleControl()
/*     */   {
/*  62 */     this.excludedJDKLocales = Arrays.asList(new Locale[] { Locale.GERMANY, Locale.ENGLISH, Locale.US, new Locale("es", "ES"), Locale.FRANCE, Locale.ITALY, Locale.JAPAN, Locale.KOREA, new Locale("sv", "SE"), Locale.CHINESE });
/*     */   }
/*     */ 
/*     */   public static CoreResourceBundleControl getRBControlInstance()
/*     */   {
/*  72 */     return resourceBundleControlInstance;
/*     */   }
/*     */ 
/*     */   public static CoreResourceBundleControl getRBControlInstance(String paramString)
/*     */   {
/*  84 */     if ((paramString.startsWith("com.sun.")) || (paramString.startsWith("java.")) || (paramString.startsWith("javax.")) || (paramString.startsWith("sun.")))
/*     */     {
/*  88 */       return resourceBundleControlInstance;
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public List<Locale> getCandidateLocales(String paramString, Locale paramLocale)
/*     */   {
/* 100 */     List localList = super.getCandidateLocales(paramString, paramLocale);
/* 101 */     localList.removeAll(this.excludedJDKLocales);
/* 102 */     return localList;
/*     */   }
/*     */ 
/*     */   public long getTimeToLive(String paramString, Locale paramLocale)
/*     */   {
/* 112 */     return -1L;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.CoreResourceBundleControl
 * JD-Core Version:    0.6.2
 */