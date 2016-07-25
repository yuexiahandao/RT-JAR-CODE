/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ public class DayDV extends AbstractDateTimeDV
/*     */ {
/*     */   private static final int DAY_SIZE = 5;
/*     */ 
/*     */   public Object getActualValue(String content, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual 147	com/sun/org/apache/xerces/internal/impl/dv/xs/DayDV:parse	(Ljava/lang/String;)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*     */     //   5: areturn
/*     */     //   6: astore_3
/*     */     //   7: new 72	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*     */     //   10: dup
/*     */     //   11: ldc 3
/*     */     //   13: iconst_2
/*     */     //   14: anewarray 78	java/lang/Object
/*     */     //   17: dup
/*     */     //   18: iconst_0
/*     */     //   19: aload_1
/*     */     //   20: aastore
/*     */     //   21: dup
/*     */     //   22: iconst_1
/*     */     //   23: ldc 4
/*     */     //   25: aastore
/*     */     //   26: invokespecial 137	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
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
/*  61 */     AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
/*  62 */     int len = str.length();
/*     */ 
/*  64 */     if ((str.charAt(0) != '-') || (str.charAt(1) != '-') || (str.charAt(2) != '-')) {
/*  65 */       throw new SchemaDateTimeException("Error in day parsing");
/*     */     }
/*     */ 
/*  69 */     date.year = 2000;
/*  70 */     date.month = 1;
/*     */ 
/*  72 */     date.day = parseInt(str, 3, 5);
/*     */ 
/*  74 */     if (5 < len) {
/*  75 */       if (!isNextCharUTCSign(str, 5, len)) {
/*  76 */         throw new SchemaDateTimeException("Error in day parsing");
/*     */       }
/*     */ 
/*  79 */       getTimeZone(str, date, 5, len);
/*     */     }
/*     */ 
/*  84 */     validateDateTime(date);
/*     */ 
/*  87 */     saveUnnormalized(date);
/*     */ 
/*  89 */     if ((date.utc != 0) && (date.utc != 90)) {
/*  90 */       normalize(date);
/*     */     }
/*  92 */     date.position = 2;
/*  93 */     return date;
/*     */   }
/*     */ 
/*     */   protected String dateToString(AbstractDateTimeDV.DateTimeData date)
/*     */   {
/* 103 */     StringBuffer message = new StringBuffer(6);
/* 104 */     message.append('-');
/* 105 */     message.append('-');
/* 106 */     message.append('-');
/* 107 */     append(message, date.day, 2);
/* 108 */     append(message, (char)date.utc, 0);
/* 109 */     return message.toString();
/*     */   }
/*     */ 
/*     */   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
/* 113 */     return datatypeFactory.newXMLGregorianCalendar(-2147483648, -2147483648, date.unNormDay, -2147483648, -2147483648, -2147483648, -2147483648, date.hasTimeZone() ? date.timezoneHr * 60 + date.timezoneMin : -2147483648);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.DayDV
 * JD-Core Version:    0.6.2
 */