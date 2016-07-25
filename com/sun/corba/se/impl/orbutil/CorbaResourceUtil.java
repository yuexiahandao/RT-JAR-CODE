/*    */ package com.sun.corba.se.impl.orbutil;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class CorbaResourceUtil
/*    */ {
/* 81 */   private static boolean resourcesInitialized = false;
/*    */   private static ResourceBundle resources;
/*    */ 
/*    */   public static String getString(String paramString)
/*    */   {
/* 33 */     if (!resourcesInitialized) {
/* 34 */       initResources();
/*    */     }
/*    */     try
/*    */     {
/* 38 */       return resources.getString(paramString);
/*    */     } catch (MissingResourceException localMissingResourceException) {
/*    */     }
/* 41 */     return null;
/*    */   }
/*    */ 
/*    */   public static String getText(String paramString) {
/* 45 */     String str = getString(paramString);
/* 46 */     if (str == null) {
/* 47 */       str = "no text found: \"" + paramString + "\"";
/*    */     }
/* 49 */     return str;
/*    */   }
/*    */ 
/*    */   public static String getText(String paramString, int paramInt) {
/* 53 */     return getText(paramString, Integer.toString(paramInt), null, null);
/*    */   }
/*    */ 
/*    */   public static String getText(String paramString1, String paramString2) {
/* 57 */     return getText(paramString1, paramString2, null, null);
/*    */   }
/*    */ 
/*    */   public static String getText(String paramString1, String paramString2, String paramString3) {
/* 61 */     return getText(paramString1, paramString2, paramString3, null);
/*    */   }
/*    */ 
/*    */   public static String getText(String paramString1, String paramString2, String paramString3, String paramString4)
/*    */   {
/* 67 */     String str = getString(paramString1);
/* 68 */     if (str == null) {
/* 69 */       str = "no text found: key = \"" + paramString1 + "\", " + "arguments = \"{0}\", \"{1}\", \"{2}\"";
/*    */     }
/*    */ 
/* 73 */     String[] arrayOfString = new String[3];
/* 74 */     arrayOfString[0] = (paramString2 != null ? paramString2.toString() : "null");
/* 75 */     arrayOfString[1] = (paramString3 != null ? paramString3.toString() : "null");
/* 76 */     arrayOfString[2] = (paramString4 != null ? paramString4.toString() : "null");
/*    */ 
/* 78 */     return MessageFormat.format(str, (Object[])arrayOfString);
/*    */   }
/*    */ 
/*    */   private static void initResources()
/*    */   {
/*    */     try
/*    */     {
/* 86 */       resources = ResourceBundle.getBundle("com.sun.corba.se.impl.orbutil.resources.sunorb");
/*    */ 
/* 88 */       resourcesInitialized = true;
/*    */     } catch (MissingResourceException localMissingResourceException) {
/* 90 */       throw new Error("fatal: missing resource bundle: " + localMissingResourceException.getClassName());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.CorbaResourceUtil
 * JD-Core Version:    0.6.2
 */