/*     */ package com.sun.xml.internal.ws.util.xml;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.istack.internal.XMLStreamReaderToContentHandler;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
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
/*     */ public class StAXSource extends SAXSource
/*     */ {
/*     */   private final XMLStreamReaderToContentHandler reader;
/*     */   private final XMLStreamReader staxReader;
/*  90 */   private XMLFilterImpl repeater = new XMLFilterImpl();
/*     */ 
/*  95 */   private final XMLReader pseudoParser = new XMLReader() { private LexicalHandler lexicalHandler;
/*     */     private EntityResolver entityResolver;
/*     */     private DTDHandler dtdHandler;
/*     */     private ErrorHandler errorHandler;
/*     */ 
/*  97 */     public boolean getFeature(String name) throws SAXNotRecognizedException { throw new SAXNotRecognizedException(name); }
/*     */ 
/*     */     public void setFeature(String name, boolean value)
/*     */       throws SAXNotRecognizedException
/*     */     {
/* 102 */       if ((!name.equals("http://xml.org/sax/features/namespaces")) || (!value))
/*     */       {
/* 104 */         if ((!name.equals("http://xml.org/sax/features/namespace-prefixes")) || (value))
/*     */         {
/* 107 */           throw new SAXNotRecognizedException(name);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 112 */     public Object getProperty(String name) throws SAXNotRecognizedException { if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 113 */         return this.lexicalHandler;
/*     */       }
/* 115 */       throw new SAXNotRecognizedException(name); }
/*     */ 
/*     */     public void setProperty(String name, Object value) throws SAXNotRecognizedException
/*     */     {
/* 119 */       if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 120 */         this.lexicalHandler = ((LexicalHandler)value);
/* 121 */         return;
/*     */       }
/* 123 */       throw new SAXNotRecognizedException(name);
/*     */     }
/*     */ 
/*     */     public void setEntityResolver(EntityResolver resolver)
/*     */     {
/* 131 */       this.entityResolver = resolver;
/*     */     }
/*     */     public EntityResolver getEntityResolver() {
/* 134 */       return this.entityResolver;
/*     */     }
/*     */ 
/*     */     public void setDTDHandler(DTDHandler handler)
/*     */     {
/* 139 */       this.dtdHandler = handler;
/*     */     }
/*     */     public DTDHandler getDTDHandler() {
/* 142 */       return this.dtdHandler;
/*     */     }
/*     */ 
/*     */     public void setContentHandler(ContentHandler handler) {
/* 146 */       StAXSource.this.repeater.setContentHandler(handler);
/*     */     }
/*     */     public ContentHandler getContentHandler() {
/* 149 */       return StAXSource.this.repeater.getContentHandler();
/*     */     }
/*     */ 
/*     */     public void setErrorHandler(ErrorHandler handler)
/*     */     {
/* 154 */       this.errorHandler = handler;
/*     */     }
/*     */     public ErrorHandler getErrorHandler() {
/* 157 */       return this.errorHandler;
/*     */     }
/*     */ 
/*     */     public void parse(InputSource input) throws SAXException {
/* 161 */       parse();
/*     */     }
/*     */ 
/*     */     public void parse(String systemId) throws SAXException {
/* 165 */       parse();
/*     */     }
/*     */ 
/*     */     public void parse()
/*     */       throws SAXException
/*     */     {
/*     */       try
/*     */       {
/* 173 */         StAXSource.this.reader.bridge();
/*     */       }
/*     */       catch (XMLStreamException e) {
/* 176 */         SAXParseException se = new SAXParseException2(e.getMessage(), null, null, e.getLocation() == null ? -1 : e.getLocation().getLineNumber(), e.getLocation() == null ? -1 : e.getLocation().getColumnNumber(), e);
/*     */ 
/* 187 */         if (this.errorHandler != null) {
/* 188 */           this.errorHandler.fatalError(se);
/*     */         }
/*     */ 
/* 192 */         throw se;
/*     */       }
/*     */       finally {
/*     */         try {
/* 196 */           StAXSource.this.staxReader.close();
/*     */         }
/*     */         catch (XMLStreamException xe)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*  95 */   };
/*     */ 
/*     */   public StAXSource(XMLStreamReader reader, boolean eagerQuit)
/*     */   {
/* 214 */     this(reader, eagerQuit, new String[0]);
/*     */   }
/*     */ 
/*     */   public StAXSource(XMLStreamReader reader, boolean eagerQuit, @NotNull String[] inscope)
/*     */   {
/* 235 */     if (reader == null)
/* 236 */       throw new IllegalArgumentException();
/* 237 */     this.staxReader = reader;
/*     */ 
/* 239 */     int eventType = reader.getEventType();
/* 240 */     if ((eventType != 7) && (eventType != 1))
/*     */     {
/* 242 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/* 245 */     this.reader = new XMLStreamReaderToContentHandler(reader, this.repeater, eagerQuit, false, inscope);
/*     */ 
/* 247 */     super.setXMLReader(this.pseudoParser);
/*     */ 
/* 249 */     super.setInputSource(new InputSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.StAXSource
 * JD-Core Version:    0.6.2
 */