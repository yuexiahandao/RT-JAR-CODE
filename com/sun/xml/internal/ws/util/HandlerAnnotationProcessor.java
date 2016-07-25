/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.server.AsyncProvider;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.handler.HandlerChainsModel;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Logger;
/*     */ import javax.jws.HandlerChain;
/*     */ import javax.jws.WebService;
/*     */ import javax.jws.soap.SOAPMessageHandlers;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.Provider;
/*     */ import javax.xml.ws.Service;
/*     */ 
/*     */ public class HandlerAnnotationProcessor
/*     */ {
/*  72 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.util");
/*     */ 
/*     */   public static HandlerAnnotationInfo buildHandlerInfo(@NotNull Class<?> clazz, QName serviceName, QName portName, WSBinding binding)
/*     */   {
/*  93 */     HandlerChain handlerChain = (HandlerChain)clazz.getAnnotation(HandlerChain.class);
/*     */ 
/*  95 */     if (handlerChain == null) {
/*  96 */       clazz = getSEI(clazz);
/*  97 */       if (clazz != null) {
/*  98 */         handlerChain = (HandlerChain)clazz.getAnnotation(HandlerChain.class);
/*     */       }
/* 100 */       if (handlerChain == null) {
/* 101 */         return null;
/*     */       }
/*     */     }
/* 104 */     if (clazz.getAnnotation(SOAPMessageHandlers.class) != null) {
/* 105 */       throw new UtilException("util.handler.cannot.combine.soapmessagehandlers", new Object[0]);
/*     */     }
/*     */ 
/* 108 */     InputStream iStream = getFileAsStream(clazz, handlerChain);
/* 109 */     XMLStreamReader reader = XMLStreamReaderFactory.create(null, iStream, true);
/*     */ 
/* 111 */     XMLStreamReaderUtil.nextElementContent(reader);
/* 112 */     HandlerAnnotationInfo handlerAnnInfo = HandlerChainsModel.parseHandlerFile(reader, clazz.getClassLoader(), serviceName, portName, binding);
/*     */     try
/*     */     {
/* 115 */       reader.close();
/* 116 */       iStream.close();
/*     */     } catch (XMLStreamException e) {
/* 118 */       e.printStackTrace();
/* 119 */       throw new UtilException(e.getMessage(), new Object[0]);
/*     */     } catch (IOException e) {
/* 121 */       e.printStackTrace();
/* 122 */       throw new UtilException(e.getMessage(), new Object[0]);
/*     */     }
/* 124 */     return handlerAnnInfo;
/*     */   }
/*     */ 
/*     */   public static HandlerChainsModel buildHandlerChainsModel(Class<?> clazz) {
/* 128 */     if (clazz == null) {
/* 129 */       return null;
/*     */     }
/* 131 */     HandlerChain handlerChain = (HandlerChain)clazz.getAnnotation(HandlerChain.class);
/*     */ 
/* 133 */     if (handlerChain == null)
/* 134 */       return null;
/* 135 */     InputStream iStream = getFileAsStream(clazz, handlerChain);
/* 136 */     XMLStreamReader reader = XMLStreamReaderFactory.create(null, iStream, true);
/*     */ 
/* 138 */     XMLStreamReaderUtil.nextElementContent(reader);
/* 139 */     HandlerChainsModel handlerChainsModel = HandlerChainsModel.parseHandlerConfigFile(clazz, reader);
/*     */     try {
/* 141 */       reader.close();
/* 142 */       iStream.close();
/*     */     } catch (XMLStreamException e) {
/* 144 */       e.printStackTrace();
/* 145 */       throw new UtilException(e.getMessage(), new Object[0]);
/*     */     } catch (IOException e) {
/* 147 */       e.printStackTrace();
/* 148 */       throw new UtilException(e.getMessage(), new Object[0]);
/*     */     }
/* 150 */     return handlerChainsModel; } 
/*     */   static Class getClass(String className) { // Byte code:
/*     */     //   0: invokestatic 238	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: invokevirtual 237	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   6: aload_0
/*     */     //   7: invokevirtual 228	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
/*     */     //   10: areturn
/*     */     //   11: astore_1
/*     */     //   12: new 117	com/sun/xml/internal/ws/util/UtilException
/*     */     //   15: dup
/*     */     //   16: ldc 6
/*     */     //   18: iconst_1
/*     */     //   19: anewarray 123	java/lang/Object
/*     */     //   22: dup
/*     */     //   23: iconst_0
/*     */     //   24: aload_0
/*     */     //   25: aastore
/*     */     //   26: invokespecial 216	com/sun/xml/internal/ws/util/UtilException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   29: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	10	11	java/lang/ClassNotFoundException } 
/* 164 */   static Class getSEI(Class<?> clazz) { if ((Provider.class.isAssignableFrom(clazz)) || (AsyncProvider.class.isAssignableFrom(clazz)))
/*     */     {
/* 166 */       return null;
/*     */     }
/* 168 */     if (Service.class.isAssignableFrom(clazz))
/*     */     {
/* 170 */       return null;
/*     */     }
/* 172 */     if (!clazz.isAnnotationPresent(WebService.class)) {
/* 173 */       throw new UtilException("util.handler.no.webservice.annotation", new Object[] { clazz.getCanonicalName() });
/*     */     }
/*     */ 
/* 177 */     WebService webService = (WebService)clazz.getAnnotation(WebService.class);
/*     */ 
/* 179 */     String ei = webService.endpointInterface();
/* 180 */     if (ei.length() > 0) {
/* 181 */       clazz = getClass(webService.endpointInterface());
/* 182 */       if (!clazz.isAnnotationPresent(WebService.class)) {
/* 183 */         throw new UtilException("util.handler.endpoint.interface.no.webservice", new Object[] { webService.endpointInterface() });
/*     */       }
/*     */ 
/* 186 */       return clazz;
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */   static InputStream getFileAsStream(Class clazz, HandlerChain chain)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokeinterface 243 1 0
/*     */     //   7: invokevirtual 227	java/lang/Class:getResource	(Ljava/lang/String;)Ljava/net/URL;
/*     */     //   10: astore_2
/*     */     //   11: aload_2
/*     */     //   12: ifnonnull +19 -> 31
/*     */     //   15: invokestatic 238	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   18: invokevirtual 237	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   21: aload_1
/*     */     //   22: invokeinterface 243 1 0
/*     */     //   27: invokevirtual 229	java/lang/ClassLoader:getResource	(Ljava/lang/String;)Ljava/net/URL;
/*     */     //   30: astore_2
/*     */     //   31: aload_2
/*     */     //   32: ifnonnull +60 -> 92
/*     */     //   35: aload_0
/*     */     //   36: invokevirtual 223	java/lang/Class:getPackage	()Ljava/lang/Package;
/*     */     //   39: invokevirtual 231	java/lang/Package:getName	()Ljava/lang/String;
/*     */     //   42: astore_3
/*     */     //   43: aload_3
/*     */     //   44: bipush 46
/*     */     //   46: bipush 47
/*     */     //   48: invokevirtual 233	java/lang/String:replace	(CC)Ljava/lang/String;
/*     */     //   51: astore_3
/*     */     //   52: new 126	java/lang/StringBuilder
/*     */     //   55: dup
/*     */     //   56: invokespecial 234	java/lang/StringBuilder:<init>	()V
/*     */     //   59: aload_3
/*     */     //   60: invokevirtual 236	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   63: ldc 1
/*     */     //   65: invokevirtual 236	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   68: aload_1
/*     */     //   69: invokeinterface 243 1 0
/*     */     //   74: invokevirtual 236	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   77: invokevirtual 235	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   80: astore_3
/*     */     //   81: invokestatic 238	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   84: invokevirtual 237	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   87: aload_3
/*     */     //   88: invokevirtual 229	java/lang/ClassLoader:getResource	(Ljava/lang/String;)Ljava/net/URL;
/*     */     //   91: astore_2
/*     */     //   92: aload_2
/*     */     //   93: ifnonnull +33 -> 126
/*     */     //   96: new 117	com/sun/xml/internal/ws/util/UtilException
/*     */     //   99: dup
/*     */     //   100: ldc 3
/*     */     //   102: iconst_2
/*     */     //   103: anewarray 123	java/lang/Object
/*     */     //   106: dup
/*     */     //   107: iconst_0
/*     */     //   108: aload_0
/*     */     //   109: invokevirtual 225	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   112: aastore
/*     */     //   113: dup
/*     */     //   114: iconst_1
/*     */     //   115: aload_1
/*     */     //   116: invokeinterface 243 1 0
/*     */     //   121: aastore
/*     */     //   122: invokespecial 216	com/sun/xml/internal/ws/util/UtilException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   125: athrow
/*     */     //   126: aload_2
/*     */     //   127: invokevirtual 239	java/net/URL:openStream	()Ljava/io/InputStream;
/*     */     //   130: areturn
/*     */     //   131: astore_3
/*     */     //   132: new 117	com/sun/xml/internal/ws/util/UtilException
/*     */     //   135: dup
/*     */     //   136: ldc 4
/*     */     //   138: iconst_2
/*     */     //   139: anewarray 123	java/lang/Object
/*     */     //   142: dup
/*     */     //   143: iconst_0
/*     */     //   144: aload_0
/*     */     //   145: invokevirtual 225	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   148: aastore
/*     */     //   149: dup
/*     */     //   150: iconst_1
/*     */     //   151: aload_1
/*     */     //   152: invokeinterface 243 1 0
/*     */     //   157: aastore
/*     */     //   158: invokespecial 216	com/sun/xml/internal/ws/util/UtilException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   161: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   126	130	131	java/io/IOException
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.HandlerAnnotationProcessor
 * JD-Core Version:    0.6.2
 */