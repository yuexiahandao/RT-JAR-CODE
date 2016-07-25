/*     */ package sun.util.calendar;
/*     */ 
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public abstract class BaseCalendar extends AbstractCalendar
/*     */ {
/*     */   public static final int JANUARY = 1;
/*     */   public static final int FEBRUARY = 2;
/*     */   public static final int MARCH = 3;
/*     */   public static final int APRIL = 4;
/*     */   public static final int MAY = 5;
/*     */   public static final int JUNE = 6;
/*     */   public static final int JULY = 7;
/*     */   public static final int AUGUST = 8;
/*     */   public static final int SEPTEMBER = 9;
/*     */   public static final int OCTOBER = 10;
/*     */   public static final int NOVEMBER = 11;
/*     */   public static final int DECEMBER = 12;
/*     */   public static final int SUNDAY = 1;
/*     */   public static final int MONDAY = 2;
/*     */   public static final int TUESDAY = 3;
/*     */   public static final int WEDNESDAY = 4;
/*     */   public static final int THURSDAY = 5;
/*     */   public static final int FRIDAY = 6;
/*     */   public static final int SATURDAY = 7;
/*     */   private static final int BASE_YEAR = 1970;
/*  71 */   private static final int[] FIXED_DATES = { 719163, 719528, 719893, 720259, 720624, 720989, 721354, 721720, 722085, 722450, 722815, 723181, 723546, 723911, 724276, 724642, 725007, 725372, 725737, 726103, 726468, 726833, 727198, 727564, 727929, 728294, 728659, 729025, 729390, 729755, 730120, 730486, 730851, 731216, 731581, 731947, 732312, 732677, 733042, 733408, 733773, 734138, 734503, 734869, 735234, 735599, 735964, 736330, 736695, 737060, 737425, 737791, 738156, 738521, 738886, 739252, 739617, 739982, 740347, 740713, 741078, 741443, 741808, 742174, 742539, 742904, 743269, 743635, 744000, 744365 };
/*     */ 
/* 311 */   static final int[] DAYS_IN_MONTH = { 31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*     */ 
/* 314 */   static final int[] ACCUMULATED_DAYS_IN_MONTH = { -30, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
/*     */ 
/* 318 */   static final int[] ACCUMULATED_DAYS_IN_MONTH_LEAP = { -30, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 };
/*     */ 
/*     */   public boolean validate(CalendarDate paramCalendarDate)
/*     */   {
/* 193 */     Date localDate = (Date)paramCalendarDate;
/* 194 */     if (localDate.isNormalized()) {
/* 195 */       return true;
/*     */     }
/* 197 */     int i = localDate.getMonth();
/* 198 */     if ((i < 1) || (i > 12)) {
/* 199 */       return false;
/*     */     }
/* 201 */     int j = localDate.getDayOfMonth();
/* 202 */     if ((j <= 0) || (j > getMonthLength(localDate.getNormalizedYear(), i))) {
/* 203 */       return false;
/*     */     }
/* 205 */     int k = localDate.getDayOfWeek();
/* 206 */     if ((k != -2147483648) && (k != getDayOfWeek(localDate))) {
/* 207 */       return false;
/*     */     }
/*     */ 
/* 210 */     if (!validateTime(paramCalendarDate)) {
/* 211 */       return false;
/*     */     }
/*     */ 
/* 214 */     localDate.setNormalized(true);
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean normalize(CalendarDate paramCalendarDate) {
/* 219 */     if (paramCalendarDate.isNormalized()) {
/* 220 */       return true;
/*     */     }
/*     */ 
/* 223 */     Date localDate = (Date)paramCalendarDate;
/* 224 */     TimeZone localTimeZone = localDate.getZone();
/*     */ 
/* 228 */     if (localTimeZone != null) {
/* 229 */       getTime(paramCalendarDate);
/* 230 */       return true;
/*     */     }
/*     */ 
/* 233 */     int i = normalizeTime(localDate);
/* 234 */     normalizeMonth(localDate);
/* 235 */     long l1 = localDate.getDayOfMonth() + i;
/* 236 */     int j = localDate.getMonth();
/* 237 */     int k = localDate.getNormalizedYear();
/* 238 */     int m = getMonthLength(k, j);
/*     */ 
/* 240 */     if ((l1 <= 0L) || (l1 > m)) {
/* 241 */       if ((l1 <= 0L) && (l1 > -28L)) {
/* 242 */         m = getMonthLength(k, --j);
/* 243 */         l1 += m;
/* 244 */         localDate.setDayOfMonth((int)l1);
/* 245 */         if (j == 0) {
/* 246 */           j = 12;
/* 247 */           localDate.setNormalizedYear(k - 1);
/*     */         }
/* 249 */         localDate.setMonth(j);
/* 250 */       } else if ((l1 > m) && (l1 < m + 28)) {
/* 251 */         l1 -= m;
/* 252 */         j++;
/* 253 */         localDate.setDayOfMonth((int)l1);
/* 254 */         if (j > 12) {
/* 255 */           localDate.setNormalizedYear(k + 1);
/* 256 */           j = 1;
/*     */         }
/* 258 */         localDate.setMonth(j);
/*     */       } else {
/* 260 */         long l2 = l1 + getFixedDate(k, j, 1, localDate) - 1L;
/* 261 */         getCalendarDateFromFixedDate(localDate, l2);
/*     */       }
/*     */     }
/* 264 */     else localDate.setDayOfWeek(getDayOfWeek(localDate));
/*     */ 
/* 266 */     paramCalendarDate.setLeapYear(isLeapYear(localDate.getNormalizedYear()));
/* 267 */     paramCalendarDate.setZoneOffset(0);
/* 268 */     paramCalendarDate.setDaylightSaving(0);
/* 269 */     localDate.setNormalized(true);
/* 270 */     return true;
/*     */   }
/*     */ 
/*     */   void normalizeMonth(CalendarDate paramCalendarDate) {
/* 274 */     Date localDate = (Date)paramCalendarDate;
/* 275 */     int i = localDate.getNormalizedYear();
/* 276 */     long l1 = localDate.getMonth();
/* 277 */     if (l1 <= 0L) {
/* 278 */       long l2 = 1L - l1;
/* 279 */       i -= (int)(l2 / 12L + 1L);
/* 280 */       l1 = 13L - l2 % 12L;
/* 281 */       localDate.setNormalizedYear(i);
/* 282 */       localDate.setMonth((int)l1);
/* 283 */     } else if (l1 > 12L) {
/* 284 */       i += (int)((l1 - 1L) / 12L);
/* 285 */       l1 = (l1 - 1L) % 12L + 1L;
/* 286 */       localDate.setNormalizedYear(i);
/* 287 */       localDate.setMonth((int)l1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getYearLength(CalendarDate paramCalendarDate)
/*     */   {
/* 304 */     return isLeapYear(((Date)paramCalendarDate).getNormalizedYear()) ? 366 : 365;
/*     */   }
/*     */ 
/*     */   public int getYearLengthInMonths(CalendarDate paramCalendarDate) {
/* 308 */     return 12;
/*     */   }
/*     */ 
/*     */   public int getMonthLength(CalendarDate paramCalendarDate)
/*     */   {
/* 323 */     Date localDate = (Date)paramCalendarDate;
/* 324 */     int i = localDate.getMonth();
/* 325 */     if ((i < 1) || (i > 12)) {
/* 326 */       throw new IllegalArgumentException("Illegal month value: " + i);
/*     */     }
/* 328 */     return getMonthLength(localDate.getNormalizedYear(), i);
/*     */   }
/*     */ 
/*     */   private final int getMonthLength(int paramInt1, int paramInt2)
/*     */   {
/* 333 */     int i = DAYS_IN_MONTH[paramInt2];
/* 334 */     if ((paramInt2 == 2) && (isLeapYear(paramInt1))) {
/* 335 */       i++;
/*     */     }
/* 337 */     return i;
/*     */   }
/*     */ 
/*     */   public long getDayOfYear(CalendarDate paramCalendarDate) {
/* 341 */     return getDayOfYear(((Date)paramCalendarDate).getNormalizedYear(), paramCalendarDate.getMonth(), paramCalendarDate.getDayOfMonth());
/*     */   }
/*     */ 
/*     */   final long getDayOfYear(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 347 */     return paramInt3 + (isLeapYear(paramInt1) ? ACCUMULATED_DAYS_IN_MONTH_LEAP[paramInt2] : ACCUMULATED_DAYS_IN_MONTH[paramInt2]);
/*     */   }
/*     */ 
/*     */   public long getFixedDate(CalendarDate paramCalendarDate)
/*     */   {
/* 354 */     if (!paramCalendarDate.isNormalized()) {
/* 355 */       normalizeMonth(paramCalendarDate);
/*     */     }
/* 357 */     return getFixedDate(((Date)paramCalendarDate).getNormalizedYear(), paramCalendarDate.getMonth(), paramCalendarDate.getDayOfMonth(), (Date)paramCalendarDate);
/*     */   }
/*     */ 
/*     */   public long getFixedDate(int paramInt1, int paramInt2, int paramInt3, Date paramDate)
/*     */   {
/* 365 */     int i = (paramInt2 == 1) && (paramInt3 == 1) ? 1 : 0;
/*     */ 
/* 368 */     if ((paramDate != null) && (paramDate.hit(paramInt1))) {
/* 369 */       if (i != 0) {
/* 370 */         return paramDate.getCachedJan1();
/*     */       }
/* 372 */       return paramDate.getCachedJan1() + getDayOfYear(paramInt1, paramInt2, paramInt3) - 1L;
/*     */     }
/*     */ 
/* 376 */     int j = paramInt1 - 1970;
/* 377 */     if ((j >= 0) && (j < FIXED_DATES.length)) {
/* 378 */       l1 = FIXED_DATES[j];
/* 379 */       if (paramDate != null) {
/* 380 */         paramDate.setCache(paramInt1, l1, isLeapYear(paramInt1) ? 366 : 365);
/*     */       }
/* 382 */       return i != 0 ? l1 : l1 + getDayOfYear(paramInt1, paramInt2, paramInt3) - 1L;
/*     */     }
/*     */ 
/* 385 */     long l1 = paramInt1 - 1L;
/* 386 */     long l2 = paramInt3;
/*     */ 
/* 388 */     if (l1 >= 0L) {
/* 389 */       l2 += 365L * l1 + l1 / 4L - l1 / 100L + l1 / 400L + (367 * paramInt2 - 362) / 12;
/*     */     }
/*     */     else
/*     */     {
/* 395 */       l2 += 365L * l1 + CalendarUtils.floorDivide(l1, 4L) - CalendarUtils.floorDivide(l1, 100L) + CalendarUtils.floorDivide(l1, 400L) + CalendarUtils.floorDivide(367 * paramInt2 - 362, 12);
/*     */     }
/*     */ 
/* 402 */     if (paramInt2 > 2) {
/* 403 */       l2 -= (isLeapYear(paramInt1) ? 1L : 2L);
/*     */     }
/*     */ 
/* 407 */     if ((paramDate != null) && (i != 0)) {
/* 408 */       paramDate.setCache(paramInt1, l2, isLeapYear(paramInt1) ? 366 : 365);
/*     */     }
/*     */ 
/* 411 */     return l2;
/*     */   }
/*     */ 
/*     */   public void getCalendarDateFromFixedDate(CalendarDate paramCalendarDate, long paramLong)
/*     */   {
/* 421 */     Date localDate = (Date)paramCalendarDate;
/*     */     int i;
/*     */     long l1;
/*     */     boolean bool;
/* 425 */     if (localDate.hit(paramLong)) {
/* 426 */       i = localDate.getCachedYear();
/* 427 */       l1 = localDate.getCachedJan1();
/* 428 */       bool = isLeapYear(i);
/*     */     }
/*     */     else
/*     */     {
/* 433 */       i = getGregorianYearFromFixedDate(paramLong);
/* 434 */       l1 = getFixedDate(i, 1, 1, null);
/* 435 */       bool = isLeapYear(i);
/*     */ 
/* 437 */       localDate.setCache(i, l1, bool ? 366 : 365);
/*     */     }
/*     */ 
/* 440 */     int j = (int)(paramLong - l1);
/* 441 */     long l2 = l1 + 31L + 28L;
/* 442 */     if (bool) {
/* 443 */       l2 += 1L;
/*     */     }
/* 445 */     if (paramLong >= l2) {
/* 446 */       j += (bool ? 1 : 2);
/*     */     }
/* 448 */     int k = 12 * j + 373;
/* 449 */     if (k > 0)
/* 450 */       k /= 367;
/*     */     else {
/* 452 */       k = CalendarUtils.floorDivide(k, 367);
/*     */     }
/* 454 */     long l3 = l1 + ACCUMULATED_DAYS_IN_MONTH[k];
/* 455 */     if ((bool) && (k >= 3)) {
/* 456 */       l3 += 1L;
/*     */     }
/* 458 */     int m = (int)(paramLong - l3) + 1;
/* 459 */     int n = getDayOfWeekFromFixedDate(paramLong);
/* 460 */     assert (n > 0) : ("negative day of week " + n);
/* 461 */     localDate.setNormalizedYear(i);
/* 462 */     localDate.setMonth(k);
/* 463 */     localDate.setDayOfMonth(m);
/* 464 */     localDate.setDayOfWeek(n);
/* 465 */     localDate.setLeapYear(bool);
/* 466 */     localDate.setNormalized(true);
/*     */   }
/*     */ 
/*     */   public int getDayOfWeek(CalendarDate paramCalendarDate)
/*     */   {
/* 473 */     long l = getFixedDate(paramCalendarDate);
/* 474 */     return getDayOfWeekFromFixedDate(l);
/*     */   }
/*     */ 
/*     */   public static final int getDayOfWeekFromFixedDate(long paramLong)
/*     */   {
/* 479 */     if (paramLong >= 0L) {
/* 480 */       return (int)(paramLong % 7L) + 1;
/*     */     }
/* 482 */     return (int)CalendarUtils.mod(paramLong, 7L) + 1;
/*     */   }
/*     */ 
/*     */   public int getYearFromFixedDate(long paramLong) {
/* 486 */     return getGregorianYearFromFixedDate(paramLong);
/*     */   }
/*     */ 
/*     */   final int getGregorianYearFromFixedDate(long paramLong)
/*     */   {
/*     */     long l;
/*     */     int n;
/*     */     int i;
/*     */     int i1;
/*     */     int j;
/*     */     int i2;
/*     */     int k;
/*     */     int i3;
/*     */     int m;
/* 498 */     if (paramLong > 0L) {
/* 499 */       l = paramLong - 1L;
/* 500 */       n = (int)(l / 146097L);
/* 501 */       i = (int)(l % 146097L);
/* 502 */       i1 = i / 36524;
/* 503 */       j = i % 36524;
/* 504 */       i2 = j / 1461;
/* 505 */       k = j % 1461;
/* 506 */       i3 = k / 365;
/* 507 */       m = k % 365 + 1;
/*     */     } else {
/* 509 */       l = paramLong - 1L;
/* 510 */       n = (int)CalendarUtils.floorDivide(l, 146097L);
/* 511 */       i = (int)CalendarUtils.mod(l, 146097L);
/* 512 */       i1 = CalendarUtils.floorDivide(i, 36524);
/* 513 */       j = CalendarUtils.mod(i, 36524);
/* 514 */       i2 = CalendarUtils.floorDivide(j, 1461);
/* 515 */       k = CalendarUtils.mod(j, 1461);
/* 516 */       i3 = CalendarUtils.floorDivide(k, 365);
/* 517 */       m = CalendarUtils.mod(k, 365) + 1;
/*     */     }
/* 519 */     int i4 = 400 * n + 100 * i1 + 4 * i2 + i3;
/* 520 */     if ((i1 != 4) && (i3 != 4)) {
/* 521 */       i4++;
/*     */     }
/* 523 */     return i4;
/*     */   }
/*     */ 
/*     */   protected boolean isLeapYear(CalendarDate paramCalendarDate)
/*     */   {
/* 532 */     return isLeapYear(((Date)paramCalendarDate).getNormalizedYear());
/*     */   }
/*     */ 
/*     */   boolean isLeapYear(int paramInt) {
/* 536 */     return CalendarUtils.isGregorianLeapYear(paramInt);
/*     */   }
/*     */ 
/*     */   public static abstract class Date extends CalendarDate
/*     */   {
/* 165 */     int cachedYear = 2004;
/* 166 */     long cachedFixedDateJan1 = 731581L;
/* 167 */     long cachedFixedDateNextJan1 = this.cachedFixedDateJan1 + 366L;
/*     */ 
/*     */     protected Date()
/*     */     {
/*     */     }
/*     */ 
/*     */     protected Date(TimeZone paramTimeZone)
/*     */     {
/* 149 */       super();
/*     */     }
/*     */ 
/*     */     public Date setNormalizedDate(int paramInt1, int paramInt2, int paramInt3) {
/* 153 */       setNormalizedYear(paramInt1);
/* 154 */       setMonth(paramInt2).setDayOfMonth(paramInt3);
/* 155 */       return this;
/*     */     }
/*     */ 
/*     */     public abstract int getNormalizedYear();
/*     */ 
/*     */     public abstract void setNormalizedYear(int paramInt);
/*     */ 
/*     */     protected final boolean hit(int paramInt)
/*     */     {
/* 170 */       return paramInt == this.cachedYear;
/*     */     }
/*     */ 
/*     */     protected final boolean hit(long paramLong) {
/* 174 */       return (paramLong >= this.cachedFixedDateJan1) && (paramLong < this.cachedFixedDateNextJan1);
/*     */     }
/*     */ 
/*     */     protected int getCachedYear() {
/* 178 */       return this.cachedYear;
/*     */     }
/*     */ 
/*     */     protected long getCachedJan1() {
/* 182 */       return this.cachedFixedDateJan1;
/*     */     }
/*     */ 
/*     */     protected void setCache(int paramInt1, long paramLong, int paramInt2) {
/* 186 */       this.cachedYear = paramInt1;
/* 187 */       this.cachedFixedDateJan1 = paramLong;
/* 188 */       this.cachedFixedDateNextJan1 = (paramLong + paramInt2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.calendar.BaseCalendar
 * JD-Core Version:    0.6.2
 */