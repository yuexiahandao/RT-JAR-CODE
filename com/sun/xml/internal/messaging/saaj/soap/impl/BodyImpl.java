/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPBody;
/*     */ import javax.xml.soap.SOAPBodyElement;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class BodyImpl extends ElementImpl
/*     */   implements SOAPBody
/*     */ {
/*     */   private SOAPFault fault;
/*     */ 
/*     */   protected BodyImpl(SOAPDocumentImpl ownerDoc, NameImpl bodyName)
/*     */   {
/*  53 */     super(ownerDoc, bodyName); } 
/*     */   protected abstract NameImpl getFaultName(String paramString);
/*     */ 
/*     */   protected abstract boolean isFault(SOAPElement paramSOAPElement);
/*     */ 
/*     */   protected abstract SOAPBodyElement createBodyElement(Name paramName);
/*     */ 
/*     */   protected abstract SOAPBodyElement createBodyElement(QName paramQName);
/*     */ 
/*     */   protected abstract SOAPFault createFaultElement();
/*     */ 
/*     */   protected abstract QName getDefaultFaultCode();
/*     */ 
/*  64 */   public SOAPFault addFault() throws SOAPException { if (hasFault()) {
/*  65 */       log.severe("SAAJ0110.impl.fault.already.exists");
/*  66 */       throw new SOAPExceptionImpl("Error: Fault already exists");
/*     */     }
/*     */ 
/*  69 */     this.fault = createFaultElement();
/*     */ 
/*  71 */     addNode(this.fault);
/*     */ 
/*  73 */     this.fault.setFaultCode(getDefaultFaultCode());
/*  74 */     this.fault.setFaultString("Fault string, and possibly fault code, not set");
/*     */ 
/*  76 */     return this.fault;
/*     */   }
/*     */ 
/*     */   public SOAPFault addFault(Name faultCode, String faultString, Locale locale)
/*     */     throws SOAPException
/*     */   {
/*  85 */     SOAPFault fault = addFault();
/*  86 */     fault.setFaultCode(faultCode);
/*  87 */     fault.setFaultString(faultString, locale);
/*  88 */     return fault;
/*     */   }
/*     */ 
/*     */   public SOAPFault addFault(QName faultCode, String faultString, Locale locale)
/*     */     throws SOAPException
/*     */   {
/*  97 */     SOAPFault fault = addFault();
/*  98 */     fault.setFaultCode(faultCode);
/*  99 */     fault.setFaultString(faultString, locale);
/* 100 */     return fault;
/*     */   }
/*     */ 
/*     */   public SOAPFault addFault(Name faultCode, String faultString)
/*     */     throws SOAPException
/*     */   {
/* 106 */     SOAPFault fault = addFault();
/* 107 */     fault.setFaultCode(faultCode);
/* 108 */     fault.setFaultString(faultString);
/* 109 */     return fault;
/*     */   }
/*     */ 
/*     */   public SOAPFault addFault(QName faultCode, String faultString)
/*     */     throws SOAPException
/*     */   {
/* 115 */     SOAPFault fault = addFault();
/* 116 */     fault.setFaultCode(faultCode);
/* 117 */     fault.setFaultString(faultString);
/* 118 */     return fault;
/*     */   }
/*     */ 
/*     */   void initializeFault() {
/* 122 */     FaultImpl flt = (FaultImpl)findFault();
/* 123 */     this.fault = flt;
/*     */   }
/*     */ 
/*     */   protected SOAPElement findFault() {
/* 127 */     Iterator eachChild = getChildElementNodes();
/* 128 */     while (eachChild.hasNext()) {
/* 129 */       SOAPElement child = (SOAPElement)eachChild.next();
/* 130 */       if (isFault(child)) {
/* 131 */         return child;
/*     */       }
/*     */     }
/*     */ 
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasFault() {
/* 139 */     initializeFault();
/* 140 */     return this.fault != null;
/*     */   }
/*     */ 
/*     */   public SOAPFault getFault() {
/* 144 */     if (hasFault())
/* 145 */       return this.fault;
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */   public SOAPBodyElement addBodyElement(Name name) throws SOAPException {
/* 150 */     SOAPBodyElement newBodyElement = (SOAPBodyElement)ElementFactory.createNamedElement(((SOAPDocument)getOwnerDocument()).getDocument(), name.getLocalName(), name.getPrefix(), name.getURI());
/*     */ 
/* 156 */     if (newBodyElement == null) {
/* 157 */       newBodyElement = createBodyElement(name);
/*     */     }
/* 159 */     addNode(newBodyElement);
/* 160 */     return newBodyElement;
/*     */   }
/*     */ 
/*     */   public SOAPBodyElement addBodyElement(QName qname) throws SOAPException {
/* 164 */     SOAPBodyElement newBodyElement = (SOAPBodyElement)ElementFactory.createNamedElement(((SOAPDocument)getOwnerDocument()).getDocument(), qname.getLocalPart(), qname.getPrefix(), qname.getNamespaceURI());
/*     */ 
/* 170 */     if (newBodyElement == null) {
/* 171 */       newBodyElement = createBodyElement(qname);
/*     */     }
/* 173 */     addNode(newBodyElement);
/* 174 */     return newBodyElement;
/*     */   }
/*     */ 
/*     */   public void setParentElement(SOAPElement element) throws SOAPException
/*     */   {
/* 179 */     if (!(element instanceof SOAPEnvelope)) {
/* 180 */       log.severe("SAAJ0111.impl.body.parent.must.be.envelope");
/* 181 */       throw new SOAPException("Parent of SOAPBody has to be a SOAPEnvelope");
/*     */     }
/* 183 */     super.setParentElement(element);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(Name name) throws SOAPException {
/* 187 */     return addBodyElement(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(QName name) throws SOAPException {
/* 191 */     return addBodyElement(name);
/*     */   }
/*     */ 
/*     */   public SOAPBodyElement addDocument(Document document)
/*     */     throws SOAPException
/*     */   {
/* 222 */     SOAPBodyElement newBodyElement = null;
/* 223 */     DocumentFragment docFrag = document.createDocumentFragment();
/* 224 */     Element rootElement = document.getDocumentElement();
/* 225 */     if (rootElement != null) {
/* 226 */       docFrag.appendChild(rootElement);
/*     */ 
/* 228 */       Document ownerDoc = getOwnerDocument();
/*     */ 
/* 231 */       org.w3c.dom.Node replacingNode = ownerDoc.importNode(docFrag, true);
/*     */ 
/* 233 */       addNode(replacingNode);
/* 234 */       Iterator i = getChildElements(NameImpl.copyElementName(rootElement));
/*     */ 
/* 238 */       while (i.hasNext())
/* 239 */         newBodyElement = (SOAPBodyElement)i.next();
/*     */     }
/* 241 */     return newBodyElement;
/*     */   }
/*     */ 
/*     */   protected SOAPElement convertToSoapElement(Element element)
/*     */   {
/* 246 */     if (((element instanceof SOAPBodyElement)) && (!element.getClass().equals(ElementImpl.class)))
/*     */     {
/* 250 */       return (SOAPElement)element;
/*     */     }
/* 252 */     return replaceElementWithSOAPElement(element, (ElementImpl)createBodyElement(NameImpl.copyElementName(element)));
/*     */   }
/*     */ 
/*     */   public SOAPElement setElementQName(QName newName)
/*     */     throws SOAPException
/*     */   {
/* 260 */     log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[] { this.elementQName.getLocalPart(), newName.getLocalPart() });
/*     */ 
/* 264 */     throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
/*     */   }
/*     */ 
/*     */   public Document extractContentAsDocument()
/*     */     throws SOAPException
/*     */   {
/* 271 */     Iterator eachChild = getChildElements();
/* 272 */     javax.xml.soap.Node firstBodyElement = null;
/*     */ 
/* 274 */     while ((eachChild.hasNext()) && (!(firstBodyElement instanceof SOAPElement)))
/*     */     {
/* 276 */       firstBodyElement = (javax.xml.soap.Node)eachChild.next();
/*     */     }
/* 278 */     boolean exactlyOneChildElement = true;
/* 279 */     if (firstBodyElement == null)
/* 280 */       exactlyOneChildElement = false;
/*     */     else {
/* 282 */       for (org.w3c.dom.Node node = firstBodyElement.getNextSibling(); 
/* 283 */         node != null; 
/* 284 */         node = node.getNextSibling())
/*     */       {
/* 286 */         if ((node instanceof Element)) {
/* 287 */           exactlyOneChildElement = false;
/* 288 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 293 */     if (!exactlyOneChildElement) {
/* 294 */       log.log(Level.SEVERE, "SAAJ0250.impl.body.should.have.exactly.one.child");
/*     */ 
/* 296 */       throw new SOAPException("Cannot extract Document from body");
/*     */     }
/*     */ 
/* 299 */     Document document = null;
/*     */     try {
/* 301 */       DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
/*     */ 
/* 303 */       factory.setNamespaceAware(true);
/* 304 */       DocumentBuilder builder = factory.newDocumentBuilder();
/* 305 */       document = builder.newDocument();
/*     */ 
/* 307 */       Element rootElement = (Element)document.importNode(firstBodyElement, true);
/*     */ 
/* 311 */       document.appendChild(rootElement);
/*     */     }
/*     */     catch (Exception e) {
/* 314 */       log.log(Level.SEVERE, "SAAJ0251.impl.cannot.extract.document.from.body");
/*     */ 
/* 316 */       throw new SOAPExceptionImpl("Unable to extract Document from body", e);
/*     */     }
/*     */ 
/* 320 */     firstBodyElement.detachNode();
/*     */ 
/* 322 */     return document;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl
 * JD-Core Version:    0.6.2
 */