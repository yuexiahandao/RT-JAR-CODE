/*      */ package java.text;
/*      */ 
/*      */ import java.io.InvalidObjectException;
/*      */ import java.text.spi.DateFormatProvider;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Locale.Category;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.TimeZone;
/*      */ import sun.util.LocaleServiceProviderPool;
/*      */ import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
/*      */ 
/*      */ public abstract class DateFormat extends Format
/*      */ {
/*      */   protected Calendar calendar;
/*      */   protected NumberFormat numberFormat;
/*      */   public static final int ERA_FIELD = 0;
/*      */   public static final int YEAR_FIELD = 1;
/*      */   public static final int MONTH_FIELD = 2;
/*      */   public static final int DATE_FIELD = 3;
/*      */   public static final int HOUR_OF_DAY1_FIELD = 4;
/*      */   public static final int HOUR_OF_DAY0_FIELD = 5;
/*      */   public static final int MINUTE_FIELD = 6;
/*      */   public static final int SECOND_FIELD = 7;
/*      */   public static final int MILLISECOND_FIELD = 8;
/*      */   public static final int DAY_OF_WEEK_FIELD = 9;
/*      */   public static final int DAY_OF_YEAR_FIELD = 10;
/*      */   public static final int DAY_OF_WEEK_IN_MONTH_FIELD = 11;
/*      */   public static final int WEEK_OF_YEAR_FIELD = 12;
/*      */   public static final int WEEK_OF_MONTH_FIELD = 13;
/*      */   public static final int AM_PM_FIELD = 14;
/*      */   public static final int HOUR1_FIELD = 15;
/*      */   public static final int HOUR0_FIELD = 16;
/*      */   public static final int TIMEZONE_FIELD = 17;
/*      */   private static final long serialVersionUID = 7218322306649953788L;
/*      */   public static final int FULL = 0;
/*      */   public static final int LONG = 1;
/*      */   public static final int MEDIUM = 2;
/*      */   public static final int SHORT = 3;
/*      */   public static final int DEFAULT = 2;
/*      */ 
/*      */   public final StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  295 */     if ((paramObject instanceof Date))
/*  296 */       return format((Date)paramObject, paramStringBuffer, paramFieldPosition);
/*  297 */     if ((paramObject instanceof Number)) {
/*  298 */       return format(new Date(((Number)paramObject).longValue()), paramStringBuffer, paramFieldPosition);
/*      */     }
/*      */ 
/*  301 */     throw new IllegalArgumentException("Cannot format given Object as a Date");
/*      */   }
/*      */ 
/*      */   public abstract StringBuffer format(Date paramDate, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */ 
/*      */   public final String format(Date paramDate)
/*      */   {
/*  336 */     return format(paramDate, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
/*      */   }
/*      */ 
/*      */   public Date parse(String paramString)
/*      */     throws ParseException
/*      */   {
/*  354 */     ParsePosition localParsePosition = new ParsePosition(0);
/*  355 */     Date localDate = parse(paramString, localParsePosition);
/*  356 */     if (localParsePosition.index == 0) {
/*  357 */       throw new ParseException("Unparseable date: \"" + paramString + "\"", localParsePosition.errorIndex);
/*      */     }
/*  359 */     return localDate;
/*      */   }
/*      */ 
/*      */   public abstract Date parse(String paramString, ParsePosition paramParsePosition);
/*      */ 
/*      */   public Object parseObject(String paramString, ParsePosition paramParsePosition)
/*      */   {
/*  415 */     return parse(paramString, paramParsePosition);
/*      */   }
/*      */ 
/*      */   public static final DateFormat getTimeInstance()
/*      */   {
/*  446 */     return get(2, 0, 1, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public static final DateFormat getTimeInstance(int paramInt)
/*      */   {
/*  458 */     return get(paramInt, 0, 1, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public static final DateFormat getTimeInstance(int paramInt, Locale paramLocale)
/*      */   {
/*  472 */     return get(paramInt, 0, 1, paramLocale);
/*      */   }
/*      */ 
/*      */   public static final DateFormat getDateInstance()
/*      */   {
/*  482 */     return get(0, 2, 2, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public static final DateFormat getDateInstance(int paramInt)
/*      */   {
/*  494 */     return get(0, paramInt, 2, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public static final DateFormat getDateInstance(int paramInt, Locale paramLocale)
/*      */   {
/*  508 */     return get(0, paramInt, 2, paramLocale);
/*      */   }
/*      */ 
/*      */   public static final DateFormat getDateTimeInstance()
/*      */   {
/*  518 */     return get(2, 2, 3, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public static final DateFormat getDateTimeInstance(int paramInt1, int paramInt2)
/*      */   {
/*  533 */     return get(paramInt2, paramInt1, 3, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public static final DateFormat getDateTimeInstance(int paramInt1, int paramInt2, Locale paramLocale)
/*      */   {
/*  547 */     return get(paramInt2, paramInt1, 3, paramLocale);
/*      */   }
/*      */ 
/*      */   public static final DateFormat getInstance()
/*      */   {
/*  555 */     return getDateTimeInstance(3, 3);
/*      */   }
/*      */ 
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/*  573 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(DateFormatProvider.class);
/*      */ 
/*  575 */     return localLocaleServiceProviderPool.getAvailableLocales();
/*      */   }
/*      */ 
/*      */   public void setCalendar(Calendar paramCalendar)
/*      */   {
/*  590 */     this.calendar = paramCalendar;
/*      */   }
/*      */ 
/*      */   public Calendar getCalendar()
/*      */   {
/*  600 */     return this.calendar;
/*      */   }
/*      */ 
/*      */   public void setNumberFormat(NumberFormat paramNumberFormat)
/*      */   {
/*  609 */     this.numberFormat = paramNumberFormat;
/*      */   }
/*      */ 
/*      */   public NumberFormat getNumberFormat()
/*      */   {
/*  619 */     return this.numberFormat;
/*      */   }
/*      */ 
/*      */   public void setTimeZone(TimeZone paramTimeZone)
/*      */   {
/*  639 */     this.calendar.setTimeZone(paramTimeZone);
/*      */   }
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  653 */     return this.calendar.getTimeZone();
/*      */   }
/*      */ 
/*      */   public void setLenient(boolean paramBoolean)
/*      */   {
/*  675 */     this.calendar.setLenient(paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isLenient()
/*      */   {
/*  691 */     return this.calendar.isLenient();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  698 */     return this.numberFormat.hashCode();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  706 */     if (this == paramObject) return true;
/*  707 */     if ((paramObject == null) || (getClass() != paramObject.getClass())) return false;
/*  708 */     DateFormat localDateFormat = (DateFormat)paramObject;
/*  709 */     return (this.calendar.getFirstDayOfWeek() == localDateFormat.calendar.getFirstDayOfWeek()) && (this.calendar.getMinimalDaysInFirstWeek() == localDateFormat.calendar.getMinimalDaysInFirstWeek()) && (this.calendar.isLenient() == localDateFormat.calendar.isLenient()) && (this.calendar.getTimeZone().equals(localDateFormat.calendar.getTimeZone())) && (this.numberFormat.equals(localDateFormat.numberFormat));
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  722 */     DateFormat localDateFormat = (DateFormat)super.clone();
/*  723 */     localDateFormat.calendar = ((Calendar)this.calendar.clone());
/*  724 */     localDateFormat.numberFormat = ((NumberFormat)this.numberFormat.clone());
/*  725 */     return localDateFormat;
/*      */   }
/*      */ 
/*      */   private static DateFormat get(int paramInt1, int paramInt2, int paramInt3, Locale paramLocale)
/*      */   {
/*  741 */     if ((paramInt3 & 0x1) != 0) {
/*  742 */       if ((paramInt1 < 0) || (paramInt1 > 3))
/*  743 */         throw new IllegalArgumentException("Illegal time style " + paramInt1);
/*      */     }
/*      */     else {
/*  746 */       paramInt1 = -1;
/*      */     }
/*  748 */     if ((paramInt3 & 0x2) != 0) {
/*  749 */       if ((paramInt2 < 0) || (paramInt2 > 3))
/*  750 */         throw new IllegalArgumentException("Illegal date style " + paramInt2);
/*      */     }
/*      */     else {
/*  753 */       paramInt2 = -1;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  758 */       LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(DateFormatProvider.class);
/*      */ 
/*  760 */       if (localLocaleServiceProviderPool.hasProviders()) {
/*  761 */         DateFormat localDateFormat = (DateFormat)localLocaleServiceProviderPool.getLocalizedObject(DateFormatGetter.INSTANCE, paramLocale, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
/*      */ 
/*  767 */         if (localDateFormat != null) {
/*  768 */           return localDateFormat;
/*      */         }
/*      */       }
/*      */ 
/*  772 */       return new SimpleDateFormat(paramInt1, paramInt2, paramLocale); } catch (MissingResourceException localMissingResourceException) {
/*      */     }
/*  774 */     return new SimpleDateFormat("M/d/yy h:mm a");
/*      */   }
/*      */ 
/*      */   private static class DateFormatGetter
/*      */     implements LocaleServiceProviderPool.LocalizedObjectGetter<DateFormatProvider, DateFormat>
/*      */   {
/* 1004 */     private static final DateFormatGetter INSTANCE = new DateFormatGetter();
/*      */ 
/*      */     public DateFormat getObject(DateFormatProvider paramDateFormatProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */     {
/* 1010 */       assert (paramArrayOfObject.length == 3);
/*      */ 
/* 1012 */       int i = ((Integer)paramArrayOfObject[0]).intValue();
/* 1013 */       int j = ((Integer)paramArrayOfObject[1]).intValue();
/* 1014 */       int k = ((Integer)paramArrayOfObject[2]).intValue();
/*      */ 
/* 1016 */       switch (k) {
/*      */       case 1:
/* 1018 */         return paramDateFormatProvider.getTimeInstance(i, paramLocale);
/*      */       case 2:
/* 1020 */         return paramDateFormatProvider.getDateInstance(j, paramLocale);
/*      */       case 3:
/* 1022 */         return paramDateFormatProvider.getDateTimeInstance(j, i, paramLocale);
/*      */       }
/* 1024 */       if (!$assertionsDisabled) throw new AssertionError("should not happen");
/*      */ 
/* 1027 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Field extends Format.Field
/*      */   {
/*      */     private static final long serialVersionUID = 7441350119349544720L;
/*  801 */     private static final Map instanceMap = new HashMap(18);
/*      */ 
/*  804 */     private static final Field[] calendarToFieldMapping = new Field[17];
/*      */     private int calendarField;
/*  894 */     public static final Field ERA = new Field("era", 0);
/*      */ 
/*  899 */     public static final Field YEAR = new Field("year", 1);
/*      */ 
/*  904 */     public static final Field MONTH = new Field("month", 2);
/*      */ 
/*  909 */     public static final Field DAY_OF_MONTH = new Field("day of month", 5);
/*      */ 
/*  916 */     public static final Field HOUR_OF_DAY1 = new Field("hour of day 1", -1);
/*      */ 
/*  922 */     public static final Field HOUR_OF_DAY0 = new Field("hour of day", 11);
/*      */ 
/*  928 */     public static final Field MINUTE = new Field("minute", 12);
/*      */ 
/*  933 */     public static final Field SECOND = new Field("second", 13);
/*      */ 
/*  938 */     public static final Field MILLISECOND = new Field("millisecond", 14);
/*      */ 
/*  944 */     public static final Field DAY_OF_WEEK = new Field("day of week", 7);
/*      */ 
/*  950 */     public static final Field DAY_OF_YEAR = new Field("day of year", 6);
/*      */ 
/*  956 */     public static final Field DAY_OF_WEEK_IN_MONTH = new Field("day of week in month", 8);
/*      */ 
/*  963 */     public static final Field WEEK_OF_YEAR = new Field("week of year", 3);
/*      */ 
/*  969 */     public static final Field WEEK_OF_MONTH = new Field("week of month", 4);
/*      */ 
/*  976 */     public static final Field AM_PM = new Field("am pm", 9);
/*      */ 
/*  983 */     public static final Field HOUR1 = new Field("hour 1", -1);
/*      */ 
/*  989 */     public static final Field HOUR0 = new Field("hour", 10);
/*      */ 
/*  995 */     public static final Field TIME_ZONE = new Field("time zone", -1);
/*      */ 
/*      */     public static Field ofCalendarField(int paramInt)
/*      */     {
/*  823 */       if ((paramInt < 0) || (paramInt >= calendarToFieldMapping.length))
/*      */       {
/*  825 */         throw new IllegalArgumentException("Unknown Calendar constant " + paramInt);
/*      */       }
/*      */ 
/*  828 */       return calendarToFieldMapping[paramInt];
/*      */     }
/*      */ 
/*      */     protected Field(String paramString, int paramInt)
/*      */     {
/*  842 */       super();
/*  843 */       this.calendarField = paramInt;
/*  844 */       if (getClass() == Field.class) {
/*  845 */         instanceMap.put(paramString, this);
/*  846 */         if (paramInt >= 0)
/*      */         {
/*  848 */           calendarToFieldMapping[paramInt] = this;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getCalendarField()
/*      */     {
/*  864 */       return this.calendarField;
/*      */     }
/*      */ 
/*      */     protected Object readResolve()
/*      */       throws InvalidObjectException
/*      */     {
/*  875 */       if (getClass() != Field.class) {
/*  876 */         throw new InvalidObjectException("subclass didn't correctly implement readResolve");
/*      */       }
/*      */ 
/*  879 */       Object localObject = instanceMap.get(getName());
/*  880 */       if (localObject != null) {
/*  881 */         return localObject;
/*      */       }
/*  883 */       throw new InvalidObjectException("unknown attribute name");
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.DateFormat
 * JD-Core Version:    0.6.2
 */