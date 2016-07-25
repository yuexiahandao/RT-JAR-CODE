/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import javax.xml.datatype.DatatypeFactory;
/*    */ import javax.xml.datatype.Duration;
/*    */ 
/*    */ class DayTimeDurationDV extends DurationDV
/*    */ {
/*    */   public Object getActualValue(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: iconst_2
/*    */     //   3: invokevirtual 89	com/sun/org/apache/xerces/internal/impl/dv/xs/DayTimeDurationDV:parse	(Ljava/lang/String;I)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*    */     //   6: areturn
/*    */     //   7: astore_3
/*    */     //   8: new 49	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*    */     //   11: dup
/*    */     //   12: ldc 2
/*    */     //   14: iconst_2
/*    */     //   15: anewarray 55	java/lang/Object
/*    */     //   18: dup
/*    */     //   19: iconst_0
/*    */     //   20: aload_1
/*    */     //   21: aastore
/*    */     //   22: dup
/*    */     //   23: iconst_1
/*    */     //   24: ldc 3
/*    */     //   26: aastore
/*    */     //   27: invokespecial 88	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*    */     //   30: athrow
/*    */     //
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	6	7	java/lang/Exception
/*    */   }
/*    */ 
/*    */   protected Duration getDuration(AbstractDateTimeDV.DateTimeData date)
/*    */   {
/* 53 */     int sign = 1;
/* 54 */     if ((date.day < 0) || (date.hour < 0) || (date.minute < 0) || (date.second < 0.0D)) {
/* 55 */       sign = -1;
/*    */     }
/* 57 */     return datatypeFactory.newDuration(sign == 1, null, null, date.day != -2147483648 ? BigInteger.valueOf(sign * date.day) : null, date.hour != -2147483648 ? BigInteger.valueOf(sign * date.hour) : null, date.minute != -2147483648 ? BigInteger.valueOf(sign * date.minute) : null, date.second != -2147483648.0D ? new BigDecimal(String.valueOf(sign * date.second)) : null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.DayTimeDurationDV
 * JD-Core Version:    0.6.2
 */