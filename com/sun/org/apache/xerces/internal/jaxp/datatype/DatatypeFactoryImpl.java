/*     */ package com.sun.org.apache.xerces.internal.jaxp.datatype;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.GregorianCalendar;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ public class DatatypeFactoryImpl extends DatatypeFactory
/*     */ {
/*     */   public Duration newDuration(String lexicalRepresentation)
/*     */   {
/* 104 */     return new DurationImpl(lexicalRepresentation);
/*     */   }
/*     */ 
/*     */   public Duration newDuration(long durationInMilliseconds)
/*     */   {
/* 144 */     return new DurationImpl(durationInMilliseconds);
/*     */   }
/*     */ 
/*     */   public Duration newDuration(boolean isPositive, BigInteger years, BigInteger months, BigInteger days, BigInteger hours, BigInteger minutes, BigDecimal seconds)
/*     */   {
/* 183 */     return new DurationImpl(isPositive, years, months, days, hours, minutes, seconds);
/*     */   }
/*     */ 
/*     */   public Duration newDurationYearMonth(boolean isPositive, BigInteger year, BigInteger month)
/*     */   {
/* 223 */     return new DurationYearMonthImpl(isPositive, year, month);
/*     */   }
/*     */ 
/*     */   public Duration newDurationYearMonth(boolean isPositive, int year, int month)
/*     */   {
/* 254 */     return new DurationYearMonthImpl(isPositive, year, month);
/*     */   }
/*     */ 
/*     */   public Duration newDurationYearMonth(String lexicalRepresentation)
/*     */   {
/* 287 */     return new DurationYearMonthImpl(lexicalRepresentation);
/*     */   }
/*     */ 
/*     */   public Duration newDurationYearMonth(long durationInMilliseconds)
/*     */   {
/* 327 */     return new DurationYearMonthImpl(durationInMilliseconds);
/*     */   }
/*     */ 
/*     */   public Duration newDurationDayTime(String lexicalRepresentation)
/*     */   {
/* 356 */     if (lexicalRepresentation == null) {
/* 357 */       throw new NullPointerException("Trying to create an xdt:dayTimeDuration with an invalid lexical representation of \"null\"");
/*     */     }
/*     */ 
/* 362 */     return new DurationDayTimeImpl(lexicalRepresentation);
/*     */   }
/*     */ 
/*     */   public Duration newDurationDayTime(long durationInMilliseconds)
/*     */   {
/* 404 */     return new DurationDayTimeImpl(durationInMilliseconds);
/*     */   }
/*     */ 
/*     */   public Duration newDurationDayTime(boolean isPositive, BigInteger day, BigInteger hour, BigInteger minute, BigInteger second)
/*     */   {
/* 446 */     return new DurationDayTimeImpl(isPositive, day, hour, minute, second != null ? new BigDecimal(second) : null);
/*     */   }
/*     */ 
/*     */   public Duration newDurationDayTime(boolean isPositive, int day, int hour, int minute, int second)
/*     */   {
/* 487 */     return new DurationDayTimeImpl(isPositive, day, hour, minute, second);
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar newXMLGregorianCalendar()
/*     */   {
/* 506 */     return new XMLGregorianCalendarImpl();
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation)
/*     */   {
/* 536 */     return new XMLGregorianCalendarImpl(lexicalRepresentation);
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar cal)
/*     */   {
/* 603 */     return new XMLGregorianCalendarImpl(cal);
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger year, int month, int day, int hour, int minute, int second, BigDecimal fractionalSecond, int timezone)
/*     */   {
/* 641 */     return new XMLGregorianCalendarImpl(year, month, day, hour, minute, second, fractionalSecond, timezone);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl
 * JD-Core Version:    0.6.2
 */