/*     */ package com.sun.org.apache.xalan.internal;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Version
/*     */ {
/*     */   public static String getVersion()
/*     */   {
/*  50 */     return getProduct() + " " + getImplementationLanguage() + " " + getMajorVersionNum() + "." + getReleaseVersionNum() + "." + (getDevelopmentVersionNum() > 0 ? "D" + getDevelopmentVersionNum() : new StringBuilder().append("").append(getMaintenanceVersionNum()).toString());
/*     */   }
/*     */ 
/*     */   public static void _main(String[] argv)
/*     */   {
/*  63 */     System.out.println(getVersion());
/*     */   }
/*     */ 
/*     */   public static String getProduct()
/*     */   {
/*  71 */     return "Xalan";
/*     */   }
/*     */ 
/*     */   public static String getImplementationLanguage()
/*     */   {
/*  79 */     return "Java";
/*     */   }
/*     */ 
/*     */   public static int getMajorVersionNum()
/*     */   {
/*  96 */     return 2;
/*     */   }
/*     */ 
/*     */   public static int getReleaseVersionNum()
/*     */   {
/* 110 */     return 7;
/*     */   }
/*     */ 
/*     */   public static int getMaintenanceVersionNum()
/*     */   {
/* 124 */     return 0;
/*     */   }
/*     */ 
/*     */   public static int getDevelopmentVersionNum()
/*     */   {
/*     */     try
/*     */     {
/* 147 */       if (new String("").length() == 0) {
/* 148 */         return 0;
/*     */       }
/* 150 */       return Integer.parseInt(""); } catch (NumberFormatException nfe) {
/*     */     }
/* 152 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.Version
 * JD-Core Version:    0.6.2
 */