/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class ProviderApiMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.providerApi");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableNULL_ADDRESS_SERVICE_ENDPOINT() {
/*  44 */     return messageFactory.getMessage("null.address.service.endpoint", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_ADDRESS_SERVICE_ENDPOINT()
/*     */   {
/*  52 */     return localizer.localize(localizableNULL_ADDRESS_SERVICE_ENDPOINT());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNO_WSDL_NO_PORT(Object arg0) {
/*  56 */     return messageFactory.getMessage("no.wsdl.no.port", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NO_WSDL_NO_PORT(Object arg0)
/*     */   {
/*  64 */     return localizer.localize(localizableNO_WSDL_NO_PORT(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_SERVICE() {
/*  68 */     return messageFactory.getMessage("null.service", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_SERVICE()
/*     */   {
/*  76 */     return localizer.localize(localizableNULL_SERVICE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_ADDRESS() {
/*  80 */     return messageFactory.getMessage("null.address", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_ADDRESS()
/*     */   {
/*  88 */     return localizer.localize(localizableNULL_ADDRESS());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_PORTNAME() {
/*  92 */     return messageFactory.getMessage("null.portname", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_PORTNAME()
/*     */   {
/* 100 */     return localizer.localize(localizableNULL_PORTNAME());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNOTFOUND_SERVICE_IN_WSDL(Object arg0, Object arg1) {
/* 104 */     return messageFactory.getMessage("notfound.service.in.wsdl", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String NOTFOUND_SERVICE_IN_WSDL(Object arg0, Object arg1)
/*     */   {
/* 112 */     return localizer.localize(localizableNOTFOUND_SERVICE_IN_WSDL(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_EPR() {
/* 116 */     return messageFactory.getMessage("null.epr", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_EPR()
/*     */   {
/* 124 */     return localizer.localize(localizableNULL_EPR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNULL_WSDL() {
/* 128 */     return messageFactory.getMessage("null.wsdl", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String NULL_WSDL()
/*     */   {
/* 136 */     return localizer.localize(localizableNULL_WSDL());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNOTFOUND_PORT_IN_WSDL(Object arg0, Object arg1, Object arg2) {
/* 140 */     return messageFactory.getMessage("notfound.port.in.wsdl", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String NOTFOUND_PORT_IN_WSDL(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 148 */     return localizer.localize(localizableNOTFOUND_PORT_IN_WSDL(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableERROR_WSDL(Object arg0) {
/* 152 */     return messageFactory.getMessage("error.wsdl", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String ERROR_WSDL(Object arg0)
/*     */   {
/* 160 */     return localizer.localize(localizableERROR_WSDL(arg0));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.ProviderApiMessages
 * JD-Core Version:    0.6.2
 */