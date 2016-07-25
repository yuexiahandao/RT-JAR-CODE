/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ 
/*     */ public class SAX2StAXStreamWriter extends SAX2StAXBaseWriter
/*     */ {
/*     */   private XMLStreamWriter writer;
/*  47 */   private boolean needToCallStartDocument = false;
/*     */ 
/*     */   public SAX2StAXStreamWriter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SAX2StAXStreamWriter(XMLStreamWriter writer)
/*     */   {
/*  55 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter getStreamWriter()
/*     */   {
/*  62 */     return this.writer;
/*     */   }
/*     */ 
/*     */   public void setStreamWriter(XMLStreamWriter writer)
/*     */   {
/*  69 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/*  75 */     super.startDocument();
/*     */ 
/*  79 */     this.needToCallStartDocument = true;
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException
/*     */   {
/*     */     try
/*     */     {
/*  86 */       this.writer.writeEndDocument();
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/*  90 */       throw new SAXException(e);
/*     */     }
/*     */ 
/*  94 */     super.endDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */     throws SAXException
/*     */   {
/* 101 */     if (this.needToCallStartDocument) {
/*     */       try {
/* 103 */         if (this.docLocator == null)
/* 104 */           this.writer.writeStartDocument();
/*     */         else {
/*     */           try {
/* 107 */             this.writer.writeStartDocument(((Locator2)this.docLocator).getXMLVersion());
/*     */           } catch (ClassCastException e) {
/* 109 */             this.writer.writeStartDocument();
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (XMLStreamException e)
/*     */       {
/* 115 */         throw new SAXException(e);
/*     */       }
/*     */ 
/* 118 */       this.needToCallStartDocument = false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 123 */       String[] qname = { null, null };
/* 124 */       parseQName(qName, qname);
/*     */ 
/* 128 */       this.writer.writeStartElement(qName);
/*     */ 
/* 150 */       int i = 0; for (int s = attributes.getLength(); i < s; i++)
/*     */       {
/* 152 */         parseQName(attributes.getQName(i), qname);
/*     */ 
/* 154 */         String attrPrefix = qname[0];
/* 155 */         String attrLocal = qname[1];
/*     */ 
/* 157 */         String attrQName = attributes.getQName(i);
/* 158 */         String attrValue = attributes.getValue(i);
/* 159 */         String attrURI = attributes.getURI(i);
/*     */ 
/* 161 */         if (("xmlns".equals(attrPrefix)) || ("xmlns".equals(attrQName)))
/*     */         {
/* 166 */           if (attrLocal.length() == 0)
/*     */           {
/* 168 */             this.writer.setDefaultNamespace(attrValue);
/*     */           }
/*     */           else
/*     */           {
/* 172 */             this.writer.setPrefix(attrLocal, attrValue);
/*     */           }
/*     */ 
/* 176 */           this.writer.writeNamespace(attrLocal, attrValue);
/*     */         }
/* 178 */         else if (attrPrefix.length() > 0)
/*     */         {
/* 180 */           this.writer.writeAttribute(attrPrefix, attrURI, attrLocal, attrValue);
/*     */         }
/*     */         else
/*     */         {
/* 184 */           this.writer.writeAttribute(attrQName, attrValue);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 190 */       throw new SAXException(e);
/*     */     }
/*     */     finally
/*     */     {
/* 194 */       super.startElement(uri, localName, qName, attributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 205 */       this.writer.writeEndElement();
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 209 */       throw new SAXException(e);
/*     */     }
/*     */     finally
/*     */     {
/* 213 */       super.endElement(uri, localName, qName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 221 */     super.comment(ch, start, length);
/*     */     try
/*     */     {
/* 224 */       this.writer.writeComment(new String(ch, start, length));
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 228 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 237 */     super.characters(ch, start, length);
/*     */     try
/*     */     {
/* 240 */       if (!this.isCDATA)
/*     */       {
/* 242 */         this.writer.writeCharacters(ch, start, length);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 248 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endCDATA()
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 258 */       this.writer.writeCData(this.CDATABuffer.toString());
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 262 */       throw new SAXException(e);
/*     */     }
/*     */ 
/* 266 */     super.endCDATA();
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 273 */     super.ignorableWhitespace(ch, start, length);
/*     */     try
/*     */     {
/* 276 */       this.writer.writeCharacters(ch, start, length);
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 280 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 289 */     super.processingInstruction(target, data);
/*     */     try
/*     */     {
/* 292 */       this.writer.writeProcessingInstruction(target, data);
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 296 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXStreamWriter
 * JD-Core Version:    0.6.2
 */