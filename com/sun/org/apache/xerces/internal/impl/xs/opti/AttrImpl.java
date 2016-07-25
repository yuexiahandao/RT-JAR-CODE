/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.TypeInfo;
/*     */ 
/*     */ public class AttrImpl extends NodeImpl
/*     */   implements Attr
/*     */ {
/*     */   Element element;
/*     */   String value;
/*     */ 
/*     */   public AttrImpl()
/*     */   {
/*  86 */     this.nodeType = 2;
/*     */   }
/*     */ 
/*     */   public AttrImpl(Element element, String prefix, String localpart, String rawname, String uri, String value)
/*     */   {
/* 100 */     super(prefix, localpart, rawname, uri, (short)2);
/* 101 */     this.element = element;
/* 102 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 106 */     return this.rawname;
/*     */   }
/*     */ 
/*     */   public boolean getSpecified() {
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   public String getValue() {
/* 114 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String getNodeValue() {
/* 118 */     return getValue();
/*     */   }
/*     */ 
/*     */   public Element getOwnerElement() {
/* 122 */     return this.element;
/*     */   }
/*     */ 
/*     */   public Document getOwnerDocument() {
/* 126 */     return this.element.getOwnerDocument();
/*     */   }
/*     */ 
/*     */   public void setValue(String value) throws DOMException {
/* 130 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public boolean isId()
/*     */   {
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   public TypeInfo getSchemaTypeInfo()
/*     */   {
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 150 */     return getName() + "=" + "\"" + getValue() + "\"";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.AttrImpl
 * JD-Core Version:    0.6.2
 */