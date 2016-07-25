/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class MTOMDecorator
/*     */   implements XmlVisitor
/*     */ {
/*     */   private final XmlVisitor next;
/*     */   private final AttachmentUnmarshaller au;
/*     */   private UnmarshallerImpl parent;
/*  50 */   private final Base64Data base64data = new Base64Data();
/*     */   private boolean inXopInclude;
/*     */   private boolean followXop;
/*     */ 
/*     */   public MTOMDecorator(UnmarshallerImpl parent, XmlVisitor next, AttachmentUnmarshaller au)
/*     */   {
/*  68 */     this.parent = parent;
/*  69 */     this.next = next;
/*  70 */     this.au = au;
/*     */   }
/*     */ 
/*     */   public void startDocument(LocatorEx loc, NamespaceContext nsContext) throws SAXException {
/*  74 */     this.next.startDocument(loc, nsContext);
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/*  78 */     this.next.endDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(TagName tagName) throws SAXException {
/*  82 */     if ((tagName.local.equals("Include")) && (tagName.uri.equals("http://www.w3.org/2004/08/xop/include")))
/*     */     {
/*  84 */       String href = tagName.atts.getValue("href");
/*  85 */       DataHandler attachment = this.au.getAttachmentAsDataHandler(href);
/*  86 */       if (attachment == null)
/*     */       {
/*  88 */         this.parent.getEventHandler().handleEvent(null);
/*     */       }
/*     */ 
/*  91 */       this.base64data.set(attachment);
/*  92 */       this.next.text(this.base64data);
/*  93 */       this.inXopInclude = true;
/*  94 */       this.followXop = true;
/*     */     } else {
/*  96 */       this.next.startElement(tagName);
/*     */     }
/*     */   }
/*     */ 
/* 100 */   public void endElement(TagName tagName) throws SAXException { if (this.inXopInclude)
/*     */     {
/* 102 */       this.inXopInclude = false;
/* 103 */       this.followXop = true;
/* 104 */       return;
/*     */     }
/* 106 */     this.next.endElement(tagName); }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String nsUri) throws SAXException
/*     */   {
/* 110 */     this.next.startPrefixMapping(prefix, nsUri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 114 */     this.next.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void text(CharSequence pcdata) throws SAXException {
/* 118 */     if (!this.followXop)
/* 119 */       this.next.text(pcdata);
/*     */     else
/* 121 */       this.followXop = false;
/*     */   }
/*     */ 
/*     */   public UnmarshallingContext getContext() {
/* 125 */     return this.next.getContext();
/*     */   }
/*     */ 
/*     */   public XmlVisitor.TextPredictor getPredictor() {
/* 129 */     return this.next.getPredictor();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.MTOMDecorator
 * JD-Core Version:    0.6.2
 */