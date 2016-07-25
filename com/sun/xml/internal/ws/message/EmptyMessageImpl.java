/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class EmptyMessageImpl extends AbstractMessageImpl
/*     */ {
/*     */   private final HeaderList headers;
/*     */   private final AttachmentSet attachmentSet;
/*     */ 
/*     */   public EmptyMessageImpl(SOAPVersion version)
/*     */   {
/*  60 */     super(version);
/*  61 */     this.headers = new HeaderList();
/*  62 */     this.attachmentSet = new AttachmentSetImpl();
/*     */   }
/*     */ 
/*     */   public EmptyMessageImpl(HeaderList headers, @NotNull AttachmentSet attachmentSet, SOAPVersion version) {
/*  66 */     super(version);
/*  67 */     if (headers == null)
/*  68 */       headers = new HeaderList();
/*  69 */     this.attachmentSet = attachmentSet;
/*  70 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */   private EmptyMessageImpl(EmptyMessageImpl that)
/*     */   {
/*  77 */     super(that);
/*  78 */     this.headers = new HeaderList(that.headers);
/*  79 */     this.attachmentSet = that.attachmentSet;
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders() {
/*  83 */     return !this.headers.isEmpty();
/*     */   }
/*     */ 
/*     */   public HeaderList getHeaders() {
/*  87 */     return this.headers;
/*     */   }
/*     */ 
/*     */   public String getPayloadLocalPart() {
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() throws XMLStreamException {
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Message copy() {
/* 119 */     return new EmptyMessageImpl(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.EmptyMessageImpl
 * JD-Core Version:    0.6.2
 */