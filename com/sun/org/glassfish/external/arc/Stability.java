/*     */ package com.sun.org.glassfish.external.arc;
/*     */ 
/*     */ public enum Stability
/*     */ {
/* 108 */   COMMITTED("Committed"), 
/*     */ 
/* 179 */   UNCOMMITTED("Uncommitted"), 
/*     */ 
/* 277 */   VOLATILE("Volatile"), 
/*     */ 
/* 304 */   NOT_AN_INTERFACE("Not-An-Interface"), 
/*     */ 
/* 311 */   PRIVATE("Private"), 
/*     */ 
/* 318 */   EXPERIMENTAL("Experimental"), 
/*     */ 
/* 323 */   UNSPECIFIED("Unspecified");
/*     */ 
/*     */   private final String mName;
/*     */ 
/* 326 */   private Stability(String name) { this.mName = name; } 
/*     */   public String toString() {
/* 328 */     return this.mName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.arc.Stability
 * JD-Core Version:    0.6.2
 */