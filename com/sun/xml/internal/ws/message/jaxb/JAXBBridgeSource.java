/*     */ package com.sun.xml.internal.ws.message.jaxb;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ final class JAXBBridgeSource extends SAXSource
/*     */ {
/*     */   private final Bridge bridge;
/*     */   private final Object contentObject;
/*  58 */   private final XMLReader pseudoParser = new XMLFilterImpl() { private LexicalHandler lexicalHandler;
/*     */ 
/*  60 */     public boolean getFeature(String name) throws SAXNotRecognizedException { if (name.equals("http://xml.org/sax/features/namespaces"))
/*  61 */         return true;
/*  62 */       if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/*  63 */         return false;
/*  64 */       throw new SAXNotRecognizedException(name); }
/*     */ 
/*     */     public void setFeature(String name, boolean value) throws SAXNotRecognizedException
/*     */     {
/*  68 */       if ((name.equals("http://xml.org/sax/features/namespaces")) && (value))
/*  69 */         return;
/*  70 */       if ((name.equals("http://xml.org/sax/features/namespace-prefixes")) && (!value))
/*  71 */         return;
/*  72 */       throw new SAXNotRecognizedException(name);
/*     */     }
/*     */ 
/*     */     public Object getProperty(String name) throws SAXNotRecognizedException {
/*  76 */       if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/*  77 */         return this.lexicalHandler;
/*     */       }
/*  79 */       throw new SAXNotRecognizedException(name);
/*     */     }
/*     */ 
/*     */     public void setProperty(String name, Object value) throws SAXNotRecognizedException {
/*  83 */       if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/*  84 */         this.lexicalHandler = ((LexicalHandler)value);
/*  85 */         return;
/*     */       }
/*  87 */       throw new SAXNotRecognizedException(name);
/*     */     }
/*     */ 
/*     */     public void parse(InputSource input)
/*     */       throws SAXException
/*     */     {
/*  93 */       parse();
/*     */     }
/*     */ 
/*     */     public void parse(String systemId) throws SAXException {
/*  97 */       parse();
/*     */     }
/*     */ 
/*     */     public void parse()
/*     */       throws SAXException
/*     */     {
/*     */       try
/*     */       {
/* 105 */         startDocument();
/*     */ 
/* 107 */         JAXBBridgeSource.this.bridge.marshal(JAXBBridgeSource.this.contentObject, this);
/* 108 */         endDocument();
/*     */       }
/*     */       catch (JAXBException e) {
/* 111 */         SAXParseException se = new SAXParseException(e.getMessage(), null, null, -1, -1, e);
/*     */ 
/* 117 */         fatalError(se);
/*     */ 
/* 121 */         throw se;
/*     */       }
/*     */     }
/*  58 */   };
/*     */ 
/*     */   public JAXBBridgeSource(Bridge bridge, Object contentObject)
/*     */   {
/*  44 */     this.bridge = bridge;
/*  45 */     this.contentObject = contentObject;
/*     */ 
/*  47 */     super.setXMLReader(this.pseudoParser);
/*     */ 
/*  49 */     super.setInputSource(new InputSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.jaxb.JAXBBridgeSource
 * JD-Core Version:    0.6.2
 */