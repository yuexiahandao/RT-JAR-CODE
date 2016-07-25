/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import sun.util.calendar.BaseCalendar;
/*      */ import sun.util.calendar.BaseCalendar.Date;
/*      */ import sun.util.calendar.CalendarDate;
/*      */ import sun.util.calendar.CalendarSystem;
/*      */ import sun.util.calendar.CalendarUtils;
/*      */ import sun.util.calendar.Era;
/*      */ import sun.util.calendar.Gregorian;
/*      */ import sun.util.calendar.JulianCalendar;
/*      */ import sun.util.calendar.ZoneInfo;
/*      */ 
/*      */ public class GregorianCalendar extends Calendar
/*      */ {
/*      */   public static final int BC = 0;
/*      */   static final int BCE = 0;
/*      */   public static final int AD = 1;
/*      */   static final int CE = 1;
/*      */   private static final int EPOCH_OFFSET = 719163;
/*      */   private static final int EPOCH_YEAR = 1970;
/*  397 */   static final int[] MONTH_LENGTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*      */ 
/*  399 */   static final int[] LEAP_MONTH_LENGTH = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*      */   private static final int ONE_SECOND = 1000;
/*      */   private static final int ONE_MINUTE = 60000;
/*      */   private static final int ONE_HOUR = 3600000;
/*      */   private static final long ONE_DAY = 86400000L;
/*      */   private static final long ONE_WEEK = 604800000L;
/*  436 */   static final int[] MIN_VALUES = { 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, -46800000, 0 };
/*      */ 
/*  455 */   static final int[] LEAST_MAX_VALUES = { 1, 292269054, 11, 52, 4, 28, 365, 7, 4, 1, 11, 23, 59, 59, 999, 50400000, 1200000 };
/*      */ 
/*  474 */   static final int[] MAX_VALUES = { 1, 292278994, 11, 53, 6, 31, 366, 7, 6, 1, 11, 23, 59, 59, 999, 50400000, 7200000 };
/*      */   static final long serialVersionUID = -8125100834729963327L;
/*  498 */   private static final Gregorian gcal = CalendarSystem.getGregorianCalendar();
/*      */   private static JulianCalendar jcal;
/*      */   private static Era[] jeras;
/*      */   static final long DEFAULT_GREGORIAN_CUTOVER = -12219292800000L;
/*  523 */   private long gregorianCutover = -12219292800000L;
/*      */ 
/*  528 */   private transient long gregorianCutoverDate = 577736L;
/*      */ 
/*  535 */   private transient int gregorianCutoverYear = 1582;
/*      */ 
/*  541 */   private transient int gregorianCutoverYearJulian = 1582;
/*      */   private transient BaseCalendar.Date gdate;
/*      */   private transient BaseCalendar.Date cdate;
/*      */   private transient BaseCalendar calsys;
/*      */   private transient int[] zoneOffsets;
/*      */   private transient int[] originalFields;
/* 2222 */   private transient long cachedFixedDate = -9223372036854775808L;
/*      */ 
/*      */   public GregorianCalendar()
/*      */   {
/*  586 */     this(TimeZone.getDefaultRef(), Locale.getDefault(Locale.Category.FORMAT));
/*  587 */     setZoneShared(true);
/*      */   }
/*      */ 
/*      */   public GregorianCalendar(TimeZone paramTimeZone)
/*      */   {
/*  597 */     this(paramTimeZone, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public GregorianCalendar(Locale paramLocale)
/*      */   {
/*  607 */     this(TimeZone.getDefaultRef(), paramLocale);
/*  608 */     setZoneShared(true);
/*      */   }
/*      */ 
/*      */   public GregorianCalendar(TimeZone paramTimeZone, Locale paramLocale)
/*      */   {
/*  619 */     super(paramTimeZone, paramLocale);
/*  620 */     this.gdate = gcal.newCalendarDate(paramTimeZone);
/*  621 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */ 
/*      */   public GregorianCalendar(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  634 */     this(paramInt1, paramInt2, paramInt3, 0, 0, 0, 0);
/*      */   }
/*      */ 
/*      */   public GregorianCalendar(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  652 */     this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 0, 0);
/*      */   }
/*      */ 
/*      */   public GregorianCalendar(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  672 */     this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 0);
/*      */   }
/*      */ 
/*      */   GregorianCalendar(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*      */   {
/*  694 */     this.gdate = gcal.newCalendarDate(getZone());
/*  695 */     set(1, paramInt1);
/*  696 */     set(2, paramInt2);
/*  697 */     set(5, paramInt3);
/*      */ 
/*  701 */     if ((paramInt4 >= 12) && (paramInt4 <= 23))
/*      */     {
/*  705 */       internalSet(9, 1);
/*  706 */       internalSet(10, paramInt4 - 12);
/*      */     }
/*      */     else
/*      */     {
/*  710 */       internalSet(10, paramInt4);
/*      */     }
/*      */ 
/*  713 */     setFieldsComputed(1536);
/*      */ 
/*  715 */     set(11, paramInt4);
/*  716 */     set(12, paramInt5);
/*  717 */     set(13, paramInt6);
/*      */ 
/*  720 */     internalSet(14, paramInt7);
/*      */   }
/*      */ 
/*      */   public void setGregorianChange(Date paramDate)
/*      */   {
/*  739 */     long l = paramDate.getTime();
/*  740 */     if (l == this.gregorianCutover) {
/*  741 */       return;
/*      */     }
/*      */ 
/*  745 */     complete();
/*  746 */     setGregorianChange(l);
/*      */   }
/*      */ 
/*      */   private void setGregorianChange(long paramLong) {
/*  750 */     this.gregorianCutover = paramLong;
/*  751 */     this.gregorianCutoverDate = (CalendarUtils.floorDivide(paramLong, 86400000L) + 719163L);
/*      */ 
/*  759 */     if (paramLong == 9223372036854775807L) {
/*  760 */       this.gregorianCutoverDate += 1L;
/*      */     }
/*      */ 
/*  763 */     BaseCalendar.Date localDate = getGregorianCutoverDate();
/*      */ 
/*  766 */     this.gregorianCutoverYear = localDate.getYear();
/*      */ 
/*  768 */     BaseCalendar localBaseCalendar = getJulianCalendarSystem();
/*  769 */     localDate = (BaseCalendar.Date)localBaseCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
/*  770 */     localBaseCalendar.getCalendarDateFromFixedDate(localDate, this.gregorianCutoverDate - 1L);
/*  771 */     this.gregorianCutoverYearJulian = localDate.getNormalizedYear();
/*      */ 
/*  773 */     if (this.time < this.gregorianCutover)
/*      */     {
/*  776 */       setUnnormalized();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Date getGregorianChange()
/*      */   {
/*  789 */     return new Date(this.gregorianCutover);
/*      */   }
/*      */ 
/*      */   public boolean isLeapYear(int paramInt)
/*      */   {
/*  802 */     if ((paramInt & 0x3) != 0) {
/*  803 */       return false;
/*      */     }
/*      */ 
/*  806 */     if (paramInt > this.gregorianCutoverYear) {
/*  807 */       return (paramInt % 100 != 0) || (paramInt % 400 == 0);
/*      */     }
/*  809 */     if (paramInt < this.gregorianCutoverYearJulian)
/*  810 */       return true;
/*      */     int i;
/*  815 */     if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian) {
/*  816 */       BaseCalendar.Date localDate = getCalendarDate(this.gregorianCutoverDate);
/*  817 */       i = localDate.getMonth() < 3 ? 1 : 0;
/*      */     } else {
/*  819 */       i = paramInt == this.gregorianCutoverYear ? 1 : 0;
/*      */     }
/*  821 */     return (paramInt % 100 != 0) || (paramInt % 400 == 0);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  839 */     return ((paramObject instanceof GregorianCalendar)) && (super.equals(paramObject)) && (this.gregorianCutover == ((GregorianCalendar)paramObject).gregorianCutover);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  848 */     return super.hashCode() ^ (int)this.gregorianCutoverDate;
/*      */   }
/*      */ 
/*      */   public void add(int paramInt1, int paramInt2)
/*      */   {
/*  882 */     if (paramInt2 == 0) {
/*  883 */       return;
/*      */     }
/*      */ 
/*  886 */     if ((paramInt1 < 0) || (paramInt1 >= 15)) {
/*  887 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*  891 */     complete();
/*      */     int i;
/*  893 */     if (paramInt1 == 1) {
/*  894 */       i = internalGet(1);
/*  895 */       if (internalGetEra() == 1) {
/*  896 */         i += paramInt2;
/*  897 */         if (i > 0) {
/*  898 */           set(1, i);
/*      */         } else {
/*  900 */           set(1, 1 - i);
/*      */ 
/*  902 */           set(0, 0);
/*      */         }
/*      */       }
/*      */       else {
/*  906 */         i -= paramInt2;
/*  907 */         if (i > 0) {
/*  908 */           set(1, i);
/*      */         } else {
/*  910 */           set(1, 1 - i);
/*      */ 
/*  912 */           set(0, 1);
/*      */         }
/*      */       }
/*  915 */       pinDayOfMonth();
/*  916 */     } else if (paramInt1 == 2) {
/*  917 */       i = internalGet(2) + paramInt2;
/*  918 */       int j = internalGet(1);
/*      */       int k;
/*  921 */       if (i >= 0)
/*  922 */         k = i / 12;
/*      */       else {
/*  924 */         k = (i + 1) / 12 - 1;
/*      */       }
/*  926 */       if (k != 0) {
/*  927 */         if (internalGetEra() == 1) {
/*  928 */           j += k;
/*  929 */           if (j > 0) {
/*  930 */             set(1, j);
/*      */           } else {
/*  932 */             set(1, 1 - j);
/*      */ 
/*  934 */             set(0, 0);
/*      */           }
/*      */         }
/*      */         else {
/*  938 */           j -= k;
/*  939 */           if (j > 0) {
/*  940 */             set(1, j);
/*      */           } else {
/*  942 */             set(1, 1 - j);
/*      */ 
/*  944 */             set(0, 1);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  949 */       if (i >= 0) {
/*  950 */         set(2, i % 12);
/*      */       }
/*      */       else {
/*  953 */         i %= 12;
/*  954 */         if (i < 0) {
/*  955 */           i += 12;
/*      */         }
/*  957 */         set(2, 0 + i);
/*      */       }
/*  959 */       pinDayOfMonth();
/*  960 */     } else if (paramInt1 == 0) {
/*  961 */       i = internalGet(0) + paramInt2;
/*  962 */       if (i < 0) {
/*  963 */         i = 0;
/*      */       }
/*  965 */       if (i > 1) {
/*  966 */         i = 1;
/*      */       }
/*  968 */       set(0, i);
/*      */     } else {
/*  970 */       long l1 = paramInt2;
/*  971 */       long l2 = 0L;
/*  972 */       switch (paramInt1)
/*      */       {
/*      */       case 10:
/*      */       case 11:
/*  977 */         l1 *= 3600000L;
/*  978 */         break;
/*      */       case 12:
/*  981 */         l1 *= 60000L;
/*  982 */         break;
/*      */       case 13:
/*  985 */         l1 *= 1000L;
/*  986 */         break;
/*      */       case 14:
/*  989 */         break;
/*      */       case 3:
/*      */       case 4:
/*      */       case 8:
/*  997 */         l1 *= 7L;
/*  998 */         break;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/* 1003 */         break;
/*      */       case 9:
/* 1008 */         l1 = paramInt2 / 2;
/* 1009 */         l2 = 12 * (paramInt2 % 2);
/*      */       }
/*      */ 
/* 1015 */       if (paramInt1 >= 10) {
/* 1016 */         setTimeInMillis(this.time + l1);
/* 1017 */         return;
/*      */       }
/*      */ 
/* 1026 */       long l3 = getCurrentFixedDate();
/* 1027 */       l2 += internalGet(11);
/* 1028 */       l2 *= 60L;
/* 1029 */       l2 += internalGet(12);
/* 1030 */       l2 *= 60L;
/* 1031 */       l2 += internalGet(13);
/* 1032 */       l2 *= 1000L;
/* 1033 */       l2 += internalGet(14);
/* 1034 */       if (l2 >= 86400000L) {
/* 1035 */         l3 += 1L;
/* 1036 */         l2 -= 86400000L;
/* 1037 */       } else if (l2 < 0L) {
/* 1038 */         l3 -= 1L;
/* 1039 */         l2 += 86400000L;
/*      */       }
/*      */ 
/* 1042 */       l3 += l1;
/* 1043 */       int m = internalGet(15) + internalGet(16);
/* 1044 */       setTimeInMillis((l3 - 719163L) * 86400000L + l2 - m);
/* 1045 */       m -= internalGet(15) + internalGet(16);
/*      */ 
/* 1047 */       if (m != 0) {
/* 1048 */         setTimeInMillis(this.time + m);
/* 1049 */         long l4 = getCurrentFixedDate();
/*      */ 
/* 1052 */         if (l4 != l3)
/* 1053 */           setTimeInMillis(this.time - m);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void roll(int paramInt, boolean paramBoolean)
/*      */   {
/* 1078 */     roll(paramInt, paramBoolean ? 1 : -1);
/*      */   }
/*      */ 
/*      */   public void roll(int paramInt1, int paramInt2)
/*      */   {
/* 1128 */     if (paramInt2 == 0) {
/* 1129 */       return;
/*      */     }
/*      */ 
/* 1132 */     if ((paramInt1 < 0) || (paramInt1 >= 15)) {
/* 1133 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/* 1137 */     complete();
/*      */ 
/* 1139 */     int i = getMinimum(paramInt1);
/* 1140 */     int j = getMaximum(paramInt1);
/*      */     int k;
/*      */     int i1;
/*      */     int i2;
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     long l4;
/*      */     int i11;
/*      */     long l1;
/*      */     int i6;
/* 1142 */     switch (paramInt1)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*      */     case 9:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/* 1153 */       break;
/*      */     case 10:
/*      */     case 11:
/* 1158 */       k = j + 1;
/* 1159 */       i1 = internalGet(paramInt1);
/* 1160 */       i2 = (i1 + paramInt2) % k;
/* 1161 */       if (i2 < 0) {
/* 1162 */         i2 += k;
/*      */       }
/* 1164 */       this.time += 3600000 * (i2 - i1);
/*      */ 
/* 1170 */       CalendarDate localCalendarDate = this.calsys.getCalendarDate(this.time, getZone());
/* 1171 */       if (internalGet(5) != localCalendarDate.getDayOfMonth()) {
/* 1172 */         localCalendarDate.setDate(internalGet(1), internalGet(2) + 1, internalGet(5));
/*      */ 
/* 1175 */         if (paramInt1 == 10) {
/* 1176 */           assert (internalGet(9) == 1);
/* 1177 */           localCalendarDate.addHours(12);
/*      */         }
/* 1179 */         this.time = this.calsys.getTime(localCalendarDate);
/*      */       }
/* 1181 */       int i5 = localCalendarDate.getHours();
/* 1182 */       internalSet(paramInt1, i5 % k);
/* 1183 */       if (paramInt1 == 10) {
/* 1184 */         internalSet(11, i5);
/*      */       } else {
/* 1186 */         internalSet(9, i5 / 12);
/* 1187 */         internalSet(10, i5 % 12);
/*      */       }
/*      */ 
/* 1191 */       int i8 = localCalendarDate.getZoneOffset();
/* 1192 */       int i10 = localCalendarDate.getDaylightSaving();
/* 1193 */       internalSet(15, i8 - i10);
/* 1194 */       internalSet(16, i10);
/* 1195 */       return;
/*      */     case 2:
/* 1204 */       if (!isCutoverYear(this.cdate.getNormalizedYear())) {
/* 1205 */         k = (internalGet(2) + paramInt2) % 12;
/* 1206 */         if (k < 0) {
/* 1207 */           k += 12;
/*      */         }
/* 1209 */         set(2, k);
/*      */ 
/* 1214 */         i1 = monthLength(k);
/* 1215 */         if (internalGet(5) > i1) {
/* 1216 */           set(5, i1);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1221 */         k = getActualMaximum(2) + 1;
/* 1222 */         i1 = (internalGet(2) + paramInt2) % k;
/* 1223 */         if (i1 < 0) {
/* 1224 */           i1 += k;
/*      */         }
/* 1226 */         set(2, i1);
/* 1227 */         i2 = getActualMaximum(5);
/* 1228 */         if (internalGet(5) > i2) {
/* 1229 */           set(5, i2);
/*      */         }
/*      */       }
/* 1232 */       return;
/*      */     case 3:
/* 1237 */       k = this.cdate.getNormalizedYear();
/* 1238 */       j = getActualMaximum(3);
/* 1239 */       set(7, internalGet(7));
/* 1240 */       i1 = internalGet(3);
/* 1241 */       i2 = i1 + paramInt2;
/*      */       long l6;
/* 1242 */       if (!isCutoverYear(k))
/*      */       {
/* 1245 */         if ((i2 > i) && (i2 < j)) {
/* 1246 */           set(3, i2);
/* 1247 */           return;
/*      */         }
/* 1249 */         l6 = getCurrentFixedDate();
/*      */ 
/* 1251 */         long l8 = l6 - 7 * (i1 - i);
/* 1252 */         if (this.calsys.getYearFromFixedDate(l8) != k) {
/* 1253 */           i++;
/*      */         }
/*      */ 
/* 1257 */         l6 += 7 * (j - internalGet(3));
/* 1258 */         if (this.calsys.getYearFromFixedDate(l6) != k) {
/* 1259 */           j--;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1265 */         l6 = getCurrentFixedDate();
/*      */ 
/* 1267 */         if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian)
/* 1268 */           localObject1 = getCutoverCalendarSystem();
/* 1269 */         else if (k == this.gregorianCutoverYear)
/* 1270 */           localObject1 = gcal;
/*      */         else {
/* 1272 */           localObject1 = getJulianCalendarSystem();
/*      */         }
/* 1274 */         long l9 = l6 - 7 * (i1 - i);
/*      */ 
/* 1276 */         if (((BaseCalendar)localObject1).getYearFromFixedDate(l9) != k) {
/* 1277 */           i++;
/*      */         }
/*      */ 
/* 1281 */         l6 += 7 * (j - i1);
/* 1282 */         localObject1 = l6 >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem();
/* 1283 */         if (((BaseCalendar)localObject1).getYearFromFixedDate(l6) != k) {
/* 1284 */           j--;
/* 1288 */         }
/*      */ i2 = getRolledValue(i1, paramInt2, i, j) - 1;
/* 1289 */         localObject2 = getCalendarDate(l9 + i2 * 7);
/* 1290 */         set(2, ((BaseCalendar.Date)localObject2).getMonth() - 1);
/* 1291 */         set(5, ((BaseCalendar.Date)localObject2).getDayOfMonth());
/*      */         return;
/*      */       }break;
/*      */     case 4:
/* 1297 */       boolean bool = isCutoverYear(this.cdate.getNormalizedYear());
/*      */ 
/* 1299 */       i1 = internalGet(7) - getFirstDayOfWeek();
/* 1300 */       if (i1 < 0) {
/* 1301 */         i1 += 7;
/*      */       }
/*      */ 
/* 1304 */       l4 = getCurrentFixedDate();
/*      */       long l7;
/* 1307 */       if (bool) {
/* 1308 */         l7 = getFixedDateMonth1(this.cdate, l4);
/* 1309 */         i11 = actualMonthLength();
/*      */       } else {
/* 1311 */         l7 = l4 - internalGet(5) + 1L;
/* 1312 */         i11 = this.calsys.getMonthLength(this.cdate);
/*      */       }
/*      */ 
/* 1316 */       long l10 = BaseCalendar.getDayOfWeekDateOnOrBefore(l7 + 6L, getFirstDayOfWeek());
/*      */ 
/* 1320 */       if ((int)(l10 - l7) >= getMinimalDaysInFirstWeek()) {
/* 1321 */         l10 -= 7L;
/*      */       }
/* 1323 */       j = getActualMaximum(paramInt1);
/*      */ 
/* 1326 */       int i13 = getRolledValue(internalGet(paramInt1), paramInt2, 1, j) - 1;
/*      */ 
/* 1329 */       long l11 = l10 + i13 * 7 + i1;
/*      */ 
/* 1333 */       if (l11 < l7)
/* 1334 */         l11 = l7;
/* 1335 */       else if (l11 >= l7 + i11)
/* 1336 */         l11 = l7 + i11 - 1L;
/*      */       int i14;
/* 1339 */       if (bool)
/*      */       {
/* 1342 */         BaseCalendar.Date localDate3 = getCalendarDate(l11);
/* 1343 */         i14 = localDate3.getDayOfMonth();
/*      */       } else {
/* 1345 */         i14 = (int)(l11 - l7) + 1;
/*      */       }
/* 1347 */       set(5, i14);
/* 1348 */       return;
/*      */     case 5:
/* 1353 */       if (!isCutoverYear(this.cdate.getNormalizedYear())) {
/* 1354 */         j = this.calsys.getMonthLength(this.cdate);
/*      */       } else {
/* 1359 */         l1 = getCurrentFixedDate();
/* 1360 */         l4 = getFixedDateMonth1(this.cdate, l1);
/*      */ 
/* 1364 */         i6 = getRolledValue((int)(l1 - l4), paramInt2, 0, actualMonthLength() - 1);
/* 1365 */         localObject1 = getCalendarDate(l4 + i6);
/* 1366 */         assert (((BaseCalendar.Date)localObject1).getMonth() - 1 == internalGet(2));
/* 1367 */         set(5, ((BaseCalendar.Date)localObject1).getDayOfMonth());
/*      */         return;
/*      */       }
/*      */       break;
/*      */     case 6:
/* 1373 */       j = getActualMaximum(paramInt1);
/* 1374 */       if (isCutoverYear(this.cdate.getNormalizedYear())) {
/* 1379 */         l1 = getCurrentFixedDate();
/* 1380 */         l4 = l1 - internalGet(6) + 1L;
/* 1381 */         i6 = getRolledValue((int)(l1 - l4) + 1, paramInt2, i, j);
/* 1382 */         localObject1 = getCalendarDate(l4 + i6 - 1L);
/* 1383 */         set(2, ((BaseCalendar.Date)localObject1).getMonth() - 1);
/* 1384 */         set(5, ((BaseCalendar.Date)localObject1).getDayOfMonth());
/*      */         return;
/*      */       }break;
/*      */     case 7:
/* 1390 */       if (!isCutoverYear(this.cdate.getNormalizedYear()))
/*      */       {
/* 1393 */         int m = internalGet(3);
/* 1394 */         if ((m > 1) && (m < 52)) {
/* 1395 */           set(3, m);
/* 1396 */           j = 7;
/* 1397 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1405 */       paramInt2 %= 7;
/* 1406 */       if (paramInt2 == 0) {
/* 1407 */         return;
/*      */       }
/* 1409 */       long l2 = getCurrentFixedDate();
/* 1410 */       l4 = BaseCalendar.getDayOfWeekDateOnOrBefore(l2, getFirstDayOfWeek());
/* 1411 */       l2 += paramInt2;
/* 1412 */       if (l2 < l4)
/* 1413 */         l2 += 7L;
/* 1414 */       else if (l2 >= l4 + 7L) {
/* 1415 */         l2 -= 7L;
/*      */       }
/* 1417 */       BaseCalendar.Date localDate1 = getCalendarDate(l2);
/* 1418 */       set(0, localDate1.getNormalizedYear() <= 0 ? 0 : 1);
/* 1419 */       set(localDate1.getYear(), localDate1.getMonth() - 1, localDate1.getDayOfMonth());
/* 1420 */       return;
/*      */     case 8:
/* 1425 */       i = 1;
/* 1426 */       if (!isCutoverYear(this.cdate.getNormalizedYear())) {
/* 1427 */         int n = internalGet(5);
/* 1428 */         i1 = this.calsys.getMonthLength(this.cdate);
/* 1429 */         int i3 = i1 % 7;
/* 1430 */         j = i1 / 7;
/* 1431 */         int i4 = (n - 1) % 7;
/* 1432 */         if (i4 < i3) {
/* 1433 */           j++;
/*      */         }
/* 1435 */         set(7, internalGet(7));
/*      */       }
/*      */       else
/*      */       {
/* 1440 */         long l3 = getCurrentFixedDate();
/* 1441 */         long l5 = getFixedDateMonth1(this.cdate, l3);
/* 1442 */         int i7 = actualMonthLength();
/* 1443 */         int i9 = i7 % 7;
/* 1444 */         j = i7 / 7;
/* 1445 */         i11 = (int)(l3 - l5) % 7;
/* 1446 */         if (i11 < i9) {
/* 1447 */           j++; } int i12 = getRolledValue(internalGet(paramInt1), paramInt2, i, j) - 1;
/* 1450 */         l3 = l5 + i12 * 7 + i11;
/* 1451 */         localObject2 = l3 >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem();
/* 1452 */         BaseCalendar.Date localDate2 = (BaseCalendar.Date)((BaseCalendar)localObject2).newCalendarDate(TimeZone.NO_TIMEZONE);
/* 1453 */         ((BaseCalendar)localObject2).getCalendarDateFromFixedDate(localDate2, l3);
/* 1454 */         set(5, localDate2.getDayOfMonth());
/*      */         return; } break; } set(paramInt1, getRolledValue(internalGet(paramInt1), paramInt2, i, j));
/*      */   }
/*      */ 
/*      */   public int getMinimum(int paramInt)
/*      */   {
/* 1482 */     return MIN_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getMaximum(int paramInt)
/*      */   {
/* 1505 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 8:
/* 1517 */       if (this.gregorianCutoverYear <= 200)
/*      */       {
/* 1521 */         GregorianCalendar localGregorianCalendar = (GregorianCalendar)clone();
/* 1522 */         localGregorianCalendar.setLenient(true);
/* 1523 */         localGregorianCalendar.setTimeInMillis(this.gregorianCutover);
/* 1524 */         int i = localGregorianCalendar.getActualMaximum(paramInt);
/* 1525 */         localGregorianCalendar.setTimeInMillis(this.gregorianCutover - 1L);
/* 1526 */         int j = localGregorianCalendar.getActualMaximum(paramInt);
/* 1527 */         return Math.max(MAX_VALUES[paramInt], Math.max(i, j));
/*      */       }break;
/*      */     case 7:
/* 1530 */     }return MAX_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getGreatestMinimum(int paramInt)
/*      */   {
/* 1553 */     if (paramInt == 5) {
/* 1554 */       BaseCalendar.Date localDate = getGregorianCutoverDate();
/* 1555 */       long l = getFixedDateMonth1(localDate, this.gregorianCutoverDate);
/* 1556 */       localDate = getCalendarDate(l);
/* 1557 */       return Math.max(MIN_VALUES[paramInt], localDate.getDayOfMonth());
/*      */     }
/* 1559 */     return MIN_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getLeastMaximum(int paramInt)
/*      */   {
/* 1582 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 8:
/* 1591 */       GregorianCalendar localGregorianCalendar = (GregorianCalendar)clone();
/* 1592 */       localGregorianCalendar.setLenient(true);
/* 1593 */       localGregorianCalendar.setTimeInMillis(this.gregorianCutover);
/* 1594 */       int i = localGregorianCalendar.getActualMaximum(paramInt);
/* 1595 */       localGregorianCalendar.setTimeInMillis(this.gregorianCutover - 1L);
/* 1596 */       int j = localGregorianCalendar.getActualMaximum(paramInt);
/* 1597 */       return Math.min(LEAST_MAX_VALUES[paramInt], Math.min(i, j));
/*      */     case 7:
/*      */     }
/* 1600 */     return LEAST_MAX_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getActualMinimum(int paramInt)
/*      */   {
/* 1631 */     if (paramInt == 5) {
/* 1632 */       GregorianCalendar localGregorianCalendar = getNormalizedCalendar();
/* 1633 */       int i = localGregorianCalendar.cdate.getNormalizedYear();
/* 1634 */       if ((i == this.gregorianCutoverYear) || (i == this.gregorianCutoverYearJulian)) {
/* 1635 */         long l = getFixedDateMonth1(localGregorianCalendar.cdate, localGregorianCalendar.calsys.getFixedDate(localGregorianCalendar.cdate));
/* 1636 */         BaseCalendar.Date localDate = getCalendarDate(l);
/* 1637 */         return localDate.getDayOfMonth();
/*      */       }
/*      */     }
/* 1640 */     return getMinimum(paramInt);
/*      */   }
/*      */ 
/*      */   public int getActualMaximum(int paramInt)
/*      */   {
/* 1677 */     if ((0x1FE81 & 1 << paramInt) != 0) {
/* 1678 */       return getMaximum(paramInt);
/*      */     }
/*      */ 
/* 1681 */     GregorianCalendar localGregorianCalendar = getNormalizedCalendar();
/* 1682 */     BaseCalendar.Date localDate1 = localGregorianCalendar.cdate;
/* 1683 */     BaseCalendar localBaseCalendar1 = localGregorianCalendar.calsys;
/* 1684 */     int i = localDate1.getNormalizedYear();
/*      */ 
/* 1686 */     int j = -1;
/*      */     long l1;
/*      */     int n;
/*      */     int i2;
/*      */     int m;
/* 1687 */     switch (paramInt)
/*      */     {
/*      */     case 2:
/* 1690 */       if (!localGregorianCalendar.isCutoverYear(i)) {
/* 1691 */         j = 11;
/*      */       }
/*      */       else
/*      */       {
/*      */         do
/*      */         {
/* 1698 */           l1 = gcal.getFixedDate(++i, 1, 1, null);
/* 1699 */         }while (l1 < this.gregorianCutoverDate);
/* 1700 */         BaseCalendar.Date localDate2 = (BaseCalendar.Date)localDate1.clone();
/* 1701 */         localBaseCalendar1.getCalendarDateFromFixedDate(localDate2, l1 - 1L);
/* 1702 */         j = localDate2.getMonth() - 1;
/*      */       }
/* 1704 */       break;
/*      */     case 5:
/* 1708 */       j = localBaseCalendar1.getMonthLength(localDate1);
/* 1709 */       if ((localGregorianCalendar.isCutoverYear(i)) && (localDate1.getDayOfMonth() != j))
/*      */       {
/* 1714 */         l1 = localGregorianCalendar.getCurrentFixedDate();
/* 1715 */         if (l1 < this.gregorianCutoverDate)
/*      */         {
/* 1718 */           int i1 = localGregorianCalendar.actualMonthLength();
/* 1719 */           long l5 = localGregorianCalendar.getFixedDateMonth1(localGregorianCalendar.cdate, l1) + i1 - 1L;
/*      */ 
/* 1721 */           BaseCalendar.Date localDate4 = localGregorianCalendar.getCalendarDate(l5);
/* 1722 */           j = localDate4.getDayOfMonth();
/*      */         }
/*      */       }
/* 1724 */       break;
/*      */     case 6:
/* 1728 */       if (!localGregorianCalendar.isCutoverYear(i)) {
/* 1729 */         j = localBaseCalendar1.getYearLength(localDate1);
/*      */       }
/*      */       else
/*      */       {
/* 1735 */         if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian) {
/* 1736 */           BaseCalendar localBaseCalendar2 = localGregorianCalendar.getCutoverCalendarSystem();
/* 1737 */           l1 = localBaseCalendar2.getFixedDate(i, 1, 1, null);
/* 1738 */         } else if (i == this.gregorianCutoverYearJulian) {
/* 1739 */           l1 = localBaseCalendar1.getFixedDate(i, 1, 1, null);
/*      */         } else {
/* 1741 */           l1 = this.gregorianCutoverDate;
/*      */         }
/*      */ 
/* 1744 */         long l3 = gcal.getFixedDate(++i, 1, 1, null);
/* 1745 */         if (l3 < this.gregorianCutoverDate) {
/* 1746 */           l3 = this.gregorianCutoverDate;
/*      */         }
/* 1748 */         assert (l1 <= localBaseCalendar1.getFixedDate(localDate1.getNormalizedYear(), localDate1.getMonth(), localDate1.getDayOfMonth(), localDate1));
/*      */ 
/* 1750 */         assert (l3 >= localBaseCalendar1.getFixedDate(localDate1.getNormalizedYear(), localDate1.getMonth(), localDate1.getDayOfMonth(), localDate1));
/*      */ 
/* 1752 */         j = (int)(l3 - l1);
/*      */       }
/* 1754 */       break;
/*      */     case 3:
/* 1758 */       if (!localGregorianCalendar.isCutoverYear(i))
/*      */       {
/* 1760 */         CalendarDate localCalendarDate1 = localBaseCalendar1.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 1761 */         localCalendarDate1.setDate(localDate1.getYear(), 1, 1);
/* 1762 */         n = localBaseCalendar1.getDayOfWeek(localCalendarDate1);
/*      */ 
/* 1764 */         n -= getFirstDayOfWeek();
/* 1765 */         if (n < 0) {
/* 1766 */           n += 7;
/*      */         }
/* 1768 */         j = 52;
/* 1769 */         i2 = n + getMinimalDaysInFirstWeek() - 1;
/* 1770 */         if ((i2 == 6) || ((localDate1.isLeapYear()) && ((i2 == 5) || (i2 == 12))))
/*      */         {
/* 1772 */           j++;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1777 */         if (localGregorianCalendar == this) {
/* 1778 */           localGregorianCalendar = (GregorianCalendar)localGregorianCalendar.clone();
/*      */         }
/* 1780 */         int k = getActualMaximum(6);
/* 1781 */         localGregorianCalendar.set(6, k);
/* 1782 */         j = localGregorianCalendar.get(3);
/* 1783 */         if (internalGet(1) != localGregorianCalendar.getWeekYear()) {
/* 1784 */           localGregorianCalendar.set(6, k - 7);
/* 1785 */           j = localGregorianCalendar.get(3);
/*      */         }
/*      */       }
/* 1788 */       break;
/*      */     case 4:
/* 1792 */       if (!localGregorianCalendar.isCutoverYear(i)) {
/* 1793 */         CalendarDate localCalendarDate2 = localBaseCalendar1.newCalendarDate(null);
/* 1794 */         localCalendarDate2.setDate(localDate1.getYear(), localDate1.getMonth(), 1);
/* 1795 */         n = localBaseCalendar1.getDayOfWeek(localCalendarDate2);
/* 1796 */         i2 = localBaseCalendar1.getMonthLength(localCalendarDate2);
/* 1797 */         n -= getFirstDayOfWeek();
/* 1798 */         if (n < 0) {
/* 1799 */           n += 7;
/*      */         }
/* 1801 */         int i3 = 7 - n;
/* 1802 */         j = 3;
/* 1803 */         if (i3 >= getMinimalDaysInFirstWeek()) {
/* 1804 */           j++;
/*      */         }
/* 1806 */         i2 -= i3 + 21;
/* 1807 */         if (i2 > 0) {
/* 1808 */           j++;
/* 1809 */           if (i2 > 7) {
/* 1810 */             j++;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1817 */         if (localGregorianCalendar == this) {
/* 1818 */           localGregorianCalendar = (GregorianCalendar)localGregorianCalendar.clone();
/*      */         }
/* 1820 */         m = localGregorianCalendar.internalGet(1);
/* 1821 */         n = localGregorianCalendar.internalGet(2);
/*      */         do {
/* 1823 */           j = localGregorianCalendar.get(4);
/* 1824 */           localGregorianCalendar.add(4, 1);
/* 1825 */         }while ((localGregorianCalendar.get(1) == m) && (localGregorianCalendar.get(2) == n));
/*      */       }
/* 1827 */       break;
/*      */     case 8:
/* 1833 */       i2 = localDate1.getDayOfWeek();
/* 1834 */       if (!localGregorianCalendar.isCutoverYear(i)) {
/* 1835 */         BaseCalendar.Date localDate3 = (BaseCalendar.Date)localDate1.clone();
/* 1836 */         m = localBaseCalendar1.getMonthLength(localDate3);
/* 1837 */         localDate3.setDayOfMonth(1);
/* 1838 */         localBaseCalendar1.normalize(localDate3);
/* 1839 */         n = localDate3.getDayOfWeek();
/*      */       }
/*      */       else {
/* 1842 */         if (localGregorianCalendar == this) {
/* 1843 */           localGregorianCalendar = (GregorianCalendar)clone();
/*      */         }
/* 1845 */         m = localGregorianCalendar.actualMonthLength();
/* 1846 */         localGregorianCalendar.set(5, localGregorianCalendar.getActualMinimum(5));
/* 1847 */         n = localGregorianCalendar.get(7);
/*      */       }
/* 1849 */       int i4 = i2 - n;
/* 1850 */       if (i4 < 0) {
/* 1851 */         i4 += 7;
/*      */       }
/* 1853 */       m -= i4;
/* 1854 */       j = (m + 6) / 7;
/*      */ 
/* 1856 */       break;
/*      */     case 1:
/* 1879 */       if (localGregorianCalendar == this) {
/* 1880 */         localGregorianCalendar = (GregorianCalendar)clone();
/*      */       }
/*      */ 
/* 1887 */       long l2 = localGregorianCalendar.getYearOffsetInMillis();
/*      */ 
/* 1889 */       if (localGregorianCalendar.internalGetEra() == 1) {
/* 1890 */         localGregorianCalendar.setTimeInMillis(9223372036854775807L);
/* 1891 */         j = localGregorianCalendar.get(1);
/* 1892 */         long l4 = localGregorianCalendar.getYearOffsetInMillis();
/* 1893 */         if (l2 > l4)
/* 1894 */           j--;
/*      */       }
/*      */       else {
/* 1897 */         BaseCalendar localBaseCalendar3 = localGregorianCalendar.getTimeInMillis() >= this.gregorianCutover ? gcal : getJulianCalendarSystem();
/*      */ 
/* 1899 */         CalendarDate localCalendarDate3 = localBaseCalendar3.getCalendarDate(-9223372036854775808L, getZone());
/* 1900 */         long l6 = (localBaseCalendar1.getDayOfYear(localCalendarDate3) - 1L) * 24L + localCalendarDate3.getHours();
/* 1901 */         l6 *= 60L;
/* 1902 */         l6 += localCalendarDate3.getMinutes();
/* 1903 */         l6 *= 60L;
/* 1904 */         l6 += localCalendarDate3.getSeconds();
/* 1905 */         l6 *= 1000L;
/* 1906 */         l6 += localCalendarDate3.getMillis();
/* 1907 */         j = localCalendarDate3.getYear();
/* 1908 */         if (j <= 0) {
/* 1909 */           assert (localBaseCalendar3 == gcal);
/* 1910 */           j = 1 - j;
/*      */         }
/* 1912 */         if (l2 < l6) {
/* 1913 */           j--;
/*      */         }
/*      */       }
/*      */ 
/* 1917 */       break;
/*      */     case 7:
/*      */     default:
/* 1920 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*      */     }
/* 1922 */     return j;
/*      */   }
/*      */ 
/*      */   private final long getYearOffsetInMillis()
/*      */   {
/* 1930 */     long l = (internalGet(6) - 1) * 24;
/* 1931 */     l += internalGet(11);
/* 1932 */     l *= 60L;
/* 1933 */     l += internalGet(12);
/* 1934 */     l *= 60L;
/* 1935 */     l += internalGet(13);
/* 1936 */     l *= 1000L;
/* 1937 */     return l + internalGet(14) - (internalGet(15) + internalGet(16));
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 1943 */     GregorianCalendar localGregorianCalendar = (GregorianCalendar)super.clone();
/*      */ 
/* 1945 */     localGregorianCalendar.gdate = ((BaseCalendar.Date)this.gdate.clone());
/* 1946 */     if (this.cdate != null) {
/* 1947 */       if (this.cdate != this.gdate)
/* 1948 */         localGregorianCalendar.cdate = ((BaseCalendar.Date)this.cdate.clone());
/*      */       else {
/* 1950 */         localGregorianCalendar.cdate = localGregorianCalendar.gdate;
/*      */       }
/*      */     }
/* 1953 */     localGregorianCalendar.originalFields = null;
/* 1954 */     localGregorianCalendar.zoneOffsets = null;
/* 1955 */     return localGregorianCalendar;
/*      */   }
/*      */ 
/*      */   public TimeZone getTimeZone() {
/* 1959 */     TimeZone localTimeZone = super.getTimeZone();
/*      */ 
/* 1961 */     this.gdate.setZone(localTimeZone);
/* 1962 */     if ((this.cdate != null) && (this.cdate != this.gdate)) {
/* 1963 */       this.cdate.setZone(localTimeZone);
/*      */     }
/* 1965 */     return localTimeZone;
/*      */   }
/*      */ 
/*      */   public void setTimeZone(TimeZone paramTimeZone) {
/* 1969 */     super.setTimeZone(paramTimeZone);
/*      */ 
/* 1971 */     this.gdate.setZone(paramTimeZone);
/* 1972 */     if ((this.cdate != null) && (this.cdate != this.gdate))
/* 1973 */       this.cdate.setZone(paramTimeZone);
/*      */   }
/*      */ 
/*      */   public final boolean isWeekDateSupported()
/*      */   {
/* 1989 */     return true;
/*      */   }
/*      */ 
/*      */   public int getWeekYear()
/*      */   {
/* 2016 */     int i = get(1);
/* 2017 */     if (internalGetEra() == 0) {
/* 2018 */       i = 1 - i;
/*      */     }
/*      */ 
/* 2023 */     if (i > this.gregorianCutoverYear + 1) {
/* 2024 */       j = internalGet(3);
/* 2025 */       if (internalGet(2) == 0) {
/* 2026 */         if (j >= 52) {
/* 2027 */           i--;
/*      */         }
/*      */       }
/* 2030 */       else if (j == 1) {
/* 2031 */         i++;
/*      */       }
/*      */ 
/* 2034 */       return i;
/*      */     }
/*      */ 
/* 2038 */     int j = internalGet(6);
/* 2039 */     int k = getActualMaximum(6);
/* 2040 */     int m = getMinimalDaysInFirstWeek();
/*      */ 
/* 2044 */     if ((j > m) && (j < k - 6)) {
/* 2045 */       return i;
/*      */     }
/*      */ 
/* 2049 */     GregorianCalendar localGregorianCalendar = (GregorianCalendar)clone();
/* 2050 */     localGregorianCalendar.setLenient(true);
/*      */ 
/* 2053 */     localGregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */ 
/* 2055 */     localGregorianCalendar.set(6, 1);
/* 2056 */     localGregorianCalendar.complete();
/*      */ 
/* 2059 */     int n = getFirstDayOfWeek() - localGregorianCalendar.get(7);
/* 2060 */     if (n != 0) {
/* 2061 */       if (n < 0) {
/* 2062 */         n += 7;
/*      */       }
/* 2064 */       localGregorianCalendar.add(6, n);
/*      */     }
/* 2066 */     int i1 = localGregorianCalendar.get(6);
/* 2067 */     if (j < i1) {
/* 2068 */       if (i1 <= m)
/* 2069 */         i--;
/*      */     }
/*      */     else {
/* 2072 */       localGregorianCalendar.set(1, i + 1);
/* 2073 */       localGregorianCalendar.set(6, 1);
/* 2074 */       localGregorianCalendar.complete();
/* 2075 */       int i2 = getFirstDayOfWeek() - localGregorianCalendar.get(7);
/* 2076 */       if (i2 != 0) {
/* 2077 */         if (i2 < 0) {
/* 2078 */           i2 += 7;
/*      */         }
/* 2080 */         localGregorianCalendar.add(6, i2);
/*      */       }
/* 2082 */       i1 = localGregorianCalendar.get(6) - 1;
/* 2083 */       if (i1 == 0) {
/* 2084 */         i1 = 7;
/*      */       }
/* 2086 */       if (i1 >= m) {
/* 2087 */         int i3 = k - j + 1;
/* 2088 */         if (i3 <= 7 - i1) {
/* 2089 */           i++;
/*      */         }
/*      */       }
/*      */     }
/* 2093 */     return i;
/*      */   }
/*      */ 
/*      */   public void setWeekDate(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2137 */     if ((paramInt3 < 1) || (paramInt3 > 7)) {
/* 2138 */       throw new IllegalArgumentException("invalid dayOfWeek: " + paramInt3);
/*      */     }
/*      */ 
/* 2143 */     GregorianCalendar localGregorianCalendar = (GregorianCalendar)clone();
/* 2144 */     localGregorianCalendar.setLenient(true);
/* 2145 */     int i = localGregorianCalendar.get(0);
/* 2146 */     localGregorianCalendar.clear();
/* 2147 */     localGregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 2148 */     localGregorianCalendar.set(0, i);
/* 2149 */     localGregorianCalendar.set(1, paramInt1);
/* 2150 */     localGregorianCalendar.set(3, 1);
/* 2151 */     localGregorianCalendar.set(7, getFirstDayOfWeek());
/* 2152 */     int j = paramInt3 - getFirstDayOfWeek();
/* 2153 */     if (j < 0) {
/* 2154 */       j += 7;
/*      */     }
/* 2156 */     j += 7 * (paramInt2 - 1);
/* 2157 */     if (j != 0)
/* 2158 */       localGregorianCalendar.add(6, j);
/*      */     else {
/* 2160 */       localGregorianCalendar.complete();
/*      */     }
/*      */ 
/* 2163 */     if ((!isLenient()) && ((localGregorianCalendar.getWeekYear() != paramInt1) || (localGregorianCalendar.internalGet(3) != paramInt2) || (localGregorianCalendar.internalGet(7) != paramInt3)))
/*      */     {
/* 2167 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/* 2170 */     set(0, localGregorianCalendar.internalGet(0));
/* 2171 */     set(1, localGregorianCalendar.internalGet(1));
/* 2172 */     set(2, localGregorianCalendar.internalGet(2));
/* 2173 */     set(5, localGregorianCalendar.internalGet(5));
/*      */ 
/* 2177 */     internalSet(3, paramInt2);
/* 2178 */     complete();
/*      */   }
/*      */ 
/*      */   public int getWeeksInWeekYear()
/*      */   {
/* 2199 */     GregorianCalendar localGregorianCalendar = getNormalizedCalendar();
/* 2200 */     int i = localGregorianCalendar.getWeekYear();
/* 2201 */     if (i == localGregorianCalendar.internalGet(1)) {
/* 2202 */       return localGregorianCalendar.getActualMaximum(3);
/*      */     }
/*      */ 
/* 2206 */     if (localGregorianCalendar == this) {
/* 2207 */       localGregorianCalendar = (GregorianCalendar)localGregorianCalendar.clone();
/*      */     }
/* 2209 */     localGregorianCalendar.setWeekDate(i, 2, internalGet(7));
/* 2210 */     return localGregorianCalendar.getActualMaximum(3);
/*      */   }
/*      */ 
/*      */   protected void computeFields()
/*      */   {
/* 2234 */     int i = 0;
/* 2235 */     if (isPartiallyNormalized())
/*      */     {
/* 2237 */       i = getSetStateFields();
/* 2238 */       int j = (i ^ 0xFFFFFFFF) & 0x1FFFF;
/*      */ 
/* 2241 */       if ((j != 0) || (this.calsys == null)) {
/* 2242 */         i |= computeFields(j, i & 0x18000);
/*      */ 
/* 2244 */         assert (i == 131071);
/*      */       }
/*      */     } else {
/* 2247 */       i = 131071;
/* 2248 */       computeFields(i, 0);
/*      */     }
/*      */ 
/* 2251 */     setFieldsComputed(i);
/*      */   }
/*      */ 
/*      */   private int computeFields(int paramInt1, int paramInt2)
/*      */   {
/* 2269 */     int i = 0;
/* 2270 */     TimeZone localTimeZone = getZone();
/* 2271 */     if (this.zoneOffsets == null) {
/* 2272 */       this.zoneOffsets = new int[2];
/*      */     }
/* 2274 */     if (paramInt2 != 98304) {
/* 2275 */       if ((localTimeZone instanceof ZoneInfo)) {
/* 2276 */         i = ((ZoneInfo)localTimeZone).getOffsets(this.time, this.zoneOffsets);
/*      */       } else {
/* 2278 */         i = localTimeZone.getOffset(this.time);
/* 2279 */         this.zoneOffsets[0] = localTimeZone.getRawOffset();
/* 2280 */         this.zoneOffsets[1] = (i - this.zoneOffsets[0]);
/*      */       }
/*      */     }
/* 2283 */     if (paramInt2 != 0) {
/* 2284 */       if (isFieldSet(paramInt2, 15)) {
/* 2285 */         this.zoneOffsets[0] = internalGet(15);
/*      */       }
/* 2287 */       if (isFieldSet(paramInt2, 16)) {
/* 2288 */         this.zoneOffsets[1] = internalGet(16);
/*      */       }
/* 2290 */       i = this.zoneOffsets[0] + this.zoneOffsets[1];
/*      */     }
/*      */ 
/* 2296 */     long l1 = i / 86400000L;
/* 2297 */     int j = i % 86400000;
/* 2298 */     l1 += this.time / 86400000L;
/* 2299 */     j += (int)(this.time % 86400000L);
/* 2300 */     if (j >= 86400000L) {
/* 2301 */       j = (int)(j - 86400000L);
/* 2302 */       l1 += 1L;
/*      */     } else {
/* 2304 */       while (j < 0) {
/* 2305 */         j = (int)(j + 86400000L);
/* 2306 */         l1 -= 1L;
/*      */       }
/*      */     }
/* 2309 */     l1 += 719163L;
/*      */ 
/* 2311 */     int k = 1;
/*      */     int m;
/* 2313 */     if (l1 >= this.gregorianCutoverDate)
/*      */     {
/* 2316 */       assert ((this.cachedFixedDate == -9223372036854775808L) || (this.gdate.isNormalized())) : "cache control: not normalized";
/*      */ 
/* 2322 */       assert ((this.cachedFixedDate == -9223372036854775808L) || (gcal.getFixedDate(this.gdate.getNormalizedYear(), this.gdate.getMonth(), this.gdate.getDayOfMonth(), this.gdate) == this.cachedFixedDate)) : ("cache control: inconsictency, cachedFixedDate=" + this.cachedFixedDate + ", computed=" + gcal.getFixedDate(this.gdate.getNormalizedYear(), this.gdate.getMonth(), this.gdate.getDayOfMonth(), this.gdate) + ", date=" + this.gdate);
/*      */ 
/* 2332 */       if (l1 != this.cachedFixedDate) {
/* 2333 */         gcal.getCalendarDateFromFixedDate(this.gdate, l1);
/* 2334 */         this.cachedFixedDate = l1;
/*      */       }
/*      */ 
/* 2337 */       m = this.gdate.getYear();
/* 2338 */       if (m <= 0) {
/* 2339 */         m = 1 - m;
/* 2340 */         k = 0;
/*      */       }
/* 2342 */       this.calsys = gcal;
/* 2343 */       this.cdate = this.gdate;
/* 2344 */       if ((!$assertionsDisabled) && (this.cdate.getDayOfWeek() <= 0)) throw new AssertionError("dow=" + this.cdate.getDayOfWeek() + ", date=" + this.cdate); 
/*      */     }
/*      */     else
/*      */     {
/* 2347 */       this.calsys = getJulianCalendarSystem();
/* 2348 */       this.cdate = jcal.newCalendarDate(getZone());
/* 2349 */       jcal.getCalendarDateFromFixedDate(this.cdate, l1);
/* 2350 */       Era localEra = this.cdate.getEra();
/* 2351 */       if (localEra == jeras[0]) {
/* 2352 */         k = 0;
/*      */       }
/* 2354 */       m = this.cdate.getYear();
/*      */     }
/*      */ 
/* 2358 */     internalSet(0, k);
/* 2359 */     internalSet(1, m);
/* 2360 */     int n = paramInt1 | 0x3;
/*      */ 
/* 2362 */     int i1 = this.cdate.getMonth() - 1;
/* 2363 */     int i2 = this.cdate.getDayOfMonth();
/*      */ 
/* 2366 */     if ((paramInt1 & 0xA4) != 0)
/*      */     {
/* 2368 */       internalSet(2, i1);
/* 2369 */       internalSet(5, i2);
/* 2370 */       internalSet(7, this.cdate.getDayOfWeek());
/* 2371 */       n |= 164;
/*      */     }
/*      */     int i3;
/* 2374 */     if ((paramInt1 & 0x7E00) != 0)
/*      */     {
/* 2376 */       if (j != 0) {
/* 2377 */         i3 = j / 3600000;
/* 2378 */         internalSet(11, i3);
/* 2379 */         internalSet(9, i3 / 12);
/* 2380 */         internalSet(10, i3 % 12);
/* 2381 */         int i4 = j % 3600000;
/* 2382 */         internalSet(12, i4 / 60000);
/* 2383 */         i4 %= 60000;
/* 2384 */         internalSet(13, i4 / 1000);
/* 2385 */         internalSet(14, i4 % 1000);
/*      */       } else {
/* 2387 */         internalSet(11, 0);
/* 2388 */         internalSet(9, 0);
/* 2389 */         internalSet(10, 0);
/* 2390 */         internalSet(12, 0);
/* 2391 */         internalSet(13, 0);
/* 2392 */         internalSet(14, 0);
/*      */       }
/* 2394 */       n |= 32256;
/*      */     }
/*      */ 
/* 2398 */     if ((paramInt1 & 0x18000) != 0) {
/* 2399 */       internalSet(15, this.zoneOffsets[0]);
/* 2400 */       internalSet(16, this.zoneOffsets[1]);
/* 2401 */       n |= 98304;
/*      */     }
/*      */ 
/* 2404 */     if ((paramInt1 & 0x158) != 0) {
/* 2405 */       i3 = this.cdate.getNormalizedYear();
/* 2406 */       long l2 = this.calsys.getFixedDate(i3, 1, 1, this.cdate);
/* 2407 */       int i5 = (int)(l1 - l2) + 1;
/* 2408 */       long l3 = l1 - i2 + 1L;
/* 2409 */       int i6 = 0;
/* 2410 */       int i7 = this.calsys == gcal ? this.gregorianCutoverYear : this.gregorianCutoverYearJulian;
/* 2411 */       int i8 = i2 - 1;
/*      */ 
/* 2414 */       if (i3 == i7)
/*      */       {
/* 2416 */         if (this.gregorianCutoverYearJulian <= this.gregorianCutoverYear)
/*      */         {
/* 2420 */           l2 = getFixedDateJan1(this.cdate, l1);
/* 2421 */           if (l1 >= this.gregorianCutoverDate) {
/* 2422 */             l3 = getFixedDateMonth1(this.cdate, l1);
/*      */           }
/*      */         }
/* 2425 */         i9 = (int)(l1 - l2) + 1;
/* 2426 */         i6 = i5 - i9;
/* 2427 */         i5 = i9;
/* 2428 */         i8 = (int)(l1 - l3);
/*      */       }
/* 2430 */       internalSet(6, i5);
/* 2431 */       internalSet(8, i8 / 7 + 1);
/*      */ 
/* 2433 */       int i9 = getWeekNumber(l2, l1);
/*      */       long l4;
/*      */       long l5;
/* 2437 */       if (i9 == 0)
/*      */       {
/* 2445 */         l4 = l2 - 1L;
/* 2446 */         l5 = l2 - 365L;
/* 2447 */         if (i3 > i7 + 1) {
/* 2448 */           if (CalendarUtils.isGregorianLeapYear(i3 - 1))
/* 2449 */             l5 -= 1L;
/*      */         }
/* 2451 */         else if (i3 <= this.gregorianCutoverYearJulian) {
/* 2452 */           if (CalendarUtils.isJulianLeapYear(i3 - 1))
/* 2453 */             l5 -= 1L;
/*      */         }
/*      */         else {
/* 2456 */           Object localObject2 = this.calsys;
/*      */ 
/* 2458 */           int i12 = getCalendarDate(l4).getNormalizedYear();
/* 2459 */           if (i12 == this.gregorianCutoverYear) {
/* 2460 */             localObject2 = getCutoverCalendarSystem();
/* 2461 */             if (localObject2 == jcal) {
/* 2462 */               l5 = ((BaseCalendar)localObject2).getFixedDate(i12, 1, 1, null);
/*      */             }
/*      */             else
/*      */             {
/* 2467 */               l5 = this.gregorianCutoverDate;
/* 2468 */               localObject2 = gcal;
/*      */             }
/* 2470 */           } else if (i12 <= this.gregorianCutoverYearJulian) {
/* 2471 */             localObject2 = getJulianCalendarSystem();
/* 2472 */             l5 = ((BaseCalendar)localObject2).getFixedDate(i12, 1, 1, null);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2478 */         i9 = getWeekNumber(l5, l4);
/*      */       }
/* 2480 */       else if ((i3 > this.gregorianCutoverYear) || (i3 < this.gregorianCutoverYearJulian - 1))
/*      */       {
/* 2483 */         if (i9 >= 52) {
/* 2484 */           l4 = l2 + 365L;
/* 2485 */           if (this.cdate.isLeapYear()) {
/* 2486 */             l4 += 1L;
/*      */           }
/* 2488 */           l5 = BaseCalendar.getDayOfWeekDateOnOrBefore(l4 + 6L, getFirstDayOfWeek());
/*      */ 
/* 2490 */           int i11 = (int)(l5 - l4);
/* 2491 */           if ((i11 >= getMinimalDaysInFirstWeek()) && (l1 >= l5 - 7L))
/*      */           {
/* 2493 */             i9 = 1;
/*      */           }
/*      */         }
/*      */       } else {
/* 2497 */         Object localObject1 = this.calsys;
/* 2498 */         int i10 = i3 + 1;
/* 2499 */         if ((i10 == this.gregorianCutoverYearJulian + 1) && (i10 < this.gregorianCutoverYear))
/*      */         {
/* 2502 */           i10 = this.gregorianCutoverYear;
/*      */         }
/* 2504 */         if (i10 == this.gregorianCutoverYear) {
/* 2505 */           localObject1 = getCutoverCalendarSystem();
/*      */         }
/*      */ 
/* 2509 */         if ((i10 > this.gregorianCutoverYear) || (this.gregorianCutoverYearJulian == this.gregorianCutoverYear) || (i10 == this.gregorianCutoverYearJulian))
/*      */         {
/* 2512 */           l5 = ((BaseCalendar)localObject1).getFixedDate(i10, 1, 1, null);
/*      */         }
/*      */         else
/*      */         {
/* 2517 */           l5 = this.gregorianCutoverDate;
/* 2518 */           localObject1 = gcal;
/*      */         }
/*      */ 
/* 2521 */         long l6 = BaseCalendar.getDayOfWeekDateOnOrBefore(l5 + 6L, getFirstDayOfWeek());
/*      */ 
/* 2523 */         int i13 = (int)(l6 - l5);
/* 2524 */         if ((i13 >= getMinimalDaysInFirstWeek()) && (l1 >= l6 - 7L))
/*      */         {
/* 2526 */           i9 = 1;
/*      */         }
/*      */       }
/*      */ 
/* 2530 */       internalSet(3, i9);
/* 2531 */       internalSet(4, getWeekNumber(l3, l1));
/* 2532 */       n |= 344;
/*      */     }
/* 2534 */     return n;
/*      */   }
/*      */ 
/*      */   private final int getWeekNumber(long paramLong1, long paramLong2)
/*      */   {
/* 2549 */     long l = Gregorian.getDayOfWeekDateOnOrBefore(paramLong1 + 6L, getFirstDayOfWeek());
/*      */ 
/* 2551 */     int i = (int)(l - paramLong1);
/* 2552 */     assert (i <= 7);
/* 2553 */     if (i >= getMinimalDaysInFirstWeek()) {
/* 2554 */       l -= 7L;
/*      */     }
/* 2556 */     int j = (int)(paramLong2 - l);
/* 2557 */     if (j >= 0) {
/* 2558 */       return j / 7 + 1;
/*      */     }
/* 2560 */     return CalendarUtils.floorDivide(j, 7) + 1;
/*      */   }
/*      */ 
/*      */   protected void computeTime()
/*      */   {
/* 2574 */     if (!isLenient()) {
/* 2575 */       if (this.originalFields == null) {
/* 2576 */         this.originalFields = new int[17];
/*      */       }
/* 2578 */       for (i = 0; i < 17; i++) {
/* 2579 */         j = internalGet(i);
/* 2580 */         if (isExternallySet(i))
/*      */         {
/* 2582 */           if ((j < getMinimum(i)) || (j > getMaximum(i))) {
/* 2583 */             throw new IllegalArgumentException(getFieldName(i));
/*      */           }
/*      */         }
/* 2586 */         this.originalFields[i] = j;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2592 */     int i = selectFields();
/*      */ 
/* 2597 */     int j = isSet(1) ? internalGet(1) : 1970;
/*      */ 
/* 2599 */     int k = internalGetEra();
/* 2600 */     if (k == 0)
/* 2601 */       j = 1 - j;
/* 2602 */     else if (k != 1)
/*      */     {
/* 2607 */       throw new IllegalArgumentException("Invalid era");
/*      */     }
/*      */ 
/* 2611 */     if ((j <= 0) && (!isSet(0))) {
/* 2612 */       i |= 1;
/* 2613 */       setFieldsComputed(1);
/*      */     }
/*      */ 
/* 2618 */     long l1 = 0L;
/* 2619 */     if (isFieldSet(i, 11)) {
/* 2620 */       l1 += internalGet(11);
/*      */     } else {
/* 2622 */       l1 += internalGet(10);
/*      */ 
/* 2624 */       if (isFieldSet(i, 9)) {
/* 2625 */         l1 += 12 * internalGet(9);
/*      */       }
/*      */     }
/* 2628 */     l1 *= 60L;
/* 2629 */     l1 += internalGet(12);
/* 2630 */     l1 *= 60L;
/* 2631 */     l1 += internalGet(13);
/* 2632 */     l1 *= 1000L;
/* 2633 */     l1 += internalGet(14);
/*      */ 
/* 2637 */     long l2 = l1 / 86400000L;
/* 2638 */     l1 %= 86400000L;
/* 2639 */     while (l1 < 0L) {
/* 2640 */       l1 += 86400000L;
/* 2641 */       l2 -= 1L;
/*      */     }
/*      */     long l4;
/* 2647 */     if ((j > this.gregorianCutoverYear) && (j > this.gregorianCutoverYearJulian)) {
/* 2648 */       l3 = l2 + getFixedDate(gcal, j, i);
/* 2649 */       if (l3 >= this.gregorianCutoverDate) {
/* 2650 */         l2 = l3;
/* 2651 */         break label619;
/*      */       }
/* 2653 */       l4 = l2 + getFixedDate(getJulianCalendarSystem(), j, i);
/* 2654 */     } else if ((j < this.gregorianCutoverYear) && (j < this.gregorianCutoverYearJulian)) {
/* 2655 */       l4 = l2 + getFixedDate(getJulianCalendarSystem(), j, i);
/* 2656 */       if (l4 < this.gregorianCutoverDate) {
/* 2657 */         l2 = l4;
/* 2658 */         break label619;
/*      */       }
/* 2660 */       l3 = l4;
/*      */     } else {
/* 2662 */       l4 = l2 + getFixedDate(getJulianCalendarSystem(), j, i);
/* 2663 */       l3 = l2 + getFixedDate(gcal, j, i);
/*      */     }
/*      */ 
/* 2670 */     if ((isFieldSet(i, 6)) || (isFieldSet(i, 3))) {
/* 2671 */       if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian) {
/* 2672 */         l2 = l4;
/* 2673 */         break label619;
/* 2674 */       }if (j == this.gregorianCutoverYear) {
/* 2675 */         l2 = l3;
/* 2676 */         break label619;
/*      */       }
/*      */     }
/*      */ 
/* 2680 */     if (l3 >= this.gregorianCutoverDate) {
/* 2681 */       if (l4 >= this.gregorianCutoverDate) {
/* 2682 */         l2 = l3;
/*      */       }
/* 2687 */       else if ((this.calsys == gcal) || (this.calsys == null))
/* 2688 */         l2 = l3;
/*      */       else {
/* 2690 */         l2 = l4;
/*      */       }
/*      */ 
/*      */     }
/* 2694 */     else if (l4 < this.gregorianCutoverDate) {
/* 2695 */       l2 = l4;
/*      */     }
/*      */     else {
/* 2698 */       if (!isLenient()) {
/* 2699 */         throw new IllegalArgumentException("the specified date doesn't exist");
/*      */       }
/*      */ 
/* 2703 */       l2 = l4;
/*      */     }
/*      */ 
/* 2709 */     label619: long l3 = (l2 - 719163L) * 86400000L + l1;
/*      */ 
/* 2724 */     TimeZone localTimeZone = getZone();
/* 2725 */     if (this.zoneOffsets == null) {
/* 2726 */       this.zoneOffsets = new int[2];
/*      */     }
/* 2728 */     int m = i & 0x18000;
/* 2729 */     if (m != 98304) {
/* 2730 */       if ((localTimeZone instanceof ZoneInfo)) {
/* 2731 */         ((ZoneInfo)localTimeZone).getOffsetsByWall(l3, this.zoneOffsets);
/*      */       } else {
/* 2733 */         n = isFieldSet(i, 15) ? internalGet(15) : localTimeZone.getRawOffset();
/*      */ 
/* 2735 */         localTimeZone.getOffsets(l3 - n, this.zoneOffsets);
/*      */       }
/*      */     }
/* 2738 */     if (m != 0) {
/* 2739 */       if (isFieldSet(m, 15)) {
/* 2740 */         this.zoneOffsets[0] = internalGet(15);
/*      */       }
/* 2742 */       if (isFieldSet(m, 16)) {
/* 2743 */         this.zoneOffsets[1] = internalGet(16);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2748 */     l3 -= this.zoneOffsets[0] + this.zoneOffsets[1];
/*      */ 
/* 2751 */     this.time = l3;
/*      */ 
/* 2753 */     int n = computeFields(i | getSetStateFields(), m);
/*      */ 
/* 2755 */     if (!isLenient()) {
/* 2756 */       for (int i1 = 0; i1 < 17; i1++)
/* 2757 */         if (isExternallySet(i1))
/*      */         {
/* 2760 */           if (this.originalFields[i1] != internalGet(i1)) {
/* 2761 */             String str = this.originalFields[i1] + " -> " + internalGet(i1);
/*      */ 
/* 2763 */             System.arraycopy(this.originalFields, 0, this.fields, 0, this.fields.length);
/* 2764 */             throw new IllegalArgumentException(getFieldName(i1) + ": " + str);
/*      */           }
/*      */         }
/*      */     }
/* 2768 */     setFieldsNormalized(n);
/*      */   }
/*      */ 
/*      */   private long getFixedDate(BaseCalendar paramBaseCalendar, int paramInt1, int paramInt2)
/*      */   {
/* 2783 */     int i = 0;
/* 2784 */     if (isFieldSet(paramInt2, 2))
/*      */     {
/* 2787 */       i = internalGet(2);
/*      */ 
/* 2790 */       if (i > 11) {
/* 2791 */         paramInt1 += i / 12;
/* 2792 */         i %= 12;
/* 2793 */       } else if (i < 0) {
/* 2794 */         int[] arrayOfInt = new int[1];
/* 2795 */         paramInt1 += CalendarUtils.floorDivide(i, 12, arrayOfInt);
/* 2796 */         i = arrayOfInt[0];
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2802 */     long l1 = paramBaseCalendar.getFixedDate(paramInt1, i + 1, 1, paramBaseCalendar == gcal ? this.gdate : null);
/*      */     int m;
/* 2804 */     if (isFieldSet(paramInt2, 2))
/*      */     {
/* 2806 */       if (isFieldSet(paramInt2, 5))
/*      */       {
/* 2813 */         if (isSet(5))
/*      */         {
/* 2816 */           l1 += internalGet(5);
/* 2817 */           l1 -= 1L;
/*      */         }
/*      */       }
/* 2820 */       else if (isFieldSet(paramInt2, 4)) {
/* 2821 */         long l2 = BaseCalendar.getDayOfWeekDateOnOrBefore(l1 + 6L, getFirstDayOfWeek());
/*      */ 
/* 2825 */         if (l2 - l1 >= getMinimalDaysInFirstWeek()) {
/* 2826 */           l2 -= 7L;
/*      */         }
/* 2828 */         if (isFieldSet(paramInt2, 7)) {
/* 2829 */           l2 = BaseCalendar.getDayOfWeekDateOnOrBefore(l2 + 6L, internalGet(7));
/*      */         }
/*      */ 
/* 2835 */         l1 = l2 + 7 * (internalGet(4) - 1);
/*      */       }
/*      */       else
/*      */       {
/*      */         int j;
/* 2838 */         if (isFieldSet(paramInt2, 7))
/* 2839 */           j = internalGet(7);
/*      */         else
/* 2841 */           j = getFirstDayOfWeek();
/*      */         int k;
/* 2847 */         if (isFieldSet(paramInt2, 8))
/* 2848 */           k = internalGet(8);
/*      */         else {
/* 2850 */           k = 1;
/*      */         }
/* 2852 */         if (k >= 0) {
/* 2853 */           l1 = BaseCalendar.getDayOfWeekDateOnOrBefore(l1 + 7 * k - 1L, j);
/*      */         }
/*      */         else
/*      */         {
/* 2858 */           m = monthLength(i, paramInt1) + 7 * (k + 1);
/*      */ 
/* 2860 */           l1 = BaseCalendar.getDayOfWeekDateOnOrBefore(l1 + m - 1L, j);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2866 */       if ((paramInt1 == this.gregorianCutoverYear) && (paramBaseCalendar == gcal) && (l1 < this.gregorianCutoverDate) && (this.gregorianCutoverYear != this.gregorianCutoverYearJulian))
/*      */       {
/* 2872 */         l1 = this.gregorianCutoverDate;
/*      */       }
/*      */ 
/* 2875 */       if (isFieldSet(paramInt2, 6))
/*      */       {
/* 2877 */         l1 += internalGet(6);
/* 2878 */         l1 -= 1L;
/*      */       } else {
/* 2880 */         long l3 = BaseCalendar.getDayOfWeekDateOnOrBefore(l1 + 6L, getFirstDayOfWeek());
/*      */ 
/* 2884 */         if (l3 - l1 >= getMinimalDaysInFirstWeek()) {
/* 2885 */           l3 -= 7L;
/*      */         }
/* 2887 */         if (isFieldSet(paramInt2, 7)) {
/* 2888 */           m = internalGet(7);
/* 2889 */           if (m != getFirstDayOfWeek()) {
/* 2890 */             l3 = BaseCalendar.getDayOfWeekDateOnOrBefore(l3 + 6L, m);
/*      */           }
/*      */         }
/*      */ 
/* 2894 */         l1 = l3 + 7L * (internalGet(3) - 1L);
/*      */       }
/*      */     }
/*      */ 
/* 2898 */     return l1;
/*      */   }
/*      */ 
/*      */   private final GregorianCalendar getNormalizedCalendar()
/*      */   {
/*      */     GregorianCalendar localGregorianCalendar;
/* 2908 */     if (isFullyNormalized()) {
/* 2909 */       localGregorianCalendar = this;
/*      */     }
/*      */     else {
/* 2912 */       localGregorianCalendar = (GregorianCalendar)clone();
/* 2913 */       localGregorianCalendar.setLenient(true);
/* 2914 */       localGregorianCalendar.complete();
/*      */     }
/* 2916 */     return localGregorianCalendar;
/*      */   }
/*      */ 
/*      */   private static final synchronized BaseCalendar getJulianCalendarSystem()
/*      */   {
/* 2924 */     if (jcal == null) {
/* 2925 */       jcal = (JulianCalendar)CalendarSystem.forName("julian");
/* 2926 */       jeras = jcal.getEras();
/*      */     }
/* 2928 */     return jcal;
/*      */   }
/*      */ 
/*      */   private BaseCalendar getCutoverCalendarSystem()
/*      */   {
/* 2937 */     if (this.gregorianCutoverYearJulian < this.gregorianCutoverYear) {
/* 2938 */       return gcal;
/*      */     }
/* 2940 */     return getJulianCalendarSystem();
/*      */   }
/*      */ 
/*      */   private final boolean isCutoverYear(int paramInt)
/*      */   {
/* 2948 */     int i = this.calsys == gcal ? this.gregorianCutoverYear : this.gregorianCutoverYearJulian;
/* 2949 */     return paramInt == i;
/*      */   }
/*      */ 
/*      */   private final long getFixedDateJan1(BaseCalendar.Date paramDate, long paramLong)
/*      */   {
/* 2962 */     assert ((paramDate.getNormalizedYear() == this.gregorianCutoverYear) || (paramDate.getNormalizedYear() == this.gregorianCutoverYearJulian));
/*      */ 
/* 2964 */     if ((this.gregorianCutoverYear != this.gregorianCutoverYearJulian) && 
/* 2965 */       (paramLong >= this.gregorianCutoverDate))
/*      */     {
/* 2970 */       return this.gregorianCutoverDate;
/*      */     }
/*      */ 
/* 2974 */     BaseCalendar localBaseCalendar = getJulianCalendarSystem();
/* 2975 */     return localBaseCalendar.getFixedDate(paramDate.getNormalizedYear(), 1, 1, null);
/*      */   }
/*      */ 
/*      */   private final long getFixedDateMonth1(BaseCalendar.Date paramDate, long paramLong)
/*      */   {
/* 2988 */     assert ((paramDate.getNormalizedYear() == this.gregorianCutoverYear) || (paramDate.getNormalizedYear() == this.gregorianCutoverYearJulian));
/*      */ 
/* 2990 */     BaseCalendar.Date localDate1 = getGregorianCutoverDate();
/* 2991 */     if ((localDate1.getMonth() == 1) && (localDate1.getDayOfMonth() == 1))
/*      */     {
/* 2994 */       return paramLong - paramDate.getDayOfMonth() + 1L;
/*      */     }
/*      */     long l;
/* 2999 */     if (paramDate.getMonth() == localDate1.getMonth())
/*      */     {
/* 3001 */       BaseCalendar.Date localDate2 = getLastJulianDate();
/* 3002 */       if ((this.gregorianCutoverYear == this.gregorianCutoverYearJulian) && (localDate1.getMonth() == localDate2.getMonth()))
/*      */       {
/* 3005 */         l = jcal.getFixedDate(paramDate.getNormalizedYear(), paramDate.getMonth(), 1, null);
/*      */       }
/*      */       else
/*      */       {
/* 3011 */         l = this.gregorianCutoverDate;
/*      */       }
/*      */     }
/*      */     else {
/* 3015 */       l = paramLong - paramDate.getDayOfMonth() + 1L;
/*      */     }
/*      */ 
/* 3018 */     return l;
/*      */   }
/*      */ 
/*      */   private final BaseCalendar.Date getCalendarDate(long paramLong)
/*      */   {
/* 3027 */     BaseCalendar localBaseCalendar = paramLong >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem();
/* 3028 */     BaseCalendar.Date localDate = (BaseCalendar.Date)localBaseCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 3029 */     localBaseCalendar.getCalendarDateFromFixedDate(localDate, paramLong);
/* 3030 */     return localDate;
/*      */   }
/*      */ 
/*      */   private final BaseCalendar.Date getGregorianCutoverDate()
/*      */   {
/* 3038 */     return getCalendarDate(this.gregorianCutoverDate);
/*      */   }
/*      */ 
/*      */   private final BaseCalendar.Date getLastJulianDate()
/*      */   {
/* 3046 */     return getCalendarDate(this.gregorianCutoverDate - 1L);
/*      */   }
/*      */ 
/*      */   private final int monthLength(int paramInt1, int paramInt2)
/*      */   {
/* 3056 */     return isLeapYear(paramInt2) ? LEAP_MONTH_LENGTH[paramInt1] : MONTH_LENGTH[paramInt1];
/*      */   }
/*      */ 
/*      */   private final int monthLength(int paramInt)
/*      */   {
/* 3066 */     int i = internalGet(1);
/* 3067 */     if (internalGetEra() == 0) {
/* 3068 */       i = 1 - i;
/*      */     }
/* 3070 */     return monthLength(paramInt, i);
/*      */   }
/*      */ 
/*      */   private final int actualMonthLength() {
/* 3074 */     int i = this.cdate.getNormalizedYear();
/* 3075 */     if ((i != this.gregorianCutoverYear) && (i != this.gregorianCutoverYearJulian)) {
/* 3076 */       return this.calsys.getMonthLength(this.cdate);
/*      */     }
/* 3078 */     Object localObject = (BaseCalendar.Date)this.cdate.clone();
/* 3079 */     long l1 = this.calsys.getFixedDate((CalendarDate)localObject);
/* 3080 */     long l2 = getFixedDateMonth1((BaseCalendar.Date)localObject, l1);
/* 3081 */     long l3 = l2 + this.calsys.getMonthLength((CalendarDate)localObject);
/* 3082 */     if (l3 < this.gregorianCutoverDate) {
/* 3083 */       return (int)(l3 - l2);
/*      */     }
/* 3085 */     if (this.cdate != this.gdate) {
/* 3086 */       localObject = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/*      */     }
/* 3088 */     gcal.getCalendarDateFromFixedDate((CalendarDate)localObject, l3);
/* 3089 */     l3 = getFixedDateMonth1((BaseCalendar.Date)localObject, l3);
/* 3090 */     return (int)(l3 - l2);
/*      */   }
/*      */ 
/*      */   private final int yearLength(int paramInt)
/*      */   {
/* 3098 */     return isLeapYear(paramInt) ? 366 : 365;
/*      */   }
/*      */ 
/*      */   private final int yearLength()
/*      */   {
/* 3106 */     int i = internalGet(1);
/* 3107 */     if (internalGetEra() == 0) {
/* 3108 */       i = 1 - i;
/*      */     }
/* 3110 */     return yearLength(i);
/*      */   }
/*      */ 
/*      */   private final void pinDayOfMonth()
/*      */   {
/* 3120 */     int i = internalGet(1);
/*      */     int j;
/* 3122 */     if ((i > this.gregorianCutoverYear) || (i < this.gregorianCutoverYearJulian)) {
/* 3123 */       j = monthLength(internalGet(2));
/*      */     } else {
/* 3125 */       GregorianCalendar localGregorianCalendar = getNormalizedCalendar();
/* 3126 */       j = localGregorianCalendar.getActualMaximum(5);
/*      */     }
/* 3128 */     int k = internalGet(5);
/* 3129 */     if (k > j)
/* 3130 */       set(5, j);
/*      */   }
/*      */ 
/*      */   private final long getCurrentFixedDate()
/*      */   {
/* 3139 */     return this.calsys == gcal ? this.cachedFixedDate : this.calsys.getFixedDate(this.cdate);
/*      */   }
/*      */ 
/*      */   private static final int getRolledValue(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 3146 */     assert ((paramInt1 >= paramInt3) && (paramInt1 <= paramInt4));
/* 3147 */     int i = paramInt4 - paramInt3 + 1;
/* 3148 */     paramInt2 %= i;
/* 3149 */     int j = paramInt1 + paramInt2;
/* 3150 */     if (j > paramInt4)
/* 3151 */       j -= i;
/* 3152 */     else if (j < paramInt3) {
/* 3153 */       j += i;
/*      */     }
/* 3155 */     assert ((j >= paramInt3) && (j <= paramInt4));
/* 3156 */     return j;
/*      */   }
/*      */ 
/*      */   private final int internalGetEra()
/*      */   {
/* 3164 */     return isSet(0) ? internalGet(0) : 1;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 3172 */     paramObjectInputStream.defaultReadObject();
/* 3173 */     if (this.gdate == null) {
/* 3174 */       this.gdate = gcal.newCalendarDate(getZone());
/* 3175 */       this.cachedFixedDate = -9223372036854775808L;
/*      */     }
/* 3177 */     setGregorianChange(this.gregorianCutover);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.GregorianCalendar
 * JD-Core Version:    0.6.2
 */