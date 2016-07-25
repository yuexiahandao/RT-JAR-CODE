/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import java.math.BigInteger;
/*    */ import javax.xml.datatype.DatatypeFactory;
/*    */ import javax.xml.datatype.Duration;
/*    */ 
/*    */ class YearMonthDurationDV extends DurationDV
/*    */ {
/*    */   public Object getActualValue(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: iconst_1
/*    */     //   3: invokevirtual 73	com/sun/org/apache/xerces/internal/impl/dv/xs/YearMonthDurationDV:parse	(Ljava/lang/String;I)Lcom/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData;
/*    */     //   6: areturn
/*    */     //   7: astore_3
/*    */     //   8: new 40	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*    */     //   11: dup
/*    */     //   12: ldc 2
/*    */     //   14: iconst_2
/*    */     //   15: anewarray 46	java/lang/Object
/*    */     //   18: dup
/*    */     //   19: iconst_0
/*    */     //   20: aload_1
/*    */     //   21: aastore
/*    */     //   22: dup
/*    */     //   23: iconst_1
/*    */     //   24: ldc 3
/*    */     //   26: aastore
/*    */     //   27: invokespecial 71	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*    */     //   30: athrow
/*    */     //
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	6	7	java/lang/Exception
/*    */   }
/*    */ 
/*    */   protected Duration getDuration(AbstractDateTimeDV.DateTimeData date)
/*    */   {
/* 52 */     int sign = 1;
/* 53 */     if ((date.year < 0) || (date.month < 0)) {
/* 54 */       sign = -1;
/*    */     }
/* 56 */     return datatypeFactory.newDuration(sign == 1, date.year != -2147483648 ? BigInteger.valueOf(sign * date.year) : null, date.month != -2147483648 ? BigInteger.valueOf(sign * date.month) : null, null, null, null, null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.YearMonthDurationDV
 * JD-Core Version:    0.6.2
 */