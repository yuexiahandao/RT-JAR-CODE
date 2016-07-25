/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.EncodingMap;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class EncodingInfo
/*     */ {
/*  37 */   private Object[] fArgsForMethod = null;
/*     */   String ianaName;
/*     */   String javaName;
/*     */   int lastPrintable;
/*  46 */   Object fCharsetEncoder = null;
/*     */ 
/*  49 */   Object fCharToByteConverter = null;
/*     */ 
/*  54 */   boolean fHaveTriedCToB = false;
/*     */ 
/*  57 */   boolean fHaveTriedCharsetEncoder = false;
/*     */ 
/*     */   public EncodingInfo(String ianaName, String javaName, int lastPrintable)
/*     */   {
/*  63 */     this.ianaName = ianaName;
/*  64 */     this.javaName = EncodingMap.getIANA2JavaMapping(ianaName);
/*  65 */     this.lastPrintable = lastPrintable;
/*     */   }
/*     */ 
/*     */   public String getIANAName()
/*     */   {
/*  72 */     return this.ianaName;
/*     */   }
/*     */ 
/*     */   public Writer getWriter(OutputStream output)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  86 */     if (this.javaName != null)
/*  87 */       return new OutputStreamWriter(output, this.javaName);
/*  88 */     this.javaName = EncodingMap.getIANA2JavaMapping(this.ianaName);
/*  89 */     if (this.javaName == null)
/*     */     {
/*  91 */       return new OutputStreamWriter(output, "UTF8");
/*  92 */     }return new OutputStreamWriter(output, this.javaName);
/*     */   }
/*     */ 
/*     */   public boolean isPrintable(char ch)
/*     */   {
/* 101 */     if (ch <= this.lastPrintable) {
/* 102 */       return true;
/*     */     }
/* 104 */     return isPrintable0(ch);
/*     */   }
/*     */ 
/*     */   private boolean isPrintable0(char ch)
/*     */   {
/* 117 */     if ((this.fCharsetEncoder == null) && (CharsetMethods.fgNIOCharsetAvailable) && (!this.fHaveTriedCharsetEncoder)) {
/* 118 */       if (this.fArgsForMethod == null) {
/* 119 */         this.fArgsForMethod = new Object[1];
/*     */       }
/*     */       try
/*     */       {
/* 123 */         this.fArgsForMethod[0] = this.javaName;
/* 124 */         Object charset = CharsetMethods.fgCharsetForNameMethod.invoke(null, this.fArgsForMethod);
/* 125 */         if (((Boolean)CharsetMethods.fgCharsetCanEncodeMethod.invoke(charset, (Object[])null)).booleanValue()) {
/* 126 */           this.fCharsetEncoder = CharsetMethods.fgCharsetNewEncoderMethod.invoke(charset, (Object[])null);
/*     */         }
/*     */         else
/*     */         {
/* 130 */           this.fHaveTriedCharsetEncoder = true;
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 135 */         this.fHaveTriedCharsetEncoder = true;
/*     */       }
/*     */     }
/*     */ 
/* 139 */     if (this.fCharsetEncoder != null) {
/*     */       try {
/* 141 */         this.fArgsForMethod[0] = new Character(ch);
/* 142 */         return ((Boolean)CharsetMethods.fgCharsetEncoderCanEncodeMethod.invoke(this.fCharsetEncoder, this.fArgsForMethod)).booleanValue();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 146 */         this.fCharsetEncoder = null;
/* 147 */         this.fHaveTriedCharsetEncoder = false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 154 */     if (this.fCharToByteConverter == null) {
/* 155 */       if ((this.fHaveTriedCToB) || (!CharToByteConverterMethods.fgConvertersAvailable))
/*     */       {
/* 157 */         return false;
/*     */       }
/* 159 */       if (this.fArgsForMethod == null) {
/* 160 */         this.fArgsForMethod = new Object[1];
/*     */       }
/*     */       try
/*     */       {
/* 164 */         this.fArgsForMethod[0] = this.javaName;
/* 165 */         this.fCharToByteConverter = CharToByteConverterMethods.fgGetConverterMethod.invoke(null, this.fArgsForMethod);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 169 */         this.fHaveTriedCToB = true;
/* 170 */         return false;
/*     */       }
/*     */     }
/*     */     try {
/* 174 */       this.fArgsForMethod[0] = new Character(ch);
/* 175 */       return ((Boolean)CharToByteConverterMethods.fgCanConvertMethod.invoke(this.fCharToByteConverter, this.fArgsForMethod)).booleanValue();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 180 */       this.fCharToByteConverter = null;
/* 181 */       this.fHaveTriedCToB = false;
/* 182 */     }return false;
/*     */   }
/*     */ 
/*     */   public static void testJavaEncodingName(String name)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 189 */     byte[] bTest = { 118, 97, 108, 105, 100 };
/* 190 */     String s = new String(bTest, name);
/*     */   }
/*     */ 
/*     */   static class CharToByteConverterMethods
/*     */   {
/* 244 */     private static Method fgGetConverterMethod = null;
/*     */ 
/* 247 */     private static Method fgCanConvertMethod = null;
/*     */ 
/* 250 */     private static boolean fgConvertersAvailable = false;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/* 257 */         Class clazz = Class.forName("sun.io.CharToByteConverter");
/* 258 */         fgGetConverterMethod = clazz.getMethod("getConverter", new Class[] { String.class });
/* 259 */         fgCanConvertMethod = clazz.getMethod("canConvert", new Class[] { Character.TYPE });
/* 260 */         fgConvertersAvailable = true;
/*     */       }
/*     */       catch (Exception exc)
/*     */       {
/* 265 */         fgGetConverterMethod = null;
/* 266 */         fgCanConvertMethod = null;
/* 267 */         fgConvertersAvailable = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class CharsetMethods
/*     */   {
/* 199 */     private static Method fgCharsetForNameMethod = null;
/*     */ 
/* 202 */     private static Method fgCharsetCanEncodeMethod = null;
/*     */ 
/* 205 */     private static Method fgCharsetNewEncoderMethod = null;
/*     */ 
/* 208 */     private static Method fgCharsetEncoderCanEncodeMethod = null;
/*     */ 
/* 211 */     private static boolean fgNIOCharsetAvailable = false;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/* 218 */         Class charsetClass = Class.forName("java.nio.charset.Charset");
/* 219 */         Class charsetEncoderClass = Class.forName("java.nio.charset.CharsetEncoder");
/* 220 */         fgCharsetForNameMethod = charsetClass.getMethod("forName", new Class[] { String.class });
/* 221 */         fgCharsetCanEncodeMethod = charsetClass.getMethod("canEncode", new Class[0]);
/* 222 */         fgCharsetNewEncoderMethod = charsetClass.getMethod("newEncoder", new Class[0]);
/* 223 */         fgCharsetEncoderCanEncodeMethod = charsetEncoderClass.getMethod("canEncode", new Class[] { Character.TYPE });
/* 224 */         fgNIOCharsetAvailable = true;
/*     */       }
/*     */       catch (Exception exc)
/*     */       {
/* 229 */         fgCharsetForNameMethod = null;
/* 230 */         fgCharsetCanEncodeMethod = null;
/* 231 */         fgCharsetEncoderCanEncodeMethod = null;
/* 232 */         fgCharsetNewEncoderMethod = null;
/* 233 */         fgNIOCharsetAvailable = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.EncodingInfo
 * JD-Core Version:    0.6.2
 */