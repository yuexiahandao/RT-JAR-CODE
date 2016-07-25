/*      */ package com.sun.xml.internal.bind.v2.runtime;
/*      */ 
/*      */ import com.sun.istack.internal.SAXException2;
/*      */ import com.sun.xml.internal.bind.CycleRecoverable;
/*      */ import com.sun.xml.internal.bind.CycleRecoverable.Context;
/*      */ import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
/*      */ import com.sun.xml.internal.bind.util.ValidationEventLocatorExImpl;
/*      */ import com.sun.xml.internal.bind.v2.runtime.output.MTOMXmlOutput;
/*      */ import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
/*      */ import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl.Element;
/*      */ import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
/*      */ import com.sun.xml.internal.bind.v2.runtime.output.XmlOutput;
/*      */ import com.sun.xml.internal.bind.v2.runtime.property.Property;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntData;
/*      */ import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.activation.MimeType;
/*      */ import javax.xml.bind.DatatypeConverter;
/*      */ import javax.xml.bind.JAXBException;
/*      */ import javax.xml.bind.Marshaller;
/*      */ import javax.xml.bind.Marshaller.Listener;
/*      */ import javax.xml.bind.ValidationEvent;
/*      */ import javax.xml.bind.ValidationEventHandler;
/*      */ import javax.xml.bind.ValidationEventLocator;
/*      */ import javax.xml.bind.annotation.DomHandler;
/*      */ import javax.xml.bind.annotation.XmlNs;
/*      */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*      */ import javax.xml.bind.helpers.NotIdentifiableEventImpl;
/*      */ import javax.xml.bind.helpers.ValidationEventImpl;
/*      */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.sax.SAXResult;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public final class XMLSerializer extends Coordinator
/*      */ {
/*      */   public final JAXBContextImpl grammar;
/*      */   private XmlOutput out;
/*      */   public final NameList nameList;
/*      */   public final int[] knownUri2prefixIndexMap;
/*      */   private final NamespaceContextImpl nsContext;
/*      */   private NamespaceContextImpl.Element nse;
/*  137 */   ThreadLocal<Property> currentProperty = new ThreadLocal();
/*      */ 
/*  143 */   private boolean textHasAlreadyPrinted = false;
/*      */ 
/*  148 */   private boolean seenRoot = false;
/*      */   private final MarshallerImpl marshaller;
/*  154 */   private final Set<Object> idReferencedObjects = new HashSet();
/*      */ 
/*  157 */   private final Set<Object> objectsWithId = new HashSet();
/*      */ 
/*  163 */   private final CollisionCheckStack<Object> cycleDetectionStack = new CollisionCheckStack();
/*      */   private String schemaLocation;
/*      */   private String noNsSchemaLocation;
/*      */   private Transformer identityTransformer;
/*      */   private ContentHandlerAdaptor contentHandlerAdapter;
/*      */   private boolean fragment;
/*      */   private Base64Data base64Data;
/*  185 */   private final IntData intData = new IntData();
/*      */   public AttachmentMarshaller attachmentMarshaller;
/*      */   private MimeType expectedMimeType;
/*      */   private boolean inlineBinaryFlag;
/*      */   private QName schemaType;
/*      */ 
/*      */   XMLSerializer(MarshallerImpl _owner)
/*      */   {
/*  190 */     this.marshaller = _owner;
/*  191 */     this.grammar = this.marshaller.context;
/*  192 */     this.nsContext = new NamespaceContextImpl(this);
/*  193 */     this.nameList = this.marshaller.context.nameList;
/*  194 */     this.knownUri2prefixIndexMap = new int[this.nameList.namespaceURIs.length];
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public Base64Data getCachedBase64DataInstance()
/*      */   {
/*  205 */     return new Base64Data();
/*      */   }
/*      */ 
/*      */   private String getIdFromObject(Object identifiableObject)
/*      */     throws SAXException, JAXBException
/*      */   {
/*  212 */     return this.grammar.getBeanInfo(identifiableObject, true).getId(identifiableObject, this);
/*      */   }
/*      */ 
/*      */   private void handleMissingObjectError(String fieldName) throws SAXException, IOException, XMLStreamException {
/*  216 */     reportMissingObjectError(fieldName);
/*      */ 
/*  219 */     endNamespaceDecls(null);
/*  220 */     endAttributes();
/*      */   }
/*      */ 
/*      */   public void reportError(ValidationEvent ve) throws SAXException
/*      */   {
/*      */     ValidationEventHandler handler;
/*      */     try
/*      */     {
/*  228 */       handler = this.marshaller.getEventHandler();
/*      */     } catch (JAXBException e) {
/*  230 */       throw new SAXException2(e);
/*      */     }
/*      */ 
/*  233 */     if (!handler.handleEvent(ve)) {
/*  234 */       if ((ve.getLinkedException() instanceof Exception)) {
/*  235 */         throw new SAXException2((Exception)ve.getLinkedException());
/*      */       }
/*  237 */       throw new SAXException2(ve.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void reportError(String fieldName, Throwable t)
/*      */     throws SAXException
/*      */   {
/*  248 */     ValidationEvent ve = new ValidationEventImpl(1, t.getMessage(), getCurrentLocation(fieldName), t);
/*      */ 
/*  250 */     reportError(ve);
/*      */   }
/*      */ 
/*      */   public void startElement(Name tagName, Object outerPeer) {
/*  254 */     startElement();
/*  255 */     this.nse.setTagName(tagName, outerPeer);
/*      */   }
/*      */ 
/*      */   public void startElement(String nsUri, String localName, String preferredPrefix, Object outerPeer) {
/*  259 */     startElement();
/*  260 */     int idx = this.nsContext.declareNsUri(nsUri, preferredPrefix, false);
/*  261 */     this.nse.setTagName(idx, localName, outerPeer);
/*      */   }
/*      */ 
/*      */   public void startElementForce(String nsUri, String localName, String forcedPrefix, Object outerPeer)
/*      */   {
/*  269 */     startElement();
/*  270 */     int idx = this.nsContext.force(nsUri, forcedPrefix);
/*  271 */     this.nse.setTagName(idx, localName, outerPeer);
/*      */   }
/*      */ 
/*      */   public void endNamespaceDecls(Object innerPeer) throws IOException, XMLStreamException {
/*  275 */     this.nsContext.collectionMode = false;
/*  276 */     this.nse.startElement(this.out, innerPeer);
/*      */   }
/*      */ 
/*      */   public void endAttributes()
/*      */     throws SAXException, IOException, XMLStreamException
/*      */   {
/*  284 */     if (!this.seenRoot) {
/*  285 */       this.seenRoot = true;
/*  286 */       if ((this.schemaLocation != null) || (this.noNsSchemaLocation != null)) {
/*  287 */         int p = this.nsContext.getPrefixIndex("http://www.w3.org/2001/XMLSchema-instance");
/*  288 */         if (this.schemaLocation != null)
/*  289 */           this.out.attribute(p, "schemaLocation", this.schemaLocation);
/*  290 */         if (this.noNsSchemaLocation != null) {
/*  291 */           this.out.attribute(p, "noNamespaceSchemaLocation", this.noNsSchemaLocation);
/*      */         }
/*      */       }
/*      */     }
/*  295 */     this.out.endStartTag();
/*      */   }
/*      */ 
/*      */   public void endElement()
/*      */     throws SAXException, IOException, XMLStreamException
/*      */   {
/*  303 */     this.nse.endElement(this.out);
/*  304 */     this.nse = this.nse.pop();
/*  305 */     this.textHasAlreadyPrinted = false;
/*      */   }
/*      */ 
/*      */   public void leafElement(Name tagName, String data, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  309 */     if (this.seenRoot) {
/*  310 */       this.textHasAlreadyPrinted = false;
/*  311 */       this.nse = this.nse.push();
/*  312 */       this.out.beginStartTag(tagName);
/*  313 */       this.out.endStartTag();
/*  314 */       this.out.text(data, false);
/*  315 */       this.out.endTag(tagName);
/*  316 */       this.nse = this.nse.pop();
/*      */     }
/*      */     else
/*      */     {
/*  320 */       startElement(tagName, null);
/*  321 */       endNamespaceDecls(null);
/*  322 */       endAttributes();
/*  323 */       this.out.text(data, false);
/*  324 */       endElement();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leafElement(Name tagName, Pcdata data, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  329 */     if (this.seenRoot) {
/*  330 */       this.textHasAlreadyPrinted = false;
/*  331 */       this.nse = this.nse.push();
/*  332 */       this.out.beginStartTag(tagName);
/*  333 */       this.out.endStartTag();
/*  334 */       this.out.text(data, false);
/*  335 */       this.out.endTag(tagName);
/*  336 */       this.nse = this.nse.pop();
/*      */     }
/*      */     else
/*      */     {
/*  340 */       startElement(tagName, null);
/*  341 */       endNamespaceDecls(null);
/*  342 */       endAttributes();
/*  343 */       this.out.text(data, false);
/*  344 */       endElement();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leafElement(Name tagName, int data, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  349 */     this.intData.reset(data);
/*  350 */     leafElement(tagName, this.intData, fieldName);
/*      */   }
/*      */ 
/*      */   public void text(String text, String fieldName)
/*      */     throws SAXException, IOException, XMLStreamException
/*      */   {
/*  390 */     if (text == null) {
/*  391 */       reportMissingObjectError(fieldName);
/*  392 */       return;
/*      */     }
/*      */ 
/*  395 */     this.out.text(text, this.textHasAlreadyPrinted);
/*  396 */     this.textHasAlreadyPrinted = true;
/*      */   }
/*      */ 
/*      */   public void text(Pcdata text, String fieldName)
/*      */     throws SAXException, IOException, XMLStreamException
/*      */   {
/*  405 */     if (text == null) {
/*  406 */       reportMissingObjectError(fieldName);
/*  407 */       return;
/*      */     }
/*      */ 
/*  410 */     this.out.text(text, this.textHasAlreadyPrinted);
/*  411 */     this.textHasAlreadyPrinted = true;
/*      */   }
/*      */ 
/*      */   public void attribute(String uri, String local, String value)
/*      */     throws SAXException
/*      */   {
/*      */     int prefix;
/*      */     int prefix;
/*  416 */     if (uri.length() == 0)
/*      */     {
/*  418 */       prefix = -1;
/*      */     }
/*  420 */     else prefix = this.nsContext.getPrefixIndex(uri);
/*      */ 
/*      */     try
/*      */     {
/*  424 */       this.out.attribute(prefix, local, value);
/*      */     } catch (IOException e) {
/*  426 */       throw new SAXException2(e);
/*      */     } catch (XMLStreamException e) {
/*  428 */       throw new SAXException2(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attribute(Name name, CharSequence value)
/*      */     throws IOException, XMLStreamException
/*      */   {
/*  435 */     this.out.attribute(name, value.toString());
/*      */   }
/*      */ 
/*      */   public NamespaceContext2 getNamespaceContext() {
/*  439 */     return this.nsContext;
/*      */   }
/*      */ 
/*      */   public String onID(Object owner, String value)
/*      */   {
/*  444 */     this.objectsWithId.add(owner);
/*  445 */     return value;
/*      */   }
/*      */ 
/*      */   public String onIDREF(Object obj) throws SAXException {
/*      */     String id;
/*      */     try {
/*  451 */       id = getIdFromObject(obj);
/*      */     } catch (JAXBException e) {
/*  453 */       reportError(null, e);
/*  454 */       return null;
/*      */     }
/*  456 */     this.idReferencedObjects.add(obj);
/*  457 */     if (id == null) {
/*  458 */       reportError(new NotIdentifiableEventImpl(1, Messages.NOT_IDENTIFIABLE.format(new Object[0]), new ValidationEventLocatorImpl(obj)));
/*      */     }
/*      */ 
/*  463 */     return id;
/*      */   }
/*      */ 
/*      */   public void childAsRoot(Object obj)
/*      */     throws JAXBException, IOException, SAXException, XMLStreamException
/*      */   {
/*  471 */     JaxBeanInfo beanInfo = this.grammar.getBeanInfo(obj, true);
/*      */ 
/*  476 */     this.cycleDetectionStack.pushNocheck(obj);
/*      */ 
/*  478 */     boolean lookForLifecycleMethods = beanInfo.lookForLifecycleMethods();
/*  479 */     if (lookForLifecycleMethods) {
/*  480 */       fireBeforeMarshalEvents(beanInfo, obj);
/*      */     }
/*      */ 
/*  483 */     beanInfo.serializeRoot(obj, this);
/*      */ 
/*  485 */     if (lookForLifecycleMethods) {
/*  486 */       fireAfterMarshalEvents(beanInfo, obj);
/*      */     }
/*      */ 
/*  489 */     this.cycleDetectionStack.pop();
/*      */   }
/*      */ 
/*      */   private Object pushObject(Object obj, String fieldName)
/*      */     throws SAXException
/*      */   {
/*  505 */     if (!this.cycleDetectionStack.push(obj)) {
/*  506 */       return obj;
/*      */     }
/*      */ 
/*  509 */     if ((obj instanceof CycleRecoverable)) {
/*  510 */       obj = ((CycleRecoverable)obj).onCycleDetected(new CycleRecoverable.Context() {
/*      */         public Marshaller getMarshaller() {
/*  512 */           return XMLSerializer.this.marshaller;
/*      */         }
/*      */       });
/*  515 */       if (obj != null)
/*      */       {
/*  519 */         this.cycleDetectionStack.pop();
/*  520 */         return pushObject(obj, fieldName);
/*      */       }
/*  522 */       return null;
/*      */     }
/*      */ 
/*  526 */     reportError(new ValidationEventImpl(1, Messages.CYCLE_IN_MARSHALLER.format(new Object[] { this.cycleDetectionStack.getCycleString() }), getCurrentLocation(fieldName), null));
/*      */ 
/*  531 */     return null;
/*      */   }
/*      */ 
/*      */   public final void childAsSoleContent(Object child, String fieldName)
/*      */     throws SAXException, IOException, XMLStreamException
/*      */   {
/*  550 */     if (child == null) {
/*  551 */       handleMissingObjectError(fieldName);
/*      */     } else {
/*  553 */       child = pushObject(child, fieldName);
/*  554 */       if (child == null)
/*      */       {
/*  556 */         endNamespaceDecls(null);
/*  557 */         endAttributes();
/*  558 */         this.cycleDetectionStack.pop();
/*      */       }
/*      */       JaxBeanInfo beanInfo;
/*      */       try
/*      */       {
/*  563 */         beanInfo = this.grammar.getBeanInfo(child, true);
/*      */       } catch (JAXBException e) {
/*  565 */         reportError(fieldName, e);
/*      */ 
/*  567 */         endNamespaceDecls(null);
/*  568 */         endAttributes();
/*  569 */         this.cycleDetectionStack.pop();
/*  570 */         return;
/*      */       }
/*      */ 
/*  573 */       boolean lookForLifecycleMethods = beanInfo.lookForLifecycleMethods();
/*  574 */       if (lookForLifecycleMethods) {
/*  575 */         fireBeforeMarshalEvents(beanInfo, child);
/*      */       }
/*      */ 
/*  578 */       beanInfo.serializeURIs(child, this);
/*  579 */       endNamespaceDecls(child);
/*  580 */       beanInfo.serializeAttributes(child, this);
/*  581 */       endAttributes();
/*  582 */       beanInfo.serializeBody(child, this);
/*      */ 
/*  584 */       if (lookForLifecycleMethods) {
/*  585 */         fireAfterMarshalEvents(beanInfo, child);
/*      */       }
/*      */ 
/*  588 */       this.cycleDetectionStack.pop();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void childAsXsiType(Object child, String fieldName, JaxBeanInfo expected, boolean nillable)
/*      */     throws SAXException, IOException, XMLStreamException
/*      */   {
/*  617 */     if (child == null) {
/*  618 */       handleMissingObjectError(fieldName);
/*      */     } else {
/*  620 */       child = pushObject(child, fieldName);
/*  621 */       if (child == null) {
/*  622 */         endNamespaceDecls(null);
/*  623 */         endAttributes();
/*  624 */         return;
/*      */       }
/*      */ 
/*  627 */       boolean asExpected = child.getClass() == expected.jaxbType;
/*  628 */       JaxBeanInfo actual = expected;
/*  629 */       QName actualTypeName = null;
/*      */ 
/*  631 */       if ((asExpected) && (actual.lookForLifecycleMethods())) {
/*  632 */         fireBeforeMarshalEvents(actual, child);
/*      */       }
/*      */ 
/*  635 */       if (!asExpected) {
/*      */         try {
/*  637 */           actual = this.grammar.getBeanInfo(child, true);
/*  638 */           if (actual.lookForLifecycleMethods())
/*  639 */             fireBeforeMarshalEvents(actual, child);
/*      */         }
/*      */         catch (JAXBException e) {
/*  642 */           reportError(fieldName, e);
/*  643 */           endNamespaceDecls(null);
/*  644 */           endAttributes();
/*  645 */           return;
/*      */         }
/*  647 */         if (actual == expected) {
/*  648 */           asExpected = true;
/*      */         } else {
/*  650 */           actualTypeName = actual.getTypeName(child);
/*  651 */           if (actualTypeName == null) {
/*  652 */             reportError(new ValidationEventImpl(1, Messages.SUBSTITUTED_BY_ANONYMOUS_TYPE.format(new Object[] { expected.jaxbType.getName(), child.getClass().getName(), actual.jaxbType.getName() }), getCurrentLocation(fieldName)));
/*      */           }
/*      */           else
/*      */           {
/*  661 */             getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
/*  662 */             getNamespaceContext().declareNamespace(actualTypeName.getNamespaceURI(), null, false);
/*      */           }
/*      */         }
/*      */       }
/*  666 */       actual.serializeURIs(child, this);
/*      */ 
/*  668 */       if (nillable) {
/*  669 */         getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
/*      */       }
/*      */ 
/*  672 */       endNamespaceDecls(child);
/*  673 */       if (!asExpected) {
/*  674 */         attribute("http://www.w3.org/2001/XMLSchema-instance", "type", DatatypeConverter.printQName(actualTypeName, getNamespaceContext()));
/*      */       }
/*      */ 
/*  678 */       actual.serializeAttributes(child, this);
/*  679 */       boolean nilDefined = actual.isNilIncluded();
/*  680 */       if ((nillable) && (!nilDefined)) {
/*  681 */         attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
/*      */       }
/*      */ 
/*  684 */       endAttributes();
/*  685 */       actual.serializeBody(child, this);
/*      */ 
/*  687 */       if (actual.lookForLifecycleMethods()) {
/*  688 */         fireAfterMarshalEvents(actual, child);
/*      */       }
/*      */ 
/*  691 */       this.cycleDetectionStack.pop();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fireAfterMarshalEvents(JaxBeanInfo beanInfo, Object currentTarget)
/*      */   {
/*  706 */     if (beanInfo.hasAfterMarshalMethod()) {
/*  707 */       Method m = beanInfo.getLifecycleMethods().afterMarshal;
/*  708 */       fireMarshalEvent(currentTarget, m);
/*      */     }
/*      */ 
/*  712 */     Marshaller.Listener externalListener = this.marshaller.getListener();
/*  713 */     if (externalListener != null)
/*  714 */       externalListener.afterMarshal(currentTarget);
/*      */   }
/*      */ 
/*      */   private void fireBeforeMarshalEvents(JaxBeanInfo beanInfo, Object currentTarget)
/*      */   {
/*  730 */     if (beanInfo.hasBeforeMarshalMethod()) {
/*  731 */       Method m = beanInfo.getLifecycleMethods().beforeMarshal;
/*  732 */       fireMarshalEvent(currentTarget, m);
/*      */     }
/*      */ 
/*  736 */     Marshaller.Listener externalListener = this.marshaller.getListener();
/*  737 */     if (externalListener != null)
/*  738 */       externalListener.beforeMarshal(currentTarget);
/*      */   }
/*      */ 
/*      */   private void fireMarshalEvent(Object target, Method m)
/*      */   {
/*      */     try {
/*  744 */       m.invoke(target, new Object[] { this.marshaller });
/*      */     }
/*      */     catch (Exception e) {
/*  747 */       throw new IllegalStateException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attWildcardAsURIs(Map<QName, String> attributes, String fieldName) {
/*  752 */     if (attributes == null) return;
/*  753 */     for (Map.Entry e : attributes.entrySet()) {
/*  754 */       QName n = (QName)e.getKey();
/*  755 */       String nsUri = n.getNamespaceURI();
/*  756 */       if (nsUri.length() > 0) {
/*  757 */         String p = n.getPrefix();
/*  758 */         if (p.length() == 0) p = null;
/*  759 */         this.nsContext.declareNsUri(nsUri, p, true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attWildcardAsAttributes(Map<QName, String> attributes, String fieldName) throws SAXException {
/*  765 */     if (attributes == null) return;
/*  766 */     for (Map.Entry e : attributes.entrySet()) {
/*  767 */       QName n = (QName)e.getKey();
/*  768 */       attribute(n.getNamespaceURI(), n.getLocalPart(), (String)e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void writeXsiNilTrue()
/*      */     throws SAXException, IOException, XMLStreamException
/*      */   {
/*  783 */     getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
/*  784 */     endNamespaceDecls(null);
/*  785 */     attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
/*  786 */     endAttributes();
/*      */   }
/*      */ 
/*      */   public <E> void writeDom(E element, DomHandler<E, ?> domHandler, Object parentBean, String fieldName) throws SAXException {
/*  790 */     Source source = domHandler.marshal(element, this);
/*  791 */     if (this.contentHandlerAdapter == null)
/*  792 */       this.contentHandlerAdapter = new ContentHandlerAdaptor(this);
/*      */     try {
/*  794 */       getIdentityTransformer().transform(source, new SAXResult(this.contentHandlerAdapter));
/*      */     } catch (TransformerException e) {
/*  796 */       reportError(fieldName, e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Transformer getIdentityTransformer() {
/*  801 */     if (this.identityTransformer == null)
/*  802 */       this.identityTransformer = JAXBContextImpl.createTransformer();
/*  803 */     return this.identityTransformer;
/*      */   }
/*      */ 
/*      */   public void setPrefixMapper(NamespacePrefixMapper prefixMapper) {
/*  807 */     this.nsContext.setPrefixMapper(prefixMapper);
/*      */   }
/*      */ 
/*      */   public void startDocument(XmlOutput out, boolean fragment, String schemaLocation, String noNsSchemaLocation)
/*      */     throws IOException, SAXException, XMLStreamException
/*      */   {
/*  819 */     setThreadAffinity();
/*  820 */     pushCoordinator();
/*  821 */     this.nsContext.reset();
/*  822 */     this.nse = this.nsContext.getCurrent();
/*  823 */     if ((this.attachmentMarshaller != null) && (this.attachmentMarshaller.isXOPPackage()))
/*  824 */       out = new MTOMXmlOutput(out);
/*  825 */     this.out = out;
/*  826 */     this.objectsWithId.clear();
/*  827 */     this.idReferencedObjects.clear();
/*  828 */     this.textHasAlreadyPrinted = false;
/*  829 */     this.seenRoot = false;
/*  830 */     this.schemaLocation = schemaLocation;
/*  831 */     this.noNsSchemaLocation = noNsSchemaLocation;
/*  832 */     this.fragment = fragment;
/*  833 */     this.inlineBinaryFlag = false;
/*  834 */     this.expectedMimeType = null;
/*  835 */     this.cycleDetectionStack.reset();
/*      */ 
/*  837 */     out.startDocument(this, fragment, this.knownUri2prefixIndexMap, this.nsContext);
/*      */   }
/*      */ 
/*      */   public void endDocument() throws IOException, SAXException, XMLStreamException {
/*  841 */     this.out.endDocument(this.fragment);
/*      */   }
/*      */ 
/*      */   public void close() {
/*  845 */     this.out = null;
/*  846 */     clearCurrentProperty();
/*  847 */     popCoordinator();
/*  848 */     resetThreadAffinity();
/*      */   }
/*      */ 
/*      */   public void addInscopeBinding(String nsUri, String prefix)
/*      */   {
/*  861 */     this.nsContext.put(nsUri, prefix);
/*      */   }
/*      */ 
/*      */   public String getXMIMEContentType()
/*      */   {
/*  875 */     String v = this.grammar.getXMIMEContentType(this.cycleDetectionStack.peek());
/*  876 */     if (v != null) return v;
/*      */ 
/*  879 */     if (this.expectedMimeType != null) {
/*  880 */       return this.expectedMimeType.toString();
/*      */     }
/*  882 */     return null;
/*      */   }
/*      */ 
/*      */   private void startElement() {
/*  886 */     this.nse = this.nse.push();
/*      */ 
/*  888 */     if (!this.seenRoot)
/*      */     {
/*  890 */       if (this.grammar.getXmlNsSet() != null) {
/*  891 */         for (XmlNs xmlNs : this.grammar.getXmlNsSet()) {
/*  892 */           this.nsContext.declareNsUri(xmlNs.namespaceURI(), xmlNs.prefix() == null ? "" : xmlNs.prefix(), xmlNs.prefix() != null);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  900 */       String[] knownUris = this.nameList.namespaceURIs;
/*  901 */       for (int i = 0; i < knownUris.length; i++) {
/*  902 */         this.knownUri2prefixIndexMap[i] = this.nsContext.declareNsUri(knownUris[i], null, this.nameList.nsUriCannotBeDefaulted[i]);
/*      */       }
/*      */ 
/*  906 */       String[] uris = this.nsContext.getPrefixMapper().getPreDeclaredNamespaceUris();
/*  907 */       if (uris != null) {
/*  908 */         for (String uri : uris) {
/*  909 */           if (uri != null)
/*  910 */             this.nsContext.declareNsUri(uri, null, false);
/*      */         }
/*      */       }
/*  913 */       String[] pairs = this.nsContext.getPrefixMapper().getPreDeclaredNamespaceUris2();
/*  914 */       if (pairs != null) {
/*  915 */         for (int i = 0; i < pairs.length; i += 2) {
/*  916 */           String prefix = pairs[i];
/*  917 */           String nsUri = pairs[(i + 1)];
/*  918 */           if ((prefix != null) && (nsUri != null))
/*      */           {
/*  922 */             this.nsContext.put(nsUri, prefix);
/*      */           }
/*      */         }
/*      */       }
/*  926 */       if ((this.schemaLocation != null) || (this.noNsSchemaLocation != null)) {
/*  927 */         this.nsContext.declareNsUri("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
/*      */       }
/*      */     }
/*      */ 
/*  931 */     this.nsContext.collectionMode = true;
/*  932 */     this.textHasAlreadyPrinted = false;
/*      */   }
/*      */ 
/*      */   public MimeType setExpectedMimeType(MimeType expectedMimeType)
/*      */   {
/*  942 */     MimeType old = this.expectedMimeType;
/*  943 */     this.expectedMimeType = expectedMimeType;
/*  944 */     return old;
/*      */   }
/*      */ 
/*      */   public boolean setInlineBinaryFlag(boolean value)
/*      */   {
/*  953 */     boolean old = this.inlineBinaryFlag;
/*  954 */     this.inlineBinaryFlag = value;
/*  955 */     return old;
/*      */   }
/*      */ 
/*      */   public boolean getInlineBinaryFlag() {
/*  959 */     return this.inlineBinaryFlag;
/*      */   }
/*      */ 
/*      */   public QName setSchemaType(QName st)
/*      */   {
/*  974 */     QName old = this.schemaType;
/*  975 */     this.schemaType = st;
/*  976 */     return old;
/*      */   }
/*      */ 
/*      */   public QName getSchemaType() {
/*  980 */     return this.schemaType;
/*      */   }
/*      */ 
/*      */   public void setObjectIdentityCycleDetection(boolean val) {
/*  984 */     this.cycleDetectionStack.setUseIdentity(val);
/*      */   }
/*      */   public boolean getObjectIdentityCycleDetection() {
/*  987 */     return this.cycleDetectionStack.getUseIdentity();
/*      */   }
/*      */ 
/*      */   void reconcileID() throws SAXException
/*      */   {
/*  992 */     this.idReferencedObjects.removeAll(this.objectsWithId);
/*      */ 
/*  994 */     for (Iterator i$ = this.idReferencedObjects.iterator(); i$.hasNext(); ) { Object idObj = i$.next();
/*      */       try {
/*  996 */         String id = getIdFromObject(idObj);
/*  997 */         reportError(new NotIdentifiableEventImpl(1, Messages.DANGLING_IDREF.format(new Object[] { id }), new ValidationEventLocatorImpl(idObj)));
/*      */       }
/*      */       catch (JAXBException e)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1007 */     this.idReferencedObjects.clear();
/* 1008 */     this.objectsWithId.clear();
/*      */   }
/*      */ 
/*      */   public boolean handleError(Exception e) {
/* 1012 */     return handleError(e, this.cycleDetectionStack.peek(), null);
/*      */   }
/*      */ 
/*      */   public boolean handleError(Exception e, Object source, String fieldName) {
/* 1016 */     return handleEvent(new ValidationEventImpl(1, e.getMessage(), new ValidationEventLocatorExImpl(source, fieldName), e));
/*      */   }
/*      */ 
/*      */   public boolean handleEvent(ValidationEvent event)
/*      */   {
/*      */     try
/*      */     {
/* 1026 */       return this.marshaller.getEventHandler().handleEvent(event);
/*      */     }
/*      */     catch (JAXBException e) {
/* 1029 */       throw new Error(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void reportMissingObjectError(String fieldName) throws SAXException {
/* 1034 */     reportError(new ValidationEventImpl(1, Messages.MISSING_OBJECT.format(new Object[] { fieldName }), getCurrentLocation(fieldName), new NullPointerException()));
/*      */   }
/*      */ 
/*      */   public void errorMissingId(Object obj)
/*      */     throws SAXException
/*      */   {
/* 1045 */     reportError(new ValidationEventImpl(1, Messages.MISSING_ID.format(new Object[] { obj }), new ValidationEventLocatorImpl(obj)));
/*      */   }
/*      */ 
/*      */   public ValidationEventLocator getCurrentLocation(String fieldName)
/*      */   {
/* 1052 */     return new ValidationEventLocatorExImpl(this.cycleDetectionStack.peek(), fieldName);
/*      */   }
/*      */ 
/*      */   protected ValidationEventLocator getLocation() {
/* 1056 */     return getCurrentLocation(null);
/*      */   }
/*      */ 
/*      */   public Property getCurrentProperty()
/*      */   {
/* 1064 */     return (Property)this.currentProperty.get();
/*      */   }
/*      */ 
/*      */   public void clearCurrentProperty()
/*      */   {
/* 1071 */     if (this.currentProperty != null)
/* 1072 */       this.currentProperty.remove();
/*      */   }
/*      */ 
/*      */   public static XMLSerializer getInstance()
/*      */   {
/* 1081 */     return (XMLSerializer)Coordinator._getInstance();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.XMLSerializer
 * JD-Core Version:    0.6.2
 */