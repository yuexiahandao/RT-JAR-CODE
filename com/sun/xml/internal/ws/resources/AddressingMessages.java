/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class AddressingMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.addressing");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableNON_ANONYMOUS_RESPONSE_ONEWAY() {
/*  44 */     return messageFactory.getMessage("nonAnonymous.response.oneway", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NON_ANONYMOUS_RESPONSE_ONEWAY()
/*     */   {
/*  52 */     return localizer.localize(localizableNON_ANONYMOUS_RESPONSE_ONEWAY());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_WSA_HEADERS() {
/*  56 */     return messageFactory.getMessage("null.wsa.headers", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_WSA_HEADERS()
/*     */   {
/*  64 */     return localizer.localize(localizableNULL_WSA_HEADERS());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUNKNOWN_WSA_HEADER() {
/*  68 */     return messageFactory.getMessage("unknown.wsa.header", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String UNKNOWN_WSA_HEADER()
/*     */   {
/*  76 */     return localizer.localize(localizableUNKNOWN_WSA_HEADER());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_ACTION() {
/*  80 */     return messageFactory.getMessage("null.action", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_ACTION()
/*     */   {
/*  88 */     return localizer.localize(localizableNULL_ACTION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_WSAW_ANONYMOUS(Object arg0) {
/*  92 */     return messageFactory.getMessage("invalid.wsaw.anonymous", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_WSAW_ANONYMOUS(Object arg0)
/*     */   {
/* 100 */     return localizer.localize(localizableINVALID_WSAW_ANONYMOUS(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_SOAP_VERSION() {
/* 104 */     return messageFactory.getMessage("null.soap.version", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_SOAP_VERSION()
/*     */   {
/* 112 */     return localizer.localize(localizableNULL_SOAP_VERSION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWSDL_BOUND_OPERATION_NOT_FOUND(Object arg0) {
/* 116 */     return messageFactory.getMessage("wsdlBoundOperation.notFound", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String WSDL_BOUND_OPERATION_NOT_FOUND(Object arg0)
/*     */   {
/* 124 */     return localizer.localize(localizableWSDL_BOUND_OPERATION_NOT_FOUND(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_UNIQUE_OPERATION_SIGNATURE(Object arg0, Object arg1, Object arg2, Object arg3) {
/* 128 */     return messageFactory.getMessage("non.unique.operation.signature", new Object[] { arg0, arg1, arg2, arg3 });
/*     */   }
/*     */ 
/*     */   public static String NON_UNIQUE_OPERATION_SIGNATURE(Object arg0, Object arg1, Object arg2, Object arg3)
/*     */   {
/* 136 */     return localizer.localize(localizableNON_UNIQUE_OPERATION_SIGNATURE(arg0, arg1, arg2, arg3));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_ANONYMOUS_RESPONSE() {
/* 140 */     return messageFactory.getMessage("nonAnonymous.response", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NON_ANONYMOUS_RESPONSE()
/*     */   {
/* 148 */     return localizer.localize(localizableNON_ANONYMOUS_RESPONSE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableVALIDATION_SERVER_NULL_ACTION() {
/* 152 */     return messageFactory.getMessage("validation.server.nullAction", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String VALIDATION_SERVER_NULL_ACTION()
/*     */   {
/* 160 */     return localizer.localize(localizableVALIDATION_SERVER_NULL_ACTION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableFAULT_TO_CANNOT_PARSE() {
/* 164 */     return messageFactory.getMessage("faultTo.cannot.parse", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String FAULT_TO_CANNOT_PARSE()
/*     */   {
/* 172 */     return localizer.localize(localizableFAULT_TO_CANNOT_PARSE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableVALIDATION_CLIENT_NULL_ACTION() {
/* 176 */     return messageFactory.getMessage("validation.client.nullAction", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String VALIDATION_CLIENT_NULL_ACTION()
/*     */   {
/* 184 */     return localizer.localize(localizableVALIDATION_CLIENT_NULL_ACTION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_MESSAGE() {
/* 188 */     return messageFactory.getMessage("null.message", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_MESSAGE()
/*     */   {
/* 196 */     return localizer.localize(localizableNULL_MESSAGE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableACTION_NOT_SUPPORTED_EXCEPTION(Object arg0) {
/* 200 */     return messageFactory.getMessage("action.not.supported.exception", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String ACTION_NOT_SUPPORTED_EXCEPTION(Object arg0)
/*     */   {
/* 208 */     return localizer.localize(localizableACTION_NOT_SUPPORTED_EXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_ANONYMOUS_RESPONSE_NULL_HEADERS(Object arg0) {
/* 212 */     return messageFactory.getMessage("nonAnonymous.response.nullHeaders", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NON_ANONYMOUS_RESPONSE_NULL_HEADERS(Object arg0)
/*     */   {
/* 220 */     return localizer.localize(localizableNON_ANONYMOUS_RESPONSE_NULL_HEADERS(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_ANONYMOUS_RESPONSE_SENDING(Object arg0) {
/* 224 */     return messageFactory.getMessage("nonAnonymous.response.sending", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NON_ANONYMOUS_RESPONSE_SENDING(Object arg0)
/*     */   {
/* 232 */     return localizer.localize(localizableNON_ANONYMOUS_RESPONSE_SENDING(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableREPLY_TO_CANNOT_PARSE() {
/* 236 */     return messageFactory.getMessage("replyTo.cannot.parse", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String REPLY_TO_CANNOT_PARSE()
/*     */   {
/* 244 */     return localizer.localize(localizableREPLY_TO_CANNOT_PARSE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINVALID_ADDRESSING_HEADER_EXCEPTION(Object arg0, Object arg1) {
/* 248 */     return messageFactory.getMessage("invalid.addressing.header.exception", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String INVALID_ADDRESSING_HEADER_EXCEPTION(Object arg0, Object arg1)
/*     */   {
/* 256 */     return localizer.localize(localizableINVALID_ADDRESSING_HEADER_EXCEPTION(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWSAW_ANONYMOUS_PROHIBITED() {
/* 260 */     return messageFactory.getMessage("wsaw.anonymousProhibited", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String WSAW_ANONYMOUS_PROHIBITED()
/*     */   {
/* 268 */     return localizer.localize(localizableWSAW_ANONYMOUS_PROHIBITED());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_WSDL_PORT() {
/* 272 */     return messageFactory.getMessage("null.wsdlPort", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_WSDL_PORT()
/*     */   {
/* 280 */     return localizer.localize(localizableNULL_WSDL_PORT());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableADDRESSING_SHOULD_BE_ENABLED() {
/* 284 */     return messageFactory.getMessage("addressing.should.be.enabled.", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String ADDRESSING_SHOULD_BE_ENABLED()
/*     */   {
/* 292 */     return localizer.localize(localizableADDRESSING_SHOULD_BE_ENABLED());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_ADDRESSING_VERSION() {
/* 296 */     return messageFactory.getMessage("null.addressing.version", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_ADDRESSING_VERSION()
/*     */   {
/* 304 */     return localizer.localize(localizableNULL_ADDRESSING_VERSION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableMISSING_HEADER_EXCEPTION(Object arg0) {
/* 308 */     return messageFactory.getMessage("missing.header.exception", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String MISSING_HEADER_EXCEPTION(Object arg0)
/*     */   {
/* 316 */     return localizer.localize(localizableMISSING_HEADER_EXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_PACKET() {
/* 320 */     return messageFactory.getMessage("null.packet", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_PACKET()
/*     */   {
/* 328 */     return localizer.localize(localizableNULL_PACKET());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWRONG_ADDRESSING_VERSION(Object arg0, Object arg1) {
/* 332 */     return messageFactory.getMessage("wrong.addressing.version", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String WRONG_ADDRESSING_VERSION(Object arg0, Object arg1)
/*     */   {
/* 340 */     return localizer.localize(localizableWRONG_ADDRESSING_VERSION(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableADDRESSING_NOT_ENABLED(Object arg0) {
/* 344 */     return messageFactory.getMessage("addressing.notEnabled", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String ADDRESSING_NOT_ENABLED(Object arg0)
/*     */   {
/* 352 */     return localizer.localize(localizableADDRESSING_NOT_ENABLED(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_ANONYMOUS_UNKNOWN_PROTOCOL(Object arg0) {
/* 356 */     return messageFactory.getMessage("nonAnonymous.unknown.protocol", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NON_ANONYMOUS_UNKNOWN_PROTOCOL(Object arg0)
/*     */   {
/* 364 */     return localizer.localize(localizableNON_ANONYMOUS_UNKNOWN_PROTOCOL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_ANONYMOUS_RESPONSE_NULL_MESSAGE(Object arg0) {
/* 368 */     return messageFactory.getMessage("nonAnonymous.response.nullMessage", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NON_ANONYMOUS_RESPONSE_NULL_MESSAGE(Object arg0)
/*     */   {
/* 376 */     return localizer.localize(localizableNON_ANONYMOUS_RESPONSE_NULL_MESSAGE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_HEADERS() {
/* 380 */     return messageFactory.getMessage("null.headers", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_HEADERS()
/*     */   {
/* 388 */     return localizer.localize(localizableNULL_HEADERS());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_BINDING() {
/* 392 */     return messageFactory.getMessage("null.binding", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_BINDING()
/*     */   {
/* 400 */     return localizer.localize(localizableNULL_BINDING());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.AddressingMessages
 * JD-Core Version:    0.6.2
 */