/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.DSAKeyValueResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.RSAKeyValueResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.RetrievalMethodResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509CertificateResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509IssuerSerialResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509SKIResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509SubjectNameResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class KeyResolver
/*     */ {
/*  53 */   private static Logger log = Logger.getLogger(KeyResolver.class.getName());
/*     */ 
/*  57 */   private static List<KeyResolver> resolverVector = new CopyOnWriteArrayList();
/*     */   private final KeyResolverSpi resolverSpi;
/*     */ 
/*     */   private KeyResolver(KeyResolverSpi paramKeyResolverSpi)
/*     */   {
/*  68 */     this.resolverSpi = paramKeyResolverSpi;
/*     */   }
/*     */ 
/*     */   public static int length()
/*     */   {
/*  77 */     return resolverVector.size();
/*     */   }
/*     */ 
/*     */   public static final X509Certificate getX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  93 */     for (Object localObject1 = resolverVector.iterator(); ((Iterator)localObject1).hasNext(); ) { KeyResolver localKeyResolver = (KeyResolver)((Iterator)localObject1).next();
/*  94 */       if (localKeyResolver == null) {
/*  95 */         localObject2 = new Object[] { (paramElement != null) && (paramElement.getNodeType() == 1) ? paramElement.getTagName() : "null" };
/*     */ 
/* 101 */         throw new KeyResolverException("utils.resolver.noClass", (Object[])localObject2);
/*     */       }
/* 103 */       if (log.isLoggable(Level.FINE)) {
/* 104 */         log.log(Level.FINE, "check resolvability by class " + localKeyResolver.getClass());
/*     */       }
/*     */ 
/* 107 */       Object localObject2 = localKeyResolver.resolveX509Certificate(paramElement, paramString, paramStorageResolver);
/* 108 */       if (localObject2 != null) {
/* 109 */         return localObject2;
/*     */       }
/*     */     }
/*     */ 
/* 113 */     localObject1 = new Object[] { (paramElement != null) && (paramElement.getNodeType() == 1) ? paramElement.getTagName() : "null" };
/*     */ 
/* 118 */     throw new KeyResolverException("utils.resolver.noClass", (Object[])localObject1);
/*     */   }
/*     */ 
/*     */   public static final PublicKey getPublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 134 */     for (Object localObject1 = resolverVector.iterator(); ((Iterator)localObject1).hasNext(); ) { KeyResolver localKeyResolver = (KeyResolver)((Iterator)localObject1).next();
/* 135 */       if (localKeyResolver == null) {
/* 136 */         localObject2 = new Object[] { (paramElement != null) && (paramElement.getNodeType() == 1) ? paramElement.getTagName() : "null" };
/*     */ 
/* 142 */         throw new KeyResolverException("utils.resolver.noClass", (Object[])localObject2);
/*     */       }
/* 144 */       if (log.isLoggable(Level.FINE)) {
/* 145 */         log.log(Level.FINE, "check resolvability by class " + localKeyResolver.getClass());
/*     */       }
/*     */ 
/* 148 */       Object localObject2 = localKeyResolver.resolvePublicKey(paramElement, paramString, paramStorageResolver);
/* 149 */       if (localObject2 != null) {
/* 150 */         return localObject2;
/*     */       }
/*     */     }
/*     */ 
/* 154 */     localObject1 = new Object[] { (paramElement != null) && (paramElement.getNodeType() == 1) ? paramElement.getTagName() : "null" };
/*     */ 
/* 159 */     throw new KeyResolverException("utils.resolver.noClass", (Object[])localObject1);
/*     */   }
/*     */ 
/*     */   public static void register(String paramString, boolean paramBoolean)
/*     */     throws ClassNotFoundException, IllegalAccessException, InstantiationException
/*     */   {
/* 181 */     JavaUtils.checkRegisterPermission();
/* 182 */     KeyResolverSpi localKeyResolverSpi = (KeyResolverSpi)Class.forName(paramString).newInstance();
/*     */ 
/* 184 */     localKeyResolverSpi.setGlobalResolver(paramBoolean);
/* 185 */     register(localKeyResolverSpi, false);
/*     */   }
/*     */ 
/*     */   public static void registerAtStart(String paramString, boolean paramBoolean)
/*     */   {
/* 203 */     JavaUtils.checkRegisterPermission();
/* 204 */     KeyResolverSpi localKeyResolverSpi = null;
/* 205 */     Object localObject = null;
/*     */     try {
/* 207 */       localKeyResolverSpi = (KeyResolverSpi)Class.forName(paramString).newInstance();
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 209 */       localObject = localClassNotFoundException;
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 211 */       localObject = localIllegalAccessException;
/*     */     } catch (InstantiationException localInstantiationException) {
/* 213 */       localObject = localInstantiationException;
/*     */     }
/*     */ 
/* 216 */     if (localObject != null) {
/* 217 */       throw ((IllegalArgumentException)new IllegalArgumentException("Invalid KeyResolver class name").initCause(localObject));
/*     */     }
/*     */ 
/* 220 */     localKeyResolverSpi.setGlobalResolver(paramBoolean);
/* 221 */     register(localKeyResolverSpi, true);
/*     */   }
/*     */ 
/*     */   public static void register(KeyResolverSpi paramKeyResolverSpi, boolean paramBoolean)
/*     */   {
/* 242 */     JavaUtils.checkRegisterPermission();
/* 243 */     KeyResolver localKeyResolver = new KeyResolver(paramKeyResolverSpi);
/* 244 */     if (paramBoolean)
/* 245 */       resolverVector.add(0, localKeyResolver);
/*     */     else
/* 247 */       resolverVector.add(localKeyResolver);
/*     */   }
/*     */ 
/*     */   public static void registerClassNames(List<String> paramList)
/*     */     throws ClassNotFoundException, IllegalAccessException, InstantiationException
/*     */   {
/* 269 */     JavaUtils.checkRegisterPermission();
/* 270 */     ArrayList localArrayList = new ArrayList(paramList.size());
/* 271 */     for (String str : paramList) {
/* 272 */       KeyResolverSpi localKeyResolverSpi = (KeyResolverSpi)Class.forName(str).newInstance();
/*     */ 
/* 274 */       localKeyResolverSpi.setGlobalResolver(false);
/* 275 */       localArrayList.add(new KeyResolver(localKeyResolverSpi));
/*     */     }
/* 277 */     resolverVector.addAll(localArrayList);
/*     */   }
/*     */ 
/*     */   public static void registerDefaultResolvers()
/*     */   {
/* 285 */     ArrayList localArrayList = new ArrayList();
/* 286 */     localArrayList.add(new KeyResolver(new RSAKeyValueResolver()));
/* 287 */     localArrayList.add(new KeyResolver(new DSAKeyValueResolver()));
/* 288 */     localArrayList.add(new KeyResolver(new X509CertificateResolver()));
/* 289 */     localArrayList.add(new KeyResolver(new X509SKIResolver()));
/* 290 */     localArrayList.add(new KeyResolver(new RetrievalMethodResolver()));
/* 291 */     localArrayList.add(new KeyResolver(new X509SubjectNameResolver()));
/* 292 */     localArrayList.add(new KeyResolver(new X509IssuerSerialResolver()));
/*     */ 
/* 294 */     resolverVector.addAll(localArrayList);
/*     */   }
/*     */ 
/*     */   public PublicKey resolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 310 */     return this.resolverSpi.engineLookupAndResolvePublicKey(paramElement, paramString, paramStorageResolver);
/*     */   }
/*     */ 
/*     */   public X509Certificate resolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 326 */     return this.resolverSpi.engineLookupResolveX509Certificate(paramElement, paramString, paramStorageResolver);
/*     */   }
/*     */ 
/*     */   public SecretKey resolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 339 */     return this.resolverSpi.engineLookupAndResolveSecretKey(paramElement, paramString, paramStorageResolver);
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString1, String paramString2)
/*     */   {
/* 349 */     this.resolverSpi.engineSetProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public String getProperty(String paramString)
/*     */   {
/* 359 */     return this.resolverSpi.engineGetProperty(paramString);
/*     */   }
/*     */ 
/*     */   public boolean understandsProperty(String paramString)
/*     */   {
/* 370 */     return this.resolverSpi.understandsProperty(paramString);
/*     */   }
/*     */ 
/*     */   public String resolverClassName()
/*     */   {
/* 380 */     return this.resolverSpi.getClass().getName();
/*     */   }
/*     */ 
/*     */   public static Iterator<KeyResolverSpi> iterator()
/*     */   {
/* 414 */     return new ResolverIterator(resolverVector);
/*     */   }
/*     */ 
/*     */   static class ResolverIterator
/*     */     implements Iterator<KeyResolverSpi>
/*     */   {
/*     */     List<KeyResolver> res;
/*     */     Iterator<KeyResolver> it;
/*     */ 
/*     */     public ResolverIterator(List<KeyResolver> paramList)
/*     */     {
/* 391 */       this.res = paramList;
/* 392 */       this.it = this.res.iterator();
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 396 */       return this.it.hasNext();
/*     */     }
/*     */ 
/*     */     public KeyResolverSpi next() {
/* 400 */       KeyResolver localKeyResolver = (KeyResolver)this.it.next();
/* 401 */       if (localKeyResolver == null) {
/* 402 */         throw new RuntimeException("utils.resolver.noClass");
/*     */       }
/*     */ 
/* 405 */       return localKeyResolver.resolverSpi;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 409 */       throw new UnsupportedOperationException("Can't remove resolvers using the iterator");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver
 * JD-Core Version:    0.6.2
 */