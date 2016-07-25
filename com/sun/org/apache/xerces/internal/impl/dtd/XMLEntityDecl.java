/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ public class XMLEntityDecl
/*     */ {
/*     */   public String name;
/*     */   public String publicId;
/*     */   public String systemId;
/*     */   public String baseSystemId;
/*     */   public String notation;
/*     */   public boolean isPE;
/*     */   public boolean inExternal;
/*     */   public String value;
/*     */ 
/*     */   public void setValues(String name, String publicId, String systemId, String baseSystemId, String notation, boolean isPE, boolean inExternal)
/*     */   {
/* 115 */     setValues(name, publicId, systemId, baseSystemId, notation, null, isPE, inExternal);
/*     */   }
/*     */ 
/*     */   public void setValues(String name, String publicId, String systemId, String baseSystemId, String notation, String value, boolean isPE, boolean inExternal)
/*     */   {
/* 133 */     this.name = name;
/* 134 */     this.publicId = publicId;
/* 135 */     this.systemId = systemId;
/* 136 */     this.baseSystemId = baseSystemId;
/* 137 */     this.notation = notation;
/* 138 */     this.value = value;
/* 139 */     this.isPE = isPE;
/* 140 */     this.inExternal = inExternal;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 147 */     this.name = null;
/* 148 */     this.publicId = null;
/* 149 */     this.systemId = null;
/* 150 */     this.baseSystemId = null;
/* 151 */     this.notation = null;
/* 152 */     this.value = null;
/* 153 */     this.isPE = false;
/* 154 */     this.inExternal = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLEntityDecl
 * JD-Core Version:    0.6.2
 */