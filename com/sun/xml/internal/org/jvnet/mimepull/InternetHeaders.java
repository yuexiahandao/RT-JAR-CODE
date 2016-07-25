/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ final class InternetHeaders
/*     */ {
/*  62 */   private final FinalArrayList<hdr> headers = new FinalArrayList();
/*     */ 
/*     */   InternetHeaders(MIMEParser.LineInputStream lis)
/*     */   {
/*  79 */     String prevline = null;
/*     */ 
/*  81 */     StringBuffer lineBuffer = new StringBuffer();
/*     */     try
/*     */     {
/*     */       String line;
/*     */       do {
/*  86 */         line = lis.readLine();
/*  87 */         if ((line != null) && ((line.startsWith(" ")) || (line.startsWith("\t"))))
/*     */         {
/*  90 */           if (prevline != null) {
/*  91 */             lineBuffer.append(prevline);
/*  92 */             prevline = null;
/*     */           }
/*  94 */           lineBuffer.append("\r\n");
/*  95 */           lineBuffer.append(line);
/*     */         }
/*     */         else {
/*  98 */           if (prevline != null) {
/*  99 */             addHeaderLine(prevline);
/* 100 */           } else if (lineBuffer.length() > 0)
/*     */           {
/* 102 */             addHeaderLine(lineBuffer.toString());
/* 103 */             lineBuffer.setLength(0);
/*     */           }
/* 105 */           prevline = line;
/*     */         }
/* 107 */         if (line == null) break;  } while (line.length() > 0);
/*     */     } catch (IOException ioex) {
/* 109 */       throw new MIMEParsingException("Error in input stream", ioex);
/*     */     }
/*     */   }
/*     */ 
/*     */   List<String> getHeader(String name)
/*     */   {
/* 123 */     FinalArrayList v = new FinalArrayList();
/*     */ 
/* 125 */     int len = this.headers.size();
/* 126 */     for (int i = 0; i < len; i++) {
/* 127 */       hdr h = (hdr)this.headers.get(i);
/* 128 */       if (name.equalsIgnoreCase(h.name)) {
/* 129 */         v.add(h.getValue());
/*     */       }
/*     */     }
/* 132 */     return v.size() == 0 ? null : v;
/*     */   }
/*     */ 
/*     */   FinalArrayList<? extends Header> getAllHeaders()
/*     */   {
/* 142 */     return this.headers;
/*     */   }
/*     */ 
/*     */   void addHeaderLine(String line)
/*     */   {
/*     */     try
/*     */     {
/* 156 */       char c = line.charAt(0);
/* 157 */       if ((c == ' ') || (c == '\t')) {
/* 158 */         hdr h = (hdr)this.headers.get(this.headers.size() - 1);
/*     */         hdr tmp46_45 = h; tmp46_45.line = (tmp46_45.line + "\r\n" + line);
/*     */       } else {
/* 161 */         this.headers.add(new hdr(line));
/*     */       }
/*     */     }
/*     */     catch (StringIndexOutOfBoundsException e)
/*     */     {
/*     */     }
/*     */     catch (NoSuchElementException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.InternetHeaders
 * JD-Core Version:    0.6.2
 */