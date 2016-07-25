/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ 
/*     */ public final class SAXLocatorWrapper
/*     */   implements XMLLocator
/*     */ {
/*  37 */   private Locator fLocator = null;
/*  38 */   private Locator2 fLocator2 = null;
/*     */ 
/*     */   public void setLocator(Locator locator)
/*     */   {
/*  43 */     this.fLocator = locator;
/*  44 */     if (((locator instanceof Locator2)) || (locator == null))
/*  45 */       this.fLocator2 = ((Locator2)locator);
/*     */   }
/*     */ 
/*     */   public Locator getLocator()
/*     */   {
/*  50 */     return this.fLocator;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/*  58 */     if (this.fLocator != null) {
/*  59 */       return this.fLocator.getPublicId();
/*     */     }
/*  61 */     return null;
/*     */   }
/*     */ 
/*     */   public String getLiteralSystemId() {
/*  65 */     if (this.fLocator != null) {
/*  66 */       return this.fLocator.getSystemId();
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   public String getBaseSystemId() {
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   public String getExpandedSystemId() {
/*  76 */     return getLiteralSystemId();
/*     */   }
/*     */ 
/*     */   public int getLineNumber() {
/*  80 */     if (this.fLocator != null) {
/*  81 */       return this.fLocator.getLineNumber();
/*     */     }
/*  83 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber() {
/*  87 */     if (this.fLocator != null) {
/*  88 */       return this.fLocator.getColumnNumber();
/*     */     }
/*  90 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getCharacterOffset() {
/*  94 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/*  98 */     if (this.fLocator2 != null) {
/*  99 */       return this.fLocator2.getEncoding();
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   public String getXMLVersion() {
/* 105 */     if (this.fLocator2 != null) {
/* 106 */       return this.fLocator2.getXMLVersion();
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.SAXLocatorWrapper
 * JD-Core Version:    0.6.2
 */