/*      */ package com.sun.org.apache.xerces.internal.jaxp.datatype;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.DatatypeMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import javax.xml.datatype.DatatypeConstants;
/*      */ import javax.xml.datatype.Duration;
/*      */ import javax.xml.datatype.XMLGregorianCalendar;
/*      */ import javax.xml.namespace.QName;
/*      */ 
/*      */ public class XMLGregorianCalendarImpl extends XMLGregorianCalendar
/*      */   implements Serializable, Cloneable
/*      */ {
/*  201 */   private BigInteger eon = null;
/*      */ 
/*  206 */   private int year = -2147483648;
/*      */ 
/*  211 */   private int month = -2147483648;
/*      */ 
/*  216 */   private int day = -2147483648;
/*      */ 
/*  221 */   private int timezone = -2147483648;
/*      */ 
/*  226 */   private int hour = -2147483648;
/*      */ 
/*  231 */   private int minute = -2147483648;
/*      */ 
/*  236 */   private int second = -2147483648;
/*      */ 
/*  241 */   private BigDecimal fractionalSecond = null;
/*      */ 
/*  246 */   private static final BigInteger BILLION = new BigInteger("1000000000");
/*      */ 
/*  252 */   private static final Date PURE_GREGORIAN_CHANGE = new Date(-9223372036854775808L);
/*      */   private static final int YEAR = 0;
/*      */   private static final int MONTH = 1;
/*      */   private static final int DAY = 2;
/*      */   private static final int HOUR = 3;
/*      */   private static final int MINUTE = 4;
/*      */   private static final int SECOND = 5;
/*      */   private static final int MILLISECOND = 6;
/*      */   private static final int TIMEZONE = 7;
/*  299 */   private static final String[] FIELD_NAME = { "Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond", "Timezone" };
/*      */   private static final long serialVersionUID = 1L;
/*  330 */   public static final XMLGregorianCalendar LEAP_YEAR_DEFAULT = createDateTime(400, 1, 1, 0, 0, 0, -2147483648, -2147483648);
/*      */ 
/* 2256 */   private static final BigInteger FOUR = BigInteger.valueOf(4L);
/* 2257 */   private static final BigInteger HUNDRED = BigInteger.valueOf(100L);
/* 2258 */   private static final BigInteger FOUR_HUNDRED = BigInteger.valueOf(400L);
/* 2259 */   private static final BigInteger SIXTY = BigInteger.valueOf(60L);
/* 2260 */   private static final BigInteger TWENTY_FOUR = BigInteger.valueOf(24L);
/* 2261 */   private static final BigInteger TWELVE = BigInteger.valueOf(12L);
/* 2262 */   private static final BigDecimal DECIMAL_ZERO = new BigDecimal("0");
/* 2263 */   private static final BigDecimal DECIMAL_ONE = new BigDecimal("1");
/* 2264 */   private static final BigDecimal DECIMAL_SIXTY = new BigDecimal("60");
/*      */ 
/* 2267 */   private static int[] daysInMonth = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*      */ 
/*      */   protected XMLGregorianCalendarImpl(String lexicalRepresentation)
/*      */     throws IllegalArgumentException
/*      */   {
/*  365 */     String format = null;
/*  366 */     String lexRep = lexicalRepresentation;
/*  367 */     int NOT_FOUND = -1;
/*  368 */     int lexRepLength = lexRep.length();
/*      */ 
/*  375 */     if (lexRep.indexOf('T') != -1)
/*      */     {
/*  377 */       format = "%Y-%M-%DT%h:%m:%s%z";
/*  378 */     } else if ((lexRepLength >= 3) && (lexRep.charAt(2) == ':'))
/*      */     {
/*  380 */       format = "%h:%m:%s%z";
/*  381 */     } else if (lexRep.startsWith("--"))
/*      */     {
/*  383 */       if ((lexRepLength >= 3) && (lexRep.charAt(2) == '-'))
/*      */       {
/*  385 */         format = "---%D%z";
/*  386 */       } else if ((lexRepLength == 4) || (lexRepLength == 5) || (lexRepLength == 10))
/*      */       {
/*  391 */         format = "--%M%z";
/*      */       }
/*      */       else
/*      */       {
/*  398 */         format = "--%M-%D%z";
/*      */       }
/*      */     }
/*      */     else {
/*  402 */       int countSeparator = 0;
/*      */ 
/*  407 */       int timezoneOffset = lexRep.indexOf(':');
/*  408 */       if (timezoneOffset != -1)
/*      */       {
/*  414 */         lexRepLength -= 6;
/*      */       }
/*      */ 
/*  417 */       for (int i = 1; i < lexRepLength; i++) {
/*  418 */         if (lexRep.charAt(i) == '-') {
/*  419 */           countSeparator++;
/*      */         }
/*      */       }
/*  422 */       if (countSeparator == 0)
/*      */       {
/*  424 */         format = "%Y%z";
/*  425 */       } else if (countSeparator == 1)
/*      */       {
/*  427 */         format = "%Y-%M%z";
/*      */       }
/*      */       else
/*      */       {
/*  431 */         format = "%Y-%M-%D%z";
/*      */       }
/*      */     }
/*  434 */     Parser p = new Parser(format, lexRep, null);
/*  435 */     p.parse();
/*      */ 
/*  438 */     if (!isValid())
/*  439 */       throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCRepresentation", new Object[] { lexicalRepresentation }));
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendarImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected XMLGregorianCalendarImpl(BigInteger year, int month, int day, int hour, int minute, int second, BigDecimal fractionalSecond, int timezone)
/*      */   {
/*  482 */     setYear(year);
/*  483 */     setMonth(month);
/*  484 */     setDay(day);
/*  485 */     setTime(hour, minute, second, fractionalSecond);
/*  486 */     setTimezone(timezone);
/*      */ 
/*  489 */     if (!isValid())
/*      */     {
/*  491 */       throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-fractional", new Object[] { year, new Integer(month), new Integer(day), new Integer(hour), new Integer(minute), new Integer(second), fractionalSecond, new Integer(timezone) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   private XMLGregorianCalendarImpl(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone)
/*      */   {
/*  553 */     setYear(year);
/*  554 */     setMonth(month);
/*  555 */     setDay(day);
/*  556 */     setTime(hour, minute, second);
/*  557 */     setTimezone(timezone);
/*  558 */     setMillisecond(millisecond);
/*      */ 
/*  560 */     if (!isValid())
/*      */     {
/*  562 */       throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-milli", new Object[] { new Integer(year), new Integer(month), new Integer(day), new Integer(hour), new Integer(minute), new Integer(second), new Integer(millisecond), new Integer(timezone) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendarImpl(GregorianCalendar cal)
/*      */   {
/*  645 */     int year = cal.get(1);
/*  646 */     if (cal.get(0) == 0) {
/*  647 */       year = -year;
/*      */     }
/*  649 */     setYear(year);
/*      */ 
/*  653 */     setMonth(cal.get(2) + 1);
/*  654 */     setDay(cal.get(5));
/*  655 */     setTime(cal.get(11), cal.get(12), cal.get(13), cal.get(14));
/*      */ 
/*  662 */     int offsetInMinutes = (cal.get(15) + cal.get(16)) / 60000;
/*  663 */     setTimezone(offsetInMinutes);
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar createDateTime(BigInteger year, int month, int day, int hours, int minutes, int seconds, BigDecimal fractionalSecond, int timezone)
/*      */   {
/*  699 */     return new XMLGregorianCalendarImpl(year, month, day, hours, minutes, seconds, fractionalSecond, timezone);
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar createDateTime(int year, int month, int day, int hour, int minute, int second)
/*      */   {
/*  735 */     return new XMLGregorianCalendarImpl(year, month, day, hour, minute, second, -2147483648, -2147483648);
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar createDateTime(int year, int month, int day, int hours, int minutes, int seconds, int milliseconds, int timezone)
/*      */   {
/*  777 */     return new XMLGregorianCalendarImpl(year, month, day, hours, minutes, seconds, milliseconds, timezone);
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar createDate(int year, int month, int day, int timezone)
/*      */   {
/*  814 */     return new XMLGregorianCalendarImpl(year, month, day, -2147483648, -2147483648, -2147483648, -2147483648, timezone);
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar createTime(int hours, int minutes, int seconds, int timezone)
/*      */   {
/*  846 */     return new XMLGregorianCalendarImpl(-2147483648, -2147483648, -2147483648, hours, minutes, seconds, -2147483648, timezone);
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar createTime(int hours, int minutes, int seconds, BigDecimal fractionalSecond, int timezone)
/*      */   {
/*  881 */     return new XMLGregorianCalendarImpl(null, -2147483648, -2147483648, hours, minutes, seconds, fractionalSecond, timezone);
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar createTime(int hours, int minutes, int seconds, int milliseconds, int timezone)
/*      */   {
/*  916 */     return new XMLGregorianCalendarImpl(-2147483648, -2147483648, -2147483648, hours, minutes, seconds, milliseconds, timezone);
/*      */   }
/*      */ 
/*      */   public BigInteger getEon()
/*      */   {
/*  943 */     return this.eon;
/*      */   }
/*      */ 
/*      */   public int getYear()
/*      */   {
/*  959 */     return this.year;
/*      */   }
/*      */ 
/*      */   public BigInteger getEonAndYear()
/*      */   {
/*  980 */     if ((this.year != -2147483648) && (this.eon != null))
/*      */     {
/*  983 */       return this.eon.add(BigInteger.valueOf(this.year));
/*      */     }
/*      */ 
/*  987 */     if ((this.year != -2147483648) && (this.eon == null))
/*      */     {
/*  990 */       return BigInteger.valueOf(this.year);
/*      */     }
/*      */ 
/*  995 */     return null;
/*      */   }
/*      */ 
/*      */   public int getMonth()
/*      */   {
/* 1008 */     return this.month;
/*      */   }
/*      */ 
/*      */   public int getDay()
/*      */   {
/* 1020 */     return this.day;
/*      */   }
/*      */ 
/*      */   public int getTimezone()
/*      */   {
/* 1033 */     return this.timezone;
/*      */   }
/*      */ 
/*      */   public int getHour()
/*      */   {
/* 1045 */     return this.hour;
/*      */   }
/*      */ 
/*      */   public int getMinute()
/*      */   {
/* 1057 */     return this.minute;
/*      */   }
/*      */ 
/*      */   public int getSecond()
/*      */   {
/* 1079 */     return this.second;
/*      */   }
/*      */ 
/*      */   private BigDecimal getSeconds()
/*      */   {
/* 1086 */     if (this.second == -2147483648) {
/* 1087 */       return DECIMAL_ZERO;
/*      */     }
/* 1089 */     BigDecimal result = BigDecimal.valueOf(this.second);
/* 1090 */     if (this.fractionalSecond != null) {
/* 1091 */       return result.add(this.fractionalSecond);
/*      */     }
/* 1093 */     return result;
/*      */   }
/*      */ 
/*      */   public int getMillisecond()
/*      */   {
/* 1118 */     if (this.fractionalSecond == null) {
/* 1119 */       return -2147483648;
/*      */     }
/*      */ 
/* 1124 */     return this.fractionalSecond.movePointRight(3).intValue();
/*      */   }
/*      */ 
/*      */   public BigDecimal getFractionalSecond()
/*      */   {
/* 1146 */     return this.fractionalSecond;
/*      */   }
/*      */ 
/*      */   public void setYear(BigInteger year)
/*      */   {
/* 1163 */     if (year == null) {
/* 1164 */       this.eon = null;
/* 1165 */       this.year = -2147483648;
/*      */     } else {
/* 1167 */       BigInteger temp = year.remainder(BILLION);
/* 1168 */       this.year = temp.intValue();
/* 1169 */       setEon(year.subtract(temp));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setYear(int year)
/*      */   {
/* 1187 */     if (year == -2147483648) {
/* 1188 */       this.year = -2147483648;
/* 1189 */       this.eon = null;
/* 1190 */     } else if (Math.abs(year) < BILLION.intValue()) {
/* 1191 */       this.year = year;
/* 1192 */       this.eon = null;
/*      */     } else {
/* 1194 */       BigInteger theYear = BigInteger.valueOf(year);
/* 1195 */       BigInteger remainder = theYear.remainder(BILLION);
/* 1196 */       this.year = remainder.intValue();
/* 1197 */       setEon(theYear.subtract(remainder));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setEon(BigInteger eon)
/*      */   {
/* 1210 */     if ((eon != null) && (eon.compareTo(BigInteger.ZERO) == 0))
/*      */     {
/* 1212 */       this.eon = null;
/*      */     }
/* 1214 */     else this.eon = eon;
/*      */   }
/*      */ 
/*      */   public void setMonth(int month)
/*      */   {
/* 1230 */     if (((month < 1) || (12 < month)) && 
/* 1231 */       (month != -2147483648))
/* 1232 */       invalidFieldValue(1, month);
/* 1233 */     this.month = month;
/*      */   }
/*      */ 
/*      */   public void setDay(int day)
/*      */   {
/* 1248 */     if (((day < 1) || (31 < day)) && 
/* 1249 */       (day != -2147483648))
/* 1250 */       invalidFieldValue(2, day);
/* 1251 */     this.day = day;
/*      */   }
/*      */ 
/*      */   public void setTimezone(int offset)
/*      */   {
/* 1267 */     if (((offset < -840) || (840 < offset)) && 
/* 1268 */       (offset != -2147483648))
/* 1269 */       invalidFieldValue(7, offset);
/* 1270 */     this.timezone = offset;
/*      */   }
/*      */ 
/*      */   public void setTime(int hour, int minute, int second)
/*      */   {
/* 1290 */     setTime(hour, minute, second, null);
/*      */   }
/*      */ 
/*      */   private void invalidFieldValue(int field, int value) {
/* 1294 */     throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFieldValue", new Object[] { new Integer(value), FIELD_NAME[field] }));
/*      */   }
/*      */ 
/*      */   private void testHour()
/*      */   {
/* 1303 */     if (getHour() == 24) {
/* 1304 */       if ((getMinute() != 0) || (getSecond() != 0))
/*      */       {
/* 1306 */         invalidFieldValue(3, getHour());
/*      */       }
/*      */ 
/* 1310 */       setHour(0, false);
/* 1311 */       add(new DurationImpl(true, 0, 0, 1, 0, 0, 0));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setHour(int hour)
/*      */   {
/* 1317 */     setHour(hour, true);
/*      */   }
/*      */ 
/*      */   private void setHour(int hour, boolean validate)
/*      */   {
/* 1322 */     if (((hour < 0) || (hour > 24)) && 
/* 1323 */       (hour != -2147483648)) {
/* 1324 */       invalidFieldValue(3, hour);
/*      */     }
/*      */ 
/* 1328 */     this.hour = hour;
/*      */ 
/* 1330 */     if (validate)
/* 1331 */       testHour();
/*      */   }
/*      */ 
/*      */   public void setMinute(int minute)
/*      */   {
/* 1336 */     if (((minute < 0) || (59 < minute)) && 
/* 1337 */       (minute != -2147483648))
/* 1338 */       invalidFieldValue(4, minute);
/* 1339 */     this.minute = minute;
/*      */   }
/*      */ 
/*      */   public void setSecond(int second) {
/* 1343 */     if (((second < 0) || (60 < second)) && 
/* 1344 */       (second != -2147483648))
/* 1345 */       invalidFieldValue(5, second);
/* 1346 */     this.second = second;
/*      */   }
/*      */ 
/*      */   public void setTime(int hour, int minute, int second, BigDecimal fractional)
/*      */   {
/* 1372 */     setHour(hour, false);
/*      */ 
/* 1374 */     setMinute(minute);
/* 1375 */     if (second != 60)
/* 1376 */       setSecond(second);
/* 1377 */     else if (((hour == 23) && (minute == 59)) || ((hour == 0) && (minute == 0)))
/* 1378 */       setSecond(second);
/*      */     else {
/* 1380 */       invalidFieldValue(5, second);
/*      */     }
/*      */ 
/* 1383 */     setFractionalSecond(fractional);
/*      */ 
/* 1386 */     testHour();
/*      */   }
/*      */ 
/*      */   public void setTime(int hour, int minute, int second, int millisecond)
/*      */   {
/* 1408 */     setHour(hour, false);
/*      */ 
/* 1410 */     setMinute(minute);
/* 1411 */     if (second != 60)
/* 1412 */       setSecond(second);
/* 1413 */     else if (((hour == 23) && (minute == 59)) || ((hour == 0) && (minute == 0)))
/* 1414 */       setSecond(second);
/*      */     else {
/* 1416 */       invalidFieldValue(5, second);
/*      */     }
/* 1418 */     setMillisecond(millisecond);
/*      */ 
/* 1421 */     testHour();
/*      */   }
/*      */ 
/*      */   public int compare(XMLGregorianCalendar rhs)
/*      */   {
/* 1448 */     XMLGregorianCalendar lhs = this;
/*      */ 
/* 1450 */     int result = 2;
/* 1451 */     XMLGregorianCalendarImpl P = (XMLGregorianCalendarImpl)lhs;
/* 1452 */     XMLGregorianCalendarImpl Q = (XMLGregorianCalendarImpl)rhs;
/*      */ 
/* 1454 */     if (P.getTimezone() == Q.getTimezone())
/*      */     {
/* 1459 */       return internalCompare(P, Q);
/*      */     }
/* 1461 */     if ((P.getTimezone() != -2147483648) && (Q.getTimezone() != -2147483648))
/*      */     {
/* 1466 */       P = (XMLGregorianCalendarImpl)P.normalize();
/* 1467 */       Q = (XMLGregorianCalendarImpl)Q.normalize();
/* 1468 */       return internalCompare(P, Q);
/* 1469 */     }if (P.getTimezone() != -2147483648)
/*      */     {
/* 1471 */       if (P.getTimezone() != 0) {
/* 1472 */         P = (XMLGregorianCalendarImpl)P.normalize();
/*      */       }
/*      */ 
/* 1476 */       XMLGregorianCalendar MinQ = Q.normalizeToTimezone(840);
/* 1477 */       result = internalCompare(P, MinQ);
/* 1478 */       if (result == -1) {
/* 1479 */         return result;
/*      */       }
/*      */ 
/* 1483 */       XMLGregorianCalendar MaxQ = Q.normalizeToTimezone(-840);
/* 1484 */       result = internalCompare(P, MaxQ);
/* 1485 */       if (result == 1) {
/* 1486 */         return result;
/*      */       }
/*      */ 
/* 1489 */       return 2;
/*      */     }
/*      */ 
/* 1493 */     if (Q.getTimezone() != 0) {
/* 1494 */       Q = (XMLGregorianCalendarImpl)Q.normalizeToTimezone(Q.getTimezone());
/*      */     }
/*      */ 
/* 1498 */     XMLGregorianCalendar MaxP = P.normalizeToTimezone(-840);
/* 1499 */     result = internalCompare(MaxP, Q);
/* 1500 */     if (result == -1) {
/* 1501 */       return result;
/*      */     }
/*      */ 
/* 1505 */     XMLGregorianCalendar MinP = P.normalizeToTimezone(840);
/* 1506 */     result = internalCompare(MinP, Q);
/* 1507 */     if (result == 1) {
/* 1508 */       return result;
/*      */     }
/*      */ 
/* 1511 */     return 2;
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendar normalize()
/*      */   {
/* 1524 */     XMLGregorianCalendar normalized = normalizeToTimezone(this.timezone);
/*      */ 
/* 1527 */     if (getTimezone() == -2147483648) {
/* 1528 */       normalized.setTimezone(-2147483648);
/*      */     }
/*      */ 
/* 1532 */     if (getMillisecond() == -2147483648) {
/* 1533 */       normalized.setMillisecond(-2147483648);
/*      */     }
/*      */ 
/* 1536 */     return normalized;
/*      */   }
/*      */ 
/*      */   private XMLGregorianCalendar normalizeToTimezone(int timezone)
/*      */   {
/* 1547 */     int minutes = timezone;
/* 1548 */     XMLGregorianCalendar result = (XMLGregorianCalendar)clone();
/*      */ 
/* 1552 */     minutes = -minutes;
/* 1553 */     Duration d = new DurationImpl(minutes >= 0, 0, 0, 0, 0, minutes < 0 ? -minutes : minutes, 0);
/*      */ 
/* 1561 */     result.add(d);
/*      */ 
/* 1564 */     result.setTimezone(0);
/* 1565 */     return result;
/*      */   }
/*      */ 
/*      */   private static int internalCompare(XMLGregorianCalendar P, XMLGregorianCalendar Q)
/*      */   {
/* 1588 */     if (P.getEon() == Q.getEon())
/*      */     {
/* 1592 */       int result = compareField(P.getYear(), Q.getYear());
/* 1593 */       if (result != 0)
/* 1594 */         return result;
/*      */     }
/*      */     else {
/* 1597 */       result = compareField(P.getEonAndYear(), Q.getEonAndYear());
/* 1598 */       if (result != 0) {
/* 1599 */         return result;
/*      */       }
/*      */     }
/*      */ 
/* 1603 */     int result = compareField(P.getMonth(), Q.getMonth());
/* 1604 */     if (result != 0) {
/* 1605 */       return result;
/*      */     }
/*      */ 
/* 1608 */     result = compareField(P.getDay(), Q.getDay());
/* 1609 */     if (result != 0) {
/* 1610 */       return result;
/*      */     }
/*      */ 
/* 1613 */     result = compareField(P.getHour(), Q.getHour());
/* 1614 */     if (result != 0) {
/* 1615 */       return result;
/*      */     }
/*      */ 
/* 1618 */     result = compareField(P.getMinute(), Q.getMinute());
/* 1619 */     if (result != 0) {
/* 1620 */       return result;
/*      */     }
/* 1622 */     result = compareField(P.getSecond(), Q.getSecond());
/* 1623 */     if (result != 0) {
/* 1624 */       return result;
/*      */     }
/*      */ 
/* 1627 */     result = compareField(P.getFractionalSecond(), Q.getFractionalSecond());
/* 1628 */     return result;
/*      */   }
/*      */ 
/*      */   private static int compareField(int Pfield, int Qfield)
/*      */   {
/* 1636 */     if (Pfield == Qfield)
/*      */     {
/* 1640 */       return 0;
/*      */     }
/* 1642 */     if ((Pfield == -2147483648) || (Qfield == -2147483648))
/*      */     {
/* 1644 */       return 2;
/*      */     }
/*      */ 
/* 1647 */     return Pfield < Qfield ? -1 : 1;
/*      */   }
/*      */ 
/*      */   private static int compareField(BigInteger Pfield, BigInteger Qfield)
/*      */   {
/* 1653 */     if (Pfield == null) {
/* 1654 */       return Qfield == null ? 0 : 2;
/*      */     }
/* 1656 */     if (Qfield == null) {
/* 1657 */       return 2;
/*      */     }
/* 1659 */     return Pfield.compareTo(Qfield);
/*      */   }
/*      */ 
/*      */   private static int compareField(BigDecimal Pfield, BigDecimal Qfield)
/*      */   {
/* 1664 */     if (Pfield == Qfield) {
/* 1665 */       return 0;
/*      */     }
/*      */ 
/* 1668 */     if (Pfield == null) {
/* 1669 */       Pfield = DECIMAL_ZERO;
/*      */     }
/*      */ 
/* 1672 */     if (Qfield == null) {
/* 1673 */       Qfield = DECIMAL_ZERO;
/*      */     }
/*      */ 
/* 1676 */     return Pfield.compareTo(Qfield);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1688 */     if ((obj == null) || (!(obj instanceof XMLGregorianCalendar))) {
/* 1689 */       return false;
/*      */     }
/* 1691 */     return compare((XMLGregorianCalendar)obj) == 0;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1706 */     int timezone = getTimezone();
/* 1707 */     if (timezone == -2147483648) {
/* 1708 */       timezone = 0;
/*      */     }
/* 1710 */     XMLGregorianCalendar gc = this;
/* 1711 */     if (timezone != 0) {
/* 1712 */       gc = normalizeToTimezone(getTimezone());
/*      */     }
/* 1714 */     return gc.getYear() + gc.getMonth() + gc.getDay() + gc.getHour() + gc.getMinute() + gc.getSecond();
/*      */   }
/*      */ 
/*      */   public static XMLGregorianCalendar parse(String lexicalRepresentation)
/*      */   {
/* 1751 */     return new XMLGregorianCalendarImpl(lexicalRepresentation);
/*      */   }
/*      */ 
/*      */   public String toXMLFormat()
/*      */   {
/* 1770 */     QName typekind = getXMLSchemaType();
/*      */ 
/* 1772 */     String formatString = null;
/*      */ 
/* 1775 */     if (typekind == DatatypeConstants.DATETIME)
/* 1776 */       formatString = "%Y-%M-%DT%h:%m:%s%z";
/* 1777 */     else if (typekind == DatatypeConstants.DATE)
/* 1778 */       formatString = "%Y-%M-%D%z";
/* 1779 */     else if (typekind == DatatypeConstants.TIME)
/* 1780 */       formatString = "%h:%m:%s%z";
/* 1781 */     else if (typekind == DatatypeConstants.GMONTH)
/* 1782 */       formatString = "--%M%z";
/* 1783 */     else if (typekind == DatatypeConstants.GDAY)
/* 1784 */       formatString = "---%D%z";
/* 1785 */     else if (typekind == DatatypeConstants.GYEAR)
/* 1786 */       formatString = "%Y%z";
/* 1787 */     else if (typekind == DatatypeConstants.GYEARMONTH)
/* 1788 */       formatString = "%Y-%M%z";
/* 1789 */     else if (typekind == DatatypeConstants.GMONTHDAY) {
/* 1790 */       formatString = "--%M-%D%z";
/*      */     }
/* 1792 */     return format(formatString);
/*      */   }
/*      */ 
/*      */   public QName getXMLSchemaType()
/*      */   {
/* 1908 */     int mask = (this.year != -2147483648 ? 32 : 0) | (this.month != -2147483648 ? 16 : 0) | (this.day != -2147483648 ? 8 : 0) | (this.hour != -2147483648 ? 4 : 0) | (this.minute != -2147483648 ? 2 : 0) | (this.second != -2147483648 ? 1 : 0);
/*      */ 
/* 1916 */     switch (mask) {
/*      */     case 63:
/* 1918 */       return DatatypeConstants.DATETIME;
/*      */     case 56:
/* 1920 */       return DatatypeConstants.DATE;
/*      */     case 7:
/* 1922 */       return DatatypeConstants.TIME;
/*      */     case 48:
/* 1924 */       return DatatypeConstants.GYEARMONTH;
/*      */     case 24:
/* 1926 */       return DatatypeConstants.GMONTHDAY;
/*      */     case 32:
/* 1928 */       return DatatypeConstants.GYEAR;
/*      */     case 16:
/* 1930 */       return DatatypeConstants.GMONTH;
/*      */     case 8:
/* 1932 */       return DatatypeConstants.GDAY;
/*      */     }
/* 1934 */     throw new IllegalStateException(getClass().getName() + "#getXMLSchemaType() :" + DatatypeMessageFormatter.formatMessage(null, "InvalidXGCFields", null));
/*      */   }
/*      */ 
/*      */   public boolean isValid()
/*      */   {
/* 1954 */     if (getMonth() == 2)
/*      */     {
/* 1956 */       int maxDays = 29;
/*      */ 
/* 1958 */       if (this.eon == null) {
/* 1959 */         if (this.year != -2147483648)
/* 1960 */           maxDays = maximumDayInMonthFor(this.year, getMonth());
/*      */       } else {
/* 1962 */         BigInteger years = getEonAndYear();
/* 1963 */         if (years != null) {
/* 1964 */           maxDays = maximumDayInMonthFor(getEonAndYear(), 2);
/*      */         }
/*      */       }
/* 1967 */       if (getDay() > maxDays) {
/* 1968 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1973 */     if (getHour() == 24) {
/* 1974 */       if (getMinute() != 0)
/* 1975 */         return false;
/* 1976 */       if (getSecond() != 0) {
/* 1977 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1985 */     if (this.eon == null)
/*      */     {
/* 1987 */       if (this.year == 0)
/* 1988 */         return false;
/*      */     }
/*      */     else {
/* 1991 */       BigInteger yearField = getEonAndYear();
/* 1992 */       if (yearField != null) {
/* 1993 */         int result = compareField(yearField, BigInteger.ZERO);
/* 1994 */         if (result == 0) {
/* 1995 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1999 */     return true;
/*      */   }
/*      */ 
/*      */   public void add(Duration duration)
/*      */   {
/* 2042 */     boolean[] fieldUndefined = { false, false, false, false, false, false };
/*      */ 
/* 2051 */     int signum = duration.getSign();
/*      */ 
/* 2053 */     int startMonth = getMonth();
/* 2054 */     if (startMonth == -2147483648) {
/* 2055 */       startMonth = 1;
/* 2056 */       fieldUndefined[1] = true;
/*      */     }
/*      */ 
/* 2059 */     BigInteger dMonths = sanitize(duration.getField(DatatypeConstants.MONTHS), signum);
/* 2060 */     BigInteger temp = BigInteger.valueOf(startMonth).add(dMonths);
/* 2061 */     setMonth(temp.subtract(BigInteger.ONE).mod(TWELVE).intValue() + 1);
/* 2062 */     BigInteger carry = new BigDecimal(temp.subtract(BigInteger.ONE)).divide(new BigDecimal(TWELVE), 3).toBigInteger();
/*      */ 
/* 2068 */     BigInteger startYear = getEonAndYear();
/* 2069 */     if (startYear == null) {
/* 2070 */       fieldUndefined[0] = true;
/* 2071 */       startYear = BigInteger.ZERO;
/*      */     }
/* 2073 */     BigInteger dYears = sanitize(duration.getField(DatatypeConstants.YEARS), signum);
/* 2074 */     BigInteger endYear = startYear.add(dYears).add(carry);
/* 2075 */     setYear(endYear);
/*      */     BigDecimal startSeconds;
/*      */     BigDecimal startSeconds;
/* 2089 */     if (getSecond() == -2147483648) {
/* 2090 */       fieldUndefined[5] = true;
/* 2091 */       startSeconds = DECIMAL_ZERO;
/*      */     }
/*      */     else {
/* 2094 */       startSeconds = getSeconds();
/*      */     }
/*      */ 
/* 2098 */     BigDecimal dSeconds = DurationImpl.sanitize((BigDecimal)duration.getField(DatatypeConstants.SECONDS), signum);
/* 2099 */     BigDecimal tempBD = startSeconds.add(dSeconds);
/* 2100 */     BigDecimal fQuotient = new BigDecimal(new BigDecimal(tempBD.toBigInteger()).divide(DECIMAL_SIXTY, 3).toBigInteger());
/*      */ 
/* 2102 */     BigDecimal endSeconds = tempBD.subtract(fQuotient.multiply(DECIMAL_SIXTY));
/*      */ 
/* 2104 */     carry = fQuotient.toBigInteger();
/* 2105 */     setSecond(endSeconds.intValue());
/* 2106 */     BigDecimal tempFracSeconds = endSeconds.subtract(new BigDecimal(BigInteger.valueOf(getSecond())));
/* 2107 */     if (tempFracSeconds.compareTo(DECIMAL_ZERO) < 0) {
/* 2108 */       setFractionalSecond(DECIMAL_ONE.add(tempFracSeconds));
/* 2109 */       if (getSecond() == 0) {
/* 2110 */         setSecond(59);
/* 2111 */         carry = carry.subtract(BigInteger.ONE);
/*      */       } else {
/* 2113 */         setSecond(getSecond() - 1);
/*      */       }
/*      */     } else {
/* 2116 */       setFractionalSecond(tempFracSeconds);
/*      */     }
/*      */ 
/* 2124 */     int startMinutes = getMinute();
/* 2125 */     if (startMinutes == -2147483648) {
/* 2126 */       fieldUndefined[4] = true;
/* 2127 */       startMinutes = 0;
/*      */     }
/* 2129 */     BigInteger dMinutes = sanitize(duration.getField(DatatypeConstants.MINUTES), signum);
/*      */ 
/* 2131 */     temp = BigInteger.valueOf(startMinutes).add(dMinutes).add(carry);
/* 2132 */     setMinute(temp.mod(SIXTY).intValue());
/* 2133 */     carry = new BigDecimal(temp).divide(DECIMAL_SIXTY, 3).toBigInteger();
/*      */ 
/* 2140 */     int startHours = getHour();
/* 2141 */     if (startHours == -2147483648) {
/* 2142 */       fieldUndefined[3] = true;
/* 2143 */       startHours = 0;
/*      */     }
/* 2145 */     BigInteger dHours = sanitize(duration.getField(DatatypeConstants.HOURS), signum);
/*      */ 
/* 2147 */     temp = BigInteger.valueOf(startHours).add(dHours).add(carry);
/* 2148 */     setHour(temp.mod(TWENTY_FOUR).intValue(), false);
/* 2149 */     carry = new BigDecimal(temp).divide(new BigDecimal(TWENTY_FOUR), 3).toBigInteger();
/*      */ 
/* 2175 */     int startDay = getDay();
/* 2176 */     if (startDay == -2147483648) {
/* 2177 */       fieldUndefined[2] = true;
/* 2178 */       startDay = 1;
/*      */     }
/* 2180 */     BigInteger dDays = sanitize(duration.getField(DatatypeConstants.DAYS), signum);
/* 2181 */     int maxDayInMonth = maximumDayInMonthFor(getEonAndYear(), getMonth());
/*      */     BigInteger tempDays;
/*      */     BigInteger tempDays;
/* 2182 */     if (startDay > maxDayInMonth) {
/* 2183 */       tempDays = BigInteger.valueOf(maxDayInMonth);
/*      */     }
/*      */     else
/*      */     {
/*      */       BigInteger tempDays;
/* 2184 */       if (startDay < 1)
/* 2185 */         tempDays = BigInteger.ONE;
/*      */       else
/* 2187 */         tempDays = BigInteger.valueOf(startDay);
/*      */     }
/* 2189 */     BigInteger endDays = tempDays.add(dDays).add(carry);
/*      */     while (true)
/*      */     {
/*      */       int monthCarry;
/*      */       int monthCarry;
/* 2193 */       if (endDays.compareTo(BigInteger.ONE) < 0)
/*      */       {
/* 2195 */         BigInteger mdimf = null;
/* 2196 */         if (this.month >= 2) {
/* 2197 */           mdimf = BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear(), getMonth() - 1));
/*      */         }
/*      */         else {
/* 2200 */           mdimf = BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear().subtract(BigInteger.valueOf(1L)), 12));
/*      */         }
/* 2202 */         endDays = endDays.add(mdimf);
/* 2203 */         monthCarry = -1; } else {
/* 2204 */         if (endDays.compareTo(BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear(), getMonth()))) <= 0) break;
/* 2205 */         endDays = endDays.add(BigInteger.valueOf(-maximumDayInMonthFor(getEonAndYear(), getMonth())));
/* 2206 */         monthCarry = 1;
/*      */       }
/*      */ 
/* 2211 */       int intTemp = getMonth() + monthCarry;
/* 2212 */       int endMonth = (intTemp - 1) % 12;
/*      */       int quotient;
/*      */       int quotient;
/* 2214 */       if (endMonth < 0) {
/* 2215 */         endMonth = 12 + endMonth + 1;
/* 2216 */         quotient = new BigDecimal(intTemp - 1).divide(new BigDecimal(TWELVE), 0).intValue();
/*      */       } else {
/* 2218 */         quotient = (intTemp - 1) / 12;
/* 2219 */         endMonth++;
/*      */       }
/* 2221 */       setMonth(endMonth);
/* 2222 */       if (quotient != 0) {
/* 2223 */         setYear(getEonAndYear().add(BigInteger.valueOf(quotient)));
/*      */       }
/*      */     }
/* 2226 */     setDay(endDays.intValue());
/*      */ 
/* 2229 */     for (int i = 0; i <= 5; i++)
/* 2230 */       if (fieldUndefined[i] != 0)
/* 2231 */         switch (i) {
/*      */         case 0:
/* 2233 */           setYear(-2147483648);
/* 2234 */           break;
/*      */         case 1:
/* 2236 */           setMonth(-2147483648);
/* 2237 */           break;
/*      */         case 2:
/* 2239 */           setDay(-2147483648);
/* 2240 */           break;
/*      */         case 3:
/* 2242 */           setHour(-2147483648, false);
/* 2243 */           break;
/*      */         case 4:
/* 2245 */           setMinute(-2147483648);
/* 2246 */           break;
/*      */         case 5:
/* 2248 */           setSecond(-2147483648);
/* 2249 */           setFractionalSecond(null);
/*      */         }
/*      */   }
/*      */ 
/*      */   private static int maximumDayInMonthFor(BigInteger year, int month)
/*      */   {
/* 2272 */     if (month != 2) {
/* 2273 */       return daysInMonth[month];
/*      */     }
/* 2275 */     if ((year.mod(FOUR_HUNDRED).equals(BigInteger.ZERO)) || ((!year.mod(HUNDRED).equals(BigInteger.ZERO)) && (year.mod(FOUR).equals(BigInteger.ZERO))))
/*      */     {
/* 2279 */       return 29;
/*      */     }
/* 2281 */     return daysInMonth[month];
/*      */   }
/*      */ 
/*      */   private static int maximumDayInMonthFor(int year, int month)
/*      */   {
/* 2287 */     if (month != 2) {
/* 2288 */       return daysInMonth[month];
/*      */     }
/* 2290 */     if ((year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0)))
/*      */     {
/* 2293 */       return 29;
/*      */     }
/* 2295 */     return daysInMonth[2];
/*      */   }
/*      */ 
/*      */   public GregorianCalendar toGregorianCalendar()
/*      */   {
/* 2394 */     GregorianCalendar result = null;
/* 2395 */     int DEFAULT_TIMEZONE_OFFSET = -2147483648;
/* 2396 */     TimeZone tz = getTimeZone(-2147483648);
/*      */ 
/* 2400 */     Locale locale = getDefaultLocale();
/*      */ 
/* 2402 */     result = new GregorianCalendar(tz, locale);
/* 2403 */     result.clear();
/* 2404 */     result.setGregorianChange(PURE_GREGORIAN_CHANGE);
/*      */ 
/* 2407 */     BigInteger year = getEonAndYear();
/* 2408 */     if (year != null) {
/* 2409 */       result.set(0, year.signum() == -1 ? 0 : 1);
/* 2410 */       result.set(1, year.abs().intValue());
/*      */     }
/*      */ 
/* 2414 */     if (this.month != -2147483648)
/*      */     {
/* 2416 */       result.set(2, this.month - 1);
/*      */     }
/*      */ 
/* 2420 */     if (this.day != -2147483648) {
/* 2421 */       result.set(5, this.day);
/*      */     }
/*      */ 
/* 2425 */     if (this.hour != -2147483648) {
/* 2426 */       result.set(11, this.hour);
/*      */     }
/*      */ 
/* 2430 */     if (this.minute != -2147483648) {
/* 2431 */       result.set(12, this.minute);
/*      */     }
/*      */ 
/* 2435 */     if (this.second != -2147483648) {
/* 2436 */       result.set(13, this.second);
/*      */     }
/*      */ 
/* 2440 */     if (this.fractionalSecond != null) {
/* 2441 */       result.set(14, getMillisecond());
/*      */     }
/*      */ 
/* 2444 */     return result;
/*      */   }
/*      */ 
/*      */   private Locale getDefaultLocale()
/*      */   {
/* 2453 */     String lang = SecuritySupport.getSystemProperty("user.language.format");
/* 2454 */     String country = SecuritySupport.getSystemProperty("user.country.format");
/* 2455 */     String variant = SecuritySupport.getSystemProperty("user.variant.format");
/* 2456 */     Locale locale = null;
/* 2457 */     if (lang != null) {
/* 2458 */       if (country != null) {
/* 2459 */         if (variant != null)
/* 2460 */           locale = new Locale(lang, country, variant);
/*      */         else
/* 2462 */           locale = new Locale(lang, country);
/*      */       }
/*      */       else {
/* 2465 */         locale = new Locale(lang);
/*      */       }
/*      */     }
/* 2468 */     if (locale == null) {
/* 2469 */       locale = Locale.getDefault();
/*      */     }
/* 2471 */     return locale;
/*      */   }
/*      */ 
/*      */   public GregorianCalendar toGregorianCalendar(TimeZone timezone, Locale aLocale, XMLGregorianCalendar defaults)
/*      */   {
/* 2529 */     GregorianCalendar result = null;
/* 2530 */     TimeZone tz = timezone;
/* 2531 */     if (tz == null) {
/* 2532 */       int defaultZoneoffset = -2147483648;
/* 2533 */       if (defaults != null) {
/* 2534 */         defaultZoneoffset = defaults.getTimezone();
/*      */       }
/* 2536 */       tz = getTimeZone(defaultZoneoffset);
/*      */     }
/* 2538 */     if (aLocale == null) {
/* 2539 */       aLocale = Locale.getDefault();
/*      */     }
/* 2541 */     result = new GregorianCalendar(tz, aLocale);
/* 2542 */     result.clear();
/* 2543 */     result.setGregorianChange(PURE_GREGORIAN_CHANGE);
/*      */ 
/* 2546 */     BigInteger year = getEonAndYear();
/* 2547 */     if (year != null) {
/* 2548 */       result.set(0, year.signum() == -1 ? 0 : 1);
/* 2549 */       result.set(1, year.abs().intValue());
/*      */     }
/*      */     else {
/* 2552 */       BigInteger defaultYear = defaults != null ? defaults.getEonAndYear() : null;
/* 2553 */       if (defaultYear != null) {
/* 2554 */         result.set(0, defaultYear.signum() == -1 ? 0 : 1);
/* 2555 */         result.set(1, defaultYear.abs().intValue());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2560 */     if (this.month != -2147483648)
/*      */     {
/* 2562 */       result.set(2, this.month - 1);
/*      */     }
/*      */     else {
/* 2565 */       int defaultMonth = defaults != null ? defaults.getMonth() : -2147483648;
/* 2566 */       if (defaultMonth != -2147483648)
/*      */       {
/* 2568 */         result.set(2, defaultMonth - 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2573 */     if (this.day != -2147483648) {
/* 2574 */       result.set(5, this.day);
/*      */     }
/*      */     else {
/* 2577 */       int defaultDay = defaults != null ? defaults.getDay() : -2147483648;
/* 2578 */       if (defaultDay != -2147483648) {
/* 2579 */         result.set(5, defaultDay);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2584 */     if (this.hour != -2147483648) {
/* 2585 */       result.set(11, this.hour);
/*      */     }
/*      */     else {
/* 2588 */       int defaultHour = defaults != null ? defaults.getHour() : -2147483648;
/* 2589 */       if (defaultHour != -2147483648) {
/* 2590 */         result.set(11, defaultHour);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2595 */     if (this.minute != -2147483648) {
/* 2596 */       result.set(12, this.minute);
/*      */     }
/*      */     else {
/* 2599 */       int defaultMinute = defaults != null ? defaults.getMinute() : -2147483648;
/* 2600 */       if (defaultMinute != -2147483648) {
/* 2601 */         result.set(12, defaultMinute);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2606 */     if (this.second != -2147483648) {
/* 2607 */       result.set(13, this.second);
/*      */     }
/*      */     else {
/* 2610 */       int defaultSecond = defaults != null ? defaults.getSecond() : -2147483648;
/* 2611 */       if (defaultSecond != -2147483648) {
/* 2612 */         result.set(13, defaultSecond);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2617 */     if (this.fractionalSecond != null) {
/* 2618 */       result.set(14, getMillisecond());
/*      */     }
/*      */     else {
/* 2621 */       BigDecimal defaultFractionalSecond = defaults != null ? defaults.getFractionalSecond() : null;
/* 2622 */       if (defaultFractionalSecond != null) {
/* 2623 */         result.set(14, defaults.getMillisecond());
/*      */       }
/*      */     }
/*      */ 
/* 2627 */     return result;
/*      */   }
/*      */ 
/*      */   public TimeZone getTimeZone(int defaultZoneoffset)
/*      */   {
/* 2647 */     TimeZone result = null;
/* 2648 */     int zoneoffset = getTimezone();
/*      */ 
/* 2650 */     if (zoneoffset == -2147483648) {
/* 2651 */       zoneoffset = defaultZoneoffset;
/*      */     }
/* 2653 */     if (zoneoffset == -2147483648) {
/* 2654 */       result = TimeZone.getDefault();
/*      */     }
/*      */     else {
/* 2657 */       char sign = zoneoffset < 0 ? '-' : '+';
/* 2658 */       if (sign == '-') {
/* 2659 */         zoneoffset = -zoneoffset;
/*      */       }
/* 2661 */       int hour = zoneoffset / 60;
/* 2662 */       int minutes = zoneoffset - hour * 60;
/*      */ 
/* 2669 */       StringBuffer customTimezoneId = new StringBuffer(8);
/* 2670 */       customTimezoneId.append("GMT");
/* 2671 */       customTimezoneId.append(sign);
/* 2672 */       customTimezoneId.append(hour);
/* 2673 */       if (minutes != 0) {
/* 2674 */         customTimezoneId.append(minutes);
/*      */       }
/* 2676 */       result = TimeZone.getTimeZone(customTimezoneId.toString());
/*      */     }
/* 2678 */     return result;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 2689 */     return new XMLGregorianCalendarImpl(getEonAndYear(), this.month, this.day, this.hour, this.minute, this.second, this.fractionalSecond, this.timezone);
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/* 2703 */     this.eon = null;
/* 2704 */     this.year = -2147483648;
/* 2705 */     this.month = -2147483648;
/* 2706 */     this.day = -2147483648;
/* 2707 */     this.timezone = -2147483648;
/* 2708 */     this.hour = -2147483648;
/* 2709 */     this.minute = -2147483648;
/* 2710 */     this.second = -2147483648;
/* 2711 */     this.fractionalSecond = null;
/*      */   }
/*      */ 
/*      */   public void setMillisecond(int millisecond) {
/* 2715 */     if (millisecond == -2147483648) {
/* 2716 */       this.fractionalSecond = null;
/*      */     } else {
/* 2718 */       if (((millisecond < 0) || (999 < millisecond)) && 
/* 2719 */         (millisecond != -2147483648))
/* 2720 */         invalidFieldValue(6, millisecond);
/* 2721 */       this.fractionalSecond = new BigDecimal(millisecond).movePointLeft(3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFractionalSecond(BigDecimal fractional) {
/* 2726 */     if ((fractional != null) && (
/* 2727 */       (fractional.compareTo(DECIMAL_ZERO) < 0) || (fractional.compareTo(DECIMAL_ONE) > 0)))
/*      */     {
/* 2729 */       throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFractional", new Object[] { fractional }));
/*      */     }
/*      */ 
/* 2733 */     this.fractionalSecond = fractional;
/*      */   }
/*      */ 
/*      */   private static boolean isDigit(char ch)
/*      */   {
/* 2918 */     return ('0' <= ch) && (ch <= '9');
/*      */   }
/*      */ 
/*      */   private String format(String format)
/*      */   {
/* 2935 */     char[] buf = new char[32];
/* 2936 */     int bufPtr = 0;
/*      */ 
/* 2938 */     int fidx = 0; int flen = format.length();
/*      */ 
/* 2940 */     while (fidx < flen) {
/* 2941 */       char fch = format.charAt(fidx++);
/* 2942 */       if (fch != '%') {
/* 2943 */         buf[(bufPtr++)] = fch;
/*      */       }
/*      */       else
/*      */       {
/* 2947 */         switch (format.charAt(fidx++)) {
/*      */         case 'Y':
/* 2949 */           if (this.eon == null)
/*      */           {
/* 2951 */             int y = getYear();
/* 2952 */             if (y < 0) {
/* 2953 */               buf[(bufPtr++)] = '-';
/* 2954 */               y = -y;
/*      */             }
/* 2956 */             bufPtr = print4Number(buf, bufPtr, y);
/*      */           } else {
/* 2958 */             String s = getEonAndYear().toString();
/*      */ 
/* 2960 */             char[] n = new char[buf.length + s.length()];
/* 2961 */             System.arraycopy(buf, 0, n, 0, bufPtr);
/* 2962 */             buf = n;
/* 2963 */             for (int i = s.length(); i < 4; i++)
/* 2964 */               buf[(bufPtr++)] = '0';
/* 2965 */             s.getChars(0, s.length(), buf, bufPtr);
/* 2966 */             bufPtr += s.length();
/*      */           }
/* 2968 */           break;
/*      */         case 'M':
/* 2970 */           bufPtr = print2Number(buf, bufPtr, getMonth());
/* 2971 */           break;
/*      */         case 'D':
/* 2973 */           bufPtr = print2Number(buf, bufPtr, getDay());
/* 2974 */           break;
/*      */         case 'h':
/* 2976 */           bufPtr = print2Number(buf, bufPtr, getHour());
/* 2977 */           break;
/*      */         case 'm':
/* 2979 */           bufPtr = print2Number(buf, bufPtr, getMinute());
/* 2980 */           break;
/*      */         case 's':
/* 2982 */           bufPtr = print2Number(buf, bufPtr, getSecond());
/* 2983 */           if (getFractionalSecond() != null)
/*      */           {
/* 2985 */             String frac = getFractionalSecond().toString();
/*      */ 
/* 2987 */             int pos = frac.indexOf("E-");
/* 2988 */             if (pos >= 0) {
/* 2989 */               String zeros = frac.substring(pos + 2);
/* 2990 */               frac = frac.substring(0, pos);
/* 2991 */               pos = frac.indexOf(".");
/* 2992 */               if (pos >= 0) {
/* 2993 */                 frac = frac.substring(0, pos) + frac.substring(pos + 1);
/*      */               }
/* 2995 */               int count = Integer.parseInt(zeros);
/* 2996 */               if (count < 40) {
/* 2997 */                 frac = "00000000000000000000000000000000000000000".substring(0, count - 1) + frac;
/*      */               }
/*      */               else {
/* 3000 */                 while (count > 1) {
/* 3001 */                   frac = "0" + frac;
/* 3002 */                   count--;
/*      */                 }
/*      */               }
/* 3005 */               frac = "0." + frac;
/*      */             }
/*      */ 
/* 3009 */             char[] n = new char[buf.length + frac.length()];
/* 3010 */             System.arraycopy(buf, 0, n, 0, bufPtr);
/* 3011 */             buf = n;
/*      */ 
/* 3013 */             frac.getChars(1, frac.length(), buf, bufPtr);
/* 3014 */             bufPtr += frac.length() - 1;
/* 3015 */           }break;
/*      */         case 'z':
/* 3018 */           int offset = getTimezone();
/* 3019 */           if (offset == 0) {
/* 3020 */             buf[(bufPtr++)] = 'Z';
/*      */           }
/* 3022 */           else if (offset != -2147483648) {
/* 3023 */             if (offset < 0) {
/* 3024 */               buf[(bufPtr++)] = '-';
/* 3025 */               offset *= -1;
/*      */             } else {
/* 3027 */               buf[(bufPtr++)] = '+';
/*      */             }
/* 3029 */             bufPtr = print2Number(buf, bufPtr, offset / 60);
/* 3030 */             buf[(bufPtr++)] = ':';
/* 3031 */             bufPtr = print2Number(buf, bufPtr, offset % 60); } break;
/*      */         default:
/* 3035 */           throw new InternalError();
/*      */         }
/*      */       }
/*      */     }
/* 3039 */     return new String(buf, 0, bufPtr);
/*      */   }
/*      */ 
/*      */   private int print2Number(char[] out, int bufptr, int number)
/*      */   {
/* 3049 */     out[(bufptr++)] = ((char)(48 + number / 10));
/* 3050 */     out[(bufptr++)] = ((char)(48 + number % 10));
/* 3051 */     return bufptr;
/*      */   }
/*      */ 
/*      */   private int print4Number(char[] out, int bufptr, int number)
/*      */   {
/* 3061 */     out[(bufptr + 3)] = ((char)(48 + number % 10));
/* 3062 */     number /= 10;
/* 3063 */     out[(bufptr + 2)] = ((char)(48 + number % 10));
/* 3064 */     number /= 10;
/* 3065 */     out[(bufptr + 1)] = ((char)(48 + number % 10));
/* 3066 */     number /= 10;
/* 3067 */     out[bufptr] = ((char)(48 + number % 10));
/* 3068 */     return bufptr + 4;
/*      */   }
/*      */ 
/*      */   static BigInteger sanitize(Number value, int signum)
/*      */   {
/* 3077 */     if ((signum == 0) || (value == null)) {
/* 3078 */       return BigInteger.ZERO;
/*      */     }
/* 3080 */     return signum < 0 ? ((BigInteger)value).negate() : (BigInteger)value;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*      */   }
/*      */ 
/*      */   private final class Parser
/*      */   {
/*      */     private final String format;
/*      */     private final String value;
/*      */     private final int flen;
/*      */     private final int vlen;
/*      */     private int fidx;
/*      */     private int vidx;
/*      */ 
/*      */     private Parser(String format, String value)
/*      */     {
/* 2747 */       this.format = format;
/* 2748 */       this.value = value;
/* 2749 */       this.flen = format.length();
/* 2750 */       this.vlen = value.length();
/*      */     }
/*      */ 
/*      */     public void parse()
/*      */       throws IllegalArgumentException
/*      */     {
/* 2762 */       while (this.fidx < this.flen) {
/* 2763 */         char fch = this.format.charAt(this.fidx++);
/*      */ 
/* 2765 */         if (fch != '%') {
/* 2766 */           skip(fch);
/*      */         }
/*      */         else
/*      */         {
/* 2771 */           switch (this.format.charAt(this.fidx++)) {
/*      */           case 'Y':
/* 2773 */             parseAndSetYear(4);
/* 2774 */             break;
/*      */           case 'M':
/* 2777 */             XMLGregorianCalendarImpl.this.setMonth(parseInt(2, 2));
/* 2778 */             break;
/*      */           case 'D':
/* 2781 */             XMLGregorianCalendarImpl.this.setDay(parseInt(2, 2));
/* 2782 */             break;
/*      */           case 'h':
/* 2785 */             XMLGregorianCalendarImpl.this.setHour(parseInt(2, 2), false);
/* 2786 */             break;
/*      */           case 'm':
/* 2789 */             XMLGregorianCalendarImpl.this.setMinute(parseInt(2, 2));
/* 2790 */             break;
/*      */           case 's':
/* 2793 */             XMLGregorianCalendarImpl.this.setSecond(parseInt(2, 2));
/*      */ 
/* 2795 */             if (peek() == '.')
/* 2796 */               XMLGregorianCalendarImpl.this.setFractionalSecond(parseBigDecimal()); break;
/*      */           case 'z':
/* 2801 */             char vch = peek();
/* 2802 */             if (vch == 'Z') {
/* 2803 */               this.vidx += 1;
/* 2804 */               XMLGregorianCalendarImpl.this.setTimezone(0);
/* 2805 */             } else if ((vch == '+') || (vch == '-')) {
/* 2806 */               this.vidx += 1;
/* 2807 */               int h = parseInt(2, 2);
/* 2808 */               skip(':');
/* 2809 */               int m = parseInt(2, 2);
/* 2810 */               XMLGregorianCalendarImpl.this.setTimezone((h * 60 + m) * (vch == '+' ? 1 : -1));
/* 2811 */             }break;
/*      */           default:
/* 2817 */             throw new InternalError();
/*      */           }
/*      */         }
/*      */       }
/* 2821 */       if (this.vidx != this.vlen)
/*      */       {
/* 2823 */         throw new IllegalArgumentException(this.value);
/*      */       }
/* 2825 */       XMLGregorianCalendarImpl.this.testHour();
/*      */     }
/*      */ 
/*      */     private char peek() throws IllegalArgumentException {
/* 2829 */       if (this.vidx == this.vlen) {
/* 2830 */         return 65535;
/*      */       }
/* 2832 */       return this.value.charAt(this.vidx);
/*      */     }
/*      */ 
/*      */     private char read() throws IllegalArgumentException {
/* 2836 */       if (this.vidx == this.vlen) {
/* 2837 */         throw new IllegalArgumentException(this.value);
/*      */       }
/* 2839 */       return this.value.charAt(this.vidx++);
/*      */     }
/*      */ 
/*      */     private void skip(char ch) throws IllegalArgumentException {
/* 2843 */       if (read() != ch)
/* 2844 */         throw new IllegalArgumentException(this.value);
/*      */     }
/*      */ 
/*      */     private int parseInt(int minDigits, int maxDigits)
/*      */       throws IllegalArgumentException
/*      */     {
/* 2851 */       int n = 0;
/*      */ 
/* 2853 */       int vstart = this.vidx;
/*      */       char ch;
/* 2854 */       while ((XMLGregorianCalendarImpl.isDigit(ch = peek())) && (this.vidx - vstart <= maxDigits)) {
/* 2855 */         this.vidx += 1;
/* 2856 */         n = n * 10 + ch - 48;
/*      */       }
/* 2858 */       if (this.vidx - vstart < minDigits)
/*      */       {
/* 2860 */         throw new IllegalArgumentException(this.value);
/*      */       }
/*      */ 
/* 2863 */       return n;
/*      */     }
/*      */ 
/*      */     private void parseAndSetYear(int minDigits) throws IllegalArgumentException
/*      */     {
/* 2868 */       int vstart = this.vidx;
/* 2869 */       int n = 0;
/* 2870 */       boolean neg = false;
/*      */ 
/* 2873 */       if (peek() == '-') {
/* 2874 */         this.vidx += 1;
/* 2875 */         neg = true;
/*      */       }
/*      */       while (true) {
/* 2878 */         char ch = peek();
/* 2879 */         if (!XMLGregorianCalendarImpl.isDigit(ch))
/*      */           break;
/* 2881 */         this.vidx += 1;
/* 2882 */         n = n * 10 + ch - 48;
/*      */       }
/*      */ 
/* 2885 */       if (this.vidx - vstart < minDigits)
/*      */       {
/* 2887 */         throw new IllegalArgumentException(this.value);
/*      */       }
/*      */ 
/* 2890 */       if (this.vidx - vstart < 7)
/*      */       {
/* 2893 */         if (neg) n = -n;
/* 2894 */         XMLGregorianCalendarImpl.this.year = n;
/* 2895 */         XMLGregorianCalendarImpl.this.eon = null;
/*      */       } else {
/* 2897 */         XMLGregorianCalendarImpl.this.setYear(new BigInteger(this.value.substring(vstart, this.vidx)));
/*      */       }
/*      */     }
/*      */ 
/*      */     private BigDecimal parseBigDecimal() throws IllegalArgumentException
/*      */     {
/* 2903 */       int vstart = this.vidx;
/*      */ 
/* 2905 */       if (peek() == '.')
/* 2906 */         this.vidx += 1;
/*      */       else {
/* 2908 */         throw new IllegalArgumentException(this.value);
/*      */       }
/* 2910 */       while (XMLGregorianCalendarImpl.isDigit(peek())) {
/* 2911 */         this.vidx += 1;
/*      */       }
/* 2913 */       return new BigDecimal(this.value.substring(vstart, this.vidx));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl
 * JD-Core Version:    0.6.2
 */