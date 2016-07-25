/*    */ package com.sun.org.apache.xerces.internal.impl.dv.dtd;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ public class ListDatatypeValidator
/*    */   implements DatatypeValidator
/*    */ {
/*    */   DatatypeValidator fItemValidator;
/*    */ 
/*    */   public ListDatatypeValidator(DatatypeValidator itemDV)
/*    */   {
/* 42 */     this.fItemValidator = itemDV;
/*    */   }
/*    */ 
/*    */   public void validate(String content, ValidationContext context)
/*    */     throws InvalidDatatypeValueException
/*    */   {
/* 57 */     StringTokenizer parsedList = new StringTokenizer(content, " ");
/* 58 */     int numberOfTokens = parsedList.countTokens();
/* 59 */     if (numberOfTokens == 0) {
/* 60 */       throw new InvalidDatatypeValueException("EmptyList", null);
/*    */     }
/*    */ 
/* 63 */     while (parsedList.hasMoreTokens())
/* 64 */       this.fItemValidator.validate(parsedList.nextToken(), context);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.dtd.ListDatatypeValidator
 * JD-Core Version:    0.6.2
 */