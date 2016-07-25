/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlElementRef;
/*    */ import javax.xml.bind.annotation.XmlElementRefs;
/*    */ 
/*    */ final class XmlElementRefsQuick extends Quick
/*    */   implements XmlElementRefs
/*    */ {
/*    */   private final XmlElementRefs core;
/*    */ 
/*    */   public XmlElementRefsQuick(Locatable upstream, XmlElementRefs core)
/*    */   {
/* 40 */     super(upstream);
/* 41 */     this.core = core;
/*    */   }
/*    */ 
/*    */   protected Annotation getAnnotation() {
/* 45 */     return this.core;
/*    */   }
/*    */ 
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 49 */     return new XmlElementRefsQuick(upstream, (XmlElementRefs)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlElementRefs> annotationType() {
/* 53 */     return XmlElementRefs.class;
/*    */   }
/*    */ 
/*    */   public XmlElementRef[] value() {
/* 57 */     return this.core.value();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlElementRefsQuick
 * JD-Core Version:    0.6.2
 */