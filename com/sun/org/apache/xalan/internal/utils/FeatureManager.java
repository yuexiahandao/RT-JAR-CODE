/*     */ package com.sun.org.apache.xalan.internal.utils;
/*     */ 
/*     */ public final class FeatureManager extends FeaturePropertyBase
/*     */ {
/*     */   public FeatureManager()
/*     */   {
/*  75 */     this.values = new String[Feature.values().length];
/*  76 */     for (Feature feature : Feature.values()) {
/*  77 */       this.values[feature.ordinal()] = feature.defaultValue();
/*     */     }
/*     */ 
/*  80 */     readSystemProperties();
/*     */   }
/*     */ 
/*     */   public boolean isFeatureEnabled(Feature feature)
/*     */   {
/*  90 */     return Boolean.parseBoolean(this.values[feature.ordinal()]);
/*     */   }
/*     */ 
/*     */   public boolean isFeatureEnabled(String propertyName)
/*     */   {
/*  99 */     return Boolean.parseBoolean(this.values[getIndex(propertyName)]);
/*     */   }
/*     */ 
/*     */   public int getIndex(String propertyName)
/*     */   {
/* 108 */     for (Feature feature : Feature.values()) {
/* 109 */       if (feature.equalsName(propertyName)) {
/* 110 */         return feature.ordinal();
/*     */       }
/*     */     }
/* 113 */     return -1;
/*     */   }
/*     */ 
/*     */   private void readSystemProperties()
/*     */   {
/* 120 */     getSystemProperty(Feature.ORACLE_ENABLE_EXTENSION_FUNCTION, "javax.xml.enableExtensionFunctions");
/*     */   }
/*     */ 
/*     */   public static enum Feature
/*     */   {
/*  51 */     ORACLE_ENABLE_EXTENSION_FUNCTION("http://www.oracle.com/xml/jaxp/properties/enableExtensionFunctions", "true");
/*     */ 
/*     */     final String name;
/*     */     final String defaultValue;
/*     */ 
/*     */     private Feature(String name, String value) {
/*  58 */       this.name = name;
/*  59 */       this.defaultValue = value;
/*     */     }
/*     */ 
/*     */     public boolean equalsName(String propertyName) {
/*  63 */       return propertyName == null ? false : this.name.equals(propertyName);
/*     */     }
/*     */ 
/*     */     String defaultValue() {
/*  67 */       return this.defaultValue;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum State
/*     */   {
/*  44 */     DEFAULT, FSP, JAXPDOTPROPERTIES, SYSTEMPROPERTY, APIPROPERTY;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.utils.FeatureManager
 * JD-Core Version:    0.6.2
 */