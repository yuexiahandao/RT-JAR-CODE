/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Marshaller.Listener;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ 
/*     */ public abstract class AbstractMarshallerImpl
/*     */   implements Marshaller
/*     */ {
/*  70 */   private ValidationEventHandler eventHandler = new DefaultValidationEventHandler();
/*     */ 
/*  77 */   private String encoding = "UTF-8";
/*     */ 
/*  80 */   private String schemaLocation = null;
/*     */ 
/*  83 */   private String noNSSchemaLocation = null;
/*     */ 
/*  86 */   private boolean formattedOutput = false;
/*     */ 
/*  89 */   private boolean fragment = false;
/*     */ 
/* 243 */   static String[] aliases = { "UTF-8", "UTF8", "UTF-16", "Unicode", "UTF-16BE", "UnicodeBigUnmarked", "UTF-16LE", "UnicodeLittleUnmarked", "US-ASCII", "ASCII", "TIS-620", "TIS620", "ISO-10646-UCS-2", "Unicode", "EBCDIC-CP-US", "cp037", "EBCDIC-CP-CA", "cp037", "EBCDIC-CP-NL", "cp037", "EBCDIC-CP-WT", "cp037", "EBCDIC-CP-DK", "cp277", "EBCDIC-CP-NO", "cp277", "EBCDIC-CP-FI", "cp278", "EBCDIC-CP-SE", "cp278", "EBCDIC-CP-IT", "cp280", "EBCDIC-CP-ES", "cp284", "EBCDIC-CP-GB", "cp285", "EBCDIC-CP-FR", "cp297", "EBCDIC-CP-AR1", "cp420", "EBCDIC-CP-HE", "cp424", "EBCDIC-CP-BE", "cp500", "EBCDIC-CP-CH", "cp500", "EBCDIC-CP-ROECE", "cp870", "EBCDIC-CP-YU", "cp870", "EBCDIC-CP-IS", "cp871", "EBCDIC-CP-AR2", "cp918" };
/*     */ 
/*     */   public final void marshal(Object obj, OutputStream os)
/*     */     throws JAXBException
/*     */   {
/*  94 */     checkNotNull(obj, "obj", os, "os");
/*  95 */     marshal(obj, new StreamResult(os));
/*     */   }
/*     */ 
/*     */   public void marshal(Object jaxbElement, File output) throws JAXBException {
/*  99 */     checkNotNull(jaxbElement, "jaxbElement", output, "output");
/*     */     try {
/* 101 */       OutputStream os = new BufferedOutputStream(new FileOutputStream(output));
/*     */       try {
/* 103 */         marshal(jaxbElement, new StreamResult(os));
/*     */       } finally {
/* 105 */         os.close();
/*     */       }
/*     */     } catch (IOException e) {
/* 108 */       throw new JAXBException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void marshal(Object obj, Writer w)
/*     */     throws JAXBException
/*     */   {
/* 115 */     checkNotNull(obj, "obj", w, "writer");
/* 116 */     marshal(obj, new StreamResult(w));
/*     */   }
/*     */ 
/*     */   public final void marshal(Object obj, ContentHandler handler)
/*     */     throws JAXBException
/*     */   {
/* 122 */     checkNotNull(obj, "obj", handler, "handler");
/* 123 */     marshal(obj, new SAXResult(handler));
/*     */   }
/*     */ 
/*     */   public final void marshal(Object obj, Node node)
/*     */     throws JAXBException
/*     */   {
/* 129 */     checkNotNull(obj, "obj", node, "node");
/* 130 */     marshal(obj, new DOMResult(node));
/*     */   }
/*     */ 
/*     */   public Node getNode(Object obj)
/*     */     throws JAXBException
/*     */   {
/* 142 */     checkNotNull(obj, "obj", Boolean.TRUE, "foo");
/*     */ 
/* 144 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   protected String getEncoding()
/*     */   {
/* 153 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   protected void setEncoding(String encoding)
/*     */   {
/* 163 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */   protected String getSchemaLocation()
/*     */   {
/* 172 */     return this.schemaLocation;
/*     */   }
/*     */ 
/*     */   protected void setSchemaLocation(String location)
/*     */   {
/* 181 */     this.schemaLocation = location;
/*     */   }
/*     */ 
/*     */   protected String getNoNSSchemaLocation()
/*     */   {
/* 191 */     return this.noNSSchemaLocation;
/*     */   }
/*     */ 
/*     */   protected void setNoNSSchemaLocation(String location)
/*     */   {
/* 200 */     this.noNSSchemaLocation = location;
/*     */   }
/*     */ 
/*     */   protected boolean isFormattedOutput()
/*     */   {
/* 210 */     return this.formattedOutput;
/*     */   }
/*     */ 
/*     */   protected void setFormattedOutput(boolean v)
/*     */   {
/* 219 */     this.formattedOutput = v;
/*     */   }
/*     */ 
/*     */   protected boolean isFragment()
/*     */   {
/* 230 */     return this.fragment;
/*     */   }
/*     */ 
/*     */   protected void setFragment(boolean v)
/*     */   {
/* 239 */     this.fragment = v;
/*     */   }
/*     */ 
/*     */   protected String getJavaEncoding(String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 295 */       "1".getBytes(encoding);
/* 296 */       return encoding;
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 299 */       for (int i = 0; i < aliases.length; i += 2) {
/* 300 */         if (encoding.equals(aliases[i])) {
/* 301 */           "1".getBytes(aliases[(i + 1)]);
/* 302 */           return aliases[(i + 1)];
/*     */         }
/*     */       }
/*     */     }
/* 306 */     throw new UnsupportedEncodingException(encoding);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws PropertyException
/*     */   {
/* 326 */     if (name == null) {
/* 327 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/* 332 */     if ("jaxb.encoding".equals(name)) {
/* 333 */       checkString(name, value);
/* 334 */       setEncoding((String)value);
/* 335 */       return;
/*     */     }
/* 337 */     if ("jaxb.formatted.output".equals(name)) {
/* 338 */       checkBoolean(name, value);
/* 339 */       setFormattedOutput(((Boolean)value).booleanValue());
/* 340 */       return;
/*     */     }
/* 342 */     if ("jaxb.noNamespaceSchemaLocation".equals(name)) {
/* 343 */       checkString(name, value);
/* 344 */       setNoNSSchemaLocation((String)value);
/* 345 */       return;
/*     */     }
/* 347 */     if ("jaxb.schemaLocation".equals(name)) {
/* 348 */       checkString(name, value);
/* 349 */       setSchemaLocation((String)value);
/* 350 */       return;
/*     */     }
/* 352 */     if ("jaxb.fragment".equals(name)) {
/* 353 */       checkBoolean(name, value);
/* 354 */       setFragment(((Boolean)value).booleanValue());
/* 355 */       return;
/*     */     }
/*     */ 
/* 358 */     throw new PropertyException(name, value);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws PropertyException
/*     */   {
/* 370 */     if (name == null) {
/* 371 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/* 376 */     if ("jaxb.encoding".equals(name))
/* 377 */       return getEncoding();
/* 378 */     if ("jaxb.formatted.output".equals(name))
/* 379 */       return isFormattedOutput() ? Boolean.TRUE : Boolean.FALSE;
/* 380 */     if ("jaxb.noNamespaceSchemaLocation".equals(name))
/* 381 */       return getNoNSSchemaLocation();
/* 382 */     if ("jaxb.schemaLocation".equals(name))
/* 383 */       return getSchemaLocation();
/* 384 */     if ("jaxb.fragment".equals(name)) {
/* 385 */       return isFragment() ? Boolean.TRUE : Boolean.FALSE;
/*     */     }
/* 387 */     throw new PropertyException(name);
/*     */   }
/*     */ 
/*     */   public ValidationEventHandler getEventHandler()
/*     */     throws JAXBException
/*     */   {
/* 393 */     return this.eventHandler;
/*     */   }
/*     */ 
/*     */   public void setEventHandler(ValidationEventHandler handler)
/*     */     throws JAXBException
/*     */   {
/* 402 */     if (handler == null)
/* 403 */       this.eventHandler = new DefaultValidationEventHandler();
/*     */     else
/* 405 */       this.eventHandler = handler;
/*     */   }
/*     */ 
/*     */   private void checkBoolean(String name, Object value)
/*     */     throws PropertyException
/*     */   {
/* 416 */     if (!(value instanceof Boolean))
/* 417 */       throw new PropertyException(Messages.format("AbstractMarshallerImpl.MustBeBoolean", name));
/*     */   }
/*     */ 
/*     */   private void checkString(String name, Object value)
/*     */     throws PropertyException
/*     */   {
/* 425 */     if (!(value instanceof String))
/* 426 */       throw new PropertyException(Messages.format("AbstractMarshallerImpl.MustBeString", name));
/*     */   }
/*     */ 
/*     */   private void checkNotNull(Object o1, String o1Name, Object o2, String o2Name)
/*     */   {
/* 436 */     if (o1 == null) {
/* 437 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", o1Name));
/*     */     }
/*     */ 
/* 440 */     if (o2 == null)
/* 441 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", o2Name));
/*     */   }
/*     */ 
/*     */   public void marshal(Object obj, XMLEventWriter writer)
/*     */     throws JAXBException
/*     */   {
/* 449 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void marshal(Object obj, XMLStreamWriter writer)
/*     */     throws JAXBException
/*     */   {
/* 455 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema schema) {
/* 459 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Schema getSchema() {
/* 463 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setAdapter(XmlAdapter adapter) {
/* 467 */     if (adapter == null)
/* 468 */       throw new IllegalArgumentException();
/* 469 */     setAdapter(adapter.getClass(), adapter);
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
/* 473 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type) {
/* 477 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setAttachmentMarshaller(AttachmentMarshaller am) {
/* 481 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public AttachmentMarshaller getAttachmentMarshaller() {
/* 485 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setListener(Marshaller.Listener listener) {
/* 489 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Marshaller.Listener getListener() {
/* 493 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.helpers.AbstractMarshallerImpl
 * JD-Core Version:    0.6.2
 */