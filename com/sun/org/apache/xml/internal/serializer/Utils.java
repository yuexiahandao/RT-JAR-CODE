/*    */ package com.sun.org.apache.xml.internal.serializer;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ class Utils
/*    */ {
/*    */   static Class ClassForName(String classname)
/*    */     throws ClassNotFoundException
/*    */   {
/* 63 */     Object o = CacheHolder.cache.get(classname);
/*    */     Class c;
/* 64 */     if (o == null)
/*    */     {
/* 67 */       Class c = Class.forName(classname);
/*    */ 
/* 72 */       CacheHolder.cache.put(classname, c);
/*    */     }
/*    */     else
/*    */     {
/* 76 */       c = (Class)o;
/*    */     }
/* 78 */     return c;
/*    */   }
/*    */ 
/*    */   private static class CacheHolder
/*    */   {
/* 41 */     static final Hashtable cache = new Hashtable();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.Utils
 * JD-Core Version:    0.6.2
 */