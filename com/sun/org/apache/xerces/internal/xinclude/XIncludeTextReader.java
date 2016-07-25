/*     */ package com.sun.org.apache.xerces.internal.xinclude;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
/*     */ import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
/*     */ import com.sun.org.apache.xerces.internal.util.EncodingMap;
/*     */ import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
/*     */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class XIncludeTextReader
/*     */ {
/*     */   private Reader fReader;
/*     */   private XIncludeHandler fHandler;
/*     */   private XMLInputSource fSource;
/*     */   private XMLErrorReporter fErrorReporter;
/*  72 */   private XMLString fTempString = new XMLString();
/*     */ 
/*     */   public XIncludeTextReader(XMLInputSource source, XIncludeHandler handler, int bufferSize)
/*     */     throws IOException
/*     */   {
/*  83 */     this.fHandler = handler;
/*  84 */     this.fSource = source;
/*  85 */     this.fTempString = new XMLString(new char[bufferSize + 1], 0, 0);
/*     */   }
/*     */ 
/*     */   public void setErrorReporter(XMLErrorReporter errorReporter)
/*     */   {
/*  96 */     this.fErrorReporter = errorReporter;
/*     */   }
/*     */ 
/*     */   protected Reader getReader(XMLInputSource source)
/*     */     throws IOException
/*     */   {
/* 105 */     if (source.getCharacterStream() != null) {
/* 106 */       return source.getCharacterStream();
/*     */     }
/*     */ 
/* 109 */     InputStream stream = null;
/*     */ 
/* 111 */     String encoding = source.getEncoding();
/* 112 */     if (encoding == null) {
/* 113 */       encoding = "UTF-8";
/*     */     }
/* 115 */     if (source.getByteStream() != null) {
/* 116 */       stream = source.getByteStream();
/*     */ 
/* 118 */       if (!(stream instanceof BufferedInputStream))
/* 119 */         stream = new BufferedInputStream(stream, this.fTempString.ch.length);
/*     */     }
/*     */     else
/*     */     {
/* 123 */       String expandedSystemId = XMLEntityManager.expandSystemId(source.getSystemId(), source.getBaseSystemId(), false);
/*     */ 
/* 125 */       URL url = new URL(expandedSystemId);
/* 126 */       URLConnection urlCon = url.openConnection();
/*     */ 
/* 129 */       if (((urlCon instanceof HttpURLConnection)) && ((source instanceof HTTPInputSource))) {
/* 130 */         HttpURLConnection urlConnection = (HttpURLConnection)urlCon;
/* 131 */         HTTPInputSource httpInputSource = (HTTPInputSource)source;
/*     */ 
/* 134 */         Iterator propIter = httpInputSource.getHTTPRequestProperties();
/* 135 */         while (propIter.hasNext()) {
/* 136 */           Map.Entry entry = (Map.Entry)propIter.next();
/* 137 */           urlConnection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
/*     */         }
/*     */ 
/* 141 */         boolean followRedirects = httpInputSource.getFollowHTTPRedirects();
/* 142 */         if (!followRedirects) {
/* 143 */           XMLEntityManager.setInstanceFollowRedirects(urlConnection, followRedirects);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 148 */       stream = new BufferedInputStream(urlCon.getInputStream());
/*     */ 
/* 151 */       String rawContentType = urlCon.getContentType();
/*     */ 
/* 154 */       int index = rawContentType != null ? rawContentType.indexOf(';') : -1;
/*     */ 
/* 156 */       String contentType = null;
/* 157 */       String charset = null;
/* 158 */       if (index != -1)
/*     */       {
/* 160 */         contentType = rawContentType.substring(0, index).trim();
/*     */ 
/* 164 */         charset = rawContentType.substring(index + 1).trim();
/* 165 */         if (charset.startsWith("charset="))
/*     */         {
/* 167 */           charset = charset.substring(8).trim();
/*     */ 
/* 169 */           if (((charset.charAt(0) == '"') && (charset.charAt(charset.length() - 1) == '"')) || ((charset.charAt(0) == '\'') && (charset.charAt(charset.length() - 1) == '\'')))
/*     */           {
/* 174 */             charset = charset.substring(1, charset.length() - 1);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 179 */           charset = null;
/*     */         }
/*     */       }
/*     */       else {
/* 183 */         contentType = rawContentType.trim();
/*     */       }
/*     */ 
/* 186 */       String detectedEncoding = null;
/*     */ 
/* 194 */       if (contentType.equals("text/xml")) {
/* 195 */         if (charset != null) {
/* 196 */           detectedEncoding = charset;
/*     */         }
/*     */         else
/*     */         {
/* 200 */           detectedEncoding = "US-ASCII";
/*     */         }
/*     */       }
/* 203 */       else if (contentType.equals("application/xml")) {
/* 204 */         if (charset != null) {
/* 205 */           detectedEncoding = charset;
/*     */         }
/*     */         else
/*     */         {
/* 209 */           detectedEncoding = getEncodingName(stream);
/*     */         }
/*     */       }
/* 212 */       else if (contentType.endsWith("+xml")) {
/* 213 */         detectedEncoding = getEncodingName(stream);
/*     */       }
/*     */ 
/* 216 */       if (detectedEncoding != null) {
/* 217 */         encoding = detectedEncoding;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 222 */     encoding = encoding.toUpperCase(Locale.ENGLISH);
/*     */ 
/* 225 */     encoding = consumeBOM(stream, encoding);
/*     */ 
/* 231 */     if (encoding.equals("UTF-8")) {
/* 232 */       return new UTF8Reader(stream, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
/*     */     }
/*     */ 
/* 239 */     String javaEncoding = EncodingMap.getIANA2JavaMapping(encoding);
/*     */ 
/* 244 */     if (javaEncoding == null) {
/* 245 */       MessageFormatter aFormatter = this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210");
/*     */ 
/* 247 */       Locale aLocale = this.fErrorReporter.getLocale();
/* 248 */       throw new IOException(aFormatter.formatMessage(aLocale, "EncodingDeclInvalid", new Object[] { encoding }));
/*     */     }
/*     */ 
/* 252 */     if (javaEncoding.equals("ASCII")) {
/* 253 */       return new ASCIIReader(stream, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
/*     */     }
/*     */ 
/* 259 */     return new InputStreamReader(stream, javaEncoding);
/*     */   }
/*     */ 
/*     */   protected String getEncodingName(InputStream stream)
/*     */     throws IOException
/*     */   {
/* 270 */     byte[] b4 = new byte[4];
/* 271 */     String encoding = null;
/*     */ 
/* 275 */     stream.mark(4);
/* 276 */     int count = stream.read(b4, 0, 4);
/* 277 */     stream.reset();
/* 278 */     if (count == 4) {
/* 279 */       encoding = getEncodingName(b4);
/*     */     }
/*     */ 
/* 282 */     return encoding;
/*     */   }
/*     */ 
/*     */   protected String consumeBOM(InputStream stream, String encoding)
/*     */     throws IOException
/*     */   {
/* 296 */     byte[] b = new byte[3];
/* 297 */     int count = 0;
/* 298 */     stream.mark(3);
/* 299 */     if (encoding.equals("UTF-8")) {
/* 300 */       count = stream.read(b, 0, 3);
/* 301 */       if (count == 3) {
/* 302 */         int b0 = b[0] & 0xFF;
/* 303 */         int b1 = b[1] & 0xFF;
/* 304 */         int b2 = b[2] & 0xFF;
/* 305 */         if ((b0 != 239) || (b1 != 187) || (b2 != 191))
/*     */         {
/* 307 */           stream.reset();
/*     */         }
/*     */       }
/*     */       else {
/* 311 */         stream.reset();
/*     */       }
/*     */     }
/* 314 */     else if (encoding.startsWith("UTF-16")) {
/* 315 */       count = stream.read(b, 0, 2);
/* 316 */       if (count == 2) {
/* 317 */         int b0 = b[0] & 0xFF;
/* 318 */         int b1 = b[1] & 0xFF;
/* 319 */         if ((b0 == 254) && (b1 == 255)) {
/* 320 */           return "UTF-16BE";
/*     */         }
/* 322 */         if ((b0 == 255) && (b1 == 254)) {
/* 323 */           return "UTF-16LE";
/*     */         }
/*     */       }
/*     */ 
/* 327 */       stream.reset();
/*     */     }
/*     */ 
/* 333 */     return encoding;
/*     */   }
/*     */ 
/*     */   protected String getEncodingName(byte[] b4)
/*     */   {
/* 350 */     int b0 = b4[0] & 0xFF;
/* 351 */     int b1 = b4[1] & 0xFF;
/* 352 */     if ((b0 == 254) && (b1 == 255))
/*     */     {
/* 354 */       return "UTF-16BE";
/*     */     }
/* 356 */     if ((b0 == 255) && (b1 == 254))
/*     */     {
/* 358 */       return "UTF-16LE";
/*     */     }
/*     */ 
/* 362 */     int b2 = b4[2] & 0xFF;
/* 363 */     if ((b0 == 239) && (b1 == 187) && (b2 == 191)) {
/* 364 */       return "UTF-8";
/*     */     }
/*     */ 
/* 368 */     int b3 = b4[3] & 0xFF;
/* 369 */     if ((b0 == 0) && (b1 == 0) && (b2 == 0) && (b3 == 60))
/*     */     {
/* 371 */       return "ISO-10646-UCS-4";
/*     */     }
/* 373 */     if ((b0 == 60) && (b1 == 0) && (b2 == 0) && (b3 == 0))
/*     */     {
/* 375 */       return "ISO-10646-UCS-4";
/*     */     }
/* 377 */     if ((b0 == 0) && (b1 == 0) && (b2 == 60) && (b3 == 0))
/*     */     {
/* 379 */       return "ISO-10646-UCS-4";
/*     */     }
/* 381 */     if ((b0 == 0) && (b1 == 60) && (b2 == 0) && (b3 == 0))
/*     */     {
/* 383 */       return "ISO-10646-UCS-4";
/*     */     }
/* 385 */     if ((b0 == 0) && (b1 == 60) && (b2 == 0) && (b3 == 63))
/*     */     {
/* 388 */       return "UTF-16BE";
/*     */     }
/* 390 */     if ((b0 == 60) && (b1 == 0) && (b2 == 63) && (b3 == 0))
/*     */     {
/* 393 */       return "UTF-16LE";
/*     */     }
/* 395 */     if ((b0 == 76) && (b1 == 111) && (b2 == 167) && (b3 == 148))
/*     */     {
/* 398 */       return "CP037";
/*     */     }
/*     */ 
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */   public void parse()
/*     */     throws IOException
/*     */   {
/* 415 */     this.fReader = getReader(this.fSource);
/* 416 */     this.fSource = null;
/* 417 */     int readSize = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1);
/* 418 */     while (readSize != -1) {
/* 419 */       for (int i = 0; i < readSize; i++) {
/* 420 */         char ch = this.fTempString.ch[i];
/* 421 */         if (!isValid(ch)) {
/* 422 */           if (XMLChar.isHighSurrogate(ch))
/*     */           {
/* 425 */             i++;
/*     */             int ch2;
/*     */             int ch2;
/* 425 */             if (i < readSize) {
/* 426 */               ch2 = this.fTempString.ch[i];
/*     */             }
/*     */             else
/*     */             {
/* 430 */               ch2 = this.fReader.read();
/* 431 */               if (ch2 != -1) {
/* 432 */                 this.fTempString.ch[(readSize++)] = ((char)ch2);
/*     */               }
/*     */             }
/* 435 */             if (XMLChar.isLowSurrogate(ch2))
/*     */             {
/* 437 */               int sup = XMLChar.supplemental(ch, (char)ch2);
/* 438 */               if (!isValid(sup)) {
/* 439 */                 this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[] { Integer.toString(sup, 16) }, (short)2);
/*     */               }
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 446 */               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[] { Integer.toString(ch2, 16) }, (short)2);
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 453 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[] { Integer.toString(ch, 16) }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 460 */       if ((this.fHandler != null) && (readSize > 0)) {
/* 461 */         this.fTempString.offset = 0;
/* 462 */         this.fTempString.length = readSize;
/* 463 */         this.fHandler.characters(this.fTempString, this.fHandler.modifyAugmentations(null, true));
/*     */       }
/*     */ 
/* 467 */       readSize = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInputSource(XMLInputSource source)
/*     */   {
/* 478 */     this.fSource = source;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 488 */     if (this.fReader != null) {
/* 489 */       this.fReader.close();
/* 490 */       this.fReader = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isValid(int ch)
/*     */   {
/* 501 */     return XMLChar.isValid(ch);
/*     */   }
/*     */ 
/*     */   protected void setBufferSize(int bufferSize)
/*     */   {
/* 511 */     if (this.fTempString.ch.length != ++bufferSize)
/* 512 */       this.fTempString.ch = new char[bufferSize];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xinclude.XIncludeTextReader
 * JD-Core Version:    0.6.2
 */