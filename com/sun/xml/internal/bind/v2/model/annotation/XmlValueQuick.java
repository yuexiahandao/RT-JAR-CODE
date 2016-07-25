/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlValue;
/*    */ 
/*    */ final class XmlValueQuick extends Quick
/*    */   implements XmlValue
/*    */ {
/*    */   private final XmlValue core;
/*    */ 
/*    */   public XmlValueQuick(Locatable upstream, XmlValue core)
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
/* 48 */     return new XmlValueQuick(upstream, (XmlValue)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlValue> annotationType() {
/* 52 */     return XmlValue.class;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlValueQuick
 * JD-Core Version:    0.6.2
 */