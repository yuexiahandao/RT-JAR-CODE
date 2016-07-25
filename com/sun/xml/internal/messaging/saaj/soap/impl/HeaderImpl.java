/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPHeaderElement;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class HeaderImpl extends ElementImpl
/*     */   implements SOAPHeader
/*     */ {
/*     */   protected static final boolean MUST_UNDERSTAND_ONLY = false;
/*     */ 
/*     */   protected HeaderImpl(SOAPDocumentImpl ownerDoc, NameImpl name)
/*     */   {
/*  45 */     super(ownerDoc, name);
/*     */   }
/*     */   protected abstract SOAPHeaderElement createHeaderElement(Name paramName) throws SOAPException;
/*     */ 
/*     */   protected abstract SOAPHeaderElement createHeaderElement(QName paramQName) throws SOAPException;
/*     */ 
/*     */   protected abstract NameImpl getNotUnderstoodName();
/*     */ 
/*     */   protected abstract NameImpl getUpgradeName();
/*     */ 
/*     */   protected abstract NameImpl getSupportedEnvelopeName();
/*     */ 
/*  57 */   public SOAPHeaderElement addHeaderElement(Name name) throws SOAPException { SOAPElement newHeaderElement = ElementFactory.createNamedElement(((SOAPDocument)getOwnerDocument()).getDocument(), name.getLocalName(), name.getPrefix(), name.getURI());
/*     */ 
/*  63 */     if ((newHeaderElement == null) || (!(newHeaderElement instanceof SOAPHeaderElement)))
/*     */     {
/*  65 */       newHeaderElement = createHeaderElement(name);
/*     */     }
/*     */ 
/*  70 */     String uri = newHeaderElement.getElementQName().getNamespaceURI();
/*  71 */     if ((uri == null) || ("".equals(uri))) {
/*  72 */       log.severe("SAAJ0131.impl.header.elems.ns.qualified");
/*  73 */       throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
/*     */     }
/*  75 */     addNode(newHeaderElement);
/*  76 */     return (SOAPHeaderElement)newHeaderElement; }
/*     */ 
/*     */   public SOAPHeaderElement addHeaderElement(QName name) throws SOAPException
/*     */   {
/*  80 */     SOAPElement newHeaderElement = ElementFactory.createNamedElement(((SOAPDocument)getOwnerDocument()).getDocument(), name.getLocalPart(), name.getPrefix(), name.getNamespaceURI());
/*     */ 
/*  86 */     if ((newHeaderElement == null) || (!(newHeaderElement instanceof SOAPHeaderElement)))
/*     */     {
/*  88 */       newHeaderElement = createHeaderElement(name);
/*     */     }
/*     */ 
/*  93 */     String uri = newHeaderElement.getElementQName().getNamespaceURI();
/*  94 */     if ((uri == null) || ("".equals(uri))) {
/*  95 */       log.severe("SAAJ0131.impl.header.elems.ns.qualified");
/*  96 */       throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
/*     */     }
/*  98 */     addNode(newHeaderElement);
/*  99 */     return (SOAPHeaderElement)newHeaderElement;
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(Name name) throws SOAPException {
/* 103 */     return addHeaderElement(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(QName name) throws SOAPException {
/* 107 */     return addHeaderElement(name);
/*     */   }
/*     */ 
/*     */   public Iterator examineHeaderElements(String actor) {
/* 111 */     return getHeaderElementsForActor(actor, false, false);
/*     */   }
/*     */ 
/*     */   public Iterator extractHeaderElements(String actor) {
/* 115 */     return getHeaderElementsForActor(actor, true, false);
/*     */   }
/*     */ 
/*     */   protected Iterator getHeaderElementsForActor(String actor, boolean detach, boolean mustUnderstand)
/*     */   {
/* 122 */     if ((actor == null) || (actor.equals(""))) {
/* 123 */       log.severe("SAAJ0132.impl.invalid.value.for.actor.or.role");
/* 124 */       throw new IllegalArgumentException("Invalid value for actor or role");
/*     */     }
/* 126 */     return getHeaderElements(actor, detach, mustUnderstand);
/*     */   }
/*     */ 
/*     */   protected Iterator getHeaderElements(String actor, boolean detach, boolean mustUnderstand)
/*     */   {
/* 133 */     List elementList = new ArrayList();
/*     */ 
/* 135 */     Iterator eachChild = getChildElements();
/*     */ 
/* 137 */     Object currentChild = iterate(eachChild);
/* 138 */     while (currentChild != null) {
/* 139 */       if (!(currentChild instanceof SOAPHeaderElement)) {
/* 140 */         currentChild = iterate(eachChild);
/*     */       } else {
/* 142 */         HeaderElementImpl currentElement = (HeaderElementImpl)currentChild;
/*     */ 
/* 144 */         currentChild = iterate(eachChild);
/*     */ 
/* 146 */         boolean isMustUnderstandMatching = (!mustUnderstand) || (currentElement.getMustUnderstand());
/*     */ 
/* 148 */         boolean doAdd = false;
/* 149 */         if ((actor == null) && (isMustUnderstandMatching)) {
/* 150 */           doAdd = true;
/*     */         } else {
/* 152 */           String currentActor = currentElement.getActorOrRole();
/* 153 */           if (currentActor == null) {
/* 154 */             currentActor = "";
/*     */           }
/*     */ 
/* 157 */           if ((currentActor.equalsIgnoreCase(actor)) && (isMustUnderstandMatching))
/*     */           {
/* 159 */             doAdd = true;
/*     */           }
/*     */         }
/*     */ 
/* 163 */         if (doAdd) {
/* 164 */           elementList.add(currentElement);
/* 165 */           if (detach) {
/* 166 */             currentElement.detachNode();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 172 */     return elementList.listIterator();
/*     */   }
/*     */ 
/*     */   private Object iterate(Iterator each) {
/* 176 */     return each.hasNext() ? each.next() : null;
/*     */   }
/*     */ 
/*     */   public void setParentElement(SOAPElement element) throws SOAPException {
/* 180 */     if (!(element instanceof SOAPEnvelope)) {
/* 181 */       log.severe("SAAJ0133.impl.header.parent.mustbe.envelope");
/* 182 */       throw new SOAPException("Parent of SOAPHeader has to be a SOAPEnvelope");
/*     */     }
/* 184 */     super.setParentElement(element);
/*     */   }
/*     */ 
/*     */   public SOAPElement addChildElement(String localName)
/*     */     throws SOAPException
/*     */   {
/* 193 */     SOAPElement element = super.addChildElement(localName);
/*     */ 
/* 195 */     String uri = element.getElementName().getURI();
/* 196 */     if ((uri == null) || ("".equals(uri))) {
/* 197 */       log.severe("SAAJ0134.impl.header.elems.ns.qualified");
/* 198 */       throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
/*     */     }
/* 200 */     return element;
/*     */   }
/*     */ 
/*     */   public Iterator examineAllHeaderElements() {
/* 204 */     return getHeaderElements(null, false, false);
/*     */   }
/*     */ 
/*     */   public Iterator examineMustUnderstandHeaderElements(String actor) {
/* 208 */     return getHeaderElements(actor, false, true);
/*     */   }
/*     */ 
/*     */   public Iterator extractAllHeaderElements()
/*     */   {
/* 213 */     return getHeaderElements(null, true, false);
/*     */   }
/*     */ 
/*     */   public SOAPHeaderElement addUpgradeHeaderElement(Iterator supportedSoapUris) throws SOAPException
/*     */   {
/* 218 */     if (supportedSoapUris == null) {
/* 219 */       log.severe("SAAJ0411.ver1_2.no.null.supportedURIs");
/* 220 */       throw new SOAPException("Argument cannot be null; iterator of supportedURIs cannot be null");
/*     */     }
/* 222 */     if (!supportedSoapUris.hasNext()) {
/* 223 */       log.severe("SAAJ0412.ver1_2.no.empty.list.of.supportedURIs");
/* 224 */       throw new SOAPException("List of supported URIs cannot be empty");
/*     */     }
/* 226 */     Name upgradeName = getUpgradeName();
/* 227 */     SOAPHeaderElement upgradeHeaderElement = (SOAPHeaderElement)addChildElement(upgradeName);
/*     */ 
/* 229 */     Name supportedEnvelopeName = getSupportedEnvelopeName();
/* 230 */     int i = 0;
/* 231 */     while (supportedSoapUris.hasNext()) {
/* 232 */       SOAPElement subElement = upgradeHeaderElement.addChildElement(supportedEnvelopeName);
/*     */ 
/* 234 */       String ns = "ns" + Integer.toString(i);
/* 235 */       subElement.addAttribute(NameImpl.createFromUnqualifiedName("qname"), ns + ":Envelope");
/*     */ 
/* 238 */       subElement.addNamespaceDeclaration(ns, (String)supportedSoapUris.next());
/*     */ 
/* 241 */       i++;
/*     */     }
/* 243 */     return upgradeHeaderElement;
/*     */   }
/*     */ 
/*     */   public SOAPHeaderElement addUpgradeHeaderElement(String supportedSoapUri) throws SOAPException
/*     */   {
/* 248 */     return addUpgradeHeaderElement(new String[] { supportedSoapUri });
/*     */   }
/*     */ 
/*     */   public SOAPHeaderElement addUpgradeHeaderElement(String[] supportedSoapUris)
/*     */     throws SOAPException
/*     */   {
/* 254 */     if (supportedSoapUris == null) {
/* 255 */       log.severe("SAAJ0411.ver1_2.no.null.supportedURIs");
/* 256 */       throw new SOAPException("Argument cannot be null; array of supportedURIs cannot be null");
/*     */     }
/* 258 */     if (supportedSoapUris.length == 0) {
/* 259 */       log.severe("SAAJ0412.ver1_2.no.empty.list.of.supportedURIs");
/* 260 */       throw new SOAPException("List of supported URIs cannot be empty");
/*     */     }
/* 262 */     Name upgradeName = getUpgradeName();
/* 263 */     SOAPHeaderElement upgradeHeaderElement = (SOAPHeaderElement)addChildElement(upgradeName);
/*     */ 
/* 265 */     Name supportedEnvelopeName = getSupportedEnvelopeName();
/* 266 */     for (int i = 0; i < supportedSoapUris.length; i++) {
/* 267 */       SOAPElement subElement = upgradeHeaderElement.addChildElement(supportedEnvelopeName);
/*     */ 
/* 269 */       String ns = "ns" + Integer.toString(i);
/* 270 */       subElement.addAttribute(NameImpl.createFromUnqualifiedName("qname"), ns + ":Envelope");
/*     */ 
/* 273 */       subElement.addNamespaceDeclaration(ns, supportedSoapUris[i]);
/*     */     }
/* 275 */     return upgradeHeaderElement;
/*     */   }
/*     */ 
/*     */   protected SOAPElement convertToSoapElement(Element element) {
/* 279 */     if ((element instanceof SOAPHeaderElement))
/* 280 */       return (SOAPElement)element;
/*     */     SOAPHeaderElement headerElement;
/*     */     try
/*     */     {
/* 284 */       headerElement = createHeaderElement(NameImpl.copyElementName(element));
/*     */     }
/*     */     catch (SOAPException e) {
/* 287 */       throw new ClassCastException("Could not convert Element to SOAPHeaderElement: " + e.getMessage());
/*     */     }
/* 289 */     return replaceElementWithSOAPElement(element, (ElementImpl)headerElement);
/*     */   }
/*     */ 
/*     */   public SOAPElement setElementQName(QName newName)
/*     */     throws SOAPException
/*     */   {
/* 296 */     log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[] { this.elementQName.getLocalPart(), newName.getLocalPart() });
/*     */ 
/* 300 */     throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
 * JD-Core Version:    0.6.2
 */