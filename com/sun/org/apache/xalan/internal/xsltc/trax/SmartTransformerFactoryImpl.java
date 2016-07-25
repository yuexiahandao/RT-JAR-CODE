/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Templates;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.SAXTransformerFactory;
/*     */ import javax.xml.transform.sax.TemplatesHandler;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import org.xml.sax.XMLFilter;
/*     */ 
/*     */ public class SmartTransformerFactoryImpl extends SAXTransformerFactory
/*     */ {
/*     */   private static final String CLASS_NAME = "SmartTransformerFactoryImpl";
/*  63 */   private SAXTransformerFactory _xsltcFactory = null;
/*  64 */   private SAXTransformerFactory _xalanFactory = null;
/*  65 */   private SAXTransformerFactory _currFactory = null;
/*  66 */   private ErrorListener _errorlistener = null;
/*  67 */   private URIResolver _uriresolver = null;
/*     */ 
/*  72 */   private boolean featureSecureProcessing = false;
/*     */ 
/*     */   private void createXSLTCTransformerFactory()
/*     */   {
/*  84 */     this._xsltcFactory = new TransformerFactoryImpl();
/*  85 */     this._currFactory = this._xsltcFactory;
/*     */   }
/*     */ 
/*     */   private void createXalanTransformerFactory() {
/*  89 */     String xalanMessage = "com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.";
/*     */     try
/*     */     {
/*  95 */       Class xalanFactClass = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl", true);
/*     */ 
/*  98 */       this._xalanFactory = ((SAXTransformerFactory)xalanFactClass.newInstance());
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/* 102 */       System.err.println("com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
/*     */     }
/*     */     catch (InstantiationException e) {
/* 105 */       System.err.println("com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 108 */       System.err.println("com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
/*     */     }
/* 110 */     this._currFactory = this._xalanFactory;
/*     */   }
/*     */ 
/*     */   public void setErrorListener(ErrorListener listener)
/*     */     throws IllegalArgumentException
/*     */   {
/* 116 */     this._errorlistener = listener;
/*     */   }
/*     */ 
/*     */   public ErrorListener getErrorListener() {
/* 120 */     return this._errorlistener;
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 127 */     if ((name.equals("translet-name")) || (name.equals("debug"))) {
/* 128 */       if (this._xsltcFactory == null) {
/* 129 */         createXSLTCTransformerFactory();
/*     */       }
/* 131 */       return this._xsltcFactory.getAttribute(name);
/*     */     }
/*     */ 
/* 134 */     if (this._xalanFactory == null) {
/* 135 */       createXalanTransformerFactory();
/*     */     }
/* 137 */     return this._xalanFactory.getAttribute(name);
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, Object value)
/*     */     throws IllegalArgumentException
/*     */   {
/* 144 */     if ((name.equals("translet-name")) || (name.equals("debug"))) {
/* 145 */       if (this._xsltcFactory == null) {
/* 146 */         createXSLTCTransformerFactory();
/*     */       }
/* 148 */       this._xsltcFactory.setAttribute(name, value);
/*     */     }
/*     */     else {
/* 151 */       if (this._xalanFactory == null) {
/* 152 */         createXalanTransformerFactory();
/*     */       }
/* 154 */       this._xalanFactory.setAttribute(name, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 183 */     if (name == null) {
/* 184 */       ErrorMsg err = new ErrorMsg("JAXP_SET_FEATURE_NULL_NAME");
/* 185 */       throw new NullPointerException(err.toString());
/*     */     }
/*     */ 
/* 188 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 189 */       this.featureSecureProcessing = value;
/*     */ 
/* 191 */       return;
/*     */     }
/*     */ 
/* 195 */     ErrorMsg err = new ErrorMsg("JAXP_UNSUPPORTED_FEATURE", name);
/* 196 */     throw new TransformerConfigurationException(err.toString());
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */   {
/* 211 */     String[] features = { "http://javax.xml.transform.dom.DOMSource/feature", "http://javax.xml.transform.dom.DOMResult/feature", "http://javax.xml.transform.sax.SAXSource/feature", "http://javax.xml.transform.sax.SAXResult/feature", "http://javax.xml.transform.stream.StreamSource/feature", "http://javax.xml.transform.stream.StreamResult/feature" };
/*     */ 
/* 221 */     if (name == null) {
/* 222 */       ErrorMsg err = new ErrorMsg("JAXP_GET_FEATURE_NULL_NAME");
/* 223 */       throw new NullPointerException(err.toString());
/*     */     }
/*     */ 
/* 227 */     for (int i = 0; i < features.length; i++) {
/* 228 */       if (name.equals(features[i])) {
/* 229 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 233 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 234 */       return this.featureSecureProcessing;
/*     */     }
/*     */ 
/* 238 */     return false;
/*     */   }
/*     */ 
/*     */   public URIResolver getURIResolver() {
/* 242 */     return this._uriresolver;
/*     */   }
/*     */ 
/*     */   public void setURIResolver(URIResolver resolver) {
/* 246 */     this._uriresolver = resolver;
/*     */   }
/*     */ 
/*     */   public Source getAssociatedStylesheet(Source source, String media, String title, String charset)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 253 */     if (this._currFactory == null) {
/* 254 */       createXSLTCTransformerFactory();
/*     */     }
/* 256 */     return this._currFactory.getAssociatedStylesheet(source, media, title, charset);
/*     */   }
/*     */ 
/*     */   public Transformer newTransformer()
/*     */     throws TransformerConfigurationException
/*     */   {
/* 268 */     if (this._xalanFactory == null) {
/* 269 */       createXalanTransformerFactory();
/*     */     }
/* 271 */     if (this._errorlistener != null) {
/* 272 */       this._xalanFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 274 */     if (this._uriresolver != null) {
/* 275 */       this._xalanFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 277 */     this._currFactory = this._xalanFactory;
/* 278 */     return this._currFactory.newTransformer();
/*     */   }
/*     */ 
/*     */   public Transformer newTransformer(Source source)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 290 */     if (this._xalanFactory == null) {
/* 291 */       createXalanTransformerFactory();
/*     */     }
/* 293 */     if (this._errorlistener != null) {
/* 294 */       this._xalanFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 296 */     if (this._uriresolver != null) {
/* 297 */       this._xalanFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 299 */     this._currFactory = this._xalanFactory;
/* 300 */     return this._currFactory.newTransformer(source);
/*     */   }
/*     */ 
/*     */   public Templates newTemplates(Source source)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 312 */     if (this._xsltcFactory == null) {
/* 313 */       createXSLTCTransformerFactory();
/*     */     }
/* 315 */     if (this._errorlistener != null) {
/* 316 */       this._xsltcFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 318 */     if (this._uriresolver != null) {
/* 319 */       this._xsltcFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 321 */     this._currFactory = this._xsltcFactory;
/* 322 */     return this._currFactory.newTemplates(source);
/*     */   }
/*     */ 
/*     */   public TemplatesHandler newTemplatesHandler()
/*     */     throws TransformerConfigurationException
/*     */   {
/* 333 */     if (this._xsltcFactory == null) {
/* 334 */       createXSLTCTransformerFactory();
/*     */     }
/* 336 */     if (this._errorlistener != null) {
/* 337 */       this._xsltcFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 339 */     if (this._uriresolver != null) {
/* 340 */       this._xsltcFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 342 */     return this._xsltcFactory.newTemplatesHandler();
/*     */   }
/*     */ 
/*     */   public TransformerHandler newTransformerHandler()
/*     */     throws TransformerConfigurationException
/*     */   {
/* 353 */     if (this._xalanFactory == null) {
/* 354 */       createXalanTransformerFactory();
/*     */     }
/* 356 */     if (this._errorlistener != null) {
/* 357 */       this._xalanFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 359 */     if (this._uriresolver != null) {
/* 360 */       this._xalanFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 362 */     return this._xalanFactory.newTransformerHandler();
/*     */   }
/*     */ 
/*     */   public TransformerHandler newTransformerHandler(Source src)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 373 */     if (this._xalanFactory == null) {
/* 374 */       createXalanTransformerFactory();
/*     */     }
/* 376 */     if (this._errorlistener != null) {
/* 377 */       this._xalanFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 379 */     if (this._uriresolver != null) {
/* 380 */       this._xalanFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 382 */     return this._xalanFactory.newTransformerHandler(src);
/*     */   }
/*     */ 
/*     */   public TransformerHandler newTransformerHandler(Templates templates)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 394 */     if (this._xsltcFactory == null) {
/* 395 */       createXSLTCTransformerFactory();
/*     */     }
/* 397 */     if (this._errorlistener != null) {
/* 398 */       this._xsltcFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 400 */     if (this._uriresolver != null) {
/* 401 */       this._xsltcFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 403 */     return this._xsltcFactory.newTransformerHandler(templates);
/*     */   }
/*     */ 
/*     */   public XMLFilter newXMLFilter(Source src)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 414 */     if (this._xsltcFactory == null) {
/* 415 */       createXSLTCTransformerFactory();
/*     */     }
/* 417 */     if (this._errorlistener != null) {
/* 418 */       this._xsltcFactory.setErrorListener(this._errorlistener);
/*     */     }
/* 420 */     if (this._uriresolver != null) {
/* 421 */       this._xsltcFactory.setURIResolver(this._uriresolver);
/*     */     }
/* 423 */     Templates templates = this._xsltcFactory.newTemplates(src);
/* 424 */     if (templates == null) return null;
/* 425 */     return newXMLFilter(templates);
/*     */   }
/*     */ 
/*     */   public XMLFilter newXMLFilter(Templates templates)
/*     */     throws TransformerConfigurationException
/*     */   {
/*     */     try
/*     */     {
/* 436 */       return new TrAXFilter(templates);
/*     */     }
/*     */     catch (TransformerConfigurationException e1) {
/* 439 */       if (this._xsltcFactory == null) {
/* 440 */         createXSLTCTransformerFactory();
/*     */       }
/* 442 */       ErrorListener errorListener = this._xsltcFactory.getErrorListener();
/* 443 */       if (errorListener != null) {
/*     */         try {
/* 445 */           errorListener.fatalError(e1);
/* 446 */           return null;
/*     */         }
/*     */         catch (TransformerException e2) {
/* 449 */           new TransformerConfigurationException(e2);
/*     */         }
/*     */       }
/* 452 */       throw e1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl
 * JD-Core Version:    0.6.2
 */