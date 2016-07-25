/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.XMLReaderManager;
/*     */ import java.io.IOException;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.Templates;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ public class TrAXFilter extends XMLFilterImpl
/*     */ {
/*     */   private Templates _templates;
/*     */   private TransformerImpl _transformer;
/*     */   private TransformerHandlerImpl _transformerHandler;
/*  58 */   private boolean _useServicesMechanism = true;
/*     */ 
/*     */   public TrAXFilter(Templates templates)
/*     */     throws TransformerConfigurationException
/*     */   {
/*  63 */     this._templates = templates;
/*  64 */     this._transformer = ((TransformerImpl)templates.newTransformer());
/*  65 */     this._transformerHandler = new TransformerHandlerImpl(this._transformer);
/*  66 */     this._useServicesMechanism = this._transformer.useServicesMechnism();
/*     */   }
/*     */ 
/*     */   public Transformer getTransformer() {
/*  70 */     return this._transformer;
/*     */   }
/*     */ 
/*     */   private void createParent() throws SAXException {
/*  74 */     XMLReader parent = null;
/*     */     try {
/*  76 */       SAXParserFactory pfactory = SAXParserFactory.newInstance();
/*  77 */       pfactory.setNamespaceAware(true);
/*     */ 
/*  79 */       if (this._transformer.isSecureProcessing())
/*     */         try {
/*  81 */           pfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*     */         }
/*     */         catch (SAXException e)
/*     */         {
/*     */         }
/*  86 */       SAXParser saxparser = pfactory.newSAXParser();
/*  87 */       parent = saxparser.getXMLReader();
/*     */     }
/*     */     catch (ParserConfigurationException e) {
/*  90 */       throw new SAXException(e);
/*     */     }
/*     */     catch (FactoryConfigurationError e) {
/*  93 */       throw new SAXException(e.toString());
/*     */     }
/*     */ 
/*  96 */     if (parent == null) {
/*  97 */       parent = XMLReaderFactory.createXMLReader();
/*     */     }
/*     */ 
/* 101 */     setParent(parent);
/*     */   }
/*     */ 
/*     */   public void parse(InputSource input) throws SAXException, IOException
/*     */   {
/* 106 */     XMLReader managedReader = null;
/*     */     try
/*     */     {
/* 109 */       if (getParent() == null) {
/*     */         try {
/* 111 */           managedReader = XMLReaderManager.getInstance(this._useServicesMechanism).getXMLReader();
/*     */ 
/* 113 */           setParent(managedReader);
/*     */         } catch (SAXException e) {
/* 115 */           throw new SAXException(e.toString());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 120 */       getParent().parse(input);
/*     */     } finally {
/* 122 */       if (managedReader != null)
/* 123 */         XMLReaderManager.getInstance(this._useServicesMechanism).releaseXMLReader(managedReader);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parse(String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 130 */     parse(new InputSource(systemId));
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */   {
/* 135 */     this._transformerHandler.setResult(new SAXResult(handler));
/* 136 */     if (getParent() == null) {
/*     */       try {
/* 138 */         createParent();
/*     */       }
/*     */       catch (SAXException e) {
/* 141 */         return;
/*     */       }
/*     */     }
/* 144 */     getParent().setContentHandler(this._transformerHandler);
/*     */   }
/*     */ 
/*     */   public void setErrorListener(ErrorListener handler)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter
 * JD-Core Version:    0.6.2
 */