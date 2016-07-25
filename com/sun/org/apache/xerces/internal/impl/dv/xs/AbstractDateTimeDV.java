/*      */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
/*      */ import com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime;
/*      */ import java.math.BigDecimal;
/*      */ import javax.xml.datatype.DatatypeFactory;
/*      */ import javax.xml.datatype.Duration;
/*      */ import javax.xml.datatype.XMLGregorianCalendar;
/*      */ 
/*      */ public abstract class AbstractDateTimeDV extends TypeValidator
/*      */ {
/*      */   private static final boolean DEBUG = false;
/*      */   protected static final int YEAR = 2000;
/*      */   protected static final int MONTH = 1;
/*      */   protected static final int DAY = 1;
/*   62 */   protected static final DatatypeFactory datatypeFactory = new DatatypeFactoryImpl();
/*      */ 
/*      */   public short getAllowedFacets()
/*      */   {
/*   66 */     return 2552;
/*      */   }
/*      */ 
/*      */   public boolean isIdentical(Object value1, Object value2)
/*      */   {
/*   74 */     if ((!(value1 instanceof DateTimeData)) || (!(value2 instanceof DateTimeData))) {
/*   75 */       return false;
/*      */     }
/*      */ 
/*   78 */     DateTimeData v1 = (DateTimeData)value1;
/*   79 */     DateTimeData v2 = (DateTimeData)value2;
/*      */ 
/*   83 */     if ((v1.timezoneHr == v2.timezoneHr) && (v1.timezoneMin == v2.timezoneMin)) {
/*   84 */       return v1.equals(v2);
/*      */     }
/*      */ 
/*   87 */     return false;
/*      */   }
/*      */ 
/*      */   public int compare(Object value1, Object value2)
/*      */   {
/*   93 */     return compareDates((DateTimeData)value1, (DateTimeData)value2, true);
/*      */   }
/*      */ 
/*      */   protected short compareDates(DateTimeData date1, DateTimeData date2, boolean strict)
/*      */   {
/*  107 */     if (date1.utc == date2.utc) {
/*  108 */       return compareOrder(date1, date2);
/*      */     }
/*      */ 
/*  112 */     DateTimeData tempDate = new DateTimeData(null, this);
/*      */ 
/*  114 */     if (date1.utc == 90)
/*      */     {
/*  118 */       cloneDate(date2, tempDate);
/*  119 */       tempDate.timezoneHr = 14;
/*  120 */       tempDate.timezoneMin = 0;
/*  121 */       tempDate.utc = 43;
/*  122 */       normalize(tempDate);
/*  123 */       short c1 = compareOrder(date1, tempDate);
/*  124 */       if (c1 == -1) {
/*  125 */         return c1;
/*      */       }
/*      */ 
/*  130 */       cloneDate(date2, tempDate);
/*  131 */       tempDate.timezoneHr = -14;
/*  132 */       tempDate.timezoneMin = 0;
/*  133 */       tempDate.utc = 45;
/*  134 */       normalize(tempDate);
/*  135 */       short c2 = compareOrder(date1, tempDate);
/*  136 */       if (c2 == 1) {
/*  137 */         return c2;
/*      */       }
/*      */ 
/*  140 */       return 2;
/*  141 */     }if (date2.utc == 90)
/*      */     {
/*  145 */       cloneDate(date1, tempDate);
/*  146 */       tempDate.timezoneHr = -14;
/*  147 */       tempDate.timezoneMin = 0;
/*  148 */       tempDate.utc = 45;
/*      */ 
/*  152 */       normalize(tempDate);
/*  153 */       short c1 = compareOrder(tempDate, date2);
/*      */ 
/*  158 */       if (c1 == -1) {
/*  159 */         return c1;
/*      */       }
/*      */ 
/*  164 */       cloneDate(date1, tempDate);
/*  165 */       tempDate.timezoneHr = 14;
/*  166 */       tempDate.timezoneMin = 0;
/*  167 */       tempDate.utc = 43;
/*  168 */       normalize(tempDate);
/*  169 */       short c2 = compareOrder(tempDate, date2);
/*      */ 
/*  173 */       if (c2 == 1) {
/*  174 */         return c2;
/*      */       }
/*      */ 
/*  177 */       return 2;
/*      */     }
/*  179 */     return 2;
/*      */   }
/*      */ 
/*      */   protected short compareOrder(DateTimeData date1, DateTimeData date2)
/*      */   {
/*  193 */     if (date1.position < 1) {
/*  194 */       if (date1.year < date2.year) {
/*  195 */         return -1;
/*      */       }
/*  197 */       if (date1.year > date2.year) {
/*  198 */         return 1;
/*      */       }
/*      */     }
/*  201 */     if (date1.position < 2) {
/*  202 */       if (date1.month < date2.month) {
/*  203 */         return -1;
/*      */       }
/*  205 */       if (date1.month > date2.month) {
/*  206 */         return 1;
/*      */       }
/*      */     }
/*  209 */     if (date1.day < date2.day) {
/*  210 */       return -1;
/*      */     }
/*  212 */     if (date1.day > date2.day) {
/*  213 */       return 1;
/*      */     }
/*  215 */     if (date1.hour < date2.hour) {
/*  216 */       return -1;
/*      */     }
/*  218 */     if (date1.hour > date2.hour) {
/*  219 */       return 1;
/*      */     }
/*  221 */     if (date1.minute < date2.minute) {
/*  222 */       return -1;
/*      */     }
/*  224 */     if (date1.minute > date2.minute) {
/*  225 */       return 1;
/*      */     }
/*  227 */     if (date1.second < date2.second) {
/*  228 */       return -1;
/*      */     }
/*  230 */     if (date1.second > date2.second) {
/*  231 */       return 1;
/*      */     }
/*  233 */     if (date1.utc < date2.utc) {
/*  234 */       return -1;
/*      */     }
/*  236 */     if (date1.utc > date2.utc) {
/*  237 */       return 1;
/*      */     }
/*  239 */     return 0;
/*      */   }
/*      */ 
/*      */   protected void getTime(String buffer, int start, int end, DateTimeData data)
/*      */     throws RuntimeException
/*      */   {
/*  252 */     int stop = start + 2;
/*      */ 
/*  255 */     data.hour = parseInt(buffer, start, stop);
/*      */ 
/*  259 */     if (buffer.charAt(stop++) != ':') {
/*  260 */       throw new RuntimeException("Error in parsing time zone");
/*      */     }
/*  262 */     start = stop;
/*  263 */     stop += 2;
/*  264 */     data.minute = parseInt(buffer, start, stop);
/*      */ 
/*  267 */     if (buffer.charAt(stop++) != ':') {
/*  268 */       throw new RuntimeException("Error in parsing time zone");
/*      */     }
/*      */ 
/*  272 */     int sign = findUTCSign(buffer, start, end);
/*      */ 
/*  275 */     start = stop;
/*  276 */     stop = sign < 0 ? end : sign;
/*  277 */     data.second = parseSecond(buffer, start, stop);
/*      */ 
/*  280 */     if (sign > 0)
/*  281 */       getTimeZone(buffer, data, sign, end);
/*      */   }
/*      */ 
/*      */   protected int getDate(String buffer, int start, int end, DateTimeData date)
/*      */     throws RuntimeException
/*      */   {
/*  296 */     start = getYearMonth(buffer, start, end, date);
/*      */ 
/*  298 */     if (buffer.charAt(start++) != '-') {
/*  299 */       throw new RuntimeException("CCYY-MM must be followed by '-' sign");
/*      */     }
/*  301 */     int stop = start + 2;
/*  302 */     date.day = parseInt(buffer, start, stop);
/*  303 */     return stop;
/*      */   }
/*      */ 
/*      */   protected int getYearMonth(String buffer, int start, int end, DateTimeData date)
/*      */     throws RuntimeException
/*      */   {
/*  317 */     if (buffer.charAt(0) == '-')
/*      */     {
/*  321 */       start++;
/*      */     }
/*  323 */     int i = indexOf(buffer, start, end, '-');
/*  324 */     if (i == -1) {
/*  325 */       throw new RuntimeException("Year separator is missing or misplaced");
/*      */     }
/*  327 */     int length = i - start;
/*  328 */     if (length < 4)
/*  329 */       throw new RuntimeException("Year must have 'CCYY' format");
/*  330 */     if ((length > 4) && (buffer.charAt(start) == '0')) {
/*  331 */       throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden");
/*      */     }
/*  333 */     date.year = parseIntYear(buffer, i);
/*  334 */     if (buffer.charAt(i) != '-') {
/*  335 */       throw new RuntimeException("CCYY must be followed by '-' sign");
/*      */     }
/*  337 */     i++; start = i;
/*  338 */     i = start + 2;
/*  339 */     date.month = parseInt(buffer, start, i);
/*  340 */     return i;
/*      */   }
/*      */ 
/*      */   protected void parseTimeZone(String buffer, int start, int end, DateTimeData date)
/*      */     throws RuntimeException
/*      */   {
/*  355 */     if (start < end) {
/*  356 */       if (!isNextCharUTCSign(buffer, start, end)) {
/*  357 */         throw new RuntimeException("Error in month parsing");
/*      */       }
/*  359 */       getTimeZone(buffer, date, start, end);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void getTimeZone(String buffer, DateTimeData data, int sign, int end)
/*      */     throws RuntimeException
/*      */   {
/*  372 */     data.utc = buffer.charAt(sign);
/*      */ 
/*  374 */     if (buffer.charAt(sign) == 'Z') {
/*  375 */       if (end > ++sign) {
/*  376 */         throw new RuntimeException("Error in parsing time zone");
/*      */       }
/*  378 */       return;
/*      */     }
/*  380 */     if (sign <= end - 6)
/*      */     {
/*  382 */       int negate = buffer.charAt(sign) == '-' ? -1 : 1;
/*      */ 
/*  384 */       sign++; int stop = sign + 2;
/*  385 */       data.timezoneHr = (negate * parseInt(buffer, sign, stop));
/*  386 */       if (buffer.charAt(stop++) != ':') {
/*  387 */         throw new RuntimeException("Error in parsing time zone");
/*      */       }
/*      */ 
/*  391 */       data.timezoneMin = (negate * parseInt(buffer, stop, stop + 2));
/*      */ 
/*  393 */       if (stop + 2 != end) {
/*  394 */         throw new RuntimeException("Error in parsing time zone");
/*      */       }
/*  396 */       if ((data.timezoneHr != 0) || (data.timezoneMin != 0))
/*  397 */         data.normalized = false;
/*      */     }
/*      */     else {
/*  400 */       throw new RuntimeException("Error in parsing time zone");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int indexOf(String buffer, int start, int end, char ch)
/*      */   {
/*  416 */     for (int i = start; i < end; i++) {
/*  417 */       if (buffer.charAt(i) == ch) {
/*  418 */         return i;
/*      */       }
/*      */     }
/*  421 */     return -1;
/*      */   }
/*      */ 
/*      */   protected void validateDateTime(DateTimeData data)
/*      */   {
/*  438 */     if (data.year == 0) {
/*  439 */       throw new RuntimeException("The year \"0000\" is an illegal year value");
/*      */     }
/*      */ 
/*  443 */     if ((data.month < 1) || (data.month > 12)) {
/*  444 */       throw new RuntimeException("The month must have values 1 to 12");
/*      */     }
/*      */ 
/*  449 */     if ((data.day > maxDayInMonthFor(data.year, data.month)) || (data.day < 1)) {
/*  450 */       throw new RuntimeException("The day must have values 1 to 31");
/*      */     }
/*      */ 
/*  454 */     if ((data.hour > 23) || (data.hour < 0)) {
/*  455 */       if ((data.hour == 24) && (data.minute == 0) && (data.second == 0.0D)) {
/*  456 */         data.hour = 0;
/*  457 */         if (++data.day > maxDayInMonthFor(data.year, data.month)) {
/*  458 */           data.day = 1;
/*  459 */           if (++data.month > 12) {
/*  460 */             data.month = 1;
/*      */ 
/*  463 */             if (++data.year == 0)
/*  464 */               data.year = 1;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  469 */         throw new RuntimeException("Hour must have values 0-23, unless 24:00:00");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  474 */     if ((data.minute > 59) || (data.minute < 0)) {
/*  475 */       throw new RuntimeException("Minute must have values 0-59");
/*      */     }
/*      */ 
/*  479 */     if ((data.second >= 60.0D) || (data.second < 0.0D)) {
/*  480 */       throw new RuntimeException("Second must have values 0-59");
/*      */     }
/*      */ 
/*  485 */     if ((data.timezoneHr > 14) || (data.timezoneHr < -14)) {
/*  486 */       throw new RuntimeException("Time zone should have range -14:00 to +14:00");
/*      */     }
/*  488 */     if (((data.timezoneHr == 14) || (data.timezoneHr == -14)) && (data.timezoneMin != 0))
/*  489 */       throw new RuntimeException("Time zone should have range -14:00 to +14:00");
/*  490 */     if ((data.timezoneMin > 59) || (data.timezoneMin < -59))
/*  491 */       throw new RuntimeException("Minute must have values 0-59");
/*      */   }
/*      */ 
/*      */   protected int findUTCSign(String buffer, int start, int end)
/*      */   {
/*  506 */     for (int i = start; i < end; i++) {
/*  507 */       int c = buffer.charAt(i);
/*  508 */       if ((c == 90) || (c == 43) || (c == 45)) {
/*  509 */         return i;
/*      */       }
/*      */     }
/*      */ 
/*  513 */     return -1;
/*      */   }
/*      */ 
/*      */   protected final boolean isNextCharUTCSign(String buffer, int start, int end)
/*      */   {
/*  521 */     if (start < end) {
/*  522 */       char c = buffer.charAt(start);
/*  523 */       return (c == 'Z') || (c == '+') || (c == '-');
/*      */     }
/*  525 */     return false;
/*      */   }
/*      */ 
/*      */   protected int parseInt(String buffer, int start, int end)
/*      */     throws NumberFormatException
/*      */   {
/*  539 */     int radix = 10;
/*  540 */     int result = 0;
/*  541 */     int digit = 0;
/*  542 */     int limit = -2147483647;
/*  543 */     int multmin = limit / radix;
/*  544 */     int i = start;
/*      */     do {
/*  546 */       digit = getDigit(buffer.charAt(i));
/*  547 */       if (digit < 0) {
/*  548 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */       }
/*  550 */       if (result < multmin) {
/*  551 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */       }
/*  553 */       result *= radix;
/*  554 */       if (result < limit + digit) {
/*  555 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */       }
/*  557 */       result -= digit;
/*      */ 
/*  559 */       i++; } while (i < end);
/*  560 */     return -result;
/*      */   }
/*      */ 
/*      */   protected int parseIntYear(String buffer, int end)
/*      */   {
/*  565 */     int radix = 10;
/*  566 */     int result = 0;
/*  567 */     boolean negative = false;
/*  568 */     int i = 0;
/*      */ 
/*  571 */     int digit = 0;
/*      */     int limit;
/*  573 */     if (buffer.charAt(0) == '-') {
/*  574 */       negative = true;
/*  575 */       int limit = -2147483648;
/*  576 */       i++;
/*      */     }
/*      */     else {
/*  579 */       limit = -2147483647;
/*      */     }
/*  581 */     int multmin = limit / radix;
/*  582 */     while (i < end) {
/*  583 */       digit = getDigit(buffer.charAt(i++));
/*  584 */       if (digit < 0) {
/*  585 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */       }
/*  587 */       if (result < multmin) {
/*  588 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */       }
/*  590 */       result *= radix;
/*  591 */       if (result < limit + digit) {
/*  592 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */       }
/*  594 */       result -= digit;
/*      */     }
/*      */ 
/*  597 */     if (negative) {
/*  598 */       if (i > 1) {
/*  599 */         return result;
/*      */       }
/*  601 */       throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */     }
/*      */ 
/*  604 */     return -result;
/*      */   }
/*      */ 
/*      */   protected void normalize(DateTimeData date)
/*      */   {
/*  621 */     int negate = -1;
/*      */ 
/*  627 */     int temp = date.minute + negate * date.timezoneMin;
/*  628 */     int carry = fQuotient(temp, 60);
/*  629 */     date.minute = mod(temp, 60, carry);
/*      */ 
/*  635 */     temp = date.hour + negate * date.timezoneHr + carry;
/*  636 */     carry = fQuotient(temp, 24);
/*  637 */     date.hour = mod(temp, 24, carry);
/*      */ 
/*  643 */     date.day += carry;
/*      */     while (true)
/*      */     {
/*  646 */       temp = maxDayInMonthFor(date.year, date.month);
/*  647 */       if (date.day < 1) {
/*  648 */         date.day += maxDayInMonthFor(date.year, date.month - 1);
/*  649 */         carry = -1; } else {
/*  650 */         if (date.day <= temp) break;
/*  651 */         date.day -= temp;
/*  652 */         carry = 1;
/*      */       }
/*      */ 
/*  656 */       temp = date.month + carry;
/*  657 */       date.month = modulo(temp, 1, 13);
/*  658 */       date.year += fQuotient(temp, 1, 13);
/*  659 */       if (date.year == 0) {
/*  660 */         date.year = ((date.timezoneHr < 0) || (date.timezoneMin < 0) ? 1 : -1);
/*      */       }
/*      */     }
/*  663 */     date.utc = 90;
/*      */   }
/*      */ 
/*      */   protected void saveUnnormalized(DateTimeData date)
/*      */   {
/*  670 */     date.unNormYear = date.year;
/*  671 */     date.unNormMonth = date.month;
/*  672 */     date.unNormDay = date.day;
/*  673 */     date.unNormHour = date.hour;
/*  674 */     date.unNormMinute = date.minute;
/*  675 */     date.unNormSecond = date.second;
/*      */   }
/*      */ 
/*      */   protected void resetDateObj(DateTimeData data)
/*      */   {
/*  684 */     data.year = 0;
/*  685 */     data.month = 0;
/*  686 */     data.day = 0;
/*  687 */     data.hour = 0;
/*  688 */     data.minute = 0;
/*  689 */     data.second = 0.0D;
/*  690 */     data.utc = 0;
/*  691 */     data.timezoneHr = 0;
/*  692 */     data.timezoneMin = 0;
/*      */   }
/*      */ 
/*      */   protected int maxDayInMonthFor(int year, int month)
/*      */   {
/*  704 */     if ((month == 4) || (month == 6) || (month == 9) || (month == 11))
/*  705 */       return 30;
/*  706 */     if (month == 2) {
/*  707 */       if (isLeapYear(year)) {
/*  708 */         return 29;
/*      */       }
/*  710 */       return 28;
/*      */     }
/*      */ 
/*  713 */     return 31;
/*      */   }
/*      */ 
/*      */   private boolean isLeapYear(int year)
/*      */   {
/*  720 */     return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
/*      */   }
/*      */ 
/*      */   protected int mod(int a, int b, int quotient)
/*      */   {
/*  728 */     return a - quotient * b;
/*      */   }
/*      */ 
/*      */   protected int fQuotient(int a, int b)
/*      */   {
/*  737 */     return (int)Math.floor(a / b);
/*      */   }
/*      */ 
/*      */   protected int modulo(int temp, int low, int high)
/*      */   {
/*  745 */     int a = temp - low;
/*  746 */     int b = high - low;
/*  747 */     return mod(a, b, fQuotient(a, b)) + low;
/*      */   }
/*      */ 
/*      */   protected int fQuotient(int temp, int low, int high)
/*      */   {
/*  756 */     return fQuotient(temp - low, high - low);
/*      */   }
/*      */ 
/*      */   protected String dateToString(DateTimeData date) {
/*  760 */     StringBuffer message = new StringBuffer(25);
/*  761 */     append(message, date.year, 4);
/*  762 */     message.append('-');
/*  763 */     append(message, date.month, 2);
/*  764 */     message.append('-');
/*  765 */     append(message, date.day, 2);
/*  766 */     message.append('T');
/*  767 */     append(message, date.hour, 2);
/*  768 */     message.append(':');
/*  769 */     append(message, date.minute, 2);
/*  770 */     message.append(':');
/*  771 */     append(message, date.second);
/*  772 */     append(message, (char)date.utc, 0);
/*  773 */     return message.toString();
/*      */   }
/*      */ 
/*      */   protected final void append(StringBuffer message, int value, int nch) {
/*  777 */     if (value == -2147483648) {
/*  778 */       message.append(value);
/*  779 */       return;
/*      */     }
/*  781 */     if (value < 0) {
/*  782 */       message.append('-');
/*  783 */       value = -value;
/*      */     }
/*  785 */     if (nch == 4) {
/*  786 */       if (value < 10)
/*  787 */         message.append("000");
/*  788 */       else if (value < 100)
/*  789 */         message.append("00");
/*  790 */       else if (value < 1000) {
/*  791 */         message.append('0');
/*      */       }
/*  793 */       message.append(value);
/*  794 */     } else if (nch == 2) {
/*  795 */       if (value < 10) {
/*  796 */         message.append('0');
/*      */       }
/*  798 */       message.append(value);
/*      */     }
/*  800 */     else if (value != 0) {
/*  801 */       message.append((char)value);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void append(StringBuffer message, double value)
/*      */   {
/*  807 */     if (value < 0.0D) {
/*  808 */       message.append('-');
/*  809 */       value = -value;
/*      */     }
/*  811 */     if (value < 10.0D) {
/*  812 */       message.append('0');
/*      */     }
/*  814 */     append2(message, value);
/*      */   }
/*      */ 
/*      */   protected final void append2(StringBuffer message, double value) {
/*  818 */     int intValue = (int)value;
/*  819 */     if (value == intValue)
/*  820 */       message.append(intValue);
/*      */     else
/*  822 */       append3(message, value);
/*      */   }
/*      */ 
/*      */   private void append3(StringBuffer message, double value)
/*      */   {
/*  827 */     String d = String.valueOf(value);
/*  828 */     int eIndex = d.indexOf('E');
/*  829 */     if (eIndex == -1) {
/*  830 */       message.append(d);
/*  831 */       return;
/*      */     }
/*      */ 
/*  834 */     if (value < 1.0D)
/*      */     {
/*      */       int exp;
/*      */       try {
/*  838 */         exp = parseInt(d, eIndex + 2, d.length());
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  842 */         message.append(d);
/*  843 */         return;
/*      */       }
/*  845 */       message.append("0.");
/*  846 */       for (int i = 1; i < exp; i++) {
/*  847 */         message.append('0');
/*      */       }
/*      */ 
/*  850 */       int end = eIndex - 1;
/*  851 */       while (end > 0) {
/*  852 */         char c = d.charAt(end);
/*  853 */         if (c != '0') {
/*      */           break;
/*      */         }
/*  856 */         end--;
/*      */       }
/*      */ 
/*  859 */       for (int i = 0; i <= end; i++) {
/*  860 */         char c = d.charAt(i);
/*  861 */         if (c != '.')
/*  862 */           message.append(c);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       int exp;
/*      */       try {
/*  869 */         exp = parseInt(d, eIndex + 1, d.length());
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  873 */         message.append(d);
/*  874 */         return;
/*      */       }
/*  876 */       int integerEnd = exp + 2;
/*  877 */       for (int i = 0; i < eIndex; i++) {
/*  878 */         char c = d.charAt(i);
/*  879 */         if (c != '.') {
/*  880 */           if (i == integerEnd) {
/*  881 */             message.append('.');
/*      */           }
/*  883 */           message.append(c);
/*      */         }
/*      */       }
/*      */ 
/*  887 */       for (int i = integerEnd - eIndex; i > 0; i--)
/*  888 */         message.append('0');
/*      */     }
/*      */   }
/*      */ 
/*      */   protected double parseSecond(String buffer, int start, int end)
/*      */     throws NumberFormatException
/*      */   {
/*  895 */     int dot = -1;
/*  896 */     for (int i = start; i < end; i++) {
/*  897 */       char ch = buffer.charAt(i);
/*  898 */       if (ch == '.')
/*  899 */         dot = i;
/*  900 */       else if ((ch > '9') || (ch < '0')) {
/*  901 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */       }
/*      */     }
/*  904 */     if (dot == -1) {
/*  905 */       if (start + 2 != end)
/*  906 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */     }
/*  908 */     else if ((start + 2 != dot) || (dot + 1 == end)) {
/*  909 */       throw new NumberFormatException("'" + buffer + "' has wrong format");
/*      */     }
/*  911 */     return Double.parseDouble(buffer.substring(start, end));
/*      */   }
/*      */ 
/*      */   private void cloneDate(DateTimeData finalValue, DateTimeData tempDate)
/*      */   {
/*  918 */     tempDate.year = finalValue.year;
/*  919 */     tempDate.month = finalValue.month;
/*  920 */     tempDate.day = finalValue.day;
/*  921 */     tempDate.hour = finalValue.hour;
/*  922 */     tempDate.minute = finalValue.minute;
/*  923 */     tempDate.second = finalValue.second;
/*  924 */     tempDate.utc = finalValue.utc;
/*  925 */     tempDate.timezoneHr = finalValue.timezoneHr;
/*  926 */     tempDate.timezoneMin = finalValue.timezoneMin;
/*      */   }
/*      */ 
/*      */   protected XMLGregorianCalendar getXMLGregorianCalendar(DateTimeData data)
/*      */   {
/* 1156 */     return null;
/*      */   }
/*      */ 
/*      */   protected Duration getDuration(DateTimeData data) {
/* 1160 */     return null;
/*      */   }
/*      */ 
/*      */   protected final BigDecimal getFractionalSecondsAsBigDecimal(DateTimeData data) {
/* 1164 */     StringBuffer buf = new StringBuffer();
/* 1165 */     append3(buf, data.unNormSecond);
/* 1166 */     String value = buf.toString();
/* 1167 */     int index = value.indexOf('.');
/* 1168 */     if (index == -1) {
/* 1169 */       return null;
/*      */     }
/* 1171 */     value = value.substring(index);
/* 1172 */     BigDecimal _val = new BigDecimal(value);
/* 1173 */     if (_val.compareTo(BigDecimal.valueOf(0L)) == 0) {
/* 1174 */       return null;
/*      */     }
/* 1176 */     return _val;
/*      */   }
/*      */ 
/*      */   static final class DateTimeData
/*      */     implements XSDateTime
/*      */   {
/*      */     int year;
/*      */     int month;
/*      */     int day;
/*      */     int hour;
/*      */     int minute;
/*      */     int utc;
/*      */     double second;
/*      */     int timezoneHr;
/*      */     int timezoneMin;
/*      */     private String originalValue;
/*  938 */     boolean normalized = true;
/*      */     int unNormYear;
/*      */     int unNormMonth;
/*      */     int unNormDay;
/*      */     int unNormHour;
/*      */     int unNormMinute;
/*      */     double unNormSecond;
/*      */     int position;
/*      */     final AbstractDateTimeDV type;
/*      */     private volatile String canonical;
/*      */ 
/*      */     public DateTimeData(String originalValue, AbstractDateTimeDV type)
/*      */     {
/*  955 */       this.originalValue = originalValue;
/*  956 */       this.type = type;
/*      */     }
/*      */ 
/*      */     public DateTimeData(int year, int month, int day, int hour, int minute, double second, int utc, String originalValue, boolean normalized, AbstractDateTimeDV type)
/*      */     {
/*  961 */       this.year = year;
/*  962 */       this.month = month;
/*  963 */       this.day = day;
/*  964 */       this.hour = hour;
/*  965 */       this.minute = minute;
/*  966 */       this.second = second;
/*  967 */       this.utc = utc;
/*  968 */       this.type = type;
/*  969 */       this.originalValue = originalValue;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object obj)
/*      */     {
/*  974 */       if (!(obj instanceof DateTimeData)) {
/*  975 */         return false;
/*      */       }
/*  977 */       return this.type.compareDates(this, (DateTimeData)obj, true) == 0;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  988 */       DateTimeData tempDate = new DateTimeData(null, this.type);
/*  989 */       this.type.cloneDate(this, tempDate);
/*  990 */       this.type.normalize(tempDate);
/*  991 */       return this.type.dateToString(tempDate).hashCode();
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  996 */       if (this.canonical == null) {
/*  997 */         this.canonical = this.type.dateToString(this);
/*      */       }
/*  999 */       return this.canonical;
/*      */     }
/*      */ 
/*      */     public int getYears()
/*      */     {
/* 1007 */       if ((this.type instanceof DurationDV)) {
/* 1008 */         return 0;
/*      */       }
/* 1010 */       return this.normalized ? this.year : this.unNormYear;
/*      */     }
/*      */ 
/*      */     public int getMonths()
/*      */     {
/* 1018 */       if ((this.type instanceof DurationDV)) {
/* 1019 */         return this.year * 12 + this.month;
/*      */       }
/* 1021 */       return this.normalized ? this.month : this.unNormMonth;
/*      */     }
/*      */ 
/*      */     public int getDays()
/*      */     {
/* 1029 */       if ((this.type instanceof DurationDV)) {
/* 1030 */         return 0;
/*      */       }
/* 1032 */       return this.normalized ? this.day : this.unNormDay;
/*      */     }
/*      */ 
/*      */     public int getHours()
/*      */     {
/* 1040 */       if ((this.type instanceof DurationDV)) {
/* 1041 */         return 0;
/*      */       }
/* 1043 */       return this.normalized ? this.hour : this.unNormHour;
/*      */     }
/*      */ 
/*      */     public int getMinutes()
/*      */     {
/* 1051 */       if ((this.type instanceof DurationDV)) {
/* 1052 */         return 0;
/*      */       }
/* 1054 */       return this.normalized ? this.minute : this.unNormMinute;
/*      */     }
/*      */ 
/*      */     public double getSeconds()
/*      */     {
/* 1062 */       if ((this.type instanceof DurationDV)) {
/* 1063 */         return this.day * 24 * 60 * 60 + this.hour * 60 * 60 + this.minute * 60 + this.second;
/*      */       }
/* 1065 */       return this.normalized ? this.second : this.unNormSecond;
/*      */     }
/*      */ 
/*      */     public boolean hasTimeZone()
/*      */     {
/* 1073 */       return this.utc != 0;
/*      */     }
/*      */ 
/*      */     public int getTimeZoneHours()
/*      */     {
/* 1081 */       return this.timezoneHr;
/*      */     }
/*      */ 
/*      */     public int getTimeZoneMinutes()
/*      */     {
/* 1089 */       return this.timezoneMin;
/*      */     }
/*      */ 
/*      */     public String getLexicalValue()
/*      */     {
/* 1097 */       return this.originalValue;
/*      */     }
/*      */ 
/*      */     public XSDateTime normalize()
/*      */     {
/* 1105 */       if (!this.normalized) {
/* 1106 */         DateTimeData dt = (DateTimeData)clone();
/* 1107 */         dt.normalized = true;
/* 1108 */         return dt;
/*      */       }
/* 1110 */       return this;
/*      */     }
/*      */ 
/*      */     public boolean isNormalized()
/*      */     {
/* 1118 */       return this.normalized;
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/* 1123 */       DateTimeData dt = new DateTimeData(this.year, this.month, this.day, this.hour, this.minute, this.second, this.utc, this.originalValue, this.normalized, this.type);
/*      */ 
/* 1125 */       dt.canonical = this.canonical;
/* 1126 */       dt.position = this.position;
/* 1127 */       dt.timezoneHr = this.timezoneHr;
/* 1128 */       dt.timezoneMin = this.timezoneMin;
/* 1129 */       dt.unNormYear = this.unNormYear;
/* 1130 */       dt.unNormMonth = this.unNormMonth;
/* 1131 */       dt.unNormDay = this.unNormDay;
/* 1132 */       dt.unNormHour = this.unNormHour;
/* 1133 */       dt.unNormMinute = this.unNormMinute;
/* 1134 */       dt.unNormSecond = this.unNormSecond;
/* 1135 */       return dt;
/*      */     }
/*      */ 
/*      */     public XMLGregorianCalendar getXMLGregorianCalendar()
/*      */     {
/* 1143 */       return this.type.getXMLGregorianCalendar(this);
/*      */     }
/*      */ 
/*      */     public Duration getDuration()
/*      */     {
/* 1151 */       return this.type.getDuration(this);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
 * JD-Core Version:    0.6.2
 */