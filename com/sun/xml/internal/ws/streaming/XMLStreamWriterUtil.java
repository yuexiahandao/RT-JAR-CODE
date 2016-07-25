/*     */ package com.sun.xml.internal.ws.streaming;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class XMLStreamWriterUtil
/*     */ {
/*     */   @Nullable
/*     */   public static OutputStream getOutputStream(XMLStreamWriter writer)
/*     */     throws XMLStreamException
/*     */   {
/*  56 */     Object obj = null;
/*     */ 
/*  59 */     if ((writer instanceof Map)) {
/*  60 */       obj = ((Map)writer).get("sjsxp-outputstream");
/*     */     }
/*     */ 
/*  64 */     if (obj == null) {
/*     */       try {
/*  66 */         obj = writer.getProperty("com.ctc.wstx.outputUnderlyingStream");
/*     */       }
/*     */       catch (Exception ie)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  74 */     if (obj == null) {
/*     */       try {
/*  76 */         obj = writer.getProperty("http://java.sun.com/xml/stream/properties/outputstream");
/*     */       }
/*     */       catch (Exception ie)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  84 */     if (obj != null) {
/*  85 */       writer.writeCharacters("");
/*  86 */       writer.flush();
/*  87 */       return (OutputStream)obj;
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public static String encodeQName(XMLStreamWriter writer, QName qname, PrefixFactory prefixFactory)
/*     */   {
/*     */     try
/*     */     {
/* 101 */       String namespaceURI = qname.getNamespaceURI();
/* 102 */       String localPart = qname.getLocalPart();
/*     */ 
/* 104 */       if ((namespaceURI == null) || (namespaceURI.equals(""))) {
/* 105 */         return localPart;
/*     */       }
/*     */ 
/* 108 */       String prefix = writer.getPrefix(namespaceURI);
/* 109 */       if (prefix == null) {
/* 110 */         prefix = prefixFactory.getPrefix(namespaceURI);
/* 111 */         writer.writeNamespace(prefix, namespaceURI);
/*     */       }
/* 113 */       return prefix + ":" + localPart;
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 117 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil
 * JD-Core Version:    0.6.2
 */