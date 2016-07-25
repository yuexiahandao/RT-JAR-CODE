/*     */ package com.sun.org.apache.xml.internal.security.utils.resolver;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverDirectHTTP;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverFragment;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverLocalFilesystem;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverXPointer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ public class ResourceResolver
/*     */ {
/*  46 */   private static Logger log = Logger.getLogger(ResourceResolver.class.getName());
/*     */ 
/*  50 */   private static List<ResourceResolver> resolverList = new ArrayList();
/*     */   private final ResourceResolverSpi resolverSpi;
/*     */ 
/*     */   public ResourceResolver(ResourceResolverSpi paramResourceResolverSpi)
/*     */   {
/*  61 */     this.resolverSpi = paramResourceResolverSpi;
/*     */   }
/*     */ 
/*     */   public static final ResourceResolver getInstance(Attr paramAttr, String paramString)
/*     */     throws ResourceResolverException
/*     */   {
/*  75 */     return getInstance(paramAttr, paramString, false);
/*     */   }
/*     */ 
/*     */   public static final ResourceResolver getInstance(Attr paramAttr, String paramString, boolean paramBoolean)
/*     */     throws ResourceResolverException
/*     */   {
/*  91 */     synchronized (resolverList) {
/*  92 */       for (ResourceResolver localResourceResolver1 : resolverList) {
/*  93 */         ResourceResolver localResourceResolver2 = localResourceResolver1;
/*  94 */         if (!localResourceResolver1.resolverSpi.engineIsThreadSafe()) {
/*     */           try {
/*  96 */             localResourceResolver2 = new ResourceResolver((ResourceResolverSpi)localResourceResolver1.resolverSpi.getClass().newInstance());
/*     */           }
/*     */           catch (InstantiationException localInstantiationException) {
/*  99 */             throw new ResourceResolverException("", localInstantiationException, paramAttr, paramString);
/*     */           } catch (IllegalAccessException localIllegalAccessException) {
/* 101 */             throw new ResourceResolverException("", localIllegalAccessException, paramAttr, paramString);
/*     */           }
/*     */         }
/*     */ 
/* 105 */         if (log.isLoggable(Level.FINE)) {
/* 106 */           log.log(Level.FINE, "check resolvability by class " + localResourceResolver2.getClass().getName());
/*     */         }
/*     */ 
/* 111 */         localResourceResolver2.resolverSpi.secureValidation = paramBoolean;
/* 112 */         if ((localResourceResolver2 != null) && (localResourceResolver2.canResolve(paramAttr, paramString)))
/*     */         {
/* 114 */           if ((paramBoolean) && (((localResourceResolver2.resolverSpi instanceof ResolverLocalFilesystem)) || ((localResourceResolver2.resolverSpi instanceof ResolverDirectHTTP))))
/*     */           {
/* 117 */             Object[] arrayOfObject = { localResourceResolver2.resolverSpi.getClass().getName() };
/* 118 */             throw new ResourceResolverException("signature.Reference.ForbiddenResolver", arrayOfObject, paramAttr, paramString);
/*     */           }
/*     */ 
/* 122 */           return localResourceResolver2;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 127 */     ??? = new Object[] { paramAttr != null ? paramAttr.getNodeValue() : "null", paramString };
/*     */ 
/* 129 */     throw new ResourceResolverException("utils.resolver.noClass", (Object[])???, paramAttr, paramString);
/*     */   }
/*     */ 
/*     */   public static ResourceResolver getInstance(Attr paramAttr, String paramString, List<ResourceResolver> paramList)
/*     */     throws ResourceResolverException
/*     */   {
/* 145 */     return getInstance(paramAttr, paramString, paramList, false);
/*     */   }
/*     */ 
/*     */   public static ResourceResolver getInstance(Attr paramAttr, String paramString, List<ResourceResolver> paramList, boolean paramBoolean)
/*     */     throws ResourceResolverException
/*     */   {
/* 162 */     if (log.isLoggable(Level.FINE)) {
/* 163 */       log.log(Level.FINE, "I was asked to create a ResourceResolver and got " + (paramList == null ? 0 : paramList.size()));
/*     */     }
/*     */ 
/* 170 */     if (paramList != null) {
/* 171 */       for (int i = 0; i < paramList.size(); i++) {
/* 172 */         ResourceResolver localResourceResolver = (ResourceResolver)paramList.get(i);
/*     */ 
/* 174 */         if (localResourceResolver != null) {
/* 175 */           if (log.isLoggable(Level.FINE)) {
/* 176 */             String str = localResourceResolver.resolverSpi.getClass().getName();
/* 177 */             log.log(Level.FINE, "check resolvability by class " + str);
/*     */           }
/*     */ 
/* 180 */           localResourceResolver.resolverSpi.secureValidation = paramBoolean;
/* 181 */           if (localResourceResolver.canResolve(paramAttr, paramString)) {
/* 182 */             return localResourceResolver;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 188 */     return getInstance(paramAttr, paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   public static void register(String paramString)
/*     */   {
/* 201 */     JavaUtils.checkRegisterPermission();
/*     */     try {
/* 203 */       Class localClass = Class.forName(paramString);
/*     */ 
/* 205 */       register(localClass, false);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 207 */       log.log(Level.WARNING, "Error loading resolver " + paramString + " disabling it");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void registerAtStart(String paramString)
/*     */   {
/* 221 */     JavaUtils.checkRegisterPermission();
/*     */     try {
/* 223 */       Class localClass = Class.forName(paramString);
/*     */ 
/* 225 */       register(localClass, true);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 227 */       log.log(Level.WARNING, "Error loading resolver " + paramString + " disabling it");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void register(Class<? extends ResourceResolverSpi> paramClass, boolean paramBoolean)
/*     */   {
/* 240 */     JavaUtils.checkRegisterPermission();
/*     */     try {
/* 242 */       ResourceResolverSpi localResourceResolverSpi = (ResourceResolverSpi)paramClass.newInstance();
/* 243 */       register(localResourceResolverSpi, paramBoolean);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 245 */       log.log(Level.WARNING, "Error loading resolver " + paramClass + " disabling it");
/*     */     } catch (InstantiationException localInstantiationException) {
/* 247 */       log.log(Level.WARNING, "Error loading resolver " + paramClass + " disabling it");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void register(ResourceResolverSpi paramResourceResolverSpi, boolean paramBoolean)
/*     */   {
/* 260 */     JavaUtils.checkRegisterPermission();
/* 261 */     synchronized (resolverList) {
/* 262 */       if (paramBoolean)
/* 263 */         resolverList.add(0, new ResourceResolver(paramResourceResolverSpi));
/*     */       else {
/* 265 */         resolverList.add(new ResourceResolver(paramResourceResolverSpi));
/*     */       }
/*     */     }
/* 268 */     if (log.isLoggable(Level.FINE))
/* 269 */       log.log(Level.FINE, "Registered resolver: " + paramResourceResolverSpi.toString());
/*     */   }
/*     */ 
/*     */   public static void registerDefaultResolvers()
/*     */   {
/* 277 */     synchronized (resolverList) {
/* 278 */       resolverList.add(new ResourceResolver(new ResolverFragment()));
/* 279 */       resolverList.add(new ResourceResolver(new ResolverLocalFilesystem()));
/* 280 */       resolverList.add(new ResourceResolver(new ResolverXPointer()));
/* 281 */       resolverList.add(new ResourceResolver(new ResolverDirectHTTP()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput resolve(Attr paramAttr, String paramString)
/*     */     throws ResourceResolverException
/*     */   {
/* 296 */     return this.resolverSpi.engineResolve(paramAttr, paramString);
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString1, String paramString2)
/*     */   {
/* 306 */     this.resolverSpi.engineSetProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public String getProperty(String paramString)
/*     */   {
/* 316 */     return this.resolverSpi.engineGetProperty(paramString);
/*     */   }
/*     */ 
/*     */   public void addProperties(Map<String, String> paramMap)
/*     */   {
/* 325 */     this.resolverSpi.engineAddProperies(paramMap);
/*     */   }
/*     */ 
/*     */   public String[] getPropertyKeys()
/*     */   {
/* 334 */     return this.resolverSpi.engineGetPropertyKeys();
/*     */   }
/*     */ 
/*     */   public boolean understandsProperty(String paramString)
/*     */   {
/* 344 */     return this.resolverSpi.understandsProperty(paramString);
/*     */   }
/*     */ 
/*     */   private boolean canResolve(Attr paramAttr, String paramString)
/*     */   {
/* 355 */     return this.resolverSpi.engineCanResolve(paramAttr, paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver
 * JD-Core Version:    0.6.2
 */