/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityDescription;
/*     */ 
/*     */ public class XMLEntityDescriptionImpl extends XMLResourceIdentifierImpl
/*     */   implements XMLEntityDescription
/*     */ {
/*     */   protected String fEntityName;
/*     */ 
/*     */   public XMLEntityDescriptionImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLEntityDescriptionImpl(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId)
/*     */   {
/*  54 */     setDescription(entityName, publicId, literalSystemId, baseSystemId, expandedSystemId);
/*     */   }
/*     */ 
/*     */   public XMLEntityDescriptionImpl(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace)
/*     */   {
/*  69 */     setDescription(entityName, publicId, literalSystemId, baseSystemId, expandedSystemId, namespace);
/*     */   }
/*     */ 
/*     */   public void setEntityName(String name)
/*     */   {
/*  89 */     this.fEntityName = name;
/*     */   }
/*     */ 
/*     */   public String getEntityName()
/*     */   {
/*  98 */     return this.fEntityName;
/*     */   }
/*     */ 
/*     */   public void setDescription(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId)
/*     */   {
/* 112 */     setDescription(entityName, publicId, literalSystemId, baseSystemId, expandedSystemId, null);
/*     */   }
/*     */ 
/*     */   public void setDescription(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace)
/*     */   {
/* 127 */     this.fEntityName = entityName;
/* 128 */     setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, namespace);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 135 */     super.clear();
/* 136 */     this.fEntityName = null;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 145 */     int code = super.hashCode();
/* 146 */     if (this.fEntityName != null) {
/* 147 */       code += this.fEntityName.hashCode();
/*     */     }
/* 149 */     return code;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     StringBuffer str = new StringBuffer();
/* 155 */     if (this.fEntityName != null) {
/* 156 */       str.append(this.fEntityName);
/*     */     }
/* 158 */     str.append(':');
/* 159 */     if (this.fPublicId != null) {
/* 160 */       str.append(this.fPublicId);
/*     */     }
/* 162 */     str.append(':');
/* 163 */     if (this.fLiteralSystemId != null) {
/* 164 */       str.append(this.fLiteralSystemId);
/*     */     }
/* 166 */     str.append(':');
/* 167 */     if (this.fBaseSystemId != null) {
/* 168 */       str.append(this.fBaseSystemId);
/*     */     }
/* 170 */     str.append(':');
/* 171 */     if (this.fExpandedSystemId != null) {
/* 172 */       str.append(this.fExpandedSystemId);
/*     */     }
/* 174 */     str.append(':');
/* 175 */     if (this.fNamespace != null) {
/* 176 */       str.append(this.fNamespace);
/*     */     }
/* 178 */     return str.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLEntityDescriptionImpl
 * JD-Core Version:    0.6.2
 */