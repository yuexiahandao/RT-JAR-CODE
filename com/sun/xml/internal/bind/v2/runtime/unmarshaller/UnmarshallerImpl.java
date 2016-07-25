/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.IDResolver;
/*     */ import com.sun.xml.internal.bind.api.ClassResolver;
/*     */ import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
/*     */ import com.sun.xml.internal.bind.unmarshaller.InfosetScanner;
/*     */ import com.sun.xml.internal.bind.unmarshaller.Messages;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.runtime.AssociationMap;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller.Listener;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public final class UnmarshallerImpl extends AbstractUnmarshallerImpl
/*     */   implements ValidationEventHandler, Closeable
/*     */ {
/*     */   protected final JAXBContextImpl context;
/*     */   private Schema schema;
/*     */   public final UnmarshallingContext coordinator;
/*     */   private Unmarshaller.Listener externalListener;
/*     */   private AttachmentUnmarshaller attachmentUnmarshaller;
/* 102 */   private IDResolver idResolver = new DefaultIDResolver();
/*     */ 
/* 154 */   private static final DefaultHandler dummyHandler = new DefaultHandler();
/*     */   public static final String FACTORY = "com.sun.xml.internal.bind.ObjectFactory";
/*     */ 
/*     */   public UnmarshallerImpl(JAXBContextImpl context, AssociationMap assoc)
/*     */   {
/* 105 */     this.context = context;
/* 106 */     this.coordinator = new UnmarshallingContext(this, assoc);
/*     */     try
/*     */     {
/* 109 */       setEventHandler(this);
/*     */     } catch (JAXBException e) {
/* 111 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public UnmarshallerHandler getUnmarshallerHandler() {
/* 116 */     return getUnmarshallerHandler(true, null);
/*     */   }
/*     */ 
/*     */   private SAXConnector getUnmarshallerHandler(boolean intern, JaxBeanInfo expectedType) {
/* 120 */     XmlVisitor h = createUnmarshallerHandler(null, false, expectedType);
/* 121 */     if (intern)
/* 122 */       h = new InterningXmlVisitor(h);
/* 123 */     return new SAXConnector(h, null);
/*     */   }
/*     */ 
/*     */   public final XmlVisitor createUnmarshallerHandler(InfosetScanner scanner, boolean inplace, JaxBeanInfo expectedType)
/*     */   {
/* 141 */     this.coordinator.reset(scanner, inplace, expectedType, this.idResolver);
/* 142 */     XmlVisitor unmarshaller = this.coordinator;
/*     */ 
/* 145 */     if (this.schema != null) {
/* 146 */       unmarshaller = new ValidatingUnmarshaller(this.schema, unmarshaller);
/*     */     }
/* 148 */     if ((this.attachmentUnmarshaller != null) && (this.attachmentUnmarshaller.isXOPPackage())) {
/* 149 */       unmarshaller = new MTOMDecorator(this, unmarshaller, this.attachmentUnmarshaller);
/*     */     }
/* 151 */     return unmarshaller;
/*     */   }
/*     */ 
/*     */   public static boolean needsInterning(XMLReader reader)
/*     */   {
/*     */     try
/*     */     {
/* 159 */       reader.setFeature("http://xml.org/sax/features/string-interning", true);
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/*     */     try {
/* 165 */       if (reader.getFeature("http://xml.org/sax/features/string-interning"))
/* 166 */         return false;
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   protected Object unmarshal(XMLReader reader, InputSource source) throws JAXBException {
/* 175 */     return unmarshal0(reader, source, null);
/*     */   }
/*     */ 
/*     */   protected <T> JAXBElement<T> unmarshal(XMLReader reader, InputSource source, Class<T> expectedType) throws JAXBException {
/* 179 */     if (expectedType == null)
/* 180 */       throw new IllegalArgumentException();
/* 181 */     return (JAXBElement)unmarshal0(reader, source, getBeanInfo(expectedType));
/*     */   }
/*     */ 
/*     */   private Object unmarshal0(XMLReader reader, InputSource source, JaxBeanInfo expectedType) throws JAXBException
/*     */   {
/* 186 */     SAXConnector connector = getUnmarshallerHandler(needsInterning(reader), expectedType);
/*     */ 
/* 188 */     reader.setContentHandler(connector);
/*     */ 
/* 200 */     reader.setErrorHandler(this.coordinator);
/*     */     try
/*     */     {
/* 203 */       reader.parse(source);
/*     */     } catch (IOException e) {
/* 205 */       this.coordinator.clearStates();
/* 206 */       throw new UnmarshalException(e);
/*     */     } catch (SAXException e) {
/* 208 */       this.coordinator.clearStates();
/* 209 */       throw createUnmarshalException(e);
/*     */     }
/*     */ 
/* 212 */     Object result = connector.getResult();
/*     */ 
/* 217 */     reader.setContentHandler(dummyHandler);
/* 218 */     reader.setErrorHandler(dummyHandler);
/*     */ 
/* 220 */     return result;
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(Source source, Class<T> expectedType) throws JAXBException
/*     */   {
/* 225 */     if ((source instanceof SAXSource)) {
/* 226 */       SAXSource ss = (SAXSource)source;
/*     */ 
/* 228 */       XMLReader reader = ss.getXMLReader();
/* 229 */       if (reader == null) {
/* 230 */         reader = getXMLReader();
/*     */       }
/* 232 */       return unmarshal(reader, ss.getInputSource(), expectedType);
/*     */     }
/* 234 */     if ((source instanceof StreamSource)) {
/* 235 */       return unmarshal(getXMLReader(), streamSourceToInputSource((StreamSource)source), expectedType);
/*     */     }
/* 237 */     if ((source instanceof DOMSource)) {
/* 238 */       return unmarshal(((DOMSource)source).getNode(), expectedType);
/*     */     }
/*     */ 
/* 241 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public Object unmarshal0(Source source, JaxBeanInfo expectedType) throws JAXBException {
/* 245 */     if ((source instanceof SAXSource)) {
/* 246 */       SAXSource ss = (SAXSource)source;
/*     */ 
/* 248 */       XMLReader reader = ss.getXMLReader();
/* 249 */       if (reader == null) {
/* 250 */         reader = getXMLReader();
/*     */       }
/* 252 */       return unmarshal0(reader, ss.getInputSource(), expectedType);
/*     */     }
/* 254 */     if ((source instanceof StreamSource)) {
/* 255 */       return unmarshal0(getXMLReader(), streamSourceToInputSource((StreamSource)source), expectedType);
/*     */     }
/* 257 */     if ((source instanceof DOMSource)) {
/* 258 */       return unmarshal0(((DOMSource)source).getNode(), expectedType);
/*     */     }
/*     */ 
/* 261 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public final ValidationEventHandler getEventHandler()
/*     */   {
/*     */     try
/*     */     {
/* 268 */       return super.getEventHandler();
/*     */     } catch (JAXBException e) {
/*     */     }
/* 271 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   public final boolean hasEventHandler()
/*     */   {
/* 281 */     return getEventHandler() != this;
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(Node node, Class<T> expectedType) throws JAXBException
/*     */   {
/* 286 */     if (expectedType == null)
/* 287 */       throw new IllegalArgumentException();
/* 288 */     return (JAXBElement)unmarshal0(node, getBeanInfo(expectedType));
/*     */   }
/*     */ 
/*     */   public final Object unmarshal(Node node) throws JAXBException {
/* 292 */     return unmarshal0(node, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final Object unmarshal(SAXSource source) throws JAXBException
/*     */   {
/* 298 */     return super.unmarshal(source);
/*     */   }
/*     */ 
/*     */   public final Object unmarshal0(Node node, JaxBeanInfo expectedType) throws JAXBException {
/*     */     try {
/* 303 */       DOMScanner scanner = new DOMScanner();
/*     */ 
/* 305 */       InterningXmlVisitor handler = new InterningXmlVisitor(createUnmarshallerHandler(null, false, expectedType));
/* 306 */       scanner.setContentHandler(new SAXConnector(handler, scanner));
/*     */ 
/* 308 */       if (node.getNodeType() == 1) {
/* 309 */         scanner.scan((Element)node);
/*     */       }
/* 311 */       else if (node.getNodeType() == 9) {
/* 312 */         scanner.scan((Document)node);
/*     */       }
/*     */       else {
/* 315 */         throw new IllegalArgumentException("Unexpected node type: " + node);
/*     */       }
/* 317 */       Object retVal = handler.getContext().getResult();
/* 318 */       handler.getContext().clearResult();
/* 319 */       return retVal;
/*     */     } catch (SAXException e) {
/* 321 */       throw createUnmarshalException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object unmarshal(XMLStreamReader reader) throws JAXBException
/*     */   {
/* 327 */     return unmarshal0(reader, null);
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(XMLStreamReader reader, Class<T> expectedType) throws JAXBException
/*     */   {
/* 332 */     if (expectedType == null)
/* 333 */       throw new IllegalArgumentException();
/* 334 */     return (JAXBElement)unmarshal0(reader, getBeanInfo(expectedType));
/*     */   }
/*     */ 
/*     */   public Object unmarshal0(XMLStreamReader reader, JaxBeanInfo expectedType) throws JAXBException {
/* 338 */     if (reader == null) {
/* 339 */       throw new IllegalArgumentException(Messages.format("Unmarshaller.NullReader"));
/*     */     }
/*     */ 
/* 343 */     int eventType = reader.getEventType();
/* 344 */     if ((eventType != 1) && (eventType != 7))
/*     */     {
/* 347 */       throw new IllegalStateException(Messages.format("Unmarshaller.IllegalReaderState", Integer.valueOf(eventType)));
/*     */     }
/*     */ 
/* 351 */     XmlVisitor h = createUnmarshallerHandler(null, false, expectedType);
/* 352 */     StAXConnector connector = StAXStreamConnector.create(reader, h);
/*     */     try
/*     */     {
/* 355 */       connector.bridge();
/*     */     } catch (XMLStreamException e) {
/* 357 */       throw handleStreamException(e);
/*     */     }
/*     */ 
/* 360 */     Object retVal = h.getContext().getResult();
/* 361 */     h.getContext().clearResult();
/* 362 */     return retVal;
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(XMLEventReader reader, Class<T> expectedType) throws JAXBException
/*     */   {
/* 367 */     if (expectedType == null)
/* 368 */       throw new IllegalArgumentException();
/* 369 */     return (JAXBElement)unmarshal0(reader, getBeanInfo(expectedType));
/*     */   }
/*     */ 
/*     */   public Object unmarshal(XMLEventReader reader) throws JAXBException
/*     */   {
/* 374 */     return unmarshal0(reader, null);
/*     */   }
/*     */ 
/*     */   private Object unmarshal0(XMLEventReader reader, JaxBeanInfo expectedType) throws JAXBException {
/* 378 */     if (reader == null) {
/* 379 */       throw new IllegalArgumentException(Messages.format("Unmarshaller.NullReader"));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 384 */       XMLEvent event = reader.peek();
/*     */ 
/* 386 */       if ((!event.isStartElement()) && (!event.isStartDocument()))
/*     */       {
/* 388 */         throw new IllegalStateException(Messages.format("Unmarshaller.IllegalReaderState", Integer.valueOf(event.getEventType())));
/*     */       }
/*     */ 
/* 394 */       boolean isZephyr = reader.getClass().getName().equals("com.sun.xml.internal.stream.XMLReaderImpl");
/* 395 */       XmlVisitor h = createUnmarshallerHandler(null, false, expectedType);
/* 396 */       if (!isZephyr)
/* 397 */         h = new InterningXmlVisitor(h);
/* 398 */       new StAXEventConnector(reader, h).bridge();
/* 399 */       return h.getContext().getResult();
/*     */     } catch (XMLStreamException e) {
/* 401 */       throw handleStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object unmarshal0(InputStream input, JaxBeanInfo expectedType) throws JAXBException {
/* 406 */     return unmarshal0(getXMLReader(), new InputSource(input), expectedType);
/*     */   }
/*     */ 
/*     */   private static JAXBException handleStreamException(XMLStreamException e)
/*     */   {
/* 416 */     Throwable ne = e.getNestedException();
/* 417 */     if ((ne instanceof JAXBException))
/* 418 */       return (JAXBException)ne;
/* 419 */     if ((ne instanceof SAXException))
/* 420 */       return new UnmarshalException(ne);
/* 421 */     return new UnmarshalException(e);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws PropertyException
/*     */   {
/* 426 */     if (name.equals(IDResolver.class.getName())) {
/* 427 */       return this.idResolver;
/*     */     }
/* 429 */     return super.getProperty(name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value) throws PropertyException
/*     */   {
/* 434 */     if (name.equals("com.sun.xml.internal.bind.ObjectFactory")) {
/* 435 */       this.coordinator.setFactories(value);
/* 436 */       return;
/*     */     }
/* 438 */     if (name.equals(IDResolver.class.getName())) {
/* 439 */       this.idResolver = ((IDResolver)value);
/* 440 */       return;
/*     */     }
/* 442 */     if (name.equals(ClassResolver.class.getName())) {
/* 443 */       this.coordinator.classResolver = ((ClassResolver)value);
/* 444 */       return;
/*     */     }
/* 446 */     if (name.equals(ClassLoader.class.getName())) {
/* 447 */       this.coordinator.classLoader = ((ClassLoader)value);
/* 448 */       return;
/*     */     }
/* 450 */     super.setProperty(name, value);
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema schema)
/*     */   {
/* 457 */     this.schema = schema;
/*     */   }
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 462 */     return this.schema;
/*     */   }
/*     */ 
/*     */   public AttachmentUnmarshaller getAttachmentUnmarshaller()
/*     */   {
/* 467 */     return this.attachmentUnmarshaller;
/*     */   }
/*     */ 
/*     */   public void setAttachmentUnmarshaller(AttachmentUnmarshaller au)
/*     */   {
/* 472 */     this.attachmentUnmarshaller = au;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean isValidating()
/*     */   {
/* 480 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setValidating(boolean validating)
/*     */   {
/* 488 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter)
/*     */   {
/* 493 */     if (type == null)
/* 494 */       throw new IllegalArgumentException();
/* 495 */     this.coordinator.putAdapter(type, adapter);
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type)
/*     */   {
/* 500 */     if (type == null)
/* 501 */       throw new IllegalArgumentException();
/* 502 */     if (this.coordinator.containsAdapter(type))
/*     */     {
/* 504 */       return this.coordinator.getAdapter(type);
/*     */     }
/* 506 */     return null;
/*     */   }
/*     */ 
/*     */   public UnmarshalException createUnmarshalException(SAXException e)
/*     */   {
/* 512 */     return super.createUnmarshalException(e);
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(ValidationEvent event)
/*     */   {
/* 520 */     return event.getSeverity() != 2;
/*     */   }
/*     */ 
/*     */   private static InputSource streamSourceToInputSource(StreamSource ss) {
/* 524 */     InputSource is = new InputSource();
/* 525 */     is.setSystemId(ss.getSystemId());
/* 526 */     is.setByteStream(ss.getInputStream());
/* 527 */     is.setCharacterStream(ss.getReader());
/*     */ 
/* 529 */     return is;
/*     */   }
/*     */ 
/*     */   public <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz) throws JAXBException {
/* 533 */     return this.context.getBeanInfo(clazz, true);
/*     */   }
/*     */ 
/*     */   public Unmarshaller.Listener getListener()
/*     */   {
/* 538 */     return this.externalListener;
/*     */   }
/*     */ 
/*     */   public void setListener(Unmarshaller.Listener listener)
/*     */   {
/* 543 */     this.externalListener = listener;
/*     */   }
/*     */ 
/*     */   public UnmarshallingContext getContext() {
/* 547 */     return this.coordinator;
/*     */   }
/*     */ 
/*     */   protected void finalize() throws Throwable
/*     */   {
/*     */     try
/*     */     {
/* 554 */       ClassFactory.cleanCache();
/*     */     } finally {
/* 556 */       super.finalize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 565 */     ClassFactory.cleanCache();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl
 * JD-Core Version:    0.6.2
 */