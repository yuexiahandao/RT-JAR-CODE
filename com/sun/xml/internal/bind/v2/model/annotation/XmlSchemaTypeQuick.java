/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlSchemaType;
/*    */ 
/*    */ final class XmlSchemaTypeQuick extends Quick
/*    */   implements XmlSchemaType
/*    */ {
/*    */   private final XmlSchemaType core;
/*    */ 
/*    */   public XmlSchemaTypeQuick(Locatable upstream, XmlSchemaType core)
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
/* 48 */     return new XmlSchemaTypeQuick(upstream, (XmlSchemaType)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlSchemaType> annotationType() {
/* 52 */     return XmlSchemaType.class;
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
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlSchemaTypeQuick
 * JD-Core Version:    0.6.2
 */