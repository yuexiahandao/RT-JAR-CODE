/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.DOMLocator;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DOMLocatorImpl
/*     */   implements DOMLocator
/*     */ {
/*  47 */   public int fColumnNumber = -1;
/*     */ 
/*  53 */   public int fLineNumber = -1;
/*     */ 
/*  56 */   public Node fRelatedNode = null;
/*     */ 
/*  62 */   public String fUri = null;
/*     */ 
/*  68 */   public int fByteOffset = -1;
/*     */ 
/*  75 */   public int fUtf16Offset = -1;
/*     */ 
/*     */   public DOMLocatorImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DOMLocatorImpl(int lineNumber, int columnNumber, String uri)
/*     */   {
/*  85 */     this.fLineNumber = lineNumber;
/*  86 */     this.fColumnNumber = columnNumber;
/*  87 */     this.fUri = uri;
/*     */   }
/*     */ 
/*     */   public DOMLocatorImpl(int lineNumber, int columnNumber, int utf16Offset, String uri) {
/*  91 */     this.fLineNumber = lineNumber;
/*  92 */     this.fColumnNumber = columnNumber;
/*  93 */     this.fUri = uri;
/*  94 */     this.fUtf16Offset = utf16Offset;
/*     */   }
/*     */ 
/*     */   public DOMLocatorImpl(int lineNumber, int columnNumber, int byteoffset, Node relatedData, String uri) {
/*  98 */     this.fLineNumber = lineNumber;
/*  99 */     this.fColumnNumber = columnNumber;
/* 100 */     this.fByteOffset = byteoffset;
/* 101 */     this.fRelatedNode = relatedData;
/* 102 */     this.fUri = uri;
/*     */   }
/*     */ 
/*     */   public DOMLocatorImpl(int lineNumber, int columnNumber, int byteoffset, Node relatedData, String uri, int utf16Offset) {
/* 106 */     this.fLineNumber = lineNumber;
/* 107 */     this.fColumnNumber = columnNumber;
/* 108 */     this.fByteOffset = byteoffset;
/* 109 */     this.fRelatedNode = relatedData;
/* 110 */     this.fUri = uri;
/* 111 */     this.fUtf16Offset = utf16Offset;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 120 */     return this.fLineNumber;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 128 */     return this.fColumnNumber;
/*     */   }
/*     */ 
/*     */   public String getUri()
/*     */   {
/* 136 */     return this.fUri;
/*     */   }
/*     */ 
/*     */   public Node getRelatedNode()
/*     */   {
/* 141 */     return this.fRelatedNode;
/*     */   }
/*     */ 
/*     */   public int getByteOffset()
/*     */   {
/* 150 */     return this.fByteOffset;
/*     */   }
/*     */ 
/*     */   public int getUtf16Offset()
/*     */   {
/* 159 */     return this.fUtf16Offset;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMLocatorImpl
 * JD-Core Version:    0.6.2
 */