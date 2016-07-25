/*     */ package javax.xml.parsers;
/*     */ 
/*     */ import javax.xml.validation.Schema;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public abstract class SAXParserFactory
/*     */ {
/*     */   private static final String DEFAULT_PROPERTY_NAME = "javax.xml.parsers.SAXParserFactory";
/*  51 */   private boolean validating = false;
/*     */ 
/*  56 */   private boolean namespaceAware = false;
/*     */ 
/*     */   public static SAXParserFactory newInstance()
/*     */   {
/*     */     try
/*     */     {
/* 126 */       return (SAXParserFactory)FactoryFinder.find("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
/*     */     }
/*     */     catch (FactoryFinder.ConfigurationError e)
/*     */     {
/* 132 */       throw new FactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static SAXParserFactory newInstance(String factoryClassName, ClassLoader classLoader)
/*     */   {
/*     */     try
/*     */     {
/* 174 */       return (SAXParserFactory)FactoryFinder.newInstance(factoryClassName, classLoader, false);
/*     */     } catch (FactoryFinder.ConfigurationError e) {
/* 176 */       throw new FactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract SAXParser newSAXParser()
/*     */     throws ParserConfigurationException, SAXException;
/*     */ 
/*     */   public void setNamespaceAware(boolean awareness)
/*     */   {
/* 206 */     this.namespaceAware = awareness;
/*     */   }
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/* 235 */     this.validating = validating;
/*     */   }
/*     */ 
/*     */   public boolean isNamespaceAware()
/*     */   {
/* 247 */     return this.namespaceAware;
/*     */   }
/*     */ 
/*     */   public boolean isValidating()
/*     */   {
/* 259 */     return this.validating;
/*     */   }
/*     */ 
/*     */   public abstract void setFeature(String paramString, boolean paramBoolean)
/*     */     throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;
/*     */ 
/*     */   public abstract boolean getFeature(String paramString)
/*     */     throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 351 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema schema)
/*     */   {
/* 419 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public void setXIncludeAware(boolean state)
/*     */   {
/* 446 */     if (state)
/* 447 */       throw new UnsupportedOperationException(" setXIncludeAware is not supported on this JAXP implementation or earlier: " + getClass());
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware()
/*     */   {
/* 464 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.parsers.SAXParserFactory
 * JD-Core Version:    0.6.2
 */