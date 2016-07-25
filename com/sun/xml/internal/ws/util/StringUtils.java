/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ public class StringUtils
/*    */ {
/*    */   public static String decapitalize(String name)
/*    */   {
/* 48 */     if ((name == null) || (name.length() == 0)) {
/* 49 */       return name;
/*    */     }
/* 51 */     if ((name.length() > 1) && (Character.isUpperCase(name.charAt(1))) && (Character.isUpperCase(name.charAt(0))))
/*    */     {
/* 55 */       return name;
/*    */     }
/* 57 */     char[] chars = name.toCharArray();
/* 58 */     chars[0] = Character.toLowerCase(chars[0]);
/* 59 */     return new String(chars);
/*    */   }
/*    */ 
/*    */   public static String capitalize(String name)
/*    */   {
/* 72 */     if ((name == null) || (name.length() == 0)) {
/* 73 */       return name;
/*    */     }
/* 75 */     char[] chars = name.toCharArray();
/* 76 */     chars[0] = Character.toUpperCase(chars[0]);
/* 77 */     return new String(chars);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.StringUtils
 * JD-Core Version:    0.6.2
 */