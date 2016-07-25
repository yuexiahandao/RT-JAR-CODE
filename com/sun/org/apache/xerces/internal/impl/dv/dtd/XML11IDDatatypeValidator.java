/*    */ package com.sun.org.apache.xerces.internal.impl.dv.dtd;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*    */ 
/*    */ public class XML11IDDatatypeValidator extends IDDatatypeValidator
/*    */ {
/*    */   public void validate(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/* 68 */     if (context.useNamespaces()) {
/* 69 */       if (!XML11Char.isXML11ValidNCName(content)) {
/* 70 */         throw new InvalidDatatypeValueException("IDInvalidWithNamespaces", new Object[] { content });
/*    */       }
/*    */ 
/*    */     }
/* 74 */     else if (!XML11Char.isXML11ValidName(content)) {
/* 75 */       throw new InvalidDatatypeValueException("IDInvalid", new Object[] { content });
/*    */     }
/*    */ 
/* 79 */     if (context.isIdDeclared(content)) {
/* 80 */       throw new InvalidDatatypeValueException("IDNotUnique", new Object[] { content });
/*    */     }
/*    */ 
/* 83 */     context.addId(content);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11IDDatatypeValidator
 * JD-Core Version:    0.6.2
 */