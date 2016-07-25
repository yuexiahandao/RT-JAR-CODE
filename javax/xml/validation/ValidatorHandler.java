/*     */ package javax.xml.validation;
/*     */ 
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public abstract class ValidatorHandler
/*     */   implements ContentHandler
/*     */ {
/*     */   public abstract void setContentHandler(ContentHandler paramContentHandler);
/*     */ 
/*     */   public abstract ContentHandler getContentHandler();
/*     */ 
/*     */   public abstract void setErrorHandler(ErrorHandler paramErrorHandler);
/*     */ 
/*     */   public abstract ErrorHandler getErrorHandler();
/*     */ 
/*     */   public abstract void setResourceResolver(LSResourceResolver paramLSResourceResolver);
/*     */ 
/*     */   public abstract LSResourceResolver getResourceResolver();
/*     */ 
/*     */   public abstract TypeInfoProvider getTypeInfoProvider();
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 358 */     if (name == null) {
/* 359 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 362 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 408 */     if (name == null) {
/* 409 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 412 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object object)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 441 */     if (name == null) {
/* 442 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 445 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 479 */     if (name == null) {
/* 480 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 483 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.validation.ValidatorHandler
 * JD-Core Version:    0.6.2
 */