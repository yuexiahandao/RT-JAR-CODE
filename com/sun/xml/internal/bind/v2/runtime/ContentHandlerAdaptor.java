/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.istack.internal.SAXException2;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ final class ContentHandlerAdaptor extends DefaultHandler
/*     */ {
/*  48 */   private final FinalArrayList<String> prefixMap = new FinalArrayList();
/*     */   private final XMLSerializer serializer;
/*  53 */   private final StringBuffer text = new StringBuffer();
/*     */ 
/*     */   ContentHandlerAdaptor(XMLSerializer _serializer)
/*     */   {
/*  57 */     this.serializer = _serializer;
/*     */   }
/*     */ 
/*     */   public void startDocument() {
/*  61 */     this.prefixMap.clear();
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) {
/*  65 */     this.prefixMap.add(prefix);
/*  66 */     this.prefixMap.add(uri);
/*     */   }
/*     */ 
/*     */   private boolean containsPrefixMapping(String prefix, String uri) {
/*  70 */     for (int i = 0; i < this.prefixMap.size(); i += 2)
/*  71 */       if ((((String)this.prefixMap.get(i)).equals(prefix)) && (((String)this.prefixMap.get(i + 1)).equals(uri)))
/*     */       {
/*  73 */         return true;
/*     */       }
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
/*     */   {
/*     */     try {
/*  81 */       flushText();
/*     */ 
/*  83 */       int len = atts.getLength();
/*     */ 
/*  85 */       String p = getPrefix(qName);
/*     */ 
/*  88 */       if (containsPrefixMapping(p, namespaceURI))
/*  89 */         this.serializer.startElementForce(namespaceURI, localName, p, null);
/*     */       else {
/*  91 */         this.serializer.startElement(namespaceURI, localName, p, null);
/*     */       }
/*     */ 
/*  94 */       for (int i = 0; i < this.prefixMap.size(); i += 2)
/*     */       {
/*  99 */         this.serializer.getNamespaceContext().force((String)this.prefixMap.get(i + 1), (String)this.prefixMap.get(i));
/*     */       }
/*     */ 
/* 103 */       for (int i = 0; i < len; i++) {
/* 104 */         String qname = atts.getQName(i);
/* 105 */         if ((!qname.startsWith("xmlns")) && (atts.getURI(i).length() != 0))
/*     */         {
/* 107 */           String prefix = getPrefix(qname);
/*     */ 
/* 109 */           this.serializer.getNamespaceContext().declareNamespace(atts.getURI(i), prefix, true);
/*     */         }
/*     */       }
/*     */ 
/* 113 */       this.serializer.endNamespaceDecls(null);
/*     */ 
/* 115 */       for (int i = 0; i < len; i++)
/*     */       {
/* 117 */         if (!atts.getQName(i).startsWith("xmlns"))
/*     */         {
/* 119 */           this.serializer.attribute(atts.getURI(i), atts.getLocalName(i), atts.getValue(i));
/*     */         }
/*     */       }
/* 121 */       this.prefixMap.clear();
/* 122 */       this.serializer.endAttributes();
/*     */     } catch (IOException e) {
/* 124 */       throw new SAXException2(e);
/*     */     } catch (XMLStreamException e) {
/* 126 */       throw new SAXException2(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getPrefix(String qname)
/*     */   {
/* 132 */     int idx = qname.indexOf(':');
/* 133 */     String prefix = idx == -1 ? "" : qname.substring(0, idx);
/* 134 */     return prefix;
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*     */     try {
/* 139 */       flushText();
/* 140 */       this.serializer.endElement();
/*     */     } catch (IOException e) {
/* 142 */       throw new SAXException2(e);
/*     */     } catch (XMLStreamException e) {
/* 144 */       throw new SAXException2(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void flushText() throws SAXException, IOException, XMLStreamException {
/* 149 */     if (this.text.length() != 0) {
/* 150 */       this.serializer.text(this.text.toString(), null);
/* 151 */       this.text.setLength(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) {
/* 156 */     this.text.append(ch, start, length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.ContentHandlerAdaptor
 * JD-Core Version:    0.6.2
 */