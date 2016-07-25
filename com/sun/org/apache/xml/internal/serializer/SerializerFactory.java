/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Messages;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Utils;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import org.xml.sax.ContentHandler;
/*     */ 
/*     */ public final class SerializerFactory
/*     */ {
/*  76 */   private static Hashtable m_formats = new Hashtable();
/*     */ 
/*     */   public static Serializer getSerializer(Properties format)
/*     */   {
/*     */     Serializer ser;
/*     */     try
/*     */     {
/*  99 */       String method = format.getProperty("method");
/*     */ 
/* 101 */       if (method == null) {
/* 102 */         String msg = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[] { "method" });
/*     */ 
/* 105 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */ 
/* 108 */       String className = format.getProperty("{http://xml.apache.org/xalan}content-handler");
/*     */ 
/* 112 */       if (null == className)
/*     */       {
/* 115 */         Properties methodDefaults = OutputPropertiesFactory.getDefaultMethodProperties(method);
/*     */ 
/* 117 */         className = methodDefaults.getProperty("{http://xml.apache.org/xalan}content-handler");
/*     */ 
/* 119 */         if (null == className) {
/* 120 */           String msg = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[] { "{http://xml.apache.org/xalan}content-handler" });
/*     */ 
/* 123 */           throw new IllegalArgumentException(msg);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 130 */       Class cls = ObjectFactory.findProviderClass(className, true);
/*     */ 
/* 134 */       Object obj = cls.newInstance();
/*     */ 
/* 136 */       if ((obj instanceof SerializationHandler))
/*     */       {
/* 139 */         Serializer ser = (Serializer)cls.newInstance();
/* 140 */         ser.setOutputFormat(format);
/*     */       }
/*     */       else
/*     */       {
/*     */         Serializer ser;
/* 148 */         if ((obj instanceof ContentHandler))
/*     */         {
/* 157 */           className = "com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler";
/* 158 */           cls = ObjectFactory.findProviderClass(className, true);
/* 159 */           SerializationHandler sh = (SerializationHandler)cls.newInstance();
/*     */ 
/* 161 */           sh.setContentHandler((ContentHandler)obj);
/* 162 */           sh.setOutputFormat(format);
/*     */ 
/* 164 */           ser = sh;
/*     */         }
/*     */         else
/*     */         {
/* 170 */           throw new Exception(Utils.messages.createMessage("ER_SERIALIZER_NOT_CONTENTHANDLER", new Object[] { className }));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 180 */       throw new WrappedRuntimeException(e);
/*     */     }
/*     */ 
/* 184 */     return ser;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.SerializerFactory
 * JD-Core Version:    0.6.2
 */