/*     */ package javax.xml.validation;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public abstract class SchemaFactory
/*     */ {
/* 113 */   private static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*     */   public static final SchemaFactory newInstance(String schemaLanguage)
/*     */   {
/* 194 */     ClassLoader cl = ss.getContextClassLoader();
/*     */ 
/* 196 */     if (cl == null)
/*     */     {
/* 199 */       cl = SchemaFactory.class.getClassLoader();
/*     */     }
/*     */ 
/* 202 */     SchemaFactory f = new SchemaFactoryFinder(cl).newFactory(schemaLanguage);
/* 203 */     if (f == null) {
/* 204 */       throw new IllegalArgumentException("No SchemaFactory that implements the schema language specified by: " + schemaLanguage + " could be loaded");
/*     */     }
/*     */ 
/* 209 */     return f;
/*     */   }
/*     */ 
/*     */   public static SchemaFactory newInstance(String schemaLanguage, String factoryClassName, ClassLoader classLoader)
/*     */   {
/* 255 */     ClassLoader cl = classLoader;
/*     */ 
/* 257 */     if (cl == null) {
/* 258 */       cl = ss.getContextClassLoader();
/*     */     }
/*     */ 
/* 261 */     SchemaFactory f = new SchemaFactoryFinder(cl).createInstance(factoryClassName);
/* 262 */     if (f == null) {
/* 263 */       throw new IllegalArgumentException("Factory " + factoryClassName + " could not be loaded to implement the schema language specified by: " + schemaLanguage);
/*     */     }
/*     */ 
/* 268 */     if (f.isSchemaLanguageSupported(schemaLanguage)) {
/* 269 */       return f;
/*     */     }
/* 271 */     throw new IllegalArgumentException("Factory " + f.getClass().getName() + " does not implement the schema language specified by: " + schemaLanguage);
/*     */   }
/*     */ 
/*     */   public abstract boolean isSchemaLanguageSupported(String paramString);
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 318 */     if (name == null) {
/* 319 */       throw new NullPointerException("the name parameter is null");
/*     */     }
/* 321 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 376 */     if (name == null) {
/* 377 */       throw new NullPointerException("the name parameter is null");
/*     */     }
/* 379 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object object)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 441 */     if (name == null) {
/* 442 */       throw new NullPointerException("the name parameter is null");
/*     */     }
/* 444 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 476 */     if (name == null) {
/* 477 */       throw new NullPointerException("the name parameter is null");
/*     */     }
/* 479 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */   public abstract void setErrorHandler(ErrorHandler paramErrorHandler);
/*     */ 
/*     */   public abstract ErrorHandler getErrorHandler();
/*     */ 
/*     */   public abstract void setResourceResolver(LSResourceResolver paramLSResourceResolver);
/*     */ 
/*     */   public abstract LSResourceResolver getResourceResolver();
/*     */ 
/*     */   public Schema newSchema(Source schema)
/*     */     throws SAXException
/*     */   {
/* 627 */     return newSchema(new Source[] { schema });
/*     */   }
/*     */ 
/*     */   public Schema newSchema(File schema)
/*     */     throws SAXException
/*     */   {
/* 643 */     return newSchema(new StreamSource(schema));
/*     */   }
/*     */ 
/*     */   public Schema newSchema(URL schema)
/*     */     throws SAXException
/*     */   {
/* 659 */     return newSchema(new StreamSource(schema.toExternalForm()));
/*     */   }
/*     */ 
/*     */   public abstract Schema newSchema(Source[] paramArrayOfSource)
/*     */     throws SAXException;
/*     */ 
/*     */   public abstract Schema newSchema()
/*     */     throws SAXException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.validation.SchemaFactory
 * JD-Core Version:    0.6.2
 */