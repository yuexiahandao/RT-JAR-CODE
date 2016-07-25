/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public abstract class SerializerFactory
/*     */ {
/*     */   public static final String FactoriesProperty = "com.sun.org.apache.xml.internal.serialize.factories";
/*  46 */   private static Hashtable _factories = new Hashtable();
/*     */ 
/*     */   public static void registerSerializerFactory(SerializerFactory factory)
/*     */   {
/*  91 */     synchronized (_factories) {
/*  92 */       String method = factory.getSupportedMethod();
/*  93 */       _factories.put(method, factory);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static SerializerFactory getSerializerFactory(String method)
/*     */   {
/* 104 */     return (SerializerFactory)_factories.get(method);
/*     */   }
/*     */ 
/*     */   protected abstract String getSupportedMethod();
/*     */ 
/*     */   public abstract Serializer makeSerializer(OutputFormat paramOutputFormat);
/*     */ 
/*     */   public abstract Serializer makeSerializer(Writer paramWriter, OutputFormat paramOutputFormat);
/*     */ 
/*     */   public abstract Serializer makeSerializer(OutputStream paramOutputStream, OutputFormat paramOutputFormat)
/*     */     throws UnsupportedEncodingException;
/*     */ 
/*     */   static
/*     */   {
/*  59 */     SerializerFactory factory = new SerializerFactoryImpl("xml");
/*  60 */     registerSerializerFactory(factory);
/*  61 */     factory = new SerializerFactoryImpl("html");
/*  62 */     registerSerializerFactory(factory);
/*  63 */     factory = new SerializerFactoryImpl("xhtml");
/*  64 */     registerSerializerFactory(factory);
/*  65 */     factory = new SerializerFactoryImpl("text");
/*  66 */     registerSerializerFactory(factory);
/*     */ 
/*  68 */     String list = SecuritySupport.getSystemProperty("com.sun.org.apache.xml.internal.serialize.factories");
/*  69 */     if (list != null) {
/*  70 */       StringTokenizer token = new StringTokenizer(list, " ;,:");
/*  71 */       while (token.hasMoreTokens()) {
/*  72 */         String className = token.nextToken();
/*     */         try {
/*  74 */           factory = (SerializerFactory)ObjectFactory.newInstance(className, true);
/*  75 */           if (_factories.containsKey(factory.getSupportedMethod()))
/*  76 */             _factories.put(factory.getSupportedMethod(), factory);
/*     */         }
/*     */         catch (Exception except)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.SerializerFactory
 * JD-Core Version:    0.6.2
 */