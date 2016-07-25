/*     */ package com.sun.xml.internal.ws.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion.EPR;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.EPRExtension;
/*     */ import com.sun.xml.internal.ws.api.server.BoundEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.Module;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocumentFilter;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.server.WSEndpointImpl;
/*     */ import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
/*     */ import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class EPRSDDocumentFilter
/*     */   implements SDDocumentFilter
/*     */ {
/*     */   private final WSEndpointImpl<?> endpoint;
/*     */   List<BoundEndpoint> beList;
/*     */ 
/*     */   public EPRSDDocumentFilter(@NotNull WSEndpointImpl<?> endpoint)
/*     */   {
/*  61 */     this.endpoint = endpoint;
/*     */   }
/*     */   @Nullable
/*     */   private WSEndpointImpl<?> getEndpoint(String serviceName, String portName) {
/*  65 */     if ((serviceName == null) || (portName == null))
/*  66 */       return null;
/*  67 */     if ((this.endpoint.getServiceName().getLocalPart().equals(serviceName)) && (this.endpoint.getPortName().getLocalPart().equals(portName))) {
/*  68 */       return this.endpoint;
/*     */     }
/*  70 */     if (this.beList == null)
/*     */     {
/*  72 */       Module module = (Module)this.endpoint.getContainer().getSPI(Module.class);
/*  73 */       if (module != null)
/*  74 */         this.beList = module.getBoundEndpoints();
/*     */       else {
/*  76 */         this.beList = Collections.emptyList();
/*     */       }
/*     */     }
/*     */ 
/*  80 */     for (BoundEndpoint be : this.beList) {
/*  81 */       WSEndpoint wse = be.getEndpoint();
/*  82 */       if ((wse.getServiceName().getLocalPart().equals(serviceName)) && (wse.getPortName().getLocalPart().equals(portName))) {
/*  83 */         return (WSEndpointImpl)wse;
/*     */       }
/*     */     }
/*     */ 
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter filter(SDDocument doc, XMLStreamWriter w) throws XMLStreamException, IOException
/*     */   {
/*  92 */     if (!doc.isWSDL()) {
/*  93 */       return w;
/*     */     }
/*     */ 
/*  96 */     return new XMLStreamWriterFilter(w) {
/*  97 */       private boolean eprExtnFilterON = false;
/*     */ 
/*  99 */       private boolean portHasEPR = false;
/* 100 */       private int eprDepth = -1;
/*     */ 
/* 102 */       private String serviceName = null;
/* 103 */       private boolean onService = false;
/* 104 */       private int serviceDepth = -1;
/*     */ 
/* 106 */       private String portName = null;
/* 107 */       private boolean onPort = false;
/* 108 */       private int portDepth = -1;
/*     */       private String portAddress;
/* 111 */       private boolean onPortAddress = false;
/*     */ 
/*     */       private void handleStartElement(String localName, String namespaceURI) throws XMLStreamException {
/* 114 */         resetOnElementFlags();
/* 115 */         if (this.serviceDepth >= 0) {
/* 116 */           this.serviceDepth += 1;
/*     */         }
/* 118 */         if (this.portDepth >= 0) {
/* 119 */           this.portDepth += 1;
/*     */         }
/* 121 */         if (this.eprDepth >= 0) {
/* 122 */           this.eprDepth += 1;
/*     */         }
/*     */ 
/* 125 */         if ((namespaceURI.equals(WSDLConstants.QNAME_SERVICE.getNamespaceURI())) && (localName.equals(WSDLConstants.QNAME_SERVICE.getLocalPart()))) {
/* 126 */           this.onService = true;
/* 127 */           this.serviceDepth = 0;
/* 128 */         } else if ((namespaceURI.equals(WSDLConstants.QNAME_PORT.getNamespaceURI())) && (localName.equals(WSDLConstants.QNAME_PORT.getLocalPart()))) {
/* 129 */           if (this.serviceDepth >= 1) {
/* 130 */             this.onPort = true;
/* 131 */             this.portDepth = 0;
/*     */           }
/* 133 */         } else if ((namespaceURI.equals("http://www.w3.org/2005/08/addressing")) && (localName.equals("EndpointReference"))) {
/* 134 */           if ((this.serviceDepth >= 1) && (this.portDepth >= 1)) {
/* 135 */             this.portHasEPR = true;
/* 136 */             this.eprDepth = 0;
/*     */           }
/* 138 */         } else if (((namespaceURI.equals(WSDLConstants.NS_SOAP_BINDING_ADDRESS.getNamespaceURI())) || (namespaceURI.equals(WSDLConstants.NS_SOAP12_BINDING_ADDRESS.getNamespaceURI()))) && (localName.equals("address")) && (this.portDepth == 1))
/*     */         {
/* 140 */           this.onPortAddress = true;
/*     */         }
/* 142 */         WSEndpoint endpoint = EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName);
/*     */ 
/* 145 */         if ((endpoint != null) && 
/* 146 */           (this.eprDepth == 1) && (!namespaceURI.equals("http://www.w3.org/2005/08/addressing")))
/*     */         {
/* 148 */           this.eprExtnFilterON = true;
/*     */         }
/*     */       }
/*     */ 
/*     */       private void resetOnElementFlags()
/*     */       {
/* 161 */         if (this.onService) {
/* 162 */           this.onService = false;
/*     */         }
/* 164 */         if (this.onPort) {
/* 165 */           this.onPort = false;
/*     */         }
/* 167 */         if (this.onPortAddress)
/* 168 */           this.onPortAddress = false;
/*     */       }
/*     */ 
/*     */       private void writeEPRExtensions(Collection<WSEndpointReference.EPRExtension> eprExtns)
/*     */         throws XMLStreamException
/*     */       {
/* 175 */         if (eprExtns != null)
/* 176 */           for (WSEndpointReference.EPRExtension e : eprExtns) {
/* 177 */             XMLStreamReaderToXMLStreamWriter c = new XMLStreamReaderToXMLStreamWriter();
/* 178 */             XMLStreamReader r = e.readAsXMLStreamReader();
/* 179 */             c.bridge(r, this.writer);
/* 180 */             XMLStreamReaderFactory.recycle(r);
/*     */           }
/*     */       }
/*     */ 
/*     */       public void writeStartElement(String prefix, String localName, String namespaceURI)
/*     */         throws XMLStreamException
/*     */       {
/* 187 */         handleStartElement(localName, namespaceURI);
/* 188 */         if (!this.eprExtnFilterON)
/* 189 */           super.writeStartElement(prefix, localName, namespaceURI);
/*     */       }
/*     */ 
/*     */       public void writeStartElement(String namespaceURI, String localName)
/*     */         throws XMLStreamException
/*     */       {
/* 195 */         handleStartElement(localName, namespaceURI);
/* 196 */         if (!this.eprExtnFilterON)
/* 197 */           super.writeStartElement(namespaceURI, localName);
/*     */       }
/*     */ 
/*     */       public void writeStartElement(String localName)
/*     */         throws XMLStreamException
/*     */       {
/* 203 */         if (!this.eprExtnFilterON)
/* 204 */           super.writeStartElement(localName);
/*     */       }
/*     */ 
/*     */       private void handleEndElement() throws XMLStreamException
/*     */       {
/* 209 */         resetOnElementFlags();
/*     */ 
/* 211 */         if (this.portDepth == 0)
/*     */         {
/* 213 */           if ((!this.portHasEPR) && (EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName) != null))
/*     */           {
/* 216 */             this.writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "EndpointReference", AddressingVersion.W3C.nsUri);
/* 217 */             this.writer.writeNamespace(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.nsUri);
/* 218 */             this.writer.writeStartElement(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.eprType.address, AddressingVersion.W3C.nsUri);
/* 219 */             this.writer.writeCharacters(this.portAddress);
/* 220 */             this.writer.writeEndElement();
/* 221 */             writeEPRExtensions(EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName).getEndpointReferenceExtensions());
/* 222 */             this.writer.writeEndElement();
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 227 */         if (this.eprDepth == 0) {
/* 228 */           if ((this.portHasEPR) && (EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName) != null)) {
/* 229 */             writeEPRExtensions(EPRSDDocumentFilter.this.getEndpoint(this.serviceName, this.portName).getEndpointReferenceExtensions());
/*     */           }
/* 231 */           this.eprExtnFilterON = false;
/*     */         }
/*     */ 
/* 234 */         if (this.serviceDepth >= 0) {
/* 235 */           this.serviceDepth -= 1;
/*     */         }
/* 237 */         if (this.portDepth >= 0) {
/* 238 */           this.portDepth -= 1;
/*     */         }
/* 240 */         if (this.eprDepth >= 0) {
/* 241 */           this.eprDepth -= 1;
/*     */         }
/*     */ 
/* 244 */         if (this.serviceDepth == -1) {
/* 245 */           this.serviceName = null;
/*     */         }
/* 247 */         if (this.portDepth == -1) {
/* 248 */           this.portHasEPR = false;
/* 249 */           this.portAddress = null;
/* 250 */           this.portName = null;
/*     */         }
/*     */       }
/*     */ 
/*     */       public void writeEndElement() throws XMLStreamException
/*     */       {
/* 256 */         handleEndElement();
/* 257 */         if (!this.eprExtnFilterON)
/* 258 */           super.writeEndElement();
/*     */       }
/*     */ 
/*     */       private void handleAttribute(String localName, String value)
/*     */       {
/* 263 */         if (localName.equals("name")) {
/* 264 */           if (this.onService) {
/* 265 */             this.serviceName = value;
/* 266 */             this.onService = false;
/* 267 */           } else if (this.onPort) {
/* 268 */             this.portName = value;
/* 269 */             this.onPort = false;
/*     */           }
/*     */         }
/* 272 */         if ((localName.equals("location")) && (this.onPortAddress))
/* 273 */           this.portAddress = value;
/*     */       }
/*     */ 
/*     */       public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
/*     */         throws XMLStreamException
/*     */       {
/* 281 */         handleAttribute(localName, value);
/* 282 */         if (!this.eprExtnFilterON)
/* 283 */           super.writeAttribute(prefix, namespaceURI, localName, value);
/*     */       }
/*     */ 
/*     */       public void writeAttribute(String namespaceURI, String localName, String value)
/*     */         throws XMLStreamException
/*     */       {
/* 289 */         handleAttribute(localName, value);
/* 290 */         if (!this.eprExtnFilterON)
/* 291 */           super.writeAttribute(namespaceURI, localName, value);
/*     */       }
/*     */ 
/*     */       public void writeAttribute(String localName, String value)
/*     */         throws XMLStreamException
/*     */       {
/* 297 */         handleAttribute(localName, value);
/* 298 */         if (!this.eprExtnFilterON)
/* 299 */           super.writeAttribute(localName, value);
/*     */       }
/*     */ 
/*     */       public void writeEmptyElement(String namespaceURI, String localName)
/*     */         throws XMLStreamException
/*     */       {
/* 306 */         if (!this.eprExtnFilterON)
/* 307 */           super.writeEmptyElement(namespaceURI, localName);
/*     */       }
/*     */ 
/*     */       public void writeNamespace(String prefix, String namespaceURI)
/*     */         throws XMLStreamException
/*     */       {
/* 313 */         if (!this.eprExtnFilterON)
/* 314 */           super.writeNamespace(prefix, namespaceURI);
/*     */       }
/*     */ 
/*     */       public void setNamespaceContext(NamespaceContext context)
/*     */         throws XMLStreamException
/*     */       {
/* 320 */         if (!this.eprExtnFilterON)
/* 321 */           super.setNamespaceContext(context);
/*     */       }
/*     */ 
/*     */       public void setDefaultNamespace(String uri)
/*     */         throws XMLStreamException
/*     */       {
/* 327 */         if (!this.eprExtnFilterON)
/* 328 */           super.setDefaultNamespace(uri);
/*     */       }
/*     */ 
/*     */       public void setPrefix(String prefix, String uri)
/*     */         throws XMLStreamException
/*     */       {
/* 334 */         if (!this.eprExtnFilterON)
/* 335 */           super.setPrefix(prefix, uri);
/*     */       }
/*     */ 
/*     */       public void writeProcessingInstruction(String target, String data)
/*     */         throws XMLStreamException
/*     */       {
/* 341 */         if (!this.eprExtnFilterON)
/* 342 */           super.writeProcessingInstruction(target, data);
/*     */       }
/*     */ 
/*     */       public void writeEmptyElement(String prefix, String localName, String namespaceURI)
/*     */         throws XMLStreamException
/*     */       {
/* 348 */         if (!this.eprExtnFilterON)
/* 349 */           super.writeEmptyElement(prefix, localName, namespaceURI);
/*     */       }
/*     */ 
/*     */       public void writeCData(String data)
/*     */         throws XMLStreamException
/*     */       {
/* 355 */         if (!this.eprExtnFilterON)
/* 356 */           super.writeCData(data);
/*     */       }
/*     */ 
/*     */       public void writeCharacters(String text)
/*     */         throws XMLStreamException
/*     */       {
/* 362 */         if (!this.eprExtnFilterON)
/* 363 */           super.writeCharacters(text);
/*     */       }
/*     */ 
/*     */       public void writeComment(String data)
/*     */         throws XMLStreamException
/*     */       {
/* 369 */         if (!this.eprExtnFilterON)
/* 370 */           super.writeComment(data);
/*     */       }
/*     */ 
/*     */       public void writeDTD(String dtd)
/*     */         throws XMLStreamException
/*     */       {
/* 376 */         if (!this.eprExtnFilterON)
/* 377 */           super.writeDTD(dtd);
/*     */       }
/*     */ 
/*     */       public void writeDefaultNamespace(String namespaceURI)
/*     */         throws XMLStreamException
/*     */       {
/* 383 */         if (!this.eprExtnFilterON)
/* 384 */           super.writeDefaultNamespace(namespaceURI);
/*     */       }
/*     */ 
/*     */       public void writeEmptyElement(String localName)
/*     */         throws XMLStreamException
/*     */       {
/* 390 */         if (!this.eprExtnFilterON)
/* 391 */           super.writeEmptyElement(localName);
/*     */       }
/*     */ 
/*     */       public void writeEntityRef(String name)
/*     */         throws XMLStreamException
/*     */       {
/* 397 */         if (!this.eprExtnFilterON)
/* 398 */           super.writeEntityRef(name);
/*     */       }
/*     */ 
/*     */       public void writeProcessingInstruction(String target)
/*     */         throws XMLStreamException
/*     */       {
/* 404 */         if (!this.eprExtnFilterON)
/* 405 */           super.writeProcessingInstruction(target);
/*     */       }
/*     */ 
/*     */       public void writeCharacters(char[] text, int start, int len)
/*     */         throws XMLStreamException
/*     */       {
/* 412 */         if (!this.eprExtnFilterON)
/* 413 */           super.writeCharacters(text, start, len);
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.EPRSDDocumentFilter
 * JD-Core Version:    0.6.2
 */