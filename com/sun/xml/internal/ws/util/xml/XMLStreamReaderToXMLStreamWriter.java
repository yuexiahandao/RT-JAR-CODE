/*     */ package com.sun.xml.internal.ws.util.xml;
/*     */ 
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class XMLStreamReaderToXMLStreamWriter
/*     */ {
/*     */   private static final int BUF_SIZE = 4096;
/*     */   protected XMLStreamReader in;
/*     */   protected XMLStreamWriter out;
/*     */   private char[] buf;
/*     */ 
/*     */   public void bridge(XMLStreamReader in, XMLStreamWriter out)
/*     */     throws XMLStreamException
/*     */   {
/*  61 */     assert ((in != null) && (out != null));
/*  62 */     this.in = in;
/*  63 */     this.out = out;
/*     */ 
/*  66 */     int depth = 0;
/*     */ 
/*  68 */     this.buf = new char[4096];
/*     */ 
/*  71 */     int event = in.getEventType();
/*  72 */     if (event == 7)
/*     */     {
/*  74 */       while (!in.isStartElement()) {
/*  75 */         event = in.next();
/*  76 */         if (event == 5) {
/*  77 */           handleComment();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  82 */     if (event != 1) {
/*  83 */       throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
/*     */     }
/*     */ 
/*     */     do
/*     */     {
/*  89 */       switch (event) {
/*     */       case 1:
/*  91 */         depth++;
/*  92 */         handleStartElement();
/*  93 */         break;
/*     */       case 2:
/*  95 */         handleEndElement();
/*  96 */         depth--;
/*  97 */         if (depth == 0)
/*     */           return;
/*     */         break;
/*     */       case 4:
/* 101 */         handleCharacters();
/* 102 */         break;
/*     */       case 9:
/* 104 */         handleEntityReference();
/* 105 */         break;
/*     */       case 3:
/* 107 */         handlePI();
/* 108 */         break;
/*     */       case 5:
/* 110 */         handleComment();
/* 111 */         break;
/*     */       case 11:
/* 113 */         handleDTD();
/* 114 */         break;
/*     */       case 12:
/* 116 */         handleCDATA();
/* 117 */         break;
/*     */       case 6:
/* 119 */         handleSpace();
/* 120 */         break;
/*     */       case 7:
/*     */       case 8:
/*     */       case 10:
/*     */       default:
/* 122 */         throw new InternalError("processing event: " + event);
/*     */       }
/*     */ 
/* 125 */       event = in.next();
/* 126 */     }while (depth != 0);
/*     */   }
/*     */ 
/*     */   protected void handlePI() throws XMLStreamException {
/* 130 */     this.out.writeProcessingInstruction(this.in.getPITarget(), this.in.getPIData());
/*     */   }
/*     */ 
/*     */   protected void handleCharacters()
/*     */     throws XMLStreamException
/*     */   {
/* 137 */     int start = 0; for (int read = this.buf.length; read == this.buf.length; start += this.buf.length) {
/* 138 */       read = this.in.getTextCharacters(start, this.buf, 0, this.buf.length);
/* 139 */       this.out.writeCharacters(this.buf, 0, read);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleEndElement() throws XMLStreamException {
/* 144 */     this.out.writeEndElement();
/*     */   }
/*     */ 
/*     */   protected void handleStartElement() throws XMLStreamException {
/* 148 */     String nsUri = this.in.getNamespaceURI();
/* 149 */     if (nsUri == null)
/* 150 */       this.out.writeStartElement(this.in.getLocalName());
/*     */     else {
/* 152 */       this.out.writeStartElement(fixNull(this.in.getPrefix()), this.in.getLocalName(), nsUri);
/*     */     }
/*     */ 
/* 159 */     int nsCount = this.in.getNamespaceCount();
/* 160 */     for (int i = 0; i < nsCount; i++) {
/* 161 */       this.out.writeNamespace(this.in.getNamespacePrefix(i), fixNull(this.in.getNamespaceURI(i)));
/*     */     }
/*     */ 
/* 167 */     int attCount = this.in.getAttributeCount();
/* 168 */     for (int i = 0; i < attCount; i++)
/* 169 */       handleAttribute(i);
/*     */   }
/*     */ 
/*     */   protected void handleAttribute(int i)
/*     */     throws XMLStreamException
/*     */   {
/* 180 */     String nsUri = this.in.getAttributeNamespace(i);
/* 181 */     String prefix = this.in.getAttributePrefix(i);
/* 182 */     if (fixNull(nsUri).equals("http://www.w3.org/2000/xmlns/"))
/*     */     {
/* 184 */       return;
/*     */     }
/*     */ 
/* 187 */     if ((nsUri == null) || (prefix == null) || (prefix.equals(""))) {
/* 188 */       this.out.writeAttribute(this.in.getAttributeLocalName(i), this.in.getAttributeValue(i));
/*     */     }
/*     */     else
/*     */     {
/* 193 */       this.out.writeAttribute(prefix, nsUri, this.in.getAttributeLocalName(i), this.in.getAttributeValue(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleDTD()
/*     */     throws XMLStreamException
/*     */   {
/* 203 */     this.out.writeDTD(this.in.getText());
/*     */   }
/*     */ 
/*     */   protected void handleComment() throws XMLStreamException {
/* 207 */     this.out.writeComment(this.in.getText());
/*     */   }
/*     */ 
/*     */   protected void handleEntityReference() throws XMLStreamException {
/* 211 */     this.out.writeEntityRef(this.in.getText());
/*     */   }
/*     */ 
/*     */   protected void handleSpace() throws XMLStreamException {
/* 215 */     handleCharacters();
/*     */   }
/*     */ 
/*     */   protected void handleCDATA() throws XMLStreamException {
/* 219 */     this.out.writeCData(this.in.getText());
/*     */   }
/*     */ 
/*     */   private static String fixNull(String s) {
/* 223 */     if (s == null) return "";
/* 224 */     return s;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter
 * JD-Core Version:    0.6.2
 */