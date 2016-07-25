/*     */ package com.sun.org.apache.xerces.internal.xni;
/*     */ 
/*     */ public class QName
/*     */   implements Cloneable
/*     */ {
/*     */   public String prefix;
/*     */   public String localpart;
/*     */   public String rawname;
/*     */   public String uri;
/*     */ 
/*     */   public QName()
/*     */   {
/* 116 */     clear();
/*     */   }
/*     */ 
/*     */   public QName(String prefix, String localpart, String rawname, String uri)
/*     */   {
/* 121 */     setValues(prefix, localpart, rawname, uri);
/*     */   }
/*     */ 
/*     */   public QName(QName qname)
/*     */   {
/* 126 */     setValues(qname);
/*     */   }
/*     */ 
/*     */   public void setValues(QName qname)
/*     */   {
/* 139 */     this.prefix = qname.prefix;
/* 140 */     this.localpart = qname.localpart;
/* 141 */     this.rawname = qname.rawname;
/* 142 */     this.uri = qname.uri;
/*     */   }
/*     */ 
/*     */   public void setValues(String prefix, String localpart, String rawname, String uri)
/*     */   {
/* 155 */     this.prefix = prefix;
/* 156 */     this.localpart = localpart;
/* 157 */     this.rawname = rawname;
/* 158 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 163 */     this.prefix = null;
/* 164 */     this.localpart = null;
/* 165 */     this.rawname = null;
/* 166 */     this.uri = null;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 175 */     return new QName(this);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 184 */     if (this.uri != null) {
/* 185 */       return this.uri.hashCode() + (this.localpart != null ? this.localpart.hashCode() : 0);
/*     */     }
/*     */ 
/* 188 */     return this.rawname != null ? this.rawname.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object object)
/*     */   {
/* 193 */     if (object == this) {
/* 194 */       return true;
/*     */     }
/*     */ 
/* 197 */     if ((object != null) && ((object instanceof QName))) {
/* 198 */       QName qname = (QName)object;
/* 199 */       if (qname.uri != null) {
/* 200 */         return (qname.localpart.equals(this.localpart)) && (qname.uri.equals(this.uri));
/*     */       }
/* 202 */       if (this.uri == null) {
/* 203 */         return this.rawname.equals(qname.rawname);
/*     */       }
/*     */     }
/*     */ 
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 213 */     StringBuffer str = new StringBuffer();
/* 214 */     boolean comma = false;
/* 215 */     if (this.prefix != null) {
/* 216 */       str.append("prefix=\"" + this.prefix + '"');
/* 217 */       comma = true;
/*     */     }
/* 219 */     if (this.localpart != null) {
/* 220 */       if (comma) {
/* 221 */         str.append(',');
/*     */       }
/* 223 */       str.append("localpart=\"" + this.localpart + '"');
/* 224 */       comma = true;
/*     */     }
/* 226 */     if (this.rawname != null) {
/* 227 */       if (comma) {
/* 228 */         str.append(',');
/*     */       }
/* 230 */       str.append("rawname=\"" + this.rawname + '"');
/* 231 */       comma = true;
/*     */     }
/* 233 */     if (this.uri != null) {
/* 234 */       if (comma) {
/* 235 */         str.append(',');
/*     */       }
/* 237 */       str.append("uri=\"" + this.uri + '"');
/*     */     }
/* 239 */     return str.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.QName
 * JD-Core Version:    0.6.2
 */