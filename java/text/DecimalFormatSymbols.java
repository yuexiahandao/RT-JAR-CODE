/*     */ package java.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.spi.DecimalFormatSymbolsProvider;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.Locale.Category;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import sun.util.LocaleServiceProviderPool;
/*     */ import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
/*     */ import sun.util.resources.LocaleData;
/*     */ 
/*     */ public class DecimalFormatSymbols
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private char zeroDigit;
/*     */   private char groupingSeparator;
/*     */   private char decimalSeparator;
/*     */   private char perMill;
/*     */   private char percent;
/*     */   private char digit;
/*     */   private char patternSeparator;
/*     */   private String infinity;
/*     */   private String NaN;
/*     */   private char minusSign;
/*     */   private String currencySymbol;
/*     */   private String intlCurrencySymbol;
/*     */   private char monetarySeparator;
/*     */   private char exponential;
/*     */   private String exponentialSeparator;
/*     */   private Locale locale;
/*     */   private transient Currency currency;
/*     */   static final long serialVersionUID = 5772796243397350300L;
/*     */   private static final int currentSerialVersion = 3;
/* 810 */   private int serialVersionOnStream = 3;
/*     */ 
/* 816 */   private static final ConcurrentHashMap<Locale, Object[]> cachedLocaleData = new ConcurrentHashMap(3);
/*     */ 
/*     */   public DecimalFormatSymbols()
/*     */   {
/*  79 */     initialize(Locale.getDefault(Locale.Category.FORMAT));
/*     */   }
/*     */ 
/*     */   public DecimalFormatSymbols(Locale paramLocale)
/*     */   {
/*  94 */     initialize(paramLocale);
/*     */   }
/*     */ 
/*     */   public static Locale[] getAvailableLocales()
/*     */   {
/* 112 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(DecimalFormatSymbolsProvider.class);
/*     */ 
/* 114 */     return localLocaleServiceProviderPool.getAvailableLocales();
/*     */   }
/*     */ 
/*     */   public static final DecimalFormatSymbols getInstance()
/*     */   {
/* 128 */     return getInstance(Locale.getDefault(Locale.Category.FORMAT));
/*     */   }
/*     */ 
/*     */   public static final DecimalFormatSymbols getInstance(Locale paramLocale)
/*     */   {
/* 147 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(DecimalFormatSymbolsProvider.class);
/*     */ 
/* 149 */     if (localLocaleServiceProviderPool.hasProviders()) {
/* 150 */       DecimalFormatSymbols localDecimalFormatSymbols = (DecimalFormatSymbols)localLocaleServiceProviderPool.getLocalizedObject(DecimalFormatSymbolsGetter.INSTANCE, paramLocale, new Object[0]);
/*     */ 
/* 152 */       if (localDecimalFormatSymbols != null) {
/* 153 */         return localDecimalFormatSymbols;
/*     */       }
/*     */     }
/*     */ 
/* 157 */     return new DecimalFormatSymbols(paramLocale);
/*     */   }
/*     */ 
/*     */   public char getZeroDigit()
/*     */   {
/* 164 */     return this.zeroDigit;
/*     */   }
/*     */ 
/*     */   public void setZeroDigit(char paramChar)
/*     */   {
/* 171 */     this.zeroDigit = paramChar;
/*     */   }
/*     */ 
/*     */   public char getGroupingSeparator()
/*     */   {
/* 178 */     return this.groupingSeparator;
/*     */   }
/*     */ 
/*     */   public void setGroupingSeparator(char paramChar)
/*     */   {
/* 185 */     this.groupingSeparator = paramChar;
/*     */   }
/*     */ 
/*     */   public char getDecimalSeparator()
/*     */   {
/* 192 */     return this.decimalSeparator;
/*     */   }
/*     */ 
/*     */   public void setDecimalSeparator(char paramChar)
/*     */   {
/* 199 */     this.decimalSeparator = paramChar;
/*     */   }
/*     */ 
/*     */   public char getPerMill()
/*     */   {
/* 206 */     return this.perMill;
/*     */   }
/*     */ 
/*     */   public void setPerMill(char paramChar)
/*     */   {
/* 213 */     this.perMill = paramChar;
/*     */   }
/*     */ 
/*     */   public char getPercent()
/*     */   {
/* 220 */     return this.percent;
/*     */   }
/*     */ 
/*     */   public void setPercent(char paramChar)
/*     */   {
/* 227 */     this.percent = paramChar;
/*     */   }
/*     */ 
/*     */   public char getDigit()
/*     */   {
/* 234 */     return this.digit;
/*     */   }
/*     */ 
/*     */   public void setDigit(char paramChar)
/*     */   {
/* 241 */     this.digit = paramChar;
/*     */   }
/*     */ 
/*     */   public char getPatternSeparator()
/*     */   {
/* 249 */     return this.patternSeparator;
/*     */   }
/*     */ 
/*     */   public void setPatternSeparator(char paramChar)
/*     */   {
/* 257 */     this.patternSeparator = paramChar;
/*     */   }
/*     */ 
/*     */   public String getInfinity()
/*     */   {
/* 265 */     return this.infinity;
/*     */   }
/*     */ 
/*     */   public void setInfinity(String paramString)
/*     */   {
/* 273 */     this.infinity = paramString;
/*     */   }
/*     */ 
/*     */   public String getNaN()
/*     */   {
/* 281 */     return this.NaN;
/*     */   }
/*     */ 
/*     */   public void setNaN(String paramString)
/*     */   {
/* 289 */     this.NaN = paramString;
/*     */   }
/*     */ 
/*     */   public char getMinusSign()
/*     */   {
/* 298 */     return this.minusSign;
/*     */   }
/*     */ 
/*     */   public void setMinusSign(char paramChar)
/*     */   {
/* 307 */     this.minusSign = paramChar;
/*     */   }
/*     */ 
/*     */   public String getCurrencySymbol()
/*     */   {
/* 317 */     return this.currencySymbol;
/*     */   }
/*     */ 
/*     */   public void setCurrencySymbol(String paramString)
/*     */   {
/* 327 */     this.currencySymbol = paramString;
/*     */   }
/*     */ 
/*     */   public String getInternationalCurrencySymbol()
/*     */   {
/* 337 */     return this.intlCurrencySymbol;
/*     */   }
/*     */ 
/*     */   public void setInternationalCurrencySymbol(String paramString)
/*     */   {
/* 357 */     this.intlCurrencySymbol = paramString;
/* 358 */     this.currency = null;
/* 359 */     if (paramString != null)
/*     */       try {
/* 361 */         this.currency = Currency.getInstance(paramString);
/* 362 */         this.currencySymbol = this.currency.getSymbol();
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public Currency getCurrency()
/*     */   {
/* 377 */     return this.currency;
/*     */   }
/*     */ 
/*     */   public void setCurrency(Currency paramCurrency)
/*     */   {
/* 393 */     if (paramCurrency == null) {
/* 394 */       throw new NullPointerException();
/*     */     }
/* 396 */     this.currency = paramCurrency;
/* 397 */     this.intlCurrencySymbol = paramCurrency.getCurrencyCode();
/* 398 */     this.currencySymbol = paramCurrency.getSymbol(this.locale);
/*     */   }
/*     */ 
/*     */   public char getMonetaryDecimalSeparator()
/*     */   {
/* 408 */     return this.monetarySeparator;
/*     */   }
/*     */ 
/*     */   public void setMonetaryDecimalSeparator(char paramChar)
/*     */   {
/* 417 */     this.monetarySeparator = paramChar;
/*     */   }
/*     */ 
/*     */   char getExponentialSymbol()
/*     */   {
/* 429 */     return this.exponential;
/*     */   }
/*     */ 
/*     */   public String getExponentSeparator()
/*     */   {
/* 441 */     return this.exponentialSeparator;
/*     */   }
/*     */ 
/*     */   void setExponentialSymbol(char paramChar)
/*     */   {
/* 449 */     this.exponential = paramChar;
/*     */   }
/*     */ 
/*     */   public void setExponentSeparator(String paramString)
/*     */   {
/* 463 */     if (paramString == null) {
/* 464 */       throw new NullPointerException();
/*     */     }
/* 466 */     this.exponentialSeparator = paramString;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 479 */       return (DecimalFormatSymbols)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 482 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 490 */     if (paramObject == null) return false;
/* 491 */     if (this == paramObject) return true;
/* 492 */     if (getClass() != paramObject.getClass()) return false;
/* 493 */     DecimalFormatSymbols localDecimalFormatSymbols = (DecimalFormatSymbols)paramObject;
/* 494 */     return (this.zeroDigit == localDecimalFormatSymbols.zeroDigit) && (this.groupingSeparator == localDecimalFormatSymbols.groupingSeparator) && (this.decimalSeparator == localDecimalFormatSymbols.decimalSeparator) && (this.percent == localDecimalFormatSymbols.percent) && (this.perMill == localDecimalFormatSymbols.perMill) && (this.digit == localDecimalFormatSymbols.digit) && (this.minusSign == localDecimalFormatSymbols.minusSign) && (this.patternSeparator == localDecimalFormatSymbols.patternSeparator) && (this.infinity.equals(localDecimalFormatSymbols.infinity)) && (this.NaN.equals(localDecimalFormatSymbols.NaN)) && (this.currencySymbol.equals(localDecimalFormatSymbols.currencySymbol)) && (this.intlCurrencySymbol.equals(localDecimalFormatSymbols.intlCurrencySymbol)) && (this.currency == localDecimalFormatSymbols.currency) && (this.monetarySeparator == localDecimalFormatSymbols.monetarySeparator) && (this.exponentialSeparator.equals(localDecimalFormatSymbols.exponentialSeparator)) && (this.locale.equals(localDecimalFormatSymbols.locale));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 516 */     int i = this.zeroDigit;
/* 517 */     i = i * 37 + this.groupingSeparator;
/* 518 */     i = i * 37 + this.decimalSeparator;
/* 519 */     return i;
/*     */   }
/*     */ 
/*     */   private void initialize(Locale paramLocale)
/*     */   {
/* 526 */     this.locale = paramLocale;
/*     */ 
/* 529 */     int i = 0;
/* 530 */     Object[] arrayOfObject = (Object[])cachedLocaleData.get(paramLocale);
/* 531 */     if (arrayOfObject == null)
/*     */     {
/* 534 */       localObject = paramLocale;
/* 535 */       String str = paramLocale.getUnicodeLocaleType("nu");
/* 536 */       if ((str != null) && (str.equals("thai"))) {
/* 537 */         localObject = new Locale("th", "TH", "TH");
/*     */       }
/* 539 */       arrayOfObject = new Object[3];
/* 540 */       ResourceBundle localResourceBundle = LocaleData.getNumberFormatData((Locale)localObject);
/* 541 */       arrayOfObject[0] = localResourceBundle.getStringArray("NumberElements");
/* 542 */       i = 1;
/*     */     }
/*     */ 
/* 545 */     Object localObject = (String[])arrayOfObject[0];
/*     */ 
/* 547 */     this.decimalSeparator = localObject[0].charAt(0);
/* 548 */     this.groupingSeparator = localObject[1].charAt(0);
/* 549 */     this.patternSeparator = localObject[2].charAt(0);
/* 550 */     this.percent = localObject[3].charAt(0);
/* 551 */     this.zeroDigit = localObject[4].charAt(0);
/* 552 */     this.digit = localObject[5].charAt(0);
/* 553 */     this.minusSign = localObject[6].charAt(0);
/* 554 */     this.exponential = localObject[7].charAt(0);
/* 555 */     this.exponentialSeparator = localObject[7];
/* 556 */     this.perMill = localObject[8].charAt(0);
/* 557 */     this.infinity = localObject[9];
/* 558 */     this.NaN = localObject[10];
/*     */ 
/* 564 */     if (!"".equals(paramLocale.getCountry()))
/*     */       try {
/* 566 */         this.currency = Currency.getInstance(paramLocale);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException1)
/*     */       {
/*     */       }
/* 571 */     if (this.currency != null) {
/* 572 */       this.intlCurrencySymbol = this.currency.getCurrencyCode();
/* 573 */       if ((arrayOfObject[1] != null) && (arrayOfObject[1] == this.intlCurrencySymbol)) {
/* 574 */         this.currencySymbol = ((String)arrayOfObject[2]);
/*     */       } else {
/* 576 */         this.currencySymbol = this.currency.getSymbol(paramLocale);
/* 577 */         arrayOfObject[1] = this.intlCurrencySymbol;
/* 578 */         arrayOfObject[2] = this.currencySymbol;
/* 579 */         i = 1;
/*     */       }
/*     */     }
/*     */     else {
/* 583 */       this.intlCurrencySymbol = "XXX";
/*     */       try {
/* 585 */         this.currency = Currency.getInstance(this.intlCurrencySymbol);
/*     */       } catch (IllegalArgumentException localIllegalArgumentException2) {
/*     */       }
/* 588 */       this.currencySymbol = "¤";
/*     */     }
/*     */ 
/* 593 */     this.monetarySeparator = this.decimalSeparator;
/*     */ 
/* 595 */     if (i != 0)
/* 596 */       cachedLocaleData.putIfAbsent(paramLocale, arrayOfObject);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 619 */     paramObjectInputStream.defaultReadObject();
/* 620 */     if (this.serialVersionOnStream < 1)
/*     */     {
/* 623 */       this.monetarySeparator = this.decimalSeparator;
/* 624 */       this.exponential = 'E';
/*     */     }
/* 626 */     if (this.serialVersionOnStream < 2)
/*     */     {
/* 628 */       this.locale = Locale.ROOT;
/*     */     }
/* 630 */     if (this.serialVersionOnStream < 3)
/*     */     {
/* 632 */       this.exponentialSeparator = Character.toString(this.exponential);
/*     */     }
/* 634 */     this.serialVersionOnStream = 3;
/*     */ 
/* 636 */     if (this.intlCurrencySymbol != null)
/*     */       try {
/* 638 */         this.currency = Currency.getInstance(this.intlCurrencySymbol);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   private static class DecimalFormatSymbolsGetter
/*     */     implements LocaleServiceProviderPool.LocalizedObjectGetter<DecimalFormatSymbolsProvider, DecimalFormatSymbols>
/*     */   {
/* 825 */     private static final DecimalFormatSymbolsGetter INSTANCE = new DecimalFormatSymbolsGetter();
/*     */ 
/*     */     public DecimalFormatSymbols getObject(DecimalFormatSymbolsProvider paramDecimalFormatSymbolsProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 833 */       assert (paramArrayOfObject.length == 0);
/* 834 */       return paramDecimalFormatSymbolsProvider.getInstance(paramLocale);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.DecimalFormatSymbols
 * JD-Core Version:    0.6.2
 */