/*    */ package com.sun.xml.internal.ws.resources;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*    */ 
/*    */ public final class SenderMessages
/*    */ {
/* 40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.sender");
/* 41 */   private static final Localizer localizer = new Localizer();
/*    */ 
/*    */   public static Localizable localizableSENDER_REQUEST_ILLEGAL_VALUE_FOR_CONTENT_NEGOTIATION(Object arg0) {
/* 44 */     return messageFactory.getMessage("sender.request.illegalValueForContentNegotiation", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */   public static String SENDER_REQUEST_ILLEGAL_VALUE_FOR_CONTENT_NEGOTIATION(Object arg0)
/*    */   {
/* 52 */     return localizer.localize(localizableSENDER_REQUEST_ILLEGAL_VALUE_FOR_CONTENT_NEGOTIATION(arg0));
/*    */   }
/*    */ 
/*    */   public static Localizable localizableSENDER_RESPONSE_CANNOT_DECODE_FAULT_DETAIL() {
/* 56 */     return messageFactory.getMessage("sender.response.cannotDecodeFaultDetail", new Object[0]);
/*    */   }
/*    */ 
/*    */   public static String SENDER_RESPONSE_CANNOT_DECODE_FAULT_DETAIL()
/*    */   {
/* 64 */     return localizer.localize(localizableSENDER_RESPONSE_CANNOT_DECODE_FAULT_DETAIL());
/*    */   }
/*    */ 
/*    */   public static Localizable localizableSENDER_NESTED_ERROR(Object arg0) {
/* 68 */     return messageFactory.getMessage("sender.nestedError", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */   public static String SENDER_NESTED_ERROR(Object arg0)
/*    */   {
/* 76 */     return localizer.localize(localizableSENDER_NESTED_ERROR(arg0));
/*    */   }
/*    */ 
/*    */   public static Localizable localizableSENDER_REQUEST_MESSAGE_NOT_READY() {
/* 80 */     return messageFactory.getMessage("sender.request.messageNotReady", new Object[0]);
/*    */   }
/*    */ 
/*    */   public static String SENDER_REQUEST_MESSAGE_NOT_READY()
/*    */   {
/* 88 */     return localizer.localize(localizableSENDER_REQUEST_MESSAGE_NOT_READY());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.SenderMessages
 * JD-Core Version:    0.6.2
 */