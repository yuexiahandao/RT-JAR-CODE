/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class DispatchMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.dispatch");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableINVALID_NULLARG_XMLHTTP_REQUEST_METHOD(Object arg0, Object arg1) {
/*  44 */     return messageFactory.getMessage("invalid.nullarg.xmlhttp.request.method", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_NULLARG_XMLHTTP_REQUEST_METHOD(Object arg0, Object arg1)
/*     */   {
/*  52 */     return localizer.localize(localizableINVALID_NULLARG_XMLHTTP_REQUEST_METHOD(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_SOAPMESSAGE_DISPATCH_MSGMODE(Object arg0, Object arg1) {
/*  56 */     return messageFactory.getMessage("invalid.soapmessage.dispatch.msgmode", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_SOAPMESSAGE_DISPATCH_MSGMODE(Object arg0, Object arg1)
/*     */   {
/*  64 */     return localizer.localize(localizableINVALID_SOAPMESSAGE_DISPATCH_MSGMODE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_RESPONSE_DESERIALIZATION() {
/*  68 */     return messageFactory.getMessage("invalid.response.deserialization", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String INVALID_RESPONSE_DESERIALIZATION()
/*     */   {
/*  76 */     return localizer.localize(localizableINVALID_RESPONSE_DESERIALIZATION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_QUERY_LEADING_CHAR(Object arg0) {
/*  80 */     return messageFactory.getMessage("invalid.query.leading.char", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_QUERY_LEADING_CHAR(Object arg0)
/*     */   {
/*  88 */     return localizer.localize(localizableINVALID_QUERY_LEADING_CHAR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_QUERY_STRING(Object arg0) {
/*  92 */     return messageFactory.getMessage("invalid.query.string", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_QUERY_STRING(Object arg0)
/*     */   {
/* 100 */     return localizer.localize(localizableINVALID_QUERY_STRING(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableDUPLICATE_PORT(Object arg0) {
/* 104 */     return messageFactory.getMessage("duplicate.port", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String DUPLICATE_PORT(Object arg0)
/*     */   {
/* 112 */     return localizer.localize(localizableDUPLICATE_PORT(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_DATASOURCE_DISPATCH_BINDING(Object arg0, Object arg1) {
/* 116 */     return messageFactory.getMessage("invalid.datasource.dispatch.binding", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_DATASOURCE_DISPATCH_BINDING(Object arg0, Object arg1)
/*     */   {
/* 124 */     return localizer.localize(localizableINVALID_DATASOURCE_DISPATCH_BINDING(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_DATASOURCE_DISPATCH_MSGMODE(Object arg0, Object arg1) {
/* 128 */     return messageFactory.getMessage("invalid.datasource.dispatch.msgmode", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_DATASOURCE_DISPATCH_MSGMODE(Object arg0, Object arg1)
/*     */   {
/* 136 */     return localizer.localize(localizableINVALID_DATASOURCE_DISPATCH_MSGMODE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_NULLARG_SOAP_MSGMODE(Object arg0, Object arg1) {
/* 140 */     return messageFactory.getMessage("invalid.nullarg.soap.msgmode", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_NULLARG_SOAP_MSGMODE(Object arg0, Object arg1)
/*     */   {
/* 148 */     return localizer.localize(localizableINVALID_NULLARG_SOAP_MSGMODE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_URI(Object arg0) {
/* 152 */     return messageFactory.getMessage("invalid.uri", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_URI(Object arg0)
/*     */   {
/* 160 */     return localizer.localize(localizableINVALID_URI(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_SOAPMESSAGE_DISPATCH_BINDING(Object arg0, Object arg1) {
/* 164 */     return messageFactory.getMessage("invalid.soapmessage.dispatch.binding", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_SOAPMESSAGE_DISPATCH_BINDING(Object arg0, Object arg1)
/*     */   {
/* 172 */     return localizer.localize(localizableINVALID_SOAPMESSAGE_DISPATCH_BINDING(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_URI_PATH_QUERY(Object arg0, Object arg1) {
/* 176 */     return messageFactory.getMessage("invalid.uri.path.query", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_URI_PATH_QUERY(Object arg0, Object arg1)
/*     */   {
/* 184 */     return localizer.localize(localizableINVALID_URI_PATH_QUERY(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_RESPONSE() {
/* 188 */     return messageFactory.getMessage("invalid.response", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String INVALID_RESPONSE()
/*     */   {
/* 196 */     return localizer.localize(localizableINVALID_RESPONSE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_URI_RESOLUTION(Object arg0) {
/* 200 */     return messageFactory.getMessage("invalid.uri.resolution", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_URI_RESOLUTION(Object arg0)
/*     */   {
/* 208 */     return localizer.localize(localizableINVALID_URI_RESOLUTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_URI_DECODE() {
/* 212 */     return messageFactory.getMessage("invalid.uri.decode", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String INVALID_URI_DECODE()
/*     */   {
/* 220 */     return localizer.localize(localizableINVALID_URI_DECODE());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.DispatchMessages
 * JD-Core Version:    0.6.2
 */