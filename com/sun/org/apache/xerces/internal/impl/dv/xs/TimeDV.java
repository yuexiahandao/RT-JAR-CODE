/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ public class TimeDV extends AbstractDateTimeDV
/*     */ {
/*     */   public Object getActualValue(String content, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual 153	com/sun/org/apache/xerces/internal/impl/dv/xs/TimeDV:parse	(Ljava/lang/String;)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*     */     //   5: areturn
/*     */     //   6: astore_3
/*     */     //   7: new 70	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*     */     //   10: dup
/*     */     //   11: ldc 2
/*     */     //   13: iconst_2
/*     */     //   14: anewarray 76	java/lang/Object
/*     */     //   17: dup
/*     */     //   18: iconst_0
/*     */     //   19: aload_1
/*     */     //   20: aastore
/*     */     //   21: dup
/*     */     //   22: iconst_1
/*     */     //   23: ldc 3
/*     */     //   25: aastore
/*     */     //   26: invokespecial 144	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
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
/*  70 */     date.year = 2000;
/*  71 */     date.month = 1;
/*  72 */     date.day = 15;
/*  73 */     getTime(str, 0, len, date);
/*     */ 
/*  77 */     validateDateTime(date);
/*     */ 
/*  80 */     saveUnnormalized(date);
/*     */ 
/*  82 */     if ((date.utc != 0) && (date.utc != 90)) {
/*  83 */       normalize(date);
/*     */     }
/*  85 */     date.position = 2;
/*  86 */     return date;
/*     */   }
/*     */ 
/*     */   protected String dateToString(AbstractDateTimeDV.DateTimeData date)
/*     */   {
/*  96 */     StringBuffer message = new StringBuffer(16);
/*  97 */     append(message, date.hour, 2);
/*  98 */     message.append(':');
/*  99 */     append(message, date.minute, 2);
/* 100 */     message.append(':');
/* 101 */     append(message, date.second);
/*     */ 
/* 103 */     append(message, (char)date.utc, 0);
/* 104 */     return message.toString();
/*     */   }
/*     */ 
/*     */   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
/* 108 */     return datatypeFactory.newXMLGregorianCalendar(null, -2147483648, -2147483648, date.unNormHour, date.unNormMinute, (int)date.unNormSecond, date.unNormSecond != 0.0D ? getFractionalSecondsAsBigDecimal(date) : null, date.hasTimeZone() ? date.timezoneHr * 60 + date.timezoneMin : -2147483648);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.TimeDV
 * JD-Core Version:    0.6.2
 */