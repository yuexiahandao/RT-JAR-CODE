/*     */ package java.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.spi.CurrencyNameProvider;
/*     */ import sun.util.LocaleServiceProviderPool;
/*     */ import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ import sun.util.resources.LocaleData;
/*     */ import sun.util.resources.OpenListResourceBundle;
/*     */ 
/*     */ public final class Currency
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -158308464356906721L;
/*     */   private final String currencyCode;
/*     */   private final transient int defaultFractionDigits;
/*     */   private final transient int numericCode;
/* 102 */   private static HashMap<String, Currency> instances = new HashMap(7);
/*     */   private static HashSet<Currency> available;
/*     */   static int formatVersion;
/*     */   static int dataVersion;
/*     */   static int[] mainTable;
/*     */   static long[] scCutOverTimes;
/*     */   static String[] scOldCurrencies;
/*     */   static String[] scNewCurrencies;
/*     */   static int[] scOldCurrenciesDFD;
/*     */   static int[] scNewCurrenciesDFD;
/*     */   static int[] scOldCurrenciesNumericCode;
/*     */   static int[] scNewCurrenciesNumericCode;
/*     */   static String otherCurrencies;
/*     */   static int[] otherCurrenciesDFD;
/*     */   static int[] otherCurrenciesNumericCode;
/*     */   private static final int MAGIC_NUMBER = 1131770436;
/*     */   private static final int A_TO_Z = 26;
/*     */   private static final int INVALID_COUNTRY_ENTRY = 127;
/*     */   private static final int COUNTRY_WITHOUT_CURRENCY_ENTRY = 128;
/*     */   private static final int SIMPLE_CASE_COUNTRY_MASK = 0;
/*     */   private static final int SIMPLE_CASE_COUNTRY_FINAL_CHAR_MASK = 31;
/*     */   private static final int SIMPLE_CASE_COUNTRY_DEFAULT_DIGITS_MASK = 96;
/*     */   private static final int SIMPLE_CASE_COUNTRY_DEFAULT_DIGITS_SHIFT = 5;
/*     */   private static final int SPECIAL_CASE_COUNTRY_MASK = 128;
/*     */   private static final int SPECIAL_CASE_COUNTRY_INDEX_MASK = 31;
/*     */   private static final int SPECIAL_CASE_COUNTRY_INDEX_DELTA = 1;
/*     */   private static final int COUNTRY_TYPE_MASK = 128;
/*     */   private static final int NUMERIC_CODE_MASK = 261888;
/*     */   private static final int NUMERIC_CODE_SHIFT = 8;
/*     */   private static final int VALID_FORMAT_VERSION = 1;
/*     */   private static final int SYMBOL = 0;
/*     */   private static final int DISPLAYNAME = 1;
/*     */ 
/*     */   private Currency(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 269 */     this.currencyCode = paramString;
/* 270 */     this.defaultFractionDigits = paramInt1;
/* 271 */     this.numericCode = paramInt2;
/*     */   }
/*     */ 
/*     */   public static Currency getInstance(String paramString)
/*     */   {
/* 284 */     return getInstance(paramString, -2147483648, 0);
/*     */   }
/*     */ 
/*     */   private static Currency getInstance(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 289 */     synchronized (instances)
/*     */     {
/* 293 */       Currency localCurrency = (Currency)instances.get(paramString);
/* 294 */       if (localCurrency != null) {
/* 295 */         return localCurrency;
/*     */       }
/*     */ 
/* 298 */       if (paramInt1 == -2147483648)
/*     */       {
/* 302 */         if (paramString.length() != 3) {
/* 303 */           throw new IllegalArgumentException();
/*     */         }
/* 305 */         char c1 = paramString.charAt(0);
/* 306 */         char c2 = paramString.charAt(1);
/* 307 */         int i = getMainTableEntry(c1, c2);
/* 308 */         if (((i & 0x80) == 0) && (i != 127) && (paramString.charAt(2) - 'A' == (i & 0x1F)))
/*     */         {
/* 311 */           paramInt1 = (i & 0x60) >> 5;
/* 312 */           paramInt2 = (i & 0x3FF00) >> 8;
/*     */         }
/*     */         else {
/* 315 */           if (paramString.charAt(2) == '-') {
/* 316 */             throw new IllegalArgumentException();
/*     */           }
/* 318 */           int j = otherCurrencies.indexOf(paramString);
/* 319 */           if (j == -1) {
/* 320 */             throw new IllegalArgumentException();
/*     */           }
/* 322 */           paramInt1 = otherCurrenciesDFD[(j / 4)];
/* 323 */           paramInt2 = otherCurrenciesNumericCode[(j / 4)];
/*     */         }
/*     */       }
/*     */ 
/* 327 */       localCurrency = new Currency(paramString, paramInt1, paramInt2);
/* 328 */       instances.put(paramString, localCurrency);
/* 329 */       return localCurrency;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Currency getInstance(Locale paramLocale)
/*     */   {
/* 355 */     String str = paramLocale.getCountry();
/* 356 */     if (str == null) {
/* 357 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 360 */     if (str.length() != 2) {
/* 361 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 364 */     char c1 = str.charAt(0);
/* 365 */     char c2 = str.charAt(1);
/* 366 */     int i = getMainTableEntry(c1, c2);
/* 367 */     if (((i & 0x80) == 0) && (i != 127))
/*     */     {
/* 369 */       char c3 = (char)((i & 0x1F) + 65);
/* 370 */       int k = (i & 0x60) >> 5;
/* 371 */       int m = (i & 0x3FF00) >> 8;
/* 372 */       StringBuffer localStringBuffer = new StringBuffer(str);
/* 373 */       localStringBuffer.append(c3);
/* 374 */       return getInstance(localStringBuffer.toString(), k, m);
/*     */     }
/*     */ 
/* 377 */     if (i == 127) {
/* 378 */       throw new IllegalArgumentException();
/*     */     }
/* 380 */     if (i == 128) {
/* 381 */       return null;
/*     */     }
/* 383 */     int j = (i & 0x1F) - 1;
/* 384 */     if ((scCutOverTimes[j] == 9223372036854775807L) || (System.currentTimeMillis() < scCutOverTimes[j])) {
/* 385 */       return getInstance(scOldCurrencies[j], scOldCurrenciesDFD[j], scOldCurrenciesNumericCode[j]);
/*     */     }
/*     */ 
/* 388 */     return getInstance(scNewCurrencies[j], scNewCurrenciesDFD[j], scNewCurrenciesNumericCode[j]);
/*     */   }
/*     */ 
/*     */   public static Set<Currency> getAvailableCurrencies()
/*     */   {
/* 406 */     synchronized (Currency.class) {
/* 407 */       if (available == null) {
/* 408 */         available = new HashSet(256);
/*     */ 
/* 411 */         for (char c1 = 'A'; c1 <= 'Z'; c1 = (char)(c1 + '\001')) {
/* 412 */           for (char c2 = 'A'; c2 <= 'Z'; c2 = (char)(c2 + '\001')) {
/* 413 */             int i = getMainTableEntry(c1, c2);
/* 414 */             if (((i & 0x80) == 0) && (i != 127))
/*     */             {
/* 416 */               char c3 = (char)((i & 0x1F) + 65);
/* 417 */               int j = (i & 0x60) >> 5;
/* 418 */               int k = (i & 0x3FF00) >> 8;
/* 419 */               StringBuilder localStringBuilder = new StringBuilder();
/* 420 */               localStringBuilder.append(c1);
/* 421 */               localStringBuilder.append(c2);
/* 422 */               localStringBuilder.append(c3);
/* 423 */               available.add(getInstance(localStringBuilder.toString(), j, k));
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 429 */         StringTokenizer localStringTokenizer = new StringTokenizer(otherCurrencies, "-");
/* 430 */         while (localStringTokenizer.hasMoreElements()) {
/* 431 */           available.add(getInstance((String)localStringTokenizer.nextElement()));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 437 */     ??? = (Set)available.clone();
/* 438 */     return ???;
/*     */   }
/*     */ 
/*     */   public String getCurrencyCode()
/*     */   {
/* 447 */     return this.currencyCode;
/*     */   }
/*     */ 
/*     */   public String getSymbol()
/*     */   {
/* 459 */     return getSymbol(Locale.getDefault(Locale.Category.DISPLAY));
/*     */   }
/*     */ 
/*     */   public String getSymbol(Locale paramLocale)
/*     */   {
/*     */     try
/*     */     {
/* 477 */       LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(CurrencyNameProvider.class);
/*     */ 
/* 480 */       if (localLocaleServiceProviderPool.hasProviders())
/*     */       {
/* 486 */         localObject = (String)localLocaleServiceProviderPool.getLocalizedObject(CurrencyNameGetter.INSTANCE, paramLocale, (OpenListResourceBundle)null, this.currencyCode, new Object[] { Integer.valueOf(0) });
/*     */ 
/* 490 */         if (localObject != null) {
/* 491 */           return localObject;
/*     */         }
/*     */       }
/*     */ 
/* 495 */       Object localObject = LocaleData.getCurrencyNames(paramLocale);
/* 496 */       return ((ResourceBundle)localObject).getString(this.currencyCode);
/*     */     } catch (MissingResourceException localMissingResourceException) {
/*     */     }
/* 499 */     return this.currencyCode;
/*     */   }
/*     */ 
/*     */   public int getDefaultFractionDigits()
/*     */   {
/* 513 */     return this.defaultFractionDigits;
/*     */   }
/*     */ 
/*     */   public int getNumericCode()
/*     */   {
/* 523 */     return this.numericCode;
/*     */   }
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 535 */     return getDisplayName(Locale.getDefault(Locale.Category.DISPLAY));
/*     */   }
/*     */ 
/*     */   public String getDisplayName(Locale paramLocale)
/*     */   {
/*     */     try
/*     */     {
/* 551 */       OpenListResourceBundle localOpenListResourceBundle = LocaleData.getCurrencyNames(paramLocale);
/* 552 */       String str1 = null;
/* 553 */       String str2 = this.currencyCode.toLowerCase(Locale.ROOT);
/*     */ 
/* 557 */       LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(CurrencyNameProvider.class);
/*     */ 
/* 559 */       if (localLocaleServiceProviderPool.hasProviders()) {
/* 560 */         str1 = (String)localLocaleServiceProviderPool.getLocalizedObject(CurrencyNameGetter.INSTANCE, paramLocale, str2, localOpenListResourceBundle, this.currencyCode, new Object[] { Integer.valueOf(1) });
/*     */       }
/*     */ 
/* 565 */       if (str1 == null) {
/* 566 */         str1 = localOpenListResourceBundle.getString(str2);
/*     */       }
/*     */ 
/* 569 */       if (str1 != null) {
/* 570 */         return str1;
/*     */       }
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException)
/*     */     {
/*     */     }
/*     */ 
/* 577 */     return this.currencyCode;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 586 */     return this.currencyCode;
/*     */   }
/*     */ 
/*     */   private Object readResolve()
/*     */   {
/* 593 */     return getInstance(this.currencyCode);
/*     */   }
/*     */ 
/*     */   private static int getMainTableEntry(char paramChar1, char paramChar2)
/*     */   {
/* 601 */     if ((paramChar1 < 'A') || (paramChar1 > 'Z') || (paramChar2 < 'A') || (paramChar2 > 'Z')) {
/* 602 */       throw new IllegalArgumentException();
/*     */     }
/* 604 */     return mainTable[((paramChar1 - 'A') * 26 + (paramChar2 - 'A'))];
/*     */   }
/*     */ 
/*     */   private static void setMainTableEntry(char paramChar1, char paramChar2, int paramInt)
/*     */   {
/* 612 */     if ((paramChar1 < 'A') || (paramChar1 > 'Z') || (paramChar2 < 'A') || (paramChar2 > 'Z')) {
/* 613 */       throw new IllegalArgumentException();
/*     */     }
/* 615 */     mainTable[((paramChar1 - 'A') * 26 + (paramChar2 - 'A'))] = paramInt;
/*     */   }
/*     */ 
/*     */   private static int[] readIntArray(DataInputStream paramDataInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 648 */     int[] arrayOfInt = new int[paramInt];
/* 649 */     for (int i = 0; i < paramInt; i++) {
/* 650 */       arrayOfInt[i] = paramDataInputStream.readInt();
/*     */     }
/*     */ 
/* 653 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static long[] readLongArray(DataInputStream paramDataInputStream, int paramInt) throws IOException {
/* 657 */     long[] arrayOfLong = new long[paramInt];
/* 658 */     for (int i = 0; i < paramInt; i++) {
/* 659 */       arrayOfLong[i] = paramDataInputStream.readLong();
/*     */     }
/*     */ 
/* 662 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   private static String[] readStringArray(DataInputStream paramDataInputStream, int paramInt) throws IOException {
/* 666 */     String[] arrayOfString = new String[paramInt];
/* 667 */     for (int i = 0; i < paramInt; i++) {
/* 668 */       arrayOfString[i] = paramDataInputStream.readUTF();
/*     */     }
/*     */ 
/* 671 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static void replaceCurrencyData(Pattern paramPattern, String paramString1, String paramString2)
/*     */   {
/* 687 */     if (paramString1.length() != 2)
/*     */     {
/* 689 */       localObject = "The entry in currency.properties for " + paramString1 + " is ignored because of the invalid country code.";
/*     */ 
/* 693 */       info((String)localObject, null);
/* 694 */       return;
/*     */     }
/*     */ 
/* 697 */     Object localObject = paramPattern.matcher(paramString2);
/* 698 */     if (!((Matcher)localObject).find())
/*     */     {
/* 700 */       str = "The entry in currency.properties for " + paramString1 + " is ignored because the value format is not recognized.";
/*     */ 
/* 705 */       info(str, null);
/* 706 */       return;
/*     */     }
/*     */ 
/* 709 */     String str = ((Matcher)localObject).group(1);
/* 710 */     int i = Integer.parseInt(((Matcher)localObject).group(2));
/* 711 */     int j = Integer.parseInt(((Matcher)localObject).group(3));
/* 712 */     int k = i << 8;
/*     */ 
/* 715 */     for (int m = 0; (m < scOldCurrencies.length) && 
/* 716 */       (!scOldCurrencies[m].equals(str)); m++);
/* 721 */     if (m == scOldCurrencies.length)
/*     */     {
/* 723 */       k |= j << 5 | str.charAt(2) - 'A';
/*     */     }
/*     */     else
/*     */     {
/* 727 */       k |= 0x80 | m + 1;
/*     */     }
/*     */ 
/* 730 */     setMainTableEntry(paramString1.charAt(0), paramString1.charAt(1), k);
/*     */   }
/*     */ 
/*     */   private static void info(String paramString, Throwable paramThrowable) {
/* 734 */     PlatformLogger localPlatformLogger = PlatformLogger.getLogger("java.util.Currency");
/* 735 */     if (localPlatformLogger.isLoggable(800))
/* 736 */       if (paramThrowable != null)
/* 737 */         localPlatformLogger.info(paramString, paramThrowable);
/*     */       else
/* 739 */         localPlatformLogger.info(paramString);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 192 */     AccessController.doPrivileged(new PrivilegedAction() { public Object run() { String str1 = System.getProperty("java.home");
/*     */         Object localObject1;
/*     */         try {
/* 196 */           String str2 = str1 + File.separator + "lib" + File.separator + "currency.data";
/*     */ 
/* 198 */           localObject1 = new DataInputStream(new BufferedInputStream(new FileInputStream(str2)));
/*     */ 
/* 201 */           if (((DataInputStream)localObject1).readInt() != 1131770436) {
/* 202 */             throw new InternalError("Currency data is possibly corrupted");
/*     */           }
/* 204 */           Currency.formatVersion = ((DataInputStream)localObject1).readInt();
/* 205 */           if (Currency.formatVersion != 1) {
/* 206 */             throw new InternalError("Currency data format is incorrect");
/*     */           }
/* 208 */           Currency.dataVersion = ((DataInputStream)localObject1).readInt();
/* 209 */           Currency.mainTable = Currency.readIntArray((DataInputStream)localObject1, 676);
/* 210 */           int i = ((DataInputStream)localObject1).readInt();
/* 211 */           Currency.scCutOverTimes = Currency.readLongArray((DataInputStream)localObject1, i);
/* 212 */           Currency.scOldCurrencies = Currency.readStringArray((DataInputStream)localObject1, i);
/* 213 */           Currency.scNewCurrencies = Currency.readStringArray((DataInputStream)localObject1, i);
/* 214 */           Currency.scOldCurrenciesDFD = Currency.readIntArray((DataInputStream)localObject1, i);
/* 215 */           Currency.scNewCurrenciesDFD = Currency.readIntArray((DataInputStream)localObject1, i);
/* 216 */           Currency.scOldCurrenciesNumericCode = Currency.readIntArray((DataInputStream)localObject1, i);
/* 217 */           Currency.scNewCurrenciesNumericCode = Currency.readIntArray((DataInputStream)localObject1, i);
/* 218 */           int j = ((DataInputStream)localObject1).readInt();
/* 219 */           Currency.otherCurrencies = ((DataInputStream)localObject1).readUTF();
/* 220 */           Currency.otherCurrenciesDFD = Currency.readIntArray((DataInputStream)localObject1, j);
/* 221 */           Currency.otherCurrenciesNumericCode = Currency.readIntArray((DataInputStream)localObject1, j);
/* 222 */           ((DataInputStream)localObject1).close();
/*     */         } catch (IOException localIOException1) {
/* 224 */           localObject1 = new InternalError();
/* 225 */           ((InternalError)localObject1).initCause(localIOException1);
/* 226 */           throw ((Throwable)localObject1);
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 231 */           File localFile = new File(str1 + File.separator + "lib" + File.separator + "currency.properties");
/*     */ 
/* 234 */           if (localFile.exists()) {
/* 235 */             localObject1 = new Properties();
/* 236 */             Object localObject2 = new FileReader(localFile); localObject3 = null;
/*     */             try { ((Properties)localObject1).load((Reader)localObject2); }
/*     */             catch (Throwable localThrowable2)
/*     */             {
/* 236 */               localObject3 = localThrowable2; throw localThrowable2;
/*     */             } finally {
/* 238 */               if (localObject2 != null) if (localObject3 != null) try { ((FileReader)localObject2).close(); } catch (Throwable localThrowable3) { ((Throwable)localObject3).addSuppressed(localThrowable3); } else ((FileReader)localObject2).close(); 
/*     */             }
/* 239 */             localObject2 = ((Properties)localObject1).stringPropertyNames();
/* 240 */             localObject3 = Pattern.compile("([A-Z]{3})\\s*,\\s*(\\d{3})\\s*,\\s*([0-3])");
/*     */ 
/* 242 */             for (String str3 : (Set)localObject2)
/* 243 */               Currency.replaceCurrencyData((Pattern)localObject3, str3.toUpperCase(Locale.ROOT), ((Properties)localObject1).getProperty(str3).toUpperCase(Locale.ROOT));
/*     */           }
/*     */         }
/*     */         catch (IOException localIOException2)
/*     */         {
/*     */           Object localObject3;
/* 249 */           Currency.info("currency.properties is ignored because of an IOException", localIOException2);
/*     */         }
/* 251 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static class CurrencyNameGetter
/*     */     implements LocaleServiceProviderPool.LocalizedObjectGetter<CurrencyNameProvider, String>
/*     */   {
/* 625 */     private static final CurrencyNameGetter INSTANCE = new CurrencyNameGetter();
/*     */ 
/*     */     public String getObject(CurrencyNameProvider paramCurrencyNameProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 631 */       assert (paramArrayOfObject.length == 1);
/* 632 */       int i = ((Integer)paramArrayOfObject[0]).intValue();
/*     */ 
/* 634 */       switch (i) {
/*     */       case 0:
/* 636 */         return paramCurrencyNameProvider.getSymbol(paramString, paramLocale);
/*     */       case 1:
/* 638 */         return paramCurrencyNameProvider.getDisplayName(paramString, paramLocale);
/*     */       }
/* 640 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/* 643 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Currency
 * JD-Core Version:    0.6.2
 */