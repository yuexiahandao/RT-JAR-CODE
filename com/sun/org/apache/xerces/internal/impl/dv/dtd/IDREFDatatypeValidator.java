/*    */ package com.sun.org.apache.xerces.internal.impl.dv.dtd;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*    */ 
/*    */ public class IDREFDatatypeValidator
/*    */   implements DatatypeValidator
/*    */ {
/*    */   public void validate(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/* 63 */     if (context.useNamespaces()) {
/* 64 */       if (!XMLChar.isValidNCName(content)) {
/* 65 */         throw new InvalidDatatypeValueException("IDREFInvalidWithNamespaces", new Object[] { content });
/*    */       }
/*    */ 
/*    */     }
/* 69 */     else if (!XMLChar.isValidName(content)) {
/* 70 */       throw new InvalidDatatypeValueException("IDREFInvalid", new Object[] { content });
/*    */     }
/*    */ 
/* 74 */     context.addIdRef(content);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.dtd.IDREFDatatypeValidator
 * JD-Core Version:    0.6.2
 */