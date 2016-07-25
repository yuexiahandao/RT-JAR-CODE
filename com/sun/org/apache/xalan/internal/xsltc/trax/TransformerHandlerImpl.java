/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*     */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.DeclHandler;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class TransformerHandlerImpl
/*     */   implements TransformerHandler, DeclHandler
/*     */ {
/*     */   private TransformerImpl _transformer;
/*  57 */   private AbstractTranslet _translet = null;
/*     */   private String _systemId;
/*  59 */   private SAXImpl _dom = null;
/*  60 */   private ContentHandler _handler = null;
/*  61 */   private LexicalHandler _lexHandler = null;
/*  62 */   private DTDHandler _dtdHandler = null;
/*  63 */   private DeclHandler _declHandler = null;
/*  64 */   private Result _result = null;
/*  65 */   private Locator _locator = null;
/*     */ 
/*  67 */   private boolean _done = false;
/*     */ 
/*  73 */   private boolean _isIdentity = false;
/*     */ 
/*     */   public TransformerHandlerImpl(TransformerImpl transformer)
/*     */   {
/*  80 */     this._transformer = transformer;
/*     */ 
/*  82 */     if (transformer.isIdentity())
/*     */     {
/*  84 */       this._handler = new DefaultHandler();
/*  85 */       this._isIdentity = true;
/*     */     }
/*     */     else
/*     */     {
/*  89 */       this._translet = this._transformer.getTranslet();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 100 */     return this._systemId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String id)
/*     */   {
/* 110 */     this._systemId = id;
/*     */   }
/*     */ 
/*     */   public Transformer getTransformer()
/*     */   {
/* 120 */     return this._transformer;
/*     */   }
/*     */ 
/*     */   public void setResult(Result result)
/*     */     throws IllegalArgumentException
/*     */   {
/* 131 */     this._result = result;
/*     */ 
/* 133 */     if (null == result) {
/* 134 */       ErrorMsg err = new ErrorMsg("ER_RESULT_NULL");
/* 135 */       throw new IllegalArgumentException(err.toString());
/*     */     }
/*     */ 
/* 138 */     if (this._isIdentity) {
/*     */       try
/*     */       {
/* 141 */         SerializationHandler outputHandler = this._transformer.getOutputHandler(result);
/*     */ 
/* 143 */         this._transformer.transferOutputProperties(outputHandler);
/*     */ 
/* 145 */         this._handler = outputHandler;
/* 146 */         this._lexHandler = outputHandler;
/*     */       }
/*     */       catch (TransformerException e) {
/* 149 */         this._result = null;
/*     */       }
/*     */     }
/* 152 */     else if (this._done)
/*     */       try
/*     */       {
/* 155 */         this._transformer.setDOM(this._dom);
/* 156 */         this._transformer.transform(null, this._result);
/*     */       }
/*     */       catch (TransformerException e)
/*     */       {
/* 160 */         throw new IllegalArgumentException(e.getMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 172 */     this._handler.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 181 */     if (this._result == null) {
/* 182 */       ErrorMsg err = new ErrorMsg("JAXP_SET_RESULT_ERR");
/* 183 */       throw new SAXException(err.toString());
/*     */     }
/*     */ 
/* 186 */     if (!this._isIdentity) {
/* 187 */       boolean hasIdCall = this._translet != null ? this._translet.hasIdCall() : false;
/* 188 */       XSLTCDTMManager dtmManager = null;
/*     */       try
/*     */       {
/* 192 */         dtmManager = (XSLTCDTMManager)this._transformer.getTransformerFactory().getDTMManagerClass().newInstance();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 197 */         throw new SAXException(e);
/*     */       }
/*     */       DTMWSFilter wsFilter;
/*     */       DTMWSFilter wsFilter;
/* 201 */       if ((this._translet != null) && ((this._translet instanceof StripFilter)))
/* 202 */         wsFilter = new DOMWSFilter(this._translet);
/*     */       else {
/* 204 */         wsFilter = null;
/*     */       }
/*     */ 
/* 208 */       this._dom = ((SAXImpl)dtmManager.getDTM(null, false, wsFilter, true, false, hasIdCall));
/*     */ 
/* 211 */       this._handler = this._dom.getBuilder();
/* 212 */       this._lexHandler = ((LexicalHandler)this._handler);
/* 213 */       this._dtdHandler = ((DTDHandler)this._handler);
/* 214 */       this._declHandler = ((DeclHandler)this._handler);
/*     */ 
/* 218 */       this._dom.setDocumentURI(this._systemId);
/*     */ 
/* 220 */       if (this._locator != null) {
/* 221 */         this._handler.setDocumentLocator(this._locator);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 226 */     this._handler.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 235 */     this._handler.endDocument();
/*     */ 
/* 237 */     if (!this._isIdentity)
/*     */     {
/* 239 */       if (this._result != null) {
/*     */         try {
/* 241 */           this._transformer.setDOM(this._dom);
/* 242 */           this._transformer.transform(null, this._result);
/*     */         }
/*     */         catch (TransformerException e) {
/* 245 */           throw new SAXException(e);
/*     */         }
/*     */       }
/*     */ 
/* 249 */       this._done = true;
/*     */ 
/* 252 */       this._transformer.setDOM(this._dom);
/*     */     }
/* 254 */     if ((this._isIdentity) && ((this._result instanceof DOMResult)))
/* 255 */       ((DOMResult)this._result).setNode(this._transformer.getTransletOutputHandlerFactory().getNode());
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qname, Attributes attributes)
/*     */     throws SAXException
/*     */   {
/* 267 */     this._handler.startElement(uri, localName, qname, attributes);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qname)
/*     */     throws SAXException
/*     */   {
/* 277 */     this._handler.endElement(namespaceURI, localName, qname);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 287 */     this._handler.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void startCDATA()
/*     */     throws SAXException
/*     */   {
/* 294 */     if (this._lexHandler != null)
/* 295 */       this._lexHandler.startCDATA();
/*     */   }
/*     */ 
/*     */   public void endCDATA()
/*     */     throws SAXException
/*     */   {
/* 303 */     if (this._lexHandler != null)
/* 304 */       this._lexHandler.endCDATA();
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 315 */     if (this._lexHandler != null)
/* 316 */       this._lexHandler.comment(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 328 */     this._handler.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 336 */     this._locator = locator;
/*     */ 
/* 338 */     if (this._handler != null)
/* 339 */       this._handler.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 348 */     this._handler.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 357 */     this._handler.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/* 365 */     this._handler.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 374 */     if (this._lexHandler != null)
/* 375 */       this._lexHandler.startDTD(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void endDTD()
/*     */     throws SAXException
/*     */   {
/* 383 */     if (this._lexHandler != null)
/* 384 */       this._lexHandler.endDTD();
/*     */   }
/*     */ 
/*     */   public void startEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 392 */     if (this._lexHandler != null)
/* 393 */       this._lexHandler.startEntity(name);
/*     */   }
/*     */ 
/*     */   public void endEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 401 */     if (this._lexHandler != null)
/* 402 */       this._lexHandler.endEntity(name);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*     */     throws SAXException
/*     */   {
/* 412 */     if (this._dtdHandler != null)
/* 413 */       this._dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 424 */     if (this._dtdHandler != null)
/* 425 */       this._dtdHandler.notationDecl(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
/*     */     throws SAXException
/*     */   {
/* 435 */     if (this._declHandler != null)
/* 436 */       this._declHandler.attributeDecl(eName, aName, type, valueDefault, value);
/*     */   }
/*     */ 
/*     */   public void elementDecl(String name, String model)
/*     */     throws SAXException
/*     */   {
/* 446 */     if (this._declHandler != null)
/* 447 */       this._declHandler.elementDecl(name, model);
/*     */   }
/*     */ 
/*     */   public void externalEntityDecl(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 457 */     if (this._declHandler != null)
/* 458 */       this._declHandler.externalEntityDecl(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void internalEntityDecl(String name, String value)
/*     */     throws SAXException
/*     */   {
/* 468 */     if (this._declHandler != null)
/* 469 */       this._declHandler.internalEntityDecl(name, value);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 478 */     this._systemId = null;
/* 479 */     this._dom = null;
/* 480 */     this._handler = null;
/* 481 */     this._lexHandler = null;
/* 482 */     this._dtdHandler = null;
/* 483 */     this._declHandler = null;
/* 484 */     this._result = null;
/* 485 */     this._locator = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.TransformerHandlerImpl
 * JD-Core Version:    0.6.2
 */