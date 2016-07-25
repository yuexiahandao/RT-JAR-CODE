/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ public class MonthDayDV extends AbstractDateTimeDV
/*     */ {
/*     */   private static final int MONTHDAY_SIZE = 7;
/*     */ 
/*     */   public Object getActualValue(String content, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual 157	com/sun/org/apache/xerces/internal/impl/dv/xs/MonthDayDV:parse	(Ljava/lang/String;)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*     */     //   5: areturn
/*     */     //   6: astore_3
/*     */     //   7: new 77	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*     */     //   10: dup
/*     */     //   11: ldc 4
/*     */     //   13: iconst_2
/*     */     //   14: anewarray 83	java/lang/Object
/*     */     //   17: dup
/*     */     //   18: iconst_0
/*     */     //   19: aload_1
/*     */     //   20: aastore
/*     */     //   21: dup
/*     */     //   22: iconst_1
/*     */     //   23: ldc 5
/*     */     //   25: aastore
/*     */     //   26: invokespecial 147	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   29: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	5	6	java/lang/Exception
/*     */   }
/*     */ 
/*     */   protected AbstractDateTimeDV.DateTimeData parse(String str)
/*     */     throws SchemaDateTimeException
/*     */   {
/*  68 */     AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
/*  69 */     int len = str.length();
/*     */ 
/*  72 */     date.year = 2000;
/*     */ 
/*  74 */     if ((str.charAt(0) != '-') || (str.charAt(1) != '-')) {
/*  75 */       throw new SchemaDateTimeException("Invalid format for gMonthDay: " + str);
/*     */     }
/*  77 */     date.month = parseInt(str, 2, 4);
/*  78 */     int start = 4;
/*     */ 
/*  80 */     if (str.charAt(start++) != '-') {
/*  81 */       throw new SchemaDateTimeException("Invalid format for gMonthDay: " + str);
/*     */     }
/*     */ 
/*  84 */     date.day = parseInt(str, start, start + 2);
/*     */ 
/*  86 */     if (7 < len) {
/*  87 */       if (!isNextCharUTCSign(str, 7, len)) {
/*  88 */         throw new SchemaDateTimeException("Error in month parsing:" + str);
/*     */       }
/*     */ 
/*  91 */       getTimeZone(str, date, 7, len);
/*     */     }
/*     */ 
/*  96 */     validateDateTime(date);
/*     */ 
/*  99 */     saveUnnormalized(date);
/*     */ 
/* 101 */     if ((date.utc != 0) && (date.utc != 90)) {
/* 102 */       normalize(date);
/*     */     }
/* 104 */     date.position = 1;
/* 105 */     return date;
/*     */   }
/*     */ 
/*     */   protected String dateToString(AbstractDateTimeDV.DateTimeData date)
/*     */   {
/* 115 */     StringBuffer message = new StringBuffer(8);
/* 116 */     message.append('-');
/* 117 */     message.append('-');
/* 118 */     append(message, date.month, 2);
/* 119 */     message.append('-');
/* 120 */     append(message, date.day, 2);
/* 121 */     append(message, (char)date.utc, 0);
/* 122 */     return message.toString();
/*     */   }
/*     */ 
/*     */   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
/* 126 */     return datatypeFactory.newXMLGregorianCalendar(-2147483648, date.unNormMonth, date.unNormDay, -2147483648, -2147483648, -2147483648, -2147483648, date.hasTimeZone() ? date.timezoneHr * 60 + date.timezoneMin : -2147483648);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.MonthDayDV
 * JD-Core Version:    0.6.2
 */