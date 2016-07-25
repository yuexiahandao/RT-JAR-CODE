/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ public class MonthDV extends AbstractDateTimeDV
/*     */ {
/*     */   public Object getActualValue(String content, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual 151	com/sun/org/apache/xerces/internal/impl/dv/xs/MonthDV:parse	(Ljava/lang/String;)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*     */     //   5: areturn
/*     */     //   6: astore_3
/*     */     //   7: new 73	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*     */     //   10: dup
/*     */     //   11: ldc 4
/*     */     //   13: iconst_2
/*     */     //   14: anewarray 79	java/lang/Object
/*     */     //   17: dup
/*     */     //   18: iconst_0
/*     */     //   19: aload_1
/*     */     //   20: aastore
/*     */     //   21: dup
/*     */     //   22: iconst_1
/*     */     //   23: ldc 5
/*     */     //   25: aastore
/*     */     //   26: invokespecial 141	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
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
/*  65 */     AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
/*  66 */     int len = str.length();
/*     */ 
/*  69 */     date.year = 2000;
/*  70 */     date.day = 1;
/*  71 */     if ((str.charAt(0) != '-') || (str.charAt(1) != '-')) {
/*  72 */       throw new SchemaDateTimeException("Invalid format for gMonth: " + str);
/*     */     }
/*  74 */     int stop = 4;
/*  75 */     date.month = parseInt(str, 2, stop);
/*     */ 
/*  80 */     if ((str.length() >= stop + 2) && (str.charAt(stop) == '-') && (str.charAt(stop + 1) == '-'))
/*     */     {
/*  82 */       stop += 2;
/*     */     }
/*  84 */     if (stop < len) {
/*  85 */       if (!isNextCharUTCSign(str, stop, len)) {
/*  86 */         throw new SchemaDateTimeException("Error in month parsing: " + str);
/*     */       }
/*     */ 
/*  89 */       getTimeZone(str, date, stop, len);
/*     */     }
/*     */ 
/*  93 */     validateDateTime(date);
/*     */ 
/*  96 */     saveUnnormalized(date);
/*     */ 
/*  98 */     if ((date.utc != 0) && (date.utc != 90)) {
/*  99 */       normalize(date);
/*     */     }
/* 101 */     date.position = 1;
/* 102 */     return date;
/*     */   }
/*     */ 
/*     */   protected String dateToString(AbstractDateTimeDV.DateTimeData date)
/*     */   {
/* 154 */     StringBuffer message = new StringBuffer(5);
/* 155 */     message.append('-');
/* 156 */     message.append('-');
/* 157 */     append(message, date.month, 2);
/* 158 */     append(message, (char)date.utc, 0);
/* 159 */     return message.toString();
/*     */   }
/*     */ 
/*     */   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
/* 163 */     return datatypeFactory.newXMLGregorianCalendar(-2147483648, date.unNormMonth, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, date.hasTimeZone() ? date.timezoneHr * 60 + date.timezoneMin : -2147483648);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.MonthDV
 * JD-Core Version:    0.6.2
 */