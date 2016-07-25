/*     */ package com.sun.xml.internal.ws.resources;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ 
/*     */ public final class StreamingMessages
/*     */ {
/*  40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.streaming");
/*  41 */   private static final Localizer localizer = new Localizer();
/*     */ 
/*     */   public static Localizable localizableFASTINFOSET_DECODING_NOT_ACCEPTED() {
/*  44 */     return messageFactory.getMessage("fastinfoset.decodingNotAccepted", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String FASTINFOSET_DECODING_NOT_ACCEPTED()
/*     */   {
/*  52 */     return localizer.localize(localizableFASTINFOSET_DECODING_NOT_ACCEPTED());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTAX_CANT_CREATE() {
/*  56 */     return messageFactory.getMessage("stax.cantCreate", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String STAX_CANT_CREATE()
/*     */   {
/*  64 */     return localizer.localize(localizableSTAX_CANT_CREATE());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTREAMING_IO_EXCEPTION(Object arg0) {
/*  68 */     return messageFactory.getMessage("streaming.ioException", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String STREAMING_IO_EXCEPTION(Object arg0)
/*     */   {
/*  76 */     return localizer.localize(localizableSTREAMING_IO_EXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSOURCEREADER_INVALID_SOURCE(Object arg0) {
/*  80 */     return messageFactory.getMessage("sourcereader.invalidSource", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String SOURCEREADER_INVALID_SOURCE(Object arg0)
/*     */   {
/*  88 */     return localizer.localize(localizableSOURCEREADER_INVALID_SOURCE(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_UNEXPECTED_STATE(Object arg0, Object arg1) {
/*  92 */     return messageFactory.getMessage("xmlreader.unexpectedState", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_UNEXPECTED_STATE(Object arg0, Object arg1)
/*     */   {
/* 100 */     return localizer.localize(localizableXMLREADER_UNEXPECTED_STATE(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_IO_EXCEPTION(Object arg0) {
/* 104 */     return messageFactory.getMessage("xmlreader.ioException", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_IO_EXCEPTION(Object arg0)
/*     */   {
/* 112 */     return localizer.localize(localizableXMLREADER_IO_EXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableFASTINFOSET_NO_IMPLEMENTATION() {
/* 116 */     return messageFactory.getMessage("fastinfoset.noImplementation", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String FASTINFOSET_NO_IMPLEMENTATION()
/*     */   {
/* 124 */     return localizer.localize(localizableFASTINFOSET_NO_IMPLEMENTATION());
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLWRITER_IO_EXCEPTION(Object arg0) {
/* 128 */     return messageFactory.getMessage("xmlwriter.ioException", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLWRITER_IO_EXCEPTION(Object arg0)
/*     */   {
/* 136 */     return localizer.localize(localizableXMLWRITER_IO_EXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_UNEXPECTED_CHARACTER_CONTENT(Object arg0) {
/* 140 */     return messageFactory.getMessage("xmlreader.unexpectedCharacterContent", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_UNEXPECTED_CHARACTER_CONTENT(Object arg0)
/*     */   {
/* 148 */     return localizer.localize(localizableXMLREADER_UNEXPECTED_CHARACTER_CONTENT(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTREAMING_PARSE_EXCEPTION(Object arg0) {
/* 152 */     return messageFactory.getMessage("streaming.parseException", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String STREAMING_PARSE_EXCEPTION(Object arg0)
/*     */   {
/* 160 */     return localizer.localize(localizableSTREAMING_PARSE_EXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLWRITER_NO_PREFIX_FOR_URI(Object arg0) {
/* 164 */     return messageFactory.getMessage("xmlwriter.noPrefixForURI", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLWRITER_NO_PREFIX_FOR_URI(Object arg0)
/*     */   {
/* 172 */     return localizer.localize(localizableXMLWRITER_NO_PREFIX_FOR_URI(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_NESTED_ERROR(Object arg0) {
/* 176 */     return messageFactory.getMessage("xmlreader.nestedError", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_NESTED_ERROR(Object arg0)
/*     */   {
/* 184 */     return localizer.localize(localizableXMLREADER_NESTED_ERROR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableSTAXREADER_XMLSTREAMEXCEPTION(Object arg0) {
/* 188 */     return messageFactory.getMessage("staxreader.xmlstreamexception", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String STAXREADER_XMLSTREAMEXCEPTION(Object arg0)
/*     */   {
/* 196 */     return localizer.localize(localizableSTAXREADER_XMLSTREAMEXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLWRITER_NESTED_ERROR(Object arg0) {
/* 200 */     return messageFactory.getMessage("xmlwriter.nestedError", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLWRITER_NESTED_ERROR(Object arg0)
/*     */   {
/* 208 */     return localizer.localize(localizableXMLWRITER_NESTED_ERROR(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_ILLEGAL_STATE_ENCOUNTERED(Object arg0) {
/* 212 */     return messageFactory.getMessage("xmlreader.illegalStateEncountered", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_ILLEGAL_STATE_ENCOUNTERED(Object arg0)
/*     */   {
/* 220 */     return localizer.localize(localizableXMLREADER_ILLEGAL_STATE_ENCOUNTERED(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_UNEXPECTED_STATE_TAG(Object arg0, Object arg1) {
/* 224 */     return messageFactory.getMessage("xmlreader.unexpectedState.tag", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_UNEXPECTED_STATE_TAG(Object arg0, Object arg1)
/*     */   {
/* 232 */     return localizer.localize(localizableXMLREADER_UNEXPECTED_STATE_TAG(arg0, arg1));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_UNEXPECTED_STATE_MESSAGE(Object arg0, Object arg1, Object arg2) {
/* 236 */     return messageFactory.getMessage("xmlreader.unexpectedState.message", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_UNEXPECTED_STATE_MESSAGE(Object arg0, Object arg1, Object arg2)
/*     */   {
/* 244 */     return localizer.localize(localizableXMLREADER_UNEXPECTED_STATE_MESSAGE(arg0, arg1, arg2));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLREADER_PARSE_EXCEPTION(Object arg0) {
/* 248 */     return messageFactory.getMessage("xmlreader.parseException", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */   public static String XMLREADER_PARSE_EXCEPTION(Object arg0)
/*     */   {
/* 256 */     return localizer.localize(localizableXMLREADER_PARSE_EXCEPTION(arg0));
/*     */   }
/*     */ 
/*     */   public static Localizable localizableXMLRECORDER_RECORDING_ENDED() {
/* 260 */     return messageFactory.getMessage("xmlrecorder.recording.ended", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static String XMLRECORDER_RECORDING_ENDED()
/*     */   {
/* 268 */     return localizer.localize(localizableXMLRECORDER_RECORDING_ENDED());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.StreamingMessages
 * JD-Core Version:    0.6.2
 */