/*     */ package sun.util.calendar;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public final class Era
/*     */ {
/*     */   private final String name;
/*     */   private final String abbr;
/*     */   private final long since;
/*     */   private final CalendarDate sinceDate;
/*     */   private final boolean localTime;
/* 135 */   private int hash = 0;
/*     */ 
/*     */   public Era(String paramString1, String paramString2, long paramLong, boolean paramBoolean)
/*     */   {
/*  82 */     this.name = paramString1;
/*  83 */     this.abbr = paramString2;
/*  84 */     this.since = paramLong;
/*  85 */     this.localTime = paramBoolean;
/*  86 */     Gregorian localGregorian = CalendarSystem.getGregorianCalendar();
/*  87 */     Gregorian.Date localDate = localGregorian.newCalendarDate(null);
/*  88 */     localGregorian.getCalendarDate(paramLong, localDate);
/*  89 */     this.sinceDate = new ImmutableGregorianDate(localDate);
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  93 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getDisplayName(Locale paramLocale) {
/*  97 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getAbbreviation() {
/* 101 */     return this.abbr;
/*     */   }
/*     */ 
/*     */   public String getDiaplayAbbreviation(Locale paramLocale) {
/* 105 */     return this.abbr;
/*     */   }
/*     */ 
/*     */   public long getSince(TimeZone paramTimeZone) {
/* 109 */     if ((paramTimeZone == null) || (!this.localTime)) {
/* 110 */       return this.since;
/*     */     }
/* 112 */     int i = paramTimeZone.getOffset(this.since);
/* 113 */     return this.since - i;
/*     */   }
/*     */ 
/*     */   public CalendarDate getSinceDate() {
/* 117 */     return this.sinceDate;
/*     */   }
/*     */ 
/*     */   public boolean isLocalTime() {
/* 121 */     return this.localTime;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 125 */     if (!(paramObject instanceof Era)) {
/* 126 */       return false;
/*     */     }
/* 128 */     Era localEra = (Era)paramObject;
/* 129 */     return (this.name.equals(localEra.name)) && (this.abbr.equals(localEra.abbr)) && (this.since == localEra.since) && (this.localTime == localEra.localTime);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 138 */     if (this.hash == 0) {
/* 139 */       this.hash = (this.name.hashCode() ^ this.abbr.hashCode() ^ (int)this.since ^ (int)(this.since >> 32) ^ (this.localTime ? 1 : 0));
/*     */     }
/*     */ 
/* 142 */     return this.hash;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 146 */     StringBuilder localStringBuilder = new StringBuilder();
/* 147 */     localStringBuilder.append('[');
/* 148 */     localStringBuilder.append(getName()).append(" (");
/* 149 */     localStringBuilder.append(getAbbreviation()).append(')');
/* 150 */     localStringBuilder.append(" since ").append(getSinceDate());
/* 151 */     if (this.localTime) {
/* 152 */       localStringBuilder.setLength(localStringBuilder.length() - 1);
/* 153 */       localStringBuilder.append(" local time");
/*     */     }
/* 155 */     localStringBuilder.append(']');
/* 156 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.calendar.Era
 * JD-Core Version:    0.6.2
 */