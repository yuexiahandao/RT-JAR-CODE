/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.Envelope;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
/*     */ import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPBody;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ 
/*     */ public abstract class EnvelopeImpl extends ElementImpl
/*     */   implements Envelope
/*     */ {
/*     */   protected HeaderImpl header;
/*     */   protected BodyImpl body;
/*  58 */   String omitXmlDecl = "yes";
/*  59 */   String charset = "utf-8";
/*  60 */   String xmlDecl = null;
/*     */ 
/*     */   protected EnvelopeImpl(SOAPDocumentImpl ownerDoc, Name name) {
/*  63 */     super(ownerDoc, name);
/*     */   }
/*     */ 
/*     */   protected EnvelopeImpl(SOAPDocumentImpl ownerDoc, QName name) {
/*  67 */     super(ownerDoc, name);
/*     */   }
/*     */ 
/*     */   protected EnvelopeImpl(SOAPDocumentImpl ownerDoc, NameImpl name, boolean createHeader, boolean createBody)
/*     */     throws SOAPException
/*     */   {
/*  76 */     this(ownerDoc, name);
/*     */ 
/*  78 */     ensureNamespaceIsDeclared(getElementQName().getPrefix(), getElementQName().getNamespaceURI());
/*     */ 
/*  82 */     if (createHeader) {
/*  83 */       addHeader();
/*     */     }
/*  85 */     if (createBody)
/*  86 */       addBody(); 
/*     */   }
/*     */ 
/*     */   protected abstract NameImpl getHeaderName(String paramString);
/*     */ 
/*     */   protected abstract NameImpl getBodyName(String paramString);
/*     */ 
/*  93 */   public SOAPHeader addHeader() throws SOAPException { return addHeader(null); }
/*     */ 
/*     */   public SOAPHeader addHeader(String prefix)
/*     */     throws SOAPException
/*     */   {
/*  98 */     if ((prefix == null) || (prefix.equals(""))) {
/*  99 */       prefix = getPrefix();
/*     */     }
/*     */ 
/* 102 */     NameImpl headerName = getHeaderName(prefix);
/* 103 */     NameImpl bodyName = getBodyName(prefix);
/*     */ 
/* 105 */     HeaderImpl header = null;
/* 106 */     SOAPElement firstChild = null;
/*     */ 
/* 108 */     Iterator eachChild = getChildElementNodes();
/* 109 */     if (eachChild.hasNext()) {
/* 110 */       firstChild = (SOAPElement)eachChild.next();
/* 111 */       if (firstChild.getElementName().equals(headerName)) {
/* 112 */         log.severe("SAAJ0120.impl.header.already.exists");
/* 113 */         throw new SOAPExceptionImpl("Can't add a header when one is already present.");
/* 114 */       }if (!firstChild.getElementName().equals(bodyName)) {
/* 115 */         log.severe("SAAJ0121.impl.invalid.first.child.of.envelope");
/* 116 */         throw new SOAPExceptionImpl("First child of Envelope must be either a Header or Body");
/*     */       }
/*     */     }
/*     */ 
/* 120 */     header = (HeaderImpl)createElement(headerName);
/* 121 */     insertBefore(header, firstChild);
/* 122 */     header.ensureNamespaceIsDeclared(headerName.getPrefix(), headerName.getURI());
/*     */ 
/* 124 */     return header;
/*     */   }
/*     */ 
/*     */   protected void lookForHeader() throws SOAPException {
/* 128 */     NameImpl headerName = getHeaderName(null);
/*     */ 
/* 130 */     HeaderImpl hdr = (HeaderImpl)findChild(headerName);
/* 131 */     this.header = hdr;
/*     */   }
/*     */ 
/*     */   public SOAPHeader getHeader() throws SOAPException {
/* 135 */     lookForHeader();
/* 136 */     return this.header;
/*     */   }
/*     */ 
/*     */   protected void lookForBody() throws SOAPException {
/* 140 */     NameImpl bodyName = getBodyName(null);
/*     */ 
/* 142 */     BodyImpl bodyChildElement = (BodyImpl)findChild(bodyName);
/* 143 */     this.body = bodyChildElement;
/*     */   }
/*     */ 
/*     */   public SOAPBody addBody() throws SOAPException {
/* 147 */     return addBody(null);
/*     */   }
/*     */ 
/*     */   public SOAPBody addBody(String prefix) throws SOAPException {
/* 151 */     lookForBody();
/*     */ 
/* 153 */     if ((prefix == null) || (prefix.equals(""))) {
/* 154 */       prefix = getPrefix();
/*     */     }
/*     */ 
/* 157 */     if (this.body == null) {
/* 158 */       NameImpl bodyName = getBodyName(prefix);
/* 159 */       this.body = ((BodyImpl)createElement(bodyName));
/* 160 */       insertBefore(this.body, null);
/* 161 */       this.body.ensureNamespaceIsDeclared(bodyName.getPrefix(), bodyName.getURI());
/*     */     } else {
/* 163 */       log.severe("SAAJ0122.impl.body.already.exists");
/* 164 */       throw new SOAPExceptionImpl("Can't add a body when one is already present.");
/*     */     }
/*     */ 
/* 167 */     return this.body;
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(Name name) throws SOAPException {
/* 171 */     if (getBodyName(null).equals(name)) {
/* 172 */       return addBody(name.getPrefix());
/*     */     }
/* 174 */     if (getHeaderName(null).equals(name)) {
/* 175 */       return addHeader(name.getPrefix());
/*     */     }
/*     */ 
/* 178 */     return super.addElement(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(QName name) throws SOAPException {
/* 182 */     if (getBodyName(null).equals(NameImpl.convertToName(name))) {
/* 183 */       return addBody(name.getPrefix());
/*     */     }
/* 185 */     if (getHeaderName(null).equals(NameImpl.convertToName(name))) {
/* 186 */       return addHeader(name.getPrefix());
/*     */     }
/*     */ 
/* 189 */     return super.addElement(name);
/*     */   }
/*     */ 
/*     */   public SOAPBody getBody() throws SOAPException {
/* 193 */     lookForBody();
/* 194 */     return this.body;
/*     */   }
/*     */ 
/*     */   public Source getContent() {
/* 198 */     return new DOMSource(getOwnerDocument());
/*     */   }
/*     */ 
/*     */   public Name createName(String localName, String prefix, String uri)
/*     */     throws SOAPException
/*     */   {
/* 208 */     if ("xmlns".equals(prefix)) {
/* 209 */       log.severe("SAAJ0123.impl.no.reserved.xmlns");
/* 210 */       throw new SOAPExceptionImpl("Cannot declare reserved xmlns prefix");
/*     */     }
/*     */ 
/* 213 */     if ((prefix == null) && ("xmlns".equals(localName))) {
/* 214 */       log.severe("SAAJ0124.impl.qualified.name.cannot.be.xmlns");
/* 215 */       throw new SOAPExceptionImpl("Qualified name cannot be xmlns");
/*     */     }
/*     */ 
/* 218 */     return NameImpl.create(localName, prefix, uri);
/*     */   }
/*     */ 
/*     */   public Name createName(String localName, String prefix) throws SOAPException
/*     */   {
/* 223 */     String namespace = getNamespaceURI(prefix);
/* 224 */     if (namespace == null) {
/* 225 */       log.log(Level.SEVERE, "SAAJ0126.impl.cannot.locate.ns", new String[] { prefix });
/*     */ 
/* 229 */       throw new SOAPExceptionImpl("Unable to locate namespace for prefix " + prefix);
/*     */     }
/*     */ 
/* 232 */     return NameImpl.create(localName, prefix, namespace);
/*     */   }
/*     */ 
/*     */   public Name createName(String localName) throws SOAPException {
/* 236 */     return NameImpl.createFromUnqualifiedName(localName);
/*     */   }
/*     */ 
/*     */   public void setOmitXmlDecl(String value) {
/* 240 */     this.omitXmlDecl = value;
/*     */   }
/*     */ 
/*     */   public void setXmlDecl(String value) {
/* 244 */     this.xmlDecl = value;
/*     */   }
/*     */ 
/*     */   private String getOmitXmlDecl() {
/* 248 */     return this.omitXmlDecl;
/*     */   }
/*     */ 
/*     */   public void setCharsetEncoding(String value) {
/* 252 */     this.charset = value;
/*     */   }
/*     */ 
/*     */   public void output(OutputStream out) throws IOException {
/*     */     try {
/* 257 */       Transformer transformer = EfficientStreamingTransformer.newTransformer();
/*     */ 
/* 260 */       transformer.setOutputProperty("omit-xml-declaration", "yes");
/*     */ 
/* 264 */       transformer.setOutputProperty("encoding", this.charset);
/*     */ 
/* 268 */       if ((this.omitXmlDecl.equals("no")) && (this.xmlDecl == null)) {
/* 269 */         this.xmlDecl = ("<?xml version=\"" + getOwnerDocument().getXmlVersion() + "\" encoding=\"" + this.charset + "\" ?>");
/*     */       }
/*     */ 
/* 273 */       StreamResult result = new StreamResult(out);
/* 274 */       if (this.xmlDecl != null) {
/* 275 */         OutputStreamWriter writer = new OutputStreamWriter(out, this.charset);
/* 276 */         writer.write(this.xmlDecl);
/* 277 */         writer.flush();
/* 278 */         result = new StreamResult(writer);
/*     */       }
/*     */ 
/* 282 */       log.log(Level.FINE, "SAAJ0190.impl.set.xml.declaration", new String[] { this.omitXmlDecl });
/*     */ 
/* 286 */       log.log(Level.FINE, "SAAJ0191.impl.set.encoding", new String[] { this.charset });
/*     */ 
/* 292 */       transformer.transform(getContent(), result);
/*     */     } catch (Exception ex) {
/* 294 */       throw new IOException(ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void output(OutputStream out, boolean isFastInfoset)
/*     */     throws IOException
/*     */   {
/* 304 */     if (!isFastInfoset) {
/* 305 */       output(out);
/*     */     }
/*     */     else
/*     */       try
/*     */       {
/* 310 */         Source source = getContent();
/* 311 */         Transformer transformer = EfficientStreamingTransformer.newTransformer();
/* 312 */         transformer.transform(getContent(), FastInfosetReflection.FastInfosetResult_new(out));
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 316 */         throw new IOException(ex.getMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   public SOAPElement setElementQName(QName newName)
/*     */     throws SOAPException
/*     */   {
/* 355 */     log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[] { this.elementQName.getLocalPart(), newName.getLocalPart() });
/*     */ 
/* 359 */     throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl
 * JD-Core Version:    0.6.2
 */