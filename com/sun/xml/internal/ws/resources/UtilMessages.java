/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class UtilMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.util");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableUTIL_LOCATION(Object arg0, Object arg1) {
/*  44 */     return messageFactory.getMessage("util.location", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String UTIL_LOCATION(Object arg0, Object arg1)
/*     */   {
/*  52 */     return localizer.localize(localizableUTIL_LOCATION(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUTIL_FAILED_TO_PARSE_HANDLERCHAIN_FILE(Object arg0, Object arg1) {
/*  56 */     return messageFactory.getMessage("util.failed.to.parse.handlerchain.file", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String UTIL_FAILED_TO_PARSE_HANDLERCHAIN_FILE(Object arg0, Object arg1)
/*     */   {
/*  64 */     return localizer.localize(localizableUTIL_FAILED_TO_PARSE_HANDLERCHAIN_FILE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUTIL_PARSER_WRONG_ELEMENT(Object arg0, Object arg1, Object arg2) {
/*  68 */     return messageFactory.getMessage("util.parser.wrong.element", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String UTIL_PARSER_WRONG_ELEMENT(Object arg0, Object arg1, Object arg2)
/*     */   {
/*  76 */     return localizer.localize(localizableUTIL_PARSER_WRONG_ELEMENT(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUTIL_HANDLER_CLASS_NOT_FOUND(Object arg0) {
/*  80 */     return messageFactory.getMessage("util.handler.class.not.found", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String UTIL_HANDLER_CLASS_NOT_FOUND(Object arg0)
/*     */   {
/*  88 */     return localizer.localize(localizableUTIL_HANDLER_CLASS_NOT_FOUND(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUTIL_HANDLER_ENDPOINT_INTERFACE_NO_WEBSERVICE(Object arg0) {
/*  92 */     return messageFactory.getMessage("util.handler.endpoint.interface.no.webservice", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String UTIL_HANDLER_ENDPOINT_INTERFACE_NO_WEBSERVICE(Object arg0)
/*     */   {
/* 100 */     return localizer.localize(localizableUTIL_HANDLER_ENDPOINT_INTERFACE_NO_WEBSERVICE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUTIL_HANDLER_NO_WEBSERVICE_ANNOTATION(Object arg0) {
/* 104 */     return messageFactory.getMessage("util.handler.no.webservice.annotation", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String UTIL_HANDLER_NO_WEBSERVICE_ANNOTATION(Object arg0)
/*     */   {
/* 112 */     return localizer.localize(localizableUTIL_HANDLER_NO_WEBSERVICE_ANNOTATION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUTIL_FAILED_TO_FIND_HANDLERCHAIN_FILE(Object arg0, Object arg1) {
/* 116 */     return messageFactory.getMessage("util.failed.to.find.handlerchain.file", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String UTIL_FAILED_TO_FIND_HANDLERCHAIN_FILE(Object arg0, Object arg1)
/*     */   {
/* 124 */     return localizer.localize(localizableUTIL_FAILED_TO_FIND_HANDLERCHAIN_FILE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUTIL_HANDLER_CANNOT_COMBINE_SOAPMESSAGEHANDLERS() {
/* 128 */     return messageFactory.getMessage("util.handler.cannot.combine.soapmessagehandlers", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String UTIL_HANDLER_CANNOT_COMBINE_SOAPMESSAGEHANDLERS()
/*     */   {
/* 136 */     return localizer.localize(localizableUTIL_HANDLER_CANNOT_COMBINE_SOAPMESSAGEHANDLERS());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.UtilMessages
 * JD-Core Version:    0.6.2
 */