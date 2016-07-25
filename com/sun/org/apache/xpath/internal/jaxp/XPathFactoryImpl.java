/*     */ package com.sun.org.apache.xpath.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xalan.internal.utils.FeatureManager;
/*     */ import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;
/*     */ import com.sun.org.apache.xalan.internal.utils.FeaturePropertyBase.State;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import javax.xml.xpath.XPathFactoryConfigurationException;
/*     */ import javax.xml.xpath.XPathFunctionResolver;
/*     */ import javax.xml.xpath.XPathVariableResolver;
/*     */ 
/*     */ public class XPathFactoryImpl extends XPathFactory
/*     */ {
/*     */   private static final String CLASS_NAME = "XPathFactoryImpl";
/*  52 */   private XPathFunctionResolver xPathFunctionResolver = null;
/*     */ 
/*  57 */   private XPathVariableResolver xPathVariableResolver = null;
/*     */ 
/*  62 */   private boolean _isNotSecureProcessing = true;
/*     */ 
/*  66 */   private boolean _isSecureMode = false;
/*     */ 
/*  71 */   private boolean _useServicesMechanism = true;
/*     */   private final FeatureManager _featureManager;
/*     */ 
/*     */   public XPathFactoryImpl()
/*     */   {
/*  76 */     this(true);
/*     */   }
/*     */ 
/*     */   public static XPathFactory newXPathFactoryNoServiceLoader() {
/*  80 */     return new XPathFactoryImpl(false);
/*     */   }
/*     */ 
/*     */   public XPathFactoryImpl(boolean useServicesMechanism) {
/*  84 */     this._featureManager = new FeatureManager();
/*  85 */     if (System.getSecurityManager() != null) {
/*  86 */       this._isSecureMode = true;
/*  87 */       this._isNotSecureProcessing = false;
/*  88 */       this._featureManager.setValue(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION, FeaturePropertyBase.State.FSP, "false");
/*     */     }
/*     */ 
/*  91 */     this._useServicesMechanism = useServicesMechanism;
/*     */   }
/*     */ 
/*     */   public boolean isObjectModelSupported(String objectModel)
/*     */   {
/* 108 */     if (objectModel == null) {
/* 109 */       String fmsg = XSLMessages.createXPATHMessage("ER_OBJECT_MODEL_NULL", new Object[] { getClass().getName() });
/*     */ 
/* 113 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 116 */     if (objectModel.length() == 0) {
/* 117 */       String fmsg = XSLMessages.createXPATHMessage("ER_OBJECT_MODEL_EMPTY", new Object[] { getClass().getName() });
/*     */ 
/* 120 */       throw new IllegalArgumentException(fmsg);
/*     */     }
/*     */ 
/* 124 */     if (objectModel.equals("http://java.sun.com/jaxp/xpath/dom")) {
/* 125 */       return true;
/*     */     }
/*     */ 
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   public XPath newXPath()
/*     */   {
/* 139 */     return new XPathImpl(this.xPathVariableResolver, this.xPathFunctionResolver, !this._isNotSecureProcessing, this._useServicesMechanism, this._featureManager);
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws XPathFactoryConfigurationException
/*     */   {
/* 175 */     if (name == null) {
/* 176 */       String fmsg = XSLMessages.createXPATHMessage("ER_FEATURE_NAME_NULL", new Object[] { "XPathFactoryImpl", new Boolean(value) });
/*     */ 
/* 179 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 183 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 184 */       if ((this._isSecureMode) && (!value)) {
/* 185 */         String fmsg = XSLMessages.createXPATHMessage("ER_SECUREPROCESSING_FEATURE", new Object[] { name, "XPathFactoryImpl", new Boolean(value) });
/*     */ 
/* 188 */         throw new XPathFactoryConfigurationException(fmsg);
/*     */       }
/*     */ 
/* 191 */       this._isNotSecureProcessing = (!value);
/* 192 */       if ((value) && (this._featureManager != null)) {
/* 193 */         this._featureManager.setValue(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION, FeaturePropertyBase.State.FSP, "false");
/*     */       }
/*     */ 
/* 198 */       return;
/*     */     }
/* 200 */     if (name.equals("http://www.oracle.com/feature/use-service-mechanism"))
/*     */     {
/* 202 */       if (!this._isSecureMode)
/* 203 */         this._useServicesMechanism = value;
/* 204 */       return;
/*     */     }
/*     */ 
/* 207 */     if ((this._featureManager != null) && (this._featureManager.setValue(name, FeaturePropertyBase.State.APIPROPERTY, value)))
/*     */     {
/* 209 */       return;
/*     */     }
/*     */ 
/* 213 */     String fmsg = XSLMessages.createXPATHMessage("ER_FEATURE_UNKNOWN", new Object[] { name, "XPathFactoryImpl", new Boolean(value) });
/*     */ 
/* 216 */     throw new XPathFactoryConfigurationException(fmsg);
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws XPathFactoryConfigurationException
/*     */   {
/* 246 */     if (name == null) {
/* 247 */       String fmsg = XSLMessages.createXPATHMessage("ER_GETTING_NULL_FEATURE", new Object[] { "XPathFactoryImpl" });
/*     */ 
/* 250 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 254 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 255 */       return !this._isNotSecureProcessing;
/*     */     }
/* 257 */     if (name.equals("http://www.oracle.com/feature/use-service-mechanism")) {
/* 258 */       return this._useServicesMechanism;
/*     */     }
/*     */ 
/* 262 */     String propertyValue = this._featureManager != null ? this._featureManager.getValueAsString(name) : null;
/*     */ 
/* 264 */     if (propertyValue != null) {
/* 265 */       return this._featureManager.isFeatureEnabled(name);
/*     */     }
/*     */ 
/* 269 */     String fmsg = XSLMessages.createXPATHMessage("ER_GETTING_UNKNOWN_FEATURE", new Object[] { name, "XPathFactoryImpl" });
/*     */ 
/* 273 */     throw new XPathFactoryConfigurationException(fmsg);
/*     */   }
/*     */ 
/*     */   public void setXPathFunctionResolver(XPathFunctionResolver resolver)
/*     */   {
/* 293 */     if (resolver == null) {
/* 294 */       String fmsg = XSLMessages.createXPATHMessage("ER_NULL_XPATH_FUNCTION_RESOLVER", new Object[] { "XPathFactoryImpl" });
/*     */ 
/* 297 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 300 */     this.xPathFunctionResolver = resolver;
/*     */   }
/*     */ 
/*     */   public void setXPathVariableResolver(XPathVariableResolver resolver)
/*     */   {
/* 319 */     if (resolver == null) {
/* 320 */       String fmsg = XSLMessages.createXPATHMessage("ER_NULL_XPATH_VARIABLE_RESOLVER", new Object[] { "XPathFactoryImpl" });
/*     */ 
/* 323 */       throw new NullPointerException(fmsg);
/*     */     }
/*     */ 
/* 326 */     this.xPathVariableResolver = resolver;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl
 * JD-Core Version:    0.6.2
 */