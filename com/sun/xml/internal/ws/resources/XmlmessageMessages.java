/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class XmlmessageMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.xmlmessage");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableXML_NULL_HEADERS() {
/*  44 */     return messageFactory.getMessage("xml.null.headers", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_NULL_HEADERS()
/*     */   {
/*  52 */     return localizer.localize(localizableXML_NULL_HEADERS());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_SET_PAYLOAD_ERR() {
/*  56 */     return messageFactory.getMessage("xml.set.payload.err", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_SET_PAYLOAD_ERR()
/*     */   {
/*  64 */     return localizer.localize(localizableXML_SET_PAYLOAD_ERR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_CONTENT_TYPE_MUSTBE_MULTIPART() {
/*  68 */     return messageFactory.getMessage("xml.content-type.mustbe.multipart", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_CONTENT_TYPE_MUSTBE_MULTIPART()
/*     */   {
/*  76 */     return localizer.localize(localizableXML_CONTENT_TYPE_MUSTBE_MULTIPART());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_UNKNOWN_CONTENT_TYPE() {
/*  80 */     return messageFactory.getMessage("xml.unknown.Content-Type", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_UNKNOWN_CONTENT_TYPE()
/*     */   {
/*  88 */     return localizer.localize(localizableXML_UNKNOWN_CONTENT_TYPE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_GET_DS_ERR() {
/*  92 */     return messageFactory.getMessage("xml.get.ds.err", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_GET_DS_ERR()
/*     */   {
/* 100 */     return localizer.localize(localizableXML_GET_DS_ERR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_CONTENT_TYPE_PARSE_ERR() {
/* 104 */     return messageFactory.getMessage("xml.Content-Type.parse.err", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_CONTENT_TYPE_PARSE_ERR()
/*     */   {
/* 112 */     return localizer.localize(localizableXML_CONTENT_TYPE_PARSE_ERR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_GET_SOURCE_ERR() {
/* 116 */     return messageFactory.getMessage("xml.get.source.err", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_GET_SOURCE_ERR()
/*     */   {
/* 124 */     return localizer.localize(localizableXML_GET_SOURCE_ERR());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_CANNOT_INTERNALIZE_MESSAGE() {
/* 128 */     return messageFactory.getMessage("xml.cannot.internalize.message", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_CANNOT_INTERNALIZE_MESSAGE()
/*     */   {
/* 136 */     return localizer.localize(localizableXML_CANNOT_INTERNALIZE_MESSAGE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_NO_CONTENT_TYPE() {
/* 140 */     return messageFactory.getMessage("xml.no.Content-Type", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XML_NO_CONTENT_TYPE()
/*     */   {
/* 148 */     return localizer.localize(localizableXML_NO_CONTENT_TYPE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_ROOT_PART_INVALID_CONTENT_TYPE(Object arg0) {
/* 152 */     return messageFactory.getMessage("xml.root.part.invalid.Content-Type", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XML_ROOT_PART_INVALID_CONTENT_TYPE(Object arg0)
/*     */   {
/* 160 */     return localizer.localize(localizableXML_ROOT_PART_INVALID_CONTENT_TYPE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXML_INVALID_CONTENT_TYPE(Object arg0) {
/* 164 */     return messageFactory.getMessage("xml.invalid.content-type", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XML_INVALID_CONTENT_TYPE(Object arg0)
/*     */   {
/* 172 */     return localizer.localize(localizableXML_INVALID_CONTENT_TYPE(arg0));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.XmlmessageMessages
 * JD-Core Version:    0.6.2
 */