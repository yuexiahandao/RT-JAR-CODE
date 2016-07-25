/*     */ package com.sun.org.apache.xpath.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*     */ import com.sun.org.apache.xalan.internal.utils.FeatureManager;
/*     */ import com.sun.org.apache.xpath.internal.XPath;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpression;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFunctionException;
/*     */ import javax.xml.xpath.XPathFunctionResolver;
/*     */ import javax.xml.xpath.XPathVariableResolver;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class XPathExpressionImpl
/*     */   implements XPathExpression
/*     */ {
/*     */   private XPathFunctionResolver functionResolver;
/*     */   private XPathVariableResolver variableResolver;
/*     */   private JAXPPrefixResolver prefixResolver;
/*     */   private XPath xpath;
/*  68 */   private boolean featureSecureProcessing = false;
/*     */ 
/*  70 */   private boolean useServicesMechanism = true;
/*     */   private final FeatureManager featureManager;
/* 241 */   static DocumentBuilderFactory dbf = null;
/* 242 */   static DocumentBuilder db = null;
/* 243 */   static Document d = null;
/*     */ 
/*     */   protected XPathExpressionImpl()
/*     */   {
/*  78 */     this(null, null, null, null, false, true, new FeatureManager());
/*     */   }
/*     */ 
/*     */   protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver)
/*     */   {
/*  86 */     this(xpath, prefixResolver, functionResolver, variableResolver, false, true, new FeatureManager());
/*     */   }
/*     */ 
/*     */   protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver, boolean featureSecureProcessing, boolean useServicesMechanism, FeatureManager featureManager)
/*     */   {
/*  94 */     this.xpath = xpath;
/*  95 */     this.prefixResolver = prefixResolver;
/*  96 */     this.functionResolver = functionResolver;
/*  97 */     this.variableResolver = variableResolver;
/*  98 */     this.featureSecureProcessing = featureSecureProcessing;
/*  99 */     this.useServicesMechanism = useServicesMechanism;
/* 100 */     this.featureManager = featureManager;
/*     */   }
/*     */ 
/*     */   public void setXPath(XPath xpath) {
/* 104 */     this.xpath = xpath;
/*     */   }
/*     */ 
/*     */   public Object eval(Object item, QName returnType) throws TransformerException
/*     */   {
/* 109 */     XObject resultObject = eval(item);
/* 110 */     return getResultAsType(resultObject, returnType);
/*     */   }
/*     */ 
/*     */   private XObject eval(Object contextItem) throws TransformerException
/*     */   {
/* 115 */     XPathContext xpathSupport = null;
/* 116 */     if (this.functionResolver != null) {
/* 117 */       JAXPExtensionsProvider jep = new JAXPExtensionsProvider(this.functionResolver, this.featureSecureProcessing, this.featureManager);
/*     */ 
/* 119 */       xpathSupport = new XPathContext(jep);
/*     */     } else {
/* 121 */       xpathSupport = new XPathContext();
/*     */     }
/*     */ 
/* 124 */     xpathSupport.setVarStack(new JAXPVariableStack(this.variableResolver));
/* 125 */     XObject xobj = null;
/*     */ 
/* 127 */     Node contextNode = (Node)contextItem;
/*     */ 
/* 132 */     if (contextNode == null)
/* 133 */       xobj = this.xpath.execute(xpathSupport, -1, this.prefixResolver);
/*     */     else {
/* 135 */       xobj = this.xpath.execute(xpathSupport, contextNode, this.prefixResolver);
/*     */     }
/* 137 */     return xobj;
/*     */   }
/*     */ 
/*     */   public Object evaluate(Object item, QName returnType)
/*     */     throws XPathExpressionException
/*     */   {
/* 175 */     if (returnType == null)
/*     */     {
/* 177 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "returnType" });
/*     */ 
/* 180 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 184 */     if (!isSupported(returnType)) {
/* 185 */       String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
/*     */ 
/* 188 */       throw new IllegalArgumentException(fmsg);
/*     */     }
/*     */     try {
/* 191 */       return eval(item, returnType);
/*     */     }
/*     */     catch (NullPointerException npe)
/*     */     {
/* 196 */       throw new XPathExpressionException(npe);
/*     */     } catch (TransformerException te) {
/* 198 */       Throwable nestedException = te.getException();
/* 199 */       if ((nestedException instanceof XPathFunctionException)) {
/* 200 */         throw ((XPathFunctionException)nestedException);
/*     */       }
/*     */ 
/* 204 */       throw new XPathExpressionException(te);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String evaluate(Object item)
/*     */     throws XPathExpressionException
/*     */   {
/* 236 */     return (String)evaluate(item, XPathConstants.STRING);
/*     */   }
/*     */ 
/*     */   public Object evaluate(InputSource source, QName returnType)
/*     */     throws XPathExpressionException
/*     */   {
/* 281 */     if ((source == null) || (returnType == null)) {
/* 282 */       String fmsg = XSLMessages.createXPATHMessage("ER_SOURCE_RETURN_TYPE_CANNOT_BE_NULL", null);
/*     */ 
/* 285 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 289 */     if (!isSupported(returnType)) {
/* 290 */       String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
/*     */ 
/* 293 */       throw new IllegalArgumentException(fmsg);
/*     */     }
/*     */     try {
/* 296 */       if (dbf == null) {
/* 297 */         dbf = FactoryImpl.getDOMFactory(this.useServicesMechanism);
/* 298 */         dbf.setNamespaceAware(true);
/* 299 */         dbf.setValidating(false);
/*     */       }
/* 301 */       db = dbf.newDocumentBuilder();
/* 302 */       Document document = db.parse(source);
/* 303 */       return eval(document, returnType);
/*     */     } catch (Exception e) {
/* 305 */       throw new XPathExpressionException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String evaluate(InputSource source)
/*     */     throws XPathExpressionException
/*     */   {
/* 332 */     return (String)evaluate(source, XPathConstants.STRING);
/*     */   }
/*     */ 
/*     */   private boolean isSupported(QName returnType)
/*     */   {
/* 337 */     if ((returnType.equals(XPathConstants.STRING)) || (returnType.equals(XPathConstants.NUMBER)) || (returnType.equals(XPathConstants.BOOLEAN)) || (returnType.equals(XPathConstants.NODE)) || (returnType.equals(XPathConstants.NODESET)))
/*     */     {
/* 343 */       return true;
/*     */     }
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   private Object getResultAsType(XObject resultObject, QName returnType)
/*     */     throws TransformerException
/*     */   {
/* 351 */     if (returnType.equals(XPathConstants.STRING)) {
/* 352 */       return resultObject.str();
/*     */     }
/*     */ 
/* 355 */     if (returnType.equals(XPathConstants.NUMBER)) {
/* 356 */       return new Double(resultObject.num());
/*     */     }
/*     */ 
/* 359 */     if (returnType.equals(XPathConstants.BOOLEAN)) {
/* 360 */       return new Boolean(resultObject.bool());
/*     */     }
/*     */ 
/* 363 */     if (returnType.equals(XPathConstants.NODESET)) {
/* 364 */       return resultObject.nodelist();
/*     */     }
/*     */ 
/* 367 */     if (returnType.equals(XPathConstants.NODE)) {
/* 368 */       NodeIterator ni = resultObject.nodeset();
/*     */ 
/* 370 */       return ni.nextNode();
/*     */     }
/*     */ 
/* 374 */     String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
/*     */ 
/* 377 */     throw new IllegalArgumentException(fmsg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.jaxp.XPathExpressionImpl
 * JD-Core Version:    0.6.2
 */