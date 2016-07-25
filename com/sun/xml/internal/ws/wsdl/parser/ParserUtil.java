/*     */ package com.sun.xml.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public class ParserUtil
/*     */ {
/*     */   public static String getAttribute(XMLStreamReader reader, String name)
/*     */   {
/*  50 */     return reader.getAttributeValue(null, name);
/*     */   }
/*     */ 
/*     */   public static String getAttribute(XMLStreamReader reader, String nsUri, String name) {
/*  54 */     return reader.getAttributeValue(nsUri, name);
/*     */   }
/*     */ 
/*     */   public static String getAttribute(XMLStreamReader reader, QName name) {
/*  58 */     return reader.getAttributeValue(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */   public static QName getQName(XMLStreamReader reader, String tag) {
/*  62 */     String localName = XmlUtil.getLocalPart(tag);
/*  63 */     String pfix = XmlUtil.getPrefix(tag);
/*  64 */     String uri = reader.getNamespaceURI(fixNull(pfix));
/*  65 */     return new QName(uri, localName);
/*     */   }
/*     */ 
/*     */   public static String getMandatoryNonEmptyAttribute(XMLStreamReader reader, String name)
/*     */   {
/*  71 */     String value = reader.getAttributeValue(null, name);
/*     */ 
/*  73 */     if (value == null)
/*  74 */       failWithLocalName("client.missing.attribute", reader, name);
/*  75 */     else if (value.equals("")) {
/*  76 */       failWithLocalName("client.invalidAttributeValue", reader, name);
/*     */     }
/*     */ 
/*  79 */     return value;
/*     */   }
/*     */ 
/*     */   public static void failWithFullName(String key, XMLStreamReader reader)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void failWithLocalName(String key, XMLStreamReader reader)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void failWithLocalName(String key, XMLStreamReader reader, String arg)
/*     */   {
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   private static String fixNull(@Nullable String s)
/*     */   {
/* 106 */     if (s == null) return "";
/* 107 */     return s;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.ParserUtil
 * JD-Core Version:    0.6.2
 */