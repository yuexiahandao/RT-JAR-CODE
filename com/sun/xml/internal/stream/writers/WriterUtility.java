/*     */ package com.sun.xml.internal.stream.writers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ 
/*     */ public class WriterUtility
/*     */ {
/*     */   public static final String START_COMMENT = "<!--";
/*     */   public static final String END_COMMENT = "-->";
/*     */   public static final String DEFAULT_ENCODING = " encoding=\"utf-8\"";
/*     */   public static final String DEFAULT_XMLDECL = "<?xml version=\"1.0\" ?>";
/*     */   public static final String DEFAULT_XML_VERSION = "1.0";
/*     */   public static final char CLOSE_START_TAG = '>';
/*     */   public static final char OPEN_START_TAG = '<';
/*     */   public static final String OPEN_END_TAG = "</";
/*     */   public static final char CLOSE_END_TAG = '>';
/*     */   public static final String START_CDATA = "<![CDATA[";
/*     */   public static final String END_CDATA = "]]>";
/*     */   public static final String CLOSE_EMPTY_ELEMENT = "/>";
/*     */   public static final String SPACE = " ";
/*     */   public static final String UTF_8 = "utf-8";
/*     */   static final boolean DEBUG_XML_CONTENT = false;
/*  70 */   boolean fEscapeCharacters = true;
/*     */ 
/*  73 */   Writer fWriter = null;
/*     */   CharsetEncoder fEncoder;
/*     */ 
/*     */   public WriterUtility()
/*     */   {
/*  79 */     this.fEncoder = getDefaultEncoder();
/*     */   }
/*     */ 
/*     */   public WriterUtility(Writer writer)
/*     */   {
/*  85 */     this.fWriter = writer;
/*  86 */     if ((writer instanceof OutputStreamWriter)) {
/*  87 */       String charset = ((OutputStreamWriter)writer).getEncoding();
/*  88 */       if (charset != null)
/*  89 */         this.fEncoder = Charset.forName(charset).newEncoder();
/*     */     }
/*  91 */     else if ((writer instanceof FileWriter)) {
/*  92 */       String charset = ((FileWriter)writer).getEncoding();
/*  93 */       if (charset != null) {
/*  94 */         this.fEncoder = Charset.forName(charset).newEncoder();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  99 */       this.fEncoder = getDefaultEncoder();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer writer)
/*     */   {
/* 108 */     this.fWriter = writer;
/*     */   }
/*     */ 
/*     */   public void setEscapeCharacters(boolean escape) {
/* 112 */     this.fEscapeCharacters = escape;
/*     */   }
/*     */ 
/*     */   public boolean getEscapeCharacters() {
/* 116 */     return this.fEscapeCharacters;
/*     */   }
/*     */ 
/*     */   public void writeXMLContent(char[] content, int start, int length)
/*     */     throws IOException
/*     */   {
/* 124 */     writeXMLContent(content, start, length, getEscapeCharacters());
/*     */   }
/*     */ 
/*     */   private void writeXMLContent(char[] content, int start, int length, boolean escapeCharacter)
/*     */     throws IOException
/*     */   {
/* 138 */     int end = start + length;
/*     */ 
/* 141 */     int startWritePos = start;
/*     */ 
/* 143 */     for (int index = start; index < end; index++) {
/* 144 */       char ch = content[index];
/*     */ 
/* 146 */       if ((this.fEncoder != null) && (!this.fEncoder.canEncode(ch)))
/*     */       {
/* 148 */         this.fWriter.write(content, startWritePos, index - startWritePos);
/*     */ 
/* 151 */         this.fWriter.write("&#x");
/* 152 */         this.fWriter.write(Integer.toHexString(ch));
/* 153 */         this.fWriter.write(59);
/*     */ 
/* 156 */         startWritePos = index + 1;
/*     */       }
/*     */ 
/* 166 */       switch (ch) {
/*     */       case '<':
/* 168 */         if (escapeCharacter)
/*     */         {
/* 170 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 171 */           this.fWriter.write("&lt;");
/*     */ 
/* 178 */           startWritePos = index + 1; } break;
/*     */       case '&':
/* 183 */         if (escapeCharacter)
/*     */         {
/* 185 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 186 */           this.fWriter.write("&amp;");
/*     */ 
/* 193 */           startWritePos = index + 1; } break;
/*     */       case '>':
/* 199 */         if (escapeCharacter)
/*     */         {
/* 201 */           this.fWriter.write(content, startWritePos, index - startWritePos);
/* 202 */           this.fWriter.write("&gt;");
/*     */ 
/* 209 */           startWritePos = index + 1;
/*     */         }
/*     */ 
/*     */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 219 */     this.fWriter.write(content, startWritePos, end - startWritePos);
/*     */   }
/*     */ 
/*     */   public void writeXMLContent(String content)
/*     */     throws IOException
/*     */   {
/* 227 */     if ((content == null) || (content.length() == 0)) return;
/* 228 */     writeXMLContent(content.toCharArray(), 0, content.length());
/*     */   }
/*     */ 
/*     */   public void writeXMLAttributeValue(String value)
/*     */     throws IOException
/*     */   {
/* 239 */     writeXMLContent(value.toCharArray(), 0, value.length(), true);
/*     */   }
/*     */ 
/*     */   private CharsetEncoder getDefaultEncoder() {
/*     */     try {
/* 244 */       String encoding = SecuritySupport.getSystemProperty("file.encoding");
/* 245 */       if (encoding != null) {
/* 246 */         return Charset.forName(encoding).newEncoder();
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */     }
/* 252 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.writers.WriterUtility
 * JD-Core Version:    0.6.2
 */