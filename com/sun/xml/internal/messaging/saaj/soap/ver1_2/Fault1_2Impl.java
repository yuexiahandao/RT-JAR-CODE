/*     */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPConstants;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFaultElement;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class Fault1_2Impl extends FaultImpl
/*     */ {
/*  49 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.ver1_2", "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");
/*     */ 
/*  54 */   private static final QName textName = new QName("http://www.w3.org/2003/05/soap-envelope", "Text");
/*     */ 
/*  56 */   private final QName valueName = new QName("http://www.w3.org/2003/05/soap-envelope", "Value", getPrefix());
/*     */ 
/*  58 */   private final QName subcodeName = new QName("http://www.w3.org/2003/05/soap-envelope", "Subcode", getPrefix());
/*     */ 
/*  61 */   private SOAPElement innermostSubCodeElement = null;
/*     */ 
/*     */   public Fault1_2Impl(SOAPDocumentImpl ownerDoc, String name, String prefix) {
/*  64 */     super(ownerDoc, NameImpl.createFault1_2Name(name, prefix));
/*     */   }
/*     */ 
/*     */   public Fault1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
/*  68 */     super(ownerDocument, NameImpl.createFault1_2Name(null, prefix));
/*     */   }
/*     */ 
/*     */   protected NameImpl getDetailName() {
/*  72 */     return NameImpl.createSOAP12Name("Detail", getPrefix());
/*     */   }
/*     */ 
/*     */   protected NameImpl getFaultCodeName() {
/*  76 */     return NameImpl.createSOAP12Name("Code", getPrefix());
/*     */   }
/*     */ 
/*     */   protected NameImpl getFaultStringName() {
/*  80 */     return getFaultReasonName();
/*     */   }
/*     */ 
/*     */   protected NameImpl getFaultActorName() {
/*  84 */     return getFaultRoleName();
/*     */   }
/*     */ 
/*     */   private NameImpl getFaultRoleName() {
/*  88 */     return NameImpl.createSOAP12Name("Role", getPrefix());
/*     */   }
/*     */ 
/*     */   private NameImpl getFaultReasonName() {
/*  92 */     return NameImpl.createSOAP12Name("Reason", getPrefix());
/*     */   }
/*     */ 
/*     */   private NameImpl getFaultReasonTextName() {
/*  96 */     return NameImpl.createSOAP12Name("Text", getPrefix());
/*     */   }
/*     */ 
/*     */   private NameImpl getFaultNodeName() {
/* 100 */     return NameImpl.createSOAP12Name("Node", getPrefix());
/*     */   }
/*     */ 
/*     */   private static NameImpl getXmlLangName() {
/* 104 */     return NameImpl.createXmlName("lang");
/*     */   }
/*     */ 
/*     */   protected DetailImpl createDetail() {
/* 108 */     return new Detail1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument());
/*     */   }
/*     */ 
/*     */   protected FaultElementImpl createSOAPFaultElement(String localName)
/*     */   {
/* 113 */     return new FaultElement1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), localName);
/*     */   }
/*     */ 
/*     */   protected void checkIfStandardFaultCode(String faultCode, String uri)
/*     */     throws SOAPException
/*     */   {
/* 120 */     QName qname = new QName(uri, faultCode);
/* 121 */     if ((SOAPConstants.SOAP_DATAENCODINGUNKNOWN_FAULT.equals(qname)) || (SOAPConstants.SOAP_MUSTUNDERSTAND_FAULT.equals(qname)) || (SOAPConstants.SOAP_RECEIVER_FAULT.equals(qname)) || (SOAPConstants.SOAP_SENDER_FAULT.equals(qname)) || (SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.equals(qname)))
/*     */     {
/* 126 */       return;
/* 127 */     }log.log(Level.SEVERE, "SAAJ0435.ver1_2.code.not.standard", qname);
/*     */ 
/* 131 */     throw new SOAPExceptionImpl(qname + " is not a standard Code value");
/*     */   }
/*     */ 
/*     */   protected void finallySetFaultCode(String faultcode) throws SOAPException {
/* 135 */     SOAPElement value = this.faultCodeElement.addChildElement(this.valueName);
/* 136 */     value.addTextNode(faultcode);
/*     */   }
/*     */ 
/*     */   private void findReasonElement() {
/* 140 */     findFaultStringElement();
/*     */   }
/*     */ 
/*     */   public Iterator getFaultReasonTexts() throws SOAPException
/*     */   {
/* 145 */     if (this.faultStringElement == null)
/* 146 */       findReasonElement();
/* 147 */     Iterator eachTextElement = this.faultStringElement.getChildElements(textName);
/*     */ 
/* 149 */     List texts = new ArrayList();
/* 150 */     while (eachTextElement.hasNext()) {
/* 151 */       SOAPElement textElement = (SOAPElement)eachTextElement.next();
/* 152 */       Locale thisLocale = getLocale(textElement);
/* 153 */       if (thisLocale == null) {
/* 154 */         log.severe("SAAJ0431.ver1_2.xml.lang.missing");
/* 155 */         throw new SOAPExceptionImpl("\"xml:lang\" attribute is not present on the Text element");
/*     */       }
/* 157 */       texts.add(textElement.getValue());
/*     */     }
/* 159 */     if (texts.isEmpty()) {
/* 160 */       log.severe("SAAJ0434.ver1_2.text.element.not.present");
/* 161 */       throw new SOAPExceptionImpl("env:Text must be present inside env:Reason");
/*     */     }
/* 163 */     return texts.iterator();
/*     */   }
/*     */ 
/*     */   public void addFaultReasonText(String text, Locale locale)
/*     */     throws SOAPException
/*     */   {
/* 169 */     if (locale == null) {
/* 170 */       log.severe("SAAJ0430.ver1_2.locale.required");
/* 171 */       throw new SOAPException("locale is required and must not be null");
/*     */     }
/*     */ 
/* 175 */     if (this.faultStringElement == null)
/* 176 */       findReasonElement();
/*     */     SOAPElement reasonText;
/*     */     SOAPElement reasonText;
/* 179 */     if (this.faultStringElement == null) {
/* 180 */       this.faultStringElement = addSOAPFaultElement("Reason");
/* 181 */       reasonText = this.faultStringElement.addChildElement(getFaultReasonTextName());
/*     */     }
/*     */     else
/*     */     {
/* 185 */       removeDefaultFaultString();
/* 186 */       reasonText = getFaultReasonTextElement(locale);
/* 187 */       if (reasonText != null)
/* 188 */         reasonText.removeContents();
/*     */       else {
/* 190 */         reasonText = this.faultStringElement.addChildElement(getFaultReasonTextName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 196 */     String xmlLang = localeToXmlLang(locale);
/* 197 */     reasonText.addAttribute(getXmlLangName(), xmlLang);
/* 198 */     reasonText.addTextNode(text);
/*     */   }
/*     */ 
/*     */   private void removeDefaultFaultString() throws SOAPException {
/* 202 */     SOAPElement reasonText = getFaultReasonTextElement(Locale.getDefault());
/* 203 */     if (reasonText != null) {
/* 204 */       String defaultFaultString = "Fault string, and possibly fault code, not set";
/*     */ 
/* 206 */       if (defaultFaultString.equals(reasonText.getValue()))
/* 207 */         reasonText.detachNode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getFaultReasonText(Locale locale)
/*     */     throws SOAPException
/*     */   {
/* 214 */     if (locale == null) {
/* 215 */       return null;
/*     */     }
/*     */ 
/* 218 */     if (this.faultStringElement == null) {
/* 219 */       findReasonElement();
/*     */     }
/* 221 */     if (this.faultStringElement != null) {
/* 222 */       SOAPElement textElement = getFaultReasonTextElement(locale);
/* 223 */       if (textElement != null) {
/* 224 */         textElement.normalize();
/* 225 */         return textElement.getFirstChild().getNodeValue();
/*     */       }
/*     */     }
/*     */ 
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator getFaultReasonLocales() throws SOAPException
/*     */   {
/* 234 */     if (this.faultStringElement == null)
/* 235 */       findReasonElement();
/* 236 */     Iterator eachTextElement = this.faultStringElement.getChildElements(textName);
/*     */ 
/* 238 */     List localeSet = new ArrayList();
/* 239 */     while (eachTextElement.hasNext()) {
/* 240 */       SOAPElement textElement = (SOAPElement)eachTextElement.next();
/* 241 */       Locale thisLocale = getLocale(textElement);
/* 242 */       if (thisLocale == null) {
/* 243 */         log.severe("SAAJ0431.ver1_2.xml.lang.missing");
/* 244 */         throw new SOAPExceptionImpl("\"xml:lang\" attribute is not present on the Text element");
/*     */       }
/* 246 */       localeSet.add(thisLocale);
/*     */     }
/* 248 */     if (localeSet.isEmpty()) {
/* 249 */       log.severe("SAAJ0434.ver1_2.text.element.not.present");
/* 250 */       throw new SOAPExceptionImpl("env:Text elements with mandatory xml:lang attributes must be present inside env:Reason");
/*     */     }
/* 252 */     return localeSet.iterator();
/*     */   }
/*     */ 
/*     */   public Locale getFaultStringLocale() {
/* 256 */     Locale locale = null;
/*     */     try {
/* 258 */       locale = (Locale)getFaultReasonLocales().next(); } catch (SOAPException e) {
/*     */     }
/* 260 */     return locale;
/*     */   }
/*     */ 
/*     */   private SOAPElement getFaultReasonTextElement(Locale locale)
/*     */     throws SOAPException
/*     */   {
/* 270 */     Iterator eachTextElement = this.faultStringElement.getChildElements(textName);
/*     */ 
/* 272 */     while (eachTextElement.hasNext()) {
/* 273 */       SOAPElement textElement = (SOAPElement)eachTextElement.next();
/* 274 */       Locale thisLocale = getLocale(textElement);
/* 275 */       if (thisLocale == null) {
/* 276 */         log.severe("SAAJ0431.ver1_2.xml.lang.missing");
/* 277 */         throw new SOAPExceptionImpl("\"xml:lang\" attribute is not present on the Text element");
/*     */       }
/* 279 */       if (thisLocale.equals(locale)) {
/* 280 */         return textElement;
/*     */       }
/*     */     }
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */   public String getFaultNode() {
/* 287 */     SOAPElement faultNode = findChild(getFaultNodeName());
/* 288 */     if (faultNode == null) {
/* 289 */       return null;
/*     */     }
/* 291 */     return faultNode.getValue();
/*     */   }
/*     */ 
/*     */   public void setFaultNode(String uri) throws SOAPException {
/* 295 */     SOAPElement faultNode = findChild(getFaultNodeName());
/* 296 */     if (faultNode != null) {
/* 297 */       faultNode.detachNode();
/*     */     }
/* 299 */     faultNode = createSOAPFaultElement(getFaultNodeName());
/* 300 */     faultNode = faultNode.addTextNode(uri);
/* 301 */     if (getFaultRole() != null) {
/* 302 */       insertBefore(faultNode, this.faultActorElement);
/* 303 */       return;
/*     */     }
/* 305 */     if (hasDetail()) {
/* 306 */       insertBefore(faultNode, this.detail);
/* 307 */       return;
/*     */     }
/* 309 */     addNode(faultNode);
/*     */   }
/*     */ 
/*     */   public String getFaultRole() {
/* 313 */     return getFaultActor();
/*     */   }
/*     */ 
/*     */   public void setFaultRole(String uri) throws SOAPException {
/* 317 */     if (this.faultActorElement == null)
/* 318 */       findFaultActorElement();
/* 319 */     if (this.faultActorElement != null)
/* 320 */       this.faultActorElement.detachNode();
/* 321 */     this.faultActorElement = createSOAPFaultElement(getFaultActorName());
/*     */ 
/* 323 */     this.faultActorElement.addTextNode(uri);
/* 324 */     if (hasDetail()) {
/* 325 */       insertBefore(this.faultActorElement, this.detail);
/* 326 */       return;
/*     */     }
/* 328 */     addNode(this.faultActorElement);
/*     */   }
/*     */ 
/*     */   public String getFaultCode() {
/* 332 */     if (this.faultCodeElement == null)
/* 333 */       findFaultCodeElement();
/* 334 */     Iterator codeValues = this.faultCodeElement.getChildElements(this.valueName);
/*     */ 
/* 336 */     return ((SOAPElement)codeValues.next()).getValue();
/*     */   }
/*     */ 
/*     */   public QName getFaultCodeAsQName() {
/* 340 */     String faultcode = getFaultCode();
/* 341 */     if (faultcode == null) {
/* 342 */       return null;
/*     */     }
/* 344 */     if (this.faultCodeElement == null)
/* 345 */       findFaultCodeElement();
/* 346 */     Iterator valueElements = this.faultCodeElement.getChildElements(this.valueName);
/*     */ 
/* 348 */     return convertCodeToQName(faultcode, (SOAPElement)valueElements.next());
/*     */   }
/*     */ 
/*     */   public Name getFaultCodeAsName()
/*     */   {
/* 354 */     String faultcode = getFaultCode();
/* 355 */     if (faultcode == null) {
/* 356 */       return null;
/*     */     }
/* 358 */     if (this.faultCodeElement == null)
/* 359 */       findFaultCodeElement();
/* 360 */     Iterator valueElements = this.faultCodeElement.getChildElements(this.valueName);
/*     */ 
/* 362 */     return NameImpl.convertToName(convertCodeToQName(faultcode, (SOAPElement)valueElements.next()));
/*     */   }
/*     */ 
/*     */   public String getFaultString()
/*     */   {
/* 369 */     String reason = null;
/*     */     try
/*     */     {
/* 373 */       reason = (String)getFaultReasonTexts().next(); } catch (SOAPException e) {
/*     */     }
/* 375 */     return reason;
/*     */   }
/*     */ 
/*     */   public void setFaultString(String faultString) throws SOAPException {
/* 379 */     addFaultReasonText(faultString, Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public void setFaultString(String faultString, Locale locale)
/*     */     throws SOAPException
/*     */   {
/* 386 */     addFaultReasonText(faultString, locale);
/*     */   }
/*     */ 
/*     */   public void appendFaultSubcode(QName subcode) throws SOAPException {
/* 390 */     if (subcode == null) {
/* 391 */       return;
/*     */     }
/* 393 */     if ((subcode.getNamespaceURI() == null) || ("".equals(subcode.getNamespaceURI())))
/*     */     {
/* 396 */       log.severe("SAAJ0432.ver1_2.subcode.not.ns.qualified");
/* 397 */       throw new SOAPExceptionImpl("A Subcode must be namespace-qualified");
/*     */     }
/* 399 */     if (this.innermostSubCodeElement == null) {
/* 400 */       if (this.faultCodeElement == null)
/* 401 */         findFaultCodeElement();
/* 402 */       this.innermostSubCodeElement = this.faultCodeElement;
/*     */     }
/* 404 */     String prefix = null;
/* 405 */     if ((subcode.getPrefix() == null) || ("".equals(subcode.getPrefix()))) {
/* 406 */       prefix = ((ElementImpl)this.innermostSubCodeElement).getNamespacePrefix(subcode.getNamespaceURI());
/*     */     }
/*     */     else
/*     */     {
/* 410 */       prefix = subcode.getPrefix();
/* 411 */     }if ((prefix == null) || ("".equals(prefix))) {
/* 412 */       prefix = "ns1";
/*     */     }
/* 414 */     this.innermostSubCodeElement = this.innermostSubCodeElement.addChildElement(this.subcodeName);
/*     */ 
/* 416 */     SOAPElement subcodeValueElement = this.innermostSubCodeElement.addChildElement(this.valueName);
/*     */ 
/* 418 */     ((ElementImpl)subcodeValueElement).ensureNamespaceIsDeclared(prefix, subcode.getNamespaceURI());
/*     */ 
/* 421 */     subcodeValueElement.addTextNode(prefix + ":" + subcode.getLocalPart());
/*     */   }
/*     */ 
/*     */   public void removeAllFaultSubcodes() {
/* 425 */     if (this.faultCodeElement == null)
/* 426 */       findFaultCodeElement();
/* 427 */     Iterator subcodeElements = this.faultCodeElement.getChildElements(this.subcodeName);
/*     */ 
/* 429 */     if (subcodeElements.hasNext()) {
/* 430 */       SOAPElement subcode = (SOAPElement)subcodeElements.next();
/* 431 */       subcode.detachNode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator getFaultSubcodes() {
/* 436 */     if (this.faultCodeElement == null)
/* 437 */       findFaultCodeElement();
/* 438 */     final List subcodeList = new ArrayList();
/* 439 */     SOAPElement currentCodeElement = this.faultCodeElement;
/* 440 */     Iterator subcodeElements = currentCodeElement.getChildElements(this.subcodeName);
/*     */ 
/* 442 */     while (subcodeElements.hasNext()) {
/* 443 */       currentCodeElement = (ElementImpl)subcodeElements.next();
/* 444 */       Iterator valueElements = currentCodeElement.getChildElements(this.valueName);
/*     */ 
/* 446 */       SOAPElement valueElement = (SOAPElement)valueElements.next();
/* 447 */       String code = valueElement.getValue();
/* 448 */       subcodeList.add(convertCodeToQName(code, valueElement));
/* 449 */       subcodeElements = currentCodeElement.getChildElements(this.subcodeName);
/*     */     }
/*     */ 
/* 452 */     return new Iterator() {
/* 453 */       Iterator subCodeIter = subcodeList.iterator();
/*     */ 
/*     */       public boolean hasNext() {
/* 456 */         return this.subCodeIter.hasNext();
/*     */       }
/*     */ 
/*     */       public Object next() {
/* 460 */         return this.subCodeIter.next();
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 464 */         throw new UnsupportedOperationException("Method remove() not supported on SubCodes Iterator");
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static Locale getLocale(SOAPElement reasonText)
/*     */   {
/* 471 */     return xmlLangToLocale(reasonText.getAttributeValue(getXmlLangName()));
/*     */   }
/*     */ 
/*     */   public void setEncodingStyle(String encodingStyle)
/*     */     throws SOAPException
/*     */   {
/* 479 */     log.severe("SAAJ0407.ver1_2.no.encodingStyle.in.fault");
/* 480 */     throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Fault");
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(Name name, String value) throws SOAPException
/*     */   {
/* 485 */     if ((name.getLocalName().equals("encodingStyle")) && (name.getURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/* 487 */       setEncodingStyle(value);
/*     */     }
/* 489 */     return super.addAttribute(name, value);
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(QName name, String value) throws SOAPException
/*     */   {
/* 494 */     if ((name.getLocalPart().equals("encodingStyle")) && (name.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/* 496 */       setEncodingStyle(value);
/*     */     }
/* 498 */     return super.addAttribute(name, value);
/*     */   }
/*     */ 
/*     */   public SOAPElement addTextNode(String text) throws SOAPException {
/* 502 */     log.log(Level.SEVERE, "SAAJ0416.ver1_2.adding.text.not.legal", getElementQName());
/*     */ 
/* 506 */     throw new SOAPExceptionImpl("Adding text to SOAP 1.2 Fault is not legal");
/*     */   }
/*     */ 
/*     */   public SOAPElement addChildElement(SOAPElement element) throws SOAPException
/*     */   {
/* 511 */     String localName = element.getLocalName();
/* 512 */     if ("Detail".equalsIgnoreCase(localName)) {
/* 513 */       if (hasDetail()) {
/* 514 */         log.severe("SAAJ0436.ver1_2.detail.exists.error");
/* 515 */         throw new SOAPExceptionImpl("Cannot add Detail, Detail already exists");
/*     */       }
/* 517 */       String uri = element.getElementQName().getNamespaceURI();
/* 518 */       if (!uri.equals("http://www.w3.org/2003/05/soap-envelope")) {
/* 519 */         log.severe("SAAJ0437.ver1_2.version.mismatch.error");
/* 520 */         throw new SOAPExceptionImpl("Cannot add Detail, Incorrect SOAP version specified for Detail element");
/*     */       }
/*     */     }
/* 523 */     if ((element instanceof Detail1_2Impl)) {
/* 524 */       ElementImpl importedElement = (ElementImpl)importElement(element);
/* 525 */       addNode(importedElement);
/* 526 */       return convertToSoapElement(importedElement);
/*     */     }
/* 528 */     return super.addChildElement(element);
/*     */   }
/*     */ 
/*     */   protected boolean isStandardFaultElement(String localName) {
/* 532 */     if ((localName.equalsIgnoreCase("code")) || (localName.equalsIgnoreCase("reason")) || (localName.equalsIgnoreCase("node")) || (localName.equalsIgnoreCase("role")) || (localName.equalsIgnoreCase("detail")))
/*     */     {
/* 537 */       return true;
/*     */     }
/* 539 */     return false;
/*     */   }
/*     */ 
/*     */   protected QName getDefaultFaultCode() {
/* 543 */     return SOAPConstants.SOAP_SENDER_FAULT;
/*     */   }
/*     */ 
/*     */   protected FaultElementImpl createSOAPFaultElement(QName qname) {
/* 547 */     return new FaultElement1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), qname);
/*     */   }
/*     */ 
/*     */   protected FaultElementImpl createSOAPFaultElement(Name qname)
/*     */   {
/* 553 */     return new FaultElement1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), (NameImpl)qname);
/*     */   }
/*     */ 
/*     */   public void setFaultActor(String faultActor)
/*     */     throws SOAPException
/*     */   {
/* 559 */     setFaultRole(faultActor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.Fault1_2Impl
 * JD-Core Version:    0.6.2
 */