/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class XML11DTDScannerImpl extends XMLDTDScannerImpl
/*     */ {
/* 101 */   private String[] fStrings = new String[3];
/*     */ 
/* 104 */   private XMLString fString = new XMLString();
/*     */ 
/* 107 */   private XMLStringBuffer fStringBuffer = new XMLStringBuffer();
/*     */ 
/* 110 */   private XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
/* 111 */   private XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
/*     */ 
/*     */   public XML11DTDScannerImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XML11DTDScannerImpl(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager)
/*     */   {
/* 123 */     super(symbolTable, errorReporter, entityManager);
/*     */   }
/*     */ 
/*     */   protected boolean scanPubidLiteral(XMLString literal)
/*     */     throws IOException, XNIException
/*     */   {
/* 159 */     int quote = this.fEntityScanner.scanChar();
/* 160 */     if ((quote != 39) && (quote != 34)) {
/* 161 */       reportFatalError("QuoteRequiredInPublicID", null);
/* 162 */       return false;
/*     */     }
/*     */ 
/* 165 */     this.fStringBuffer.clear();
/*     */ 
/* 167 */     boolean skipSpace = true;
/* 168 */     boolean dataok = true;
/*     */     while (true) {
/* 170 */       int c = this.fEntityScanner.scanChar();
/*     */ 
/* 172 */       if ((c == 32) || (c == 10) || (c == 13) || (c == 133) || (c == 8232)) {
/* 173 */         if (!skipSpace)
/*     */         {
/* 175 */           this.fStringBuffer.append(' ');
/* 176 */           skipSpace = true;
/*     */         }
/*     */       } else {
/* 179 */         if (c == quote) {
/* 180 */           if (skipSpace)
/*     */           {
/* 182 */             this.fStringBuffer.length -= 1;
/*     */           }
/* 184 */           literal.setValues(this.fStringBuffer);
/* 185 */           break;
/*     */         }
/* 187 */         if (XMLChar.isPubid(c)) {
/* 188 */           this.fStringBuffer.append((char)c);
/* 189 */           skipSpace = false;
/*     */         } else {
/* 191 */           if (c == -1) {
/* 192 */             reportFatalError("PublicIDUnterminated", null);
/* 193 */             return false;
/*     */           }
/*     */ 
/* 196 */           dataok = false;
/* 197 */           reportFatalError("InvalidCharInPublicID", new Object[] { Integer.toHexString(c) });
/*     */         }
/*     */       }
/*     */     }
/* 201 */     return dataok;
/*     */   }
/*     */ 
/*     */   protected void normalizeWhitespace(XMLString value)
/*     */   {
/* 209 */     int end = value.offset + value.length;
/* 210 */     for (int i = value.offset; i < end; i++) {
/* 211 */       int c = value.ch[i];
/* 212 */       if (XMLChar.isSpace(c))
/* 213 */         value.ch[i] = ' ';
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void normalizeWhitespace(XMLString value, int fromIndex)
/*     */   {
/* 223 */     int end = value.offset + value.length;
/* 224 */     for (int i = value.offset + fromIndex; i < end; i++) {
/* 225 */       int c = value.ch[i];
/* 226 */       if (XMLChar.isSpace(c))
/* 227 */         value.ch[i] = ' ';
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int isUnchangedByNormalization(XMLString value)
/*     */   {
/* 240 */     int end = value.offset + value.length;
/* 241 */     for (int i = value.offset; i < end; i++) {
/* 242 */       int c = value.ch[i];
/* 243 */       if (XMLChar.isSpace(c)) {
/* 244 */         return i - value.offset;
/*     */       }
/*     */     }
/* 247 */     return -1;
/*     */   }
/*     */ 
/*     */   protected boolean isInvalid(int value)
/*     */   {
/* 254 */     return !XML11Char.isXML11Valid(value);
/*     */   }
/*     */ 
/*     */   protected boolean isInvalidLiteral(int value)
/*     */   {
/* 261 */     return !XML11Char.isXML11ValidLiteral(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNameChar(int value)
/*     */   {
/* 268 */     return XML11Char.isXML11Name(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNameStartChar(int value)
/*     */   {
/* 275 */     return XML11Char.isXML11NameStart(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNCName(int value)
/*     */   {
/* 282 */     return XML11Char.isXML11NCName(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNameStartHighSurrogate(int value)
/*     */   {
/* 290 */     return XML11Char.isXML11NameHighSurrogate(value);
/*     */   }
/*     */ 
/*     */   protected boolean versionSupported(String version)
/*     */   {
/* 297 */     return (version.equals("1.1")) || (version.equals("1.0"));
/*     */   }
/*     */ 
/*     */   protected String getVersionNotSupportedKey()
/*     */   {
/* 304 */     return "VersionNotSupported11";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XML11DTDScannerImpl
 * JD-Core Version:    0.6.2
 */