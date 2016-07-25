/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import javax.xml.stream.Location;
/*     */ 
/*     */ public final class StAXLocationWrapper
/*     */   implements XMLLocator
/*     */ {
/*  39 */   private Location fLocation = null;
/*     */ 
/*     */   public void setLocation(Location location)
/*     */   {
/*  44 */     this.fLocation = location;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/*  48 */     return this.fLocation;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/*  56 */     if (this.fLocation != null) {
/*  57 */       return this.fLocation.getPublicId();
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */   public String getLiteralSystemId() {
/*  63 */     if (this.fLocation != null) {
/*  64 */       return this.fLocation.getSystemId();
/*     */     }
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   public String getBaseSystemId() {
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   public String getExpandedSystemId() {
/*  74 */     return getLiteralSystemId();
/*     */   }
/*     */ 
/*     */   public int getLineNumber() {
/*  78 */     if (this.fLocation != null) {
/*  79 */       return this.fLocation.getLineNumber();
/*     */     }
/*  81 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber() {
/*  85 */     if (this.fLocation != null) {
/*  86 */       return this.fLocation.getColumnNumber();
/*     */     }
/*  88 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getCharacterOffset() {
/*  92 */     if (this.fLocation != null) {
/*  93 */       return this.fLocation.getCharacterOffset();
/*     */     }
/*  95 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public String getXMLVersion() {
/* 103 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.StAXLocationWrapper
 * JD-Core Version:    0.6.2
 */