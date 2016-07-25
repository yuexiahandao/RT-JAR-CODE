/*    */ package com.sun.corba.se.impl.util;
/*    */ 
/*    */ public final class PackagePrefixChecker
/*    */ {
/*    */   private static final String PACKAGE_PREFIX = "org.omg.stub.";
/*    */ 
/*    */   public static String packagePrefix()
/*    */   {
/* 39 */     return "org.omg.stub.";
/*    */   }
/*    */   public static String correctPackageName(String paramString) {
/* 42 */     if (paramString == null) return paramString;
/* 43 */     if (hasOffendingPrefix(paramString))
/*    */     {
/* 45 */       return "org.omg.stub." + paramString;
/*    */     }
/* 47 */     return paramString;
/*    */   }
/*    */ 
/*    */   public static boolean isOffendingPackage(String paramString) {
/* 51 */     return (paramString != null) && (hasOffendingPrefix(paramString));
/*    */   }
/*    */ 
/*    */   public static boolean hasOffendingPrefix(String paramString)
/*    */   {
/* 58 */     return (paramString.startsWith("java.")) || (paramString.equals("java")) || (paramString.startsWith("net.jini.")) || (paramString.equals("net.jini")) || (paramString.startsWith("jini.")) || (paramString.equals("jini")) || (paramString.startsWith("javax.")) || (paramString.equals("javax"));
/*    */   }
/*    */ 
/*    */   public static boolean hasBeenPrefixed(String paramString)
/*    */   {
/* 68 */     return paramString.startsWith(packagePrefix());
/*    */   }
/*    */ 
/*    */   public static String withoutPackagePrefix(String paramString) {
/* 72 */     if (hasBeenPrefixed(paramString)) return paramString.substring(packagePrefix().length());
/* 73 */     return paramString;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.PackagePrefixChecker
 * JD-Core Version:    0.6.2
 */