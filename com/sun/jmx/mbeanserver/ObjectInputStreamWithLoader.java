/*    */ package com.sun.jmx.mbeanserver;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ import sun.reflect.misc.ReflectUtil;
/*    */ 
/*    */ class ObjectInputStreamWithLoader extends ObjectInputStream
/*    */ {
/*    */   private ClassLoader loader;
/*    */ 
/*    */   public ObjectInputStreamWithLoader(InputStream paramInputStream, ClassLoader paramClassLoader)
/*    */     throws IOException
/*    */   {
/* 53 */     super(paramInputStream);
/* 54 */     this.loader = paramClassLoader;
/*    */   }
/*    */ 
/*    */   protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass)
/*    */     throws IOException, ClassNotFoundException
/*    */   {
/* 60 */     if (this.loader == null) {
/* 61 */       return super.resolveClass(paramObjectStreamClass);
/*    */     }
/* 63 */     String str = paramObjectStreamClass.getName();
/* 64 */     ReflectUtil.checkPackageAccess(str);
/*    */ 
/* 66 */     return Class.forName(str, false, this.loader);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.ObjectInputStreamWithLoader
 * JD-Core Version:    0.6.2
 */