/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlEnum;
/*    */ 
/*    */ final class XmlEnumQuick extends Quick
/*    */   implements XmlEnum
/*    */ {
/*    */   private final XmlEnum core;
/*    */ 
/*    */   public XmlEnumQuick(Locatable upstream, XmlEnum core)
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
/* 48 */     return new XmlEnumQuick(upstream, (XmlEnum)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlEnum> annotationType() {
/* 52 */     return XmlEnum.class;
/*    */   }
/*    */ 
/*    */   public Class value() {
/* 56 */     return this.core.value();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlEnumQuick
 * JD-Core Version:    0.6.2
 */