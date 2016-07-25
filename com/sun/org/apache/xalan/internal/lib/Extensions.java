/*     */ package com.sun.org.apache.xalan.internal.lib;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.xslt.EnvironmentCheck;
/*     */ import com.sun.org.apache.xml.internal.utils.Hashtree2Node;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.NodeSet;
/*     */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public class Extensions
/*     */ {
/*     */   static final String JDK_DEFAULT_DOM = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
/*     */ 
/*     */   public static NodeSet nodeset(ExpressionContext myProcessor, Object rtf)
/*     */   {
/*  93 */     if ((rtf instanceof NodeIterator))
/*     */     {
/*  95 */       return new NodeSet((NodeIterator)rtf);
/*     */     }
/*     */     String textNodeValue;
/*     */     String textNodeValue;
/*  99 */     if ((rtf instanceof String))
/*     */     {
/* 101 */       textNodeValue = (String)rtf;
/*     */     }
/*     */     else
/*     */     {
/*     */       String textNodeValue;
/* 103 */       if ((rtf instanceof Boolean))
/*     */       {
/* 105 */         textNodeValue = new XBoolean(((Boolean)rtf).booleanValue()).str();
/*     */       }
/*     */       else
/*     */       {
/*     */         String textNodeValue;
/* 107 */         if ((rtf instanceof Double))
/*     */         {
/* 109 */           textNodeValue = new XNumber(((Double)rtf).doubleValue()).str();
/*     */         }
/*     */         else
/*     */         {
/* 113 */           textNodeValue = rtf.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 118 */     Document myDoc = getDocument();
/*     */ 
/* 120 */     Text textNode = myDoc.createTextNode(textNodeValue);
/* 121 */     DocumentFragment docFrag = myDoc.createDocumentFragment();
/*     */ 
/* 123 */     docFrag.appendChild(textNode);
/*     */ 
/* 125 */     return new NodeSet(docFrag);
/*     */   }
/*     */ 
/*     */   public static NodeList intersection(NodeList nl1, NodeList nl2)
/*     */   {
/* 142 */     return ExsltSets.intersection(nl1, nl2);
/*     */   }
/*     */ 
/*     */   public static NodeList difference(NodeList nl1, NodeList nl2)
/*     */   {
/* 158 */     return ExsltSets.difference(nl1, nl2);
/*     */   }
/*     */ 
/*     */   public static NodeList distinct(NodeList nl)
/*     */   {
/* 175 */     return ExsltSets.distinct(nl);
/*     */   }
/*     */ 
/*     */   public static boolean hasSameNodes(NodeList nl1, NodeList nl2)
/*     */   {
/* 188 */     NodeSet ns1 = new NodeSet(nl1);
/* 189 */     NodeSet ns2 = new NodeSet(nl2);
/*     */ 
/* 191 */     if (ns1.getLength() != ns2.getLength()) {
/* 192 */       return false;
/*     */     }
/* 194 */     for (int i = 0; i < ns1.getLength(); i++)
/*     */     {
/* 196 */       Node n = ns1.elementAt(i);
/*     */ 
/* 198 */       if (!ns2.contains(n)) {
/* 199 */         return false;
/*     */       }
/*     */     }
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */   public static XObject evaluate(ExpressionContext myContext, String xpathExpr)
/*     */     throws SAXNotSupportedException
/*     */   {
/* 225 */     return ExsltDynamic.evaluate(myContext, xpathExpr);
/*     */   }
/*     */ 
/*     */   public static NodeList tokenize(String toTokenize, String delims)
/*     */   {
/* 244 */     Document doc = getDocument();
/*     */ 
/* 246 */     StringTokenizer lTokenizer = new StringTokenizer(toTokenize, delims);
/* 247 */     NodeSet resultSet = new NodeSet();
/*     */ 
/* 249 */     synchronized (doc)
/*     */     {
/* 251 */       while (lTokenizer.hasMoreTokens())
/*     */       {
/* 253 */         resultSet.addNode(doc.createTextNode(lTokenizer.nextToken()));
/*     */       }
/*     */     }
/*     */ 
/* 257 */     return resultSet;
/*     */   }
/*     */ 
/*     */   public static NodeList tokenize(String toTokenize)
/*     */   {
/* 275 */     return tokenize(toTokenize, " \t\n\r");
/*     */   }
/*     */ 
/*     */   public static Node checkEnvironment(ExpressionContext myContext)
/*     */   {
/* 302 */     Document factoryDocument = getDocument();
/*     */ 
/* 304 */     Node resultNode = null;
/*     */     try
/*     */     {
/* 309 */       resultNode = checkEnvironmentUsingWhich(myContext, factoryDocument);
/*     */ 
/* 311 */       if (null != resultNode) {
/* 312 */         return resultNode;
/*     */       }
/*     */ 
/* 315 */       EnvironmentCheck envChecker = new EnvironmentCheck();
/* 316 */       Hashtable h = envChecker.getEnvironmentHash();
/* 317 */       resultNode = factoryDocument.createElement("checkEnvironmentExtension");
/* 318 */       envChecker.appendEnvironmentReport(resultNode, factoryDocument, h);
/* 319 */       envChecker = null;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 323 */       throw new WrappedRuntimeException(e);
/*     */     }
/*     */ 
/* 326 */     return resultNode;
/*     */   }
/*     */ 
/*     */   private static Node checkEnvironmentUsingWhich(ExpressionContext myContext, Document factoryDocument)
/*     */   {
/* 340 */     String WHICH_CLASSNAME = "org.apache.env.Which";
/* 341 */     String WHICH_METHODNAME = "which";
/* 342 */     Class[] WHICH_METHOD_ARGS = { Hashtable.class, String.class, String.class };
/*     */     try
/*     */     {
/* 348 */       Class clazz = ObjectFactory.findProviderClass("org.apache.env.Which", true);
/* 349 */       if (null == clazz) {
/* 350 */         return null;
/*     */       }
/*     */ 
/* 353 */       Method method = clazz.getMethod("which", WHICH_METHOD_ARGS);
/* 354 */       Hashtable report = new Hashtable();
/*     */ 
/* 357 */       Object[] methodArgs = { report, "XmlCommons;Xalan;Xerces;Crimson;Ant", "" };
/* 358 */       Object returnValue = method.invoke(null, methodArgs);
/*     */ 
/* 361 */       Node resultNode = factoryDocument.createElement("checkEnvironmentExtension");
/* 362 */       Hashtree2Node.appendHashToNode(report, "whichReport", resultNode, factoryDocument);
/*     */ 
/* 365 */       return resultNode;
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*     */     }
/* 370 */     return null;
/*     */   }
/*     */ 
/*     */   private static Document getDocument()
/*     */   {
/*     */     try
/*     */     {
/* 381 */       if (System.getSecurityManager() == null) {
/* 382 */         return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
/*     */       }
/* 384 */       return DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", null).newDocumentBuilder().newDocument();
/*     */     }
/*     */     catch (ParserConfigurationException pce)
/*     */     {
/* 389 */       throw new WrappedRuntimeException(pce);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.Extensions
 * JD-Core Version:    0.6.2
 */