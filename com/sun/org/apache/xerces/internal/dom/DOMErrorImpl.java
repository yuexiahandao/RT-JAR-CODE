/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import org.w3c.dom.DOMError;
/*     */ import org.w3c.dom.DOMLocator;
/*     */ 
/*     */ public class DOMErrorImpl
/*     */   implements DOMError
/*     */ {
/*  54 */   public short fSeverity = 1;
/*  55 */   public String fMessage = null;
/*  56 */   public DOMLocatorImpl fLocator = new DOMLocatorImpl();
/*  57 */   public Exception fException = null;
/*     */   public String fType;
/*     */   public Object fRelatedData;
/*     */ 
/*     */   public DOMErrorImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DOMErrorImpl(short severity, XMLParseException exception)
/*     */   {
/*  73 */     this.fSeverity = severity;
/*  74 */     this.fException = exception;
/*  75 */     this.fLocator = createDOMLocator(exception);
/*     */   }
/*     */ 
/*     */   public short getSeverity()
/*     */   {
/*  84 */     return this.fSeverity;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  92 */     return this.fMessage;
/*     */   }
/*     */ 
/*     */   public DOMLocator getLocation()
/*     */   {
/* 100 */     return this.fLocator;
/*     */   }
/*     */ 
/*     */   private DOMLocatorImpl createDOMLocator(XMLParseException exception)
/*     */   {
/* 106 */     return new DOMLocatorImpl(exception.getLineNumber(), exception.getColumnNumber(), exception.getCharacterOffset(), exception.getExpandedSystemId());
/*     */   }
/*     */ 
/*     */   public Object getRelatedException()
/*     */   {
/* 119 */     return this.fException;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 123 */     this.fSeverity = 1;
/* 124 */     this.fException = null;
/*     */   }
/*     */ 
/*     */   public String getType() {
/* 128 */     return this.fType;
/*     */   }
/*     */ 
/*     */   public Object getRelatedData() {
/* 132 */     return this.fRelatedData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMErrorImpl
 * JD-Core Version:    0.6.2
 */