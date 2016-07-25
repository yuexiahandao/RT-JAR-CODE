/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class EncodingMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.encoding");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableFAILED_TO_READ_RESPONSE(Object arg0) {
/*  44 */     return messageFactory.getMessage("failed.to.read.response", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String FAILED_TO_READ_RESPONSE(Object arg0)
/*     */   {
/*  52 */     return localizer.localize(localizableFAILED_TO_READ_RESPONSE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableEXCEPTION_INCORRECT_TYPE(Object arg0) {
/*  56 */     return messageFactory.getMessage("exception.incorrectType", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String EXCEPTION_INCORRECT_TYPE(Object arg0)
/*     */   {
/*  64 */     return localizer.localize(localizableEXCEPTION_INCORRECT_TYPE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableEXCEPTION_NOTFOUND(Object arg0) {
/*  68 */     return messageFactory.getMessage("exception.notfound", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String EXCEPTION_NOTFOUND(Object arg0)
/*     */   {
/*  76 */     return localizer.localize(localizableEXCEPTION_NOTFOUND(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXSD_UNEXPECTED_ELEMENT_NAME(Object arg0, Object arg1) {
/*  80 */     return messageFactory.getMessage("xsd.unexpectedElementName", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String XSD_UNEXPECTED_ELEMENT_NAME(Object arg0, Object arg1)
/*     */   {
/*  88 */     return localizer.localize(localizableXSD_UNEXPECTED_ELEMENT_NAME(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNESTED_DESERIALIZATION_ERROR(Object arg0) {
/*  92 */     return messageFactory.getMessage("nestedDeserializationError", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NESTED_DESERIALIZATION_ERROR(Object arg0)
/*     */   {
/* 100 */     return localizer.localize(localizableNESTED_DESERIALIZATION_ERROR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXSD_UNKNOWN_PREFIX(Object arg0) {
/* 104 */     return messageFactory.getMessage("xsd.unknownPrefix", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XSD_UNKNOWN_PREFIX(Object arg0)
/*     */   {
/* 112 */     return localizer.localize(localizableXSD_UNKNOWN_PREFIX(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNESTED_ENCODING_ERROR(Object arg0) {
/* 116 */     return messageFactory.getMessage("nestedEncodingError", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NESTED_ENCODING_ERROR(Object arg0)
/*     */   {
/* 124 */     return localizer.localize(localizableNESTED_ENCODING_ERROR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableUNKNOWN_OBJECT() {
/* 128 */     return messageFactory.getMessage("unknown.object", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String UNKNOWN_OBJECT()
/*     */   {
/* 136 */     return localizer.localize(localizableUNKNOWN_OBJECT());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableINCORRECT_MESSAGEINFO() {
/* 140 */     return messageFactory.getMessage("incorrect.messageinfo", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String INCORRECT_MESSAGEINFO()
/*     */   {
/* 148 */     return localizer.localize(localizableINCORRECT_MESSAGEINFO());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNESTED_SERIALIZATION_ERROR(Object arg0) {
/* 152 */     return messageFactory.getMessage("nestedSerializationError", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NESTED_SERIALIZATION_ERROR(Object arg0)
/*     */   {
/* 160 */     return localizer.localize(localizableNESTED_SERIALIZATION_ERROR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableNO_SUCH_CONTENT_ID(Object arg0) {
/* 164 */     return messageFactory.getMessage("noSuchContentId", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String NO_SUCH_CONTENT_ID(Object arg0)
/*     */   {
/* 172 */     return localizer.localize(localizableNO_SUCH_CONTENT_ID(arg0));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.EncodingMessages
 * JD-Core Version:    0.6.2
 */