/*     */ package com.sun.xml.internal.messaging.saaj.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PushbackReader;
/*     */ import java.io.Writer;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class XMLDeclarationParser
/*     */ {
/*     */   private String m_encoding;
/*     */   private PushbackReader m_pushbackReader;
/*     */   private boolean m_hasHeader;
/*  44 */   private String xmlDecl = null;
/*  45 */   static String gt16 = null;
/*  46 */   static String utf16Decl = null;
/*     */ 
/*     */   public XMLDeclarationParser(PushbackReader pr)
/*     */   {
/*  58 */     this.m_pushbackReader = pr;
/*  59 */     this.m_encoding = "utf-8";
/*  60 */     this.m_hasHeader = false;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/*  66 */     return this.m_encoding;
/*     */   }
/*     */ 
/*     */   public String getXmlDeclaration() {
/*  70 */     return this.xmlDecl;
/*     */   }
/*     */ 
/*     */   public void parse()
/*     */     throws TransformerException, IOException
/*     */   {
/*  77 */     int c = 0;
/*  78 */     int index = 0;
/*  79 */     char[] aChar = new char[65535];
/*  80 */     StringBuffer xmlDeclStr = new StringBuffer();
/*  81 */     while ((c = this.m_pushbackReader.read()) != -1) {
/*  82 */       aChar[index] = ((char)c);
/*  83 */       xmlDeclStr.append((char)c);
/*  84 */       index++;
/*  85 */       if (c == 62) {
/*  86 */         break;
/*     */       }
/*     */     }
/*  89 */     int len = index;
/*     */ 
/*  91 */     String decl = xmlDeclStr.toString();
/*  92 */     boolean utf16 = false;
/*  93 */     boolean utf8 = false;
/*     */ 
/*  95 */     int xmlIndex = decl.indexOf(utf16Decl);
/*  96 */     if (xmlIndex > -1) {
/*  97 */       utf16 = true;
/*     */     } else {
/*  99 */       xmlIndex = decl.indexOf("<?xml");
/* 100 */       if (xmlIndex > -1) {
/* 101 */         utf8 = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 106 */     if ((!utf16) && (!utf8)) {
/* 107 */       this.m_pushbackReader.unread(aChar, 0, len);
/* 108 */       return;
/*     */     }
/* 110 */     this.m_hasHeader = true;
/*     */ 
/* 112 */     if (utf16) {
/* 113 */       this.xmlDecl = new String(decl.getBytes(), "utf-16");
/* 114 */       this.xmlDecl = this.xmlDecl.substring(this.xmlDecl.indexOf("<"));
/*     */     } else {
/* 116 */       this.xmlDecl = decl;
/*     */     }
/*     */ 
/* 119 */     if (xmlIndex != 0) {
/* 120 */       throw new IOException("Unexpected characters before XML declaration");
/*     */     }
/*     */ 
/* 123 */     int versionIndex = this.xmlDecl.indexOf("version");
/* 124 */     if (versionIndex == -1) {
/* 125 */       throw new IOException("Mandatory 'version' attribute Missing in XML declaration");
/*     */     }
/*     */ 
/* 129 */     int encodingIndex = this.xmlDecl.indexOf("encoding");
/* 130 */     if (encodingIndex == -1) {
/* 131 */       return;
/*     */     }
/*     */ 
/* 134 */     if (versionIndex > encodingIndex) {
/* 135 */       throw new IOException("The 'version' attribute should preceed the 'encoding' attribute in an XML Declaration");
/*     */     }
/*     */ 
/* 138 */     int stdAloneIndex = this.xmlDecl.indexOf("standalone");
/* 139 */     if ((stdAloneIndex > -1) && ((stdAloneIndex < versionIndex) || (stdAloneIndex < encodingIndex))) {
/* 140 */       throw new IOException("The 'standalone' attribute should be the last attribute in an XML Declaration");
/*     */     }
/*     */ 
/* 143 */     int eqIndex = this.xmlDecl.indexOf("=", encodingIndex);
/* 144 */     if (eqIndex == -1) {
/* 145 */       throw new IOException("Missing '=' character after 'encoding' in XML declaration");
/*     */     }
/*     */ 
/* 148 */     this.m_encoding = parseEncoding(this.xmlDecl, eqIndex);
/* 149 */     if (this.m_encoding.startsWith("\""))
/* 150 */       this.m_encoding = this.m_encoding.substring(this.m_encoding.indexOf("\"") + 1, this.m_encoding.lastIndexOf("\""));
/* 151 */     else if (this.m_encoding.startsWith("'"))
/* 152 */       this.m_encoding = this.m_encoding.substring(this.m_encoding.indexOf("'") + 1, this.m_encoding.lastIndexOf("'"));
/*     */   }
/*     */ 
/*     */   public void writeTo(Writer wr)
/*     */     throws IOException
/*     */   {
/* 159 */     if (!this.m_hasHeader) return;
/* 160 */     wr.write(this.xmlDecl.toString());
/*     */   }
/*     */ 
/*     */   private String parseEncoding(String xmlDeclFinal, int eqIndex) throws IOException {
/* 164 */     StringTokenizer strTok = new StringTokenizer(xmlDeclFinal.substring(eqIndex + 1));
/*     */ 
/* 166 */     if (strTok.hasMoreTokens()) {
/* 167 */       String encodingTok = strTok.nextToken();
/* 168 */       int indexofQ = encodingTok.indexOf("?");
/* 169 */       if (indexofQ > -1) {
/* 170 */         return encodingTok.substring(0, indexofQ);
/*     */       }
/* 172 */       return encodingTok;
/*     */     }
/*     */ 
/* 175 */     throw new IOException("Error parsing 'encoding' attribute in XML declaration");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  49 */       gt16 = new String(">".getBytes("utf-16"));
/*  50 */       utf16Decl = new String("<?xml".getBytes("utf-16"));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.XMLDeclarationParser
 * JD-Core Version:    0.6.2
 */