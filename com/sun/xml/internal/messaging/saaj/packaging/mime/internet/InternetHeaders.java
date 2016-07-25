/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
/*     */ import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.AbstractList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public final class InternetHeaders
/*     */ {
/*  76 */   private final FinalArrayList headers = new FinalArrayList();
/*     */   private List headerValueView;
/*     */ 
/*     */   public InternetHeaders()
/*     */   {
/*     */   }
/*     */ 
/*     */   public InternetHeaders(InputStream is)
/*     */     throws MessagingException
/*     */   {
/* 101 */     load(is);
/*     */   }
/*     */ 
/*     */   public void load(InputStream is)
/*     */     throws MessagingException
/*     */   {
/* 119 */     LineInputStream lis = new LineInputStream(is);
/* 120 */     String prevline = null;
/*     */ 
/* 122 */     StringBuffer lineBuffer = new StringBuffer();
/*     */     try
/*     */     {
/*     */       String line;
/*     */       do {
/* 127 */         line = lis.readLine();
/* 128 */         if ((line != null) && ((line.startsWith(" ")) || (line.startsWith("\t"))))
/*     */         {
/* 131 */           if (prevline != null) {
/* 132 */             lineBuffer.append(prevline);
/* 133 */             prevline = null;
/*     */           }
/* 135 */           lineBuffer.append("\r\n");
/* 136 */           lineBuffer.append(line);
/*     */         }
/*     */         else {
/* 139 */           if (prevline != null) {
/* 140 */             addHeaderLine(prevline);
/* 141 */           } else if (lineBuffer.length() > 0)
/*     */           {
/* 143 */             addHeaderLine(lineBuffer.toString());
/* 144 */             lineBuffer.setLength(0);
/*     */           }
/* 146 */           prevline = line;
/*     */         }
/* 148 */         if (line == null) break;  } while (line.length() > 0);
/*     */     } catch (IOException ioex) {
/* 150 */       throw new MessagingException("Error in input stream", ioex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] getHeader(String name)
/*     */   {
/* 164 */     FinalArrayList v = new FinalArrayList();
/*     */ 
/* 166 */     int len = this.headers.size();
/* 167 */     for (int i = 0; i < len; i++) {
/* 168 */       hdr h = (hdr)this.headers.get(i);
/* 169 */       if (name.equalsIgnoreCase(h.name)) {
/* 170 */         v.add(h.getValue());
/*     */       }
/*     */     }
/* 173 */     if (v.size() == 0) {
/* 174 */       return null;
/*     */     }
/* 176 */     return (String[])v.toArray(new String[v.size()]);
/*     */   }
/*     */ 
/*     */   public String getHeader(String name, String delimiter)
/*     */   {
/* 192 */     String[] s = getHeader(name);
/*     */ 
/* 194 */     if (s == null) {
/* 195 */       return null;
/*     */     }
/* 197 */     if ((s.length == 1) || (delimiter == null)) {
/* 198 */       return s[0];
/*     */     }
/* 200 */     StringBuffer r = new StringBuffer(s[0]);
/* 201 */     for (int i = 1; i < s.length; i++) {
/* 202 */       r.append(delimiter);
/* 203 */       r.append(s[i]);
/*     */     }
/* 205 */     return r.toString();
/*     */   }
/*     */ 
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 219 */     boolean found = false;
/*     */ 
/* 221 */     for (int i = 0; i < this.headers.size(); i++) {
/* 222 */       hdr h = (hdr)this.headers.get(i);
/* 223 */       if (name.equalsIgnoreCase(h.name)) {
/* 224 */         if (!found)
/*     */         {
/*     */           int j;
/* 226 */           if ((h.line != null) && ((j = h.line.indexOf(':')) >= 0))
/* 227 */             h.line = (h.line.substring(0, j + 1) + " " + value);
/*     */           else {
/* 229 */             h.line = (name + ": " + value);
/*     */           }
/* 231 */           found = true;
/*     */         } else {
/* 233 */           this.headers.remove(i);
/* 234 */           i--;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 239 */     if (!found)
/* 240 */       addHeader(name, value);
/*     */   }
/*     */ 
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 253 */     int pos = this.headers.size();
/* 254 */     for (int i = this.headers.size() - 1; i >= 0; i--) {
/* 255 */       hdr h = (hdr)this.headers.get(i);
/* 256 */       if (name.equalsIgnoreCase(h.name)) {
/* 257 */         this.headers.add(i + 1, new hdr(name, value));
/* 258 */         return;
/*     */       }
/*     */ 
/* 261 */       if (h.name.equals(":"))
/* 262 */         pos = i;
/*     */     }
/* 264 */     this.headers.add(pos, new hdr(name, value));
/*     */   }
/*     */ 
/*     */   public void removeHeader(String name)
/*     */   {
/* 273 */     for (int i = 0; i < this.headers.size(); i++) {
/* 274 */       hdr h = (hdr)this.headers.get(i);
/* 275 */       if (name.equalsIgnoreCase(h.name)) {
/* 276 */         this.headers.remove(i);
/* 277 */         i--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public FinalArrayList getAllHeaders()
/*     */   {
/* 289 */     return this.headers;
/*     */   }
/*     */ 
/*     */   public void addHeaderLine(String line)
/*     */   {
/*     */     try
/*     */     {
/* 303 */       char c = line.charAt(0);
/* 304 */       if ((c == ' ') || (c == '\t')) {
/* 305 */         hdr h = (hdr)this.headers.get(this.headers.size() - 1);
/*     */         hdr tmp46_45 = h; tmp46_45.line = (tmp46_45.line + "\r\n" + line);
/*     */       } else {
/* 308 */         this.headers.add(new hdr(line));
/*     */       }
/*     */     }
/*     */     catch (StringIndexOutOfBoundsException e)
/*     */     {
/*     */     }
/*     */     catch (NoSuchElementException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public List getAllHeaderLines()
/*     */   {
/* 321 */     if (this.headerValueView == null)
/* 322 */       this.headerValueView = new AbstractList() {
/*     */         public Object get(int index) {
/* 324 */           return ((hdr)InternetHeaders.this.headers.get(index)).line;
/*     */         }
/*     */ 
/*     */         public int size() {
/* 328 */           return InternetHeaders.this.headers.size();
/*     */         }
/*     */       };
/* 331 */     return this.headerValueView;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.InternetHeaders
 * JD-Core Version:    0.6.2
 */