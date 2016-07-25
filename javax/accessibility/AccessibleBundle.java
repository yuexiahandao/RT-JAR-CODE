/*     */ package javax.accessibility;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public abstract class AccessibleBundle
/*     */ {
/*  52 */   private static Hashtable table = new Hashtable();
/*  53 */   private final String defaultResourceBundleName = "com.sun.accessibility.internal.resources.accessibility";
/*     */ 
/*  64 */   protected String key = null;
/*     */ 
/*     */   protected String toDisplayString(String paramString, Locale paramLocale)
/*     */   {
/*  82 */     loadResourceBundle(paramString, paramLocale);
/*     */ 
/*  85 */     Object localObject = table.get(paramLocale);
/*  86 */     if ((localObject != null) && ((localObject instanceof Hashtable))) {
/*  87 */       Hashtable localHashtable = (Hashtable)localObject;
/*  88 */       localObject = localHashtable.get(this.key);
/*     */ 
/*  90 */       if ((localObject != null) && ((localObject instanceof String))) {
/*  91 */         return (String)localObject;
/*     */       }
/*     */     }
/*  94 */     return this.key;
/*     */   }
/*     */ 
/*     */   public String toDisplayString(Locale paramLocale)
/*     */   {
/* 106 */     return toDisplayString("com.sun.accessibility.internal.resources.accessibility", paramLocale);
/*     */   }
/*     */ 
/*     */   public String toDisplayString()
/*     */   {
/* 114 */     return toDisplayString(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 123 */     return toDisplayString();
/*     */   }
/*     */ 
/*     */   private void loadResourceBundle(String paramString, Locale paramLocale)
/*     */   {
/* 131 */     if (!table.contains(paramLocale))
/*     */       try
/*     */       {
/* 134 */         Hashtable localHashtable = new Hashtable();
/*     */ 
/* 136 */         ResourceBundle localResourceBundle = ResourceBundle.getBundle(paramString, paramLocale);
/*     */ 
/* 138 */         Enumeration localEnumeration = localResourceBundle.getKeys();
/* 139 */         while (localEnumeration.hasMoreElements()) {
/* 140 */           String str = (String)localEnumeration.nextElement();
/* 141 */           localHashtable.put(str, localResourceBundle.getObject(str));
/*     */         }
/*     */ 
/* 144 */         table.put(paramLocale, localHashtable);
/*     */       }
/*     */       catch (MissingResourceException localMissingResourceException) {
/* 147 */         System.err.println("loadResourceBundle: " + localMissingResourceException);
/*     */ 
/* 150 */         return;
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleBundle
 * JD-Core Version:    0.6.2
 */