/*     */ package javax.xml.transform;
/*     */ 
/*     */ public abstract class TransformerFactory
/*     */ {
/*     */   public static TransformerFactory newInstance()
/*     */     throws TransformerFactoryConfigurationError
/*     */   {
/*     */     try
/*     */     {
/* 101 */       return (TransformerFactory)FactoryFinder.find("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
/*     */     }
/*     */     catch (FactoryFinder.ConfigurationError e)
/*     */     {
/* 107 */       throw new TransformerFactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static TransformerFactory newInstance(String factoryClassName, ClassLoader classLoader)
/*     */     throws TransformerFactoryConfigurationError
/*     */   {
/*     */     try
/*     */     {
/* 152 */       return (TransformerFactory)FactoryFinder.newInstance(factoryClassName, classLoader, false);
/*     */     } catch (FactoryFinder.ConfigurationError e) {
/* 154 */       throw new TransformerFactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract Transformer newTransformer(Source paramSource)
/*     */     throws TransformerConfigurationException;
/*     */ 
/*     */   public abstract Transformer newTransformer()
/*     */     throws TransformerConfigurationException;
/*     */ 
/*     */   public abstract Templates newTemplates(Source paramSource)
/*     */     throws TransformerConfigurationException;
/*     */ 
/*     */   public abstract Source getAssociatedStylesheet(Source paramSource, String paramString1, String paramString2, String paramString3)
/*     */     throws TransformerConfigurationException;
/*     */ 
/*     */   public abstract void setURIResolver(URIResolver paramURIResolver);
/*     */ 
/*     */   public abstract URIResolver getURIResolver();
/*     */ 
/*     */   public abstract void setFeature(String paramString, boolean paramBoolean)
/*     */     throws TransformerConfigurationException;
/*     */ 
/*     */   public abstract boolean getFeature(String paramString);
/*     */ 
/*     */   public abstract void setAttribute(String paramString, Object paramObject);
/*     */ 
/*     */   public abstract Object getAttribute(String paramString);
/*     */ 
/*     */   public abstract void setErrorListener(ErrorListener paramErrorListener);
/*     */ 
/*     */   public abstract ErrorListener getErrorListener();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.TransformerFactory
 * JD-Core Version:    0.6.2
 */