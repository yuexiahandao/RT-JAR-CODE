/*     */ package com.sun.xml.internal.ws.util.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
/*     */ import com.sun.xml.internal.ws.api.server.DocumentAddressResolver;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument.Schema;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocumentSource;
/*     */ import com.sun.xml.internal.ws.developer.SchemaValidationFeature;
/*     */ import com.sun.xml.internal.ws.developer.ValidationErrorHandler;
/*     */ import com.sun.xml.internal.ws.server.SDDocumentImpl;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayBuffer;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.xml.internal.ws.wsdl.SDDocumentResolver;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.Validator;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
/*     */ 
/*     */ public abstract class AbstractSchemaValidationTube extends AbstractFilterTubeImpl
/*     */ {
/*  82 */   private static final Logger LOGGER = Logger.getLogger(AbstractSchemaValidationTube.class.getName());
/*     */   protected final WSBinding binding;
/*     */   protected final SchemaValidationFeature feature;
/*  86 */   protected final DocumentAddressResolver resolver = new ValidationDocumentAddressResolver(null);
/*     */   protected final SchemaFactory sf;
/*     */ 
/*     */   public AbstractSchemaValidationTube(WSBinding binding, Tube next)
/*     */   {
/*  90 */     super(next);
/*  91 */     this.binding = binding;
/*  92 */     this.feature = ((SchemaValidationFeature)binding.getFeature(SchemaValidationFeature.class));
/*  93 */     this.sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/*     */   }
/*     */ 
/*     */   protected AbstractSchemaValidationTube(AbstractSchemaValidationTube that, TubeCloner cloner) {
/*  97 */     super(that, cloner);
/*  98 */     this.binding = that.binding;
/*  99 */     this.feature = that.feature;
/* 100 */     this.sf = that.sf;
/*     */   }
/*     */ 
/*     */   protected abstract Validator getValidator();
/*     */ 
/*     */   protected abstract boolean isNoValidation();
/*     */ 
/*     */   private Document createDOM(SDDocument doc)
/*     */   {
/* 118 */     ByteArrayBuffer bab = new ByteArrayBuffer();
/*     */     try {
/* 120 */       doc.writeTo(null, this.resolver, bab);
/*     */     } catch (IOException ioe) {
/* 122 */       throw new WebServiceException(ioe);
/*     */     }
/*     */ 
/* 126 */     Transformer trans = XmlUtil.newTransformer();
/* 127 */     Source source = new StreamSource(bab.newInputStream(), null);
/* 128 */     DOMResult result = new DOMResult();
/*     */     try {
/* 130 */       trans.transform(source, result);
/*     */     } catch (TransformerException te) {
/* 132 */       throw new WebServiceException(te);
/*     */     }
/* 134 */     return (Document)result.getNode();
/*     */   }
/*     */ 
/*     */   private void updateMultiSchemaForTns(String tns, String systemId, Map<String, List<String>> schemas)
/*     */   {
/* 289 */     List docIdList = (List)schemas.get(tns);
/* 290 */     if (docIdList == null) {
/* 291 */       docIdList = new ArrayList();
/* 292 */       schemas.put(tns, docIdList);
/*     */     }
/* 294 */     docIdList.add(systemId);
/*     */   }
/*     */ 
/*     */   protected Source[] getSchemaSources(Iterable<SDDocument> docs, MetadataResolverImpl mdresolver)
/*     */   {
/* 313 */     Map inlinedSchemas = new HashMap();
/*     */ 
/* 317 */     Map multiSchemaForTns = new HashMap();
/*     */ 
/* 319 */     for (SDDocument sdoc : docs) {
/* 320 */       if (sdoc.isWSDL()) {
/* 321 */         Document dom = createDOM(sdoc);
/*     */ 
/* 323 */         addSchemaFragmentSource(dom, sdoc.getURL().toExternalForm(), inlinedSchemas);
/* 324 */       } else if (sdoc.isSchema()) {
/* 325 */         updateMultiSchemaForTns(((SDDocument.Schema)sdoc).getTargetNamespace(), sdoc.getURL().toExternalForm(), multiSchemaForTns);
/*     */       }
/*     */     }
/* 328 */     LOGGER.fine("WSDL inlined schema fragment documents(these are used to create a pseudo schema) = " + inlinedSchemas.keySet());
/* 329 */     for (DOMSource src : inlinedSchemas.values()) {
/* 330 */       String tns = getTargetNamespace(src);
/* 331 */       updateMultiSchemaForTns(tns, src.getSystemId(), multiSchemaForTns);
/*     */     }
/*     */ 
/* 334 */     if (multiSchemaForTns.isEmpty())
/* 335 */       return new Source[0];
/* 336 */     if ((multiSchemaForTns.size() == 1) && (((List)multiSchemaForTns.values().iterator().next()).size() == 1))
/*     */     {
/* 338 */       String systemId = (String)((List)multiSchemaForTns.values().iterator().next()).get(0);
/* 339 */       return new Source[] { (Source)inlinedSchemas.get(systemId) };
/*     */     }
/*     */ 
/* 343 */     mdresolver.addSchemas(inlinedSchemas.values());
/*     */ 
/* 348 */     Map oneSchemaForTns = new HashMap();
/* 349 */     int i = 0;
/* 350 */     for (Map.Entry e : multiSchemaForTns.entrySet())
/*     */     {
/* 352 */       List sameTnsSchemas = (List)e.getValue();
/*     */       String systemId;
/* 353 */       if (sameTnsSchemas.size() > 1)
/*     */       {
/* 356 */         String systemId = "file:x-jax-ws-include-" + i++;
/* 357 */         Source src = createSameTnsPseudoSchema((String)e.getKey(), sameTnsSchemas, systemId);
/* 358 */         mdresolver.addSchema(src);
/*     */       } else {
/* 360 */         systemId = (String)sameTnsSchemas.get(0);
/*     */       }
/* 362 */       oneSchemaForTns.put(e.getKey(), systemId);
/*     */     }
/*     */ 
/* 366 */     Source pseudoSchema = createMasterPseudoSchema(oneSchemaForTns);
/* 367 */     return new Source[] { pseudoSchema };
/*     */   }
/*     */   @Nullable
/*     */   private void addSchemaFragmentSource(Document doc, String systemId, Map<String, DOMSource> map) {
/* 371 */     Element e = doc.getDocumentElement();
/* 372 */     assert (e.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/"));
/* 373 */     assert (e.getLocalName().equals("definitions"));
/*     */ 
/* 375 */     NodeList typesList = e.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "types");
/* 376 */     for (int i = 0; i < typesList.getLength(); i++) {
/* 377 */       NodeList schemaList = ((Element)typesList.item(i)).getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 378 */       for (int j = 0; j < schemaList.getLength(); j++) {
/* 379 */         Element elem = (Element)schemaList.item(j);
/* 380 */         NamespaceSupport nss = new NamespaceSupport();
/*     */ 
/* 383 */         buildNamespaceSupport(nss, elem);
/* 384 */         patchDOMFragment(nss, elem);
/* 385 */         String docId = systemId + "#schema" + j;
/* 386 */         map.put(docId, new DOMSource(elem, docId));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void buildNamespaceSupport(NamespaceSupport nss, Node node)
/*     */   {
/* 396 */     if ((node == null) || (node.getNodeType() != 1)) {
/* 397 */       return;
/*     */     }
/* 399 */     buildNamespaceSupport(nss, node.getParentNode());
/*     */ 
/* 401 */     nss.pushContext();
/* 402 */     NamedNodeMap atts = node.getAttributes();
/* 403 */     for (int i = 0; i < atts.getLength(); i++) {
/* 404 */       Attr a = (Attr)atts.item(i);
/* 405 */       if ("xmlns".equals(a.getPrefix())) {
/* 406 */         nss.declarePrefix(a.getLocalName(), a.getValue());
/*     */       }
/* 409 */       else if ("xmlns".equals(a.getName()))
/* 410 */         nss.declarePrefix("", a.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   private void patchDOMFragment(NamespaceSupport nss, Element elem)
/*     */   {
/* 423 */     NamedNodeMap atts = elem.getAttributes();
/* 424 */     for (Enumeration en = nss.getPrefixes(); en.hasMoreElements(); ) {
/* 425 */       String prefix = (String)en.nextElement();
/*     */ 
/* 427 */       for (int i = 0; i < atts.getLength(); i++) {
/* 428 */         Attr a = (Attr)atts.item(i);
/* 429 */         if ((!"xmlns".equals(a.getPrefix())) || (!a.getLocalName().equals(prefix))) {
/* 430 */           LOGGER.fine("Patching with xmlns:" + prefix + "=" + nss.getURI(prefix));
/* 431 */           elem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, nss.getURI(prefix));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   private Source createSameTnsPseudoSchema(String tns, Collection<String> docs, String pseudoSystemId)
/*     */   {
/* 453 */     assert (docs.size() > 1);
/*     */ 
/* 455 */     final StringBuilder sb = new StringBuilder("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'");
/* 456 */     if (!tns.equals("")) {
/* 457 */       sb.append(" targetNamespace='").append(tns).append("'");
/*     */     }
/* 459 */     sb.append(">\n");
/* 460 */     for (String systemId : docs) {
/* 461 */       sb.append("<xsd:include schemaLocation='").append(systemId).append("'/>\n");
/*     */     }
/* 463 */     sb.append("</xsd:schema>\n");
/* 464 */     LOGGER.fine("Pseudo Schema for the same tns=" + tns + "is " + sb);
/*     */ 
/* 467 */     return new StreamSource(pseudoSystemId)
/*     */     {
/*     */       public Reader getReader() {
/* 470 */         return new StringReader(sb.toString());
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private Source createMasterPseudoSchema(Map<String, String> docs)
/*     */   {
/* 487 */     final StringBuilder sb = new StringBuilder("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema' targetNamespace='urn:x-jax-ws-master'>\n");
/* 488 */     for (Map.Entry e : docs.entrySet()) {
/* 489 */       String systemId = (String)e.getValue();
/* 490 */       String ns = (String)e.getKey();
/* 491 */       sb.append("<xsd:import schemaLocation='").append(systemId).append("'");
/* 492 */       if (!ns.equals("")) {
/* 493 */         sb.append(" namespace='").append(ns).append("'");
/*     */       }
/* 495 */       sb.append("/>\n");
/*     */     }
/* 497 */     sb.append("</xsd:schema>");
/* 498 */     LOGGER.fine("Master Pseudo Schema = " + sb);
/*     */ 
/* 501 */     return new StreamSource("file:x-jax-ws-master-doc")
/*     */     {
/*     */       public Reader getReader() {
/* 504 */         return new StringReader(sb.toString());
/*     */       } } ;
/*     */   }
/*     */ 
/* 510 */   protected void doProcess(Packet packet) throws SAXException { getValidator().reset();
/* 511 */     Class handlerClass = this.feature.getErrorHandler();
/*     */     ValidationErrorHandler handler;
/*     */     try {
/* 514 */       handler = (ValidationErrorHandler)handlerClass.newInstance();
/*     */     } catch (Exception e) {
/* 516 */       throw new WebServiceException(e);
/*     */     }
/* 518 */     handler.setPacket(packet);
/* 519 */     getValidator().setErrorHandler(handler);
/* 520 */     Message msg = packet.getMessage().copy();
/* 521 */     Source source = msg.readPayloadAsSource();
/*     */     try
/*     */     {
/* 525 */       getValidator().validate(source);
/*     */     } catch (IOException e) {
/* 527 */       throw new WebServiceException(e);
/*     */     } }
/*     */ 
/*     */   private String getTargetNamespace(DOMSource src)
/*     */   {
/* 532 */     Element elem = (Element)src.getNode();
/* 533 */     return elem.getAttribute("targetNamespace");
/*     */   }
/*     */ 
/*     */   protected class MetadataResolverImpl
/*     */     implements SDDocumentResolver, LSResourceResolver
/*     */   {
/* 140 */     final Map<String, SDDocument> docs = new HashMap();
/*     */ 
/* 143 */     final Map<String, SDDocument> nsMapping = new HashMap();
/*     */ 
/*     */     public MetadataResolverImpl() {
/*     */     }
/*     */ 
/*     */     public MetadataResolverImpl() {
/* 149 */       for (SDDocument doc : it)
/* 150 */         if (doc.isSchema()) {
/* 151 */           this.docs.put(doc.getURL().toExternalForm(), doc);
/* 152 */           this.nsMapping.put(((SDDocument.Schema)doc).getTargetNamespace(), doc);
/*     */         }
/*     */     }
/*     */ 
/*     */     void addSchema(Source schema)
/*     */     {
/* 158 */       assert (schema.getSystemId() != null);
/*     */ 
/* 160 */       String systemId = schema.getSystemId();
/*     */       try {
/* 162 */         XMLStreamBufferResult xsbr = (XMLStreamBufferResult)XmlUtil.identityTransform(schema, new XMLStreamBufferResult());
/* 163 */         SDDocumentSource sds = SDDocumentSource.create(new URL(systemId), xsbr.getXMLStreamBuffer());
/* 164 */         SDDocument sdoc = SDDocumentImpl.create(sds, new QName(""), new QName(""));
/* 165 */         this.docs.put(systemId, sdoc);
/* 166 */         this.nsMapping.put(((SDDocument.Schema)sdoc).getTargetNamespace(), sdoc);
/*     */       } catch (Exception ex) {
/* 168 */         AbstractSchemaValidationTube.LOGGER.log(Level.WARNING, "Exception in adding schemas to resolver", ex);
/*     */       }
/*     */     }
/*     */ 
/*     */     void addSchemas(Collection<? extends Source> schemas) {
/* 173 */       for (Source src : schemas)
/* 174 */         addSchema(src);
/*     */     }
/*     */ 
/*     */     public SDDocument resolve(String systemId)
/*     */     {
/* 179 */       SDDocument sdi = (SDDocument)this.docs.get(systemId);
/* 180 */       if (sdi == null) {
/*     */         SDDocumentSource sds;
/*     */         try {
/* 183 */           sds = SDDocumentSource.create(new URL(systemId));
/*     */         } catch (MalformedURLException e) {
/* 185 */           throw new WebServiceException(e);
/*     */         }
/* 187 */         sdi = SDDocumentImpl.create(sds, new QName(""), new QName(""));
/* 188 */         this.docs.put(systemId, sdi);
/*     */       }
/* 190 */       return sdi;
/*     */     }
/*     */ 
/*     */     public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
/* 194 */       AbstractSchemaValidationTube.LOGGER.fine("type=" + type + " namespaceURI=" + namespaceURI + " publicId=" + publicId + " systemId=" + systemId + " baseURI=" + baseURI);
/*     */       try
/*     */       {
/*     */         SDDocument doc;
/*     */         final SDDocument doc;
/* 197 */         if (systemId == null) {
/* 198 */           doc = (SDDocument)this.nsMapping.get(namespaceURI);
/*     */         } else {
/* 200 */           URI rel = baseURI != null ? new URI(baseURI).resolve(systemId) : new URI(systemId);
/*     */ 
/* 203 */           doc = (SDDocument)this.docs.get(rel.toString());
/*     */         }
/* 205 */         if (doc != null)
/* 206 */           return new LSInput()
/*     */           {
/*     */             public Reader getCharacterStream() {
/* 209 */               return null;
/*     */             }
/*     */ 
/*     */             public void setCharacterStream(Reader characterStream) {
/* 213 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public InputStream getByteStream() {
/* 217 */               ByteArrayBuffer bab = new ByteArrayBuffer();
/*     */               try {
/* 219 */                 doc.writeTo(null, AbstractSchemaValidationTube.this.resolver, bab);
/*     */               } catch (IOException ioe) {
/* 221 */                 throw new WebServiceException(ioe);
/*     */               }
/* 223 */               return bab.newInputStream();
/*     */             }
/*     */ 
/*     */             public void setByteStream(InputStream byteStream) {
/* 227 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public String getStringData() {
/* 231 */               return null;
/*     */             }
/*     */ 
/*     */             public void setStringData(String stringData) {
/* 235 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public String getSystemId() {
/* 239 */               return doc.getURL().toExternalForm();
/*     */             }
/*     */ 
/*     */             public void setSystemId(String systemId) {
/* 243 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public String getPublicId() {
/* 247 */               return null;
/*     */             }
/*     */ 
/*     */             public void setPublicId(String publicId) {
/* 251 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public String getBaseURI() {
/* 255 */               return doc.getURL().toExternalForm();
/*     */             }
/*     */ 
/*     */             public void setBaseURI(String baseURI) {
/* 259 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public String getEncoding() {
/* 263 */               return null;
/*     */             }
/*     */ 
/*     */             public void setEncoding(String encoding) {
/* 267 */               throw new UnsupportedOperationException();
/*     */             }
/*     */ 
/*     */             public boolean getCertifiedText() {
/* 271 */               return false;
/*     */             }
/*     */ 
/*     */             public void setCertifiedText(boolean certifiedText) {
/* 275 */               throw new UnsupportedOperationException();
/*     */             }
/*     */           };
/*     */       }
/*     */       catch (Exception e) {
/* 280 */         AbstractSchemaValidationTube.LOGGER.log(Level.WARNING, "Exception in LSResourceResolver impl", e);
/*     */       }
/* 282 */       AbstractSchemaValidationTube.LOGGER.fine("Don't know about systemId=" + systemId + " baseURI=" + baseURI);
/* 283 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ValidationDocumentAddressResolver
/*     */     implements DocumentAddressResolver
/*     */   {
/*     */     @Nullable
/*     */     public String getRelativeAddressFor(@NotNull SDDocument current, @NotNull SDDocument referenced)
/*     */     {
/* 111 */       AbstractSchemaValidationTube.LOGGER.fine("Current = " + current.getURL() + " resolved relative=" + referenced.getURL());
/* 112 */       return referenced.getURL().toExternalForm();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube
 * JD-Core Version:    0.6.2
 */