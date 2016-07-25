/*    */ package com.sun.xml.internal.ws.addressing.model;
/*    */ 
/*    */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public class InvalidAddressingHeaderException extends WebServiceException
/*    */ {
/*    */   private QName problemHeader;
/*    */   private QName subsubcode;
/*    */ 
/*    */   public InvalidAddressingHeaderException(QName problemHeader, QName subsubcode)
/*    */   {
/* 52 */     super(AddressingMessages.INVALID_ADDRESSING_HEADER_EXCEPTION(problemHeader, subsubcode));
/* 53 */     this.problemHeader = problemHeader;
/* 54 */     this.subsubcode = subsubcode;
/*    */   }
/*    */ 
/*    */   public QName getProblemHeader() {
/* 58 */     return this.problemHeader;
/*    */   }
/*    */ 
/*    */   public QName getSubsubcode() {
/* 62 */     return this.subsubcode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException
 * JD-Core Version:    0.6.2
 */