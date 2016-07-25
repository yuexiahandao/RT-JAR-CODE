/*     */ package javax.xml.bind.util;
/*     */ 
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class JAXBSource extends SAXSource
/*     */ {
/*     */   private final Marshaller marshaller;
/*     */   private final Object contentObject;
/* 159 */   private final XMLReader pseudoParser = new XMLReader() { private LexicalHandler lexicalHandler;
/*     */     private EntityResolver entityResolver;
/*     */     private DTDHandler dtdHandler;
/* 213 */     private XMLFilterImpl repeater = new XMLFilterImpl();
/*     */     private ErrorHandler errorHandler;
/*     */ 
/* 161 */     public boolean getFeature(String name) throws SAXNotRecognizedException { if (name.equals("http://xml.org/sax/features/namespaces"))
/* 162 */         return true;
/* 163 */       if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/* 164 */         return false;
/* 165 */       throw new SAXNotRecognizedException(name); }
/*     */ 
/*     */     public void setFeature(String name, boolean value) throws SAXNotRecognizedException
/*     */     {
/* 169 */       if ((name.equals("http://xml.org/sax/features/namespaces")) && (value))
/* 170 */         return;
/* 171 */       if ((name.equals("http://xml.org/sax/features/namespace-prefixes")) && (!value))
/* 172 */         return;
/* 173 */       throw new SAXNotRecognizedException(name);
/*     */     }
/*     */ 
/*     */     public Object getProperty(String name) throws SAXNotRecognizedException {
/* 177 */       if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 178 */         return this.lexicalHandler;
/*     */       }
/* 180 */       throw new SAXNotRecognizedException(name);
/*     */     }
/*     */ 
/*     */     public void setProperty(String name, Object value) throws SAXNotRecognizedException {
/* 184 */       if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 185 */         this.lexicalHandler = ((LexicalHandler)value);
/* 186 */         return;
/*     */       }
/* 188 */       throw new SAXNotRecognizedException(name);
/*     */     }
/*     */ 
/*     */     public void setEntityResolver(EntityResolver resolver)
/*     */     {
/* 196 */       this.entityResolver = resolver;
/*     */     }
/*     */     public EntityResolver getEntityResolver() {
/* 199 */       return this.entityResolver;
/*     */     }
/*     */ 
/*     */     public void setDTDHandler(DTDHandler handler)
/*     */     {
/* 204 */       this.dtdHandler = handler;
/*     */     }
/*     */     public DTDHandler getDTDHandler() {
/* 207 */       return this.dtdHandler;
/*     */     }
/*     */ 
/*     */     public void setContentHandler(ContentHandler handler)
/*     */     {
/* 216 */       this.repeater.setContentHandler(handler);
/*     */     }
/*     */     public ContentHandler getContentHandler() {
/* 219 */       return this.repeater.getContentHandler();
/*     */     }
/*     */ 
/*     */     public void setErrorHandler(ErrorHandler handler)
/*     */     {
/* 224 */       this.errorHandler = handler;
/*     */     }
/*     */     public ErrorHandler getErrorHandler() {
/* 227 */       return this.errorHandler;
/*     */     }
/*     */ 
/*     */     public void parse(InputSource input) throws SAXException {
/* 231 */       parse();
/*     */     }
/*     */ 
/*     */     public void parse(String systemId) throws SAXException {
/* 235 */       parse();
/*     */     }
/*     */ 
/*     */     public void parse()
/*     */       throws SAXException
/*     */     {
/*     */       try
/*     */       {
/* 243 */         JAXBSource.this.marshaller.marshal(JAXBSource.this.contentObject, this.repeater);
/*     */       }
/*     */       catch (JAXBException e) {
/* 246 */         SAXParseException se = new SAXParseException(e.getMessage(), null, null, -1, -1, e);
/*     */ 
/* 252 */         if (this.errorHandler != null) {
/* 253 */           this.errorHandler.fatalError(se);
/*     */         }
/*     */ 
/* 257 */         throw se;
/*     */       }
/*     */     }
/* 159 */   };
/*     */ 
/*     */   public JAXBSource(JAXBContext context, Object contentObject)
/*     */     throws JAXBException
/*     */   {
/* 109 */     this(context == null ? assertionFailed(Messages.format("JAXBSource.NullContext")) : context.createMarshaller(), contentObject == null ? assertionFailed(Messages.format("JAXBSource.NullContent")) : contentObject);
/*     */   }
/*     */ 
/*     */   public JAXBSource(Marshaller marshaller, Object contentObject)
/*     */     throws JAXBException
/*     */   {
/* 137 */     if (marshaller == null) {
/* 138 */       throw new JAXBException(Messages.format("JAXBSource.NullMarshaller"));
/*     */     }
/*     */ 
/* 141 */     if (contentObject == null) {
/* 142 */       throw new JAXBException(Messages.format("JAXBSource.NullContent"));
/*     */     }
/*     */ 
/* 145 */     this.marshaller = marshaller;
/* 146 */     this.contentObject = contentObject;
/*     */ 
/* 148 */     super.setXMLReader(this.pseudoParser);
/*     */ 
/* 150 */     super.setInputSource(new InputSource());
/*     */   }
/*     */ 
/*     */   private static Marshaller assertionFailed(String message)
/*     */     throws JAXBException
/*     */   {
/* 269 */     throw new JAXBException(message);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.util.JAXBSource
 * JD-Core Version:    0.6.2
 */