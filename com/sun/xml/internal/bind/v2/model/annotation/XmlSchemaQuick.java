/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlNs;
/*    */ import javax.xml.bind.annotation.XmlNsForm;
/*    */ import javax.xml.bind.annotation.XmlSchema;
/*    */ 
/*    */ final class XmlSchemaQuick extends Quick
/*    */   implements XmlSchema
/*    */ {
/*    */   private final XmlSchema core;
/*    */ 
/*    */   public XmlSchemaQuick(Locatable upstream, XmlSchema core)
/*    */   {
/* 41 */     super(upstream);
/* 42 */     this.core = core;
/*    */   }
/*    */ 
/*    */   protected Annotation getAnnotation() {
/* 46 */     return this.core;
/*    */   }
/*    */ 
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 50 */     return new XmlSchemaQuick(upstream, (XmlSchema)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlSchema> annotationType() {
/* 54 */     return XmlSchema.class;
/*    */   }
/*    */ 
/*    */   public String location() {
/* 58 */     return this.core.location();
/*    */   }
/*    */ 
/*    */   public String namespace() {
/* 62 */     return this.core.namespace();
/*    */   }
/*    */ 
/*    */   public XmlNs[] xmlns() {
/* 66 */     return this.core.xmlns();
/*    */   }
/*    */ 
/*    */   public XmlNsForm elementFormDefault() {
/* 70 */     return this.core.elementFormDefault();
/*    */   }
/*    */ 
/*    */   public XmlNsForm attributeFormDefault() {
/* 74 */     return this.core.attributeFormDefault();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlSchemaQuick
 * JD-Core Version:    0.6.2
 */