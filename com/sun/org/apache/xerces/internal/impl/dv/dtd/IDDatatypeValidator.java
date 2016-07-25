/*    */ package com.sun.org.apache.xerces.internal.impl.dv.dtd;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*    */ 
/*    */ public class IDDatatypeValidator
/*    */   implements DatatypeValidator
/*    */ {
/*    */   public void validate(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/* 66 */     if (context.useNamespaces()) {
/* 67 */       if (!XMLChar.isValidNCName(content)) {
/* 68 */         throw new InvalidDatatypeValueException("IDInvalidWithNamespaces", new Object[] { content });
/*    */       }
/*    */ 
/*    */     }
/* 72 */     else if (!XMLChar.isValidName(content)) {
/* 73 */       throw new InvalidDatatypeValueException("IDInvalid", new Object[] { content });
/*    */     }
/*    */ 
/* 77 */     if (context.isIdDeclared(content)) {
/* 78 */       throw new InvalidDatatypeValueException("IDNotUnique", new Object[] { content });
/*    */     }
/*    */ 
/* 81 */     context.addId(content);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.dtd.IDDatatypeValidator
 * JD-Core Version:    0.6.2
 */