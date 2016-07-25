/*     */ package sun.util;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.spi.TimeZoneNameProvider;
/*     */ import sun.util.calendar.ZoneInfo;
/*     */ import sun.util.resources.LocaleData;
/*     */ import sun.util.resources.OpenListResourceBundle;
/*     */ 
/*     */ public final class TimeZoneNameUtility
/*     */ {
/*  50 */   private static ConcurrentHashMap<Locale, SoftReference<OpenListResourceBundle>> cachedBundles = new ConcurrentHashMap();
/*     */ 
/*  56 */   private static ConcurrentHashMap<Locale, SoftReference<String[][]>> cachedZoneData = new ConcurrentHashMap();
/*     */ 
/*     */   public static final String[][] getZoneStrings(Locale paramLocale)
/*     */   {
/*  64 */     SoftReference localSoftReference = (SoftReference)cachedZoneData.get(paramLocale);
/*     */     String[][] arrayOfString;
/*  66 */     if ((localSoftReference == null) || ((arrayOfString = (String[][])localSoftReference.get()) == null)) {
/*  67 */       arrayOfString = loadZoneStrings(paramLocale);
/*  68 */       localSoftReference = new SoftReference(arrayOfString);
/*  69 */       cachedZoneData.put(paramLocale, localSoftReference);
/*     */     }
/*     */ 
/*  72 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static final String[][] loadZoneStrings(Locale paramLocale) {
/*  76 */     LinkedList localLinkedList = new LinkedList();
/*  77 */     OpenListResourceBundle localOpenListResourceBundle = getBundle(paramLocale);
/*  78 */     Enumeration localEnumeration = localOpenListResourceBundle.getKeys();
/*  79 */     String[] arrayOfString = null;
/*     */ 
/*  81 */     while (localEnumeration.hasMoreElements()) {
/*  82 */       localObject = (String)localEnumeration.nextElement();
/*     */ 
/*  84 */       arrayOfString = retrieveDisplayNames(localOpenListResourceBundle, (String)localObject, paramLocale);
/*  85 */       if (arrayOfString != null) {
/*  86 */         localLinkedList.add(arrayOfString);
/*     */       }
/*     */     }
/*     */ 
/*  90 */     Object localObject = new String[localLinkedList.size()][];
/*  91 */     return (String[][])localLinkedList.toArray((Object[])localObject);
/*     */   }
/*     */ 
/*     */   public static final String[] retrieveDisplayNames(String paramString, Locale paramLocale)
/*     */   {
/*  98 */     OpenListResourceBundle localOpenListResourceBundle = getBundle(paramLocale);
/*  99 */     return retrieveDisplayNames(localOpenListResourceBundle, paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   private static final String[] retrieveDisplayNames(OpenListResourceBundle paramOpenListResourceBundle, String paramString, Locale paramLocale)
/*     */   {
/* 104 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(TimeZoneNameProvider.class);
/*     */ 
/* 106 */     String[] arrayOfString = null;
/*     */ 
/* 110 */     if (localLocaleServiceProviderPool.hasProviders()) {
/* 111 */       arrayOfString = (String[])localLocaleServiceProviderPool.getLocalizedObject(TimeZoneNameGetter.INSTANCE, paramLocale, paramOpenListResourceBundle, paramString, new Object[0]);
/*     */     }
/*     */ 
/* 116 */     if (arrayOfString == null) {
/*     */       try {
/* 118 */         arrayOfString = paramOpenListResourceBundle.getStringArray(paramString);
/*     */       }
/*     */       catch (MissingResourceException localMissingResourceException)
/*     */       {
/*     */       }
/*     */     }
/* 124 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static final OpenListResourceBundle getBundle(Locale paramLocale)
/*     */   {
/* 129 */     SoftReference localSoftReference = (SoftReference)cachedBundles.get(paramLocale);
/*     */     OpenListResourceBundle localOpenListResourceBundle;
/* 131 */     if ((localSoftReference == null) || ((localOpenListResourceBundle = (OpenListResourceBundle)localSoftReference.get()) == null)) {
/* 132 */       localOpenListResourceBundle = LocaleData.getTimeZoneNames(paramLocale);
/* 133 */       localSoftReference = new SoftReference(localOpenListResourceBundle);
/* 134 */       cachedBundles.put(paramLocale, localSoftReference);
/*     */     }
/*     */ 
/* 137 */     return localOpenListResourceBundle;
/*     */   }
/*     */ 
/*     */   private static class TimeZoneNameGetter
/*     */     implements LocaleServiceProviderPool.LocalizedObjectGetter<TimeZoneNameProvider, String[]>
/*     */   {
/* 147 */     private static final TimeZoneNameGetter INSTANCE = new TimeZoneNameGetter();
/*     */ 
/*     */     public String[] getObject(TimeZoneNameProvider paramTimeZoneNameProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 154 */       assert (paramArrayOfObject.length == 0);
/* 155 */       String[] arrayOfString = null;
/* 156 */       Object localObject1 = paramString;
/*     */ 
/* 158 */       if (((String)localObject1).equals("GMT")) {
/* 159 */         arrayOfString = buildZoneStrings(paramTimeZoneNameProvider, paramLocale, (String)localObject1);
/*     */       } else {
/* 161 */         Map localMap = ZoneInfo.getAliasTable();
/*     */ 
/* 163 */         if (localMap != null)
/*     */         {
/* 166 */           if (localMap.containsKey(localObject1)) {
/* 167 */             Object localObject2 = localObject1;
/* 168 */             while ((localObject1 = (String)localMap.get(localObject1)) != null) {
/* 169 */               localObject2 = localObject1;
/*     */             }
/* 171 */             localObject1 = localObject2;
/*     */           }
/*     */ 
/* 174 */           arrayOfString = buildZoneStrings(paramTimeZoneNameProvider, paramLocale, (String)localObject1);
/*     */ 
/* 176 */           if (arrayOfString == null)
/*     */           {
/* 179 */             arrayOfString = examineAliases(paramTimeZoneNameProvider, paramLocale, (String)localObject1, localMap, localMap.entrySet());
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 185 */       if (arrayOfString != null) {
/* 186 */         arrayOfString[0] = paramString;
/*     */       }
/*     */ 
/* 189 */       return arrayOfString;
/*     */     }
/*     */ 
/*     */     private static String[] examineAliases(TimeZoneNameProvider paramTimeZoneNameProvider, Locale paramLocale, String paramString, Map<String, String> paramMap, Set<Map.Entry<String, String>> paramSet)
/*     */     {
/* 196 */       if (paramMap.containsValue(paramString)) {
/* 197 */         for (Map.Entry localEntry : paramSet) {
/* 198 */           if (((String)localEntry.getValue()).equals(paramString)) {
/* 199 */             String str = (String)localEntry.getKey();
/* 200 */             String[] arrayOfString = buildZoneStrings(paramTimeZoneNameProvider, paramLocale, str);
/* 201 */             if (arrayOfString != null) {
/* 202 */               return arrayOfString;
/*     */             }
/* 204 */             arrayOfString = examineAliases(paramTimeZoneNameProvider, paramLocale, str, paramMap, paramSet);
/* 205 */             if (arrayOfString != null) {
/* 206 */               return arrayOfString;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 213 */       return null;
/*     */     }
/*     */ 
/*     */     private static String[] buildZoneStrings(TimeZoneNameProvider paramTimeZoneNameProvider, Locale paramLocale, String paramString)
/*     */     {
/* 218 */       String[] arrayOfString = new String[5];
/*     */ 
/* 220 */       for (int i = 1; i <= 4; i++) {
/* 221 */         arrayOfString[i] = paramTimeZoneNameProvider.getDisplayName(paramString, i >= 3 ? 1 : false, i % 2, paramLocale);
/* 222 */         if ((i >= 3) && (arrayOfString[i] == null)) {
/* 223 */           arrayOfString[i] = arrayOfString[(i - 2)];
/*     */         }
/*     */       }
/*     */ 
/* 227 */       if (arrayOfString[1] == null)
/*     */       {
/* 229 */         arrayOfString = null;
/*     */       }
/*     */ 
/* 232 */       return arrayOfString;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.TimeZoneNameUtility
 * JD-Core Version:    0.6.2
 */