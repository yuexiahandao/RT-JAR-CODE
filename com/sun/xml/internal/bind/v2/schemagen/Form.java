/*     */ package com.sun.xml.internal.bind.v2.schemagen;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalAttribute;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalElement;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Schema;
/*     */ import com.sun.xml.internal.txw2.TypedXmlWriter;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */  enum Form
/*     */ {
/*  42 */   QUALIFIED(XmlNsForm.QUALIFIED, true), 
/*     */ 
/*  47 */   UNQUALIFIED(XmlNsForm.UNQUALIFIED, false), 
/*     */ 
/*  54 */   UNSET(XmlNsForm.UNSET, false);
/*     */ 
/*     */   private final XmlNsForm xnf;
/*     */   public final boolean isEffectivelyQualified;
/*     */ 
/*     */   private Form(XmlNsForm xnf, boolean effectivelyQualified)
/*     */   {
/*  70 */     this.xnf = xnf;
/*  71 */     this.isEffectivelyQualified = effectivelyQualified;
/*     */   }
/*     */ 
/*     */   abstract void declare(String paramString, Schema paramSchema);
/*     */ 
/*     */   public void writeForm(LocalElement e, QName tagName)
/*     */   {
/*  84 */     _writeForm(e, tagName);
/*     */   }
/*     */ 
/*     */   public void writeForm(LocalAttribute a, QName tagName) {
/*  88 */     _writeForm(a, tagName);
/*     */   }
/*     */ 
/*     */   private void _writeForm(TypedXmlWriter e, QName tagName) {
/*  92 */     boolean qualified = tagName.getNamespaceURI().length() > 0;
/*     */ 
/*  94 */     if ((qualified) && (this != QUALIFIED)) {
/*  95 */       e._attribute("form", "qualified");
/*     */     }
/*  97 */     else if ((!qualified) && (this == QUALIFIED))
/*  98 */       e._attribute("form", "unqualified");
/*     */   }
/*     */ 
/*     */   public static Form get(XmlNsForm xnf)
/*     */   {
/* 105 */     for (Form v : values()) {
/* 106 */       if (v.xnf == xnf)
/* 107 */         return v;
/*     */     }
/* 109 */     throw new IllegalArgumentException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.Form
 * JD-Core Version:    0.6.2
 */