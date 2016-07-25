/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.Validator;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ 
/*     */ abstract class AbstractXMLSchema extends Schema
/*     */   implements XSGrammarPoolContainer
/*     */ {
/*     */   private final HashMap fFeatures;
/*     */   private final HashMap fProperties;
/*     */ 
/*     */   public AbstractXMLSchema()
/*     */   {
/*  51 */     this.fFeatures = new HashMap();
/*  52 */     this.fProperties = new HashMap();
/*     */   }
/*     */ 
/*     */   public final Validator newValidator()
/*     */   {
/*  63 */     return new ValidatorImpl(this);
/*     */   }
/*     */ 
/*     */   public final ValidatorHandler newValidatorHandler()
/*     */   {
/*  70 */     return new ValidatorHandlerImpl(this);
/*     */   }
/*     */ 
/*     */   public final Boolean getFeature(String featureId)
/*     */   {
/*  83 */     return (Boolean)this.fFeatures.get(featureId);
/*     */   }
/*     */ 
/*     */   public final void setFeature(String featureId, boolean state)
/*     */   {
/*  90 */     this.fFeatures.put(featureId, state ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   public final Object getProperty(String propertyId)
/*     */   {
/*  99 */     return this.fProperties.get(propertyId);
/*     */   }
/*     */ 
/*     */   public final void setProperty(String propertyId, Object state)
/*     */   {
/* 106 */     this.fProperties.put(propertyId, state);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.AbstractXMLSchema
 * JD-Core Version:    0.6.2
 */