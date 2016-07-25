/*     */ package sun.applet;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.URLClassLoader;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ import sun.awt.AWTSecurityManager;
/*     */ import sun.awt.AppContext;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public class AppletSecurity extends AWTSecurityManager
/*     */ {
/*  57 */   private static Field facc = null;
/*     */ 
/*  60 */   private static Field fcontext = null;
/*     */ 
/*  82 */   private HashSet restrictedPackages = new HashSet();
/*     */ 
/* 233 */   private boolean inThreadGroupCheck = false;
/*     */ 
/*     */   public AppletSecurity()
/*     */   {
/*  78 */     reset();
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  90 */     this.restrictedPackages.clear();
/*     */ 
/*  92 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*  96 */         Enumeration localEnumeration = System.getProperties().propertyNames();
/*     */ 
/*  98 */         while (localEnumeration.hasMoreElements())
/*     */         {
/* 100 */           String str1 = (String)localEnumeration.nextElement();
/*     */ 
/* 102 */           if ((str1 != null) && (str1.startsWith("package.restrict.access.")))
/*     */           {
/* 104 */             String str2 = System.getProperty(str1);
/*     */ 
/* 106 */             if ((str2 != null) && (str2.equalsIgnoreCase("true")))
/*     */             {
/* 108 */               String str3 = str1.substring(24);
/*     */ 
/* 111 */               AppletSecurity.this.restrictedPackages.add(str3);
/*     */             }
/*     */           }
/*     */         }
/* 115 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private AppletClassLoader currentAppletClassLoader()
/*     */   {
/* 126 */     ClassLoader localClassLoader1 = currentClassLoader();
/*     */ 
/* 128 */     if ((localClassLoader1 == null) || ((localClassLoader1 instanceof AppletClassLoader))) {
/* 129 */       return (AppletClassLoader)localClassLoader1;
/*     */     }
/*     */ 
/* 132 */     Class[] arrayOfClass = getClassContext();
/* 133 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 134 */       localClassLoader1 = arrayOfClass[i].getClassLoader();
/* 135 */       if ((localClassLoader1 instanceof AppletClassLoader)) {
/* 136 */         return (AppletClassLoader)localClassLoader1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 146 */     for (i = 0; i < arrayOfClass.length; i++) {
/* 147 */       final ClassLoader localClassLoader2 = arrayOfClass[i].getClassLoader();
/*     */ 
/* 149 */       if ((localClassLoader2 instanceof URLClassLoader)) {
/* 150 */         localClassLoader1 = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 153 */             AccessControlContext localAccessControlContext = null;
/* 154 */             ProtectionDomain[] arrayOfProtectionDomain = null;
/*     */             try
/*     */             {
/* 157 */               localAccessControlContext = (AccessControlContext)AppletSecurity.facc.get(localClassLoader2);
/* 158 */               if (localAccessControlContext == null) {
/* 159 */                 return null;
/*     */               }
/*     */ 
/* 162 */               arrayOfProtectionDomain = (ProtectionDomain[])AppletSecurity.fcontext.get(localAccessControlContext);
/* 163 */               if (arrayOfProtectionDomain == null)
/* 164 */                 return null;
/*     */             }
/*     */             catch (Exception localException) {
/* 167 */               throw new UnsupportedOperationException(localException);
/*     */             }
/*     */ 
/* 170 */             for (int i = 0; i < arrayOfProtectionDomain.length; i++) {
/* 171 */               ClassLoader localClassLoader = arrayOfProtectionDomain[i].getClassLoader();
/*     */ 
/* 173 */               if ((localClassLoader instanceof AppletClassLoader)) {
/* 174 */                 return localClassLoader;
/*     */               }
/*     */             }
/*     */ 
/* 178 */             return null;
/*     */           }
/*     */         });
/* 182 */         if (localClassLoader1 != null) {
/* 183 */           return (AppletClassLoader)localClassLoader1;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 189 */     localClassLoader1 = Thread.currentThread().getContextClassLoader();
/* 190 */     if ((localClassLoader1 instanceof AppletClassLoader)) {
/* 191 */       return (AppletClassLoader)localClassLoader1;
/*     */     }
/*     */ 
/* 194 */     return (AppletClassLoader)null;
/*     */   }
/*     */ 
/*     */   protected boolean inThreadGroup(ThreadGroup paramThreadGroup)
/*     */   {
/* 203 */     if (currentAppletClassLoader() == null) {
/* 204 */       return false;
/*     */     }
/* 206 */     return getThreadGroup().parentOf(paramThreadGroup);
/*     */   }
/*     */ 
/*     */   protected boolean inThreadGroup(Thread paramThread)
/*     */   {
/* 214 */     return inThreadGroup(paramThread.getThreadGroup());
/*     */   }
/*     */ 
/*     */   public void checkAccess(Thread paramThread)
/*     */   {
/* 228 */     if ((paramThread.getState() != Thread.State.TERMINATED) && (!inThreadGroup(paramThread)))
/* 229 */       checkPermission(SecurityConstants.MODIFY_THREAD_PERMISSION);
/*     */   }
/*     */ 
/*     */   public synchronized void checkAccess(ThreadGroup paramThreadGroup)
/*     */   {
/* 240 */     if (this.inThreadGroupCheck)
/*     */     {
/* 245 */       checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
/*     */     }
/*     */     else try {
/* 248 */         this.inThreadGroupCheck = true;
/* 249 */         if (!inThreadGroup(paramThreadGroup))
/* 250 */           checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
/*     */       }
/*     */       finally {
/* 253 */         this.inThreadGroupCheck = false;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void checkPackageAccess(String paramString)
/*     */   {
/* 281 */     super.checkPackageAccess(paramString);
/*     */ 
/* 284 */     for (Iterator localIterator = this.restrictedPackages.iterator(); localIterator.hasNext(); )
/*     */     {
/* 286 */       String str = (String)localIterator.next();
/*     */ 
/* 291 */       if ((paramString.equals(str)) || (paramString.startsWith(str + ".")))
/*     */       {
/* 293 */         checkPermission(new RuntimePermission("accessClassInPackage." + paramString));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkAwtEventQueueAccess()
/*     */   {
/* 310 */     AppContext localAppContext = AppContext.getAppContext();
/* 311 */     AppletClassLoader localAppletClassLoader = currentAppletClassLoader();
/*     */ 
/* 313 */     if ((AppContext.isMainContext(localAppContext)) && (localAppletClassLoader != null))
/*     */     {
/* 317 */       super.checkAwtEventQueueAccess();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ThreadGroup getThreadGroup()
/*     */   {
/* 329 */     AppletClassLoader localAppletClassLoader = currentAppletClassLoader();
/* 330 */     ThreadGroup localThreadGroup = localAppletClassLoader == null ? null : localAppletClassLoader.getThreadGroup();
/*     */ 
/* 332 */     if (localThreadGroup != null) {
/* 333 */       return localThreadGroup;
/*     */     }
/* 335 */     return super.getThreadGroup();
/*     */   }
/*     */ 
/*     */   public AppContext getAppContext()
/*     */   {
/* 352 */     AppletClassLoader localAppletClassLoader = currentAppletClassLoader();
/*     */ 
/* 354 */     if (localAppletClassLoader == null) {
/* 355 */       return null;
/*     */     }
/* 357 */     AppContext localAppContext = localAppletClassLoader.getAppContext();
/*     */ 
/* 361 */     if (localAppContext == null) {
/* 362 */       throw new SecurityException("Applet classloader has invalid AppContext");
/*     */     }
/*     */ 
/* 365 */     return localAppContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  64 */       facc = URLClassLoader.class.getDeclaredField("acc");
/*  65 */       facc.setAccessible(true);
/*  66 */       fcontext = AccessControlContext.class.getDeclaredField("context");
/*  67 */       fcontext.setAccessible(true);
/*     */     } catch (NoSuchFieldException localNoSuchFieldException) {
/*  69 */       throw new UnsupportedOperationException(localNoSuchFieldException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletSecurity
 * JD-Core Version:    0.6.2
 */