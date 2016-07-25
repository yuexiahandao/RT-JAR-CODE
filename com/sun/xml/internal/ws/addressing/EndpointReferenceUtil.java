/*     */ package com.sun.xml.internal.ws.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferSource;
/*     */ import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;
/*     */ import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion.EPR;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference.Address;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference.AttributedQName;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference.Elements;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference.ServiceNameType;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.wsaddressing.W3CEndpointReference;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class EndpointReferenceUtil
/*     */ {
/* 201 */   private static boolean w3cMetadataWritten = false;
/*     */ 
/*     */   public static <T extends EndpointReference> T transform(Class<T> clazz, @NotNull EndpointReference epr)
/*     */   {
/*  60 */     assert (epr != null);
/*  61 */     if (clazz.isAssignableFrom(W3CEndpointReference.class)) {
/*  62 */       if ((epr instanceof W3CEndpointReference))
/*  63 */         return epr;
/*  64 */       if ((epr instanceof MemberSubmissionEndpointReference))
/*  65 */         return toW3CEpr((MemberSubmissionEndpointReference)epr);
/*     */     }
/*  67 */     else if (clazz.isAssignableFrom(MemberSubmissionEndpointReference.class)) {
/*  68 */       if ((epr instanceof W3CEndpointReference))
/*  69 */         return toMSEpr((W3CEndpointReference)epr);
/*  70 */       if ((epr instanceof MemberSubmissionEndpointReference)) {
/*  71 */         return epr;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  76 */     throw new WebServiceException("Unknwon EndpointReference: " + epr.getClass());
/*     */   }
/*     */ 
/*     */   private static W3CEndpointReference toW3CEpr(MemberSubmissionEndpointReference msEpr)
/*     */   {
/*  81 */     StreamWriterBufferCreator writer = new StreamWriterBufferCreator();
/*  82 */     w3cMetadataWritten = false;
/*     */     try {
/*  84 */       writer.writeStartDocument();
/*  85 */       writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "EndpointReference", AddressingVersion.W3C.nsUri);
/*     */ 
/*  87 */       writer.writeNamespace(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.nsUri);
/*     */ 
/*  90 */       writer.writeStartElement(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.eprType.address, AddressingVersion.W3C.nsUri);
/*     */ 
/*  93 */       writer.writeCharacters(msEpr.addr.uri);
/*  94 */       writer.writeEndElement();
/*     */ 
/*  96 */       if (((msEpr.referenceProperties != null) && (msEpr.referenceProperties.elements.size() > 0)) || ((msEpr.referenceParameters != null) && (msEpr.referenceParameters.elements.size() > 0)))
/*     */       {
/*  99 */         writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "ReferenceParameters", AddressingVersion.W3C.nsUri);
/*     */ 
/* 102 */         if (msEpr.referenceProperties != null) {
/* 103 */           for (Element e : msEpr.referenceProperties.elements) {
/* 104 */             DOMUtil.serializeNode(e, writer);
/*     */           }
/*     */         }
/*     */ 
/* 108 */         if (msEpr.referenceParameters != null) {
/* 109 */           for (Element e : msEpr.referenceParameters.elements) {
/* 110 */             DOMUtil.serializeNode(e, writer);
/*     */           }
/*     */         }
/* 113 */         writer.writeEndElement();
/*     */       }
/*     */ 
/* 157 */       Element wsdlElement = null;
/*     */ 
/* 159 */       if ((msEpr.elements != null) && (msEpr.elements.size() > 0)) {
/* 160 */         for (Element e : msEpr.elements) {
/* 161 */           if ((e.getNamespaceURI().equals(MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI())) && (e.getLocalName().equals(MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart())))
/*     */           {
/* 163 */             NodeList nl = e.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", WSDLConstants.QNAME_DEFINITIONS.getLocalPart());
/*     */ 
/* 165 */             if (nl != null) {
/* 166 */               wsdlElement = (Element)nl.item(0);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 171 */       if (wsdlElement != null) {
/* 172 */         DOMUtil.serializeNode(wsdlElement, writer);
/*     */       }
/*     */ 
/* 175 */       if (w3cMetadataWritten) {
/* 176 */         writer.writeEndElement();
/*     */       }
/*     */ 
/* 179 */       if ((msEpr.elements != null) && (msEpr.elements.size() > 0)) {
/* 180 */         for (Element e : msEpr.elements) {
/* 181 */           if ((e.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/")) && (e.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())));
/* 185 */           DOMUtil.serializeNode(e, writer);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 192 */       writer.writeEndElement();
/* 193 */       writer.writeEndDocument();
/* 194 */       writer.flush();
/*     */     } catch (XMLStreamException e) {
/* 196 */       throw new WebServiceException(e);
/*     */     }
/* 198 */     return new W3CEndpointReference(new XMLStreamBufferSource(writer.getXMLStreamBuffer()));
/*     */   }
/*     */ 
/*     */   private static void writeW3CMetadata(StreamWriterBufferCreator writer)
/*     */     throws XMLStreamException
/*     */   {
/* 204 */     if (!w3cMetadataWritten) {
/* 205 */       writer.writeStartElement(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.eprType.wsdlMetadata.getLocalPart(), AddressingVersion.W3C.nsUri);
/* 206 */       w3cMetadataWritten = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static MemberSubmissionEndpointReference toMSEpr(W3CEndpointReference w3cEpr) {
/* 211 */     DOMResult result = new DOMResult();
/* 212 */     w3cEpr.writeTo(result);
/* 213 */     Node eprNode = result.getNode();
/* 214 */     Element e = DOMUtil.getFirstElementChild(eprNode);
/* 215 */     if (e == null) {
/* 216 */       return null;
/*     */     }
/* 218 */     MemberSubmissionEndpointReference msEpr = new MemberSubmissionEndpointReference();
/*     */ 
/* 220 */     NodeList nodes = e.getChildNodes();
/* 221 */     for (int i = 0; i < nodes.getLength(); i++) {
/* 222 */       if (nodes.item(i).getNodeType() == 1) {
/* 223 */         Element child = (Element)nodes.item(i);
/* 224 */         if ((child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri)) && (child.getLocalName().equals(AddressingVersion.W3C.eprType.address)))
/*     */         {
/* 226 */           if (msEpr.addr == null)
/* 227 */             msEpr.addr = new MemberSubmissionEndpointReference.Address();
/* 228 */           msEpr.addr.uri = XmlUtil.getTextForNode(child);
/*     */ 
/* 231 */           msEpr.addr.attributes = getAttributes(child);
/* 232 */         } else if ((child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri)) && (child.getLocalName().equals("ReferenceParameters")))
/*     */         {
/* 234 */           NodeList refParams = child.getChildNodes();
/* 235 */           for (int j = 0; j < refParams.getLength(); j++)
/* 236 */             if (refParams.item(j).getNodeType() == 1) {
/* 237 */               if (msEpr.referenceParameters == null) {
/* 238 */                 msEpr.referenceParameters = new MemberSubmissionEndpointReference.Elements();
/* 239 */                 msEpr.referenceParameters.elements = new ArrayList();
/*     */               }
/* 241 */               msEpr.referenceParameters.elements.add((Element)refParams.item(j));
/*     */             }
/*     */         }
/* 244 */         else if ((child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri)) && (child.getLocalName().equals(AddressingVersion.W3C.eprType.wsdlMetadata.getLocalPart())))
/*     */         {
/* 246 */           NodeList metadata = child.getChildNodes();
/* 247 */           String wsdlLocation = child.getAttributeNS("http://www.w3.org/ns/wsdl-instance", "wsdlLocation");
/*     */ 
/* 249 */           Element wsdlDefinitions = null;
/* 250 */           for (int j = 0; j < metadata.getLength(); j++) {
/* 251 */             Node node = metadata.item(j);
/* 252 */             if (node.getNodeType() == 1)
/*     */             {
/* 255 */               Element elm = (Element)node;
/* 256 */               if (((elm.getNamespaceURI().equals(AddressingVersion.W3C.wsdlNsUri)) || (elm.getNamespaceURI().equals("http://www.w3.org/2007/05/addressing/metadata"))) && (elm.getLocalName().equals(AddressingVersion.W3C.eprType.serviceName)))
/*     */               {
/* 259 */                 msEpr.serviceName = new MemberSubmissionEndpointReference.ServiceNameType();
/* 260 */                 msEpr.serviceName.portName = elm.getAttribute(AddressingVersion.W3C.eprType.portName);
/*     */ 
/* 262 */                 String service = elm.getTextContent();
/* 263 */                 String prefix = XmlUtil.getPrefix(service);
/* 264 */                 String name = XmlUtil.getLocalPart(service);
/*     */ 
/* 267 */                 if (name != null)
/*     */                 {
/* 270 */                   if (prefix != null) {
/* 271 */                     String ns = elm.lookupNamespaceURI(prefix);
/* 272 */                     if (ns != null)
/* 273 */                       msEpr.serviceName.name = new QName(ns, name, prefix);
/*     */                   } else {
/* 275 */                     msEpr.serviceName.name = new QName(null, name);
/*     */                   }
/* 277 */                   msEpr.serviceName.attributes = getAttributes(elm);
/*     */                 } } else if (((elm.getNamespaceURI().equals(AddressingVersion.W3C.wsdlNsUri)) || (elm.getNamespaceURI().equals("http://www.w3.org/2007/05/addressing/metadata"))) && (elm.getLocalName().equals(AddressingVersion.W3C.eprType.portTypeName)))
/*     */               {
/* 281 */                 msEpr.portTypeName = new MemberSubmissionEndpointReference.AttributedQName();
/*     */ 
/* 283 */                 String portType = elm.getTextContent();
/* 284 */                 String prefix = XmlUtil.getPrefix(portType);
/* 285 */                 String name = XmlUtil.getLocalPart(portType);
/*     */ 
/* 288 */                 if (name != null)
/*     */                 {
/* 291 */                   if (prefix != null) {
/* 292 */                     String ns = elm.lookupNamespaceURI(prefix);
/* 293 */                     if (ns != null)
/* 294 */                       msEpr.portTypeName.name = new QName(ns, name, prefix);
/*     */                   } else {
/* 296 */                     msEpr.portTypeName.name = new QName(null, name);
/*     */                   }
/* 298 */                   msEpr.portTypeName.attributes = getAttributes(elm);
/*     */                 } } else if ((elm.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/")) && (elm.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())))
/*     */               {
/* 301 */                 wsdlDefinitions = elm;
/*     */               }
/*     */               else
/*     */               {
/* 305 */                 if (msEpr.elements == null) {
/* 306 */                   msEpr.elements = new ArrayList();
/*     */                 }
/* 308 */                 msEpr.elements.add(elm);
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 313 */           Document doc = DOMUtil.createDom();
/* 314 */           Element mexEl = doc.createElementNS(MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI(), MemberSubmissionAddressingConstants.MEX_METADATA.getPrefix() + ":" + MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart());
/*     */ 
/* 317 */           Element metadataEl = doc.createElementNS(MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getNamespaceURI(), MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getPrefix() + ":" + MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getLocalPart());
/*     */ 
/* 320 */           metadataEl.setAttribute("Dialect", "http://schemas.xmlsoap.org/wsdl/");
/*     */ 
/* 322 */           if ((wsdlDefinitions == null) && (wsdlLocation != null) && (!wsdlLocation.equals(""))) {
/* 323 */             wsdlLocation = wsdlLocation.trim();
/* 324 */             String wsdlTns = wsdlLocation.substring(0, wsdlLocation.indexOf(' '));
/* 325 */             wsdlLocation = wsdlLocation.substring(wsdlLocation.indexOf(' ') + 1);
/* 326 */             Element wsdlEl = doc.createElementNS("http://schemas.xmlsoap.org/wsdl/", "wsdl:" + WSDLConstants.QNAME_DEFINITIONS.getLocalPart());
/*     */ 
/* 329 */             Element wsdlImportEl = doc.createElementNS("http://schemas.xmlsoap.org/wsdl/", "wsdl:" + WSDLConstants.QNAME_IMPORT.getLocalPart());
/*     */ 
/* 332 */             wsdlImportEl.setAttribute("namespace", wsdlTns);
/* 333 */             wsdlImportEl.setAttribute("location", wsdlLocation);
/* 334 */             wsdlEl.appendChild(wsdlImportEl);
/* 335 */             metadataEl.appendChild(wsdlEl);
/* 336 */           } else if (wsdlDefinitions != null) {
/* 337 */             metadataEl.appendChild(wsdlDefinitions);
/*     */           }
/* 339 */           mexEl.appendChild(metadataEl);
/*     */ 
/* 341 */           if (msEpr.elements == null) {
/* 342 */             msEpr.elements = new ArrayList();
/*     */           }
/* 344 */           msEpr.elements.add(mexEl);
/*     */         }
/*     */         else
/*     */         {
/* 349 */           if (msEpr.elements == null) {
/* 350 */             msEpr.elements = new ArrayList();
/*     */           }
/* 352 */           msEpr.elements.add(child);
/*     */         }
/*     */       }
/* 355 */       else if (nodes.item(i).getNodeType() == 2) {
/* 356 */         Node n = nodes.item(i);
/* 357 */         if (msEpr.attributes == null) {
/* 358 */           msEpr.attributes = new HashMap();
/* 359 */           String prefix = fixNull(n.getPrefix());
/* 360 */           String ns = fixNull(n.getNamespaceURI());
/* 361 */           String localName = n.getLocalName();
/* 362 */           msEpr.attributes.put(new QName(ns, localName, prefix), n.getNodeValue());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 367 */     return msEpr;
/*     */   }
/*     */ 
/*     */   private static Map<QName, String> getAttributes(Node node) {
/* 371 */     Map attribs = null;
/*     */ 
/* 373 */     NamedNodeMap nm = node.getAttributes();
/* 374 */     for (int i = 0; i < nm.getLength(); i++) {
/* 375 */       if (attribs == null)
/* 376 */         attribs = new HashMap();
/* 377 */       Node n = nm.item(i);
/* 378 */       String prefix = fixNull(n.getPrefix());
/* 379 */       String ns = fixNull(n.getNamespaceURI());
/* 380 */       String localName = n.getLocalName();
/* 381 */       if ((!prefix.equals("xmlns")) && ((prefix.length() != 0) || (!localName.equals("xmlns"))))
/*     */       {
/* 385 */         if (!localName.equals(AddressingVersion.W3C.eprType.portName))
/* 386 */           attribs.put(new QName(ns, localName, prefix), n.getNodeValue()); 
/*     */       }
/*     */     }
/* 388 */     return attribs;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   private static String fixNull(@Nullable String s)
/*     */   {
/* 394 */     if (s == null) return "";
/* 395 */     return s;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.EndpointReferenceUtil
 * JD-Core Version:    0.6.2
 */