/*    */ package com.sun.xml.internal.bind.v2.schemagen;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ContentModelContainer;
/*    */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle;
/*    */ 
/*    */  enum GroupKind
/*    */ {
/* 37 */   ALL("all"), SEQUENCE("sequence"), CHOICE("choice");
/*    */ 
/*    */   private final String name;
/*    */ 
/*    */   private GroupKind(String name) {
/* 42 */     this.name = name;
/*    */   }
/*    */ 
/*    */   Particle write(ContentModelContainer parent)
/*    */   {
/* 49 */     return (Particle)parent._element(this.name, Particle.class);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.GroupKind
 * JD-Core Version:    0.6.2
 */