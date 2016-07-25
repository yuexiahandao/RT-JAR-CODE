/*    */ package com.sun.xml.internal.ws.model;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URL;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ final class Injector
/*    */ {
/* 45 */   private static final Logger LOGGER = Logger.getLogger(Injector.class.getName());
/*    */   private static final Method defineClass;
/*    */   private static final Method resolveClass;
/*    */   private static final Method getPackage;
/*    */   private static final Method definePackage;
/*    */ 
/*    */   static synchronized Class inject(ClassLoader cl, String className, byte[] image)
/*    */   {
/*    */     try
/*    */     {
/* 81 */       return cl.loadClass(className);
/*    */     }
/*    */     catch (ClassNotFoundException e)
/*    */     {
/*    */       try {
/* 86 */         int packIndex = className.lastIndexOf('.');
/* 87 */         if (packIndex != -1) {
/* 88 */           String pkgname = className.substring(0, packIndex);
/*    */ 
/* 90 */           Package pkg = (Package)getPackage.invoke(cl, new Object[] { pkgname });
/* 91 */           if (pkg == null) {
/* 92 */             definePackage.invoke(cl, new Object[] { pkgname, null, null, null, null, null, null, null });
/*    */           }
/*    */         }
/*    */ 
/* 96 */         Class c = (Class)defineClass.invoke(cl, new Object[] { className.replace('/', '.'), image, Integer.valueOf(0), Integer.valueOf(image.length) });
/* 97 */         resolveClass.invoke(cl, new Object[] { c });
/* 98 */         return c;
/*    */       } catch (IllegalAccessException e) {
/* 100 */         LOGGER.log(Level.FINE, "Unable to inject " + className, e);
/* 101 */         throw new WebServiceException(e);
/*    */       } catch (InvocationTargetException e) {
/* 103 */         LOGGER.log(Level.FINE, "Unable to inject " + className, e);
/* 104 */         throw new WebServiceException(e);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 54 */       defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, [B.class, Integer.TYPE, Integer.TYPE });
/* 55 */       resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass", new Class[] { Class.class });
/* 56 */       getPackage = ClassLoader.class.getDeclaredMethod("getPackage", new Class[] { String.class });
/* 57 */       definePackage = ClassLoader.class.getDeclaredMethod("definePackage", new Class[] { String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class });
/*    */     }
/*    */     catch (NoSuchMethodException e)
/*    */     {
/* 62 */       throw new NoSuchMethodError(e.getMessage());
/*    */     }
/* 64 */     AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Void run()
/*    */       {
/* 68 */         Injector.defineClass.setAccessible(true);
/* 69 */         Injector.resolveClass.setAccessible(true);
/* 70 */         Injector.getPackage.setAccessible(true);
/* 71 */         Injector.definePackage.setAccessible(true);
/* 72 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.Injector
 * JD-Core Version:    0.6.2
 */