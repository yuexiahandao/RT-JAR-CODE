/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class HandlerMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.handler");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableHANDLER_MESSAGE_CONTEXT_INVALID_CLASS(Object arg0, Object arg1) {
/*  44 */     return messageFactory.getMessage("handler.messageContext.invalid.class", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String HANDLER_MESSAGE_CONTEXT_INVALID_CLASS(Object arg0, Object arg1)
/*     */   {
/*  52 */     return localizer.localize(localizableHANDLER_MESSAGE_CONTEXT_INVALID_CLASS(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableCANNOT_EXTEND_HANDLER_DIRECTLY(Object arg0) {
/*  56 */     return messageFactory.getMessage("cannot.extend.handler.directly", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String CANNOT_EXTEND_HANDLER_DIRECTLY(Object arg0)
/*     */   {
/*  64 */     return localizer.localize(localizableCANNOT_EXTEND_HANDLER_DIRECTLY(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHANDLER_NOT_VALID_TYPE(Object arg0) {
/*  68 */     return messageFactory.getMessage("handler.not.valid.type", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HANDLER_NOT_VALID_TYPE(Object arg0)
/*     */   {
/*  76 */     return localizer.localize(localizableHANDLER_NOT_VALID_TYPE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableCANNOT_INSTANTIATE_HANDLER(Object arg0, Object arg1) {
/*  80 */     return messageFactory.getMessage("cannot.instantiate.handler", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String CANNOT_INSTANTIATE_HANDLER(Object arg0, Object arg1)
/*     */   {
/*  88 */     return localizer.localize(localizableCANNOT_INSTANTIATE_HANDLER(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHANDLER_CHAIN_CONTAINS_HANDLER_ONLY(Object arg0) {
/*  92 */     return messageFactory.getMessage("handler.chain.contains.handler.only", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HANDLER_CHAIN_CONTAINS_HANDLER_ONLY(Object arg0)
/*     */   {
/* 100 */     return localizer.localize(localizableHANDLER_CHAIN_CONTAINS_HANDLER_ONLY(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHANDLER_NESTED_ERROR(Object arg0) {
/* 104 */     return messageFactory.getMessage("handler.nestedError", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HANDLER_NESTED_ERROR(Object arg0)
/*     */   {
/* 112 */     return localizer.localize(localizableHANDLER_NESTED_ERROR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHANDLER_PREDESTROY_IGNORE(Object arg0) {
/* 116 */     return messageFactory.getMessage("handler.predestroy.ignore", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HANDLER_PREDESTROY_IGNORE(Object arg0)
/*     */   {
/* 124 */     return localizer.localize(localizableHANDLER_PREDESTROY_IGNORE(arg0));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.HandlerMessages
 * JD-Core Version:    0.6.2
 */