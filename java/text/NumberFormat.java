/*      */ package java.text;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.math.RoundingMode;
/*      */ import java.text.spi.NumberFormatProvider;
/*      */ import java.util.Currency;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Locale.Category;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import sun.util.LocaleServiceProviderPool;
/*      */ import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
/*      */ import sun.util.resources.LocaleData;
/*      */ 
/*      */ public abstract class NumberFormat extends Format
/*      */ {
/*      */   public static final int INTEGER_FIELD = 0;
/*      */   public static final int FRACTION_FIELD = 1;
/*  847 */   private static final Hashtable cachedLocaleData = new Hashtable(3);
/*      */   private static final int NUMBERSTYLE = 0;
/*      */   private static final int CURRENCYSTYLE = 1;
/*      */   private static final int PERCENTSTYLE = 2;
/*      */   private static final int SCIENTIFICSTYLE = 3;
/*      */   private static final int INTEGERSTYLE = 4;
/*  863 */   private boolean groupingUsed = true;
/*      */ 
/*  881 */   private byte maxIntegerDigits = 40;
/*      */ 
/*  899 */   private byte minIntegerDigits = 1;
/*      */ 
/*  917 */   private byte maxFractionDigits = 3;
/*      */ 
/*  935 */   private byte minFractionDigits = 0;
/*      */ 
/*  943 */   private boolean parseIntegerOnly = false;
/*      */ 
/*  956 */   private int maximumIntegerDigits = 40;
/*      */ 
/*  967 */   private int minimumIntegerDigits = 1;
/*      */ 
/*  978 */   private int maximumFractionDigits = 3;
/*      */ 
/*  989 */   private int minimumFractionDigits = 0;
/*      */   static final int currentSerialVersion = 1;
/* 1014 */   private int serialVersionOnStream = 1;
/*      */   static final long serialVersionUID = -2308460125733713944L;
/*      */ 
/*      */   public StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  238 */     if (((paramObject instanceof Long)) || ((paramObject instanceof Integer)) || ((paramObject instanceof Short)) || ((paramObject instanceof Byte)) || ((paramObject instanceof AtomicInteger)) || ((paramObject instanceof AtomicLong)) || (((paramObject instanceof BigInteger)) && (((BigInteger)paramObject).bitLength() < 64)))
/*      */     {
/*  243 */       return format(((Number)paramObject).longValue(), paramStringBuffer, paramFieldPosition);
/*  244 */     }if ((paramObject instanceof Number)) {
/*  245 */       return format(((Number)paramObject).doubleValue(), paramStringBuffer, paramFieldPosition);
/*      */     }
/*  247 */     throw new IllegalArgumentException("Cannot format given Object as a Number");
/*      */   }
/*      */ 
/*      */   public final Object parseObject(String paramString, ParsePosition paramParsePosition)
/*      */   {
/*  276 */     return parse(paramString, paramParsePosition);
/*      */   }
/*      */ 
/*      */   public final String format(double paramDouble)
/*      */   {
/*  286 */     return format(paramDouble, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
/*      */   }
/*      */ 
/*      */   public final String format(long paramLong)
/*      */   {
/*  297 */     return format(paramLong, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
/*      */   }
/*      */ 
/*      */   public abstract StringBuffer format(double paramDouble, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */ 
/*      */   public abstract StringBuffer format(long paramLong, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */ 
/*      */   public abstract Number parse(String paramString, ParsePosition paramParsePosition);
/*      */ 
/*      */   public Number parse(String paramString)
/*      */     throws ParseException
/*      */   {
/*  347 */     ParsePosition localParsePosition = new ParsePosition(0);
/*  348 */     Number localNumber = parse(paramString, localParsePosition);
/*  349 */     if (localParsePosition.index == 0) {
/*  350 */       throw new ParseException("Unparseable number: \"" + paramString + "\"", localParsePosition.errorIndex);
/*      */     }
/*      */ 
/*  353 */     return localNumber;
/*      */   }
/*      */ 
/*      */   public boolean isParseIntegerOnly()
/*      */   {
/*  365 */     return this.parseIntegerOnly;
/*      */   }
/*      */ 
/*      */   public void setParseIntegerOnly(boolean paramBoolean)
/*      */   {
/*  373 */     this.parseIntegerOnly = paramBoolean;
/*      */   }
/*      */ 
/*      */   public static final NumberFormat getInstance()
/*      */   {
/*  384 */     return getInstance(Locale.getDefault(Locale.Category.FORMAT), 0);
/*      */   }
/*      */ 
/*      */   public static NumberFormat getInstance(Locale paramLocale)
/*      */   {
/*  393 */     return getInstance(paramLocale, 0);
/*      */   }
/*      */ 
/*      */   public static final NumberFormat getNumberInstance()
/*      */   {
/*  400 */     return getInstance(Locale.getDefault(Locale.Category.FORMAT), 0);
/*      */   }
/*      */ 
/*      */   public static NumberFormat getNumberInstance(Locale paramLocale)
/*      */   {
/*  407 */     return getInstance(paramLocale, 0);
/*      */   }
/*      */ 
/*      */   public static final NumberFormat getIntegerInstance()
/*      */   {
/*  423 */     return getInstance(Locale.getDefault(Locale.Category.FORMAT), 4);
/*      */   }
/*      */ 
/*      */   public static NumberFormat getIntegerInstance(Locale paramLocale)
/*      */   {
/*  439 */     return getInstance(paramLocale, 4);
/*      */   }
/*      */ 
/*      */   public static final NumberFormat getCurrencyInstance()
/*      */   {
/*  446 */     return getInstance(Locale.getDefault(Locale.Category.FORMAT), 1);
/*      */   }
/*      */ 
/*      */   public static NumberFormat getCurrencyInstance(Locale paramLocale)
/*      */   {
/*  453 */     return getInstance(paramLocale, 1);
/*      */   }
/*      */ 
/*      */   public static final NumberFormat getPercentInstance()
/*      */   {
/*  460 */     return getInstance(Locale.getDefault(Locale.Category.FORMAT), 2);
/*      */   }
/*      */ 
/*      */   public static NumberFormat getPercentInstance(Locale paramLocale)
/*      */   {
/*  467 */     return getInstance(paramLocale, 2);
/*      */   }
/*      */ 
/*      */   static final NumberFormat getScientificInstance()
/*      */   {
/*  474 */     return getInstance(Locale.getDefault(Locale.Category.FORMAT), 3);
/*      */   }
/*      */ 
/*      */   static NumberFormat getScientificInstance(Locale paramLocale)
/*      */   {
/*  481 */     return getInstance(paramLocale, 3);
/*      */   }
/*      */ 
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/*  498 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(NumberFormatProvider.class);
/*      */ 
/*  500 */     return localLocaleServiceProviderPool.getAvailableLocales();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  507 */     return this.maximumIntegerDigits * 37 + this.maxFractionDigits;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  515 */     if (paramObject == null) {
/*  516 */       return false;
/*      */     }
/*  518 */     if (this == paramObject) {
/*  519 */       return true;
/*      */     }
/*  521 */     if (getClass() != paramObject.getClass()) {
/*  522 */       return false;
/*      */     }
/*  524 */     NumberFormat localNumberFormat = (NumberFormat)paramObject;
/*  525 */     return (this.maximumIntegerDigits == localNumberFormat.maximumIntegerDigits) && (this.minimumIntegerDigits == localNumberFormat.minimumIntegerDigits) && (this.maximumFractionDigits == localNumberFormat.maximumFractionDigits) && (this.minimumFractionDigits == localNumberFormat.minimumFractionDigits) && (this.groupingUsed == localNumberFormat.groupingUsed) && (this.parseIntegerOnly == localNumberFormat.parseIntegerOnly);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  537 */     NumberFormat localNumberFormat = (NumberFormat)super.clone();
/*  538 */     return localNumberFormat;
/*      */   }
/*      */ 
/*      */   public boolean isGroupingUsed()
/*      */   {
/*  549 */     return this.groupingUsed;
/*      */   }
/*      */ 
/*      */   public void setGroupingUsed(boolean paramBoolean)
/*      */   {
/*  557 */     this.groupingUsed = paramBoolean;
/*      */   }
/*      */ 
/*      */   public int getMaximumIntegerDigits()
/*      */   {
/*  566 */     return this.maximumIntegerDigits;
/*      */   }
/*      */ 
/*      */   public void setMaximumIntegerDigits(int paramInt)
/*      */   {
/*  581 */     this.maximumIntegerDigits = Math.max(0, paramInt);
/*  582 */     if (this.minimumIntegerDigits > this.maximumIntegerDigits)
/*  583 */       this.minimumIntegerDigits = this.maximumIntegerDigits;
/*      */   }
/*      */ 
/*      */   public int getMinimumIntegerDigits()
/*      */   {
/*  593 */     return this.minimumIntegerDigits;
/*      */   }
/*      */ 
/*      */   public void setMinimumIntegerDigits(int paramInt)
/*      */   {
/*  608 */     this.minimumIntegerDigits = Math.max(0, paramInt);
/*  609 */     if (this.minimumIntegerDigits > this.maximumIntegerDigits)
/*  610 */       this.maximumIntegerDigits = this.minimumIntegerDigits;
/*      */   }
/*      */ 
/*      */   public int getMaximumFractionDigits()
/*      */   {
/*  620 */     return this.maximumFractionDigits;
/*      */   }
/*      */ 
/*      */   public void setMaximumFractionDigits(int paramInt)
/*      */   {
/*  635 */     this.maximumFractionDigits = Math.max(0, paramInt);
/*  636 */     if (this.maximumFractionDigits < this.minimumFractionDigits)
/*  637 */       this.minimumFractionDigits = this.maximumFractionDigits;
/*      */   }
/*      */ 
/*      */   public int getMinimumFractionDigits()
/*      */   {
/*  647 */     return this.minimumFractionDigits;
/*      */   }
/*      */ 
/*      */   public void setMinimumFractionDigits(int paramInt)
/*      */   {
/*  662 */     this.minimumFractionDigits = Math.max(0, paramInt);
/*  663 */     if (this.maximumFractionDigits < this.minimumFractionDigits)
/*  664 */       this.maximumFractionDigits = this.minimumFractionDigits;
/*      */   }
/*      */ 
/*      */   public Currency getCurrency()
/*      */   {
/*  684 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setCurrency(Currency paramCurrency)
/*      */   {
/*  702 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public RoundingMode getRoundingMode()
/*      */   {
/*  719 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setRoundingMode(RoundingMode paramRoundingMode)
/*      */   {
/*  737 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   private static NumberFormat getInstance(Locale paramLocale, int paramInt)
/*      */   {
/*  746 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(NumberFormatProvider.class);
/*      */ 
/*  748 */     if (localLocaleServiceProviderPool.hasProviders()) {
/*  749 */       localObject1 = (NumberFormat)localLocaleServiceProviderPool.getLocalizedObject(NumberFormatGetter.INSTANCE, paramLocale, new Object[] { Integer.valueOf(paramInt) });
/*      */ 
/*  753 */       if (localObject1 != null) {
/*  754 */         return localObject1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  759 */     Object localObject1 = (String[])cachedLocaleData.get(paramLocale);
/*  760 */     if (localObject1 == null) {
/*  761 */       localObject2 = LocaleData.getNumberFormatData(paramLocale);
/*  762 */       localObject1 = ((ResourceBundle)localObject2).getStringArray("NumberPatterns");
/*      */ 
/*  764 */       cachedLocaleData.put(paramLocale, localObject1);
/*      */     }
/*      */ 
/*  767 */     Object localObject2 = DecimalFormatSymbols.getInstance(paramLocale);
/*  768 */     int i = paramInt == 4 ? 0 : paramInt;
/*  769 */     DecimalFormat localDecimalFormat = new DecimalFormat(localObject1[i], (DecimalFormatSymbols)localObject2);
/*      */ 
/*  771 */     if (paramInt == 4) {
/*  772 */       localDecimalFormat.setMaximumFractionDigits(0);
/*  773 */       localDecimalFormat.setDecimalSeparatorAlwaysShown(false);
/*  774 */       localDecimalFormat.setParseIntegerOnly(true);
/*  775 */     } else if (paramInt == 1) {
/*  776 */       localDecimalFormat.adjustForCurrencyDefaultFractionDigits();
/*      */     }
/*      */ 
/*  779 */     return localDecimalFormat;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  805 */     paramObjectInputStream.defaultReadObject();
/*  806 */     if (this.serialVersionOnStream < 1)
/*      */     {
/*  808 */       this.maximumIntegerDigits = this.maxIntegerDigits;
/*  809 */       this.minimumIntegerDigits = this.minIntegerDigits;
/*  810 */       this.maximumFractionDigits = this.maxFractionDigits;
/*  811 */       this.minimumFractionDigits = this.minFractionDigits;
/*      */     }
/*  813 */     if ((this.minimumIntegerDigits > this.maximumIntegerDigits) || (this.minimumFractionDigits > this.maximumFractionDigits) || (this.minimumIntegerDigits < 0) || (this.minimumFractionDigits < 0))
/*      */     {
/*  816 */       throw new InvalidObjectException("Digit count range invalid");
/*      */     }
/*  818 */     this.serialVersionOnStream = 1;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  833 */     this.maxIntegerDigits = (this.maximumIntegerDigits > 127 ? 127 : (byte)this.maximumIntegerDigits);
/*      */ 
/*  835 */     this.minIntegerDigits = (this.minimumIntegerDigits > 127 ? 127 : (byte)this.minimumIntegerDigits);
/*      */ 
/*  837 */     this.maxFractionDigits = (this.maximumFractionDigits > 127 ? 127 : (byte)this.maximumFractionDigits);
/*      */ 
/*  839 */     this.minFractionDigits = (this.minimumFractionDigits > 127 ? 127 : (byte)this.minimumFractionDigits);
/*      */ 
/*  841 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   public static class Field extends Format.Field
/*      */   {
/*      */     private static final long serialVersionUID = 7494728892700160890L;
/* 1038 */     private static final Map instanceMap = new HashMap(11);
/*      */ 
/* 1075 */     public static final Field INTEGER = new Field("integer");
/*      */ 
/* 1080 */     public static final Field FRACTION = new Field("fraction");
/*      */ 
/* 1085 */     public static final Field EXPONENT = new Field("exponent");
/*      */ 
/* 1090 */     public static final Field DECIMAL_SEPARATOR = new Field("decimal separator");
/*      */ 
/* 1096 */     public static final Field SIGN = new Field("sign");
/*      */ 
/* 1101 */     public static final Field GROUPING_SEPARATOR = new Field("grouping separator");
/*      */ 
/* 1107 */     public static final Field EXPONENT_SYMBOL = new Field("exponent symbol");
/*      */ 
/* 1113 */     public static final Field PERCENT = new Field("percent");
/*      */ 
/* 1118 */     public static final Field PERMILLE = new Field("per mille");
/*      */ 
/* 1123 */     public static final Field CURRENCY = new Field("currency");
/*      */ 
/* 1128 */     public static final Field EXPONENT_SIGN = new Field("exponent sign");
/*      */ 
/*      */     protected Field(String paramString)
/*      */     {
/* 1047 */       super();
/* 1048 */       if (getClass() == Field.class)
/* 1049 */         instanceMap.put(paramString, this);
/*      */     }
/*      */ 
/*      */     protected Object readResolve()
/*      */       throws InvalidObjectException
/*      */     {
/* 1060 */       if (getClass() != Field.class) {
/* 1061 */         throw new InvalidObjectException("subclass didn't correctly implement readResolve");
/*      */       }
/*      */ 
/* 1064 */       Object localObject = instanceMap.get(getName());
/* 1065 */       if (localObject != null) {
/* 1066 */         return localObject;
/*      */       }
/* 1068 */       throw new InvalidObjectException("unknown attribute name");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class NumberFormatGetter
/*      */     implements LocaleServiceProviderPool.LocalizedObjectGetter<NumberFormatProvider, NumberFormat>
/*      */   {
/* 1137 */     private static final NumberFormatGetter INSTANCE = new NumberFormatGetter();
/*      */ 
/*      */     public NumberFormat getObject(NumberFormatProvider paramNumberFormatProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */     {
/* 1143 */       assert (paramArrayOfObject.length == 1);
/* 1144 */       int i = ((Integer)paramArrayOfObject[0]).intValue();
/*      */ 
/* 1146 */       switch (i) {
/*      */       case 0:
/* 1148 */         return paramNumberFormatProvider.getNumberInstance(paramLocale);
/*      */       case 2:
/* 1150 */         return paramNumberFormatProvider.getPercentInstance(paramLocale);
/*      */       case 1:
/* 1152 */         return paramNumberFormatProvider.getCurrencyInstance(paramLocale);
/*      */       case 4:
/* 1154 */         return paramNumberFormatProvider.getIntegerInstance(paramLocale);
/*      */       case 3:
/* 1156 */       }if (!$assertionsDisabled) throw new AssertionError(i);
/*      */ 
/* 1159 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.NumberFormat
 * JD-Core Version:    0.6.2
 */