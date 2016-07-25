/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public final class RuntimeVersion
/*    */ {
/* 55 */   public static final Version VERSION = version == null ? Version.create(null) : version;
/*    */ 
/*    */   public String getVersion()
/*    */   {
/* 62 */     return VERSION.toString();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 42 */     Version version = null;
/* 43 */     InputStream in = RuntimeVersion.class.getResourceAsStream("version.properties");
/*    */     try {
/* 45 */       version = Version.create(in);
/*    */     } finally {
/* 47 */       if (in != null)
/*    */         try {
/* 49 */           in.close();
/*    */         }
/*    */         catch (IOException ioe)
/*    */         {
/*    */         }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.RuntimeVersion
 * JD-Core Version:    0.6.2
 */