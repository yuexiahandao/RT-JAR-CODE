/*     */ package javax.xml.datatype;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class XMLGregorianCalendar
/*     */   implements Cloneable
/*     */ {
/*     */   public abstract void clear();
/*     */ 
/*     */   public abstract void reset();
/*     */ 
/*     */   public abstract void setYear(BigInteger paramBigInteger);
/*     */ 
/*     */   public abstract void setYear(int paramInt);
/*     */ 
/*     */   public abstract void setMonth(int paramInt);
/*     */ 
/*     */   public abstract void setDay(int paramInt);
/*     */ 
/*     */   public abstract void setTimezone(int paramInt);
/*     */ 
/*     */   public void setTime(int hour, int minute, int second)
/*     */   {
/* 346 */     setTime(hour, minute, second, null);
/*     */   }
/*     */ 
/*     */   public abstract void setHour(int paramInt);
/*     */ 
/*     */   public abstract void setMinute(int paramInt);
/*     */ 
/*     */   public abstract void setSecond(int paramInt);
/*     */ 
/*     */   public abstract void setMillisecond(int paramInt);
/*     */ 
/*     */   public abstract void setFractionalSecond(BigDecimal paramBigDecimal);
/*     */ 
/*     */   public void setTime(int hour, int minute, int second, BigDecimal fractional)
/*     */   {
/* 440 */     setHour(hour);
/* 441 */     setMinute(minute);
/* 442 */     setSecond(second);
/* 443 */     setFractionalSecond(fractional);
/*     */   }
/*     */ 
/*     */   public void setTime(int hour, int minute, int second, int millisecond)
/*     */   {
/* 465 */     setHour(hour);
/* 466 */     setMinute(minute);
/* 467 */     setSecond(second);
/* 468 */     setMillisecond(millisecond);
/*     */   }
/*     */ 
/*     */   public abstract BigInteger getEon();
/*     */ 
/*     */   public abstract int getYear();
/*     */ 
/*     */   public abstract BigInteger getEonAndYear();
/*     */ 
/*     */   public abstract int getMonth();
/*     */ 
/*     */   public abstract int getDay();
/*     */ 
/*     */   public abstract int getTimezone();
/*     */ 
/*     */   public abstract int getHour();
/*     */ 
/*     */   public abstract int getMinute();
/*     */ 
/*     */   public abstract int getSecond();
/*     */ 
/*     */   public int getMillisecond()
/*     */   {
/* 610 */     BigDecimal fractionalSeconds = getFractionalSecond();
/*     */ 
/* 613 */     if (fractionalSeconds == null) {
/* 614 */       return -2147483648;
/*     */     }
/*     */ 
/* 617 */     return getFractionalSecond().movePointRight(3).intValue();
/*     */   }
/*     */ 
/*     */   public abstract BigDecimal getFractionalSecond();
/*     */ 
/*     */   public abstract int compare(XMLGregorianCalendar paramXMLGregorianCalendar);
/*     */ 
/*     */   public abstract XMLGregorianCalendar normalize();
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 689 */     if ((obj == null) || (!(obj instanceof XMLGregorianCalendar))) {
/* 690 */       return false;
/*     */     }
/* 692 */     return compare((XMLGregorianCalendar)obj) == 0;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 707 */     int timezone = getTimezone();
/* 708 */     if (timezone == -2147483648) {
/* 709 */       timezone = 0;
/*     */     }
/* 711 */     XMLGregorianCalendar gc = this;
/* 712 */     if (timezone != 0) {
/* 713 */       gc = normalize();
/*     */     }
/* 715 */     return gc.getYear() + gc.getMonth() + gc.getDay() + gc.getHour() + gc.getMinute() + gc.getSecond();
/*     */   }
/*     */ 
/*     */   public abstract String toXMLFormat();
/*     */ 
/*     */   public abstract QName getXMLSchemaType();
/*     */ 
/*     */   public String toString()
/*     */   {
/* 866 */     return toXMLFormat();
/*     */   }
/*     */ 
/*     */   public abstract boolean isValid();
/*     */ 
/*     */   public abstract void add(Duration paramDuration);
/*     */ 
/*     */   public abstract GregorianCalendar toGregorianCalendar();
/*     */ 
/*     */   public abstract GregorianCalendar toGregorianCalendar(TimeZone paramTimeZone, Locale paramLocale, XMLGregorianCalendar paramXMLGregorianCalendar);
/*     */ 
/*     */   public abstract TimeZone getTimeZone(int paramInt);
/*     */ 
/*     */   public abstract Object clone();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.datatype.XMLGregorianCalendar
 * JD-Core Version:    0.6.2
 */