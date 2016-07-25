/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class ServerMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.server");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL_INCORRECTSERVICE(Object arg0, Object arg1) {
/*  44 */     return messageFactory.getMessage("runtime.parser.wsdl.incorrectservice", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL_INCORRECTSERVICE(Object arg0, Object arg1)
/*     */   {
/*  61 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL_INCORRECTSERVICE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_MISSING_ATTRIBUTE_NO_LINE() {
/*  65 */     return messageFactory.getMessage("runtime.parser.missing.attribute.no.line", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_MISSING_ATTRIBUTE_NO_LINE()
/*     */   {
/*  73 */     return localizer.localize(localizableRUNTIME_PARSER_MISSING_ATTRIBUTE_NO_LINE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTATEFUL_COOKIE_HEADER_INCORRECT(Object arg0, Object arg1) {
/*  77 */     return messageFactory.getMessage("stateful.cookie.header.incorrect", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String STATEFUL_COOKIE_HEADER_INCORRECT(Object arg0, Object arg1)
/*     */   {
/*  85 */     return localizer.localize(localizableSTATEFUL_COOKIE_HEADER_INCORRECT(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNOT_IMPLEMENT_PROVIDER(Object arg0) {
/*  89 */     return messageFactory.getMessage("not.implement.provider", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NOT_IMPLEMENT_PROVIDER(Object arg0)
/*     */   {
/*  97 */     return localizer.localize(localizableNOT_IMPLEMENT_PROVIDER(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTATEFUL_REQURES_ADDRESSING(Object arg0) {
/* 101 */     return messageFactory.getMessage("stateful.requres.addressing", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String STATEFUL_REQURES_ADDRESSING(Object arg0)
/*     */   {
/* 109 */     return localizer.localize(localizableSTATEFUL_REQURES_ADDRESSING(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOAPDECODER_ERR() {
/* 113 */     return messageFactory.getMessage("soapdecoder.err", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String SOAPDECODER_ERR()
/*     */   {
/* 121 */     return localizer.localize(localizableSOAPDECODER_ERR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_INVALID_READER_STATE(Object arg0) {
/* 125 */     return messageFactory.getMessage("runtime.parser.invalidReaderState", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_INVALID_READER_STATE(Object arg0)
/*     */   {
/* 133 */     return localizer.localize(localizableRUNTIME_PARSER_INVALID_READER_STATE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableGENERATE_NON_STANDARD_WSDL() {
/* 137 */     return messageFactory.getMessage("generate.non.standard.wsdl", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String GENERATE_NON_STANDARD_WSDL()
/*     */   {
/* 145 */     return localizer.localize(localizableGENERATE_NON_STANDARD_WSDL());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableDISPATCH_CANNOT_FIND_METHOD(Object arg0) {
/* 149 */     return messageFactory.getMessage("dispatch.cannotFindMethod", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String DISPATCH_CANNOT_FIND_METHOD(Object arg0)
/*     */   {
/* 157 */     return localizer.localize(localizableDISPATCH_CANNOT_FIND_METHOD(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNO_CONTENT_TYPE() {
/* 161 */     return messageFactory.getMessage("no.contentType", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NO_CONTENT_TYPE()
/*     */   {
/* 169 */     return localizer.localize(localizableNO_CONTENT_TYPE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_INVALID_VERSION_NUMBER() {
/* 173 */     return messageFactory.getMessage("runtime.parser.invalidVersionNumber", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_INVALID_VERSION_NUMBER()
/*     */   {
/* 181 */     return localizer.localize(localizableRUNTIME_PARSER_INVALID_VERSION_NUMBER());
/*     */   }
/*     */ 
/*     */   public static Localizable localizablePROVIDER_INVALID_PARAMETER_TYPE(Object arg0, Object arg1) {
/* 185 */     return messageFactory.getMessage("provider.invalid.parameterType", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String PROVIDER_INVALID_PARAMETER_TYPE(Object arg0, Object arg1)
/*     */   {
/* 193 */     return localizer.localize(localizablePROVIDER_INVALID_PARAMETER_TYPE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWRONG_NO_PARAMETERS(Object arg0) {
/* 197 */     return messageFactory.getMessage("wrong.no.parameters", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String WRONG_NO_PARAMETERS(Object arg0)
/*     */   {
/* 205 */     return localizer.localize(localizableWRONG_NO_PARAMETERS(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableANNOTATION_ONLY_ONCE(Object arg0) {
/* 209 */     return messageFactory.getMessage("annotation.only.once", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String ANNOTATION_ONLY_ONCE(Object arg0)
/*     */   {
/* 217 */     return localizer.localize(localizableANNOTATION_ONLY_ONCE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableALREADY_HTTPS_SERVER(Object arg0) {
/* 221 */     return messageFactory.getMessage("already.https.server", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String ALREADY_HTTPS_SERVER(Object arg0)
/*     */   {
/* 229 */     return localizer.localize(localizableALREADY_HTTPS_SERVER(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_XML_READER(Object arg0) {
/* 233 */     return messageFactory.getMessage("runtime.parser.xmlReader", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_XML_READER(Object arg0)
/*     */   {
/* 241 */     return localizer.localize(localizableRUNTIME_PARSER_XML_READER(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL_INCORRECTSERVICEPORT(Object arg0, Object arg1, Object arg2) {
/* 245 */     return messageFactory.getMessage("runtime.parser.wsdl.incorrectserviceport", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL_INCORRECTSERVICEPORT(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 260 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL_INCORRECTSERVICEPORT(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSERVER_RT_ERR(Object arg0) {
/* 264 */     return messageFactory.getMessage("server.rt.err", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String SERVER_RT_ERR(Object arg0)
/*     */   {
/* 272 */     return localizer.localize(localizableSERVER_RT_ERR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_INVALID_ATTRIBUTE_VALUE(Object arg0, Object arg1, Object arg2) {
/* 276 */     return messageFactory.getMessage("runtime.parser.invalidAttributeValue", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_INVALID_ATTRIBUTE_VALUE(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 284 */     return localizer.localize(localizableRUNTIME_PARSER_INVALID_ATTRIBUTE_VALUE(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNO_CURRENT_PACKET() {
/* 288 */     return messageFactory.getMessage("no.current.packet", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NO_CURRENT_PACKET()
/*     */   {
/* 296 */     return localizer.localize(localizableNO_CURRENT_PACKET());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_UNEXPECTED_CONTENT(Object arg0) {
/* 300 */     return messageFactory.getMessage("runtime.parser.unexpectedContent", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_UNEXPECTED_CONTENT(Object arg0)
/*     */   {
/* 308 */     return localizer.localize(localizableRUNTIME_PARSER_UNEXPECTED_CONTENT(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTATEFUL_COOKIE_HEADER_REQUIRED(Object arg0) {
/* 312 */     return messageFactory.getMessage("stateful.cookie.header.required", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String STATEFUL_COOKIE_HEADER_REQUIRED(Object arg0)
/*     */   {
/* 320 */     return localizer.localize(localizableSTATEFUL_COOKIE_HEADER_REQUIRED(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_IMPLEMENTOR() {
/* 324 */     return messageFactory.getMessage("null.implementor", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_IMPLEMENTOR()
/*     */   {
/* 332 */     return localizer.localize(localizableNULL_IMPLEMENTOR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL(Object arg0) {
/* 336 */     return messageFactory.getMessage("runtime.parser.wsdl", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL(Object arg0)
/*     */   {
/* 344 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOAPENCODER_ERR() {
/* 348 */     return messageFactory.getMessage("soapencoder.err", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String SOAPENCODER_ERR()
/*     */   {
/* 356 */     return localizer.localize(localizableSOAPENCODER_ERR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWSDL_REQUIRED() {
/* 360 */     return messageFactory.getMessage("wsdl.required", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String WSDL_REQUIRED()
/*     */   {
/* 368 */     return localizer.localize(localizableWSDL_REQUIRED());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL_NOSERVICE_IN_WSDLMODEL(Object arg0) {
/* 372 */     return messageFactory.getMessage("runtime.parser.wsdl.noservice.in.wsdlmodel", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL_NOSERVICE_IN_WSDLMODEL(Object arg0)
/*     */   {
/* 380 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL_NOSERVICE_IN_WSDLMODEL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizablePORT_NAME_REQUIRED() {
/* 384 */     return messageFactory.getMessage("port.name.required", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String PORT_NAME_REQUIRED()
/*     */   {
/* 392 */     return localizer.localize(localizablePORT_NAME_REQUIRED());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWRONG_TNS_FOR_PORT(Object arg0) {
/* 396 */     return messageFactory.getMessage("wrong.tns.for.port", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String WRONG_TNS_FOR_PORT(Object arg0)
/*     */   {
/* 404 */     return localizer.localize(localizableWRONG_TNS_FOR_PORT(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL_MULTIPLEBINDING(Object arg0, Object arg1, Object arg2) {
/* 408 */     return messageFactory.getMessage("runtime.parser.wsdl.multiplebinding", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL_MULTIPLEBINDING(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 416 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL_MULTIPLEBINDING(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNOT_KNOW_HTTP_CONTEXT_TYPE(Object arg0, Object arg1, Object arg2) {
/* 420 */     return messageFactory.getMessage("not.know.HttpContext.type", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String NOT_KNOW_HTTP_CONTEXT_TYPE(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 428 */     return localizer.localize(localizableNOT_KNOW_HTTP_CONTEXT_TYPE(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNON_UNIQUE_DISPATCH_QNAME(Object arg0, Object arg1) {
/* 432 */     return messageFactory.getMessage("non.unique.dispatch.qname", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String NON_UNIQUE_DISPATCH_QNAME(Object arg0, Object arg1)
/*     */   {
/* 440 */     return localizer.localize(localizableNON_UNIQUE_DISPATCH_QNAME(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableALREADY_HTTP_SERVER(Object arg0) {
/* 444 */     return messageFactory.getMessage("already.http.server", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String ALREADY_HTTP_SERVER(Object arg0)
/*     */   {
/* 452 */     return localizer.localize(localizableALREADY_HTTP_SERVER(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableCAN_NOT_GENERATE_WSDL(Object arg0) {
/* 456 */     return messageFactory.getMessage("can.not.generate.wsdl", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String CAN_NOT_GENERATE_WSDL(Object arg0)
/*     */   {
/* 464 */     return localizer.localize(localizableCAN_NOT_GENERATE_WSDL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_INVALID_ATTRIBUTE_VALUE(Object arg0, Object arg1) {
/* 468 */     return messageFactory.getMessage("runtime.parser.invalid.attribute.value", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_INVALID_ATTRIBUTE_VALUE(Object arg0, Object arg1)
/*     */   {
/* 476 */     return localizer.localize(localizableRUNTIME_PARSER_INVALID_ATTRIBUTE_VALUE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WRONG_ELEMENT(Object arg0, Object arg1, Object arg2) {
/* 480 */     return messageFactory.getMessage("runtime.parser.wrong.element", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WRONG_ELEMENT(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 488 */     return localizer.localize(localizableRUNTIME_PARSER_WRONG_ELEMENT(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIMEMODELER_INVALIDANNOTATION_ON_IMPL(Object arg0, Object arg1, Object arg2) {
/* 492 */     return messageFactory.getMessage("runtimemodeler.invalidannotationOnImpl", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIMEMODELER_INVALIDANNOTATION_ON_IMPL(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 500 */     return localizer.localize(localizableRUNTIMEMODELER_INVALIDANNOTATION_ON_IMPL(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL_NOSERVICE() {
/* 504 */     return messageFactory.getMessage("runtime.parser.wsdl.noservice", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL_NOSERVICE()
/*     */   {
/* 512 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL_NOSERVICE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSERVICE_NAME_REQUIRED() {
/* 516 */     return messageFactory.getMessage("service.name.required", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String SERVICE_NAME_REQUIRED()
/*     */   {
/* 524 */     return localizer.localize(localizableSERVICE_NAME_REQUIRED());
/*     */   }
/*     */ 
/*     */   public static Localizable localizablePROVIDER_NOT_PARAMETERIZED(Object arg0) {
/* 528 */     return messageFactory.getMessage("provider.not.parameterized", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String PROVIDER_NOT_PARAMETERIZED(Object arg0)
/*     */   {
/* 536 */     return localizer.localize(localizablePROVIDER_NOT_PARAMETERIZED(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_WSDL_PATCHER() {
/* 540 */     return messageFactory.getMessage("runtime.wsdl.patcher", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_WSDL_PATCHER()
/*     */   {
/* 548 */     return localizer.localize(localizableRUNTIME_WSDL_PATCHER());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_SAXPARSER_EXCEPTION(Object arg0, Object arg1) {
/* 552 */     return messageFactory.getMessage("runtime.saxparser.exception", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_SAXPARSER_EXCEPTION(Object arg0, Object arg1)
/*     */   {
/* 561 */     return localizer.localize(localizableRUNTIME_SAXPARSER_EXCEPTION(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL_NOT_FOUND(Object arg0) {
/* 565 */     return messageFactory.getMessage("runtime.parser.wsdl.not.found", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL_NOT_FOUND(Object arg0)
/*     */   {
/* 573 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL_NOT_FOUND(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWRONG_PARAMETER_TYPE(Object arg0) {
/* 577 */     return messageFactory.getMessage("wrong.parameter.type", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String WRONG_PARAMETER_TYPE(Object arg0)
/*     */   {
/* 585 */     return localizer.localize(localizableWRONG_PARAMETER_TYPE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_CLASS_NOT_FOUND(Object arg0) {
/* 589 */     return messageFactory.getMessage("runtime.parser.classNotFound", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_CLASS_NOT_FOUND(Object arg0)
/*     */   {
/* 597 */     return localizer.localize(localizableRUNTIME_PARSER_CLASS_NOT_FOUND(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUNSUPPORTED_CHARSET(Object arg0) {
/* 601 */     return messageFactory.getMessage("unsupported.charset", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String UNSUPPORTED_CHARSET(Object arg0)
/*     */   {
/* 609 */     return localizer.localize(localizableUNSUPPORTED_CHARSET(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTATIC_RESOURCE_INJECTION_ONLY(Object arg0, Object arg1) {
/* 613 */     return messageFactory.getMessage("static.resource.injection.only", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String STATIC_RESOURCE_INJECTION_ONLY(Object arg0, Object arg1)
/*     */   {
/* 621 */     return localizer.localize(localizableSTATIC_RESOURCE_INJECTION_ONLY(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNOT_ZERO_PARAMETERS(Object arg0) {
/* 625 */     return messageFactory.getMessage("not.zero.parameters", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NOT_ZERO_PARAMETERS(Object arg0)
/*     */   {
/* 633 */     return localizer.localize(localizableNOT_ZERO_PARAMETERS(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableDUPLICATE_PRIMARY_WSDL(Object arg0) {
/* 637 */     return messageFactory.getMessage("duplicate.primary.wsdl", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String DUPLICATE_PRIMARY_WSDL(Object arg0)
/*     */   {
/* 645 */     return localizer.localize(localizableDUPLICATE_PRIMARY_WSDL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableDUPLICATE_ABSTRACT_WSDL(Object arg0) {
/* 649 */     return messageFactory.getMessage("duplicate.abstract.wsdl", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String DUPLICATE_ABSTRACT_WSDL(Object arg0)
/*     */   {
/* 657 */     return localizer.localize(localizableDUPLICATE_ABSTRACT_WSDL(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTATEFUL_INVALID_WEBSERVICE_CONTEXT(Object arg0) {
/* 661 */     return messageFactory.getMessage("stateful.invalid.webservice.context", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String STATEFUL_INVALID_WEBSERVICE_CONTEXT(Object arg0)
/*     */   {
/* 669 */     return localizer.localize(localizableSTATEFUL_INVALID_WEBSERVICE_CONTEXT(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_WSDL_NOBINDING() {
/* 673 */     return messageFactory.getMessage("runtime.parser.wsdl.nobinding", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_WSDL_NOBINDING()
/*     */   {
/* 681 */     return localizer.localize(localizableRUNTIME_PARSER_WSDL_NOBINDING());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_INVALID_ELEMENT(Object arg0, Object arg1) {
/* 685 */     return messageFactory.getMessage("runtime.parser.invalidElement", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_INVALID_ELEMENT(Object arg0, Object arg1)
/*     */   {
/* 693 */     return localizer.localize(localizableRUNTIME_PARSER_INVALID_ELEMENT(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableRUNTIME_PARSER_MISSING_ATTRIBUTE(Object arg0, Object arg1, Object arg2) {
/* 697 */     return messageFactory.getMessage("runtime.parser.missing.attribute", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String RUNTIME_PARSER_MISSING_ATTRIBUTE(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 705 */     return localizer.localize(localizableRUNTIME_PARSER_MISSING_ATTRIBUTE(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableWRONG_FIELD_TYPE(Object arg0) {
/* 709 */     return messageFactory.getMessage("wrong.field.type", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String WRONG_FIELD_TYPE(Object arg0)
/*     */   {
/* 717 */     return localizer.localize(localizableWRONG_FIELD_TYPE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableDUPLICATE_PORT_KNOWN_HEADER(Object arg0) {
/* 721 */     return messageFactory.getMessage("duplicate.portKnownHeader", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String DUPLICATE_PORT_KNOWN_HEADER(Object arg0)
/*     */   {
/* 729 */     return localizer.localize(localizableDUPLICATE_PORT_KNOWN_HEADER(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUNSUPPORTED_CONTENT_TYPE(Object arg0, Object arg1) {
/* 733 */     return messageFactory.getMessage("unsupported.contentType", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String UNSUPPORTED_CONTENT_TYPE(Object arg0, Object arg1)
/*     */   {
/* 741 */     return localizer.localize(localizableUNSUPPORTED_CONTENT_TYPE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableFAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(Object arg0, Object arg1, Object arg2) {
/* 745 */     return messageFactory.getMessage("failed.to.instantiate.instanceResolver", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 753 */     return localizer.localize(localizableFAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableDD_MTOM_CONFLICT(Object arg0, Object arg1) {
/* 757 */     return messageFactory.getMessage("dd.mtom.conflict", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String DD_MTOM_CONFLICT(Object arg0, Object arg1)
/*     */   {
/* 765 */     return localizer.localize(localizableDD_MTOM_CONFLICT(arg0, arg1));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.ServerMessages
 * JD-Core Version:    0.6.2
 */