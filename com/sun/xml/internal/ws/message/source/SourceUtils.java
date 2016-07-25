/*     */ package com.sun.xml.internal.ws.message.source;
/*     */ 
/*     */ import com.sun.xml.internal.ws.message.RootElementSniffer;
/*     */ import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ final class SourceUtils
/*     */ {
/*     */   int srcType;
/*  57 */   private final int domSource = 1;
/*  58 */   private final int streamSource = 2;
/*  59 */   private final int saxSource = 4;
/*     */ 
/*     */   public SourceUtils(Source src) {
/*  62 */     if ((src instanceof StreamSource))
/*  63 */       this.srcType = 2;
/*  64 */     else if ((src instanceof DOMSource))
/*  65 */       this.srcType = 1;
/*  66 */     else if ((src instanceof SAXSource))
/*  67 */       this.srcType = 4;
/*     */   }
/*     */ 
/*     */   public boolean isDOMSource()
/*     */   {
/*  72 */     return (this.srcType & 0x1) == 1;
/*     */   }
/*     */ 
/*     */   public boolean isStreamSource() {
/*  76 */     return (this.srcType & 0x2) == 2;
/*     */   }
/*     */ 
/*     */   public boolean isSaxSource() {
/*  80 */     return (this.srcType & 0x4) == 4;
/*     */   }
/*     */ 
/*     */   public QName sniff(Source src)
/*     */   {
/*  90 */     return sniff(src, new RootElementSniffer());
/*     */   }
/*     */ 
/*     */   public QName sniff(Source src, RootElementSniffer sniffer) {
/*  94 */     String localName = null;
/*  95 */     String namespaceUri = null;
/*     */ 
/*  97 */     if (isDOMSource()) {
/*  98 */       DOMSource domSource = (DOMSource)src;
/*  99 */       Node n = domSource.getNode();
/* 100 */       if (n.getNodeType() == 9) {
/* 101 */         n = ((Document)n).getDocumentElement();
/*     */       }
/* 103 */       localName = n.getLocalName();
/* 104 */       namespaceUri = n.getNamespaceURI();
/* 105 */     } else if (isSaxSource()) {
/* 106 */       SAXSource saxSrc = (SAXSource)src;
/* 107 */       SAXResult saxResult = new SAXResult(sniffer);
/*     */       try {
/* 109 */         Transformer tr = XmlUtil.newTransformer();
/* 110 */         tr.transform(saxSrc, saxResult);
/*     */       } catch (TransformerConfigurationException e) {
/* 112 */         throw new WebServiceException(e);
/*     */       }
/*     */       catch (TransformerException e)
/*     */       {
/* 120 */         localName = sniffer.getLocalName();
/* 121 */         namespaceUri = sniffer.getNsUri();
/*     */       }
/*     */     }
/* 124 */     return new QName(namespaceUri, localName);
/*     */   }
/*     */ 
/* 128 */   public static void serializeSource(Source src, XMLStreamWriter writer) throws XMLStreamException { XMLStreamReader reader = SourceReaderFactory.createSourceReader(src, true);
/*     */     int state;
/*     */     do {
/* 131 */       state = reader.next();
/* 132 */       switch (state)
/*     */       {
/*     */       case 1:
/* 138 */         String uri = reader.getNamespaceURI();
/* 139 */         String prefix = reader.getPrefix();
/* 140 */         String localName = reader.getLocalName();
/*     */ 
/* 142 */         if (prefix == null) {
/* 143 */           if (uri == null)
/* 144 */             writer.writeStartElement(localName);
/*     */           else
/* 146 */             writer.writeStartElement(uri, localName);
/*     */         }
/*     */         else {
/* 149 */           assert (uri != null);
/*     */ 
/* 151 */           if (prefix.length() > 0)
/*     */           {
/* 155 */             String writerURI = null;
/* 156 */             if (writer.getNamespaceContext() != null)
/* 157 */               writerURI = writer.getNamespaceContext().getNamespaceURI(prefix);
/* 158 */             String writerPrefix = writer.getPrefix(uri);
/* 159 */             if (declarePrefix(prefix, uri, writerPrefix, writerURI)) {
/* 160 */               writer.writeStartElement(prefix, localName, uri);
/* 161 */               writer.setPrefix(prefix, uri != null ? uri : "");
/* 162 */               writer.writeNamespace(prefix, uri);
/*     */             } else {
/* 164 */               writer.writeStartElement(prefix, localName, uri);
/*     */             }
/*     */           } else {
/* 167 */             writer.writeStartElement(prefix, localName, uri);
/*     */           }
/*     */         }
/*     */ 
/* 171 */         int n = reader.getNamespaceCount();
/*     */ 
/* 173 */         for (int i = 0; i < n; i++) {
/* 174 */           String nsPrefix = reader.getNamespacePrefix(i);
/* 175 */           if (nsPrefix == null) nsPrefix = "";
/*     */ 
/* 177 */           String writerURI = null;
/* 178 */           if (writer.getNamespaceContext() != null) {
/* 179 */             writerURI = writer.getNamespaceContext().getNamespaceURI(nsPrefix);
/*     */           }
/*     */ 
/* 183 */           String readerURI = reader.getNamespaceURI(i);
/*     */ 
/* 192 */           if ((writerURI == null) || (nsPrefix.length() == 0) || (prefix.length() == 0) || ((!nsPrefix.equals(prefix)) && (!writerURI.equals(readerURI))))
/*     */           {
/* 194 */             writer.setPrefix(nsPrefix, readerURI != null ? readerURI : "");
/* 195 */             writer.writeNamespace(nsPrefix, readerURI != null ? readerURI : "");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 200 */         n = reader.getAttributeCount();
/* 201 */         for (int i = 0; i < n; i++) {
/* 202 */           String attrPrefix = reader.getAttributePrefix(i);
/* 203 */           String attrURI = reader.getAttributeNamespace(i);
/*     */ 
/* 205 */           writer.writeAttribute(attrPrefix != null ? attrPrefix : "", attrURI != null ? attrURI : "", reader.getAttributeLocalName(i), reader.getAttributeValue(i));
/*     */ 
/* 210 */           setUndeclaredPrefix(attrPrefix, attrURI, writer);
/*     */         }
/* 212 */         break;
/*     */       case 2:
/* 214 */         writer.writeEndElement();
/* 215 */         break;
/*     */       case 4:
/* 217 */         writer.writeCharacters(reader.getText());
/*     */       case 3: } 
/* 219 */     }while (state != 8);
/* 220 */     reader.close();
/*     */   }
/*     */ 
/*     */   private static void setUndeclaredPrefix(String prefix, String readerURI, XMLStreamWriter writer)
/*     */     throws XMLStreamException
/*     */   {
/* 230 */     String writerURI = null;
/* 231 */     if (writer.getNamespaceContext() != null) {
/* 232 */       writerURI = writer.getNamespaceContext().getNamespaceURI(prefix);
/*     */     }
/* 234 */     if (writerURI == null) {
/* 235 */       writer.setPrefix(prefix, readerURI != null ? readerURI : "");
/* 236 */       writer.writeNamespace(prefix, readerURI != null ? readerURI : "");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean declarePrefix(String rPrefix, String rUri, String wPrefix, String wUri)
/*     */   {
/* 248 */     if ((wUri == null) || ((wPrefix != null) && (!rPrefix.equals(wPrefix))) || ((rUri != null) && (!wUri.equals(rUri))))
/*     */     {
/* 250 */       return true;
/* 251 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.source.SourceUtils
 * JD-Core Version:    0.6.2
 */