/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.ldap.pool.Pool;
/*     */ import com.sun.jndi.ldap.pool.PoolCleaner;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.ldap.Control;
/*     */ 
/*     */ public final class LdapPoolManager
/*     */ {
/*     */   private static final String DEBUG = "com.sun.jndi.ldap.connect.pool.debug";
/*  60 */   public static final boolean debug = "all".equalsIgnoreCase(getProperty("com.sun.jndi.ldap.connect.pool.debug", null));
/*     */ 
/*  63 */   public static final boolean trace = (debug) || ("fine".equalsIgnoreCase(getProperty("com.sun.jndi.ldap.connect.pool.debug", null)));
/*     */   private static final String POOL_AUTH = "com.sun.jndi.ldap.connect.pool.authentication";
/*     */   private static final String POOL_PROTOCOL = "com.sun.jndi.ldap.connect.pool.protocol";
/*     */   private static final String MAX_POOL_SIZE = "com.sun.jndi.ldap.connect.pool.maxsize";
/*     */   private static final String PREF_POOL_SIZE = "com.sun.jndi.ldap.connect.pool.prefsize";
/*     */   private static final String INIT_POOL_SIZE = "com.sun.jndi.ldap.connect.pool.initsize";
/*     */   private static final String POOL_TIMEOUT = "com.sun.jndi.ldap.connect.pool.timeout";
/*     */   private static final String SASL_CALLBACK = "java.naming.security.sasl.callback";
/*     */   private static final int DEFAULT_MAX_POOL_SIZE = 0;
/*     */   private static final int DEFAULT_PREF_POOL_SIZE = 0;
/*     */   private static final int DEFAULT_INIT_POOL_SIZE = 1;
/*     */   private static final int DEFAULT_TIMEOUT = 0;
/*     */   private static final String DEFAULT_AUTH_MECHS = "none simple";
/*     */   private static final String DEFAULT_PROTOCOLS = "plain";
/*     */   private static final int NONE = 0;
/*     */   private static final int SIMPLE = 1;
/*     */   private static final int DIGEST = 2;
/*     */   private static final long idleTimeout;
/*     */   private static final int maxSize;
/*     */   private static final int prefSize;
/*     */   private static final int initSize;
/* 114 */   private static boolean supportPlainProtocol = false;
/* 115 */   private static boolean supportSslProtocol = false;
/*     */ 
/* 118 */   private static final Pool[] pools = new Pool[3];
/*     */ 
/*     */   private static int findPool(String paramString)
/*     */   {
/* 184 */     if ("none".equalsIgnoreCase(paramString))
/* 185 */       return 0;
/* 186 */     if ("simple".equalsIgnoreCase(paramString))
/* 187 */       return 1;
/* 188 */     if ("digest-md5".equalsIgnoreCase(paramString)) {
/* 189 */       return 2;
/*     */     }
/* 191 */     return -1;
/*     */   }
/*     */ 
/*     */   static boolean isPoolingAllowed(String paramString1, OutputStream paramOutputStream, String paramString2, String paramString3, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 220 */     if (((paramOutputStream != null) && (!debug)) || ((paramString3 == null) && (!supportPlainProtocol)) || (("ssl".equalsIgnoreCase(paramString3)) && (!supportSslProtocol)))
/*     */     {
/* 228 */       d("Pooling disallowed due to tracing or unsupported pooling of protocol");
/* 229 */       return false;
/*     */     }
/*     */ 
/* 233 */     String str = "java.util.Comparator";
/* 234 */     int i = 0;
/* 235 */     if ((paramString1 != null) && (!paramString1.equals("javax.net.ssl.SSLSocketFactory")))
/*     */     {
/*     */       try {
/* 238 */         Class localClass = Obj.helper.loadClass(paramString1);
/* 239 */         localObject = localClass.getInterfaces();
/* 240 */         for (int k = 0; k < localObject.length; k++)
/* 241 */           if (localObject[k].getCanonicalName().equals(str))
/* 242 */             i = 1;
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 246 */         Object localObject = new CommunicationException("Loading the socket factory");
/*     */ 
/* 248 */         ((CommunicationException)localObject).setRootCause(localException);
/* 249 */         throw ((Throwable)localObject);
/*     */       }
/* 251 */       if (i == 0) {
/* 252 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 257 */     int j = findPool(paramString2);
/* 258 */     if ((j < 0) || (pools[j] == null)) {
/* 259 */       d("authmech not found: ", paramString2);
/*     */ 
/* 261 */       return false;
/*     */     }
/*     */ 
/* 264 */     d("using authmech: ", paramString2);
/*     */ 
/* 266 */     switch (j) {
/*     */     case 0:
/*     */     case 1:
/* 269 */       return true;
/*     */     case 2:
/* 274 */       return (paramHashtable == null) || (paramHashtable.get("java.naming.security.sasl.callback") == null);
/*     */     }
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */   static LdapClient getLdapClient(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, OutputStream paramOutputStream, int paramInt4, String paramString3, Control[] paramArrayOfControl, String paramString4, String paramString5, Object paramObject, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 300 */     Object localObject = null;
/*     */ 
/* 303 */     int i = findPool(paramString3);
/*     */     Pool localPool;
/* 304 */     if ((i < 0) || ((localPool = pools[i]) == null)) {
/* 305 */       throw new IllegalArgumentException("Attempting to use pooling for an unsupported mechanism: " + paramString3);
/*     */     }
/*     */ 
/* 309 */     switch (i) {
/*     */     case 0:
/* 311 */       localObject = new ClientId(paramInt4, paramString1, paramInt1, paramString4, paramArrayOfControl, paramOutputStream, paramString2);
/*     */ 
/* 313 */       break;
/*     */     case 1:
/* 317 */       localObject = new SimpleClientId(paramInt4, paramString1, paramInt1, paramString4, paramArrayOfControl, paramOutputStream, paramString2, paramString5, paramObject);
/*     */ 
/* 319 */       break;
/*     */     case 2:
/* 323 */       localObject = new DigestClientId(paramInt4, paramString1, paramInt1, paramString4, paramArrayOfControl, paramOutputStream, paramString2, paramString5, paramObject, paramHashtable);
/*     */     }
/*     */ 
/* 328 */     return (LdapClient)localPool.getPooledConnection(localObject, paramInt2, new LdapClientFactory(paramString1, paramInt1, paramString2, paramInt2, paramInt3, paramOutputStream));
/*     */   }
/*     */ 
/*     */   public static void showStats(PrintStream paramPrintStream)
/*     */   {
/* 334 */     paramPrintStream.println("***** start *****");
/* 335 */     paramPrintStream.println("idle timeout: " + idleTimeout);
/* 336 */     paramPrintStream.println("maximum pool size: " + maxSize);
/* 337 */     paramPrintStream.println("preferred pool size: " + prefSize);
/* 338 */     paramPrintStream.println("initial pool size: " + initSize);
/* 339 */     paramPrintStream.println("protocol types: " + (supportPlainProtocol ? "plain " : "") + (supportSslProtocol ? "ssl" : ""));
/*     */ 
/* 341 */     paramPrintStream.println("authentication types: " + (pools[0] != null ? "none " : "") + (pools[1] != null ? "simple " : "") + (pools[2] != null ? "DIGEST-MD5 " : ""));
/*     */ 
/* 346 */     for (int i = 0; i < pools.length; i++) {
/* 347 */       if (pools[i] != null) {
/* 348 */         paramPrintStream.println((i == 2 ? "digest pools" : i == 1 ? "simple auth pools" : i == 0 ? "anonymous pools" : "") + ":");
/*     */ 
/* 353 */         pools[i].showStats(paramPrintStream);
/*     */       }
/*     */     }
/* 356 */     paramPrintStream.println("***** end *****");
/*     */   }
/*     */ 
/*     */   public static void expire(long paramLong)
/*     */   {
/* 367 */     for (int i = 0; i < pools.length; i++)
/* 368 */       if (pools[i] != null)
/* 369 */         pools[i].expire(paramLong);
/*     */   }
/*     */ 
/*     */   private static void d(String paramString)
/*     */   {
/* 375 */     if (debug)
/* 376 */       System.err.println("LdapPoolManager: " + paramString);
/*     */   }
/*     */ 
/*     */   private static void d(String paramString1, String paramString2)
/*     */   {
/* 381 */     if (debug)
/* 382 */       System.err.println("LdapPoolManager: " + paramString1 + paramString2);
/*     */   }
/*     */ 
/*     */   private static final String getProperty(String paramString1, final String paramString2)
/*     */   {
/* 388 */     return (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 392 */           return System.getProperty(this.val$propName, paramString2); } catch (SecurityException localSecurityException) {
/*     */         }
/* 394 */         return paramString2;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static final int getInteger(String paramString, final int paramInt)
/*     */   {
/* 402 */     Integer localInteger = (Integer)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 406 */           return Integer.getInteger(this.val$propName, paramInt); } catch (SecurityException localSecurityException) {
/*     */         }
/* 408 */         return new Integer(paramInt);
/*     */       }
/*     */     });
/* 412 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   private static final long getLong(String paramString, final long paramLong)
/*     */   {
/* 417 */     Long localLong = (Long)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 421 */           return Long.getLong(this.val$propName, paramLong); } catch (SecurityException localSecurityException) {
/*     */         }
/* 423 */         return new Long(paramLong);
/*     */       }
/*     */     });
/* 427 */     return localLong.longValue();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 121 */     maxSize = getInteger("com.sun.jndi.ldap.connect.pool.maxsize", 0);
/*     */ 
/* 123 */     prefSize = getInteger("com.sun.jndi.ldap.connect.pool.prefsize", 0);
/*     */ 
/* 125 */     initSize = getInteger("com.sun.jndi.ldap.connect.pool.initsize", 1);
/*     */ 
/* 127 */     idleTimeout = getLong("com.sun.jndi.ldap.connect.pool.timeout", 0L);
/*     */ 
/* 130 */     String str1 = getProperty("com.sun.jndi.ldap.connect.pool.authentication", "none simple");
/* 131 */     StringTokenizer localStringTokenizer = new StringTokenizer(str1);
/* 132 */     int i = localStringTokenizer.countTokens();
/*     */ 
/* 135 */     for (int k = 0; k < i; k++) {
/* 136 */       String str2 = localStringTokenizer.nextToken().toLowerCase();
/* 137 */       if (str2.equals("anonymous")) {
/* 138 */         str2 = "none";
/*     */       }
/*     */ 
/* 141 */       int j = findPool(str2);
/* 142 */       if ((j >= 0) && (pools[j] == null)) {
/* 143 */         pools[j] = new Pool(initSize, prefSize, maxSize);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 148 */     str1 = getProperty("com.sun.jndi.ldap.connect.pool.protocol", "plain");
/* 149 */     localStringTokenizer = new StringTokenizer(str1);
/* 150 */     i = localStringTokenizer.countTokens();
/*     */ 
/* 152 */     for (int m = 0; m < i; m++) {
/* 153 */       String str3 = localStringTokenizer.nextToken();
/* 154 */       if ("plain".equalsIgnoreCase(str3))
/* 155 */         supportPlainProtocol = true;
/* 156 */       else if ("ssl".equalsIgnoreCase(str3)) {
/* 157 */         supportSslProtocol = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 163 */     if (idleTimeout > 0L)
/*     */     {
/* 165 */       new PoolCleaner(idleTimeout, pools).start();
/*     */     }
/*     */ 
/* 168 */     if (debug)
/* 169 */       showStats(System.err);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapPoolManager
 * JD-Core Version:    0.6.2
 */