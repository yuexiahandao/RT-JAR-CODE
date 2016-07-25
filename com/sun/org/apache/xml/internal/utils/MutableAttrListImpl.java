/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class MutableAttrListImpl extends AttributesImpl
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 6289452013442934470L;
/*     */ 
/*     */   public MutableAttrListImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MutableAttrListImpl(Attributes atts)
/*     */   {
/*  58 */     super(atts);
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String qName, String type, String value)
/*     */   {
/*  82 */     if (null == uri) {
/*  83 */       uri = "";
/*     */     }
/*     */ 
/*  87 */     int index = getIndex(qName);
/*     */ 
/*  92 */     if (index >= 0)
/*  93 */       setAttribute(index, uri, localName, qName, type, value);
/*     */     else
/*  95 */       super.addAttribute(uri, localName, qName, type, value);
/*     */   }
/*     */ 
/*     */   public void addAttributes(Attributes atts)
/*     */   {
/* 106 */     int nAtts = atts.getLength();
/*     */ 
/* 108 */     for (int i = 0; i < nAtts; i++)
/*     */     {
/* 110 */       String uri = atts.getURI(i);
/*     */ 
/* 112 */       if (null == uri) {
/* 113 */         uri = "";
/*     */       }
/* 115 */       String localName = atts.getLocalName(i);
/* 116 */       String qname = atts.getQName(i);
/* 117 */       int index = getIndex(uri, localName);
/*     */ 
/* 119 */       if (index >= 0) {
/* 120 */         setAttribute(index, uri, localName, qname, atts.getType(i), atts.getValue(i));
/*     */       }
/*     */       else
/* 123 */         addAttribute(uri, localName, qname, atts.getType(i), atts.getValue(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean contains(String name)
/*     */   {
/* 137 */     return getValue(name) != null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.MutableAttrListImpl
 * JD-Core Version:    0.6.2
 */