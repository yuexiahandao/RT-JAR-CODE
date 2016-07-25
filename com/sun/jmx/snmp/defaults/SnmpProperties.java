/*    */ package com.sun.jmx.snmp.defaults;
/*    */ 
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class SnmpProperties
/*    */ {
/*    */   public static final String MLET_LIB_DIR = "jmx.mlet.library.dir";
/*    */   public static final String ACL_FILE = "jdmk.acl.file";
/*    */   public static final String SECURITY_FILE = "jdmk.security.file";
/*    */   public static final String UACL_FILE = "jdmk.uacl.file";
/*    */   public static final String MIB_CORE_FILE = "mibcore.file";
/*    */   public static final String JMX_SPEC_NAME = "jmx.specification.name";
/*    */   public static final String JMX_SPEC_VERSION = "jmx.specification.version";
/*    */   public static final String JMX_SPEC_VENDOR = "jmx.specification.vendor";
/*    */   public static final String JMX_IMPL_NAME = "jmx.implementation.name";
/*    */   public static final String JMX_IMPL_VENDOR = "jmx.implementation.vendor";
/*    */   public static final String JMX_IMPL_VERSION = "jmx.implementation.version";
/*    */   public static final String SSL_CIPHER_SUITE = "jdmk.ssl.cipher.suite.";
/*    */ 
/*    */   public static void load(String paramString)
/*    */     throws IOException
/*    */   {
/* 59 */     Properties localProperties = new Properties();
/* 60 */     FileInputStream localFileInputStream = new FileInputStream(paramString);
/* 61 */     localProperties.load(localFileInputStream);
/* 62 */     localFileInputStream.close();
/* 63 */     for (Enumeration localEnumeration = localProperties.keys(); localEnumeration.hasMoreElements(); ) {
/* 64 */       String str = (String)localEnumeration.nextElement();
/* 65 */       System.setProperty(str, localProperties.getProperty(str));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.defaults.SnmpProperties
 * JD-Core Version:    0.6.2
 */