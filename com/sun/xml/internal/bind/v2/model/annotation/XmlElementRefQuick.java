/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlElementRef;
/*    */ 
/*    */ final class XmlElementRefQuick extends Quick
/*    */   implements XmlElementRef
/*    */ {
/*    */   private final XmlElementRef core;
/*    */ 
/*    */   public XmlElementRefQuick(Locatable upstream, XmlElementRef core)
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
/* 48 */     return new XmlElementRefQuick(upstream, (XmlElementRef)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlElementRef> annotationType() {
/* 52 */     return XmlElementRef.class;
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
/*    */   public boolean required() {
/* 68 */     return this.core.required();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlElementRefQuick
 * JD-Core Version:    0.6.2
 */