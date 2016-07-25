/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import sun.util.calendar.BaseCalendar;
/*      */ import sun.util.calendar.BaseCalendar.Date;
/*      */ import sun.util.calendar.CalendarSystem;
/*      */ import sun.util.calendar.CalendarUtils;
/*      */ import sun.util.calendar.Gregorian;
/*      */ 
/*      */ public class SimpleTimeZone extends TimeZone
/*      */ {
/*      */   private int startMonth;
/*      */   private int startDay;
/*      */   private int startDayOfWeek;
/*      */   private int startTime;
/*      */   private int startTimeMode;
/*      */   private int endMonth;
/*      */   private int endDay;
/*      */   private int endDayOfWeek;
/*      */   private int endTime;
/*      */   private int endTimeMode;
/*      */   private int startYear;
/*      */   private int rawOffset;
/* 1114 */   private boolean useDaylight = false;
/*      */   private static final int millisPerHour = 3600000;
/*      */   private static final int millisPerDay = 86400000;
/* 1127 */   private final byte[] monthLength = staticMonthLength;
/* 1128 */   private static final byte[] staticMonthLength = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/* 1129 */   private static final byte[] staticLeapMonthLength = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*      */   private int startMode;
/*      */   private int endMode;
/*      */   private int dstSavings;
/* 1199 */   private static final Gregorian gcal = CalendarSystem.getGregorianCalendar();
/*      */   private transient long cacheYear;
/*      */   private transient long cacheStart;
/*      */   private transient long cacheEnd;
/*      */   private static final int DOM_MODE = 1;
/*      */   private static final int DOW_IN_MONTH_MODE = 2;
/*      */   private static final int DOW_GE_DOM_MODE = 3;
/*      */   private static final int DOW_LE_DOM_MODE = 4;
/*      */   public static final int WALL_TIME = 0;
/*      */   public static final int STANDARD_TIME = 1;
/*      */   public static final int UTC_TIME = 2;
/*      */   static final long serialVersionUID = -403250971215465050L;
/*      */   static final int currentSerialVersion = 2;
/* 1279 */   private int serialVersionOnStream = 2;
/*      */ 
/*      */   public SimpleTimeZone(int paramInt, String paramString)
/*      */   {
/*  160 */     this.rawOffset = paramInt;
/*  161 */     setID(paramString);
/*  162 */     this.dstSavings = 3600000;
/*      */   }
/*      */ 
/*      */   public SimpleTimeZone(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
/*      */   {
/*  218 */     this(paramInt1, paramString, paramInt2, paramInt3, paramInt4, paramInt5, 0, paramInt6, paramInt7, paramInt8, paramInt9, 0, 3600000);
/*      */   }
/*      */ 
/*      */   public SimpleTimeZone(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10)
/*      */   {
/*  277 */     this(paramInt1, paramString, paramInt2, paramInt3, paramInt4, paramInt5, 0, paramInt6, paramInt7, paramInt8, paramInt9, 0, paramInt10);
/*      */   }
/*      */ 
/*      */   public SimpleTimeZone(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12)
/*      */   {
/*  335 */     setID(paramString);
/*  336 */     this.rawOffset = paramInt1;
/*  337 */     this.startMonth = paramInt2;
/*  338 */     this.startDay = paramInt3;
/*  339 */     this.startDayOfWeek = paramInt4;
/*  340 */     this.startTime = paramInt5;
/*  341 */     this.startTimeMode = paramInt6;
/*  342 */     this.endMonth = paramInt7;
/*  343 */     this.endDay = paramInt8;
/*  344 */     this.endDayOfWeek = paramInt9;
/*  345 */     this.endTime = paramInt10;
/*  346 */     this.endTimeMode = paramInt11;
/*  347 */     this.dstSavings = paramInt12;
/*      */ 
/*  350 */     decodeRules();
/*  351 */     if (paramInt12 <= 0)
/*  352 */       throw new IllegalArgumentException("Illegal daylight saving value: " + paramInt12);
/*      */   }
/*      */ 
/*      */   public void setStartYear(int paramInt)
/*      */   {
/*  363 */     this.startYear = paramInt;
/*  364 */     invalidateCache();
/*      */   }
/*      */ 
/*      */   public void setStartRule(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  387 */     this.startMonth = paramInt1;
/*  388 */     this.startDay = paramInt2;
/*  389 */     this.startDayOfWeek = paramInt3;
/*  390 */     this.startTime = paramInt4;
/*  391 */     this.startTimeMode = 0;
/*  392 */     decodeStartRule();
/*  393 */     invalidateCache();
/*      */   }
/*      */ 
/*      */   public void setStartRule(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  413 */     setStartRule(paramInt1, paramInt2, 0, paramInt3);
/*      */   }
/*      */ 
/*      */   public void setStartRule(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  439 */     if (paramBoolean)
/*  440 */       setStartRule(paramInt1, paramInt2, -paramInt3, paramInt4);
/*      */     else
/*  442 */       setStartRule(paramInt1, -paramInt2, -paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void setEndRule(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  468 */     this.endMonth = paramInt1;
/*  469 */     this.endDay = paramInt2;
/*  470 */     this.endDayOfWeek = paramInt3;
/*  471 */     this.endTime = paramInt4;
/*  472 */     this.endTimeMode = 0;
/*  473 */     decodeEndRule();
/*  474 */     invalidateCache();
/*      */   }
/*      */ 
/*      */   public void setEndRule(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  495 */     setEndRule(paramInt1, paramInt2, 0, paramInt3);
/*      */   }
/*      */ 
/*      */   public void setEndRule(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  520 */     if (paramBoolean)
/*  521 */       setEndRule(paramInt1, paramInt2, -paramInt3, paramInt4);
/*      */     else
/*  523 */       setEndRule(paramInt1, -paramInt2, -paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public int getOffset(long paramLong)
/*      */   {
/*  539 */     return getOffsets(paramLong, null);
/*      */   }
/*      */ 
/*      */   int getOffsets(long paramLong, int[] paramArrayOfInt)
/*      */   {
/*  546 */     int i = this.rawOffset;
/*      */ 
/*  549 */     if (this.useDaylight) {
/*  550 */       synchronized (this) {
/*  551 */         if ((this.cacheStart != 0L) && 
/*  552 */           (paramLong >= this.cacheStart) && (paramLong < this.cacheEnd)) {
/*  553 */           i += this.dstSavings;
/*  554 */           break label165;
/*      */         }
/*      */       }
/*      */ 
/*  558 */       ??? = paramLong >= -12219292800000L ? gcal : (BaseCalendar)CalendarSystem.forName("julian");
/*      */ 
/*  560 */       BaseCalendar.Date localDate = (BaseCalendar.Date)((BaseCalendar)???).newCalendarDate(TimeZone.NO_TIMEZONE);
/*      */ 
/*  562 */       ((BaseCalendar)???).getCalendarDate(paramLong + this.rawOffset, localDate);
/*  563 */       int j = localDate.getNormalizedYear();
/*  564 */       if (j >= this.startYear)
/*      */       {
/*  566 */         localDate.setTimeOfDay(0, 0, 0, 0);
/*  567 */         i = getOffset((BaseCalendar)???, localDate, j, paramLong);
/*      */       }
/*      */     }
/*      */ 
/*  571 */     label165: if (paramArrayOfInt != null) {
/*  572 */       paramArrayOfInt[0] = this.rawOffset;
/*  573 */       paramArrayOfInt[1] = (i - this.rawOffset);
/*      */     }
/*  575 */     return i;
/*      */   }
/*      */ 
/*      */   public int getOffset(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  607 */     if ((paramInt1 != 1) && (paramInt1 != 0)) {
/*  608 */       throw new IllegalArgumentException("Illegal era " + paramInt1);
/*      */     }
/*      */ 
/*  611 */     int i = paramInt2;
/*  612 */     if (paramInt1 == 0)
/*      */     {
/*  614 */       i = 1 - i;
/*      */     }
/*      */ 
/*  622 */     if (i >= 292278994)
/*  623 */       i = 2800 + i % 2800;
/*  624 */     else if (i <= -292269054)
/*      */     {
/*  628 */       i = (int)CalendarUtils.mod(i, 28L);
/*      */     }
/*      */ 
/*  632 */     int j = paramInt3 + 1;
/*      */ 
/*  635 */     Object localObject = gcal;
/*  636 */     BaseCalendar.Date localDate = (BaseCalendar.Date)((BaseCalendar)localObject).newCalendarDate(TimeZone.NO_TIMEZONE);
/*  637 */     localDate.setDate(i, j, paramInt4);
/*  638 */     long l = ((BaseCalendar)localObject).getTime(localDate);
/*  639 */     l += paramInt6 - this.rawOffset;
/*      */ 
/*  647 */     if (l < -12219292800000L) {
/*  648 */       localObject = (BaseCalendar)CalendarSystem.forName("julian");
/*  649 */       localDate = (BaseCalendar.Date)((BaseCalendar)localObject).newCalendarDate(TimeZone.NO_TIMEZONE);
/*  650 */       localDate.setNormalizedDate(i, j, paramInt4);
/*  651 */       l = ((BaseCalendar)localObject).getTime(localDate) + paramInt6 - this.rawOffset;
/*      */     }
/*      */ 
/*  654 */     if ((localDate.getNormalizedYear() != i) || (localDate.getMonth() != j) || (localDate.getDayOfMonth() != paramInt4) || (paramInt5 < 1) || (paramInt5 > 7) || (paramInt6 < 0) || (paramInt6 >= 86400000))
/*      */     {
/*  662 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*  665 */     if ((!this.useDaylight) || (paramInt2 < this.startYear) || (paramInt1 != 1)) {
/*  666 */       return this.rawOffset;
/*      */     }
/*      */ 
/*  669 */     return getOffset((BaseCalendar)localObject, localDate, i, l);
/*      */   }
/*      */ 
/*      */   private int getOffset(BaseCalendar paramBaseCalendar, BaseCalendar.Date paramDate, int paramInt, long paramLong) {
/*  673 */     synchronized (this) {
/*  674 */       if (this.cacheStart != 0L) {
/*  675 */         if ((paramLong >= this.cacheStart) && (paramLong < this.cacheEnd)) {
/*  676 */           return this.rawOffset + this.dstSavings;
/*      */         }
/*  678 */         if (paramInt == this.cacheYear) {
/*  679 */           return this.rawOffset;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  684 */     long l1 = getStart(paramBaseCalendar, paramDate, paramInt);
/*  685 */     long l2 = getEnd(paramBaseCalendar, paramDate, paramInt);
/*  686 */     int i = this.rawOffset;
/*  687 */     if (l1 <= l2) {
/*  688 */       if ((paramLong >= l1) && (paramLong < l2)) {
/*  689 */         i += this.dstSavings;
/*      */       }
/*  691 */       synchronized (this) {
/*  692 */         this.cacheYear = paramInt;
/*  693 */         this.cacheStart = l1;
/*  694 */         this.cacheEnd = l2;
/*      */       }
/*      */     } else {
/*  697 */       if (paramLong < l2)
/*      */       {
/*  700 */         l1 = getStart(paramBaseCalendar, paramDate, paramInt - 1);
/*  701 */         if (paramLong >= l1)
/*  702 */           i += this.dstSavings;
/*      */       }
/*  704 */       else if (paramLong >= l1)
/*      */       {
/*  707 */         l2 = getEnd(paramBaseCalendar, paramDate, paramInt + 1);
/*  708 */         if (paramLong < l2) {
/*  709 */           i += this.dstSavings;
/*      */         }
/*      */       }
/*  712 */       if (l1 <= l2) {
/*  713 */         synchronized (this)
/*      */         {
/*  715 */           this.cacheYear = (this.startYear - 1L);
/*  716 */           this.cacheStart = l1;
/*  717 */           this.cacheEnd = l2;
/*      */         }
/*      */       }
/*      */     }
/*  721 */     return i;
/*      */   }
/*      */ 
/*      */   private long getStart(BaseCalendar paramBaseCalendar, BaseCalendar.Date paramDate, int paramInt) {
/*  725 */     int i = this.startTime;
/*  726 */     if (this.startTimeMode != 2) {
/*  727 */       i -= this.rawOffset;
/*      */     }
/*  729 */     return getTransition(paramBaseCalendar, paramDate, this.startMode, paramInt, this.startMonth, this.startDay, this.startDayOfWeek, i);
/*      */   }
/*      */ 
/*      */   private long getEnd(BaseCalendar paramBaseCalendar, BaseCalendar.Date paramDate, int paramInt)
/*      */   {
/*  734 */     int i = this.endTime;
/*  735 */     if (this.endTimeMode != 2) {
/*  736 */       i -= this.rawOffset;
/*      */     }
/*  738 */     if (this.endTimeMode == 0) {
/*  739 */       i -= this.dstSavings;
/*      */     }
/*  741 */     return getTransition(paramBaseCalendar, paramDate, this.endMode, paramInt, this.endMonth, this.endDay, this.endDayOfWeek, i);
/*      */   }
/*      */ 
/*      */   private long getTransition(BaseCalendar paramBaseCalendar, BaseCalendar.Date paramDate, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  748 */     paramDate.setNormalizedYear(paramInt2);
/*  749 */     paramDate.setMonth(paramInt3 + 1);
/*  750 */     switch (paramInt1) {
/*      */     case 1:
/*  752 */       paramDate.setDayOfMonth(paramInt4);
/*  753 */       break;
/*      */     case 2:
/*  756 */       paramDate.setDayOfMonth(1);
/*  757 */       if (paramInt4 < 0) {
/*  758 */         paramDate.setDayOfMonth(paramBaseCalendar.getMonthLength(paramDate));
/*      */       }
/*  760 */       paramDate = (BaseCalendar.Date)paramBaseCalendar.getNthDayOfWeek(paramInt4, paramInt5, paramDate);
/*  761 */       break;
/*      */     case 3:
/*  764 */       paramDate.setDayOfMonth(paramInt4);
/*  765 */       paramDate = (BaseCalendar.Date)paramBaseCalendar.getNthDayOfWeek(1, paramInt5, paramDate);
/*  766 */       break;
/*      */     case 4:
/*  769 */       paramDate.setDayOfMonth(paramInt4);
/*  770 */       paramDate = (BaseCalendar.Date)paramBaseCalendar.getNthDayOfWeek(-1, paramInt5, paramDate);
/*      */     }
/*      */ 
/*  773 */     return paramBaseCalendar.getTime(paramDate) + paramInt6;
/*      */   }
/*      */ 
/*      */   public int getRawOffset()
/*      */   {
/*  785 */     return this.rawOffset;
/*      */   }
/*      */ 
/*      */   public void setRawOffset(int paramInt)
/*      */   {
/*  795 */     this.rawOffset = paramInt;
/*      */   }
/*      */ 
/*      */   public void setDSTSavings(int paramInt)
/*      */   {
/*  808 */     if (paramInt <= 0) {
/*  809 */       throw new IllegalArgumentException("Illegal daylight saving value: " + paramInt);
/*      */     }
/*      */ 
/*  812 */     this.dstSavings = paramInt;
/*      */   }
/*      */ 
/*      */   public int getDSTSavings()
/*      */   {
/*  828 */     return this.useDaylight ? this.dstSavings : 0;
/*      */   }
/*      */ 
/*      */   public boolean useDaylightTime()
/*      */   {
/*  838 */     return this.useDaylight;
/*      */   }
/*      */ 
/*      */   public boolean observesDaylightTime()
/*      */   {
/*  852 */     return useDaylightTime();
/*      */   }
/*      */ 
/*      */   public boolean inDaylightTime(Date paramDate)
/*      */   {
/*  862 */     return getOffset(paramDate.getTime()) != this.rawOffset;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  871 */     return super.clone();
/*      */   }
/*      */ 
/*      */   public synchronized int hashCode()
/*      */   {
/*  880 */     return this.startMonth ^ this.startDay ^ this.startDayOfWeek ^ this.startTime ^ this.endMonth ^ this.endDay ^ this.endDayOfWeek ^ this.endTime ^ this.rawOffset;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  893 */     if (this == paramObject) {
/*  894 */       return true;
/*      */     }
/*  896 */     if (!(paramObject instanceof SimpleTimeZone)) {
/*  897 */       return false;
/*      */     }
/*      */ 
/*  900 */     SimpleTimeZone localSimpleTimeZone = (SimpleTimeZone)paramObject;
/*      */ 
/*  902 */     return (getID().equals(localSimpleTimeZone.getID())) && (hasSameRules(localSimpleTimeZone));
/*      */   }
/*      */ 
/*      */   public boolean hasSameRules(TimeZone paramTimeZone)
/*      */   {
/*  914 */     if (this == paramTimeZone) {
/*  915 */       return true;
/*      */     }
/*  917 */     if (!(paramTimeZone instanceof SimpleTimeZone)) {
/*  918 */       return false;
/*      */     }
/*  920 */     SimpleTimeZone localSimpleTimeZone = (SimpleTimeZone)paramTimeZone;
/*  921 */     return (this.rawOffset == localSimpleTimeZone.rawOffset) && (this.useDaylight == localSimpleTimeZone.useDaylight) && ((!this.useDaylight) || ((this.dstSavings == localSimpleTimeZone.dstSavings) && (this.startMode == localSimpleTimeZone.startMode) && (this.startMonth == localSimpleTimeZone.startMonth) && (this.startDay == localSimpleTimeZone.startDay) && (this.startDayOfWeek == localSimpleTimeZone.startDayOfWeek) && (this.startTime == localSimpleTimeZone.startTime) && (this.startTimeMode == localSimpleTimeZone.startTimeMode) && (this.endMode == localSimpleTimeZone.endMode) && (this.endMonth == localSimpleTimeZone.endMonth) && (this.endDay == localSimpleTimeZone.endDay) && (this.endDayOfWeek == localSimpleTimeZone.endDayOfWeek) && (this.endTime == localSimpleTimeZone.endTime) && (this.endTimeMode == localSimpleTimeZone.endTimeMode) && (this.startYear == localSimpleTimeZone.startYear)));
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  946 */     return getClass().getName() + "[id=" + getID() + ",offset=" + this.rawOffset + ",dstSavings=" + this.dstSavings + ",useDaylight=" + this.useDaylight + ",startYear=" + this.startYear + ",startMode=" + this.startMode + ",startMonth=" + this.startMonth + ",startDay=" + this.startDay + ",startDayOfWeek=" + this.startDayOfWeek + ",startTime=" + this.startTime + ",startTimeMode=" + this.startTimeMode + ",endMode=" + this.endMode + ",endMonth=" + this.endMonth + ",endDay=" + this.endDay + ",endDayOfWeek=" + this.endDayOfWeek + ",endTime=" + this.endTime + ",endTimeMode=" + this.endTimeMode + ']';
/*      */   }
/*      */ 
/*      */   private synchronized void invalidateCache()
/*      */   {
/* 1282 */     this.cacheYear = (this.startYear - 1);
/* 1283 */     this.cacheStart = (this.cacheEnd = 0L);
/*      */   }
/*      */ 
/*      */   private void decodeRules()
/*      */   {
/* 1351 */     decodeStartRule();
/* 1352 */     decodeEndRule();
/*      */   }
/*      */ 
/*      */   private void decodeStartRule()
/*      */   {
/* 1380 */     this.useDaylight = ((this.startDay != 0) && (this.endDay != 0));
/* 1381 */     if (this.startDay != 0) {
/* 1382 */       if ((this.startMonth < 0) || (this.startMonth > 11)) {
/* 1383 */         throw new IllegalArgumentException("Illegal start month " + this.startMonth);
/*      */       }
/*      */ 
/* 1386 */       if ((this.startTime < 0) || (this.startTime > 86400000)) {
/* 1387 */         throw new IllegalArgumentException("Illegal start time " + this.startTime);
/*      */       }
/*      */ 
/* 1390 */       if (this.startDayOfWeek == 0) {
/* 1391 */         this.startMode = 1;
/*      */       } else {
/* 1393 */         if (this.startDayOfWeek > 0) {
/* 1394 */           this.startMode = 2;
/*      */         } else {
/* 1396 */           this.startDayOfWeek = (-this.startDayOfWeek);
/* 1397 */           if (this.startDay > 0) {
/* 1398 */             this.startMode = 3;
/*      */           } else {
/* 1400 */             this.startDay = (-this.startDay);
/* 1401 */             this.startMode = 4;
/*      */           }
/*      */         }
/* 1404 */         if (this.startDayOfWeek > 7) {
/* 1405 */           throw new IllegalArgumentException("Illegal start day of week " + this.startDayOfWeek);
/*      */         }
/*      */       }
/*      */ 
/* 1409 */       if (this.startMode == 2) {
/* 1410 */         if ((this.startDay < -5) || (this.startDay > 5)) {
/* 1411 */           throw new IllegalArgumentException("Illegal start day of week in month " + this.startDay);
/*      */         }
/*      */       }
/* 1414 */       else if ((this.startDay < 1) || (this.startDay > staticMonthLength[this.startMonth]))
/* 1415 */         throw new IllegalArgumentException("Illegal start day " + this.startDay);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void decodeEndRule()
/*      */   {
/* 1427 */     this.useDaylight = ((this.startDay != 0) && (this.endDay != 0));
/* 1428 */     if (this.endDay != 0) {
/* 1429 */       if ((this.endMonth < 0) || (this.endMonth > 11)) {
/* 1430 */         throw new IllegalArgumentException("Illegal end month " + this.endMonth);
/*      */       }
/*      */ 
/* 1433 */       if ((this.endTime < 0) || (this.endTime > 86400000)) {
/* 1434 */         throw new IllegalArgumentException("Illegal end time " + this.endTime);
/*      */       }
/*      */ 
/* 1437 */       if (this.endDayOfWeek == 0) {
/* 1438 */         this.endMode = 1;
/*      */       } else {
/* 1440 */         if (this.endDayOfWeek > 0) {
/* 1441 */           this.endMode = 2;
/*      */         } else {
/* 1443 */           this.endDayOfWeek = (-this.endDayOfWeek);
/* 1444 */           if (this.endDay > 0) {
/* 1445 */             this.endMode = 3;
/*      */           } else {
/* 1447 */             this.endDay = (-this.endDay);
/* 1448 */             this.endMode = 4;
/*      */           }
/*      */         }
/* 1451 */         if (this.endDayOfWeek > 7) {
/* 1452 */           throw new IllegalArgumentException("Illegal end day of week " + this.endDayOfWeek);
/*      */         }
/*      */       }
/*      */ 
/* 1456 */       if (this.endMode == 2) {
/* 1457 */         if ((this.endDay < -5) || (this.endDay > 5)) {
/* 1458 */           throw new IllegalArgumentException("Illegal end day of week in month " + this.endDay);
/*      */         }
/*      */       }
/* 1461 */       else if ((this.endDay < 1) || (this.endDay > staticMonthLength[this.endMonth]))
/* 1462 */         throw new IllegalArgumentException("Illegal end day " + this.endDay);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void makeRulesCompatible()
/*      */   {
/* 1479 */     switch (this.startMode) {
/*      */     case 1:
/* 1481 */       this.startDay = (1 + this.startDay / 7);
/* 1482 */       this.startDayOfWeek = 1;
/* 1483 */       break;
/*      */     case 3:
/* 1488 */       if (this.startDay != 1)
/* 1489 */         this.startDay = (1 + this.startDay / 7); break;
/*      */     case 4:
/* 1494 */       if (this.startDay >= 30)
/* 1495 */         this.startDay = -1;
/*      */       else {
/* 1497 */         this.startDay = (1 + this.startDay / 7);
/*      */       }
/*      */       break;
/*      */     case 2:
/*      */     }
/* 1502 */     switch (this.endMode) {
/*      */     case 1:
/* 1504 */       this.endDay = (1 + this.endDay / 7);
/* 1505 */       this.endDayOfWeek = 1;
/* 1506 */       break;
/*      */     case 3:
/* 1511 */       if (this.endDay != 1)
/* 1512 */         this.endDay = (1 + this.endDay / 7); break;
/*      */     case 4:
/* 1517 */       if (this.endDay >= 30)
/* 1518 */         this.endDay = -1;
/*      */       else {
/* 1520 */         this.endDay = (1 + this.endDay / 7);
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 2:
/*      */     }
/*      */ 
/* 1535 */     switch (this.startTimeMode) {
/*      */     case 2:
/* 1537 */       this.startTime += this.rawOffset;
/*      */     }
/*      */ 
/* 1540 */     while (this.startTime < 0) {
/* 1541 */       this.startTime += 86400000;
/* 1542 */       this.startDayOfWeek = (1 + (this.startDayOfWeek + 5) % 7);
/*      */     }
/* 1544 */     while (this.startTime >= 86400000) {
/* 1545 */       this.startTime -= 86400000;
/* 1546 */       this.startDayOfWeek = (1 + this.startDayOfWeek % 7);
/*      */     }
/*      */ 
/* 1549 */     switch (this.endTimeMode) {
/*      */     case 2:
/* 1551 */       this.endTime += this.rawOffset + this.dstSavings;
/* 1552 */       break;
/*      */     case 1:
/* 1554 */       this.endTime += this.dstSavings;
/*      */     }
/* 1556 */     while (this.endTime < 0) {
/* 1557 */       this.endTime += 86400000;
/* 1558 */       this.endDayOfWeek = (1 + (this.endDayOfWeek + 5) % 7);
/*      */     }
/* 1560 */     while (this.endTime >= 86400000) {
/* 1561 */       this.endTime -= 86400000;
/* 1562 */       this.endDayOfWeek = (1 + this.endDayOfWeek % 7);
/*      */     }
/*      */   }
/*      */ 
/*      */   private byte[] packRules()
/*      */   {
/* 1572 */     byte[] arrayOfByte = new byte[6];
/* 1573 */     arrayOfByte[0] = ((byte)this.startDay);
/* 1574 */     arrayOfByte[1] = ((byte)this.startDayOfWeek);
/* 1575 */     arrayOfByte[2] = ((byte)this.endDay);
/* 1576 */     arrayOfByte[3] = ((byte)this.endDayOfWeek);
/*      */ 
/* 1579 */     arrayOfByte[4] = ((byte)this.startTimeMode);
/* 1580 */     arrayOfByte[5] = ((byte)this.endTimeMode);
/*      */ 
/* 1582 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private void unpackRules(byte[] paramArrayOfByte)
/*      */   {
/* 1591 */     this.startDay = paramArrayOfByte[0];
/* 1592 */     this.startDayOfWeek = paramArrayOfByte[1];
/* 1593 */     this.endDay = paramArrayOfByte[2];
/* 1594 */     this.endDayOfWeek = paramArrayOfByte[3];
/*      */ 
/* 1597 */     if (paramArrayOfByte.length >= 6) {
/* 1598 */       this.startTimeMode = paramArrayOfByte[4];
/* 1599 */       this.endTimeMode = paramArrayOfByte[5];
/*      */     }
/*      */   }
/*      */ 
/*      */   private int[] packTimes()
/*      */   {
/* 1608 */     int[] arrayOfInt = new int[2];
/* 1609 */     arrayOfInt[0] = this.startTime;
/* 1610 */     arrayOfInt[1] = this.endTime;
/* 1611 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private void unpackTimes(int[] paramArrayOfInt)
/*      */   {
/* 1619 */     this.startTime = paramArrayOfInt[0];
/* 1620 */     this.endTime = paramArrayOfInt[1];
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1643 */     byte[] arrayOfByte = packRules();
/* 1644 */     int[] arrayOfInt = packTimes();
/*      */ 
/* 1647 */     makeRulesCompatible();
/*      */ 
/* 1650 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1653 */     paramObjectOutputStream.writeInt(arrayOfByte.length);
/* 1654 */     paramObjectOutputStream.write(arrayOfByte);
/* 1655 */     paramObjectOutputStream.writeObject(arrayOfInt);
/*      */ 
/* 1659 */     unpackRules(arrayOfByte);
/* 1660 */     unpackTimes(arrayOfInt);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1672 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1674 */     if (this.serialVersionOnStream < 1)
/*      */     {
/* 1678 */       if (this.startDayOfWeek == 0) {
/* 1679 */         this.startDayOfWeek = 1;
/*      */       }
/* 1681 */       if (this.endDayOfWeek == 0) {
/* 1682 */         this.endDayOfWeek = 1;
/*      */       }
/*      */ 
/* 1687 */       this.startMode = (this.endMode = 2);
/* 1688 */       this.dstSavings = 3600000;
/*      */     }
/*      */     else
/*      */     {
/* 1693 */       int i = paramObjectInputStream.readInt();
/* 1694 */       byte[] arrayOfByte = new byte[i];
/* 1695 */       paramObjectInputStream.readFully(arrayOfByte);
/* 1696 */       unpackRules(arrayOfByte);
/*      */     }
/*      */ 
/* 1699 */     if (this.serialVersionOnStream >= 2) {
/* 1700 */       int[] arrayOfInt = (int[])paramObjectInputStream.readObject();
/* 1701 */       unpackTimes(arrayOfInt);
/*      */     }
/*      */ 
/* 1704 */     this.serialVersionOnStream = 2;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.SimpleTimeZone
 * JD-Core Version:    0.6.2
 */