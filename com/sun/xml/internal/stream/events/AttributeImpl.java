/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ 
/*     */ public class AttributeImpl extends DummyEvent
/*     */   implements Attribute
/*     */ {
/*     */   private String fValue;
/*     */   private String fNonNormalizedvalue;
/*     */   private QName fQName;
/*  55 */   private String fAttributeType = "CDATA";
/*     */   private boolean fIsSpecified;
/*     */ 
/*     */   public AttributeImpl()
/*     */   {
/*  63 */     init();
/*     */   }
/*     */   public AttributeImpl(String name, String value) {
/*  66 */     init();
/*  67 */     this.fQName = new QName(name);
/*  68 */     this.fValue = value;
/*     */   }
/*     */ 
/*     */   public AttributeImpl(String prefix, String name, String value) {
/*  72 */     this(prefix, null, name, value, null, null, false);
/*     */   }
/*     */ 
/*     */   public AttributeImpl(String prefix, String uri, String localPart, String value, String type) {
/*  76 */     this(prefix, uri, localPart, value, null, type, false);
/*     */   }
/*     */ 
/*     */   public AttributeImpl(String prefix, String uri, String localPart, String value, String nonNormalizedvalue, String type, boolean isSpecified) {
/*  80 */     this(new QName(uri, localPart, prefix), value, nonNormalizedvalue, type, isSpecified);
/*     */   }
/*     */ 
/*     */   public AttributeImpl(QName qname, String value, String nonNormalizedvalue, String type, boolean isSpecified)
/*     */   {
/*  85 */     init();
/*  86 */     this.fQName = qname;
/*  87 */     this.fValue = value;
/*  88 */     if ((type != null) && (!type.equals(""))) {
/*  89 */       this.fAttributeType = type;
/*     */     }
/*  91 */     this.fNonNormalizedvalue = nonNormalizedvalue;
/*  92 */     this.fIsSpecified = isSpecified;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  97 */     if ((this.fQName.getPrefix() != null) && (this.fQName.getPrefix().length() > 0)) {
/*  98 */       return this.fQName.getPrefix() + ":" + this.fQName.getLocalPart() + "='" + this.fValue + "'";
/*     */     }
/* 100 */     return this.fQName.getLocalPart() + "='" + this.fValue + "'";
/*     */   }
/*     */ 
/*     */   public void setName(QName name) {
/* 104 */     this.fQName = name;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/* 108 */     return this.fQName;
/*     */   }
/*     */ 
/*     */   public void setValue(String value) {
/* 112 */     this.fValue = value;
/*     */   }
/*     */ 
/*     */   public String getValue() {
/* 116 */     return this.fValue;
/*     */   }
/*     */ 
/*     */   public void setNonNormalizedValue(String nonNormalizedvalue) {
/* 120 */     this.fNonNormalizedvalue = nonNormalizedvalue;
/*     */   }
/*     */ 
/*     */   public String getNonNormalizedValue() {
/* 124 */     return this.fNonNormalizedvalue;
/*     */   }
/*     */ 
/*     */   public void setAttributeType(String attributeType) {
/* 128 */     this.fAttributeType = attributeType;
/*     */   }
/*     */ 
/*     */   public String getDTDType()
/*     */   {
/* 134 */     return this.fAttributeType;
/*     */   }
/*     */ 
/*     */   public void setSpecified(boolean isSpecified)
/*     */   {
/* 140 */     this.fIsSpecified = isSpecified;
/*     */   }
/*     */ 
/*     */   public boolean isSpecified() {
/* 144 */     return this.fIsSpecified;
/*     */   }
/*     */ 
/*     */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*     */     throws IOException
/*     */   {
/* 150 */     writer.write(toString());
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/* 155 */     setEventType(10);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.AttributeImpl
 * JD-Core Version:    0.6.2
 */