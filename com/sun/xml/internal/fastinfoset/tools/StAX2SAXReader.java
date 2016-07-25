/*     */ package com.sun.xml.internal.fastinfoset.tools;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class StAX2SAXReader
/*     */ {
/*     */   ContentHandler _handler;
/*     */   LexicalHandler _lexicalHandler;
/*     */   XMLStreamReader _reader;
/*     */ 
/*     */   public StAX2SAXReader(XMLStreamReader reader, ContentHandler handler)
/*     */   {
/*  58 */     this._handler = handler;
/*  59 */     this._reader = reader;
/*     */   }
/*     */ 
/*     */   public StAX2SAXReader(XMLStreamReader reader) {
/*  63 */     this._reader = reader;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler) {
/*  67 */     this._handler = handler;
/*     */   }
/*     */ 
/*     */   public void setLexicalHandler(LexicalHandler lexicalHandler) {
/*  71 */     this._lexicalHandler = lexicalHandler;
/*     */   }
/*     */ 
/*     */   public void adapt()
/*     */     throws XMLStreamException, SAXException
/*     */   {
/*  77 */     AttributesImpl attrs = new AttributesImpl();
/*     */ 
/*  82 */     this._handler.startDocument();
/*     */     try
/*     */     {
/*  86 */       while (this._reader.hasNext()) {
/*  87 */         int event = this._reader.next();
/*     */         int nsc;
/*     */         QName qname;
/*     */         String prefix;
/*     */         String localPart;
/*  90 */         switch (event)
/*     */         {
/*     */         case 1:
/*  93 */           nsc = this._reader.getNamespaceCount();
/*  94 */           for (int i = 0; i < nsc; i++) {
/*  95 */             this._handler.startPrefixMapping(this._reader.getNamespacePrefix(i), this._reader.getNamespaceURI(i));
/*     */           }
/*     */ 
/* 100 */           attrs.clear();
/* 101 */           int nat = this._reader.getAttributeCount();
/* 102 */           for (int i = 0; i < nat; i++) {
/* 103 */             QName q = this._reader.getAttributeName(i);
/* 104 */             String qName = this._reader.getAttributePrefix(i);
/* 105 */             if ((qName == null) || (qName == ""))
/* 106 */               qName = q.getLocalPart();
/*     */             else {
/* 108 */               qName = qName + ":" + q.getLocalPart();
/*     */             }
/* 110 */             attrs.addAttribute(this._reader.getAttributeNamespace(i), q.getLocalPart(), qName, this._reader.getAttributeType(i), this._reader.getAttributeValue(i));
/*     */           }
/*     */ 
/* 118 */           qname = this._reader.getName();
/* 119 */           prefix = qname.getPrefix();
/* 120 */           localPart = qname.getLocalPart();
/*     */ 
/* 122 */           this._handler.startElement(this._reader.getNamespaceURI(), localPart, prefix.length() > 0 ? prefix + ":" + localPart : localPart, attrs);
/*     */ 
/* 127 */           break;
/*     */         case 2:
/* 131 */           qname = this._reader.getName();
/* 132 */           prefix = qname.getPrefix();
/* 133 */           localPart = qname.getLocalPart();
/*     */ 
/* 135 */           this._handler.endElement(this._reader.getNamespaceURI(), localPart, prefix.length() > 0 ? prefix + ":" + localPart : localPart);
/*     */ 
/* 141 */           nsc = this._reader.getNamespaceCount();
/* 142 */           for (int i = 0; i < nsc; i++) {
/* 143 */             this._handler.endPrefixMapping(this._reader.getNamespacePrefix(i));
/*     */           }
/* 145 */           break;
/*     */         case 4:
/* 148 */           this._handler.characters(this._reader.getTextCharacters(), this._reader.getTextStart(), this._reader.getTextLength());
/* 149 */           break;
/*     */         case 5:
/* 151 */           this._lexicalHandler.comment(this._reader.getTextCharacters(), this._reader.getTextStart(), this._reader.getTextLength());
/* 152 */           break;
/*     */         case 3:
/* 154 */           this._handler.processingInstruction(this._reader.getPITarget(), this._reader.getPIData());
/* 155 */           break;
/*     */         case 8:
/* 157 */           break;
/*     */         case 6:
/*     */         case 7:
/*     */         default:
/* 159 */           throw new RuntimeException(CommonResourceBundle.getInstance().getString("message.StAX2SAXReader", new Object[] { Integer.valueOf(event) }));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (XMLStreamException e) {
/* 164 */       this._handler.endDocument();
/* 165 */       throw e;
/*     */     }
/*     */ 
/* 168 */     this._handler.endDocument();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.StAX2SAXReader
 * JD-Core Version:    0.6.2
 */