/*    */ package com.sun.xml.internal.ws.developer;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ 
/*    */ public abstract class ValidationErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   protected Packet packet;
/*    */ 
/*    */   public void setPacket(Packet packet)
/*    */   {
/* 56 */     this.packet = packet;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.ValidationErrorHandler
 * JD-Core Version:    0.6.2
 */