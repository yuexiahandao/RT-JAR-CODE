/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ParserConfigurationSettings
/*     */   implements XMLComponentManager
/*     */ {
/*     */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*     */   protected Set<String> fRecognizedProperties;
/*     */   protected Map<String, Object> fProperties;
/*     */   protected Set<String> fRecognizedFeatures;
/*     */   protected Map<String, Boolean> fFeatures;
/*     */   protected XMLComponentManager fParentSettings;
/*     */ 
/*     */   public ParserConfigurationSettings()
/*     */   {
/*  81 */     this(null);
/*     */   }
/*     */ 
/*     */   public ParserConfigurationSettings(XMLComponentManager parent)
/*     */   {
/*  91 */     this.fRecognizedFeatures = new HashSet();
/*  92 */     this.fRecognizedProperties = new HashSet();
/*     */ 
/*  95 */     this.fFeatures = new HashMap();
/*  96 */     this.fProperties = new HashMap();
/*     */ 
/*  99 */     this.fParentSettings = parent;
/*     */   }
/*     */ 
/*     */   public void addRecognizedFeatures(String[] featureIds)
/*     */   {
/* 117 */     int featureIdsCount = featureIds != null ? featureIds.length : 0;
/* 118 */     for (int i = 0; i < featureIdsCount; i++) {
/* 119 */       String featureId = featureIds[i];
/* 120 */       if (!this.fRecognizedFeatures.contains(featureId))
/* 121 */         this.fRecognizedFeatures.add(featureId);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws XMLConfigurationException
/*     */   {
/* 144 */     FeatureState checkState = checkFeature(featureId);
/* 145 */     if (checkState.isExceptional()) {
/* 146 */       throw new XMLConfigurationException(checkState.status, featureId);
/*     */     }
/*     */ 
/* 149 */     this.fFeatures.put(featureId, Boolean.valueOf(state));
/*     */   }
/*     */ 
/*     */   public void addRecognizedProperties(String[] propertyIds)
/*     */   {
/* 160 */     this.fRecognizedProperties.addAll(Arrays.asList(propertyIds));
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 175 */     PropertyState checkState = checkProperty(propertyId);
/* 176 */     if (checkState.isExceptional()) {
/* 177 */       throw new XMLConfigurationException(checkState.status, propertyId);
/*     */     }
/* 179 */     this.fProperties.put(propertyId, value);
/*     */   }
/*     */ 
/*     */   public final boolean getFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 202 */     FeatureState state = getFeatureState(featureId);
/* 203 */     if (state.isExceptional()) {
/* 204 */       throw new XMLConfigurationException(state.status, featureId);
/*     */     }
/* 206 */     return state.state;
/*     */   }
/*     */ 
/*     */   public final boolean getFeature(String featureId, boolean defaultValue) {
/* 210 */     FeatureState state = getFeatureState(featureId);
/* 211 */     if (state.isExceptional()) {
/* 212 */       return defaultValue;
/*     */     }
/* 214 */     return state.state;
/*     */   }
/*     */ 
/*     */   public FeatureState getFeatureState(String featureId) {
/* 218 */     Boolean state = (Boolean)this.fFeatures.get(featureId);
/*     */ 
/* 220 */     if (state == null) {
/* 221 */       FeatureState checkState = checkFeature(featureId);
/* 222 */       if (checkState.isExceptional()) {
/* 223 */         return checkState;
/*     */       }
/* 225 */       return FeatureState.is(false);
/*     */     }
/* 227 */     return FeatureState.is(state.booleanValue());
/*     */   }
/*     */ 
/*     */   public final Object getProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 245 */     PropertyState state = getPropertyState(propertyId);
/* 246 */     if (state.isExceptional()) {
/* 247 */       throw new XMLConfigurationException(state.status, propertyId);
/*     */     }
/*     */ 
/* 250 */     return state.state;
/*     */   }
/*     */ 
/*     */   public final Object getProperty(String propertyId, Object defaultValue) {
/* 254 */     PropertyState state = getPropertyState(propertyId);
/* 255 */     if (state.isExceptional()) {
/* 256 */       return defaultValue;
/*     */     }
/*     */ 
/* 259 */     return state.state;
/*     */   }
/*     */ 
/*     */   public PropertyState getPropertyState(String propertyId) {
/* 263 */     Object propertyValue = this.fProperties.get(propertyId);
/*     */ 
/* 265 */     if (propertyValue == null) {
/* 266 */       PropertyState state = checkProperty(propertyId);
/* 267 */       if (state.isExceptional()) {
/* 268 */         return state;
/*     */       }
/*     */     }
/*     */ 
/* 272 */     return PropertyState.is(propertyValue);
/*     */   }
/*     */ 
/*     */   protected FeatureState checkFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 292 */     if (!this.fRecognizedFeatures.contains(featureId)) {
/* 293 */       if (this.fParentSettings != null) {
/* 294 */         return this.fParentSettings.getFeatureState(featureId);
/*     */       }
/*     */ 
/* 297 */       return FeatureState.NOT_RECOGNIZED;
/*     */     }
/*     */ 
/* 302 */     return FeatureState.RECOGNIZED;
/*     */   }
/*     */ 
/*     */   protected PropertyState checkProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 318 */     if (!this.fRecognizedProperties.contains(propertyId)) {
/* 319 */       if (this.fParentSettings != null) {
/* 320 */         PropertyState state = this.fParentSettings.getPropertyState(propertyId);
/* 321 */         if (state.isExceptional())
/* 322 */           return state;
/*     */       }
/*     */       else
/*     */       {
/* 326 */         return PropertyState.NOT_RECOGNIZED;
/*     */       }
/*     */     }
/* 329 */     return PropertyState.RECOGNIZED;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings
 * JD-Core Version:    0.6.2
 */