/*      */ package javax.xml.datatype;
/*      */ 
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ public abstract class DatatypeFactory
/*      */ {
/*      */   public static final String DATATYPEFACTORY_PROPERTY = "javax.xml.datatype.DatatypeFactory";
/*   89 */   public static final String DATATYPEFACTORY_IMPLEMENTATION_CLASS = new String("com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl");
/*      */ 
/*   98 */   private static final Pattern XDTSCHEMA_YMD = Pattern.compile("[^DT]*");
/*      */ 
/*  101 */   private static final Pattern XDTSCHEMA_DTD = Pattern.compile("[^YM]*[DT].*");
/*      */ 
/*      */   public static DatatypeFactory newInstance()
/*      */     throws DatatypeConfigurationException
/*      */   {
/*      */     try
/*      */     {
/*  129 */       return (DatatypeFactory)FactoryFinder.find("javax.xml.datatype.DatatypeFactory", DATATYPEFACTORY_IMPLEMENTATION_CLASS);
/*      */     }
/*      */     catch (FactoryFinder.ConfigurationError e)
/*      */     {
/*  135 */       throw new DatatypeConfigurationException(e.getMessage(), e.getException());
/*      */     }
/*      */   }
/*      */ 
/*      */   public static DatatypeFactory newInstance(String factoryClassName, ClassLoader classLoader)
/*      */     throws DatatypeConfigurationException
/*      */   {
/*      */     try
/*      */     {
/*  176 */       return (DatatypeFactory)FactoryFinder.newInstance(factoryClassName, classLoader, false);
/*      */     } catch (FactoryFinder.ConfigurationError e) {
/*  178 */       throw new DatatypeConfigurationException(e.getMessage(), e.getException());
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract Duration newDuration(String paramString);
/*      */ 
/*      */   public abstract Duration newDuration(long paramLong);
/*      */ 
/*      */   public abstract Duration newDuration(boolean paramBoolean, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigDecimal paramBigDecimal);
/*      */ 
/*      */   public Duration newDuration(boolean isPositive, int years, int months, int days, int hours, int minutes, int seconds)
/*      */   {
/*  324 */     BigInteger realYears = years != -2147483648 ? BigInteger.valueOf(years) : null;
/*      */ 
/*  327 */     BigInteger realMonths = months != -2147483648 ? BigInteger.valueOf(months) : null;
/*      */ 
/*  330 */     BigInteger realDays = days != -2147483648 ? BigInteger.valueOf(days) : null;
/*      */ 
/*  333 */     BigInteger realHours = hours != -2147483648 ? BigInteger.valueOf(hours) : null;
/*      */ 
/*  336 */     BigInteger realMinutes = minutes != -2147483648 ? BigInteger.valueOf(minutes) : null;
/*      */ 
/*  339 */     BigDecimal realSeconds = seconds != -2147483648 ? BigDecimal.valueOf(seconds) : null;
/*      */ 
/*  341 */     return newDuration(isPositive, realYears, realMonths, realDays, realHours, realMinutes, realSeconds);
/*      */   }
/*      */ 
/*      */   public Duration newDurationDayTime(String lexicalRepresentation)
/*      */   {
/*  378 */     if (lexicalRepresentation == null) {
/*  379 */       throw new NullPointerException("Trying to create an xdt:dayTimeDuration with an invalid lexical representation of \"null\"");
/*      */     }
/*      */ 
/*  385 */     Matcher matcher = XDTSCHEMA_DTD.matcher(lexicalRepresentation);
/*  386 */     if (!matcher.matches()) {
/*  387 */       throw new IllegalArgumentException("Trying to create an xdt:dayTimeDuration with an invalid lexical representation of \"" + lexicalRepresentation + "\", data model requires years and months only.");
/*      */     }
/*      */ 
/*  393 */     return newDuration(lexicalRepresentation);
/*      */   }
/*      */ 
/*      */   public Duration newDurationDayTime(long durationInMilliseconds)
/*      */   {
/*  435 */     return newDuration(durationInMilliseconds);
/*      */   }
/*      */ 
/*      */   public Duration newDurationDayTime(boolean isPositive, BigInteger day, BigInteger hour, BigInteger minute, BigInteger second)
/*      */   {
/*  477 */     return newDuration(isPositive, null, null, day, hour, minute, second != null ? new BigDecimal(second) : null);
/*      */   }
/*      */ 
/*      */   public Duration newDurationDayTime(boolean isPositive, int day, int hour, int minute, int second)
/*      */   {
/*  520 */     return newDurationDayTime(isPositive, BigInteger.valueOf(day), BigInteger.valueOf(hour), BigInteger.valueOf(minute), BigInteger.valueOf(second));
/*      */   }
/*      */ 
/*      */   public Duration newDurationYearMonth(String lexicalRepresentation)
/*      */   {
/*  557 */     if (lexicalRepresentation == null) {
/*  558 */       throw new NullPointerException("Trying to create an xdt:yearMonthDuration with an invalid lexical representation of \"null\"");
/*      */     }
/*      */ 
/*  564 */     Matcher matcher = XDTSCHEMA_YMD.matcher(lexicalRepresentation);
/*  565 */     if (!matcher.matches()) {
/*  566 */       throw new IllegalArgumentException("Trying to create an xdt:yearMonthDuration with an invalid lexical representation of \"" + lexicalRepresentation + "\", data model requires days and times only.");
/*      */     }
/*      */ 
/*  572 */     return newDuration(lexicalRepresentation);
/*      */   }
/*      */ 
/*      */   public Duration newDurationYearMonth(long durationInMilliseconds)
/*      */   {
/*  615 */     Duration fullDuration = newDuration(durationInMilliseconds);
/*  616 */     boolean isPositive = fullDuration.getSign() != -1;
/*  617 */     BigInteger years = (BigInteger)fullDuration.getField(DatatypeConstants.YEARS);
/*      */ 
/*  619 */     if (years == null) years = BigInteger.ZERO;
/*  620 */     BigInteger months = (BigInteger)fullDuration.getField(DatatypeConstants.MONTHS);
/*      */ 
/*  622 */     if (months == null) months = BigInteger.ZERO;
/*      */ 
/*  624 */     return newDurationYearMonth(isPositive, years, months);
/*      */   }
/*      */ 
/*      */   public Duration newDurationYearMonth(boolean isPositive, BigInteger year, BigInteger month)
/*      */   {
/*  657 */     return newDuration(isPositive, year, month, null, null, null, null);
/*      */   }
/*      */ 
/*      */   public Duration newDurationYearMonth(boolean isPositive, int year, int month)
/*      */   {
/*  691 */     return newDurationYearMonth(isPositive, BigInteger.valueOf(year), BigInteger.valueOf(month));
/*      */   }
/*      */ 
/*      */   public abstract XMLGregorianCalendar newXMLGregorianCalendar();
/*      */ 
/*      */   public abstract XMLGregorianCalendar newXMLGregorianCalendar(String paramString);
/*      */ 
/*      */   public abstract XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar paramGregorianCalendar);
/*      */ 
/*      */   public abstract XMLGregorianCalendar newXMLGregorianCalendar(BigInteger paramBigInteger, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BigDecimal paramBigDecimal, int paramInt6);
/*      */ 
/*      */   public XMLGregorianCalendar newXMLGregorianCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone)
/*      */   {
/*  870 */     BigInteger realYear = year != -2147483648 ? BigInteger.valueOf(year) : null;
/*      */ 
/*  874 */     BigDecimal realMillisecond = null;
/*  875 */     if (millisecond != -2147483648) {
/*  876 */       if ((millisecond < 0) || (millisecond > 1000)) {
/*  877 */         throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone)with invalid millisecond: " + millisecond);
/*      */       }
/*      */ 
/*  884 */       realMillisecond = BigDecimal.valueOf(millisecond).movePointLeft(3);
/*      */     }
/*      */ 
/*  887 */     return newXMLGregorianCalendar(realYear, month, day, hour, minute, second, realMillisecond, timezone);
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendar newXMLGregorianCalendarDate(int year, int month, int day, int timezone)
/*      */   {
/*  928 */     return newXMLGregorianCalendar(year, month, day, -2147483648, -2147483648, -2147483648, -2147483648, timezone);
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendar newXMLGregorianCalendarTime(int hours, int minutes, int seconds, int timezone)
/*      */   {
/*  964 */     return newXMLGregorianCalendar(-2147483648, -2147483648, -2147483648, hours, minutes, seconds, -2147483648, timezone);
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendar newXMLGregorianCalendarTime(int hours, int minutes, int seconds, BigDecimal fractionalSecond, int timezone)
/*      */   {
/* 1003 */     return newXMLGregorianCalendar(null, -2147483648, -2147483648, hours, minutes, seconds, fractionalSecond, timezone);
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendar newXMLGregorianCalendarTime(int hours, int minutes, int seconds, int milliseconds, int timezone)
/*      */   {
/* 1043 */     BigDecimal realMilliseconds = null;
/* 1044 */     if (milliseconds != -2147483648) {
/* 1045 */       if ((milliseconds < 0) || (milliseconds > 1000)) {
/* 1046 */         throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int hours, int minutes, int seconds, int milliseconds, int timezone)with invalid milliseconds: " + milliseconds);
/*      */       }
/*      */ 
/* 1053 */       realMilliseconds = BigDecimal.valueOf(milliseconds).movePointLeft(3);
/*      */     }
/*      */ 
/* 1056 */     return newXMLGregorianCalendarTime(hours, minutes, seconds, realMilliseconds, timezone);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.datatype.DatatypeFactory
 * JD-Core Version:    0.6.2
 */