/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
/*     */ import java.io.IOException;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class MTOMXmlOutput extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XmlOutput next;
/*     */   private String nsUri;
/*     */   private String localName;
/*     */ 
/*     */   public MTOMXmlOutput(XmlOutput next)
/*     */   {
/*  56 */     this.next = next;
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/*  60 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  61 */     this.next.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/*  65 */     this.next.endDocument(fragment);
/*  66 */     super.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(Name name) throws IOException, XMLStreamException {
/*  70 */     this.next.beginStartTag(name);
/*  71 */     this.nsUri = name.nsUri;
/*  72 */     this.localName = name.localName;
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/*  76 */     this.next.beginStartTag(prefix, localName);
/*  77 */     this.nsUri = this.nsContext.getNamespaceURI(prefix);
/*  78 */     this.localName = localName;
/*     */   }
/*     */ 
/*     */   public void attribute(Name name, String value) throws IOException, XMLStreamException {
/*  82 */     this.next.attribute(name, value);
/*     */   }
/*     */ 
/*     */   public void attribute(int prefix, String localName, String value) throws IOException, XMLStreamException {
/*  86 */     this.next.attribute(prefix, localName, value);
/*     */   }
/*     */ 
/*     */   public void endStartTag() throws IOException, SAXException {
/*  90 */     this.next.endStartTag();
/*     */   }
/*     */ 
/*     */   public void endTag(Name name) throws IOException, SAXException, XMLStreamException {
/*  94 */     this.next.endTag(name);
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName) throws IOException, SAXException, XMLStreamException {
/*  98 */     this.next.endTag(prefix, localName);
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 102 */     this.next.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 106 */     if (((value instanceof Base64Data)) && (!this.serializer.getInlineBinaryFlag())) {
/* 107 */       Base64Data b64d = (Base64Data)value;
/*     */       String cid;
/*     */       String cid;
/* 109 */       if (b64d.hasData()) {
/* 110 */         cid = this.serializer.attachmentMarshaller.addMtomAttachment(b64d.get(), 0, b64d.getDataLen(), b64d.getMimeType(), this.nsUri, this.localName);
/*     */       }
/*     */       else {
/* 113 */         cid = this.serializer.attachmentMarshaller.addMtomAttachment(b64d.getDataHandler(), this.nsUri, this.localName);
/*     */       }
/*     */ 
/* 116 */       if (cid != null) {
/* 117 */         this.nsContext.getCurrent().push();
/* 118 */         int prefix = this.nsContext.declareNsUri("http://www.w3.org/2004/08/xop/include", "xop", false);
/* 119 */         beginStartTag(prefix, "Include");
/* 120 */         attribute(-1, "href", cid);
/* 121 */         endStartTag();
/* 122 */         endTag(prefix, "Include");
/* 123 */         this.nsContext.getCurrent().pop();
/* 124 */         return;
/*     */       }
/*     */     }
/* 127 */     this.next.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.MTOMXmlOutput
 * JD-Core Version:    0.6.2
 */