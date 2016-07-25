/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class ErrorHandlerAdaptor
/*     */   implements XMLErrorHandler
/*     */ {
/*  78 */   private boolean hadError = false;
/*     */ 
/*     */   public boolean hadError()
/*     */   {
/*  84 */     return this.hadError;
/*     */   }
/*  86 */   public void reset() { this.hadError = false; }
/*     */ 
/*     */ 
/*     */   protected abstract ErrorHandler getErrorHandler();
/*     */ 
/*     */   public void fatalError(String domain, String key, XMLParseException e)
/*     */   {
/*     */     try
/*     */     {
/*  98 */       this.hadError = true;
/*  99 */       getErrorHandler().fatalError(Util.toSAXParseException(e));
/*     */     } catch (SAXException se) {
/* 101 */       throw new WrappedSAXException(se);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void error(String domain, String key, XMLParseException e) {
/*     */     try {
/* 107 */       this.hadError = true;
/* 108 */       getErrorHandler().error(Util.toSAXParseException(e));
/*     */     } catch (SAXException se) {
/* 110 */       throw new WrappedSAXException(se);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void warning(String domain, String key, XMLParseException e) {
/*     */     try {
/* 116 */       getErrorHandler().warning(Util.toSAXParseException(e));
/*     */     } catch (SAXException se) {
/* 118 */       throw new WrappedSAXException(se);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.ErrorHandlerAdaptor
 * JD-Core Version:    0.6.2
 */