/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import sun.util.calendar.BaseCalendar.Date;
/*      */ import sun.util.calendar.CalendarDate;
/*      */ import sun.util.calendar.CalendarSystem;
/*      */ import sun.util.calendar.CalendarUtils;
/*      */ import sun.util.calendar.Era;
/*      */ import sun.util.calendar.Gregorian;
/*      */ import sun.util.calendar.Gregorian.Date;
/*      */ import sun.util.calendar.LocalGregorianCalendar;
/*      */ import sun.util.calendar.LocalGregorianCalendar.Date;
/*      */ import sun.util.calendar.ZoneInfo;
/*      */ import sun.util.resources.LocaleData;
/*      */ 
/*      */ class JapaneseImperialCalendar extends Calendar
/*      */ {
/*      */   public static final int BEFORE_MEIJI = 0;
/*      */   public static final int MEIJI = 1;
/*      */   public static final int TAISHO = 2;
/*      */   public static final int SHOWA = 3;
/*      */   public static final int HEISEI = 4;
/*      */   private static final int EPOCH_OFFSET = 719163;
/*      */   private static final int EPOCH_YEAR = 1970;
/*      */   private static final int ONE_SECOND = 1000;
/*      */   private static final int ONE_MINUTE = 60000;
/*      */   private static final int ONE_HOUR = 3600000;
/*      */   private static final long ONE_DAY = 86400000L;
/*      */   private static final long ONE_WEEK = 604800000L;
/*      */   private static final LocalGregorianCalendar jcal;
/*      */   private static final Gregorian gcal;
/*      */   private static final Era BEFORE_MEIJI_ERA;
/*      */   private static final Era[] eras;
/*      */   private static final long[] sinceFixedDates;
/*      */   static final int[] MIN_VALUES;
/*      */   static final int[] LEAST_MAX_VALUES;
/*      */   static final int[] MAX_VALUES;
/*      */   private static final long serialVersionUID = -3364572813905467929L;
/*      */   private transient LocalGregorianCalendar.Date jdate;
/*      */   private transient int[] zoneOffsets;
/*      */   private transient int[] originalFields;
/* 1557 */   private transient long cachedFixedDate = -9223372036854775808L;
/*      */ 
/*      */   public JapaneseImperialCalendar(TimeZone paramTimeZone, Locale paramLocale)
/*      */   {
/*  298 */     super(paramTimeZone, paramLocale);
/*  299 */     this.jdate = jcal.newCalendarDate(paramTimeZone);
/*  300 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  317 */     return ((paramObject instanceof JapaneseImperialCalendar)) && (super.equals(paramObject));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  326 */     return super.hashCode() ^ this.jdate.hashCode();
/*      */   }
/*      */ 
/*      */   public void add(int paramInt1, int paramInt2)
/*      */   {
/*  360 */     if (paramInt2 == 0) {
/*  361 */       return;
/*      */     }
/*      */ 
/*  364 */     if ((paramInt1 < 0) || (paramInt1 >= 15)) {
/*  365 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*  369 */     complete();
/*      */     LocalGregorianCalendar.Date localDate;
/*  371 */     if (paramInt1 == 1) {
/*  372 */       localDate = (LocalGregorianCalendar.Date)this.jdate.clone();
/*  373 */       localDate.addYear(paramInt2);
/*  374 */       pinDayOfMonth(localDate);
/*  375 */       set(0, getEraIndex(localDate));
/*  376 */       set(1, localDate.getYear());
/*  377 */       set(2, localDate.getMonth() - 1);
/*  378 */       set(5, localDate.getDayOfMonth());
/*  379 */     } else if (paramInt1 == 2) {
/*  380 */       localDate = (LocalGregorianCalendar.Date)this.jdate.clone();
/*  381 */       localDate.addMonth(paramInt2);
/*  382 */       pinDayOfMonth(localDate);
/*  383 */       set(0, getEraIndex(localDate));
/*  384 */       set(1, localDate.getYear());
/*  385 */       set(2, localDate.getMonth() - 1);
/*  386 */       set(5, localDate.getDayOfMonth());
/*  387 */     } else if (paramInt1 == 0) {
/*  388 */       int i = internalGet(0) + paramInt2;
/*  389 */       if (i < 0)
/*  390 */         i = 0;
/*  391 */       else if (i > eras.length - 1) {
/*  392 */         i = eras.length - 1;
/*      */       }
/*  394 */       set(0, i);
/*      */     } else {
/*  396 */       long l1 = paramInt2;
/*  397 */       long l2 = 0L;
/*  398 */       switch (paramInt1)
/*      */       {
/*      */       case 10:
/*      */       case 11:
/*  403 */         l1 *= 3600000L;
/*  404 */         break;
/*      */       case 12:
/*  407 */         l1 *= 60000L;
/*  408 */         break;
/*      */       case 13:
/*  411 */         l1 *= 1000L;
/*  412 */         break;
/*      */       case 14:
/*  415 */         break;
/*      */       case 3:
/*      */       case 4:
/*      */       case 8:
/*  423 */         l1 *= 7L;
/*  424 */         break;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  429 */         break;
/*      */       case 9:
/*  434 */         l1 = paramInt2 / 2;
/*  435 */         l2 = 12 * (paramInt2 % 2);
/*      */       }
/*      */ 
/*  441 */       if (paramInt1 >= 10) {
/*  442 */         setTimeInMillis(this.time + l1);
/*  443 */         return;
/*      */       }
/*      */ 
/*  452 */       long l3 = this.cachedFixedDate;
/*  453 */       l2 += internalGet(11);
/*  454 */       l2 *= 60L;
/*  455 */       l2 += internalGet(12);
/*  456 */       l2 *= 60L;
/*  457 */       l2 += internalGet(13);
/*  458 */       l2 *= 1000L;
/*  459 */       l2 += internalGet(14);
/*  460 */       if (l2 >= 86400000L) {
/*  461 */         l3 += 1L;
/*  462 */         l2 -= 86400000L;
/*  463 */       } else if (l2 < 0L) {
/*  464 */         l3 -= 1L;
/*  465 */         l2 += 86400000L;
/*      */       }
/*      */ 
/*  468 */       l3 += l1;
/*  469 */       int j = internalGet(15) + internalGet(16);
/*  470 */       setTimeInMillis((l3 - 719163L) * 86400000L + l2 - j);
/*  471 */       j -= internalGet(15) + internalGet(16);
/*      */ 
/*  473 */       if (j != 0) {
/*  474 */         setTimeInMillis(this.time + j);
/*  475 */         long l4 = this.cachedFixedDate;
/*      */ 
/*  478 */         if (l4 != l3)
/*  479 */           setTimeInMillis(this.time - j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void roll(int paramInt, boolean paramBoolean)
/*      */   {
/*  486 */     roll(paramInt, paramBoolean ? 1 : -1);
/*      */   }
/*      */ 
/*      */   public void roll(int paramInt1, int paramInt2)
/*      */   {
/*  512 */     if (paramInt2 == 0) {
/*  513 */       return;
/*      */     }
/*      */ 
/*  516 */     if ((paramInt1 < 0) || (paramInt1 >= 15)) {
/*  517 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*  521 */     complete();
/*      */ 
/*  523 */     int i = getMinimum(paramInt1);
/*  524 */     int j = getMaximum(paramInt1);
/*      */     int k;
/*      */     int i8;
/*      */     int i6;
/*      */     int i2;
/*      */     long l9;
/*      */     LocalGregorianCalendar.Date localDate7;
/*      */     int i15;
/*      */     int i7;
/*      */     LocalGregorianCalendar.Date localDate4;
/*      */     int m;
/*  526 */     switch (paramInt1)
/*      */     {
/*      */     case 0:
/*      */     case 9:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*  536 */       break;
/*      */     case 10:
/*      */     case 11:
/*  541 */       k = j + 1;
/*  542 */       int n = internalGet(paramInt1);
/*  543 */       int i5 = (n + paramInt2) % k;
/*  544 */       if (i5 < 0) {
/*  545 */         i5 += k;
/*      */       }
/*  547 */       this.time += 3600000 * (i5 - n);
/*      */ 
/*  553 */       LocalGregorianCalendar.Date localDate3 = jcal.getCalendarDate(this.time, getZone());
/*  554 */       if (internalGet(5) != localDate3.getDayOfMonth()) {
/*  555 */         localDate3.setEra(this.jdate.getEra());
/*  556 */         localDate3.setDate(internalGet(1), internalGet(2) + 1, internalGet(5));
/*      */ 
/*  559 */         if (paramInt1 == 10) {
/*  560 */           assert (internalGet(9) == 1);
/*  561 */           localDate3.addHours(12);
/*      */         }
/*  563 */         this.time = jcal.getTime(localDate3);
/*      */       }
/*  565 */       int i10 = localDate3.getHours();
/*  566 */       internalSet(paramInt1, i10 % k);
/*  567 */       if (paramInt1 == 10) {
/*  568 */         internalSet(11, i10);
/*      */       } else {
/*  570 */         internalSet(9, i10 / 12);
/*  571 */         internalSet(10, i10 % 12);
/*      */       }
/*      */ 
/*  575 */       int i12 = localDate3.getZoneOffset();
/*  576 */       int i14 = localDate3.getDaylightSaving();
/*  577 */       internalSet(15, i12 - i14);
/*  578 */       internalSet(16, i14);
/*  579 */       return;
/*      */     case 1:
/*  583 */       i = getActualMinimum(paramInt1);
/*  584 */       j = getActualMaximum(paramInt1);
/*  585 */       break;
/*      */     case 2:
/*  593 */       if (!isTransitionYear(this.jdate.getNormalizedYear())) {
/*  594 */         k = this.jdate.getYear();
/*      */         LocalGregorianCalendar.Date localDate1;
/*      */         LocalGregorianCalendar.Date localDate2;
/*  595 */         if (k == getMaximum(1)) {
/*  596 */           localDate1 = jcal.getCalendarDate(this.time, getZone());
/*  597 */           localDate2 = jcal.getCalendarDate(9223372036854775807L, getZone());
/*  598 */           j = localDate2.getMonth() - 1;
/*  599 */           i8 = getRolledValue(internalGet(paramInt1), paramInt2, i, j);
/*  600 */           if (i8 == j)
/*      */           {
/*  602 */             localDate1.addYear(-400);
/*  603 */             localDate1.setMonth(i8 + 1);
/*  604 */             if (localDate1.getDayOfMonth() > localDate2.getDayOfMonth()) {
/*  605 */               localDate1.setDayOfMonth(localDate2.getDayOfMonth());
/*  606 */               jcal.normalize(localDate1);
/*      */             }
/*  608 */             if ((localDate1.getDayOfMonth() == localDate2.getDayOfMonth()) && (localDate1.getTimeOfDay() > localDate2.getTimeOfDay()))
/*      */             {
/*  610 */               localDate1.setMonth(i8 + 1);
/*  611 */               localDate1.setDayOfMonth(localDate2.getDayOfMonth() - 1);
/*  612 */               jcal.normalize(localDate1);
/*      */ 
/*  614 */               i8 = localDate1.getMonth() - 1;
/*      */             }
/*  616 */             set(5, localDate1.getDayOfMonth());
/*      */           }
/*  618 */           set(2, i8);
/*  619 */         } else if (k == getMinimum(1)) {
/*  620 */           localDate1 = jcal.getCalendarDate(this.time, getZone());
/*  621 */           localDate2 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/*  622 */           i = localDate2.getMonth() - 1;
/*  623 */           i8 = getRolledValue(internalGet(paramInt1), paramInt2, i, j);
/*  624 */           if (i8 == i)
/*      */           {
/*  626 */             localDate1.addYear(400);
/*  627 */             localDate1.setMonth(i8 + 1);
/*  628 */             if (localDate1.getDayOfMonth() < localDate2.getDayOfMonth()) {
/*  629 */               localDate1.setDayOfMonth(localDate2.getDayOfMonth());
/*  630 */               jcal.normalize(localDate1);
/*      */             }
/*  632 */             if ((localDate1.getDayOfMonth() == localDate2.getDayOfMonth()) && (localDate1.getTimeOfDay() < localDate2.getTimeOfDay()))
/*      */             {
/*  634 */               localDate1.setMonth(i8 + 1);
/*  635 */               localDate1.setDayOfMonth(localDate2.getDayOfMonth() + 1);
/*  636 */               jcal.normalize(localDate1);
/*      */ 
/*  638 */               i8 = localDate1.getMonth() - 1;
/*      */             }
/*  640 */             set(5, localDate1.getDayOfMonth());
/*      */           }
/*  642 */           set(2, i8);
/*      */         } else {
/*  644 */           int i1 = (internalGet(2) + paramInt2) % 12;
/*  645 */           if (i1 < 0) {
/*  646 */             i1 += 12;
/*      */           }
/*  648 */           set(2, i1);
/*      */ 
/*  654 */           i6 = monthLength(i1);
/*  655 */           if (internalGet(5) > i6)
/*  656 */             set(5, i6);
/*      */         }
/*      */       }
/*      */       else {
/*  660 */         k = getEraIndex(this.jdate);
/*  661 */         CalendarDate localCalendarDate = null;
/*  662 */         if (this.jdate.getYear() == 1) {
/*  663 */           localCalendarDate = eras[k].getSinceDate();
/*  664 */           i = localCalendarDate.getMonth() - 1;
/*      */         }
/*  666 */         else if (k < eras.length - 1) {
/*  667 */           localCalendarDate = eras[(k + 1)].getSinceDate();
/*  668 */           if (localCalendarDate.getYear() == this.jdate.getNormalizedYear()) {
/*  669 */             j = localCalendarDate.getMonth() - 1;
/*  670 */             if (localCalendarDate.getDayOfMonth() == 1) {
/*  671 */               j--;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  677 */         if (i == j)
/*      */         {
/*  681 */           return;
/*      */         }
/*  683 */         i6 = getRolledValue(internalGet(paramInt1), paramInt2, i, j);
/*  684 */         set(2, i6);
/*  685 */         if (i6 == i) {
/*  686 */           if ((localCalendarDate.getMonth() != 1) || (localCalendarDate.getDayOfMonth() != 1))
/*      */           {
/*  688 */             if (this.jdate.getDayOfMonth() < localCalendarDate.getDayOfMonth())
/*  689 */               set(5, localCalendarDate.getDayOfMonth());
/*      */           }
/*      */         }
/*  692 */         else if ((i6 == j) && (localCalendarDate.getMonth() - 1 == i6)) {
/*  693 */           i8 = localCalendarDate.getDayOfMonth();
/*  694 */           if (this.jdate.getDayOfMonth() >= i8) {
/*  695 */             set(5, i8 - 1);
/*      */           }
/*      */         }
/*      */       }
/*  699 */       return;
/*      */     case 3:
/*  704 */       k = this.jdate.getNormalizedYear();
/*  705 */       j = getActualMaximum(3);
/*  706 */       set(7, internalGet(7));
/*  707 */       i2 = internalGet(3);
/*  708 */       i6 = i2 + paramInt2;
/*  709 */       if (!isTransitionYear(this.jdate.getNormalizedYear())) {
/*  710 */         i8 = this.jdate.getYear();
/*  711 */         if (i8 == getMaximum(1)) {
/*  712 */           j = getActualMaximum(3);
/*  713 */         } else if (i8 == getMinimum(1)) {
/*  714 */           i = getActualMinimum(3);
/*  715 */           j = getActualMaximum(3);
/*  716 */           if ((i6 > i) && (i6 < j)) {
/*  717 */             set(3, i6);
/*  718 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  724 */         if ((i6 > i) && (i6 < j)) {
/*  725 */           set(3, i6);
/*  726 */           return;
/*      */         }
/*  728 */         l9 = this.cachedFixedDate;
/*      */ 
/*  730 */         long l11 = l9 - 7 * (i2 - i);
/*  731 */         if (i8 != getMinimum(1)) {
/*  732 */           if (gcal.getYearFromFixedDate(l11) != k)
/*  733 */             i++;
/*      */         }
/*      */         else {
/*  736 */           localDate7 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/*  737 */           if (l11 < jcal.getFixedDate(localDate7)) {
/*  738 */             i++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  743 */         l9 += 7 * (j - internalGet(3));
/*  744 */         if (gcal.getYearFromFixedDate(l9) != k) {
/*  745 */           j--;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  751 */         long l7 = this.cachedFixedDate;
/*  752 */         long l10 = l7 - 7 * (i2 - i);
/*      */ 
/*  754 */         LocalGregorianCalendar.Date localDate6 = getCalendarDate(l10);
/*  755 */         if ((localDate6.getEra() != this.jdate.getEra()) || (localDate6.getYear() != this.jdate.getYear())) {
/*  756 */           i++;
/*      */         }
/*      */ 
/*  760 */         l7 += 7 * (j - i2);
/*  761 */         jcal.getCalendarDateFromFixedDate(localDate6, l7);
/*  762 */         if ((localDate6.getEra() != this.jdate.getEra()) || (localDate6.getYear() != this.jdate.getYear())) {
/*  763 */           j--;
/*  767 */         }
/*      */ i6 = getRolledValue(i2, paramInt2, i, j) - 1;
/*  768 */         localDate6 = getCalendarDate(l10 + i6 * 7);
/*  769 */         set(2, localDate6.getMonth() - 1);
/*  770 */         set(5, localDate6.getDayOfMonth());
/*      */         return;
/*      */       }break;
/*      */     case 4:
/*  776 */       boolean bool = isTransitionYear(this.jdate.getNormalizedYear());
/*      */ 
/*  778 */       i2 = internalGet(7) - getFirstDayOfWeek();
/*  779 */       if (i2 < 0) {
/*  780 */         i2 += 7;
/*      */       }
/*      */ 
/*  783 */       long l5 = this.cachedFixedDate;
/*      */ 
/*  786 */       if (bool) {
/*  787 */         l9 = getFixedDateMonth1(this.jdate, l5);
/*  788 */         i15 = actualMonthLength();
/*      */       } else {
/*  790 */         l9 = l5 - internalGet(5) + 1L;
/*  791 */         i15 = jcal.getMonthLength(this.jdate);
/*      */       }
/*      */ 
/*  795 */       long l12 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l9 + 6L, getFirstDayOfWeek());
/*      */ 
/*  799 */       if ((int)(l12 - l9) >= getMinimalDaysInFirstWeek()) {
/*  800 */         l12 -= 7L;
/*      */       }
/*  802 */       j = getActualMaximum(paramInt1);
/*      */ 
/*  805 */       int i17 = getRolledValue(internalGet(paramInt1), paramInt2, 1, j) - 1;
/*      */ 
/*  808 */       long l13 = l12 + i17 * 7 + i2;
/*      */ 
/*  812 */       if (l13 < l9)
/*  813 */         l13 = l9;
/*  814 */       else if (l13 >= l9 + i15) {
/*  815 */         l13 = l9 + i15 - 1L;
/*      */       }
/*  817 */       set(5, (int)(l13 - l9) + 1);
/*  818 */       return;
/*      */     case 5:
/*  823 */       if (!isTransitionYear(this.jdate.getNormalizedYear())) {
/*  824 */         j = jcal.getMonthLength(this.jdate);
/*      */       }
/*      */       else
/*      */       {
/*  832 */         long l1 = getFixedDateMonth1(this.jdate, this.cachedFixedDate);
/*      */ 
/*  837 */         i7 = getRolledValue((int)(this.cachedFixedDate - l1), paramInt2, 0, actualMonthLength() - 1);
/*      */ 
/*  839 */         localDate4 = getCalendarDate(l1 + i7);
/*  840 */         assert ((getEraIndex(localDate4) == internalGetEra()) && (localDate4.getYear() == internalGet(1)) && (localDate4.getMonth() - 1 == internalGet(2)));
/*      */ 
/*  842 */         set(5, localDate4.getDayOfMonth());
/*      */         return;
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 6:
/*  848 */       j = getActualMaximum(paramInt1);
/*  849 */       if (isTransitionYear(this.jdate.getNormalizedYear())) {
/*  855 */         m = getRolledValue(internalGet(6), paramInt2, i, j);
/*  856 */         long l3 = this.cachedFixedDate - internalGet(6);
/*  857 */         localDate4 = getCalendarDate(l3 + m);
/*  858 */         assert ((getEraIndex(localDate4) == internalGetEra()) && (localDate4.getYear() == internalGet(1)));
/*  859 */         set(2, localDate4.getMonth() - 1);
/*  860 */         set(5, localDate4.getDayOfMonth());
/*      */         return;
/*      */       }
/*      */       break;
/*      */     case 7:
/*  866 */       m = this.jdate.getNormalizedYear();
/*  867 */       if ((!isTransitionYear(m)) && (!isTransitionYear(m - 1)))
/*      */       {
/*  870 */         int i3 = internalGet(3);
/*  871 */         if ((i3 > 1) && (i3 < 52)) {
/*  872 */           set(3, internalGet(3));
/*  873 */           j = 7;
/*  874 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  882 */       paramInt2 %= 7;
/*  883 */       if (paramInt2 == 0) {
/*  884 */         return;
/*      */       }
/*  886 */       long l4 = this.cachedFixedDate;
/*  887 */       long l8 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l4, getFirstDayOfWeek());
/*  888 */       l4 += paramInt2;
/*  889 */       if (l4 < l8)
/*  890 */         l4 += 7L;
/*  891 */       else if (l4 >= l8 + 7L) {
/*  892 */         l4 -= 7L;
/*      */       }
/*  894 */       LocalGregorianCalendar.Date localDate5 = getCalendarDate(l4);
/*  895 */       set(0, getEraIndex(localDate5));
/*  896 */       set(localDate5.getYear(), localDate5.getMonth() - 1, localDate5.getDayOfMonth());
/*  897 */       return;
/*      */     case 8:
/*  902 */       i = 1;
/*  903 */       if (!isTransitionYear(this.jdate.getNormalizedYear())) {
/*  904 */         m = internalGet(5);
/*  905 */         int i4 = jcal.getMonthLength(this.jdate);
/*  906 */         i7 = i4 % 7;
/*  907 */         j = i4 / 7;
/*  908 */         int i9 = (m - 1) % 7;
/*  909 */         if (i9 < i7) {
/*  910 */           j++;
/*      */         }
/*  912 */         set(7, internalGet(7));
/*      */       }
/*      */       else
/*      */       {
/*  917 */         long l2 = this.cachedFixedDate;
/*  918 */         long l6 = getFixedDateMonth1(this.jdate, l2);
/*  919 */         int i11 = actualMonthLength();
/*  920 */         int i13 = i11 % 7;
/*  921 */         j = i11 / 7;
/*  922 */         i15 = (int)(l2 - l6) % 7;
/*  923 */         if (i15 < i13) {
/*  924 */           j++; } int i16 = getRolledValue(internalGet(paramInt1), paramInt2, i, j) - 1;
/*  927 */         l2 = l6 + i16 * 7 + i15;
/*  928 */         localDate7 = getCalendarDate(l2);
/*  929 */         set(5, localDate7.getDayOfMonth());
/*      */         return;
/*      */       }break;
/*  934 */     }set(paramInt1, getRolledValue(internalGet(paramInt1), paramInt2, i, j));
/*      */   }
/*      */ 
/*      */   public String getDisplayName(int paramInt1, int paramInt2, Locale paramLocale) {
/*  938 */     if (!checkDisplayNameParams(paramInt1, paramInt2, 1, 2, paramLocale, 647))
/*      */     {
/*  940 */       return null;
/*      */     }
/*      */ 
/*  944 */     if ((paramInt1 == 1) && ((paramInt2 == 1) || (get(1) != 1) || (get(0) == 0)))
/*      */     {
/*  946 */       return null;
/*      */     }
/*      */ 
/*  949 */     ResourceBundle localResourceBundle = LocaleData.getDateFormatData(paramLocale);
/*  950 */     String str1 = null;
/*  951 */     String str2 = getKey(paramInt1, paramInt2);
/*  952 */     if (str2 != null) {
/*  953 */       String[] arrayOfString = localResourceBundle.getStringArray(str2);
/*  954 */       if (paramInt1 == 1) {
/*  955 */         if (arrayOfString.length > 0)
/*  956 */           str1 = arrayOfString[0];
/*      */       }
/*      */       else {
/*  959 */         int i = get(paramInt1);
/*      */ 
/*  962 */         if ((paramInt1 == 0) && (i >= arrayOfString.length) && (i < eras.length)) {
/*  963 */           Era localEra = eras[i];
/*  964 */           str1 = paramInt2 == 1 ? localEra.getAbbreviation() : localEra.getName();
/*      */         } else {
/*  966 */           if (paramInt1 == 7)
/*  967 */             i--;
/*  968 */           str1 = arrayOfString[i];
/*      */         }
/*      */       }
/*      */     }
/*  972 */     return str1;
/*      */   }
/*      */ 
/*      */   public Map<String, Integer> getDisplayNames(int paramInt1, int paramInt2, Locale paramLocale) {
/*  976 */     if (!checkDisplayNameParams(paramInt1, paramInt2, 0, 2, paramLocale, 647))
/*      */     {
/*  978 */       return null;
/*      */     }
/*      */ 
/*  981 */     if (paramInt2 == 0) {
/*  982 */       Map localMap1 = getDisplayNamesImpl(paramInt1, 1, paramLocale);
/*  983 */       if (paramInt1 == 9) {
/*  984 */         return localMap1;
/*      */       }
/*  986 */       Map localMap2 = getDisplayNamesImpl(paramInt1, 2, paramLocale);
/*  987 */       if (localMap1 == null) {
/*  988 */         return localMap2;
/*      */       }
/*  990 */       if (localMap2 != null) {
/*  991 */         localMap1.putAll(localMap2);
/*      */       }
/*  993 */       return localMap1;
/*      */     }
/*      */ 
/*  997 */     return getDisplayNamesImpl(paramInt1, paramInt2, paramLocale);
/*      */   }
/*      */ 
/*      */   private Map<String, Integer> getDisplayNamesImpl(int paramInt1, int paramInt2, Locale paramLocale) {
/* 1001 */     ResourceBundle localResourceBundle = LocaleData.getDateFormatData(paramLocale);
/* 1002 */     String str1 = getKey(paramInt1, paramInt2);
/* 1003 */     HashMap localHashMap = new HashMap();
/* 1004 */     if (str1 != null) {
/* 1005 */       String[] arrayOfString = localResourceBundle.getStringArray(str1);
/* 1006 */       if (paramInt1 == 1) {
/* 1007 */         if (arrayOfString.length > 0)
/* 1008 */           localHashMap.put(arrayOfString[0], Integer.valueOf(1));
/*      */       }
/*      */       else {
/* 1011 */         int i = paramInt1 == 7 ? 1 : 0;
/* 1012 */         for (int j = 0; j < arrayOfString.length; j++) {
/* 1013 */           localHashMap.put(arrayOfString[j], Integer.valueOf(i + j));
/*      */         }
/*      */ 
/* 1016 */         if ((paramInt1 == 0) && (arrayOfString.length < eras.length)) {
/* 1017 */           for (j = arrayOfString.length; j < eras.length; j++) {
/* 1018 */             Era localEra = eras[j];
/* 1019 */             String str2 = paramInt2 == 1 ? localEra.getAbbreviation() : localEra.getName();
/* 1020 */             localHashMap.put(str2, Integer.valueOf(j));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1025 */     return localHashMap.size() > 0 ? localHashMap : null;
/*      */   }
/*      */ 
/*      */   private String getKey(int paramInt1, int paramInt2) {
/* 1029 */     String str = JapaneseImperialCalendar.class.getName();
/* 1030 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1031 */     switch (paramInt1) {
/*      */     case 0:
/* 1033 */       localStringBuilder.append(str);
/* 1034 */       if (paramInt2 == 1) {
/* 1035 */         localStringBuilder.append(".short");
/*      */       }
/* 1037 */       localStringBuilder.append(".Eras");
/* 1038 */       break;
/*      */     case 1:
/* 1041 */       localStringBuilder.append(str).append(".FirstYear");
/* 1042 */       break;
/*      */     case 2:
/* 1045 */       localStringBuilder.append(paramInt2 == 1 ? "MonthAbbreviations" : "MonthNames");
/* 1046 */       break;
/*      */     case 7:
/* 1049 */       localStringBuilder.append(paramInt2 == 1 ? "DayAbbreviations" : "DayNames");
/* 1050 */       break;
/*      */     case 9:
/* 1053 */       localStringBuilder.append("AmPmMarkers");
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/* 1056 */     case 8: } return localStringBuilder.length() > 0 ? localStringBuilder.toString() : null;
/*      */   }
/*      */ 
/*      */   public int getMinimum(int paramInt)
/*      */   {
/* 1078 */     return MIN_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getMaximum(int paramInt)
/*      */   {
/* 1100 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/* 1104 */       LocalGregorianCalendar.Date localDate = jcal.getCalendarDate(9223372036854775807L, getZone());
/*      */ 
/* 1106 */       return Math.max(LEAST_MAX_VALUES[1], localDate.getYear());
/*      */     }
/*      */ 
/* 1109 */     return MAX_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getGreatestMinimum(int paramInt)
/*      */   {
/* 1131 */     return paramInt == 1 ? 1 : MIN_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getLeastMaximum(int paramInt)
/*      */   {
/* 1153 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/* 1156 */       return Math.min(LEAST_MAX_VALUES[1], getMaximum(1));
/*      */     }
/*      */ 
/* 1159 */     return LEAST_MAX_VALUES[paramInt];
/*      */   }
/*      */ 
/*      */   public int getActualMinimum(int paramInt)
/*      */   {
/* 1180 */     if (!isFieldSet(14, paramInt)) {
/* 1181 */       return getMinimum(paramInt);
/*      */     }
/*      */ 
/* 1184 */     int i = 0;
/* 1185 */     JapaneseImperialCalendar localJapaneseImperialCalendar = getNormalizedCalendar();
/*      */ 
/* 1188 */     LocalGregorianCalendar.Date localDate1 = jcal.getCalendarDate(localJapaneseImperialCalendar.getTimeInMillis(), getZone());
/*      */ 
/* 1190 */     int j = getEraIndex(localDate1);
/*      */     LocalGregorianCalendar.Date localDate4;
/* 1191 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/* 1194 */       if (j > 0) {
/* 1195 */         i = 1;
/* 1196 */         long l1 = eras[j].getSince(getZone());
/* 1197 */         localDate4 = jcal.getCalendarDate(l1, getZone());
/*      */ 
/* 1201 */         localDate1.setYear(localDate4.getYear());
/* 1202 */         jcal.normalize(localDate1);
/* 1203 */         assert (localDate1.isLeapYear() == localDate4.isLeapYear());
/* 1204 */         if (getYearOffsetInMillis(localDate1) < getYearOffsetInMillis(localDate4))
/* 1205 */           i++;
/*      */       }
/*      */       else {
/* 1208 */         i = getMinimum(paramInt);
/* 1209 */         LocalGregorianCalendar.Date localDate2 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/*      */ 
/* 1213 */         int k = localDate2.getYear();
/* 1214 */         if (k > 400) {
/* 1215 */           k -= 400;
/*      */         }
/* 1217 */         localDate1.setYear(k);
/* 1218 */         jcal.normalize(localDate1);
/* 1219 */         if (getYearOffsetInMillis(localDate1) < getYearOffsetInMillis(localDate2)) {
/* 1220 */           i++;
/*      */         }
/*      */       }
/*      */ 
/* 1224 */       break;
/*      */     case 2:
/* 1229 */       if ((j > 1) && (localDate1.getYear() == 1)) {
/* 1230 */         long l2 = eras[j].getSince(getZone());
/* 1231 */         localDate4 = jcal.getCalendarDate(l2, getZone());
/* 1232 */         i = localDate4.getMonth() - 1;
/* 1233 */         if (localDate1.getDayOfMonth() < localDate4.getDayOfMonth())
/* 1234 */           i++;
/*      */       }
/* 1236 */       break;
/*      */     case 3:
/* 1242 */       i = 1;
/* 1243 */       LocalGregorianCalendar.Date localDate3 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/*      */ 
/* 1245 */       localDate3.addYear(400);
/* 1246 */       jcal.normalize(localDate3);
/* 1247 */       localDate1.setEra(localDate3.getEra());
/* 1248 */       localDate1.setYear(localDate3.getYear());
/* 1249 */       jcal.normalize(localDate1);
/*      */ 
/* 1251 */       long l3 = jcal.getFixedDate(localDate3);
/* 1252 */       long l4 = jcal.getFixedDate(localDate1);
/* 1253 */       int m = getWeekNumber(l3, l4);
/* 1254 */       long l5 = l4 - 7 * (m - 1);
/* 1255 */       if ((l5 < l3) || ((l5 == l3) && (localDate1.getTimeOfDay() < localDate3.getTimeOfDay())))
/*      */       {
/* 1258 */         i++;
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/* 1263 */     return i;
/*      */   }
/*      */ 
/*      */   public int getActualMaximum(int paramInt)
/*      */   {
/* 1292 */     if ((0x1FE81 & 1 << paramInt) != 0) {
/* 1293 */       return getMaximum(paramInt);
/*      */     }
/*      */ 
/* 1296 */     JapaneseImperialCalendar localJapaneseImperialCalendar = getNormalizedCalendar();
/* 1297 */     LocalGregorianCalendar.Date localDate1 = localJapaneseImperialCalendar.jdate;
/* 1298 */     int i = localDate1.getNormalizedYear();
/*      */ 
/* 1300 */     int j = -1;
/*      */     long l1;
/*      */     long l5;
/*      */     Object localObject2;
/*      */     LocalGregorianCalendar.Date localDate3;
/*      */     long l4;
/*      */     long l7;
/*      */     Object localObject1;
/*      */     int i3;
/*      */     int i4;
/*      */     int i5;
/* 1301 */     switch (paramInt)
/*      */     {
/*      */     case 2:
/* 1304 */       j = 11;
/* 1305 */       if (isTransitionYear(localDate1.getNormalizedYear()))
/*      */       {
/* 1307 */         int k = getEraIndex(localDate1);
/* 1308 */         if (localDate1.getYear() != 1) {
/* 1309 */           k++;
/* 1310 */           assert (k < eras.length);
/*      */         }
/* 1312 */         l1 = sinceFixedDates[k];
/* 1313 */         l5 = localJapaneseImperialCalendar.cachedFixedDate;
/* 1314 */         if (l5 < l1) {
/* 1315 */           localObject2 = (LocalGregorianCalendar.Date)localDate1.clone();
/*      */ 
/* 1317 */           jcal.getCalendarDateFromFixedDate((CalendarDate)localObject2, l1 - 1L);
/* 1318 */           j = ((LocalGregorianCalendar.Date)localObject2).getMonth() - 1;
/*      */         }
/*      */       } else {
/* 1321 */         LocalGregorianCalendar.Date localDate2 = jcal.getCalendarDate(9223372036854775807L, getZone());
/*      */ 
/* 1323 */         if ((localDate1.getEra() == localDate2.getEra()) && (localDate1.getYear() == localDate2.getYear())) {
/* 1324 */           j = localDate2.getMonth() - 1;
/*      */         }
/*      */       }
/*      */ 
/* 1328 */       break;
/*      */     case 5:
/* 1331 */       j = jcal.getMonthLength(localDate1);
/* 1332 */       break;
/*      */     case 6:
/* 1336 */       if (isTransitionYear(localDate1.getNormalizedYear()))
/*      */       {
/* 1339 */         int m = getEraIndex(localDate1);
/* 1340 */         if (localDate1.getYear() != 1) {
/* 1341 */           m++;
/* 1342 */           assert (m < eras.length);
/*      */         }
/* 1344 */         l1 = sinceFixedDates[m];
/* 1345 */         l5 = localJapaneseImperialCalendar.cachedFixedDate;
/* 1346 */         localObject2 = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 1347 */         ((CalendarDate)localObject2).setDate(localDate1.getNormalizedYear(), 1, 1);
/* 1348 */         if (l5 < l1) {
/* 1349 */           j = (int)(l1 - gcal.getFixedDate((CalendarDate)localObject2));
/*      */         } else {
/* 1351 */           ((CalendarDate)localObject2).addYear(1);
/* 1352 */           j = (int)(gcal.getFixedDate((CalendarDate)localObject2) - l1);
/*      */         }
/*      */       } else {
/* 1355 */         localDate3 = jcal.getCalendarDate(9223372036854775807L, getZone());
/*      */ 
/* 1357 */         if ((localDate1.getEra() == localDate3.getEra()) && (localDate1.getYear() == localDate3.getYear())) {
/* 1358 */           l1 = jcal.getFixedDate(localDate3);
/* 1359 */           l5 = getFixedDateJan1(localDate3, l1);
/* 1360 */           j = (int)(l1 - l5) + 1;
/* 1361 */         } else if (localDate1.getYear() == getMinimum(1)) {
/* 1362 */           LocalGregorianCalendar.Date localDate6 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/* 1363 */           l4 = jcal.getFixedDate(localDate6);
/* 1364 */           localDate6.addYear(1);
/* 1365 */           localDate6.setMonth(1).setDayOfMonth(1);
/* 1366 */           jcal.normalize(localDate6);
/* 1367 */           l7 = jcal.getFixedDate(localDate6);
/* 1368 */           j = (int)(l7 - l4);
/*      */         } else {
/* 1370 */           j = jcal.getYearLength(localDate1);
/*      */         }
/*      */       }
/*      */ 
/* 1374 */       break;
/*      */     case 3:
/* 1378 */       if (!isTransitionYear(localDate1.getNormalizedYear())) {
/* 1379 */         localDate3 = jcal.getCalendarDate(9223372036854775807L, getZone());
/*      */ 
/* 1381 */         if ((localDate1.getEra() == localDate3.getEra()) && (localDate1.getYear() == localDate3.getYear())) {
/* 1382 */           long l2 = jcal.getFixedDate(localDate3);
/* 1383 */           l5 = getFixedDateJan1(localDate3, l2);
/* 1384 */           j = getWeekNumber(l5, l2);
/* 1385 */         } else if ((localDate1.getEra() == null) && (localDate1.getYear() == getMinimum(1))) {
/* 1386 */           localObject1 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/*      */ 
/* 1388 */           ((CalendarDate)localObject1).addYear(400);
/* 1389 */           jcal.normalize((CalendarDate)localObject1);
/* 1390 */           localDate3.setEra(((CalendarDate)localObject1).getEra());
/* 1391 */           localDate3.setDate(((CalendarDate)localObject1).getYear() + 1, 1, 1);
/* 1392 */           jcal.normalize(localDate3);
/* 1393 */           l4 = jcal.getFixedDate((CalendarDate)localObject1);
/* 1394 */           l7 = jcal.getFixedDate(localDate3);
/* 1395 */           long l8 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l7 + 6L, getFirstDayOfWeek());
/*      */ 
/* 1397 */           int i6 = (int)(l8 - l7);
/* 1398 */           if (i6 >= getMinimalDaysInFirstWeek()) {
/* 1399 */             l8 -= 7L;
/*      */           }
/* 1401 */           j = getWeekNumber(l4, l8);
/*      */         }
/*      */         else {
/* 1404 */           localObject1 = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 1405 */           ((CalendarDate)localObject1).setDate(localDate1.getNormalizedYear(), 1, 1);
/* 1406 */           i3 = gcal.getDayOfWeek((CalendarDate)localObject1);
/*      */ 
/* 1408 */           i3 -= getFirstDayOfWeek();
/* 1409 */           if (i3 < 0) {
/* 1410 */             i3 += 7;
/*      */           }
/* 1412 */           j = 52;
/* 1413 */           i4 = i3 + getMinimalDaysInFirstWeek() - 1;
/* 1414 */           if ((i4 == 6) || ((localDate1.isLeapYear()) && ((i4 == 5) || (i4 == 12))))
/*      */           {
/* 1416 */             j++;
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1422 */         if (localJapaneseImperialCalendar == this) {
/* 1423 */           localJapaneseImperialCalendar = (JapaneseImperialCalendar)localJapaneseImperialCalendar.clone();
/*      */         }
/* 1425 */         int n = getActualMaximum(6);
/* 1426 */         localJapaneseImperialCalendar.set(6, n);
/* 1427 */         j = localJapaneseImperialCalendar.get(3);
/* 1428 */         if ((j == 1) && (n > 7)) {
/* 1429 */           localJapaneseImperialCalendar.add(3, -1);
/* 1430 */           j = localJapaneseImperialCalendar.get(3);
/*      */         }
/*      */       }
/* 1433 */       break;
/*      */     case 4:
/* 1437 */       LocalGregorianCalendar.Date localDate4 = jcal.getCalendarDate(9223372036854775807L, getZone());
/*      */ 
/* 1439 */       if ((localDate1.getEra() != localDate4.getEra()) || (localDate1.getYear() != localDate4.getYear())) {
/* 1440 */         localObject1 = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 1441 */         ((CalendarDate)localObject1).setDate(localDate1.getNormalizedYear(), localDate1.getMonth(), 1);
/* 1442 */         i3 = gcal.getDayOfWeek((CalendarDate)localObject1);
/* 1443 */         i4 = gcal.getMonthLength((CalendarDate)localObject1);
/* 1444 */         i3 -= getFirstDayOfWeek();
/* 1445 */         if (i3 < 0) {
/* 1446 */           i3 += 7;
/*      */         }
/* 1448 */         i5 = 7 - i3;
/* 1449 */         j = 3;
/* 1450 */         if (i5 >= getMinimalDaysInFirstWeek()) {
/* 1451 */           j++;
/*      */         }
/* 1453 */         i4 -= i5 + 21;
/* 1454 */         if (i4 > 0) {
/* 1455 */           j++;
/* 1456 */           if (i4 > 7)
/* 1457 */             j++;
/*      */         }
/*      */       }
/*      */       else {
/* 1461 */         long l3 = jcal.getFixedDate(localDate4);
/* 1462 */         long l6 = l3 - localDate4.getDayOfMonth() + 1L;
/* 1463 */         j = getWeekNumber(l6, l3);
/*      */       }
/*      */ 
/* 1466 */       break;
/*      */     case 8:
/* 1471 */       i3 = localDate1.getDayOfWeek();
/* 1472 */       BaseCalendar.Date localDate = (BaseCalendar.Date)localDate1.clone();
/* 1473 */       int i1 = jcal.getMonthLength(localDate);
/* 1474 */       localDate.setDayOfMonth(1);
/* 1475 */       jcal.normalize(localDate);
/* 1476 */       int i2 = localDate.getDayOfWeek();
/* 1477 */       i5 = i3 - i2;
/* 1478 */       if (i5 < 0) {
/* 1479 */         i5 += 7;
/*      */       }
/* 1481 */       i1 -= i5;
/* 1482 */       j = (i1 + 6) / 7;
/*      */ 
/* 1484 */       break;
/*      */     case 1:
/* 1488 */       LocalGregorianCalendar.Date localDate5 = jcal.getCalendarDate(localJapaneseImperialCalendar.getTimeInMillis(), getZone());
/*      */ 
/* 1490 */       i3 = getEraIndex(localDate1);
/*      */       LocalGregorianCalendar.Date localDate7;
/* 1491 */       if (i3 == eras.length - 1) {
/* 1492 */         localDate7 = jcal.getCalendarDate(9223372036854775807L, getZone());
/* 1493 */         j = localDate7.getYear();
/*      */ 
/* 1496 */         if (j > 400)
/* 1497 */           localDate5.setYear(j - 400);
/*      */       }
/*      */       else {
/* 1500 */         localDate7 = jcal.getCalendarDate(eras[(i3 + 1)].getSince(getZone()) - 1L, getZone());
/*      */ 
/* 1502 */         j = localDate7.getYear();
/*      */ 
/* 1505 */         localDate5.setYear(j);
/*      */       }
/* 1507 */       jcal.normalize(localDate5);
/* 1508 */       if (getYearOffsetInMillis(localDate5) > getYearOffsetInMillis(localDate7)) {
/* 1509 */         j--;
/*      */       }
/*      */ 
/* 1512 */       break;
/*      */     case 7:
/*      */     default:
/* 1515 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*      */     }
/* 1517 */     return j;
/*      */   }
/*      */ 
/*      */   private final long getYearOffsetInMillis(CalendarDate paramCalendarDate)
/*      */   {
/* 1527 */     long l = (jcal.getDayOfYear(paramCalendarDate) - 1L) * 86400000L;
/* 1528 */     return l + paramCalendarDate.getTimeOfDay() - paramCalendarDate.getZoneOffset();
/*      */   }
/*      */ 
/*      */   public Object clone() {
/* 1532 */     JapaneseImperialCalendar localJapaneseImperialCalendar = (JapaneseImperialCalendar)super.clone();
/*      */ 
/* 1534 */     localJapaneseImperialCalendar.jdate = ((LocalGregorianCalendar.Date)this.jdate.clone());
/* 1535 */     localJapaneseImperialCalendar.originalFields = null;
/* 1536 */     localJapaneseImperialCalendar.zoneOffsets = null;
/* 1537 */     return localJapaneseImperialCalendar;
/*      */   }
/*      */ 
/*      */   public TimeZone getTimeZone() {
/* 1541 */     TimeZone localTimeZone = super.getTimeZone();
/*      */ 
/* 1543 */     this.jdate.setZone(localTimeZone);
/* 1544 */     return localTimeZone;
/*      */   }
/*      */ 
/*      */   public void setTimeZone(TimeZone paramTimeZone) {
/* 1548 */     super.setTimeZone(paramTimeZone);
/*      */ 
/* 1550 */     this.jdate.setZone(paramTimeZone);
/*      */   }
/*      */ 
/*      */   protected void computeFields()
/*      */   {
/* 1569 */     int i = 0;
/* 1570 */     if (isPartiallyNormalized())
/*      */     {
/* 1572 */       i = getSetStateFields();
/* 1573 */       int j = (i ^ 0xFFFFFFFF) & 0x1FFFF;
/* 1574 */       if ((j != 0) || (this.cachedFixedDate == -9223372036854775808L)) {
/* 1575 */         i |= computeFields(j, i & 0x18000);
/*      */ 
/* 1577 */         assert (i == 131071);
/*      */       }
/*      */     }
/*      */     else {
/* 1581 */       i = 131071;
/* 1582 */       computeFields(i, 0);
/*      */     }
/*      */ 
/* 1585 */     setFieldsComputed(i);
/*      */   }
/*      */ 
/*      */   private int computeFields(int paramInt1, int paramInt2)
/*      */   {
/* 1603 */     int i = 0;
/* 1604 */     TimeZone localTimeZone = getZone();
/* 1605 */     if (this.zoneOffsets == null) {
/* 1606 */       this.zoneOffsets = new int[2];
/*      */     }
/* 1608 */     if (paramInt2 != 98304) {
/* 1609 */       if ((localTimeZone instanceof ZoneInfo)) {
/* 1610 */         i = ((ZoneInfo)localTimeZone).getOffsets(this.time, this.zoneOffsets);
/*      */       } else {
/* 1612 */         i = localTimeZone.getOffset(this.time);
/* 1613 */         this.zoneOffsets[0] = localTimeZone.getRawOffset();
/* 1614 */         this.zoneOffsets[1] = (i - this.zoneOffsets[0]);
/*      */       }
/*      */     }
/* 1617 */     if (paramInt2 != 0) {
/* 1618 */       if (isFieldSet(paramInt2, 15)) {
/* 1619 */         this.zoneOffsets[0] = internalGet(15);
/*      */       }
/* 1621 */       if (isFieldSet(paramInt2, 16)) {
/* 1622 */         this.zoneOffsets[1] = internalGet(16);
/*      */       }
/* 1624 */       i = this.zoneOffsets[0] + this.zoneOffsets[1];
/*      */     }
/*      */ 
/* 1630 */     long l1 = i / 86400000L;
/* 1631 */     int j = i % 86400000;
/* 1632 */     l1 += this.time / 86400000L;
/* 1633 */     j += (int)(this.time % 86400000L);
/* 1634 */     if (j >= 86400000L) {
/* 1635 */       j = (int)(j - 86400000L);
/* 1636 */       l1 += 1L;
/*      */     } else {
/* 1638 */       while (j < 0) {
/* 1639 */         j = (int)(j + 86400000L);
/* 1640 */         l1 -= 1L;
/*      */       }
/*      */     }
/* 1643 */     l1 += 719163L;
/*      */ 
/* 1646 */     if ((l1 != this.cachedFixedDate) || (l1 < 0L)) {
/* 1647 */       jcal.getCalendarDateFromFixedDate(this.jdate, l1);
/* 1648 */       this.cachedFixedDate = l1;
/*      */     }
/* 1650 */     int k = getEraIndex(this.jdate);
/* 1651 */     int m = this.jdate.getYear();
/*      */ 
/* 1654 */     internalSet(0, k);
/* 1655 */     internalSet(1, m);
/* 1656 */     int n = paramInt1 | 0x3;
/*      */ 
/* 1658 */     int i1 = this.jdate.getMonth() - 1;
/* 1659 */     int i2 = this.jdate.getDayOfMonth();
/*      */ 
/* 1662 */     if ((paramInt1 & 0xA4) != 0)
/*      */     {
/* 1664 */       internalSet(2, i1);
/* 1665 */       internalSet(5, i2);
/* 1666 */       internalSet(7, this.jdate.getDayOfWeek());
/* 1667 */       n |= 164;
/*      */     }
/*      */     int i3;
/* 1670 */     if ((paramInt1 & 0x7E00) != 0)
/*      */     {
/* 1672 */       if (j != 0) {
/* 1673 */         i3 = j / 3600000;
/* 1674 */         internalSet(11, i3);
/* 1675 */         internalSet(9, i3 / 12);
/* 1676 */         internalSet(10, i3 % 12);
/* 1677 */         int i4 = j % 3600000;
/* 1678 */         internalSet(12, i4 / 60000);
/* 1679 */         i4 %= 60000;
/* 1680 */         internalSet(13, i4 / 1000);
/* 1681 */         internalSet(14, i4 % 1000);
/*      */       } else {
/* 1683 */         internalSet(11, 0);
/* 1684 */         internalSet(9, 0);
/* 1685 */         internalSet(10, 0);
/* 1686 */         internalSet(12, 0);
/* 1687 */         internalSet(13, 0);
/* 1688 */         internalSet(14, 0);
/*      */       }
/* 1690 */       n |= 32256;
/*      */     }
/*      */ 
/* 1694 */     if ((paramInt1 & 0x18000) != 0) {
/* 1695 */       internalSet(15, this.zoneOffsets[0]);
/* 1696 */       internalSet(16, this.zoneOffsets[1]);
/* 1697 */       n |= 98304;
/*      */     }
/*      */ 
/* 1700 */     if ((paramInt1 & 0x158) != 0)
/*      */     {
/* 1702 */       i3 = this.jdate.getNormalizedYear();
/*      */ 
/* 1705 */       boolean bool = isTransitionYear(this.jdate.getNormalizedYear());
/*      */       long l2;
/*      */       int i5;
/* 1708 */       if (bool) {
/* 1709 */         l2 = getFixedDateJan1(this.jdate, l1);
/* 1710 */         i5 = (int)(l1 - l2) + 1;
/* 1711 */       } else if (i3 == MIN_VALUES[1]) {
/* 1712 */         LocalGregorianCalendar.Date localDate1 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/* 1713 */         l2 = jcal.getFixedDate(localDate1);
/* 1714 */         i5 = (int)(l1 - l2) + 1;
/*      */       } else {
/* 1716 */         i5 = (int)jcal.getDayOfYear(this.jdate);
/* 1717 */         l2 = l1 - i5 + 1L;
/*      */       }
/* 1719 */       long l3 = bool ? getFixedDateMonth1(this.jdate, l1) : l1 - i2 + 1L;
/*      */ 
/* 1722 */       internalSet(6, i5);
/* 1723 */       internalSet(8, (i2 - 1) / 7 + 1);
/*      */ 
/* 1725 */       int i6 = getWeekNumber(l2, l1);
/*      */       long l4;
/*      */       long l6;
/* 1729 */       if (i6 == 0)
/*      */       {
/* 1737 */         l4 = l2 - 1L;
/*      */ 
/* 1739 */         LocalGregorianCalendar.Date localDate3 = getCalendarDate(l4);
/* 1740 */         if ((!bool) && (!isTransitionYear(localDate3.getNormalizedYear()))) {
/* 1741 */           l6 = l2 - 365L;
/* 1742 */           if (localDate3.isLeapYear())
/* 1743 */             l6 -= 1L;
/*      */         }
/*      */         else
/*      */         {
/*      */           CalendarDate localCalendarDate2;
/* 1745 */           if (bool) {
/* 1746 */             if (this.jdate.getYear() == 1)
/*      */             {
/* 1752 */               if (k > 4) {
/* 1753 */                 localCalendarDate2 = eras[(k - 1)].getSinceDate();
/* 1754 */                 if (i3 == localCalendarDate2.getYear())
/* 1755 */                   localDate3.setMonth(localCalendarDate2.getMonth()).setDayOfMonth(localCalendarDate2.getDayOfMonth());
/*      */               }
/*      */               else {
/* 1758 */                 localDate3.setMonth(1).setDayOfMonth(1);
/*      */               }
/* 1760 */               jcal.normalize(localDate3);
/* 1761 */               l6 = jcal.getFixedDate(localDate3);
/*      */             } else {
/* 1763 */               l6 = l2 - 365L;
/* 1764 */               if (localDate3.isLeapYear())
/* 1765 */                 l6 -= 1L;
/*      */             }
/*      */           }
/*      */           else {
/* 1769 */             localCalendarDate2 = eras[getEraIndex(this.jdate)].getSinceDate();
/* 1770 */             localDate3.setMonth(localCalendarDate2.getMonth()).setDayOfMonth(localCalendarDate2.getDayOfMonth());
/* 1771 */             jcal.normalize(localDate3);
/* 1772 */             l6 = jcal.getFixedDate(localDate3);
/*      */           }
/*      */         }
/* 1774 */         i6 = getWeekNumber(l6, l4);
/*      */       }
/* 1776 */       else if (!bool)
/*      */       {
/* 1778 */         if (i6 >= 52) {
/* 1779 */           l4 = l2 + 365L;
/* 1780 */           if (this.jdate.isLeapYear()) {
/* 1781 */             l4 += 1L;
/*      */           }
/* 1783 */           l6 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l4 + 6L, getFirstDayOfWeek());
/*      */ 
/* 1785 */           int i8 = (int)(l6 - l4);
/* 1786 */           if ((i8 >= getMinimalDaysInFirstWeek()) && (l1 >= l6 - 7L))
/*      */           {
/* 1788 */             i6 = 1;
/*      */           }
/*      */         }
/*      */       } else {
/* 1792 */         LocalGregorianCalendar.Date localDate2 = (LocalGregorianCalendar.Date)this.jdate.clone();
/*      */         long l5;
/* 1794 */         if (this.jdate.getYear() == 1) {
/* 1795 */           localDate2.addYear(1);
/* 1796 */           localDate2.setMonth(1).setDayOfMonth(1);
/* 1797 */           l5 = jcal.getFixedDate(localDate2);
/*      */         } else {
/* 1799 */           int i7 = getEraIndex(localDate2) + 1;
/* 1800 */           CalendarDate localCalendarDate1 = eras[i7].getSinceDate();
/* 1801 */           localDate2.setEra(eras[i7]);
/* 1802 */           localDate2.setDate(1, localCalendarDate1.getMonth(), localCalendarDate1.getDayOfMonth());
/* 1803 */           jcal.normalize(localDate2);
/* 1804 */           l5 = jcal.getFixedDate(localDate2);
/*      */         }
/* 1806 */         long l7 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l5 + 6L, getFirstDayOfWeek());
/*      */ 
/* 1808 */         int i9 = (int)(l7 - l5);
/* 1809 */         if ((i9 >= getMinimalDaysInFirstWeek()) && (l1 >= l7 - 7L))
/*      */         {
/* 1811 */           i6 = 1;
/*      */         }
/*      */       }
/*      */ 
/* 1815 */       internalSet(3, i6);
/* 1816 */       internalSet(4, getWeekNumber(l3, l1));
/* 1817 */       n |= 344;
/*      */     }
/* 1819 */     return n;
/*      */   }
/*      */ 
/*      */   private final int getWeekNumber(long paramLong1, long paramLong2)
/*      */   {
/* 1834 */     long l = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(paramLong1 + 6L, getFirstDayOfWeek());
/*      */ 
/* 1836 */     int i = (int)(l - paramLong1);
/* 1837 */     assert (i <= 7);
/* 1838 */     if (i >= getMinimalDaysInFirstWeek()) {
/* 1839 */       l -= 7L;
/*      */     }
/* 1841 */     int j = (int)(paramLong2 - l);
/* 1842 */     if (j >= 0) {
/* 1843 */       return j / 7 + 1;
/*      */     }
/* 1845 */     return CalendarUtils.floorDivide(j, 7) + 1;
/*      */   }
/*      */ 
/*      */   protected void computeTime()
/*      */   {
/*      */     int j;
/* 1859 */     if (!isLenient()) {
/* 1860 */       if (this.originalFields == null) {
/* 1861 */         this.originalFields = new int[17];
/*      */       }
/* 1863 */       for (i = 0; i < 17; i++) {
/* 1864 */         j = internalGet(i);
/* 1865 */         if (isExternallySet(i))
/*      */         {
/* 1867 */           if ((j < getMinimum(i)) || (j > getMaximum(i))) {
/* 1868 */             throw new IllegalArgumentException(getFieldName(i));
/*      */           }
/*      */         }
/* 1871 */         this.originalFields[i] = j;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1877 */     int i = selectFields();
/*      */     int k;
/* 1882 */     if (isSet(0)) {
/* 1883 */       k = internalGet(0);
/* 1884 */       j = isSet(1) ? internalGet(1) : 1;
/*      */     }
/* 1886 */     else if (isSet(1)) {
/* 1887 */       k = eras.length - 1;
/* 1888 */       j = internalGet(1);
/*      */     }
/*      */     else {
/* 1891 */       k = 3;
/* 1892 */       j = 45;
/*      */     }
/*      */ 
/* 1898 */     long l1 = 0L;
/* 1899 */     if (isFieldSet(i, 11)) {
/* 1900 */       l1 += internalGet(11);
/*      */     } else {
/* 1902 */       l1 += internalGet(10);
/*      */ 
/* 1904 */       if (isFieldSet(i, 9)) {
/* 1905 */         l1 += 12 * internalGet(9);
/*      */       }
/*      */     }
/* 1908 */     l1 *= 60L;
/* 1909 */     l1 += internalGet(12);
/* 1910 */     l1 *= 60L;
/* 1911 */     l1 += internalGet(13);
/* 1912 */     l1 *= 1000L;
/* 1913 */     l1 += internalGet(14);
/*      */ 
/* 1917 */     long l2 = l1 / 86400000L;
/* 1918 */     l1 %= 86400000L;
/* 1919 */     while (l1 < 0L) {
/* 1920 */       l1 += 86400000L;
/* 1921 */       l2 -= 1L;
/*      */     }
/*      */ 
/* 1925 */     l2 += getFixedDate(k, j, i);
/*      */ 
/* 1928 */     long l3 = (l2 - 719163L) * 86400000L + l1;
/*      */ 
/* 1943 */     TimeZone localTimeZone = getZone();
/* 1944 */     if (this.zoneOffsets == null) {
/* 1945 */       this.zoneOffsets = new int[2];
/*      */     }
/* 1947 */     int m = i & 0x18000;
/* 1948 */     if (m != 98304) {
/* 1949 */       if ((localTimeZone instanceof ZoneInfo))
/* 1950 */         ((ZoneInfo)localTimeZone).getOffsetsByWall(l3, this.zoneOffsets);
/*      */       else {
/* 1952 */         localTimeZone.getOffsets(l3 - localTimeZone.getRawOffset(), this.zoneOffsets);
/*      */       }
/*      */     }
/* 1955 */     if (m != 0) {
/* 1956 */       if (isFieldSet(m, 15)) {
/* 1957 */         this.zoneOffsets[0] = internalGet(15);
/*      */       }
/* 1959 */       if (isFieldSet(m, 16)) {
/* 1960 */         this.zoneOffsets[1] = internalGet(16);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1965 */     l3 -= this.zoneOffsets[0] + this.zoneOffsets[1];
/*      */ 
/* 1968 */     this.time = l3;
/*      */ 
/* 1970 */     int n = computeFields(i | getSetStateFields(), m);
/*      */ 
/* 1972 */     if (!isLenient()) {
/* 1973 */       for (int i1 = 0; i1 < 17; i1++) {
/* 1974 */         if (isExternallySet(i1))
/*      */         {
/* 1977 */           if (this.originalFields[i1] != internalGet(i1)) {
/* 1978 */             int i2 = internalGet(i1);
/*      */ 
/* 1980 */             System.arraycopy(this.originalFields, 0, this.fields, 0, this.fields.length);
/* 1981 */             throw new IllegalArgumentException(getFieldName(i1) + "=" + i2 + ", expected " + this.originalFields[i1]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1986 */     setFieldsNormalized(n);
/*      */   }
/*      */ 
/*      */   private long getFixedDate(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2001 */     int i = 0;
/* 2002 */     int j = 1;
/* 2003 */     if (isFieldSet(paramInt3, 2))
/*      */     {
/* 2006 */       i = internalGet(2);
/*      */ 
/* 2009 */       if (i > 11) {
/* 2010 */         paramInt2 += i / 12;
/* 2011 */         i %= 12;
/* 2012 */       } else if (i < 0) {
/* 2013 */         localObject = new int[1];
/* 2014 */         paramInt2 += CalendarUtils.floorDivide(i, 12, (int[])localObject);
/* 2015 */         i = localObject[0];
/*      */       }
/*      */     }
/* 2018 */     else if ((paramInt2 == 1) && (paramInt1 != 0)) {
/* 2019 */       localObject = eras[paramInt1].getSinceDate();
/* 2020 */       i = ((CalendarDate)localObject).getMonth() - 1;
/* 2021 */       j = ((CalendarDate)localObject).getDayOfMonth();
/*      */     }
/*      */ 
/* 2026 */     if (paramInt2 == MIN_VALUES[1]) {
/* 2027 */       localObject = jcal.getCalendarDate(-9223372036854775808L, getZone());
/* 2028 */       int k = ((CalendarDate)localObject).getMonth() - 1;
/* 2029 */       if (i < k)
/* 2030 */         i = k;
/* 2031 */       if (i == k) {
/* 2032 */         j = ((CalendarDate)localObject).getDayOfMonth();
/*      */       }
/*      */     }
/* 2035 */     Object localObject = jcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 2036 */     ((LocalGregorianCalendar.Date)localObject).setEra(paramInt1 > 0 ? eras[paramInt1] : null);
/* 2037 */     ((LocalGregorianCalendar.Date)localObject).setDate(paramInt2, i + 1, j);
/* 2038 */     jcal.normalize((CalendarDate)localObject);
/*      */ 
/* 2042 */     long l1 = jcal.getFixedDate((CalendarDate)localObject);
/*      */     int i1;
/* 2044 */     if (isFieldSet(paramInt3, 2))
/*      */     {
/* 2046 */       if (isFieldSet(paramInt3, 5))
/*      */       {
/* 2053 */         if (isSet(5))
/*      */         {
/* 2056 */           l1 += internalGet(5);
/* 2057 */           l1 -= j;
/*      */         }
/*      */       }
/* 2060 */       else if (isFieldSet(paramInt3, 4)) {
/* 2061 */         long l2 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l1 + 6L, getFirstDayOfWeek());
/*      */ 
/* 2065 */         if (l2 - l1 >= getMinimalDaysInFirstWeek()) {
/* 2066 */           l2 -= 7L;
/*      */         }
/* 2068 */         if (isFieldSet(paramInt3, 7)) {
/* 2069 */           l2 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l2 + 6L, internalGet(7));
/*      */         }
/*      */ 
/* 2075 */         l1 = l2 + 7 * (internalGet(4) - 1);
/*      */       }
/*      */       else
/*      */       {
/*      */         int m;
/* 2078 */         if (isFieldSet(paramInt3, 7))
/* 2079 */           m = internalGet(7);
/*      */         else
/* 2081 */           m = getFirstDayOfWeek();
/*      */         int n;
/* 2087 */         if (isFieldSet(paramInt3, 8))
/* 2088 */           n = internalGet(8);
/*      */         else {
/* 2090 */           n = 1;
/*      */         }
/* 2092 */         if (n >= 0) {
/* 2093 */           l1 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l1 + 7 * n - 1L, m);
/*      */         }
/*      */         else
/*      */         {
/* 2098 */           i1 = monthLength(i, paramInt2) + 7 * (n + 1);
/*      */ 
/* 2100 */           l1 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l1 + i1 - 1L, m);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 2107 */     else if (isFieldSet(paramInt3, 6)) {
/* 2108 */       if (isTransitionYear(((LocalGregorianCalendar.Date)localObject).getNormalizedYear())) {
/* 2109 */         l1 = getFixedDateJan1((LocalGregorianCalendar.Date)localObject, l1);
/*      */       }
/*      */ 
/* 2112 */       l1 += internalGet(6);
/* 2113 */       l1 -= 1L;
/*      */     } else {
/* 2115 */       long l3 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l1 + 6L, getFirstDayOfWeek());
/*      */ 
/* 2119 */       if (l3 - l1 >= getMinimalDaysInFirstWeek()) {
/* 2120 */         l3 -= 7L;
/*      */       }
/* 2122 */       if (isFieldSet(paramInt3, 7)) {
/* 2123 */         i1 = internalGet(7);
/* 2124 */         if (i1 != getFirstDayOfWeek()) {
/* 2125 */           l3 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(l3 + 6L, i1);
/*      */         }
/*      */       }
/*      */ 
/* 2129 */       l1 = l3 + 7L * (internalGet(3) - 1L);
/*      */     }
/*      */ 
/* 2132 */     return l1;
/*      */   }
/*      */ 
/*      */   private final long getFixedDateJan1(LocalGregorianCalendar.Date paramDate, long paramLong)
/*      */   {
/* 2144 */     Era localEra = paramDate.getEra();
/* 2145 */     if ((paramDate.getEra() != null) && (paramDate.getYear() == 1))
/* 2146 */       for (int i = getEraIndex(paramDate); i > 0; i--) {
/* 2147 */         CalendarDate localCalendarDate = eras[i].getSinceDate();
/* 2148 */         long l = gcal.getFixedDate(localCalendarDate);
/*      */ 
/* 2150 */         if (l <= paramLong)
/*      */         {
/* 2153 */           return l;
/*      */         }
/*      */       }
/* 2156 */     Gregorian.Date localDate = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 2157 */     localDate.setDate(paramDate.getNormalizedYear(), 1, 1);
/* 2158 */     return gcal.getFixedDate(localDate);
/*      */   }
/*      */ 
/*      */   private final long getFixedDateMonth1(LocalGregorianCalendar.Date paramDate, long paramLong)
/*      */   {
/* 2171 */     int i = getTransitionEraIndex(paramDate);
/* 2172 */     if (i != -1) {
/* 2173 */       long l = sinceFixedDates[i];
/*      */ 
/* 2176 */       if (l <= paramLong) {
/* 2177 */         return l;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2182 */     return paramLong - paramDate.getDayOfMonth() + 1L;
/*      */   }
/*      */ 
/*      */   private static final LocalGregorianCalendar.Date getCalendarDate(long paramLong)
/*      */   {
/* 2191 */     LocalGregorianCalendar.Date localDate = jcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/* 2192 */     jcal.getCalendarDateFromFixedDate(localDate, paramLong);
/* 2193 */     return localDate;
/*      */   }
/*      */ 
/*      */   private final int monthLength(int paramInt1, int paramInt2)
/*      */   {
/* 2203 */     return CalendarUtils.isGregorianLeapYear(paramInt2) ? GregorianCalendar.LEAP_MONTH_LENGTH[paramInt1] : GregorianCalendar.MONTH_LENGTH[paramInt1];
/*      */   }
/*      */ 
/*      */   private final int monthLength(int paramInt)
/*      */   {
/* 2214 */     assert (this.jdate.isNormalized());
/* 2215 */     return this.jdate.isLeapYear() ? GregorianCalendar.LEAP_MONTH_LENGTH[paramInt] : GregorianCalendar.MONTH_LENGTH[paramInt];
/*      */   }
/*      */ 
/*      */   private final int actualMonthLength()
/*      */   {
/* 2220 */     int i = jcal.getMonthLength(this.jdate);
/* 2221 */     int j = getTransitionEraIndex(this.jdate);
/* 2222 */     if (j == -1) {
/* 2223 */       long l = sinceFixedDates[j];
/* 2224 */       CalendarDate localCalendarDate = eras[j].getSinceDate();
/* 2225 */       if (l <= this.cachedFixedDate)
/* 2226 */         i -= localCalendarDate.getDayOfMonth() - 1;
/*      */       else {
/* 2228 */         i = localCalendarDate.getDayOfMonth() - 1;
/*      */       }
/*      */     }
/* 2231 */     return i;
/*      */   }
/*      */ 
/*      */   private static final int getTransitionEraIndex(LocalGregorianCalendar.Date paramDate)
/*      */   {
/* 2243 */     int i = getEraIndex(paramDate);
/* 2244 */     CalendarDate localCalendarDate = eras[i].getSinceDate();
/* 2245 */     if ((localCalendarDate.getYear() == paramDate.getNormalizedYear()) && (localCalendarDate.getMonth() == paramDate.getMonth()))
/*      */     {
/* 2247 */       return i;
/*      */     }
/* 2249 */     if (i < eras.length - 1) {
/* 2250 */       localCalendarDate = eras[(++i)].getSinceDate();
/* 2251 */       if ((localCalendarDate.getYear() == paramDate.getNormalizedYear()) && (localCalendarDate.getMonth() == paramDate.getMonth()))
/*      */       {
/* 2253 */         return i;
/*      */       }
/*      */     }
/* 2256 */     return -1;
/*      */   }
/*      */ 
/*      */   private final boolean isTransitionYear(int paramInt) {
/* 2260 */     for (int i = eras.length - 1; i > 0; i--) {
/* 2261 */       int j = eras[i].getSinceDate().getYear();
/* 2262 */       if (paramInt == j) {
/* 2263 */         return true;
/*      */       }
/* 2265 */       if (paramInt > j) {
/*      */         break;
/*      */       }
/*      */     }
/* 2269 */     return false;
/*      */   }
/*      */ 
/*      */   private static final int getEraIndex(LocalGregorianCalendar.Date paramDate) {
/* 2273 */     Era localEra = paramDate.getEra();
/* 2274 */     for (int i = eras.length - 1; i > 0; i--) {
/* 2275 */       if (eras[i] == localEra) {
/* 2276 */         return i;
/*      */       }
/*      */     }
/* 2279 */     return 0;
/*      */   }
/*      */ 
/*      */   private final JapaneseImperialCalendar getNormalizedCalendar()
/*      */   {
/*      */     JapaneseImperialCalendar localJapaneseImperialCalendar;
/* 2289 */     if (isFullyNormalized()) {
/* 2290 */       localJapaneseImperialCalendar = this;
/*      */     }
/*      */     else {
/* 2293 */       localJapaneseImperialCalendar = (JapaneseImperialCalendar)clone();
/* 2294 */       localJapaneseImperialCalendar.setLenient(true);
/* 2295 */       localJapaneseImperialCalendar.complete();
/*      */     }
/* 2297 */     return localJapaneseImperialCalendar;
/*      */   }
/*      */ 
/*      */   private final void pinDayOfMonth(LocalGregorianCalendar.Date paramDate)
/*      */   {
/* 2307 */     int i = paramDate.getYear();
/* 2308 */     int j = paramDate.getDayOfMonth();
/* 2309 */     if (i != getMinimum(1)) {
/* 2310 */       paramDate.setDayOfMonth(1);
/* 2311 */       jcal.normalize(paramDate);
/* 2312 */       int k = jcal.getMonthLength(paramDate);
/* 2313 */       if (j > k)
/* 2314 */         paramDate.setDayOfMonth(k);
/*      */       else {
/* 2316 */         paramDate.setDayOfMonth(j);
/*      */       }
/* 2318 */       jcal.normalize(paramDate);
/*      */     } else {
/* 2320 */       LocalGregorianCalendar.Date localDate1 = jcal.getCalendarDate(-9223372036854775808L, getZone());
/* 2321 */       LocalGregorianCalendar.Date localDate2 = jcal.getCalendarDate(this.time, getZone());
/* 2322 */       long l = localDate2.getTimeOfDay();
/*      */ 
/* 2324 */       localDate2.addYear(400);
/* 2325 */       localDate2.setMonth(paramDate.getMonth());
/* 2326 */       localDate2.setDayOfMonth(1);
/* 2327 */       jcal.normalize(localDate2);
/* 2328 */       int m = jcal.getMonthLength(localDate2);
/* 2329 */       if (j > m) {
/* 2330 */         localDate2.setDayOfMonth(m);
/*      */       }
/* 2332 */       else if (j < localDate1.getDayOfMonth())
/* 2333 */         localDate2.setDayOfMonth(localDate1.getDayOfMonth());
/*      */       else {
/* 2335 */         localDate2.setDayOfMonth(j);
/*      */       }
/*      */ 
/* 2338 */       if ((localDate2.getDayOfMonth() == localDate1.getDayOfMonth()) && (l < localDate1.getTimeOfDay())) {
/* 2339 */         localDate2.setDayOfMonth(Math.min(j + 1, m));
/*      */       }
/*      */ 
/* 2342 */       paramDate.setDate(i, localDate2.getMonth(), localDate2.getDayOfMonth());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final int getRolledValue(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2351 */     assert ((paramInt1 >= paramInt3) && (paramInt1 <= paramInt4));
/* 2352 */     int i = paramInt4 - paramInt3 + 1;
/* 2353 */     paramInt2 %= i;
/* 2354 */     int j = paramInt1 + paramInt2;
/* 2355 */     if (j > paramInt4)
/* 2356 */       j -= i;
/* 2357 */     else if (j < paramInt3) {
/* 2358 */       j += i;
/*      */     }
/* 2360 */     assert ((j >= paramInt3) && (j <= paramInt4));
/* 2361 */     return j;
/*      */   }
/*      */ 
/*      */   private final int internalGetEra()
/*      */   {
/* 2369 */     return isSet(0) ? internalGet(0) : eras.length - 1;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2377 */     paramObjectInputStream.defaultReadObject();
/* 2378 */     if (this.jdate == null) {
/* 2379 */       this.jdate = jcal.newCalendarDate(getZone());
/* 2380 */       this.cachedFixedDate = -9223372036854775808L;
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  117 */     jcal = (LocalGregorianCalendar)CalendarSystem.forName("japanese");
/*      */ 
/*  122 */     gcal = CalendarSystem.getGregorianCalendar();
/*      */ 
/*  125 */     BEFORE_MEIJI_ERA = new Era("BeforeMeiji", "BM", -9223372036854775808L, false);
/*      */ 
/*  161 */     MIN_VALUES = new int[] { 0, -292275055, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, -46800000, 0 };
/*      */ 
/*  180 */     LEAST_MAX_VALUES = new int[] { 0, 0, 0, 0, 4, 28, 0, 7, 4, 1, 11, 23, 59, 59, 999, 50400000, 1200000 };
/*      */ 
/*  199 */     MAX_VALUES = new int[] { 0, 292278994, 11, 53, 6, 31, 366, 7, 6, 1, 11, 23, 59, 59, 999, 50400000, 7200000 };
/*      */ 
/*  223 */     Era[] arrayOfEra1 = jcal.getEras();
/*  224 */     int i = arrayOfEra1.length + 1;
/*  225 */     eras = new Era[i];
/*  226 */     sinceFixedDates = new long[i];
/*      */ 
/*  230 */     int j = 0;
/*  231 */     sinceFixedDates[j] = gcal.getFixedDate(BEFORE_MEIJI_ERA.getSinceDate());
/*  232 */     eras[(j++)] = BEFORE_MEIJI_ERA;
/*  233 */     for (Era localEra : arrayOfEra1) {
/*  234 */       CalendarDate localCalendarDate1 = localEra.getSinceDate();
/*  235 */       sinceFixedDates[j] = gcal.getFixedDate(localCalendarDate1);
/*  236 */       eras[(j++)] = localEra;
/*      */     }
/*      */     int tmp466_465 = (eras.length - 1); MAX_VALUES[0] = tmp466_465; LEAST_MAX_VALUES[0] = tmp466_465;
/*      */ 
/*  244 */     int k = 2147483647;
/*  245 */     ??? = 2147483647;
/*  246 */     Gregorian.Date localDate = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
/*  247 */     for (int i1 = 1; i1 < eras.length; i1++) {
/*  248 */       long l1 = sinceFixedDates[i1];
/*  249 */       CalendarDate localCalendarDate2 = eras[i1].getSinceDate();
/*  250 */       localDate.setDate(localCalendarDate2.getYear(), 1, 1);
/*  251 */       long l2 = gcal.getFixedDate(localDate);
/*  252 */       ??? = Math.min((int)(l2 - l1), ???);
/*  253 */       localDate.setDate(localCalendarDate2.getYear(), 12, 31);
/*  254 */       l2 = gcal.getFixedDate(localDate) + 1L;
/*  255 */       ??? = Math.min((int)(l1 - l2), ???);
/*      */ 
/*  257 */       LocalGregorianCalendar.Date localDate1 = getCalendarDate(l1 - 1L);
/*  258 */       int i2 = localDate1.getYear();
/*      */ 
/*  263 */       if ((localDate1.getMonth() != 1) || (localDate1.getDayOfMonth() != 1))
/*  264 */         i2--;
/*  265 */       k = Math.min(i2, k);
/*      */     }
/*  267 */     LEAST_MAX_VALUES[1] = k;
/*  268 */     LEAST_MAX_VALUES[6] = ???;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.JapaneseImperialCalendar
 * JD-Core Version:    0.6.2
 */