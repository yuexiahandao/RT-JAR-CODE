/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ 
/*     */ public class XMLResourceIdentifierImpl
/*     */   implements XMLResourceIdentifier
/*     */ {
/*     */   protected String fPublicId;
/*     */   protected String fLiteralSystemId;
/*     */   protected String fBaseSystemId;
/*     */   protected String fExpandedSystemId;
/*     */   protected String fNamespace;
/*     */ 
/*     */   public XMLResourceIdentifierImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLResourceIdentifierImpl(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId)
/*     */   {
/*  73 */     setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, null);
/*     */   }
/*     */ 
/*     */   public XMLResourceIdentifierImpl(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace)
/*     */   {
/*  89 */     setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, namespace);
/*     */   }
/*     */ 
/*     */   public void setValues(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId)
/*     */   {
/* 100 */     setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, null);
/*     */   }
/*     */ 
/*     */   public void setValues(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace)
/*     */   {
/* 108 */     this.fPublicId = publicId;
/* 109 */     this.fLiteralSystemId = literalSystemId;
/* 110 */     this.fBaseSystemId = baseSystemId;
/* 111 */     this.fExpandedSystemId = expandedSystemId;
/* 112 */     this.fNamespace = namespace;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 117 */     this.fPublicId = null;
/* 118 */     this.fLiteralSystemId = null;
/* 119 */     this.fBaseSystemId = null;
/* 120 */     this.fExpandedSystemId = null;
/* 121 */     this.fNamespace = null;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/* 126 */     this.fPublicId = publicId;
/*     */   }
/*     */ 
/*     */   public void setLiteralSystemId(String literalSystemId)
/*     */   {
/* 131 */     this.fLiteralSystemId = literalSystemId;
/*     */   }
/*     */ 
/*     */   public void setBaseSystemId(String baseSystemId)
/*     */   {
/* 136 */     this.fBaseSystemId = baseSystemId;
/*     */   }
/*     */ 
/*     */   public void setExpandedSystemId(String expandedSystemId)
/*     */   {
/* 141 */     this.fExpandedSystemId = expandedSystemId;
/*     */   }
/*     */ 
/*     */   public void setNamespace(String namespace)
/*     */   {
/* 146 */     this.fNamespace = namespace;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 155 */     return this.fPublicId;
/*     */   }
/*     */ 
/*     */   public String getLiteralSystemId()
/*     */   {
/* 160 */     return this.fLiteralSystemId;
/*     */   }
/*     */ 
/*     */   public String getBaseSystemId()
/*     */   {
/* 167 */     return this.fBaseSystemId;
/*     */   }
/*     */ 
/*     */   public String getExpandedSystemId()
/*     */   {
/* 172 */     return this.fExpandedSystemId;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 177 */     return this.fNamespace;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 186 */     int code = 0;
/* 187 */     if (this.fPublicId != null) {
/* 188 */       code += this.fPublicId.hashCode();
/*     */     }
/* 190 */     if (this.fLiteralSystemId != null) {
/* 191 */       code += this.fLiteralSystemId.hashCode();
/*     */     }
/* 193 */     if (this.fBaseSystemId != null) {
/* 194 */       code += this.fBaseSystemId.hashCode();
/*     */     }
/* 196 */     if (this.fExpandedSystemId != null) {
/* 197 */       code += this.fExpandedSystemId.hashCode();
/*     */     }
/* 199 */     if (this.fNamespace != null) {
/* 200 */       code += this.fNamespace.hashCode();
/*     */     }
/* 202 */     return code;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 207 */     StringBuffer str = new StringBuffer();
/* 208 */     if (this.fPublicId != null) {
/* 209 */       str.append(this.fPublicId);
/*     */     }
/* 211 */     str.append(':');
/* 212 */     if (this.fLiteralSystemId != null) {
/* 213 */       str.append(this.fLiteralSystemId);
/*     */     }
/* 215 */     str.append(':');
/* 216 */     if (this.fBaseSystemId != null) {
/* 217 */       str.append(this.fBaseSystemId);
/*     */     }
/* 219 */     str.append(':');
/* 220 */     if (this.fExpandedSystemId != null) {
/* 221 */       str.append(this.fExpandedSystemId);
/*     */     }
/* 223 */     str.append(':');
/* 224 */     if (this.fNamespace != null) {
/* 225 */       str.append(this.fNamespace);
/*     */     }
/* 227 */     return str.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl
 * JD-Core Version:    0.6.2
 */