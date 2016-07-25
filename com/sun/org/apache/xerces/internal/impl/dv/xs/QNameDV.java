/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.xs.datatypes.XSQName;
/*     */ 
/*     */ public class QNameDV extends TypeValidator
/*     */ {
/*  40 */   private static final String EMPTY_STRING = "".intern();
/*     */ 
/*     */   public short getAllowedFacets() {
/*  43 */     return 2079;
/*     */   }
/*     */ 
/*     */   public Object getActualValue(String content, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*  52 */     int colonptr = content.indexOf(":");
/*     */     String localpart;
/*     */     String prefix;
/*     */     String localpart;
/*  53 */     if (colonptr > 0) {
/*  54 */       String prefix = context.getSymbol(content.substring(0, colonptr));
/*  55 */       localpart = content.substring(colonptr + 1);
/*     */     } else {
/*  57 */       prefix = EMPTY_STRING;
/*  58 */       localpart = content;
/*     */     }
/*     */ 
/*  62 */     if ((prefix.length() > 0) && (!XMLChar.isValidNCName(prefix))) {
/*  63 */       throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "QName" });
/*     */     }
/*  65 */     if (!XMLChar.isValidNCName(localpart)) {
/*  66 */       throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "QName" });
/*     */     }
/*     */ 
/*  69 */     String uri = context.getURI(prefix);
/*  70 */     if ((prefix.length() > 0) && (uri == null)) {
/*  71 */       throw new InvalidDatatypeValueException("UndeclaredPrefix", new Object[] { content, prefix });
/*     */     }
/*  73 */     return new XQName(prefix, context.getSymbol(localpart), context.getSymbol(content), uri);
/*     */   }
/*     */ 
/*     */   public int getDataLength(Object value)
/*     */   {
/*  80 */     return ((XQName)value).rawname.length();
/*     */   }
/*     */ 
/*     */   private static final class XQName extends com.sun.org.apache.xerces.internal.xni.QName
/*     */     implements XSQName
/*     */   {
/*     */     public XQName(String prefix, String localpart, String rawname, String uri)
/*     */     {
/*  89 */       setValues(prefix, localpart, rawname, uri);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object object)
/*     */     {
/*  94 */       if ((object instanceof com.sun.org.apache.xerces.internal.xni.QName)) {
/*  95 */         com.sun.org.apache.xerces.internal.xni.QName qname = (com.sun.org.apache.xerces.internal.xni.QName)object;
/*  96 */         return (this.uri == qname.uri) && (this.localpart == qname.localpart);
/*     */       }
/*  98 */       return false;
/*     */     }
/*     */ 
/*     */     public synchronized String toString() {
/* 102 */       return this.rawname;
/*     */     }
/*     */     public javax.xml.namespace.QName getJAXPQName() {
/* 105 */       return new javax.xml.namespace.QName(this.uri, this.localpart, this.prefix);
/*     */     }
/*     */     public com.sun.org.apache.xerces.internal.xni.QName getXNIQName() {
/* 108 */       return this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.QNameDV
 * JD-Core Version:    0.6.2
 */