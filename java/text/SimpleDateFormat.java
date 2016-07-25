/*      */ package java.text;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.Locale;
/*      */ import java.util.Locale.Category;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.SimpleTimeZone;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import sun.util.calendar.CalendarUtils;
/*      */ import sun.util.calendar.ZoneInfoFile;
/*      */ import sun.util.resources.LocaleData;
/*      */ 
/*      */ public class SimpleDateFormat extends DateFormat
/*      */ {
/*      */   static final long serialVersionUID = 4774881970558875024L;
/*      */   static final int currentSerialVersion = 1;
/*  434 */   private int serialVersionOnStream = 1;
/*      */   private String pattern;
/*      */   private transient NumberFormat originalNumberFormat;
/*      */   private transient String originalNumberPattern;
/*  453 */   private transient char minusSign = '-';
/*      */ 
/*  459 */   private transient boolean hasFollowingMinusSign = false;
/*      */   private transient char[] compiledPattern;
/*      */   private static final int TAG_QUOTE_ASCII_CHAR = 100;
/*      */   private static final int TAG_QUOTE_CHARS = 101;
/*      */   private transient char zeroDigit;
/*      */   private DateFormatSymbols formatData;
/*      */   private Date defaultCenturyStart;
/*      */   private transient int defaultCenturyStartYear;
/*      */   private static final int MILLIS_PER_MINUTE = 60000;
/*      */   private static final String GMT = "GMT";
/*  507 */   private static final ConcurrentMap<Locale, String[]> cachedLocaleData = new ConcurrentHashMap(3);
/*      */ 
/*  513 */   private static final ConcurrentMap<Locale, NumberFormat> cachedNumberFormatData = new ConcurrentHashMap(3);
/*      */   private Locale locale;
/*      */   transient boolean useDateFormatSymbols;
/* 1025 */   private static final int[] PATTERN_INDEX_TO_CALENDAR_FIELD = { 0, 1, 2, 5, 11, 11, 12, 13, 14, 7, 6, 8, 3, 4, 9, 10, 10, 15, 15, 17, 1000, 15 };
/*      */ 
/* 1041 */   private static final int[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 17, 1, 9, 17 };
/*      */ 
/* 1056 */   private static final DateFormat.Field[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID = { DateFormat.Field.ERA, DateFormat.Field.YEAR, DateFormat.Field.MONTH, DateFormat.Field.DAY_OF_MONTH, DateFormat.Field.HOUR_OF_DAY1, DateFormat.Field.HOUR_OF_DAY0, DateFormat.Field.MINUTE, DateFormat.Field.SECOND, DateFormat.Field.MILLISECOND, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.DAY_OF_YEAR, DateFormat.Field.DAY_OF_WEEK_IN_MONTH, DateFormat.Field.WEEK_OF_YEAR, DateFormat.Field.WEEK_OF_MONTH, DateFormat.Field.AM_PM, DateFormat.Field.HOUR1, DateFormat.Field.HOUR0, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.YEAR, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.TIME_ZONE };
/*      */ 
/*      */   public SimpleDateFormat()
/*      */   {
/*  544 */     this(3, 3, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public SimpleDateFormat(String paramString)
/*      */   {
/*  560 */     this(paramString, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public SimpleDateFormat(String paramString, Locale paramLocale)
/*      */   {
/*  577 */     if ((paramString == null) || (paramLocale == null)) {
/*  578 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  581 */     initializeCalendar(paramLocale);
/*  582 */     this.pattern = paramString;
/*  583 */     this.formatData = DateFormatSymbols.getInstanceRef(paramLocale);
/*  584 */     this.locale = paramLocale;
/*  585 */     initialize(paramLocale);
/*      */   }
/*      */ 
/*      */   public SimpleDateFormat(String paramString, DateFormatSymbols paramDateFormatSymbols)
/*      */   {
/*  599 */     if ((paramString == null) || (paramDateFormatSymbols == null)) {
/*  600 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  603 */     this.pattern = paramString;
/*  604 */     this.formatData = ((DateFormatSymbols)paramDateFormatSymbols.clone());
/*  605 */     this.locale = Locale.getDefault(Locale.Category.FORMAT);
/*  606 */     initializeCalendar(this.locale);
/*  607 */     initialize(this.locale);
/*  608 */     this.useDateFormatSymbols = true;
/*      */   }
/*      */ 
/*      */   SimpleDateFormat(int paramInt1, int paramInt2, Locale paramLocale)
/*      */   {
/*  613 */     if (paramLocale == null) {
/*  614 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  617 */     this.locale = paramLocale;
/*      */ 
/*  619 */     initializeCalendar(paramLocale);
/*      */ 
/*  622 */     String[] arrayOfString = (String[])cachedLocaleData.get(paramLocale);
/*      */     Object localObject;
/*  623 */     if (arrayOfString == null) {
/*  624 */       localObject = LocaleData.getDateFormatData(paramLocale);
/*  625 */       if (!isGregorianCalendar())
/*      */         try {
/*  627 */           arrayOfString = ((ResourceBundle)localObject).getStringArray(getCalendarName() + ".DateTimePatterns");
/*      */         }
/*      */         catch (MissingResourceException localMissingResourceException) {
/*      */         }
/*  631 */       if (arrayOfString == null) {
/*  632 */         arrayOfString = ((ResourceBundle)localObject).getStringArray("DateTimePatterns");
/*      */       }
/*      */ 
/*  635 */       cachedLocaleData.putIfAbsent(paramLocale, arrayOfString);
/*      */     }
/*  637 */     this.formatData = DateFormatSymbols.getInstanceRef(paramLocale);
/*  638 */     if ((paramInt1 >= 0) && (paramInt2 >= 0)) {
/*  639 */       localObject = new Object[] { arrayOfString[paramInt1], arrayOfString[(paramInt2 + 4)] };
/*      */ 
/*  641 */       this.pattern = MessageFormat.format(arrayOfString[8], (Object[])localObject);
/*      */     }
/*  643 */     else if (paramInt1 >= 0) {
/*  644 */       this.pattern = arrayOfString[paramInt1];
/*      */     }
/*  646 */     else if (paramInt2 >= 0) {
/*  647 */       this.pattern = arrayOfString[(paramInt2 + 4)];
/*      */     }
/*      */     else {
/*  650 */       throw new IllegalArgumentException("No date or time style specified");
/*      */     }
/*      */ 
/*  653 */     initialize(paramLocale);
/*      */   }
/*      */ 
/*      */   private void initialize(Locale paramLocale)
/*      */   {
/*  659 */     this.compiledPattern = compile(this.pattern);
/*      */ 
/*  662 */     this.numberFormat = ((NumberFormat)cachedNumberFormatData.get(paramLocale));
/*  663 */     if (this.numberFormat == null) {
/*  664 */       this.numberFormat = NumberFormat.getIntegerInstance(paramLocale);
/*  665 */       this.numberFormat.setGroupingUsed(false);
/*      */ 
/*  668 */       cachedNumberFormatData.putIfAbsent(paramLocale, this.numberFormat);
/*      */     }
/*  670 */     this.numberFormat = ((NumberFormat)this.numberFormat.clone());
/*      */ 
/*  672 */     initializeDefaultCentury();
/*      */   }
/*      */ 
/*      */   private void initializeCalendar(Locale paramLocale) {
/*  676 */     if (this.calendar == null) {
/*  677 */       assert (paramLocale != null);
/*      */ 
/*  682 */       this.calendar = Calendar.getInstance(TimeZone.getDefault(), paramLocale);
/*      */     }
/*      */   }
/*      */ 
/*      */   private char[] compile(String paramString)
/*      */   {
/*  751 */     int i = paramString.length();
/*  752 */     int j = 0;
/*  753 */     StringBuilder localStringBuilder1 = new StringBuilder(i * 2);
/*  754 */     StringBuilder localStringBuilder2 = null;
/*  755 */     int k = 0;
/*  756 */     int m = -1;
/*      */ 
/*  758 */     for (int n = 0; n < i; n++) {
/*  759 */       int i1 = paramString.charAt(n);
/*      */       int i2;
/*  761 */       if (i1 == 39)
/*      */       {
/*  764 */         if (n + 1 < i) {
/*  765 */           i1 = paramString.charAt(n + 1);
/*  766 */           if (i1 == 39) {
/*  767 */             n++;
/*  768 */             if (k != 0) {
/*  769 */               encode(m, k, localStringBuilder1);
/*  770 */               m = -1;
/*  771 */               k = 0;
/*      */             }
/*  773 */             if (j != 0) {
/*  774 */               localStringBuilder2.append(i1); continue;
/*      */             }
/*  776 */             localStringBuilder1.append((char)(0x6400 | i1));
/*      */ 
/*  778 */             continue;
/*      */           }
/*      */         }
/*  781 */         if (j == 0) {
/*  782 */           if (k != 0) {
/*  783 */             encode(m, k, localStringBuilder1);
/*  784 */             m = -1;
/*  785 */             k = 0;
/*      */           }
/*  787 */           if (localStringBuilder2 == null)
/*  788 */             localStringBuilder2 = new StringBuilder(i);
/*      */           else {
/*  790 */             localStringBuilder2.setLength(0);
/*      */           }
/*  792 */           j = 1;
/*      */         } else {
/*  794 */           i2 = localStringBuilder2.length();
/*  795 */           if (i2 == 1) {
/*  796 */             int i3 = localStringBuilder2.charAt(0);
/*  797 */             if (i3 < 128) {
/*  798 */               localStringBuilder1.append((char)(0x6400 | i3));
/*      */             } else {
/*  800 */               localStringBuilder1.append('攁');
/*  801 */               localStringBuilder1.append(i3);
/*      */             }
/*      */           } else {
/*  804 */             encode(101, i2, localStringBuilder1);
/*  805 */             localStringBuilder1.append(localStringBuilder2);
/*      */           }
/*  807 */           j = 0;
/*      */         }
/*      */ 
/*      */       }
/*  811 */       else if (j != 0) {
/*  812 */         localStringBuilder2.append(i1);
/*      */       }
/*  815 */       else if (((i1 < 97) || (i1 > 122)) && ((i1 < 65) || (i1 > 90))) {
/*  816 */         if (k != 0) {
/*  817 */           encode(m, k, localStringBuilder1);
/*  818 */           m = -1;
/*  819 */           k = 0;
/*      */         }
/*  821 */         if (i1 < 128)
/*      */         {
/*  823 */           localStringBuilder1.append((char)(0x6400 | i1));
/*      */         }
/*      */         else
/*      */         {
/*  828 */           for (i2 = n + 1; i2 < i; i2++) {
/*  829 */             int i4 = paramString.charAt(i2);
/*  830 */             if ((i4 == 39) || ((i4 >= 97) && (i4 <= 122)) || ((i4 >= 65) && (i4 <= 90))) {
/*      */               break;
/*      */             }
/*      */           }
/*  834 */           localStringBuilder1.append((char)(0x6500 | i2 - n));
/*  835 */           for (; n < i2; n++) {
/*  836 */             localStringBuilder1.append(paramString.charAt(n));
/*      */           }
/*  838 */           n--;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  844 */         if ((i2 = "GyMdkHmsSEDFwWahKzZYuX".indexOf(i1)) == -1) {
/*  845 */           throw new IllegalArgumentException("Illegal pattern character '" + i1 + "'");
/*      */         }
/*      */ 
/*  848 */         if ((m == -1) || (m == i2)) {
/*  849 */           m = i2;
/*  850 */           k++;
/*      */         }
/*      */         else {
/*  853 */           encode(m, k, localStringBuilder1);
/*  854 */           m = i2;
/*  855 */           k = 1;
/*      */         }
/*      */       }
/*      */     }
/*  858 */     if (j != 0) {
/*  859 */       throw new IllegalArgumentException("Unterminated quote");
/*      */     }
/*      */ 
/*  862 */     if (k != 0) {
/*  863 */       encode(m, k, localStringBuilder1);
/*      */     }
/*      */ 
/*  867 */     n = localStringBuilder1.length();
/*  868 */     char[] arrayOfChar = new char[n];
/*  869 */     localStringBuilder1.getChars(0, n, arrayOfChar, 0);
/*  870 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   private static final void encode(int paramInt1, int paramInt2, StringBuilder paramStringBuilder)
/*      */   {
/*  877 */     if ((paramInt1 == 21) && (paramInt2 >= 4)) {
/*  878 */       throw new IllegalArgumentException("invalid ISO 8601 format: length=" + paramInt2);
/*      */     }
/*  880 */     if (paramInt2 < 255) {
/*  881 */       paramStringBuilder.append((char)(paramInt1 << 8 | paramInt2));
/*      */     } else {
/*  883 */       paramStringBuilder.append((char)(paramInt1 << 8 | 0xFF));
/*  884 */       paramStringBuilder.append((char)(paramInt2 >>> 16));
/*  885 */       paramStringBuilder.append((char)(paramInt2 & 0xFFFF));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initializeDefaultCentury()
/*      */   {
/*  893 */     this.calendar.setTimeInMillis(System.currentTimeMillis());
/*  894 */     this.calendar.add(1, -80);
/*  895 */     parseAmbiguousDatesAsAfter(this.calendar.getTime());
/*      */   }
/*      */ 
/*      */   private void parseAmbiguousDatesAsAfter(Date paramDate)
/*      */   {
/*  902 */     this.defaultCenturyStart = paramDate;
/*  903 */     this.calendar.setTime(paramDate);
/*  904 */     this.defaultCenturyStartYear = this.calendar.get(1);
/*      */   }
/*      */ 
/*      */   public void set2DigitYearStart(Date paramDate)
/*      */   {
/*  917 */     parseAmbiguousDatesAsAfter(new Date(paramDate.getTime()));
/*      */   }
/*      */ 
/*      */   public Date get2DigitYearStart()
/*      */   {
/*  930 */     return (Date)this.defaultCenturyStart.clone();
/*      */   }
/*      */ 
/*      */   public StringBuffer format(Date paramDate, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  947 */     paramFieldPosition.beginIndex = (paramFieldPosition.endIndex = 0);
/*  948 */     return format(paramDate, paramStringBuffer, paramFieldPosition.getFieldDelegate());
/*      */   }
/*      */ 
/*      */   private StringBuffer format(Date paramDate, StringBuffer paramStringBuffer, Format.FieldDelegate paramFieldDelegate)
/*      */   {
/*  955 */     this.calendar.setTime(paramDate);
/*      */ 
/*  957 */     boolean bool = useDateFormatSymbols();
/*      */ 
/*  959 */     for (int i = 0; i < this.compiledPattern.length; ) {
/*  960 */       int j = this.compiledPattern[i] >>> '\b';
/*  961 */       int k = this.compiledPattern[(i++)] & 0xFF;
/*  962 */       if (k == 255) {
/*  963 */         k = this.compiledPattern[(i++)] << '\020';
/*  964 */         k |= this.compiledPattern[(i++)];
/*      */       }
/*      */ 
/*  967 */       switch (j) {
/*      */       case 100:
/*  969 */         paramStringBuffer.append((char)k);
/*  970 */         break;
/*      */       case 101:
/*  973 */         paramStringBuffer.append(this.compiledPattern, i, k);
/*  974 */         i += k;
/*  975 */         break;
/*      */       default:
/*  978 */         subFormat(j, k, paramFieldDelegate, paramStringBuffer, bool);
/*      */       }
/*      */     }
/*      */ 
/*  982 */     return paramStringBuffer;
/*      */   }
/*      */ 
/*      */   public AttributedCharacterIterator formatToCharacterIterator(Object paramObject)
/*      */   {
/* 1003 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1004 */     CharacterIteratorFieldDelegate localCharacterIteratorFieldDelegate = new CharacterIteratorFieldDelegate();
/*      */ 
/* 1007 */     if ((paramObject instanceof Date)) {
/* 1008 */       format((Date)paramObject, localStringBuffer, localCharacterIteratorFieldDelegate);
/*      */     }
/* 1010 */     else if ((paramObject instanceof Number)) {
/* 1011 */       format(new Date(((Number)paramObject).longValue()), localStringBuffer, localCharacterIteratorFieldDelegate);
/*      */     } else {
/* 1013 */       if (paramObject == null) {
/* 1014 */         throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
/*      */       }
/*      */ 
/* 1018 */       throw new IllegalArgumentException("Cannot format given Object as a Date");
/*      */     }
/*      */ 
/* 1021 */     return localCharacterIteratorFieldDelegate.getIterator(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   private void subFormat(int paramInt1, int paramInt2, Format.FieldDelegate paramFieldDelegate, StringBuffer paramStringBuffer, boolean paramBoolean)
/*      */   {
/* 1075 */     int i = 2147483647;
/* 1076 */     String str = null;
/* 1077 */     int j = paramStringBuffer.length();
/*      */ 
/* 1079 */     int k = PATTERN_INDEX_TO_CALENDAR_FIELD[paramInt1];
/*      */     int m;
/* 1081 */     if (k == 17) {
/* 1082 */       if (this.calendar.isWeekDateSupported()) {
/* 1083 */         m = this.calendar.getWeekYear();
/*      */       }
/*      */       else {
/* 1086 */         paramInt1 = 1;
/* 1087 */         k = PATTERN_INDEX_TO_CALENDAR_FIELD[paramInt1];
/* 1088 */         m = this.calendar.get(k);
/*      */       }
/* 1090 */     } else if (k == 1000)
/* 1091 */       m = CalendarBuilder.toISODayOfWeek(this.calendar.get(7));
/*      */     else {
/* 1093 */       m = this.calendar.get(k);
/*      */     }
/*      */ 
/* 1096 */     int n = paramInt2 >= 4 ? 2 : 1;
/* 1097 */     if ((!paramBoolean) && (k != 1000))
/* 1098 */       str = this.calendar.getDisplayName(k, n, this.locale);
/*      */     String[] arrayOfString;
/* 1105 */     switch (paramInt1) {
/*      */     case 0:
/* 1107 */       if (paramBoolean) {
/* 1108 */         arrayOfString = this.formatData.getEras();
/* 1109 */         if (m < arrayOfString.length)
/* 1110 */           str = arrayOfString[m];
/*      */       }
/* 1112 */       if (str == null)
/* 1113 */         str = ""; break;
/*      */     case 1:
/*      */     case 19:
/* 1118 */       if ((this.calendar instanceof GregorianCalendar)) {
/* 1119 */         if (paramInt2 != 2)
/* 1120 */           zeroPaddingNumber(m, paramInt2, i, paramStringBuffer);
/*      */         else
/* 1122 */           zeroPaddingNumber(m, 2, 2, paramStringBuffer);
/*      */       }
/* 1124 */       else if (str == null)
/* 1125 */         zeroPaddingNumber(m, n == 2 ? 1 : paramInt2, i, paramStringBuffer); break;
/*      */     case 2:
/* 1132 */       if (paramBoolean)
/*      */       {
/* 1134 */         if (paramInt2 >= 4) {
/* 1135 */           arrayOfString = this.formatData.getMonths();
/* 1136 */           str = arrayOfString[m];
/* 1137 */         } else if (paramInt2 == 3) {
/* 1138 */           arrayOfString = this.formatData.getShortMonths();
/* 1139 */           str = arrayOfString[m];
/*      */         }
/*      */       }
/* 1142 */       else if (paramInt2 < 3) {
/* 1143 */         str = null;
/*      */       }
/*      */ 
/* 1146 */       if (str == null)
/* 1147 */         zeroPaddingNumber(m + 1, paramInt2, i, paramStringBuffer); break;
/*      */     case 4:
/* 1152 */       if (str == null)
/* 1153 */         if (m == 0) {
/* 1154 */           zeroPaddingNumber(this.calendar.getMaximum(11) + 1, paramInt2, i, paramStringBuffer);
/*      */         }
/*      */         else
/* 1157 */           zeroPaddingNumber(m, paramInt2, i, paramStringBuffer); 
/* 1157 */       break;
/*      */     case 9:
/* 1162 */       if (paramBoolean)
/*      */       {
/* 1164 */         if (paramInt2 >= 4) {
/* 1165 */           arrayOfString = this.formatData.getWeekdays();
/* 1166 */           str = arrayOfString[m];
/*      */         } else {
/* 1168 */           arrayOfString = this.formatData.getShortWeekdays();
/* 1169 */           str = arrayOfString[m];
/*      */         }
/*      */       }
/* 1171 */       break;
/*      */     case 14:
/* 1175 */       if (paramBoolean) {
/* 1176 */         arrayOfString = this.formatData.getAmPmStrings();
/* 1177 */         str = arrayOfString[m];
/* 1178 */       }break;
/*      */     case 15:
/* 1182 */       if (str == null)
/* 1183 */         if (m == 0) {
/* 1184 */           zeroPaddingNumber(this.calendar.getLeastMaximum(10) + 1, paramInt2, i, paramStringBuffer);
/*      */         }
/*      */         else
/* 1187 */           zeroPaddingNumber(m, paramInt2, i, paramStringBuffer); 
/* 1187 */       break;
/*      */     case 17:
/* 1192 */       if (str == null)
/*      */       {
/*      */         int i3;
/* 1193 */         if ((this.formatData.locale == null) || (this.formatData.isZoneStringsSet)) {
/* 1194 */           int i1 = this.formatData.getZoneIndex(this.calendar.getTimeZone().getID());
/*      */ 
/* 1196 */           if (i1 == -1) {
/* 1197 */             m = this.calendar.get(15) + this.calendar.get(16);
/*      */ 
/* 1199 */             paramStringBuffer.append(ZoneInfoFile.toCustomID(m));
/*      */           } else {
/* 1201 */             i3 = this.calendar.get(16) == 0 ? 1 : 3;
/* 1202 */             if (paramInt2 < 4)
/*      */             {
/* 1204 */               i3++;
/*      */             }
/* 1206 */             String[][] arrayOfString1 = this.formatData.getZoneStringsWrapper();
/* 1207 */             paramStringBuffer.append(arrayOfString1[i1][i3]);
/*      */           }
/*      */         } else {
/* 1210 */           TimeZone localTimeZone = this.calendar.getTimeZone();
/* 1211 */           i3 = this.calendar.get(16) != 0 ? 1 : 0;
/* 1212 */           int i5 = paramInt2 < 4 ? 0 : 1;
/* 1213 */           paramStringBuffer.append(localTimeZone.getDisplayName(i3, i5, this.formatData.locale)); } 
/* 1214 */       }break;
/*      */     case 18:
/* 1219 */       m = (this.calendar.get(15) + this.calendar.get(16)) / 60000;
/*      */ 
/* 1222 */       i2 = 4;
/* 1223 */       if (m >= 0)
/* 1224 */         paramStringBuffer.append('+');
/*      */       else {
/* 1226 */         i2++;
/*      */       }
/*      */ 
/* 1229 */       int i4 = m / 60 * 100 + m % 60;
/* 1230 */       CalendarUtils.sprintf0d(paramStringBuffer, i4, i2);
/* 1231 */       break;
/*      */     case 21:
/* 1234 */       m = this.calendar.get(15) + this.calendar.get(16);
/*      */ 
/* 1237 */       if (m == 0) {
/* 1238 */         paramStringBuffer.append('Z');
/*      */       }
/*      */       else
/*      */       {
/* 1242 */         m /= 60000;
/* 1243 */         if (m >= 0) {
/* 1244 */           paramStringBuffer.append('+');
/*      */         } else {
/* 1246 */           paramStringBuffer.append('-');
/* 1247 */           m = -m;
/*      */         }
/*      */ 
/* 1250 */         CalendarUtils.sprintf0d(paramStringBuffer, m / 60, 2);
/* 1251 */         if (paramInt2 != 1)
/*      */         {
/* 1255 */           if (paramInt2 == 3) {
/* 1256 */             paramStringBuffer.append(':');
/*      */           }
/* 1258 */           CalendarUtils.sprintf0d(paramStringBuffer, m % 60, 2); } 
/* 1259 */       }break;
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 16:
/*      */     case 20:
/*      */     default:
/* 1273 */       if (str == null) {
/* 1274 */         zeroPaddingNumber(m, paramInt2, i, paramStringBuffer);
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/* 1279 */     if (str != null) {
/* 1280 */       paramStringBuffer.append(str);
/*      */     }
/*      */ 
/* 1283 */     int i2 = PATTERN_INDEX_TO_DATE_FORMAT_FIELD[paramInt1];
/* 1284 */     DateFormat.Field localField = PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID[paramInt1];
/*      */ 
/* 1286 */     paramFieldDelegate.formatted(i2, localField, localField, j, paramStringBuffer.length(), paramStringBuffer);
/*      */   }
/*      */ 
/*      */   private final void zeroPaddingNumber(int paramInt1, int paramInt2, int paramInt3, StringBuffer paramStringBuffer)
/*      */   {
/*      */     try
/*      */     {
/* 1299 */       if (this.zeroDigit == 0) {
/* 1300 */         this.zeroDigit = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols().getZeroDigit();
/*      */       }
/* 1302 */       if (paramInt1 >= 0) {
/* 1303 */         if ((paramInt1 < 100) && (paramInt2 >= 1) && (paramInt2 <= 2)) {
/* 1304 */           if (paramInt1 < 10) {
/* 1305 */             if (paramInt2 == 2) {
/* 1306 */               paramStringBuffer.append(this.zeroDigit);
/*      */             }
/* 1308 */             paramStringBuffer.append((char)(this.zeroDigit + paramInt1));
/*      */           } else {
/* 1310 */             paramStringBuffer.append((char)(this.zeroDigit + paramInt1 / 10));
/* 1311 */             paramStringBuffer.append((char)(this.zeroDigit + paramInt1 % 10));
/*      */           }
/* 1313 */           return;
/* 1314 */         }if ((paramInt1 >= 1000) && (paramInt1 < 10000)) {
/* 1315 */           if (paramInt2 == 4) {
/* 1316 */             paramStringBuffer.append((char)(this.zeroDigit + paramInt1 / 1000));
/* 1317 */             paramInt1 %= 1000;
/* 1318 */             paramStringBuffer.append((char)(this.zeroDigit + paramInt1 / 100));
/* 1319 */             paramInt1 %= 100;
/* 1320 */             paramStringBuffer.append((char)(this.zeroDigit + paramInt1 / 10));
/* 1321 */             paramStringBuffer.append((char)(this.zeroDigit + paramInt1 % 10));
/* 1322 */             return;
/*      */           }
/* 1324 */           if ((paramInt2 == 2) && (paramInt3 == 2)) {
/* 1325 */             zeroPaddingNumber(paramInt1 % 100, 2, 2, paramStringBuffer);
/* 1326 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/* 1333 */     this.numberFormat.setMinimumIntegerDigits(paramInt2);
/* 1334 */     this.numberFormat.setMaximumIntegerDigits(paramInt3);
/* 1335 */     this.numberFormat.format(paramInt1, paramStringBuffer, DontCareFieldPosition.INSTANCE);
/*      */   }
/*      */ 
/*      */   public Date parse(String paramString, ParsePosition paramParsePosition)
/*      */   {
/* 1376 */     checkNegativeNumberExpression();
/*      */ 
/* 1378 */     int i = paramParsePosition.index;
/* 1379 */     int j = i;
/* 1380 */     int k = paramString.length();
/*      */ 
/* 1382 */     boolean[] arrayOfBoolean = { false };
/*      */ 
/* 1384 */     CalendarBuilder localCalendarBuilder = new CalendarBuilder();
/*      */ 
/* 1386 */     for (int m = 0; m < this.compiledPattern.length; ) {
/* 1387 */       int n = this.compiledPattern[m] >>> '\b';
/* 1388 */       int i1 = this.compiledPattern[(m++)] & 0xFF;
/* 1389 */       if (i1 == 255) {
/* 1390 */         i1 = this.compiledPattern[(m++)] << '\020';
/* 1391 */         i1 |= this.compiledPattern[(m++)];
/*      */       }
/*      */ 
/* 1394 */       switch (n) {
/*      */       case 100:
/* 1396 */         if ((i >= k) || (paramString.charAt(i) != (char)i1)) {
/* 1397 */           paramParsePosition.index = j;
/* 1398 */           paramParsePosition.errorIndex = i;
/* 1399 */           return null;
/*      */         }
/* 1401 */         i++;
/* 1402 */         break;
/*      */       case 101:
/*      */       default:
/* 1405 */         while (i1-- > 0) {
/* 1406 */           if ((i >= k) || (paramString.charAt(i) != this.compiledPattern[(m++)])) {
/* 1407 */             paramParsePosition.index = j;
/* 1408 */             paramParsePosition.errorIndex = i;
/* 1409 */             return null;
/*      */           }
/* 1411 */           i++; continue;
/*      */ 
/* 1421 */           boolean bool1 = false;
/*      */ 
/* 1431 */           boolean bool2 = false;
/*      */ 
/* 1433 */           if (m < this.compiledPattern.length) {
/* 1434 */             int i2 = this.compiledPattern[m] >>> '\b';
/* 1435 */             if ((i2 != 100) && (i2 != 101))
/*      */             {
/* 1437 */               bool1 = true;
/*      */             }
/*      */ 
/* 1440 */             if ((this.hasFollowingMinusSign) && ((i2 == 100) || (i2 == 101)))
/*      */             {
/*      */               int i3;
/* 1444 */               if (i2 == 100)
/* 1445 */                 i3 = this.compiledPattern[m] & 0xFF;
/*      */               else {
/* 1447 */                 i3 = this.compiledPattern[(m + 1)];
/*      */               }
/*      */ 
/* 1450 */               if (i3 == this.minusSign) {
/* 1451 */                 bool2 = true;
/*      */               }
/*      */             }
/*      */           }
/* 1455 */           i = subParse(paramString, i, n, i1, bool1, arrayOfBoolean, paramParsePosition, bool2, localCalendarBuilder);
/*      */ 
/* 1458 */           if (i < 0) {
/* 1459 */             paramParsePosition.index = j;
/* 1460 */             return null;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1469 */     paramParsePosition.index = i;
/*      */     Date localDate;
/*      */     try {
/* 1473 */       localDate = localCalendarBuilder.establish(this.calendar).getTime();
/*      */ 
/* 1476 */       if ((arrayOfBoolean[0] != 0) && 
/* 1477 */         (localDate.before(this.defaultCenturyStart))) {
/* 1478 */         localDate = localCalendarBuilder.addYear(100).establish(this.calendar).getTime();
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/* 1485 */       paramParsePosition.errorIndex = i;
/* 1486 */       paramParsePosition.index = j;
/* 1487 */       return null;
/*      */     }
/*      */ 
/* 1490 */     return localDate;
/*      */   }
/*      */ 
/*      */   private int matchString(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString, CalendarBuilder paramCalendarBuilder)
/*      */   {
/* 1504 */     int i = 0;
/* 1505 */     int j = paramArrayOfString.length;
/*      */ 
/* 1507 */     if (paramInt2 == 7) i = 1;
/*      */ 
/* 1513 */     int k = 0; int m = -1;
/* 1514 */     for (; i < j; i++)
/*      */     {
/* 1516 */       int n = paramArrayOfString[i].length();
/*      */ 
/* 1519 */       if ((n > k) && (paramString.regionMatches(true, paramInt1, paramArrayOfString[i], 0, n)))
/*      */       {
/* 1522 */         m = i;
/* 1523 */         k = n;
/*      */       }
/*      */     }
/* 1526 */     if (m >= 0)
/*      */     {
/* 1528 */       paramCalendarBuilder.set(paramInt2, m);
/* 1529 */       return paramInt1 + k;
/*      */     }
/* 1531 */     return -paramInt1;
/*      */   }
/*      */ 
/*      */   private int matchString(String paramString, int paramInt1, int paramInt2, Map<String, Integer> paramMap, CalendarBuilder paramCalendarBuilder)
/*      */   {
/* 1541 */     if (paramMap != null) {
/* 1542 */       Object localObject = null;
/*      */ 
/* 1544 */       for (String str : paramMap.keySet()) {
/* 1545 */         int i = str.length();
/* 1546 */         if (((localObject == null) || (i > localObject.length())) && 
/* 1547 */           (paramString.regionMatches(true, paramInt1, str, 0, i))) {
/* 1548 */           localObject = str;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1553 */       if (localObject != null) {
/* 1554 */         paramCalendarBuilder.set(paramInt2, ((Integer)paramMap.get(localObject)).intValue());
/* 1555 */         return paramInt1 + localObject.length();
/*      */       }
/*      */     }
/* 1558 */     return -paramInt1;
/*      */   }
/*      */ 
/*      */   private int matchZoneString(String paramString, int paramInt, String[] paramArrayOfString) {
/* 1562 */     for (int i = 1; i <= 4; i++)
/*      */     {
/* 1565 */       String str = paramArrayOfString[i];
/* 1566 */       if (paramString.regionMatches(true, paramInt, str, 0, str.length()))
/*      */       {
/* 1568 */         return i;
/*      */       }
/*      */     }
/* 1571 */     return -1;
/*      */   }
/*      */ 
/*      */   private boolean matchDSTString(String paramString, int paramInt1, int paramInt2, int paramInt3, String[][] paramArrayOfString)
/*      */   {
/* 1576 */     int i = paramInt3 + 2;
/* 1577 */     String str = paramArrayOfString[paramInt2][i];
/* 1578 */     if (paramString.regionMatches(true, paramInt1, str, 0, str.length()))
/*      */     {
/* 1580 */       return true;
/*      */     }
/* 1582 */     return false;
/*      */   }
/*      */ 
/*      */   private int subParseZoneString(String paramString, int paramInt, CalendarBuilder paramCalendarBuilder)
/*      */   {
/* 1590 */     boolean bool = false;
/* 1591 */     TimeZone localTimeZone1 = getTimeZone();
/*      */ 
/* 1596 */     int i = this.formatData.getZoneIndex(localTimeZone1.getID());
/* 1597 */     TimeZone localTimeZone2 = null;
/* 1598 */     String[][] arrayOfString = this.formatData.getZoneStringsWrapper();
/* 1599 */     String[] arrayOfString1 = null;
/* 1600 */     int j = 0;
/* 1601 */     if (i != -1) {
/* 1602 */       arrayOfString1 = arrayOfString[i];
/* 1603 */       if ((j = matchZoneString(paramString, paramInt, arrayOfString1)) > 0) {
/* 1604 */         if (j <= 2)
/*      */         {
/* 1606 */           bool = arrayOfString1[j].equalsIgnoreCase(arrayOfString1[(j + 2)]);
/*      */         }
/* 1608 */         localTimeZone2 = TimeZone.getTimeZone(arrayOfString1[0]);
/*      */       }
/*      */     }
/* 1611 */     if (localTimeZone2 == null) {
/* 1612 */       i = this.formatData.getZoneIndex(TimeZone.getDefault().getID());
/* 1613 */       if (i != -1) {
/* 1614 */         arrayOfString1 = arrayOfString[i];
/* 1615 */         if ((j = matchZoneString(paramString, paramInt, arrayOfString1)) > 0) {
/* 1616 */           if (j <= 2) {
/* 1617 */             bool = arrayOfString1[j].equalsIgnoreCase(arrayOfString1[(j + 2)]);
/*      */           }
/* 1619 */           localTimeZone2 = TimeZone.getTimeZone(arrayOfString1[0]);
/*      */         }
/*      */       }
/*      */     }
/*      */     int k;
/* 1624 */     if (localTimeZone2 == null) {
/* 1625 */       k = arrayOfString.length;
/* 1626 */       for (int m = 0; m < k; m++) {
/* 1627 */         arrayOfString1 = arrayOfString[m];
/* 1628 */         if ((j = matchZoneString(paramString, paramInt, arrayOfString1)) > 0) {
/* 1629 */           if (j <= 2) {
/* 1630 */             bool = arrayOfString1[j].equalsIgnoreCase(arrayOfString1[(j + 2)]);
/*      */           }
/* 1632 */           localTimeZone2 = TimeZone.getTimeZone(arrayOfString1[0]);
/* 1633 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1637 */     if (localTimeZone2 != null) {
/* 1638 */       if (!localTimeZone2.equals(localTimeZone1)) {
/* 1639 */         setTimeZone(localTimeZone2);
/*      */       }
/*      */ 
/* 1647 */       k = j >= 3 ? localTimeZone2.getDSTSavings() : 0;
/* 1648 */       if ((!bool) && ((j < 3) || (k != 0))) {
/* 1649 */         paramCalendarBuilder.clear(15).set(16, k);
/*      */       }
/* 1651 */       return paramInt + arrayOfString1[j].length();
/*      */     }
/* 1653 */     return 0;
/*      */   }
/*      */ 
/*      */   private int subParseNumericZone(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, CalendarBuilder paramCalendarBuilder)
/*      */   {
/* 1670 */     int i = paramInt1;
/*      */     try
/*      */     {
/* 1674 */       char c = paramString.charAt(i++);
/*      */ 
/* 1677 */       if (isDigit(c))
/*      */       {
/* 1680 */         int j = c - '0';
/* 1681 */         c = paramString.charAt(i++);
/* 1682 */         if (isDigit(c)) {
/* 1683 */           j = j * 10 + (c - '0');
/*      */         }
/*      */         else
/*      */         {
/* 1687 */           if ((paramInt3 <= 0) && (!paramBoolean)) {
/*      */             break label242;
/*      */           }
/* 1690 */           i--;
/*      */         }
/* 1692 */         if (j <= 23)
/*      */         {
/* 1695 */           int k = 0;
/* 1696 */           if (paramInt3 != 1)
/*      */           {
/* 1698 */             c = paramString.charAt(i++);
/* 1699 */             if (paramBoolean) {
/* 1700 */               if (c == ':')
/*      */               {
/* 1703 */                 c = paramString.charAt(i++);
/*      */               }
/* 1705 */             } else if (isDigit(c))
/*      */             {
/* 1708 */               k = c - '0';
/* 1709 */               c = paramString.charAt(i++);
/* 1710 */               if (isDigit(c)) {
/* 1713 */                 k = k * 10 + (c - '0');
/* 1714 */                 if (k > 59);
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/* 1718 */             k += j * 60;
/* 1719 */             paramCalendarBuilder.set(15, k * 60000 * paramInt2).set(16, 0);
/*      */ 
/* 1721 */             return i; } 
/*      */         }
/*      */       } } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {  }
/*      */ 
/* 1724 */     label242: return 1 - i;
/*      */   }
/*      */ 
/*      */   private boolean isDigit(char paramChar) {
/* 1728 */     return (paramChar >= '0') && (paramChar <= '9');
/*      */   }
/*      */ 
/*      */   private int subParse(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean[] paramArrayOfBoolean, ParsePosition paramParsePosition, boolean paramBoolean2, CalendarBuilder paramCalendarBuilder)
/*      */   {
/* 1752 */     Number localNumber = null;
/* 1753 */     int i = 0;
/* 1754 */     ParsePosition localParsePosition = new ParsePosition(0);
/* 1755 */     localParsePosition.index = paramInt1;
/* 1756 */     if ((paramInt2 == 19) && (!this.calendar.isWeekDateSupported()))
/*      */     {
/* 1758 */       paramInt2 = 1;
/*      */     }
/* 1760 */     int j = PATTERN_INDEX_TO_CALENDAR_FIELD[paramInt2];
/*      */     while (true)
/*      */     {
/* 1765 */       if (localParsePosition.index >= paramString.length()) {
/* 1766 */         paramParsePosition.errorIndex = paramInt1;
/* 1767 */         return -1;
/*      */       }
/* 1769 */       int k = paramString.charAt(localParsePosition.index);
/* 1770 */       if ((k != 32) && (k != 9)) break;
/* 1771 */       localParsePosition.index += 1;
/*      */     }
/*      */ 
/* 1780 */     if ((paramInt2 == 4) || (paramInt2 == 15) || ((paramInt2 == 2) && (paramInt3 <= 2)) || (paramInt2 == 1) || (paramInt2 == 19))
/*      */     {
/* 1787 */       if (paramBoolean1) {
/* 1788 */         if (paramInt1 + paramInt3 > paramString.length()) {
/*      */           break label1767;
/*      */         }
/* 1791 */         localNumber = this.numberFormat.parse(paramString.substring(0, paramInt1 + paramInt3), localParsePosition);
/*      */       } else {
/* 1793 */         localNumber = this.numberFormat.parse(paramString, localParsePosition);
/*      */       }
/* 1795 */       if (localNumber == null) {
/* 1796 */         if (paramInt2 != 1) break label1767; if ((this.calendar instanceof GregorianCalendar))
/* 1797 */           break label1767;
/*      */       }
/*      */       else {
/* 1800 */         i = localNumber.intValue();
/*      */ 
/* 1802 */         if ((paramBoolean2) && (i < 0) && (((localParsePosition.index < paramString.length()) && (paramString.charAt(localParsePosition.index) != this.minusSign)) || ((localParsePosition.index == paramString.length()) && (paramString.charAt(localParsePosition.index - 1) == this.minusSign))))
/*      */         {
/* 1807 */           i = -i;
/* 1808 */           localParsePosition.index -= 1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1813 */     boolean bool = useDateFormatSymbols();
/*      */     int m;
/*      */     int n;
/*      */     Object localObject2;
/*      */     Object localObject1;
/*      */     int i2;
/* 1816 */     switch (paramInt2) {
/*      */     case 0:
/* 1818 */       if (bool) {
/* 1819 */         if ((m = matchString(paramString, paramInt1, 0, this.formatData.getEras(), paramCalendarBuilder)) > 0)
/* 1820 */           return m;
/*      */       }
/*      */       else {
/* 1823 */         Map localMap1 = this.calendar.getDisplayNames(j, 0, this.locale);
/*      */ 
/* 1826 */         if ((m = matchString(paramString, paramInt1, j, localMap1, paramCalendarBuilder)) > 0) {
/* 1827 */           return m;
/*      */         }
/*      */       }
/* 1830 */       break;
/*      */     case 1:
/*      */     case 19:
/* 1834 */       if (!(this.calendar instanceof GregorianCalendar))
/*      */       {
/* 1837 */         n = paramInt3 >= 4 ? 2 : 1;
/* 1838 */         localObject2 = this.calendar.getDisplayNames(j, n, this.locale);
/* 1839 */         if ((localObject2 != null) && 
/* 1840 */           ((m = matchString(paramString, paramInt1, j, (Map)localObject2, paramCalendarBuilder)) > 0)) {
/* 1841 */           return m;
/*      */         }
/*      */ 
/* 1844 */         paramCalendarBuilder.set(j, i);
/* 1845 */         return localParsePosition.index;
/*      */       }
/*      */ 
/* 1854 */       if ((paramInt3 <= 2) && (localParsePosition.index - paramInt1 == 2) && (Character.isDigit(paramString.charAt(paramInt1))) && (Character.isDigit(paramString.charAt(paramInt1 + 1))))
/*      */       {
/* 1865 */         n = this.defaultCenturyStartYear % 100;
/* 1866 */         paramArrayOfBoolean[0] = (i == n ? 1 : false);
/* 1867 */         i += this.defaultCenturyStartYear / 100 * 100 + (i < n ? 100 : 0);
/*      */       }
/*      */ 
/* 1870 */       paramCalendarBuilder.set(j, i);
/* 1871 */       return localParsePosition.index;
/*      */     case 2:
/* 1874 */       if (paramInt3 <= 2)
/*      */       {
/* 1879 */         paramCalendarBuilder.set(2, i - 1);
/* 1880 */         return localParsePosition.index;
/*      */       }
/*      */ 
/* 1883 */       if (bool)
/*      */       {
/* 1887 */         n = 0;
/* 1888 */         if ((n = matchString(paramString, paramInt1, 2, this.formatData.getMonths(), paramCalendarBuilder)) > 0)
/*      */         {
/* 1890 */           return n;
/*      */         }
/*      */ 
/* 1893 */         if ((m = matchString(paramString, paramInt1, 2, this.formatData.getShortMonths(), paramCalendarBuilder)) > 0)
/*      */         {
/* 1895 */           return m;
/*      */         }
/*      */       } else {
/* 1898 */         Map localMap2 = this.calendar.getDisplayNames(j, 0, this.locale);
/*      */ 
/* 1901 */         if ((m = matchString(paramString, paramInt1, j, localMap2, paramCalendarBuilder)) > 0) {
/* 1902 */           return m;
/*      */         }
/*      */       }
/* 1905 */       break;
/*      */     case 4:
/* 1908 */       if ((isLenient()) || (
/* 1910 */         (i >= 1) && (i <= 24)))
/*      */       {
/* 1915 */         if (i == this.calendar.getMaximum(11) + 1)
/* 1916 */           i = 0;
/* 1917 */         paramCalendarBuilder.set(11, i);
/* 1918 */         return localParsePosition.index;
/*      */       }
/*      */       break;
/*      */     case 9:
/* 1922 */       if (bool)
/*      */       {
/* 1925 */         int i1 = 0;
/* 1926 */         if ((i1 = matchString(paramString, paramInt1, 7, this.formatData.getWeekdays(), paramCalendarBuilder)) > 0)
/*      */         {
/* 1928 */           return i1;
/*      */         }
/*      */ 
/* 1931 */         if ((m = matchString(paramString, paramInt1, 7, this.formatData.getShortWeekdays(), paramCalendarBuilder)) > 0)
/*      */         {
/* 1933 */           return m;
/*      */         }
/*      */       } else {
/* 1936 */         localObject1 = new int[] { 2, 1 };
/* 1937 */         for (int i7 : localObject1) {
/* 1938 */           Map localMap3 = this.calendar.getDisplayNames(j, i7, this.locale);
/* 1939 */           if ((m = matchString(paramString, paramInt1, j, localMap3, paramCalendarBuilder)) > 0) {
/* 1940 */             return m;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1945 */       break;
/*      */     case 14:
/* 1948 */       if (bool) {
/* 1949 */         if ((m = matchString(paramString, paramInt1, 9, this.formatData.getAmPmStrings(), paramCalendarBuilder)) > 0)
/*      */         {
/* 1951 */           return m;
/*      */         }
/*      */       } else {
/* 1954 */         localObject1 = this.calendar.getDisplayNames(j, 0, this.locale);
/* 1955 */         if ((m = matchString(paramString, paramInt1, j, (Map)localObject1, paramCalendarBuilder)) > 0) {
/* 1956 */           return m;
/*      */         }
/*      */       }
/* 1959 */       break;
/*      */     case 15:
/* 1962 */       if ((isLenient()) || (
/* 1964 */         (i >= 1) && (i <= 12)))
/*      */       {
/* 1969 */         if (i == this.calendar.getLeastMaximum(10) + 1)
/* 1970 */           i = 0;
/* 1971 */         paramCalendarBuilder.set(10, i);
/* 1972 */         return localParsePosition.index;
/*      */       }
/*      */       break;
/*      */     case 17:
/*      */     case 18:
/* 1977 */       i2 = 0;
/*      */       try {
/* 1979 */         int i3 = paramString.charAt(localParsePosition.index);
/* 1980 */         if (i3 == 43)
/* 1981 */           i2 = 1;
/* 1982 */         else if (i3 == 45) {
/* 1983 */           i2 = -1;
/*      */         }
/* 1985 */         if (i2 == 0)
/*      */         {
/* 1987 */           if (((i3 == 71) || (i3 == 103)) && (paramString.length() - paramInt1 >= "GMT".length()) && (paramString.regionMatches(true, paramInt1, "GMT", 0, "GMT".length())))
/*      */           {
/* 1990 */             localParsePosition.index = (paramInt1 + "GMT".length());
/*      */ 
/* 1992 */             if (paramString.length() - localParsePosition.index > 0) {
/* 1993 */               i3 = paramString.charAt(localParsePosition.index);
/* 1994 */               if (i3 == 43)
/* 1995 */                 i2 = 1;
/* 1996 */               else if (i3 == 45) {
/* 1997 */                 i2 = -1;
/*      */               }
/*      */             }
/*      */ 
/* 2001 */             if (i2 == 0) {
/* 2002 */               paramCalendarBuilder.set(15, 0).set(16, 0);
/*      */ 
/* 2004 */               return localParsePosition.index;
/*      */             }
/*      */ 
/* 2008 */             ??? = subParseNumericZone(paramString, ++localParsePosition.index, i2, 0, true, paramCalendarBuilder);
/*      */ 
/* 2010 */             if (??? > 0) {
/* 2011 */               return ???;
/*      */             }
/* 2013 */             localParsePosition.index = (-???);
/*      */           }
/*      */           else
/*      */           {
/* 2017 */             ??? = subParseZoneString(paramString, localParsePosition.index, paramCalendarBuilder);
/* 2018 */             if (??? > 0) {
/* 2019 */               return ???;
/*      */             }
/* 2021 */             localParsePosition.index = (-???);
/*      */           }
/*      */         }
/*      */         else {
/* 2025 */           ??? = subParseNumericZone(paramString, ++localParsePosition.index, i2, 0, false, paramCalendarBuilder);
/*      */ 
/* 2027 */           if (??? > 0) {
/* 2028 */             return ???;
/*      */           }
/* 2030 */           localParsePosition.index = (-???);
/*      */         }
/*      */       }
/*      */       catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*      */       }
/* 2035 */       break;
/*      */     case 21:
/* 2039 */       if (paramString.length() - localParsePosition.index > 0)
/*      */       {
/* 2043 */         i2 = 0;
/* 2044 */         int i4 = paramString.charAt(localParsePosition.index);
/* 2045 */         if (i4 == 90) {
/* 2046 */           paramCalendarBuilder.set(15, 0).set(16, 0);
/* 2047 */           return ++localParsePosition.index;
/*      */         }
/*      */ 
/* 2051 */         if (i4 == 43) {
/* 2052 */           i2 = 1;
/* 2053 */         } else if (i4 == 45) {
/* 2054 */           i2 = -1;
/*      */         } else {
/* 2056 */           localParsePosition.index += 1;
/* 2057 */           break;
/*      */         }
/* 2059 */         ??? = subParseNumericZone(paramString, ++localParsePosition.index, i2, paramInt3, paramInt3 == 3, paramCalendarBuilder);
/*      */ 
/* 2061 */         if (??? > 0) {
/* 2062 */           return ???;
/*      */         }
/* 2064 */         localParsePosition.index = (-???);
/*      */       }
/* 2066 */       break;
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 16:
/*      */     case 20:
/*      */     default:
/* 2082 */       if (paramBoolean1) {
/* 2083 */         if (paramInt1 + paramInt3 > paramString.length()) {
/*      */           break;
/*      */         }
/* 2086 */         localNumber = this.numberFormat.parse(paramString.substring(0, paramInt1 + paramInt3), localParsePosition);
/*      */       } else {
/* 2088 */         localNumber = this.numberFormat.parse(paramString, localParsePosition);
/*      */       }
/* 2090 */       if (localNumber != null) {
/* 2091 */         i = localNumber.intValue();
/*      */ 
/* 2093 */         if ((paramBoolean2) && (i < 0) && (((localParsePosition.index < paramString.length()) && (paramString.charAt(localParsePosition.index) != this.minusSign)) || ((localParsePosition.index == paramString.length()) && (paramString.charAt(localParsePosition.index - 1) == this.minusSign))))
/*      */         {
/* 2098 */           i = -i;
/* 2099 */           localParsePosition.index -= 1;
/*      */         }
/*      */ 
/* 2102 */         paramCalendarBuilder.set(j, i);
/* 2103 */         return localParsePosition.index;
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/* 2110 */     label1767: paramParsePosition.errorIndex = localParsePosition.index;
/* 2111 */     return -1;
/*      */   }
/*      */ 
/*      */   private final String getCalendarName() {
/* 2115 */     return this.calendar.getClass().getName();
/*      */   }
/*      */ 
/*      */   private boolean useDateFormatSymbols() {
/* 2119 */     if (this.useDateFormatSymbols) {
/* 2120 */       return true;
/*      */     }
/* 2122 */     return (isGregorianCalendar()) || (this.locale == null);
/*      */   }
/*      */ 
/*      */   private boolean isGregorianCalendar() {
/* 2126 */     return "java.util.GregorianCalendar".equals(getCalendarName());
/*      */   }
/*      */ 
/*      */   private String translatePattern(String paramString1, String paramString2, String paramString3)
/*      */   {
/* 2136 */     StringBuilder localStringBuilder = new StringBuilder();
/* 2137 */     int i = 0;
/* 2138 */     for (int j = 0; j < paramString1.length(); j++) {
/* 2139 */       char c = paramString1.charAt(j);
/* 2140 */       if (i != 0) {
/* 2141 */         if (c == '\'') {
/* 2142 */           i = 0;
/*      */         }
/*      */       }
/* 2145 */       else if (c == '\'') {
/* 2146 */         i = 1;
/* 2147 */       } else if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
/* 2148 */         int k = paramString2.indexOf(c);
/* 2149 */         if (k >= 0)
/*      */         {
/* 2153 */           if (k < paramString3.length())
/* 2154 */             c = paramString3.charAt(k);
/*      */         }
/*      */         else {
/* 2157 */           throw new IllegalArgumentException("Illegal pattern  character '" + c + "'");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2163 */       localStringBuilder.append(c);
/*      */     }
/* 2165 */     if (i != 0)
/* 2166 */       throw new IllegalArgumentException("Unfinished quote in pattern");
/* 2167 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public String toPattern()
/*      */   {
/* 2176 */     return this.pattern;
/*      */   }
/*      */ 
/*      */   public String toLocalizedPattern()
/*      */   {
/* 2185 */     return translatePattern(this.pattern, "GyMdkHmsSEDFwWahKzZYuX", this.formatData.getLocalPatternChars());
/*      */   }
/*      */ 
/*      */   public void applyPattern(String paramString)
/*      */   {
/* 2199 */     this.compiledPattern = compile(paramString);
/* 2200 */     this.pattern = paramString;
/*      */   }
/*      */ 
/*      */   public void applyLocalizedPattern(String paramString)
/*      */   {
/* 2212 */     String str = translatePattern(paramString, this.formatData.getLocalPatternChars(), "GyMdkHmsSEDFwWahKzZYuX");
/*      */ 
/* 2215 */     this.compiledPattern = compile(str);
/* 2216 */     this.pattern = str;
/*      */   }
/*      */ 
/*      */   public DateFormatSymbols getDateFormatSymbols()
/*      */   {
/* 2227 */     return (DateFormatSymbols)this.formatData.clone();
/*      */   }
/*      */ 
/*      */   public void setDateFormatSymbols(DateFormatSymbols paramDateFormatSymbols)
/*      */   {
/* 2239 */     this.formatData = ((DateFormatSymbols)paramDateFormatSymbols.clone());
/* 2240 */     this.useDateFormatSymbols = true;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 2250 */     SimpleDateFormat localSimpleDateFormat = (SimpleDateFormat)super.clone();
/* 2251 */     localSimpleDateFormat.formatData = ((DateFormatSymbols)this.formatData.clone());
/* 2252 */     return localSimpleDateFormat;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2262 */     return this.pattern.hashCode();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 2275 */     if (!super.equals(paramObject)) return false;
/* 2276 */     SimpleDateFormat localSimpleDateFormat = (SimpleDateFormat)paramObject;
/* 2277 */     return (this.pattern.equals(localSimpleDateFormat.pattern)) && (this.formatData.equals(localSimpleDateFormat.formatData));
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2289 */     paramObjectInputStream.defaultReadObject();
/*      */     try
/*      */     {
/* 2292 */       this.compiledPattern = compile(this.pattern);
/*      */     } catch (Exception localException) {
/* 2294 */       throw new InvalidObjectException("invalid pattern");
/*      */     }
/*      */ 
/* 2297 */     if (this.serialVersionOnStream < 1)
/*      */     {
/* 2299 */       initializeDefaultCentury();
/*      */     }
/*      */     else
/*      */     {
/* 2303 */       parseAmbiguousDatesAsAfter(this.defaultCenturyStart);
/*      */     }
/* 2305 */     this.serialVersionOnStream = 1;
/*      */ 
/* 2311 */     TimeZone localTimeZone1 = getTimeZone();
/* 2312 */     if ((localTimeZone1 instanceof SimpleTimeZone)) {
/* 2313 */       String str = localTimeZone1.getID();
/* 2314 */       TimeZone localTimeZone2 = TimeZone.getTimeZone(str);
/* 2315 */       if ((localTimeZone2 != null) && (localTimeZone2.hasSameRules(localTimeZone1)) && (localTimeZone2.getID().equals(str)))
/* 2316 */         setTimeZone(localTimeZone2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkNegativeNumberExpression()
/*      */   {
/* 2326 */     if (((this.numberFormat instanceof DecimalFormat)) && (!this.numberFormat.equals(this.originalNumberFormat)))
/*      */     {
/* 2328 */       String str = ((DecimalFormat)this.numberFormat).toPattern();
/* 2329 */       if (!str.equals(this.originalNumberPattern)) {
/* 2330 */         this.hasFollowingMinusSign = false;
/*      */ 
/* 2332 */         int i = str.indexOf(';');
/*      */ 
/* 2335 */         if (i > -1) {
/* 2336 */           int j = str.indexOf('-', i);
/* 2337 */           if ((j > str.lastIndexOf('0')) && (j > str.lastIndexOf('#')))
/*      */           {
/* 2339 */             this.hasFollowingMinusSign = true;
/* 2340 */             this.minusSign = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols().getMinusSign();
/*      */           }
/*      */         }
/* 2343 */         this.originalNumberPattern = str;
/*      */       }
/* 2345 */       this.originalNumberFormat = this.numberFormat;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.SimpleDateFormat
 * JD-Core Version:    0.6.2
 */