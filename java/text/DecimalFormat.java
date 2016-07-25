/*      */ package java.text;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Currency;
/*      */ import java.util.Locale;
/*      */ import java.util.Locale.Category;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import sun.util.resources.LocaleData;
/*      */ 
/*      */ public class DecimalFormat extends NumberFormat
/*      */ {
/*      */   private transient BigInteger bigIntegerMultiplier;
/*      */   private transient BigDecimal bigDecimalMultiplier;
/*      */   private static final int STATUS_INFINITE = 0;
/*      */   private static final int STATUS_POSITIVE = 1;
/*      */   private static final int STATUS_LENGTH = 2;
/* 2959 */   private transient DigitList digitList = new DigitList();
/*      */ 
/* 2967 */   private String positivePrefix = "";
/*      */ 
/* 2976 */   private String positiveSuffix = "";
/*      */ 
/* 2984 */   private String negativePrefix = "-";
/*      */ 
/* 2993 */   private String negativeSuffix = "";
/*      */   private String posPrefixPattern;
/*      */   private String posSuffixPattern;
/*      */   private String negPrefixPattern;
/*      */   private String negSuffixPattern;
/* 3051 */   private int multiplier = 1;
/*      */ 
/* 3062 */   private byte groupingSize = 3;
/*      */ 
/* 3071 */   private boolean decimalSeparatorAlwaysShown = false;
/*      */ 
/* 3080 */   private boolean parseBigDecimal = false;
/*      */ 
/* 3087 */   private transient boolean isCurrencyFormat = false;
/*      */ 
/* 3098 */   private DecimalFormatSymbols symbols = null;
/*      */   private boolean useExponentialNotation;
/*      */   private transient FieldPosition[] positivePrefixFieldPositions;
/*      */   private transient FieldPosition[] positiveSuffixFieldPositions;
/*      */   private transient FieldPosition[] negativePrefixFieldPositions;
/*      */   private transient FieldPosition[] negativeSuffixFieldPositions;
/*      */   private byte minExponentDigits;
/* 3157 */   private int maximumIntegerDigits = super.getMaximumIntegerDigits();
/*      */ 
/* 3169 */   private int minimumIntegerDigits = super.getMinimumIntegerDigits();
/*      */ 
/* 3181 */   private int maximumFractionDigits = super.getMaximumFractionDigits();
/*      */ 
/* 3193 */   private int minimumFractionDigits = super.getMinimumFractionDigits();
/*      */ 
/* 3201 */   private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
/*      */   static final int currentSerialVersion = 4;
/* 3230 */   private int serialVersionOnStream = 4;
/*      */   private static final char PATTERN_ZERO_DIGIT = '0';
/*      */   private static final char PATTERN_GROUPING_SEPARATOR = ',';
/*      */   private static final char PATTERN_DECIMAL_SEPARATOR = '.';
/*      */   private static final char PATTERN_PER_MILLE = '‰';
/*      */   private static final char PATTERN_PERCENT = '%';
/*      */   private static final char PATTERN_DIGIT = '#';
/*      */   private static final char PATTERN_SEPARATOR = ';';
/*      */   private static final String PATTERN_EXPONENT = "E";
/*      */   private static final char PATTERN_MINUS = '-';
/*      */   private static final char CURRENCY_SIGN = '¤';
/*      */   private static final char QUOTE = '\'';
/* 3260 */   private static FieldPosition[] EmptyFieldPositionArray = new FieldPosition[0];
/*      */   static final int DOUBLE_INTEGER_DIGITS = 309;
/*      */   static final int DOUBLE_FRACTION_DIGITS = 340;
/*      */   static final int MAXIMUM_INTEGER_DIGITS = 2147483647;
/*      */   static final int MAXIMUM_FRACTION_DIGITS = 2147483647;
/*      */   static final long serialVersionUID = 864413376551465018L;
/* 3276 */   private static final ConcurrentMap<Locale, String> cachedLocaleData = new ConcurrentHashMap(3);
/*      */ 
/*      */   public DecimalFormat()
/*      */   {
/*  396 */     Locale localLocale = Locale.getDefault(Locale.Category.FORMAT);
/*      */ 
/*  398 */     String str = (String)cachedLocaleData.get(localLocale);
/*  399 */     if (str == null)
/*      */     {
/*  401 */       ResourceBundle localResourceBundle = LocaleData.getNumberFormatData(localLocale);
/*  402 */       String[] arrayOfString = localResourceBundle.getStringArray("NumberPatterns");
/*  403 */       str = arrayOfString[0];
/*      */ 
/*  405 */       cachedLocaleData.putIfAbsent(localLocale, str);
/*      */     }
/*      */ 
/*  409 */     this.symbols = new DecimalFormatSymbols(localLocale);
/*  410 */     applyPattern(str, false);
/*      */   }
/*      */ 
/*      */   public DecimalFormat(String paramString)
/*      */   {
/*  434 */     this.symbols = new DecimalFormatSymbols(Locale.getDefault(Locale.Category.FORMAT));
/*  435 */     applyPattern(paramString, false);
/*      */   }
/*      */ 
/*      */   public DecimalFormat(String paramString, DecimalFormatSymbols paramDecimalFormatSymbols)
/*      */   {
/*  462 */     this.symbols = ((DecimalFormatSymbols)paramDecimalFormatSymbols.clone());
/*  463 */     applyPattern(paramString, false);
/*      */   }
/*      */ 
/*      */   public final StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  491 */     if (((paramObject instanceof Long)) || ((paramObject instanceof Integer)) || ((paramObject instanceof Short)) || ((paramObject instanceof Byte)) || ((paramObject instanceof AtomicInteger)) || ((paramObject instanceof AtomicLong)) || (((paramObject instanceof BigInteger)) && (((BigInteger)paramObject).bitLength() < 64)))
/*      */     {
/*  497 */       return format(((Number)paramObject).longValue(), paramStringBuffer, paramFieldPosition);
/*  498 */     }if ((paramObject instanceof BigDecimal))
/*  499 */       return format((BigDecimal)paramObject, paramStringBuffer, paramFieldPosition);
/*  500 */     if ((paramObject instanceof BigInteger))
/*  501 */       return format((BigInteger)paramObject, paramStringBuffer, paramFieldPosition);
/*  502 */     if ((paramObject instanceof Number)) {
/*  503 */       return format(((Number)paramObject).doubleValue(), paramStringBuffer, paramFieldPosition);
/*      */     }
/*  505 */     throw new IllegalArgumentException("Cannot format given Object as a Number");
/*      */   }
/*      */ 
/*      */   public StringBuffer format(double paramDouble, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  522 */     paramFieldPosition.setBeginIndex(0);
/*  523 */     paramFieldPosition.setEndIndex(0);
/*      */ 
/*  525 */     return format(paramDouble, paramStringBuffer, paramFieldPosition.getFieldDelegate());
/*      */   }
/*      */ 
/*      */   private StringBuffer format(double paramDouble, StringBuffer paramStringBuffer, Format.FieldDelegate paramFieldDelegate)
/*      */   {
/*  539 */     if ((Double.isNaN(paramDouble)) || ((Double.isInfinite(paramDouble)) && (this.multiplier == 0)))
/*      */     {
/*  541 */       i = paramStringBuffer.length();
/*  542 */       paramStringBuffer.append(this.symbols.getNaN());
/*  543 */       paramFieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, i, paramStringBuffer.length(), paramStringBuffer);
/*      */ 
/*  545 */       return paramStringBuffer;
/*      */     }
/*      */ 
/*  558 */     int i = ((paramDouble < 0.0D) || ((paramDouble == 0.0D) && (1.0D / paramDouble < 0.0D)) ? 1 : 0) ^ (this.multiplier < 0 ? 1 : 0);
/*      */ 
/*  560 */     if (this.multiplier != 1) {
/*  561 */       paramDouble *= this.multiplier;
/*      */     }
/*      */ 
/*  564 */     if (Double.isInfinite(paramDouble)) {
/*  565 */       if (i != 0) {
/*  566 */         append(paramStringBuffer, this.negativePrefix, paramFieldDelegate, getNegativePrefixFieldPositions(), NumberFormat.Field.SIGN);
/*      */       }
/*      */       else {
/*  569 */         append(paramStringBuffer, this.positivePrefix, paramFieldDelegate, getPositivePrefixFieldPositions(), NumberFormat.Field.SIGN);
/*      */       }
/*      */ 
/*  573 */       int j = paramStringBuffer.length();
/*  574 */       paramStringBuffer.append(this.symbols.getInfinity());
/*  575 */       paramFieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, j, paramStringBuffer.length(), paramStringBuffer);
/*      */ 
/*  578 */       if (i != 0) {
/*  579 */         append(paramStringBuffer, this.negativeSuffix, paramFieldDelegate, getNegativeSuffixFieldPositions(), NumberFormat.Field.SIGN);
/*      */       }
/*      */       else {
/*  582 */         append(paramStringBuffer, this.positiveSuffix, paramFieldDelegate, getPositiveSuffixFieldPositions(), NumberFormat.Field.SIGN);
/*      */       }
/*      */ 
/*  586 */       return paramStringBuffer;
/*      */     }
/*      */ 
/*  589 */     if (i != 0) {
/*  590 */       paramDouble = -paramDouble;
/*      */     }
/*      */ 
/*  594 */     assert ((paramDouble >= 0.0D) && (!Double.isInfinite(paramDouble)));
/*      */ 
/*  596 */     synchronized (this.digitList) {
/*  597 */       int k = super.getMaximumIntegerDigits();
/*  598 */       int m = super.getMinimumIntegerDigits();
/*  599 */       int n = super.getMaximumFractionDigits();
/*  600 */       int i1 = super.getMinimumFractionDigits();
/*      */ 
/*  602 */       this.digitList.set(i, paramDouble, this.useExponentialNotation ? k + n : n, !this.useExponentialNotation);
/*      */ 
/*  605 */       return subformat(paramStringBuffer, paramFieldDelegate, i, false, k, m, n, i1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public StringBuffer format(long paramLong, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  623 */     paramFieldPosition.setBeginIndex(0);
/*  624 */     paramFieldPosition.setEndIndex(0);
/*      */ 
/*  626 */     return format(paramLong, paramStringBuffer, paramFieldPosition.getFieldDelegate());
/*      */   }
/*      */ 
/*      */   private StringBuffer format(long paramLong, StringBuffer paramStringBuffer, Format.FieldDelegate paramFieldDelegate)
/*      */   {
/*  641 */     boolean bool = paramLong < 0L;
/*  642 */     if (bool) {
/*  643 */       paramLong = -paramLong;
/*      */     }
/*      */ 
/*  652 */     int i = 0;
/*  653 */     if (paramLong < 0L) {
/*  654 */       if (this.multiplier != 0)
/*  655 */         i = 1;
/*      */     }
/*  657 */     else if ((this.multiplier != 1) && (this.multiplier != 0)) {
/*  658 */       long l = 9223372036854775807L / this.multiplier;
/*  659 */       if (l < 0L) {
/*  660 */         l = -l;
/*      */       }
/*  662 */       i = paramLong > l ? 1 : 0;
/*      */     }
/*      */ 
/*  665 */     if (i != 0) {
/*  666 */       if (bool) {
/*  667 */         paramLong = -paramLong;
/*      */       }
/*  669 */       BigInteger localBigInteger = BigInteger.valueOf(paramLong);
/*  670 */       return format(localBigInteger, paramStringBuffer, paramFieldDelegate, true);
/*      */     }
/*      */ 
/*  673 */     paramLong *= this.multiplier;
/*  674 */     if (paramLong == 0L) {
/*  675 */       bool = false;
/*      */     }
/*  677 */     else if (this.multiplier < 0) {
/*  678 */       paramLong = -paramLong;
/*  679 */       bool = !bool;
/*      */     }
/*      */ 
/*  683 */     synchronized (this.digitList) {
/*  684 */       int j = super.getMaximumIntegerDigits();
/*  685 */       int k = super.getMinimumIntegerDigits();
/*  686 */       int m = super.getMaximumFractionDigits();
/*  687 */       int n = super.getMinimumFractionDigits();
/*      */ 
/*  689 */       this.digitList.set(bool, paramLong, this.useExponentialNotation ? j + m : 0);
/*      */ 
/*  692 */       return subformat(paramStringBuffer, paramFieldDelegate, bool, true, j, k, m, n);
/*      */     }
/*      */   }
/*      */ 
/*      */   private StringBuffer format(BigDecimal paramBigDecimal, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  710 */     paramFieldPosition.setBeginIndex(0);
/*  711 */     paramFieldPosition.setEndIndex(0);
/*  712 */     return format(paramBigDecimal, paramStringBuffer, paramFieldPosition.getFieldDelegate());
/*      */   }
/*      */ 
/*      */   private StringBuffer format(BigDecimal paramBigDecimal, StringBuffer paramStringBuffer, Format.FieldDelegate paramFieldDelegate)
/*      */   {
/*  726 */     if (this.multiplier != 1) {
/*  727 */       paramBigDecimal = paramBigDecimal.multiply(getBigDecimalMultiplier());
/*      */     }
/*  729 */     boolean bool = paramBigDecimal.signum() == -1;
/*  730 */     if (bool) {
/*  731 */       paramBigDecimal = paramBigDecimal.negate();
/*      */     }
/*      */ 
/*  734 */     synchronized (this.digitList) {
/*  735 */       int i = getMaximumIntegerDigits();
/*  736 */       int j = getMinimumIntegerDigits();
/*  737 */       int k = getMaximumFractionDigits();
/*  738 */       int m = getMinimumFractionDigits();
/*  739 */       int n = i + k;
/*      */ 
/*  741 */       this.digitList.set(bool, paramBigDecimal, this.useExponentialNotation ? n : n < 0 ? 2147483647 : k, !this.useExponentialNotation);
/*      */ 
/*  745 */       return subformat(paramStringBuffer, paramFieldDelegate, bool, false, i, j, k, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   private StringBuffer format(BigInteger paramBigInteger, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  763 */     paramFieldPosition.setBeginIndex(0);
/*  764 */     paramFieldPosition.setEndIndex(0);
/*      */ 
/*  766 */     return format(paramBigInteger, paramStringBuffer, paramFieldPosition.getFieldDelegate(), false);
/*      */   }
/*      */ 
/*      */   private StringBuffer format(BigInteger paramBigInteger, StringBuffer paramStringBuffer, Format.FieldDelegate paramFieldDelegate, boolean paramBoolean)
/*      */   {
/*  781 */     if (this.multiplier != 1) {
/*  782 */       paramBigInteger = paramBigInteger.multiply(getBigIntegerMultiplier());
/*      */     }
/*  784 */     boolean bool = paramBigInteger.signum() == -1;
/*  785 */     if (bool) {
/*  786 */       paramBigInteger = paramBigInteger.negate();
/*      */     }
/*      */ 
/*  789 */     synchronized (this.digitList)
/*      */     {
/*      */       int i;
/*      */       int j;
/*      */       int k;
/*      */       int m;
/*      */       int n;
/*  791 */       if (paramBoolean) {
/*  792 */         i = super.getMaximumIntegerDigits();
/*  793 */         j = super.getMinimumIntegerDigits();
/*  794 */         k = super.getMaximumFractionDigits();
/*  795 */         m = super.getMinimumFractionDigits();
/*  796 */         n = i + k;
/*      */       } else {
/*  798 */         i = getMaximumIntegerDigits();
/*  799 */         j = getMinimumIntegerDigits();
/*  800 */         k = getMaximumFractionDigits();
/*  801 */         m = getMinimumFractionDigits();
/*  802 */         n = i + k;
/*  803 */         if (n < 0) {
/*  804 */           n = 2147483647;
/*      */         }
/*      */       }
/*      */ 
/*  808 */       this.digitList.set(bool, paramBigInteger, this.useExponentialNotation ? n : 0);
/*      */ 
/*  811 */       return subformat(paramStringBuffer, paramFieldDelegate, bool, true, i, j, k, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributedCharacterIterator formatToCharacterIterator(Object paramObject)
/*      */   {
/*  836 */     CharacterIteratorFieldDelegate localCharacterIteratorFieldDelegate = new CharacterIteratorFieldDelegate();
/*      */ 
/*  838 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  840 */     if (((paramObject instanceof Double)) || ((paramObject instanceof Float))) {
/*  841 */       format(((Number)paramObject).doubleValue(), localStringBuffer, localCharacterIteratorFieldDelegate);
/*  842 */     } else if (((paramObject instanceof Long)) || ((paramObject instanceof Integer)) || ((paramObject instanceof Short)) || ((paramObject instanceof Byte)) || ((paramObject instanceof AtomicInteger)) || ((paramObject instanceof AtomicLong)))
/*      */     {
/*  845 */       format(((Number)paramObject).longValue(), localStringBuffer, localCharacterIteratorFieldDelegate);
/*  846 */     } else if ((paramObject instanceof BigDecimal)) {
/*  847 */       format((BigDecimal)paramObject, localStringBuffer, localCharacterIteratorFieldDelegate);
/*  848 */     } else if ((paramObject instanceof BigInteger)) {
/*  849 */       format((BigInteger)paramObject, localStringBuffer, localCharacterIteratorFieldDelegate, false); } else {
/*  850 */       if (paramObject == null) {
/*  851 */         throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
/*      */       }
/*      */ 
/*  854 */       throw new IllegalArgumentException("Cannot format given Object as a Number");
/*      */     }
/*      */ 
/*  857 */     return localCharacterIteratorFieldDelegate.getIterator(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   private StringBuffer subformat(StringBuffer paramStringBuffer, Format.FieldDelegate paramFieldDelegate, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  882 */     char c1 = this.symbols.getZeroDigit();
/*  883 */     int i = c1 - '0';
/*  884 */     char c2 = this.symbols.getGroupingSeparator();
/*  885 */     char c3 = this.isCurrencyFormat ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
/*      */ 
/*  895 */     if (this.digitList.isZero()) {
/*  896 */       this.digitList.decimalAt = 0;
/*      */     }
/*      */ 
/*  899 */     if (paramBoolean1) {
/*  900 */       append(paramStringBuffer, this.negativePrefix, paramFieldDelegate, getNegativePrefixFieldPositions(), NumberFormat.Field.SIGN);
/*      */     }
/*      */     else
/*  903 */       append(paramStringBuffer, this.positivePrefix, paramFieldDelegate, getPositivePrefixFieldPositions(), NumberFormat.Field.SIGN);
/*      */     int j;
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*      */     int i1;
/*      */     int i2;
/*      */     int i3;
/*      */     int i4;
/*  907 */     if (this.useExponentialNotation) {
/*  908 */       j = paramStringBuffer.length();
/*  909 */       k = -1;
/*  910 */       m = -1;
/*      */ 
/*  923 */       n = this.digitList.decimalAt;
/*  924 */       i1 = paramInt1;
/*  925 */       i2 = paramInt2;
/*  926 */       if ((i1 > 1) && (i1 > paramInt2))
/*      */       {
/*  932 */         if (n >= 1) {
/*  933 */           n = (n - 1) / i1 * i1;
/*      */         }
/*      */         else {
/*  936 */           n = (n - i1) / i1 * i1;
/*      */         }
/*  938 */         i2 = 1;
/*      */       }
/*      */       else {
/*  941 */         n -= i2;
/*      */       }
/*      */ 
/*  948 */       i3 = paramInt2 + paramInt4;
/*  949 */       if (i3 < 0) {
/*  950 */         i3 = 2147483647;
/*      */       }
/*      */ 
/*  955 */       i4 = this.digitList.isZero() ? i2 : this.digitList.decimalAt - n;
/*      */ 
/*  957 */       if (i3 < i4) {
/*  958 */         i3 = i4;
/*      */       }
/*  960 */       int i5 = this.digitList.count;
/*  961 */       if (i3 > i5) {
/*  962 */         i5 = i3;
/*      */       }
/*  964 */       int i6 = 0;
/*      */ 
/*  966 */       for (int i7 = 0; i7 < i5; i7++) {
/*  967 */         if (i7 == i4)
/*      */         {
/*  969 */           k = paramStringBuffer.length();
/*      */ 
/*  971 */           paramStringBuffer.append(c3);
/*  972 */           i6 = 1;
/*      */ 
/*  975 */           m = paramStringBuffer.length();
/*      */         }
/*  977 */         paramStringBuffer.append(i7 < this.digitList.count ? (char)(this.digitList.digits[i7] + i) : c1);
/*      */       }
/*      */ 
/*  982 */       if ((this.decimalSeparatorAlwaysShown) && (i5 == i4))
/*      */       {
/*  984 */         k = paramStringBuffer.length();
/*      */ 
/*  986 */         paramStringBuffer.append(c3);
/*  987 */         i6 = 1;
/*      */ 
/*  990 */         m = paramStringBuffer.length();
/*      */       }
/*      */ 
/*  994 */       if (k == -1) {
/*  995 */         k = paramStringBuffer.length();
/*      */       }
/*  997 */       paramFieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, j, k, paramStringBuffer);
/*      */ 
/*  999 */       if (i6 != 0) {
/* 1000 */         paramFieldDelegate.formatted(NumberFormat.Field.DECIMAL_SEPARATOR, NumberFormat.Field.DECIMAL_SEPARATOR, k, m, paramStringBuffer);
/*      */       }
/*      */ 
/* 1004 */       if (m == -1) {
/* 1005 */         m = paramStringBuffer.length();
/*      */       }
/* 1007 */       paramFieldDelegate.formatted(1, NumberFormat.Field.FRACTION, NumberFormat.Field.FRACTION, m, paramStringBuffer.length(), paramStringBuffer);
/*      */ 
/* 1014 */       i7 = paramStringBuffer.length();
/*      */ 
/* 1016 */       paramStringBuffer.append(this.symbols.getExponentSeparator());
/*      */ 
/* 1018 */       paramFieldDelegate.formatted(NumberFormat.Field.EXPONENT_SYMBOL, NumberFormat.Field.EXPONENT_SYMBOL, i7, paramStringBuffer.length(), paramStringBuffer);
/*      */ 
/* 1024 */       if (this.digitList.isZero()) {
/* 1025 */         n = 0;
/*      */       }
/*      */ 
/* 1028 */       boolean bool = n < 0;
/* 1029 */       if (bool) {
/* 1030 */         n = -n;
/* 1031 */         i7 = paramStringBuffer.length();
/* 1032 */         paramStringBuffer.append(this.symbols.getMinusSign());
/* 1033 */         paramFieldDelegate.formatted(NumberFormat.Field.EXPONENT_SIGN, NumberFormat.Field.EXPONENT_SIGN, i7, paramStringBuffer.length(), paramStringBuffer);
/*      */       }
/*      */ 
/* 1036 */       this.digitList.set(bool, n);
/*      */ 
/* 1038 */       int i8 = paramStringBuffer.length();
/*      */ 
/* 1040 */       for (int i9 = this.digitList.decimalAt; i9 < this.minExponentDigits; i9++) {
/* 1041 */         paramStringBuffer.append(c1);
/*      */       }
/* 1043 */       for (i9 = 0; i9 < this.digitList.decimalAt; i9++) {
/* 1044 */         paramStringBuffer.append(i9 < this.digitList.count ? (char)(this.digitList.digits[i9] + i) : c1);
/*      */       }
/*      */ 
/* 1047 */       paramFieldDelegate.formatted(NumberFormat.Field.EXPONENT, NumberFormat.Field.EXPONENT, i8, paramStringBuffer.length(), paramStringBuffer);
/*      */     }
/*      */     else {
/* 1050 */       j = paramStringBuffer.length();
/*      */ 
/* 1056 */       k = paramInt2;
/* 1057 */       m = 0;
/* 1058 */       if ((this.digitList.decimalAt > 0) && (k < this.digitList.decimalAt)) {
/* 1059 */         k = this.digitList.decimalAt;
/*      */       }
/*      */ 
/* 1066 */       if (k > paramInt1) {
/* 1067 */         k = paramInt1;
/* 1068 */         m = this.digitList.decimalAt - k;
/*      */       }
/*      */ 
/* 1071 */       n = paramStringBuffer.length();
/* 1072 */       for (i1 = k - 1; i1 >= 0; i1--) {
/* 1073 */         if ((i1 < this.digitList.decimalAt) && (m < this.digitList.count))
/*      */         {
/* 1075 */           paramStringBuffer.append((char)(this.digitList.digits[(m++)] + i));
/*      */         }
/*      */         else {
/* 1078 */           paramStringBuffer.append(c1);
/*      */         }
/*      */ 
/* 1084 */         if ((isGroupingUsed()) && (i1 > 0) && (this.groupingSize != 0) && (i1 % this.groupingSize == 0))
/*      */         {
/* 1086 */           i2 = paramStringBuffer.length();
/* 1087 */           paramStringBuffer.append(c2);
/* 1088 */           paramFieldDelegate.formatted(NumberFormat.Field.GROUPING_SEPARATOR, NumberFormat.Field.GROUPING_SEPARATOR, i2, paramStringBuffer.length(), paramStringBuffer);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1096 */       i1 = (paramInt4 > 0) || ((!paramBoolean2) && (m < this.digitList.count)) ? 1 : 0;
/*      */ 
/* 1102 */       if ((i1 == 0) && (paramStringBuffer.length() == n)) {
/* 1103 */         paramStringBuffer.append(c1);
/*      */       }
/*      */ 
/* 1106 */       paramFieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, j, paramStringBuffer.length(), paramStringBuffer);
/*      */ 
/* 1110 */       i2 = paramStringBuffer.length();
/* 1111 */       if ((this.decimalSeparatorAlwaysShown) || (i1 != 0)) {
/* 1112 */         paramStringBuffer.append(c3);
/*      */       }
/*      */ 
/* 1115 */       if (i2 != paramStringBuffer.length()) {
/* 1116 */         paramFieldDelegate.formatted(NumberFormat.Field.DECIMAL_SEPARATOR, NumberFormat.Field.DECIMAL_SEPARATOR, i2, paramStringBuffer.length(), paramStringBuffer);
/*      */       }
/*      */ 
/* 1120 */       i3 = paramStringBuffer.length();
/*      */ 
/* 1122 */       for (i4 = 0; i4 < paramInt3; i4++)
/*      */       {
/* 1129 */         if ((i4 >= paramInt4) && ((paramBoolean2) || (m >= this.digitList.count)))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/* 1137 */         if (-1 - i4 > this.digitList.decimalAt - 1) {
/* 1138 */           paramStringBuffer.append(c1);
/*      */         }
/* 1144 */         else if ((!paramBoolean2) && (m < this.digitList.count))
/* 1145 */           paramStringBuffer.append((char)(this.digitList.digits[(m++)] + i));
/*      */         else {
/* 1147 */           paramStringBuffer.append(c1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1152 */       paramFieldDelegate.formatted(1, NumberFormat.Field.FRACTION, NumberFormat.Field.FRACTION, i3, paramStringBuffer.length(), paramStringBuffer);
/*      */     }
/*      */ 
/* 1156 */     if (paramBoolean1) {
/* 1157 */       append(paramStringBuffer, this.negativeSuffix, paramFieldDelegate, getNegativeSuffixFieldPositions(), NumberFormat.Field.SIGN);
/*      */     }
/*      */     else
/*      */     {
/* 1161 */       append(paramStringBuffer, this.positiveSuffix, paramFieldDelegate, getPositiveSuffixFieldPositions(), NumberFormat.Field.SIGN);
/*      */     }
/*      */ 
/* 1165 */     return paramStringBuffer;
/*      */   }
/*      */ 
/*      */   private void append(StringBuffer paramStringBuffer, String paramString, Format.FieldDelegate paramFieldDelegate, FieldPosition[] paramArrayOfFieldPosition, Format.Field paramField)
/*      */   {
/* 1185 */     int i = paramStringBuffer.length();
/*      */ 
/* 1187 */     if (paramString.length() > 0) {
/* 1188 */       paramStringBuffer.append(paramString);
/* 1189 */       int j = 0; for (int k = paramArrayOfFieldPosition.length; j < k; 
/* 1190 */         j++) {
/* 1191 */         FieldPosition localFieldPosition = paramArrayOfFieldPosition[j];
/* 1192 */         Format.Field localField = localFieldPosition.getFieldAttribute();
/*      */ 
/* 1194 */         if (localField == NumberFormat.Field.SIGN) {
/* 1195 */           localField = paramField;
/*      */         }
/* 1197 */         paramFieldDelegate.formatted(localField, localField, i + localFieldPosition.getBeginIndex(), i + localFieldPosition.getEndIndex(), paramStringBuffer);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Number parse(String paramString, ParsePosition paramParsePosition)
/*      */   {
/* 1262 */     if (paramString.regionMatches(paramParsePosition.index, this.symbols.getNaN(), 0, this.symbols.getNaN().length())) {
/* 1263 */       paramParsePosition.index += this.symbols.getNaN().length();
/* 1264 */       return new Double((0.0D / 0.0D));
/*      */     }
/*      */ 
/* 1267 */     boolean[] arrayOfBoolean = new boolean[2];
/* 1268 */     if (!subparse(paramString, paramParsePosition, this.positivePrefix, this.negativePrefix, this.digitList, false, arrayOfBoolean)) {
/* 1269 */       return null;
/*      */     }
/*      */ 
/* 1273 */     if (arrayOfBoolean[0] != 0) {
/* 1274 */       if (arrayOfBoolean[1] == (this.multiplier >= 0 ? 1 : 0)) {
/* 1275 */         return new Double((1.0D / 0.0D));
/*      */       }
/* 1277 */       return new Double((-1.0D / 0.0D));
/*      */     }
/*      */ 
/* 1281 */     if (this.multiplier == 0) {
/* 1282 */       if (this.digitList.isZero())
/* 1283 */         return new Double((0.0D / 0.0D));
/* 1284 */       if (arrayOfBoolean[1] != 0) {
/* 1285 */         return new Double((1.0D / 0.0D));
/*      */       }
/* 1287 */       return new Double((-1.0D / 0.0D));
/*      */     }
/*      */ 
/* 1291 */     if (isParseBigDecimal()) {
/* 1292 */       BigDecimal localBigDecimal = this.digitList.getBigDecimal();
/*      */ 
/* 1294 */       if (this.multiplier != 1) {
/*      */         try {
/* 1296 */           localBigDecimal = localBigDecimal.divide(getBigDecimalMultiplier());
/*      */         }
/*      */         catch (ArithmeticException localArithmeticException) {
/* 1299 */           localBigDecimal = localBigDecimal.divide(getBigDecimalMultiplier(), this.roundingMode);
/*      */         }
/*      */       }
/*      */ 
/* 1303 */       if (arrayOfBoolean[1] == 0) {
/* 1304 */         localBigDecimal = localBigDecimal.negate();
/*      */       }
/* 1306 */       return localBigDecimal;
/*      */     }
/* 1308 */     int i = 1;
/* 1309 */     int j = 0;
/* 1310 */     double d = 0.0D;
/* 1311 */     long l = 0L;
/*      */ 
/* 1314 */     if (this.digitList.fitsIntoLong(arrayOfBoolean[1], isParseIntegerOnly())) {
/* 1315 */       i = 0;
/* 1316 */       l = this.digitList.getLong();
/* 1317 */       if (l < 0L)
/* 1318 */         j = 1;
/*      */     }
/*      */     else {
/* 1321 */       d = this.digitList.getDouble();
/*      */     }
/*      */ 
/* 1326 */     if (this.multiplier != 1) {
/* 1327 */       if (i != 0) {
/* 1328 */         d /= this.multiplier;
/*      */       }
/* 1331 */       else if (l % this.multiplier == 0L) {
/* 1332 */         l /= this.multiplier;
/*      */       } else {
/* 1334 */         d = l / this.multiplier;
/* 1335 */         i = 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1340 */     if ((arrayOfBoolean[1] == 0) && (j == 0)) {
/* 1341 */       d = -d;
/* 1342 */       l = -l;
/*      */     }
/*      */ 
/* 1354 */     if ((this.multiplier != 1) && (i != 0)) {
/* 1355 */       l = ()d;
/* 1356 */       i = ((d != l) || ((d == 0.0D) && (1.0D / d < 0.0D))) && (!isParseIntegerOnly()) ? 1 : 0;
/*      */     }
/*      */ 
/* 1361 */     return i != 0 ? new Double(d) : new Long(l);
/*      */   }
/*      */ 
/*      */   private BigInteger getBigIntegerMultiplier()
/*      */   {
/* 1370 */     if (this.bigIntegerMultiplier == null) {
/* 1371 */       this.bigIntegerMultiplier = BigInteger.valueOf(this.multiplier);
/*      */     }
/* 1373 */     return this.bigIntegerMultiplier;
/*      */   }
/*      */ 
/*      */   private BigDecimal getBigDecimalMultiplier()
/*      */   {
/* 1381 */     if (this.bigDecimalMultiplier == null) {
/* 1382 */       this.bigDecimalMultiplier = new BigDecimal(this.multiplier);
/*      */     }
/* 1384 */     return this.bigDecimalMultiplier;
/*      */   }
/*      */ 
/*      */   private final boolean subparse(String paramString1, ParsePosition paramParsePosition, String paramString2, String paramString3, DigitList paramDigitList, boolean paramBoolean, boolean[] paramArrayOfBoolean)
/*      */   {
/* 1408 */     int i = paramParsePosition.index;
/* 1409 */     int j = paramParsePosition.index;
/*      */ 
/* 1414 */     boolean bool1 = paramString1.regionMatches(i, paramString2, 0, paramString2.length());
/*      */ 
/* 1416 */     boolean bool2 = paramString1.regionMatches(i, paramString3, 0, paramString3.length());
/*      */ 
/* 1419 */     if ((bool1) && (bool2)) {
/* 1420 */       if (paramString2.length() > paramString3.length())
/* 1421 */         bool2 = false;
/* 1422 */       else if (paramString2.length() < paramString3.length()) {
/* 1423 */         bool1 = false;
/*      */       }
/*      */     }
/*      */ 
/* 1427 */     if (bool1) {
/* 1428 */       i += paramString2.length();
/* 1429 */     } else if (bool2) {
/* 1430 */       i += paramString3.length();
/*      */     } else {
/* 1432 */       paramParsePosition.errorIndex = i;
/* 1433 */       return false;
/*      */     }
/*      */ 
/* 1437 */     paramArrayOfBoolean[0] = false;
/* 1438 */     if ((!paramBoolean) && (paramString1.regionMatches(i, this.symbols.getInfinity(), 0, this.symbols.getInfinity().length())))
/*      */     {
/* 1440 */       i += this.symbols.getInfinity().length();
/* 1441 */       paramArrayOfBoolean[0] = true;
/*      */     }
/*      */     else
/*      */     {
/* 1450 */       paramDigitList.decimalAt = (paramDigitList.count = 0);
/* 1451 */       int m = this.symbols.getZeroDigit();
/* 1452 */       char c1 = this.isCurrencyFormat ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
/*      */ 
/* 1455 */       char c2 = this.symbols.getGroupingSeparator();
/* 1456 */       String str = this.symbols.getExponentSeparator();
/* 1457 */       int n = 0;
/* 1458 */       int i1 = 0;
/* 1459 */       int i2 = 0;
/* 1460 */       int i3 = 0;
/*      */ 
/* 1464 */       int i4 = 0;
/*      */ 
/* 1466 */       int k = -1;
/* 1467 */       for (; i < paramString1.length(); i++) {
/* 1468 */         char c3 = paramString1.charAt(i);
/*      */ 
/* 1481 */         int i5 = c3 - m;
/* 1482 */         if ((i5 < 0) || (i5 > 9)) {
/* 1483 */           i5 = Character.digit(c3, 10);
/*      */         }
/*      */ 
/* 1486 */         if (i5 == 0)
/*      */         {
/* 1488 */           k = -1;
/* 1489 */           i2 = 1;
/*      */ 
/* 1492 */           if (paramDigitList.count == 0)
/*      */           {
/* 1494 */             if (n != 0)
/*      */             {
/* 1502 */               paramDigitList.decimalAt -= 1;
/*      */             }
/*      */           } else { i4++;
/* 1505 */             paramDigitList.append((char)(i5 + 48)); }
/*      */         }
/* 1507 */         else if ((i5 > 0) && (i5 <= 9)) {
/* 1508 */           i2 = 1;
/* 1509 */           i4++;
/* 1510 */           paramDigitList.append((char)(i5 + 48));
/*      */ 
/* 1513 */           k = -1;
/* 1514 */         } else if ((!paramBoolean) && (c3 == c1))
/*      */         {
/* 1517 */           if ((isParseIntegerOnly()) || (n != 0)) {
/*      */             break;
/*      */           }
/* 1520 */           paramDigitList.decimalAt = i4;
/* 1521 */           n = 1;
/* 1522 */         } else if ((!paramBoolean) && (c3 == c2) && (isGroupingUsed())) {
/* 1523 */           if (n != 0)
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/* 1529 */           k = i; } else {
/* 1530 */           if ((paramBoolean) || (!paramString1.regionMatches(i, str, 0, str.length())) || (i1 != 0)) {
/*      */             break;
/*      */           }
/* 1533 */           ParsePosition localParsePosition = new ParsePosition(i + str.length());
/* 1534 */           boolean[] arrayOfBoolean = new boolean[2];
/* 1535 */           DigitList localDigitList = new DigitList();
/*      */ 
/* 1537 */           if ((!subparse(paramString1, localParsePosition, "", Character.toString(this.symbols.getMinusSign()), localDigitList, true, arrayOfBoolean)) || (!localDigitList.fitsIntoLong(arrayOfBoolean[1], true)))
/*      */             break;
/* 1539 */           i = localParsePosition.index;
/* 1540 */           i3 = (int)localDigitList.getLong();
/* 1541 */           if (arrayOfBoolean[1] == 0) {
/* 1542 */             i3 = -i3;
/*      */           }
/* 1544 */           i1 = 1; break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1553 */       if (k != -1) {
/* 1554 */         i = k;
/*      */       }
/*      */ 
/* 1558 */       if (n == 0) {
/* 1559 */         paramDigitList.decimalAt = i4;
/*      */       }
/*      */ 
/* 1563 */       paramDigitList.decimalAt += i3;
/*      */ 
/* 1569 */       if ((i2 == 0) && (i4 == 0)) {
/* 1570 */         paramParsePosition.index = j;
/* 1571 */         paramParsePosition.errorIndex = j;
/* 1572 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1577 */     if (!paramBoolean) {
/* 1578 */       if (bool1) {
/* 1579 */         bool1 = paramString1.regionMatches(i, this.positiveSuffix, 0, this.positiveSuffix.length());
/*      */       }
/*      */ 
/* 1582 */       if (bool2) {
/* 1583 */         bool2 = paramString1.regionMatches(i, this.negativeSuffix, 0, this.negativeSuffix.length());
/*      */       }
/*      */ 
/* 1588 */       if ((bool1) && (bool2)) {
/* 1589 */         if (this.positiveSuffix.length() > this.negativeSuffix.length())
/* 1590 */           bool2 = false;
/* 1591 */         else if (this.positiveSuffix.length() < this.negativeSuffix.length()) {
/* 1592 */           bool1 = false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1597 */       if (bool1 == bool2) {
/* 1598 */         paramParsePosition.errorIndex = i;
/* 1599 */         return false;
/*      */       }
/*      */ 
/* 1602 */       paramParsePosition.index = (i + (bool1 ? this.positiveSuffix.length() : this.negativeSuffix.length()));
/*      */     }
/*      */     else {
/* 1605 */       paramParsePosition.index = i;
/*      */     }
/*      */ 
/* 1608 */     paramArrayOfBoolean[1] = bool1;
/* 1609 */     if (paramParsePosition.index == j) {
/* 1610 */       paramParsePosition.errorIndex = i;
/* 1611 */       return false;
/*      */     }
/* 1613 */     return true;
/*      */   }
/*      */ 
/*      */   public DecimalFormatSymbols getDecimalFormatSymbols()
/*      */   {
/*      */     try
/*      */     {
/* 1625 */       return (DecimalFormatSymbols)this.symbols.clone(); } catch (Exception localException) {
/*      */     }
/* 1627 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDecimalFormatSymbols(DecimalFormatSymbols paramDecimalFormatSymbols)
/*      */   {
/*      */     try
/*      */     {
/* 1641 */       this.symbols = ((DecimalFormatSymbols)paramDecimalFormatSymbols.clone());
/* 1642 */       expandAffixes();
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getPositivePrefix()
/*      */   {
/* 1653 */     return this.positivePrefix;
/*      */   }
/*      */ 
/*      */   public void setPositivePrefix(String paramString)
/*      */   {
/* 1661 */     this.positivePrefix = paramString;
/* 1662 */     this.posPrefixPattern = null;
/* 1663 */     this.positivePrefixFieldPositions = null;
/*      */   }
/*      */ 
/*      */   private FieldPosition[] getPositivePrefixFieldPositions()
/*      */   {
/* 1675 */     if (this.positivePrefixFieldPositions == null) {
/* 1676 */       if (this.posPrefixPattern != null) {
/* 1677 */         this.positivePrefixFieldPositions = expandAffix(this.posPrefixPattern);
/*      */       }
/*      */       else {
/* 1680 */         this.positivePrefixFieldPositions = EmptyFieldPositionArray;
/*      */       }
/*      */     }
/* 1683 */     return this.positivePrefixFieldPositions;
/*      */   }
/*      */ 
/*      */   public String getNegativePrefix()
/*      */   {
/* 1691 */     return this.negativePrefix;
/*      */   }
/*      */ 
/*      */   public void setNegativePrefix(String paramString)
/*      */   {
/* 1699 */     this.negativePrefix = paramString;
/* 1700 */     this.negPrefixPattern = null;
/*      */   }
/*      */ 
/*      */   private FieldPosition[] getNegativePrefixFieldPositions()
/*      */   {
/* 1712 */     if (this.negativePrefixFieldPositions == null) {
/* 1713 */       if (this.negPrefixPattern != null) {
/* 1714 */         this.negativePrefixFieldPositions = expandAffix(this.negPrefixPattern);
/*      */       }
/*      */       else {
/* 1717 */         this.negativePrefixFieldPositions = EmptyFieldPositionArray;
/*      */       }
/*      */     }
/* 1720 */     return this.negativePrefixFieldPositions;
/*      */   }
/*      */ 
/*      */   public String getPositiveSuffix()
/*      */   {
/* 1728 */     return this.positiveSuffix;
/*      */   }
/*      */ 
/*      */   public void setPositiveSuffix(String paramString)
/*      */   {
/* 1736 */     this.positiveSuffix = paramString;
/* 1737 */     this.posSuffixPattern = null;
/*      */   }
/*      */ 
/*      */   private FieldPosition[] getPositiveSuffixFieldPositions()
/*      */   {
/* 1749 */     if (this.positiveSuffixFieldPositions == null) {
/* 1750 */       if (this.posSuffixPattern != null) {
/* 1751 */         this.positiveSuffixFieldPositions = expandAffix(this.posSuffixPattern);
/*      */       }
/*      */       else {
/* 1754 */         this.positiveSuffixFieldPositions = EmptyFieldPositionArray;
/*      */       }
/*      */     }
/* 1757 */     return this.positiveSuffixFieldPositions;
/*      */   }
/*      */ 
/*      */   public String getNegativeSuffix()
/*      */   {
/* 1765 */     return this.negativeSuffix;
/*      */   }
/*      */ 
/*      */   public void setNegativeSuffix(String paramString)
/*      */   {
/* 1773 */     this.negativeSuffix = paramString;
/* 1774 */     this.negSuffixPattern = null;
/*      */   }
/*      */ 
/*      */   private FieldPosition[] getNegativeSuffixFieldPositions()
/*      */   {
/* 1786 */     if (this.negativeSuffixFieldPositions == null) {
/* 1787 */       if (this.negSuffixPattern != null) {
/* 1788 */         this.negativeSuffixFieldPositions = expandAffix(this.negSuffixPattern);
/*      */       }
/*      */       else {
/* 1791 */         this.negativeSuffixFieldPositions = EmptyFieldPositionArray;
/*      */       }
/*      */     }
/* 1794 */     return this.negativeSuffixFieldPositions;
/*      */   }
/*      */ 
/*      */   public int getMultiplier()
/*      */   {
/* 1804 */     return this.multiplier;
/*      */   }
/*      */ 
/*      */   public void setMultiplier(int paramInt)
/*      */   {
/* 1821 */     this.multiplier = paramInt;
/* 1822 */     this.bigDecimalMultiplier = null;
/* 1823 */     this.bigIntegerMultiplier = null;
/*      */   }
/*      */ 
/*      */   public int getGroupingSize()
/*      */   {
/* 1835 */     return this.groupingSize;
/*      */   }
/*      */ 
/*      */   public void setGroupingSize(int paramInt)
/*      */   {
/* 1849 */     this.groupingSize = ((byte)paramInt);
/*      */   }
/*      */ 
/*      */   public boolean isDecimalSeparatorAlwaysShown()
/*      */   {
/* 1858 */     return this.decimalSeparatorAlwaysShown;
/*      */   }
/*      */ 
/*      */   public void setDecimalSeparatorAlwaysShown(boolean paramBoolean)
/*      */   {
/* 1867 */     this.decimalSeparatorAlwaysShown = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isParseBigDecimal()
/*      */   {
/* 1877 */     return this.parseBigDecimal;
/*      */   }
/*      */ 
/*      */   public void setParseBigDecimal(boolean paramBoolean)
/*      */   {
/* 1887 */     this.parseBigDecimal = paramBoolean;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 1895 */       DecimalFormat localDecimalFormat = (DecimalFormat)super.clone();
/* 1896 */       localDecimalFormat.symbols = ((DecimalFormatSymbols)this.symbols.clone());
/* 1897 */       localDecimalFormat.digitList = ((DigitList)this.digitList.clone());
/* 1898 */       return localDecimalFormat; } catch (Exception localException) {
/*      */     }
/* 1900 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1909 */     if (paramObject == null) return false;
/* 1910 */     if (!super.equals(paramObject)) return false;
/* 1911 */     DecimalFormat localDecimalFormat = (DecimalFormat)paramObject;
/* 1912 */     return ((this.posPrefixPattern == localDecimalFormat.posPrefixPattern) && (this.positivePrefix.equals(localDecimalFormat.positivePrefix))) || ((this.posPrefixPattern != null) && (this.posPrefixPattern.equals(localDecimalFormat.posPrefixPattern)) && (((this.posSuffixPattern == localDecimalFormat.posSuffixPattern) && (this.positiveSuffix.equals(localDecimalFormat.positiveSuffix))) || ((this.posSuffixPattern != null) && (this.posSuffixPattern.equals(localDecimalFormat.posSuffixPattern)) && (((this.negPrefixPattern == localDecimalFormat.negPrefixPattern) && (this.negativePrefix.equals(localDecimalFormat.negativePrefix))) || ((this.negPrefixPattern != null) && (this.negPrefixPattern.equals(localDecimalFormat.negPrefixPattern)) && (((this.negSuffixPattern == localDecimalFormat.negSuffixPattern) && (this.negativeSuffix.equals(localDecimalFormat.negativeSuffix))) || ((this.negSuffixPattern != null) && (this.negSuffixPattern.equals(localDecimalFormat.negSuffixPattern)) && (this.multiplier == localDecimalFormat.multiplier) && (this.groupingSize == localDecimalFormat.groupingSize) && (this.decimalSeparatorAlwaysShown == localDecimalFormat.decimalSeparatorAlwaysShown) && (this.parseBigDecimal == localDecimalFormat.parseBigDecimal) && (this.useExponentialNotation == localDecimalFormat.useExponentialNotation) && ((!this.useExponentialNotation) || (this.minExponentDigits == localDecimalFormat.minExponentDigits)) && (this.maximumIntegerDigits == localDecimalFormat.maximumIntegerDigits) && (this.minimumIntegerDigits == localDecimalFormat.minimumIntegerDigits) && (this.maximumFractionDigits == localDecimalFormat.maximumFractionDigits) && (this.minimumFractionDigits == localDecimalFormat.minimumFractionDigits) && (this.roundingMode == localDecimalFormat.roundingMode) && (this.symbols.equals(localDecimalFormat.symbols)))))))));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1947 */     return super.hashCode() * 37 + this.positivePrefix.hashCode();
/*      */   }
/*      */ 
/*      */   public String toPattern()
/*      */   {
/* 1957 */     return toPattern(false);
/*      */   }
/*      */ 
/*      */   public String toLocalizedPattern()
/*      */   {
/* 1966 */     return toPattern(true);
/*      */   }
/*      */ 
/*      */   private void expandAffixes()
/*      */   {
/* 1977 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1978 */     if (this.posPrefixPattern != null) {
/* 1979 */       this.positivePrefix = expandAffix(this.posPrefixPattern, localStringBuffer);
/* 1980 */       this.positivePrefixFieldPositions = null;
/*      */     }
/* 1982 */     if (this.posSuffixPattern != null) {
/* 1983 */       this.positiveSuffix = expandAffix(this.posSuffixPattern, localStringBuffer);
/* 1984 */       this.positiveSuffixFieldPositions = null;
/*      */     }
/* 1986 */     if (this.negPrefixPattern != null) {
/* 1987 */       this.negativePrefix = expandAffix(this.negPrefixPattern, localStringBuffer);
/* 1988 */       this.negativePrefixFieldPositions = null;
/*      */     }
/* 1990 */     if (this.negSuffixPattern != null) {
/* 1991 */       this.negativeSuffix = expandAffix(this.negSuffixPattern, localStringBuffer);
/* 1992 */       this.negativeSuffixFieldPositions = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private String expandAffix(String paramString, StringBuffer paramStringBuffer)
/*      */   {
/* 2011 */     paramStringBuffer.setLength(0);
/* 2012 */     for (int i = 0; i < paramString.length(); ) {
/* 2013 */       char c = paramString.charAt(i++);
/* 2014 */       if (c == '\'') {
/* 2015 */         c = paramString.charAt(i++);
/* 2016 */         switch (c) {
/*      */         case '¤':
/* 2018 */           if ((i < paramString.length()) && (paramString.charAt(i) == '¤'))
/*      */           {
/* 2020 */             i++;
/* 2021 */             paramStringBuffer.append(this.symbols.getInternationalCurrencySymbol()); continue;
/*      */           }
/* 2023 */           paramStringBuffer.append(this.symbols.getCurrencySymbol());
/*      */ 
/* 2025 */           break;
/*      */         case '%':
/* 2027 */           c = this.symbols.getPercent();
/* 2028 */           break;
/*      */         case '‰':
/* 2030 */           c = this.symbols.getPerMill();
/* 2031 */           break;
/*      */         case '-':
/* 2033 */           c = this.symbols.getMinusSign();
/*      */         }
/*      */       }
/*      */       else {
/* 2037 */         paramStringBuffer.append(c);
/*      */       }
/*      */     }
/* 2039 */     return paramStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private FieldPosition[] expandAffix(String paramString)
/*      */   {
/* 2058 */     ArrayList localArrayList = null;
/* 2059 */     int i = 0;
/* 2060 */     for (int j = 0; j < paramString.length(); ) {
/* 2061 */       int k = paramString.charAt(j++);
/* 2062 */       if (k == 39) {
/* 2063 */         int m = -1;
/* 2064 */         NumberFormat.Field localField = null;
/* 2065 */         k = paramString.charAt(j++);
/*      */         Object localObject;
/* 2066 */         switch (k)
/*      */         {
/*      */         case 164:
/* 2069 */           if ((j < paramString.length()) && (paramString.charAt(j) == '¤'))
/*      */           {
/* 2071 */             j++;
/* 2072 */             localObject = this.symbols.getInternationalCurrencySymbol();
/*      */           } else {
/* 2074 */             localObject = this.symbols.getCurrencySymbol();
/*      */           }
/* 2076 */           if (((String)localObject).length() <= 0) continue;
/* 2077 */           if (localArrayList == null) {
/* 2078 */             localArrayList = new ArrayList(2);
/*      */           }
/* 2080 */           FieldPosition localFieldPosition = new FieldPosition(NumberFormat.Field.CURRENCY);
/* 2081 */           localFieldPosition.setBeginIndex(i);
/* 2082 */           localFieldPosition.setEndIndex(i + ((String)localObject).length());
/* 2083 */           localArrayList.add(localFieldPosition);
/* 2084 */           i += ((String)localObject).length();
/* 2085 */           break;
/*      */         case 37:
/* 2088 */           k = this.symbols.getPercent();
/* 2089 */           m = -1;
/* 2090 */           localField = NumberFormat.Field.PERCENT;
/* 2091 */           break;
/*      */         case 8240:
/* 2093 */           k = this.symbols.getPerMill();
/* 2094 */           m = -1;
/* 2095 */           localField = NumberFormat.Field.PERMILLE;
/* 2096 */           break;
/*      */         case 45:
/* 2098 */           k = this.symbols.getMinusSign();
/* 2099 */           m = -1;
/* 2100 */           localField = NumberFormat.Field.SIGN;
/*      */         default:
/* 2103 */           if (localField != null) {
/* 2104 */             if (localArrayList == null) {
/* 2105 */               localArrayList = new ArrayList(2);
/*      */             }
/* 2107 */             localObject = new FieldPosition(localField, m);
/* 2108 */             ((FieldPosition)localObject).setBeginIndex(i);
/* 2109 */             ((FieldPosition)localObject).setEndIndex(i + 1);
/* 2110 */             localArrayList.add(localObject);
/*      */           }break;
/*      */         }
/*      */       } else { i++; }
/*      */     }
/* 2115 */     if (localArrayList != null) {
/* 2116 */       return (FieldPosition[])localArrayList.toArray(EmptyFieldPositionArray);
/*      */     }
/* 2118 */     return EmptyFieldPositionArray;
/*      */   }
/*      */ 
/*      */   private void appendAffix(StringBuffer paramStringBuffer, String paramString1, String paramString2, boolean paramBoolean)
/*      */   {
/* 2138 */     if (paramString1 == null) {
/* 2139 */       appendAffix(paramStringBuffer, paramString2, paramBoolean);
/*      */     }
/*      */     else
/*      */     {
/*      */       int i;
/* 2142 */       for (int j = 0; j < paramString1.length(); j = i) {
/* 2143 */         i = paramString1.indexOf('\'', j);
/* 2144 */         if (i < 0) {
/* 2145 */           appendAffix(paramStringBuffer, paramString1.substring(j), paramBoolean);
/* 2146 */           break;
/*      */         }
/* 2148 */         if (i > j) {
/* 2149 */           appendAffix(paramStringBuffer, paramString1.substring(j, i), paramBoolean);
/*      */         }
/* 2151 */         char c = paramString1.charAt(++i);
/* 2152 */         i++;
/* 2153 */         if (c == '\'') {
/* 2154 */           paramStringBuffer.append(c);
/*      */         }
/* 2156 */         else if ((c == '¤') && (i < paramString1.length()) && (paramString1.charAt(i) == '¤'))
/*      */         {
/* 2159 */           i++;
/* 2160 */           paramStringBuffer.append(c);
/*      */         }
/* 2162 */         else if (paramBoolean) {
/* 2163 */           switch (c) {
/*      */           case '%':
/* 2165 */             c = this.symbols.getPercent();
/* 2166 */             break;
/*      */           case '‰':
/* 2168 */             c = this.symbols.getPerMill();
/* 2169 */             break;
/*      */           case '-':
/* 2171 */             c = this.symbols.getMinusSign();
/*      */           }
/*      */         }
/*      */ 
/* 2175 */         paramStringBuffer.append(c);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void appendAffix(StringBuffer paramStringBuffer, String paramString, boolean paramBoolean)
/*      */   {
/*      */     int i;
/* 2187 */     if (paramBoolean) {
/* 2188 */       i = (paramString.indexOf(this.symbols.getZeroDigit()) >= 0) || (paramString.indexOf(this.symbols.getGroupingSeparator()) >= 0) || (paramString.indexOf(this.symbols.getDecimalSeparator()) >= 0) || (paramString.indexOf(this.symbols.getPercent()) >= 0) || (paramString.indexOf(this.symbols.getPerMill()) >= 0) || (paramString.indexOf(this.symbols.getDigit()) >= 0) || (paramString.indexOf(this.symbols.getPatternSeparator()) >= 0) || (paramString.indexOf(this.symbols.getMinusSign()) >= 0) || (paramString.indexOf('¤') >= 0) ? 1 : 0;
/*      */     }
/*      */     else
/*      */     {
/* 2199 */       i = (paramString.indexOf('0') >= 0) || (paramString.indexOf(',') >= 0) || (paramString.indexOf('.') >= 0) || (paramString.indexOf('%') >= 0) || (paramString.indexOf('‰') >= 0) || (paramString.indexOf('#') >= 0) || (paramString.indexOf(';') >= 0) || (paramString.indexOf('-') >= 0) || (paramString.indexOf('¤') >= 0) ? 1 : 0;
/*      */     }
/*      */ 
/* 2209 */     if (i != 0) paramStringBuffer.append('\'');
/* 2210 */     if (paramString.indexOf('\'') < 0) paramStringBuffer.append(paramString);
/*      */     else {
/* 2212 */       for (int j = 0; j < paramString.length(); j++) {
/* 2213 */         char c = paramString.charAt(j);
/* 2214 */         paramStringBuffer.append(c);
/* 2215 */         if (c == '\'') paramStringBuffer.append(c);
/*      */       }
/*      */     }
/* 2218 */     if (i != 0) paramStringBuffer.append('\'');
/*      */   }
/*      */ 
/*      */   private String toPattern(boolean paramBoolean)
/*      */   {
/* 2224 */     StringBuffer localStringBuffer = new StringBuffer();
/* 2225 */     for (int i = 1; i >= 0; i--) {
/* 2226 */       if (i == 1)
/* 2227 */         appendAffix(localStringBuffer, this.posPrefixPattern, this.positivePrefix, paramBoolean);
/* 2228 */       else appendAffix(localStringBuffer, this.negPrefixPattern, this.negativePrefix, paramBoolean);
/*      */ 
/* 2230 */       int k = this.useExponentialNotation ? getMaximumIntegerDigits() : Math.max(this.groupingSize, getMinimumIntegerDigits()) + 1;
/*      */ 
/* 2233 */       for (int j = k; j > 0; j--) {
/* 2234 */         if ((j != k) && (isGroupingUsed()) && (this.groupingSize != 0) && (j % this.groupingSize == 0))
/*      */         {
/* 2236 */           localStringBuffer.append(paramBoolean ? this.symbols.getGroupingSeparator() : ',');
/*      */         }
/*      */ 
/* 2239 */         localStringBuffer.append(paramBoolean ? this.symbols.getDigit() : j <= getMinimumIntegerDigits() ? '0' : paramBoolean ? this.symbols.getZeroDigit() : '#');
/*      */       }
/*      */ 
/* 2243 */       if ((getMaximumFractionDigits() > 0) || (this.decimalSeparatorAlwaysShown)) {
/* 2244 */         localStringBuffer.append(paramBoolean ? this.symbols.getDecimalSeparator() : '.');
/*      */       }
/* 2246 */       for (j = 0; j < getMaximumFractionDigits(); j++) {
/* 2247 */         if (j < getMinimumFractionDigits()) {
/* 2248 */           localStringBuffer.append(paramBoolean ? this.symbols.getZeroDigit() : '0');
/*      */         }
/*      */         else {
/* 2251 */           localStringBuffer.append(paramBoolean ? this.symbols.getDigit() : '#');
/*      */         }
/*      */       }
/*      */ 
/* 2255 */       if (this.useExponentialNotation)
/*      */       {
/* 2257 */         localStringBuffer.append(paramBoolean ? this.symbols.getExponentSeparator() : "E");
/*      */ 
/* 2259 */         for (j = 0; j < this.minExponentDigits; j++) {
/* 2260 */           localStringBuffer.append(paramBoolean ? this.symbols.getZeroDigit() : '0');
/*      */         }
/*      */       }
/* 2263 */       if (i == 1) {
/* 2264 */         appendAffix(localStringBuffer, this.posSuffixPattern, this.positiveSuffix, paramBoolean);
/* 2265 */         if (((this.negSuffixPattern != this.posSuffixPattern) || (!this.negativeSuffix.equals(this.positiveSuffix))) && ((this.negSuffixPattern != null) && (this.negSuffixPattern.equals(this.posSuffixPattern)) && (
/* 2269 */           ((this.negPrefixPattern != null) && (this.posPrefixPattern != null) && (this.negPrefixPattern.equals("'-" + this.posPrefixPattern))) || ((this.negPrefixPattern == this.posPrefixPattern) && (this.negativePrefix.equals(this.symbols.getMinusSign() + this.positivePrefix))))))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/* 2275 */         localStringBuffer.append(paramBoolean ? this.symbols.getPatternSeparator() : ';');
/*      */       } else {
/* 2277 */         appendAffix(localStringBuffer, this.negSuffixPattern, this.negativeSuffix, paramBoolean);
/*      */       }
/*      */     }
/* 2279 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public void applyPattern(String paramString)
/*      */   {
/* 2304 */     applyPattern(paramString, false);
/*      */   }
/*      */ 
/*      */   public void applyLocalizedPattern(String paramString)
/*      */   {
/* 2330 */     applyPattern(paramString, true);
/*      */   }
/*      */ 
/*      */   private void applyPattern(String paramString, boolean paramBoolean)
/*      */   {
/* 2337 */     int i = 48;
/* 2338 */     int j = 44;
/* 2339 */     int k = 46;
/* 2340 */     char c = '%';
/* 2341 */     int m = 8240;
/* 2342 */     int n = 35;
/* 2343 */     int i1 = 59;
/* 2344 */     String str = "E";
/* 2345 */     int i2 = 45;
/* 2346 */     if (paramBoolean) {
/* 2347 */       i = this.symbols.getZeroDigit();
/* 2348 */       j = this.symbols.getGroupingSeparator();
/* 2349 */       k = this.symbols.getDecimalSeparator();
/* 2350 */       c = this.symbols.getPercent();
/* 2351 */       m = this.symbols.getPerMill();
/* 2352 */       n = this.symbols.getDigit();
/* 2353 */       i1 = this.symbols.getPatternSeparator();
/* 2354 */       str = this.symbols.getExponentSeparator();
/* 2355 */       i2 = this.symbols.getMinusSign();
/*      */     }
/* 2357 */     int i3 = 0;
/* 2358 */     this.decimalSeparatorAlwaysShown = false;
/* 2359 */     this.isCurrencyFormat = false;
/* 2360 */     this.useExponentialNotation = false;
/*      */ 
/* 2366 */     int i4 = 0;
/* 2367 */     int i5 = 0;
/*      */ 
/* 2369 */     int i6 = 0;
/* 2370 */     for (int i7 = 1; (i7 >= 0) && (i6 < paramString.length()); i7--) {
/* 2371 */       int i8 = 0;
/* 2372 */       StringBuffer localStringBuffer1 = new StringBuffer();
/* 2373 */       StringBuffer localStringBuffer2 = new StringBuffer();
/* 2374 */       int i9 = -1;
/* 2375 */       int i10 = 1;
/* 2376 */       int i11 = 0; int i12 = 0; int i13 = 0;
/* 2377 */       int i14 = -1;
/*      */ 
/* 2386 */       int i15 = 0;
/*      */ 
/* 2389 */       StringBuffer localStringBuffer3 = localStringBuffer1;
/*      */       int i17;
/* 2391 */       for (int i16 = i6; i16 < paramString.length(); i16++) {
/* 2392 */         i17 = paramString.charAt(i16);
/* 2393 */         switch (i15)
/*      */         {
/*      */         case 0:
/*      */         case 2:
/* 2397 */           if (i8 != 0)
/*      */           {
/* 2401 */             if (i17 == 39) {
/* 2402 */               if ((i16 + 1 < paramString.length()) && (paramString.charAt(i16 + 1) == '\''))
/*      */               {
/* 2404 */                 i16++;
/* 2405 */                 localStringBuffer3.append("''"); continue;
/*      */               }
/* 2407 */               i8 = 0;
/*      */ 
/* 2409 */               continue;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2414 */             if ((i17 == n) || (i17 == i) || (i17 == j) || (i17 == k))
/*      */             {
/* 2418 */               i15 = 1;
/* 2419 */               if (i7 == 1) {
/* 2420 */                 i4 = i16;
/*      */               }
/* 2422 */               i16--;
/* 2423 */               continue;
/* 2424 */             }if (i17 == 164)
/*      */             {
/* 2427 */               int i18 = (i16 + 1 < paramString.length()) && (paramString.charAt(i16 + 1) == '¤') ? 1 : 0;
/*      */ 
/* 2429 */               if (i18 != 0) {
/* 2430 */                 i16++;
/*      */               }
/* 2432 */               this.isCurrencyFormat = true;
/* 2433 */               localStringBuffer3.append(i18 != 0 ? "'¤¤" : "'¤");
/* 2434 */               continue;
/* 2435 */             }if (i17 == 39)
/*      */             {
/* 2440 */               if (i17 == 39) {
/* 2441 */                 if ((i16 + 1 < paramString.length()) && (paramString.charAt(i16 + 1) == '\''))
/*      */                 {
/* 2443 */                   i16++;
/* 2444 */                   localStringBuffer3.append("''"); continue;
/*      */                 }
/* 2446 */                 i8 = 1;
/*      */ 
/* 2448 */                 continue;
/*      */               }
/*      */             } else { if (i17 == i1)
/*      */               {
/* 2454 */                 if ((i15 == 0) || (i7 == 0)) {
/* 2455 */                   throw new IllegalArgumentException("Unquoted special character '" + i17 + "' in pattern \"" + paramString + '"');
/*      */                 }
/*      */ 
/* 2458 */                 i6 = i16 + 1;
/* 2459 */                 i16 = paramString.length();
/* 2460 */                 continue;
/*      */               }
/*      */ 
/* 2464 */               if (i17 == c) {
/* 2465 */                 if (i10 != 1) {
/* 2466 */                   throw new IllegalArgumentException("Too many percent/per mille characters in pattern \"" + paramString + '"');
/*      */                 }
/*      */ 
/* 2469 */                 i10 = 100;
/* 2470 */                 localStringBuffer3.append("'%");
/* 2471 */                 continue;
/* 2472 */               }if (i17 == m) {
/* 2473 */                 if (i10 != 1) {
/* 2474 */                   throw new IllegalArgumentException("Too many percent/per mille characters in pattern \"" + paramString + '"');
/*      */                 }
/*      */ 
/* 2477 */                 i10 = 1000;
/* 2478 */                 localStringBuffer3.append("'‰");
/* 2479 */                 continue;
/* 2480 */               }if (i17 == i2) {
/* 2481 */                 localStringBuffer3.append("'-");
/* 2482 */                 continue;
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2488 */           localStringBuffer3.append(i17);
/* 2489 */           break;
/*      */         case 1:
/* 2497 */           if (i7 == 1) {
/* 2498 */             i5++;
/*      */           } else {
/* 2500 */             i5--; if (i5 != 0) continue;
/* 2501 */             i15 = 2;
/* 2502 */             localStringBuffer3 = localStringBuffer2; continue;
/*      */           }
/*      */ 
/* 2516 */           if (i17 == n) {
/* 2517 */             if (i12 > 0)
/* 2518 */               i13++;
/*      */             else {
/* 2520 */               i11++;
/*      */             }
/* 2522 */             if ((i14 >= 0) && (i9 < 0))
/* 2523 */               i14 = (byte)(i14 + 1);
/*      */           }
/* 2525 */           else if (i17 == i) {
/* 2526 */             if (i13 > 0) {
/* 2527 */               throw new IllegalArgumentException("Unexpected '0' in pattern \"" + paramString + '"');
/*      */             }
/*      */ 
/* 2530 */             i12++;
/* 2531 */             if ((i14 >= 0) && (i9 < 0))
/* 2532 */               i14 = (byte)(i14 + 1);
/*      */           }
/* 2534 */           else if (i17 == j) {
/* 2535 */             i14 = 0;
/* 2536 */           } else if (i17 == k) {
/* 2537 */             if (i9 >= 0) {
/* 2538 */               throw new IllegalArgumentException("Multiple decimal separators in pattern \"" + paramString + '"');
/*      */             }
/*      */ 
/* 2541 */             i9 = i11 + i12 + i13;
/* 2542 */           } else if (paramString.regionMatches(i16, str, 0, str.length())) {
/* 2543 */             if (this.useExponentialNotation) {
/* 2544 */               throw new IllegalArgumentException("Multiple exponential symbols in pattern \"" + paramString + '"');
/*      */             }
/*      */ 
/* 2547 */             this.useExponentialNotation = true;
/* 2548 */             this.minExponentDigits = 0;
/*      */ 
/* 2552 */             i16 += str.length();
/* 2553 */             while ((i16 < paramString.length()) && (paramString.charAt(i16) == i))
/*      */             {
/* 2555 */               this.minExponentDigits = ((byte)(this.minExponentDigits + 1));
/* 2556 */               i5++;
/* 2557 */               i16++;
/*      */             }
/*      */ 
/* 2560 */             if ((i11 + i12 < 1) || (this.minExponentDigits < 1))
/*      */             {
/* 2562 */               throw new IllegalArgumentException("Malformed exponential pattern \"" + paramString + '"');
/*      */             }
/*      */ 
/* 2567 */             i15 = 2;
/* 2568 */             localStringBuffer3 = localStringBuffer2;
/* 2569 */             i16--;
/*      */           }
/*      */           else {
/* 2572 */             i15 = 2;
/* 2573 */             localStringBuffer3 = localStringBuffer2;
/* 2574 */             i16--;
/* 2575 */             i5--;
/*      */           }
/*      */ 
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2594 */       if ((i12 == 0) && (i11 > 0) && (i9 >= 0))
/*      */       {
/* 2596 */         i16 = i9;
/* 2597 */         if (i16 == 0) {
/* 2598 */           i16++;
/*      */         }
/* 2600 */         i13 = i11 - i16;
/* 2601 */         i11 = i16 - 1;
/* 2602 */         i12 = 1;
/*      */       }
/*      */ 
/* 2606 */       if (((i9 < 0) && (i13 > 0)) || ((i9 >= 0) && ((i9 < i11) || (i9 > i11 + i12))) || (i14 == 0) || (i8 != 0))
/*      */       {
/* 2610 */         throw new IllegalArgumentException("Malformed pattern \"" + paramString + '"');
/*      */       }
/*      */ 
/* 2614 */       if (i7 == 1) {
/* 2615 */         this.posPrefixPattern = localStringBuffer1.toString();
/* 2616 */         this.posSuffixPattern = localStringBuffer2.toString();
/* 2617 */         this.negPrefixPattern = this.posPrefixPattern;
/* 2618 */         this.negSuffixPattern = this.posSuffixPattern;
/* 2619 */         i16 = i11 + i12 + i13;
/*      */ 
/* 2624 */         i17 = i9 >= 0 ? i9 : i16;
/*      */ 
/* 2626 */         setMinimumIntegerDigits(i17 - i11);
/* 2627 */         setMaximumIntegerDigits(this.useExponentialNotation ? i11 + getMinimumIntegerDigits() : 2147483647);
/*      */ 
/* 2630 */         setMaximumFractionDigits(i9 >= 0 ? i16 - i9 : 0);
/*      */ 
/* 2632 */         setMinimumFractionDigits(i9 >= 0 ? i11 + i12 - i9 : 0);
/*      */ 
/* 2634 */         setGroupingUsed(i14 > 0);
/* 2635 */         this.groupingSize = (i14 > 0 ? i14 : 0);
/* 2636 */         this.multiplier = i10;
/* 2637 */         setDecimalSeparatorAlwaysShown((i9 == 0) || (i9 == i16));
/*      */       }
/*      */       else {
/* 2640 */         this.negPrefixPattern = localStringBuffer1.toString();
/* 2641 */         this.negSuffixPattern = localStringBuffer2.toString();
/* 2642 */         i3 = 1;
/*      */       }
/*      */     }
/*      */ 
/* 2646 */     if (paramString.length() == 0) {
/* 2647 */       this.posPrefixPattern = (this.posSuffixPattern = "");
/* 2648 */       setMinimumIntegerDigits(0);
/* 2649 */       setMaximumIntegerDigits(2147483647);
/* 2650 */       setMinimumFractionDigits(0);
/* 2651 */       setMaximumFractionDigits(2147483647);
/*      */     }
/*      */ 
/* 2657 */     if ((i3 == 0) || ((this.negPrefixPattern.equals(this.posPrefixPattern)) && (this.negSuffixPattern.equals(this.posSuffixPattern))))
/*      */     {
/* 2660 */       this.negSuffixPattern = this.posSuffixPattern;
/* 2661 */       this.negPrefixPattern = ("'-" + this.posPrefixPattern);
/*      */     }
/*      */ 
/* 2664 */     expandAffixes();
/*      */   }
/*      */ 
/*      */   public void setMaximumIntegerDigits(int paramInt)
/*      */   {
/* 2676 */     this.maximumIntegerDigits = Math.min(Math.max(0, paramInt), 2147483647);
/* 2677 */     super.setMaximumIntegerDigits(this.maximumIntegerDigits > 309 ? 309 : this.maximumIntegerDigits);
/*      */ 
/* 2679 */     if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
/* 2680 */       this.minimumIntegerDigits = this.maximumIntegerDigits;
/* 2681 */       super.setMinimumIntegerDigits(this.minimumIntegerDigits > 309 ? 309 : this.minimumIntegerDigits);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMinimumIntegerDigits(int paramInt)
/*      */   {
/* 2695 */     this.minimumIntegerDigits = Math.min(Math.max(0, paramInt), 2147483647);
/* 2696 */     super.setMinimumIntegerDigits(this.minimumIntegerDigits > 309 ? 309 : this.minimumIntegerDigits);
/*      */ 
/* 2698 */     if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
/* 2699 */       this.maximumIntegerDigits = this.minimumIntegerDigits;
/* 2700 */       super.setMaximumIntegerDigits(this.maximumIntegerDigits > 309 ? 309 : this.maximumIntegerDigits);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMaximumFractionDigits(int paramInt)
/*      */   {
/* 2714 */     this.maximumFractionDigits = Math.min(Math.max(0, paramInt), 2147483647);
/* 2715 */     super.setMaximumFractionDigits(this.maximumFractionDigits > 340 ? 340 : this.maximumFractionDigits);
/*      */ 
/* 2717 */     if (this.minimumFractionDigits > this.maximumFractionDigits) {
/* 2718 */       this.minimumFractionDigits = this.maximumFractionDigits;
/* 2719 */       super.setMinimumFractionDigits(this.minimumFractionDigits > 340 ? 340 : this.minimumFractionDigits);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMinimumFractionDigits(int paramInt)
/*      */   {
/* 2733 */     this.minimumFractionDigits = Math.min(Math.max(0, paramInt), 2147483647);
/* 2734 */     super.setMinimumFractionDigits(this.minimumFractionDigits > 340 ? 340 : this.minimumFractionDigits);
/*      */ 
/* 2736 */     if (this.minimumFractionDigits > this.maximumFractionDigits) {
/* 2737 */       this.maximumFractionDigits = this.minimumFractionDigits;
/* 2738 */       super.setMaximumFractionDigits(this.maximumFractionDigits > 340 ? 340 : this.maximumFractionDigits);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getMaximumIntegerDigits()
/*      */   {
/* 2752 */     return this.maximumIntegerDigits;
/*      */   }
/*      */ 
/*      */   public int getMinimumIntegerDigits()
/*      */   {
/* 2764 */     return this.minimumIntegerDigits;
/*      */   }
/*      */ 
/*      */   public int getMaximumFractionDigits()
/*      */   {
/* 2776 */     return this.maximumFractionDigits;
/*      */   }
/*      */ 
/*      */   public int getMinimumFractionDigits()
/*      */   {
/* 2788 */     return this.minimumFractionDigits;
/*      */   }
/*      */ 
/*      */   public Currency getCurrency()
/*      */   {
/* 2802 */     return this.symbols.getCurrency();
/*      */   }
/*      */ 
/*      */   public void setCurrency(Currency paramCurrency)
/*      */   {
/* 2818 */     if (paramCurrency != this.symbols.getCurrency()) {
/* 2819 */       this.symbols.setCurrency(paramCurrency);
/* 2820 */       if (this.isCurrencyFormat)
/* 2821 */         expandAffixes();
/*      */     }
/*      */   }
/*      */ 
/*      */   public RoundingMode getRoundingMode()
/*      */   {
/* 2834 */     return this.roundingMode;
/*      */   }
/*      */ 
/*      */   public void setRoundingMode(RoundingMode paramRoundingMode)
/*      */   {
/* 2846 */     if (paramRoundingMode == null) {
/* 2847 */       throw new NullPointerException();
/*      */     }
/*      */ 
/* 2850 */     this.roundingMode = paramRoundingMode;
/* 2851 */     this.digitList.setRoundingMode(paramRoundingMode);
/*      */   }
/*      */ 
/*      */   void adjustForCurrencyDefaultFractionDigits()
/*      */   {
/* 2859 */     Currency localCurrency = this.symbols.getCurrency();
/* 2860 */     if (localCurrency == null)
/*      */       try {
/* 2862 */         localCurrency = Currency.getInstance(this.symbols.getInternationalCurrencySymbol());
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {
/*      */       }
/* 2866 */     if (localCurrency != null) {
/* 2867 */       int i = localCurrency.getDefaultFractionDigits();
/* 2868 */       if (i != -1) {
/* 2869 */         int j = getMinimumFractionDigits();
/*      */ 
/* 2872 */         if (j == getMaximumFractionDigits()) {
/* 2873 */           setMinimumFractionDigits(i);
/* 2874 */           setMaximumFractionDigits(i);
/*      */         } else {
/* 2876 */           setMinimumFractionDigits(Math.min(i, j));
/* 2877 */           setMaximumFractionDigits(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2929 */     paramObjectInputStream.defaultReadObject();
/* 2930 */     this.digitList = new DigitList();
/*      */ 
/* 2932 */     if (this.serialVersionOnStream < 4) {
/* 2933 */       setRoundingMode(RoundingMode.HALF_EVEN);
/*      */     }
/*      */ 
/* 2938 */     if ((super.getMaximumIntegerDigits() > 309) || (super.getMaximumFractionDigits() > 340))
/*      */     {
/* 2940 */       throw new InvalidObjectException("Digit count out of range");
/*      */     }
/* 2942 */     if (this.serialVersionOnStream < 3) {
/* 2943 */       setMaximumIntegerDigits(super.getMaximumIntegerDigits());
/* 2944 */       setMinimumIntegerDigits(super.getMinimumIntegerDigits());
/* 2945 */       setMaximumFractionDigits(super.getMaximumFractionDigits());
/* 2946 */       setMinimumFractionDigits(super.getMinimumFractionDigits());
/*      */     }
/* 2948 */     if (this.serialVersionOnStream < 1)
/*      */     {
/* 2950 */       this.useExponentialNotation = false;
/*      */     }
/* 2952 */     this.serialVersionOnStream = 4;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.DecimalFormat
 * JD-Core Version:    0.6.2
 */