/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class LocatorWrapper
/*     */   implements XMLLocator
/*     */ {
/*     */   private final Locator locator;
/*     */ 
/*     */   public LocatorWrapper(Locator _loc)
/*     */   {
/*  76 */     this.locator = _loc;
/*     */   }
/*  78 */   public int getColumnNumber() { return this.locator.getColumnNumber(); } 
/*  79 */   public int getLineNumber() { return this.locator.getLineNumber(); } 
/*  80 */   public String getBaseSystemId() { return null; } 
/*  81 */   public String getExpandedSystemId() { return this.locator.getSystemId(); } 
/*  82 */   public String getLiteralSystemId() { return this.locator.getSystemId(); } 
/*  83 */   public String getPublicId() { return this.locator.getPublicId(); } 
/*  84 */   public String getEncoding() { return null; }
/*     */ 
/*     */ 
/*     */   public int getCharacterOffset()
/*     */   {
/*  96 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getXMLVersion()
/*     */   {
/* 107 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.LocatorWrapper
 * JD-Core Version:    0.6.2
 */