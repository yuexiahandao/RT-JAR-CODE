/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ 
/*    */ final class XmlTransientQuick extends Quick
/*    */   implements XmlTransient
/*    */ {
/*    */   private final XmlTransient core;
/*    */ 
/*    */   public XmlTransientQuick(Locatable upstream, XmlTransient core)
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
/* 48 */     return new XmlTransientQuick(upstream, (XmlTransient)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlTransient> annotationType() {
/* 52 */     return XmlTransient.class;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlTransientQuick
 * JD-Core Version:    0.6.2
 */