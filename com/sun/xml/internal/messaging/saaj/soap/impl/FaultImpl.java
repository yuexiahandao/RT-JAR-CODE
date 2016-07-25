/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Detail;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.soap.SOAPFaultElement;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class FaultImpl extends ElementImpl
/*     */   implements SOAPFault
/*     */ {
/*     */   protected SOAPFaultElement faultStringElement;
/*     */   protected SOAPFaultElement faultActorElement;
/*     */   protected SOAPFaultElement faultCodeElement;
/*     */   protected Detail detail;
/*     */ 
/*     */   protected FaultImpl(SOAPDocumentImpl ownerDoc, NameImpl name)
/*     */   {
/*  53 */     super(ownerDoc, name); } 
/*     */   protected abstract NameImpl getDetailName();
/*     */ 
/*     */   protected abstract NameImpl getFaultCodeName();
/*     */ 
/*     */   protected abstract NameImpl getFaultStringName();
/*     */ 
/*     */   protected abstract NameImpl getFaultActorName();
/*     */ 
/*     */   protected abstract DetailImpl createDetail();
/*     */ 
/*     */   protected abstract FaultElementImpl createSOAPFaultElement(String paramString);
/*     */ 
/*     */   protected abstract FaultElementImpl createSOAPFaultElement(QName paramQName);
/*     */ 
/*     */   protected abstract FaultElementImpl createSOAPFaultElement(Name paramName);
/*     */ 
/*     */   protected abstract void checkIfStandardFaultCode(String paramString1, String paramString2) throws SOAPException;
/*     */ 
/*     */   protected abstract void finallySetFaultCode(String paramString) throws SOAPException;
/*     */ 
/*     */   protected abstract boolean isStandardFaultElement(String paramString);
/*     */ 
/*     */   protected abstract QName getDefaultFaultCode();
/*     */ 
/*  72 */   protected void findFaultCodeElement() { this.faultCodeElement = ((SOAPFaultElement)findChild(getFaultCodeName())); }
/*     */ 
/*     */ 
/*     */   protected void findFaultActorElement()
/*     */   {
/*  77 */     this.faultActorElement = ((SOAPFaultElement)findChild(getFaultActorName()));
/*     */   }
/*     */ 
/*     */   protected void findFaultStringElement()
/*     */   {
/*  82 */     this.faultStringElement = ((SOAPFaultElement)findChild(getFaultStringName()));
/*     */   }
/*     */ 
/*     */   public void setFaultCode(String faultCode) throws SOAPException
/*     */   {
/*  87 */     setFaultCode(NameImpl.getLocalNameFromTagName(faultCode), NameImpl.getPrefixFromTagName(faultCode), null);
/*     */   }
/*     */ 
/*     */   public void setFaultCode(String faultCode, String prefix, String uri)
/*     */     throws SOAPException
/*     */   {
/*  96 */     if (((prefix == null) || ("".equals(prefix))) && 
/*  97 */       (uri != null) && (!"".equals(uri))) {
/*  98 */       prefix = getNamespacePrefix(uri);
/*  99 */       if ((prefix == null) || ("".equals(prefix))) {
/* 100 */         prefix = "ns0";
/*     */       }
/*     */     }
/*     */ 
/* 104 */     if (this.faultCodeElement == null) {
/* 105 */       findFaultCodeElement();
/*     */     }
/* 107 */     if (this.faultCodeElement == null)
/* 108 */       this.faultCodeElement = addFaultCodeElement();
/*     */     else {
/* 110 */       this.faultCodeElement.removeContents();
/*     */     }
/* 112 */     if ((uri == null) || ("".equals(uri))) {
/* 113 */       uri = this.faultCodeElement.getNamespaceURI(prefix);
/*     */     }
/* 115 */     if ((uri == null) || ("".equals(uri))) {
/* 116 */       if ((prefix != null) && (!"".equals(prefix)))
/*     */       {
/* 118 */         log.log(Level.SEVERE, "SAAJ0140.impl.no.ns.URI", new Object[] { prefix + ":" + faultCode });
/* 119 */         throw new SOAPExceptionImpl("Empty/Null NamespaceURI specified for faultCode \"" + prefix + ":" + faultCode + "\"");
/*     */       }
/* 121 */       uri = "";
/*     */     }
/*     */ 
/* 124 */     checkIfStandardFaultCode(faultCode, uri);
/* 125 */     ((FaultElementImpl)this.faultCodeElement).ensureNamespaceIsDeclared(prefix, uri);
/*     */ 
/* 127 */     if ((prefix == null) || ("".equals(prefix)))
/* 128 */       finallySetFaultCode(faultCode);
/*     */     else
/* 130 */       finallySetFaultCode(prefix + ":" + faultCode);
/*     */   }
/*     */ 
/*     */   public void setFaultCode(Name faultCodeQName) throws SOAPException
/*     */   {
/* 135 */     setFaultCode(faultCodeQName.getLocalName(), faultCodeQName.getPrefix(), faultCodeQName.getURI());
/*     */   }
/*     */ 
/*     */   public void setFaultCode(QName faultCodeQName)
/*     */     throws SOAPException
/*     */   {
/* 142 */     setFaultCode(faultCodeQName.getLocalPart(), faultCodeQName.getPrefix(), faultCodeQName.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   protected static QName convertCodeToQName(String code, SOAPElement codeContainingElement)
/*     */   {
/* 152 */     int prefixIndex = code.indexOf(':');
/* 153 */     if (prefixIndex == -1) {
/* 154 */       return new QName(code);
/*     */     }
/*     */ 
/* 157 */     String prefix = code.substring(0, prefixIndex);
/* 158 */     String nsName = ((ElementImpl)codeContainingElement).lookupNamespaceURI(prefix);
/*     */ 
/* 160 */     return new QName(nsName, getLocalPart(code), prefix);
/*     */   }
/*     */ 
/*     */   protected void initializeDetail() {
/* 164 */     NameImpl detailName = getDetailName();
/* 165 */     this.detail = ((Detail)findChild(detailName));
/*     */   }
/*     */ 
/*     */   public Detail getDetail() {
/* 169 */     if (this.detail == null)
/* 170 */       initializeDetail();
/* 171 */     if ((this.detail != null) && (this.detail.getParentNode() == null))
/*     */     {
/* 173 */       this.detail = null;
/*     */     }
/* 175 */     return this.detail;
/*     */   }
/*     */ 
/*     */   public Detail addDetail() throws SOAPException {
/* 179 */     if (this.detail == null)
/* 180 */       initializeDetail();
/* 181 */     if (this.detail == null) {
/* 182 */       this.detail = createDetail();
/* 183 */       addNode(this.detail);
/* 184 */       return this.detail;
/*     */     }
/*     */ 
/* 187 */     throw new SOAPExceptionImpl("Error: Detail already exists");
/*     */   }
/*     */ 
/*     */   public boolean hasDetail()
/*     */   {
/* 192 */     return getDetail() != null;
/*     */   }
/*     */ 
/*     */   public abstract void setFaultActor(String paramString) throws SOAPException;
/*     */ 
/*     */   public String getFaultActor() {
/* 198 */     if (this.faultActorElement == null)
/* 199 */       findFaultActorElement();
/* 200 */     if (this.faultActorElement != null) {
/* 201 */       return this.faultActorElement.getValue();
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   public SOAPElement setElementQName(QName newName) throws SOAPException
/*     */   {
/* 208 */     log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[] { this.elementQName.getLocalPart(), newName.getLocalPart() });
/*     */ 
/* 212 */     throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
/*     */   }
/*     */ 
/*     */   protected SOAPElement convertToSoapElement(Element element)
/*     */   {
/* 217 */     if ((element instanceof SOAPFaultElement))
/* 218 */       return (SOAPElement)element;
/* 219 */     if ((element instanceof SOAPElement)) {
/* 220 */       SOAPElement soapElement = (SOAPElement)element;
/* 221 */       if (getDetailName().equals(soapElement.getElementName())) {
/* 222 */         return replaceElementWithSOAPElement(element, createDetail());
/*     */       }
/* 224 */       String localName = soapElement.getElementName().getLocalName();
/*     */ 
/* 226 */       if (isStandardFaultElement(localName)) {
/* 227 */         return replaceElementWithSOAPElement(element, createSOAPFaultElement(soapElement.getElementQName()));
/*     */       }
/*     */ 
/* 230 */       return soapElement;
/*     */     }
/*     */ 
/* 233 */     Name elementName = NameImpl.copyElementName(element);
/*     */     ElementImpl newElement;
/*     */     ElementImpl newElement;
/* 235 */     if (getDetailName().equals(elementName)) {
/* 236 */       newElement = createDetail();
/*     */     } else {
/* 238 */       String localName = elementName.getLocalName();
/*     */       ElementImpl newElement;
/* 239 */       if (isStandardFaultElement(localName)) {
/* 240 */         newElement = createSOAPFaultElement(elementName);
/*     */       }
/*     */       else
/* 243 */         newElement = (ElementImpl)createElement(elementName);
/*     */     }
/* 245 */     return replaceElementWithSOAPElement(element, newElement);
/*     */   }
/*     */ 
/*     */   protected SOAPFaultElement addFaultCodeElement() throws SOAPException
/*     */   {
/* 250 */     if (this.faultCodeElement == null)
/* 251 */       findFaultCodeElement();
/* 252 */     if (this.faultCodeElement == null) {
/* 253 */       this.faultCodeElement = addSOAPFaultElement(getFaultCodeName().getLocalName());
/*     */ 
/* 255 */       return this.faultCodeElement;
/*     */     }
/* 257 */     throw new SOAPExceptionImpl("Error: Faultcode already exists");
/*     */   }
/*     */ 
/*     */   private SOAPFaultElement addFaultStringElement() throws SOAPException
/*     */   {
/* 262 */     if (this.faultStringElement == null)
/* 263 */       findFaultStringElement();
/* 264 */     if (this.faultStringElement == null) {
/* 265 */       this.faultStringElement = addSOAPFaultElement(getFaultStringName().getLocalName());
/*     */ 
/* 267 */       return this.faultStringElement;
/*     */     }
/*     */ 
/* 270 */     throw new SOAPExceptionImpl("Error: Faultstring already exists");
/*     */   }
/*     */ 
/*     */   private SOAPFaultElement addFaultActorElement() throws SOAPException
/*     */   {
/* 275 */     if (this.faultActorElement == null)
/* 276 */       findFaultActorElement();
/* 277 */     if (this.faultActorElement == null) {
/* 278 */       this.faultActorElement = addSOAPFaultElement(getFaultActorName().getLocalName());
/*     */ 
/* 280 */       return this.faultActorElement;
/*     */     }
/*     */ 
/* 283 */     throw new SOAPExceptionImpl("Error: Faultactor already exists");
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(Name name) throws SOAPException
/*     */   {
/* 288 */     if (getDetailName().equals(name))
/* 289 */       return addDetail();
/* 290 */     if (getFaultCodeName().equals(name))
/* 291 */       return addFaultCodeElement();
/* 292 */     if (getFaultStringName().equals(name))
/* 293 */       return addFaultStringElement();
/* 294 */     if (getFaultActorName().equals(name)) {
/* 295 */       return addFaultActorElement();
/*     */     }
/* 297 */     return super.addElement(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(QName name) throws SOAPException {
/* 301 */     return addElement(NameImpl.convertToName(name));
/*     */   }
/*     */ 
/*     */   protected FaultElementImpl addSOAPFaultElement(String localName)
/*     */     throws SOAPException
/*     */   {
/* 307 */     FaultElementImpl faultElem = createSOAPFaultElement(localName);
/* 308 */     addNode(faultElem);
/* 309 */     return faultElem;
/*     */   }
/*     */ 
/*     */   protected static Locale xmlLangToLocale(String xmlLang)
/*     */   {
/* 316 */     if (xmlLang == null) {
/* 317 */       return null;
/*     */     }
/*     */ 
/* 321 */     int index = xmlLang.indexOf("-");
/*     */ 
/* 324 */     if (index == -1) {
/* 325 */       index = xmlLang.indexOf("_");
/*     */     }
/*     */ 
/* 328 */     if (index == -1)
/*     */     {
/* 330 */       return new Locale(xmlLang, "");
/*     */     }
/*     */ 
/* 333 */     String language = xmlLang.substring(0, index);
/* 334 */     String country = xmlLang.substring(index + 1);
/* 335 */     return new Locale(language, country);
/*     */   }
/*     */ 
/*     */   protected static String localeToXmlLang(Locale locale) {
/* 339 */     String xmlLang = locale.getLanguage();
/* 340 */     String country = locale.getCountry();
/* 341 */     if (!"".equals(country)) {
/* 342 */       xmlLang = xmlLang + "-" + country;
/*     */     }
/* 344 */     return xmlLang;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
 * JD-Core Version:    0.6.2
 */