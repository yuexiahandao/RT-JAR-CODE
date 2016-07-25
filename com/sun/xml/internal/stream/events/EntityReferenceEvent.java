/*    */ package com.sun.xml.internal.stream.events;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import javax.xml.stream.events.EntityDeclaration;
/*    */ import javax.xml.stream.events.EntityReference;
/*    */ 
/*    */ public class EntityReferenceEvent extends DummyEvent
/*    */   implements EntityReference
/*    */ {
/*    */   private EntityDeclaration fEntityDeclaration;
/*    */   private String fEntityName;
/*    */ 
/*    */   public EntityReferenceEvent()
/*    */   {
/* 43 */     init();
/*    */   }
/*    */ 
/*    */   public EntityReferenceEvent(String entityName, EntityDeclaration entityDeclaration) {
/* 47 */     init();
/* 48 */     this.fEntityName = entityName;
/* 49 */     this.fEntityDeclaration = entityDeclaration;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 53 */     return this.fEntityName;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 57 */     String text = this.fEntityDeclaration.getReplacementText();
/* 58 */     if (text == null)
/* 59 */       text = "";
/* 60 */     return "&" + getName() + ";='" + text + "'";
/*    */   }
/*    */ 
/*    */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*    */     throws IOException
/*    */   {
/* 66 */     writer.write(38);
/* 67 */     writer.write(getName());
/* 68 */     writer.write(59);
/*    */   }
/*    */ 
/*    */   public EntityDeclaration getDeclaration() {
/* 72 */     return this.fEntityDeclaration;
/*    */   }
/*    */ 
/*    */   protected void init() {
/* 76 */     setEventType(9);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.EntityReferenceEvent
 * JD-Core Version:    0.6.2
 */