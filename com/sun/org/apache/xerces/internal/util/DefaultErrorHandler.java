/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class DefaultErrorHandler
/*     */   implements XMLErrorHandler
/*     */ {
/*     */   protected PrintWriter fOut;
/*     */ 
/*     */   public DefaultErrorHandler()
/*     */   {
/*  54 */     this(new PrintWriter(System.err));
/*     */   }
/*     */ 
/*     */   public DefaultErrorHandler(PrintWriter out)
/*     */   {
/*  62 */     this.fOut = out;
/*     */   }
/*     */ 
/*     */   public void warning(String domain, String key, XMLParseException ex)
/*     */     throws XNIException
/*     */   {
/*  72 */     printError("Warning", ex);
/*     */   }
/*     */ 
/*     */   public void error(String domain, String key, XMLParseException ex)
/*     */     throws XNIException
/*     */   {
/*  78 */     printError("Error", ex);
/*     */   }
/*     */ 
/*     */   public void fatalError(String domain, String key, XMLParseException ex)
/*     */     throws XNIException
/*     */   {
/*  84 */     printError("Fatal Error", ex);
/*  85 */     throw ex;
/*     */   }
/*     */ 
/*     */   private void printError(String type, XMLParseException ex)
/*     */   {
/*  95 */     this.fOut.print("[");
/*  96 */     this.fOut.print(type);
/*  97 */     this.fOut.print("] ");
/*  98 */     String systemId = ex.getExpandedSystemId();
/*  99 */     if (systemId != null) {
/* 100 */       int index = systemId.lastIndexOf('/');
/* 101 */       if (index != -1)
/* 102 */         systemId = systemId.substring(index + 1);
/* 103 */       this.fOut.print(systemId);
/*     */     }
/* 105 */     this.fOut.print(':');
/* 106 */     this.fOut.print(ex.getLineNumber());
/* 107 */     this.fOut.print(':');
/* 108 */     this.fOut.print(ex.getColumnNumber());
/* 109 */     this.fOut.print(": ");
/* 110 */     this.fOut.print(ex.getMessage());
/* 111 */     this.fOut.println();
/* 112 */     this.fOut.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.DefaultErrorHandler
 * JD-Core Version:    0.6.2
 */