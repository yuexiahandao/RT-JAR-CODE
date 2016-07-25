/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormat;
/*      */ import sun.util.calendar.BaseCalendar;
/*      */ import sun.util.calendar.BaseCalendar.Date;
/*      */ import sun.util.calendar.CalendarDate;
/*      */ import sun.util.calendar.CalendarSystem;
/*      */ import sun.util.calendar.CalendarUtils;
/*      */ import sun.util.calendar.ZoneInfo;
/*      */ 
/*      */ public class Date
/*      */   implements Serializable, Cloneable, Comparable<Date>
/*      */ {
/*  132 */   private static final BaseCalendar gcal = CalendarSystem.getGregorianCalendar();
/*      */   private static BaseCalendar jcal;
/*      */   private transient long fastTime;
/*      */   private transient BaseCalendar.Date cdate;
/*      */   private static int defaultCenturyStart;
/*      */   private static final long serialVersionUID = 7523967970034938905L;
/*  617 */   private static final String[] wtb = { "am", "pm", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", "gmt", "ut", "utc", "est", "edt", "cst", "cdt", "mst", "mdt", "pst", "pdt" };
/*      */ 
/*  626 */   private static final int[] ttb = { 14, 1, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 10000, 10000, 10000, 10300, 10240, 10360, 10300, 10420, 10360, 10480, 10420 };
/*      */ 
/*      */   public Date()
/*      */   {
/*  163 */     this(System.currentTimeMillis());
/*      */   }
/*      */ 
/*      */   public Date(long paramLong)
/*      */   {
/*  176 */     this.fastTime = paramLong;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Date(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  195 */     this(paramInt1, paramInt2, paramInt3, 0, 0, 0);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Date(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  218 */     this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 0);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Date(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  242 */     int i = paramInt1 + 1900;
/*      */ 
/*  244 */     if (paramInt2 >= 12) {
/*  245 */       i += paramInt2 / 12;
/*  246 */       paramInt2 %= 12;
/*  247 */     } else if (paramInt2 < 0) {
/*  248 */       i += CalendarUtils.floorDivide(paramInt2, 12);
/*  249 */       paramInt2 = CalendarUtils.mod(paramInt2, 12);
/*      */     }
/*  251 */     BaseCalendar localBaseCalendar = getCalendarSystem(i);
/*  252 */     this.cdate = ((BaseCalendar.Date)localBaseCalendar.newCalendarDate(TimeZone.getDefaultRef()));
/*  253 */     this.cdate.setNormalizedDate(i, paramInt2 + 1, paramInt3).setTimeOfDay(paramInt4, paramInt5, paramInt6, 0);
/*  254 */     getTimeImpl();
/*  255 */     this.cdate = null;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Date(String paramString)
/*      */   {
/*  272 */     this(parse(paramString));
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  279 */     Date localDate = null;
/*      */     try {
/*  281 */       localDate = (Date)super.clone();
/*  282 */       if (this.cdate != null)
/*  283 */         localDate.cdate = ((BaseCalendar.Date)this.cdate.clone());
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  286 */     return localDate;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static long UTC(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  317 */     int i = paramInt1 + 1900;
/*      */ 
/*  319 */     if (paramInt2 >= 12) {
/*  320 */       i += paramInt2 / 12;
/*  321 */       paramInt2 %= 12;
/*  322 */     } else if (paramInt2 < 0) {
/*  323 */       i += CalendarUtils.floorDivide(paramInt2, 12);
/*  324 */       paramInt2 = CalendarUtils.mod(paramInt2, 12);
/*      */     }
/*  326 */     int j = paramInt2 + 1;
/*  327 */     BaseCalendar localBaseCalendar = getCalendarSystem(i);
/*  328 */     BaseCalendar.Date localDate = (BaseCalendar.Date)localBaseCalendar.newCalendarDate(null);
/*  329 */     localDate.setNormalizedDate(i, j, paramInt3).setTimeOfDay(paramInt4, paramInt5, paramInt6, 0);
/*      */ 
/*  333 */     Date localDate1 = new Date(0L);
/*  334 */     localDate1.normalize(localDate);
/*  335 */     return localDate1.fastTime;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static long parse(String paramString)
/*      */   {
/*  453 */     int i = -2147483648;
/*  454 */     int j = -1;
/*  455 */     int k = -1;
/*  456 */     int m = -1;
/*  457 */     int n = -1;
/*  458 */     int i1 = -1;
/*  459 */     int i2 = -1;
/*  460 */     int i3 = -1;
/*  461 */     int i4 = 0;
/*  462 */     int i5 = -1;
/*  463 */     int i6 = -1;
/*  464 */     int i7 = -1;
/*  465 */     int i8 = 0;
/*      */ 
/*  468 */     if (paramString != null)
/*      */     {
/*  470 */       int i9 = paramString.length();
/*      */       while (true) { if (i4 >= i9) break label783;
/*  472 */         i3 = paramString.charAt(i4);
/*  473 */         i4++;
/*  474 */         if ((i3 > 32) && (i3 != 44))
/*      */         {
/*      */           int i10;
/*  476 */           if (i3 == 40) {
/*  477 */             for (i10 = 1; 
/*  478 */               i4 < i9; 
/*  481 */               i10++)
/*      */             {
/*  479 */               i3 = paramString.charAt(i4);
/*  480 */               i4++;
/*  481 */               if (i3 != 40)
/*      */                 break label126;
/*      */             }
/*      */             continue;
/*  482 */             label126: if (i3 != 41) break;
/*  483 */             i10--; if (i10 > 0) break;
/*  484 */             continue;
/*      */           }
/*      */ 
/*  488 */           if ((48 <= i3) && (i3 <= 57)) {
/*  489 */             i5 = i3 - 48;
/*  490 */             while ((i4 < i9) && ('0' <= (i3 = paramString.charAt(i4))) && (i3 <= 57)) {
/*  491 */               i5 = i5 * 10 + i3 - 48;
/*  492 */               i4++;
/*      */             }
/*  494 */             if ((i8 == 43) || ((i8 == 45) && (i != -2147483648)))
/*      */             {
/*  496 */               if (i5 < 24)
/*  497 */                 i5 *= 60;
/*      */               else
/*  499 */                 i5 = i5 % 100 + i5 / 100 * 60;
/*  500 */               if (i8 == 43)
/*  501 */                 i5 = -i5;
/*  502 */               if ((i7 != 0) && (i7 != -1))
/*      */                 break label1001;
/*  504 */               i7 = i5;
/*  505 */             } else if (i5 >= 70) {
/*  506 */               if (i != -2147483648)
/*      */                 break label1001;
/*  508 */               if ((i3 > 32) && (i3 != 44) && (i3 != 47) && (i4 < i9))
/*      */                 break label1001;
/*  510 */               i = i5;
/*      */             }
/*  513 */             else if (i3 == 58) {
/*  514 */               if (m < 0) {
/*  515 */                 m = (byte)i5; } else {
/*  516 */                 if (n >= 0) break label1001;
/*  517 */                 n = (byte)i5;
/*      */               }
/*      */             }
/*  520 */             else if (i3 == 47) {
/*  521 */               if (j < 0) {
/*  522 */                 j = (byte)(i5 - 1); } else {
/*  523 */                 if (k >= 0) break label1001;
/*  524 */                 k = (byte)i5;
/*      */               }
/*      */             } else {
/*  527 */               if ((i4 < i9) && (i3 != 44) && (i3 > 32) && (i3 != 45))
/*      */                 break label1001;
/*  529 */               if ((m >= 0) && (n < 0)) {
/*  530 */                 n = (byte)i5;
/*  531 */               } else if ((n >= 0) && (i1 < 0)) {
/*  532 */                 i1 = (byte)i5;
/*  533 */               } else if (k < 0) {
/*  534 */                 k = (byte)i5;
/*      */               } else {
/*  536 */                 if ((i != -2147483648) || (j < 0) || (k < 0)) break label1001;
/*  537 */                 i = i5;
/*      */               }
/*      */             }
/*  540 */             i8 = 0;
/*  541 */           } else if ((i3 == 47) || (i3 == 58) || (i3 == 43) || (i3 == 45)) {
/*  542 */             i8 = i3;
/*      */           } else {
/*  544 */             i10 = i4 - 1;
/*  545 */             while (i4 < i9) {
/*  546 */               i3 = paramString.charAt(i4);
/*  547 */               if (((65 > i3) || (i3 > 90)) && ((97 > i3) || (i3 > 122)))
/*      */                 break;
/*  549 */               i4++;
/*      */             }
/*  551 */             if (i4 <= i10 + 1) {
/*      */               break label1001;
/*      */             }
/*  554 */             int i11 = wtb.length;
/*      */             do { i11--; if (i11 < 0) break; }
/*  555 */             while (!wtb[i11].regionMatches(true, 0, paramString, i10, i4 - i10));
/*  556 */             int i12 = ttb[i11];
/*  557 */             if (i12 != 0) {
/*  558 */               if (i12 == 1) {
/*  559 */                 if ((m > 12) || (m < 1))
/*      */                   break label1001;
/*  561 */                 if (m < 12)
/*  562 */                   m += 12;
/*  563 */               } else if (i12 == 14) {
/*  564 */                 if ((m > 12) || (m < 1))
/*      */                   break label1001;
/*  566 */                 if (m == 12)
/*  567 */                   m = 0;
/*  568 */               } else if (i12 <= 13) {
/*  569 */                 if (j >= 0) break label1001;
/*  570 */                 j = (byte)(i12 - 2);
/*      */               }
/*      */               else
/*      */               {
/*  574 */                 i7 = i12 - 10000;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  579 */             if (i11 < 0)
/*      */               break label1001;
/*  581 */             i8 = 0;
/*      */           }
/*      */         } }
/*  584 */       label783: if ((i != -2147483648) && (j >= 0) && (k >= 0))
/*      */       {
/*  587 */         if (i < 100) {
/*  588 */           synchronized (Date.class) {
/*  589 */             if (defaultCenturyStart == 0) {
/*  590 */               defaultCenturyStart = gcal.getCalendarDate().getYear() - 80;
/*      */             }
/*      */           }
/*  593 */           i += defaultCenturyStart / 100 * 100;
/*  594 */           if (i < defaultCenturyStart) i += 100;
/*      */         }
/*  596 */         if (i1 < 0)
/*  597 */           i1 = 0;
/*  598 */         if (n < 0)
/*  599 */           n = 0;
/*  600 */         if (m < 0)
/*  601 */           m = 0;
/*  602 */         ??? = getCalendarSystem(i);
/*  603 */         if (i7 == -1) {
/*  604 */           localDate = (BaseCalendar.Date)((BaseCalendar)???).newCalendarDate(TimeZone.getDefaultRef());
/*  605 */           localDate.setDate(i, j + 1, k);
/*  606 */           localDate.setTimeOfDay(m, n, i1, 0);
/*  607 */           return ((BaseCalendar)???).getTime(localDate);
/*      */         }
/*  609 */         BaseCalendar.Date localDate = (BaseCalendar.Date)((BaseCalendar)???).newCalendarDate(null);
/*  610 */         localDate.setDate(i, j + 1, k);
/*  611 */         localDate.setTimeOfDay(m, n, i1, 0);
/*  612 */         return ((BaseCalendar)???).getTime(localDate) + i7 * 60000;
/*      */       }
/*      */     }
/*  615 */     label1001: throw new IllegalArgumentException();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getYear()
/*      */   {
/*  649 */     return normalize().getYear() - 1900;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setYear(int paramInt)
/*      */   {
/*  669 */     getCalendarDate().setNormalizedYear(paramInt + 1900);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getMonth()
/*      */   {
/*  685 */     return normalize().getMonth() - 1;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setMonth(int paramInt)
/*      */   {
/*  704 */     int i = 0;
/*  705 */     if (paramInt >= 12) {
/*  706 */       i = paramInt / 12;
/*  707 */       paramInt %= 12;
/*  708 */     } else if (paramInt < 0) {
/*  709 */       i = CalendarUtils.floorDivide(paramInt, 12);
/*  710 */       paramInt = CalendarUtils.mod(paramInt, 12);
/*      */     }
/*  712 */     BaseCalendar.Date localDate = getCalendarDate();
/*  713 */     if (i != 0) {
/*  714 */       localDate.setNormalizedYear(localDate.getNormalizedYear() + i);
/*      */     }
/*  716 */     localDate.setMonth(paramInt + 1);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getDate()
/*      */   {
/*  734 */     return normalize().getDayOfMonth();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setDate(int paramInt)
/*      */   {
/*  754 */     getCalendarDate().setDayOfMonth(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getDay()
/*      */   {
/*  773 */     return normalize().getDayOfWeek() - 1;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getHours()
/*      */   {
/*  790 */     return normalize().getHours();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setHours(int paramInt)
/*      */   {
/*  807 */     getCalendarDate().setHours(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getMinutes()
/*      */   {
/*  822 */     return normalize().getMinutes();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setMinutes(int paramInt)
/*      */   {
/*  839 */     getCalendarDate().setMinutes(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getSeconds()
/*      */   {
/*  855 */     return normalize().getSeconds();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setSeconds(int paramInt)
/*      */   {
/*  872 */     getCalendarDate().setSeconds(paramInt);
/*      */   }
/*      */ 
/*      */   public long getTime()
/*      */   {
/*  883 */     return getTimeImpl();
/*      */   }
/*      */ 
/*      */   private final long getTimeImpl() {
/*  887 */     if ((this.cdate != null) && (!this.cdate.isNormalized())) {
/*  888 */       normalize();
/*      */     }
/*  890 */     return this.fastTime;
/*      */   }
/*      */ 
/*      */   public void setTime(long paramLong)
/*      */   {
/*  900 */     this.fastTime = paramLong;
/*  901 */     this.cdate = null;
/*      */   }
/*      */ 
/*      */   public boolean before(Date paramDate)
/*      */   {
/*  915 */     return getMillisOf(this) < getMillisOf(paramDate);
/*      */   }
/*      */ 
/*      */   public boolean after(Date paramDate)
/*      */   {
/*  929 */     return getMillisOf(this) > getMillisOf(paramDate);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  948 */     return ((paramObject instanceof Date)) && (getTime() == ((Date)paramObject).getTime());
/*      */   }
/*      */ 
/*      */   static final long getMillisOf(Date paramDate)
/*      */   {
/*  956 */     if ((paramDate.cdate == null) || (paramDate.cdate.isNormalized())) {
/*  957 */       return paramDate.fastTime;
/*      */     }
/*  959 */     BaseCalendar.Date localDate = (BaseCalendar.Date)paramDate.cdate.clone();
/*  960 */     return gcal.getTime(localDate);
/*      */   }
/*      */ 
/*      */   public int compareTo(Date paramDate)
/*      */   {
/*  975 */     long l1 = getMillisOf(this);
/*  976 */     long l2 = getMillisOf(paramDate);
/*  977 */     return l1 == l2 ? 0 : l1 < l2 ? -1 : 1;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  991 */     long l = getTime();
/*  992 */     return (int)l ^ (int)(l >> 32);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1027 */     BaseCalendar.Date localDate = normalize();
/* 1028 */     StringBuilder localStringBuilder = new StringBuilder(28);
/* 1029 */     int i = localDate.getDayOfWeek();
/* 1030 */     if (i == 1) {
/* 1031 */       i = 8;
/*      */     }
/* 1033 */     convertToAbbr(localStringBuilder, wtb[i]).append(' ');
/* 1034 */     convertToAbbr(localStringBuilder, wtb[(localDate.getMonth() - 1 + 2 + 7)]).append(' ');
/* 1035 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getDayOfMonth(), 2).append(' ');
/*      */ 
/* 1037 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getHours(), 2).append(':');
/* 1038 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getMinutes(), 2).append(':');
/* 1039 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getSeconds(), 2).append(' ');
/* 1040 */     TimeZone localTimeZone = localDate.getZone();
/* 1041 */     if (localTimeZone != null)
/* 1042 */       localStringBuilder.append(localTimeZone.getDisplayName(localDate.isDaylightTime(), 0, Locale.US));
/*      */     else {
/* 1044 */       localStringBuilder.append("GMT");
/*      */     }
/* 1046 */     localStringBuilder.append(' ').append(localDate.getYear());
/* 1047 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static final StringBuilder convertToAbbr(StringBuilder paramStringBuilder, String paramString)
/*      */   {
/* 1056 */     paramStringBuilder.append(Character.toUpperCase(paramString.charAt(0)));
/* 1057 */     paramStringBuilder.append(paramString.charAt(1)).append(paramString.charAt(2));
/* 1058 */     return paramStringBuilder;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public String toLocaleString()
/*      */   {
/* 1079 */     DateFormat localDateFormat = DateFormat.getDateTimeInstance();
/* 1080 */     return localDateFormat.format(this);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public String toGMTString()
/*      */   {
/* 1117 */     long l = getTime();
/* 1118 */     BaseCalendar localBaseCalendar = getCalendarSystem(l);
/* 1119 */     BaseCalendar.Date localDate = (BaseCalendar.Date)localBaseCalendar.getCalendarDate(getTime(), (TimeZone)null);
/*      */ 
/* 1121 */     StringBuilder localStringBuilder = new StringBuilder(32);
/* 1122 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getDayOfMonth(), 1).append(' ');
/* 1123 */     convertToAbbr(localStringBuilder, wtb[(localDate.getMonth() - 1 + 2 + 7)]).append(' ');
/* 1124 */     localStringBuilder.append(localDate.getYear()).append(' ');
/* 1125 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getHours(), 2).append(':');
/* 1126 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getMinutes(), 2).append(':');
/* 1127 */     CalendarUtils.sprintf0d(localStringBuilder, localDate.getSeconds(), 2);
/* 1128 */     localStringBuilder.append(" GMT");
/* 1129 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getTimezoneOffset()
/*      */   {
/*      */     int i;
/* 1167 */     if (this.cdate == null) {
/* 1168 */       TimeZone localTimeZone = TimeZone.getDefaultRef();
/* 1169 */       if ((localTimeZone instanceof ZoneInfo))
/* 1170 */         i = ((ZoneInfo)localTimeZone).getOffsets(this.fastTime, null);
/*      */       else
/* 1172 */         i = localTimeZone.getOffset(this.fastTime);
/*      */     }
/*      */     else {
/* 1175 */       normalize();
/* 1176 */       i = this.cdate.getZoneOffset();
/*      */     }
/* 1178 */     return -i / 60000;
/*      */   }
/*      */ 
/*      */   private final BaseCalendar.Date getCalendarDate() {
/* 1182 */     if (this.cdate == null) {
/* 1183 */       BaseCalendar localBaseCalendar = getCalendarSystem(this.fastTime);
/* 1184 */       this.cdate = ((BaseCalendar.Date)localBaseCalendar.getCalendarDate(this.fastTime, TimeZone.getDefaultRef()));
/*      */     }
/*      */ 
/* 1187 */     return this.cdate;
/*      */   }
/*      */ 
/*      */   private final BaseCalendar.Date normalize() {
/* 1191 */     if (this.cdate == null) {
/* 1192 */       localObject = getCalendarSystem(this.fastTime);
/* 1193 */       this.cdate = ((BaseCalendar.Date)((BaseCalendar)localObject).getCalendarDate(this.fastTime, TimeZone.getDefaultRef()));
/*      */ 
/* 1195 */       return this.cdate;
/*      */     }
/*      */ 
/* 1200 */     if (!this.cdate.isNormalized()) {
/* 1201 */       this.cdate = normalize(this.cdate);
/*      */     }
/*      */ 
/* 1206 */     Object localObject = TimeZone.getDefaultRef();
/* 1207 */     if (localObject != this.cdate.getZone()) {
/* 1208 */       this.cdate.setZone((TimeZone)localObject);
/* 1209 */       BaseCalendar localBaseCalendar = getCalendarSystem(this.cdate);
/* 1210 */       localBaseCalendar.getCalendarDate(this.fastTime, this.cdate);
/*      */     }
/* 1212 */     return this.cdate;
/*      */   }
/*      */ 
/*      */   private final BaseCalendar.Date normalize(BaseCalendar.Date paramDate)
/*      */   {
/* 1217 */     int i = paramDate.getNormalizedYear();
/* 1218 */     int j = paramDate.getMonth();
/* 1219 */     int k = paramDate.getDayOfMonth();
/* 1220 */     int m = paramDate.getHours();
/* 1221 */     int n = paramDate.getMinutes();
/* 1222 */     int i1 = paramDate.getSeconds();
/* 1223 */     int i2 = paramDate.getMillis();
/* 1224 */     TimeZone localTimeZone = paramDate.getZone();
/*      */ 
/* 1234 */     if ((i == 1582) || (i > 280000000) || (i < -280000000)) {
/* 1235 */       if (localTimeZone == null) {
/* 1236 */         localTimeZone = TimeZone.getTimeZone("GMT");
/*      */       }
/* 1238 */       localObject = new GregorianCalendar(localTimeZone);
/* 1239 */       ((GregorianCalendar)localObject).clear();
/* 1240 */       ((GregorianCalendar)localObject).set(14, i2);
/* 1241 */       ((GregorianCalendar)localObject).set(i, j - 1, k, m, n, i1);
/* 1242 */       this.fastTime = ((GregorianCalendar)localObject).getTimeInMillis();
/* 1243 */       localBaseCalendar = getCalendarSystem(this.fastTime);
/* 1244 */       paramDate = (BaseCalendar.Date)localBaseCalendar.getCalendarDate(this.fastTime, localTimeZone);
/* 1245 */       return paramDate;
/*      */     }
/*      */ 
/* 1248 */     Object localObject = getCalendarSystem(i);
/* 1249 */     if (localObject != getCalendarSystem(paramDate)) {
/* 1250 */       paramDate = (BaseCalendar.Date)((BaseCalendar)localObject).newCalendarDate(localTimeZone);
/* 1251 */       paramDate.setNormalizedDate(i, j, k).setTimeOfDay(m, n, i1, i2);
/*      */     }
/*      */ 
/* 1254 */     this.fastTime = ((BaseCalendar)localObject).getTime(paramDate);
/*      */ 
/* 1258 */     BaseCalendar localBaseCalendar = getCalendarSystem(this.fastTime);
/* 1259 */     if (localBaseCalendar != localObject) {
/* 1260 */       paramDate = (BaseCalendar.Date)localBaseCalendar.newCalendarDate(localTimeZone);
/* 1261 */       paramDate.setNormalizedDate(i, j, k).setTimeOfDay(m, n, i1, i2);
/* 1262 */       this.fastTime = localBaseCalendar.getTime(paramDate);
/*      */     }
/* 1264 */     return paramDate;
/*      */   }
/*      */ 
/*      */   private static final BaseCalendar getCalendarSystem(int paramInt)
/*      */   {
/* 1275 */     if (paramInt >= 1582) {
/* 1276 */       return gcal;
/*      */     }
/* 1278 */     return getJulianCalendar();
/*      */   }
/*      */ 
/*      */   private static final BaseCalendar getCalendarSystem(long paramLong)
/*      */   {
/* 1285 */     if ((paramLong >= 0L) || (paramLong >= -12219292800000L - TimeZone.getDefaultRef().getOffset(paramLong)))
/*      */     {
/* 1288 */       return gcal;
/*      */     }
/* 1290 */     return getJulianCalendar();
/*      */   }
/*      */ 
/*      */   private static final BaseCalendar getCalendarSystem(BaseCalendar.Date paramDate) {
/* 1294 */     if (jcal == null) {
/* 1295 */       return gcal;
/*      */     }
/* 1297 */     if (paramDate.getEra() != null) {
/* 1298 */       return jcal;
/*      */     }
/* 1300 */     return gcal;
/*      */   }
/*      */ 
/*      */   private static final synchronized BaseCalendar getJulianCalendar() {
/* 1304 */     if (jcal == null) {
/* 1305 */       jcal = (BaseCalendar)CalendarSystem.forName("julian");
/*      */     }
/* 1307 */     return jcal;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1320 */     paramObjectOutputStream.writeLong(getTimeImpl());
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1329 */     this.fastTime = paramObjectInputStream.readLong();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Date
 * JD-Core Version:    0.6.2
 */