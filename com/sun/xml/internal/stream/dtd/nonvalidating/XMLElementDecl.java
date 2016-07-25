/*    */ package com.sun.xml.internal.stream.dtd.nonvalidating;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.QName;
/*    */ 
/*    */ public class XMLElementDecl
/*    */ {
/*    */   public static final short TYPE_ANY = 0;
/*    */   public static final short TYPE_EMPTY = 1;
/*    */   public static final short TYPE_MIXED = 2;
/*    */   public static final short TYPE_CHILDREN = 3;
/*    */   public static final short TYPE_SIMPLE = 4;
/* 45 */   public final QName name = new QName();
/*    */ 
/* 48 */   public int scope = -1;
/*    */ 
/* 51 */   public short type = -1;
/*    */ 
/* 55 */   public final XMLSimpleType simpleType = new XMLSimpleType();
/*    */ 
/*    */   public void setValues(QName name, int scope, short type, XMLSimpleType simpleType)
/*    */   {
/* 67 */     this.name.setValues(name);
/* 68 */     this.scope = scope;
/* 69 */     this.type = type;
/* 70 */     this.simpleType.setValues(simpleType);
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 77 */     this.name.clear();
/* 78 */     this.type = -1;
/* 79 */     this.scope = -1;
/* 80 */     this.simpleType.clear();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.dtd.nonvalidating.XMLElementDecl
 * JD-Core Version:    0.6.2
 */