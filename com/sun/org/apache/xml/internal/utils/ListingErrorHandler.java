/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class ListingErrorHandler
/*     */   implements ErrorHandler, ErrorListener
/*     */ {
/*  57 */   protected PrintWriter m_pw = null;
/*     */ 
/* 500 */   protected boolean throwOnWarning = false;
/*     */ 
/* 532 */   protected boolean throwOnError = true;
/*     */ 
/* 565 */   protected boolean throwOnFatalError = true;
/*     */ 
/*     */   public ListingErrorHandler(PrintWriter pw)
/*     */   {
/*  65 */     if (null == pw) {
/*  66 */       throw new NullPointerException(XMLMessages.createXMLMessage("ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", null));
/*     */     }
/*     */ 
/*  69 */     this.m_pw = pw;
/*     */   }
/*     */ 
/*     */   public ListingErrorHandler()
/*     */   {
/*  77 */     this.m_pw = new PrintWriter(System.err, true);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 105 */     logExceptionLocation(this.m_pw, exception);
/*     */ 
/* 108 */     this.m_pw.println("warning: " + exception.getMessage());
/* 109 */     this.m_pw.flush();
/*     */ 
/* 111 */     if (getThrowOnWarning())
/* 112 */       throw exception;
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 144 */     logExceptionLocation(this.m_pw, exception);
/* 145 */     this.m_pw.println("error: " + exception.getMessage());
/* 146 */     this.m_pw.flush();
/*     */ 
/* 148 */     if (getThrowOnError())
/* 149 */       throw exception;
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 176 */     logExceptionLocation(this.m_pw, exception);
/* 177 */     this.m_pw.println("fatalError: " + exception.getMessage());
/* 178 */     this.m_pw.flush();
/*     */ 
/* 180 */     if (getThrowOnFatalError())
/* 181 */       throw exception;
/*     */   }
/*     */ 
/*     */   public void warning(TransformerException exception)
/*     */     throws TransformerException
/*     */   {
/* 209 */     logExceptionLocation(this.m_pw, exception);
/* 210 */     this.m_pw.println("warning: " + exception.getMessage());
/* 211 */     this.m_pw.flush();
/*     */ 
/* 213 */     if (getThrowOnWarning())
/* 214 */       throw exception;
/*     */   }
/*     */ 
/*     */   public void error(TransformerException exception)
/*     */     throws TransformerException
/*     */   {
/* 236 */     logExceptionLocation(this.m_pw, exception);
/* 237 */     this.m_pw.println("error: " + exception.getMessage());
/* 238 */     this.m_pw.flush();
/*     */ 
/* 240 */     if (getThrowOnError())
/* 241 */       throw exception;
/*     */   }
/*     */ 
/*     */   public void fatalError(TransformerException exception)
/*     */     throws TransformerException
/*     */   {
/* 264 */     logExceptionLocation(this.m_pw, exception);
/* 265 */     this.m_pw.println("error: " + exception.getMessage());
/* 266 */     this.m_pw.flush();
/*     */ 
/* 268 */     if (getThrowOnError())
/* 269 */       throw exception;
/*     */   }
/*     */ 
/*     */   public static void logExceptionLocation(PrintWriter pw, Throwable exception)
/*     */   {
/* 287 */     if (null == pw) {
/* 288 */       pw = new PrintWriter(System.err, true);
/*     */     }
/* 290 */     SourceLocator locator = null;
/* 291 */     Throwable cause = exception;
/*     */     do
/*     */     {
/* 297 */       if ((cause instanceof SAXParseException))
/*     */       {
/* 304 */         locator = new SAXSourceLocator((SAXParseException)cause);
/*     */       }
/* 306 */       else if ((cause instanceof TransformerException))
/*     */       {
/* 308 */         SourceLocator causeLocator = ((TransformerException)cause).getLocator();
/* 309 */         if (null != causeLocator)
/*     */         {
/* 311 */           locator = causeLocator;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 316 */       if ((cause instanceof TransformerException))
/* 317 */         cause = ((TransformerException)cause).getCause();
/* 318 */       else if ((cause instanceof WrappedRuntimeException))
/* 319 */         cause = ((WrappedRuntimeException)cause).getException();
/* 320 */       else if ((cause instanceof SAXException))
/* 321 */         cause = ((SAXException)cause).getException();
/*     */       else
/* 323 */         cause = null;
/*     */     }
/* 325 */     while (null != cause);
/*     */ 
/* 330 */     if (null != locator)
/*     */     {
/* 332 */       String id = null != locator.getSystemId() ? locator.getSystemId() : locator.getPublicId() != locator.getPublicId() ? locator.getPublicId() : "SystemId-Unknown";
/*     */ 
/* 337 */       pw.print(id + ":Line=" + locator.getLineNumber() + ";Column=" + locator.getColumnNumber() + ": ");
/*     */ 
/* 339 */       pw.println("exception:" + exception.getMessage());
/* 340 */       pw.println("root-cause:" + (null != cause ? cause.getMessage() : "null"));
/*     */ 
/* 342 */       logSourceLine(pw, locator);
/*     */     }
/*     */     else
/*     */     {
/* 346 */       pw.print("SystemId-Unknown:locator-unavailable: ");
/* 347 */       pw.println("exception:" + exception.getMessage());
/* 348 */       pw.println("root-cause:" + (null != cause ? cause.getMessage() : "null"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void logSourceLine(PrintWriter pw, SourceLocator locator)
/*     */   {
/* 364 */     if (null == locator) {
/* 365 */       return;
/*     */     }
/* 367 */     if (null == pw) {
/* 368 */       pw = new PrintWriter(System.err, true);
/*     */     }
/* 370 */     String url = locator.getSystemId();
/*     */ 
/* 374 */     if (null == url)
/*     */     {
/* 376 */       pw.println("line: (No systemId; cannot read file)");
/* 377 */       pw.println();
/* 378 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 385 */       int line = locator.getLineNumber();
/* 386 */       int column = locator.getColumnNumber();
/* 387 */       pw.println("line: " + getSourceLine(url, line));
/* 388 */       StringBuffer buf = new StringBuffer("line: ");
/* 389 */       for (int i = 1; i < column; i++)
/*     */       {
/* 391 */         buf.append(' ');
/*     */       }
/* 393 */       buf.append('^');
/* 394 */       pw.println(buf.toString());
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 398 */       pw.println("line: logSourceLine unavailable due to: " + e.getMessage());
/* 399 */       pw.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static String getSourceLine(String sourceUrl, int lineNum)
/*     */     throws Exception
/*     */   {
/* 413 */     URL url = null;
/*     */     try
/*     */     {
/* 418 */       url = new URL(sourceUrl);
/*     */     }
/*     */     catch (MalformedURLException mue)
/*     */     {
/* 422 */       int indexOfColon = sourceUrl.indexOf(':');
/* 423 */       int indexOfSlash = sourceUrl.indexOf('/');
/*     */ 
/* 425 */       if ((indexOfColon != -1) && (indexOfSlash != -1) && (indexOfColon < indexOfSlash))
/*     */       {
/* 431 */         throw mue;
/*     */       }
/*     */ 
/* 436 */       url = new URL(SystemIDResolver.getAbsoluteURI(sourceUrl));
/*     */     }
/*     */ 
/* 441 */     String line = null;
/* 442 */     InputStream is = null;
/* 443 */     BufferedReader br = null;
/*     */     try
/*     */     {
/* 447 */       URLConnection uc = url.openConnection();
/* 448 */       is = uc.getInputStream();
/* 449 */       br = new BufferedReader(new InputStreamReader(is));
/*     */ 
/* 453 */       for (int i = 1; i <= lineNum; i++)
/*     */       {
/* 455 */         line = br.readLine();
/*     */       }
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 463 */       br.close();
/* 464 */       is.close();
/*     */     }
/*     */ 
/* 468 */     return line;
/*     */   }
/*     */ 
/*     */   public void setThrowOnWarning(boolean b)
/*     */   {
/* 486 */     this.throwOnWarning = b;
/*     */   }
/*     */ 
/*     */   public boolean getThrowOnWarning()
/*     */   {
/* 496 */     return this.throwOnWarning;
/*     */   }
/*     */ 
/*     */   public void setThrowOnError(boolean b)
/*     */   {
/* 518 */     this.throwOnError = b;
/*     */   }
/*     */ 
/*     */   public boolean getThrowOnError()
/*     */   {
/* 528 */     return this.throwOnError;
/*     */   }
/*     */ 
/*     */   public void setThrowOnFatalError(boolean b)
/*     */   {
/* 551 */     this.throwOnFatalError = b;
/*     */   }
/*     */ 
/*     */   public boolean getThrowOnFatalError()
/*     */   {
/* 561 */     return this.throwOnFatalError;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.ListingErrorHandler
 * JD-Core Version:    0.6.2
 */