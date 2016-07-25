/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public abstract class TagName
/*     */ {
/*     */   public String uri;
/*     */   public String local;
/*     */   public Attributes atts;
/*     */ 
/*     */   public final boolean matches(String nsUri, String local)
/*     */   {
/*  85 */     return (this.uri == nsUri) && (this.local == local);
/*     */   }
/*     */ 
/*     */   public final boolean matches(Name name)
/*     */   {
/*  92 */     return (this.local == name.localName) && (this.uri == name.nsUri);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 106 */     return '{' + this.uri + '}' + this.local;
/*     */   }
/*     */ 
/*     */   public abstract String getQname();
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 122 */     String qname = getQname();
/* 123 */     int idx = qname.indexOf(':');
/* 124 */     if (idx < 0) return "";
/* 125 */     return qname.substring(0, idx);
/*     */   }
/*     */ 
/*     */   public QName createQName()
/*     */   {
/* 132 */     return new QName(this.uri, this.local, getPrefix());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName
 * JD-Core Version:    0.6.2
 */