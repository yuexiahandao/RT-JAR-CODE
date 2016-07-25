/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class ClientMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.client");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableFAILED_TO_PARSE(Object arg0, Object arg1) {
/*  44 */     return messageFactory.getMessage("failed.to.parse", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String FAILED_TO_PARSE(Object arg0, Object arg1)
/*     */   {
/*  53 */     return localizer.localize(localizableFAILED_TO_PARSE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_BINDING_ID(Object arg0, Object arg1) {
/*  57 */     return messageFactory.getMessage("invalid.binding.id", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_BINDING_ID(Object arg0, Object arg1)
/*     */   {
/*  65 */     return localizer.localize(localizableINVALID_BINDING_ID(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableEPR_WITHOUT_ADDRESSING_ON() {
/*  69 */     return messageFactory.getMessage("epr.without.addressing.on", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String EPR_WITHOUT_ADDRESSING_ON()
/*     */   {
/*  77 */     return localizer.localize(localizableEPR_WITHOUT_ADDRESSING_ON());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_SERVICE_NO_WSDL(Object arg0) {
/*  81 */     return messageFactory.getMessage("invalid.service.no.wsdl", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_SERVICE_NO_WSDL(Object arg0)
/*     */   {
/*  89 */     return localizer.localize(localizableINVALID_SERVICE_NO_WSDL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_SOAP_ROLE_NONE() {
/*  93 */     return messageFactory.getMessage("invalid.soap.role.none", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String INVALID_SOAP_ROLE_NONE()
/*     */   {
/* 101 */     return localizer.localize(localizableINVALID_SOAP_ROLE_NONE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUNDEFINED_BINDING(Object arg0) {
/* 105 */     return messageFactory.getMessage("undefined.binding", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String UNDEFINED_BINDING(Object arg0)
/*     */   {
/* 113 */     return localizer.localize(localizableUNDEFINED_BINDING(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHTTP_NOT_FOUND(Object arg0) {
/* 117 */     return messageFactory.getMessage("http.not.found", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HTTP_NOT_FOUND(Object arg0)
/*     */   {
/* 125 */     return localizer.localize(localizableHTTP_NOT_FOUND(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHTTP_CLIENT_CANNOT_CONNECT(Object arg0) {
/* 129 */     return messageFactory.getMessage("http.client.cannot.connect", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HTTP_CLIENT_CANNOT_CONNECT(Object arg0)
/*     */   {
/* 137 */     return localizer.localize(localizableHTTP_CLIENT_CANNOT_CONNECT(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_EPR_PORT_NAME(Object arg0, Object arg1) {
/* 141 */     return messageFactory.getMessage("invalid.epr.port.name", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_EPR_PORT_NAME(Object arg0, Object arg1)
/*     */   {
/* 149 */     return localizer.localize(localizableINVALID_EPR_PORT_NAME(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableFAILED_TO_PARSE_WITH_MEX(Object arg0, Object arg1, Object arg2) {
/* 153 */     return messageFactory.getMessage("failed.to.parseWithMEX", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String FAILED_TO_PARSE_WITH_MEX(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 164 */     return localizer.localize(localizableFAILED_TO_PARSE_WITH_MEX(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHTTP_STATUS_CODE(Object arg0, Object arg1) {
/* 168 */     return messageFactory.getMessage("http.status.code", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String HTTP_STATUS_CODE(Object arg0, Object arg1)
/*     */   {
/* 176 */     return localizer.localize(localizableHTTP_STATUS_CODE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_ADDRESS(Object arg0) {
/* 180 */     return messageFactory.getMessage("invalid.address", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_ADDRESS(Object arg0)
/*     */   {
/* 188 */     return localizer.localize(localizableINVALID_ADDRESS(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUNDEFINED_PORT_TYPE(Object arg0) {
/* 192 */     return messageFactory.getMessage("undefined.portType", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String UNDEFINED_PORT_TYPE(Object arg0)
/*     */   {
/* 200 */     return localizer.localize(localizableUNDEFINED_PORT_TYPE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWSDL_CONTAINS_NO_SERVICE(Object arg0) {
/* 204 */     return messageFactory.getMessage("wsdl.contains.no.service", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String WSDL_CONTAINS_NO_SERVICE(Object arg0)
/*     */   {
/* 212 */     return localizer.localize(localizableWSDL_CONTAINS_NO_SERVICE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_SOAP_ACTION() {
/* 216 */     return messageFactory.getMessage("invalid.soap.action", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String INVALID_SOAP_ACTION()
/*     */   {
/* 224 */     return localizer.localize(localizableINVALID_SOAP_ACTION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_LOGICAL_HANDLER_SET(Object arg0) {
/* 228 */     return messageFactory.getMessage("non.logical.handler.set", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NON_LOGICAL_HANDLER_SET(Object arg0)
/*     */   {
/* 236 */     return localizer.localize(localizableNON_LOGICAL_HANDLER_SET(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableLOCAL_CLIENT_FAILED(Object arg0) {
/* 240 */     return messageFactory.getMessage("local.client.failed", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String LOCAL_CLIENT_FAILED(Object arg0)
/*     */   {
/* 248 */     return localizer.localize(localizableLOCAL_CLIENT_FAILED(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_WSDLPARSER_INVALID_WSDL(Object arg0, Object arg1, Object arg2, Object arg3) {
/* 252 */     return messageFactory.getMessage("runtime.wsdlparser.invalidWSDL", new Object[] { arg0, arg1, arg2, arg3 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_WSDLPARSER_INVALID_WSDL(Object arg0, Object arg1, Object arg2, Object arg3)
/*     */   {
/* 260 */     return localizer.localize(localizableRUNTIME_WSDLPARSER_INVALID_WSDL(arg0, arg1, arg2, arg3));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWSDL_NOT_FOUND(Object arg0) {
/* 264 */     return messageFactory.getMessage("wsdl.not.found", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String WSDL_NOT_FOUND(Object arg0)
/*     */   {
/* 272 */     return localizer.localize(localizableWSDL_NOT_FOUND(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHTTP_CLIENT_FAILED(Object arg0) {
/* 276 */     return messageFactory.getMessage("http.client.failed", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HTTP_CLIENT_FAILED(Object arg0)
/*     */   {
/* 284 */     return localizer.localize(localizableHTTP_CLIENT_FAILED(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHTTP_CLIENT_CANNOT_CREATE_MESSAGE_FACTORY() {
/* 288 */     return messageFactory.getMessage("http.client.cannotCreateMessageFactory", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String HTTP_CLIENT_CANNOT_CREATE_MESSAGE_FACTORY()
/*     */   {
/* 296 */     return localizer.localize(localizableHTTP_CLIENT_CANNOT_CREATE_MESSAGE_FACTORY());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_SERVICE_NAME_NULL(Object arg0) {
/* 300 */     return messageFactory.getMessage("invalid.service.name.null", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_SERVICE_NAME_NULL(Object arg0)
/*     */   {
/* 308 */     return localizer.localize(localizableINVALID_SERVICE_NAME_NULL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_WSDL_URL(Object arg0) {
/* 312 */     return messageFactory.getMessage("invalid.wsdl.url", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_WSDL_URL(Object arg0)
/*     */   {
/* 320 */     return localizer.localize(localizableINVALID_WSDL_URL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableHTTP_CLIENT_UNAUTHORIZED(Object arg0) {
/* 324 */     return messageFactory.getMessage("http.client.unauthorized", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String HTTP_CLIENT_UNAUTHORIZED(Object arg0)
/*     */   {
/* 332 */     return localizer.localize(localizableHTTP_CLIENT_UNAUTHORIZED(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_PORT_NAME(Object arg0, Object arg1) {
/* 336 */     return messageFactory.getMessage("invalid.port.name", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_PORT_NAME(Object arg0, Object arg1)
/*     */   {
/* 344 */     return localizer.localize(localizableINVALID_PORT_NAME(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_SERVICE_NAME(Object arg0, Object arg1) {
/* 348 */     return messageFactory.getMessage("invalid.service.name", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_SERVICE_NAME(Object arg0, Object arg1)
/*     */   {
/* 356 */     return localizer.localize(localizableINVALID_SERVICE_NAME(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUNSUPPORTED_OPERATION(Object arg0, Object arg1, Object arg2) {
/* 360 */     return messageFactory.getMessage("unsupported.operation", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String UNSUPPORTED_OPERATION(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 368 */     return localizer.localize(localizableUNSUPPORTED_OPERATION(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableFAILED_TO_PARSE_EPR(Object arg0) {
/* 372 */     return messageFactory.getMessage("failed.to.parse.epr", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String FAILED_TO_PARSE_EPR(Object arg0)
/*     */   {
/* 380 */     return localizer.localize(localizableFAILED_TO_PARSE_EPR(arg0));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.ClientMessages
 * JD-Core Version:    0.6.2
 */