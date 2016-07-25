/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*    */ 
/*    */ public class IDREFDV extends TypeValidator
/*    */ {
/*    */   public short getAllowedFacets()
/*    */   {
/* 39 */     return 2079;
/*    */   }
/*    */ 
/*    */   public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
/* 43 */     if (!XMLChar.isValidNCName(content)) {
/* 44 */       throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "NCName" });
/*    */     }
/* 46 */     return content;
/*    */   }
/*    */ 
/*    */   public void checkExtraRules(Object value, ValidationContext context) throws InvalidDatatypeValueException {
/* 50 */     context.addIdRef((String)value);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.IDREFDV
 * JD-Core Version:    0.6.2
 */