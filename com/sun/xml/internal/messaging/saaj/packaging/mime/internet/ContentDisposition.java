/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ public class ContentDisposition
/*     */ {
/*     */   private String disposition;
/*     */   private ParameterList list;
/*     */ 
/*     */   public ContentDisposition()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ContentDisposition(String disposition, ParameterList list)
/*     */   {
/*  62 */     this.disposition = disposition;
/*  63 */     this.list = list;
/*     */   }
/*     */ 
/*     */   public ContentDisposition(String s)
/*     */     throws ParseException
/*     */   {
/*  76 */     HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");
/*     */ 
/*  80 */     HeaderTokenizer.Token tk = h.next();
/*  81 */     if (tk.getType() != -1)
/*  82 */       throw new ParseException();
/*  83 */     this.disposition = tk.getValue();
/*     */ 
/*  86 */     String rem = h.getRemainder();
/*  87 */     if (rem != null)
/*  88 */       this.list = new ParameterList(rem);
/*     */   }
/*     */ 
/*     */   public String getDisposition()
/*     */   {
/*  97 */     return this.disposition;
/*     */   }
/*     */ 
/*     */   public String getParameter(String name)
/*     */   {
/* 107 */     if (this.list == null) {
/* 108 */       return null;
/*     */     }
/* 110 */     return this.list.get(name);
/*     */   }
/*     */ 
/*     */   public ParameterList getParameterList()
/*     */   {
/* 121 */     return this.list;
/*     */   }
/*     */ 
/*     */   public void setDisposition(String disposition)
/*     */   {
/* 130 */     this.disposition = disposition;
/*     */   }
/*     */ 
/*     */   public void setParameter(String name, String value)
/*     */   {
/* 142 */     if (this.list == null) {
/* 143 */       this.list = new ParameterList();
/*     */     }
/* 145 */     this.list.set(name, value);
/*     */   }
/*     */ 
/*     */   public void setParameterList(ParameterList list)
/*     */   {
/* 154 */     this.list = list;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 166 */     if (this.disposition == null) {
/* 167 */       return null;
/*     */     }
/* 169 */     if (this.list == null) {
/* 170 */       return this.disposition;
/*     */     }
/* 172 */     StringBuffer sb = new StringBuffer(this.disposition);
/*     */ 
/* 177 */     sb.append(this.list.toString(sb.length() + 21));
/* 178 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentDisposition
 * JD-Core Version:    0.6.2
 */