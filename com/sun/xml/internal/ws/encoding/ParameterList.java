/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ final class ParameterList
/*     */ {
/*     */   private final Map<String, String> list;
/*     */ 
/*     */   ParameterList(String s)
/*     */   {
/*  55 */     HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");
/*     */ 
/*  60 */     this.list = new HashMap();
/*     */     while (true) {
/*  62 */       HeaderTokenizer.Token tk = h.next();
/*  63 */       int type = tk.getType();
/*     */ 
/*  65 */       if (type == -4) {
/*  66 */         return;
/*     */       }
/*  68 */       if ((char)type != ';')
/*     */         break;
/*  70 */       tk = h.next();
/*     */ 
/*  72 */       if (tk.getType() == -4) {
/*  73 */         return;
/*     */       }
/*  75 */       if (tk.getType() != -1)
/*  76 */         throw new WebServiceException();
/*  77 */       String name = tk.getValue().toLowerCase();
/*     */ 
/*  80 */       tk = h.next();
/*  81 */       if ((char)tk.getType() != '=') {
/*  82 */         throw new WebServiceException();
/*     */       }
/*     */ 
/*  85 */       tk = h.next();
/*  86 */       type = tk.getType();
/*     */ 
/*  88 */       if ((type != -1) && (type != -2))
/*     */       {
/*  90 */         throw new WebServiceException();
/*     */       }
/*  92 */       this.list.put(name, tk.getValue());
/*     */     }
/*  94 */     throw new WebServiceException();
/*     */   }
/*     */ 
/*     */   int size()
/*     */   {
/* 104 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   String get(String name)
/*     */   {
/* 117 */     return (String)this.list.get(name.trim().toLowerCase());
/*     */   }
/*     */ 
/*     */   Iterator<String> getNames()
/*     */   {
/* 128 */     return this.list.keySet().iterator();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.ParameterList
 * JD-Core Version:    0.6.2
 */