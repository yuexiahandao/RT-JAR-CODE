/*      */ package java.lang;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ 
/*      */ class SystemClassLoaderAction
/*      */   implements PrivilegedExceptionAction<ClassLoader>
/*      */ {
/*      */   private ClassLoader parent;
/*      */ 
/*      */   SystemClassLoaderAction(ClassLoader paramClassLoader)
/*      */   {
/* 2225 */     this.parent = paramClassLoader;
/*      */   }
/*      */ 
/*      */   public ClassLoader run() throws Exception {
/* 2229 */     String str = System.getProperty("java.system.class.loader");
/* 2230 */     if (str == null) {
/* 2231 */       return this.parent;
/*      */     }
/*      */ 
/* 2234 */     Constructor localConstructor = Class.forName(str, true, this.parent).getDeclaredConstructor(new Class[] { ClassLoader.class });
/*      */ 
/* 2236 */     ClassLoader localClassLoader = (ClassLoader)localConstructor.newInstance(new Object[] { this.parent });
/*      */ 
/* 2238 */     Thread.currentThread().setContextClassLoader(localClassLoader);
/* 2239 */     return localClassLoader;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.SystemClassLoaderAction
 * JD-Core Version:    0.6.2
 */