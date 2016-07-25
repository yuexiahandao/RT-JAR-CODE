/*     */ package com.sun.xml.internal.messaging.saaj.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class FastInfosetReflection
/*     */ {
/*     */   static Constructor fiDOMDocumentParser_new;
/*     */   static Method fiDOMDocumentParser_parse;
/*     */   static Constructor fiDOMDocumentSerializer_new;
/*     */   static Method fiDOMDocumentSerializer_serialize;
/*     */   static Method fiDOMDocumentSerializer_setOutputStream;
/*     */   static Class fiFastInfosetSource_class;
/*     */   static Constructor fiFastInfosetSource_new;
/*     */   static Method fiFastInfosetSource_getInputStream;
/*     */   static Method fiFastInfosetSource_setInputStream;
/*     */   static Constructor fiFastInfosetResult_new;
/*     */   static Method fiFastInfosetResult_getOutputStream;
/*     */ 
/*     */   public static Object DOMDocumentParser_new()
/*     */     throws Exception
/*     */   {
/* 133 */     if (fiDOMDocumentParser_new == null) {
/* 134 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 136 */     return fiDOMDocumentParser_new.newInstance((Object[])null);
/*     */   }
/*     */ 
/*     */   public static void DOMDocumentParser_parse(Object parser, Document d, InputStream s)
/*     */     throws Exception
/*     */   {
/* 142 */     if (fiDOMDocumentParser_parse == null) {
/* 143 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 145 */     fiDOMDocumentParser_parse.invoke(parser, new Object[] { d, s });
/*     */   }
/*     */ 
/*     */   public static Object DOMDocumentSerializer_new()
/*     */     throws Exception
/*     */   {
/* 151 */     if (fiDOMDocumentSerializer_new == null) {
/* 152 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 154 */     return fiDOMDocumentSerializer_new.newInstance((Object[])null);
/*     */   }
/*     */ 
/*     */   public static void DOMDocumentSerializer_serialize(Object serializer, Node node)
/*     */     throws Exception
/*     */   {
/* 160 */     if (fiDOMDocumentSerializer_serialize == null) {
/* 161 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 163 */     fiDOMDocumentSerializer_serialize.invoke(serializer, new Object[] { node });
/*     */   }
/*     */ 
/*     */   public static void DOMDocumentSerializer_setOutputStream(Object serializer, OutputStream os)
/*     */     throws Exception
/*     */   {
/* 169 */     if (fiDOMDocumentSerializer_setOutputStream == null) {
/* 170 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 172 */     fiDOMDocumentSerializer_setOutputStream.invoke(serializer, new Object[] { os });
/*     */   }
/*     */ 
/*     */   public static boolean isFastInfosetSource(Source source)
/*     */   {
/* 178 */     return source.getClass().getName().equals("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource");
/*     */   }
/*     */ 
/*     */   public static Class getFastInfosetSource_class()
/*     */   {
/* 183 */     if (fiFastInfosetSource_class == null) {
/* 184 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/*     */ 
/* 187 */     return fiFastInfosetSource_class;
/*     */   }
/*     */ 
/*     */   public static Source FastInfosetSource_new(InputStream is) throws Exception
/*     */   {
/* 192 */     if (fiFastInfosetSource_new == null) {
/* 193 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 195 */     return (Source)fiFastInfosetSource_new.newInstance(new Object[] { is });
/*     */   }
/*     */ 
/*     */   public static InputStream FastInfosetSource_getInputStream(Source source)
/*     */     throws Exception
/*     */   {
/* 201 */     if (fiFastInfosetSource_getInputStream == null) {
/* 202 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 204 */     return (InputStream)fiFastInfosetSource_getInputStream.invoke(source, (Object[])null);
/*     */   }
/*     */ 
/*     */   public static void FastInfosetSource_setInputStream(Source source, InputStream is)
/*     */     throws Exception
/*     */   {
/* 210 */     if (fiFastInfosetSource_setInputStream == null) {
/* 211 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 213 */     fiFastInfosetSource_setInputStream.invoke(source, new Object[] { is });
/*     */   }
/*     */ 
/*     */   public static boolean isFastInfosetResult(Result result)
/*     */   {
/* 219 */     return result.getClass().getName().equals("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetResult");
/*     */   }
/*     */ 
/*     */   public static Result FastInfosetResult_new(OutputStream os)
/*     */     throws Exception
/*     */   {
/* 226 */     if (fiFastInfosetResult_new == null) {
/* 227 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 229 */     return (Result)fiFastInfosetResult_new.newInstance(new Object[] { os });
/*     */   }
/*     */ 
/*     */   public static OutputStream FastInfosetResult_getOutputStream(Result result)
/*     */     throws Exception
/*     */   {
/* 235 */     if (fiFastInfosetResult_getOutputStream == null) {
/* 236 */       throw new RuntimeException("Unable to locate Fast Infoset implementation");
/*     */     }
/* 238 */     return (OutputStream)fiFastInfosetResult_getOutputStream.invoke(result, (Object[])null);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 101 */       Class clazz = Class.forName("com.sun.xml.internal.fastinfoset.dom.DOMDocumentParser");
/* 102 */       fiDOMDocumentParser_new = clazz.getConstructor((Class[])null);
/* 103 */       fiDOMDocumentParser_parse = clazz.getMethod("parse", new Class[] { Document.class, InputStream.class });
/*     */ 
/* 106 */       clazz = Class.forName("com.sun.xml.internal.fastinfoset.dom.DOMDocumentSerializer");
/* 107 */       fiDOMDocumentSerializer_new = clazz.getConstructor((Class[])null);
/* 108 */       fiDOMDocumentSerializer_serialize = clazz.getMethod("serialize", new Class[] { Node.class });
/*     */ 
/* 110 */       fiDOMDocumentSerializer_setOutputStream = clazz.getMethod("setOutputStream", new Class[] { OutputStream.class });
/*     */ 
/* 113 */       fiFastInfosetSource_class = clazz = Class.forName("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource");
/* 114 */       fiFastInfosetSource_new = clazz.getConstructor(new Class[] { InputStream.class });
/*     */ 
/* 116 */       fiFastInfosetSource_getInputStream = clazz.getMethod("getInputStream", (Class[])null);
/* 117 */       fiFastInfosetSource_setInputStream = clazz.getMethod("setInputStream", new Class[] { InputStream.class });
/*     */ 
/* 120 */       clazz = Class.forName("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetResult");
/* 121 */       fiFastInfosetResult_new = clazz.getConstructor(new Class[] { OutputStream.class });
/*     */ 
/* 123 */       fiFastInfosetResult_getOutputStream = clazz.getMethod("getOutputStream", (Class[])null);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection
 * JD-Core Version:    0.6.2
 */