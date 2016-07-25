/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ 
/*     */ public class XMLElementDecl
/*     */ {
/*     */   public static final short TYPE_ANY = 0;
/*     */   public static final short TYPE_EMPTY = 1;
/*     */   public static final short TYPE_MIXED = 2;
/*     */   public static final short TYPE_CHILDREN = 3;
/*     */   public static final short TYPE_SIMPLE = 4;
/*  95 */   public final QName name = new QName();
/*     */ 
/*  98 */   public int scope = -1;
/*     */ 
/* 101 */   public short type = -1;
/*     */   public ContentModelValidator contentModelValidator;
/* 107 */   public final XMLSimpleType simpleType = new XMLSimpleType();
/*     */ 
/*     */   public void setValues(QName name, int scope, short type, ContentModelValidator contentModelValidator, XMLSimpleType simpleType)
/*     */   {
/* 123 */     this.name.setValues(name);
/* 124 */     this.scope = scope;
/* 125 */     this.type = type;
/* 126 */     this.contentModelValidator = contentModelValidator;
/* 127 */     this.simpleType.setValues(simpleType);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 134 */     this.name.clear();
/* 135 */     this.type = -1;
/* 136 */     this.scope = -1;
/* 137 */     this.contentModelValidator = null;
/* 138 */     this.simpleType.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLElementDecl
 * JD-Core Version:    0.6.2
 */