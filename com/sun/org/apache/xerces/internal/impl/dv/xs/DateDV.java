/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import javax.xml.datatype.DatatypeFactory;
/*    */ import javax.xml.datatype.XMLGregorianCalendar;
/*    */ 
/*    */ public class DateDV extends DateTimeDV
/*    */ {
/*    */   public Object getActualValue(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: invokevirtual 136	com/sun/org/apache/xerces/internal/impl/dv/xs/DateDV:parse	(Ljava/lang/String;)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*    */     //   5: areturn
/*    */     //   6: astore_3
/*    */     //   7: new 65	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*    */     //   10: dup
/*    */     //   11: ldc 2
/*    */     //   13: iconst_2
/*    */     //   14: anewarray 72	java/lang/Object
/*    */     //   17: dup
/*    */     //   18: iconst_0
/*    */     //   19: aload_1
/*    */     //   20: aastore
/*    */     //   21: dup
/*    */     //   22: iconst_1
/*    */     //   23: ldc 3
/*    */     //   25: aastore
/*    */     //   26: invokespecial 129	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
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
/* 58 */     AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
/* 59 */     int len = str.length();
/*    */ 
/* 61 */     int end = getDate(str, 0, len, date);
/* 62 */     parseTimeZone(str, end, len, date);
/*    */ 
/* 66 */     validateDateTime(date);
/*    */ 
/* 69 */     saveUnnormalized(date);
/*    */ 
/* 71 */     if ((date.utc != 0) && (date.utc != 90)) {
/* 72 */       normalize(date);
/*    */     }
/* 74 */     return date;
/*    */   }
/*    */ 
/*    */   protected String dateToString(AbstractDateTimeDV.DateTimeData date) {
/* 78 */     StringBuffer message = new StringBuffer(25);
/* 79 */     append(message, date.year, 4);
/* 80 */     message.append('-');
/* 81 */     append(message, date.month, 2);
/* 82 */     message.append('-');
/* 83 */     append(message, date.day, 2);
/* 84 */     append(message, (char)date.utc, 0);
/* 85 */     return message.toString();
/*    */   }
/*    */ 
/*    */   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
/* 89 */     return datatypeFactory.newXMLGregorianCalendar(date.unNormYear, date.unNormMonth, date.unNormDay, -2147483648, -2147483648, -2147483648, -2147483648, date.hasTimeZone() ? date.timezoneHr * 60 + date.timezoneMin : -2147483648);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.DateDV
 * JD-Core Version:    0.6.2
 */