/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ 
/*     */ class WriterToASCI extends Writer
/*     */   implements WriterChain
/*     */ {
/*     */   private final OutputStream m_os;
/*     */ 
/*     */   public WriterToASCI(OutputStream os)
/*     */   {
/*  55 */     this.m_os = os;
/*     */   }
/*     */ 
/*     */   public void write(char[] chars, int start, int length)
/*     */     throws IOException
/*     */   {
/*  73 */     int n = length + start;
/*     */ 
/*  75 */     for (int i = start; i < n; i++)
/*     */     {
/*  77 */       this.m_os.write(chars[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/*  94 */     this.m_os.write(c);
/*     */   }
/*     */ 
/*     */   public void write(String s)
/*     */     throws IOException
/*     */   {
/* 106 */     int n = s.length();
/* 107 */     for (int i = 0; i < n; i++)
/*     */     {
/* 109 */       this.m_os.write(s.charAt(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 124 */     this.m_os.flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 136 */     this.m_os.close();
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 147 */     return this.m_os;
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/* 155 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.WriterToASCI
 * JD-Core Version:    0.6.2
 */