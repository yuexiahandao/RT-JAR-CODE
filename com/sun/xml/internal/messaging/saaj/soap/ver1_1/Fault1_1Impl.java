/*     */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFaultElement;
/*     */ 
/*     */ public class Fault1_1Impl extends FaultImpl
/*     */ {
/*  55 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.ver1_1", "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");
/*     */ 
/*     */   public Fault1_1Impl(SOAPDocumentImpl ownerDocument, String prefix)
/*     */   {
/*  61 */     super(ownerDocument, NameImpl.createFault1_1Name(prefix));
/*     */   }
/*     */ 
/*     */   protected NameImpl getDetailName() {
/*  65 */     return NameImpl.createDetail1_1Name();
/*     */   }
/*     */ 
/*     */   protected NameImpl getFaultCodeName() {
/*  69 */     return NameImpl.createFromUnqualifiedName("faultcode");
/*     */   }
/*     */ 
/*     */   protected NameImpl getFaultStringName() {
/*  73 */     return NameImpl.createFromUnqualifiedName("faultstring");
/*     */   }
/*     */ 
/*     */   protected NameImpl getFaultActorName() {
/*  77 */     return NameImpl.createFromUnqualifiedName("faultactor");
/*     */   }
/*     */ 
/*     */   protected DetailImpl createDetail() {
/*  81 */     return new Detail1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument());
/*     */   }
/*     */ 
/*     */   protected FaultElementImpl createSOAPFaultElement(String localName)
/*     */   {
/*  86 */     return new FaultElement1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), localName);
/*     */   }
/*     */ 
/*     */   protected void checkIfStandardFaultCode(String faultCode, String uri)
/*     */     throws SOAPException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void finallySetFaultCode(String faultcode)
/*     */     throws SOAPException
/*     */   {
/*  99 */     this.faultCodeElement.addTextNode(faultcode);
/*     */   }
/*     */ 
/*     */   public String getFaultCode() {
/* 103 */     if (this.faultCodeElement == null)
/* 104 */       findFaultCodeElement();
/* 105 */     return this.faultCodeElement.getValue();
/*     */   }
/*     */ 
/*     */   public Name getFaultCodeAsName()
/*     */   {
/* 110 */     String faultcodeString = getFaultCode();
/* 111 */     if (faultcodeString == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     int prefixIndex = faultcodeString.indexOf(':');
/* 115 */     if (prefixIndex == -1)
/*     */     {
/* 121 */       return NameImpl.createFromUnqualifiedName(faultcodeString);
/*     */     }
/*     */ 
/* 125 */     String prefix = faultcodeString.substring(0, prefixIndex);
/* 126 */     if (this.faultCodeElement == null)
/* 127 */       findFaultCodeElement();
/* 128 */     String nsName = this.faultCodeElement.getNamespaceURI(prefix);
/* 129 */     return NameImpl.createFromQualifiedName(faultcodeString, nsName);
/*     */   }
/*     */ 
/*     */   public QName getFaultCodeAsQName() {
/* 133 */     String faultcodeString = getFaultCode();
/* 134 */     if (faultcodeString == null) {
/* 135 */       return null;
/*     */     }
/* 137 */     if (this.faultCodeElement == null)
/* 138 */       findFaultCodeElement();
/* 139 */     return convertCodeToQName(faultcodeString, this.faultCodeElement);
/*     */   }
/*     */ 
/*     */   public void setFaultString(String faultString) throws SOAPException
/*     */   {
/* 144 */     if (this.faultStringElement == null) {
/* 145 */       findFaultStringElement();
/*     */     }
/* 147 */     if (this.faultStringElement == null) {
/* 148 */       this.faultStringElement = addSOAPFaultElement("faultstring");
/*     */     } else {
/* 150 */       this.faultStringElement.removeContents();
/*     */ 
/* 152 */       this.faultStringElement.removeAttribute("xml:lang");
/*     */     }
/*     */ 
/* 155 */     this.faultStringElement.addTextNode(faultString);
/*     */   }
/*     */ 
/*     */   public String getFaultString() {
/* 159 */     if (this.faultStringElement == null)
/* 160 */       findFaultStringElement();
/* 161 */     return this.faultStringElement.getValue();
/*     */   }
/*     */ 
/*     */   public Locale getFaultStringLocale()
/*     */   {
/* 166 */     if (this.faultStringElement == null)
/* 167 */       findFaultStringElement();
/* 168 */     if (this.faultStringElement != null) {
/* 169 */       String xmlLangAttr = this.faultStringElement.getAttributeValue(NameImpl.createFromUnqualifiedName("xml:lang"));
/*     */ 
/* 172 */       if (xmlLangAttr != null)
/* 173 */         return xmlLangToLocale(xmlLangAttr);
/*     */     }
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   public void setFaultString(String faultString, Locale locale) throws SOAPException
/*     */   {
/* 180 */     setFaultString(faultString);
/* 181 */     this.faultStringElement.addAttribute(NameImpl.createFromTagName("xml:lang"), localeToXmlLang(locale));
/*     */   }
/*     */ 
/*     */   protected boolean isStandardFaultElement(String localName)
/*     */   {
/* 187 */     if ((localName.equalsIgnoreCase("detail")) || (localName.equalsIgnoreCase("faultcode")) || (localName.equalsIgnoreCase("faultstring")) || (localName.equalsIgnoreCase("faultactor")))
/*     */     {
/* 191 */       return true;
/*     */     }
/* 193 */     return false;
/*     */   }
/*     */ 
/*     */   public void appendFaultSubcode(QName subcode) {
/* 197 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "appendFaultSubcode");
/*     */ 
/* 201 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public void removeAllFaultSubcodes() {
/* 205 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "removeAllFaultSubcodes");
/*     */ 
/* 209 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public Iterator getFaultSubcodes() {
/* 213 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultSubcodes");
/*     */ 
/* 217 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public String getFaultReasonText(Locale locale) {
/* 221 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultReasonText");
/*     */ 
/* 225 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public Iterator getFaultReasonTexts() {
/* 229 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultReasonTexts");
/*     */ 
/* 233 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public Iterator getFaultReasonLocales() {
/* 237 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultReasonLocales");
/*     */ 
/* 241 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public void addFaultReasonText(String text, Locale locale) throws SOAPException
/*     */   {
/* 246 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "addFaultReasonText");
/*     */ 
/* 250 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public String getFaultRole() {
/* 254 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultRole");
/*     */ 
/* 258 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public void setFaultRole(String uri) {
/* 262 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "setFaultRole");
/*     */ 
/* 266 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public String getFaultNode() {
/* 270 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultNode");
/*     */ 
/* 274 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public void setFaultNode(String uri) {
/* 278 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "setFaultNode");
/*     */ 
/* 282 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*     */   }
/*     */ 
/*     */   protected QName getDefaultFaultCode() {
/* 286 */     return new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server");
/*     */   }
/*     */ 
/*     */   public SOAPElement addChildElement(SOAPElement element) throws SOAPException
/*     */   {
/* 291 */     String localName = element.getLocalName();
/* 292 */     if (("Detail".equalsIgnoreCase(localName)) && 
/* 293 */       (hasDetail())) {
/* 294 */       log.severe("SAAJ0305.ver1_2.detail.exists.error");
/* 295 */       throw new SOAPExceptionImpl("Cannot add Detail, Detail already exists");
/*     */     }
/*     */ 
/* 298 */     return super.addChildElement(element);
/*     */   }
/*     */ 
/*     */   protected FaultElementImpl createSOAPFaultElement(QName qname) {
/* 302 */     return new FaultElement1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), qname);
/*     */   }
/*     */ 
/*     */   protected FaultElementImpl createSOAPFaultElement(Name qname)
/*     */   {
/* 308 */     return new FaultElement1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), (NameImpl)qname);
/*     */   }
/*     */ 
/*     */   public void setFaultCode(String faultCode, String prefix, String uri)
/*     */     throws SOAPException
/*     */   {
/* 315 */     if (((prefix == null) || ("".equals(prefix))) && 
/* 316 */       (uri != null) && (!"".equals(uri))) {
/* 317 */       prefix = getNamespacePrefix(uri);
/* 318 */       if ((prefix == null) || ("".equals(prefix))) {
/* 319 */         prefix = "ns0";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 324 */     if (this.faultCodeElement == null) {
/* 325 */       findFaultCodeElement();
/*     */     }
/* 327 */     if (this.faultCodeElement == null)
/* 328 */       this.faultCodeElement = addFaultCodeElement();
/*     */     else {
/* 330 */       this.faultCodeElement.removeContents();
/*     */     }
/* 332 */     if (((uri == null) || ("".equals(uri))) && 
/* 333 */       (prefix != null) && (!"".equals("prefix"))) {
/* 334 */       uri = this.faultCodeElement.getNamespaceURI(prefix);
/*     */     }
/*     */ 
/* 338 */     if ((uri == null) || ("".equals(uri))) {
/* 339 */       if ((prefix != null) && (!"".equals(prefix)))
/*     */       {
/* 341 */         log.log(Level.SEVERE, "SAAJ0307.impl.no.ns.URI", new Object[] { prefix + ":" + faultCode });
/* 342 */         throw new SOAPExceptionImpl("Empty/Null NamespaceURI specified for faultCode \"" + prefix + ":" + faultCode + "\"");
/*     */       }
/* 344 */       uri = "";
/*     */     }
/*     */ 
/* 348 */     checkIfStandardFaultCode(faultCode, uri);
/* 349 */     ((FaultElementImpl)this.faultCodeElement).ensureNamespaceIsDeclared(prefix, uri);
/*     */ 
/* 351 */     if ((prefix == null) || ("".equals(prefix)))
/* 352 */       finallySetFaultCode(faultCode);
/*     */     else
/* 354 */       finallySetFaultCode(prefix + ":" + faultCode);
/*     */   }
/*     */ 
/*     */   private boolean standardFaultCode(String faultCode)
/*     */   {
/* 359 */     if ((faultCode.equals("VersionMismatch")) || (faultCode.equals("MustUnderstand")) || (faultCode.equals("Client")) || (faultCode.equals("Server")))
/*     */     {
/* 361 */       return true;
/*     */     }
/* 363 */     if ((faultCode.startsWith("VersionMismatch.")) || (faultCode.startsWith("MustUnderstand.")) || (faultCode.startsWith("Client.")) || (faultCode.startsWith("Server.")))
/*     */     {
/* 365 */       return true;
/*     */     }
/* 367 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFaultActor(String faultActor) throws SOAPException {
/* 371 */     if (this.faultActorElement == null)
/* 372 */       findFaultActorElement();
/* 373 */     if (this.faultActorElement != null)
/* 374 */       this.faultActorElement.detachNode();
/* 375 */     if (faultActor == null)
/* 376 */       return;
/* 377 */     this.faultActorElement = createSOAPFaultElement(getFaultActorName());
/*     */ 
/* 379 */     this.faultActorElement.addTextNode(faultActor);
/* 380 */     if (hasDetail()) {
/* 381 */       insertBefore(this.faultActorElement, this.detail);
/* 382 */       return;
/*     */     }
/* 384 */     addNode(this.faultActorElement);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.Fault1_1Impl
 * JD-Core Version:    0.6.2
 */