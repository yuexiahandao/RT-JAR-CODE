/*     */ package javax.sql.rowset;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class RowSetProvider
/*     */ {
/*     */   private static final String ROWSET_DEBUG_PROPERTY = "javax.sql.rowset.RowSetProvider.debug";
/*     */   private static final String ROWSET_FACTORY_IMPL = "com.sun.rowset.RowSetFactoryImpl";
/*     */   private static final String ROWSET_FACTORY_NAME = "javax.sql.rowset.RowSetFactory";
/*  72 */   private static boolean debug = (str != null) && (!"false".equals(str));
/*     */ 
/*     */   public static RowSetFactory newFactory()
/*     */     throws SQLException
/*     */   {
/* 124 */     RowSetFactory localRowSetFactory = null;
/* 125 */     String str = null;
/*     */     try {
/* 127 */       trace("Checking for Rowset System Property...");
/* 128 */       str = getSystemProperty("javax.sql.rowset.RowSetFactory");
/* 129 */       if (str != null) {
/* 130 */         trace("Found system property, value=" + str);
/* 131 */         localRowSetFactory = (RowSetFactory)ReflectUtil.newInstance(getFactoryClass(str, null, true));
/*     */       }
/*     */     } catch (Exception localException) {
/* 134 */       throw new SQLException("RowSetFactory: " + str + " could not be instantiated: ", localException);
/*     */     }
/*     */ 
/* 139 */     if (localRowSetFactory == null)
/*     */     {
/* 143 */       localRowSetFactory = loadViaServiceLoader();
/* 144 */       localRowSetFactory = localRowSetFactory == null ? newFactory("com.sun.rowset.RowSetFactoryImpl", null) : localRowSetFactory;
/*     */     }
/*     */ 
/* 147 */     return localRowSetFactory;
/*     */   }
/*     */ 
/*     */   public static RowSetFactory newFactory(String paramString, ClassLoader paramClassLoader)
/*     */     throws SQLException
/*     */   {
/* 179 */     trace("***In newInstance()");
/*     */ 
/* 181 */     if (paramString == null)
/* 182 */       throw new SQLException("Error: factoryClassName cannot be null");
/*     */     try
/*     */     {
/* 185 */       ReflectUtil.checkPackageAccess(paramString);
/*     */     } catch (AccessControlException localAccessControlException) {
/* 187 */       throw new SQLException("Access Exception", localAccessControlException);
/*     */     }
/*     */     try
/*     */     {
/* 191 */       Class localClass = getFactoryClass(paramString, paramClassLoader, false);
/* 192 */       RowSetFactory localRowSetFactory = (RowSetFactory)localClass.newInstance();
/* 193 */       if (debug) {
/* 194 */         trace("Created new instance of " + localClass + " using ClassLoader: " + paramClassLoader);
/*     */       }
/*     */ 
/* 197 */       return localRowSetFactory;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 199 */       throw new SQLException("Provider " + paramString + " not found", localClassNotFoundException);
/*     */     }
/*     */     catch (Exception localException) {
/* 202 */       throw new SQLException("Provider " + paramString + " could not be instantiated: " + localException, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static ClassLoader getContextClassLoader()
/*     */     throws SecurityException
/*     */   {
/* 214 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 217 */         ClassLoader localClassLoader = null;
/*     */ 
/* 219 */         localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 221 */         if (localClassLoader == null) {
/* 222 */           localClassLoader = ClassLoader.getSystemClassLoader();
/*     */         }
/*     */ 
/* 225 */         return localClassLoader;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static Class getFactoryClass(String paramString, ClassLoader paramClassLoader, boolean paramBoolean)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 242 */       if (paramClassLoader == null) {
/* 243 */         paramClassLoader = getContextClassLoader();
/* 244 */         if (paramClassLoader == null) {
/* 245 */           throw new ClassNotFoundException();
/*     */         }
/* 247 */         return paramClassLoader.loadClass(paramString);
/*     */       }
/*     */ 
/* 250 */       return paramClassLoader.loadClass(paramString);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 253 */       if (paramBoolean)
/*     */       {
/* 255 */         return Class.forName(paramString, true, RowSetFactory.class.getClassLoader());
/*     */       }
/* 257 */       throw localClassNotFoundException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static RowSetFactory loadViaServiceLoader()
/*     */     throws SQLException
/*     */   {
/* 267 */     Object localObject = null;
/*     */     try {
/* 269 */       trace("***in loadViaServiceLoader():");
/* 270 */       Iterator localIterator = ServiceLoader.load(RowSetFactory.class).iterator(); if (localIterator.hasNext()) { RowSetFactory localRowSetFactory = (RowSetFactory)localIterator.next();
/* 271 */         trace(" Loading done by the java.util.ServiceLoader :" + localRowSetFactory.getClass().getName());
/* 272 */         localObject = localRowSetFactory; }
/*     */     }
/*     */     catch (ServiceConfigurationError localServiceConfigurationError)
/*     */     {
/* 276 */       throw new SQLException("RowSetFactory: Error locating RowSetFactory using Service Loader API: " + localServiceConfigurationError, localServiceConfigurationError);
/*     */     }
/*     */ 
/* 280 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static String getSystemProperty(String paramString)
/*     */   {
/* 292 */     String str = null;
/*     */     try {
/* 294 */       str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run() {
/* 297 */           return System.getProperty(this.val$propName);
/*     */         } } );
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/* 301 */       if (debug) {
/* 302 */         trace("error getting " + paramString + ":  " + localSecurityException);
/* 303 */         localSecurityException.printStackTrace();
/*     */       }
/*     */     }
/* 306 */     return str;
/*     */   }
/*     */ 
/*     */   private static void trace(String paramString)
/*     */   {
/* 315 */     if (debug)
/* 316 */       System.err.println("###RowSets: " + paramString);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  70 */     String str = getSystemProperty("javax.sql.rowset.RowSetProvider.debug");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.RowSetProvider
 * JD-Core Version:    0.6.2
 */