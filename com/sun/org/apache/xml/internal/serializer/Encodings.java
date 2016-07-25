/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public final class Encodings
/*     */ {
/*     */   private static final int m_defaultLastPrintable = 127;
/*     */   private static final String ENCODINGS_FILE = "com/sun/org/apache/xml/internal/serializer/Encodings.properties";
/*     */   private static final String ENCODINGS_PROP = "com.sun.org.apache.xalan.internal.serialize.encodings";
/*     */   static final String DEFAULT_MIME_ENCODING = "UTF-8";
/* 569 */   private static final EncodingInfos _encodingInfos = new EncodingInfos(null);
/*     */ 
/*     */   static Writer getWriter(OutputStream output, String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  90 */     EncodingInfo ei = _encodingInfos.findEncoding(toUpperCaseFast(encoding));
/*  91 */     if (ei != null) {
/*     */       try {
/*  93 */         return new BufferedWriter(new OutputStreamWriter(output, ei.javaName));
/*     */       }
/*     */       catch (UnsupportedEncodingException usee)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 100 */     return new BufferedWriter(new OutputStreamWriter(output, encoding));
/*     */   }
/*     */ 
/*     */   public static int getLastPrintable()
/*     */   {
/* 112 */     return 127;
/*     */   }
/*     */ 
/*     */   static EncodingInfo getEncodingInfo(String encoding)
/*     */   {
/* 132 */     String normalizedEncoding = toUpperCaseFast(encoding);
/* 133 */     EncodingInfo ei = _encodingInfos.findEncoding(normalizedEncoding);
/* 134 */     if (ei == null)
/*     */     {
/*     */       try
/*     */       {
/* 144 */         Charset c = Charset.forName(encoding);
/* 145 */         String name = c.name();
/* 146 */         ei = new EncodingInfo(name, name);
/* 147 */         _encodingInfos.putEncoding(normalizedEncoding, ei);
/*     */       } catch (IllegalCharsetNameException|UnsupportedCharsetException x) {
/* 149 */         ei = new EncodingInfo(null, null);
/*     */       }
/*     */     }
/*     */ 
/* 153 */     return ei;
/*     */   }
/*     */ 
/*     */   private static String toUpperCaseFast(String s)
/*     */   {
/* 168 */     boolean different = false;
/* 169 */     int mx = s.length();
/* 170 */     char[] chars = new char[mx];
/* 171 */     for (int i = 0; i < mx; i++) {
/* 172 */       char ch = s.charAt(i);
/*     */ 
/* 174 */       if (('a' <= ch) && (ch <= 'z'))
/*     */       {
/* 176 */         ch = (char)(ch + '￠');
/* 177 */         different = true;
/*     */       }
/* 179 */       chars[i] = ch;
/*     */     }
/*     */     String upper;
/*     */     String upper;
/* 185 */     if (different)
/* 186 */       upper = String.valueOf(chars);
/*     */     else {
/* 188 */       upper = s;
/*     */     }
/* 190 */     return upper;
/*     */   }
/*     */ 
/*     */   static String getMimeEncoding(String encoding)
/*     */   {
/* 215 */     if (null == encoding)
/*     */     {
/*     */       try
/*     */       {
/* 223 */         encoding = SecuritySupport.getSystemProperty("file.encoding", "UTF8");
/*     */ 
/* 225 */         if (null != encoding)
/*     */         {
/* 235 */           String jencoding = (encoding.equalsIgnoreCase("Cp1252")) || (encoding.equalsIgnoreCase("ISO8859_1")) || (encoding.equalsIgnoreCase("8859_1")) || (encoding.equalsIgnoreCase("UTF8")) ? "UTF-8" : convertJava2MimeEncoding(encoding);
/*     */ 
/* 243 */           encoding = null != jencoding ? jencoding : "UTF-8";
/*     */         }
/*     */         else
/*     */         {
/* 248 */           encoding = "UTF-8";
/*     */         }
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/* 253 */         encoding = "UTF-8";
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 258 */       encoding = convertJava2MimeEncoding(encoding);
/*     */     }
/*     */ 
/* 261 */     return encoding;
/*     */   }
/*     */ 
/*     */   private static String convertJava2MimeEncoding(String encoding)
/*     */   {
/* 273 */     EncodingInfo enc = _encodingInfos.getEncodingFromJavaKey(toUpperCaseFast(encoding));
/*     */ 
/* 275 */     if (null != enc)
/* 276 */       return enc.name;
/* 277 */     return encoding;
/*     */   }
/*     */ 
/*     */   public static String convertMime2JavaEncoding(String encoding)
/*     */   {
/* 289 */     EncodingInfo info = _encodingInfos.findEncoding(toUpperCaseFast(encoding));
/* 290 */     return info != null ? info.javaName : encoding;
/*     */   }
/*     */ 
/*     */   static boolean isHighUTF16Surrogate(char ch)
/*     */   {
/* 528 */     return (55296 <= ch) && (ch <= 56319);
/*     */   }
/*     */ 
/*     */   static boolean isLowUTF16Surrogate(char ch)
/*     */   {
/* 538 */     return (56320 <= ch) && (ch <= 57343);
/*     */   }
/*     */ 
/*     */   static int toCodePoint(char highSurrogate, char lowSurrogate)
/*     */   {
/* 549 */     int codePoint = (highSurrogate - 55296 << 10) + (lowSurrogate - 56320) + 65536;
/*     */ 
/* 553 */     return codePoint;
/*     */   }
/*     */ 
/*     */   static int toCodePoint(char ch)
/*     */   {
/* 565 */     int codePoint = ch;
/* 566 */     return codePoint;
/*     */   }
/*     */ 
/*     */   private static final class EncodingInfos
/*     */   {
/* 298 */     private final Map<String, EncodingInfo> _encodingTableKeyJava = new HashMap();
/* 299 */     private final Map<String, EncodingInfo> _encodingTableKeyMime = new HashMap();
/*     */ 
/* 304 */     private final Map<String, EncodingInfo> _encodingDynamicTable = Collections.synchronizedMap(new HashMap());
/*     */ 
/*     */     private EncodingInfos()
/*     */     {
/* 308 */       loadEncodingInfo();
/*     */     }
/*     */ 
/*     */     private InputStream openEncodingsFileStream()
/*     */       throws MalformedURLException, IOException
/*     */     {
/* 314 */       String urlString = null;
/* 315 */       InputStream is = null;
/*     */       try
/*     */       {
/* 318 */         urlString = SecuritySupport.getSystemProperty("com.sun.org.apache.xalan.internal.serialize.encodings", "");
/*     */       }
/*     */       catch (SecurityException e) {
/*     */       }
/* 322 */       if ((urlString != null) && (urlString.length() > 0)) {
/* 323 */         URL url = new URL(urlString);
/* 324 */         is = url.openStream();
/*     */       }
/*     */ 
/* 327 */       if (is == null) {
/* 328 */         is = SecuritySupport.getResourceAsStream("com/sun/org/apache/xml/internal/serializer/Encodings.properties");
/*     */       }
/* 330 */       return is;
/*     */     }
/*     */ 
/*     */     private Properties loadProperties()
/*     */       throws MalformedURLException, IOException
/*     */     {
/* 337 */       Properties props = new Properties();
/* 338 */       InputStream is = openEncodingsFileStream();
/*     */       try {
/* 340 */         if (is != null) {
/* 341 */           props.load(is);
/*     */         }
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/* 351 */         if (is != null) {
/* 352 */           is.close();
/*     */         }
/*     */       }
/* 355 */       return props;
/*     */     }
/*     */ 
/*     */     private String[] parseMimeTypes(String val)
/*     */     {
/* 362 */       int pos = val.indexOf(' ');
/*     */ 
/* 364 */       if (pos < 0)
/*     */       {
/* 368 */         return new String[] { val };
/*     */       }
/*     */ 
/* 373 */       StringTokenizer st = new StringTokenizer(val.substring(0, pos), ",");
/*     */ 
/* 375 */       String[] values = new String[st.countTokens()];
/* 376 */       for (int i = 0; st.hasMoreTokens(); i++) {
/* 377 */         values[i] = st.nextToken();
/*     */       }
/* 379 */       return values;
/*     */     }
/*     */ 
/*     */     private String findCharsetNameFor(String name)
/*     */     {
/*     */       try
/*     */       {
/* 391 */         return Charset.forName(name).name(); } catch (Exception x) {
/*     */       }
/* 393 */       return null;
/*     */     }
/*     */ 
/*     */     private String findCharsetNameFor(String javaName, String[] mimes)
/*     */     {
/* 427 */       String cs = findCharsetNameFor(javaName);
/* 428 */       if (cs != null) return javaName;
/* 429 */       for (String m : mimes) {
/* 430 */         cs = findCharsetNameFor(m);
/* 431 */         if (cs != null) break;
/*     */       }
/* 433 */       return cs;
/*     */     }
/*     */ 
/*     */     private void loadEncodingInfo()
/*     */     {
/*     */       try
/*     */       {
/* 446 */         Properties props = loadProperties();
/*     */ 
/* 449 */         Enumeration keys = props.keys();
/* 450 */         canonicals = new HashMap();
/* 451 */         while (keys.hasMoreElements()) {
/* 452 */           String javaName = (String)keys.nextElement();
/* 453 */           String[] mimes = parseMimeTypes(props.getProperty(javaName));
/*     */ 
/* 455 */           String charsetName = findCharsetNameFor(javaName, mimes);
/* 456 */           if (charsetName != null) {
/* 457 */             String kj = Encodings.toUpperCaseFast(javaName);
/* 458 */             String kc = Encodings.toUpperCaseFast(charsetName);
/* 459 */             for (int i = 0; i < mimes.length; i++) {
/* 460 */               String mimeName = mimes[i];
/* 461 */               String km = Encodings.toUpperCaseFast(mimeName);
/* 462 */               EncodingInfo info = new EncodingInfo(mimeName, charsetName);
/* 463 */               this._encodingTableKeyMime.put(km, info);
/* 464 */               if (!canonicals.containsKey(kc))
/*     */               {
/* 469 */                 canonicals.put(kc, info);
/* 470 */                 this._encodingTableKeyJava.put(kc, info);
/*     */               }
/* 472 */               this._encodingTableKeyJava.put(kj, info);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 485 */         for (Map.Entry e : this._encodingTableKeyJava.entrySet())
/* 486 */           e.setValue(canonicals.get(Encodings.toUpperCaseFast(((EncodingInfo)e.getValue()).javaName)));
/*     */       }
/*     */       catch (MalformedURLException mue)
/*     */       {
/*     */         Map canonicals;
/* 490 */         throw new WrappedRuntimeException(mue);
/*     */       } catch (IOException ioe) {
/* 492 */         throw new WrappedRuntimeException(ioe);
/*     */       }
/*     */     }
/*     */ 
/*     */     EncodingInfo findEncoding(String normalizedEncoding) {
/* 497 */       EncodingInfo info = (EncodingInfo)this._encodingTableKeyJava.get(normalizedEncoding);
/* 498 */       if (info == null) {
/* 499 */         info = (EncodingInfo)this._encodingTableKeyMime.get(normalizedEncoding);
/*     */       }
/* 501 */       if (info == null) {
/* 502 */         info = (EncodingInfo)this._encodingDynamicTable.get(normalizedEncoding);
/*     */       }
/* 504 */       return info;
/*     */     }
/*     */ 
/*     */     EncodingInfo getEncodingFromMimeKey(String normalizedMimeName) {
/* 508 */       return (EncodingInfo)this._encodingTableKeyMime.get(normalizedMimeName);
/*     */     }
/*     */ 
/*     */     EncodingInfo getEncodingFromJavaKey(String normalizedJavaName) {
/* 512 */       return (EncodingInfo)this._encodingTableKeyJava.get(normalizedJavaName);
/*     */     }
/*     */ 
/*     */     void putEncoding(String key, EncodingInfo info) {
/* 516 */       this._encodingDynamicTable.put(key, info);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.Encodings
 * JD-Core Version:    0.6.2
 */