/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ 
/*    */ public class BooleanDV extends TypeValidator
/*    */ {
/* 37 */   private static final String[] fValueSpace = { "false", "true", "0", "1" };
/*    */ 
/*    */   public short getAllowedFacets() {
/* 40 */     return 24;
/*    */   }
/*    */ 
/*    */   public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
/* 44 */     Boolean ret = null;
/*    */ 
/* 46 */     if ((content.equals(fValueSpace[0])) || (content.equals(fValueSpace[2])))
/* 47 */       ret = Boolean.FALSE;
/* 48 */     else if ((content.equals(fValueSpace[1])) || (content.equals(fValueSpace[3])))
/* 49 */       ret = Boolean.TRUE;
/*    */     else
/* 51 */       throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "boolean" });
/* 52 */     return ret;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.BooleanDV
 * JD-Core Version:    0.6.2
 */