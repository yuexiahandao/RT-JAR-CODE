/*     */ package javax.xml.datatype;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class Duration
/*     */ {
/*     */   private static final boolean DEBUG = true;
/*     */ 
/*     */   public QName getXMLSchemaType()
/*     */   {
/* 181 */     boolean yearSet = isSet(DatatypeConstants.YEARS);
/* 182 */     boolean monthSet = isSet(DatatypeConstants.MONTHS);
/* 183 */     boolean daySet = isSet(DatatypeConstants.DAYS);
/* 184 */     boolean hourSet = isSet(DatatypeConstants.HOURS);
/* 185 */     boolean minuteSet = isSet(DatatypeConstants.MINUTES);
/* 186 */     boolean secondSet = isSet(DatatypeConstants.SECONDS);
/*     */ 
/* 189 */     if ((yearSet) && (monthSet) && (daySet) && (hourSet) && (minuteSet) && (secondSet))
/*     */     {
/* 195 */       return DatatypeConstants.DURATION;
/*     */     }
/*     */ 
/* 199 */     if ((!yearSet) && (!monthSet) && (daySet) && (hourSet) && (minuteSet) && (secondSet))
/*     */     {
/* 205 */       return DatatypeConstants.DURATION_DAYTIME;
/*     */     }
/*     */ 
/* 209 */     if ((yearSet) && (monthSet) && (!daySet) && (!hourSet) && (!minuteSet) && (!secondSet))
/*     */     {
/* 215 */       return DatatypeConstants.DURATION_YEARMONTH;
/*     */     }
/*     */ 
/* 219 */     throw new IllegalStateException("javax.xml.datatype.Duration#getXMLSchemaType(): this Duration does not match one of the XML Schema date/time datatypes: year set = " + yearSet + " month set = " + monthSet + " day set = " + daySet + " hour set = " + hourSet + " minute set = " + minuteSet + " second set = " + secondSet);
/*     */   }
/*     */ 
/*     */   public abstract int getSign();
/*     */ 
/*     */   public int getYears()
/*     */   {
/* 253 */     return getField(DatatypeConstants.YEARS).intValue();
/*     */   }
/*     */ 
/*     */   public int getMonths()
/*     */   {
/* 266 */     return getField(DatatypeConstants.MONTHS).intValue();
/*     */   }
/*     */ 
/*     */   public int getDays()
/*     */   {
/* 279 */     return getField(DatatypeConstants.DAYS).intValue();
/*     */   }
/*     */ 
/*     */   public int getHours()
/*     */   {
/* 293 */     return getField(DatatypeConstants.HOURS).intValue();
/*     */   }
/*     */ 
/*     */   public int getMinutes()
/*     */   {
/* 307 */     return getField(DatatypeConstants.MINUTES).intValue();
/*     */   }
/*     */ 
/*     */   public int getSeconds()
/*     */   {
/* 322 */     return getField(DatatypeConstants.SECONDS).intValue();
/*     */   }
/*     */ 
/*     */   public long getTimeInMillis(Calendar startInstant)
/*     */   {
/* 356 */     Calendar cal = (Calendar)startInstant.clone();
/* 357 */     addTo(cal);
/* 358 */     return getCalendarTimeInMillis(cal) - getCalendarTimeInMillis(startInstant);
/*     */   }
/*     */ 
/*     */   public long getTimeInMillis(Date startInstant)
/*     */   {
/* 394 */     Calendar cal = new GregorianCalendar();
/* 395 */     cal.setTime(startInstant);
/* 396 */     addTo(cal);
/* 397 */     return getCalendarTimeInMillis(cal) - startInstant.getTime();
/*     */   }
/*     */ 
/*     */   public abstract Number getField(DatatypeConstants.Field paramField);
/*     */ 
/*     */   public abstract boolean isSet(DatatypeConstants.Field paramField);
/*     */ 
/*     */   public abstract Duration add(Duration paramDuration);
/*     */ 
/*     */   public abstract void addTo(Calendar paramCalendar);
/*     */ 
/*     */   public void addTo(Date date)
/*     */   {
/* 559 */     if (date == null) {
/* 560 */       throw new NullPointerException("Cannot call " + getClass().getName() + "#addTo(Date date) with date == null.");
/*     */     }
/*     */ 
/* 567 */     Calendar cal = new GregorianCalendar();
/* 568 */     cal.setTime(date);
/* 569 */     addTo(cal);
/* 570 */     date.setTime(getCalendarTimeInMillis(cal));
/*     */   }
/*     */ 
/*     */   public Duration subtract(Duration rhs)
/*     */   {
/* 623 */     return add(rhs.negate());
/*     */   }
/*     */ 
/*     */   public Duration multiply(int factor)
/*     */   {
/* 643 */     return multiply(new BigDecimal(String.valueOf(factor)));
/*     */   }
/*     */ 
/*     */   public abstract Duration multiply(BigDecimal paramBigDecimal);
/*     */ 
/*     */   public abstract Duration negate();
/*     */ 
/*     */   public abstract Duration normalizeWith(Calendar paramCalendar);
/*     */ 
/*     */   public abstract int compare(Duration paramDuration);
/*     */ 
/*     */   public boolean isLongerThan(Duration duration)
/*     */   {
/* 800 */     return compare(duration) == 1;
/*     */   }
/*     */ 
/*     */   public boolean isShorterThan(Duration duration)
/*     */   {
/* 822 */     return compare(duration) == -1;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object duration)
/*     */   {
/* 865 */     if ((duration == null) || (!(duration instanceof Duration))) {
/* 866 */       return false;
/*     */     }
/*     */ 
/* 869 */     return compare((Duration)duration) == 0;
/*     */   }
/*     */ 
/*     */   public abstract int hashCode();
/*     */ 
/*     */   public String toString()
/*     */   {
/* 895 */     StringBuffer buf = new StringBuffer();
/*     */ 
/* 897 */     if (getSign() < 0) {
/* 898 */       buf.append('-');
/*     */     }
/* 900 */     buf.append('P');
/*     */ 
/* 902 */     BigInteger years = (BigInteger)getField(DatatypeConstants.YEARS);
/* 903 */     if (years != null) {
/* 904 */       buf.append(years + "Y");
/*     */     }
/*     */ 
/* 907 */     BigInteger months = (BigInteger)getField(DatatypeConstants.MONTHS);
/* 908 */     if (months != null) {
/* 909 */       buf.append(months + "M");
/*     */     }
/*     */ 
/* 912 */     BigInteger days = (BigInteger)getField(DatatypeConstants.DAYS);
/* 913 */     if (days != null) {
/* 914 */       buf.append(days + "D");
/*     */     }
/*     */ 
/* 917 */     BigInteger hours = (BigInteger)getField(DatatypeConstants.HOURS);
/* 918 */     BigInteger minutes = (BigInteger)getField(DatatypeConstants.MINUTES);
/* 919 */     BigDecimal seconds = (BigDecimal)getField(DatatypeConstants.SECONDS);
/* 920 */     if ((hours != null) || (minutes != null) || (seconds != null)) {
/* 921 */       buf.append('T');
/* 922 */       if (hours != null) {
/* 923 */         buf.append(hours + "H");
/*     */       }
/* 925 */       if (minutes != null) {
/* 926 */         buf.append(minutes + "M");
/*     */       }
/* 928 */       if (seconds != null) {
/* 929 */         buf.append(toString(seconds) + "S");
/*     */       }
/*     */     }
/*     */ 
/* 933 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private String toString(BigDecimal bd)
/*     */   {
/* 947 */     String intString = bd.unscaledValue().toString();
/* 948 */     int scale = bd.scale();
/*     */ 
/* 950 */     if (scale == 0) {
/* 951 */       return intString;
/*     */     }
/*     */ 
/* 956 */     int insertionPoint = intString.length() - scale;
/* 957 */     if (insertionPoint == 0)
/* 958 */       return "0." + intString;
/*     */     StringBuffer buf;
/* 959 */     if (insertionPoint > 0) {
/* 960 */       StringBuffer buf = new StringBuffer(intString);
/* 961 */       buf.insert(insertionPoint, '.');
/*     */     } else {
/* 963 */       buf = new StringBuffer(3 - insertionPoint + intString.length());
/* 964 */       buf.append("0.");
/* 965 */       for (int i = 0; i < -insertionPoint; i++) {
/* 966 */         buf.append('0');
/*     */       }
/* 968 */       buf.append(intString);
/*     */     }
/* 970 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private static long getCalendarTimeInMillis(Calendar cal)
/*     */   {
/* 986 */     return cal.getTime().getTime();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.datatype.Duration
 * JD-Core Version:    0.6.2
 */