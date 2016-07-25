/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ final class XmlElementQuick extends Quick
/*    */   implements XmlElement
/*    */ {
/*    */   private final XmlElement core;
/*    */ 
/*    */   public XmlElementQuick(Locatable upstream, XmlElement core)
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
/* 48 */     return new XmlElementQuick(upstream, (XmlElement)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlElement> annotationType() {
/* 52 */     return XmlElement.class;
/*    */   }
/*    */ 
/*    */   public String name() {
/* 56 */     return this.core.name();
/*    */   }
/*    */ 
/*    */   public Class type() {
/* 60 */     return this.core.type();
/*    */   }
/*    */ 
/*    */   public String namespace() {
/* 64 */     return this.core.namespace();
/*    */   }
/*    */ 
/*    */   public String defaultValue() {
/* 68 */     return this.core.defaultValue();
/*    */   }
/*    */ 
/*    */   public boolean required() {
/* 72 */     return this.core.required();
/*    */   }
/*    */ 
/*    */   public boolean nillable() {
/* 76 */     return this.core.nillable();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlElementQuick
 * JD-Core Version:    0.6.2
 */