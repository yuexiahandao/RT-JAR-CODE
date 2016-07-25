/*     */ package sun.util.calendar;
/*     */ 
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ public abstract class CalendarSystem
/*     */ {
/*  79 */   private static volatile boolean initialized = false;
/*     */   private static ConcurrentMap<String, String> names;
/*     */   private static ConcurrentMap<String, CalendarSystem> calendars;
/*     */   private static final String PACKAGE_NAME = "sun.util.calendar.";
/*  89 */   private static final String[] namePairs = { "gregorian", "Gregorian", "japanese", "LocalGregorianCalendar", "julian", "JulianCalendar" };
/*     */ 
/* 121 */   private static final Gregorian GREGORIAN_INSTANCE = new Gregorian();
/*     */ 
/*     */   private static void initNames()
/*     */   {
/* 102 */     ConcurrentHashMap localConcurrentHashMap = new ConcurrentHashMap();
/*     */ 
/* 106 */     StringBuilder localStringBuilder = new StringBuilder();
/* 107 */     for (int i = 0; i < namePairs.length; i += 2) {
/* 108 */       localStringBuilder.setLength(0);
/* 109 */       String str = "sun.util.calendar." + namePairs[(i + 1)];
/* 110 */       localConcurrentHashMap.put(namePairs[i], str);
/*     */     }
/* 112 */     synchronized (CalendarSystem.class) {
/* 113 */       if (!initialized) {
/* 114 */         names = localConcurrentHashMap;
/* 115 */         calendars = new ConcurrentHashMap();
/* 116 */         initialized = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Gregorian getGregorianCalendar()
/*     */   {
/* 130 */     return GREGORIAN_INSTANCE;
/*     */   }
/*     */ 
/*     */   public static CalendarSystem forName(String paramString)
/*     */   {
/* 144 */     if ("gregorian".equals(paramString)) {
/* 145 */       return GREGORIAN_INSTANCE;
/*     */     }
/*     */ 
/* 148 */     if (!initialized) {
/* 149 */       initNames();
/*     */     }
/*     */ 
/* 152 */     Object localObject = (CalendarSystem)calendars.get(paramString);
/* 153 */     if (localObject != null) {
/* 154 */       return localObject;
/*     */     }
/*     */ 
/* 157 */     String str = (String)names.get(paramString);
/* 158 */     if (str == null) {
/* 159 */       return null;
/*     */     }
/*     */ 
/* 162 */     if (str.endsWith("LocalGregorianCalendar"))
/*     */     {
/* 164 */       localObject = LocalGregorianCalendar.getLocalGregorianCalendar(paramString);
/*     */     }
/*     */     else try {
/* 167 */         Class localClass = Class.forName(str);
/* 168 */         localObject = (CalendarSystem)localClass.newInstance();
/*     */       } catch (Exception localException) {
/* 170 */         throw new RuntimeException("internal error", localException);
/*     */       }
/*     */ 
/* 173 */     if (localObject == null) {
/* 174 */       return null;
/*     */     }
/* 176 */     CalendarSystem localCalendarSystem = (CalendarSystem)calendars.putIfAbsent(paramString, localObject);
/* 177 */     return localCalendarSystem == null ? localObject : localCalendarSystem;
/*     */   }
/*     */ 
/*     */   public abstract String getName();
/*     */ 
/*     */   public abstract CalendarDate getCalendarDate();
/*     */ 
/*     */   public abstract CalendarDate getCalendarDate(long paramLong);
/*     */ 
/*     */   public abstract CalendarDate getCalendarDate(long paramLong, CalendarDate paramCalendarDate);
/*     */ 
/*     */   public abstract CalendarDate getCalendarDate(long paramLong, TimeZone paramTimeZone);
/*     */ 
/*     */   public abstract CalendarDate newCalendarDate();
/*     */ 
/*     */   public abstract CalendarDate newCalendarDate(TimeZone paramTimeZone);
/*     */ 
/*     */   public abstract long getTime(CalendarDate paramCalendarDate);
/*     */ 
/*     */   public abstract int getYearLength(CalendarDate paramCalendarDate);
/*     */ 
/*     */   public abstract int getYearLengthInMonths(CalendarDate paramCalendarDate);
/*     */ 
/*     */   public abstract int getMonthLength(CalendarDate paramCalendarDate);
/*     */ 
/*     */   public abstract int getWeekLength();
/*     */ 
/*     */   public abstract Era getEra(String paramString);
/*     */ 
/*     */   public abstract Era[] getEras();
/*     */ 
/*     */   public abstract void setEra(CalendarDate paramCalendarDate, String paramString);
/*     */ 
/*     */   public abstract CalendarDate getNthDayOfWeek(int paramInt1, int paramInt2, CalendarDate paramCalendarDate);
/*     */ 
/*     */   public abstract CalendarDate setTimeOfDay(CalendarDate paramCalendarDate, int paramInt);
/*     */ 
/*     */   public abstract boolean validate(CalendarDate paramCalendarDate);
/*     */ 
/*     */   public abstract boolean normalize(CalendarDate paramCalendarDate);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.calendar.CalendarSystem
 * JD-Core Version:    0.6.2
 */