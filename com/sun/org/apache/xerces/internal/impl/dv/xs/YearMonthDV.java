/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import javax.xml.datatype.DatatypeFactory;
/*    */ import javax.xml.datatype.XMLGregorianCalendar;
/*    */ 
/*    */ public class YearMonthDV extends AbstractDateTimeDV
/*    */ {
/*    */   public Object getActualValue(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: invokevirtual 136	com/sun/org/apache/xerces/internal/impl/dv/xs/YearMonthDV:parse	(Ljava/lang/String;)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*    */     //   5: areturn
/*    */     //   6: astore_3
/*    */     //   7: new 65	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*    */     //   10: dup
/*    */     //   11: ldc 2
/*    */     //   13: iconst_2
/*    */     //   14: anewarray 71	java/lang/Object
/*    */     //   17: dup
/*    */     //   18: iconst_0
/*    */     //   19: aload_1
/*    */     //   20: aastore
/*    */     //   21: dup
/*    */     //   22: iconst_1
/*    */     //   23: ldc 3
/*    */     //   25: aastore
/*    */     //   26: invokespecial 128	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*    */     //   29: athrow
/*    */     //
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	5	6	java/lang/Exception
/*    */   }
/*    */ 
/*    */   protected AbstractDateTimeDV.DateTimeData parse(String str)
/*    */     throws SchemaDateTimeException
/*    */   {
/* 64 */     AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
/* 65 */     int len = str.length();
/*    */ 
/* 68 */     int end = getYearMonth(str, 0, len, date);
/* 69 */     date.day = 1;
/* 70 */     parseTimeZone(str, end, len, date);
/*    */ 
/* 74 */     validateDateTime(date);
/*    */ 
/* 77 */     saveUnnormalized(date);
/*    */ 
/* 79 */     if ((date.utc != 0) && (date.utc != 90)) {
/* 80 */       normalize(date);
/*    */     }
/* 82 */     date.position = 0;
/* 83 */     return date;
/*    */   }
/*    */ 
/*    */   protected String dateToString(AbstractDateTimeDV.DateTimeData date) {
/* 87 */     StringBuffer message = new StringBuffer(25);
/* 88 */     append(message, date.year, 4);
/* 89 */     message.append('-');
/* 90 */     append(message, date.month, 2);
/* 91 */     append(message, (char)date.utc, 0);
/* 92 */     return message.toString();
/*    */   }
/*    */ 
/*    */   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
/* 96 */     return datatypeFactory.newXMLGregorianCalendar(date.unNormYear, date.unNormMonth, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, date.hasTimeZone() ? date.timezoneHr * 60 + date.timezoneMin : -2147483648);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.YearMonthDV
 * JD-Core Version:    0.6.2
 */