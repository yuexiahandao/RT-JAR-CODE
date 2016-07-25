/*    */ package com.sun.xml.internal.ws.addressing.model;
/*    */ 
/*    */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public class ActionNotSupportedException extends WebServiceException
/*    */ {
/*    */   private String action;
/*    */ 
/*    */   public ActionNotSupportedException(String action)
/*    */   {
/* 39 */     super(AddressingMessages.ACTION_NOT_SUPPORTED_EXCEPTION(action));
/* 40 */     this.action = action;
/*    */   }
/*    */ 
/*    */   public String getAction() {
/* 44 */     return this.action;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException
 * JD-Core Version:    0.6.2
 */