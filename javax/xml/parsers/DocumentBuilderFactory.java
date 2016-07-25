/*     */ package javax.xml.parsers;
/*     */ 
/*     */ import javax.xml.validation.Schema;
/*     */ 
/*     */ public abstract class DocumentBuilderFactory
/*     */ {
/*     */   private static final String DEFAULT_PROPERTY_NAME = "javax.xml.parsers.DocumentBuilderFactory";
/*  46 */   private boolean validating = false;
/*  47 */   private boolean namespaceAware = false;
/*  48 */   private boolean whitespace = false;
/*  49 */   private boolean expandEntityRef = true;
/*  50 */   private boolean ignoreComments = false;
/*  51 */   private boolean coalescing = false;
/*     */ 
/*  53 */   private boolean canonicalState = false;
/*     */ 
/*     */   public static DocumentBuilderFactory newInstance()
/*     */   {
/*     */     try
/*     */     {
/* 121 */       return (DocumentBuilderFactory)FactoryFinder.find("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
/*     */     }
/*     */     catch (FactoryFinder.ConfigurationError e)
/*     */     {
/* 127 */       throw new FactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static DocumentBuilderFactory newInstance(String factoryClassName, ClassLoader classLoader)
/*     */   {
/*     */     try
/*     */     {
/* 170 */       return (DocumentBuilderFactory)FactoryFinder.newInstance(factoryClassName, classLoader, false);
/*     */     } catch (FactoryFinder.ConfigurationError e) {
/* 172 */       throw new FactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract DocumentBuilder newDocumentBuilder()
/*     */     throws ParserConfigurationException;
/*     */ 
/*     */   public void setNamespaceAware(boolean awareness)
/*     */   {
/* 201 */     this.namespaceAware = awareness;
/*     */   }
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/* 230 */     this.validating = validating;
/*     */   }
/*     */ 
/*     */   public void setIgnoringElementContentWhitespace(boolean whitespace)
/*     */   {
/* 249 */     this.whitespace = whitespace;
/*     */   }
/*     */ 
/*     */   public void setExpandEntityReferences(boolean expandEntityRef)
/*     */   {
/* 262 */     this.expandEntityRef = expandEntityRef;
/*     */   }
/*     */ 
/*     */   public void setIgnoringComments(boolean ignoreComments)
/*     */   {
/* 274 */     this.ignoreComments = ignoreComments;
/*     */   }
/*     */ 
/*     */   public void setCoalescing(boolean coalescing)
/*     */   {
/* 289 */     this.coalescing = coalescing;
/*     */   }
/*     */ 
/*     */   public boolean isNamespaceAware()
/*     */   {
/* 301 */     return this.namespaceAware;
/*     */   }
/*     */ 
/*     */   public boolean isValidating()
/*     */   {
/* 313 */     return this.validating;
/*     */   }
/*     */ 
/*     */   public boolean isIgnoringElementContentWhitespace()
/*     */   {
/* 326 */     return this.whitespace;
/*     */   }
/*     */ 
/*     */   public boolean isExpandEntityReferences()
/*     */   {
/* 338 */     return this.expandEntityRef;
/*     */   }
/*     */ 
/*     */   public boolean isIgnoringComments()
/*     */   {
/* 350 */     return this.ignoreComments;
/*     */   }
/*     */ 
/*     */   public boolean isCoalescing()
/*     */   {
/* 364 */     return this.coalescing;
/*     */   }
/*     */ 
/*     */   public abstract void setAttribute(String paramString, Object paramObject)
/*     */     throws IllegalArgumentException;
/*     */ 
/*     */   public abstract Object getAttribute(String paramString)
/*     */     throws IllegalArgumentException;
/*     */ 
/*     */   public abstract void setFeature(String paramString, boolean paramBoolean)
/*     */     throws ParserConfigurationException;
/*     */ 
/*     */   public abstract boolean getFeature(String paramString)
/*     */     throws ParserConfigurationException;
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 506 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema schema)
/*     */   {
/* 584 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public void setXIncludeAware(boolean state)
/*     */   {
/* 613 */     if (state)
/* 614 */       throw new UnsupportedOperationException(" setXIncludeAware is not supported on this JAXP implementation or earlier: " + getClass());
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware()
/*     */   {
/* 631 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.parsers.DocumentBuilderFactory
 * JD-Core Version:    0.6.2
 */