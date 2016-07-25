/*     */ package java.security;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public class SecureClassLoader extends ClassLoader
/*     */ {
/*     */   private final boolean initialized;
/*  52 */   private final HashMap<CodeSource, ProtectionDomain> pdcache = new HashMap(11);
/*     */ 
/*  55 */   private static final Debug debug = Debug.getInstance("scl");
/*     */ 
/*     */   protected SecureClassLoader(ClassLoader paramClassLoader)
/*     */   {
/*  76 */     super(paramClassLoader);
/*     */ 
/*  78 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  79 */     if (localSecurityManager != null) {
/*  80 */       localSecurityManager.checkCreateClassLoader();
/*     */     }
/*  82 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   protected SecureClassLoader()
/*     */   {
/* 101 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 102 */     if (localSecurityManager != null) {
/* 103 */       localSecurityManager.checkCreateClassLoader();
/*     */     }
/* 105 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   protected final Class<?> defineClass(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2, CodeSource paramCodeSource)
/*     */   {
/* 142 */     return defineClass(paramString, paramArrayOfByte, paramInt1, paramInt2, getProtectionDomain(paramCodeSource));
/*     */   }
/*     */ 
/*     */   protected final Class<?> defineClass(String paramString, ByteBuffer paramByteBuffer, CodeSource paramCodeSource)
/*     */   {
/* 174 */     return defineClass(paramString, paramByteBuffer, getProtectionDomain(paramCodeSource));
/*     */   }
/*     */ 
/*     */   protected PermissionCollection getPermissions(CodeSource paramCodeSource)
/*     */   {
/* 191 */     check();
/* 192 */     return new Permissions();
/*     */   }
/*     */ 
/*     */   private ProtectionDomain getProtectionDomain(CodeSource paramCodeSource)
/*     */   {
/* 199 */     if (paramCodeSource == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     ProtectionDomain localProtectionDomain = null;
/* 203 */     synchronized (this.pdcache) {
/* 204 */       localProtectionDomain = (ProtectionDomain)this.pdcache.get(paramCodeSource);
/* 205 */       if (localProtectionDomain == null) {
/* 206 */         PermissionCollection localPermissionCollection = getPermissions(paramCodeSource);
/* 207 */         localProtectionDomain = new ProtectionDomain(paramCodeSource, localPermissionCollection, this, null);
/* 208 */         this.pdcache.put(paramCodeSource, localProtectionDomain);
/* 209 */         if (debug != null) {
/* 210 */           debug.println(" getPermissions " + localProtectionDomain);
/* 211 */           debug.println("");
/*     */         }
/*     */       }
/*     */     }
/* 215 */     return localProtectionDomain;
/*     */   }
/*     */ 
/*     */   private void check()
/*     */   {
/* 222 */     if (!this.initialized)
/* 223 */       throw new SecurityException("ClassLoader object not initialized");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  58 */     ClassLoader.registerAsParallelCapable();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.SecureClassLoader
 * JD-Core Version:    0.6.2
 */