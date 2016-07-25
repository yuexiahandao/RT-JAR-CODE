/*     */ package sun.util.calendar;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ class ImmutableGregorianDate extends BaseCalendar.Date
/*     */ {
/*     */   private final BaseCalendar.Date date;
/*     */ 
/*     */   ImmutableGregorianDate(BaseCalendar.Date paramDate)
/*     */   {
/*  35 */     if (paramDate == null) {
/*  36 */       throw new NullPointerException();
/*     */     }
/*  38 */     this.date = paramDate;
/*     */   }
/*     */ 
/*     */   public Era getEra() {
/*  42 */     return this.date.getEra();
/*     */   }
/*     */ 
/*     */   public CalendarDate setEra(Era paramEra) {
/*  46 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public int getYear() {
/*  50 */     return this.date.getYear();
/*     */   }
/*     */ 
/*     */   public CalendarDate setYear(int paramInt) {
/*  54 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addYear(int paramInt) {
/*  58 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public boolean isLeapYear() {
/*  62 */     return this.date.isLeapYear();
/*     */   }
/*     */ 
/*     */   void setLeapYear(boolean paramBoolean) {
/*  66 */     unsupported();
/*     */   }
/*     */ 
/*     */   public int getMonth() {
/*  70 */     return this.date.getMonth();
/*     */   }
/*     */ 
/*     */   public CalendarDate setMonth(int paramInt) {
/*  74 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addMonth(int paramInt) {
/*  78 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public int getDayOfMonth() {
/*  82 */     return this.date.getDayOfMonth();
/*     */   }
/*     */ 
/*     */   public CalendarDate setDayOfMonth(int paramInt) {
/*  86 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addDayOfMonth(int paramInt) {
/*  90 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public int getDayOfWeek() {
/*  94 */     return this.date.getDayOfWeek();
/*     */   }
/*     */ 
/*     */   public int getHours() {
/*  98 */     return this.date.getHours();
/*     */   }
/*     */ 
/*     */   public CalendarDate setHours(int paramInt) {
/* 102 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addHours(int paramInt) {
/* 106 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public int getMinutes() {
/* 110 */     return this.date.getMinutes();
/*     */   }
/*     */ 
/*     */   public CalendarDate setMinutes(int paramInt) {
/* 114 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addMinutes(int paramInt) {
/* 118 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public int getSeconds() {
/* 122 */     return this.date.getSeconds();
/*     */   }
/*     */ 
/*     */   public CalendarDate setSeconds(int paramInt) {
/* 126 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addSeconds(int paramInt) {
/* 130 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public int getMillis() {
/* 134 */     return this.date.getMillis();
/*     */   }
/*     */ 
/*     */   public CalendarDate setMillis(int paramInt) {
/* 138 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addMillis(int paramInt) {
/* 142 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public long getTimeOfDay() {
/* 146 */     return this.date.getTimeOfDay();
/*     */   }
/*     */ 
/*     */   public CalendarDate setDate(int paramInt1, int paramInt2, int paramInt3) {
/* 150 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addDate(int paramInt1, int paramInt2, int paramInt3) {
/* 154 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate setTimeOfDay(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 158 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public CalendarDate addTimeOfDay(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 162 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   protected void setTimeOfDay(long paramLong) {
/* 166 */     unsupported();
/*     */   }
/*     */ 
/*     */   public boolean isNormalized() {
/* 170 */     return this.date.isNormalized();
/*     */   }
/*     */ 
/*     */   public boolean isStandardTime() {
/* 174 */     return this.date.isStandardTime();
/*     */   }
/*     */ 
/*     */   public void setStandardTime(boolean paramBoolean) {
/* 178 */     unsupported();
/*     */   }
/*     */ 
/*     */   public boolean isDaylightTime() {
/* 182 */     return this.date.isDaylightTime();
/*     */   }
/*     */ 
/*     */   protected void setLocale(Locale paramLocale) {
/* 186 */     unsupported();
/*     */   }
/*     */ 
/*     */   public TimeZone getZone() {
/* 190 */     return this.date.getZone();
/*     */   }
/*     */ 
/*     */   public CalendarDate setZone(TimeZone paramTimeZone) {
/* 194 */     unsupported(); return this;
/*     */   }
/*     */ 
/*     */   public boolean isSameDate(CalendarDate paramCalendarDate) {
/* 198 */     return paramCalendarDate.isSameDate(paramCalendarDate);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 202 */     if (this == paramObject) {
/* 203 */       return true;
/*     */     }
/* 205 */     if (!(paramObject instanceof ImmutableGregorianDate)) {
/* 206 */       return false;
/*     */     }
/* 208 */     return this.date.equals(((ImmutableGregorianDate)paramObject).date);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 212 */     return this.date.hashCode();
/*     */   }
/*     */ 
/*     */   public Object clone() {
/* 216 */     return super.clone();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 220 */     return this.date.toString();
/*     */   }
/*     */ 
/*     */   protected void setDayOfWeek(int paramInt) {
/* 224 */     unsupported();
/*     */   }
/*     */ 
/*     */   protected void setNormalized(boolean paramBoolean) {
/* 228 */     unsupported();
/*     */   }
/*     */ 
/*     */   public int getZoneOffset() {
/* 232 */     return this.date.getZoneOffset();
/*     */   }
/*     */ 
/*     */   protected void setZoneOffset(int paramInt) {
/* 236 */     unsupported();
/*     */   }
/*     */ 
/*     */   public int getDaylightSaving() {
/* 240 */     return this.date.getDaylightSaving();
/*     */   }
/*     */ 
/*     */   protected void setDaylightSaving(int paramInt) {
/* 244 */     unsupported();
/*     */   }
/*     */ 
/*     */   public int getNormalizedYear() {
/* 248 */     return this.date.getNormalizedYear();
/*     */   }
/*     */ 
/*     */   public void setNormalizedYear(int paramInt) {
/* 252 */     unsupported();
/*     */   }
/*     */ 
/*     */   private void unsupported() {
/* 256 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.calendar.ImmutableGregorianDate
 * JD-Core Version:    0.6.2
 */