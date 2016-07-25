/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OptionalDataException;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.text.DateFormat;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import sun.util.BuddhistCalendar;
/*      */ import sun.util.calendar.ZoneInfo;
/*      */ import sun.util.resources.LocaleData;
/*      */ 
/*      */ public abstract class Calendar
/*      */   implements Serializable, Cloneable, Comparable<Calendar>
/*      */ {
/*      */   public static final int ERA = 0;
/*      */   public static final int YEAR = 1;
/*      */   public static final int MONTH = 2;
/*      */   public static final int WEEK_OF_YEAR = 3;
/*      */   public static final int WEEK_OF_MONTH = 4;
/*      */   public static final int DATE = 5;
/*      */   public static final int DAY_OF_MONTH = 5;
/*      */   public static final int DAY_OF_YEAR = 6;
/*      */   public static final int DAY_OF_WEEK = 7;
/*      */   public static final int DAY_OF_WEEK_IN_MONTH = 8;
/*      */   public static final int AM_PM = 9;
/*      */   public static final int HOUR = 10;
/*      */   public static final int HOUR_OF_DAY = 11;
/*      */   public static final int MINUTE = 12;
/*      */   public static final int SECOND = 13;
/*      */   public static final int MILLISECOND = 14;
/*      */   public static final int ZONE_OFFSET = 15;
/*      */   public static final int DST_OFFSET = 16;
/*      */   public static final int FIELD_COUNT = 17;
/*      */   public static final int SUNDAY = 1;
/*      */   public static final int MONDAY = 2;
/*      */   public static final int TUESDAY = 3;
/*      */   public static final int WEDNESDAY = 4;
/*      */   public static final int THURSDAY = 5;
/*      */   public static final int FRIDAY = 6;
/*      */   public static final int SATURDAY = 7;
/*      */   public static final int JANUARY = 0;
/*      */   public static final int FEBRUARY = 1;
/*      */   public static final int MARCH = 2;
/*      */   public static final int APRIL = 3;
/*      */   public static final int MAY = 4;
/*      */   public static final int JUNE = 5;
/*      */   public static final int JULY = 6;
/*      */   public static final int AUGUST = 7;
/*      */   public static final int SEPTEMBER = 8;
/*      */   public static final int OCTOBER = 9;
/*      */   public static final int NOVEMBER = 10;
/*      */   public static final int DECEMBER = 11;
/*      */   public static final int UNDECIMBER = 12;
/*      */   public static final int AM = 0;
/*      */   public static final int PM = 1;
/*      */   public static final int ALL_STYLES = 0;
/*      */   public static final int SHORT = 1;
/*      */   public static final int LONG = 2;
/*      */   protected int[] fields;
/*      */   protected boolean[] isSet;
/*      */   private transient int[] stamp;
/*      */   protected long time;
/*      */   protected boolean isTimeSet;
/*      */   protected boolean areFieldsSet;
/*      */   transient boolean areAllFieldsSet;
/*  810 */   private boolean lenient = true;
/*      */   private TimeZone zone;
/*  822 */   private transient boolean sharedZone = false;
/*      */   private int firstDayOfWeek;
/*      */   private int minimalDaysInFirstWeek;
/*  842 */   private static final ConcurrentMap<Locale, int[]> cachedLocaleData = new ConcurrentHashMap(3);
/*      */   private static final int UNSET = 0;
/*      */   private static final int COMPUTED = 1;
/*      */   private static final int MINIMUM_USER_STAMP = 2;
/*      */   static final int ALL_FIELDS = 131071;
/*  875 */   private int nextStamp = 2;
/*      */   static final int currentSerialVersion = 1;
/*  907 */   private int serialVersionOnStream = 1;
/*      */   static final long serialVersionUID = -1807547505821590642L;
/*      */   static final int ERA_MASK = 1;
/*      */   static final int YEAR_MASK = 2;
/*      */   static final int MONTH_MASK = 4;
/*      */   static final int WEEK_OF_YEAR_MASK = 8;
/*      */   static final int WEEK_OF_MONTH_MASK = 16;
/*      */   static final int DAY_OF_MONTH_MASK = 32;
/*      */   static final int DATE_MASK = 32;
/*      */   static final int DAY_OF_YEAR_MASK = 64;
/*      */   static final int DAY_OF_WEEK_MASK = 128;
/*      */   static final int DAY_OF_WEEK_IN_MONTH_MASK = 256;
/*      */   static final int AM_PM_MASK = 512;
/*      */   static final int HOUR_MASK = 1024;
/*      */   static final int HOUR_OF_DAY_MASK = 2048;
/*      */   static final int MINUTE_MASK = 4096;
/*      */   static final int SECOND_MASK = 8192;
/*      */   static final int MILLISECOND_MASK = 16384;
/*      */   static final int ZONE_OFFSET_MASK = 32768;
/*      */   static final int DST_OFFSET_MASK = 65536;
/* 2519 */   private static final String[] FIELD_NAME = { "ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET" };
/*      */ 
/*      */   protected Calendar()
/*      */   {
/*  939 */     this(TimeZone.getDefaultRef(), Locale.getDefault(Locale.Category.FORMAT));
/*  940 */     this.sharedZone = true;
/*      */   }
/*      */ 
/*      */   protected Calendar(TimeZone paramTimeZone, Locale paramLocale)
/*      */   {
/*  951 */     this.fields = new int[17];
/*  952 */     this.isSet = new boolean[17];
/*  953 */     this.stamp = new int[17];
/*      */ 
/*  955 */     this.zone = paramTimeZone;
/*  956 */     setWeekCountData(paramLocale);
/*      */   }
/*      */ 
/*      */   public static Calendar getInstance()
/*      */   {
/*  968 */     Calendar localCalendar = createCalendar(TimeZone.getDefaultRef(), Locale.getDefault(Locale.Category.FORMAT));
/*  969 */     localCalendar.sharedZone = true;
/*  970 */     return localCalendar;
/*      */   }
/*      */ 
/*      */   public static Calendar getInstance(TimeZone paramTimeZone)
/*      */   {
/*  983 */     return createCalendar(paramTimeZone, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public static Calendar getInstance(Locale paramLocale)
/*      */   {
/*  996 */     Calendar localCalendar = createCalendar(TimeZone.getDefaultRef(), paramLocale);
/*  997 */     localCalendar.sharedZone = true;
/*  998 */     return localCalendar;
/*      */   }
/*      */ 
/*      */   public static Calendar getInstance(TimeZone paramTimeZone, Locale paramLocale)
/*      */   {
/* 1013 */     return createCalendar(paramTimeZone, paramLocale);
/*      */   }
/*      */ 
/*      */   private static Calendar createCalendar(TimeZone paramTimeZone, Locale paramLocale)
/*      */   {
/* 1019 */     Object localObject = null;
/*      */ 
/* 1021 */     String str = paramLocale.getUnicodeLocaleType("ca");
/* 1022 */     if (str == null)
/*      */     {
/* 1026 */       if (("th".equals(paramLocale.getLanguage())) && ("TH".equals(paramLocale.getCountry())))
/*      */       {
/* 1028 */         localObject = new BuddhistCalendar(paramTimeZone, paramLocale);
/*      */       }
/* 1030 */       else localObject = new GregorianCalendar(paramTimeZone, paramLocale);
/*      */     }
/* 1032 */     else if (str.equals("japanese"))
/* 1033 */       localObject = new JapaneseImperialCalendar(paramTimeZone, paramLocale);
/* 1034 */     else if (str.equals("buddhist")) {
/* 1035 */       localObject = new BuddhistCalendar(paramTimeZone, paramLocale);
/*      */     }
/*      */     else
/*      */     {
/* 1039 */       localObject = new GregorianCalendar(paramTimeZone, paramLocale);
/*      */     }
/*      */ 
/* 1042 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static synchronized Locale[] getAvailableLocales()
/*      */   {
/* 1056 */     return DateFormat.getAvailableLocales();
/*      */   }
/*      */ 
/*      */   protected abstract void computeTime();
/*      */ 
/*      */   protected abstract void computeFields();
/*      */ 
/*      */   public final Date getTime()
/*      */   {
/* 1091 */     return new Date(getTimeInMillis());
/*      */   }
/*      */ 
/*      */   public final void setTime(Date paramDate)
/*      */   {
/* 1106 */     setTimeInMillis(paramDate.getTime());
/*      */   }
/*      */ 
/*      */   public long getTimeInMillis()
/*      */   {
/* 1117 */     if (!this.isTimeSet) {
/* 1118 */       updateTime();
/*      */     }
/* 1120 */     return this.time;
/*      */   }
/*      */ 
/*      */   public void setTimeInMillis(long paramLong)
/*      */   {
/* 1133 */     if ((this.time == paramLong) && (this.isTimeSet) && (this.areFieldsSet) && (this.areAllFieldsSet) && ((this.zone instanceof ZoneInfo)) && (!((ZoneInfo)this.zone).isDirty()))
/*      */     {
/* 1135 */       return;
/*      */     }
/* 1137 */     this.time = paramLong;
/* 1138 */     this.isTimeSet = true;
/* 1139 */     this.areFieldsSet = false;
/* 1140 */     computeFields();
/* 1141 */     this.areAllFieldsSet = (this.areFieldsSet = 1);
/*      */   }
/*      */ 
/*      */   public int get(int paramInt)
/*      */   {
/* 1162 */     complete();
/* 1163 */     return internalGet(paramInt);
/*      */   }
/*      */ 
/*      */   protected final int internalGet(int paramInt)
/*      */   {
/* 1176 */     return this.fields[paramInt];
/*      */   }
/*      */ 
/*      */   final void internalSet(int paramInt1, int paramInt2)
/*      */   {
/* 1193 */     this.fields[paramInt1] = paramInt2;
/*      */   }
/*      */ 
/*      */   public void set(int paramInt1, int paramInt2)
/*      */   {
/* 1214 */     if ((this.areFieldsSet) && (!this.areAllFieldsSet)) {
/* 1215 */       computeFields();
/*      */     }
/* 1217 */     internalSet(paramInt1, paramInt2);
/* 1218 */     this.isTimeSet = false;
/* 1219 */     this.areFieldsSet = false;
/* 1220 */     this.isSet[paramInt1] = true;
/* 1221 */     this.stamp[paramInt1] = (this.nextStamp++);
/* 1222 */     if (this.nextStamp == 2147483647)
/* 1223 */       adjustStamp();
/*      */   }
/*      */ 
/*      */   public final void set(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1243 */     set(1, paramInt1);
/* 1244 */     set(2, paramInt2);
/* 1245 */     set(5, paramInt3);
/*      */   }
/*      */ 
/*      */   public final void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1267 */     set(1, paramInt1);
/* 1268 */     set(2, paramInt2);
/* 1269 */     set(5, paramInt3);
/* 1270 */     set(11, paramInt4);
/* 1271 */     set(12, paramInt5);
/*      */   }
/*      */ 
/*      */   public final void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1295 */     set(1, paramInt1);
/* 1296 */     set(2, paramInt2);
/* 1297 */     set(5, paramInt3);
/* 1298 */     set(11, paramInt4);
/* 1299 */     set(12, paramInt5);
/* 1300 */     set(13, paramInt6);
/*      */   }
/*      */ 
/*      */   public final void clear()
/*      */   {
/* 1319 */     for (int i = 0; i < this.fields.length; )
/*      */     {
/*      */       int tmp22_21 = 0; this.fields[i] = tmp22_21; this.stamp[i] = tmp22_21;
/* 1321 */       this.isSet[(i++)] = false;
/*      */     }
/* 1323 */     this.areAllFieldsSet = (this.areFieldsSet = 0);
/* 1324 */     this.isTimeSet = false;
/*      */   }
/*      */ 
/*      */   public final void clear(int paramInt)
/*      */   {
/* 1350 */     this.fields[paramInt] = 0;
/* 1351 */     this.stamp[paramInt] = 0;
/* 1352 */     this.isSet[paramInt] = false;
/*      */ 
/* 1354 */     this.areAllFieldsSet = (this.areFieldsSet = 0);
/* 1355 */     this.isTimeSet = false;
/*      */   }
/*      */ 
/*      */   public final boolean isSet(int paramInt)
/*      */   {
/* 1368 */     return this.stamp[paramInt] != 0;
/*      */   }
/*      */ 
/*      */   public String getDisplayName(int paramInt1, int paramInt2, Locale paramLocale)
/*      */   {
/* 1413 */     if (!checkDisplayNameParams(paramInt1, paramInt2, 0, 2, paramLocale, 645))
/*      */     {
/* 1415 */       return null;
/*      */     }
/*      */ 
/* 1418 */     DateFormatSymbols localDateFormatSymbols = DateFormatSymbols.getInstance(paramLocale);
/* 1419 */     String[] arrayOfString = getFieldStrings(paramInt1, paramInt2, localDateFormatSymbols);
/* 1420 */     if (arrayOfString != null) {
/* 1421 */       int i = get(paramInt1);
/* 1422 */       if (i < arrayOfString.length) {
/* 1423 */         return arrayOfString[i];
/*      */       }
/*      */     }
/* 1426 */     return null;
/*      */   }
/*      */ 
/*      */   public Map<String, Integer> getDisplayNames(int paramInt1, int paramInt2, Locale paramLocale)
/*      */   {
/* 1473 */     if (!checkDisplayNameParams(paramInt1, paramInt2, 0, 2, paramLocale, 645))
/*      */     {
/* 1475 */       return null;
/*      */     }
/*      */ 
/* 1479 */     if (paramInt2 == 0) {
/* 1480 */       Map localMap1 = getDisplayNamesImpl(paramInt1, 1, paramLocale);
/* 1481 */       if ((paramInt1 == 0) || (paramInt1 == 9)) {
/* 1482 */         return localMap1;
/*      */       }
/* 1484 */       Map localMap2 = getDisplayNamesImpl(paramInt1, 2, paramLocale);
/* 1485 */       if (localMap1 == null) {
/* 1486 */         return localMap2;
/*      */       }
/* 1488 */       if (localMap2 != null) {
/* 1489 */         localMap1.putAll(localMap2);
/*      */       }
/* 1491 */       return localMap1;
/*      */     }
/*      */ 
/* 1495 */     return getDisplayNamesImpl(paramInt1, paramInt2, paramLocale);
/*      */   }
/*      */ 
/*      */   private Map<String, Integer> getDisplayNamesImpl(int paramInt1, int paramInt2, Locale paramLocale) {
/* 1499 */     DateFormatSymbols localDateFormatSymbols = DateFormatSymbols.getInstance(paramLocale);
/* 1500 */     String[] arrayOfString = getFieldStrings(paramInt1, paramInt2, localDateFormatSymbols);
/* 1501 */     if (arrayOfString != null) {
/* 1502 */       HashMap localHashMap = new HashMap();
/* 1503 */       for (int i = 0; i < arrayOfString.length; i++)
/* 1504 */         if (arrayOfString[i].length() != 0)
/*      */         {
/* 1507 */           localHashMap.put(arrayOfString[i], Integer.valueOf(i));
/*      */         }
/* 1509 */       return localHashMap;
/*      */     }
/* 1511 */     return null;
/*      */   }
/*      */ 
/*      */   boolean checkDisplayNameParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Locale paramLocale, int paramInt5)
/*      */   {
/* 1516 */     if ((paramInt1 < 0) || (paramInt1 >= this.fields.length) || (paramInt2 < paramInt3) || (paramInt2 > paramInt4))
/*      */     {
/* 1518 */       throw new IllegalArgumentException();
/*      */     }
/* 1520 */     if (paramLocale == null) {
/* 1521 */       throw new NullPointerException();
/*      */     }
/* 1523 */     return isFieldSet(paramInt5, paramInt1);
/*      */   }
/*      */ 
/*      */   private String[] getFieldStrings(int paramInt1, int paramInt2, DateFormatSymbols paramDateFormatSymbols) {
/* 1527 */     String[] arrayOfString = null;
/* 1528 */     switch (paramInt1) {
/*      */     case 0:
/* 1530 */       arrayOfString = paramDateFormatSymbols.getEras();
/* 1531 */       break;
/*      */     case 2:
/* 1534 */       arrayOfString = paramInt2 == 2 ? paramDateFormatSymbols.getMonths() : paramDateFormatSymbols.getShortMonths();
/* 1535 */       break;
/*      */     case 7:
/* 1538 */       arrayOfString = paramInt2 == 2 ? paramDateFormatSymbols.getWeekdays() : paramDateFormatSymbols.getShortWeekdays();
/* 1539 */       break;
/*      */     case 9:
/* 1542 */       arrayOfString = paramDateFormatSymbols.getAmPmStrings();
/*      */     case 1:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/* 1545 */     case 8: } return arrayOfString;
/*      */   }
/*      */ 
/*      */   protected void complete()
/*      */   {
/* 1557 */     if (!this.isTimeSet)
/* 1558 */       updateTime();
/* 1559 */     if ((!this.areFieldsSet) || (!this.areAllFieldsSet)) {
/* 1560 */       computeFields();
/* 1561 */       this.areAllFieldsSet = (this.areFieldsSet = 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   final boolean isExternallySet(int paramInt)
/*      */   {
/* 1579 */     return this.stamp[paramInt] >= 2;
/*      */   }
/*      */ 
/*      */   final int getSetStateFields()
/*      */   {
/* 1589 */     int i = 0;
/* 1590 */     for (int j = 0; j < this.fields.length; j++) {
/* 1591 */       if (this.stamp[j] != 0) {
/* 1592 */         i |= 1 << j;
/*      */       }
/*      */     }
/* 1595 */     return i;
/*      */   }
/*      */ 
/*      */   final void setFieldsComputed(int paramInt)
/*      */   {
/*      */     int i;
/* 1612 */     if (paramInt == 131071) {
/* 1613 */       for (i = 0; i < this.fields.length; i++) {
/* 1614 */         this.stamp[i] = 1;
/* 1615 */         this.isSet[i] = true;
/*      */       }
/* 1617 */       this.areFieldsSet = (this.areAllFieldsSet = 1);
/*      */     } else {
/* 1619 */       for (i = 0; i < this.fields.length; i++) {
/* 1620 */         if ((paramInt & 0x1) == 1) {
/* 1621 */           this.stamp[i] = 1;
/* 1622 */           this.isSet[i] = true;
/*      */         }
/* 1624 */         else if ((this.areAllFieldsSet) && (this.isSet[i] == 0)) {
/* 1625 */           this.areAllFieldsSet = false;
/*      */         }
/*      */ 
/* 1628 */         paramInt >>>= 1;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final void setFieldsNormalized(int paramInt)
/*      */   {
/* 1649 */     if (paramInt != 131071) {
/* 1650 */       for (int i = 0; i < this.fields.length; i++) {
/* 1651 */         if ((paramInt & 0x1) == 0)
/*      */         {
/*      */           int tmp34_33 = 0; this.fields[i] = tmp34_33; this.stamp[i] = tmp34_33;
/* 1653 */           this.isSet[i] = false;
/*      */         }
/* 1655 */         paramInt >>= 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1661 */     this.areFieldsSet = true;
/* 1662 */     this.areAllFieldsSet = false;
/*      */   }
/*      */ 
/*      */   final boolean isPartiallyNormalized()
/*      */   {
/* 1670 */     return (this.areFieldsSet) && (!this.areAllFieldsSet);
/*      */   }
/*      */ 
/*      */   final boolean isFullyNormalized()
/*      */   {
/* 1678 */     return (this.areFieldsSet) && (this.areAllFieldsSet);
/*      */   }
/*      */ 
/*      */   final void setUnnormalized()
/*      */   {
/* 1685 */     this.areFieldsSet = (this.areAllFieldsSet = 0);
/*      */   }
/*      */ 
/*      */   static final boolean isFieldSet(int paramInt1, int paramInt2)
/*      */   {
/* 1693 */     return (paramInt1 & 1 << paramInt2) != 0;
/*      */   }
/*      */ 
/*      */   final int selectFields()
/*      */   {
/* 1724 */     int i = 2;
/*      */ 
/* 1726 */     if (this.stamp[0] != 0) {
/* 1727 */       i |= 1;
/*      */     }
/*      */ 
/* 1741 */     int j = this.stamp[7];
/* 1742 */     int k = this.stamp[2];
/* 1743 */     int m = this.stamp[5];
/* 1744 */     int n = aggregateStamp(this.stamp[4], j);
/* 1745 */     int i1 = aggregateStamp(this.stamp[8], j);
/* 1746 */     int i2 = this.stamp[6];
/* 1747 */     int i3 = aggregateStamp(this.stamp[3], j);
/*      */ 
/* 1749 */     int i4 = m;
/* 1750 */     if (n > i4) {
/* 1751 */       i4 = n;
/*      */     }
/* 1753 */     if (i1 > i4) {
/* 1754 */       i4 = i1;
/*      */     }
/* 1756 */     if (i2 > i4) {
/* 1757 */       i4 = i2;
/*      */     }
/* 1759 */     if (i3 > i4) {
/* 1760 */       i4 = i3;
/*      */     }
/*      */ 
/* 1767 */     if (i4 == 0) {
/* 1768 */       n = this.stamp[4];
/* 1769 */       i1 = Math.max(this.stamp[8], j);
/* 1770 */       i3 = this.stamp[3];
/* 1771 */       i4 = Math.max(Math.max(n, i1), i3);
/*      */ 
/* 1777 */       if (i4 == 0) {
/* 1778 */         i4 = m = k;
/*      */       }
/*      */     }
/*      */ 
/* 1782 */     if ((i4 == m) || ((i4 == n) && (this.stamp[4] >= this.stamp[3])) || ((i4 == i1) && (this.stamp[8] >= this.stamp[3])))
/*      */     {
/* 1785 */       i |= 4;
/* 1786 */       if (i4 == m) {
/* 1787 */         i |= 32;
/*      */       } else {
/* 1789 */         assert ((i4 == n) || (i4 == i1));
/* 1790 */         if (j != 0) {
/* 1791 */           i |= 128;
/*      */         }
/* 1793 */         if (n == i1)
/*      */         {
/* 1796 */           if (this.stamp[4] >= this.stamp[8])
/* 1797 */             i |= 16;
/*      */           else {
/* 1799 */             i |= 256;
/*      */           }
/*      */         }
/* 1802 */         else if (i4 == n) {
/* 1803 */           i |= 16;
/*      */         } else {
/* 1805 */           assert (i4 == i1);
/* 1806 */           if (this.stamp[8] != 0)
/* 1807 */             i |= 256;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1813 */       assert ((i4 == i2) || (i4 == i3) || (i4 == 0));
/*      */ 
/* 1815 */       if (i4 == i2) {
/* 1816 */         i |= 64;
/*      */       } else {
/* 1818 */         assert (i4 == i3);
/* 1819 */         if (j != 0) {
/* 1820 */           i |= 128;
/*      */         }
/* 1822 */         i |= 8;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1829 */     int i5 = this.stamp[11];
/* 1830 */     int i6 = aggregateStamp(this.stamp[10], this.stamp[9]);
/* 1831 */     i4 = i6 > i5 ? i6 : i5;
/*      */ 
/* 1834 */     if (i4 == 0) {
/* 1835 */       i4 = Math.max(this.stamp[10], this.stamp[9]);
/*      */     }
/*      */ 
/* 1839 */     if (i4 != 0) {
/* 1840 */       if (i4 == i5) {
/* 1841 */         i |= 2048;
/*      */       } else {
/* 1843 */         i |= 1024;
/* 1844 */         if (this.stamp[9] != 0) {
/* 1845 */           i |= 512;
/*      */         }
/*      */       }
/*      */     }
/* 1849 */     if (this.stamp[12] != 0) {
/* 1850 */       i |= 4096;
/*      */     }
/* 1852 */     if (this.stamp[13] != 0) {
/* 1853 */       i |= 8192;
/*      */     }
/* 1855 */     if (this.stamp[14] != 0) {
/* 1856 */       i |= 16384;
/*      */     }
/* 1858 */     if (this.stamp[15] >= 2) {
/* 1859 */       i |= 32768;
/*      */     }
/* 1861 */     if (this.stamp[16] >= 2) {
/* 1862 */       i |= 65536;
/*      */     }
/*      */ 
/* 1865 */     return i;
/*      */   }
/*      */ 
/*      */   private static final int aggregateStamp(int paramInt1, int paramInt2)
/*      */   {
/* 1875 */     if ((paramInt1 == 0) || (paramInt2 == 0)) {
/* 1876 */       return 0;
/*      */     }
/* 1878 */     return paramInt1 > paramInt2 ? paramInt1 : paramInt2;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1904 */     if (this == paramObject)
/* 1905 */       return true;
/*      */     try {
/* 1907 */       Calendar localCalendar = (Calendar)paramObject;
/* 1908 */       return (compareTo(getMillisOf(localCalendar)) == 0) && (this.lenient == localCalendar.lenient) && (this.firstDayOfWeek == localCalendar.firstDayOfWeek) && (this.minimalDaysInFirstWeek == localCalendar.minimalDaysInFirstWeek) && (this.zone.equals(localCalendar.zone));
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1918 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1929 */     int i = (this.lenient ? 1 : 0) | this.firstDayOfWeek << 1 | this.minimalDaysInFirstWeek << 4 | this.zone.hashCode() << 7;
/*      */ 
/* 1933 */     long l = getMillisOf(this);
/* 1934 */     return (int)l ^ (int)(l >> 32) ^ i;
/*      */   }
/*      */ 
/*      */   public boolean before(Object paramObject)
/*      */   {
/* 1954 */     return ((paramObject instanceof Calendar)) && (compareTo((Calendar)paramObject) < 0);
/*      */   }
/*      */ 
/*      */   public boolean after(Object paramObject)
/*      */   {
/* 1975 */     return ((paramObject instanceof Calendar)) && (compareTo((Calendar)paramObject) > 0);
/*      */   }
/*      */ 
/*      */   public int compareTo(Calendar paramCalendar)
/*      */   {
/* 1999 */     return compareTo(getMillisOf(paramCalendar));
/*      */   }
/*      */ 
/*      */   public abstract void add(int paramInt1, int paramInt2);
/*      */ 
/*      */   public abstract void roll(int paramInt, boolean paramBoolean);
/*      */ 
/*      */   public void roll(int paramInt1, int paramInt2)
/*      */   {
/* 2058 */     while (paramInt2 > 0) {
/* 2059 */       roll(paramInt1, true);
/* 2060 */       paramInt2--;
/*      */     }
/* 2062 */     while (paramInt2 < 0) {
/* 2063 */       roll(paramInt1, false);
/* 2064 */       paramInt2++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setTimeZone(TimeZone paramTimeZone)
/*      */   {
/* 2075 */     this.zone = paramTimeZone;
/* 2076 */     this.sharedZone = false;
/*      */ 
/* 2086 */     this.areAllFieldsSet = (this.areFieldsSet = 0);
/*      */   }
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/* 2098 */     if (this.sharedZone) {
/* 2099 */       this.zone = ((TimeZone)this.zone.clone());
/* 2100 */       this.sharedZone = false;
/*      */     }
/* 2102 */     return this.zone;
/*      */   }
/*      */ 
/*      */   TimeZone getZone()
/*      */   {
/* 2109 */     return this.zone;
/*      */   }
/*      */ 
/*      */   void setZoneShared(boolean paramBoolean)
/*      */   {
/* 2116 */     this.sharedZone = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setLenient(boolean paramBoolean)
/*      */   {
/* 2133 */     this.lenient = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isLenient()
/*      */   {
/* 2145 */     return this.lenient;
/*      */   }
/*      */ 
/*      */   public void setFirstDayOfWeek(int paramInt)
/*      */   {
/* 2158 */     if (this.firstDayOfWeek == paramInt) {
/* 2159 */       return;
/*      */     }
/* 2161 */     this.firstDayOfWeek = paramInt;
/* 2162 */     invalidateWeekFields();
/*      */   }
/*      */ 
/*      */   public int getFirstDayOfWeek()
/*      */   {
/* 2175 */     return this.firstDayOfWeek;
/*      */   }
/*      */ 
/*      */   public void setMinimalDaysInFirstWeek(int paramInt)
/*      */   {
/* 2190 */     if (this.minimalDaysInFirstWeek == paramInt) {
/* 2191 */       return;
/*      */     }
/* 2193 */     this.minimalDaysInFirstWeek = paramInt;
/* 2194 */     invalidateWeekFields();
/*      */   }
/*      */ 
/*      */   public int getMinimalDaysInFirstWeek()
/*      */   {
/* 2209 */     return this.minimalDaysInFirstWeek;
/*      */   }
/*      */ 
/*      */   public boolean isWeekDateSupported()
/*      */   {
/* 2225 */     return false;
/*      */   }
/*      */ 
/*      */   public int getWeekYear()
/*      */   {
/* 2247 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setWeekDate(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2283 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getWeeksInWeekYear()
/*      */   {
/* 2304 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public abstract int getMinimum(int paramInt);
/*      */ 
/*      */   public abstract int getMaximum(int paramInt);
/*      */ 
/*      */   public abstract int getGreatestMinimum(int paramInt);
/*      */ 
/*      */   public abstract int getLeastMaximum(int paramInt);
/*      */ 
/*      */   public int getActualMinimum(int paramInt)
/*      */   {
/* 2402 */     int i = getGreatestMinimum(paramInt);
/* 2403 */     int j = getMinimum(paramInt);
/*      */ 
/* 2406 */     if (i == j) {
/* 2407 */       return i;
/*      */     }
/*      */ 
/* 2412 */     Calendar localCalendar = (Calendar)clone();
/* 2413 */     localCalendar.setLenient(true);
/*      */ 
/* 2418 */     int k = i;
/*      */     do
/*      */     {
/* 2421 */       localCalendar.set(paramInt, i);
/* 2422 */       if (localCalendar.get(paramInt) != i) {
/*      */         break;
/*      */       }
/* 2425 */       k = i;
/* 2426 */       i--;
/*      */     }
/* 2428 */     while (i >= j);
/*      */ 
/* 2430 */     return k;
/*      */   }
/*      */ 
/*      */   public int getActualMaximum(int paramInt)
/*      */   {
/* 2456 */     int i = getLeastMaximum(paramInt);
/* 2457 */     int j = getMaximum(paramInt);
/*      */ 
/* 2460 */     if (i == j) {
/* 2461 */       return i;
/*      */     }
/*      */ 
/* 2466 */     Calendar localCalendar = (Calendar)clone();
/* 2467 */     localCalendar.setLenient(true);
/*      */ 
/* 2471 */     if ((paramInt == 3) || (paramInt == 4)) {
/* 2472 */       localCalendar.set(7, this.firstDayOfWeek);
/*      */     }
/*      */ 
/* 2477 */     int k = i;
/*      */     do
/*      */     {
/* 2480 */       localCalendar.set(paramInt, i);
/* 2481 */       if (localCalendar.get(paramInt) != i) {
/*      */         break;
/*      */       }
/* 2484 */       k = i;
/* 2485 */       i++;
/*      */     }
/* 2487 */     while (i <= j);
/*      */ 
/* 2489 */     return k;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 2500 */       Calendar localCalendar = (Calendar)super.clone();
/*      */ 
/* 2502 */       localCalendar.fields = new int[17];
/* 2503 */       localCalendar.isSet = new boolean[17];
/* 2504 */       localCalendar.stamp = new int[17];
/* 2505 */       for (int i = 0; i < 17; i++) {
/* 2506 */         localCalendar.fields[i] = this.fields[i];
/* 2507 */         localCalendar.stamp[i] = this.stamp[i];
/* 2508 */         localCalendar.isSet[i] = this.isSet[i];
/*      */       }
/* 2510 */       localCalendar.zone = ((TimeZone)this.zone.clone());
/* 2511 */       return localCalendar;
/*      */     }
/*      */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/* 2515 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   static final String getFieldName(int paramInt)
/*      */   {
/* 2535 */     return FIELD_NAME[paramInt];
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2551 */     StringBuilder localStringBuilder = new StringBuilder(800);
/* 2552 */     localStringBuilder.append(getClass().getName()).append('[');
/* 2553 */     appendValue(localStringBuilder, "time", this.isTimeSet, this.time);
/* 2554 */     localStringBuilder.append(",areFieldsSet=").append(this.areFieldsSet);
/* 2555 */     localStringBuilder.append(",areAllFieldsSet=").append(this.areAllFieldsSet);
/* 2556 */     localStringBuilder.append(",lenient=").append(this.lenient);
/* 2557 */     localStringBuilder.append(",zone=").append(this.zone);
/* 2558 */     appendValue(localStringBuilder, ",firstDayOfWeek", true, this.firstDayOfWeek);
/* 2559 */     appendValue(localStringBuilder, ",minimalDaysInFirstWeek", true, this.minimalDaysInFirstWeek);
/* 2560 */     for (int i = 0; i < 17; i++) {
/* 2561 */       localStringBuilder.append(',');
/* 2562 */       appendValue(localStringBuilder, FIELD_NAME[i], isSet(i), this.fields[i]);
/*      */     }
/* 2564 */     localStringBuilder.append(']');
/* 2565 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static final void appendValue(StringBuilder paramStringBuilder, String paramString, boolean paramBoolean, long paramLong)
/*      */   {
/* 2571 */     paramStringBuilder.append(paramString).append('=');
/* 2572 */     if (paramBoolean)
/* 2573 */       paramStringBuilder.append(paramLong);
/*      */     else
/* 2575 */       paramStringBuilder.append('?');
/*      */   }
/*      */ 
/*      */   private void setWeekCountData(Locale paramLocale)
/*      */   {
/* 2588 */     int[] arrayOfInt = (int[])cachedLocaleData.get(paramLocale);
/* 2589 */     if (arrayOfInt == null) {
/* 2590 */       ResourceBundle localResourceBundle = LocaleData.getCalendarData(paramLocale);
/* 2591 */       arrayOfInt = new int[2];
/* 2592 */       arrayOfInt[0] = Integer.parseInt(localResourceBundle.getString("firstDayOfWeek"));
/* 2593 */       arrayOfInt[1] = Integer.parseInt(localResourceBundle.getString("minimalDaysInFirstWeek"));
/* 2594 */       cachedLocaleData.putIfAbsent(paramLocale, arrayOfInt);
/*      */     }
/* 2596 */     this.firstDayOfWeek = arrayOfInt[0];
/* 2597 */     this.minimalDaysInFirstWeek = arrayOfInt[1];
/*      */   }
/*      */ 
/*      */   private void updateTime()
/*      */   {
/* 2606 */     computeTime();
/*      */ 
/* 2609 */     this.isTimeSet = true;
/*      */   }
/*      */ 
/*      */   private int compareTo(long paramLong) {
/* 2613 */     long l = getMillisOf(this);
/* 2614 */     return l == paramLong ? 0 : l > paramLong ? 1 : -1;
/*      */   }
/*      */ 
/*      */   private static final long getMillisOf(Calendar paramCalendar) {
/* 2618 */     if (paramCalendar.isTimeSet) {
/* 2619 */       return paramCalendar.time;
/*      */     }
/* 2621 */     Calendar localCalendar = (Calendar)paramCalendar.clone();
/* 2622 */     localCalendar.setLenient(true);
/* 2623 */     return localCalendar.getTimeInMillis();
/*      */   }
/*      */ 
/*      */   private final void adjustStamp()
/*      */   {
/* 2631 */     int i = 2;
/* 2632 */     int j = 2;
/*      */     while (true)
/*      */     {
/* 2635 */       int k = 2147483647;
/* 2636 */       for (int m = 0; m < this.stamp.length; m++) {
/* 2637 */         int n = this.stamp[m];
/* 2638 */         if ((n >= j) && (k > n)) {
/* 2639 */           k = n;
/*      */         }
/* 2641 */         if (i < n) {
/* 2642 */           i = n;
/*      */         }
/*      */       }
/* 2645 */       if ((i != k) && (k == 2147483647)) {
/*      */         break;
/*      */       }
/* 2648 */       for (m = 0; m < this.stamp.length; m++) {
/* 2649 */         if (this.stamp[m] == k) {
/* 2650 */           this.stamp[m] = j;
/*      */         }
/*      */       }
/* 2653 */       j++;
/* 2654 */       if (k == i) {
/*      */         break;
/*      */       }
/*      */     }
/* 2658 */     this.nextStamp = j;
/*      */   }
/*      */ 
/*      */   private void invalidateWeekFields()
/*      */   {
/* 2667 */     if ((this.stamp[4] != 1) && (this.stamp[3] != 1))
/*      */     {
/* 2669 */       return;
/*      */     }
/*      */ 
/* 2675 */     Calendar localCalendar = (Calendar)clone();
/* 2676 */     localCalendar.setLenient(true);
/* 2677 */     localCalendar.clear(4);
/* 2678 */     localCalendar.clear(3);
/*      */     int i;
/* 2680 */     if (this.stamp[4] == 1) {
/* 2681 */       i = localCalendar.get(4);
/* 2682 */       if (this.fields[4] != i) {
/* 2683 */         this.fields[4] = i;
/*      */       }
/*      */     }
/*      */ 
/* 2687 */     if (this.stamp[3] == 1) {
/* 2688 */       i = localCalendar.get(3);
/* 2689 */       if (this.fields[3] != i)
/* 2690 */         this.fields[3] = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 2713 */     if (!this.isTimeSet) {
/*      */       try {
/* 2715 */         updateTime();
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2723 */     TimeZone localTimeZone = null;
/* 2724 */     if ((this.zone instanceof ZoneInfo)) {
/* 2725 */       SimpleTimeZone localSimpleTimeZone = ((ZoneInfo)this.zone).getLastRuleInstance();
/* 2726 */       if (localSimpleTimeZone == null) {
/* 2727 */         localSimpleTimeZone = new SimpleTimeZone(this.zone.getRawOffset(), this.zone.getID());
/*      */       }
/* 2729 */       localTimeZone = this.zone;
/* 2730 */       this.zone = localSimpleTimeZone;
/*      */     }
/*      */ 
/* 2734 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 2739 */     paramObjectOutputStream.writeObject(localTimeZone);
/* 2740 */     if (localTimeZone != null)
/* 2741 */       this.zone = localTimeZone;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2763 */     final ObjectInputStream localObjectInputStream = paramObjectInputStream;
/* 2764 */     localObjectInputStream.defaultReadObject();
/*      */ 
/* 2766 */     this.stamp = new int[17];
/*      */ 
/* 2771 */     if (this.serialVersionOnStream >= 2)
/*      */     {
/* 2773 */       this.isTimeSet = true;
/* 2774 */       if (this.fields == null) this.fields = new int[17];
/* 2775 */       if (this.isSet == null) this.isSet = new boolean[17];
/*      */     }
/* 2777 */     else if (this.serialVersionOnStream >= 0)
/*      */     {
/* 2779 */       for (int i = 0; i < 17; i++) {
/* 2780 */         this.stamp[i] = (this.isSet[i] != 0 ? 1 : 0);
/*      */       }
/* 2783 */     }this.serialVersionOnStream = 1;
/*      */ 
/* 2786 */     ZoneInfo localZoneInfo = null;
/*      */     Object localObject;
/*      */     try { localZoneInfo = (ZoneInfo)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public ZoneInfo run() throws Exception {
/* 2791 */           return (ZoneInfo)localObjectInputStream.readObject();
/*      */         }
/*      */       }
/*      */       , CalendarAccessControlContext.INSTANCE);
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/* 2796 */       localObject = localPrivilegedActionException.getException();
/* 2797 */       if (!(localObject instanceof OptionalDataException)) {
/* 2798 */         if ((localObject instanceof RuntimeException))
/* 2799 */           throw ((RuntimeException)localObject);
/* 2800 */         if ((localObject instanceof IOException))
/* 2801 */           throw ((IOException)localObject);
/* 2802 */         if ((localObject instanceof ClassNotFoundException)) {
/* 2803 */           throw ((ClassNotFoundException)localObject);
/*      */         }
/* 2805 */         throw new RuntimeException((Throwable)localObject);
/*      */       }
/*      */     }
/* 2808 */     if (localZoneInfo != null) {
/* 2809 */       this.zone = localZoneInfo;
/*      */     }
/*      */ 
/* 2816 */     if ((this.zone instanceof SimpleTimeZone)) {
/* 2817 */       String str = this.zone.getID();
/* 2818 */       localObject = TimeZone.getTimeZone(str);
/* 2819 */       if ((localObject != null) && (((TimeZone)localObject).hasSameRules(this.zone)) && (((TimeZone)localObject).getID().equals(str)))
/* 2820 */         this.zone = ((TimeZone)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CalendarAccessControlContext
/*      */   {
/* 2751 */     private static final AccessControlContext INSTANCE = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, localPermissionCollection) });
/*      */ 
/*      */     static
/*      */     {
/* 2748 */       RuntimePermission localRuntimePermission = new RuntimePermission("accessClassInPackage.sun.util.calendar");
/* 2749 */       PermissionCollection localPermissionCollection = localRuntimePermission.newPermissionCollection();
/* 2750 */       localPermissionCollection.add(localRuntimePermission);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Calendar
 * JD-Core Version:    0.6.2
 */