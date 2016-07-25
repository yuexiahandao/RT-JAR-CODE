/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ 
/*    */ final class XmlTypeQuick extends Quick
/*    */   implements XmlType
/*    */ {
/*    */   private final XmlType core;
/*    */ 
/*    */   public XmlTypeQuick(Locatable upstream, XmlType core)
/*    */   {
/* 39 */     super(upstream);
/* 40 */     this.core = core;
/*    */   }
/*    */ 
/*    */   protected Annotation getAnnotation() {
/* 44 */     return this.core;
/*    */   }
/*    */ 
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 48 */     return new XmlTypeQuick(upstream, (XmlType)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlType> annotationType() {
/* 52 */     return XmlType.class;
/*    */   }
/*    */ 
/*    */   public String name() {
/* 56 */     return this.core.name();
/*    */   }
/*    */ 
/*    */   public String namespace() {
/* 60 */     return this.core.namespace();
/*    */   }
/*    */ 
/*    */   public String[] propOrder() {
/* 64 */     return this.core.propOrder();
/*    */   }
/*    */ 
/*    */   public Class factoryClass() {
/* 68 */     return this.core.factoryClass();
/*    */   }
/*    */ 
/*    */   public String factoryMethod() {
/* 72 */     return this.core.factoryMethod();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlTypeQuick
 * JD-Core Version:    0.6.2
 */