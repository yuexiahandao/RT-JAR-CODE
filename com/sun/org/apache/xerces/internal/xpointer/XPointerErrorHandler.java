/*     */ package com.sun.org.apache.xerces.internal.xpointer;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ class XPointerErrorHandler
/*     */   implements XMLErrorHandler
/*     */ {
/*     */   protected PrintWriter fOut;
/*     */ 
/*     */   public XPointerErrorHandler()
/*     */   {
/*  52 */     this(new PrintWriter(System.err));
/*     */   }
/*     */ 
/*     */   public XPointerErrorHandler(PrintWriter out)
/*     */   {
/*  60 */     this.fOut = out;
/*     */   }
/*     */ 
/*     */   public void warning(String domain, String key, XMLParseException ex)
/*     */     throws XNIException
/*     */   {
/*  70 */     printError("Warning", ex);
/*     */   }
/*     */ 
/*     */   public void error(String domain, String key, XMLParseException ex)
/*     */     throws XNIException
/*     */   {
/*  76 */     printError("Error", ex);
/*     */   }
/*     */ 
/*     */   public void fatalError(String domain, String key, XMLParseException ex)
/*     */     throws XNIException
/*     */   {
/*  83 */     printError("Fatal Error", ex);
/*  84 */     throw ex;
/*     */   }
/*     */ 
/*     */   private void printError(String type, XMLParseException ex)
/*     */   {
/*  94 */     this.fOut.print("[");
/*  95 */     this.fOut.print(type);
/*  96 */     this.fOut.print("] ");
/*  97 */     String systemId = ex.getExpandedSystemId();
/*  98 */     if (systemId != null) {
/*  99 */       int index = systemId.lastIndexOf('/');
/* 100 */       if (index != -1)
/* 101 */         systemId = systemId.substring(index + 1);
/* 102 */       this.fOut.print(systemId);
/*     */     }
/* 104 */     this.fOut.print(':');
/* 105 */     this.fOut.print(ex.getLineNumber());
/* 106 */     this.fOut.print(':');
/* 107 */     this.fOut.print(ex.getColumnNumber());
/* 108 */     this.fOut.print(": ");
/* 109 */     this.fOut.print(ex.getMessage());
/* 110 */     this.fOut.println();
/* 111 */     this.fOut.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xpointer.XPointerErrorHandler
 * JD-Core Version:    0.6.2
 */