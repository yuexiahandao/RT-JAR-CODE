/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.Unmarshaller.Listener;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public abstract class AbstractUnmarshallerImpl
/*     */   implements Unmarshaller
/*     */ {
/*  76 */   private ValidationEventHandler eventHandler = new DefaultValidationEventHandler();
/*     */ 
/*  80 */   protected boolean validating = false;
/*     */ 
/*  85 */   private XMLReader reader = null;
/*     */ 
/*     */   protected XMLReader getXMLReader()
/*     */     throws JAXBException
/*     */   {
/*  97 */     if (this.reader == null) {
/*     */       try
/*     */       {
/* 100 */         SAXParserFactory parserFactory = SAXParserFactory.newInstance();
/* 101 */         parserFactory.setNamespaceAware(true);
/*     */ 
/* 105 */         parserFactory.setValidating(false);
/* 106 */         this.reader = parserFactory.newSAXParser().getXMLReader();
/*     */       } catch (ParserConfigurationException e) {
/* 108 */         throw new JAXBException(e);
/*     */       } catch (SAXException e) {
/* 110 */         throw new JAXBException(e);
/*     */       }
/*     */     }
/* 113 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public Object unmarshal(Source source) throws JAXBException {
/* 117 */     if (source == null) {
/* 118 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "source"));
/*     */     }
/*     */ 
/* 122 */     if ((source instanceof SAXSource))
/* 123 */       return unmarshal((SAXSource)source);
/* 124 */     if ((source instanceof StreamSource))
/* 125 */       return unmarshal(streamSourceToInputSource((StreamSource)source));
/* 126 */     if ((source instanceof DOMSource)) {
/* 127 */       return unmarshal(((DOMSource)source).getNode());
/*     */     }
/*     */ 
/* 130 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   private Object unmarshal(SAXSource source)
/*     */     throws JAXBException
/*     */   {
/* 136 */     XMLReader r = source.getXMLReader();
/* 137 */     if (r == null) {
/* 138 */       r = getXMLReader();
/*     */     }
/* 140 */     return unmarshal(r, source.getInputSource());
/*     */   }
/*     */ 
/*     */   protected abstract Object unmarshal(XMLReader paramXMLReader, InputSource paramInputSource)
/*     */     throws JAXBException;
/*     */ 
/*     */   public final Object unmarshal(InputSource source)
/*     */     throws JAXBException
/*     */   {
/* 152 */     if (source == null) {
/* 153 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "source"));
/*     */     }
/*     */ 
/* 157 */     return unmarshal(getXMLReader(), source);
/*     */   }
/*     */ 
/*     */   private Object unmarshal(String url) throws JAXBException
/*     */   {
/* 162 */     return unmarshal(new InputSource(url));
/*     */   }
/*     */ 
/*     */   public final Object unmarshal(URL url) throws JAXBException {
/* 166 */     if (url == null) {
/* 167 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "url"));
/*     */     }
/*     */ 
/* 171 */     return unmarshal(url.toExternalForm());
/*     */   }
/*     */ 
/*     */   public final Object unmarshal(File f) throws JAXBException {
/* 175 */     if (f == null) {
/* 176 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "file"));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 182 */       String path = f.getAbsolutePath();
/* 183 */       if (File.separatorChar != '/')
/* 184 */         path = path.replace(File.separatorChar, '/');
/* 185 */       if (!path.startsWith("/"))
/* 186 */         path = "/" + path;
/* 187 */       if ((!path.endsWith("/")) && (f.isDirectory()))
/* 188 */         path = path + "/";
/* 189 */       return unmarshal(new URL("file", "", path));
/*     */     } catch (MalformedURLException e) {
/* 191 */       throw new IllegalArgumentException(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Object unmarshal(InputStream is)
/*     */     throws JAXBException
/*     */   {
/* 198 */     if (is == null) {
/* 199 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "is"));
/*     */     }
/*     */ 
/* 203 */     InputSource isrc = new InputSource(is);
/* 204 */     return unmarshal(isrc);
/*     */   }
/*     */ 
/*     */   public final Object unmarshal(Reader reader) throws JAXBException {
/* 208 */     if (reader == null) {
/* 209 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "reader"));
/*     */     }
/*     */ 
/* 213 */     InputSource isrc = new InputSource(reader);
/* 214 */     return unmarshal(isrc);
/*     */   }
/*     */ 
/*     */   private static InputSource streamSourceToInputSource(StreamSource ss)
/*     */   {
/* 219 */     InputSource is = new InputSource();
/* 220 */     is.setSystemId(ss.getSystemId());
/* 221 */     is.setByteStream(ss.getInputStream());
/* 222 */     is.setCharacterStream(ss.getReader());
/*     */ 
/* 224 */     return is;
/*     */   }
/*     */ 
/*     */   public boolean isValidating()
/*     */     throws JAXBException
/*     */   {
/* 241 */     return this.validating;
/*     */   }
/*     */ 
/*     */   public void setEventHandler(ValidationEventHandler handler)
/*     */     throws JAXBException
/*     */   {
/* 261 */     if (handler == null)
/* 262 */       this.eventHandler = new DefaultValidationEventHandler();
/*     */     else
/* 264 */       this.eventHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */     throws JAXBException
/*     */   {
/* 282 */     this.validating = validating;
/*     */   }
/*     */ 
/*     */   public ValidationEventHandler getEventHandler()
/*     */     throws JAXBException
/*     */   {
/* 295 */     return this.eventHandler;
/*     */   }
/*     */ 
/*     */   protected UnmarshalException createUnmarshalException(SAXException e)
/*     */   {
/* 320 */     Exception nested = e.getException();
/* 321 */     if ((nested instanceof UnmarshalException)) {
/* 322 */       return (UnmarshalException)nested;
/*     */     }
/* 324 */     if ((nested instanceof RuntimeException))
/*     */     {
/* 328 */       throw ((RuntimeException)nested);
/*     */     }
/*     */ 
/* 332 */     if (nested != null) {
/* 333 */       return new UnmarshalException(nested);
/*     */     }
/* 335 */     return new UnmarshalException(e);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws PropertyException
/*     */   {
/* 347 */     if (name == null) {
/* 348 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/* 352 */     throw new PropertyException(name, value);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws PropertyException
/*     */   {
/* 364 */     if (name == null) {
/* 365 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/* 369 */     throw new PropertyException(name);
/*     */   }
/*     */ 
/*     */   public Object unmarshal(XMLEventReader reader) throws JAXBException
/*     */   {
/* 374 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Object unmarshal(XMLStreamReader reader) throws JAXBException
/*     */   {
/* 379 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(Node node, Class<T> expectedType) throws JAXBException {
/* 383 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(Source source, Class<T> expectedType) throws JAXBException {
/* 387 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(XMLStreamReader reader, Class<T> expectedType) throws JAXBException {
/* 391 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(XMLEventReader reader, Class<T> expectedType) throws JAXBException {
/* 395 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema schema) {
/* 399 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Schema getSchema() {
/* 403 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setAdapter(XmlAdapter adapter) {
/* 407 */     if (adapter == null)
/* 408 */       throw new IllegalArgumentException();
/* 409 */     setAdapter(adapter.getClass(), adapter);
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
/* 413 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type) {
/* 417 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setAttachmentUnmarshaller(AttachmentUnmarshaller au) {
/* 421 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public AttachmentUnmarshaller getAttachmentUnmarshaller() {
/* 425 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setListener(Unmarshaller.Listener listener) {
/* 429 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Unmarshaller.Listener getListener() {
/* 433 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.helpers.AbstractUnmarshallerImpl
 * JD-Core Version:    0.6.2
 */