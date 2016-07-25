/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ final class XmlRootElementQuick extends Quick
/*    */   implements XmlRootElement
/*    */ {
/*    */   private final XmlRootElement core;
/*    */ 
/*    */   public XmlRootElementQuick(Locatable upstream, XmlRootElement core)
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
/* 48 */     return new XmlRootElementQuick(upstream, (XmlRootElement)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlRootElement> annotationType() {
/* 52 */     return XmlRootElement.class;
/*    */   }
/*    */ 
/*    */   public String name() {
/* 56 */     return this.core.name();
/*    */   }
/*    */ 
/*    */   public String namespace() {
/* 60 */     return this.core.namespace();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlRootElementQuick
 * JD-Core Version:    0.6.2
 */