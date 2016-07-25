/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class SoapMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.soap");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableSOAP_FAULT_CREATE_ERR(Object arg0) {
/*  44 */     return messageFactory.getMessage("soap.fault.create.err", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String SOAP_FAULT_CREATE_ERR(Object arg0)
/*     */   {
/*  52 */     return localizer.localize(localizableSOAP_FAULT_CREATE_ERR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOAP_MSG_FACTORY_CREATE_ERR(Object arg0) {
/*  56 */     return messageFactory.getMessage("soap.msg.factory.create.err", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String SOAP_MSG_FACTORY_CREATE_ERR(Object arg0)
/*     */   {
/*  64 */     return localizer.localize(localizableSOAP_MSG_FACTORY_CREATE_ERR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOAP_MSG_CREATE_ERR(Object arg0) {
/*  68 */     return messageFactory.getMessage("soap.msg.create.err", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String SOAP_MSG_CREATE_ERR(Object arg0)
/*     */   {
/*  76 */     return localizer.localize(localizableSOAP_MSG_CREATE_ERR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOAP_FACTORY_CREATE_ERR(Object arg0) {
/*  80 */     return messageFactory.getMessage("soap.factory.create.err", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String SOAP_FACTORY_CREATE_ERR(Object arg0)
/*     */   {
/*  88 */     return localizer.localize(localizableSOAP_FACTORY_CREATE_ERR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOAP_PROTOCOL_INVALID_FAULT_CODE(Object arg0) {
/*  92 */     return messageFactory.getMessage("soap.protocol.invalidFaultCode", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String SOAP_PROTOCOL_INVALID_FAULT_CODE(Object arg0)
/*     */   {
/* 100 */     return localizer.localize(localizableSOAP_PROTOCOL_INVALID_FAULT_CODE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOAP_VERSION_MISMATCH_ERR(Object arg0, Object arg1) {
/* 104 */     return messageFactory.getMessage("soap.version.mismatch.err", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String SOAP_VERSION_MISMATCH_ERR(Object arg0, Object arg1)
/*     */   {
/* 112 */     return localizer.localize(localizableSOAP_VERSION_MISMATCH_ERR(arg0, arg1));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.SoapMessages
 * JD-Core Version:    0.6.2
 */