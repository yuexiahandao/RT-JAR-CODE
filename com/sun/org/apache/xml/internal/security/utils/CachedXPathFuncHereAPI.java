/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.FuncHere;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.FuncHereContext;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
/*     */ import com.sun.org.apache.xpath.internal.CachedXPathAPI;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.XPath;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.compiler.FunctionTable;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ 
/*     */ public class CachedXPathFuncHereAPI
/*     */ {
/*  52 */   static Logger log = Logger.getLogger(CachedXPathFuncHereAPI.class.getName());
/*     */ 
/*  58 */   FuncHereContext _funcHereContext = null;
/*     */ 
/*  61 */   DTMManager _dtmManager = null;
/*     */ 
/*  63 */   XPathContext _context = null;
/*     */ 
/*  65 */   String xpathStr = null;
/*     */ 
/*  67 */   XPath xpath = null;
/*     */ 
/*  69 */   static FunctionTable _funcTable = null;
/*     */ 
/*     */   public FuncHereContext getFuncHereContext()
/*     */   {
/*  81 */     return this._funcHereContext;
/*     */   }
/*     */ 
/*     */   private CachedXPathFuncHereAPI()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CachedXPathFuncHereAPI(XPathContext paramXPathContext)
/*     */   {
/*  96 */     this._dtmManager = paramXPathContext.getDTMManager();
/*  97 */     this._context = paramXPathContext;
/*     */   }
/*     */ 
/*     */   public CachedXPathFuncHereAPI(CachedXPathAPI paramCachedXPathAPI)
/*     */   {
/* 106 */     this._dtmManager = paramCachedXPathAPI.getXPathContext().getDTMManager();
/* 107 */     this._context = paramCachedXPathAPI.getXPathContext();
/*     */   }
/*     */ 
/*     */   public Node selectSingleNode(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/* 123 */     return selectSingleNode(paramNode1, paramNode2, paramNode1);
/*     */   }
/*     */ 
/*     */   public Node selectSingleNode(Node paramNode1, Node paramNode2, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/* 142 */     NodeIterator localNodeIterator = selectNodeIterator(paramNode1, paramNode2, paramNode3);
/*     */ 
/* 146 */     return localNodeIterator.nextNode();
/*     */   }
/*     */ 
/*     */   public NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/* 161 */     return selectNodeIterator(paramNode1, paramNode2, paramNode1);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/* 181 */     XObject localXObject = eval(paramNode1, paramNode2, getStrFromNode(paramNode2), paramNode3);
/*     */ 
/* 184 */     return localXObject.nodeset();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public NodeList selectNodeList(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/* 200 */     return selectNodeList(paramNode1, paramNode2, getStrFromNode(paramNode2), paramNode1);
/*     */   }
/*     */ 
/*     */   public NodeList selectNodeList(Node paramNode1, Node paramNode2, String paramString, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/* 220 */     XObject localXObject = eval(paramNode1, paramNode2, paramString, paramNode3);
/*     */ 
/* 223 */     return localXObject.nodelist();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public XObject eval(Node paramNode1, Node paramNode2)
/*     */     throws TransformerException
/*     */   {
/* 244 */     return eval(paramNode1, paramNode2, getStrFromNode(paramNode2), paramNode1);
/*     */   }
/*     */ 
/*     */   public XObject eval(Node paramNode1, Node paramNode2, String paramString, Node paramNode3)
/*     */     throws TransformerException
/*     */   {
/* 278 */     if (this._funcHereContext == null) {
/* 279 */       this._funcHereContext = new FuncHereContext(paramNode2, this._dtmManager);
/*     */     }
/*     */ 
/* 287 */     PrefixResolverDefault localPrefixResolverDefault = new PrefixResolverDefault(paramNode3.getNodeType() == 9 ? ((Document)paramNode3).getDocumentElement() : paramNode3);
/*     */ 
/* 295 */     if (paramString != this.xpathStr) {
/* 296 */       if (paramString.indexOf("here()") > 0) {
/* 297 */         this._context.reset();
/* 298 */         this._dtmManager = this._context.getDTMManager();
/*     */       }
/* 300 */       this.xpath = createXPath(paramString, localPrefixResolverDefault);
/* 301 */       this.xpathStr = paramString;
/*     */     }
/*     */ 
/* 306 */     int i = this._funcHereContext.getDTMHandleFromNode(paramNode1);
/*     */ 
/* 308 */     return this.xpath.execute(this._funcHereContext, i, localPrefixResolverDefault);
/*     */   }
/*     */ 
/*     */   public XObject eval(Node paramNode1, Node paramNode2, String paramString, PrefixResolver paramPrefixResolver)
/*     */     throws TransformerException
/*     */   {
/* 345 */     if (paramString != this.xpathStr) {
/* 346 */       if (paramString.indexOf("here()") > 0) {
/* 347 */         this._context.reset();
/* 348 */         this._dtmManager = this._context.getDTMManager();
/*     */       }
/*     */       try {
/* 351 */         this.xpath = createXPath(paramString, paramPrefixResolver);
/*     */       }
/*     */       catch (TransformerException localTransformerException) {
/* 354 */         Throwable localThrowable = localTransformerException.getCause();
/* 355 */         if (((localThrowable instanceof ClassNotFoundException)) && 
/* 356 */           (localThrowable.getMessage().indexOf("FuncHere") > 0)) {
/* 357 */           throw new RuntimeException(I18n.translate("endorsed.jdk1.4.0") + localTransformerException);
/*     */         }
/*     */ 
/* 360 */         throw localTransformerException;
/*     */       }
/* 362 */       this.xpathStr = paramString;
/*     */     }
/*     */ 
/* 366 */     if (this._funcHereContext == null) {
/* 367 */       this._funcHereContext = new FuncHereContext(paramNode2, this._dtmManager);
/*     */     }
/*     */ 
/* 371 */     int i = this._funcHereContext.getDTMHandleFromNode(paramNode1);
/*     */ 
/* 373 */     return this.xpath.execute(this._funcHereContext, i, paramPrefixResolver);
/*     */   }
/*     */ 
/*     */   private XPath createXPath(String paramString, PrefixResolver paramPrefixResolver) throws TransformerException {
/* 377 */     XPath localXPath = null;
/* 378 */     Class[] arrayOfClass = { String.class, SourceLocator.class, PrefixResolver.class, Integer.TYPE, ErrorListener.class, FunctionTable.class };
/*     */ 
/* 380 */     Object[] arrayOfObject = { paramString, null, paramPrefixResolver, new Integer(0), null, _funcTable };
/*     */     try {
/* 382 */       Constructor localConstructor = XPath.class.getConstructor(arrayOfClass);
/* 383 */       localXPath = (XPath)localConstructor.newInstance(arrayOfObject);
/*     */     } catch (Throwable localThrowable) {
/*     */     }
/* 386 */     if (localXPath == null) {
/* 387 */       localXPath = new XPath(paramString, null, paramPrefixResolver, 0, null);
/*     */     }
/* 389 */     return localXPath;
/*     */   }
/*     */ 
/*     */   public static String getStrFromNode(Node paramNode)
/*     */   {
/* 400 */     if (paramNode.getNodeType() == 3)
/*     */     {
/* 404 */       StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 406 */       for (Node localNode = paramNode.getParentNode().getFirstChild(); 
/* 407 */         localNode != null; 
/* 408 */         localNode = localNode.getNextSibling()) {
/* 409 */         if (localNode.getNodeType() == 3) {
/* 410 */           localStringBuffer.append(((Text)localNode).getData());
/*     */         }
/*     */       }
/*     */ 
/* 414 */       return localStringBuffer.toString();
/* 415 */     }if (paramNode.getNodeType() == 2)
/* 416 */       return ((Attr)paramNode).getNodeValue();
/* 417 */     if (paramNode.getNodeType() == 7) {
/* 418 */       return ((ProcessingInstruction)paramNode).getNodeValue();
/*     */     }
/*     */ 
/* 421 */     return null;
/*     */   }
/* 425 */   private static void fixupFunctionTable() { int i = 0;
/* 426 */     log.log(Level.INFO, "Registering Here function");
/*     */     Method localMethod;
/*     */     Object[] arrayOfObject;
/*     */     try {
/* 431 */       Class[] arrayOfClass1 = { String.class, Expression.class };
/* 432 */       localMethod = FunctionTable.class.getMethod("installFunction", arrayOfClass1);
/* 433 */       if ((localMethod.getModifiers() & 0x8) != 0) {
/* 434 */         arrayOfObject = new Object[] { "here", new FuncHere() };
/* 435 */         localMethod.invoke(null, arrayOfObject);
/* 436 */         i = 1;
/*     */       }
/*     */     } catch (Throwable localThrowable1) {
/* 439 */       log.log(Level.FINE, "Error installing function using the static installFunction method", localThrowable1);
/*     */     }
/* 441 */     if (i == 0) {
/*     */       try {
/* 443 */         _funcTable = new FunctionTable();
/* 444 */         Class[] arrayOfClass2 = { String.class, Class.class };
/* 445 */         localMethod = FunctionTable.class.getMethod("installFunction", arrayOfClass2);
/* 446 */         arrayOfObject = new Object[] { "here", FuncHere.class };
/* 447 */         localMethod.invoke(_funcTable, arrayOfObject);
/* 448 */         i = 1;
/*     */       } catch (Throwable localThrowable2) {
/* 450 */         log.log(Level.FINE, "Error installing function using the static installFunction method", localThrowable2);
/*     */       }
/*     */     }
/* 453 */     if (log.isLoggable(Level.FINE))
/* 454 */       if (i != 0) {
/* 455 */         log.log(Level.FINE, "Registered class " + FuncHere.class.getName() + " for XPath function 'here()' function in internal table");
/*     */       }
/*     */       else
/* 458 */         log.log(Level.FINE, "Unable to register class " + FuncHere.class.getName() + " for XPath function 'here()' function in internal table");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  72 */     fixupFunctionTable();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.CachedXPathFuncHereAPI
 * JD-Core Version:    0.6.2
 */