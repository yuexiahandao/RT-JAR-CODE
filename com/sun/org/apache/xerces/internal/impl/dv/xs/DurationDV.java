/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.Duration;
/*     */ 
/*     */ public class DurationDV extends AbstractDateTimeDV
/*     */ {
/*     */   public static final int DURATION_TYPE = 0;
/*     */   public static final int YEARMONTHDURATION_TYPE = 1;
/*     */   public static final int DAYTIMEDURATION_TYPE = 2;
/*  52 */   private static final AbstractDateTimeDV.DateTimeData[] DATETIMES = { new AbstractDateTimeDV.DateTimeData(1696, 9, 1, 0, 0, 0.0D, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1697, 2, 1, 0, 0, 0.0D, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 3, 1, 0, 0, 0.0D, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 7, 1, 0, 0, 0.0D, 90, null, true, null) };
/*     */ 
/*     */   public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: iconst_0
/*     */     //   3: invokevirtual 245	com/sun/org/apache/xerces/internal/impl/dv/xs/DurationDV:parse	(Ljava/lang/String;I)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*     */     //   6: areturn
/*     */     //   7: astore_3
/*     */     //   8: new 138	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*     */     //   11: dup
/*     */     //   12: ldc 4
/*     */     //   14: iconst_2
/*     */     //   15: anewarray 147	java/lang/Object
/*     */     //   18: dup
/*     */     //   19: iconst_0
/*     */     //   20: aload_1
/*     */     //   21: aastore
/*     */     //   22: dup
/*     */     //   23: iconst_1
/*     */     //   24: ldc 5
/*     */     //   26: aastore
/*     */     //   27: invokespecial 229	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   30: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	6	7	java/lang/Exception } 
/*  75 */   protected AbstractDateTimeDV.DateTimeData parse(String str, int durationType) throws SchemaDateTimeException { int len = str.length();
/*  76 */     AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
/*     */ 
/*  78 */     int start = 0;
/*  79 */     char c = str.charAt(start++);
/*  80 */     if ((c != 'P') && (c != '-')) {
/*  81 */       throw new SchemaDateTimeException();
/*     */     }
/*     */ 
/*  84 */     date.utc = (c == '-' ? 45 : 0);
/*  85 */     if ((c == '-') && (str.charAt(start++) != 'P')) {
/*  86 */       throw new SchemaDateTimeException();
/*     */     }
/*     */ 
/*  90 */     int negate = 1;
/*     */ 
/*  92 */     if (date.utc == 45) {
/*  93 */       negate = -1;
/*     */     }
/*     */ 
/*  97 */     boolean designator = false;
/*     */ 
/*  99 */     int endDate = indexOf(str, start, len, 'T');
/* 100 */     if (endDate == -1) {
/* 101 */       endDate = len;
/*     */     }
/* 103 */     else if (durationType == 1) {
/* 104 */       throw new SchemaDateTimeException();
/*     */     }
/*     */ 
/* 108 */     int end = indexOf(str, start, endDate, 'Y');
/* 109 */     if (end != -1)
/*     */     {
/* 111 */       if (durationType == 2) {
/* 112 */         throw new SchemaDateTimeException();
/*     */       }
/*     */ 
/* 116 */       date.year = (negate * parseInt(str, start, end));
/* 117 */       start = end + 1;
/* 118 */       designator = true;
/*     */     }
/*     */ 
/* 121 */     end = indexOf(str, start, endDate, 'M');
/* 122 */     if (end != -1)
/*     */     {
/* 124 */       if (durationType == 2) {
/* 125 */         throw new SchemaDateTimeException();
/*     */       }
/*     */ 
/* 129 */       date.month = (negate * parseInt(str, start, end));
/* 130 */       start = end + 1;
/* 131 */       designator = true;
/*     */     }
/*     */ 
/* 134 */     end = indexOf(str, start, endDate, 'D');
/* 135 */     if (end != -1)
/*     */     {
/* 137 */       if (durationType == 1) {
/* 138 */         throw new SchemaDateTimeException();
/*     */       }
/*     */ 
/* 142 */       date.day = (negate * parseInt(str, start, end));
/* 143 */       start = end + 1;
/* 144 */       designator = true;
/*     */     }
/*     */ 
/* 147 */     if ((len == endDate) && (start != len)) {
/* 148 */       throw new SchemaDateTimeException();
/*     */     }
/* 150 */     if (len != endDate)
/*     */     {
/* 156 */       end = indexOf(str, ++start, len, 'H');
/* 157 */       if (end != -1)
/*     */       {
/* 159 */         date.hour = (negate * parseInt(str, start, end));
/* 160 */         start = end + 1;
/* 161 */         designator = true;
/*     */       }
/*     */ 
/* 164 */       end = indexOf(str, start, len, 'M');
/* 165 */       if (end != -1)
/*     */       {
/* 167 */         date.minute = (negate * parseInt(str, start, end));
/* 168 */         start = end + 1;
/* 169 */         designator = true;
/*     */       }
/*     */ 
/* 172 */       end = indexOf(str, start, len, 'S');
/* 173 */       if (end != -1)
/*     */       {
/* 175 */         date.second = (negate * parseSecond(str, start, end));
/* 176 */         start = end + 1;
/* 177 */         designator = true;
/*     */       }
/*     */ 
/* 181 */       if ((start != len) || (str.charAt(--start) == 'T')) {
/* 182 */         throw new SchemaDateTimeException();
/*     */       }
/*     */     }
/*     */ 
/* 186 */     if (!designator) {
/* 187 */       throw new SchemaDateTimeException();
/*     */     }
/*     */ 
/* 190 */     return date;
/*     */   }
/*     */ 
/*     */   protected short compareDates(AbstractDateTimeDV.DateTimeData date1, AbstractDateTimeDV.DateTimeData date2, boolean strict)
/*     */   {
/* 214 */     short resultB = 2;
/*     */ 
/* 216 */     short resultA = compareOrder(date1, date2);
/* 217 */     if (resultA == 0) {
/* 218 */       return 0;
/*     */     }
/*     */ 
/* 221 */     AbstractDateTimeDV.DateTimeData[] result = new AbstractDateTimeDV.DateTimeData[2];
/* 222 */     result[0] = new AbstractDateTimeDV.DateTimeData(null, this);
/* 223 */     result[1] = new AbstractDateTimeDV.DateTimeData(null, this);
/*     */ 
/* 226 */     AbstractDateTimeDV.DateTimeData tempA = addDuration(date1, DATETIMES[0], result[0]);
/* 227 */     AbstractDateTimeDV.DateTimeData tempB = addDuration(date2, DATETIMES[0], result[1]);
/* 228 */     resultA = compareOrder(tempA, tempB);
/* 229 */     if (resultA == 2) {
/* 230 */       return 2;
/*     */     }
/*     */ 
/* 233 */     tempA = addDuration(date1, DATETIMES[1], result[0]);
/* 234 */     tempB = addDuration(date2, DATETIMES[1], result[1]);
/* 235 */     resultB = compareOrder(tempA, tempB);
/* 236 */     resultA = compareResults(resultA, resultB, strict);
/* 237 */     if (resultA == 2) {
/* 238 */       return 2;
/*     */     }
/*     */ 
/* 241 */     tempA = addDuration(date1, DATETIMES[2], result[0]);
/* 242 */     tempB = addDuration(date2, DATETIMES[2], result[1]);
/* 243 */     resultB = compareOrder(tempA, tempB);
/* 244 */     resultA = compareResults(resultA, resultB, strict);
/* 245 */     if (resultA == 2) {
/* 246 */       return 2;
/*     */     }
/*     */ 
/* 249 */     tempA = addDuration(date1, DATETIMES[3], result[0]);
/* 250 */     tempB = addDuration(date2, DATETIMES[3], result[1]);
/* 251 */     resultB = compareOrder(tempA, tempB);
/* 252 */     resultA = compareResults(resultA, resultB, strict);
/*     */ 
/* 254 */     return resultA;
/*     */   }
/*     */ 
/*     */   private short compareResults(short resultA, short resultB, boolean strict)
/*     */   {
/* 259 */     if (resultB == 2) {
/* 260 */       return 2;
/*     */     }
/* 262 */     if ((resultA != resultB) && (strict)) {
/* 263 */       return 2;
/*     */     }
/* 265 */     if ((resultA != resultB) && (!strict)) {
/* 266 */       if ((resultA != 0) && (resultB != 0)) {
/* 267 */         return 2;
/*     */       }
/*     */ 
/* 270 */       return resultA != 0 ? resultA : resultB;
/*     */     }
/*     */ 
/* 273 */     return resultA;
/*     */   }
/*     */ 
/*     */   private AbstractDateTimeDV.DateTimeData addDuration(AbstractDateTimeDV.DateTimeData date, AbstractDateTimeDV.DateTimeData addto, AbstractDateTimeDV.DateTimeData duration)
/*     */   {
/* 282 */     resetDateObj(duration);
/*     */ 
/* 284 */     int temp = addto.month + date.month;
/* 285 */     duration.month = modulo(temp, 1, 13);
/* 286 */     int carry = fQuotient(temp, 1, 13);
/*     */ 
/* 289 */     duration.year = (addto.year + date.year + carry);
/*     */ 
/* 292 */     double dtemp = addto.second + date.second;
/* 293 */     carry = (int)Math.floor(dtemp / 60.0D);
/* 294 */     duration.second = (dtemp - carry * 60);
/*     */ 
/* 297 */     temp = addto.minute + date.minute + carry;
/* 298 */     carry = fQuotient(temp, 60);
/* 299 */     duration.minute = mod(temp, 60, carry);
/*     */ 
/* 302 */     temp = addto.hour + date.hour + carry;
/* 303 */     carry = fQuotient(temp, 24);
/* 304 */     duration.hour = mod(temp, 24, carry);
/*     */ 
/* 307 */     duration.day = (addto.day + date.day + carry);
/*     */     while (true)
/*     */     {
/* 311 */       temp = maxDayInMonthFor(duration.year, duration.month);
/* 312 */       if (duration.day < 1) {
/* 313 */         duration.day += maxDayInMonthFor(duration.year, duration.month - 1);
/* 314 */         carry = -1;
/*     */       } else {
/* 316 */         if (duration.day <= temp) break;
/* 317 */         duration.day -= temp;
/* 318 */         carry = 1;
/*     */       }
/*     */ 
/* 323 */       temp = duration.month + carry;
/* 324 */       duration.month = modulo(temp, 1, 13);
/* 325 */       duration.year += fQuotient(temp, 1, 13);
/*     */     }
/*     */ 
/* 328 */     duration.utc = 90;
/* 329 */     return duration;
/*     */   }
/*     */ 
/*     */   protected double parseSecond(String buffer, int start, int end) throws NumberFormatException
/*     */   {
/* 334 */     int dot = -1;
/* 335 */     for (int i = start; i < end; i++) {
/* 336 */       char ch = buffer.charAt(i);
/* 337 */       if (ch == '.')
/* 338 */         dot = i;
/* 339 */       else if ((ch > '9') || (ch < '0'))
/* 340 */         throw new NumberFormatException("'" + buffer + "' has wrong format");
/*     */     }
/* 342 */     if (dot + 1 == end) {
/* 343 */       throw new NumberFormatException("'" + buffer + "' has wrong format");
/*     */     }
/* 345 */     double value = Double.parseDouble(buffer.substring(start, end));
/* 346 */     if (value == (1.0D / 0.0D)) {
/* 347 */       throw new NumberFormatException("'" + buffer + "' has wrong format");
/*     */     }
/* 349 */     return value;
/*     */   }
/*     */ 
/*     */   protected String dateToString(AbstractDateTimeDV.DateTimeData date) {
/* 353 */     StringBuffer message = new StringBuffer(30);
/* 354 */     if ((date.year < 0) || (date.month < 0) || (date.day < 0) || (date.hour < 0) || (date.minute < 0) || (date.second < 0.0D))
/*     */     {
/* 356 */       message.append('-');
/*     */     }
/* 358 */     message.append('P');
/* 359 */     message.append((date.year < 0 ? -1 : 1) * date.year);
/* 360 */     message.append('Y');
/* 361 */     message.append((date.month < 0 ? -1 : 1) * date.month);
/* 362 */     message.append('M');
/* 363 */     message.append((date.day < 0 ? -1 : 1) * date.day);
/* 364 */     message.append('D');
/* 365 */     message.append('T');
/* 366 */     message.append((date.hour < 0 ? -1 : 1) * date.hour);
/* 367 */     message.append('H');
/* 368 */     message.append((date.minute < 0 ? -1 : 1) * date.minute);
/* 369 */     message.append('M');
/* 370 */     append2(message, (date.second < 0.0D ? -1 : 1) * date.second);
/* 371 */     message.append('S');
/*     */ 
/* 373 */     return message.toString();
/*     */   }
/*     */ 
/*     */   protected Duration getDuration(AbstractDateTimeDV.DateTimeData date) {
/* 377 */     int sign = 1;
/* 378 */     if ((date.year < 0) || (date.month < 0) || (date.day < 0) || (date.hour < 0) || (date.minute < 0) || (date.second < 0.0D))
/*     */     {
/* 380 */       sign = -1;
/*     */     }
/* 382 */     return datatypeFactory.newDuration(sign == 1, date.year != -2147483648 ? BigInteger.valueOf(sign * date.year) : null, date.month != -2147483648 ? BigInteger.valueOf(sign * date.month) : null, date.day != -2147483648 ? BigInteger.valueOf(sign * date.day) : null, date.hour != -2147483648 ? BigInteger.valueOf(sign * date.hour) : null, date.minute != -2147483648 ? BigInteger.valueOf(sign * date.minute) : null, date.second != -2147483648.0D ? new BigDecimal(String.valueOf(sign * date.second)) : null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.DurationDV
 * JD-Core Version:    0.6.2
 */