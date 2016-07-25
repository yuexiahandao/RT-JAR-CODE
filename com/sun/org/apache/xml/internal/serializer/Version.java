/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class Version
/*     */ {
/*     */   public static String getVersion()
/*     */   {
/*  47 */     return getProduct() + " " + getImplementationLanguage() + " " + getMajorVersionNum() + "." + getReleaseVersionNum() + "." + (getDevelopmentVersionNum() > 0 ? "D" + getDevelopmentVersionNum() : new StringBuilder().append("").append(getMaintenanceVersionNum()).toString());
/*     */   }
/*     */ 
/*     */   public static void _main(String[] argv)
/*     */   {
/*  60 */     System.out.println(getVersion());
/*     */   }
/*     */ 
/*     */   public static String getProduct()
/*     */   {
/*  68 */     return "Serializer";
/*     */   }
/*     */ 
/*     */   public static String getImplementationLanguage()
/*     */   {
/*  76 */     return "Java";
/*     */   }
/*     */ 
/*     */   public static int getMajorVersionNum()
/*     */   {
/*  93 */     return 2;
/*     */   }
/*     */ 
/*     */   public static int getReleaseVersionNum()
/*     */   {
/* 107 */     return 7;
/*     */   }
/*     */ 
/*     */   public static int getMaintenanceVersionNum()
/*     */   {
/* 121 */     return 0;
/*     */   }
/*     */ 
/*     */   public static int getDevelopmentVersionNum()
/*     */   {
/*     */     try
/*     */     {
/* 144 */       if (new String("").length() == 0) {
/* 145 */         return 0;
/*     */       }
/* 147 */       return Integer.parseInt(""); } catch (NumberFormatException nfe) {
/*     */     }
/* 149 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.Version
 * JD-Core Version:    0.6.2
 */