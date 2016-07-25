/*    */ package com.sun.org.apache.xerces.internal.impl.dv.dtd;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*    */ 
/*    */ public class XML11IDREFDatatypeValidator extends IDREFDatatypeValidator
/*    */ {
/*    */   public void validate(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/* 65 */     if (context.useNamespaces()) {
/* 66 */       if (!XML11Char.isXML11ValidNCName(content)) {
/* 67 */         throw new InvalidDatatypeValueException("IDREFInvalidWithNamespaces", new Object[] { content });
/*    */       }
/*    */ 
/*    */     }
/* 71 */     else if (!XML11Char.isXML11ValidName(content)) {
/* 72 */       throw new InvalidDatatypeValueException("IDREFInvalid", new Object[] { content });
/*    */     }
/*    */ 
/* 76 */     context.addIdRef(content);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11IDREFDatatypeValidator
 * JD-Core Version:    0.6.2
 */