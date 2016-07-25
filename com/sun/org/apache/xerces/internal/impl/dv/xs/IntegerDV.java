/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ 
/*    */ public class IntegerDV extends DecimalDV
/*    */ {
/*    */   public Object getActualValue(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: new 29	com/sun/org/apache/xerces/internal/impl/dv/xs/DecimalDV$XDecimal
/*    */     //   3: dup
/*    */     //   4: aload_1
/*    */     //   5: iconst_1
/*    */     //   6: invokespecial 43	com/sun/org/apache/xerces/internal/impl/dv/xs/DecimalDV$XDecimal:<init>	(Ljava/lang/String;Z)V
/*    */     //   9: areturn
/*    */     //   10: astore_3
/*    */     //   11: new 27	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*    */     //   14: dup
/*    */     //   15: ldc 1
/*    */     //   17: iconst_2
/*    */     //   18: anewarray 32	java/lang/Object
/*    */     //   21: dup
/*    */     //   22: iconst_0
/*    */     //   23: aload_1
/*    */     //   24: aastore
/*    */     //   25: dup
/*    */     //   26: iconst_1
/*    */     //   27: ldc 2
/*    */     //   29: aastore
/*    */     //   30: invokespecial 41	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*    */     //   33: athrow
/*    */     //
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	9	10	java/lang/NumberFormatException
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.IntegerDV
 * JD-Core Version:    0.6.2
 */