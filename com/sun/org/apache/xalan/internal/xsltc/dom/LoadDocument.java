/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.EmptyIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*     */ import java.io.FileNotFoundException;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ 
/*     */ public final class LoadDocument
/*     */ {
/*     */   private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */ 
/*     */   public static DTMAxisIterator documentF(Object arg1, DTMAxisIterator arg2, String xslURI, AbstractTranslet translet, DOM dom)
/*     */     throws TransletException
/*     */   {
/*  67 */     String baseURI = null;
/*  68 */     int arg2FirstNode = arg2.next();
/*  69 */     if (arg2FirstNode == -1)
/*     */     {
/*  71 */       return EmptyIterator.getInstance();
/*     */     }
/*     */ 
/*  76 */     baseURI = dom.getDocumentURI(arg2FirstNode);
/*  77 */     if (!SystemIDResolver.isAbsoluteURI(baseURI)) {
/*  78 */       baseURI = SystemIDResolver.getAbsoluteURIFromRelative(baseURI);
/*     */     }
/*     */     try
/*     */     {
/*  82 */       if ((arg1 instanceof String)) {
/*  83 */         if (((String)arg1).length() == 0) {
/*  84 */           return document(xslURI, "", translet, dom);
/*     */         }
/*  86 */         return document((String)arg1, baseURI, translet, dom);
/*     */       }
/*  88 */       if ((arg1 instanceof DTMAxisIterator)) {
/*  89 */         return document((DTMAxisIterator)arg1, baseURI, translet, dom);
/*     */       }
/*  91 */       String err = "document(" + arg1.toString() + ")";
/*  92 */       throw new IllegalArgumentException(err);
/*     */     }
/*     */     catch (Exception e) {
/*  95 */       throw new TransletException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static DTMAxisIterator documentF(Object arg, String xslURI, AbstractTranslet translet, DOM dom)
/*     */     throws TransletException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       if ((arg instanceof String)) {
/* 110 */         if (xslURI == null) {
/* 111 */           xslURI = "";
/*     */         }
/* 113 */         String baseURI = xslURI;
/* 114 */         if (!SystemIDResolver.isAbsoluteURI(xslURI)) {
/* 115 */           baseURI = SystemIDResolver.getAbsoluteURIFromRelative(xslURI);
/*     */         }
/* 117 */         String href = (String)arg;
/* 118 */         if (href.length() == 0) {
/* 119 */           href = "";
/*     */ 
/* 123 */           TemplatesImpl templates = (TemplatesImpl)translet.getTemplates();
/* 124 */           DOM sdom = null;
/* 125 */           if (templates != null) {
/* 126 */             sdom = templates.getStylesheetDOM();
/*     */           }
/*     */ 
/* 132 */           if (sdom != null) {
/* 133 */             return document(sdom, translet, dom);
/*     */           }
/*     */ 
/* 136 */           return document(href, baseURI, translet, dom, true);
/*     */         }
/*     */ 
/* 140 */         return document(href, baseURI, translet, dom);
/*     */       }
/* 142 */       if ((arg instanceof DTMAxisIterator)) {
/* 143 */         return document((DTMAxisIterator)arg, null, translet, dom);
/*     */       }
/* 145 */       String err = "document(" + arg.toString() + ")";
/* 146 */       throw new IllegalArgumentException(err);
/*     */     }
/*     */     catch (Exception e) {
/* 149 */       throw new TransletException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static DTMAxisIterator document(String uri, String base, AbstractTranslet translet, DOM dom)
/*     */     throws Exception
/*     */   {
/* 157 */     return document(uri, base, translet, dom, false);
/*     */   }
/*     */ 
/*     */   private static DTMAxisIterator document(String uri, String base, AbstractTranslet translet, DOM dom, boolean cacheDOM)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 166 */       String originalUri = uri;
/* 167 */       MultiDOM multiplexer = (MultiDOM)dom;
/*     */ 
/* 170 */       if ((base != null) && (!base.equals(""))) {
/* 171 */         uri = SystemIDResolver.getAbsoluteURI(uri, base);
/*     */       }
/*     */ 
/* 176 */       if ((uri == null) || (uri.equals(""))) {
/* 177 */         return EmptyIterator.getInstance();
/*     */       }
/*     */ 
/* 181 */       int mask = multiplexer.getDocumentMask(uri);
/* 182 */       if (mask != -1) {
/* 183 */         DOM newDom = ((DOMAdapter)multiplexer.getDOMAdapter(uri)).getDOMImpl();
/*     */ 
/* 185 */         if ((newDom instanceof DOMEnhancedForDTM)) {
/* 186 */           return new SingletonIterator(((DOMEnhancedForDTM)newDom).getDocument(), true);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 193 */       DOMCache cache = translet.getDOMCache();
/*     */ 
/* 196 */       mask = multiplexer.nextMask();
/*     */       DOM newdom;
/* 198 */       if (cache != null) {
/* 199 */         DOM newdom = cache.retrieveDocument(base, originalUri, translet);
/* 200 */         if (newdom == null) {
/* 201 */           Exception e = new FileNotFoundException(originalUri);
/* 202 */           throw new TransletException(e);
/*     */         }
/*     */       } else {
/* 205 */         String accessError = SecuritySupport.checkAccess(uri, translet.getAllowedProtocols(), "all");
/* 206 */         if (accessError != null) {
/* 207 */           ErrorMsg msg = new ErrorMsg("ACCESSING_XSLT_TARGET_ERR", SecuritySupport.sanitizePath(uri), accessError);
/*     */ 
/* 209 */           throw new Exception(msg.toString());
/*     */         }
/*     */ 
/* 215 */         XSLTCDTMManager dtmManager = (XSLTCDTMManager)multiplexer.getDTMManager();
/*     */ 
/* 217 */         DOMEnhancedForDTM enhancedDOM = (DOMEnhancedForDTM)dtmManager.getDTM(new StreamSource(uri), false, null, true, false, translet.hasIdCall(), cacheDOM);
/*     */ 
/* 221 */         newdom = enhancedDOM;
/*     */ 
/* 224 */         if (cacheDOM) {
/* 225 */           TemplatesImpl templates = (TemplatesImpl)translet.getTemplates();
/* 226 */           if (templates != null) {
/* 227 */             templates.setStylesheetDOM(enhancedDOM);
/*     */           }
/*     */         }
/*     */ 
/* 231 */         translet.prepassDocument(enhancedDOM);
/* 232 */         enhancedDOM.setDocumentURI(uri);
/*     */       }
/*     */ 
/* 236 */       DOMAdapter domAdapter = translet.makeDOMAdapter(newdom);
/* 237 */       multiplexer.addDOMAdapter(domAdapter);
/*     */ 
/* 240 */       translet.buildKeys(domAdapter, null, null, newdom.getDocument());
/*     */ 
/* 243 */       return new SingletonIterator(newdom.getDocument(), true);
/*     */     } catch (Exception e) {
/* 245 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static DTMAxisIterator document(DTMAxisIterator arg1, String baseURI, AbstractTranslet translet, DOM dom)
/*     */     throws Exception
/*     */   {
/* 255 */     UnionIterator union = new UnionIterator(dom);
/* 256 */     int node = -1;
/*     */ 
/* 258 */     while ((node = arg1.next()) != -1) {
/* 259 */       String uri = dom.getStringValueX(node);
/*     */ 
/* 261 */       if (baseURI == null) {
/* 262 */         baseURI = dom.getDocumentURI(node);
/* 263 */         if (!SystemIDResolver.isAbsoluteURI(baseURI))
/* 264 */           baseURI = SystemIDResolver.getAbsoluteURIFromRelative(baseURI);
/*     */       }
/* 266 */       union.addIterator(document(uri, baseURI, translet, dom));
/*     */     }
/* 268 */     return union;
/*     */   }
/*     */ 
/*     */   private static DTMAxisIterator document(DOM newdom, AbstractTranslet translet, DOM dom)
/*     */     throws Exception
/*     */   {
/* 285 */     DTMManager dtmManager = ((MultiDOM)dom).getDTMManager();
/*     */ 
/* 287 */     if ((dtmManager != null) && ((newdom instanceof DTM))) {
/* 288 */       ((DTM)newdom).migrateTo(dtmManager);
/*     */     }
/*     */ 
/* 291 */     translet.prepassDocument(newdom);
/*     */ 
/* 294 */     DOMAdapter domAdapter = translet.makeDOMAdapter(newdom);
/* 295 */     ((MultiDOM)dom).addDOMAdapter(domAdapter);
/*     */ 
/* 298 */     translet.buildKeys(domAdapter, null, null, newdom.getDocument());
/*     */ 
/* 302 */     return new SingletonIterator(newdom.getDocument(), true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.LoadDocument
 * JD-Core Version:    0.6.2
 */