/*    */ package com.sun.management.jmx;
/*    */ 
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ @Deprecated
/*    */ public class JMProperties
/*    */ {
/*    */ 
/*    */   @Deprecated
/*    */   public static final String MLET_LIB_DIR = "jmx.mlet.library.dir";
/*    */ 
/*    */   @Deprecated
/*    */   public static final String JMX_SPEC_NAME = "jmx.specification.name";
/*    */ 
/*    */   @Deprecated
/*    */   public static final String JMX_SPEC_VERSION = "jmx.specification.version";
/*    */ 
/*    */   @Deprecated
/*    */   public static final String JMX_SPEC_VENDOR = "jmx.specification.vendor";
/*    */ 
/*    */   @Deprecated
/*    */   public static final String JMX_IMPL_NAME = "jmx.implementation.name";
/*    */ 
/*    */   @Deprecated
/*    */   public static final String JMX_IMPL_VENDOR = "jmx.implementation.vendor";
/*    */ 
/*    */   @Deprecated
/*    */   public static final String JMX_IMPL_VERSION = "jmx.implementation.version";
/*    */ 
/*    */   @Deprecated
/*    */   public static void load(String paramString)
/*    */     throws IOException
/*    */   {
/* 42 */     Properties localProperties = new Properties(System.getProperties());
/* 43 */     FileInputStream localFileInputStream = new FileInputStream(paramString);
/* 44 */     localProperties.load(localFileInputStream);
/* 45 */     localFileInputStream.close();
/* 46 */     System.setProperties(localProperties);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.jmx.JMProperties
 * JD-Core Version:    0.6.2
 */