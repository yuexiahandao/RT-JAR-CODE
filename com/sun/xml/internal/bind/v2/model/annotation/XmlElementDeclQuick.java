/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlElementDecl;
/*    */ 
/*    */ final class XmlElementDeclQuick extends Quick
/*    */   implements XmlElementDecl
/*    */ {
/*    */   private final XmlElementDecl core;
/*    */ 
/*    */   public XmlElementDeclQuick(Locatable upstream, XmlElementDecl core)
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
/* 48 */     return new XmlElementDeclQuick(upstream, (XmlElementDecl)core);
/*    */   }
/*    */ 
/*    */   public Class<XmlElementDecl> annotationType() {
/* 52 */     return XmlElementDecl.class;
/*    */   }
/*    */ 
/*    */   public String name() {
/* 56 */     return this.core.name();
/*    */   }
/*    */ 
/*    */   public Class scope() {
/* 60 */     return this.core.scope();
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
/*    */   public String substitutionHeadNamespace() {
/* 72 */     return this.core.substitutionHeadNamespace();
/*    */   }
/*    */ 
/*    */   public String substitutionHeadName() {
/* 76 */     return this.core.substitutionHeadName();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.XmlElementDeclQuick
 * JD-Core Version:    0.6.2
 */