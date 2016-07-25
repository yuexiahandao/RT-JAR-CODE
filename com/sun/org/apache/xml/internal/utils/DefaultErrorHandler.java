/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class DefaultErrorHandler
/*     */   implements ErrorHandler, ErrorListener
/*     */ {
/*     */   PrintWriter m_pw;
/*  53 */   boolean m_throwExceptionOnError = true;
/*     */ 
/*     */   public DefaultErrorHandler(PrintWriter pw)
/*     */   {
/*  60 */     this.m_pw = pw;
/*     */   }
/*     */ 
/*     */   public DefaultErrorHandler(PrintStream pw)
/*     */   {
/*  68 */     this.m_pw = new PrintWriter(pw, true);
/*     */   }
/*     */ 
/*     */   public DefaultErrorHandler()
/*     */   {
/*  76 */     this(true);
/*     */   }
/*     */ 
/*     */   public DefaultErrorHandler(boolean throwExceptionOnError)
/*     */   {
/*  84 */     this.m_pw = new PrintWriter(System.err, true);
/*  85 */     this.m_throwExceptionOnError = throwExceptionOnError;
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 107 */     printLocation(this.m_pw, exception);
/* 108 */     this.m_pw.println("Parser warning: " + exception.getMessage());
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 137 */     throw exception;
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 164 */     throw exception;
/*     */   }
/*     */ 
/*     */   public void warning(TransformerException exception)
/*     */     throws TransformerException
/*     */   {
/* 186 */     printLocation(this.m_pw, exception);
/*     */ 
/* 188 */     this.m_pw.println(exception.getMessage());
/*     */   }
/*     */ 
/*     */   public void error(TransformerException exception)
/*     */     throws TransformerException
/*     */   {
/* 217 */     if (this.m_throwExceptionOnError) {
/* 218 */       throw exception;
/*     */     }
/*     */ 
/* 221 */     printLocation(this.m_pw, exception);
/* 222 */     this.m_pw.println(exception.getMessage());
/*     */   }
/*     */ 
/*     */   public void fatalError(TransformerException exception)
/*     */     throws TransformerException
/*     */   {
/* 250 */     if (this.m_throwExceptionOnError) {
/* 251 */       throw exception;
/*     */     }
/*     */ 
/* 254 */     printLocation(this.m_pw, exception);
/* 255 */     this.m_pw.println(exception.getMessage());
/*     */   }
/*     */ 
/*     */   public static void ensureLocationSet(TransformerException exception)
/*     */   {
/* 262 */     SourceLocator locator = null;
/* 263 */     Throwable cause = exception;
/*     */     do
/*     */     {
/* 268 */       if ((cause instanceof SAXParseException))
/*     */       {
/* 270 */         locator = new SAXSourceLocator((SAXParseException)cause);
/*     */       }
/* 272 */       else if ((cause instanceof TransformerException))
/*     */       {
/* 274 */         SourceLocator causeLocator = ((TransformerException)cause).getLocator();
/* 275 */         if (null != causeLocator) {
/* 276 */           locator = causeLocator;
/*     */         }
/*     */       }
/* 279 */       if ((cause instanceof TransformerException))
/* 280 */         cause = ((TransformerException)cause).getCause();
/* 281 */       else if ((cause instanceof SAXException))
/* 282 */         cause = ((SAXException)cause).getException();
/*     */       else
/* 284 */         cause = null;
/*     */     }
/* 286 */     while (null != cause);
/*     */ 
/* 288 */     exception.setLocator(locator);
/*     */   }
/*     */ 
/*     */   public static void printLocation(PrintStream pw, TransformerException exception)
/*     */   {
/* 293 */     printLocation(new PrintWriter(pw), exception);
/*     */   }
/*     */ 
/*     */   public static void printLocation(PrintStream pw, SAXParseException exception)
/*     */   {
/* 298 */     printLocation(new PrintWriter(pw), exception);
/*     */   }
/*     */ 
/*     */   public static void printLocation(PrintWriter pw, Throwable exception)
/*     */   {
/* 303 */     SourceLocator locator = null;
/* 304 */     Throwable cause = exception;
/*     */     do
/*     */     {
/* 309 */       if ((cause instanceof SAXParseException))
/*     */       {
/* 311 */         locator = new SAXSourceLocator((SAXParseException)cause);
/*     */       }
/* 313 */       else if ((cause instanceof TransformerException))
/*     */       {
/* 315 */         SourceLocator causeLocator = ((TransformerException)cause).getLocator();
/* 316 */         if (null != causeLocator)
/* 317 */           locator = causeLocator;
/*     */       }
/* 319 */       if ((cause instanceof TransformerException))
/* 320 */         cause = ((TransformerException)cause).getCause();
/* 321 */       else if ((cause instanceof WrappedRuntimeException))
/* 322 */         cause = ((WrappedRuntimeException)cause).getException();
/* 323 */       else if ((cause instanceof SAXException))
/* 324 */         cause = ((SAXException)cause).getException();
/*     */       else
/* 326 */         cause = null;
/*     */     }
/* 328 */     while (null != cause);
/*     */ 
/* 330 */     if (null != locator)
/*     */     {
/* 333 */       String id = null != locator.getSystemId() ? locator.getSystemId() : null != locator.getPublicId() ? locator.getPublicId() : XMLMessages.createXMLMessage("ER_SYSTEMID_UNKNOWN", null);
/*     */ 
/* 338 */       pw.print(id + "; " + XMLMessages.createXMLMessage("line", null) + locator.getLineNumber() + "; " + XMLMessages.createXMLMessage("column", null) + locator.getColumnNumber() + "; ");
/*     */     }
/*     */     else
/*     */     {
/* 342 */       pw.print("(" + XMLMessages.createXMLMessage("ER_LOCATION_UNKNOWN", null) + ")");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.DefaultErrorHandler
 * JD-Core Version:    0.6.2
 */