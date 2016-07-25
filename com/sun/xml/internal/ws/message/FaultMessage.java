/*    */ package com.sun.xml.internal.ws.message;
/*    */ 
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.api.message.FilterMessageImpl;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class FaultMessage extends FilterMessageImpl
/*    */ {
/*    */ 
/*    */   @Nullable
/*    */   private final QName detailEntryName;
/*    */ 
/*    */   public FaultMessage(Message delegate, @Nullable QName detailEntryName)
/*    */   {
/* 47 */     super(delegate);
/* 48 */     this.detailEntryName = detailEntryName;
/*    */   }
/*    */ 
/*    */   @Nullable
/*    */   public QName getFirstDetailEntryName() {
/* 53 */     return this.detailEntryName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.FaultMessage
 * JD-Core Version:    0.6.2
 */