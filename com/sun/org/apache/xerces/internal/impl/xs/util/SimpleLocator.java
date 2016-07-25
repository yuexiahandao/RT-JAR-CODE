/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ 
/*     */ public class SimpleLocator
/*     */   implements XMLLocator
/*     */ {
/*     */   String lsid;
/*     */   String esid;
/*     */   int line;
/*     */   int column;
/*     */   int charOffset;
/*     */ 
/*     */   public SimpleLocator()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SimpleLocator(String lsid, String esid, int line, int column)
/*     */   {
/*  42 */     this(lsid, esid, line, column, -1);
/*     */   }
/*     */ 
/*     */   public void setValues(String lsid, String esid, int line, int column) {
/*  46 */     setValues(lsid, esid, line, column, -1);
/*     */   }
/*     */ 
/*     */   public SimpleLocator(String lsid, String esid, int line, int column, int offset) {
/*  50 */     this.line = line;
/*  51 */     this.column = column;
/*  52 */     this.lsid = lsid;
/*  53 */     this.esid = esid;
/*  54 */     this.charOffset = offset;
/*     */   }
/*     */ 
/*     */   public void setValues(String lsid, String esid, int line, int column, int offset) {
/*  58 */     this.line = line;
/*  59 */     this.column = column;
/*  60 */     this.lsid = lsid;
/*  61 */     this.esid = esid;
/*  62 */     this.charOffset = offset;
/*     */   }
/*     */ 
/*     */   public int getLineNumber() {
/*  66 */     return this.line;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber() {
/*  70 */     return this.column;
/*     */   }
/*     */ 
/*     */   public int getCharacterOffset() {
/*  74 */     return this.charOffset;
/*     */   }
/*     */ 
/*     */   public String getPublicId() {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   public String getExpandedSystemId() {
/*  82 */     return this.esid;
/*     */   }
/*     */ 
/*     */   public String getLiteralSystemId() {
/*  86 */     return this.lsid;
/*     */   }
/*     */ 
/*     */   public String getBaseSystemId() {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public void setColumnNumber(int col)
/*     */   {
/*  96 */     this.column = col;
/*     */   }
/*     */ 
/*     */   public void setLineNumber(int line)
/*     */   {
/* 103 */     this.line = line;
/*     */   }
/*     */ 
/*     */   public void setCharacterOffset(int offset) {
/* 107 */     this.charOffset = offset;
/*     */   }
/*     */ 
/*     */   public void setBaseSystemId(String systemId)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setExpandedSystemId(String systemId)
/*     */   {
/* 119 */     this.esid = systemId;
/*     */   }
/*     */ 
/*     */   public void setLiteralSystemId(String systemId)
/*     */   {
/* 126 */     this.lsid = systemId;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   public String getXMLVersion() {
/* 145 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator
 * JD-Core Version:    0.6.2
 */