/*    */ package com.sun.xml.internal.stream.dtd.nonvalidating;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.QName;
/*    */ 
/*    */ public class XMLAttributeDecl
/*    */ {
/* 31 */   public final QName name = new QName();
/*    */ 
/* 34 */   public final XMLSimpleType simpleType = new XMLSimpleType();
/*    */   public boolean optional;
/*    */ 
/*    */   public void setValues(QName name, XMLSimpleType simpleType, boolean optional)
/*    */   {
/* 48 */     this.name.setValues(name);
/* 49 */     this.simpleType.setValues(simpleType);
/* 50 */     this.optional = optional;
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 57 */     this.name.clear();
/* 58 */     this.simpleType.clear();
/* 59 */     this.optional = false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.dtd.nonvalidating.XMLAttributeDecl
 * JD-Core Version:    0.6.2
 */