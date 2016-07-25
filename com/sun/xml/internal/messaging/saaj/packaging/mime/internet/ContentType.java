/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ public final class ContentType
/*     */ {
/*     */   private String primaryType;
/*     */   private String subType;
/*     */   private ParameterList list;
/*     */ 
/*     */   public ContentType()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ContentType(String primaryType, String subType, ParameterList list)
/*     */   {
/*  63 */     this.primaryType = primaryType;
/*  64 */     this.subType = subType;
/*  65 */     if (list == null)
/*  66 */       list = new ParameterList();
/*  67 */     this.list = list;
/*     */   }
/*     */ 
/*     */   public ContentType(String s)
/*     */     throws ParseException
/*     */   {
/*  79 */     HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");
/*     */ 
/*  83 */     HeaderTokenizer.Token tk = h.next();
/*  84 */     if (tk.getType() != -1)
/*  85 */       throw new ParseException();
/*  86 */     this.primaryType = tk.getValue();
/*     */ 
/*  89 */     tk = h.next();
/*  90 */     if ((char)tk.getType() != '/') {
/*  91 */       throw new ParseException();
/*     */     }
/*     */ 
/*  94 */     tk = h.next();
/*  95 */     if (tk.getType() != -1)
/*  96 */       throw new ParseException();
/*  97 */     this.subType = tk.getValue();
/*     */ 
/* 100 */     String rem = h.getRemainder();
/* 101 */     if (rem != null)
/* 102 */       this.list = new ParameterList(rem);
/*     */   }
/*     */ 
/*     */   public ContentType copy() {
/* 106 */     return new ContentType(this.primaryType, this.subType, this.list.copy());
/*     */   }
/*     */ 
/*     */   public String getPrimaryType()
/*     */   {
/* 114 */     return this.primaryType;
/*     */   }
/*     */ 
/*     */   public String getSubType()
/*     */   {
/* 122 */     return this.subType;
/*     */   }
/*     */ 
/*     */   public String getBaseType()
/*     */   {
/* 133 */     return this.primaryType + '/' + this.subType;
/*     */   }
/*     */ 
/*     */   public String getParameter(String name)
/*     */   {
/* 142 */     if (this.list == null) {
/* 143 */       return null;
/*     */     }
/* 145 */     return this.list.get(name);
/*     */   }
/*     */ 
/*     */   public ParameterList getParameterList()
/*     */   {
/* 155 */     return this.list;
/*     */   }
/*     */ 
/*     */   public void setPrimaryType(String primaryType)
/*     */   {
/* 163 */     this.primaryType = primaryType;
/*     */   }
/*     */ 
/*     */   public void setSubType(String subType)
/*     */   {
/* 171 */     this.subType = subType;
/*     */   }
/*     */ 
/*     */   public void setParameter(String name, String value)
/*     */   {
/* 182 */     if (this.list == null) {
/* 183 */       this.list = new ParameterList();
/*     */     }
/* 185 */     this.list.set(name, value);
/*     */   }
/*     */ 
/*     */   public void setParameterList(ParameterList list)
/*     */   {
/* 193 */     this.list = list;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 204 */     if ((this.primaryType == null) || (this.subType == null)) {
/* 205 */       return null;
/*     */     }
/* 207 */     StringBuffer sb = new StringBuffer();
/* 208 */     sb.append(this.primaryType).append('/').append(this.subType);
/* 209 */     if (this.list != null)
/*     */     {
/* 213 */       sb.append(this.list.toString());
/*     */     }
/* 215 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public boolean match(ContentType cType)
/*     */   {
/* 238 */     if (!this.primaryType.equalsIgnoreCase(cType.getPrimaryType())) {
/* 239 */       return false;
/*     */     }
/* 241 */     String sType = cType.getSubType();
/*     */ 
/* 244 */     if ((this.subType.charAt(0) == '*') || (sType.charAt(0) == '*')) {
/* 245 */       return true;
/*     */     }
/*     */ 
/* 248 */     if (!this.subType.equalsIgnoreCase(sType)) {
/* 249 */       return false;
/*     */     }
/* 251 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean match(String s)
/*     */   {
/*     */     try
/*     */     {
/* 272 */       return match(new ContentType(s)); } catch (ParseException pex) {
/*     */     }
/* 274 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType
 * JD-Core Version:    0.6.2
 */