/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import java.math.BigInteger;
/*    */ import javax.xml.datatype.DatatypeFactory;
/*    */ import javax.xml.datatype.XMLGregorianCalendar;
/*    */ 
/*    */ public class DateTimeDV extends AbstractDateTimeDV
/*    */ {
/*    */   public Object getActualValue(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: invokevirtual 151	com/sun/org/apache/xerces/internal/impl/dv/xs/DateTimeDV:parse	(Ljava/lang/String;)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*    */     //   5: areturn
/*    */     //   6: astore_3
/*    */     //   7: new 74	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*    */     //   10: dup
/*    */     //   11: ldc 4
/*    */     //   13: iconst_2
/*    */     //   14: anewarray 80	java/lang/Object
/*    */     //   17: dup
/*    */     //   18: iconst_0
/*    */     //   19: aload_1
/*    */     //   20: aastore
/*    */     //   21: dup
/*    */     //   22: iconst_1
/*    */     //   23: ldc 5
/*    */     //   25: aastore
/*    */     //   26: invokespecial 143	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
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
/* 60 */     AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
/* 61 */     int len = str.length();
/*    */ 
/* 63 */     int end = indexOf(str, 0, len, 'T');
/*    */ 
/* 66 */     int dateEnd = getDate(str, 0, end, date);
/* 67 */     getTime(str, end + 1, len, date);
/*    */ 
/* 70 */     if (dateEnd != end) {
/* 71 */       throw new RuntimeException(str + " is an invalid dateTime dataype value. " + "Invalid character(s) seprating date and time values.");
/*    */     }
/*    */ 
/* 79 */     validateDateTime(date);
/*    */ 
/* 82 */     saveUnnormalized(date);
/*    */ 
/* 84 */     if ((date.utc != 0) && (date.utc != 90)) {
/* 85 */       normalize(date);
/*    */     }
/* 87 */     return date;
/*    */   }
/*    */ 
/*    */   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
/* 91 */     return datatypeFactory.newXMLGregorianCalendar(BigInteger.valueOf(date.unNormYear), date.unNormMonth, date.unNormDay, date.unNormHour, date.unNormMinute, (int)date.unNormSecond, date.unNormSecond != 0.0D ? getFractionalSecondsAsBigDecimal(date) : null, date.hasTimeZone() ? date.timezoneHr * 60 + date.timezoneMin : -2147483648);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.DateTimeDV
 * JD-Core Version:    0.6.2
 */