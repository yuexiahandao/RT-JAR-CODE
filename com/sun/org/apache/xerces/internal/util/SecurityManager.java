/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ public final class SecurityManager
/*     */ {
/*     */   private static final int DEFAULT_ENTITY_EXPANSION_LIMIT = 64000;
/*     */   private static final int DEFAULT_MAX_OCCUR_NODE_LIMIT = 5000;
/*     */   private static final int DEFAULT_ELEMENT_ATTRIBUTE_LIMIT = 10000;
/*     */   private int entityExpansionLimit;
/*     */   private int maxOccurLimit;
/*     */   private int fElementAttributeLimit;
/*     */ 
/*     */   public SecurityManager()
/*     */   {
/* 113 */     this.entityExpansionLimit = 64000;
/* 114 */     this.maxOccurLimit = 5000;
/* 115 */     this.fElementAttributeLimit = 10000;
/*     */ 
/* 118 */     readSystemProperties();
/*     */   }
/*     */ 
/*     */   public void setEntityExpansionLimit(int limit)
/*     */   {
/* 129 */     this.entityExpansionLimit = limit;
/*     */   }
/*     */ 
/*     */   public int getEntityExpansionLimit()
/*     */   {
/* 140 */     return this.entityExpansionLimit;
/*     */   }
/*     */ 
/*     */   public void setMaxOccurNodeLimit(int limit)
/*     */   {
/* 153 */     this.maxOccurLimit = limit;
/*     */   }
/*     */ 
/*     */   public int getMaxOccurNodeLimit()
/*     */   {
/* 166 */     return this.maxOccurLimit;
/*     */   }
/*     */ 
/*     */   public int getElementAttrLimit() {
/* 170 */     return this.fElementAttributeLimit;
/*     */   }
/*     */ 
/*     */   public void setElementAttrLimit(int limit) {
/* 174 */     this.fElementAttributeLimit = limit;
/*     */   }
/*     */ 
/*     */   private void readSystemProperties()
/*     */   {
/*     */     try {
/* 180 */       String value = System.getProperty("entityExpansionLimit");
/* 181 */       if ((value != null) && (!value.equals(""))) {
/* 182 */         this.entityExpansionLimit = Integer.parseInt(value);
/* 183 */         if (this.entityExpansionLimit < 0)
/* 184 */           this.entityExpansionLimit = 64000;
/*     */       }
/*     */       else {
/* 187 */         this.entityExpansionLimit = 64000;
/*     */       }
/*     */     } catch (Exception ex) {
/*     */     }
/*     */     try { String value = System.getProperty("maxOccurLimit");
/* 192 */       if ((value != null) && (!value.equals(""))) {
/* 193 */         this.maxOccurLimit = Integer.parseInt(value);
/* 194 */         if (this.maxOccurLimit < 0)
/* 195 */           this.maxOccurLimit = 5000;
/*     */       }
/*     */       else {
/* 198 */         this.maxOccurLimit = 5000;
/*     */       } } catch (Exception ex) {
/*     */     }
/*     */     try {
/* 202 */       String value = System.getProperty("elementAttributeLimit");
/* 203 */       if ((value != null) && (!value.equals(""))) {
/* 204 */         this.fElementAttributeLimit = Integer.parseInt(value);
/* 205 */         if (this.fElementAttributeLimit < 0)
/* 206 */           this.fElementAttributeLimit = 10000;
/*     */       }
/*     */       else {
/* 209 */         this.fElementAttributeLimit = 10000;
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.SecurityManager
 * JD-Core Version:    0.6.2
 */