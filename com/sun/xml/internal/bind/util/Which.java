/*    */ package com.sun.xml.internal.bind.util;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ public class Which
/*    */ {
/*    */   public static String which(Class clazz)
/*    */   {
/* 39 */     return which(clazz.getName(), clazz.getClassLoader());
/*    */   }
/*    */ 
/*    */   public static String which(String classname, ClassLoader loader)
/*    */   {
/* 51 */     String classnameAsResource = classname.replace('.', '/') + ".class";
/*    */ 
/* 53 */     if (loader == null) {
/* 54 */       loader = ClassLoader.getSystemClassLoader();
/*    */     }
/*    */ 
/* 57 */     URL it = loader.getResource(classnameAsResource);
/* 58 */     if (it != null) {
/* 59 */       return it.toString();
/*    */     }
/* 61 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.util.Which
 * JD-Core Version:    0.6.2
 */