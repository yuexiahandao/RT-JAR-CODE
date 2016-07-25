/*     */ package com.sun.org.apache.xpath.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*     */ import com.sun.org.apache.xalan.internal.utils.FeatureManager;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.io.IOException;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
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
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XPathImpl
/*     */   implements javax.xml.xpath.XPath
/*     */ {
/*     */   private XPathVariableResolver variableResolver;
/*     */   private XPathFunctionResolver functionResolver;
/*     */   private XPathVariableResolver origVariableResolver;
/*     */   private XPathFunctionResolver origFunctionResolver;
/*  67 */   private NamespaceContext namespaceContext = null;
/*     */   private JAXPPrefixResolver prefixResolver;
/*  72 */   private boolean featureSecureProcessing = false;
/*  73 */   private boolean useServiceMechanism = true;
/*     */   private final FeatureManager featureManager;
/* 163 */   private static Document d = null;
/*     */ 
/*     */   XPathImpl(XPathVariableResolver vr, XPathFunctionResolver fr)
/*     */   {
/*  77 */     this(vr, fr, false, true, new FeatureManager());
/*     */   }
/*     */ 
/*     */   XPathImpl(XPathVariableResolver vr, XPathFunctionResolver fr, boolean featureSecureProcessing, boolean useServiceMechanism, FeatureManager featureManager)
/*     */   {
/*  83 */     this.origVariableResolver = (this.variableResolver = vr);
/*  84 */     this.origFunctionResolver = (this.functionResolver = fr);
/*  85 */     this.featureSecureProcessing = featureSecureProcessing;
/*  86 */     this.useServiceMechanism = useServiceMechanism;
/*  87 */     this.featureManager = featureManager;
/*     */   }
/*     */ 
/*     */   public void setXPathVariableResolver(XPathVariableResolver resolver)
/*     */   {
/*  96 */     if (resolver == null) {
/*  97 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPathVariableResolver" });
/*     */ 
/* 100 */       throw new NullPointerException(fmsg);
/*     */     }
/* 102 */     this.variableResolver = resolver;
/*     */   }
/*     */ 
/*     */   public XPathVariableResolver getXPathVariableResolver()
/*     */   {
/* 111 */     return this.variableResolver;
/*     */   }
/*     */ 
/*     */   public void setXPathFunctionResolver(XPathFunctionResolver resolver)
/*     */   {
/* 120 */     if (resolver == null) {
/* 121 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPathFunctionResolver" });
/*     */ 
/* 124 */       throw new NullPointerException(fmsg);
/*     */     }
/* 126 */     this.functionResolver = resolver;
/*     */   }
/*     */ 
/*     */   public XPathFunctionResolver getXPathFunctionResolver()
/*     */   {
/* 135 */     return this.functionResolver;
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext nsContext)
/*     */   {
/* 144 */     if (nsContext == null) {
/* 145 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "NamespaceContext" });
/*     */ 
/* 148 */       throw new NullPointerException(fmsg);
/*     */     }
/* 150 */     this.namespaceContext = nsContext;
/* 151 */     this.prefixResolver = new JAXPPrefixResolver(nsContext);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/* 160 */     return this.namespaceContext;
/*     */   }
/*     */ 
/*     */   private DocumentBuilder getParser()
/*     */   {
/*     */     try
/*     */     {
/* 178 */       DocumentBuilderFactory dbf = FactoryImpl.getDOMFactory(this.useServiceMechanism);
/* 179 */       dbf.setNamespaceAware(true);
/* 180 */       dbf.setValidating(false);
/* 181 */       return dbf.newDocumentBuilder();
/*     */     }
/*     */     catch (ParserConfigurationException e) {
/* 184 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private XObject eval(String expression, Object contextItem)
/*     */     throws TransformerException
/*     */   {
/* 191 */     com.sun.org.apache.xpath.internal.XPath xpath = new com.sun.org.apache.xpath.internal.XPath(expression, null, this.prefixResolver, 0);
/*     */ 
/* 193 */     XPathContext xpathSupport = null;
/* 194 */     if (this.functionResolver != null) {
/* 195 */       JAXPExtensionsProvider jep = new JAXPExtensionsProvider(this.functionResolver, this.featureSecureProcessing, this.featureManager);
/*     */ 
/* 197 */       xpathSupport = new XPathContext(jep);
/*     */     } else {
/* 199 */       xpathSupport = new XPathContext();
/*     */     }
/*     */ 
/* 202 */     XObject xobj = null;
/*     */ 
/* 204 */     xpathSupport.setVarStack(new JAXPVariableStack(this.variableResolver));
/*     */ 
/* 207 */     if ((contextItem instanceof Node)) {
/* 208 */       xobj = xpath.execute(xpathSupport, (Node)contextItem, this.prefixResolver);
/*     */     }
/*     */     else {
/* 211 */       xobj = xpath.execute(xpathSupport, -1, this.prefixResolver);
/*     */     }
/*     */ 
/* 214 */     return xobj;
/*     */   }
/*     */ 
/*     */   public Object evaluate(String expression, Object item, QName returnType)
/*     */     throws XPathExpressionException
/*     */   {
/* 250 */     if (expression == null) {
/* 251 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPath expression" });
/*     */ 
/* 254 */       throw new NullPointerException(fmsg);
/*     */     }
/* 256 */     if (returnType == null) {
/* 257 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "returnType" });
/*     */ 
/* 260 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 264 */     if (!isSupported(returnType)) {
/* 265 */       String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
/*     */ 
/* 268 */       throw new IllegalArgumentException(fmsg);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 273 */       XObject resultObject = eval(expression, item);
/* 274 */       return getResultAsType(resultObject, returnType);
/*     */     }
/*     */     catch (NullPointerException npe)
/*     */     {
/* 279 */       throw new XPathExpressionException(npe);
/*     */     } catch (TransformerException te) {
/* 281 */       Throwable nestedException = te.getException();
/* 282 */       if ((nestedException instanceof XPathFunctionException)) {
/* 283 */         throw ((XPathFunctionException)nestedException);
/*     */       }
/*     */ 
/* 287 */       throw new XPathExpressionException(te);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isSupported(QName returnType)
/*     */   {
/* 294 */     if ((returnType.equals(XPathConstants.STRING)) || (returnType.equals(XPathConstants.NUMBER)) || (returnType.equals(XPathConstants.BOOLEAN)) || (returnType.equals(XPathConstants.NODE)) || (returnType.equals(XPathConstants.NODESET)))
/*     */     {
/* 300 */       return true;
/*     */     }
/* 302 */     return false;
/*     */   }
/*     */ 
/*     */   private Object getResultAsType(XObject resultObject, QName returnType)
/*     */     throws TransformerException
/*     */   {
/* 308 */     if (returnType.equals(XPathConstants.STRING)) {
/* 309 */       return resultObject.str();
/*     */     }
/*     */ 
/* 312 */     if (returnType.equals(XPathConstants.NUMBER)) {
/* 313 */       return new Double(resultObject.num());
/*     */     }
/*     */ 
/* 316 */     if (returnType.equals(XPathConstants.BOOLEAN)) {
/* 317 */       return new Boolean(resultObject.bool());
/*     */     }
/*     */ 
/* 320 */     if (returnType.equals(XPathConstants.NODESET)) {
/* 321 */       return resultObject.nodelist();
/*     */     }
/*     */ 
/* 324 */     if (returnType.equals(XPathConstants.NODE)) {
/* 325 */       NodeIterator ni = resultObject.nodeset();
/*     */ 
/* 327 */       return ni.nextNode();
/*     */     }
/* 329 */     String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
/*     */ 
/* 332 */     throw new IllegalArgumentException(fmsg);
/*     */   }
/*     */ 
/*     */   public String evaluate(String expression, Object item)
/*     */     throws XPathExpressionException
/*     */   {
/* 363 */     return (String)evaluate(expression, item, XPathConstants.STRING);
/*     */   }
/*     */ 
/*     */   public XPathExpression compile(String expression)
/*     */     throws XPathExpressionException
/*     */   {
/* 385 */     if (expression == null) {
/* 386 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPath expression" });
/*     */ 
/* 389 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */     try {
/* 392 */       com.sun.org.apache.xpath.internal.XPath xpath = new com.sun.org.apache.xpath.internal.XPath(expression, null, this.prefixResolver, 0);
/*     */ 
/* 395 */       return new XPathExpressionImpl(xpath, this.prefixResolver, this.functionResolver, this.variableResolver, this.featureSecureProcessing, this.useServiceMechanism, this.featureManager);
/*     */     }
/*     */     catch (TransformerException te)
/*     */     {
/* 400 */       throw new XPathExpressionException(te);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object evaluate(String expression, InputSource source, QName returnType)
/*     */     throws XPathExpressionException
/*     */   {
/* 436 */     if (source == null) {
/* 437 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "source" });
/*     */ 
/* 440 */       throw new NullPointerException(fmsg);
/*     */     }
/* 442 */     if (expression == null) {
/* 443 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPath expression" });
/*     */ 
/* 446 */       throw new NullPointerException(fmsg);
/*     */     }
/* 448 */     if (returnType == null) {
/* 449 */       String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "returnType" });
/*     */ 
/* 452 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 457 */     if (!isSupported(returnType)) {
/* 458 */       String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
/*     */ 
/* 461 */       throw new IllegalArgumentException(fmsg);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 466 */       Document document = getParser().parse(source);
/*     */ 
/* 468 */       XObject resultObject = eval(expression, document);
/* 469 */       return getResultAsType(resultObject, returnType);
/*     */     } catch (SAXException e) {
/* 471 */       throw new XPathExpressionException(e);
/*     */     } catch (IOException e) {
/* 473 */       throw new XPathExpressionException(e);
/*     */     } catch (TransformerException te) {
/* 475 */       Throwable nestedException = te.getException();
/* 476 */       if ((nestedException instanceof XPathFunctionException)) {
/* 477 */         throw ((XPathFunctionException)nestedException);
/*     */       }
/* 479 */       throw new XPathExpressionException(te);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String evaluate(String expression, InputSource source)
/*     */     throws XPathExpressionException
/*     */   {
/* 513 */     return (String)evaluate(expression, source, XPathConstants.STRING);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 532 */     this.variableResolver = this.origVariableResolver;
/* 533 */     this.functionResolver = this.origFunctionResolver;
/* 534 */     this.namespaceContext = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.jaxp.XPathImpl
 * JD-Core Version:    0.6.2
 */