/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ 
/*    */ final class XmlAttributeQuick extends Quick
/*    */   implements XmlAttribute
/*    */ {
/*    */   private final XmlAttribute core;
/*    */ 
/*    */   public XmlAttributeQuick(Locatable upstream, XmlAttribute core)
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
/* 48 */     return new XmlAttributeQuick(upstream, (XmlAttribute)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlAttribute> annotationType() {
/* 52 */     return XmlAttribute.class;
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
/*    */   public boolean required() {
/* 64 */     return this.core.required();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlAttributeQuick
 * JD-Core Version:    0.6.2
 */