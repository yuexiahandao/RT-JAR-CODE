/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ 
/*     */ public class XMLAttributeDecl
/*     */ {
/*  76 */   public final QName name = new QName();
/*     */ 
/*  79 */   public final XMLSimpleType simpleType = new XMLSimpleType();
/*     */   public boolean optional;
/*     */ 
/*     */   public void setValues(QName name, XMLSimpleType simpleType, boolean optional)
/*     */   {
/*  96 */     this.name.setValues(name);
/*  97 */     this.simpleType.setValues(simpleType);
/*  98 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 105 */     this.name.clear();
/* 106 */     this.simpleType.clear();
/* 107 */     this.optional = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLAttributeDecl
 * JD-Core Version:    0.6.2
 */