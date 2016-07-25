/*     */ package javax.xml.xpath;
/*     */ 
/*     */ public abstract class XPathFactory
/*     */ {
/*     */   public static final String DEFAULT_PROPERTY_NAME = "javax.xml.xpath.XPathFactory";
/*     */   public static final String DEFAULT_OBJECT_MODEL_URI = "http://java.sun.com/jaxp/xpath/dom";
/*  66 */   private static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*     */   public static final XPathFactory newInstance()
/*     */   {
/*     */     try
/*     */     {
/*  96 */       return newInstance("http://java.sun.com/jaxp/xpath/dom");
/*     */     } catch (XPathFactoryConfigurationException xpathFactoryConfigurationException) {
/*  98 */       throw new RuntimeException("XPathFactory#newInstance() failed to create an XPathFactory for the default object model: http://java.sun.com/jaxp/xpath/dom with the XPathFactoryConfigurationException: " + xpathFactoryConfigurationException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final XPathFactory newInstance(String uri)
/*     */     throws XPathFactoryConfigurationException
/*     */   {
/* 163 */     if (uri == null) {
/* 164 */       throw new NullPointerException("XPathFactory#newInstance(String uri) cannot be called with uri == null");
/*     */     }
/*     */ 
/* 169 */     if (uri.length() == 0) {
/* 170 */       throw new IllegalArgumentException("XPathFactory#newInstance(String uri) cannot be called with uri == \"\"");
/*     */     }
/*     */ 
/* 175 */     ClassLoader classLoader = ss.getContextClassLoader();
/*     */ 
/* 177 */     if (classLoader == null)
/*     */     {
/* 179 */       classLoader = XPathFactory.class.getClassLoader();
/*     */     }
/*     */ 
/* 182 */     XPathFactory xpathFactory = new XPathFactoryFinder(classLoader).newFactory(uri);
/*     */ 
/* 184 */     if (xpathFactory == null) {
/* 185 */       throw new XPathFactoryConfigurationException("No XPathFactory implementation found for the object model: " + uri);
/*     */     }
/*     */ 
/* 191 */     return xpathFactory;
/*     */   }
/*     */ 
/*     */   public static XPathFactory newInstance(String uri, String factoryClassName, ClassLoader classLoader)
/*     */     throws XPathFactoryConfigurationException
/*     */   {
/* 242 */     ClassLoader cl = classLoader;
/*     */ 
/* 244 */     if (uri == null) {
/* 245 */       throw new NullPointerException("XPathFactory#newInstance(String uri) cannot be called with uri == null");
/*     */     }
/*     */ 
/* 250 */     if (uri.length() == 0) {
/* 251 */       throw new IllegalArgumentException("XPathFactory#newInstance(String uri) cannot be called with uri == \"\"");
/*     */     }
/*     */ 
/* 256 */     if (cl == null) {
/* 257 */       cl = ss.getContextClassLoader();
/*     */     }
/*     */ 
/* 260 */     XPathFactory f = new XPathFactoryFinder(cl).createInstance(factoryClassName);
/*     */ 
/* 262 */     if (f == null) {
/* 263 */       throw new XPathFactoryConfigurationException("No XPathFactory implementation found for the object model: " + uri);
/*     */     }
/*     */ 
/* 269 */     if (f.isObjectModelSupported(uri)) {
/* 270 */       return f;
/*     */     }
/* 272 */     throw new XPathFactoryConfigurationException("Factory " + factoryClassName + " doesn't support given " + uri + " object model");
/*     */   }
/*     */ 
/*     */   public abstract boolean isObjectModelSupported(String paramString);
/*     */ 
/*     */   public abstract void setFeature(String paramString, boolean paramBoolean)
/*     */     throws XPathFactoryConfigurationException;
/*     */ 
/*     */   public abstract boolean getFeature(String paramString)
/*     */     throws XPathFactoryConfigurationException;
/*     */ 
/*     */   public abstract void setXPathVariableResolver(XPathVariableResolver paramXPathVariableResolver);
/*     */ 
/*     */   public abstract void setXPathFunctionResolver(XPathFunctionResolver paramXPathFunctionResolver);
/*     */ 
/*     */   public abstract XPath newXPath();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.xpath.XPathFactory
 * JD-Core Version:    0.6.2
 */