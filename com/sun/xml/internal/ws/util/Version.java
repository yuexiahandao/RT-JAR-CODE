/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public final class Version
/*    */ {
/*    */   public final String BUILD_ID;
/*    */   public final String BUILD_VERSION;
/*    */   public final String MAJOR_VERSION;
/*    */ 
/*    */   private Version(String buildId, String buildVersion, String majorVersion)
/*    */   {
/* 52 */     this.BUILD_ID = fixNull(buildId);
/* 53 */     this.BUILD_VERSION = fixNull(buildVersion);
/* 54 */     this.MAJOR_VERSION = fixNull(majorVersion);
/*    */   }
/*    */ 
/*    */   public static Version create(InputStream is) {
/* 58 */     Properties props = new Properties();
/*    */     try {
/* 60 */       props.load(is);
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/*    */     }
/*    */     catch (Exception e) {
/*    */     }
/* 67 */     return new Version(props.getProperty("build-id"), props.getProperty("build-version"), props.getProperty("major-version"));
/*    */   }
/*    */ 
/*    */   private String fixNull(String v)
/*    */   {
/* 74 */     if (v == null) return "unknown";
/* 75 */     return v;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 79 */     return this.BUILD_VERSION;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.Version
 * JD-Core Version:    0.6.2
 */