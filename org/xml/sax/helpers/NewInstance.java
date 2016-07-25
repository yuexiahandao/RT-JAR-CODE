/*    */ package org.xml.sax.helpers;
/*    */ 
/*    */ class NewInstance
/*    */ {
/*    */   private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal";
/*    */ 
/*    */   static Object newInstance(ClassLoader classLoader, String className)
/*    */     throws ClassNotFoundException, IllegalAccessException, InstantiationException
/*    */   {
/* 71 */     boolean internal = false;
/* 72 */     if ((System.getSecurityManager() != null) && 
/* 73 */       (className != null) && (className.startsWith("com.sun.org.apache.xerces.internal")))
/* 74 */       internal = true;
/*    */     Class driverClass;
/*    */     Class driverClass;
/* 79 */     if ((classLoader == null) || (internal))
/* 80 */       driverClass = Class.forName(className);
/*    */     else {
/* 82 */       driverClass = classLoader.loadClass(className);
/*    */     }
/* 84 */     return driverClass.newInstance();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.NewInstance
 * JD-Core Version:    0.6.2
 */