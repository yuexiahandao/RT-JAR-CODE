/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
/*     */ import com.sun.xml.internal.bind.marshaller.DataWriter;
/*     */ import com.sun.xml.internal.bind.marshaller.DumbEscapeHandler;
/*     */ import com.sun.xml.internal.bind.marshaller.MinimumEscapeHandler;
/*     */ import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
/*     */ import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
/*     */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*     */ import com.sun.xml.internal.bind.marshaller.XMLWriter;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.C14nXmlOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.ForkXmlOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.IndentingUTF8XmlOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.XMLEventWriterOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.XMLStreamWriterOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.XmlOutput;
/*     */ import com.sun.xml.internal.bind.v2.util.FatalAdapter;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.Marshaller.Listener;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.bind.helpers.AbstractMarshallerImpl;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public final class MarshallerImpl extends AbstractMarshallerImpl
/*     */   implements ValidationEventHandler
/*     */ {
/*  99 */   private String indent = "    ";
/*     */ 
/* 102 */   private NamespacePrefixMapper prefixMapper = null;
/*     */ 
/* 105 */   private CharacterEscapeHandler escapeHandler = null;
/*     */ 
/* 108 */   private String header = null;
/*     */   final JAXBContextImpl context;
/*     */   protected final XMLSerializer serializer;
/*     */   private Schema schema;
/* 121 */   private Marshaller.Listener externalListener = null;
/*     */   private boolean c14nSupport;
/*     */   private Flushable toBeFlushed;
/*     */   private Closeable toBeClosed;
/*     */   protected static final String INDENT_STRING = "com.sun.xml.internal.bind.indentString";
/*     */   protected static final String PREFIX_MAPPER = "com.sun.xml.internal.bind.namespacePrefixMapper";
/*     */   protected static final String ENCODING_HANDLER = "com.sun.xml.internal.bind.characterEscapeHandler";
/*     */   protected static final String ENCODING_HANDLER2 = "com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler";
/*     */   protected static final String XMLDECLARATION = "com.sun.xml.internal.bind.xmlDeclaration";
/*     */   protected static final String XML_HEADERS = "com.sun.xml.internal.bind.xmlHeaders";
/*     */   protected static final String C14N = "com.sun.xml.internal.bind.c14n";
/*     */   protected static final String OBJECT_IDENTITY_CYCLE_DETECTION = "com.sun.xml.internal.bind.objectIdentitityCycleDetection";
/*     */ 
/*     */   public MarshallerImpl(JAXBContextImpl c, AssociationMap assoc)
/*     */   {
/* 136 */     this.context = c;
/* 137 */     this.serializer = new XMLSerializer(this);
/* 138 */     this.c14nSupport = this.context.c14nSupport;
/*     */     try
/*     */     {
/* 141 */       setEventHandler(this);
/*     */     } catch (JAXBException e) {
/* 143 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JAXBContextImpl getContext() {
/* 148 */     return this.context;
/*     */   }
/*     */ 
/*     */   public void marshal(Object obj, OutputStream out, NamespaceContext inscopeNamespace)
/*     */     throws JAXBException
/*     */   {
/* 158 */     write(obj, createWriter(out), new StAXPostInitAction(inscopeNamespace, this.serializer));
/*     */   }
/*     */ 
/*     */   public void marshal(Object obj, XMLStreamWriter writer) throws JAXBException
/*     */   {
/* 163 */     write(obj, XMLStreamWriterOutput.create(writer, this.context), new StAXPostInitAction(writer, this.serializer));
/*     */   }
/*     */ 
/*     */   public void marshal(Object obj, XMLEventWriter writer) throws JAXBException
/*     */   {
/* 168 */     write(obj, new XMLEventWriterOutput(writer), new StAXPostInitAction(writer, this.serializer));
/*     */   }
/*     */ 
/*     */   public void marshal(Object obj, XmlOutput output) throws JAXBException {
/* 172 */     write(obj, output, null);
/*     */   }
/*     */ 
/*     */   final XmlOutput createXmlOutput(Result result)
/*     */     throws JAXBException
/*     */   {
/* 179 */     if ((result instanceof SAXResult)) {
/* 180 */       return new SAXOutput(((SAXResult)result).getHandler());
/*     */     }
/* 182 */     if ((result instanceof DOMResult)) {
/* 183 */       Node node = ((DOMResult)result).getNode();
/*     */ 
/* 185 */       if (node == null) {
/* 186 */         Document doc = JAXBContextImpl.createDom();
/* 187 */         ((DOMResult)result).setNode(doc);
/* 188 */         return new SAXOutput(new SAX2DOMEx(doc));
/*     */       }
/* 190 */       return new SAXOutput(new SAX2DOMEx(node));
/*     */     }
/*     */ 
/* 193 */     if ((result instanceof StreamResult)) {
/* 194 */       StreamResult sr = (StreamResult)result;
/*     */ 
/* 196 */       if (sr.getWriter() != null)
/* 197 */         return createWriter(sr.getWriter());
/* 198 */       if (sr.getOutputStream() != null)
/* 199 */         return createWriter(sr.getOutputStream());
/* 200 */       if (sr.getSystemId() != null) {
/* 201 */         String fileURL = sr.getSystemId();
/*     */         try
/*     */         {
/* 204 */           fileURL = new URI(fileURL).getPath();
/*     */         }
/*     */         catch (URISyntaxException use)
/*     */         {
/*     */         }
/*     */         try {
/* 210 */           FileOutputStream fos = new FileOutputStream(fileURL);
/* 211 */           assert (this.toBeClosed == null);
/* 212 */           this.toBeClosed = fos;
/* 213 */           return createWriter(fos);
/*     */         } catch (IOException e) {
/* 215 */           throw new MarshalException(e);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 221 */     throw new MarshalException(Messages.UNSUPPORTED_RESULT.format(new Object[0]));
/*     */   }
/*     */ 
/*     */   final Runnable createPostInitAction(Result result)
/*     */   {
/* 228 */     if ((result instanceof DOMResult)) {
/* 229 */       Node node = ((DOMResult)result).getNode();
/* 230 */       return new DomPostInitAction(node, this.serializer);
/*     */     }
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */   public void marshal(Object target, Result result) throws JAXBException {
/* 236 */     write(target, createXmlOutput(result), createPostInitAction(result));
/*     */   }
/*     */ 
/*     */   protected final <T> void write(Name rootTagName, JaxBeanInfo<T> bi, T obj, XmlOutput out, Runnable postInitAction)
/*     */     throws JAXBException
/*     */   {
/*     */     try
/*     */     {
/*     */       return;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */     }
/*     */     finally
/*     */     {
/* 270 */       cleanUp();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void write(Object obj, XmlOutput out, Runnable postInitAction)
/*     */     throws JAXBException
/*     */   {
/*     */     try
/*     */     {
/* 279 */       if (obj == null) {
/* 280 */         throw new IllegalArgumentException(Messages.NOT_MARSHALLABLE.format(new Object[0]));
/*     */       }
/* 282 */       if (this.schema != null)
/*     */       {
/* 284 */         ValidatorHandler validator = this.schema.newValidatorHandler();
/* 285 */         validator.setErrorHandler(new FatalAdapter(this.serializer));
/*     */ 
/* 287 */         XMLFilterImpl f = new XMLFilterImpl()
/*     */         {
/*     */           public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 290 */             super.startPrefixMapping(prefix.intern(), uri.intern());
/*     */           }
/*     */         };
/* 293 */         f.setContentHandler(validator);
/* 294 */         out = new ForkXmlOutput(new SAXOutput(f)
/*     */         {
/*     */           public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, IOException, XMLStreamException {
/* 297 */             super.startDocument(serializer, false, nsUriIndex2prefixIndex, nsContext);
/*     */           }
/*     */ 
/*     */           public void endDocument(boolean fragment) throws SAXException, IOException, XMLStreamException {
/* 301 */             super.endDocument(false);
/*     */           }
/*     */         }
/*     */         , out);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 307 */         prewrite(out, isFragment(), postInitAction);
/* 308 */         this.serializer.childAsRoot(obj);
/*     */       }
/*     */       catch (SAXException e) {
/* 311 */         throw new MarshalException(e);
/*     */       } catch (IOException e) {
/* 313 */         throw new MarshalException(e);
/*     */       } catch (XMLStreamException e) {
/* 315 */         throw new MarshalException(e);
/*     */       } finally {
/* 317 */         this.serializer.close();
/*     */       }
/*     */     } finally {
/* 320 */       cleanUp();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void cleanUp() {
/* 325 */     if (this.toBeFlushed != null)
/*     */       try {
/* 327 */         this.toBeFlushed.flush();
/*     */       }
/*     */       catch (IOException e) {
/*     */       }
/* 331 */     if (this.toBeClosed != null)
/*     */       try {
/* 333 */         this.toBeClosed.close();
/*     */       }
/*     */       catch (IOException e) {
/*     */       }
/* 337 */     this.toBeFlushed = null;
/* 338 */     this.toBeClosed = null;
/*     */   }
/*     */ 
/*     */   private void prewrite(XmlOutput out, boolean fragment, Runnable postInitAction)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 344 */     this.serializer.startDocument(out, fragment, getSchemaLocation(), getNoNSSchemaLocation());
/* 345 */     if (postInitAction != null) postInitAction.run();
/* 346 */     if (this.prefixMapper != null)
/*     */     {
/* 348 */       String[] decls = this.prefixMapper.getContextualNamespaceDecls();
/* 349 */       if (decls != null) {
/* 350 */         for (int i = 0; i < decls.length; i += 2) {
/* 351 */           String prefix = decls[i];
/* 352 */           String nsUri = decls[(i + 1)];
/* 353 */           if ((nsUri != null) && (prefix != null))
/* 354 */             this.serializer.addInscopeBinding(nsUri, prefix);
/*     */         }
/*     */       }
/*     */     }
/* 358 */     this.serializer.setPrefixMapper(this.prefixMapper);
/*     */   }
/*     */ 
/*     */   private void postwrite() throws IOException, SAXException, XMLStreamException {
/* 362 */     this.serializer.endDocument();
/* 363 */     this.serializer.reconcileID();
/*     */   }
/*     */ 
/*     */   protected CharacterEscapeHandler createEscapeHandler(String encoding)
/*     */   {
/* 374 */     if (this.escapeHandler != null)
/*     */     {
/* 376 */       return this.escapeHandler;
/*     */     }
/* 378 */     if (encoding.startsWith("UTF"))
/*     */     {
/* 381 */       return MinimumEscapeHandler.theInstance;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 386 */       return new NioEscapeHandler(getJavaEncoding(encoding));
/*     */     } catch (Throwable e) {
/*     */     }
/* 389 */     return DumbEscapeHandler.theInstance;
/*     */   }
/*     */ 
/*     */   public XmlOutput createWriter(Writer w, String encoding)
/*     */   {
/* 395 */     if (!(w instanceof BufferedWriter)) {
/* 396 */       w = new BufferedWriter(w);
/*     */     }
/* 398 */     assert (this.toBeFlushed == null);
/* 399 */     this.toBeFlushed = w;
/*     */ 
/* 401 */     CharacterEscapeHandler ceh = createEscapeHandler(encoding);
/*     */     XMLWriter xw;
/*     */     XMLWriter xw;
/* 404 */     if (isFormattedOutput()) {
/* 405 */       DataWriter d = new DataWriter(w, encoding, ceh);
/* 406 */       d.setIndentStep(this.indent);
/* 407 */       xw = d;
/*     */     } else {
/* 409 */       xw = new XMLWriter(w, encoding, ceh);
/*     */     }
/* 411 */     xw.setXmlDecl(!isFragment());
/* 412 */     xw.setHeader(this.header);
/* 413 */     return new SAXOutput(xw);
/*     */   }
/*     */ 
/*     */   public XmlOutput createWriter(Writer w) {
/* 417 */     return createWriter(w, getEncoding());
/*     */   }
/*     */ 
/*     */   public XmlOutput createWriter(OutputStream os) throws JAXBException {
/* 421 */     return createWriter(os, getEncoding());
/*     */   }
/*     */ 
/*     */   public XmlOutput createWriter(OutputStream os, String encoding)
/*     */     throws JAXBException
/*     */   {
/* 429 */     if (encoding.equals("UTF-8")) {
/* 430 */       Encoded[] table = this.context.getUTF8NameTable();
/*     */       UTF8XmlOutput out;
/*     */       UTF8XmlOutput out;
/* 432 */       if (isFormattedOutput()) {
/* 433 */         out = new IndentingUTF8XmlOutput(os, this.indent, table, this.escapeHandler);
/*     */       }
/*     */       else
/*     */       {
/*     */         UTF8XmlOutput out;
/* 435 */         if (this.c14nSupport)
/* 436 */           out = new C14nXmlOutput(os, table, this.context.c14nSupport, this.escapeHandler);
/*     */         else
/* 438 */           out = new UTF8XmlOutput(os, table, this.escapeHandler);
/*     */       }
/* 440 */       if (this.header != null)
/* 441 */         out.setHeader(this.header);
/* 442 */       return out;
/*     */     }
/*     */     try
/*     */     {
/* 446 */       return createWriter(new OutputStreamWriter(os, getJavaEncoding(encoding)), encoding);
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/* 450 */       throw new MarshalException(Messages.UNSUPPORTED_ENCODING.format(new Object[] { encoding }), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws PropertyException
/*     */   {
/* 459 */     if ("com.sun.xml.internal.bind.indentString".equals(name))
/* 460 */       return this.indent;
/* 461 */     if (("com.sun.xml.internal.bind.characterEscapeHandler".equals(name)) || ("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler".equals(name)))
/* 462 */       return this.escapeHandler;
/* 463 */     if ("com.sun.xml.internal.bind.namespacePrefixMapper".equals(name))
/* 464 */       return this.prefixMapper;
/* 465 */     if ("com.sun.xml.internal.bind.xmlDeclaration".equals(name))
/* 466 */       return Boolean.valueOf(!isFragment());
/* 467 */     if ("com.sun.xml.internal.bind.xmlHeaders".equals(name))
/* 468 */       return this.header;
/* 469 */     if ("com.sun.xml.internal.bind.c14n".equals(name))
/* 470 */       return Boolean.valueOf(this.c14nSupport);
/* 471 */     if ("com.sun.xml.internal.bind.objectIdentitityCycleDetection".equals(name)) {
/* 472 */       return Boolean.valueOf(this.serializer.getObjectIdentityCycleDetection());
/*     */     }
/* 474 */     return super.getProperty(name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value) throws PropertyException
/*     */   {
/* 479 */     if ("com.sun.xml.internal.bind.indentString".equals(name)) {
/* 480 */       checkString(name, value);
/* 481 */       this.indent = ((String)value);
/* 482 */       return;
/*     */     }
/* 484 */     if (("com.sun.xml.internal.bind.characterEscapeHandler".equals(name)) || ("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler".equals(name))) {
/* 485 */       if (!(value instanceof CharacterEscapeHandler)) {
/* 486 */         throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, CharacterEscapeHandler.class.getName(), value.getClass().getName() }));
/*     */       }
/*     */ 
/* 491 */       this.escapeHandler = ((CharacterEscapeHandler)value);
/* 492 */       return;
/*     */     }
/* 494 */     if ("com.sun.xml.internal.bind.namespacePrefixMapper".equals(name)) {
/* 495 */       if (!(value instanceof NamespacePrefixMapper)) {
/* 496 */         throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, NamespacePrefixMapper.class.getName(), value.getClass().getName() }));
/*     */       }
/*     */ 
/* 501 */       this.prefixMapper = ((NamespacePrefixMapper)value);
/* 502 */       return;
/*     */     }
/* 504 */     if ("com.sun.xml.internal.bind.xmlDeclaration".equals(name)) {
/* 505 */       checkBoolean(name, value);
/*     */ 
/* 508 */       super.setProperty("jaxb.fragment", Boolean.valueOf(!((Boolean)value).booleanValue()));
/* 509 */       return;
/*     */     }
/* 511 */     if ("com.sun.xml.internal.bind.xmlHeaders".equals(name)) {
/* 512 */       checkString(name, value);
/* 513 */       this.header = ((String)value);
/* 514 */       return;
/*     */     }
/* 516 */     if ("com.sun.xml.internal.bind.c14n".equals(name)) {
/* 517 */       checkBoolean(name, value);
/* 518 */       this.c14nSupport = ((Boolean)value).booleanValue();
/* 519 */       return;
/*     */     }
/* 521 */     if ("com.sun.xml.internal.bind.objectIdentitityCycleDetection".equals(name)) {
/* 522 */       checkBoolean(name, value);
/* 523 */       this.serializer.setObjectIdentityCycleDetection(((Boolean)value).booleanValue());
/* 524 */       return;
/*     */     }
/*     */ 
/* 527 */     super.setProperty(name, value);
/*     */   }
/*     */ 
/*     */   private void checkBoolean(String name, Object value)
/*     */     throws PropertyException
/*     */   {
/* 534 */     if (!(value instanceof Boolean))
/* 535 */       throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, Boolean.class.getName(), value.getClass().getName() }));
/*     */   }
/*     */ 
/*     */   private void checkString(String name, Object value)
/*     */     throws PropertyException
/*     */   {
/* 546 */     if (!(value instanceof String))
/* 547 */       throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, String.class.getName(), value.getClass().getName() }));
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter)
/*     */   {
/* 556 */     if (type == null)
/* 557 */       throw new IllegalArgumentException();
/* 558 */     this.serializer.putAdapter(type, adapter);
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type)
/*     */   {
/* 563 */     if (type == null)
/* 564 */       throw new IllegalArgumentException();
/* 565 */     if (this.serializer.containsAdapter(type))
/*     */     {
/* 567 */       return this.serializer.getAdapter(type);
/*     */     }
/* 569 */     return null;
/*     */   }
/*     */ 
/*     */   public void setAttachmentMarshaller(AttachmentMarshaller am)
/*     */   {
/* 574 */     this.serializer.attachmentMarshaller = am;
/*     */   }
/*     */ 
/*     */   public AttachmentMarshaller getAttachmentMarshaller()
/*     */   {
/* 579 */     return this.serializer.attachmentMarshaller;
/*     */   }
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 584 */     return this.schema;
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema s)
/*     */   {
/* 589 */     this.schema = s;
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(ValidationEvent event)
/*     */   {
/* 597 */     return false;
/*     */   }
/*     */ 
/*     */   public Marshaller.Listener getListener()
/*     */   {
/* 602 */     return this.externalListener;
/*     */   }
/*     */ 
/*     */   public void setListener(Marshaller.Listener listener)
/*     */   {
/* 607 */     this.externalListener = listener;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.MarshallerImpl
 * JD-Core Version:    0.6.2
 */