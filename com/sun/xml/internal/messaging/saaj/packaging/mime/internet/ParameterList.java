/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class ParameterList
/*     */ {
/*     */   private final HashMap list;
/*     */ 
/*     */   public ParameterList()
/*     */   {
/*  53 */     this.list = new HashMap();
/*     */   }
/*     */ 
/*     */   private ParameterList(HashMap m) {
/*  57 */     this.list = m;
/*     */   }
/*     */ 
/*     */   public ParameterList(String s)
/*     */     throws ParseException
/*     */   {
/*  71 */     HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");
/*     */ 
/*  76 */     this.list = new HashMap();
/*     */     while (true) {
/*  78 */       HeaderTokenizer.Token tk = h.next();
/*  79 */       int type = tk.getType();
/*     */ 
/*  81 */       if (type == -4) {
/*  82 */         return;
/*     */       }
/*  84 */       if ((char)type != ';')
/*     */         break;
/*  86 */       tk = h.next();
/*     */ 
/*  88 */       if (tk.getType() == -4) {
/*  89 */         return;
/*     */       }
/*  91 */       if (tk.getType() != -1)
/*  92 */         throw new ParseException();
/*  93 */       String name = tk.getValue().toLowerCase();
/*     */ 
/*  96 */       tk = h.next();
/*  97 */       if ((char)tk.getType() != '=') {
/*  98 */         throw new ParseException();
/*     */       }
/*     */ 
/* 101 */       tk = h.next();
/* 102 */       type = tk.getType();
/*     */ 
/* 104 */       if ((type != -1) && (type != -2))
/*     */       {
/* 106 */         throw new ParseException();
/*     */       }
/* 108 */       this.list.put(name, tk.getValue());
/*     */     }
/* 110 */     throw new ParseException();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 120 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   public String get(String name)
/*     */   {
/* 133 */     return (String)this.list.get(name.trim().toLowerCase());
/*     */   }
/*     */ 
/*     */   public void set(String name, String value)
/*     */   {
/* 144 */     this.list.put(name.trim().toLowerCase(), value);
/*     */   }
/*     */ 
/*     */   public void remove(String name)
/*     */   {
/* 154 */     this.list.remove(name.trim().toLowerCase());
/*     */   }
/*     */ 
/*     */   public Iterator getNames()
/*     */   {
/* 164 */     return this.list.keySet().iterator();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 175 */     return toString(0);
/*     */   }
/*     */ 
/*     */   public String toString(int used)
/*     */   {
/* 193 */     StringBuffer sb = new StringBuffer();
/* 194 */     Iterator itr = this.list.entrySet().iterator();
/*     */ 
/* 196 */     while (itr.hasNext()) {
/* 197 */       Map.Entry e = (Map.Entry)itr.next();
/* 198 */       String name = (String)e.getKey();
/* 199 */       String value = quote((String)e.getValue());
/* 200 */       sb.append("; ");
/* 201 */       used += 2;
/* 202 */       int len = name.length() + value.length() + 1;
/* 203 */       if (used + len > 76) {
/* 204 */         sb.append("\r\n\t");
/* 205 */         used = 8;
/*     */       }
/* 207 */       sb.append(name).append('=');
/* 208 */       used += name.length() + 1;
/* 209 */       if (used + value.length() > 76)
/*     */       {
/* 211 */         String s = MimeUtility.fold(used, value);
/* 212 */         sb.append(s);
/* 213 */         int lastlf = s.lastIndexOf('\n');
/* 214 */         if (lastlf >= 0)
/* 215 */           used += s.length() - lastlf - 1;
/*     */         else
/* 217 */           used += s.length();
/*     */       } else {
/* 219 */         sb.append(value);
/* 220 */         used += value.length();
/*     */       }
/*     */     }
/*     */ 
/* 224 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String quote(String value)
/*     */   {
/* 229 */     return MimeUtility.quote(value, "()<>@,;:\\\"\t []/?=");
/*     */   }
/*     */ 
/*     */   public ParameterList copy() {
/* 233 */     return new ParameterList((HashMap)this.list.clone());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParameterList
 * JD-Core Version:    0.6.2
 */