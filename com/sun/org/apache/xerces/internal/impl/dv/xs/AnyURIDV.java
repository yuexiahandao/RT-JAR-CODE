/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import com.sun.org.apache.xerces.internal.util.URI;
/*     */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ public class AnyURIDV extends TypeValidator
/*     */ {
/*     */   private static final URI BASE_URI;
/*     */   private static boolean[] gNeedEscaping;
/*     */   private static char[] gAfterEscaping1;
/*     */   private static char[] gAfterEscaping2;
/*     */   private static char[] gHexChs;
/*     */ 
/*     */   public short getAllowedFacets()
/*     */   {
/*  49 */     return 2079;
/*     */   }
/*     */ 
/*     */   public Object getActualValue(String content, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*     */     try
/*     */     {
/*  57 */       if (content.length() != 0)
/*     */       {
/*  59 */         String encoded = encode(content);
/*     */ 
/*  63 */         new URI(BASE_URI, encoded);
/*     */       }
/*     */     } catch (URI.MalformedURIException ex) {
/*  66 */       throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "anyURI" });
/*     */     }
/*     */ 
/*  70 */     return content;
/*     */   }
/*     */ 
/*     */   private static String encode(String anyURI)
/*     */   {
/* 107 */     int len = anyURI.length();
/* 108 */     StringBuffer buffer = new StringBuffer(len * 3);
/*     */ 
/* 111 */     for (int i = 0; 
/* 112 */       i < len; i++) {
/* 113 */       int ch = anyURI.charAt(i);
/*     */ 
/* 115 */       if (ch >= 128)
/*     */         break;
/* 117 */       if (gNeedEscaping[ch] != 0) {
/* 118 */         buffer.append('%');
/* 119 */         buffer.append(gAfterEscaping1[ch]);
/* 120 */         buffer.append(gAfterEscaping2[ch]);
/*     */       }
/*     */       else {
/* 123 */         buffer.append((char)ch);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 128 */     if (i < len)
/*     */     {
/* 130 */       byte[] bytes = null;
/*     */       try
/*     */       {
/* 133 */         bytes = anyURI.substring(i).getBytes("UTF-8");
/*     */       }
/*     */       catch (UnsupportedEncodingException e) {
/* 136 */         return anyURI;
/*     */       }
/* 138 */       len = bytes.length;
/*     */ 
/* 141 */       for (i = 0; i < len; i++) {
/* 142 */         byte b = bytes[i];
/*     */ 
/* 144 */         if (b < 0) {
/* 145 */           int ch = b + 256;
/* 146 */           buffer.append('%');
/* 147 */           buffer.append(gHexChs[(ch >> 4)]);
/* 148 */           buffer.append(gHexChs[(ch & 0xF)]);
/*     */         }
/* 150 */         else if (gNeedEscaping[b] != 0) {
/* 151 */           buffer.append('%');
/* 152 */           buffer.append(gAfterEscaping1[b]);
/* 153 */           buffer.append(gAfterEscaping2[b]);
/*     */         }
/*     */         else {
/* 156 */           buffer.append((char)b);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 163 */     if (buffer.length() != len) {
/* 164 */       return buffer.toString();
/*     */     }
/*     */ 
/* 167 */     return anyURI;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  40 */     URI uri = null;
/*     */     try {
/*  42 */       uri = new URI("abc://def.ghi.jkl");
/*     */     } catch (URI.MalformedURIException ex) {
/*     */     }
/*  45 */     BASE_URI = uri;
/*     */ 
/*  74 */     gNeedEscaping = new boolean[''];
/*     */ 
/*  76 */     gAfterEscaping1 = new char[''];
/*     */ 
/*  78 */     gAfterEscaping2 = new char[''];
/*  79 */     gHexChs = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*  83 */     for (int i = 0; i <= 31; i++) {
/*  84 */       gNeedEscaping[i] = true;
/*  85 */       gAfterEscaping1[i] = gHexChs[(i >> 4)];
/*  86 */       gAfterEscaping2[i] = gHexChs[(i & 0xF)];
/*     */     }
/*  88 */     gNeedEscaping[127] = true;
/*  89 */     gAfterEscaping1[127] = '7';
/*  90 */     gAfterEscaping2[127] = 'F';
/*  91 */     char[] escChs = { ' ', '<', '>', '"', '{', '}', '|', '\\', '^', '~', '`' };
/*     */ 
/*  93 */     int len = escChs.length;
/*     */ 
/*  95 */     for (int i = 0; i < len; i++) {
/*  96 */       char ch = escChs[i];
/*  97 */       gNeedEscaping[ch] = true;
/*  98 */       gAfterEscaping1[ch] = gHexChs[(ch >> '\004')];
/*  99 */       gAfterEscaping2[ch] = gHexChs[(ch & 0xF)];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV
 * JD-Core Version:    0.6.2
 */